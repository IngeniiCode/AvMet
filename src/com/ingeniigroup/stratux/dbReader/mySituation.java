/**
 * STRATUX Database - mySituation table interface  
 * 
 * @since 6 October 2017
 * @author David DeMartini
 * @serial ig0003-am
 * @version 0.0.2
 * @see http://www.ingeniigroup.com/stratux/avmet
 * @repo https://github.com/IngeniiCode/AvMet
 */
package com.ingeniigroup.stratux.dbReader;

/**
 *
 * @author david
 */
public class mySituation {
	
}

/**
 * 
 * SCHEMA
 *
 *   id                         INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, 
 *   LastFixSinceMidnightUTC    REAL, 
 *   Lat                        REAL, 
 *   Lng                        REAL, 
 *   Quality                    INTEGER, 
 *   HeightAboveEllipsoid       REAL, 
 *   GeoidSep                   REAL, 
 *   Satellites                 INTEGER, 
 *   SatellitesTracked          INTEGER, 
 *   SatellitesSeen             INTEGER, 
 *   Accuracy                   REAL, 
 *   NACp                       INTEGER, 
 *   Alt                        REAL, 
 *   AccuracyVert               REAL, 
 *   GPSVertVel                 REAL, 
 *   LastFixLocalTime           STRING, 
 *   TrueCourse                 REAL, 
 *   GPSTurnRate                REAL, 
 *   GroundSpeed                INTEGER, 
 *   LastGroundTrackTime        STRING, 
 *   GPSTime                    STRING, 
 *   LastGPSTimeTime            STRING, 
 *   LastValidNMEAMessageTime   STRING, 
 *   LastValidNMEAMessage       TEXT, 
 *   PositionSampleRate         REAL, 
 *   Temp                       REAL, 
 *   Pressure_alt               REAL, 
 *   LastTempPressTime          STRING, 
 *   Pitch                      REAL, 
 *   Roll                       REAL, 
 *   Gyro_heading               REAL, 
 *   LastAttitudeTime           STRING, 
 *   timestamp_id               INTEGER
 */ 
