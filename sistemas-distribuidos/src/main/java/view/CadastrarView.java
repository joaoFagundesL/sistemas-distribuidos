package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import controller.CandidatoController;
import controller.UsuarioController;
import dao.UsuarioDAO;
import modelo.Usuario;
//import utitlity.SenhaHash;

public class CadastrarView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField nomeField;
	private JTextField emailField;
	private JTextField senhaField;
	private JTextField usuarioField;
	private JTextField controlField;

	public CadastrarView() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 312, 316);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Nome");
		lblNewLabel.setBounds(47, 50, 46, 14);
		contentPane.add(lblNewLabel);
		
		nomeField = new JTextField();
		nomeField.setBounds(99, 47, 161, 20);
		contentPane.add(nomeField);
		nomeField.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Email");
		lblNewLabel_1.setBounds(47, 81, 46, 14);
		contentPane.add(lblNewLabel_1);
		
		emailField = new JTextField();
		emailField.setBounds(99, 78, 161, 20);
		contentPane.add(emailField);
		emailField.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("Senha");
		lblNewLabel_2.setBounds(47, 143, 46, 14);
		contentPane.add(lblNewLabel_2);
		
		senhaField = new JTextField();
		senhaField.setBounds(99, 140, 161, 20);
		contentPane.add(senhaField);
		senhaField.setColumns(10);
		
		JButton btnNewButton = new JButton("Enviar");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String nome = nomeField.getText();
				String usuario = usuarioField.getText();
				String senha = senhaField.getText();
				String email = emailField.getText();
				Integer control = Integer.parseInt(controlField.getText());
								
				UsuarioDAO dao = new UsuarioDAO();
				
				if (dao.consultarPeloUser(usuario) == null && dao.consultarPeloEmail(email) == null) {
					UsuarioController ucontroller = new UsuarioController();
					Usuario u = ucontroller.insert(nome, email, usuario, senha);
					
					if (control == 0) {
						CandidatoController ccontroller = new CandidatoController();
						ccontroller.insert(u);
					} else {
						
					}
					
					JFrame frame = new JFrame("Concluido!");
					JOptionPane.showMessageDialog(frame, "Cadastro Realizado!");
					dispose();
				} else {
					JFrame frame = new JFrame("Erro!");
					JOptionPane.showMessageDialog(frame, "Credenciais Invalidas/Em uso!");
				}
			}
		});
		btnNewButton.setBounds(47, 223, 89, 23);
		contentPane.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Cancelar");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnNewButton_1.setBounds(146, 223, 89, 23);
		contentPane.add(btnNewButton_1);
		
		JLabel lblNewLabel_4 = new JLabel("Usuario");
		lblNewLabel_4.setBounds(47, 110, 46, 14);
		contentPane.add(lblNewLabel_4);
		
		usuarioField = new JTextField();
		usuarioField.setBounds(99, 109, 161, 20);
		contentPane.add(usuarioField);
		usuarioField.setColumns(10);
		
		JLabel lblNewLabel_5 = new JLabel("Funcionario/Cliente (0/1)");
		lblNewLabel_5.setBounds(47, 174, 141, 14);
		contentPane.add(lblNewLabel_5);
		
		controlField = new JTextField();
		controlField.setBounds(176, 171, 84, 20);
		contentPane.add(controlField);
		controlField.setColumns(10);
	}
}