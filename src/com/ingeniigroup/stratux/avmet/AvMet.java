/**
 * AvMet - Aviation Metrics Application
 *
 * Proof of Concept demonstrator project
 * 
 * <Description>
 * 
 * Application is provided path to a STRAUX generated sqlite3 database file. 
 *
 * If file does not exist, signal a fatal error and exit.
 *
 * If file is compressed (expected gzip compression) decompress
 *
 * Check file for type of sqlite3 database; flag fatal error if it is not.
 *
 * Verify database contains expected schema; flag fatal error if it is not.
 *
 * Search required tables for the metric requested.  Default is –all metrics. 
 *
 * 
 * @since 6 October 2017
 * @author David DeMartini
 * @serial ig0003-am
 * @version 0.0.1
 * @see http://www.ingeniigroup.com/stratux/avmet
 * 
 */
package com.ingeniigroup.stratux.AvMet;

/**
 *
 * @author David DeMartini - Ingenii Group LLC
 */
public class AvMet {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		// TODO code application logic here
	}
	
}
