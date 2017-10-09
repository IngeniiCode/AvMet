/**
 * 
 * 
 * @since 6 October 2017
 * @author David DeMartini
 * @serial ig0003-am
 * @version 0.0.1
 * @see http://www.ingeniigroup.com/stratux/avmet
 * @repo https://github.com/IngeniiCode/AvMet
 */
package com.ingeniigroup.stratux.dbReader;

import java.sql.Connection;

import java.util.Map;

import com.ingeniigroup.stratux.dbConnect.StratuxDB;
import com.ingeniigroup.stratux.Tools.ICAO;
import com.ingeniigroup.stratux.Contact.Contact;
import com.ingeniigroup.stratux.Tools.Squawk;
import java.sql.ResultSet;


/**
 *
 * @author David DeMartini 
 */
public class traffic {

	private static StratuxDB DB;
	
	public traffic(StratuxDB dbconn){
		traffic.DB = dbconn;  // import the connection
	}
	
	/**
	 * 
	 * @return Object fastest contact. 
	 */
	public boolean getFastest(){
		
		// define query to find fastest aircraft
		String sql  = "SELECT DISTINCT Icao_addr,Reg,Tail,Alt,speed FROM traffic WHERE Speed_valid=1 AND OnGround=0 ORDER BY speed desc limit 1";
		
		try {
			// prepare, execute query and get resultSet
			ResultSet result = traffic.DB.getResultSet(sql);

			// check results to see if they make any sense.
			if (traffic.DB.getResultNextRecord(result)) {
				
				int    Icao_addr = result.getInt("Icao_addr");
				String ICOA24    = ICAO.int2ICAO24(Icao_addr);  // convert the integer into expected format
				String Tail      = findTail(result.getString("Tail"),Icao_addr);

				// there was something there.
				System.out.printf("FASTEST: %s [%s] @ %d kts\n",Tail,ICOA24,result.getInt("speed"));

				return true;
			}
		}
		catch (Exception ex){
			
		}
		
		return false;
	}
	
	/**
	 * 
	 * @return Object slowest contact. 
	 */
	public boolean getSlowest(){
		
		// define query to find fastest aircraft
		String sql  = "SELECT DISTINCT Icao_addr,Reg,Tail,Alt,speed FROM traffic WHERE Speed_valid=1 AND OnGround=0 order by speed asc limit 1";
		
		try {
			// prepare, execute query and get resultSet
			ResultSet result = traffic.DB.getResultSet(sql);

			// check results to see if they make any sense.
			if (traffic.DB.getResultNextRecord(result)) {
				
				int    Icao_addr = result.getInt("Icao_addr");
				String ICOA24    = ICAO.int2ICAO24(Icao_addr);  // convert the integer into expected format
				String Tail      = findTail(result.getString("Tail"),Icao_addr);

				// there was something there.
				System.out.printf("SLOWEST: %s [%s] @ %d kts\n",Tail,ICOA24,result.getInt("speed"));

				return true;
				
			}
		}
		catch (Exception ex){
			
		}
		
		return false;
	}	
	
	/**
	 * 
	 * @return Object highest altitude contact. 
	 */
	public boolean getHighest(){
		
		// define query to find fastest aircraft
		String sql  = "SELECT DISTINCT Icao_addr,Reg,Tail,Alt,speed FROM traffic WHERE Speed_valid=1 AND OnGround=0 order by Alt desc limit 1";
		
		try {
			// prepare, execute query and get resultSet
			ResultSet result = traffic.DB.getResultSet(sql);

			// check results to see if they make any sense.
			if (traffic.DB.getResultNextRecord(result)) {
				
				int    Icao_addr = result.getInt("Icao_addr");
				String ICOA24    = ICAO.int2ICAO24(Icao_addr);  // convert the integer into expected format
				String Tail      = findTail(result.getString("Tail"),Icao_addr);

				// there was something there.
				System.out.printf("HIGHEST: %s [%s] @ %d ft\n",Tail,ICOA24,result.getInt("Alt"));

				return true;
			}
		}
		catch (Exception ex){
			
		}
		
		return false;
	}
	
