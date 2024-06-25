import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class MessageSender implements Runnable {
    Scanner clientOutput = new Scanner(System.in);
    DataOutputStream serverOutputStream = null;

    public MessageSender(DataOutputStream serverOutputStream) {
        this.serverOutputStream = serverOutputStream;
    }

    @Override
    public void run() {
        String clientMessage = "";
        while((clientMessage = clientOutput.nextLine()) != null) {
            try {
                serverOutputStream.writeUTF(clientMessage);
            } catch (IOException e) {
                break;
            }
        }
    }
}
