import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static ServerSocket serverSocket = null;
    private static ExecutorService clientExecutorService = Executors.newCachedThreadPool();
    public static ArrayList<Socket> clientList = new ArrayList<>();

    public static void main(String[] args) {
        readWantedPort();
        listenConnections(serverSocket);
        closeAllConnections();
    }

    public static void readWantedPort() {
        System.out.println("Enter the port on which the server will open: ");
        String portString = new Scanner(System.in).nextLine();
        openServerSocketAtPort(portString);
    }

    public static void openServerSocketAtPort(String port) {
        try {
            serverSocket = new ServerSocket(Integer.parseInt(port));
            System.out.println("Server has oppened");
        } catch (IOException e) {
            System.out.println("Application port already open, try again");
            readWantedPort();
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid port number, try again");
            readWantedPort();
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
