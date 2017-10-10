/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
	private static int  total_contacts   = 0;
	private static int  error_contacts   = 0;
	private static int  deleted_contacts = 0;
	
	/**
	 * Constructor -- needs to be passed the StratuxDB object
	 * @param dbconn 
	 */
	public static void Fix(StratuxDB dbconn){
		Fix(dbconn,false);
	}
	public static void Fix(StratuxDB dbconn,boolean verbose){	
		System.out.println("Scrubbing tainted data");
		
		FixTrafficTable.verbose  = verbose;
		
		FixTrafficTable.DB = dbconn;  // import the connection
		
		// collect aircraft designators
		collectAircraft();
		
		// grind through the aircraft records.
		fixTrafficData();
		
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
		FixTrafficTable.verbose  = verbose;
		
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
	 *  Fix an individual aircraft's screwed up records
	 */
	private static boolean fixAircraftLog(int Iaco){
		
		// iterate through the aircraft's records
		String sql = String.format("SELECT id,Icao_addr,Tail,Alt,Speed,Distance FROM traffic WHERE Icao_addr=%d ORDER BY id ASC;",Iaco);
		
		try {
			// prepare, execute query and get resultSet
			ResultSet result = FixTrafficTable.DB.getResultSet(sql);
			
			// check to see if there is anything to process
			if(!result.isBeforeFirst()){
				// No emergencies to report
				System.out.printf("No Aircraft %d not found in table\n",Iaco);
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
					id       = result.getInt("id");
					speed    = result.getInt("speed");
					altitude = result.getInt("Alt");
					distance = result.getInt("Distance");
					
					// test altitude
					if(badAltitude(altitude,prev_altitude)){
						if(FixTrafficTable.verbose) System.out.printf("%d -- Bad Altitude detected\t %d  -->  %d\n",Iaco,prev_altitude,altitude);
						FixTrafficTable.DB.deleteRecord("traffic","id",id);
						FixTrafficTable.error_contacts++;
						continue;
					}
					
					// test speed
					if(badSpeed(speed,prev_speed)){
						if(FixTrafficTable.verbose) System.out.printf("%d -- Bad Speed detected\t %d  -->  %d\n",Iaco,prev_altitude,altitude);
						FixTrafficTable.DB.deleteRecord("traffic","id",id);
						FixTrafficTable.error_contacts++;
						continue;
					}
					
					// test distance
					if(badSpeed(distance,prev_distance)){
						//if(FixTrafficTable.verbose) 
						System.out.printf("%d -- Bad Distance detected\t %d  -->  %d\n",Iaco,prev_distance,distance);
						FixTrafficTable.DB.deleteRecord("traffic","id",id);
						FixTrafficTable.error_contacts++;
						continue;
					}
					
					// perform condense operation if selected
					if(FixTrafficTable.condense){
						if(isDuplicate(altitude,speed,prev_altitude,prev_speed)){
							FixTrafficTable.DB.deleteRecord("traffic","id",id);
							FixTrafficTable.deleted_contacts++;
							continue; 
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
	 * Detect Duplicate
	 * 
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
}
