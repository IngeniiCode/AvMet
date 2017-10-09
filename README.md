# AvMet
Aviation Metrics Project -- Extract data from STRATUX daily database logs

# What's cool
Nothing yet.. but it's getting there!   (Oct 2017)

# Running AvMet
AvMet expectes the first parameter to be path to STRATUX database file.  File can be compressed (.giz only at this time).  

ex:
  *   java -jar dist/AvMet.jar  <path_to_db>

Optional extra paramters where can be in any order after the database

 * keep -  This will leave the uncompressed DB (other artifact files are still cleaned up).  
           If the original file was not compressed, database is not removed so flag is not required.


