/**
 * MySQL Export Features
 * 
 * Create export file that can create and load a new (or existing) MySQL database
 * with exported data from a daily STRATUX SQLite3 database logfile.
 * 
 * @since 15 October 2017
 * @author David DeMartini
 * @serial com.ingeniigroup.stratux.avmet.04
 * @version 0.2.0
 * @see http://www.ingeniigroup.com/stratux/avmet
 * @repo https://github.com/IngeniiCode/AvMet
 */
package com.ingeniigroup.stratux.Export.File;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.util.Iterator;

import com.ingeniigroup.stratux.Tools.FileIO;
import com.ingeniigroup.stratux.Tools.DateTime;
import com.ingeniigroup.stratux.Tools.Squawk;

/**
 *
 * @author david
 */
public class MySQL {
	
	private String outfile_prefix;
	private List<String> tables = new ArrayList<String>();
	
	// some useful consts
	private final static char   NL            = 10;  // NewLine
	private final static String DIVIDER       = NL + " " + NL + "## -------------------------------- " + NL;
	private final static String TABLE_CREATE  = "CREATE TABLE IF NOT EXISTS ";
	private final static String ENGINE_TYPE   = " ENGINE=MyISAM DEFAULT CHARSET=latin1;";  // switched to MyISAM for SPEED!!
	
	/**
	 * Entry point to EXPORT
	 * 
	 * @param outfile_prefix
	 * @param timestamp_prefix configure to add a timestamp to the prifix
	 */
	public  MySQL() {
		// no prefix was provided, so use the default!
		this.outfile_prefix = "./stratux.mysql";
	}
	public MySQL(boolean timestamp_prefix){ 
		this.outfile_prefix = (timestamp_prefix) ? String.format("./stratux.mysql.%s",DateTime.timeprefix())
				: "./stratux.mysql";
	}
	public MySQL(String outfile_prefix){ 
		this.outfile_prefix   = outfile_prefix;
	}
	public MySQL(String outfile_prefix,boolean timestamp_prefix){ 
		this.outfile_prefix = (timestamp_prefix) ? String.format("%s.%s",outfile_prefix,DateTime.timeprefix())
				: outfile_prefix;
	}
	
	/**
	 * Export StatuxDB Data Import Files.
	 * 
	 * Generates all of the required INFILE data loading files for populating
	 * a MySQL Stratux Datastore
	 * 
	 * @param output_prefix - can define the outfile at time of export or use 
	 *        current setting
	 * @param use_timestamp - add a timestamp suffix to prefix all output files
	 * 
	 * @return output_prefix - prefix used for the output files. 
	 */
	public String ExportData() throws FileNotFoundException, IOException { return ExportData("",false); }
	public String ExportData(String output_prefix) throws FileNotFoundException, IOException { return ExportData("",false); }
	public String ExportData(String output_prefix, boolean use_timestamp) throws FileNotFoundException, IOException {
		
		// determine output filename
		output_prefix = (!output_prefix.isEmpty()) ? output_prefix : this.outfile_prefix;
		
		// Write to file.
		try {
			
			// Write the data
	/*		
			// Open the file
			FileIO dbFile = new FileIO();
			dbFile.openOutFile(output_prefix);
			
			
			
			// Close file
			dbFile.closeOutFile();  // if you don't call close, file will be truncated or empty
	*/				
			return output_prefix;
		}
		catch (Exception ex) {
			System.err.println("Export Error: " + ex.getMessage());
		}
		
		return null;  // something went wrong to reach here.. no output file written
	}
	
	/**
	 * Export StatuxDB Data Schema
	 * 
	 * Generates all of the required CREATE TABLE commands and pre-loads any 
	 * predominantly static data tables.
	 * 
	 * @param outfile - can define the outfile at time of export or use 
	 *        current setting
	 * 
	 * @return file that data was exported to. 
	 */
	public String ExportSchema() throws FileNotFoundException, IOException { return ExportSchema("",false); }
	public String ExportSchema(boolean export_squawk) throws FileNotFoundException, IOException { return ExportSchema("",export_squawk); }
	public String ExportSchema(String outfile_prefix, boolean export_squawk) throws FileNotFoundException, IOException {
		
		// determine output filename
		String output_file = String.format("%s.schema.sql",(!outfile_prefix.isEmpty()) ? outfile_prefix : this.outfile_prefix);
		System.out.println("Schema Outfile: " + output_file);	
		
		// Write to file.
		try {
			// Open the file
			FileIO dbFile = new FileIO();
			dbFile.openOutFile(output_file);
			
			// Perform the table setups
			create_icao_contact_schema();
			create_aircraft_schema();
			create_squawk_codes_schema();
			create_tracking_log_schema();
			
			// Write the schemas
			dbFile.write(compileSchemas());
			
			// creats load commands for squawk codes
			dbFile.write(squawk_code_mysql_inserts());
			
			// Close file
			dbFile.closeOutFile();  // if you don't call close, file will be truncated or empty
					
			return output_file;
		}
		catch (Exception ex) {
			System.err.println("Export Error: " + ex.getMessage());
		}
		
		return null;  // something went wrong to reach here.. no output file written
	}

