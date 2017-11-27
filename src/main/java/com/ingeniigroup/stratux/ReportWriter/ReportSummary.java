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
import com.ingeniigroup.stratux.Tools.ICAO;
import com.ingeniigroup.stratux.Tools.Squawk;
import com.ingeniigroup.stratux.dbReader.traffic;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

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
	
	private StratuxDB  DB;
	private String     start_time;
	private String     end_time;
	/* data containers */
	private List<HashMap>  General; 
	private List<HashMap>  Emergency;
	private List<HashMap>  Special;
	
	/**
	 * CONSTRUCTOR
	 * 
	 * @param db StratuxDB 
	 * @throws java.sql.SQLException
	 */
	public ReportSummary(StratuxDB db) throws SQLException  {
		
		// setup
		General   = new ArrayList<HashMap>();
		Emergency = new ArrayList<HashMap>();
		Special   = new ArrayList<HashMap>();
		
		// import the connection
		DB = db;  
		
		// execute the get()
		getMetrics();
	}
	
	/**
	 *  Collect the metrics into internal map to be used by report writing
	 *  modules
	 * 
	 */
	private void getMetrics() throws SQLException {
		
		// setup the interfaces
		traffic traffic = new traffic(DB);
		
		// Display start time
		traffic.startReport();
		start_time = traffic.start_time;
		end_time   = traffic.end_time;
		
		// Highest speed 
		General.add(traffic.getFastest());

		// Slowest speed ( greater than 25 kts)
		//General.add(traffic.getSlowest());

		// Highest altitude
		//General.add(traffic.getHighest());
				
		// Lowest altitude ( greater than 0 )
		//General.add(traffic.getLowest());

		// Most contact events
		

		// Fewest contact events
		

		// Closest contact
		//General.add(traffic.getClosest());
		
		// Furthest contact
		//General.add(traffic.getFurthest());
		
		// Squawk counts
		

		// Emergency Squawk events
		//Emergency.add(traffic.reportEmergencies());
		
		// Non-Emergency special Squawk events
		//Special.add(traffic.reportSpecialIdents());
				
		// Flyover events (less than 1nm range)
		
	}
	
	/**
	 * Standardized Event Statistic String Formatter
	 * 
	 * @param String type
	 * @param ResultSet result
	 * 
	 * @throws SQLException 
	 */
	private void printStat(HashMap item) throws SQLException {
		
		try {
			String Label       = item.get("Label").toString();
			int    Icao_addr  = Integer.parseInt(item.get("Icao_addr").toString());
			int    Altitude   = Integer.parseInt(item.get("Alt").toString());
			int    Speed      = Integer.parseInt(item.get("Speed").toString());
			int    SqCode     = Integer.parseInt(item.get("Squawk").toString());
			float  DistMi     = Float.parseFloat(item.get("Dist_miles").toString());
			String Callsign   = item.get("Callsign").toString();
			String ICOA24     = item.get("ICOA24").toString();  // convert the integer into expected format
			String Distance   = ((DistMi) < 5.0) ? String.format("%.02f",DistMi) : String.format("%f",DistMi);
			String Message    = Squawk.getMessage(SqCode);
			String SquawkCode = (SqCode > 0) ? String.format("%04d",SqCode) : "----";
			// there was something there.
			
			System.out.printf("\t%13s %7s [%6s]  %s  %7d ft.  %4d kts.  %6s mi.  -  %s\n",Label,Callsign,ICOA24,SquawkCode,Altitude,Speed,Distance,Message);
		}
		catch (Exception ex){
			System.err.printf("reportStat ERROR: %s\n",ex.getMessage());
		}
		
	}
	
	/**
	 *  Write the data to STDOUT
	 */
	public void STDOUT() throws SQLException {
		
		// Print out the summary header block
		System.out.println("============================================================================");
		System.out.printf("\t%s UTC   -->   %s UTC\n",traffic.start_time,traffic.end_time);
		System.out.println("\t--------------------------------------------------------------------");
		
		// Print out the General metrics if it has entries
		if(!General.isEmpty()){
			Iterator<HashMap> itr = General.iterator();

			// iterate 
			while (itr.hasNext()) { 
				HashMap item = itr.next();
				printStat(item);
			}
			
		}
	}
}
