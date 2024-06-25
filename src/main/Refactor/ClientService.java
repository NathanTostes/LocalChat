import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public final class ClientService implements Runnable {
    private static int numberOfClients = 0;
    private String clientId = null;
    private Socket clientSocket = null;
    private DataInputStream clientInputStream = null;

    public ClientService(Socket clientSocket) {
        numberOfClients++;
        this.clientId = "User " + numberOfClients;
        this.clientSocket = clientSocket;
        try {
            clientInputStream = new DataInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            System.out.println("(Error) Connection stream not opened");
        }
        sendMessage("has joined");
    }

    @Override
    public void run() {
        String clientMessage = "";
        try {
            while ((clientMessage = clientInputStream.readUTF()) != null) {
                sendMessage(clientMessage);
            }
        } catch (IOException e) {
            closeConnection();
        }
    }

    private void sendMessage(String clientMessage) {
        if (clientMessage.isBlank()) {
            return;
        }
        clientMessage = formatMessage(clientMessage);
        System.out.println(clientMessage);
        DataOutputStream clientOutputStream = null;
        for (Socket client : NewServer.clientList) {
            if(this.clientSocket == client) {
                continue;
            }
            try {
                clientOutputStream = new DataOutputStream(client.getOutputStream());
                clientOutputStream.writeUTF(clientMessage);
            } catch (IOException e) {
                System.out.println("(ERROR) Message can not be sent for all users");
            }
        }
    }

    private String formatMessage(String clientMessage) {
        return clientId + ": " + clientMessage;
    }

    private void closeConnection() {
        try {
            sendMessage("has left");
            clientInputStream.close();
            clientSocket.close();
        } catch (IOException e) {
            System.out.println("(ERROR) Connection with " + clientId + " not close correctly");
        }
    }
}
