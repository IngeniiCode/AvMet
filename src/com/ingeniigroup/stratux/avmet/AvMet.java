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
 * @version 0.0.2
 * @see http://www.ingeniigroup.com/stratux/avmet
 * @repo https://github.com/IngeniiCode/AvMet
 * 
 * 
 */
package com.ingeniigroup.stratux.AvMet;

import com.ingeniigroup.stratux.dbConnect.*;
import com.ingeniigroup.stratux.dbReader.*;
import com.ingeniigroup.stratux.Export.DB.*;
import com.ingeniigroup.stratux.Export.File.*;
import com.ingeniigroup.stratux.Repair.FixTrafficTable;
import java.io.IOException;


/**
 * @author David DeMartini - Ingenii Group LLC
 */
public class AvMet {
	
	private static boolean   waszipped;           // original file was gzipped
	private static boolean   rezip;               // re-zip the file
	private static boolean   no_database;         // run tool without a source database
	private static boolean   keepdb;              // keep original DB 
	private static boolean   scrubdb;             // run a detainting process on the DB
	private static boolean   condense;            // remove adjacent similar records in database
	private static boolean   usetemp;             // write the extracted database to a temp file.
	private static boolean   verbose;             // show more information during execution
	private static boolean   keepdupes;           // do not run the duplicate timestamp scrubber
	private static boolean   export_useprefix;    // flag tells export processes to use a local timestamp in file prefixes
	private static boolean   export_mysql;        // export data in MySQL DB Load format
	private static boolean   export_mysql_schema; // export the database schema and basic data.
	private static boolean   export_squawk_mysql; // export Squawk insert commands and DB config.
	private static boolean   export_squawk_json;  // export Squawk Data as JSON.
	private static boolean   export_xlsx;         // export data in  XLSX format

	private static String    sourcefile;          // origin database file
	private static String    dbFname;
	
