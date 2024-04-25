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

public class CadastrarViewCandidato extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField nomeField;
	private JTextField emailField;
	private JTextField senhaField;

	public CadastrarViewCandidato() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 281, 231);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Nome");
		lblNewLabel.setBounds(33, 50, 46, 14);
		contentPane.add(lblNewLabel);
		
		nomeField = new JTextField();
		nomeField.setBounds(78, 47, 161, 20);
		contentPane.add(nomeField);
		nomeField.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Email");
		lblNewLabel_1.setBounds(33, 81, 46, 14);
		contentPane.add(lblNewLabel_1);
		
		emailField = new JTextField();
		emailField.setBounds(78, 76, 161, 20);
		contentPane.add(emailField);
		emailField.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("Senha");
		lblNewLabel_2.setBounds(33, 113, 46, 14);
		contentPane.add(lblNewLabel_2);
		
		senhaField = new JTextField();
		senhaField.setBounds(78, 110, 161, 20);
		contentPane.add(senhaField);
		senhaField.setColumns(10);
		
		JButton btnNewButton = new JButton("Enviar");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String nome = nomeField.getText();
				String senha = senhaField.getText();
				String email = emailField.getText();

        JSONObject request = new JSONObject();
        JSONObject response = new JSONObject();

        request = buildSignupCandidate(request, email, nome, senha);

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
    btnNewButton.setBounds(47, 182, 89, 23);
    contentPane.add(btnNewButton);

    JButton btnNewButton_1 = new JButton("Cancelar");
    btnNewButton_1.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        dispose();
      }
    });
    btnNewButton_1.setBounds(148, 182, 89, 23);
    contentPane.add(btnNewButton_1);
  }

  private JSONObject buildSignupCandidate(JSONObject request, String email, String nome, String senha) {
    request.put("operation", "SIGNUP_CANDIDATE");
    JSONObject data = new JSONObject();
    data.put("email", email);
    data.put("password", senha);
    data.put("name", nome);
    request.put("data", data);
    return request;

  }
}
