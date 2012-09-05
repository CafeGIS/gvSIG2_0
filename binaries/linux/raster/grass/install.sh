#!/bin/sh
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
	if [ -L $targetDir/$srcFile -o ! -e $targetDir/$srcFile ];then
	{
		ln -sf $src $targetDir/$srcFile
	}
	fi
}
srcFile=libgrass_datetime.6.0.2.so
links=libgrass_datetime.so
doLinks

srcFile=libgrass_dbmibase.6.0.2.so
links=libgrass_dbmibase.so
doLinks

srcFile=libgrass_dbmiclient.6.0.2.so
links=libgrass_dbmiclient.so
doLinks

srcFile=libgrass_dgl.6.0.2.so
links=libgrass_dgl.so
doLinks

srcFile=libgrass_dig2.6.0.2.so
links=libgrass_dig2.so
doLinks

srcFile=libgrass_gis.6.0.2.so
links=libgrass_gis.so
doLinks

srcFile=libgrass_gmath.6.0.2.so
links=libgrass_gmath.so
doLinks

srcFile=libgrass_gproj.6.0.2.so
links=libgrass_gproj.so
doLinks

srcFile=libgrass_I.6.0.2.so
links=libgrass_I.so
doLinks

srcFile=libgrass_linkm.6.0.2.so
links=libgrass_linkm.so
doLinks

srcFile=libgrass_rtree.6.0.2.so
links=libgrass_rtree.so
doLinks

srcFile=libgrass_vask.6.0.2.so
links=libgrass_vask.so
doLinks

srcFile=libgrass_vect.6.0.2.so
links=libgrass_vect.so
doLinks
