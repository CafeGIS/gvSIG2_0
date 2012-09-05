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


srcFile=libjecw.so.0.0.6
links="libjecw.so libjecw.so.0"
doLinks

srcFile=libNCScnet.so
links="libNCScnet.so.0 libNCSCnet.so libNCSCnet.so.0 libNCSCNet.so libNCSCNet.so.0"
doLinks

srcFile=libNCSEcw.so
links=libNCSEcw.so.0
doLinks

srcFile=libNCSEcwC.so
links=libNCSEcwC.so.0
doLinks

srcFile=libNCSUtil.so
links=libNCSUtil.so.0
doLinks