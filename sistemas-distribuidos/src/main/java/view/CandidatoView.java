package view;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

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

import dao.CandidatoDAO;
import dao.UsuarioDAO;
import modelo.Candidato;
import modelo.Usuario;
//import utitlity.SenhaHash;

public class CandidatoView extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	Candidato c;
	private JTextField nomeCandidatoField;
	private JTextField senhaField;
	private JTextField emailField;
	private JTable table;
	private JTextField usuarioTextField;
	
	public CandidatoView() {
		initComponents();
	}
	
	public void initComponents() {
		setLayout(null);
		setBackground(SystemColor.control);
		
		JScrollPane scrollPane = new JScrollPane();
		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Nome", "Email", "Usuario", "Senha"
			}
		));
		
		usuarioTextField = new JTextField();
		usuarioTextField.setBounds(142, 165, 213, 20);
		add(usuarioTextField);
		usuarioTextField.setColumns(10);
		
		emailField = new JTextField();
		emailField.setBounds(142, 133, 213, 21);
		add(emailField);
		emailField.setColumns(10);
		
		senhaField = new JTextField();
		senhaField.setBounds(142, 196, 213, 20);
		add(senhaField);
		senhaField.setColumns(10);
		
		nomeCandidatoField = new JTextField();
		nomeCandidatoField.setBounds(142, 106, 213, 20);
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
				
				/* Dizendo para o objeto p que foi o professor que o usuario clicou */
				CandidatoDAO dao = new CandidatoDAO();
//				c = dao.consultarPeloCpf(cpfText.getText());
			}
		});
		scrollPane.setBounds(0, 380, 561, 235);
		add(scrollPane);
		
		JLabel nomeLabelProfessor = new JLabel("Nome");
		nomeLabelProfessor.setBounds(64, 108, 60, 17);
		add(nomeLabelProfessor);
		

		JButton btnEnviar_1 = new JButton("Enviar");
		btnEnviar_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String nome = nomeCandidatoField.getText();
				String email = emailField.getText();
				String senha = senhaField.getText();
				String usuario = usuarioTextField.getText();
				
				Usuario u = new Usuario();
				UsuarioDAO udao = new UsuarioDAO();
				u.setSenha(senha);
				u.setUser(usuario);
				u.setEmail(email);
				u.setNome(nome);
				
				try {
					udao.insertWithQuery(u);
				} catch (Exception e1) {
					e1.printStackTrace();
				}

				Candidato c = new Candidato();
				c.setUsuario(u);
				CandidatoDAO cdao = new CandidatoDAO();
				try {
					cdao.insertWithQuery(c);
					JFrame frame = new JFrame("Enviado!");
					JOptionPane.showMessageDialog(frame, "Registro Enviado!");
					limparTela();
					popularTabelaCandidato();
				} catch (Exception e1) {
					e1.printStackTrace();
					JFrame frame = new JFrame("Erro!");
					JOptionPane.showMessageDialog(frame, "Erro!");
				}
			}
		});
		btnEnviar_1.setBounds(64, 313, 94, 27);
		add(btnEnviar_1);
		
		JButton btnRemover_1 = new JButton("Remover");
		btnRemover_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CandidatoDAO cDao = new CandidatoDAO();
				UsuarioDAO udao = new UsuarioDAO();

				cDao.remover(Candidato.class, c.getId());
				udao.remover(Usuario.class, c.getUsuario().getId());
				
				JFrame frame = new JFrame("JOptionPane exemplo");
				JOptionPane.showMessageDialog(frame, "Registro Excluído!");
				
				limparTela();
				
				popularTabelaCandidato();
			}
		});
		btnRemover_1.setBounds(170, 313, 94, 27);
		add(btnRemover_1);
		
		JButton btnLimpar_1 = new JButton("Limpar");
		btnLimpar_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				limparTela();
			}
		});
		btnLimpar_1.setBounds(386, 313, 98, 27);
		add(btnLimpar_1);
		
		JButton btnAtualizar_1 = new JButton("Atualizar");
		btnAtualizar_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				/* Obtem os campos que o usuario informou */
				String email = emailField.getText();
				String nome = nomeCandidatoField.getText();
				String senha = senhaField.getText();
				String usuario = usuarioTextField.getText();
				
				/* Atualiza */
				UsuarioDAO udao = new UsuarioDAO();
				udao.update(c.getUsuario(), nome, email, usuario, senha);
				
				/* Confirmando atualizacao */
				JFrame frame = new JFrame("Mensagem");
				JOptionPane.showMessageDialog(frame, "Atualizado com sucesso!");
				
				/* Atualizando a JTable */
				popularTabelaCandidato();
			
			}
		});
		btnAtualizar_1.setBounds(276, 313, 98, 27);
		add(btnAtualizar_1);
		
		JLabel lblCadastroDeAluno_1 = new JLabel("CRUD");
		lblCadastroDeAluno_1.setFont(new Font("Dialog", Font.BOLD, 18));
		lblCadastroDeAluno_1.setBounds(64, 71, 306, 17);
		add(lblCadastroDeAluno_1);
	
		
		JLabel lblSenha = new JLabel("Senha");
		lblSenha.setBounds(64, 198, 60, 17);
		add(lblSenha);
		
		JLabel lblNewLabel = new JLabel("Email");
		lblNewLabel.setBounds(64, 135, 60, 17);
		add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Usuário");
		lblNewLabel_1.setBounds(64, 168, 46, 14);
		add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("CPF");
		lblNewLabel_2.setBounds(64, 235, 46, 14);
		add(lblNewLabel_2);
		
		
		
	}
	
	public void limparTela() {
		nomeCandidatoField.setText("");
		emailField.setText("");
		senhaField.setText("");
		usuarioTextField.setText("");
	}
	
	public void popularTabelaCandidato() {
		DefaultTableModel modelo = (DefaultTableModel) table.getModel();
		
		CandidatoDAO pdao = new CandidatoDAO();
		List<Candidato> candidatos = pdao.consultarTodos(); 
				
		if (modelo.getRowCount() > 0) {
			modelo.setRowCount(0);
		}
		
		for(Candidato c : candidatos) {
			Object[] arr = new Object[4];
			arr[0] = c.getUsuario().getNome();
			arr[1] = c.getUsuario().getEmail();
			arr[2] = c.getUsuario().getUser();
			arr[3] = c.getUsuario().getSenha();
			modelo.addRow(arr);		
		}
	}
}
