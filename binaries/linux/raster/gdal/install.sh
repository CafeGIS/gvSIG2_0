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
srcFile=libcrypto.so.0.9.7
links="libcrypto.so libcrypto.so.0"
doLinks

srcFile=libgdal.so.1.12.2
links="libgdal.so libgdal.so.1"
doLinks

srcFile=libjasper-1.701.so.1
links=libjasper-1.701.so
doLinks

srcFile=libjgdal.so.0.9.1
links="libjgdal.so libjgdal.so.0"
doLinks

srcFile=libodbc.so.1
links=libodbc.so
doLinks

srcFile=libpng.so.3
links=libpng.so
doLinks

srcFile=libpq.so.5.1
links=libpq.so.5
doLinks

srcFile=libssl.so.0.9.7
links="libssl.so libssl.so.0"
doLinks

srcFile=libcrypt.so.1
links=libcrypt.so
doLinks

srcFile=libjpeg.so.62
links=libjpeg.so
doLinks

srcFile=libm.so.6
links=libm.so
doLinks

srcFile=libnsl.so.1
links=libnsl.so
doLinks

srcFile=libz.so.1
links=libz.so
doLinks
