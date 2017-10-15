/**
 * Squawk Code conversion / translation & alerting utility
 * 
 * 
 * Codes update 17-OCT-2017
 * 
 * @since 6 October 2017
 * @author David DeMartini
 * @serial ig0003-am
 * @version 0.0.1c
 * @see http://www.ingeniigroup.com/stratux/avmet
 * @repo https://github.com/IngeniiCode/AvMet
 */
package com.ingeniigroup.stratux.Tools;

import java.util.Map;
import java.util.HashMap;

/**
 * NOTE:  These codes have been updated to comply with Order 7110.66E US Airspace  
 * Beacon Allocation Plan:
 *		https://www.faa.gov/documentlibrary/media/order/final_order_7110_66e_nbcap.pdf
 * 
 * @author david
 */
public class Squawk {
	
	// need a map of the squak code translations

	private static final Map<Integer,String> codes;
	static {
        codes = new HashMap<Integer,String>();
   
		/* unremarkable codes */
		codes.put(0000, "ATTRC En Route Safety and Operations Support");
		codes.put(0021, "VFR below 5000ft.");
		codes.put(0022, "VFR above 5000ft.");
		codes.put(0033, "Parachute Drop Operations");
		codes.put(0100, "Airport Flight Operations");
		codes.put(0500, "External ARTCC subsets");
		codes.put(0600, "External ARTCC subsets");
		codes.put(0700, "External ARTCC subsets");
		codes.put(1000, "IFR below 18000ft");
		codes.put(1100, "Oceanic Airspace");
		codes.put(1200, "Civil VFR (North America)");
		codes.put(1201, "VFR (vicinity of LAX)");
		codes.put(1202, "VFR Glider Operations");
		codes.put(1205, "VFR Helicopter Operations");
		codes.put(1206, "Law Enforcement / Government Helicopter Operations - LAX area");
		codes.put(1234, "Conducting Pattern Work");
		codes.put(1255, "Fire Fighting Operations");  // was Aircraft not in contact with an ATC
		codes.put(1276, "Air Defense Identification Zone penetration");
		codes.put(1277, "SAR missions (USAF or USCG)");
		codes.put(1300, "Oceanic Airspace");
		codes.put(1400, "VFR flight above 12500ft");
		codes.put(1500, "Oceanic Airspace");
		codes.put(1600, "External ARTCC");
		codes.put(1700, "External ARTCC");
		codes.put(2000, "Oceanic Airspace");
		codes.put(2100, "Oceanic Airspace");
		codes.put(2200, "Oceanic Airspace");
		codes.put(2300, "Oceanic Airspace");
		codes.put(2400, "Oceanic Airspace");
		codes.put(2500, "External ARTCC");
		codes.put(2600, "External ARTCC");
		codes.put(2700, "External ARTCC");
		codes.put(3000, "External ARTCC");
		codes.put(3100, "External ARTCC");
		codes.put(3200, "External ARTCC");
		codes.put(3300, "External ARTCC");
		codes.put(3400, "External ARTCC");
		codes.put(3500, "External ARTCC");
		codes.put(3600, "External ARTCC");
		codes.put(3700, "External ARTCC");
		codes.put(4000, "Oceanic Airspace");   // was VFR Military Training Route
		codes.put(4100, "External ARTCC");
		codes.put(4200, "ATTRC En Route Safety and Operations Support");
		codes.put(4300, "ATTRC En Route Safety and Operations Support");
		codes.put(4400, "SR-71, YF-12, U-2 and B-57, pressure suit flights");  // added 17-OCT
		codes.put(4453, "High balloon operations -- Palestine TX");
		codes.put(4500, "ATTRC En Route Safety and Operations Support");
		codes.put(4600, "ATTRC En Route Safety and Operations Support");
		codes.put(4700, "ATTRC En Route Safety and Operations Support");
		codes.put(5000, "NORAD");
		codes.put(5100, "Potomac TRACON - DOD");
		codes.put(5200, "Potomac TRACON - DOD");
		codes.put(5300, "ATTRC En Route Safety and Operations Support");
		codes.put(5400, "NORAD");
		codes.put(5500, "ATTRC En Route Safety and Operations Support");
		codes.put(5600, "External ARTCC subsets");
		codes.put(5700, "External ARTCC subsets");
		codes.put(6000, "External ARTCC subsets");
		codes.put(6100, "NORAD");
		codes.put(6200, "External ARTCC subsets");
		codes.put(6300, "External ARTCC subsets");
		codes.put(6400, "NORAD");
		codes.put(6500, "External ARTCC subsets");
		codes.put(6600, "External ARTCC subsets");
		codes.put(6700, "External ARTCC subsets");
		codes.put(7000, "VFR");
		codes.put(7001, "VFR");
		codes.put(7004, "Aerobatic");
		codes.put(7100, "External ARTCC");
		codes.put(7200, "External ARTCC");
		codes.put(7300, "External ARTCC");
		codes.put(7615, "Civil - littoral surveillance");

		/* Very Special Alert Codes! */
		codes.put(7400, "Unmanned Aircraft - Lost Ground Link");
		codes.put(7500, "!! HIJACKING !!");
		codes.put(7600, "Radio Failure");
		codes.put(7700, "* EMERGENCY *");
		codes.put(7777, "Military Intercept");
   };    
	
