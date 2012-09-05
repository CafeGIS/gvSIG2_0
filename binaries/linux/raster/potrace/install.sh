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
srcFile=libpotrace.so.0.0.1
links="libpotrace.so libpotrace.so.0"
doLinks

srcFile=libjpotrace.so.0.0.1
links="libjpotrace.so libjpotrace.so.0"
doLinks
