/**
 * 
 * @since 6 October 2017
 * @author David DeMartini
 * @serial ig0003-am
 * @version 0.0.2
 * @see http://www.ingeniigroup.com/stratux/avmet
 * @repo https://github.com/IngeniiCode/AvMet
 */
package com.ingeniigroup.stratux.Contact;

import java.util.Map;
import java.util.HashMap;

/**
 *
 * @author david
 */
public class Contact {
		 
	 /**
	  *   constructor
	  * 
	  *    setup the minimum expected values from a contact event
	  *    @return 
	  */
	 public Map Contact(){
		 
		Map contact = new HashMap(){
			 {
				put("Icao_int",null);       // Icao_addr INTEGER
				put("Icao_hex","");         // computed String from Icao_addr
				put("Reg","");              // Reg  STRING
				put("Tail","");             // Tail STRING
				put("Squawk",null);         // Squawk INTEGER
				put("OnGround",0);          // OnGround INTEGER
				put("Emitter",null);        // Emitter_category INTEGER
				put("TargetType",0);        // TargetType INTEGER
				put("Altitude",0);          // Alt INTEGER
				put("VerticalVelocity",0);  // Vvel INTEGER,
				put("Timestamp","");        // Timestamp  STRING
				put("Bearing","");          // Bearing REAL
				put("Distance","");         // Distance REAL
				put("Last_seen","");        // Last_seen STRING
				put("timestamp_id","");     // timestamp_id INTEGER
			}
		};
		 
		return contact;
	 }
	 
}
