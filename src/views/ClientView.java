package views;

import java.awt.BorderLayout;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import controllers.Client;
import utils.Callback;
import views.components.CircleArea;
import views.components.Login;

/**
 * The client view component for the client GUI window.
 * The client view has two child components, the login component
 * and the circle area component.
 * 
 * @author Gianluca (20079110)
 *
 */
public class ClientView extends JFrame {
	
	/** Suppress eclipse warning */
	private static final long serialVersionUID = 1L;
	
	/** Text area for showing logs from server */
	private JTextArea jta = new JTextArea();
	
	/** Login fields view component */
	private JPanel loginComponent;
	
	/** Circle calculation view component */
	private JPanel areaComponent;

	/** Client controller */
	private Client client;
	
	private boolean loggedIn = false;
	
	public ClientView(Client client) {
		this.client = client;
		
		// Callback functions passed to child components
		Callback loginEvent = id -> login(id);
		Callback requestEvent = payload -> request(payload);
		Callback exitEvent = str -> close();
		
		// View components instantiated with relevant callbacks
		loginComponent = new Login(loginEvent, exitEvent);
		areaComponent = new CircleArea(requestEvent, exitEvent);

		// Add add components to the frame
		setLayout(new BorderLayout());
		add(loginComponent, BorderLayout.NORTH);
		add(new JScrollPane(jta), BorderLayout.CENTER);

		setTitle("Client");
		setLocation(500, 0);
		setSize(500, 300);
		setVisible(true);

	}
	
	/**
	 * Method used to log activities to the 
	 * text area component
	 * 
	 * @param message
	 */
	public void log(String message) {
		jta.append(message);
	}
	
	/**
	 * Method for changing the UI components when a student
	 * user session has been created. If there is an active session
	 * the user login fields will be removed and replaced with
	 * the circle radius field
	 * 
	 * @param loggedIn - boolean if a user is logged in or not
	 */
	public void toggle(boolean loggedIn) {
		// check that the session isn't accidentally switched
		if (loggedIn == this.loggedIn) return;
		// update session value
		this.loggedIn = loggedIn;
		if (loggedIn) {
			// if the student is logged in
			// remove the login component
			// and replace with the radius component
			getContentPane().remove(loginComponent);
			getContentPane().add(areaComponent, BorderLayout.NORTH);
			validate();
			return; // early return
		} 
		// remove the radius component
		// and replace with the login component
		getContentPane().remove(areaComponent);
		getContentPane().add(loginComponent, BorderLayout.NORTH);
		validate();
	}
	
	/**
	 * Attempt a login. Before the student can be logged in,
	 * a socket connection to the server needs to be created
	 * as this is the first possible attempt at reaching the server.
	 * 
	 * @param id - student id
	 */
	public void login(String id) {
		try {
			client.login(id);
		} catch(IOException e) {
			log(e.getMessage() + "\n");
		}
	}

	/**
	 * Attempt to make a socket request to the
	 * server to calculate the area of the circle
	 * with the given radius
	 * 
	 * @param payload - the radius to be calculated
	 */
	public void request(String payload) {
		try {
			client.request(payload);
		} catch(IOException e) {
			log(e.getMessage() + "\n");
		}
	}
	
	/**
	 * When closing the client, we want to make sure to
	 * close the socket connection too.
	 * 
	 */
	public void close() {
		try {
			client.close();
			dispose();
		} catch (IOException e) {
			log(e.getMessage() + "\n");
		}
	}
}
