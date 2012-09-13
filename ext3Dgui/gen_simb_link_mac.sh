export BINARIES_3D=$PWD/../binaries/mac/3D;
echo $BINARIES_3D;
cd /Developer/DepMan/lib
find . -type l -exec ls -l {} ';' |sed 's/.*[.][/]//g' | sed 's/\(.*\) -> \(.*\)$/ln -sf \2 \1/g' > $BINARIES_3D/install-3D.tmp ;
cat $BINARIES_3D/install-3D.sh.head $BINARIES_3D/install-3D.tmp > $BINARIES_3D/install-3D.sh ;
rm -f $BINARIES_3D/install-3D.tmp
