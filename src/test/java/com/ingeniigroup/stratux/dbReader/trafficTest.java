/**
 * 
 * @since 6 October 2017
 * @author David DeMartini
 * @serial com.ingeniigroup.stratux.avmet.04
 * @version 0.0.1
 * @see http://www.ingeniigroup.com/stratux/avmet
 * @repo https://github.com/IngeniiCode/AvMet
 */
package com.ingeniigroup.stratux.dbReader;

import com.ingeniigroup.stratux.dbConnect.Gunzip;
import com.ingeniigroup.stratux.dbConnect.StratuxDB;

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
public class trafficTest {
	
	private static String dbTestFilename;
	private static Gunzip GZ;
	private static StratuxDB DB;
	
	public trafficTest() {
	}
	
	@BeforeClass
	public static void setUpClass() {
		GZ = new Gunzip();
		
		// reference the good reference database checking into testdbs
		trafficTest.dbTestFilename = GZ.unzipDB("testdbs/stratux.sqlite.01.gz");
		
		System.out.printf("Testing DB File: %s\n",trafficTest.dbTestFilename);
		trafficTest.DB = new StratuxDB(trafficTest.dbTestFilename);
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

	/**
	 * Test of getFastest method, of class traffic.
	 */
	@Test
	public void testGetFastest() {
		System.out.println("getFastest");
		traffic instance = new traffic(trafficTest.DB);
		boolean result = instance.getFastest();
		// TODO review the generated test code and remove the default call to fail.
		assertTrue(result);
	}

	/**
	 * Test of getSlowest method, of class traffic.
	 */
	@Test
	public void testGetSlowest() {
		System.out.println("getSlowest");
		traffic instance = new traffic(trafficTest.DB);
		boolean result = instance.getSlowest();
		// TODO review the generated test code and remove the default call to fail.
		assertTrue(result);
	}

	/**
	 * Test of getHighest method, of class traffic.
	 */
	@Test
	public void testGetHighest() {
		System.out.println("getHighest");
		traffic instance = new traffic(trafficTest.DB);
		boolean result = instance.getHighest();
		// TODO review the generated test code and remove the default call to fail.
		assertTrue(result);
	}

	/**
	 * Test of reportEmergencies method, of class traffic.
	 */
	@Test
	public void testReportEmergencies() {
		System.out.println("reportEmergencies");
		traffic instance = new traffic(trafficTest.DB);
		boolean result = instance.reportEmergencies();
		// TODO review the generated test code and remove the default call to fail.
		assertFalse(result); // no emergencies should be in the test case
	}
	
}
