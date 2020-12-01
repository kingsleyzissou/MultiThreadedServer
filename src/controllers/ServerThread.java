package controllers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import models.Student;
import utils.Timestamp;
import views.ClientView;

/**
 * ServerThread class for managing new threads
 * on the server
 * 
 * @author Gianluca (20079110)
 *
 */
public class ServerThread extends Thread {
	
	/** Socket connection */
	private Socket socket;
	
	/** Data input stream */
	private DataOutputStream outputStream;
	
	/** Data Output stream connection */
	private DataInputStream inputStream;
	
	/** Client name for display in the server */
	private String clientName;
	
	/** Client hostname */
	private String hostname;
	
	/** Client ipaddress */
	private String ipaddress;
	
	/** Client port */
	private int port;

	/** Server object for server passing requests back to server */
	private Server server;
	
	/** Session user */
	private Student student;


	/**
	 * Create the new thread and create the input and
	 * output streams
	 * 
	 * @param socket
	 * @throws IOException
	 */
	public ServerThread(Socket socket, Server server) throws IOException {
		this.socket = socket;
		this.server = server;

		this.inputStream = new DataInputStream(socket.getInputStream());
		this.outputStream = new DataOutputStream(socket.getOutputStream());
		
		String name = this.getName().split("-")[1];
		clientName  =  "Client-" + name;

		InetAddress inetAddress = socket.getInetAddress();
		hostname = inetAddress.getHostName();
		ipaddress = inetAddress.getHostAddress();
		port = socket.getPort();
		server.view.log(
			"\n" + Timestamp.now() + "\n"
			+ "New client connection: \n"
			+ "Client thread: " + clientName + "\n"
			+ "Client host: " + hostname + "\n"
			+ "Client address: " + ipaddress + "\n"
			+ "Client port: " + socket.getPort() + "\n"
			+ "\n"
		);
	}

	/*
	 * Invocation method that is called when a
	 * new thread is started
	 * 
	 */
	public void run() {
		while (true) {
			try {
				String payload = inputStream.readUTF();
				
				if(payload.equals("exit")) {
					printClientDetails();
					server.view.log("Exiting...\n"); 
					socket.close(); 
					break; 
				}
				
				handleRequest(payload);
			} catch (IOException e) {
				printClientDetails();
				server.view.log(e + " on " + socket + "\n");
				break;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		closeStreams();
	}	
	
	/**
	 * Parse the request and send new session requests for login
	 * and requests where a session exists for calculating the area
	 * 
	 * @param payload - student id or radius of a circle
	 * @throws IOException
	 */
	private void handleRequest(String payload) throws IOException {
		// convert the payload from a string to a number
		int number = parseNumber(payload);
		
		// if a session user exists
		// calculate area of a circle
		if (student != null) {
			area(number);
			return; // early return
		}
		
		// there is no session, so the
		// request is a login request
		login(number);
	}
	
	/**
	 * Function for creating a new session for a student
	 * so they they can send requests to the server
	 * for calculating the area of a circle
	 * 
	 * @param studentNumber - id for authentication
	 * @throws IOException
	 */
	private void login(int studentNumber) throws IOException {
		outputStream.writeUTF("[Server] Processing student number: " + studentNumber + "\n");
		printClientDetails();
		server.view.log("Login request with student id: " + studentNumber + "\n");

		// find the student by their id
		Student student = server.find(studentNumber);
		
		// if a student is not found
		if (student == null) {
			// send a false value to indicate not logged in
			outputStream.writeBoolean(false);
			// give feedback to the UI of failed login
			outputStream.writeUTF("Sorry " + studentNumber + ". You are not a registered student. Try again or exit.\n");
			server.view.log(Timestamp.now() + "\n");
			server.view.log("[Response] Login unsuccessful\n\n");
			return;
		}

		// set the session user
		this.student = student;
		server.view.log(Timestamp.now() + "\n");
		server.view.log("[Response] Login successful\n\n");
		// inform the client that login was successful
		outputStream.writeBoolean(true);
		// provide feedback message
		outputStream.writeUTF(
			"Welcome " + student.firstname + " " + student.lastname
			+ "! You are now connected to the Server. \n"
			+ "Please enter the radius of a circle\n"
		);
	}
	
	/**
	 * If a session exists, a student is now able to interact
	 * with the server via the client. This method is used to calculate
	 * the area of a circle and return the result to the client. The number
	 * of requests by the student is also incremented
	 * 
	 * @param radius - radius used to calculate circle area
	 * @throws IOException
	 */
	private void area(int radius) throws IOException {
		outputStream.writeUTF("[Server] Calculating area of circle for: " + radius + "\n");
		printClientDetails();
		server.view.log(
				"Student "
				+ student.student_id
				+ " requests area for circle radius "
				+ radius + " cm "
				+ "\n"
		);
		// Increment student requests
		student.requests = student.requests + 1;
		// have the server update the student requests
		server.incrementRequests(student);
		String area = server.calculateArea(radius);
		server.view.log(Timestamp.now() + "\n");
		server.view.log("[Response] Area: " + area + " cm2\n\n");
		// output the result to the client
		outputStream.writeUTF(
			"The area of a circle with radius "
			+ radius + " cm is: \n"
			+ area
			+ " cm2\n"
		);

	}
	
	/**
	 * Helper method to print client host, address
	 * and thread to Server GUI
	 * 
	 */
	private void printClientDetails() {
		server.view.log(
			Timestamp.now() + "\n"
			+ "[" + clientName + " @ "
			+ ipaddress + ":" + port
			+ " (" + hostname + ")"
			+ "]\n"
		);
	}
	
	/**
	 * Close the data streams
	 * 
	 */
	private void closeStreams() {
		try {
			inputStream.close();
			outputStream.close();
			printClientDetails();
			server.view.log("Data streams closed\n");
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Helper method to parse a number string
	 * 
	 * @param input - number to parse
	 * @return the number as an integer
	 */
	private int parseNumber(String input) {
		try {
			return Integer.parseInt(input);
		} catch (NumberFormatException e) {
			return 0;
		}
	}
}