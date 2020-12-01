package models;

/**
 * Student object that is used
 * for the model throughout the application.
 * 
 * @author Gianluca (20079110)
 *
 */
public class Student {
	
	// public fields are used to simplify code
	public int id;
	public int student_id;
	public String firstname;
	public String lastname;
	public int requests;
	
	public Student(
			int id,
			int student_id,
			String firstname, 
			String lastname,
			int requests
			
	) {
		this.id = id;
		this.student_id = student_id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.requests = requests;
	}
	
	public String toString() {
		return  this.firstname 
				+ " " 
				+ this.lastname 
				+ " (" 
				+ this.student_id
				+ ""
				+ ") Requests: $"
				+ this.requests;
	}

}
