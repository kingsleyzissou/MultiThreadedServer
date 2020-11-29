package views;

import java.awt.BorderLayout;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import controllers.Client;
import controllers.Server;
import models.Student;

public class CircleArea extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// Text area for displaying contents
	private JTextArea jta = new JTextArea();
	
	private JButton newClient = new JButton("New client");
	private JButton exit = new JButton("Exit");

	private Server server;
	
	public CircleArea(Server server) {
		this.server = server;
		JPanel p2 = new JPanel();
		p2.setLayout(new BorderLayout());
		p2.add(newClient);

		// Add command button action listeners
		newClient.addActionListener(e -> createClient());
		exit.addActionListener(e -> exit());

		// Add the exit button to a new panel
		JPanel p3 = new JPanel();
		p3.setLayout(new BorderLayout());
		p3.add(exit);

		// Add the command button panels to the frame
		setLayout(new BorderLayout());
		add(p2, BorderLayout.EAST);
		add(p3, BorderLayout.WEST);
		add(new JScrollPane(jta), BorderLayout.CENTER);

		setTitle("Server");
		setSize(500, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	
		
		
	}
	
	public void log(String message) {
		jta.append(message);
	}

	public void init() {
		server.openSocket();
	}

	private void createClient() {
		try {
			(new Client()).init();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void exit() {
		System.out.println("Closing server. Goodbye...");
		server.closeSocket();
		System.exit(0);
	}

}