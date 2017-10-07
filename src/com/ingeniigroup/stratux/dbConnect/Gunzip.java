/**
 * Gunzip - insitu file extractor
 * 
 * <Description>
 * 
 * Extract a compressed db file in situ. 
 *
 * 
 * @since 6 October 2017
 * @author David DeMartini
 * @version 0.0.1
 * @see http://www.ingeniigroup.com/stratux/avmet
 * 
 */
package com.ingeniigroup.stratux.dbConnect;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.util.zip.GZIPInputStream;

/**
 * @author David DeMartini - Ingenii Group LLC
 */
public class Gunzip {
	
	private boolean wasZipped = false;  // set flag to indicate it was zipped
	private String  dbFname   = "";
	private String  filename  = "";
	
	public String unzipDB(String filename){
	
		this.filename = filename; // set back into class
		
		try {
			// establish there is a file to be opened
			getDBFile(filename);  // get the file handle
	
			return this.dbFname;
		} 
		catch (Exception ex){
			System.err.printf("DB File [%s] Open Error: %s\n", filename, ex.getMessage());
			System.exit(9);
		}
		
		return null; // unable to process request
	}
	
	/*
	 *  getter for wasZipped flag
	 */
	public boolean wasZipped(){
		return this.wasZipped;
	}
	
	/* 
	 *  getter for the computed db filename
	 */
	public String dbFname(){
		return this.dbFname;
	}
	
	/*
	 *  getter for the originally used filename
	 */
	public String origFname(){
		return this.filename;
	}
	
	/* 
		=== P R I V A T E === 
	*/
	
	/*
	 *  Perform testing to see if the file is real, and if so, does it need 
	 *  to be unzipped.. and if it appears to require that.. return the new 
	 *  name, otherwise return the original name.
	 *
	 */
	private void getDBFile(String filename){
			
		int length      = 0;
		byte[] iobuffer = new byte[1024];
		boolean legit   = test4file(filename);

		if(legit){
			
			// legit file -- it is expected that the input file is currently
			// gzipped so try to unzip it first.
			
			try {
		
				// process the filename to see if it has a .gz extenstion
				if(isZipped(filename)){ 
				
					System.out.printf("Extracting  %s  from  %s\n",this.dbFname,this.filename);
					// Gzip IOs
					GZIPInputStream gzipInStream   = new GZIPInputStream(new FileInputStream(filename));
					FileOutputStream gzipOutStream = new FileOutputStream(this.dbFname);

					// read stream in and write back out
					while ((length = gzipInStream.read(iobuffer)) > 0) {
						gzipOutStream.write(iobuffer, 0, length);
					}

					// close the file handles.. zombies and file locks are only cool on AMC
					System.out.printf("Extracted:  %s\n",this.dbFname);
					gzipInStream.close();
					gzipOutStream.close();
				}
				
			}
			catch (Exception ex){
				System.err.printf("Unable to process [%s] Error: %s\n", filename, ex.getMessage());
				System.exit(9);
			}
		}
		// attempt to unzip the file, if that fails just return the 
		// original filename as it might have already been unzipped
		
		return;
	}
	
	/*
	 *  check file extention to see if it ends in .gz
	 */
	private boolean isZipped(String filename){
		
		// extention storage
		String extension = "";
		this.dbFname     = filename;  // set the filename, assuming it's NOT a gzip
		
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
			this.wasZipped = true;
			this.dbFname = filename.substring(0,filename.lastIndexOf("."));
			return true;
		}
		
		// not a gzipped file so return the unadultrated filename
		this.dbFname = filename;
		return false;
	}
	
	/*  
	 * handle locating the file... kick back an error if it's not found
	 */
	private boolean test4file(String filename){
		
		try {
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
		catch (Exception ex){
			System.err.printf("DB File Open Error: %s\n", ex.getMessage());
		}
		
		return false;  // empty handle
	}
	
}
