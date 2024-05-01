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
import controller.CandidatoController;
import controller.UsuarioController;

public class CandidatoView extends JPanel {

  private static final long serialVersionUID = 1L;

  private JTextField nomeCandidatoField;
  private JTextField senhaField;
  private JTextField emailField;
  private JTable table;

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
        "Nome", "Email", "Senha"
      }
    ));

    emailField = new JTextField();
    emailField.setBounds(142, 160, 213, 21);
    add(emailField);
    emailField.setColumns(10);

    senhaField = new JTextField();
    senhaField.setBounds(142, 193, 213, 20);
    add(senhaField);
    senhaField.setColumns(10);

    nomeCandidatoField = new JTextField();
    nomeCandidatoField.setBounds(142, 129, 213, 20);
    add(nomeCandidatoField);
    nomeCandidatoField.setColumns(10);


    scrollPane.setViewportView(table);
    table.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {

        String nome =  table.getValueAt(table.getSelectedRow(), 0).toString();
        String email = table.getValueAt(table.getSelectedRow(), 1).toString();
        String senha = table.getValueAt(table.getSelectedRow(), 2).toString();

        limparTela();

        nomeCandidatoField.setText(nome);
        emailField.setText(email);
        senhaField.setText(senha);
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
        JSONObject request = new JSONObject();
        String token = Client.getInstance().getToken();
        buildJsonDelete(request, token);
        try {
          JSONObject response = Client.getInstance().sendRequest(request);
        } catch(IOException err) {
          err.printStackTrace();
        }

        JFrame frame = new JFrame("JOptionPane exemplo");
        JOptionPane.showMessageDialog(frame, "Registro Excluído!");

      }
    });
    btnRemover_1.setBounds(142, 255, 94, 27);
    add(btnRemover_1);

    JButton btnLimpar_1 = new JButton("Limpar");
    btnLimpar_1.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        limparTela();
      }
    });
    btnLimpar_1.setBounds(248, 294, 98, 27);
    add(btnLimpar_1);

    JButton btnAtualizar_1 = new JButton("Atualizar");
    btnAtualizar_1.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String email = emailField.getText();
        String nome = nomeCandidatoField.getText();
        String senha = senhaField.getText();

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

          if (data.has("name")) {
            nomeResponse = data.getString("name");
          } else {
           // nomeResponse = modelo.getValueAt(0, 0).toString();
          }

          if (data.has("email")) {
            emailResponse = data.getString("email");
          } else {
            //emailResponse = modelo.getValueAt(0, 1).toString();
          }

          if (data.has("password")) {
            senhaResponse = data.getString("password");
          } else {
            //senhaResponse = modelo.getValueAt(0, 2).toString();
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
          //popularTabelaCandidato(nomeResponse, emailResponse, senhaResponse);
        } catch(IOException err) {
          err.printStackTrace();
        }
      }
    });
    btnAtualizar_1.setBounds(248, 255, 98, 27);
    add(btnAtualizar_1);


    JLabel lblSenha = new JLabel("Senha");
    lblSenha.setBounds(64, 191, 60, 17);
    add(lblSenha);

    JLabel lblNewLabel = new JLabel("Email");
    lblNewLabel.setBounds(64, 162, 60, 17);
    add(lblNewLabel);

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
          popularTabelaCandidato(nome, email, senha);
        } catch(IOException err) {
          err.printStackTrace();
        }
      }
    });
    refreshBtn.setBounds(142, 294, 98, 27);
    add(refreshBtn);

  }

  public JSONObject buildJsonLookup(JSONObject json, String token) {
    json.put("operation", "LOOKUP_ACCOUNT_CANDIDATE");
    JSONObject data = new JSONObject();
    data.put("token", token);
    json.put("data", data);
    return json;
  }

  public JSONObject buildJsonDelete(JSONObject json, String token) {
    json.put("operation", "DELETE_ACCOUNT_CANDIDATE");
    JSONObject data = new JSONObject();
    data.put("token", token);
    json.put("data", data);
    return json;
  }

  public JSONObject buildJsonUpdate(JSONObject json, String token, JSONObject data) {
    json.put("operation", "UPDATE_ACCOUNT_CANDIDATE");
    data.put("token", token);
    json.put("data", data);

    return json;
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

  public void setCandidato(
  ) {
  }

  public void popularTabelaCandidato(String nome, String email, String senha) {
    DefaultTableModel modelo = (DefaultTableModel) table.getModel();

    // CandidatoDAO cdao = new CandidatoDAO();
    // Candidato can = cdao.consultarPorId(Candidato.class, c.getId());

    if (modelo.getRowCount() > 0) {
      modelo.setRowCount(0);
    }

    // if (can == null)
    // return;
    // 
    Object[] arr = new Object[3];
    arr[0] = nome;
    arr[1] = email;
    arr[2] = senha;

    modelo.addRow(arr);		
  }
}
