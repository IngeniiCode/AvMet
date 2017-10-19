/**
 * SQLite STRATUX <traffic> table repair utility
 * 
 * Centralized file testing methods
 * 
 * @since 12 October 2017
 * @author David DeMartini
 * @serial ig0003-am
 * @version 0.1.0
 * @see http://www.ingeniigroup.com/stratux/avmet
 * @repo https://github.com/IngeniiCode/AvMet
 */
package com.ingeniigroup.stratux.Repair;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import com.ingeniigroup.stratux.dbConnect.StratuxDB;
import java.sql.ResultSet;

/**
 *
 * @author david
 */
public class FixTrafficTable {
	
	private static StratuxDB DB;
	private static List<Integer> aircraft;
	private static List<String>  sql_batch_delete = new ArrayList<String>();
	private static boolean condense;
	private static boolean verbose;
	private static boolean keepdupes;
	private static int  total_contacts   = 0;
	private static int  error_contacts   = 0;
	private static int  deleted_contacts = 0;
	
	/**
	 * Constructor -- needs to be passed the StratuxDB object
	 * @param dbconn 
	 */
	public static void Fix(StratuxDB dbconn) {
		Fix(dbconn,false);
	}
	public static void Fix(StratuxDB dbconn,boolean verbose) {	
		System.out.println("Scrubbing tainted data");
		
		FixTrafficTable.verbose  = (verbose) ? verbose : dbconn.Verbose(); // use flag or check dbconnector
		FixTrafficTable.keepdupes = dbconn.KeepDupes();  // test for the keepdupes flag
		
		FixTrafficTable.DB = dbconn;  // import the connection
		
		// collect aircraft designators
		collectAircraft();
		
		// grind through the aircraft records.
		fixTrafficData();
		
		// announce completion of this state in processing
		System.out.printf("Processed %d records, removed %d errors\n",FixTrafficTable.total_contacts,FixTrafficTable.error_contacts);
		
	}
	
	/**
	 * Perform a repair and condense on the database. 
	 * 
	 * @param dbconn 
	 */
	public static void Condense(StratuxDB dbconn) {
		Condense(dbconn,false);
	}
	public static void Condense(StratuxDB dbconn,boolean verbose) {
		
		// set the condenser flag
		FixTrafficTable.condense = true;
		FixTrafficTable.verbose  = (verbose) ? verbose : dbconn.Verbose(); // use flag or check dbconnector
		
		// call the fix function
		Fix(dbconn);
		
		System.out.printf("Removed %d duplicates\n",FixTrafficTable.deleted_contacts);
	}
	
	/**
	 * Construct an iteration wrapper to call the individual aircraft repair processes.
	 * 
	 * @return boolean true | false
	 */
	private static boolean fixTrafficData() {
	
		// remove obvious gross errors
		fixGrossErrors();
		
		// create an iterator
		Iterator<Integer> aircraftIterator = FixTrafficTable.aircraft.iterator();
		
		// iterate 
		while (aircraftIterator.hasNext()) {
			fixAircraftLog(aircraftIterator.next());
		}
		
		// check batch to see if it has entries, if so then execute them
		if(!FixTrafficTable.sql_batch_delete.isEmpty()){
			System.out.printf("Removing %d bad traffic records\n", FixTrafficTable.sql_batch_delete.size());
			FixTrafficTable.DB.sqlBatch(FixTrafficTable.sql_batch_delete);
		}
		
		return true;
	}
	
