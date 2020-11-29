package controllers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.text.DecimalFormat;

import models.Student;

/**
 * Private class for a thread corresponding to a client connection request
 */
public class ServerThread extends Thread {
	// The socket the client is connected through
	private Socket socket;
	private final DataInputStream inputStream; 
	private final DataOutputStream outputStream; 


	// The ip address of the client
	private InetAddress address;


	private Server server;
	private Student student;

	/**
	 * The Constructor for the client thread, which creates the socket input and
	 * output data streams
	 * 
	 * @param socket
	 * @throws IOException
	 */
	public ServerThread(Socket socket, Server server) throws IOException {
		this.server = server;
		this.socket = socket;
		this.inputStream =  new DataInputStream(socket.getInputStream());
		this.outputStream  =  new DataOutputStream(socket.getOutputStream());

		InetAddress inetAddress = socket.getInetAddress();
		server.view.log("Client's host name is " + inetAddress.getHostName());
		server.view.log("Client's IP Address is " + inetAddress.getHostAddress() + "\n");
	}

	/*
	 * The method that runs when the thread starts
	 */
	public void run() {
		while (true) {
			try {
				String payload = inputStream.readUTF();
				
				if(payload.equals("exit")) { 
					
					server.view.log("Client " + this.socket + " sends exit..."); 
					server.view.log("Closing this connection."); 
					this.socket.close(); 
					server.view.log("Connection closed");
					break; 
				} 	
				
				String response = handleRequest(payload);
				outputStream.writeUTF(response);
			} catch (Exception e) {
				System.err.println(e + " on " + socket);
				break;
			}
		}
		closeStreams();
	}
	
	private String handleRequest(String payload) {
		// Parse the student number
		int number = parseNumber(payload);
		if (student == null) {
			return this.login(number);
		}
		return this.area(number);
	}
	
	private void closeStreams() {
		try {
			inputStream.close();
			outputStream.close();
			server.view.log("Data streams closed");
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private String login(int studentNumber) {
		System.out.println("Processing student number: " + studentNumber + "\n");

		
		Student student = this.server.login(studentNumber);

		// Send the result message to the client
		if (student != null) {
			this.student = student;
			return "Welcome " + student.firstname + " " + student.lastname
					+ "! You are now connected to the Server. \n"
					+ "Please enter the radius of a circle";
		}
		
		return "Sorry " + studentNumber + ". You are not a registered student. Try again or exit.";
	
	}
	
	private String area(int radius) {
		System.out.println("Calculating area of circle for: " + radius + "\n");
		
		if (student != null) {
			DecimalFormat df = new DecimalFormat("0.00");
			student.requests = student.requests + 1;
			server.incrementRequests(student);
			return "The area of a circle with radius "
					+ radius + " cm is: \n"
					+ df.format(Math.PI * Math.pow(radius, 2))
					+ " cm2";		
		}
		
		return "Sorry unable to calculate area of circle";
	
	}

	/**
	 * Helper method to parse a student number string and catch formatting
	 * exceptions.
	 * 
	 * @param studentNumber
	 *            the string to parse
	 * @return the student number as an integer
	 */
	private int parseNumber(String input) {
		try {
			return Integer.parseInt(input);
		} catch (NumberFormatException e) {
			return 0;
		}
	}
}