AvMet
=====
Aviation Metrics Project -- Extract data from STRATUX daily database logs.

Developed by David DeMartini @ Ingenii Group LLC.  

### Contact Information:
David DeMartini
Ingenii Group LLC

   http://www.daviddemartini.com

   software@ingeniigroup.com

Version 0.2.0

Announcements!
==============

### 13-NOV-2017
Project now using a more liberal OSS license.  See LICENSE.md for details.

###  2-NOV-2017
Project updated to generate a fat .jar.  Final target jar will integrate any required supporting code modules/jars into
the monolithic application package (.jar)

Check the release notes for latest new features and caveats!!  v.0.2.0 

Running AvMet
=============
AvMet expects the first parameter to be path to STRATUX database file.  File can 
be compressed (.giz only at this time).

ex:
  *   java -jar target/AvMet.jar --db=<path_to_db_file>

### Required parameter

* **--db=<path_do_db>** - Location of a valid STRATUX SQLite database,  
Gzip compressed SQLite3 db file 

* **--nodb** - no db file.

   Perform operations that do not require an import database. Ex:  mysql scheam or 
data loader file operations such as __export_mysql_schema__  or __export_squawk_mysql__

### Optional parameters
Optional extra parameters where can be in any order after the database

* **--keepdb** - do not remove extracted db

   Leave the uncompressed DB (other artifact files are still cleaned up). If the 
original file was not compressed, database is  not removed so flag is not required.
 
* **--scrub** - cleanup `traffic` table before reporting

   Traverse the `traffic` table, and remove events that do not fit the expected 
progression for a specific contact.  This removes transient bad that has been seen 
in majority live database samples

   Part of the __scrub__ process will remove event records within an airframe's
dataset that have identical `Timestamp` values.  If you with to disable this 
feature of scrub, add the __keepdupes__ flag to parameter list

* **--keepdupes** - do not remove duplicate `Timestamp` events

   Part of the __scrub__ process will remove duplicate `Timestamp` records within 
an airframe's dataset.  This flag has no effect unless also using the __scrub__
option

* **--pack** - remove similar events (pack) to save space in database

   Traverse the `traffic` table, and remove events have identical Altitude, 
Speed within a specific contact's dataset, this can condense the database by 
40% to 75%   (11-OCT-2017 disabled due to bug)

* **--usetemp** - use a local scratch/temp db file

   When extracting a database from SQL, write to a local temp file.  This is most 
useful when the source database is compressed and on a  remote or temporary device:  
output file will be  `./sqlite-stratux-temp`

   If you wish to keep the temporary database for further analysis after running
AvMet, add the __keepdb__ option

* **--useprefix** - use a generated date/time prefix for all saved output files

   When exporting data, each file will be prefixed with a date-time stamp string

* **--verbose** - increase progress reporting 

   Report more details on processing activity.  Warning, it can be VERY verbose 
when processing very dirty `traffic` tables

 * **--export_mysql** - Export data in MySQL DB Load format.

 * **--export_mysql_schema** - Generate a Database Schema file 

   Create a MySQL Schema file with necessary CREATE TABLE commands.  This also includes the 
squawk table loading data.  Default output file name is __./stratix.mysql.schema.sql__

 * **--export_squawk_mysql** - Generate a database Insert command file for Squawk Codes

   Create a MySQL Schema file with necessary CREATE TABLE command for the `squawk` table.  This also includes the 
squawk table loading data.  Default output file name is __./stratux.mysql.squawk.sql__

   Generate a MySQL database schema file, that will create the required tables if they do not
exist, and load the squawk table with records (this will __overwrite existing squawk records__)

 * **--export_squawk_json** - Generate a Squawk Codes JSON file 

   Create a JSON object file with current Squawk -> Message mapping.  Default output file name is __./stratix.squawk.json__

   Generate a MySQL database schema file, that will create the required tables if they do not
exist, and load the squawk table with records (this will __overwrite existing squawk records__)

 * **--export_xls** - Enabled XLS workbook exporting (minimal flag).

   Create an XLS workbook of the day's summary data.

 * **--export_xls_to=<outfile>** - Write/Append XLSX data out to specific file
   
   Write or append the output data to specific file.  Creates new file is it does not
already exist; appends to workbook using new day-specific tabs if valid XLS file. 