	/**
	 * Export Squawk SQL Record Inserts
	 * 
	 * Generates all of the required CREATE TABLE commands and pre-loads any 
	 * predominantly static data tables.
	 * 
	 * @param outfile - can define the outfile at time of export or use 
	 *        current setting
	 * 
	 * @return file that data was exported to. 
	 */
	public String ExportSquawkData() throws FileNotFoundException, IOException, Exception { return ExportSquawkData(""); }
	public String ExportSquawkData(String outfile_prefix ) throws FileNotFoundException, IOException, Exception {
		
		// determine output filename
		String output_file = String.format("%s.squawk.sql",(!outfile_prefix.isEmpty()) ? outfile_prefix : this.outfile_prefix);
		System.out.println("Squawk Data Outfile: " + output_file);	
				
		// Write to file.
		try {
			// Open the file
			FileIO dbFile = new FileIO();
			dbFile.openOutFile(output_file);
			
			// creats load commands for squawk codes
			dbFile.write(squawk_code_mysql_inserts());
			
			// Close file
			dbFile.closeOutFile();  // if you don't call close, file will be truncated or empty
					
			return output_file;
		}
		catch (Exception ex) {
			System.err.println("Export Error: " + ex.getMessage());
		}
		
		return null;  // something went wrong to reach here.. no output file written
	}	
	
	/**
	 * Create squawk codes for  squawk_codes table.
	 * 
	 * Generates required commands to load the squawk_codes table
	 */
	private String squawk_code_mysql_inserts(){
		
		List<String> squawk_codes = new ArrayList<String>();
		int squawk_dec_max = 4095;  // this is the max squawk code 
		
		// Assemble the list of things
		for(int i=0; i<=squawk_dec_max; i++){
			int code       = Squawk.dec2oct(i);
			String message = Squawk.getMessage(code);
			squawk_codes.add(String.format("(%04d,'%s')",code,message));
		}
		
		// Join and return!
		String squawk_data = String.format("INSERT INTO squawk_codes (code,message) VALUES %s ON DUPLICATE KEY UPDATE code=VALUES(code), message=VALUES(message);\n",String.join(","+NL,squawk_codes));
		
		// Add to the schemas list.
		return NL + "## -----   Squawk Code Loading ---- " + NL + squawk_data + NL;
		
	}

	
	/**
	 *  Create icao_contact Schema 
	 * 
	 * Generates required commands to create MySQL table icao_contact
	 */
	private void create_icao_contact_schema(){
		
		this.tables.add( "`icao_contact` (" + NL
			+ "  `icao_int` int(11) NOT NULL," + NL
			+ "  `icao_code` char(6) NOT NULL," + NL
			+ "  `tail` char(9) DEFAULT NULL," + NL
			+ "  `callsign` varchar(12) DEFAULT NULL," + NL
			+ "  `contact_first` datetime DEFAULT NULL," + NL
			+ "  `contact_last` datetime DEFAULT NULL," + NL
			+ "  PRIMARY KEY (`icao_int`)," + NL
			+ "  UNIQUE KEY `icao_code_UNIQUE` (`icao_code`)," + NL
			+ "  INDEX `tail` (`tail`)," + NL
			+ "  INDEX `callsign` (`callsign`)" + NL
			+ ")");
	}
	
