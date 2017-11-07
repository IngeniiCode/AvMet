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

import jxl.Workbook;
import jxl.write.*;
import jxl.write.Number;

import java.io.File;
import java.io.IOException;

import com.ingeniigroup.stratux.Tools.DateTime;
import java.io.FileNotFoundException;

/**
 * XLSX Export Features
 * 
 * Create export file that can create a tabbed XLSX export file from a daily 
 * STRATUX SQLite3 database logfile.
 * 
 * @since 15 October 2017
 * @author David DeMartini
 * @serial com.ingeniigroup.stratux.avmet.04
 * @version 0.2.0c
 * @see http://www.ingeniigroup.com/stratux/avmet
 * @repo https://github.com/IngeniiCode/AvMet
 */

public class XLSX {
	
	private String outfile_prefix;
	private String outfile;
	private WritableWorkbook WB;
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
		init();
	}
	public XLSX(boolean timestamp_prefix){ 
		this.outfile_prefix = (timestamp_prefix) ? String.format("./stratux.spreadsheet.%s",DateTime.timeprefix())
				: "./stratux.spreadsheet";
		init();
	}
	public XLSX(String outfile_prefix){ 
		this.outfile_prefix = outfile_prefix;
		init();
	}
	public XLSX(String outfile_prefix,boolean timestamp_prefix){ 
		this.outfile_prefix = (timestamp_prefix) ? String.format("%s.%s",outfile_prefix,DateTime.timeprefix())
				: outfile_prefix;
		init();
	}
	
	/**
	 * 
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException 
	 */
	public String ExportData() throws FileNotFoundException, IOException { return ExportData("",false); }
	public String ExportData(String output_prefix) throws FileNotFoundException, IOException { return ExportData("",false); }
	public String ExportData(String output_prefix, boolean use_timestamp) throws FileNotFoundException, IOException {
		
		// determine output filename, unless it was already explicitly declared
		outfile = (!outfile.isEmpty()) 
				? outfile 
				: String.format("%s.xls",(!outfile_prefix.isEmpty()) ? outfile_prefix : this.outfile_prefix); 
		
		// Write to file.
		try {
			
			// Create the workbook instance and primary sheet
			WB = Workbook.createWorkbook(new File(outfile));
            WritableSheet excelSheet = WB.createSheet("STRATUX Daily Summary", 0);
			
			// Add a header to the spreadsheet
			Label label = new Label(0, 0, "STRATUX Daily Summary");
            excelSheet.addCell(label);
			
			// Write the data
			WB.write();
			WB.close();

			return output_prefix;
		}
		catch (Exception ex) {
			System.err.println("Export Error: " + ex.getMessage());
		}
		
		return null;  // something went wrong to reach here.. no output file written
	}
	
	/**
	 * Set a specific file to write / append XLS data to
	 * 
	 * @param existing_file 
	 */
	public void useExisting(String existing_file){
		outfile = existing_file;
	}
	
	/**
	 * Common initializer
	 */
	private void init(){
		
	}
}
