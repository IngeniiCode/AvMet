/**
 *  Copyright (c) 2017  David DeMartini @ Ingenii Group LLC
 * 
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 * 
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 * 
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package com.ingeniigroup.stratux.dbReader;

/**
 * STRATUX Database - status table interface  
 * 
 * @since 6 October 2017
 * @author David DeMartini
 * @serial com.ingeniigroup.stratux.avmet.04
 * @version 0.2.0
 * @see http://www.ingeniigroup.com/stratux/avmet
 * @repo https://github.com/IngeniiCode/AvMet
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
