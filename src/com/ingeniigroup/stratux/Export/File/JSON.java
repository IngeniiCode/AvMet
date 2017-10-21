/**
 * JSON Export Features
 * 
 * Create export file that can create and load a new (or existing) MySQL database
 * with exported data from a daily STRATUX SQLite3 database logfile.
 * 
 * @since 15 October 2017
 * @author David DeMartini
 * @serial ig0003-am
 * @version 0.0.2
 * @see http://www.ingeniigroup.com/stratux/avmet
 * @repo https://github.com/IngeniiCode/AvMet
 */
package com.ingeniigroup.stratux.Export.File;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.ingeniigroup.stratux.Tools.FileIO;
import com.ingeniigroup.stratux.Tools.DateTime;
import com.ingeniigroup.stratux.Tools.Squawk;
import com.ingeniigroup.stratux.Tools.JsonMicro.JsonObject;

/**
 *
 * @author david
 */
public class JSON {
	
	private String outfile_prefix;
	
	/**
	 * Entry point to EXPORT
	 * 
	 * @param outfile_prefix
	 * @param timestamp_prefix configure to add a timestamp to the prifix
	 */
	public  JSON() {
		// no prefix was provided, so use the default!
		this.outfile_prefix = "./stratux";
	}
	public JSON(boolean timestamp_prefix){ 
		this.outfile_prefix = (timestamp_prefix) ? String.format("./stratux.%s",DateTime.timeprefix())
				: "./stratux.json";
	}
	public JSON(String outfile_prefix){ 
		this.outfile_prefix   = outfile_prefix;
	}
	public JSON(String outfile_prefix,boolean timestamp_prefix){ 
		this.outfile_prefix = (timestamp_prefix) ? String.format("%s.%s",outfile_prefix,DateTime.timeprefix())
				: outfile_prefix;
	}
	
	/**
	 * Export Squawk JSON Data
	 * 
	 * Generates JSON data object of the squawk data.
	 * 
	 * @param outfile - can define the outfile at time of export or use 
	 *        current setting
	 * 
	 * @return file that data was exported to. 
	 */
	public String ExportSquawkData() throws FileNotFoundException, IOException, Exception { return ExportSquawkData(""); }
	public String ExportSquawkData(String outfile_prefix ) throws FileNotFoundException, IOException, Exception {
		
		// determine output filename
		String output_file = String.format("%s.squawk.json",(!outfile_prefix.isEmpty()) ? outfile_prefix : this.outfile_prefix);
		System.out.println("Squawk Data Outfile: " + output_file);	
				
		// Write to file.
		try {
			// Open the file
			FileIO dbFile = new FileIO();
			dbFile.openOutFile(output_file);
			
			// creats load commands for squawk codes
			dbFile.write(squawk_code_json());
			
			// Close file
			dbFile.closeOutFile();  // if you don't call close, file will be truncated or empty
					
			return output_file;
		}
		catch (Exception ex) {
			System.err.println("Export Error: " + ex.getMessage());
		}
		
		return null;  // something went wrong to reach here.. no output file written
	}	
	
	/**
	 * Create squawk codes for squawk JSON.
	 * 
	 * Generates JSON of the squawk code lookup table
	 */
	private String squawk_code_json(){
		
		int squawk_dec_max = 4095;  // this is the max squawk code 
		
		// start the reponse 
		JsonObject squawk_codes = new JsonObject();
	
		// Assemble the list of things
		for(int i=0; i<=squawk_dec_max; i++){
			int code       = Squawk.dec2oct(i);
			String message = Squawk.getMessage(code);
			String squawk  = String.format("%04d",code);
			squawk_codes.add(squawk,message);
		}
		
		return squawk_codes.toString();
		
	}

	
}
