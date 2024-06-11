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
import javax.swing.JSeparator;
import java.awt.Color;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import java.awt.Choice;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Label;

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
    
    JCheckBox eBox = new JCheckBox("E");
    eBox.setBounds(240, 114, 112, 25);
    add(eBox);
    
    JCheckBox ouBox = new JCheckBox("OU");
    ouBox.setBounds(240, 143, 112, 25);
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

                for (int i = 0; i < size; i++) {
                  JSONObject vagaObject = jobset.getJSONObject(i);
                  String skill = vagaObject.getString("skill");
                  String experienceVagaString = vagaObject.getString("experience");
                  Integer experienceVaga = Integer.parseInt(experienceVagaString);
                  String idString = vagaObject.getString("id");
                  Integer id = Integer.parseInt(idString);
                  limparTable();
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
    checkBoxList.add(javaBox); // Adiciona à lista

    JCheckBox rubyBox = new JCheckBox("Ruby");
    rubyBox.setBounds(49, 143, 112, 25);
    add(rubyBox);
    checkBoxList.add(rubyBox); // Adiciona à lista
    
    experienceField = new JTextField();
    experienceField.setBounds(113, 213, 114, 21);
    add(experienceField);
    experienceField.setColumns(10);
    
    JLabel lblExperience = new JLabel("experience");
    lblExperience.setBounds(25, 214, 83, 19);
    add(lblExperience);
    
    JButton limparButton = new JButton("Limpar");
    limparButton.addActionListener(new ActionListener() {
    	public void actionPerformed(ActionEvent e) {
    		limparTable();
    	}
    });
    limparButton.setBounds(158, 281, 101, 27);
    add(limparButton);

  }

  public JSONObject buildJsonSearch(JSONObject json, List<String> langs, String experience, String filter
		  ,String token) {
	  json.put("operation", "SEARCH_JOB");
	  json.put("token", token);
	  JSONObject data = new JSONObject();
	  data.put("skill", langs);
	  
	  if (!experience.equals("")) {
		  data.put("experience", experience);
	  }
	  
	  data.put("filter", filter);
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
