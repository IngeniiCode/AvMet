/**
 * 
 */
package com.ingeniigroup.stratux.dbReader;

import java.sql.Connection;

import java.util.Map;

import com.ingeniigroup.stratux.dbConnect.StratuxDB;
import com.ingeniigroup.stratux.Tools.ICAO;
import com.ingeniigroup.stratux.Contact.Contact;
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
	public void getFastest(){
		
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

			}
		}
		catch (Exception ex){
			
		}
	}
	
	/**
	 * 
	 * @return Object slowest contact. 
	 */
	public void getSlowest(){
		
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

			}
		}
		catch (Exception ex){
			
		}
		
	}	
	
	/**
	 * 
	 * @return Object highest altitude contact. 
	 */
	public void getHighest(){
		
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

			}
		}
		catch (Exception ex){
			
		}
		
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
	public void reportEmergencies(){
		
		String sql  = "SELECT DISTINCT Icao_addr,Tail,Squawk FROM traffic WHERE Squawk IN(7500,7600,7700,7777) order by Alt desc";
		
		try {
			// prepare, execute query and get resultSet
			ResultSet result = traffic.DB.getResultSet(sql);
			
			if(!result.isBeforeFirst()){
				// No emergencies to report
				System.out.println("No Emergencies Detected");
				return;  // Bail Out!
			}

			do {
				if (traffic.DB.getResultNextRecord(result)) {
				
					int    Icao_addr = result.getInt("Icao_addr");
					String ICOA24    = ICAO.int2ICAO24(Icao_addr);  // convert the integer into expected format
					String Tail      = findTail(result.getString("Tail"),Icao_addr);
					//String Emergency = squawkMessage(result.getInt("Squawk"));

					// there was something there.
					System.out.printf("EMERGENCY: %s [%s] @ %d ft\n",Tail,ICOA24,result.getInt("Alt"));

				}
				
			} while (!result.isAfterLast());
			
			// check results to see if they make any sense.
			
		}
		catch (Exception ex){
			
		}
		
		
		
		
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

