package view;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import javax.swing.JCheckBox;

public class FiltrarVagaView extends JPanel {

  private static final long serialVersionUID = 1L;
  private JTable table;
  private List<JCheckBox> checkBoxList; 
  private JTextField experienceField;

  public FiltrarVagaView() {
    initComponents();
  }

  public void initComponents() {
    setLayout(null);
    setBackground(SystemColor.control);

    checkBoxList = new ArrayList<>(); 
    
    JCheckBox eBox = new JCheckBox("AND");
    eBox.setBounds(165, 85, 112, 25);
    add(eBox);
    
    JCheckBox ouBox = new JCheckBox("OR");
    ouBox.setBounds(49, 85, 112, 25);
    add(ouBox);

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

    scrollPane.setViewportView(table);
    table.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
 
      }
    });
    scrollPane.setBounds(0, 348, 431, 222);
    add(scrollPane);
    
    JButton filtrarButton = new JButton("Filtrar");
    filtrarButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        List<String> selectedSkills = new ArrayList<>();
        for (JCheckBox checkBox : checkBoxList) {
          if (checkBox.isSelected()) {
            selectedSkills.add(checkBox.getText());
          }
        }
        
        JSONObject request = new JSONObject();
        JSONObject response = new JSONObject();
     
        String experience = experienceField.getText();
      
        String filter;
        
        if (eBox.isSelected()) {
        	filter = eBox.getText();
        } else {
        	filter = ouBox.getText();
        }
        
        String token = Client.getInstance().getToken();
        
        request = buildJsonSearch(request, selectedSkills, experience, filter, token);
        
        
        try {
            response = Client.getInstance().sendRequest(request);
            
            JSONObject data = response.getJSONObject("data");

            String status = (String) response.get("status");
            
            if (response.getString("status").equals("SUCCESS")) {
                String sizeString = data.getString("jobset_size");
                Integer size = Integer.parseInt(sizeString);
                JSONArray jobset = data.getJSONArray("jobset");
                
                limparTable();
                
                for (int i = 0; i < size; i++) {
                  JSONObject vagaObject = jobset.getJSONObject(i);
                  String skill = vagaObject.getString("skill");
                  String experienceVagaString = vagaObject.getString("experience");
                  Integer experienceVaga = Integer.parseInt(experienceVagaString);
                  String idString = vagaObject.getString("id");
                  Integer id = Integer.parseInt(idString);
                  popularTabelaVaga(skill, experienceVaga, id);
                }
              }

          } catch(IOException err) {
            err.printStackTrace();
          }
      }
    });
    filtrarButton.setBounds(49, 281, 101, 27);
    add(filtrarButton);
    
    JCheckBox javaBox = new JCheckBox("Java");
    javaBox.setBounds(49, 114, 112, 25);
    add(javaBox);
    checkBoxList.add(javaBox);

    JCheckBox rubyBox = new JCheckBox("Ruby");
    rubyBox.setBounds(49, 143, 112, 25);
    add(rubyBox);
    checkBoxList.add(rubyBox);
    
    experienceField = new JTextField();
    experienceField.setBounds(128, 248, 114, 21);
    add(experienceField);
    experienceField.setColumns(10);
    
    JLabel lblExperience = new JLabel("experience");
    lblExperience.setBounds(49, 249, 83, 19);
    add(lblExperience);
    
    JButton limparButton = new JButton("Limpar");
    limparButton.addActionListener(new ActionListener() {
    	public void actionPerformed(ActionEvent e) {
    		limparTable();
    	}
    });
    limparButton.setBounds(158, 281, 101, 27);
    add(limparButton);
    
    JCheckBox chckbxTypescript = new JCheckBox("TypeScript");
    chckbxTypescript.setBounds(49, 172, 112, 25);
    add(chckbxTypescript);
    checkBoxList.add(chckbxTypescript);
    
    JCheckBox rubyBox_1_1 = new JCheckBox("C");
    rubyBox_1_1.setBounds(165, 114, 112, 25);
    add(rubyBox_1_1);
    checkBoxList.add(rubyBox_1_1);
    
    JCheckBox rubyBox_1_2 = new JCheckBox("HTML");
    rubyBox_1_2.setBounds(165, 143, 112, 25);
    add(rubyBox_1_2);
    checkBoxList.add(rubyBox_1_2);

    JCheckBox rubyBox_1_3 = new JCheckBox("CSS");
    rubyBox_1_3.setBounds(165, 172, 112, 25);
    add(rubyBox_1_3);
    checkBoxList.add(rubyBox_1_3);

    JCheckBox rubyBox_1_4 = new JCheckBox("NodeJS");
    rubyBox_1_4.setBounds(281, 114, 112, 25);
    add(rubyBox_1_4);
    checkBoxList.add(rubyBox_1_4);

    JCheckBox rubyBox_1_5 = new JCheckBox("React");
    rubyBox_1_5.setBounds(281, 143, 112, 25);
    add(rubyBox_1_5);
    checkBoxList.add(rubyBox_1_5);

    JCheckBox rubyBox_1_6 = new JCheckBox("ReactNative");
    rubyBox_1_6.setBounds(281, 172, 112, 25);
    add(rubyBox_1_6);
    checkBoxList.add(rubyBox_1_6);

    JCheckBox chckbxJavascript = new JCheckBox("JavaScript");
    chckbxJavascript.setBounds(49, 201, 112, 25);
    add(chckbxJavascript);
    checkBoxList.add(chckbxJavascript);


  }

  public JSONObject buildJsonSearch(JSONObject json, List<String> langs, String experience, String filter
		  ,String token) {
	  json.put("operation", "SEARCH_JOB");
	  json.put("token", token);
	  JSONObject data = new JSONObject();

    int aux1 = 0, aux2 = 0;
	  
	  if (!langs.isEmpty()) {		  
		  data.put("skill", langs);
      aux1 = 1;
	  }
	  
	  if (!experience.equals("")) {
		  data.put("experience", experience);
      aux2 = 1;
	  }
	  
    if (aux1 == 1 && aux2 == 1) {
	    data.put("filter", filter);
    }

	  json.put("data", data);
	  return json;
  }

 
  public void limparTable() {
    DefaultTableModel modelo = (DefaultTableModel) table.getModel();
    modelo.setNumRows(0);
  }

  public void popularTabelaVaga(String skill, int experience, int id) {
    DefaultTableModel modelo = (DefaultTableModel) table.getModel();

    Object[] arr = new Object[3];
    arr[0] = id;
    arr[1] = skill;
    arr[2] = experience;

    modelo.addRow(arr);    
  }
}
