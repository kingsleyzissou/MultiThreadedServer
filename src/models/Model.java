package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import utils.DBConnection;

/**
 * Model object for handling database
 * transactions for a student object.
 * 
 * @author Gianluca (20079110)
 *
 */
public class Model {
	
	private Connection connection = null;
	
	/**
	 * Instantiate the model
	 * 
	 * @throws SQLException
	 */
	public void init() throws SQLException {
		this.connection = DBConnection.getConnection();
	}

	/**
	 * Method to get all the students
	 * from the database
	 * 
	 * @return list of all students
	 * @throws SQLException
	 */
	public ArrayList<Student> all() throws SQLException {
		ArrayList<Student> list = new ArrayList<>();
		Statement stmt = this.connection.createStatement();
		stmt.executeQuery("SELECT * FROM students");
		ResultSet res = stmt.getResultSet();
		while(res.next()) {
			Student student = new Student(
				res.getInt("SID"),
				res.getInt("STUD_ID"),
				res.getString("FNAME"),
				res.getString("SNAME"),
				res.getInt("TOT_REQ")
			);
			list.add(student);
		}
		res.close();
		stmt.close();
		return list;
	}
	
	/**
	 * Return a specific student by id
	 * 
	 * @param id of student
	 * @return student
	 * @throws SQLException
	 */
	public Student find(int id) throws SQLException {
		Statement stmt = this.connection.createStatement();
		stmt.executeQuery("SELECT * FROM students WHERE STUD_ID = " + id);
		ResultSet res = stmt.getResultSet();
		if (res.next()) {
			return new Student(
				res.getInt("SID"),
				res.getInt("STUD_ID"),
				res.getString("FNAME"),
				res.getString("SNAME"),
				res.getInt("TOT_REQ")
			);
		}
		return null;
	}
	
	/**
	 * Add student to the database
	 * 
	 * @param e student to be added to database
	 * @throws SQLException
	 */
	public void create(Student e) throws SQLException {
		String sql = "INSERT INTO students ("
				+ "FNAME, SNAME, STUD_ID"
				+ ") VALUES (?, ?, ?)";
		PreparedStatement stmt = this.connection.prepareStatement(sql);
		stmt.setString(1, e.firstname);
		stmt.setString(2, e.lastname);
		stmt.setInt(3, e.student_id);
		stmt.executeUpdate();
	}
	
	/**
	 * Edit existing database student
	 * 
	 * @param e student to be updated
	 * @throws SQLException
	 */
	public void update(Student s) throws SQLException {
		String sql = "UPDATE students SET "
				+ "FNAME=?, "
				+ "SNAME=?, "
				+ "STUD_ID=?, "
				+ "TOT_REQ=? "
				+ "WHERE SID=?";
		PreparedStatement stmt = this.connection.prepareStatement(sql);
		stmt.setString(1, s.firstname);
		stmt.setString(2, s.lastname);
		stmt.setInt(3, s.student_id);
		stmt.setInt(4, s.requests);
		stmt.setInt(5, s.id);
		stmt.executeUpdate();
		stmt.close();
	}
	
	/**
	 * Delete existing database student
	 * 
	 * @param e student to be deleted
	 * @throws SQLException
	 */
	public void delete(Student s) throws SQLException {
		String sql = "DELETE FROM students WHERE id=?";
		PreparedStatement stmt = this.connection.prepareStatement(sql);
		stmt.setInt(1, s.student_id);
		stmt.executeUpdate();
		stmt.close();
	}

}
