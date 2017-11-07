#!/usr/bin/perl

# get hex value from CLI
my $icoHex = $ARGV[0];

# convert into an integer value
my $icoInt = unpack('l',pack 'l', hex($icoHex));

# display conversion
printf("Decoding %s to %d\n",$icoHex,$icoInt);

exit;