Release Notes
=============

### 13-NOV-2017
Scrapped the complex and restrictive Creative Commons license, and replaced it 
with a simpler one, that allows commercial and non-commercial re-use, as long as
attribution is provided for the work done.

###  3-NOV-2017
Adding Excel (XLSX) integration requires 3rd party libraries.  Although ease of 
use is nice, my primary concern is WEIGHT of the code!

Results of testing the impact of integration of these packages:

__Pre Apache-POI Excell integration__
  -rw-r--r-- 1 david staff    74,257 Nov  2 17:08 AvMet-0.1.0.jar
  -rw-r--r-- 1 david staff  6,772,749 Nov  2 17:08 AvMet.jar

__Post Apache-POI Excell integration__
  -rw-r--r-- 1 david staff    74,538 Nov  3 07:51 AvMet-0.1.0.jar
  -rw-r--r-- 1 david staff 20,393,477 Nov  3 07:51 AvMet.jar

__Post JExcelAPI Integration__
  -rw-r--r-- 1 david staff    74,521 Nov  3 07:54 AvMet-0.1.0.jar
  -rw-r--r-- 1 david staff  7,865,204 Nov  3 07:54 AvMet.jar

The results are quite clear.  Despite the added complexity of programming 
JXL vs. the POI solution, the impact of adding POI to the final .jar is more than
I find acceptable.  


###  2-NOV-2017
Project updated to generate a fat .jar.  Final target jar will integrate any required supporting code modules/jars into
the monolithic application package (.jar)

###  1-NOV-2017
Updated project to use Apache Maven!  

### 26-OCT-2017
Added Creative Commons Licensing file allowing Non-Commercial use of code.

Implemented Gross Error detection and mitigation -- this has almost completely
eliminated extreme tracking events.  Some edge cases persist; improvements are continuing.

### 19-OCT-2017
New option to export existing Squawk data into a JSON data object.  This is suitable for a variety
of applications where the translation is desired. ( v.0.0.2 )

**export_squawk_json** - Generate a Squawk Codes JSON file

### 18-OCT-2017
New options to allow using tool to simply export MySQL or other database configurations:

**nodb** - run without an input database

**export_squawk_mysql** - generate a Squawk database (in JSON) or bulk SQL inserts


### 16-OCT-2017
Internally refactoring some file IO methods
Updated the JavaDoc blocks where sparse

### 15-OCT-2017
Adding feature to export data into a MySQL import / load file.


### 12-OCT-2017
Fixed bug in Emergency and Special Squawk code alerting to only return a unique
special Squawk code per aircraft.  If more than one special interest code is 
used, a single entry for each will be expressed.   EX:

	**ALERT:**  N113MH [A03873] 5216    10050 ft.  @   215 kts.  DOD aircraft
	**ALERT:**  N579PS [A7712E] 5273    16575 ft.  @   251 kts.  DOD aircraft
	**ALERT:** SWA1518 [ABB972] 5276    23800 ft.  @   438 kts.  DOD aircraft

### 12-OCT-2017
Refactor of reporting process, added String formatting templates, cut about 20 
lines of code by doing this.  Updated output looks like this:

	Start: 2017-10-08 08:07:00.572 +0000 UTC
	-----------------------------------------
	  FASTEST:         [F8D460]     275 ft.  @   574 kts.       8 mi.
	  SLOWEST:   N443R [A55739]     900 ft.  @    19 kts.    1.81 mi.
	  HIGHEST:  N478EV [A5DF4A]   51075 ft.  @    95 kts.     482 mi.
	   LOWEST:         [F8D460]     275 ft.  @   574 kts.       8 mi.
	  CLOSEST:  N872WH [ABFFC0]    3200 ft.  @   178 kts.    0.22 mi.
	 FURTHEST:  N202RR [A19AF8]    4600 ft.  @    70 kts.    8331 mi.

