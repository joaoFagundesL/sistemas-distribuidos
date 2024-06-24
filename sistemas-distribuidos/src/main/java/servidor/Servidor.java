package servidor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
import service.CandidatoServico;
import service.CompetenciaServico;
import service.RecruiterServico;
import service.VagaServico;

public class Servidor extends JFrame {

  private static final long serialVersionUID = 1L;
  private JPanel contentPane;
  private DefaultListModel<String> listModel;
  private JList<String> list;

  CandidatoServico candidatoServico;
  RecruiterServico recruiterServico;
  CompetenciaServico competenciaServico;
  VagaServico vagaServico;

  /* com esse set tenho todas informacoes do cliente que eu preciso,
     * caso precise adicionar algo mais é so mexer em ClientInfo (nome, por exemplo) */

  private Set<ClientInfo> connectedClients;
  private JTextField portaField;
  JTextArea textArea;

  public Servidor() {
    this.candidatoServico = new CandidatoServico();
    this.recruiterServico = new RecruiterServico();
    this.competenciaServico = new CompetenciaServico();
    this.vagaServico = new VagaServico();
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
    portaField.setText("21234");
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
          content.append("Servidor iniciado na porta " + port + "\n");

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
    ClientInfo client = null;
    try (
    BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)
  ) {
      String clientMessage;

      client = new ClientInfo(clientSocket.getInetAddress().getHostAddress(), clientSocket.getPort());

      connectedClients.add(client);
      updateConnectedUsersList();

      final ClientInfo finalClient = client; 

      while ((clientMessage = reader.readLine()) != null) {
        System.out.println("Received from client: " + clientMessage);

        JSONObject jsonMessage = new JSONObject(clientMessage);
        String formattedMessage = jsonMessage.toString(4);

        SwingUtilities.invokeLater(() -> {
          textArea.append(">> " + finalClient.getIpAddress() + " " + finalClient.getPort() + ": \n");
          textArea.append(formattedMessage + "\n\n");
          DefaultCaret caret = (DefaultCaret) textArea.getCaret();
          caret.setUpdatePolicy(DefaultCaret.OUT_BOTTOM);
        });

        connectedClients.add(finalClient);
        updateConnectedUsersList();
        handleOperation(clientMessage, writer);
      }
      removerClient(client);
    } catch (IOException e) {
      removerClient(client);
    }
  }  

  private void removerClient(ClientInfo client) {
    connectedClients.remove(client);
    updateConnectedUsersList();
    textArea.append(">> Cliente desconectado: " + client.getIpAddress() + " " + client.getPort() + "\n");
    DefaultCaret caret = (DefaultCaret)textArea.getCaret();
    caret.setUpdatePolicy(DefaultCaret.OUT_BOTTOM);
  }

  private void handleOperation(String clientMessage, PrintWriter writer) {
    JSONObject jsonMessage = new JSONObject(clientMessage);
    String operation = jsonMessage.getString("operation");
    JSONObject jsonResponse = new JSONObject();

    switch (operation) {
      case "LOGIN_CANDIDATE":
      candidatoServico.loginCandidato(jsonMessage, jsonResponse);
      break;

      case "LOGOUT_CANDIDATE":
      candidatoServico.logoutCandidato(jsonMessage, jsonResponse);
      break;

      case "SIGNUP_CANDIDATE":
      candidatoServico.signupCandidato(jsonMessage, jsonResponse);
      break;

      case "LOOKUP_ACCOUNT_CANDIDATE":
      candidatoServico.lookup_candidate(jsonMessage, jsonResponse);
      break;

      case "UPDATE_ACCOUNT_CANDIDATE":
      candidatoServico.updateCandidato(jsonMessage, jsonResponse);        
      break;

      case "DELETE_ACCOUNT_CANDIDATE":
      candidatoServico.deleteAccount(jsonMessage, jsonResponse);
      break;

      case "DELETE_ACCOUNT_RECRUITER":
      recruiterServico.deleteAccount(jsonMessage, jsonResponse);
      break;

      case "UPDATE_ACCOUNT_RECRUITER":
      recruiterServico.updateRecruiter(jsonMessage, jsonResponse);
      break;

      case "SIGNUP_RECRUITER":
      recruiterServico.signupRecruiter(jsonMessage, jsonResponse);
      break;

      case "LOGIN_RECRUITER":
      recruiterServico.loginRecruiter(jsonMessage, jsonResponse);
      break;

      case "LOGOUT_RECRUITER":
      recruiterServico.logoutRecruiter(jsonMessage, jsonResponse);
      break;

      case "LOOKUP_ACCOUNT_RECRUITER":
      recruiterServico.lookup_recruiter(jsonMessage, jsonResponse);
      break;

      case "INCLUDE_SKILL":
      competenciaServico.includeSkill(jsonMessage, jsonResponse);
      break;

      case "LOOKUP_SKILLSET":
      competenciaServico.lookupSkillset(jsonMessage, jsonResponse);
      break;

      case "LOOKUP_SKILL":
      competenciaServico.lookupSkill(jsonMessage, jsonResponse);
      break;

      case "DELETE_SKILL":
      competenciaServico.deleteSkill(jsonMessage, jsonResponse);
      break;

      case "UPDATE_SKILL":
      competenciaServico.updateSkill(jsonMessage, jsonResponse);
      break;

      case "INCLUDE_JOB":
      vagaServico.includeJob(jsonMessage, jsonResponse);
      break;

      case "LOOKUP_JOB":
      vagaServico.lookupJob(jsonMessage, jsonResponse);
      break;

      case "LOOKUP_JOBSET":
      vagaServico.lookupJobset(jsonMessage, jsonResponse);
      break;

      case "DELETE_JOB":
      vagaServico.deleteJob(jsonMessage, jsonResponse);
      break;

      case "UPDATE_JOB":
      vagaServico.updateJob(jsonMessage, jsonResponse);
      break;
      
      case "SEARCH_JOB":
      candidatoServico.searchJob(jsonMessage, jsonResponse);
      break;
      
      case "SET_JOB_AVAILABLE":
    	  vagaServico.setJobAvailable(jsonMessage, jsonResponse);
      break;
      
      case "SET_JOB_SEARCHABLE":
    	  vagaServico.setJobSearchable(jsonMessage, jsonResponse);
      break;
      
      default:
      buildInvalidOperation(jsonResponse, operation);
    }

    String jsonString = jsonResponse.toString();
    String messageToSend = jsonString;
    writer.println(messageToSend.toString());
    System.out.println("Message to client: " + messageToSend);
    writer.flush();
  }

  private JSONObject buildInvalidOperation(JSONObject res, String operation) {
    res.put("status", "INVALID_OPERATION");
    res.put("operation", "operation");
    JSONObject data = new JSONObject();
    res.put("data", data);
    return res;
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
