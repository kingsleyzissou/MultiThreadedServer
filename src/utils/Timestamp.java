package utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Helper class for returning
 * a formatted time stamp
 * 
 * @author Gianluca (20079110)
 *
 */
public class Timestamp {
	
	/** Date format for time stamps */
	static DateFormat timestamp = new SimpleDateFormat("yyyy/MM/dd - hh:mm:ss");

	/**
	 * Helper method to get the time stamp
	 * for displaying in GUI logs
	 * 
	 * @return
	 */
	public static String now() {
		return timestamp.format(new Date());
	}
}
