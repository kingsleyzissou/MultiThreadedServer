package views;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

//import Client.ExitListener;
//import Client.SendListener;
import controllers.Client;
import models.Student;

public class LoginForm extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// Swing area and text fields
	private JTextField jtf = new JTextField();
	private JTextArea jta = new JTextArea();
	
	// Command buttons
	private JButton send = new JButton("Send");
	private JButton exit = new JButton("Exit");

	private Client client;
	
	public LoginForm(Client client) {
		this.client = client;
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		p.add(new JLabel("Enter student number"), BorderLayout.WEST);
		p.add(jtf, BorderLayout.CENTER);
		jtf.setHorizontalAlignment(JTextField.RIGHT);

		// Add the send button to a new panel
		JPanel p2 = new JPanel();
		p2.setLayout(new BorderLayout());
		p2.add(send);

		// Add the exit button to a new panel
		JPanel p3 = new JPanel();
		p3.setLayout(new BorderLayout());
		p3.add(exit);

		// Add the command button panels to the frame
		setLayout(new BorderLayout());
		add(p, BorderLayout.NORTH);
		add(p2, BorderLayout.EAST);
		add(p3, BorderLayout.WEST);
		add(new JScrollPane(jta), BorderLayout.CENTER);
		

		// Add command button action listeners
		send.addActionListener(e -> login());
		exit.addActionListener(e -> close());

		setTitle("Client");
		setSize(500, 300);
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

	}
	
	public void init() {
		client.openSocket();
	}
	
	public void log(String message) {
		jta.append(message);
	}

	private void login() {
		String id = jtf.getText();
		client.login(id);
	}
	
	private void close() {
		client.close();
		dispose();
	}
	

}
