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
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import org.json.JSONArray;
import org.json.JSONObject;

import cliente.Client;
import javax.swing.JSeparator;
import java.awt.Color;

public class CompetenciaView extends JPanel {

  private static final long serialVersionUID = 1L;

  private JTextField skillField;
  private JTable table;
  private JTextField idField;

  public CompetenciaView() {
    initComponents();
  }

  public void initComponents() {
    setLayout(null);
    setBackground(SystemColor.control);

    JSpinner spinner = new JSpinner();

    JScrollPane scrollPane = new JScrollPane();
    table = new JTable();
    table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
    table.setModel(new DefaultTableModel(
      new Object[][] {
      },
      new String[] {
        "Id", "Skill", "Experience" 
      }
    ));

    skillField = new JTextField();
    skillField.setBounds(114, 162, 114, 20);
    add(skillField);
    skillField.setColumns(10);


    scrollPane.setViewportView(table);
    table.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        // String nome =  table.getValueAt(table.getSelectedRow(), 0).toString();
        // String email = table.getValueAt(table.getSelectedRow(), 1).toString();
        // String senha = table.getValueAt(table.getSelectedRow(), 2).toString();
        //
        // limparTela();
        //
        // skillField.setText(nome);
        // emailField.setText(email);
        // senhaField.setText(senha);
      }
    });
    scrollPane.setBounds(0, 348, 431, 222);
    add(scrollPane);

    JLabel skillLabel = new JLabel("Skill");
    skillLabel.setBounds(45, 164, 60, 17);
    add(skillLabel);

    JButton btnRemover_1 = new JButton("Remover");
    btnRemover_1.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        JSONObject request = new JSONObject();
        String token = Client.getInstance().getToken();

        String idString = idField.getText();

        if (idString.equals("")) {
          JFrame frame = new JFrame("JOptionPane exemplo");
          JOptionPane.showMessageDialog(frame, "Informe o id!");
          return;
        }

        Integer id = Integer.parseInt(idString);

        buildJsonDelete(request, id, token);
        try {
          JSONObject response = Client.getInstance().sendRequest(request);

          String status = response.getString("status");

          if (status.equals("SUCCESS")) {
            JFrame frame = new JFrame("JOptionPane exemplo");
            JOptionPane.showMessageDialog(frame, "Registro Excluído!");
          } else {
            JFrame frame = new JFrame("JOptionPane exemplo");
            JOptionPane.showMessageDialog(frame, "Id nao existe");
          }
        } catch(IOException err) {
          err.printStackTrace();
        }
      }
    });
    btnRemover_1.setBounds(306, 76, 94, 27);
    add(btnRemover_1);

    JButton btnLimpar_1 = new JButton("Limpar");
    btnLimpar_1.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        limparTela();
      }
    });
    btnLimpar_1.setBounds(45, 272, 98, 27);
    add(btnLimpar_1);

    JButton btnAtualizar_1 = new JButton("Atualizar");
    btnAtualizar_1.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String skill = skillField.getText();
        Integer experience = (Integer) spinner.getValue();
        String idString = idField.getText();
        Integer id = Integer.parseInt(idString);

        JSONObject dataRequest = new JSONObject();  

        if(!skill.equals("")) {
          dataRequest.put("skill", skill);
        }

        if (experience != null) {
          dataRequest.put("experience", experience);
        }

        if (id != null) {
          dataRequest.put("id", id);
        }

        JSONObject request = new JSONObject();
        String token = Client.getInstance().getToken();
        buildJsonUpdate(request, token, dataRequest);

        try {
          DefaultTableModel modelo = (DefaultTableModel) table.getModel();
          JSONObject response = Client.getInstance().sendRequest(request);
          JSONObject data = response.getJSONObject("data");

          String skillResponse = "";
          Integer experienceResponse = null;
          Integer idResponse = null;

          if (data.has("skill")) {
            skillResponse = data.getString("skill");
          } else {
          }

          if (data.has("id")) {
            idResponse = data.getInt("id");
          }

          if (data.has("experience")) {
            experienceResponse = data.getInt("experience");
          } else {
          }

          String status = response.getString("status");

          if (status.equals("SUCCESS")) {
            JFrame frame = new JFrame("Mensagem");
            JOptionPane.showMessageDialog(frame, "Atualizado com sucesso!");
          } else if(status.equals("SKILL_EXISTS")){
            JFrame frame = new JFrame("Mensagem");
            JOptionPane.showMessageDialog(frame, "Skill existe!");
          } else {
            JFrame frame = new JFrame("Mensagem");
            JOptionPane.showMessageDialog(frame, "Erro!");
          }
        } catch(IOException err) {
          err.printStackTrace();
        }
      }
    });
    btnAtualizar_1.setBounds(154, 233, 98, 27);
    add(btnAtualizar_1);

    JLabel experienceLabel = new JLabel("Experience");
    experienceLabel.setBounds(45, 193, 60, 17);
    add(experienceLabel);

    JButton refreshBtn = new JButton("Refresh");
    refreshBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        limparTable(); 

        JSONObject request = new JSONObject();
        String token = Client.getInstance().getToken();
        buildJsonLookup(request, token);
        try {
          JSONObject response = Client.getInstance().sendRequest(request);
          JSONObject data = response.getJSONObject("data");


          if (response.getString("status").equals("SUCCESS")) {
            int size = data.getInt("skillset_size");
            JSONArray skillset = data.getJSONArray("skillset");

            for (int i = 0; i < size; i++) {
              JSONObject skillObject = skillset.getJSONObject(i);
              String skill = skillObject.getString("skill");
              int experience = skillObject.getInt("experience");
              int id = skillObject.getInt("id");
              popularTabelaCompetencia(skill, experience, id);
            }

          }

        } catch(IOException err) {
          err.printStackTrace();
        }
      }
    });
    refreshBtn.setBounds(264, 233, 98, 27);
    add(refreshBtn);

    spinner.setBounds(115, 191, 82, 22);
    add(spinner);

    JButton enviarBotao = new JButton("Criar");
    enviarBotao.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String skill = skillField.getText();
        Integer experience = (Integer) spinner.getValue();

        JSONObject request = new JSONObject();

        String token = Client.getInstance().getToken();
        request = buildCreateSkill(request, skill, experience, token);

        JSONObject response;
        try {
          response = Client.getInstance().sendRequest(request);
          JSONObject data = response.getJSONObject("data");

          String status = response.getString("status");

          if (status.equals("SUCCESS")) {
            JFrame frame = new JFrame("Mensagem");
            JOptionPane.showMessageDialog(frame, "Inserido com sucesso!");
          } else {
            JFrame frame = new JFrame("Mensagem");
            JOptionPane.showMessageDialog(frame, "Erro!");
          }

        } catch (IOException e1) {
          e1.printStackTrace();
        }
      }
    });
    enviarBotao.setBounds(45, 233, 101, 27);
    add(enviarBotao);

    idField = new JTextField();
    idField.setBounds(70, 78, 114, 24);
    add(idField);
    idField.setColumns(10);

    JButton btnNewButton = new JButton("Pesquisar");
    btnNewButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String idString = idField.getText();

        if (idString.equals("")) {
          JFrame frame = new JFrame("Mensagem");
          JOptionPane.showMessageDialog(frame, "Informe o id!");
          return;
        }

        Integer id = Integer.parseInt(idString);

        JSONObject request = new JSONObject();

        String token = Client.getInstance().getToken();
        request = buildLookup(request, id, token);

        JSONObject response;
        try {
          response = Client.getInstance().sendRequest(request);
          JSONObject data = response.getJSONObject("data");

          String status = response.getString("status");

          if (status.equals("SUCCESS")) {
            limparTable();
            String skill = data.getString("skill");
            Integer experience = data.getInt("experience");
            int idComp = data.getInt("id");
            popularTabelaCompetencia(skill, experience, idComp);
          } else {
            JFrame frame = new JFrame("Mensagem");
            JOptionPane.showMessageDialog(frame, "Id nao existe!");
          }

        } catch (IOException e1) {
          e1.printStackTrace();
        }
      }

    });
    btnNewButton.setBounds(191, 76, 109, 26);
    add(btnNewButton);

    JLabel lblId = new JLabel("ID");
    lblId.setBounds(45, 79, 58, 17);
    add(lblId);

    JSeparator separator = new JSeparator();
    separator.setBackground(new Color(97, 53, 131));
    separator.setBounds(0, 130, 649, 2);
    add(separator);
  }

  public JSONObject buildLookup(JSONObject json, Integer id, String token) {
    json.put("operation", "LOOKUP_SKILL");
    JSONObject data = new JSONObject();
    data.put("id", id);
    json.put("token", token);
    json.put("data", data);
    return json;
  }

  public JSONObject buildCreateSkill(JSONObject json, String skill, Integer experience, String token) {
    json.put("operation", "INCLUDE_SKILL");
    JSONObject data = new JSONObject();
    json.put("token", token);
    data.put("skill", skill);
    data.put("experience", experience);
    json.put("data", data);
    return json;
  }

  public JSONObject buildJsonDelete(JSONObject json, Integer id, String token) {
    json.put("operation", "DELETE_SKILL");
    JSONObject data = new JSONObject();
    data.put("id", id);
    json.put("token", token);
    json.put("data", data);
    return json;
  }

  public JSONObject buildJsonUpdate(JSONObject json, String token, JSONObject data) {
    json.put("operation", "UPDATE_SKILL");
    json.put("token", token);
    json.put("data", data);

    return json;
  }

  public void limparTela() {
    skillField.setText("");
    idField.setText("");
    limparTable();
  }

  public JSONObject buildJsonLookup(JSONObject json, String token) {
    json.put("operation", "LOOKUP_SKILLSET");
    JSONObject data = new JSONObject();
    json.put("token", token);
    json.put("data", data);
    return json;
  }

  public void limparTable() {
    DefaultTableModel modelo = (DefaultTableModel) table.getModel();
    modelo.setNumRows(0);
  }

  public void popularTabelaCompetencia(String skill, int experience, int id) {
    DefaultTableModel modelo = (DefaultTableModel) table.getModel();

    Object[] arr = new Object[3];
    arr[0] = id;
    arr[1] = skill;
    arr[2] = experience;

    modelo.addRow(arr);		
  }
}
