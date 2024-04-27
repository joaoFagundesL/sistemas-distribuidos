package view;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import modelo.Empresa;

public class EmpresaViewTeste extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	// Empresa e;
	private JTextField nomeCandidatoField;
	private JTextField senhaField;
	private JTextField emailField;
	private JTable table;
	private JTextField usuarioTextField;
	
	public EmpresaViewTeste(
    // final Empresa e
  ) {
		// this.e = e;
		initComponents(
      // e
    );
	}
	
	public void initComponents(
  // final Empresa e
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
				"Nome", "Email", "Senha", "Ramo", "Descricao"
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
		
		popularTabelaEmpresa();
		
		scrollPane.setViewportView(table);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				
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
	
	public void setEmpresa(Empresa e) {
        // this.e = e;
//        System.out.println(c.getUsuario().getUser());
        // popularTabelaEmpresa();
    }
	
	public void popularTabelaEmpresa() {
		// DefaultTableModel modelo = (DefaultTableModel) table.getModel();
		
		// EmpresaDAO edao = new EmpresaDAO();
		// Empresa em = edao.consultarPorId(Empresa.class, e.getId());
		
		
		// if (modelo.getRowCount() > 0) {
		// 	modelo.setRowCount(0);
		// }
		
		// if (em == null)
		// 	return;
		
		// Object[] arr = new Object[3];
		// arr[0] = em.getUsuario().getNome();
		// arr[1] = em.getUsuario().getEmail();
		// arr[2] = em.getUsuario().getSenha();
		
		// modelo.addRow(arr);		
	}
}