	/**
	 * Return the translated Squawk message
	 * 
	 *  Check Squawk codes to see if any emergencies where announced.
	 * 
	 *  <Codes>
	 *   * 7500 - Hijacking
	 *   * 7600 - Radio Failure
	 *   * 7700 - General Emergency
	 *   * 7777 - Military Intercept Operations
	 * 
	 * @param squawk
	 * @return 
	 */
	public static String getMessage(int squawk){
		
		String message = "";
		
		// Check to see if the code falls into this 
		// range of internationally defined Squawk event codes
		if(codes.containsKey(squawk)){
			return codes.get(squawk).toString();
		}
		
		// not a pre-defined code...  run some range tests next to quess
		if (inRange(squawk,41,57)) {
			message = "test";
		}
		else if (inRange(squawk,100,400)){
			message = "Unique Purpose and Experimental activities";  // ud 17-OCT
		}
		else if (inRange(squawk,100,700)){  // Note! this is an overlap defined IN Order 7110.66E
			message = "Oceanic Airspace";   // ud 17-OCT
		}
		else if (inRange(squawk,1207,1272)){
			message = "DVFR Aircraft (USA)";  // ud 17-OCT
		}
		else if (inRange(squawk,1273,1275)){
			message = "Calibration Performance Monitoring Equipment";
		}
		else if (inRange(squawk,4401,4433)){
			message = "Fed Law Enforcement";
		}
		else if (inRange(squawk,4434,4437)){
			message = "Weather reconnaissance";
		}
		else if (inRange(squawk,4440,4441)){
			message = "Lockheed/NASA from Moffett Field above 60000ft";
		}
		else if (inRange(squawk,4442,4446)){
			message = "Lockheed from Air Force Plant 42 above 60000ft";
		}
		else if (inRange(squawk,4447,4452)){
			message = "Special Flight Operations";  // was SR-71/U-2 operations above 60000ft
		}
		else if (inRange(squawk,4454,4465)){
			message = "Air Force above 60000ft";
		}
		else if (inRange(squawk,4466,4477)){
			message = "Fed Law Enforcement";
		}
		else if (inRange(squawk,5050,5057)){
			message = "DOD / HQ NORAD";
		}
		else if (inRange(squawk,5061,5062)){
			message = "Potomac TRACON - DOD ";
		}
		else if (inRange(squawk,5063,5077)){
			message = "DOD / HQ NORAD";
		}
		else if (inRange(squawk,5100,5300)){
			message = "DOD aircraft";
		}
		else if (inRange(squawk,7501,7577)){
			message = "Continental NORAD";
		}
		else if (inRange(squawk,7601,7607)){
			message = "Law Enforcement Special Use";
		}
		else if (inRange(squawk,7610,7676)){
			message = "External ARTCC subsets";
		}
		else if (inRange(squawk,7701,7707)){
			message = "Law Enforcement Special Use";
		}
		else if (inRange(squawk,7710,7776)){
			message = "External ARTCC";
		}
		else {
			message = String.format("[%04d]",squawk);
		}
		
		return message;
	}
	
	/**
	 * Convert a decimal value to the correct numberics 
	 * for a Squawk code octal.
	 * 
	 * @param octint
	 * 
	 * @return octnum
	 */
	public static int dec2oct(int decint){
		
		int octnum[] = new int[100];
		int i        = 1;
		int j        = i;
		int quot     = decint;
		String temp  = "";
		
		// disassemble
		while(quot != 0) {
			octnum[i++] = quot % 8; // add possitional representation of value
			quot = quot / 8;  // cut by 8.
        }
		
		// reassemble in order
		for(j=i-1; j>0; j--){
            temp += octnum[j];
		}
		
		// convert back to a numberic value and return
		return Integer.parseInt(temp);
	}
	
	/**
	 * Convert an octal value to the correct int value
	 * 
	 * @param octint
	 * 
	 * @return decint
	 */
	public static int oct2dec(int octint){
		
		int decint = 0; 
		int i      = 0;
		
		while(octint != 0) {
			decint = decint + (octint%10) * (int) Math.pow(8, i);
			i++;
			octint = octint/10;
		}
		
		return decint;
	}
	
	/**
	 * Range test shorthand 
	 * 
	 * @param value - value to test
	 * @param min   - lower bound
	 * @param max   - upper bound
	 * 
	 * @return  true | false  
	 */
	private static boolean inRange(int value, int min, int max){
		return min <= value && value <= max;
	}
}
