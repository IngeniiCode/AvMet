/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
	
	public StratuxDBTest() {
	}
	
	@BeforeClass
	public static void setUpClass() {
		
		Gunzip GZ = new Gunzip();
		StratuxDBTest.dbTestFilename = GZ.unzipDB("testdbs/stratux.sqlite.01.gz");
		
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
	public void testBasicConnection() {
		
		System.out.printf("Testing DB File: %s\n",StratuxDBTest.dbTestFilename);
		StratuxDB DB = new StratuxDB(StratuxDBTest.dbTestFilename);
		
		boolean connected = DB.Connected();
		assertTrue(connected);
		
	}
	
}
