/**
 * XLSX Export Features
 * 
 * Create export file that can create a tabbed XLSX export file from a daily 
 * STRATUX SQLite3 database logfile.
 * 
 * @since 15 October 2017
 * @author David DeMartini
 * @serial ig0003-am
 * @version 0.0.1cc
 * @see http://www.ingeniigroup.com/stratux/avmet
 * @repo https://github.com/IngeniiCode/AvMet
 */
package com.ingeniigroup.stratux.Export.File;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.ingeniigroup.stratux.Tools.DateTime;

/**
 *
 * @author david
 */
public class XLSX {
	
	private String outfile_prefix;
	private static String str_spreadsheet_create;
	
	/**
	 * Entry point to EXPORT
	 * 
	 * @param   Output filename to use
	 * 
	 * @return  Output filename used 
	 */
	
	public  XLSX() {
		// no prefix was provided, so use the default!
		this.outfile_prefix = "./stratux.spreadsheet";
	}
	public XLSX(boolean timestamp_prefix){ 
		this.outfile_prefix = (timestamp_prefix) ? String.format("./stratux.spreadsheet.%s",DateTime.timeprefix())
				: "./stratux.spreadsheet";
	}
	public XLSX(String outfile_prefix){ 
		this.outfile_prefix   = outfile_prefix;
	}
	public XLSX(String outfile_prefix,boolean timestamp_prefix){ 
		this.outfile_prefix = (timestamp_prefix) ? String.format("%s.%s",outfile_prefix,DateTime.timeprefix())
				: outfile_prefix;
	}
	
	public String ExportData() throws FileNotFoundException, IOException { return ExportData("",false); }
	public String ExportData(String output_prefix) throws FileNotFoundException, IOException { return ExportData("",false); }
	public String ExportData(String output_prefix, boolean use_timestamp) throws FileNotFoundException, IOException {
		
		// determine output filename
		output_prefix = (!output_prefix.isEmpty()) ? output_prefix : this.outfile_prefix;
		
		// Write to file.
		try {
			
			// Write the data
	
			return output_prefix;
		}
		catch (Exception ex) {
			System.err.println("Export Error: " + ex.getMessage());
		}
		
		return null;  // something went wrong to reach here.. no output file written
	}
	
}
