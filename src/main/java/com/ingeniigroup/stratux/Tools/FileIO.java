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

import com.ingeniigroup.stratux.Repair.FixTrafficTable;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.ArrayList;

import com.ingeniigroup.stratux.Tools.FileTest;
import java.util.Iterator;

/*
 * File I/O interfaces
 * 
 * @since 6 October 2017
 * @author David DeMartini
 * @serial com.ingeniigroup.stratux.avmet.04
 * @version 0.2.0
 * @see http://www.ingeniigroup.com/stratux/avmet
 * @repo https://github.com/IngeniiCode/AvMet
 */
public class FileIO {
	
	private String input_filename;
	private String output_filename;
	private BufferedWriter   FileOut;
	private FileInputStream  FileIn;
	
	/**
	 *  CONSTRUCTOR
	 */
	public FileIO(){
	
	}
	
	/**
	 * Open out the Inputfile Stream and manage within this class
	 * 
	 * @param filename
	 * @throws FileNotFoundException 
	 */
	public void openInFile(String filename) throws FileNotFoundException{
		if(filename.isEmpty()){
			throw new FileNotFoundException("Filename passed to openInFile() is EMPTY.");
		}
		// continue to attempt establishment of FileInputStream handle
		if(FileTest.test4file(filename)){
			this.input_filename = filename;
			this.FileIn = new FileInputStream(this.input_filename);
		}
	}
	
	/**
	 * Write string to open output file
	 * 
	 * @param data
	 * @throws IOException 
	 */
	public void write(String data) throws IOException{
		
		// write!
		this.FileOut.write(data);
		
	}
	public void write(List<String> data) throws IOException{
		
		// create poor-man's buffer for content
		String content = "";
		
		// iterate list and write!
		Iterator<String> dataIterator = data.iterator();
		while (dataIterator.hasNext()) {
			content += dataIterator.next();
		}
		
		// write to file
		write(content);
	}
	
	/**
	 * Open up the Output file Stream and manage within this class
	 * 
	 * @param filename
	 * @throws FileNotFoundException 
	 */
	public void openOutFile(String filename) throws FileNotFoundException, IOException {
		// test for empty before trying anything else
		if(filename.isEmpty()){
			throw new FileNotFoundException("Filename passed to openOutFile() is EMPTY.");
		}
		
		// continue to attempt establishment of BufferedWriter handle
		this.output_filename = filename;
		this.FileOut         = new BufferedWriter(new FileWriter(this.output_filename));
    
	}
	
	/**
	 * Close the current InFile handle
	 * 
	 */
	public void closeInFile() throws IOException {
		// simply check to see if this is anything, and if so go ahead and try to close
		if(!this.FileIn.toString().isEmpty()){
			this.FileIn.close();
		}
	}
	
	/**
	 * Close the current OutFile handle
	 * 
	 */
	public void closeOutFile()  throws IOException {
		// simply check to see if this is anything, and if so go ahead and try to close
		if(!this.FileOut.toString().isEmpty()){
			this.FileOut.close();
		}
	}
	
}
