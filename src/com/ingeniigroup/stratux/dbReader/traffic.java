/**
 * 
 * @since 6 October 2017
 * @author David DeMartini
 * @serial ig0003-am
 * @version 0.0.1
 * @see http://www.ingeniigroup.com/stratux/avmet
 * @repo https://github.com/IngeniiCode/AvMet
 */
package com.ingeniigroup.stratux.dbReader;

import com.ingeniigroup.stratux.dbConnect.StratuxDB;
import com.ingeniigroup.stratux.Tools.ICAO;
import com.ingeniigroup.stratux.Tools.Squawk;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author David DeMartini 
 */
public class traffic {

	private static StratuxDB DB;
	private static boolean verbose;
	
	/**
	 * Constructor
	 * 
	 * @param dbconn     - dbconnector class object
	 */
	public traffic(StratuxDB dbconn){
		traffic.DB = dbconn;  // import the connection
	}
	
	/**
	 * Get dataset start time from traffic database
	 * 
	 * @return  String timestamp  
	 */
	public void getFirstTime(){
		
		// define query to find fastest aircraft
		String sql  = "SELECT MIN(timestamp) as timestamp FROM traffic";
		
		try {
			// prepare, execute query and get resultSet
			ResultSet result = traffic.DB.getResultSet(sql);

			// check results to see if they make any sense.
			if (traffic.DB.getResultNextRecord(result)) {
				System.out.printf("\tStart: %s\n\t-----------------------------------------\n",result.getString("timestamp"));
			}
		}
		catch (Exception ex){
			System.err.printf("DB Error: %s\n",ex.getMessage());
			ex.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @return Object fastest contact. 
	 */
	public boolean getFastest(){
		
		// define query to find fastest aircraft
		String sql  = "SELECT DISTINCT Icao_addr,Tail,Alt,speed,(Distance * 0.000621371) as Dist_miles FROM traffic WHERE Speed_valid=1 AND OnGround=0 ORDER BY speed desc limit 1";
		
		try {
			// prepare, execute query and get resultSet
			ResultSet result = traffic.DB.getResultSet(sql);

			// check results to see if they make any sense.
			if (traffic.DB.getResultNextRecord(result)) {
				reportStat("FASTEST:",result);
				return true;
			}
		}
		catch (Exception ex){
			System.err.printf("DB Error: %s\n",ex.getMessage());
			ex.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * 
	 * @return Object slowest contact. 
	 */
	public boolean getSlowest(){
		
		// define query to find fastest aircraft
		String sql  = "SELECT DISTINCT Icao_addr,Tail,Alt,speed,(Distance * 0.000621371) as Dist_miles FROM traffic WHERE Speed_valid=1 AND OnGround=0 order by speed asc limit 1";
		
		try {
			// prepare, execute query and get resultSet
			ResultSet result = traffic.DB.getResultSet(sql);

			// check results to see if they make any sense.
			if (traffic.DB.getResultNextRecord(result)) {
				reportStat("SLOWEST:",result);
				return true;
			}
		}
		catch (Exception ex){
			System.err.printf("DB Error: %s\n",ex.getMessage());
			ex.printStackTrace();
		}
		
		return false;
	}	
	
	/**
	 * 
	 * @return Object highest altitude contact. 
	 */
	public boolean getHighest(){
		
		// define query to find fastest aircraft
		String sql  = "SELECT DISTINCT Icao_addr,Tail,Alt,speed,(Distance * 0.000621371) as Dist_miles FROM traffic WHERE Speed_valid=1 AND OnGround=0 order by Alt desc limit 1";
		
		try {
			// prepare, execute query and get resultSet
			ResultSet result = traffic.DB.getResultSet(sql);

			// check results to see if they make any sense.
			if (traffic.DB.getResultNextRecord(result)) {
				reportStat("HIGHEST:",result);
				return true;
			}
		}
		catch (Exception ex){
			System.err.printf("DB Error: %s\n",ex.getMessage());
			ex.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * 
	 * @return Object highest altitude contact. 
	 */
	public boolean getLowest(){
		
		// define query to find fastest aircraft
		String sql  = "SELECT DISTINCT Icao_addr,Tail,Alt,speed,(Distance * 0.000621371) as Dist_miles FROM traffic WHERE Speed_valid=1 AND OnGround=0 order by Alt asc limit 1";
		
		try {
			// prepare, execute query and get resultSet
			ResultSet result = traffic.DB.getResultSet(sql);

			// check results to see if they make any sense.
			if (traffic.DB.getResultNextRecord(result)) {
				reportStat("LOWEST:",result);
				return true;
			}
		}
		catch (Exception ex){
			System.err.printf("DB Error: %s\n",ex.getMessage());
			ex.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * 
	 * @return Object closest off-ground contact. 
	 */
	public boolean getClosest(){
		
		// define query to find fastest aircraft
		String sql  = "SELECT DISTINCT Icao_addr,Tail,Alt,speed,(Distance * 0.000621371) as Dist_miles FROM traffic WHERE Speed_valid=1 AND OnGround=0 order by Distance asc limit 1";
		
		try {
			// prepare, execute query and get resultSet
			ResultSet result = traffic.DB.getResultSet(sql);

			// check results to see if they make any sense.
			if (traffic.DB.getResultNextRecord(result)) {		
				reportStat("CLOSEST:",result);
				return true;
			}
		}
		catch (Exception ex){
			System.err.printf("DB Error: %s\n",ex.getMessage());
			ex.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * 
	 * @return Object closest off-ground contact. 
	 */
	public boolean getFurthest(){
		
		// define query to find fastest aircraft
		String sql  = "SELECT DISTINCT Icao_addr,Tail,Alt,speed,(Distance * 0.000621371) as Dist_miles FROM traffic WHERE Speed_valid=1 AND OnGround=0 order by Distance desc limit 1";
		
		try {
			// prepare, execute query and get resultSet
			ResultSet result = traffic.DB.getResultSet(sql);

			// check results to see if they make any sense.
			if (traffic.DB.getResultNextRecord(result)) {
				reportStat("FURTHEST:",result);
				return true;
			}
		}
		catch (Exception ex){
			System.err.printf("DB Error: %s\n",ex.getMessage());
			ex.printStackTrace();
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
		
		String sql  = "SELECT DISTINCT Icao_addr,Tail,Squawk,Alt,speed FROM traffic WHERE Squawk IN(7500,7600,7700,7777) order by Alt desc";
		
		try {
			// prepare, execute query and get resultSet
			ResultSet result = traffic.DB.getResultSet(sql);
			
			if(!result.isBeforeFirst()){
				// No emergencies to report
				//System.out.println("\tNo Alerts Detected");
				return false;  // Bail Out!
			}

			do {
				if (traffic.DB.getResultNextRecord(result)) {
				
					int    Icao_addr = result.getInt("Icao_addr");
					String ICOA24    = ICAO.int2ICAO24(Icao_addr);  // convert the integer into expected format
					String Tail      = findTail(result.getString("Tail"),Icao_addr);
					String Message   = Squawk.getMessage(result.getInt("Squawk"));

					// there was something there.
					System.out.printf("\tALERT: %s [%s] @ %d ft - %s\n",Tail,ICOA24,result.getInt("Alt"),Message);

				}
				
			} while (!result.isAfterLast());  // be looping.. 
			
			// check results to see if they make any sense.
			
			return true;
		}
		catch (Exception ex){
			System.err.printf("DB Error: %s\n",ex.getMessage());
			ex.printStackTrace();
 		}
		
		return false;
	}
	
	/**
	 *  Check Squawk codes to see if any interesing squawks in use
	 * 
	 *  <Codes>
	 *   * 7500 - Hijacking
	 *   * 7600 - Radio Failure
	 *   * 7700 - General Emergency
	 *   * 7777 - Military Intercept Operations
	 */
	public boolean reportSpecialIdents(){
		
		String sql  = "SELECT DISTINCT Icao_addr,Tail,Squawk,Alt,speed FROM traffic WHERE Squawk IN(1276,1277)"      // Air defense and SAR missions
				+ " UNION "
				+ "SELECT DISTINCT Icao_addr,Tail,Squawk,Alt,speed FROM traffic WHERE Squawk IN(4000,5000,5400)"     // Military / NORAD 
				+ " UNION "
				+ "SELECT DISTINCT Icao_addr,Tail,Squawk,Alt,speed FROM traffic WHERE Squawk BETWEEN 4400 AND 4500"  // Various Law Enforcement and USAF recon
				+ " UNION "
				+ "SELECT DISTINCT Icao_addr,Tail,Squawk,Alt,speed FROM traffic WHERE Squawk BETWEEN 5100 AND 5300"  // DOD aircraft
				+ " UNION "
				+ "SELECT DISTINCT Icao_addr,Tail,Squawk,Alt,speed FROM traffic WHERE Squawk BETWEEN 7501 AND 7577"   // Special NORAD
				+ " UNION "
				+ "SELECT DISTINCT Icao_addr,Tail,Squawk,Alt,speed FROM traffic WHERE Squawk BETWEEN 7601 AND 7607"   // FAA Special Use
				+ " UNION "
				+ "SELECT DISTINCT Icao_addr,Tail,Squawk,Alt,speed FROM traffic WHERE Squawk BETWEEN 7701 AND 7707";   // FAA Special Use
		
		try {
			// prepare, execute query and get resultSet
			ResultSet result = traffic.DB.getResultSet(sql);
			
			if(!result.isBeforeFirst()){
				// No emergencies to report
				//System.out.println("\tNo Alerts Detected");
				return false;  // Bail Out!
			}

			do {
				if (traffic.DB.getResultNextRecord(result)) {
				
					int    Icao_addr = result.getInt("Icao_addr");
					String ICOA24    = ICAO.int2ICAO24(Icao_addr);  // convert the integer into expected format
					String Tail      = findTail(result.getString("Tail"),Icao_addr);
					String Message   = Squawk.getMessage(result.getInt("Squawk"));

					// there was something there.
					System.out.printf("\tALERT: %s [%s] %d kts @ %d ft. - %s\n",Tail,ICOA24,result.getInt("speed"),result.getInt("Alt"),Message);

				}
				
			} while (!result.isAfterLast());  // be looping.. 
			
			// check results to see if they make any sense.
			
			return true;
		}
		catch (Exception ex){
			System.err.printf("DB Error: %s\n",ex.getMessage());
			ex.printStackTrace();
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
			String sql  = String.format("SELECT Reg,Tail FROM traffic WHERE Icao_addr=%d AND (Tail > '' OR Reg > '') LIMIT 1",Icao_addr);

			try {
				// prepare, execute query and get resultSet
				ResultSet result = traffic.DB.getResultSet(sql);

				// check results to see if they make any sense.
				if (traffic.DB.getResultNextRecord(result)) {
					// pull through Tail and if that is Empty.. use Reg.. and if that is empty.. well.. not much can be done. 
					tailnum = (result.getString("Tail").isEmpty()) ? result.getString("Reg") : result.getString("Tail");
				}

			}
			catch (Exception ex){
				System.err.printf("DB Error: %s\n",ex.getMessage());
				ex.printStackTrace();
			}
		}
		
		return tailnum;
	}
	
	/**
	 * Standardized Event Statistic String Formatter
	 * 
	 * @param String type
	 * @param ResultSet result
	 * 
	 * @throws SQLException 
	 */
	private void reportStat(String type,ResultSet result) throws SQLException {
		
		int    Icao_addr = result.getInt("Icao_addr");
		int    Altitude  = result.getInt("Alt");
		int    Speed     = result.getInt("speed");
		String ICOA24    = ICAO.int2ICAO24(Icao_addr);  // convert the integer into expected format
		String Tail      = findTail(result.getString("Tail"),Icao_addr);
		String Distance  = (result.getInt("Dist_miles") < 5) ? String.format("%.02f",result.getFloat("Dist_miles")) : String.format("%d",result.getInt("Dist_miles"));
		// there was something there.
				
		System.out.printf("\t%10s %7s [%6s] %7d ft.  @  %4d kts.  %6s mi.\n",type,Tail,ICOA24,Altitude,Speed,Distance);
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

