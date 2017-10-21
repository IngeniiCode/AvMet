/**
 * Simplified wrapper for generating time stuffs
 * 
 * @since 17 October 2017
 * @author David DeMartini
 * @serial ig0003-am
 * @version 0.1.0c
 * @see http://www.ingeniigroup.com/stratux/avmet
 * @repo https://github.com/IngeniiCode/AvMet
 */
package com.ingeniigroup.stratux.Tools;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author david
 */
public class DateTime {
	
	/**
	 * Generate a time prefix string
	 * 
	 * @return time_prefix 
	 */
	public static String timeprefix(){

		//Get current date time
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HHmm");
		// return string
        return(now.format(formatter));

	}
	
}
