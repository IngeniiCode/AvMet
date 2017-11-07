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
package com.ingeniigroup.stratux.Export.File;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.ingeniigroup.stratux.Tools.FileIO;
import com.ingeniigroup.stratux.Tools.DateTime;
import com.ingeniigroup.stratux.Tools.Squawk;
import com.ingeniigroup.stratux.Tools.JsonMicro.JsonObject;

/**
 * JSON Export Features
 * 
 * Create export file that can create and load a new (or existing) MySQL database
 * with exported data from a daily STRATUX SQLite3 database logfile.
 * 
 * @since 15 October 2017
 * @author David DeMartini
 * @serial com.ingeniigroup.stratux.avmet.04
 * @version 0.2.0
 * @see http://www.ingeniigroup.com/stratux/avmet
 * @repo https://github.com/IngeniiCode/AvMet
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
