package servidor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;

import org.json.JSONObject;

import cliente.ClientInfo;
import controller.CandidatoController;
import controller.EmpresaController;
import controller.UsuarioController;
import dao.UsuarioDAO;
import modelo.Usuario;
import modelo.Empresa;
import utitlity.EmailValidator;
import utitlity.JwtUtility;

public class Servidor extends JFrame {

  private static final long serialVersionUID = 1L;
  private JPanel contentPane;
  private DefaultListModel<String> listModel;
  private JList<String> list;

  /* com esse set tenho todas informacoes do cliente que eu preciso,
     * caso precise adicionar algo mais é so mexer em ClientInfo (nome, por exemplo) */

  private Set<ClientInfo> connectedClients;
  private JTextField portaField;
  JTextArea textArea;

  public Servidor() {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100, 100, 507, 369);
    contentPane = new JPanel();
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

    setContentPane(contentPane);
    contentPane.setLayout(null);

    listModel = new DefaultListModel<>();
    list = new JList<>(listModel);
    list.setBounds(310, 34, 155, 272);
    contentPane.add(list);

    JLabel lblNewLabel = new JLabel("Usuários");
    lblNewLabel.setBounds(358, 11, 58, 14);
    contentPane.add(lblNewLabel);

    JLabel portaLabel = new JLabel("Porta");
    portaLabel.setBounds(20, 53, 46, 14);
    contentPane.add(portaLabel);

    portaField = new JTextField();
    portaField.setBounds(56, 50, 86, 20);
    contentPane.add(portaField);
    portaField.setColumns(10);

