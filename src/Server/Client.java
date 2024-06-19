package Server;

import java.io.*;
import java.lang.Thread.State;
import java.net.*;
import java.util.*;

public class Client {
	private static Scanner messager = new Scanner(System.in);
	private static Socket socket = null;
	private static DataInputStream in;
	private static DataOutputStream out;

	public static void main(String[] args) {
		connect();
		listenMessages();
		closeConnections();
	}

	private static void connect() {
		try {
			socket = new Socket("localhost", 9999);
			out = new DataOutputStream(socket.getOutputStream());
			in = new DataInputStream(socket.getInputStream());
			Thread serverMessages = new Thread(new ClientMessages(in));
			serverMessages.start();
		} catch (IOException e) {
			System.out.println("(Error) Connect to server failed");
		}
	}

	private static void listenMessages() {
		String message = "";
		try {
			while ((message = messager.nextLine()) != null) {
				out.writeUTF(message);
			}
		} catch (IOException e) {
		}
	}

	private static void closeConnections() {
		try {
			messager.close();
			socket.close();
			out.close();
			in.close();
		} catch (IOException e) {
			System.out.println("(Error) Connections has not closed correctly");
		}
	}
}