package servidor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
import controller.UsuarioController;
import dao.UsuarioDAO;
import modelo.Usuario;
import utitlity.EmailValidator;

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
    ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
    ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream())
  ) {
      while (true) {
        String clientMessage = (String) inputStream.readObject();
        System.out.println("Received from client: " + clientMessage);

        JSONObject jsonMessage = new JSONObject(clientMessage);
        String formattedMessage = jsonMessage.toString(4);

        ClientInfo client = new ClientInfo(clientSocket.getInetAddress().getHostAddress(),
          clientSocket.getPort());

        SwingUtilities.invokeLater(() -> {
          textArea.append(">> " + client.getIpAddress() + " " + client.getPort() + ": \n");
          textArea.append(formattedMessage + "\n\n"); 
          DefaultCaret caret = (DefaultCaret)textArea.getCaret();
          caret.setUpdatePolicy(DefaultCaret.OUT_BOTTOM);
        });

        connectedClients.add(client);
        updateConnectedUsersList();

        handleOperation(clientMessage, outputStream);
      }
    } catch (IOException | ClassNotFoundException e) {
      System.out.println("FIM");
    }
  } 

  private void handleOperation(String clientMessage, ObjectOutputStream outputStream) {
    JSONObject jsonMessage = new JSONObject(clientMessage);
    String operation = jsonMessage.getString("operation");
    JSONObject jsonResponse = new JSONObject();

    if (operation.equals("LOGIN_CANDIDATE")) {
      CandidatoController fController = new CandidatoController();
      JSONObject data = jsonMessage.getJSONObject("data");

      String email = data.getString("email");
      String senha = data.getString("password");

      if (!fController.isUserValid(email)) {
        buildJsonLogin(jsonResponse, "USER_NOT_FOUND");
      }

      else if (!fController.isPasswordValid(email, senha)) {
        buildJsonLogin(jsonResponse, "INVALID_PASSWORD");
      }

      else {				
        buildJsonLogin(jsonResponse, "SUCCESS");
        // new MainViewCandidato(this, fController.getCandidadoLogin(userName));
      } 
    }

    else if (operation.equals("LOGOUT_CANDIDATE")) {
      buildLogoutJson(jsonResponse);
    }

    else if (operation.equals("SIGNUP_CANDIDATE")) {

      UsuarioDAO dao = new UsuarioDAO();

      JSONObject data = jsonMessage.getJSONObject("data");

      String email = data.getString("email");
      String senha = data.getString("password");
      String nome = data.getString("name");

      EmailValidator emailValidator = new EmailValidator();
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

    } else {
      buildInvalidOperation(jsonResponse, operation);
    }

    try {
      outputStream.writeObject(jsonResponse.toString());
      outputStream.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private JSONObject buildJsonLogin(JSONObject res, String status) {
    res.put("operation", "LOGIN_CANDIDATE");
    res.put("status", status);
    JSONObject data = new JSONObject();
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





