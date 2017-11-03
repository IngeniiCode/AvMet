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
package com.ingeniigroup.stratux.Tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.util.zip.GZIPInputStream;

/**
 *
 * @author david
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
