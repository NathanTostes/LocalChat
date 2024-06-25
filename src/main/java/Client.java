import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {
    public static Socket serverSocket = null;
    private static DataInputStream serverInputStream = null;
    private static DataOutputStream serverOutputStream = null;
    private static ExecutorService serverThread = Executors.newSingleThreadExecutor();
    private static boolean serverOpen = true;

    public static void main(String[] args) {
        readWantedConnectionPort();
        serverThread.execute(new MessageSender(serverOutputStream));
        listenServerMessages();
        closeConnections();
    }

    private static void readWantedConnectionPort() {
        System.out.println("Enter the host inet address (or write \"localhost\" for connect on the same machine): ");
        String host = new Scanner(System.in).nextLine();
        System.out.println("Enter the port on which client will connect: ");
        int port = new Scanner(System.in).nextInt();
        estabilishServerConnections(host, port);
    }

    private static void estabilishServerConnections(String host, int port) {
        try {
            serverSocket = new Socket(host, port);
            serverInputStream = new DataInputStream(serverSocket.getInputStream());
            serverOutputStream = new DataOutputStream(serverSocket.getOutputStream());
            System.out.println("Connection estabilish with server");
        } catch (IOException e) {
            System.out.println("Connection with server fail, try again");
            readWantedConnectionPort();
        }
    }

    private static void listenServerMessages() {
        String serverMessage = "";
        try {
            while ((serverMessage = serverInputStream.readUTF()) != null) {
                System.out.println(serverMessage);
            }
        } catch (IOException e) {
            System.out.println("Server was closed");
        }
    }

    public static boolean isServerOpen() {
        return serverOpen;
    }

    private static void closeConnections() {
        try {
            serverInputStream.close();
            serverOutputStream.close();
            serverSocket.close();
            serverThread.shutdownNow();
            serverOpen = false;
        } catch (IOException e) {
            System.out.println("Connections not close correctly");
        }
    }
}