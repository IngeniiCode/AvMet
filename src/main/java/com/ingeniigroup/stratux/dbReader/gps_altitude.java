/**
 * STRATUX Database - gps_altitude table interface 
 * 
 * @since 6 October 2017
 * @author David DeMartini
 * @serial com.ingeniigroup.stratux.avmet.04
 * @version 0.2.0
 * @see http://www.ingeniigroup.com/stratux/avmet
 * @repo https://github.com/IngeniiCode/AvMet
 */
package com.ingeniigroup.stratux.dbReader;

/**
 *
 * @author david
 */
public class gps_altitude {
	
}

/**
 * 
 * SCHEMA
 * 
 *   id             INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, 
 *   stratuxTime    INTEGER, 
 *   nmeaTime       REAL, 
 *   msgType        TEXT, 
 *   gsf            REAL, 
 *   coursef        REAL, 
 *   alt            REAL, 
 *   vv             REAL, 
 *   gpsTurnRate    REAL, 
 *   gpsPitch       REAL, 
 *   gpsRoll        REAL, 
 *   gpsLoadFactor  REAL, 
 *   timestamp_id   INTEGER
 * 
 */
