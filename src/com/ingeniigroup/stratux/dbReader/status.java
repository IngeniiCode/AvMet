/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ingeniigroup.stratux.dbReader;

/**
 *
 * @author david
 */
public class status {
	
}

/**
 * SCHEMA
 * 
 *   id                                           INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT
 *   Version                                      TEXT
 *   Build                                        TEXT
 *   HardwareBuild                                TEXT
 *   Devices                                      INTEGER
 *   Connected_Users                              INTEGER
 *   DiskBytesFree                                INTEGER
 *   UAT_messages_last_minute                     INTEGER
 *   UAT_messages_max                             INTEGER
 *   ES_messages_last_minute                      INTEGER
 *   ES_messages_max                              INTEGER
 *   UAT_traffic_targets_tracking                 INTEGER
 *   ES_traffic_targets_tracking                  INTEGER
 *   Ping_connected                               INTEGER
 *   GPS_satellites_locked                        INTEGER
 *   GPS_satellites_seen                          INTEGER
 *   GPS_satellites_tracked                       INTEGER
 *   GPS_position_accuracy                        REAL
 *   GPS_connected                                INTEGER
 *   GPS_solution                                 TEXT
 *   GPS_detected_type                            INTEGER
 *   Uptime                                       INTEGER
 *   UptimeClock                                  STRING
 *   CPUTemp                                      REAL
 *   NetworkDataMessagesSent                      INTEGER
 *   NetworkDataMessagesSentNonqueueable          INTEGER
 *   NetworkDataBytesSent                         INTEGER
 *   NetworkDataBytesSentNonqueueable             INTEGER
 *   NetworkDataMessagesSentLastSec               INTEGER
 *   NetworkDataMessagesSentNonqueueableLastSec   INTEGER
 *   NetworkDataBytesSentLastSec                  INTEGER
 *   NetworkDataBytesSentNonqueueableLastSec      INTEGER
 *   UAT_METAR_total                              INTEGER
 *   UAT_TAF_total                                INTEGER
 *   UAT_NEXRAD_total                             INTEGER
 *   UAT_SIGMET_total                             INTEGER
 *   UAT_PIREP_total                              INTEGER
 *   UAT_NOTAM_total                              INTEGER
 *   UAT_OTHER_total                              INTEGER
 *   Logfile_Size                                 INTEGER
 *   timestamp_id                                 INTEGER
 * 
 */