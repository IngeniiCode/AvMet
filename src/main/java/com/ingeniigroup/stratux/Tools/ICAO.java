/**
 * ICAO conversion / translation utility
 * 
 * @since 6 October 2017
 * @author David DeMartini
 * @serial com.ingeniigroup.stratux.avmet.04
 * @version 0.2.0
 * @see http://www.ingeniigroup.com/stratux/avmet
 * @repo https://github.com/IngeniiCode/AvMet
 */
package com.ingeniigroup.stratux.Tools;

/**
 *
 * @author david
 */
public class ICAO {
	
	/**
	 * Convert integer value into Octal ICAO string
	 * 
	 * @param int Icao_int
	 * 
	 * @return String Icao_Code
	 */
	public static String int2ICAO24(int Icao_int) {
		// simply reformat value and return
		return String.format("%06X",Icao_int).toUpperCase();
	}
	
	/**
	 * Convert ICAO24 String into the integer value used by STRATUX
	 * 
	 * @param String Icao_string
	 * 
	 * @return int Icao_int 
	 */
	public static int strICAO2int(String Icao_string) {
		return Integer.valueOf(Icao_string, 16).intValue();  // Integer.parseXX functions didn't accpet string, this worked fine.
	}
	
}
