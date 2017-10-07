/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ingeniigroup.stratux.dbConnect;

import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.DriverManager;
import org.sqlite.SQLiteDataSource;
import org.sqlite.SQLiteJDBCLoader;

/**
 *
 * @author david
 */
public class StratuxDB {
	
	private boolean connected;
	private String dbFname;
	private String dbName;
	private Connection db;
	
	/*
	 *  Make DB Connection and prepare to handle queries.
	 */
	public StratuxDB(String dbFname){
		
		try {
			
			// init SQLite Connection
			this.dbFname   = dbFname;
			this.connected = Connect();

			// check to see if a database name was returned
			if(!this.connected){
				throw new Exception("No Database Found in File: " + this.dbFname);
			}
			
			// return database name and set connected flag
			System.out.print("Connected....\n");
			
		}
		catch (Exception ex){
			System.err.printf("Fatal Error Connecting to DB at [%s]\nError: %s\n",this.dbFname,ex.getMessage());
		}
		
	}
	
	/*
	 *  Return flag indicating db is open for business
	*/
	public boolean Connected(){
		return this.connected;
	}
	
	/* 
	 *  Return the connection object
	 */
	public Connection db(){
		return this.db;
	}
	
	/**
	 * getResultSet()
	 * 
	 * Create Statement, prepare and execute Query (sql), test for valid 
	 * ResultSet handle, return if !null, throw exceptions if null
	 * 
	 * @param sql
	 * 
	 * @return  valid result set
	 */
	public ResultSet getResultSet(String sql){
		
		ResultSet result    = null;
		Statement statement = null;
		
		try {
			statement = this.db.createStatement();
			
			if (statement == null) {
				throw new Exception("ERR 118 - Unable to prepare statement");
			}
			
			result = statement.executeQuery(sql);
			
			if (result == null) {
				System.err.print("Unable to get Query Result\n");
				throw new Exception("ERR 126 - Unable to communicate with database\n");
			}

		} 
		catch (Exception ex) {
			System.err.printf("ERROR!!  %s\n", ex.getMessage());
			System.err.print(ex.getStackTrace());
		} 
		
		return result;
	}
	
	public boolean getResultNextRecord(ResultSet result){
		
		try {
			if(result.isAfterLast()){
				// no more results to return -- not unusual
				return false;
			}
			// iterate to the next record and hand back the handle
			result.next();  // increment pointer to next record
		} 
		catch (Exception ex) {
			System.err.printf("ERROR!!  %s\n", ex.getMessage());
			System.err.print(ex.getStackTrace());
		} 
		
		return true;
	}
	
	/*
	 *  Connect to the mighty SQLite3 Datafile
	 *  
	 *  NOTE:  Unfortunatly, the current connector for SQLite will "connect" to
	 *         almost any file, or actually no file at all, and still return
	 *         a valid connection flag.  More extensive connection testing seems
	 *         to be required to actually validate that the file was there *and*
	 *         it is really a database, not a simple text file.
	 */
	private boolean Connect() throws ClassNotFoundException {
		
		String dbURL = "jdbc:sqlite:" + this.dbFname.trim();
		System.out.printf("Opening %s\n", dbURL);
			   
        try {
			// declare what driver to use for DriverManager
			Class.forName("org.sqlite.JDBC");
			
			this.db = DriverManager.getConnection(dbURL);
			
			return testConnection();  // return opposite of test.
        } 
		catch (SQLException ex) {
           System.err.printf("Fatal Error Connecting to DB at [%s]\nError: %s\n",dbURL,ex.getMessage());
        }
		
		return false;
	}
	
	/**
	 * Test Connection and determine if this looks like a STRATUX database
	 * 
	 * @param conn
	 *          a JDBC connection object
	 *
	 * @return true if a given connection object is a valid one; otherwise return
	 *         false.
	 */
	private boolean testConnection() {

		// Prepare and get result set.
		ResultSet result = getResultSet("SELECT id FROM traffic ORDER BY id DESC limit 1;");
		
		try {
			
			if (result.next()) {
				
				// connection object is valid and passes integrity check
				if (!result.getString(1).isEmpty()){
					return true;
				}
				
				// database fails integrity check.
				System.err.print("Database does not contain expected <traiffic> table!\n");
				throw new Exception("ERR 114 - Database is not STRATUX\n");
			}
			
			// it's garbage
			throw new Exception("ERR 141 - Unable to read as SQLite database\n");

		} 
		catch (Exception ex) {
			System.err.printf("ERROR!!  %s\n", ex.getMessage());
			System.err.print(ex.getStackTrace());
			return false;
		} 
    }
	
	/**
	 * Test Integrity of the Database 
	 * 
	 * @param conn
	 *          a JDBC connection object
	 *
	 * @return true if a given connection object is a valid one; otherwise return
	 *         false.
	 */
	private boolean testIntegrity() {

		// Prepare and get result set.
		ResultSet result = getResultSet("pragma integrity_check;");
		
		try {
			
			if (result.next()) {
				
				// connection object is valid and passes integrity check
				if (result.getString(1).equals("ok")){
					return true;
				}
				
				// database fails integrity check.
				System.err.printf("Database appears to be corrupted!\n%s\n",result.getString(1));
				throw new Exception("ERR 134 - Database is corrupted\n");
			}
			
			// it's garbage
			throw new Exception("ERR 141 - Unable to read as SQLite database\n");

		} 
		catch (Exception ex) {
			System.err.printf("ERROR!!  %s\n", ex.getMessage());
			System.err.print(ex.getStackTrace());
			return false;
		} 
    }
}
