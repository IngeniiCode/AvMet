/**
 * 
 * @since 6 October 2017
 * @author David DeMartini
 * @serial ig0003-am
 * @version 0.0.1
 * @see http://www.ingeniigroup.com/stratux/avmet
 * @repo https://github.com/IngeniiCode/AvMet
 */
package com.ingeniigroup.stratux.Tools;

/**
 *
 * @author david
 */
public class ICAO {
	
	
	public static String int2ICAO24(int Icao_int){
		
		return String.format("%06X",Icao_int).toUpperCase();
	}
}
