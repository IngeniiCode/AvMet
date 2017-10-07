/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ingeniigroup.stratux.dbConnect;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author david
 */
public class GunzipTest {
	
	private static Gunzip GZ = new Gunzip();
	
	@Test
    public void test_unzipDB() {
				
        String result = GZ.unzipDB("testdbs/filename.test.01.gz");
		// result should be the unzipped filename
		assertEquals("testdbs/filename.test.01", result);
	}
	
	@Test
	public void test_wasZipped() {
		
		Boolean result = GZ.wasZipped();
		// flag should be set to TRUE from first test step
		assertTrue(result);
	
	}
	
	@Test
	public void test_dbFname() {
		
		String result = GZ.dbFname();
		// should be "testdbs/filename.test.01"
		assertEquals("testdbs/filename.test.01", result);

	}
	
	@Test
	public void test_dbFname_gzipp() {
		
		String result = GZ.dbFname();
		// should be "testdbs/filename.test.01"
		assertNotEquals("testdbs/filename.test.01.gz", result);

	}
	
}