	/**
	 * Remove records with duplicate timestamps 
	 * 	  
	 * @param int Iaco
	 * 
	 * @return boolean  true|false
	 */
	private static boolean dumpTimestampDuplicates(int Icao){
				
		// more aggressive de-duplication..  doesn't really matter what it says in the duplicate timestamps, there can be only one.. 
		// NOTE -- removing these unnecessary sorts improved speed by ~ 10%   
		//String sql = String.format("SELECT Icao_addr,Timestamp,(count(*) -1) AS duplicates FROM traffic WHERE Icao_addr=%d GROUP BY Timestamp HAVING duplicates > 0 ORDER BY Timestamp ASC;",Icao);
		String sql = String.format("SELECT Icao_addr,Timestamp,(count(*) -1) AS duplicates FROM traffic WHERE Icao_addr=%d GROUP BY Alt,Speed,Timestamp HAVING duplicates > 0",Icao);
		
		// iterate
		try {
			// prepare, execute query and get resultSet
			ResultSet result = FixTrafficTable.DB.getResultSet(sql);  // using aggressive filter
			
			// check to see if there is anything to process
			if(!result.isBeforeFirst()){
				// No duplicates found in aircraft's record set
				return false;  // Bail Out!
			}
			
			List<String> batch = new ArrayList<String>();
			
			// id,Iaco_addr,Alt,Speed
			do {
				
				if (FixTrafficTable.DB.getResultNextRecord(result)) {
					String timestamp = result.getString("Timestamp");
					int    dupecount = result.getInt("duplicates");
					
					// ** NOTE ** Because the JDBC driver for SQLite was not compiled 
					// with  ORDER and LIMIT enabled for delete.. it requires the use 
					// of a sub-select to feed the IDs into an outer DELET query.  
					// issue existed as of  sqlite-jdbc-3.20.0.jar  (October 2017)
					String delsql = String.format("DELETE FROM traffic WHERE id IN(SELECT id FROM traffic WHERE Timestamp=\"%s\" AND Icao_addr=%s ORDER BY id ASC LIMIT %d)",timestamp,Icao,dupecount);
					// add to the batch fo deletes to be performed
					batch.add(delsql);
								
				}
				
			} while (!result.isAfterLast());  // be looping..
			
			// send batch to SQL for processing as a batch
			if(!batch.isEmpty()){
				if(FixTrafficTable.verbose) System.out.printf("%d -- Removing %d event duplicates\n",Icao,batch.size());
				FixTrafficTable.DB.sqlBatch(batch);
			}
			
			return true;  // execution completed
		}
		catch (Exception ex){
			System.err.printf("dumpTimestampDuplicates Error: %s\n",ex.getMessage());
			ex.printStackTrace();
 		}
		
		return false;  // nothing happened.
	}
	
	/**
	 *  Fix an individual aircraft's screwed up records
	 * 
	 * @param Icao -  integer ICAO24 value
	 * 
	 * @return boolean 
	 */
	private static boolean fixAircraftLog(int Icao) {
		
		if(FixTrafficTable.verbose) System.out.printf("* Processing %d\n",Icao);
		
		if(!FixTrafficTable.keepdupes){
			// first remove duplicates for a given aircraft
			dumpTimestampDuplicates(Icao);
		}
		
		FixCallsigns(Icao);
		
		// Select all a specific aircraft's records 
		String sql = String.format("SELECT id,Icao_addr,Reg,Tail,Alt,VVel,Speed,Distance,Timestamp FROM traffic WHERE Icao_addr=%d ORDER BY id ASC;",Icao);  // removed nasty sorting colomns 
		
		try {
			// prepare, execute query and get resultSet
			ResultSet result = FixTrafficTable.DB.getResultSet(sql);  // using aggressive filt4r 
			
			// check to see if there is anything to process
			if(!result.isBeforeFirst()){
				// No emergencies to report
				if(FixTrafficTable.verbose) System.out.printf("Aircraft %d not found in table\n",Icao);
				return false;  // Bail Out!
			}
			
			// define the variables that will persist.
			int id             = -1;
			int speed          = -1;
			int altitude       = -1;
			int distance       = -1;
			int vertical_vel   = -1;
			int prev_speed;
			int prev_altitude;
			int prev_distance;
			
			// id,Iaco_addr,Alt,Speed
			do {
				
				// set last record found into previous vars
				prev_speed    = speed;
				prev_altitude = altitude;
				prev_distance = distance;
				
				if (FixTrafficTable.DB.getResultNextRecord(result)) {
					
					id           = result.getInt("id");
					speed        = result.getInt("speed");
					altitude     = result.getInt("Alt");
					distance     = result.getInt("Distance");
					vertical_vel = result.getInt("VVel");
					
					// test altitude
					if(badAltitude(altitude,prev_altitude,vertical_vel)){
						if(FixTrafficTable.verbose) printDelaError("Altitude",Icao,prev_altitude,altitude);
						delIdSql(id);
						continue;
					}
					
					// test speed
					if(badSpeed(speed,prev_speed)){
						if(FixTrafficTable.verbose) printDelaError("Speed",Icao,prev_speed,speed);
						delIdSql(id);
						continue;
					}
					
					// test distance
					if(badSpeed(distance,prev_distance)){
						if(FixTrafficTable.verbose) printDelaError("Distance",Icao,prev_distance,distance);
						delIdSql(id);
						continue;
					}
					
					// perform condense operation if selected
					if(FixTrafficTable.condense){
						if(isDuplicate(altitude,speed,prev_altitude,prev_speed)){
							delIdSql(id);
						}
					}
				}
				
			} while (!result.isAfterLast());  // be looping.. 

			// increment contacts counter
			FixTrafficTable.total_contacts += FixTrafficTable.aircraft.size();
			
			return true;
		}
		catch (Exception ex){
			System.err.printf("fixAircraftLog Error: %s\n",ex.getMessage());
			ex.printStackTrace();
 		}
		
		return true;
	}

