package Server;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
	private static ServerSocket connectionsSocket = null;
	private static Socket client = null;
	private static ArrayList<Socket> clientList = new ArrayList<>();

	public static void main(String[] args){
		openServerAtPort(9999);
		connectionListener();
		closeConnections();
	}
	
	public static void openServerAtPort(int portNumber) {
		try {
			connectionsSocket = new ServerSocket(portNumber);
			System.out.println("Server openned at port " + portNumber);
		} catch(IOException e) {
			System.out.println("(Error) Cannot open server at port " + portNumber);
		}
	}
	
	public static void connectionListener() {
		try {
			while(true) {
				client = connectionsSocket.accept();
				clientList.add(client);
				new Thread(new ConnectionController(client, clientList)).start();;
			}
		} catch (IOException e) {
			System.out.println("(Error) Server cannot connect with client");
		}
	}

	private static void closeConnections() {
		try {
			for(Socket clientSocket : clientList) {
				clientSocket.close();
			}
			connectionsSocket.close();
			System.out.println("Server closed");
		} catch (IOException e) {
			System.out.println("(Error) Connections has not closed correctly");
		}
	}
}