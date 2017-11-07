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

package com.ingeniigroup.stratux.ReportWriter;

import com.ingeniigroup.stratux.dbConnect.StratuxDB;
import com.ingeniigroup.stratux.AvMet.AvMet;
import com.ingeniigroup.stratux.dbReader.traffic;
import java.sql.SQLException;

/**
 * AvMet - Summary Report Generator
 * 
 * @since 10 November 2017
 * @author David DeMartini
 * @serial com.ingeniigroup.stratux.avmet.04
 * @version 0.2.0
 * @see http://www.ingeniigroup.com/stratux/avmet
 * @repo https://github.com/IngeniiCode/AvMet
 */
public class ReportSummary {
	
	private static StratuxDB DB;
	private static String    start_time;
	private static String    end_time;

	/**
	 * CONSTRUCTOR
	 * 
	 * @param db StratuxDB 
	 * @throws java.sql.SQLException
	 */
	public ReportSummary(StratuxDB db) throws SQLException  {
		
		// import the connection
		DB = db;  
		
		// execute the get()
		get();
	}
	
	/**
	 *  Collect the metrics into internal map to be used by report writing
	 *  modules
	 * 
	 */
	private void get() throws SQLException {
		
		// setup the interfaces
		traffic traffic = new traffic(DB);
		
		// Display start time
		traffic.startReport();
		
		// Highest speed 
		traffic.getFastest(); 

		// Slowest speed ( greater than 25 kts)
		traffic.getSlowest();

		// Highest altitude
		traffic.getHighest();
				
		// Lowest altitude ( greater than 0 )
		traffic.getLowest();

		// Most contact events
		

		// Fewest contact events
		

		// Closest contact
		traffic.getClosest();
		
		// Furthest contact
		traffic.getFurthest();
		
		// Squawk counts
		

		// Emergency Squawk events
		traffic.reportEmergencies();
		
		// Non-Emergency special Squawk events
		traffic.reportSpecialIdents();
				
		// Flyover events (less than 1nm range)
		

	}
	
	
}
