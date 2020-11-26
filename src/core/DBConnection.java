package core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
	
	/** The name of the MySQL account to use (or empty for anonymous) */
	private final static String userName = "root";

	/** The password for the MySQL account (or empty for anonymous) */
	private final static String password = "root";

	/** The name of the computer running MySQL */
	private final static String serverName = "localhost";

	/** The port of the MySQL server (default is 3306) */
	private final static int portNumber = 3306;

	/** The name of the database we are testing with (this default is installed with MySQL) */
	private final static String dbName = "Assign2";
	
	private static Connection connection = null;
	
	/**
	 * Singleton class so only one database
	 * instance is created
	 * 
	 * @return db connection
	 * @throws SQLException
	 */
	public static Connection getConnection() throws SQLException {
		// create the connection if has not already been created
		if (DBConnection.connection == null) {
			
			Properties connectionProps = new Properties();
			connectionProps.put("user", DBConnection.userName);
			connectionProps.put("password", DBConnection.password);

			DBConnection.connection = DriverManager.getConnection(
				"jdbc:mysql://"
				+ DBConnection.serverName 
				+ ":" + DBConnection.portNumber 
				+ "/" + DBConnection.dbName,
				connectionProps
			);
		}
		
		// return the database connection
		return DBConnection.connection;
	}

}
