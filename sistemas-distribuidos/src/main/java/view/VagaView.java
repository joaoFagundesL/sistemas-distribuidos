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
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class VagaView extends JPanel {

  private static final long serialVersionUID = 1L;
  private JTable table;
  private JTextField idField;

  public VagaView() {
    initComponents();
  }

  public void initComponents() {
    setLayout(null);
    setBackground(SystemColor.control);

    JSpinner spinner = new JSpinner();
    JComboBox newSkillBox= new JComboBox();

    JScrollPane scrollPane = new JScrollPane();
    table = new JTable();
    table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
    table.setModel(new DefaultTableModel(
      new Object[][] {
      },
      new String[] {
        "Id", "Skill", "Experience", "Available", "Searchable"
      }
    ));


    scrollPane.setViewportView(table);
    table.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
 
      }
    });
    scrollPane.setBounds(0, 348, 431, 222);
    add(scrollPane);

    JLabel skillLabel = new JLabel("Skill");
    skillLabel.setBounds(45, 164, 60, 17);
    add(skillLabel);
    
    JComboBox comboBox = new JComboBox();
    comboBox.setModel(new DefaultComboBoxModel(new String[] {"Java", "Ruby", "React", "ReactNative", "C", "HTML", "CSS", "JavaScript", "TypeScript", "NodeJS"}));
    comboBox.setBounds(113, 159, 94, 22);
    add(comboBox);
    
    JComboBox availableBox = new JComboBox();
    availableBox.setModel(new DefaultComboBoxModel(new String[] {"YES", "NO"}));
    availableBox.setBounds(306, 188, 94, 22);
    add(availableBox);
    
    JComboBox searchableBox = new JComboBox();
    searchableBox.setModel(new DefaultComboBoxModel(new String[] {"YES", "NO"}));
    searchableBox.setBounds(306, 216, 94, 22);
    add(searchableBox);

    JButton btnRemover_1 = new JButton("Remover");
    btnRemover_1.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        JSONObject request = new JSONObject();
        String token = Client.getInstance().getToken();
        
        String idString = idField.getText();
        
        if (idString.equals("")) {
        	JFrame frame = new JFrame("JOptionPane exemplo");
            JOptionPane.showMessageDialog(frame, "ID inválido");
            return;
        }
        
        Integer id = Integer.parseInt(idString);
        
        if (id < 0 || id == null) {
          JFrame frame = new JFrame("JOptionPane exemplo");
          JOptionPane.showMessageDialog(frame, "ID inválido");
          return;
        }
        
        buildJsonDelete(request, idString, token);
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
    btnLimpar_1.setBounds(45, 294, 98, 27);
    add(btnLimpar_1);

    JButton btnAtualizar_1 = new JButton("Atualizar");
    btnAtualizar_1.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String skill = (String) newSkillBox.getSelectedItem();

        String idString = idField.getText();

        if (idString.equals("")) {
          JFrame frame = new JFrame("Mensagem");
          JOptionPane.showMessageDialog(frame, "ID vazio!");
          return;
        }

        Integer jobId = Integer.parseInt(idString);
                
        Integer experience = (Integer) spinner.getValue();
        String experienceString = experience.toString();

        JSONObject dataRequest = new JSONObject();  

        if (jobId < 0 || jobId == null) {
          JFrame frame = new JFrame("Mensagem");
          JOptionPane.showMessageDialog(frame, "Invalid id!");
          return;
        }

        dataRequest.put("id", idString);

        if(!skill.equals("")) {
          dataRequest.put("skill", skill);
        }

        if (experience != null) {
          dataRequest.put("experience", experienceString);
        }

        JSONObject request = new JSONObject();
        String token = Client.getInstance().getToken();
        buildJsonUpdate(request, token, dataRequest);

        try {
          DefaultTableModel modelo = (DefaultTableModel) table.getModel();
          JSONObject response = Client.getInstance().sendRequest(request);
          JSONObject data = response.getJSONObject("data");

          String skillResponse = "";
          String experienceResponse = "";
          String idResponse = "";

          if (data.has("skill")) {
            skillResponse = data.getString("skill");
          } else {
          }

          if (data.has("experience")) {
            experienceResponse = data.getString("experience");
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
    btnAtualizar_1.setBounds(154, 255, 98, 27);
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
            String sizeString = data.getString("jobset_size");
            Integer size = Integer.parseInt(sizeString);
            JSONArray jobset = data.getJSONArray("jobset");

            for (int i = 0; i < size; i++) {
              JSONObject skillObject = jobset.getJSONObject(i);
              String skill = skillObject.getString("skill");
              String experienceString = skillObject.getString("experience");
              Integer experience = Integer.parseInt(experienceString);
              String idString = skillObject.getString("id");
              Integer id = Integer.parseInt(idString);
              popularTabelaCompetencia(skill, experience, id, skillObject.getString("available"), skillObject.getString("searchable"));
            }

          }

        } catch(IOException err) {
          err.printStackTrace();
        }
      }
    });
    refreshBtn.setBounds(155, 294, 98, 27);
    add(refreshBtn);

    spinner.setBounds(115, 191, 82, 22);
    add(spinner);

    JButton enviarBotao = new JButton("Criar");
    enviarBotao.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String skill = (String) comboBox.getSelectedItem();
        Integer experience = (Integer) spinner.getValue();
        
        String experienceString = experience.toString();
        String available = (String) availableBox.getSelectedItem();
        String searchable = (String) searchableBox.getSelectedItem();

        JSONObject request = new JSONObject();
        

        String token = Client.getInstance().getToken();
        request = buildIncludeJob(request, skill, experienceString, token, available, searchable);

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
    enviarBotao.setBounds(45, 255, 101, 27);
    add(enviarBotao);

    JButton btnNewButton = new JButton("Pesquisar");
    btnNewButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String idString = idField.getText();
        Integer id = Integer.parseInt(idString);

        if (id < 0 || id == null) {
          JFrame frame = new JFrame("Mensagem");
          JOptionPane.showMessageDialog(frame, "Invalido!");
          return;
        }

        JSONObject request = new JSONObject();

        String token = Client.getInstance().getToken();
        request = buildLookup(request, token, idString);

        JSONObject response;
        try {
          response = Client.getInstance().sendRequest(request);
          JSONObject data = response.getJSONObject("data");

          String status = response.getString("status");

          if (status.equals("SUCCESS")) {
            limparTable();
            String skillUpdated = data.getString("skill");
            String experienceString = data.getString("experience");
            Integer experience = Integer.parseInt(experienceString);
            String idCompString = data.getString("id");
            Integer idComp = Integer.parseInt(idCompString);
            popularTabelaCompetencia(skillUpdated, experience, idComp, data.getString("available"), data.getString("searchable"));
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

    JLabel id = new JLabel("id");
    id.setBounds(31, 81, 58, 17);
    add(id);

    JSeparator separator = new JSeparator();
    separator.setBackground(new Color(97, 53, 131));
    separator.setBounds(0, 130, 649, 2);
    add(separator);

    newSkillBox.setModel(new DefaultComboBoxModel(new String[] {"Java", "Ruby", "React", "ReactNative", "C", "HTML", "CSS", "JavaScript", "TypeScript", "NodeJS"}));
    newSkillBox.setBounds(306, 161, 94, 22);
    add(newSkillBox);

    JLabel lblNewSkill = new JLabel("Update to");
    lblNewSkill.setBounds(235, 164, 58, 17);
    add(lblNewSkill);

    idField = new JTextField();
    idField.setBounds(57, 79, 114, 21);
    add(idField);
    idField.setColumns(10);
    
    
    
    JLabel lblSearchable = new JLabel("Searchable");
    lblSearchable.setBounds(235, 193, 65, 17);
    add(lblSearchable);
    
    JLabel lblAvailable = new JLabel("Available");
    lblAvailable.setBounds(235, 219, 58, 17);
    add(lblAvailable);

  }

  public JSONObject buildLookup(JSONObject json, String token, String id) {
    json.put("operation", "LOOKUP_JOB");
    JSONObject data = new JSONObject();
    data.put("id",id);
    json.put("token", token);
    json.put("data", data);
    return json;
  }

  public JSONObject buildIncludeJob(JSONObject json, String skill, String experience, String token,
		                           String available, String searchable) {
    json.put("operation", "INCLUDE_JOB");
    JSONObject data = new JSONObject();
    json.put("token", token);
    data.put("skill", skill);
    data.put("available", available);
    data.put("searchable", searchable);
    data.put("experience", experience);
    json.put("data", data);
    return json;
  }

  public JSONObject buildJsonDelete(JSONObject json, String id, String token) {
    json.put("operation", "DELETE_JOB");
    JSONObject data = new JSONObject();
    data.put("id", id);
    json.put("token", token);
    json.put("data", data);
    return json;
  }

  public JSONObject buildJsonUpdate(JSONObject json, String token, JSONObject data) {
    json.put("operation", "UPDATE_JOB");
    json.put("token", token);
    json.put("data", data);

    return json;
  }

  public void limparTela() {
    limparTable();
  }

  public JSONObject buildJsonLookup(JSONObject json, String token) {
    json.put("operation", "LOOKUP_JOBSET");
    JSONObject data = new JSONObject();
    json.put("token", token);
    json.put("data", data);
    return json;
  }

  public void limparTable() {
    DefaultTableModel modelo = (DefaultTableModel) table.getModel();
    modelo.setNumRows(0);
  }

  public void popularTabelaCompetencia(String skill, int experience, int id, String available,
		                               String searchable) {
    DefaultTableModel modelo = (DefaultTableModel) table.getModel();

    Object[] arr = new Object[5];
    arr[0] = id;
    arr[1] = skill;
    arr[2] = experience;
    arr[3] = available;
    arr[4] = searchable;

    modelo.addRow(arr);		
  }
}
