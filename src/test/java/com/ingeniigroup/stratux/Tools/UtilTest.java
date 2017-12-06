/**
 * 
 * @since 30 November 2017
 * @author David DeMartini
 * @serial com.ingeniigroup.stratux.avmet.04
 * @version 0.0.1
 * @see http://www.ingeniigroup.com/stratux/avmet
 * @repo https://github.com/IngeniiCode/AvMet
 */
package com.ingeniigroup.stratux.Tools;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author david
 */
public class UtilTest {
	
	public UtilTest() {
	}
	
	/**
	 * Test of trunc method, of class Util.
	 */
	
	@Test
	public void test_long_string_truncation() {
		System.out.println("Test truncte longer than max string");
		int max_length   = 12;
		String string    = "abcdefghijklmnop";
		String expResult = "abcdefghijkl";
		String result = Util.trunc(max_length,string);
		assertEquals(expResult,result);
	}
	
	@Test
	public void test_short_string_truncation() {
		System.out.println("Test truncte shorter than max string");
		int max_length   = 12;
		String string    = "abcde";
		String expResult = "abcde";
		String result = Util.trunc(max_length,string);
		assertEquals(expResult,result);
	}
	
}
