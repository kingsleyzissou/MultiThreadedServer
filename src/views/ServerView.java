package views;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import controllers.Server;

/**
 * The server view component for rendering the server
 * GUI window.
 * 
 * @author Gianluca (20079110)
 *
 */
public class ServerView extends JFrame {

	/** Suppress eclipse warning */
	private static final long serialVersionUID = 1L;
	
	/** Text area for showing */
	private JTextArea jta = new JTextArea();
	
	/** Control button for creating a new client */
	private JButton client = new JButton("New client");
	
	/** Control button for closing the server */
	private JButton exit = new JButton("Exit");

	/** Server controller */
	private Server server;
	
	public ServerView(Server server) {
		this.server = server;
		
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(1,2));
		p.add(exit);
		p.add(client);

		// Add control button action listeners
		client.addActionListener(e -> server.createThread());
		exit.addActionListener(e -> exit());

		// Add the control buttons to the frame
		setLayout(new BorderLayout());
		add(p, BorderLayout.NORTH);
		add(new JScrollPane(jta), BorderLayout.CENTER);

		setTitle("Server");
		setSize(500, 800);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
	 * When closing the server, we want to make sure to
	 * close the socket connection too and then gracefully
	 * exit the application.
	 * 
	 */
	private void exit() {
		try {
			System.out.println("Closing server. Goodbye...");
			server.closeSocket();
			System.exit(0);
		} catch(IOException e) {
			log(e.getMessage() + "\n\n");
		}
	}

}
