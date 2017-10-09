/**
 * 
 * @since 6 October 2017
 * @author David DeMartini
 * @serial ig0003-am
 * @version 0.0.1
 * @see http://www.ingeniigroup.com/stratux/avmet
 * @repo https://github.com/IngeniiCode/AvMet
 */
package com.ingeniigroup.stratux.dbConnect;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author david
 */
public class StratuxDBTest {
	
	private static String dbTestFilename;
	private static Gunzip GZ;
	
	public StratuxDBTest() {
	}
	
	@BeforeClass
	public static void setUpClass() {
		
		GZ = new Gunzip();
		
	}
	
	@AfterClass
	public static void tearDownClass() {
	}
	
	@Before
	public void setUp() {
	}
	
	@After
	public void tearDown() {
	}

	@Test
	public void testBasicConnection_real_DB() {
		
		// reference the good reference database checking into testdbs
		StratuxDBTest.dbTestFilename = GZ.unzipDB("testdbs/stratux.sqlite.01.gz");
		
		System.out.printf("Testing DB File: %s\n",StratuxDBTest.dbTestFilename);
		StratuxDB DB = new StratuxDB(StratuxDBTest.dbTestFilename);
		
		boolean connected = DB.Connected();
		assertTrue(connected);
		
	}
	
	@Test
	public void testBasicConnection_fake_DB() {
		
		// reference the good reference database checking into testdbs
		StratuxDBTest.dbTestFilename = GZ.unzipDB("testdbs/filename.test.01.gz");
		
		System.out.printf("Testing DB File: %s\n",StratuxDBTest.dbTestFilename);
		StratuxDB DB = new StratuxDB(StratuxDBTest.dbTestFilename);
		
		boolean connected = DB.Connected();
		assertFalse(connected);
		
	}
	
}
