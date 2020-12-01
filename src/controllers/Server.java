package controllers;

import static javax.swing.JOptionPane.showMessageDialog;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.text.DecimalFormat;

import models.Model;
import models.Student;
import utils.Timestamp;
import views.ServerView;

/**
 * Server controller for managing the
 * server view and the corresponding
 * controller actions
 * 
 * @author Gianluca (20079110)
 *
 */
public class Server {
	
	/** Model for querying database */
	private Model model = new Model();
	
	/** Server socket connection */
	public ServerSocket serverSocket;
	
	/** Server view component */
	public ServerView view = new ServerView(this);

	
	/**
	 * Instantiate the model, open the server socket
	 * and create a for loop to listen for socket connections
	 * from the client
	 * 
	 */
	public void init() {
		try {
			model.init();
			serverSocket = new ServerSocket(8000);
			view.log(Timestamp.now() + "\n");
			view.log("Connected to database\n");
			view.log("Server started \n");
			listen();
		} catch(IOException e) {
			view.log(e.getMessage() + "\n");
		} catch(SQLException e) {
			showMessageDialog(null, "Unable to connect to database");
			System.exit(0);
		}
	}
	
	/**
	 * Listen for socket connections and create
	 * new threads accordingly
	 * 
	 * @throws IOException
	 */
	private void listen() throws IOException {
		while (true) {
			Socket socket = serverSocket.accept();
			ServerThread thread = new ServerThread(socket, this);
			thread.start();
		}
	}
	
	/**
	 * Create a new client with a new socket
	 * connection
	 * 
	 */
	public void createThread() {
		new Client();
	}
	
	/**
	 * Find the a student by ID 
	 * 
	 * @return student
	 */
	public Student find(int id) {
		try {
			return model.find(id);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Update the student in the database
	 * with the incremented request
	 * 
	 * @param student
	 */
	public void incrementRequests(Student student) {
		try {
			model.update(student);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Calculate the area of a circle from
	 * the given radius
	 * 
	 * @param radius for the area calculation
	 * @return area of a circle
	 */
	public String calculateArea(int radius) {
		// Format the area to 2 decimal places
		DecimalFormat df = new DecimalFormat("0.00");
		// Return the value
		return df.format(Math.PI * Math.pow(radius, 2));
	}


	/**
	 * Close the server socket
	 * 
	 * @throws IOException
	 */
	public void closeSocket() throws IOException {
		// check if socket is open
		// to prevent null pointer
		if (serverSocket != null) {
			serverSocket.close();
		}
	}

}
