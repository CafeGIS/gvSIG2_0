#!/bin/bash

arg_path=$1;
sefile=$2
FILES="pom.xml"

if [ $# -ne 2 ] 
then 
	echo "usage: $0 [path] $2 [sedfile]"
else
	for i in $FILES; do
 		find $arg_path -name $i | grep -v ".svn" | xargs sed -i -f $2;
	done
fi

