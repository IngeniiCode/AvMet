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
 * @serial ig0003-am
 * @version 0.0.1c
 * @see http://www.ingeniigroup.com/stratux/avmet
 * @repo https://github.com/IngeniiCode/AvMet
 */
package com.ingeniigroup.stratux.dbConnect;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.GZIPInputStream;

import com.ingeniigroup.stratux.Tools.FileTest;

/**
 * @author David DeMartini - Ingenii Group LLC
 */
public class Gunzip {
	
	private boolean wasZipped = false;  // set flag to indicate it was zipped
	private String  dbFname   = "";
	private String  filename  = "";
	
	/**
	 * Perform the extraction of database, if it's zipped
	 * 
	 * @param filename
	 * @param usetemp
	 * 
	 * @return path to filename that will be used 
	 */
	public String unzipDB(String filename){
		// no flag was provided for <usetemp> parameter, send <false>
		return unzipDB(filename,false);  // call the unified method
	}	
	public String unzipDB(String filename,boolean usetemp){
		
		this.filename = filename; // set back into class
		
		try {
			// establish there is a file to be opened
			getDBFile(filename,usetemp);  // get the file handle
	
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
	
	/**
	 *  Perform testing to see if the file is real, and if so, does it need 
	 *  to be unzipped.. and if it appears to require that.. return the new 
	 *  name, otherwise return the original name.
	 *  
	 *  Performance testing indicated the optimal buffer size for this process
	 *  is 4096.  Started with a buffer of 1024, then increased buffer size by
	 *  factor of 2 until the point of diminshing return.  Unzipping the SQLite3 
	 *  database files hit that point with a buffer size of 4096  
	 *
	 */
	private void getDBFile(String filename, boolean usetemp) {
			
		int length = 0;
		byte[] iobuffer = new byte[4096];   // bumping 4x decreased time by 25% 8x and 16x had not benefit - dad
		
		try {
			if(FileTest.test4file(filename)){

				// legit file -- it is expected that the input file is currently
				// gzipped so try to unzip it first.
				this.dbFname = filename;
		
				// process the filename to see if it has a .gz extenstion
				if(FileTest.looksZipped(filename)){ 	
					this.wasZipped = true;
					this.dbFname = FileTest.stripExtention(filename);
				}
								
				// if file looks like it was zipped.. unzip!
				if(this.wasZipped){

					// Check for 'usetemp' flag -- this only comes into play
					// if unzipping is being performed and a temp file is defined
					if(usetemp){
						// change the final output target of gzip expansion
						this.dbFname = "./sqlite-stratux-temp";
					}
					
					// announce file use
					System.out.printf("Extracting ( %s ) -->  [ %s ]\n",this.filename,this.dbFname);
				
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
		}
		catch (Exception ex){
			System.err.printf("Unable to process [%s] Error: %s\n", filename, ex.getMessage());
			System.exit(9);
		}

	}
		
}
