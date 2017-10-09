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
 * @repo https://github.com/IngeniiCode/AvMet
 * 
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
	
	private static boolean   waszipped;  // flag indicating original file was gzipped
	private static boolean   rezip;      // flag telling process to re-zip the file
	private static boolean   keepdb;     // flag telling process to keep original DB 
	private static boolean   untaintdb;  // run a detaining process on the DB
	private static String    dbFname;
	private static StratuxDB DB;
	
	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		
		// perform initializations
		init(args);
		
		// set DB connection
		setDBconn();
		
		// report the metrics
		reportMetrics();
		
		// cleanup
		cleanup();
		
	}
	
	/* 
	 *  =============================
	 *        P R I V A T E   
	 *  =============================
	*/
	
	/**
	 * Init  do some prep work before running 
	 */
	private static void init(String[] args){
	
		// set dbFname
		setFname(args[0]);
		
		// set keep flag if there is an arg.
		if(args.length>1){
			for(int i=0;i<args.length;i++){
				// look for options and set where found.
				switch(args[i].toLowerCase()){
					case "keep":
					case "keepdb":
						AvMet.keepdb = true;
						break;
					case "untaint":
					case "untaintdb":
					case "scrub":
					case "scrubdb":
						AvMet.untaintdb = true;
						break;
					default:
						
				}
			}
		}
	}
	
	
	/**
	 *   using supplied parameter, process filename, unzip if necessary
	 */
	private static void setFname(String fName){
		
		System.out.printf("Attmpting to use input file '%s'\n",fName);
		
		// init and execute
		Gunzip GZ = new Gunzip();
		//System.out.printf("Was Zipped = %s\n", GZ.wasZipped());
		AvMet.dbFname   = GZ.unzipDB(fName);
		AvMet.waszipped = GZ.wasZipped();    // set flag to cleanup after execution
		
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
	 *  Cleanup any leftover files
	 */
	private static void cleanup(){
		
		// call the db connector's cleanup function, but override the waszipped
		// action if the 'keep' option was present
		AvMet.DB.Cleanup((AvMet.keepdb) ? false : AvMet.waszipped);
		
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
		traffic.getSlowest();

		// Highest altitude
		traffic.getHighest();
				
		// Lowest altitude ( greater than 0 )

		// Most contact events

		// Fewest contact events

		// Closest contact

		
		// Furthest contact

		
		// Squawk counts

		// Emergency Squawk events
		traffic.reportEmergencies();
		
		// Flyover events (less than 1nm range)


	}
}
