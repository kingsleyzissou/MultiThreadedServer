package controllers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import utils.Timestamp;
import views.ClientView;

/**
 * Client controller for managing the
 * client view and the corresponding
 * controller actions
 * 
 * @author Gianluca (20079110)
 *
 */
public class Client {
	
	/** Socket connection */
	private Socket socket;
	
	/** Data input stream */
	private DataOutputStream outputStream;
	
	/** Data Output stream connection */
	private DataInputStream inputStream;
	
	/** Client view component */
	private ClientView view = new ClientView(this);
	
	/**
	 * Open a socket connection to the
	 * server
	 * 
	 * @throws IOException
	 */
	public void openSocket() throws IOException {
		// Create a socket to connect to the server
		socket = new Socket("localhost", 8000);

		// Create an input stream to receive data from the server
		inputStream = new DataInputStream(socket.getInputStream());

		// Create an output stream to send data to the server
		outputStream = new DataOutputStream(socket.getOutputStream());
		view.log(Timestamp.now() + "\n");
		view.log("Connection to server established 	\n\n");
	}
	
	/**
	 * Login function to authenticate the client
	 * with the server
	 * 
	 * @param payload - the id number to be authenticated
	 * @throws IOException
	 */
	public void login(String payload) throws IOException {
		// check if a socket connection is open
		// otherwise open socket
		if (!socketOpen()) {
			openSocket();
		}
		// send input to server
		outputStream.writeUTF(payload);
		// flush the stream
		outputStream.flush();
		view.log(Timestamp.now() + "\n");
		// log server acknowledgment message
		view.log(inputStream.readUTF());
		// handle user session
		createSession();
		// log the message from the server
		view.log(inputStream.readUTF() + "\n");
	}
	
	/**
	 * Check if the student is successfully logged in
	 * and update the UI accordingly
	 * 
	 * @throws IOException
	 */
	public void createSession() throws IOException {
		boolean loggedIn = inputStream.readBoolean();
		view.toggle(loggedIn);
	}
	

	/**
	 * Make a request to the server over a socket connection
	 * to calculate the radius of a circle
	 * 
 	 * @param payload - the radius of the circle for area calculation
	 * @throws IOException
	 */
	public void request(String payload) throws IOException {
		// check if a socket connection is open
		// otherwise open socket
		if (!socketOpen()) {
			openSocket();
		}
		outputStream.writeUTF(payload);
		outputStream.flush();
		view.log(Timestamp.now() + "\n");
		// log server acknowledgment message
		view.log(inputStream.readUTF());
		// log the message from the server
		view.log(inputStream.readUTF() + "\n");
	}
	
	/**
	 * Helper method to check if the socket
	 * and streams are open
	 * 
	 * @return true or false
	 */
	private boolean socketOpen() {
		return !(outputStream == null && inputStream == null && socket == null);
	}

	/**
	 * Close the socket and streams
	 * 
	 * @throws IOException
	 */
	public void close() throws IOException {
		// check if socket is open
		// to avoid null pointer
		if (socketOpen()) {
			// message server to close the socket
			outputStream.writeUTF("exit");
			// close streams
			outputStream.close();
			inputStream.close();
			// close socket
			socket.close();
		}
	}


}
