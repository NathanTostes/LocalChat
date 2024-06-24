import java.io.*;
import java.net.*;
import java.util.*;

public class ConnectionController implements Runnable {
	private static int clientNumber = 0;
	private int clientId = 0;
	private Socket client = null;
	DataInputStream clientMessager = null;
	DataOutputStream out = null;
	private ArrayList<Socket> clientList = null;

	public ConnectionController(Socket client, ArrayList clientList) throws IOException {
		clientNumber++;
		this.clientId = clientNumber;
		this.client = client;
		this.clientList = clientList;
	}

	@Override
	public void run() {
		newConnection();
		clientReadMessage();
	}
	
	private void newConnection() {
		System.out.println("New connection with user " + clientId);
		try {
			this.clientMessager = new DataInputStream(client.getInputStream());
			clientsSendMessage("The user " + clientId + " has been joined");
		} catch (IOException e) {
			System.out.println("(Error) Client input/output failed");
		}
	}
	
	private void clientReadMessage() {
		String line = "";
		try {
			while ((line = clientMessager.readUTF()) != null) {
				clientsSendMessage("User " + clientId + ": " + line);
			}
		} catch (IOException e) {
			closeConnections();
		}
	}

	private void clientsSendMessage(String message) {
		try {
			for (Socket clientSocket : clientList) {
				if (clientSocket != this.client) {
					out = new DataOutputStream(clientSocket.getOutputStream());
					out.writeUTF(message);
				}
			}
		} catch (IOException e) {
			System.out.println("(Error) Client " + clientId + " message cannot send for all clients");
		}
	}

	private void closeConnections() {
		try {
			clientList.remove(client);
			System.out.println("Connection closed with user " + clientId);
			clientsSendMessage("User " + clientId + " has been left");
			clientMessager.close();
		} catch (IOException e) {
			System.out.println("(Error) Client connections not closed correctly");
		}
	}
}