	/**
	 *  Check Squawk codes to see if any emergencies where announced.
	 * 
	 *  <Codes>
	 *   * 7500 - Hijacking
	 *   * 7600 - Radio Failure
	 *   * 7700 - General Emergency
	 *   * 7777 - Military Intercept Operations
	 */
	public boolean reportEmergencies(){
		
		String sql  = "SELECT DISTINCT Icao_addr,Tail,Squawk FROM traffic WHERE Squawk IN(7500,7600,7700,7777) order by Alt desc";
		
		try {
			// prepare, execute query and get resultSet
			ResultSet result = traffic.DB.getResultSet(sql);
			
			if(!result.isBeforeFirst()){
				// No emergencies to report
				System.out.println("No Alerts Detected");
				return false;  // Bail Out!
			}

			do {
				if (traffic.DB.getResultNextRecord(result)) {
				
					int    Icao_addr = result.getInt("Icao_addr");
					String ICOA24    = ICAO.int2ICAO24(Icao_addr);  // convert the integer into expected format
					String Tail      = findTail(result.getString("Tail"),Icao_addr);
					String Message   = Squawk.getMessage(result.getInt("Squawk"));

					// there was something there.
					System.out.printf("ALERT: %s [%s] @ %d ft - %s\n",Tail,ICOA24,result.getInt("Alt"),Message);

				}
				
			} while (!result.isAfterLast());  // be looping.. 
			
			// check results to see if they make any sense.
			
			return true;
		}
		catch (Exception ex){
			//  ToDo
 		}
		
		return false;
	}
	
	/**
	 * Check to see if the tail number was in the main record, if not then search the other records to see if appeared at any time
	 * 
	 * @param tailnum
	 * 
	 * @return tailnumber  
	 */
	private String findTail(String tailnum, int Icao_addr){
		
		if(tailnum.isEmpty()) {
			// define query to find fastest aircraft
			String sql  = String.format("SELECT Tail FROM traffic WHERE Icao_addr=%d AND Tail>'' LIMIT 1",Icao_addr);

			try {
				// prepare, execute query and get resultSet
				ResultSet result = traffic.DB.getResultSet(sql);

				// check results to see if they make any sense.
				if (traffic.DB.getResultNextRecord(result)) {
					 tailnum = result.getString("Tail");
				}

			}
			catch (Exception ex){
				// ToDo
			}
		}
		
		return tailnum;
	}
	
	/**
	 * Schema
	 *		id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	 *		Icao_addr INTEGER, 
	 *		Reg TEXT, 
	 *		Tail TEXT, 
	 *		Emitter_category INTEGER, 
	 *		OnGround INTEGER, 
	 *		Addr_type INTEGER, 
	 *		TargetType INTEGER, 
	 *		SignalLevel REAL, 
	 *		Squawk INTEGER, 
	 *		Position_valid INTEGER, 
	 *		Lat REAL, 
	 *		Lng REAL, 
	 *		Alt INTEGER, 
	 *		GnssDiffFromBaroAlt INTEGER, 
	 *		AltIsGNSS INTEGER, 
	 *		NIC INTEGER, 
	 *		NACp INTEGER, 
	 *		Track INTEGER, 
	 *		Speed INTEGER, 
	 *		Speed_valid INTEGER, 
	 *		Vvel INTEGER, 
	 *		Timestamp STRING, 
	 *		PriorityStatus INTEGER, 
	 *		Age REAL, 
	 *		AgeLastAlt REAL, 
	 *		Last_seen STRING, 
	 *		Last_alt STRING, 
	 *		Last_GnssDiff STRING, 
	 *		Last_GnssDiffAlt INTEGER, 
	 *		Last_speed STRING, 
	 *		Last_source INTEGER, 
	 *		ExtrapolatedPosition INTEGER, 
	 *		Bearing REAL, 
	 *		Distance REAL, 
	 *		timestamp_id INTEGER
	 */
}

