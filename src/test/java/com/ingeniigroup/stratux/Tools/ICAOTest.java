/**
 * 
 * @since 6 October 2017
 * @author David DeMartini
 * @serial com.ingeniigroup.stratux.avmet.04
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

		// TEST 2
		// setup and announce
		int Icao_int  = 854109;
		String wanted = "0D085D";
		System.out.printf("Test int2ICAO24 - convert %d to ICAO24 address %s\n",Icao_int,wanted);
		
		String result = ICAO.int2ICAO24(Icao_int);
		assertEquals(wanted,result);
		System.out.printf("%d mapped to %s\n",Icao_int,result);
		
	}
	
	@Test
	public void testInt2ICAO24_3() {

		// TEST 3
		// setup and announce
		int Icao_int  = 854702;
		String wanted = "0D0AAE";
		System.out.printf("Test int2ICAO24 - convert %d to ICAO24 address %s\n",Icao_int,wanted);
		
		String result = ICAO.int2ICAO24(Icao_int);
		assertEquals(wanted,result);
		System.out.printf("%d mapped to %s\n",Icao_int,result);
		
	}
	
	@Test
	public void testicao2int_1(){
		
		// TEST 4
		// setup and announce
		
		String Icao_string = "0D0AAE";
		int wanted = 854702;
		System.out.printf("Test strICAO2int - convert %s to ICAO24 address %d\n",Icao_string,wanted);
		
		int result = ICAO.strICAO2int(Icao_string);
		assertEquals(wanted,result);
		System.out.printf("%s mapped to %d\n",Icao_string,result);
		
	}
	
	@Test
	public void testicao2int_2(){
		
		// TEST 4
		// setup and announce
		
		String Icao_string = "0D0857";
		int wanted = 854103;
		System.out.printf("Test strICAO2int - convert %s to ICAO24 address %d\n",Icao_string,wanted);
		
		int result = ICAO.strICAO2int(Icao_string);
		assertEquals(wanted,result);
		System.out.printf("%s mapped to %d\n",Icao_string,result);
		
	}
	
}
