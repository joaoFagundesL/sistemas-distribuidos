package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.json.JSONObject;

import cliente.Client;
//import utitlity.SenhaHash;

public class CadastrarViewEmpresa extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField nomeField;
	private JTextField emailField;
	private JTextField senhaField;
	private JTextField branchField;
	private JTextField descricaoField;

	public CadastrarViewEmpresa() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 325, 293);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Nome");
		lblNewLabel.setBounds(33, 50, 46, 14);
		contentPane.add(lblNewLabel);
		
		nomeField = new JTextField();
		nomeField.setBounds(98, 47, 161, 20);
		contentPane.add(nomeField);
		nomeField.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Email");
		lblNewLabel_1.setBounds(33, 81, 46, 14);
		contentPane.add(lblNewLabel_1);
		
		emailField = new JTextField();
		emailField.setBounds(98, 79, 161, 20);
		contentPane.add(emailField);
		emailField.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("Senha");
		lblNewLabel_2.setBounds(33, 113, 46, 14);
		contentPane.add(lblNewLabel_2);
		
		senhaField = new JTextField();
		senhaField.setBounds(98, 111, 161, 20);
		contentPane.add(senhaField);
		senhaField.setColumns(10);
		
		JButton btnNewButton = new JButton("Enviar");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String nome = nomeField.getText();
				String senha = senhaField.getText();
				String email = emailField.getText();
				String branch = branchField.getText();
				String descricao = descricaoField.getText();

        JSONObject request = new JSONObject();
        JSONObject response = new JSONObject();

        request = buildSignupEmpresa(request, email, nome, senha, branch, descricao);

        try {
          response = Client.getInstance().sendRequest(request);
          String status = (String) response.get("status");

          if (status.equals("USER_EXISTS")) {
            JFrame frame = new JFrame("Invalid!");
            JOptionPane.showMessageDialog(frame, "Usuario ja existe!");
          } else if (status.equals("INVALID_EMAIL")) {
            JFrame frame = new JFrame("Invalid!");
            JOptionPane.showMessageDialog(frame, "Email inválido!");
          } else if (status.equals("INVALID_PASSWORD")) {
            JFrame frame = new JFrame("Invalid!");
            JOptionPane.showMessageDialog(frame, "Senha inválida!");
          } else if (status.equals("INVALID_FIELD")) {
            JFrame frame = new JFrame("Invalid!");
            JOptionPane.showMessageDialog(frame, "Preencha todos campos!");
          } else {
            JFrame frame = new JFrame("Success!");
            JOptionPane.showMessageDialog(frame, "Cadastro realizado!");
            dispose();
          }

        } catch(IOException err) {
          err.printStackTrace();
        }
      }
    });
    btnNewButton.setBounds(59, 221, 89, 23);
    contentPane.add(btnNewButton);

    JButton btnNewButton_1 = new JButton("Cancelar");
    btnNewButton_1.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        dispose();
      }
    });
    btnNewButton_1.setBounds(170, 221, 89, 23);
    contentPane.add(btnNewButton_1);
    
    JLabel lblBranch = new JLabel("Branch");
    lblBranch.setBounds(33, 147, 58, 17);
    contentPane.add(lblBranch);
    
    JLabel lblNewLabel_3 = new JLabel("Descricao");
    lblNewLabel_3.setBounds(30, 176, 79, 14);
    contentPane.add(lblNewLabel_3);
    
    branchField = new JTextField();
    branchField.setBounds(98, 143, 161, 21);
    contentPane.add(branchField);
    branchField.setColumns(10);
    
    descricaoField = new JTextField();
    descricaoField.setBounds(98, 174, 161, 21);
    contentPane.add(descricaoField);
    descricaoField.setColumns(10);
  }

  private JSONObject buildSignupEmpresa(JSONObject request, String email, String nome, String senha,
                      String branch, String descricao) {

    request.put("operation", "SIGNUP_RECRUITER");
    JSONObject data = new JSONObject();
    data.put("name", nome);
    data.put("email", email);
    data.put("password", senha);
    data.put("branch", branch);
    data.put("description",descricao);
    request.put("data", data);
    return request;

  }
}
