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
package com.ingeniigroup.stratux.Contact;

import java.util.Map;
import java.util.HashMap;

/**
 * Contact prototype class
 * 
 * @since 6 October 2017
 * @author David DeMartini
 * @serial com.ingeniigroup.stratux.avmet.04
 * @version 0.2.0
 * @see http://www.ingeniigroup.com/stratux/avmet
 * @repo https://github.com/IngeniiCode/AvMet
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
