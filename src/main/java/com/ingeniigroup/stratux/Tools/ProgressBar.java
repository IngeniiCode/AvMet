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

import java.util.HashMap;
import java.util.Map;


/**
 * Progress Bar Automator
 * 
 * 
 * @since 28 November 2017
 * @author David DeMartini
 * @serial com.ingeniigroup.stratux.avmet.04
 * @version 0.2.0
 * @see http://www.ingeniigroup.com/stratux/avmet
 * @repo https://github.com/IngeniiCode/AvMet
 */
public class ProgressBar {
	
	private static int counter          = 0;    // current possition in the bar
	private static int barwidth         = 40;   // width of the bar with boundries
	private static int barvalue         = 40;   // the representative value of the bar
	private static int curr_possition   = 0;    // keep track of the current bar location
	private static String backspc       = "\b"; // backspace char string
	private static String fillchar      = ".";  // configurable fill character string
	private static String blockchar     = "=";  // configurable blocking character string
	// pulsing char rotator
	private static final Map<Integer,String> pulsechar;  
	static {
        pulsechar = new HashMap<Integer,String>();
            /* rotater chars */
            pulsechar.put(0, "+" + backspc);
            pulsechar.put(1, "-" + backspc);
        };    
	
	/**
	 * Mechanism to define a bar width other than the default
	 * @param width 
	 */
	public static void width(int width) { 
            barwidth = width;
	}
	
	/**
	 * 
	 * @param maxval 
	 */
	public static void start(int maxval){
            barvalue = maxval;
            startBar();
	} 
	public static void start() { 
            startBar();
	}
	
	/**
	 * Change the default fill character
	 * 
	 * set the fill char.. only use first part of the string 
	 * in case someone attempts to be a wise-guy
	 * 
	 * @param filler 
	 */
	public static void fillchar(String filler){
		fillchar = filler.substring(0,1);
	}
	
	/**
	 * Change the default blocking character
	 * 
	 * set the block char.. only use first part of the string 
	 * in case someone attempts to be a wise-guy
	 * 
	 * @param block 
	 */
	public static void blockchar(String block){
		blockchar = block.substring(0,1);
	}
	
	/**
	 *  Increment the status bar animation
	 */
	public static void next(){
		
		counter++;  // increment
		//System.out.printf("%s",pulsechar.get(counter%2));  // pulse
		float percent_complete = (float)counter / (float)barvalue;
		int possition          = (int)(percent_complete * barwidth);
                
		if(possition > curr_possition){
			curr_possition = possition;
			System.out.print(blockchar);
		}
                
		// pulse until end of bar reached
		if(curr_possition < barwidth){
			System.out.printf("%s",pulsechar.get(counter%2));  // pulse
		}
	}
	
	/**
	 * Finish the status bar animation and newline
         * 
	 */
	public static void done(){
		counter   = 0;
		System.out.println("]");  // originally just a blank, but Win10 console erases with backspace so this is one fix
	}
	
	/**
	 * Build the status bar and print to STDOUT
	 * 
	 */
	private static void startBar(){
		
		// setup the strings to start bar
		String bar = "";
		String left = backspc;
		
		// fill in the bar and build the backspacing string
		for(int i=0;i<barwidth;i++){
			bar  += fillchar;
			left += backspc;
		}
		
		// print the bar & backspace to begining
		System.out.print("["+bar+"]");
		System.out.print(left);
		
	}
}
