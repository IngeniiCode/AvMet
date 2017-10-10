# AvMet
Aviation Metrics Project -- Extract data from STRATUX daily database logs

# What's cool
Nothing yet.. but it's getting there!   (Oct 2017)

# Running AvMet
AvMet expects the first parameter to be path to STRATUX database file.  File can 
be compressed (.giz only at this time).

ex:
  *   java -jar dist/AvMet.jar  <path_to_db>

*Required parameter*
Must declare the path (must be resolvable from current path) to input data file; 
either an SQLite3 db file, or a Gzip compressed SQLite3 db file.


Optional extra parameters where can be in any order after the database

 * keep - This will leave the uncompressed DB (other artifact files are still 
          cleaned up).  If the original file was not compressed, database is 
          not removed so flag is not required.
 
 * scrub - Traverse the <traffic> table, and remove events that do not fit the 
           expected progression for a specific contact.  This removes transient
           bad that has been seen in majority live database samples

 * condense -  Traverse the <traffic> table, and remove events have identical 
               Altitude, Speed within a specific contact's dataset, this can 
               condense the database by 40% to 75% 

 * usetemp = When extracting a database from SQL, write to a local temp file.  This
             is most useful when the source database is compressed and on a 
             remote or temporary device:  output file will be  ./sqlite-stratux-temp
