/**
 *  Copyright (c) 2017  David DeMartini @ Ingenii Group LLC
 * 
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 * 
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 * 
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package com.ingeniigroup.stratux.dbReader;

import com.ingeniigroup.stratux.dbConnect.StratuxDB;
import com.ingeniigroup.stratux.Tools.ICAO;
import com.ingeniigroup.stratux.Tools.Squawk;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * STRATUX Database - traffic table interface   
 * 
 * @since 6 October 2017
 * @author David DeMartini
 * @serial com.ingeniigroup.stratux.avmet.04
 * @version 0.2.0
 * @see http://www.ingeniigroup.com/stratux/avmet
 * @repo https://github.com/IngeniiCode/AvMet
 */
public class traffic {

	private static StratuxDB DB;
	private static String    start_time;
	private static String    end_time;
	private static boolean   include_5100_5300_DOD;  // future feature to re-include 5100-5300 range as DOD aircraft
	
	/**
	 * Constructor
	 * 
	 * @param dbconn     - dbconnector class object
	 */
	public traffic(StratuxDB dbconn){
		traffic.DB = dbconn;  // import the connection
	}
	
	/**
	 * Start the Report display
	 *   
	 */
	public void startReport(){
				
		// define query to find fastest aircraft
		String sql  = "SELECT substr(MIN(timestamp),0,20) AS start_time, substr(MAX(timestamp),0,20) AS end_time FROM traffic";
		
		try {
			// prepare, execute query and get resultSet
			ResultSet result = traffic.DB.getResultSet(sql);
			
			// check results to see if they make any sense.
			if (traffic.DB.getResultNextRecord(result)) {
				start_time = result.getString("start_time");
				end_time   = result.getString("end_time");
				System.out.println("============================================================================");
				System.out.printf("\t%s UTC   -->   %s UTC\n",start_time,end_time);
				System.out.println("\t--------------------------------------------------------------------");
			}
		}
		catch (Exception ex){
			System.err.printf("getFirstTime Error: %s\t%s\n",ex.getMessage(),sql);
		}
	}
	
	/**
	 * Return the FASTEST record in the database 
	 * 
	 * @return Object fastest contact. 
	 */
	public boolean getFastest(){
		
		// define query to find fastest aircraft
		String sql  = "SELECT Icao_addr,Tail as Callsign,Reg as Tailnum,Squawk,Alt,Speed,(Distance * 0.000621371) as Dist_miles FROM traffic WHERE Speed_valid=1 AND OnGround=0 ORDER BY Speed DESC LIMIT 1";
		
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
			System.err.printf("getFastest Error: %s\t%s\n",ex.getMessage(),sql);
		}
		
