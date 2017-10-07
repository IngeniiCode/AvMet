/**
 * 
 */
package com.ingeniigroup.stratux.dbReader;

import java.sql.Connection;

import java.util.Map;

import com.ingeniigroup.stratux.dbConnect.StratuxDB;
import com.ingeniigroup.stratux.ICAO.Tools;
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
		String sql  = "SELECT DISTINCT Icao_addr,Reg,Tail,Alt,speed FROM traffic WHERE Speed_valid=1 order by speed desc limit 1";
		
		try {
			// prepare, execute query and get resultSet
			ResultSet result = traffic.DB.getResultSet(sql);

			// check results to see if they make any sense.
			if (traffic.DB.getResultNextRecord(result)) {
				
				String ICOA24 = Tools.int2ICAO24(result.getInt("Icao_addr"));

				// there was something there.
				System.out.printf("FASTEST: %s (%d)@ %d Kts\n",ICOA24,result.getInt("Icao_addr"),result.getInt("speed"));

			}
		}
		catch (Exception ex){
			
		}
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

