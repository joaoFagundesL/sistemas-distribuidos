package cliente;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Set;

public class Client {
    public static void main(String[] args) {
        final String SERVER_IP = "127.0.0.1";
        final int SERVER_PORT = 12345;

        try {
            Socket socket = new Socket(SERVER_IP, SERVER_PORT);
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

            outputStream.writeObject("Olá, servidor!");
            outputStream.flush();
            
            Set<ClientInfo> connectedClients = (Set<ClientInfo>) inputStream.readObject();
            System.out.println(connectedClients.size());
            
            /* apenas para mostrar que a lista de usuarios conectados funciona, no servidor eu faço
             * um write no Object passando o set
             */
          
            for (ClientInfo client : connectedClients) {
                System.out.println("(Recebido do Servidor) IP: " + client.getIpAddress() + ", Porta: " + client.getPort());
            }

            outputStream.close();
            inputStream.close();
            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
