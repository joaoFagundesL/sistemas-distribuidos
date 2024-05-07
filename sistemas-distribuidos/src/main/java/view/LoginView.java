package view;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import org.json.JSONObject;
import cliente.Client;

public class LoginView {
  JPanel panelCont = new JPanel();
  JPanel panelFirst = new JPanel();
  JButton btnVoltar = new JButton("Voltar");
  CardLayout cl = new CardLayout();
  private JTextField textField;
  private static LoginView instance = null;
  private JPasswordField passwordField;

  @SuppressWarnings("rawtypes")
  JComboBox comboBox = new JComboBox();

  JFrame frame = new JFrame("Sistema");

  public LoginView() {
    initComponents(frame);
    frame.setBounds(100, 100, 438, 345);
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.setVisible(true);
  }
  
  public static LoginView getInstance() {
	    if (instance == null) {
	      instance = new LoginView();
	    }
	    return instance;
	  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  public void initComponents(JFrame frame) {
    panelFirst.setLayout(null);
    panelFirst.setBackground(new Color(240, 240, 240));
    panelCont.setLayout(cl);
    panelCont.add(panelFirst, "1");
    comboBox.setBackground(UIManager.getColor("Button.background"));
    comboBox.setModel(new DefaultComboBoxModel(new String[] {"Selecione", "Candidato", "Recruiter"}));
    comboBox.setBounds(118, 156, 186, 21);
    panelFirst.add(comboBox);

    textField = new JTextField();
    textField.setBounds(118, 64, 186, 21);
    panelFirst.add(textField);
    textField.setColumns(10);

    JLabel lblUsuario = new JLabel("User");
    lblUsuario.setBounds(118, 47, 60, 17);
    panelFirst.add(lblUsuario);

    JLabel lblSenha = new JLabel("Senha");
    lblSenha.setBounds(118, 94, 60, 17);
    panelFirst.add(lblSenha);


    JLabel lblEntrarComo = new JLabel("Entrar como");
    lblEntrarComo.setBounds(117, 138, 83, 17);
    panelFirst.add(lblEntrarComo);

    passwordField = new JPasswordField();
    passwordField.setBounds(118, 110, 186, 21);
    panelFirst.add(passwordField);

    JButton btnNewButton_1 = new JButton("Login");
    btnNewButton_1.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String op = (String) comboBox.getSelectedItem();

        if("Candidato".equals(op)) {
          logarCandidato();
        }		
        else if("Recruiter".equals(op)) {
          logarRecruiter();
        }

        else {
          erroSelecao();
        }	
      }
    });
    btnNewButton_1.setForeground(Color.BLACK);
    btnNewButton_1.setBackground(UIManager.getColor("Button.background"));
    btnNewButton_1.setBounds(118, 192, 186, 27);
    panelFirst.add(btnNewButton_1);

    JButton btnNewButton = new JButton("SignUp Candidato");
    btnNewButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        CadastrarViewCandidato cv = new CadastrarViewCandidato();
        cv.setVisible(true);
      }
    });
    btnNewButton.setBackground(UIManager.getColor("Button.background"));
    btnNewButton.setForeground(Color.BLACK);
    btnNewButton.setBounds(118, 230, 186, 27);
    panelFirst.add(btnNewButton);
    
    JButton btnSignupEmpresa = new JButton("SignUp Empresa");
    btnSignupEmpresa.addActionListener(new ActionListener() {
    	public void actionPerformed(ActionEvent e) {
        CadastrarViewEmpresa ce = new CadastrarViewEmpresa();
        ce.setVisible(true);
    	}
    });
    btnSignupEmpresa.setForeground(Color.BLACK);
    btnSignupEmpresa.setBackground(UIManager.getColor("Button.background"));
    btnSignupEmpresa.setBounds(118, 269, 186, 27);
    panelFirst.add(btnSignupEmpresa);
    frame.getContentPane().add(panelCont);

  }

  public void logarCandidato() {
    JSONObject request = buildJsonCandidato();
    JSONObject serverResponse = new JSONObject();

    try {
      serverResponse = Client.getInstance().sendRequest(request);
      String status = (String) serverResponse.get("status");

      // if (status.equals("USER_NOT_FOUND")) {
      //   JFrame frame = new JFrame("Invalid User!");
      //   JOptionPane.showMessageDialog(frame, "User Inválido!");
      // } else if (status.equals("INVALID_PASSWORD")) {
      //   JFrame frame = new JFrame("Invalid password!");
      //   JOptionPane.showMessageDialog(frame, "Senha Inválida!");
      
      if (status.equals("INVALID_LOGIN")) {
        JFrame frame = new JFrame("Invalid Login!");
        JOptionPane.showMessageDialog(frame, "Credencias Inválidas!");
      } else if (status.equals("INVALID_FIELD")) {
        JFrame frame = new JFrame("Invalid!");
        JOptionPane.showMessageDialog(frame, "Preencha todos campos!");
      } else {
        frame.setVisible(false);
        MainViewCandidato.getInstance().initComponents(this);
      }

    } catch(IOException e) {
      e.printStackTrace();
    } 
  }

  public void logarRecruiter() {
    JSONObject request = buildJsonRecruiter();
    JSONObject serverResponse = new JSONObject();

    try {
      serverResponse = Client.getInstance().sendRequest(request);
      String status = (String) serverResponse.get("status");

      // if (status.equals("USER_NOT_FOUND")) {
      //   JFrame frame = new JFrame("Invalid User!");
      //   JOptionPane.showMessageDialog(frame, "User Inválido!");
      // } else if (status.equals("INVALID_PASSWORD")) {
      //   JFrame frame = new JFrame("Invalid password!");
      //   JOptionPane.showMessageDialog(frame, "Senha Inválida!");

      if (status.equals("INVALID_LOGIN")) {
        JFrame frame = new JFrame("Invalid Login!");
        JOptionPane.showMessageDialog(frame, "Credenciais Inválidas");
      } else if (status.equals("INVALID_FIELD")) {
        JFrame frame = new JFrame("Invalid!");
        JOptionPane.showMessageDialog(frame, "Preencha todos campos!");
      } else {
        frame.setVisible(false);
        MainViewEmpresa.getInstance().initComponents(this);
      }

    } catch(IOException e) {
      e.printStackTrace();
    }
  }

  public JSONObject buildJsonCandidato() {
    String userName = textField.getText();
    String senha = String.valueOf(passwordField.getPassword());

    JSONObject request = new JSONObject();
    request.put("operation", "LOGIN_CANDIDATE");
    JSONObject data = new JSONObject();
    data.put("email", userName);
    data.put("password", senha);
    request.put("data", data);
    return request;
  }  

  public JSONObject buildJsonRecruiter() {
    String userName = textField.getText();
    String senha = String.valueOf(passwordField.getPassword());

    JSONObject request = new JSONObject();
    request.put("operation", "LOGIN_RECRUITER");
    JSONObject data = new JSONObject();
    data.put("email", userName);
    data.put("password", senha);
    request.put("data", data);
    return request;
  }    

  public void erroSelecao() {
    JFrame frame = new JFrame("Erro");
    JOptionPane.showMessageDialog(frame, "É preciso selecionar uma opção!");
  }
}
