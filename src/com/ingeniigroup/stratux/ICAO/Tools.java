/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ingeniigroup.stratux.ICAO;

/**
 *
 * @author david
 */
public class Tools {
	
	
	public static String int2ICAO24(int Icao_int){
		
		return String.format("%06X",Icao_int).toUpperCase();
	}
}
