package view;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import org.json.JSONObject;

import cliente.Client;
import modelo.Empresa;

public class EmpresaView extends JPanel {

  private static final long serialVersionUID = 1L;

  // Empresa e;
  private JTextField nomeCandidatoField;
  private JTextField senhaField;
  private JTextField emailField;
  private JTable table;
  private JTextField branchTextField;
  private JTextField textField;

  public EmpresaView(
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
        "Nome", "Email", "Senha", "Industry", "Descricao"
      }
    ));

    branchTextField = new JTextField();
    branchTextField.setBounds(142, 173, 213, 20);
    add(branchTextField);
    branchTextField.setColumns(10);

    emailField = new JTextField();
    emailField.setBounds(142, 140, 213, 21);
    add(emailField);
    emailField.setColumns(10);

    senhaField = new JTextField();
    senhaField.setBounds(142, 205, 213, 20);
    add(senhaField);
    senhaField.setColumns(10);

    nomeCandidatoField = new JTextField();
    nomeCandidatoField.setBounds(142, 108, 213, 20);
    add(nomeCandidatoField);
    nomeCandidatoField.setColumns(10);

    JButton refreshBtn = new JButton("Refresh");
    refreshBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        JSONObject request = new JSONObject();
        String token = Client.getInstance().getToken();
        buildJsonLookup(request, token);
        try {
          JSONObject response = Client.getInstance().sendRequest(request);
          JSONObject data = response.getJSONObject("data");

          String nome =  data.getString("name");
          String email = data.getString("email");
          String senha = data.getString("password");
          String industry = data.getString("industry");
          String descricao = data.getString("description");
          popularTabelaEmpresa(nome, email, senha, industry, descricao);
        } catch(IOException err) {
          err.printStackTrace();
        }
      }
    });
    refreshBtn.setBounds(142, 308, 98, 27);
    add(refreshBtn);

    scrollPane.setViewportView(table);
    table.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {

      }
    });
    scrollPane.setBounds(0, 348, 431, 222);
    add(scrollPane);

    JLabel nomeLabelProfessor = new JLabel("Nome");
    nomeLabelProfessor.setBounds(64, 110, 60, 17);
    add(nomeLabelProfessor);

    JButton btnRemover_1 = new JButton("Remover");
    btnRemover_1.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {

      }
    });
    btnRemover_1.setBounds(252, 308, 94, 27);
    add(btnRemover_1);

    JButton btnLimpar_1 = new JButton("Limpar");
    btnLimpar_1.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        limparTela();
      }
    });
    btnLimpar_1.setBounds(252, 270, 94, 27);
    add(btnLimpar_1);

    JButton btnAtualizar_1 = new JButton("Atualizar");
    btnAtualizar_1.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {


      }
    });
    btnAtualizar_1.setBounds(142, 270, 98, 27);
    add(btnAtualizar_1);


    JLabel lblSenha = new JLabel("Senha");
    lblSenha.setBounds(64, 209, 60, 17);
    add(lblSenha);

    JLabel lblNewLabel = new JLabel("Email");
    lblNewLabel.setBounds(64, 144, 60, 17);
    add(lblNewLabel);

    JLabel lblNewLabel_1 = new JLabel("Branch");
    lblNewLabel_1.setBounds(64, 176, 46, 14);
    add(lblNewLabel_1);

    JLabel lblNewLabel_2 = new JLabel("Descricao");
    lblNewLabel_2.setBounds(64, 238, 58, 17);
    add(lblNewLabel_2);

    textField = new JTextField();
    textField.setBounds(142, 237, 213, 21);
    add(textField);
    textField.setColumns(10);

  }

  public void limparTela() {
    nomeCandidatoField.setText("");
    emailField.setText("");
    senhaField.setText("");
  }

  public void limparTable() {
    DefaultTableModel modelo = (DefaultTableModel) table.getModel();
    modelo.setNumRows(0);
  }

  public void setEmpresa(Empresa e) {
    // this.e = e;
    //        System.out.println(c.getUsuario().getUser());
    // popularTabelaEmpresa();
  }

  public JSONObject buildJsonLookup(JSONObject json, String token) {
    json.put("operation", "LOOKUP_ACCOUNT_RECRUITER");
    json.put("token", token);
    JSONObject data = new JSONObject();
    json.put("data", data);
    return json;
  }

  public void popularTabelaEmpresa(String nome, String email,
    String senha, String industry, String descricao) {
    DefaultTableModel modelo = (DefaultTableModel) table.getModel();

    // EmpresaDAO edao = new EmpresaDAO();
    // Empresa em = edao.consultarPorId(Empresa.class, e.getId());


    if (modelo.getRowCount() > 0) {
      modelo.setRowCount(0);
    }

    // if (em == null)
    // 	return;

    Object[] arr = new Object[5];
    arr[0] = nome;
    arr[1] = email;
    arr[2] = senha;
    arr[3] = industry;
    arr[4] = descricao;

    modelo.addRow(arr);		
  }
}
