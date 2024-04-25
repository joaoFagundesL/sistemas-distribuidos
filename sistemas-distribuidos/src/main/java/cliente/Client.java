package cliente;

import org.json.JSONObject;

import view.LoginView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {

  private static final String SERVER_IP = "127.0.0.1";
  private static final int SERVER_PORT = 12345;
  private Socket socket ;
  private ObjectOutputStream outputStream ;
  private ObjectInputStream inputStream ;

  // private static Client instance = null;

  private Client() {
    try {
      socket = new Socket(SERVER_IP, SERVER_PORT);
      outputStream = new ObjectOutputStream(socket.getOutputStream());
      inputStream = new ObjectInputStream(socket.getInputStream());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    Client client = new Client();
    LoginView loginView = new LoginView(client);

    // JSONObject loginRequest = createLoginRequest("joao@gmail.com", "joao");
    // sendRequest(outputStream, loginRequest);
    //
    // processResponse(inputStream);
    //
    // JSONObject logoutRequest = createLogoutRequest();
    // sendRequest(outputStream, logoutRequest);
    //
    // processResponse(inputStream);
    //
  }

  // public static Client getInstance() {
  //   if (instance == null) {
  //     instance = new Client();
  //   }
  //   return instance;
  // }
  //
  public JSONObject sendRequest(JSONObject request) throws IOException {
    JSONObject serverResponse = new JSONObject();
    outputStream.writeObject(request.toString());
    outputStream.flush();
    try {
      serverResponse = processResponse();
    } catch(IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }

    return serverResponse;
  }

  public JSONObject processResponse() throws IOException, ClassNotFoundException {
    String response = (String) inputStream.readObject();
    JSONObject jsonResponse = new JSONObject(response);
    return jsonResponse;
  }
}
