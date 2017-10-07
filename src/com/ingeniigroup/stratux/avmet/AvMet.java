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

import com.ingeniigroup.stratux.dbConnect.Gunzip;

/**
 * @author David DeMartini - Ingenii Group LLC
 */
public class AvMet {
	
	private static boolean rezip;  // flag telling process to re-zip the file
	private static String  dbFname;
	
	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		
		// TODO code application logic here
		String fName = args[0];
		
		System.out.printf("Attmpting to use input file '%s'\n",fName);
		
		// init and execute
		Gunzip GZ = new Gunzip();
		AvMet.dbFname = GZ.unzipDB(fName);
		
		System.out.printf("Was Zipped = %s\n", GZ.wasZipped());
		
		if(AvMet.dbFname.isEmpty()){
			System.err.printf("ERROR: Unable to located a suitable DB file; unable to use (%s)\n",fName);
			System.exit(9);
		}
		else {
			
			System.out.printf("Using Database File: '%s'\n",AvMet.dbFname);
		}
	}
	
}
