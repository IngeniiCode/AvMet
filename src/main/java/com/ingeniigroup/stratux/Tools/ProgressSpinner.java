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
 * Progress Spinner Automator
 * 
 * 
 * @since 27 November 2017
 * @author David DeMartini
 * @serial com.ingeniigroup.stratux.avmet.04
 * @version 0.2.0
 * @see http://www.ingeniigroup.com/stratux/avmet
 * @repo https://github.com/IngeniiCode/AvMet
 */
public class ProgressSpinner {
	
	private static int counter  = 0;
	private static char backspc = '\b';
	private static final Map<Integer,String> rotchar;
	static {
        rotchar = new HashMap<Integer,String>();
		/* rotater chars */
		rotchar.put(0,   backspc + "|");
		rotchar.put(1,   backspc + "/");
		rotchar.put(2,   backspc + "-");
		rotchar.put(3,   backspc + "\\");
		rotchar.put(99,  backspc + "" );
   };    
	
	/**
	 * Display the next portion of the spinner
	 * @param val 
	 */
	public static void next(int val){
		System.out.printf("%s",rotchar.get(val%4));
	}
	public static void next() { next(counter++); }
	
	/**
	 * End the spinner
	 * 
	 * Prints a backspace to chew up any spinner char left by a 
	 * next() call and resets the internal counter to 0
	 */
	public static void last(){
		// if there seems to be an active counter.. kick a backspace
		if(counter > 0) { 
			System.out.printf("%s",rotchar.get(99)); 
		}
		// reset the counter, regardless
		counter = 0;
	}
	
}