		return false;
	}
	
	/**
	 * Return the SLOWEST record in the database 
	 * 
	 * @return Object slowest contact. 
	 */
	public boolean getSlowest(){
		
		// define query to find fastest aircraft
		String sql  = "SELECT Icao_addr,Tail as Callsign,Reg as Tailnum,Squawk,Alt,Speed,(Distance * 0.000621371) as Dist_miles FROM traffic WHERE Speed_valid=1 AND OnGround=0 ORDER BY Speed ASC LIMIT 1";
		
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
			System.err.printf("getSlowest Error: %s\t%s\n",ex.getMessage(),sql);
		}
		
		return false;
	}	
	
	/**
	 * 
	 * @return Object highest altitude contact. 
	 */
	public boolean getHighest(){
		
		// define query to find fastest aircraft
		String sql  = "SELECT Icao_addr,Tail as Callsign,Reg as Tailnum,Squawk,Alt,Speed,(Distance * 0.000621371) as Dist_miles FROM traffic WHERE Speed_valid=1 AND OnGround=0 order by Alt desc limit 1";
		
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
			System.err.printf("getHighest Error: %s\t%s\n",ex.getMessage(),sql);
		}
		
		return false;
	}
	
	/**
	 * 
	 * @return Object highest altitude contact. 
	 */
	public boolean getLowest(){
		
		// define query to find fastest aircraft
		String sql  = "SELECT Icao_addr,Tail as Callsign,Reg as Tailnum,Squawk,Alt,Speed,(Distance * 0.000621371) as Dist_miles FROM traffic WHERE Speed_valid=1 AND OnGround=0 order by Alt asc limit 1";
		
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
			System.err.printf("getLowest Error: %s\t%s\n",ex.getMessage(),sql);
		}
		
		return false;
	}
	
	/**
	 * 
	 * @return Object closest off-ground contact. 
	 */
	public boolean getClosest(){
		
		// define query to find fastest aircraft
		String sql  = "SELECT Icao_addr,Tail as Callsign,Reg as Tailnum,Squawk,Alt,Speed,(Distance * 0.000621371) as Dist_miles FROM traffic WHERE Speed_valid=1 AND OnGround=0 order by Distance asc limit 1";
		
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
			System.err.printf("getClosest Error: %s\t%s\n",ex.getMessage(),sql);
		}
		
		return false;
	}
	
	/**
	 * 
	 * @return Object closest off-ground contact. 
	 */
	public boolean getFurthest(){
		
		// define query to find fastest aircraft
		String sql  = "SELECT Icao_addr,Tail as Callsign,Reg as Tailnum,Squawk,Alt,Speed,(Distance * 0.000621371) as Dist_miles FROM traffic WHERE Speed_valid=1 AND OnGround=0 order by Distance desc limit 1";
		
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
			System.err.printf("getFurthest Error: %s\t%s\n",ex.getMessage(),sql);
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
		
		String sql  = "SELECT DISTINCT Icao_addr,Tail as Callsign,Reg as Tailnum,Squawk,Alt,Speed,(Distance * 0.000621371) as Dist_miles FROM traffic WHERE Squawk IN(7500,7600,7700,7777) order by Alt desc";
		
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
					reportSquawkEvent("EMERGENCY:",result);
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
	 *  Check Squawk codes to see if any interesting squawks in use
	 * 
	 *  <Codes>
	 *   * 7400 - UAV Lost Ground Link
	 *   * 7500 - Hijacking
	 *   * 7600 - Radio Failure
	 *   * 7700 - General Emergency
	 *   * 7777 - Military Intercept Operations
	 */
	public boolean reportSpecialIdents(){
		
		String sql  = "SELECT Icao_addr,Tail as Callsign,Reg as Tailnum,Squawk,Alt,Speed,(Distance * 0.000621371) as Dist_miles FROM traffic WHERE Squawk IN(1276,1277) GROUP BY Icao_addr"      // Air defense and SAR missions
				+ " UNION "
				+ "SELECT Icao_addr,Tail as Callsign,Reg as Tailnum,Squawk,Alt,Speed,(Distance * 0.000621371) as Dist_miles FROM traffic WHERE Squawk IN(4000,5000,5400) GROUP BY Icao_addr"     // Military / NORAD 
				+ " UNION "
				+ "SELECT Icao_addr,Tail as Callsign,Reg as Tailnum,Squawk,Alt,Speed,(Distance * 0.000621371) as Dist_miles FROM traffic WHERE Squawk BETWEEN 4400 AND 4500 GROUP BY Icao_addr"  // Various Law Enforcement and USAF recon
				+ " UNION "
				+ "SELECT Icao_addr,Tail as Callsign,Reg as Tailnum,Squawk,Alt,Speed,(Distance * 0.000621371) as Dist_miles FROM traffic WHERE Squawk BETWEEN 7501 AND 7577 GROUP BY Icao_addr"   // Special NORAD
				+ " UNION "
				+ "SELECT Icao_addr,Tail as Callsign,Reg as Tailnum,Squawk,Alt,Speed,(Distance * 0.000621371) as Dist_miles FROM traffic WHERE Squawk BETWEEN 7601 AND 7607 GROUP BY Icao_addr"   // FAA Special Use
				+ " UNION "
				+ "SELECT Icao_addr,Tail as Callsign,Reg as Tailnum,Squawk,Alt,Speed,(Distance * 0.000621371) as Dist_miles FROM traffic WHERE Squawk BETWEEN 7701 AND 7707 GROUP BY Icao_addr";   // FAA Special Use
		
		// add special cases
		if(traffic.include_5100_5300_DOD){
			// Possible DOD aircraft
			sql += " UNION SELECT Icao_addr,Tail as Callsign,Reg as Tailnum,Squawk,Alt,Speed,(Distance * 0.000621371) as Dist_miles FROM traffic WHERE Squawk BETWEEN 5100 AND 5300 GROUP BY Icao_addr";
		}
			
		// execute
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
					reportSquawkEvent("ALERT:",result);
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
	
	private String findTail(String callsign, int Icao_addr) throws SQLException {

		// check for null pointer before checking to see if it's empty
		if(callsign == null || callsign.isEmpty()) {

			// define query to find fastest aircraft
			String sql  = String.format("SELECT CASE WHEN Tail IS NOT NULL THEN Tail WHEN Reg IS NOT NULL THEN Reg ELSE '-------' END AS Callsign FROM traffic WHERE Icao_addr=%d AND (Tail IS NOT NULL OR Reg IS NOT NULL) LIMIT 1",Icao_addr);
			
			try {
				// prepare, execute query and get resultSet
				ResultSet tailresult = traffic.DB.getResultSet(sql);

				if(!tailresult.getString("Callsign").isEmpty()){
					return tailresult.getString("Callsign");
				}
				return "- - -";
				
			}
			catch (Exception ex){
				System.err.printf("findTail Error: %s\t%s\n",ex.getMessage(),sql);
			}
		}
		
		// it was not null and not empty -- use original value
		return callsign;
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
		
		try {
			int    Icao_addr = result.getInt("Icao_addr");
			int    Altitude  = result.getInt("Alt");
			int    Speed     = result.getInt("Speed");
			int    SqCode    = result.getInt("Squawk"); 
			String Callsign  = findTail(result.getString("Callsign"),Icao_addr);
			String ICOA24    = ICAO.int2ICAO24(Icao_addr);  // convert the integer into expected format
			String Distance  = (result.getInt("Dist_miles") < 5) ? String.format("%.02f",result.getFloat("Dist_miles")) : String.format("%d",result.getInt("Dist_miles"));
			String Message    = Squawk.getMessage(SqCode);
			String SquawkCode = (SqCode > 0) ? String.format("%04d",SqCode) : "----";
			// there was something there.
			
			System.out.printf("\t%13s %7s [%6s]  %s  %7d ft.  %4d kts.  %6s mi.  -  %s\n",type,Callsign,ICOA24,SquawkCode,Altitude,Speed,Distance,Message);
		}
		catch (Exception ex){
			System.err.printf("reportStat %s ERROR: %s\n",type,ex.getMessage());
		}
		
	}
	
	/**
	 * Standardized Squawk Event Statistic String Formatter
	 * 
	 * @param type
	 * @param result
	 * @throws SQLException 
	 */
	private void reportSquawkEvent(String type,ResultSet result) throws SQLException {
		
		int    Icao_addr = result.getInt("Icao_addr");
		int    Altitude  = result.getInt("Alt");
		int    Speed     = result.getInt("Speed");
		int    SqCode    = result.getInt("Squawk"); 
		String ICOA24    = ICAO.int2ICAO24(Icao_addr);  // convert the integer into expected format
		String Callsign  = findTail(result.getString("Callsign"),Icao_addr);
		String Distance  = (result.getInt("Dist_miles") < 5) ? String.format("%.02f",result.getFloat("Dist_miles")) : String.format("%d",result.getInt("Dist_miles"));
		// there was something there.
		
		// special string formatting
		String fmt_prefix    = "\u001b["; //NOI18N
		String fmt_suffix    = "m";
		String fmt_separator = ";";
		String fmt_closer    = fmt_prefix + fmt_suffix;
		String fmt_escape    = fmt_prefix;
		String fmt_bold      = fmt_prefix + "1" + fmt_suffix;
		String color_red     = "31";
		String color_yellow  = "33";
				
		switch(type.toLowerCase()){
			case "emergnecy":
			case "emergency:":
				fmt_escape = fmt_prefix + color_red + fmt_separator + "1" + fmt_suffix;   // red!
				break;
			default:
				fmt_escape = fmt_prefix + color_yellow + fmt_separator + "1" + fmt_suffix;     // bright yellow
		}
		
		String Type       = String.format("%s%10s%s",fmt_bold,type,fmt_closer);
		String Message    = String.format("%s%s%s",fmt_escape,Squawk.getMessage(SqCode),fmt_closer);
		String SquawkCode = (SqCode > 0) ? String.format("%s%04d%s",fmt_escape,SqCode,fmt_closer) : "----";
		
		//System.out.printf("\t%10s %7s [%6s] %7d ft.  @  %4d kts.  %6s mi.\n",type,Tail,ICOA24,Altitude,Speed,Distance);		
		
		System.out.printf("\t** %s %7s [%6s]  %s  %7d ft.  %4d kts.  %6s mi.  -  %s\n",Type,Callsign,ICOA24,SquawkCode,Altitude,Speed,Distance,Message);
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

