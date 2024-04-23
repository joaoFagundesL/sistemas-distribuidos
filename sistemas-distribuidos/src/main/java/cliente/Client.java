package cliente;

import org.json.JSONObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        Socket socket = null;
        ObjectOutputStream outputStream = null;
        ObjectInputStream inputStream = null;

        try {
            socket = new Socket(SERVER_IP, SERVER_PORT);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());

            JSONObject loginRequest = createLoginRequest("joao@gmail.com", "joao");
            sendRequest(outputStream, loginRequest);

            processResponse(inputStream);

            JSONObject logoutRequest = createLogoutRequest();
            sendRequest(outputStream, logoutRequest);

            processResponse(inputStream);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) outputStream.close();
                if (inputStream != null) inputStream.close();
                if (socket != null) socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static JSONObject createLoginRequest(String email, String password) {
        JSONObject request = new JSONObject();
        request.put("operation", "LOGIN_CANDIDATE");
        JSONObject data = new JSONObject();
        data.put("email", email);
        data.put("password", password);
        request.put("data", data);
        return request;
    }

    private static JSONObject createLogoutRequest() {
        JSONObject request = new JSONObject();
        request.put("operation", "LOGOUT_CANDIDATE");
        JSONObject data = new JSONObject();
        data.put("token", "token_aleatorio");
        request.put("data", data);
        return request;
    }

    private static void sendRequest(ObjectOutputStream outputStream, JSONObject request) throws IOException {
        outputStream.writeObject(request.toString());
        outputStream.flush();
    }

    private static void processResponse(ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
        String response = (String) inputStream.readObject();
        JSONObject jsonResponse = new JSONObject(response);
        System.out.println("Server response: " + jsonResponse);
    }
}