	/**
	 *  Bad Altitude Comparitor 
	 * 
	 */
	private static boolean badAltitude(int altitude,int prev_altitude,int vertical_vel){
		
		if(prev_altitude == -1 || prev_altitude < 1000){
			// this is a starting record.. return false;
			return false;
		}
		
		// calculate the altitude difference as an absolute percentage
		int alt_diff = Math.abs(prev_altitude - altitude);
		
		// check to see if the delta is more than 100% below 5000 and 50% above 
		if(prev_altitude < 5000){
			return ((alt_diff / prev_altitude) > 1.0) ? true : false;
		}
		else {
			return ((alt_diff / prev_altitude) > 0.5) ? true : false;
		}
		
	}
	
	/** Bad Speed Comparitor
	 * 
	 * 
	 */
	private static boolean badSpeed(int speed, int prev_speed){
		if(prev_speed == -1 || prev_speed < 100){
			// this is a starting record.. return false;
			return false;
		}
		
		// calculate the altitude difference as an absolute percentage
		int speed_diff = Math.abs(prev_speed - speed);
		
		// check to see if the delta is more than 100% below 5000 and 50% above 
		if(prev_speed < 5000){
			return ((speed_diff / prev_speed) > 1.0) ? true : false;
		}
		else {
			return ((speed_diff / prev_speed) > 0.5) ? true : false;
		}
		
	}
	
	/**
	 * Look for missing Tail (callsign) records and use SQL to fix them
	 * 
	 * This query will update the Tail (callsign) to a good stored Callsign if 
	 * in the aircrafts log, and also removes any  Tail number pollution when it 
	 * can be confirmed by checking equality with the Reg record.  
	 * Example:  query will re-align these three distinct cases found in an
	 * example aircraft
	 * 
	 *		10749524|ATN3853|N359AZ
	 *		10749524|N359AZ|N359AZ
	 *		10749524||N359AZ
	 * 
	 * Operation is safe for fixing records where Tail is missing from some records
	 * and the Reg value is missing completely:
	 * 
	 *		7455314||
	 *		7455314|KAL277|
	 * 
	 * @param Icao 
	 */
	private static void FixCallsigns(int Icao){
		
		// Fix it in one shot using some subquery action
		FixTrafficTable.DB.sqlExecute(String.format("UPDATE traffic SET Tail=(SELECT CASE WHEN Tail IS NOT '' THEN Tail WHEN Reg IS NOT '' THEN Reg ELSE '-------' END AS Callsign FROM traffic WHERE Icao_addr=%d AND (Tail IS NOT '' OR Reg IS NOT '')) WHERE Icao_addr=%d",Icao,Icao));
	
	}
	
