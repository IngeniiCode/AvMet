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
import com.ingeniigroup.stratux.Repair.FixTrafficTable;


/**
 * @author David DeMartini - Ingenii Group LLC
 */
public class AvMet {
	
	private static boolean   waszipped;  // original file was gzipped
	private static boolean   rezip;      // re-zip the file
	private static boolean   keepdb;     // keep original DB 
	private static boolean   scrubdb;    // run a detainting process on the DB
	private static boolean   condense;   // remove adjacent similar records in database
	private static boolean   usetemp;    // write the extracted database to a temp file.
	private static String    sourcefile; // origin database file
	private static String    dbFname;
	private static StratuxDB DB;
	
	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) throws Exception {
		
		// perform initializations
		init(args);

		// set DB connection
		setDBconn();
		
		// check for cleaning and condensing operations
		if(AvMet.condense){
			// condense also performs the scrub operation
			FixTrafficTable.Condense(AvMet.DB);
		}
		else if (AvMet.scrubdb){
			// performs only the scrubbing operation
			FixTrafficTable.Fix(AvMet.DB);
		}
		
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
	private static void init(String[] args) throws Exception{
	
		if(args.length < 1){
			// No database file defined.. this is a fatal event
			throw new Exception("Must declare a database file to open as first parameter");
		}
		
		// store the sourcefile
		AvMet.sourcefile = args[0];
		
		// set keep flag if there is an arg.
		if(args.length>1){
			
			for(int i=1; i < args.length; i++){
				// look for options and set where found.
				switch(args[i].toLowerCase()){
					case "keep":
					case "keepdb":
						System.out.println("Keep DB!");
						AvMet.keepdb = true;
						break;
					case "untaint":
					case "untaintdb":
					case "scrub":
					case "scrubdb":
						System.out.println("Scrub DB!");
						AvMet.scrubdb = true;
						break;
					case "cond":
					case "condense":
						System.out.println("Condense DB!");
						AvMet.condense = true;
						break;
					case "tempdb":
					case "usetemp":
						System.out.println("Condense DB!");
						AvMet.usetemp = true;
						break;
					default:
						// no options selected.. 
				}
			}
		}
		
		// Begin processing the input file
		setFname();
		
	}
	
	
	/**
	 *   using supplied parameter, process filename, unzip if necessary
	 */
	private static void setFname(){
		
		
		System.out.printf("Attmpting to use input file '%s'\n",AvMet.sourcefile);
		
		// init and execute
		Gunzip GZ = new Gunzip();
		//System.out.printf("Was Zipped = %s\n", GZ.wasZipped());
		AvMet.dbFname   = GZ.unzipDB(AvMet.sourcefile,AvMet.usetemp); 
		AvMet.waszipped = GZ.wasZipped();    // set flag to cleanup after execution
		
		if(AvMet.dbFname.isEmpty()){
			System.err.printf("ERROR: Unable to located a suitable DB file; unable to use (%s)\n",AvMet.sourcefile);
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

		AvMet.DB.Disonnect();  // make call to disconnect.
		
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
		
		// Display start time
		traffic.getFirstTime();
		
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
