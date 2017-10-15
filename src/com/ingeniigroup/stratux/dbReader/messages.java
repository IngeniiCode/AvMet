/**
 * STRATUX Database - messages table interface 
 * 
 * @since 6 October 2017
 * @author David DeMartini
 * @serial ig0003-am
 * @version 0.0.1c
 * @see http://www.ingeniigroup.com/stratux/avmet
 * @repo https://github.com/IngeniiCode/AvMet
 */
package com.ingeniigroup.stratux.dbReader;

/**
 *
 * @author david
 */
public class messages {
	
}

/**
 * 
 * SCHEMA
 * 
 *   id                 INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, 
 *   MessageClass       INTEGER, 
 *   TimeReceived       STRING, 
 *   Data               TEXT, 
 *   Signal_amplitude   INTEGER, 
 *   Signal_strength    REAL, 
 *   ADSBTowerID        TEXT, 
 *   timestamp_id       INTEGER
 * 
 */ 
