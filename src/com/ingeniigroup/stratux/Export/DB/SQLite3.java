/**
 * SQLite3 Database Export
 * 
 * Create export database from the daily STRATUX SQLite3 database logfile.
 * 
 * @since 15 October 2017
 * @author David DeMartini
 * @serial ig0003-am
 * @version 0.0.1c
 * @see http://www.ingeniigroup.com/stratux/avmet
 * @repo https://github.com/IngeniiCode/AvMet
 */
package com.ingeniigroup.stratux.Export.DB;

import com.ingeniigroup.stratux.Export.File.XLSX;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;

/**
 *
 * @author david
 */
public class SQLite3 {
	
	private static String output_file;
	
	/**
	 * Entry point to EXPORT
	 * 
	 * @param   Output filename to use
	 * 
	 * @return  Output filename used 
	 */
	public static String Export(){
		return Export("");  // 
	}
	public static String Export(String outfile){
		
		// if the outfile is defined, then use it, otherwise drop a generic output
		// file the localy where this thing is being run  
		 SQLite3.output_file = (!outfile.isEmpty()) ? outfile : "./stratux.tracking.sqlite3";  // physical file where  XLSX file is to be written
		
		return  SQLite3.output_file;
	}
	
}
