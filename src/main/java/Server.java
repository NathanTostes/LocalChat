import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static ServerSocket serverSocket = null;
    private static ExecutorService clientExecutorService = Executors.newCachedThreadPool();
    public static ArrayList<Socket> clientList = new ArrayList<>();

    public static void main(String[] args) {
        ServerSocket serverSocket = openServerSocket();
        listenConnections(serverSocket);
        closeAllConnections();
    }

    public static ServerSocket openServerSocket() {
        try {
            serverSocket = new ServerSocket(9999);
            System.out.println("Server has oppened");
            return serverSocket;
        } catch (IOException e) {
            System.out.println("(ERROR) Application port already open");
            return null;
        }
    }

    private static void listenConnections(ServerSocket serverSocket) {
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                clientList.add(clientSocket);
                clientExecutorService.execute(new ClientService(clientSocket));
            } catch (IOException e) {
                System.out.println("(ERROR) Connection fail");
            }
        }
    }

    private static void closeAllConnections() {
        clientExecutorService.close();
        for (Socket clientSocket : clientList) {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.out.println("(ERROR) Connections not close correctly");
            }
        }
    }
}
