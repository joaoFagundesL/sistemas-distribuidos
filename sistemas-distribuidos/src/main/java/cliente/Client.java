package cliente;

import org.json.JSONObject;
import view.LoginView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

  private static final String SERVER_IP = "localhost";
  private static final int SERVER_PORT = 21234;
  private Socket socket;
  private PrintWriter outputStream;
  private BufferedReader inputStream;
  private String token;

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  private static Client instance = null;

  private Client() {
    try {
      socket = new Socket(SERVER_IP, SERVER_PORT);
      outputStream = new PrintWriter(socket.getOutputStream(), true);
      inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    Client client = Client.getInstance();
    LoginView loginView = new LoginView(); 
  }

  public JSONObject sendRequest(JSONObject request) throws IOException {
    JSONObject serverResponse = new JSONObject();
    String jsonRequest = request.toString();

    outputStream.println(jsonRequest); 

    try {
      serverResponse = processResponse(); 
    } catch (IOException e) {
      e.printStackTrace();
    }

    return serverResponse;
  }

  public JSONObject processResponse() throws IOException {
    String response = inputStream.readLine();
    JSONObject jsonResponse = new JSONObject(response);

    String status = jsonResponse.getString("status");
    JSONObject dataJson = jsonResponse.getJSONObject("data");

    if (dataJson.has("token")) {
      String token = dataJson.getString("token");
      if (status.equals("SUCCESS")) {
        setToken(token);
      }
    }

    System.out.println("Received from server: " + response);
    return jsonResponse;
  }

  public static Client getInstance() {
    if (instance == null) {
      instance = new Client();
    }
    return instance;
  }
}
