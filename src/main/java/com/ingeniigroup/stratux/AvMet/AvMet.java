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
 * @version 0.1.0
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
import java.sql.SQLException;
import static java.util.Arrays.asList;

import joptsimple.OptionParser;
import joptsimple.OptionSet;


/**
 * @author David DeMartini - Ingenii Group LLC
 */
public class AvMet {
	
	private static boolean   waszipped;        // original file was gzipped

	private static String    sourcefile;       // origin database file
	private static String    dbFname;
	
	private static OptionSet OPT;              // object for OptionSet
	private static StratuxDB DB;               // obejct for SQLite STRATUX db
	private static MySQL     MySQL;            // object for MYSQL export
	private static JSON      JSON;             // object for JSON export
	private static XLSX      XLSX;             // object for XSLS export
	
	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) throws Exception {
		
		// define the opt object
		OptionParser Parse = initParser();
		try {
			
			// perform the options parsing
			AvMet.OPT = Parse.parse(args);  // parse those args!
			
		} 
		catch (Exception ex) {
			// something didn't work out in the parser checking -- display help
			Parse.printHelpOn( System.out );
		}
		
		// if there is a database to rad, begin processing
		if(!OPT.has("nodb")){
			
			// Begin processing the input file
			AvMet.sourcefile = AvMet.OPT.valueOf("db").toString();
			setFname();
		
			// set DB connection
			setDBconn();

			// check for cleaning and condensing operations
			if(OPT.has("pack")){
				// condense also performs the scrub operation
				FixTrafficTable.Condense(AvMet.DB);
			}
			else if (OPT.has("scrub")){
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
	 * OptionParser configuration 
	 * 
	 * Configures the accepted parameters for application
	 * 
	 * @return OptionParser
	 */
	private static OptionParser initParser() {
		
		// init the object 
		OptionParser parser = new OptionParser();

		try {			
			// configure the object 
			parser.accepts( "nodb"      ,"run without an input db" );
			parser.accepts( "db"        ,"path to source STRATUX SQLite3 database file").requiredUnless( "nodb" ).withRequiredArg(); // required unless nodb used
			parser.accepts( "keepdb"    ,"keep source database file" );
//			parser.accepts( "rezip"     ,"re-zip extracted database file (only when extraction was performed)");
			parser.accepts( "scrub"     ,"scrub out obviously bad data" );
			parser.accepts( "pack"      ,"remove possitionaly duplicate entries (aggressive)" );
			parser.accepts( "tempdb"    ,"extract to a tempoary db file ").withOptionalArg();
			parser.accepts( "keepdupes" ,"retain duplicate traffic records, only has impact when --scrub used" );
			parser.accepts( "verbose"   ,"be noisy" );
			parser.accepts( "useprefix" ,"use a date-time stamp for exported files" ).withOptionalArg();
			// MySQL Export Flags
//			parser.accepts( "export_mysql",         "-- not currently implemented --" );
			parser.accepts( "export_mysql_schema",  "export MySQL CREATE TABLE statements to build database" );
			// Squawk Exports
			parser.accepts( "export_squawk_mysql",  "export MySQL INSERTS to populate the squawk lookup table" );
			parser.accepts( "export_squawk_json",   "export Squawk codes into a Json data object" );
			// XLSX Export Flags
//			parser.accepts( "export_xlsx",          "-- not currently implemented --" );

			// Enable Help Options
			parser.acceptsAll( asList( "h", "?" ), "show help" ).forHelp();
			
		}
		catch (Exception ex){  }
		
		// return the object
		return parser;
	}
	
	/**
	 *   using supplied parameter, process filename, unzip if necessary
	 */
	private static void setFname(){
		
		// init and execute
		Gunzip GZ = new Gunzip();
		
		AvMet.dbFname   = GZ.unzipDB(AvMet.sourcefile,OPT.has("tempdb")); 
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
		AvMet.DB = new StratuxDB(AvMet.dbFname,OPT.has("verbose"));
		
		// notify of any relevant flags
		AvMet.DB.KeepDupes(OPT.has("keepdupes"));
		
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
		AvMet.DB.Cleanup((OPT.has("keepdb")) ? false : AvMet.waszipped);
		
	}
	
	/**
	 *  run the metrics pulls from the database
	 */
	private static void reportMetrics() throws SQLException{
		
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
		
		if(OPT.has("export_mysql_schema")){
			AvMet.MySQL = new MySQL(OPT.has("useprefix"));
			AvMet.MySQL.ExportSchema();
		}
		
		if(OPT.has("export_squawk_mysql")){
			AvMet.MySQL = new MySQL(OPT.has("useprefix"));
			AvMet.MySQL.ExportSquawkData();
		}
		
		if(OPT.has("export_squawk_json")){
			AvMet.JSON = new JSON(OPT.has("useprefix"));
			AvMet.JSON.ExportSquawkData();
		}
		
		if(OPT.has("export_mysql")){
			AvMet.MySQL = new MySQL(OPT.has("useprefix"));
			AvMet.MySQL.ExportData();
		}
		
		if(OPT.has("export_xlsx")){
			AvMet.XLSX = new XLSX(OPT.has("useprefix"));
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
