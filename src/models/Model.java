package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import core.DBConnection;

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
	 * Method to get all the employees
	 * from the database
	 * 
	 * @return list of all employees
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
	 * Return a specific employee by id
	 * 
	 * @param id of employee
	 * @return employee
	 * @throws SQLException
	 */
	public Student show(int id) throws SQLException {
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
	 * Add employee to the database
	 * 
	 * @param e employee to be added to database
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
	 * Edit existing database employee
	 * 
	 * @param e employee to be updated
	 * @throws SQLException
	 */
	public void update(Student s) throws SQLException {
		String sql = "UPDATE students SET "
				+ "FNAME=?, "
				+ "SNAME=?, "
				+ "STUD_ID=?, "
				+ "TOT_REQ=? "
				+ "WHERE id=?";
		PreparedStatement stmt = this.connection.prepareStatement(sql);
		stmt.setString(1, s.firstname);
		stmt.setString(2, s.lastname);
		stmt.setInt(3, s.student_id);
		stmt.setInt(4, s.requests);
		stmt.setInt(7, s.student_id);
		stmt.executeUpdate();
		stmt.close();
	}
	
	/**
	 * Delete existing database employee
	 * 
	 * @param e employee to be deleted
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
