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

srcFile=libjmrsid.so.0.0.6
links="libjmrsid.so libjmrsid.so.0"
doLinks
