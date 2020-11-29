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

	
	// IO streams and socket
	private DataOutputStream outputStream;
	private DataInputStream inputStream;
	
	private LoginForm view = new LoginForm(this);


	/**
	 * Method to instantiate the controller,
	 * create the model and render the view
	 */
	@Override
	public void init() {
		view.init();
	}
	
	public void openSocket() {
		try {
			// Create a socket to connect to the server
			Socket socket = new Socket("localhost", 8000);

			// Create an input stream to receive data from the server
			inputStream = new DataInputStream(socket.getInputStream());

			// Create an output stream to send data to the server
			outputStream = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			view.log(e.getMessage() + "\n");
		}
	}

	/**
	 * Return array list of employees
	 * 
	 * @return list of employees
	 */
	public void login(String studentId) {
		try {
			outputStream.writeUTF(studentId);
			outputStream.flush();
			String result = inputStream.readUTF();
			view.log(result + "\n\n");
		} catch (IOException e) {
			e.printStackTrace();
			view.log(e.getMessage() + "\n");
		}
	}

	public void close() {
		try {
			outputStream.writeUTF("exit");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
