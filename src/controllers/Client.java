package controllers;

import static javax.swing.JOptionPane.showMessageDialog;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JTextArea;

import models.Model;
import models.Student;
import views.CircleArea;
import views.LoginForm;

public class Client implements Controller {
	
	/** Model for querying database */
	private Model model = new Model();
	
	// IO streams and socket
	private DataOutputStream toServer;
	private DataInputStream fromServer;
	private Socket socket;


	/**
	 * Method to instantiate the controller,
	 * create the model and render the view
	 */
	@Override
	public void init() {
		try {
			model.init();
			System.out.println("Connected to database");
			new LoginForm(this);
		} catch(SQLException e) {
			showMessageDialog(null, "Unable to connect to database");
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public void openSocket(JTextArea jta) {
		try {
			// Create a socket to connect to the server
			Socket socket = new Socket("localhost", 8000);

			// Create an input stream to receive data from the server
			fromServer = new DataInputStream(socket.getInputStream());

			// Create an output stream to send data to the server
			toServer = new DataOutputStream(socket.getOutputStream());
		} catch (IOException ex) {
			jta.append(ex.getMessage() + "\n");
		}
	}

	/**
	 * Return array list of employees
	 * 
	 * @return list of employees
	 */
	public Student login(int id) {
		try {
			return model.show(id);
		} catch (SQLException e) {
			e.printStackTrace();
//			view.showError("Error fetching Employees");
			return null;
		}
	}


}
