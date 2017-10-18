/**
 * SQLite STRATUX <traffic> table repair utility
 * 
 * Centralized file testing methods
 * 
 * @since 12 October 2017
 * @author David DeMartini
 * @serial ig0003-am
 * @version 0.0.2
 * @see http://www.ingeniigroup.com/stratux/avmet
 * @repo https://github.com/IngeniiCode/AvMet
 */
package com.ingeniigroup.stratux.Repair;

import com.ingeniigroup.stratux.Tools.ICAO;
import com.ingeniigroup.stratux.Tools.Squawk;
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
	private static boolean condense;
	private static boolean verbose;
	private static boolean keepdupes;
	private static int  total_contacts   = 0;
	private static int  error_contacts   = 0;
	private static int  deleted_contacts = 0;
	private static int  duplicate_events = 0;
	
	/**
	 * Constructor -- needs to be passed the StratuxDB object
	 * @param dbconn 
	 */
	public static void Fix(StratuxDB dbconn){
		Fix(dbconn,false);
	}
	public static void Fix(StratuxDB dbconn,boolean verbose){	
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
	public static void Condense(StratuxDB dbconn){
		Condense(dbconn,false);
	}
	public static void Condense(StratuxDB dbconn,boolean verbose){
		
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
	private static boolean fixTrafficData(){
		
		// create an iterator
		Iterator<Integer> aircraftIterator = FixTrafficTable.aircraft.iterator();
		// iterate 
		while (aircraftIterator.hasNext()) {
			fixAircraftLog(aircraftIterator.next());
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
		
		// iterate through the aircraft's records, grouping by Altitude, Speed, Distance and Timestamp -- tests show this to be about 35% as aggressive is only grouping by Timestamp.
		String sql_conservative = String.format("SELECT Icao_addr,Reg,Tail,Alt,Speed,Distance,Timestamp,(count(*) -1) AS duplicates FROM traffic WHERE Icao_addr=%d AND OnGround=0 GROUP BY Alt,Speed,Distance,Timestamp HAVING duplicates > 0 ORDER BY Timestamp ASC;",Icao);
		
		// more aggressive de-duplication..  doesn't really matter what it says in the duplicate timestamps, there can be only one.. 
		String sql_aggressive = String.format("SELECT Icao_addr,Reg,Tail,Alt,Speed,Distance,Timestamp,(count(*) -1) AS duplicates FROM traffic WHERE Icao_addr=%d AND OnGround=0 GROUP BY Timestamp HAVING duplicates > 0 ORDER BY Timestamp ASC;",Icao);
		
		// iterate
		try {
			// prepare, execute query and get resultSet
			ResultSet result = FixTrafficTable.DB.getResultSet(sql_aggressive);  // using aggressive filter
			
			// check to see if there is anything to process
			if(!result.isBeforeFirst()){
				// No duplicates found in aircraft's record set
				return false;  // Bail Out!
			}
			
			// define delete batch 
			List batch = new ArrayList<Integer>();	
			
			// id,Iaco_addr,Alt,Speed
			do {
				
				if (FixTrafficTable.DB.getResultNextRecord(result)) {
					String timestamp = result.getString("Timestamp");
					int    dupecount = result.getInt("duplicates");
					
					// ** NOTE ** Because the JDBC driver for SQLite was not compiled 
					// with  ORDER and LIMIT enabled for delete.. it requires the use 
					// of a sub-select to feed the IDs into an outer DELET query.  
					// issue existed as of  sqlite-jdbc-3.20.0.jar  (October 2017)
					String sql = String.format("DELETE FROM traffic WHERE id IN(SELECT id FROM traffic WHERE Timestamp=\"%s\" AND Icao_addr=%s ORDER BY id ASC LIMIT %d)",timestamp,Icao,dupecount);
					// add to the batch fo deletes to be performed
					batch.add(sql);
								
					// increment contacts counter
					FixTrafficTable.duplicate_events += dupecount;
				}
				
			} while (!result.isAfterLast());  // be looping..
			
			// send batch to SQL for processing as a batch
			if(FixTrafficTable.verbose) System.out.printf("%d -- Running %d Duplicate batches\n",Icao,batch.size());
			FixTrafficTable.DB.sqlBatch(batch);
			
			return true;  // execution completed
		}
		catch (Exception ex){
			System.err.printf("fixAircraftLog Error: %s\n",ex.getMessage());
			ex.printStackTrace();
 		}
		
		return false;  // nothing happened.
	}
	
	/**
	 *  Fix an individual aircraft's screwed up records
	 */
	private static boolean fixAircraftLog(int Icao){
		
		if(!FixTrafficTable.keepdupes){
			dumpTimestampDuplicates(Icao);
		}
		
		String sql = String.format("SELECT id,Icao_addr,Reg,Tail,Alt,Speed,Distance,Timestamp FROM traffic WHERE Icao_addr=%d AND OnGround=0 ORDER BY Timestamp,id ASC;",Icao);
		
		try {
			// prepare, execute query and get resultSet
			ResultSet result = FixTrafficTable.DB.getResultSet(sql);  // using aggressive filt4r 
			
			// define delete batch 
			List batch = new ArrayList<Integer>();	
			
			// check to see if there is anything to process
			if(!result.isBeforeFirst()){
				// No emergencies to report
				System.out.printf("Aircraft %d not found in table\n",Icao);
				return false;  // Bail Out!
			}
			
			// define the variables that will persist.
			int id             = -1;
			int speed          = -1;
			int altitude       = -1;
			int distance       = -1;
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
					
					boolean del_record = false;
					
					id       = result.getInt("id");
					speed    = result.getInt("speed");
					altitude = result.getInt("Alt");
					distance = result.getInt("Distance");
					
					// test altitude
					if(badAltitude(altitude,prev_altitude)){
						if(FixTrafficTable.verbose) printDelaError("Altitude",Icao,prev_altitude,altitude);
						FixTrafficTable.error_contacts++;
						del_record = true;
						continue;
					}
					
					// test speed
					if(badSpeed(speed,prev_speed)){
						if(FixTrafficTable.verbose) printDelaError("Speed",Icao,prev_speed,speed);
						FixTrafficTable.error_contacts++;
						del_record = true;
						continue;
					}
					
					// test distance
					if(badSpeed(distance,prev_distance)){
						if(FixTrafficTable.verbose) printDelaError("Distance",Icao,prev_distance,distance);
						FixTrafficTable.error_contacts++;
						del_record = true;
						continue;
					}
					
					// perform condense operation if selected
					if(FixTrafficTable.condense){
						if(isDuplicate(altitude,speed,prev_altitude,prev_speed)){
							FixTrafficTable.deleted_contacts++;
							del_record = true;
							continue; 
						}
					}
					
					if(del_record){
						// syntesize the delete request and add to batch
						batch.add(String.format("DELETE FROM traffic WHERE id=%d",id));
					}
				}
				
			} while (!result.isAfterLast());  // be looping.. 
			
			// send batch to SQL for processing as a batch
			if(batch.size() > 0){
				if(FixTrafficTable.verbose) System.out.printf("%d -- Running %d Duplicate records\n",Icao,batch.size());
				FixTrafficTable.DB.sqlBatch(batch);
			}
			
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
	private static boolean badAltitude(int altitude,int prev_altitude){
		
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
		String sql = "SELECT DISTINCT Icao_addr FROM traffic ORDER BY id ASC";
		
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
