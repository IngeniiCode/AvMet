/**
 * STRATUX Database - settings table interface 
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
public class settings {
	
}

/**
 * SCHEMA
 * 
 *   id                     INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, 
 *   UAT_Enabled            INTEGER, 
 *   ES_Enabled             INTEGER, 
 *   Ping_Enabled           INTEGER, 
 *   GPS_Enabled            INTEGER, 
 *   DisplayTrafficSource   INTEGER, 
 *   DEBUG                  INTEGER, 
 *   ReplayLog              INTEGER, 
 *   PPM                    INTEGER, 
 *   OwnshipModeS           TEXT, 
 *   WatchList              TEXT, 
 *   DeveloperMode          INTEGER, 
 *   timestamp_id           INTEGER
 */