	/**
	 * Create aircraft Schema 
	 * 
	 * Generates required commands to create MySQL table aircraft
	 */
	private void create_aircraft_schema(){
		
		this.tables.add("`aircraft` (" + NL
			+ "  `icao_int` INT NOT NULL," + NL
			+ "  `icao_code` CHAR(6) NULL," + NL
			+ "  `manufacture` VARCHAR(63) NULL," + NL
			+ "  `model` VARCHAR(45) NULL," + NL
			+ "  `operator` VARCHAR(63) NULL," + NL
			+ "  `notes` TINYTEXT NULL," + NL
			+ "  PRIMARY KEY (`icao_int`)," + NL
			+ "  INDEX `icaocode` (`icao_code` ASC)," + NL
			+ "  INDEX `maker` (`manufacture` ASC)," + NL
			+ "  INDEX `model` (`model` ASC)," + NL
			+ "  INDEX `operator` (`operator` ASC)" + NL
			+ ")");
	}
	
	/**
	 * Create squawk_codes Schema 
	 * 
	 * Generates required commands to create MySQL table squawk_codes
	 */
	private void create_squawk_codes_schema(){
		
		this.tables.add("`squawk_codes` (" + NL
			+ "  `code` SMALLINT NOT NULL," + NL
			+ "  `message` VARCHAR(127) NULL," + NL
			+ "  `category` ENUM('civil','military','nasa','weather','experimental','other','unknown') NULL DEFAULT 'unknown'," + NL
			+ "  `interest` ENUM('low','medium','hight') NULL DEFAULT 'low'," + NL
			+ "  `highlight` ENUM('','bold','yellow','bold-yellow','red','bold-red','green','bold-green','blue','bold-blue') NULL DEFAULT ''," + NL
			+ "  PRIMARY KEY (`code`)," + NL
			+ "  INDEX `category` (`category`)," + NL
			+ "  INDEX `interest` (`interest`)" + NL
			+ ")");
	}

	
	/**
	 * Create tracking_log Schema 
	 * 
	 * Generates required commands to create MySQL table tracking_log
	 */
	private void create_tracking_log_schema(){
		
		this.tables.add("`tracking_log` (" + NL
			+ "  `id` BIGINT NOT NULL," + NL
			+ "  `icao_int` INT NULL," + NL
			+ "  `registration` VARCHAR(9) NULL," + NL
			+ "  `tail` VARCHAR(9) NULL," + NL
			+ "  `callsign` VARCHAR(12) NULL," + NL
			+ "  `on_ground` ENUM('no','yes') NULL DEFAULT 'no'," + NL
			+ "  `target_type` INT NULL," + NL
			+ "  `signal_level` VARCHAR(45) NULL," + NL
			+ "  `squawk` SMALLINT NULL," + NL
			+ "  `valid_possition` ENUM('yes','no') NULL DEFAULT 'yes'," + NL
			+ "  `valid_speed` ENUM('yes','no') NULL DEFAULT 'yes'," + NL
			+ "  `altitude` INT NULL," + NL
			+ "  `speed` SMALLINT NULL," + NL
			+ "  `vertical_velocity` SMALLINT NULL," + NL
			+ "  `position` POINT NULL COMMENT 'Latitude / Longitude point -- using points allows MySQL to perform geospatial calculations such as distance, bearing, etc. etc. '," + NL
			+ "  `track` DECIMAL(8,5) NULL," + NL
			+ "  `bearing` DECIMAL(8,5) NULL," + NL
			+ "  `distance` DECIMAL(6,2) NULL," + NL
			+ "  `timestamp` TIMESTAMP NULL," + NL
			+ "  PRIMARY KEY (`id`)," + NL
			+ "  INDEX `icao_int` (`icao_int` ASC)," + NL
			+ "  INDEX `on_ground` (`on_ground` ASC)," + NL
			+ "  INDEX `target_type` (`target_type` ASC)," + NL
			+ "  INDEX `altitude` (`altitude` ASC)," + NL
			+ "  INDEX `speed` (`speed` ASC)," + NL
			+ "  INDEX `possition` (`position` ASC)," + NL
			+ "  INDEX `distance` (`distance` ASC)," + NL
			+ "  INDEX `timestamp` (`timestamp` ASC)" + NL
			+ ")");
	}
	
	/**
	 * Iterate through the created table schemas and compile a 
	 * single final Schema definition. 
	 * 
	 * @return schema string 
	 */
	private String compileSchemas(){
		
		// Assemble the data to write into file
		String schema = "";
		Iterator<String> iterator = this.tables.iterator();
		// iterate 
		while (iterator.hasNext()) {
			schema += DIVIDER + TABLE_CREATE + iterator.next() + ENGINE_TYPE + NL;
		}	
	
		return schema;
	}

}