    JButton iniciarBtn = new JButton("Iniciar");
    iniciarBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String port = portaField.getText();
        if (!port.isEmpty()) {
          startServer(Integer.parseInt(port));
          iniciarBtn.setEnabled(false);

          StringBuilder content = new StringBuilder();
          content.append("Servidor iniciado na porta 12345\n");

          textArea.setText(content.toString());
        }
      }
    });
    iniciarBtn.setBounds(149, 49, 78, 23);
    contentPane.add(iniciarBtn);

    textArea = new JTextArea();
    textArea.setEditable(false); 
    textArea.setBounds(20, 108, 280, 198);

    JScrollPane scrollPane = new JScrollPane(textArea);
    scrollPane.setBounds(20, 108, 280, 198);
    contentPane.add(scrollPane); 

    connectedClients = new HashSet<>();
  }

  private void startServer(int port) {
    new Thread(() -> {
      try (ServerSocket serverSocket = new ServerSocket(port)) {
        while (true) {
          Socket clientSocket = serverSocket.accept();
          new Thread(() -> handleClient(clientSocket)).start();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }).start();
  }

  private void handleClient(Socket clientSocket) {
    try (
      BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
      PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)  ) {
      while (true) {

        ClientInfo client = new ClientInfo(clientSocket.getInetAddress().getHostAddress(),
          clientSocket.getPort());

        connectedClients.add(client);
        updateConnectedUsersList();

        String clientMessage = reader.readLine();
        System.out.println("Received from client: " + clientMessage);

        JSONObject jsonMessage = new JSONObject(clientMessage);
        String formattedMessage = jsonMessage.toString(4);

        SwingUtilities.invokeLater(() -> {
          textArea.append(">> " + client.getIpAddress() + " " + client.getPort() + ": \n");
          textArea.append(formattedMessage + "\n\n"); 
          DefaultCaret caret = (DefaultCaret)textArea.getCaret();
          caret.setUpdatePolicy(DefaultCaret.OUT_BOTTOM);
        });

        connectedClients.add(client);
        updateConnectedUsersList();

        handleOperation(clientMessage, writer);
      }
    } catch (IOException  e) {
      System.out.println("FIM");
    }
  } 

  private void handleOperation(String clientMessage, PrintWriter writer) {
    JSONObject jsonMessage = new JSONObject(clientMessage);
    String operation = jsonMessage.getString("operation");
    JSONObject jsonResponse = new JSONObject();

    if (operation.equals("LOGIN_CANDIDATE")) {
      loginCandidato(jsonMessage, jsonResponse); 
    } else if (operation.equals("LOGOUT_CANDIDATE")) {
      buildLogoutJson(jsonResponse);
    } else if (operation.equals("SIGNUP_CANDIDATE")) {
      signupCandidato(jsonMessage, jsonResponse);
    } else if (operation.equals("SIGNUP_RECRUITER")) {
      signupRecruiter(jsonMessage, jsonResponse);

    } else {
      buildInvalidOperation(jsonResponse, operation);
    }

      String jsonString = jsonResponse.toString();
      String messageToSend = jsonString + (char) 0x1C;
      writer.println(messageToSend.toString());
      writer.flush();
  }

  private void loginCandidato(JSONObject jsonMessage, JSONObject jsonResponse) {
    CandidatoController fController = new CandidatoController();
    JSONObject data = jsonMessage.getJSONObject("data");

    String email = data.getString("email");
    String senha = data.getString("password");

    if (!fController.isUserValid(email)) {
      buildJsonLogin(jsonResponse, "USER_NOT_FOUND", "");
    }

    else if (!fController.isPasswordValid(email, senha)) {
      buildJsonLogin(jsonResponse, "INVALID_PASSWORD", "");
    } 

    else {				
      Integer id = fController.consultarId(email);
      String idString = String.valueOf(id);
      String token = JwtUtility.generateToken(idString, "user");
      buildJsonLogin(jsonResponse, "SUCCESS", token);
    
      UsuarioController uController = new UsuarioController();
      uController.inserirToken(id, token);
    } 
  }

  private void signupCandidato(JSONObject jsonMessage, JSONObject jsonResponse) {
    EmailValidator emailValidator = new EmailValidator();
    UsuarioDAO dao = new UsuarioDAO();
    JSONObject data = jsonMessage.getJSONObject("data");

    String email = data.getString("email");
    String senha = data.getString("password");
    String nome = data.getString("name");

    if (!emailValidator.isValidEmail(email)) {
      buildJsonSignupCandidate(jsonResponse, "INVALID_EMAIL");
    } else if (dao.consultarPeloEmail(email) == null) {
      UsuarioController ucontroller = new UsuarioController();
      Usuario u = ucontroller.insert(nome, email, senha);

      CandidatoController ccontroller = new CandidatoController();
      ccontroller.insert(u);

      buildJsonSignupCandidate(jsonResponse, "SUCCESS");
    } else {
      buildJsonSignupCandidate(jsonResponse, "USER_EXISTS");
    }

  }

  private void signupRecruiter(JSONObject jsonMessage, JSONObject jsonResponse) {
    EmailValidator emailValidator = new EmailValidator();
    UsuarioDAO dao = new UsuarioDAO();
    JSONObject data = jsonMessage.getJSONObject("data");

    String email = data.getString("email");
    String senha = data.getString("password");
    String nome = data.getString("name");
    String branch = data.getString("branch");
    String descricao = data.getString("description");

    if (!emailValidator.isValidEmail(email)) {
      buildJsonSignupRecruiter(jsonResponse, "INVALID_EMAIL");

    } else if (dao.consultarPeloEmail(email) == null) {
      UsuarioController ucontroller = new UsuarioController();
      Usuario u = ucontroller.insert(nome, email, senha);
      EmpresaController econtroller = new EmpresaController();
      Empresa e = econtroller.insert(u, descricao, branch);
      buildJsonSignupRecruiter(jsonResponse, "SUCCESS");

    } else {
      buildJsonSignupRecruiter(jsonResponse, "USER_EXISTS");
    }
  }

  private JSONObject buildJsonLogin(JSONObject res, String status, String token) {
    res.put("operation", "LOGIN_CANDIDATE");
    res.put("status", status);
    res.put("token", token);
    JSONObject data = new JSONObject();
    res.put("data", data);
    return res;
  }

  private JSONObject buildJsonSignupRecruiter(JSONObject res, String status) {
    res.put("operation", "SIGNUP_RECRUITER");
    res.put("status", status);
    JSONObject data = new JSONObject();
    data.put("", "");
    res.put("data", data);
    return res;
  }

  private JSONObject buildInvalidOperation(JSONObject res, String operation) {
    res.put("status", "INVALID_OPERATION");
    res.put("operation", "operation");
    JSONObject data = new JSONObject();
    data.put("", "");
    res.put("data", data);
    return res;
  }

  private JSONObject buildJsonSignupCandidate(JSONObject res, String status) {
    res.put("operation", "SIGNUP_CANDIDATE");
    res.put("status", status);
    JSONObject data = new JSONObject();
    data.put("", "");
    res.put("data", data);
    return res;
  }

  private JSONObject buildLogoutJson(JSONObject json) {
    json.put("operation", "LOGOUT_CANDIDATE");
    json.put("status", "SUCCESS");
    JSONObject data = new JSONObject();
    json.put("data", data);
    return json;
  }

  private void updateConnectedUsersList() {
    SwingUtilities.invokeLater(() -> {
      listModel.clear();

      for (ClientInfo client : connectedClients) {
        String info = "IP: " + client.getIpAddress() + ", Porta: " + client.getPort();
        listModel.addElement(info);
      }
    });
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      try {
        Servidor frame = new Servidor();
        frame.setVisible(true);
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
  }
}