	private static StratuxDB DB;                  // obejct for SQLite STRATUX db
	private static MySQL     MySQL;               // object for MYSQL export
	private static JSON      JSON;                // object for JSON export
	private static XLSX      XLSX;                // object for XSLS export
	
	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) throws Exception {
		
		// perform initializations
		init(args);

		// if there is a database to rad, begin processing
		if(!AvMet.no_database){
			
			// Begin processing the input file
			setFname();
		
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
		
		// create export files if requested  (** this might need to be integrated into the scrubbing processes) 
		exportData();
		
	}
	
	/* 
	 *  =============================
	 *        P R I V A T E   
	 *  =============================
	*/
	
	/**
	 * Init  do some prep work before running 
	 */
	private static void init(String[] args) throws Exception {
	
		if(args.length < 1){
			// No database file defined.. this is a fatal event
			System.out.println("Must declare a database option (nodb | <db_file_path>) as first parameter");
			throw new Exception("Missing required parameter");
		}
		
		// process the database options fl
		switch(args[0].toLowerCase().trim()){
			case "nodb":
				AvMet.no_database = true;
				break;
			case "":
				throw new Exception("Database option was empty/null");  // no need for a break; here
			default:
				// store the sourcefile
				AvMet.sourcefile = args[0];
		}
		
		// set keep flag if there is an arg.
		if(args.length>1){
			
			for(int i=1; i < args.length; i++){
				// look for options and set where found.
				switch(args[i].toLowerCase()){
					case "keepdb":
						if(AvMet.verbose) System.out.println("Keep DB!");
						AvMet.keepdb = true;
						break;
					case "keepdupes":
						if(AvMet.verbose) System.out.println("Keep Dupe Records!");
						AvMet.keepdupes = true;
						break;
					case "scrub":
						if(AvMet.verbose) System.out.println("Scrub DB!");
						AvMet.scrubdb = true;
						break;
					case "cond":
					case "condense":
						//if(AvMet.verbose) System.out.println("Condense DB!");
						//AvMet.condense = true;
						break;
					case "tempdb":
					case "usetemp":
						if(AvMet.verbose) System.out.println("Condense DB!");
						AvMet.usetemp = true;
						break;
					case "verbose":
						AvMet.verbose = true;
						if(AvMet.verbose) System.out.println("Be Verbose!");
						break;	
						
					/* Export Options */
					case "useprefix":
						AvMet.export_useprefix = true;
						break;
					case "export_mysql_schema":
						AvMet.export_mysql_schema = true;
						break;
					case "export_mysql":
						AvMet.export_mysql = true;
						break;
					case "export_xlsx":
						AvMet.export_xlsx = true;
						break;
					case "export_squawk_mysql":
						AvMet.export_squawk_mysql = true;
						break;
					case "export_squawk_json":
						AvMet.export_squawk_json = true;
						break;
					default:
						System.out.println("Invalid option " + args[i]);
						// no options selected.. 
				}
			}
		}
		else {
			if(AvMet.no_database){
				// no database defiend by no other options selected, this a non-op exception
				System.out.println("Option 'nodb' has no standalone function -- Program exiting in error.");
				throw new Exception("'nodb' parameter error");
			}
		}
	}
	
	/**
	 *   using supplied parameter, process filename, unzip if necessary
	 */
	private static void setFname(){
		
		// init and execute
		Gunzip GZ = new Gunzip();
		
		AvMet.dbFname   = GZ.unzipDB(AvMet.sourcefile,AvMet.usetemp); 
		AvMet.waszipped = GZ.wasZipped();    // set flag to cleanup after execution
		
		if(AvMet.dbFname.isEmpty()){
			System.err.printf("ERROR: Unable to locate a suitable DB file; unable to use (%s)\n",AvMet.sourcefile);
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
		AvMet.DB = new StratuxDB(AvMet.dbFname,AvMet.verbose);
		
		// notify of any relevant flags
		AvMet.DB.KeepDupes(keepdupes);
		
		if(!AvMet.DB.Connected()){
			System.err.printf("ERROR: Unable to connect to database\n");
			System.exit(19);
		}
		
	}
	
	/**
	 *  Cleanup any leftover files
	 */
	private static void cleanup(){

		// close connections
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
		traffic.getLowest();

		// Most contact events
		

		// Fewest contact events
		

		// Closest contact
		traffic.getClosest();
		
		// Furthest contact
		traffic.getFurthest();
		
		// Squawk counts
		

		// Emergency Squawk events
		traffic.reportEmergencies();
		
		// Non-Emergency special Squawk events
		traffic.reportSpecialIdents();
				
		// Flyover events (less than 1nm range)
		

	}
	
	/**
	 * Export file processing
	 */
	private static void exportData() throws IOException, Exception {
		
		if(AvMet.export_mysql_schema){
			AvMet.MySQL = new MySQL(AvMet.export_useprefix);
			AvMet.MySQL.ExportSchema();
		}
		
		if(AvMet.export_squawk_mysql){
			AvMet.MySQL = new MySQL(AvMet.export_useprefix);
			AvMet.MySQL.ExportSquawkData();
		}
		
		if(AvMet.export_squawk_json){
			AvMet.JSON = new JSON(AvMet.export_useprefix);
			AvMet.JSON.ExportSquawkData();
		}
		
		if(AvMet.export_mysql){
			AvMet.MySQL = new MySQL(AvMet.export_useprefix);
			AvMet.MySQL.ExportData();
		}
		
		if(AvMet.export_xlsx){
			AvMet.XLSX = new XLSX(AvMet.export_useprefix);
			AvMet.XLSX.ExportData();
		}
		
	}
}

/**  ToDo!!  
 * 
 *  * add config file capability, look in ~/.avmet.conf  and  ./.avment.conf
 *    for default program configurations such as output database files, flags
 *    re-zip options etc. etc.    
 * 
 */
