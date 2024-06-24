import java.io.*;

public class ClientMessages implements Runnable {
	private DataInputStream messager;
	
	public ClientMessages(DataInputStream messager) {
		this.messager = messager;
	}
	
	@Override
	public void run() {
		String message = "";
		try {
			while((message = messager.readUTF()) != null) {
				System.out.println(message);
			}
		} catch(IOException e) {
			System.out.println("Server was closed");
		}
	}
}