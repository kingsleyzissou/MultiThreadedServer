package core;

import controllers.Client;
import controllers.Server;

/**
 * Main entry point for the application.
 * The App class is responsable for creating the client
 * and server controllers and initiating the server.
 * 
 * @author Gianluca (20079110)
 *
 */
public class App {
	
	public static void main(String[] args) { 
		new Client();
		Server server = new Server();
		server.init();
	}

}
