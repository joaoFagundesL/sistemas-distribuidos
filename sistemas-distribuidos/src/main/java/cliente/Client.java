package cliente;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.json.JSONObject;

public class Client {
    public static void main(String[] args) {
        final String SERVER_IP = "127.0.0.1";
        final int SERVER_PORT = 12345;

        Socket socket = null;
        ObjectOutputStream outputStream = null;
        ObjectInputStream inputStream = null;

        try {
            // Establish connection to the server
            socket = new Socket(SERVER_IP, SERVER_PORT);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());

            // Construct and send JSON request
            JSONObject request = new JSONObject();
            request.put("operation", "LOGIN_CANDIDATE");
            JSONObject data = new JSONObject();
            data.put("email", "joao@gmail.com");
            data.put("password", "joao");
            request.put("data", data);

            outputStream.writeObject(request.toString());
            outputStream.flush();

            // Read and process server response (if applicable)
            Object response = inputStream.readObject();
            if (response != null) {
                System.out.println("Server response: " + response);
            }

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
}
