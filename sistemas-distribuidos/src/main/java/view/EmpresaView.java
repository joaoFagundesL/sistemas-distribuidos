package view;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

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

import org.json.JSONObject;

import cliente.Client;
import modelo.Empresa;

public class EmpresaView extends JPanel {

  private static final long serialVersionUID = 1L;

  private JTextField nomeCandidatoField;
  private JTextField senhaField;
  private JTextField emailField;
  private JTable table;
  private JTextField branchTextField;
  private JTextField descricaoField;

  public EmpresaView() {
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

          if (response.getString("status").equals("SUCCESS")) {
            String nome =  data.getString("name");
            String email = data.getString("email");
            String senha = data.getString("password");
            String industry = data.getString("industry");
            String descricao = data.getString("description");
            popularTabelaEmpresa(nome, email, senha, industry, descricao);
          }

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

        String nome =  table.getValueAt(table.getSelectedRow(), 0).toString();
        String email = table.getValueAt(table.getSelectedRow(), 1).toString();
        String senha = table.getValueAt(table.getSelectedRow(), 2).toString();
        String industry = table.getValueAt(table.getSelectedRow(), 3).toString();
        String description = table.getValueAt(table.getSelectedRow(), 4). toString();

        limparTela();

        nomeCandidatoField.setText(nome);
        emailField.setText(email);
        senhaField.setText(senha);
        branchTextField.setText(industry);
        descricaoField.setText(description);
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
        JSONObject request = new JSONObject();
        String token = Client.getInstance().getToken();
        buildJsonDelete(request, token);
        try {
          JSONObject response = Client.getInstance().sendRequest(request);

          String status = response.getString("status");

          if (status.equals("SUCCESS")) {
            JFrame frame = new JFrame("JOptionPane exemplo");
            JOptionPane.showMessageDialog(frame, "Registro Excluído!");
            MainViewEmpresa.getInstance().frame.dispose();
            LoginView.getInstance().frame.setVisible(true);
          } else {
            JFrame frame = new JFrame("JOptionPane exemplo");
            JOptionPane.showMessageDialog(frame, "Error");
          }
        } catch(IOException err) {
          err.printStackTrace();
        }
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
        String email = emailField.getText();
        String nome = nomeCandidatoField.getText();
        String senha = senhaField.getText();
        String industry = branchTextField.getText();  
        String description = descricaoField.getText();

        JSONObject dataRequest = new JSONObject();  

        if(!nome.equals("")) {
          dataRequest.put("name", nome);
        }

        if (!email.equals("")) {
          dataRequest.put("email", email);
        }

        if (!senha.equals("")) {
          dataRequest.put("password", senha);
        }

        if (!industry.equals("")) {
          dataRequest.put("industry", industry);
        }

        if (!description.equals("")) {
          dataRequest.put("description", description);
          System.out.println("description not empty");
        }

        JSONObject request = new JSONObject();
        String token = Client.getInstance().getToken();
        buildJsonUpdate(request, token, dataRequest);

        try {
          DefaultTableModel modelo = (DefaultTableModel) table.getModel();
          JSONObject response = Client.getInstance().sendRequest(request);
          JSONObject data = response.getJSONObject("data");

          String nomeResponse = "";
          String emailResponse = "";
          String senhaResponse = "";
          String industryResponse = "";
          String descriptionResponse = "";

          if (data.has("name")) {
            nomeResponse = data.getString("name");
          } else {
          }

          if (data.has("email")) {
            emailResponse = data.getString("email");
          }

          if (data.has("password")) {
            senhaResponse = data.getString("password");
          }          

          if (data.has("description")) {
            descriptionResponse = data.getString("description");
          }

          if (data.has("industry")) {
            industryResponse = data.getString("industry");
          }

          String status = response.getString("status");

          if (status.equals("SUCCESS")) {
            JFrame frame = new JFrame("Mensagem");
            JOptionPane.showMessageDialog(frame, "Atualizado com sucesso!");
          } else if(status.equals("INVALID_EMAIL")){
            JFrame frame = new JFrame("Mensagem");
            JOptionPane.showMessageDialog(frame, "Email em uso!");
          } else {
            JFrame frame = new JFrame("Mensagem");
            JOptionPane.showMessageDialog(frame, "Erro!");
          }
        } catch(IOException err) {
          err.printStackTrace();
        }
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

    descricaoField = new JTextField();
    descricaoField.setBounds(142, 237, 213, 21);
    add(descricaoField);
    descricaoField.setColumns(10);

  }

  public void limparTela() {
    nomeCandidatoField.setText("");
    emailField.setText("");
    senhaField.setText("");
    descricaoField.setText("");
    branchTextField.setText("");
    limparTable();
  }

  public void limparTable() {
    DefaultTableModel modelo = (DefaultTableModel) table.getModel();
    modelo.setNumRows(0);
    System.out.println("ENTROU PARA LIMPAR");
  }

  public JSONObject buildJsonLookup(JSONObject json, String token) {
    json.put("operation", "LOOKUP_ACCOUNT_RECRUITER");
    json.put("token", token);
    JSONObject data = new JSONObject();
    json.put("data", data);
    return json;
  }

  public JSONObject buildJsonDelete(JSONObject json, String token) {
    json.put("operation", "DELETE_ACCOUNT_RECRUITER");
    JSONObject data = new JSONObject();
    json.put("token", token);
    json.put("data", data);
    return json;
  }

  public JSONObject buildJsonUpdate(JSONObject json, String token, JSONObject data) {
    json.put("operation", "UPDATE_ACCOUNT_RECRUITER");
    json.put("token", token);
    json.put("data", data);

    return json;
  }

  public void popularTabelaEmpresa(String nome, String email,
    String senha, String industry, String descricao) {
    DefaultTableModel modelo = (DefaultTableModel) table.getModel();

    if (modelo.getRowCount() > 0) {
      modelo.setRowCount(0);
    }

    Object[] arr = new Object[5];
    arr[0] = nome;
    arr[1] = email;
    arr[2] = senha;
    arr[3] = industry;
    arr[4] = descricao;

    modelo.addRow(arr);		
  }
}
