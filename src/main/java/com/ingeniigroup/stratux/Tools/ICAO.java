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

package com.ingeniigroup.stratux.Tools;

/**
 * ICAO conversion / translation utility
 * 
 * @since 6 October 2017
 * @author David DeMartini
 * @serial com.ingeniigroup.stratux.avmet.04
 * @version 0.2.0
 * @see http://www.ingeniigroup.com/stratux/avmet
 * @repo https://github.com/IngeniiCode/AvMet
 */
public class ICAO {
	
	/**
	 * Convert integer value into Octal ICAO string
	 * 
	 * @param int Icao_int
	 * 
	 * @return String Icao_Code
	 */
	public static String int2ICAO24(int Icao_int) {
		// simply reformat value and return
		return String.format("%06X",Icao_int).toUpperCase();
	}
	
	/**
	 * Convert ICAO24 String into the integer value used by STRATUX
	 * 
	 * @param String Icao_string
	 * 
	 * @return int Icao_int 
	 */
	public static int strICAO2int(String Icao_string) {
		return Integer.valueOf(Icao_string, 16).intValue();  // Integer.parseXX functions didn't accpet string, this worked fine.
	}
	
}
