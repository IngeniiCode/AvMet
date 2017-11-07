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
package com.ingeniigroup.stratux.Export.DB;

import com.ingeniigroup.stratux.Export.File.XLSX;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;

/**
 * SQLite3 Database Export
 * 
 * Create export database from the daily STRATUX SQLite3 database logfile.
 * 
 * @since 15 October 2017
 * @author David DeMartini
 * @serial com.ingeniigroup.stratux.avmet.04
 * @version 0.2.0
 * @see http://www.ingeniigroup.com/stratux/avmet
 * @repo https://github.com/IngeniiCode/AvMet
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
