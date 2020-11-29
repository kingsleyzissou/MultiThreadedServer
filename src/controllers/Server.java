package controllers;

import static javax.swing.JOptionPane.showMessageDialog;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Date;

import javax.swing.JTextArea;

import models.Model;
import models.Student;
import views.CircleArea;

public class Server implements Controller {
	
	/** Model for querying database */
	private Model model = new Model();
	
	public CircleArea view = new CircleArea(this);
	
	public ServerSocket serverSocket;

	
	/**
	 * Method to instantiate the controller,
	 * create the model and render the view
	 */
	@Override
	public void init() {
		try {
			System.out.println("Connected to database");
			model.init();
			view.init();
			
		} catch(SQLException e) {
			showMessageDialog(null, "Unable to connect to database");
			System.exit(0);
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
			return null;
		}
	}
	
	public void incrementRequests(Student student) {
		try {
			model.update(student);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	public void openSocket() {
		try {
			serverSocket = new ServerSocket(8000);
			this.view.log("Server started at " + new Date() + "\n");
			while (true) {
				Socket socket = serverSocket.accept();
				ServerThread thread = new ServerThread(socket, this);
				thread.start();
			}
		} catch(IOException e) {
			this.view.log(e.getMessage() + "\n");
		}
	}

	public void closeSocket() {
		try {
			serverSocket.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

}
