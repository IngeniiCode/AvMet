/**
 * 
 * @since 6 October 2017
 * @author David DeMartini
 * @serial ig0003-am
 * @version 0.0.1
 * @see http://www.ingeniigroup.com/stratux/avmet
 * @repo https://github.com/IngeniiCode/AvMet
 */
package com.ingeniigroup.stratux.Tools;

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

/** 
 * Test these special codes
 *   codes.put(7500,"!! HIJACKING !!");
 *   codes.put(7600,"Radio Failure");
 *   codes.put(7700,"* EMERGENCY *");
 *   codes.put(7777,"Military Intercept");
 */ 
public class SquawkTest {
	
	public SquawkTest() {
	}
	
	/**
	 * Test of High Value Interest codes
	 */
	
	@Test
	public void test7500() {
		int squawk = 7500;
		String expResult = "!! HIJACKING !!";
		System.out.printf("%04d => %s\n",squawk,expResult);
		String result = Squawk.getMessage(squawk);
		assertEquals(expResult,result);
	}
	
	@Test
	public void test7600() {
		int squawk = 7600;
		String expResult = "Radio Failure";
		System.out.printf("%04d => %s\n",squawk,expResult);
		String result = Squawk.getMessage(squawk);
		assertEquals(expResult,result);
	}
	
	@Test
	public void test7700() {
		int squawk = 7700;
		String expResult = "* EMERGENCY *";
		System.out.printf("%04d => %s\n",squawk,expResult);
		String result = Squawk.getMessage(squawk);
		assertEquals(expResult,result);
	}
	
	@Test
	public void test7777() {
		int squawk = 7777;
		String expResult = "Military Intercept";
		System.out.printf("%04d => %s\n",squawk,expResult);
		String result = Squawk.getMessage(squawk);
		assertEquals(expResult,result);
	}
	
	/**
	 * Test other interesting codes
	 */
	
	@Test
	public void test4470() {
		int squawk = 4470;
		String expResult = "SR-71, YF-12, U-2 and B-57, pressure suit flights";
		System.out.printf("%04d => %s\n",squawk,expResult);
		String result = Squawk.getMessage(squawk);
		assertEquals(expResult,result);
	}
	
	
	@Test
	public void test99() {
		int squawk = 99;
		String expResult = "[0099]";
		System.out.printf("%04d => %s\n",squawk,expResult);
		String result = Squawk.getMessage(squawk);
		assertEquals(expResult,result);
	}
	
	@Test
	public void test_dec2oct_50() {
		int dec = 50;
		int expResult = 62;
		System.out.printf("%d => %s\n",dec,expResult);
		int result = Squawk.dec2oct(dec);
		assertEquals(expResult,result);
	}
	
	@Test
	public void test_dec2oct_4095() {
		int dec = 4095;
		int expResult = 7777;
		System.out.printf("%d => %s\n",dec,expResult);
		int result = Squawk.dec2oct(dec);
		assertEquals(expResult,result);
	}
	
	@Test
	public void test_oct2dec_7777(){
		int oct = 7777;
		int expResult = 4095;
		System.out.printf("%d => %s\n",oct,expResult);
		int result = Squawk.oct2dec(oct);
		assertEquals(expResult,result);
	}
	
}