### 11-OCT-2017  
Bug in the Squawk alerting code is expressing too much data, showing every event 
record for a contact using a notable Squawk code (so far the hits in test datasets 
I have available are all DOD. Also need to express the actual Squawk code flagged 
the event for reporting. (ToDo)

	[ ... ]
	ALERT: N579PS [A7712E] 219 kts @ 19050 ft. - DOD aircraft
	ALERT: N579PS [A7712E] 220 kts @ 19050 ft. - DOD aircraft
	ALERT: N579PS [A7712E] 221 kts @ 19050 ft. - DOD aircraft
	ALERT: N579PS [A7712E] 217 kts @ 19075 ft. - DOD aircraft
	ALERT: SWA1518 [ABB972] 221 kts @ 4175 ft. - DOD aircraft
	ALERT: SWA1518 [ABB972] 221 kts @ 4225 ft. - DOD aircraft
	ALERT: SWA1518 [ABB972] 221 kts @ 4300 ft. - DOD aircraft
	ALERT: SWA1518 [ABB972] 222 kts @ 4350 ft. - DOD aircraft
	[ ... ]

### 11-OCT-2017
Beta 2 merged to master for user evaluations!!  Welcome to Aviation Metrics (**AvMet**).

###  9-OCT-2017
First Beta merged to master.  

Development Notes
=================

## Data Scrubbing

**Note: 12-OCT-2017**
Scrubbing processor has a bug that prevents it from properly handling more than a 
single out of range error. 

During analysis of example datasets, anomalous entries were detected in nearly each 
of the databases.  Some of these were very obvious to detect, others were much 
more complex to handle.

###  Airbus A319 flying at 119,000 ft. 
American Airlines A319 that was showing a flight altitude for 119,000 ft. (well 
beyond the edge of space, and obviously bogus).  Inspection of the database showed 
a single entry was involved and a simple delta detection algorythim was all needed 
to detect and remove when encountered.  

### 8300 Mile Range Issue
The current challenge is handling cases where more than a single record is in play 
and the simplistic delta vector is insufficient to mitigate reporting errors. For 
example, in a recent test, the aircraft position data caused a Distance calculation 
of over 8300 miles.  The expected maximum detection range is around 60 miles for a 
fixed ground unit (which I am developing with).

	Start: 2017-10-08 08:07:00.572 +0000 UTC
	-----------------------------------------
	FASTEST:  [F8D460] @ 574 kts
	SLOWEST: N443R [A55739] @ 19 kts
	HIGHEST: N478EV [A5DF4A] @ 51075 ft
	LOWEST:  [F8D460] @ 275 ft
	CLOSEST: N872WH [ABFFC0] 0.22 mi @ 3200 ft
	FURTHEST: N202RR [A19AF8] 8331 mi @ 4600 ft


### Conflated Contact Events
Several times within the database, a contact is represented by a small batch of 
contact records, all with an identical timestamp.  In a lot of these cases there 
are other differences in the records (only distance has changed in every case, 
I suspect that could be minor location drifting of the receiver's own GPS device, 
making the distance calculation drift as well. 

Example:

	Icao_addr | Reg    | Tail   | Alt   | Speed | Distance         | Timestamp
	10870602  | N478EV | N478EV | 51075 | 95    | 776868.38301342  | 2017-10-08 16:30:30.067041328 +0000 UTC
	10870602  | N478EV | N478EV | 51075 | 95    | 776868.38301342  | 2017-10-08 16:30:30.067041328 +0000 UTC
	10870602  | N478EV | N478EV | 51075 | 95    | 776868.38301342  | 2017-10-08 16:30:30.067041328 +0000 UTC
	10870602  | N478EV | N478EV | 51075 | 95    | 776868.594938619 | 2017-10-08 16:30:30.067041328 +0000 UTC
	10870602  | N478EV | N478EV | 51075 | 95    | 776868.806863807 | 2017-10-08 16:30:30.067041328 +0000 UTC
	10870602  | N478EV | N478EV | 51075 | 95    | 776869.018789002 | 2017-10-08 16:30:30.067041328 +0000 UTC
 
*NOTE: Removing the dupes seemed to resolve the *8300 Mile Range Issue* as a side-effect

License
=======

__Copyright (c) 2017  David DeMartini @ Ingenii Group LLC__
 
  Permission is hereby granted, free of charge, to any person obtaining a copy
  of this software and associated documentation files (the "Software"), to deal
  in the Software without restriction, including without limitation the rights
  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  copies of the Software, and to permit persons to whom the Software is
  furnished to do so, subject to the following conditions:
 
  The above copyright notice and this permission notice shall be included in all
  copies or substantial portions of the Software.
 
  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
  SOFTWARE.