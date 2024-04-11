package view;


import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import controller.CandidatoController;
import modelo.Candidato;
import javax.swing.UIManager;

import org.json.JSONObject;

public class LoginView {

	JSONObject jsonMessage = new JSONObject();
	JPanel panelCont = new JPanel();
	JPanel panelFirst = new JPanel();
	JButton btnVoltar = new JButton("Voltar");
	CardLayout cl = new CardLayout();
	private JTextField textField;
	private JPasswordField passwordField;
	
	@SuppressWarnings("rawtypes")
	JComboBox comboBox = new JComboBox();

	JFrame frame = new JFrame("Sistema");
	
	public LoginView(JSONObject jsonMessage) {
		this.jsonMessage = jsonMessage;
		initComponents(frame);
		frame.setBounds(100, 100, 435, 345);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void initComponents(JFrame frame) {
		JSONObject data = jsonMessage.getJSONObject("data");

		panelFirst.setLayout(null);
		panelFirst.setBackground(new Color(240, 240, 240));
		panelCont.setLayout(cl);
		panelCont.add(panelFirst, "1");
		comboBox.setBackground(UIManager.getColor("Button.background"));
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Selecione", "Candidato", "Funcionario"}));
		comboBox.setBounds(118, 156, 186, 21);
		panelFirst.add(comboBox);
		
		textField = new JTextField();
		textField.setBounds(118, 64, 186, 21);
		panelFirst.add(textField);
		textField.setColumns(10);
		textField.setText(data.getString("email"));
		
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
		passwordField.setText(data.getString("password"));
		
		JButton btnNewButton_1 = new JButton("Login");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				frame.setVisible(false);
//				new MainView(frame);
				
				String op = (String) comboBox.getSelectedItem();
				
				if("Candidato".equals(op)) {
				    logarCandidato();
				}		
				else if(false) {
					
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
		
		JButton btnNewButton = new JButton("Cadastrar");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CadastrarView cv = new CadastrarView();
				cv.setVisible(true);
			}
		});
		btnNewButton.setBackground(UIManager.getColor("Button.background"));
		btnNewButton.setForeground(Color.BLACK);
		btnNewButton.setBounds(118, 230, 186, 27);
		panelFirst.add(btnNewButton);
		frame.getContentPane().add(panelCont);
		
	}
	
	public void logarCandidato() {
		CandidatoController fController = new CandidatoController();
		System.out.println( String.valueOf(passwordField.getPassword()));
		Candidato c = fController.validarLogin(textField.getText(), String.valueOf(passwordField.getPassword()));
		if(c == null) {
			JFrame frame = new JFrame("Erro");
			JOptionPane.showMessageDialog(frame, "Login ou Senha inválidos!");
		} else {				
			frame.setVisible(false);
			System.out.println(c.getUsuario().getNome());
			 new MainViewCandidato(frame, c);
		}
	}
	
//	public void logarFuncionario() {
//		ProfessorController adController = new ProfessorController();
//		Professor p = adController.validarLogin(textField.getText(), String.valueOf(passwordField.getPassword()));
//		
//		if(p == null) {
//			JFrame frame = new JFrame("Erro");
//			JOptionPane.showMessageDialog(frame, "Login ou Senha inválidos!");
//		} else {				
//			frame.dispose();
//			frame.setVisible(false);
//			new ProfessorMainView(p);
//		}
//	}
	
	public void erroSelecao() {
		JFrame frame = new JFrame("Erro");
		JOptionPane.showMessageDialog(frame, "É preciso selecionar uma opção!");
	}
}