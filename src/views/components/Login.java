package views.components;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import utils.Callback;

/**
 * Login view component for capturing the student id
 * for a student to login.
 * 
 * The component accepts to callbacks, one for closing the
 * client and one for submitting the login request to the server.
 * 
 * @author Gianluca (20079110)
 *
 */
public class Login extends JPanel {

	/** Suppress eclipse warning */
	private static final long serialVersionUID = 1L;
	
	/** Label for the text field */
	private JLabel label = new JLabel("Student number: ");
	
	/** Text field for capturing student id */
	private JTextField jtf = new JTextField(15);
	
	/** Control button for submitting login request */
	private JButton login = new JButton("Login");
	
	/** Control button for closing the client */
	private JButton exit = new JButton("Exit");
	
	/**
	 * Constructor for the login view component. The
	 * constructor accepts two callbacks which will later
	 * be used in the event listeners on the component.
	 * This was a method for getting the child component to
	 * call methods from the parent component.
	 * 
	 * @param loginCb
	 * @param close
	 */
	public Login(Callback loginCb, Callback closeCb) {
		// Add control button action listeners with the callbacks
		login.addActionListener(e -> loginCb.invoke(jtf.getText()));
		exit.addActionListener(e -> closeCb.invoke(""));

		// add components
		add(label);
		add(jtf);
		add(login);
		add(exit);

		setVisible(true);
	}

}
