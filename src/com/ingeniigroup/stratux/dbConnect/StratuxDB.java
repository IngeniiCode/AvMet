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
	 * Test Validity of a Connection
	 * 
	 * @param conn
	 *          a JDBC connection object
	 *
	 * @return true if a given connection object is a valid one; otherwise return
	 *         false.
	 */
	private boolean testConnection() {

		ResultSet result    = null;
		Statement statement = null;
		
		try {
			statement = this.db.createStatement();
			
			if (statement == null) {
				throw new Exception("ERR 118 - Unable to prepare statement");
			}
			
			result = statement.executeQuery("pragma integrity_check;");
			
			if (result == null) {
				System.err.print("Unable to locate 'status' table in database -- does not look like STRATUX\n");
				throw new Exception("ERR 126 - Not STRATUX database");
			}

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
