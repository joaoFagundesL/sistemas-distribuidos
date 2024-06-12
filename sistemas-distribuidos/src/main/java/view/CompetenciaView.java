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

public class CompetenciaView extends JPanel {

  private static final long serialVersionUID = 1L;
  private JTable table;

  public CompetenciaView() {
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
       "Skill", "Experience" 
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
    
    JComboBox comboBox_1 = new JComboBox();
    comboBox_1.setModel(new DefaultComboBoxModel(new String[] {"Java", "Ruby", "React", "ReactNative", "C", "HTML", "CSS", "JavaScript", "TypeScript", "NodeJS"}));
    comboBox_1.setBounds(69, 76, 94, 22);
    add(comboBox_1);

    JButton btnRemover_1 = new JButton("Remover");
    btnRemover_1.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        JSONObject request = new JSONObject();
        String token = Client.getInstance().getToken();
        
        String skill = (String) comboBox_1.getSelectedItem();
        
        if (skill.equals("")) {
          JFrame frame = new JFrame("JOptionPane exemplo");
          JOptionPane.showMessageDialog(frame, "Informe a skil!");
          return;
        }
        
        buildJsonDelete(request, skill, token);
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
        String skill = (String) comboBox.getSelectedItem();
        String newSkill = (String) newSkillBox.getSelectedItem();
                
        Integer experience = (Integer) spinner.getValue();
        
        String experienceString = experience.toString();

        JSONObject dataRequest = new JSONObject();  

        if(!skill.equals("")) {
          dataRequest.put("skill", skill);
        }

        if(!newSkill.equals("")) {
            dataRequest.put("newSkill", newSkill);
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
          String newSkillResponse = "";
          Integer experienceResponse = null;
          Integer idResponse = null;
          String experienceResponseString = "";
          String  idResponseString = "";

          if (data.has("skill")) {
            skillResponse = data.getString("skill");
          } else {
          }

          if (data.has("experience")) {
            experienceResponseString = data.getString("experience");
            experienceResponse = Integer.parseInt(experienceResponseString);
          } else {
          }
          
          if (data.has("newSkill")) {
              newSkillResponse = data.getString("newSkill");
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
    btnAtualizar_1.setBounds(158, 233, 98, 27);
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
            String sizeString = data.getString("skillset_size");
            Integer size = Integer.parseInt(sizeString);

            JSONArray skillset = data.getJSONArray("skillset");

            for (int i = 0; i < size; i++) {
              JSONObject skillObject = skillset.getJSONObject(i);
              String skill = skillObject.getString("skill");
              String experienceString = skillObject.getString("experience");
              Integer experience = Integer.parseInt(experienceString);
              // String idString = skillObject.getString("id");
              // Integer id = Integer.parseInt(idString);
              popularTabelaCompetencia(skill, experience);
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
        String skill = (String) comboBox.getSelectedItem();
        Integer experience = (Integer) spinner.getValue();
        
        String experienceString = experience.toString();

        JSONObject request = new JSONObject();

        String token = Client.getInstance().getToken();
        request = buildCreateSkill(request, skill, experienceString, token);

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

    JButton btnNewButton = new JButton("Pesquisar");
    btnNewButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
          String skill = (String) comboBox_1.getSelectedItem();

        if (skill.equals("")) {
          JFrame frame = new JFrame("Mensagem");
          JOptionPane.showMessageDialog(frame, "Informe a skill!");
          return;
        }

        JSONObject request = new JSONObject();

        String token = Client.getInstance().getToken();
        request = buildLookup(request, skill, token);

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
            // String idCompString = data.getString("id");
            // Integer idComp = Integer.parseInt(idCompString);

            popularTabelaCompetencia(skillUpdated, experience);
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

    JLabel skill = new JLabel("Skill");
    skill.setBounds(31, 81, 58, 17);
    add(skill);

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
    
  }

  public JSONObject buildLookup(JSONObject json, String skill, String token) {
    json.put("operation", "LOOKUP_SKILL");
    JSONObject data = new JSONObject();
    data.put("skill", skill);
    json.put("token", token);
    json.put("data", data);
    return json;
  }

  public JSONObject buildCreateSkill(JSONObject json, String skill, String experience, String token) {
    json.put("operation", "INCLUDE_SKILL");
    JSONObject data = new JSONObject();
    json.put("token", token);
    data.put("skill", skill);
    data.put("experience", experience);
    json.put("data", data);
    return json;
  }

  public JSONObject buildJsonDelete(JSONObject json, String skill, String token) {
    json.put("operation", "DELETE_SKILL");
    JSONObject data = new JSONObject();
    data.put("skill", skill);
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

  public void popularTabelaCompetencia(String skill, int experience) {
    DefaultTableModel modelo = (DefaultTableModel) table.getModel();

    Object[] arr = new Object[2];
    arr[0] = skill;
    arr[1] = experience;

    modelo.addRow(arr);		
  }
}
