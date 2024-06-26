package view;
import java.awt.SystemColor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import org.json.JSONArray;
import org.json.JSONObject;
import cliente.Client;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GetCompanyView extends JPanel {
	
  private static final long serialVersionUID = 1L;
  private JTable table;
  private JSONObject lastResponse; // Para armazenar o último JSON response

  public GetCompanyView() {
    initComponents();
  }

  public void initComponents() {
    setLayout(null);
    setBackground(SystemColor.control);

    JScrollPane scrollPane = new JScrollPane();
    table = new JTable();
    table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
    table.setModel(new DefaultTableModel(
      new Object[][] {},
      new String[] { "Name", "Industry", "Email", "Description" }
    ));

    scrollPane.setViewportView(table);
    scrollPane.setBounds(12, 144, 431, 222);
    add(scrollPane);
  }

  public void handleGetCompany() {
    limparTable();
    JSONObject request = new JSONObject();
    String token = Client.getInstance().getToken();
    buildJsonGet(request, token);
    
    try {
      JSONObject response = Client.getInstance().sendRequest(request);
      String status = response.getString("status");

      
      if (status.equals("SUCCESS")) {
        lastResponse = response; 
        JSONObject data = response.getJSONObject("data");
        String sizeString = data.getString("company_size");
        int size = Integer.parseInt(sizeString);
        JSONArray companySet = data.getJSONArray("company");
        
        for (int i = 0; i < size; i++) {
          JSONObject companyObject = companySet.getJSONObject(i);
          String name = companyObject.getString("name");
          String description = companyObject.getString("description");
          String email = companyObject.getString("email");
          String industry = companyObject.getString("industry");
          popularTabelaEmpresa(name, industry, email, description);
        }
        
      } else {
        JFrame frame = new JFrame("Error");
        JOptionPane.showMessageDialog(frame, "Error fetching company data.");
      }
      
    } catch (IOException | org.json.JSONException e) {
      e.printStackTrace();
      JFrame frame = new JFrame("Error");
      JOptionPane.showMessageDialog(frame, "Error: " + e.getMessage());
    }
  }

  public void limparTable() {
    DefaultTableModel modelo = (DefaultTableModel) table.getModel();
    modelo.setRowCount(0);
  }

  public JSONObject buildJsonGet(JSONObject json, String token) {
    json.put("operation", "GET_COMPANY");
    json.put("token", token);
    json.put("data", new JSONObject());
    return json;
  }

  public void popularTabelaEmpresa(String name, String industry, String email, String description) {
    DefaultTableModel modelo = (DefaultTableModel) table.getModel();
    Object[] arr = new Object[4];
    System.out.println("inside poupular tabela " + name + industry + email + description);
    
    arr[0] = name;
    arr[1] = industry;
    arr[2] = email;
    arr[3] = description;
    modelo.addRow(arr);
  }
}
