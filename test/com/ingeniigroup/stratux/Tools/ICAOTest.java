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

import com.ingeniigroup.stratux.Tools.ICAO;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author david
 */
public class ICAOTest {
	
	public ICAOTest() {
	}

	/**
	 * Test of int2ICAO24 method, of class ICAO.
	 */
	@Test
	public void testInt2ICAO24_1() {

		// TEST 1
		// setup and announce
		int Icao_int  = 11268104;
		String wanted = "ABF008";
		System.out.printf("Test int2ICAO24 - convert %d to ICAO24 address %s\n",Icao_int,wanted);
		
		String result = ICAO.int2ICAO24(Icao_int);
		assertEquals(wanted,result);
		System.out.printf("%d mapped to %s\n",Icao_int,result);
		// TODO review the generated test code and remove the default call to fail.
		
	}
	
	@Test
	public void testInt2ICAO24_2() {

		// TEST 1
		// setup and announce
		int Icao_int  = 854109;
		String wanted = "0D085D";
		System.out.printf("Test int2ICAO24 - convert %d to ICAO24 address %s\n",Icao_int,wanted);
		
		String result = ICAO.int2ICAO24(Icao_int);
		assertEquals(wanted,result);
		System.out.printf("%d mapped to %s\n",Icao_int,result);
		// TODO review the generated test code and remove the default call to fail.
		
	}
	
		@Test
	public void testInt2ICAO24_3() {

		// TEST 1
		// setup and announce
		int Icao_int  = 854702;
		String wanted = "0D0AAE";
		System.out.printf("Test int2ICAO24 - convert %d to ICAO24 address %s\n",Icao_int,wanted);
		
		String result = ICAO.int2ICAO24(Icao_int);
		assertEquals(wanted,result);
		System.out.printf("%d mapped to %s\n",Icao_int,result);
		// TODO review the generated test code and remove the default call to fail.
		
	}
	
}
