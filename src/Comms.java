import java.io.*;
import java.net.*;

public class Comms {

	ServerSocket serverSocket;
	Socket clientSocket;
	
	// for streaming data in and out in byte
	ObjectOutputStream output;
	ObjectInputStream input;
	
	public void sendMsg(Message m) throws IOException {
		
		output = new ObjectOutputStream(clientSocket.getOutputStream());
		output.flush();
		
		output.writeObject(m);
	}

	public Message getMsg() throws ClassNotFoundException, IOException {
		
		input = new ObjectInputStream(clientSocket.getInputStream());
		
		Message msg = (Message) input.readObject();

		return msg;
	}
	
	public void closeConnection() {
	
		try {
			
			System.out.println("CLOSING CONNECTION");
			output.close();
			input.close();
			clientSocket.close();
			
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}
}

class ClientComms extends Comms {

	public ClientComms() {

		try {
			
			connectToServer();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
	}
	
	// connects to server
	public void connectToServer() throws IOException {
		
			System.out.println("TRYING TO CONNECT .. ");
			clientSocket = new Socket("127.0.0.1", 444);
			System.out.println("Connected to : " + clientSocket.getInetAddress().getHostName());
	}
}