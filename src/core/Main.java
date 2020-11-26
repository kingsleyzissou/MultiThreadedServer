package core;

import controllers.Controller;
import controllers.Server;

public class Main {
	
	public static void main(String[] args) { 
		Controller controller = new Server();
		controller.init();
	}

}
