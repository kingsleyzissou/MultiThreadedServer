package controllers;

import static javax.swing.JOptionPane.showMessageDialog;

import java.io.IOException;
import java.net.ServerSocket;
import java.sql.SQLException;
import java.util.Date;

import javax.swing.JTextArea;

import models.Model;
import views.CircleArea;

public class Server implements Controller {
	
	/** Model for querying database */
	private Model model = new Model();
	
	/** Gui view */
	private CircleArea circleArea;

	
	/**
	 * Method to instantiate the controller,
	 * create the model and render the view
	 */
	@Override
	public void init() {
		try {
			model.init();
			System.out.println("Connected to database");
			circleArea = new CircleArea(this);
		} catch(SQLException e) {
			showMessageDialog(null, "Unable to connect to database");
			e.printStackTrace();
			System.exit(0);
		}
	}


	public void openSocket(JTextArea jta) {
		try {
			ServerSocket socket = new ServerSocket(8000);
			jta.append("Server started at " + new Date() + '\n');
			
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	

}
