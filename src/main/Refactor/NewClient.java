import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NewClient {
    public static Socket serverSocket = null;
    private static DataInputStream serverInputStream = null;
    private static DataOutputStream serverOutputStream = null;
    private static ExecutorService serverThread = Executors.newSingleThreadExecutor();
    private static boolean serverOpen = true;

    public static void main(String[] args) {
        if (!estabilishServerConnections()) {
            return;
        }
        serverThread.execute(new MessageSender(serverOutputStream));
        listenServerMessages();
        closeConnections();
    }

    private static boolean estabilishServerConnections() {
        try {
            serverSocket = new Socket("localhost", 9999);
            serverInputStream = new DataInputStream(serverSocket.getInputStream());
            serverOutputStream = new DataOutputStream(serverSocket.getOutputStream());
            System.out.println("Connection estabilish with server");
            return true;
        } catch (IOException e) {
            System.out.println("(ERROR) Connection with server fail");
            return false;
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