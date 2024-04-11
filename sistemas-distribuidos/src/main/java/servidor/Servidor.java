package servidor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import org.json.JSONObject;

import cliente.ClientInfo;
import view.LoginView;

public class Servidor extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private DefaultListModel<String> listModel;
    private JList<String> list;
    
    /* com esse set tenho todas informacoes do cliente que eu preciso,
     * caso precise adicionar algo mais é so mexer em ClientInfo (nome, por exemplo) */
    
    private Set<ClientInfo> connectedClients;
    private JTextField portaField;
    JTextArea textArea;

    public Servidor() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 507, 369);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        listModel = new DefaultListModel<>();
        list = new JList<>(listModel);
        list.setBounds(310, 34, 155, 272);
        contentPane.add(list);
        
        JLabel lblNewLabel = new JLabel("Usuários");
        lblNewLabel.setBounds(358, 11, 58, 14);
        contentPane.add(lblNewLabel);
        
        JLabel portaLabel = new JLabel("Porta");
        portaLabel.setBounds(20, 53, 46, 14);
        contentPane.add(portaLabel);
        
        portaField = new JTextField();
        portaField.setBounds(56, 50, 86, 20);
        contentPane.add(portaField);
        portaField.setColumns(10);
        
        JButton iniciarBtn = new JButton("Iniciar");
        iniciarBtn.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		String port = portaField.getText();
        		if (!port.isEmpty()) {
        			startServer(Integer.parseInt(port));
        			iniciarBtn.setEnabled(false);
        			
        			StringBuilder content = new StringBuilder();
        	    content.append("Servidor iniciado na porta 12345\n");
        	       
              textArea.setText(content.toString());
        		}
        	}
        });
        iniciarBtn.setBounds(149, 49, 78, 23);
        contentPane.add(iniciarBtn);
        
        textArea = new JTextArea();
        textArea.setEditable(false); 
        textArea.setBounds(20, 108, 280, 198);
        contentPane.add(textArea);

        connectedClients = new HashSet<>();
    }

    private void startServer(int port) {
        new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(port);
                System.out.println("Servidor iniciado na porta " + port + "\n");

                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                    ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());

                    String clientMessage = (String) inputStream.readObject();
                    handleOperation(clientMessage);
                    
                    ClientInfo client = new ClientInfo(clientSocket.getInetAddress().getHostAddress(),
                            clientSocket.getPort());
                    
                    /* text area */
                    StringBuilder content = new StringBuilder();

        	        content.append(">> " + client.getIpAddress() + " " + client.getPort() + 
                    		": " + "\n");
        	        
        	        textArea.append(content.toString());
                    textArea.setCaretPosition(textArea.getDocument().getLength()); 
                    
                    /* adiciona cliente no hash */
                    connectedClients.add(client);
                    updateConnectedUsersList();
                    
                    /* escrevendo o set no object, caso eu quisesse escrever mais coisas é só criar
                     * uma classe, reuno as informacoes, chamo o construtor e depois escrevo out.writeObject(dataWrapper);
                     *  class DataWrapper implements Serializable {
						    private Set<String> stringSet;
						    private String message;
						    public DataWrapper(Set<String> stringSet, String message) {
						        this.stringSet = stringSet;
						        this.message = message; }
						    public Set<String> getStringSet() { return stringSet;}
						    public String getMessage() {return message;}
						}  
                     *  */
                    outputStream.writeObject(connectedClients);
                    outputStream.flush();

                    outputStream.close();
                    inputStream.close();
                    clientSocket.close();
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void handleOperation(String clientMessage) {
        JSONObject jsonMessage = new JSONObject(clientMessage);
        String operation = jsonMessage.getString("operation");
        
        System.out.println(operation);
        if (operation.equals("LOGIN_CANDIDATE")) {
            new LoginView(jsonMessage);
        } 
    }

    private void updateConnectedUsersList() {
        SwingUtilities.invokeLater(() -> {
            listModel.clear();

            for (ClientInfo client : connectedClients) {
                String info = "IP: " + client.getIpAddress() + ", Porta: " + client.getPort();
                listModel.addElement(info);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                Servidor frame = new Servidor();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
