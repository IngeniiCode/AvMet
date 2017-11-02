/**
 *  STRATUX  SQLite3 database interaction utility
 * 
 * @since 6 October 2017
 * @author David DeMartini
 * @serial ig0003-am
 * @version 0.1.0
 * @see http://www.ingeniigroup.com/stratux/avmet
 * @repo https://github.com/IngeniiCode/AvMet
 */
package com.ingeniigroup.stratux.dbConnect;

import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Iterator;

/**
 *
 * @author david
 */
public class StratuxDB {
	
	private boolean connected;
	private boolean verbose;
	private boolean keepdupes;
	private String dbFname;
	private String dbName;
	private Connection db;
	
	/*
	 *  Make DB Connection and prepare to handle queries.
	 */
	public StratuxDB(String dbFname) {
		initDB(dbFname,false);
	}
	public StratuxDB(String dbFname, boolean verbose){
		initDB(dbFname,verbose);
	}
	
	
	/**
	 *  Make the required DB connections
	 * 
	 * @param String dbFname - filename of SQLite3 database file (or archive)
 	 * @param boolean verbose - flag determines verbosity 
	 * 
	 */
	public void initDB(String dbFname, boolean verbose){
		
		// set verbosity
		this.verbose = verbose;
		
		try {
			
			// init SQLite Connection
			this.dbFname   = dbFname;
			this.connected = Connect();

			// check to see if a database name was returned
			if(!this.connected){
				throw new Exception("No Database Found in File: " + this.dbFname);
			}
			
			// return database name and set connected flag
			//System.out.print("Connected....\n");
			
		}
		catch (Exception ex){
			System.err.printf("Fatal Error Connecting to DB at [%s]\nError: %s\n",this.dbFname,ex.getMessage());
			System.exit(9);
		}
		
	}
	
	/**
	 *  Return flag indicating db is open for business
	*/
	public boolean Connected(){
		return this.connected;
	}
	
	/**
	 *  Set / Clear keepdupes flag
	 */
	public boolean KeepDupes(){
		return this.keepdupes;
	}
	public boolean KeepDupes(boolean keepdupes){
		return this.keepdupes = keepdupes;
	}
	
	/**
	 *  Return verbosity flag
	 */
	public boolean Verbose(){
		return this.verbose;
	}
	
	/** 
	 *  Return the connection object
	 */
	public Connection db(){
		return this.db;
	}
	
	/**
	 *  Cleanup 
	 * 
	 *  Remove the sqlite3 artifacts after a database is opened.
	 * 
	 *  Files are suffixed with  -wal  &  -shm
	 */
	public void Cleanup(){
		Cleanup(false);  // call function and set false flag
	}
	public void Cleanup(boolean deleteDBfile){
		
		List<String> files = new ArrayList<String>();
		
		// these are always removed.
		files.add(String.format("%s-wal",dbFname));
		files.add(String.format("%s-shm",dbFname));
		
		// optional uncompressed db file removal
		if(deleteDBfile){
			files.add(dbFname);  // add name of uncompressed DB
		}
		
		try {
		
			System.out.println("Cleaning up artifact files..");
			
			files.forEach(filename -> {
				File unwanted = new File(filename);
				if(unwanted.exists() && !unwanted.isDirectory()) { 
					// delete the file!
					unwanted.delete();
				}
			});
		}
		catch (Exception ex){
			System.err.println("Unable to unlink database artifacts -- you may need to clean them up individually.");
		}
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
	
	/**
	 * 
	 * @param result
	 * @return 
	 */
	public boolean getResultNextRecord(ResultSet result){
		
		try {
			// iterate to the next record and hand back the handle
			return result.next();  // increment pointer to next record
		} 
		catch (Exception ex) {
			System.err.printf("ERROR!!  %s\n", ex.getMessage());
		} 
		return false;
	}
	
	/**
	 * Delete a record from table where record_id is an int
	 * 
	 * @param table
	 * @param primary_field
	 * @param record_id
	 * 
	 * @return 
	 */
	public boolean deleteRecord(String table, String primary_field, int record_id){
		
		String query = String.format("DELETE FROM %s WHERE %s=%d",table,primary_field,record_id);
		
		try {	
			Statement statement = this.db.createStatement();
			statement.executeUpdate(query);
			//System.out.println(query);
		} 
		catch (SQLException ex) {
           System.err.printf("Error: %s in %s\n",ex.getMessage(),query);
        }
		
		return true;
	}
	
	/**
	 * Delete a record from table where record_id is an String
	 * 
	 * @param table
	 * @param primary_field
	 * @param record_id
	 * 
	 * @return 
	 */
	public boolean deleteRecord(String table, String primary_field, String record_id){
		
		String query = String.format("DELETE FROM %s WHERE %s='%d'",table,primary_field,record_id);
		
		try {
			Statement statement = this.db.createStatement();
			statement.executeUpdate(query);
			//System.out.println(query);
		} 
		catch (SQLException ex) {
			System.err.printf("Error: %s in %s\n",ex.getMessage(),query);
		}
		
		return true;
	}
	
	/**
	 * SQL Do Update 
	 * 
	 * @param String sql -- query to blindly execute
	 * 
	 * @return boolean true|false
	 * @throws ClassNotFoundException 
	 */
	public boolean sqlExecute(String query){
		
		try {
			Statement statement = this.db.createStatement();
			//statement.executeUpdate(query);
			statement.execute(query);
			return true;
		} 
		catch (SQLException ex) {
			System.err.printf("Error: %s\tquery: { %s }\n",ex.getMessage(),query);
		}
		
		return false;
	}
	
	/**
	 * SQL Do Update 
	 * 
	 * @param String sql -- query to blindly execute
	 * 
	 * @return boolean true|false
	 * @throws ClassNotFoundException 
	 */
	public boolean sqlBatch(List<String> batch){
		
		try {
			
			Statement statement = this.db.createStatement();

			// create an iterator
			Iterator<String> sqlIterator = batch.iterator();

			// iterate 
			while (sqlIterator.hasNext()) {
				statement.addBatch(sqlIterator.next());
			}
			
			// write that batch
			statement.executeBatch();
			
		} 
		catch (SQLException ex) {
			System.err.printf("Error: %s\tProblems with batch\n",ex.getMessage());
		}
		finally {
			// finally.. we're done?
		}
		
		return false;
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
	public boolean Connect() throws ClassNotFoundException {
		
		String dbURL = "jdbc:sqlite:" + this.dbFname.trim();
			   
		try {
			// declare what driver to use for DriverManager
			Class.forName("org.sqlite.JDBC");
			
			this.db = DriverManager.getConnection(dbURL);
			
			return testConnection();  // return opposite of test.
		} 
		catch (SQLException ex) {
			System.err.printf("Fatal Error Connecting to DB at [%s]\nError: %s\n",dbURL,ex.getMessage());
			System.exit(9);
		}
		
		return false;
	}
	
	/**
	 *   Disconnect from DB
	 */
	public boolean Disonnect(){
		
		try {
			if(!this.db.isClosed()){
				this.db.close();
				return true; // disconnect seemed to work OK.
			}
		}
		catch (Exception ex) {
			System.err.println("Error occured while trying to close connection\t" + ex.getMessage());
		}
		return false;  // disconnect somehow failed.
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
