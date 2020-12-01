package views.components;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import utils.Callback;

/**
 * Circle Area input view component for showing 
 * the form fields to a session user for calculating the area 
 * ofa circle.
 * 
 * The component accepts to callbacks, one for closing the
 * client and one for submitting a request to the server.
 * 
 * @author Gianluca (20079110)
 *
 */
public class CircleArea extends JPanel {

	/** Suppress eclipse warning */
	private static final long serialVersionUID = 1L;
	
	/** Label for the text field */
	private JLabel label = new JLabel("Circle radius: ");
	
	/** Text field for capturing circle radius */
	private JTextField jtf = new JTextField(15);
	
	/** Control button for submitting area request */
	private JButton send = new JButton("Send");
	
	/** Control button for closing the client */
	private JButton exit = new JButton("Exit");
	
	/**
	 * Constructor for the radius input view component. The
	 * constructor accepts two callbacks which will later
	 * be used in the event listeners on the component.
	 * This was a method for getting the child component to
	 * call methods from the parent component.
	 * 
	 * @param loginCb
	 * @param close
	 */
	public CircleArea(Callback requestCb, Callback closeCb) {
		// Add control button action listeners with the callbacks
		send.addActionListener(e -> requestCb.invoke(jtf.getText()));
		exit.addActionListener(e -> closeCb.invoke(""));
		
		// add components
		add(label);
		add(jtf);		
		add(send);
		add(exit);

		setVisible(true);
	}

}
