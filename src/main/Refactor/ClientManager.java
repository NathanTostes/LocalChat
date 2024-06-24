import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;

public final class ClientManager implements Runnable {
    private int numberOfClients = 0;
    private String clientId = null;
    private Socket clientSocket = null;


    public ClientManager(Socket clientSocket) {
        numberOfClients++;
        this.clientId = "User " + numberOfClients;
        this.clientSocket = clientSocket;
        System.out.println("New connection with " + clientId);
    }

    public String getClientId() {
        return this.clientId;
    }

    @Override
    public void run() {
        try (DataInputStream clientInputStream = (DataInputStream) clientSocket.getInputStream()) {
            String clientMessage = "";
            while ((clientMessage = clientInputStream.readUTF()) != null) {
                sendForAllOtherClients(clientMessage);
            }
        } catch (IOException e) {
            System.out.println("(ERROR) Message from " + clientId + " can not be read");
        }
        closeConnection();
    }

    private void sendForAllOtherClients(String clientMessage) {
        clientMessage = clientId + ": " + clientMessage;
        System.out.println(clientMessage);
        for (Socket client : NewServer.clientList) {
            if (this.clientSocket == client) {
                continue;
            }
            try (DataOutputStream clientOutputStream = (DataOutputStream) client.getOutputStream()) {
                clientOutputStream.writeUTF(clientMessage);
            } catch (IOException e) {
                System.out.println("(ERROR) Message not send for all users");
            }
        }
    }

    private void closeConnection() {
        try {
            clientSocket.close();
            sendForAllOtherClients(clientId + " has left");
        } catch (IOException e) {
            System.out.println("(ERROR) Connection with " + clientId + " not close correctly");
        }
    }
}
