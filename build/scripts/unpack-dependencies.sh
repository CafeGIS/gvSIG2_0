#!/bin/bash

dep_dir=$1;
dest_dir=$2;

if [ ! -d $dest_dir ]; then mkdir $dest_dir; fi;
if [ ! -d $dep_dir ]; then exit 0; fi 

files=$(find $dep_dir -name *.tar.gz)
for i in $files; do
	echo "Expanding $i in $dest_dir"
	tar xzf $i -C $dest_dir
done