	/**
	 * Detect Duplicate relevant record values
	 * 
	 * @param int altitude
	 * @param int speed
	 * @param int prev_altitude
	 * @param int prev_speed
	 * 
	 * @return boolean  true|false
	 */
	private static boolean isDuplicate(int altitude,int speed,int prev_altitude,int prev_speed){
		if(speed == prev_speed && altitude == prev_altitude){
			return true;
		}
		return false;
	}
	
	/**
	 *  Get unique list of the aircraft addresses
	 * 
	 *  Run a distinct select for all the unique Icao codes in the traffic database
	 *  and stuff those into an array list for processing by other components.
	 */
	private static boolean collectAircraft(){
		
		// get distinct list of the aircraft Icao_addrs
		FixTrafficTable.aircraft = new ArrayList<Integer>();
		
		// create the query and construct a command
		String sql = "SELECT DISTINCT Icao_addr FROM traffic ORDER BY Icao_addr ASC";
		
		try {
			// prepare, execute query and get resultSet
			ResultSet result = FixTrafficTable.DB.getResultSet(sql);
			
			if(!result.isBeforeFirst()){
				// No emergencies to report
				System.out.println("No Aircraft in table");
				return false;  // Bail Out!
			}

			do {
				if (FixTrafficTable.DB.getResultNextRecord(result)) {
					int Icao_addr = result.getInt("Icao_addr");
					//System.out.printf("\t ic: %d\n",Icao_addr);
					FixTrafficTable.aircraft.add(result.getInt("Icao_addr"));
				}
			} while (!result.isAfterLast());  // be looping.. 
			
			// Echo number of aircraft to process.
			System.out.printf("Collected %d contacts\n",FixTrafficTable.aircraft.size());
			
			return true;
		}
		catch (Exception ex){
			System.err.printf("Fix Error: %s\n",ex.getMessage());
			ex.printStackTrace();
 		}	 
		return false;
	}
	
	/**
	 * Remove Gross Errors
	 * 
	 * There are some records that are just going to be worthless
	 */
	private static void fixGrossErrors(){
		
		// Remove records where Altitude is Zed but instriments report aircraft is in flight.
		FixTrafficTable.DB.sqlExecute("DELETE FROM traffic WHERE Alt=0 AND OnGround=0");
		
		// Remove small batch records where the values seem to be insane, above 50,000 ft or 
		// more than 1000 miles away...  that's just not going to be very belieable if there
		// are only a small number of hits, that arbitrary number being less than 5. 
		FixTrafficTable.DB.sqlExecute("DELETE FROM traffic WHERE id IN (" +
			"SELECT id FROM traffic WHERE Icao_addr IN(SELECT Icao_addr as hits FROM traffic WHERE Alt>55000 GROUP BY Icao_addr HAVING count(*) < 5) and Alt>55000\n" +
			" UNION " +
			"SELECT id FROM traffic WHERE Icao_addr IN(SELECT Icao_addr as hits FROM traffic WHERE Alt < 0 GROUP BY Icao_addr HAVING count(*) < 5) and Alt < 0\n" +
			" UNION " +
			"SELECT id FROM traffic WHERE Icao_addr IN(SELECT Icao_addr as hits FROM traffic WHERE (Distance * 0.000621371) > 500 GROUP BY Icao_addr HAVING count(*) < 5) and (Distance * 0.000621371) > 500" +
			");");
				
	}
	
	/**
	 * Add the Delete ID record to stack.
	 * @param id
	 * @return 
	 */
	private static void delIdSql(int id){
		FixTrafficTable.sql_batch_delete.add(String.format("DELETE FROM traffic WHERE id=%d",id));
	}
	
	/**
	 *  Bad Record Message Formatter
	 * 
	 * @param String type
	 * @param int Icao
	 * @param int val_from
	 * @param int val_to 
	 */
	private static void printDelaError(String type,int Icao,int val_from,int val_to){
		System.out.printf("%10s -- Bad %-10s\t%8s --> %-8s\n",Icao,type,val_from,val_to);
	}
	
}
