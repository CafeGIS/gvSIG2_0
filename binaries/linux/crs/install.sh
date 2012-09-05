#!/bin/sh

VER=0.1.1
VER_1=0
VERPROJ=0.5.0
VERPROJ_1=0

srcDir=`dirname $0`
if [ -z "$1" ]
then {
	targetDir=.
} else {
	targetDir=$1
}
fi

doLinks() {
	local src=$srcDir/$srcFile
	for alink in $links
	do
	{
		if [ -L $targetDir/$alink -o ! -e $targetDir/$alink ]; then
		{
			ln -sf $src $targetDir/$alink
		}
		fi
	}	
	done
	if [ -L $targetDir/$srcFile -o ! -e $targetDir/$srcFile ]; then
	{
		ln -sf $src $targetDir/$srcFile
	}
	fi
}


srcFile=libcrsjniproj.so.$VER
links="libcrsjniproj.so libcrsjniproj.so.$VER_1"
doLinks

srcFile=libproj.so.$VERPROJ
links="libproj.so libproj.so.$VERPROJ_1"
doLinks