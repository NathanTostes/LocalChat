import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NewServer {
    private int numberOfClients = 0;
    private static ExecutorService clientManager = Executors.newCachedThreadPool();
    public static LinkedList<Socket> clientList = new LinkedList<>();

    public static void main(String[] args) {
        ServerSocket serverSocket = openServerSocket();
        listenConnections(serverSocket);
        closeAllConnections();
    }

    public static ServerSocket openServerSocket() {
        try {
            ServerSocket serverSocket = new ServerSocket(6900);
            System.out.println("Server has oppened");
            return serverSocket;
        } catch (IOException e) {
            System.out.println("(ERROR) Application port already open");
            return null;
        }
    }

    private static void listenConnections(ServerSocket serverSocket) {
        while (true) {
            try (Socket clientSocket = serverSocket.accept()) {
                clientList.add(clientSocket);
                clientManager.execute(new ClientManager(clientSocket));
            } catch (IOException e) {
                System.out.println("(ERROR) Connection fail");
            }
        }
    }

    private static void closeAllConnections() {
        clientManager.close();
        for (Socket clientSocket : clientList) {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.out.println("(ERROR) Connections not close correctly");
            }
        }
    }
}
