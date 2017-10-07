/**
 * AvMet - Aviation Metrics Application
 *
 * Proof of Concept demonstrator project
 * 
 * <Description>
 * 
 * Application is provided path to a STRAUX generated sqlite3 database file. 
 *
 * If file does not exist, signal a fatal error and exit.
 *
 * If file is compressed (expected gzip compression) decompress
 *
 * Check file for type of sqlite3 database; flag fatal error if it is not.
 *
 * Verify database contains expected schema; flag fatal error if it is not.
 *
 * Search required tables for the metric requested.  Default is –all metrics. 
 *
 * 
 * @since 6 October 2017
 * @author David DeMartini
 * @serial ig0003-am
 * @version 0.0.1
 * @see http://www.ingeniigroup.com/stratux/avmet
 * 
 */
package com.ingeniigroup.stratux.AvMet;

import com.ingeniigroup.stratux.dbConnect.*;
import com.ingeniigroup.stratux.dbReader.*;
import com.ingeniigroup.stratux.Contact.Contact;

/**
 * @author David DeMartini - Ingenii Group LLC
 */
public class AvMet {
	
	private static boolean rezip;  // flag telling process to re-zip the file
	private static String  dbFname;
	private static StratuxDB DB;
	
	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		
		// set dbFname
		setFname(args[0]);
		
		// set DB connection
		setDBconn();
		
		// report the metrics
		reportMetrics();
		
		
		
	}
	
	/* 
	 *  =============================
	 *        P R I V A T E   
	 *  =============================
	*/
	
	/**
	 *   using supplied parameter, process filename, unzip if necessary
	 */
	private static void setFname(String fName){
		
		System.out.printf("Attmpting to use input file '%s'\n",fName);
		
		// init and execute
		Gunzip GZ = new Gunzip();
		//System.out.printf("Was Zipped = %s\n", GZ.wasZipped());
		AvMet.dbFname = GZ.unzipDB(fName);
		
		if(AvMet.dbFname.isEmpty()){
			System.err.printf("ERROR: Unable to located a suitable DB file; unable to use (%s)\n",fName);
			System.exit(13);
		}
		else {
			System.out.printf("Using Database File: '%s'\n",AvMet.dbFname);
		}
	}
	
	/**
	 *   set the DB connection and verify connectivity
	 */
	private static void setDBconn(){
		
		// set the DB connection
		AvMet.DB = new StratuxDB(AvMet.dbFname);
		
		if(!AvMet.DB.Connected()){
			System.err.printf("ERROR: Unable to connect to database\n");
			System.exit(19);
		}
		
	}
	
	/**
	 *  run the metrics pulls from the database
	 */
	private static void reportMetrics(){
		
		// setup the interfaces
		traffic traffic = new traffic(AvMet.DB);
		
		// Highest speed 
		traffic.getFastest(); 

		// Slowest speed ( greater than 25 kts)
		
	
		// Highest altitude

		// Lowest altitude ( greater than 0 )

		// Most contact events

		// Fewest contact events

		// Closest contact

		// Furthest contact

		// Squawk counts

		// Emergency Squawk events

		// Flyover events (less than 1nm range)


	}
}
