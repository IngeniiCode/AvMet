AvMet
=====
Aviation Metrics Project -- Extract data from STRATUX daily database logs.

Developed by David DeMartini @ Ingenii Group LLC.  

### Contact Information:
David DeMartini
Ingenii Group LLC

http://www.daviddemartini.com
software@ingeniigroup.com


Announcements!
==============
### 11-OCT-2017
First Best merged to master for user evaluations!!  Welcome to Aviation Metrics (AvMet).
 

Running AvMet
=============
AvMet expects the first parameter to be path to STRATUX database file.  File can 
be compressed (.giz only at this time).

ex:
  *   java -jar dist/AvMet.jar  <path_to_db>

### Required parameter
Must declare the path (must be resolvable from current path) to input data file; 
either an SQLite3 db file, or a Gzip compressed SQLite3 db file.

### Optional parameters
Optional extra parameters where can be in any order after the database

 * keep - This will leave the uncompressed DB (other artifact files are still 
          cleaned up).  If the original file was not compressed, database is 
          not removed so flag is not required.
 
 * scrub - Traverse the <traffic> table, and remove events that do not fit the 
           expected progression for a specific contact.  This removes transient
           bad that has been seen in majority live database samples

 * condense -  Traverse the <traffic> table, and remove events have identical 
               Altitude, Speed within a specific contact's dataset, this can 
               condense the database by 40% to 75%   (11-OCT-2017 disabled due to bug)

 * usetemp - When extracting a database from SQL, write to a local temp file.  This
             is most useful when the source database is compressed and on a 
             remote or temporary device:  output file will be  ./sqlite-stratux-temp

 * verbose - Report more detailed scrubbing activity


Release Notes
=============

### 11-OCT-2017
Beta 2 merged to master for user evaluations!!  Welcome to Aviation Metrics (AvMet).

###  9-OCT-2017
First Beta merged to master.  

Development Notes
=================

## Data Scrubbing

*Note: 11-OCT-2017*
Scrubbing processor has a bug that prevents it from properly handling more than a 
single out of range error. 

During analysis of example datasets, anomalous entries were detected in nearly each 
of the databases.  Some of these were very obvious to detect, others were much more 
complex to handle.

###  Airbus A319 flying at 119,000 ft. 
American Airlines A319 that was showing a flight altitude for 119,000 ft. (well
beyond the edge of space, and obviously bogus).  Inspection of the database showed a
single entry was involved and a simple delta detection algorythim was all needed to
detect and remove when encountered.  

### 8300 Mile Range Issue
The current challenge is handling cases where more than a single record is in play
and the simplistic delta vector is insufficient to mitigate reporting errors. 
For example, in a recent test, the aircraft position data caused a Distance 
calculation of over 8300 miles.  The expected maximum detection range is around
60 miles for a fixed ground unit (which I am developing with).

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
are other differences in the records (only distance has changed in every case, I
suspect that could be minor location drifting of the receiver's own GPS device, 
making the distance calculation drift as well. 

Example:

	Icao_addr | Reg    | Tail   | Alt   | Speed | Distance         | Timestamp
	10870602  | N478EV | N478EV | 51075 | 95    | 776868.38301342  | 2017-10-08 16:30:30.067041328 +0000 UTC
	10870602  | N478EV | N478EV | 51075 | 95    | 776868.38301342  | 2017-10-08 16:30:30.067041328 +0000 UTC
	10870602  | N478EV | N478EV | 51075 | 95    | 776868.38301342  | 2017-10-08 16:30:30.067041328 +0000 UTC
	10870602  | N478EV | N478EV | 51075 | 95    | 776868.594938619 | 2017-10-08 16:30:30.067041328 +0000 UTC
	10870602  | N478EV | N478EV | 51075 | 95    | 776868.806863807 | 2017-10-08 16:30:30.067041328 +0000 UTC
	10870602  | N478EV | N478EV | 51075 | 95    | 776869.018789002 | 2017-10-08 16:30:30.067041328 +0000 UTC
 
*NOTE: Removing the dupes seemed to resolve the <8300 Mile Range Issue> as a side-effect

