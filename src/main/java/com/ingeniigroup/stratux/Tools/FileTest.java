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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.util.zip.GZIPInputStream;

/**
 * File Testing Package
 * 
 * Centralized file testing methods
 * 
 * @since 16 October 2017
 * @author David DeMartini
 * @serial com.ingeniigroup.stratux.avmet.04
 * @version 0.2.0
 * @see http://www.ingeniigroup.com/stratux/avmet
 * @repo https://github.com/IngeniiCode/AvMet
 */
public class FileTest {
	
	/**
	 * Encased function to check for existence of the file, toss errors if not
	 * found.
	 * 
	 * @param filename
	 * @return boolean
	 * @throws FileNotFoundException 
	 */
	public static boolean test4file(String filename) throws FileNotFoundException {
		
		// declare file handle
		File dbfile = new File(filename);
		
		// check to see if file exists, throw exception if it does not
		if(dbfile.exists() && !dbfile.isDirectory()) { 
			// return the file handle
			return true;
		}
		else {
			throw new FileNotFoundException(filename);
		}
	}
	
	/**
	 * Check to see if the filename passed looks like it's zipped.
	 * 
	 * @param filename
	 * @return 
	 */
	public static boolean looksZipped(String filename){
		
		// extention storage
		String extension = "";
		
		// get the extension
		int i = filename.lastIndexOf('.');
		int p = Math.max(filename.lastIndexOf('/'), filename.lastIndexOf('\\'));
		// iterate
		if (i > p) {
			extension = filename.substring(i+1);
		}
		
		// perform simple case insenstive extention testing -- everything should
		// be  .gz, but what if it's a case-agnostic filesystem like Windows, or
		// certain MacOS FS's..   is this a completely robust solution... NO, but it 
		// does suffice for the task at hand.  <Perfection is the enemy of Good>.
		if(extension.equalsIgnoreCase("gz")){
			return true;
		}
		
		// not a gzipped file so return the unadultrated filename
		return false;
	}
	
	/**
	 * Strip off the filenames extention and return rest of the string as 
	 * the 'reduced' filename
	 * 
	 * @param filename
	 * @return filename 
	 */
	public static String stripExtention(String filename){
		
		String extension = "";
		
		// get the extension
		int i = filename.lastIndexOf('.');
		int p = Math.max(filename.lastIndexOf('/'), filename.lastIndexOf('\\'));
		
		// hack away at the filename 
		if (i > p) {
			extension = filename.substring(i+1);
			return filename.substring(0,filename.lastIndexOf("."));
		}
		
		return filename;  // doesn't seem to be anything that looks like an extension	
	}
	
	
}
