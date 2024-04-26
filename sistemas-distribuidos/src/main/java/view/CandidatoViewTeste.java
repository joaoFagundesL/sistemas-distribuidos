package view;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import controller.CandidatoController;
import controller.UsuarioController;
import dao.CandidatoDAO;
import modelo.Candidato;
import modelo.Usuario;

public class CandidatoViewTeste extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	// Candidato c;
	private JTextField nomeCandidatoField;
	private JTextField senhaField;
	private JTextField emailField;
	private JTable table;
	private JTextField usuarioTextField;
	
	public CandidatoViewTeste(
    // final Candidato c
  ) {
		// this.c = c;
		initComponents(
      // c
    );
	}
	
	public void initComponents(
    // final Candidato c
  ) {
		setLayout(null);
		setBackground(SystemColor.control);
		
		JScrollPane scrollPane = new JScrollPane();
		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Nome", "Email", "Senha"
			}
		));
		
		usuarioTextField = new JTextField();
		usuarioTextField.setBounds(142, 192, 213, 20);
		add(usuarioTextField);
		usuarioTextField.setColumns(10);
		
		emailField = new JTextField();
		emailField.setBounds(142, 160, 213, 21);
		add(emailField);
		emailField.setColumns(10);
		
		senhaField = new JTextField();
		senhaField.setBounds(142, 223, 213, 20);
		add(senhaField);
		senhaField.setColumns(10);
		
		nomeCandidatoField = new JTextField();
		nomeCandidatoField.setBounds(142, 129, 213, 20);
		add(nomeCandidatoField);
		nomeCandidatoField.setColumns(10);
		
		popularTabelaCandidato();
		
		scrollPane.setViewportView(table);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				String nome =  table.getValueAt(table.getSelectedRow(), 0).toString();
				String email = table.getValueAt(table.getSelectedRow(), 1).toString();
				String usuario = table.getValueAt(table.getSelectedRow(), 2).toString();
				String senha = table.getValueAt(table.getSelectedRow(), 3).toString();

				limparTela();
				
				nomeCandidatoField.setText(nome);
				emailField.setText(email);
				senhaField.setText(senha);
				usuarioTextField.setText(usuario);
			}
		});
		scrollPane.setBounds(0, 348, 431, 222);
		add(scrollPane);
		
		JLabel nomeLabelProfessor = new JLabel("Nome");
		nomeLabelProfessor.setBounds(64, 131, 60, 17);
		add(nomeLabelProfessor);
		
		JButton btnRemover_1 = new JButton("Remover");
		btnRemover_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CandidatoController ccontroller = new CandidatoController();
				UsuarioController ucontroller = new UsuarioController();

				// ccontroller.remover(Candidato.class, c.getId());
				// ucontroller.remover(Usuario.class, c.getUsuario().getId());
				
				JFrame frame = new JFrame("JOptionPane exemplo");
				JOptionPane.showMessageDialog(frame, "Registro Excluído!");
				
				limparTela();
				
				// setCandidato(c);
			}
		});
		btnRemover_1.setBounds(53, 281, 94, 27);
		add(btnRemover_1);
		
		JButton btnLimpar_1 = new JButton("Limpar");
		btnLimpar_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				limparTela();
			}
		});
		btnLimpar_1.setBounds(265, 281, 98, 27);
		add(btnLimpar_1);
		
		JButton btnAtualizar_1 = new JButton("Atualizar");
		btnAtualizar_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				String email = emailField.getText();
				String nome = nomeCandidatoField.getText();
				String senha = senhaField.getText();
				String usuario = usuarioTextField.getText();
				
				UsuarioController ucontroller = new UsuarioController();
				// ucontroller.update(c, nome, email, usuario, senha);
				
				CandidatoDAO cdao = new CandidatoDAO();
				// cdao.update(c, nome, email, usuario, senha);
				
				
				JFrame frame = new JFrame("Mensagem");
				JOptionPane.showMessageDialog(frame, "Atualizado com sucesso!");

				// setCandidato(c);	
			}
		});
		btnAtualizar_1.setBounds(157, 281, 98, 27);
		add(btnAtualizar_1);
	
		
		JLabel lblSenha = new JLabel("Senha");
		lblSenha.setBounds(64, 225, 60, 17);
		add(lblSenha);
		
		JLabel lblNewLabel = new JLabel("Email");
		lblNewLabel.setBounds(64, 162, 60, 17);
		add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Usuário");
		lblNewLabel_1.setBounds(64, 195, 46, 14);
		add(lblNewLabel_1);
		
	}
	
	public void limparTela() {
		nomeCandidatoField.setText("");
		emailField.setText("");
		senhaField.setText("");
		usuarioTextField.setText("");
	}
	
	public void setCandidato(
    // Candidato c
  ) {
        // this.c = c;
//        System.out.println(c.getUsuario().getUser());
        popularTabelaCandidato();
    }
	
	public void popularTabelaCandidato() {
		DefaultTableModel modelo = (DefaultTableModel) table.getModel();
		
		// CandidatoDAO cdao = new CandidatoDAO();
		// Candidato can = cdao.consultarPorId(Candidato.class, c.getId());
		
		// if (modelo.getRowCount() > 0) {
		// 	modelo.setRowCount(0);
		// }
		// 
		// if (can == null)
			// return;
		// 
		// Object[] arr = new Object[4];
		// arr[0] = can.getUsuario().getNome();
		// arr[1] = can.getUsuario().getEmail();
		// arr[2] = can.getUsuario().getUser();
		// arr[3] = can.getUsuario().getSenha();
		// 
		// modelo.addRow(arr);		
	}
}
