#!/bin/bash

# Si se produce un error, salimos inmediatamente
set -e


# Obtenemos el locale
LOC=`echo $LANG | sed 's/_.*//'`

# Comprobar que estamos en el directorio adecuado
if [ "$LOC" = "es" ] ; then
  ERROR_BASEDIR="Este script debe invocarse desde el directorio  \'install\' del proyecto"
else
  ERROR_BASEDIR="This script must be run from the \'install\' directory of the project"
fi
if [ ! -d ../../install/instalador-gvSIG-lin ] || [ ! -d ../../install/instalador-gvSIG-win ] || [ ! -d ../../_fwAndami ] ; then
        echo $ERROR_BASEDIR
        exit 1 ;
fi

DIR_BASE=`pwd`
GVSIG_INSTALLDIR=`readlink -f ../../install`
ANDAMI_DIR=`readlink -f ../../_fwAndami`

source variables.sh
# Get a version with format 1_0_alpha
UNDERSCORE_VERSION=`echo $FULLVERSION | sed 'y/./_/'`
# Get a version with format 10_alpha
BRIEFVERSION=`echo $FULLVERSION | sed 's/\.//'`
# The name of the dir which will be ZIPed, containing the resulting installer
INSTALLER_DIR="$APPNAME"-$FULLVERSION\_installer
JARNAME="$APPNAME"-"$FULLVERSION".jar
# The extension targets on the this version of gvSIG:
GVSIG_VERSION=1.0

BUILDNUMBER=`sed -n 's/build.number=//p' "$ANDAMI_DIR"/gvSIG/extensiones/"$MAIN_INSTALL_PLUGIN"/build.number`

#ZIPNAME="$APPNAME"-"$UNDERSCORE_VERSION"\_"$BUILDNUMBER".zip
DIR_LIN=.
DIR_WIN=.

DIR_LIN_GVSIG="$GVSIG_INSTALLDIR"/instalador-gvSIG-lin
DIR_WIN_GVSIG="$GVSIG_INSTALLDIR"/instalador-gvSIG-win


WINDOWSZIP="$APPNAME"-"$BRIEFVERSION"\_"$BUILDNUMBER"-windows-i586.7z
LINUXZIP="$APPNAME"-"$BRIEFVERSION"\_"$BUILDNUMBER"-linux-i586.tgz

LINUXBIN="$APPNAME"-"$BRIEFVERSION"\_"$BUILDNUMBER"-linux-i586.bin

WINDOWSEXE="$APPNAME"-"$BRIEFVERSION"\_"$BUILDNUMBER"-windows-i586.exe

#Directorios
#OUTPUT_DIR=/mnt/sercartlin/grupo-sig-ca/Testing/Versiones/v"$FULLVERSION"\_"$BUILDNUMBER"
OUTPUT_DIR="$TARGET_DIR"/v"$FULLVERSION"\_"$BUILDNUMBER"
PATH_SOURCE_EXT="$ANDAMI_DIR"/gvSIG/extensiones


echo "*****************"
echo "      BASE       "
echo "*****************"

cd "$DIR_BASE"/
rm bin -rf
mkdir -p bin/gvSIG/extensiones
#cp "$DIR_WIN_GVSIG"/installer_files/LEEME "$DIR_WIN_GVSIG"installer_files/LLIG-ME "$DIR_WIN_GVSIG"installer_files/README bin
cp resources/gpl.txt bin
# No enlazamos directamente el directorio lib para no incluir el directorio CVS
mkdir -p bin/lib
cd "$DIR_BASE"/bin/lib
for i in "$ANDAMI_DIR"/lib/*.jar ; do
  ln -s "$i" .
done
for i in "$ANDAMI_DIR"/lib/*.zip ; do
  ln -s "$i" .
done
cd "$DIR_BASE"/
ln -s "$ANDAMI_DIR"/andami.jar bin/
ln -s "$ANDAMI_DIR"/castor.properties bin/
#cp resources/andami-config.xml bin
echo OK.


# Para Windows
#Copiamos el lanzador y sus tracuciones al tmpResources
rm -Rf tmpResources
mkdir tmpResources
cp -R "$GVSIG_INSTALLDIR"/launcher/izpack-launcher-1.3/dist/* ./tmpResources
mv ./tmpResources/launcher-Win32.exe ./tmpResources/gvSIG.exe
#Quitamos el ini, manifest y los CVS
rm ./tmpResources/*.ini ./tmpResources/*.manifest
find ./tmpResources -name CVS -type d -exec rm -rf {} 2> /dev/null ';' || true
echo OK.

# Estas extensiones se copian directamente al directorio destino, ya que no vamos a dar
# opcion a no instalarlas, son obligatorias
cd "$DIR_BASE"
i=0
while [ ! -z ${MANDATORY_EXTENSIONS[$i]} ]
do
  echo ln -s "$PATH_SOURCE_EXT"/${MANDATORY_EXTENSIONS[$i]} bin/gvSIG/extensiones
  ln -s "$PATH_SOURCE_EXT"/${MANDATORY_EXTENSIONS[$i]} bin/gvSIG/extensiones
  i=`expr $i + 1`
done

echo "*****************"
[ $LOC = "es" ] && echo "   EXTENSIONES   "
[ $LOC != "es" ] && echo "   EXTENSIONS   "
echo "*****************"

rm -rf extensiones
mkdir extensiones

i=0
while [ ! -z ${EXTENSIONS[$i]} ]
do
  [ $LOC = "es" ] && echo "Enlazando "${EXTENSIONS[$i]}
  [ $LOC != "es" ] && echo "Linking "${EXTENSIONS[$i]}
  echo ln -s "$PATH_SOURCE_EXT"/${EXTENSIONS[$i]} extensiones
  ln -s "$PATH_SOURCE_EXT"/${EXTENSIONS[$i]} extensiones
  i=`expr $i + 1`
done

echo "*****************"
echo "    INST     "
echo "*****************"
# Generar el instalador (jar)
cd "$DIR_BASE"/
rm "$JARNAME" -f
ant -DJARNAME="$JARNAME" -DGVSIG_VERSION="$GVSIG_VERSION" -DAPPNAME="$APPNAME"


echo "******************"
[ $LOC = "es" ] && echo " GENERAR DISTRIB "
[ $LOC != "es" ] && echo " GENERATE DISTRIB "
echo "******************"
# Generar el tar.gz para Linux y el EXE para Windows

mkdir -p "$OUTPUT_DIR"
echo "- Linux"
## Nueva instalacion para linux
rm -Rf "$INSTALLER_DIR"
mkdir -p "$INSTALLER_DIR"/tmp_gvSIGInstall
cp "$JARNAME" "$INSTALLER_DIR"/tmp_gvSIGInstall
cd "$INSTALLER_DIR"
chmod u+x "$GVSIG_INSTALLDIR"/launcher/izpack-launcher-1.3_linux/dist/launcher-Linux
cp -R "$GVSIG_INSTALLDIR"/launcher/izpack-launcher-1.3_linux/dist/* ./tmp_gvSIGInstall
chmod a+x ./tmp_gvSIGInstall/launcher-Linux
find . -name CVS -type d -exec rm -rf {} 2> /dev/null ';' || true

sed "s/%JARNAME%/$JARNAME/" ./tmp_gvSIGInstall/launcher.ini > ./tmp_gvSIGInstall/launcher.ini.bak
mv ./tmp_gvSIGInstall/launcher.ini.bak ./tmp_gvSIGInstall/launcher.ini;

tar -cvzf ./tmp.tgz ./tmp_gvSIGInstall
echo '#!/bin/sh' > xx.tmp
lcount=`cat xx.tmp "$GVSIG_INSTALLDIR"/launcher/izpack-launcher-1.3_linux/h_gvSIG-install.sh | wc -l`
lcount=$(($lcount+2)) # sumamos dos: uno por la linea siguiente y otro para el inicio
echo "lcount=$lcount" >> xx.tmp
cat xx.tmp "$GVSIG_INSTALLDIR"/launcher/izpack-launcher-1.3_linux/h_gvSIG-install.sh ./tmp.tgz  > "$LINUXBIN"
rm xx.tmp
chmod a+x "$LINUXBIN"
mv "$LINUXBIN" "$OUTPUT_DIR"
cd "$DIR_BASE"/
rm -Rf "$INSTALLER_DIR"
## Fin Nueva instalacion para linux

## Para Windows
echo "- Windows"
#set -x
cd "$DIR_BASE"
rm -Rf "$INSTALLER_DIR"

mkdir "$INSTALLER_DIR"
cp -aR "$GVSIG_INSTALLDIR"/launcher/izpack-launcher-1.3/dist/* "$INSTALLER_DIR"
rm -f "$INSTALLER_DIR"/install.bat

find "$INSTALLER_DIR" -name CVS -type d -exec rm -rf {} 2> /dev/null ';' || true

## hacemos las sustituciones de la variable
sed "s/%JARNAME%/$JARNAME/" "$INSTALLER_DIR"/launcher-Win32.ini > "$INSTALLER_DIR"/launcher-Win32.ini.bak
mv "$INSTALLER_DIR"/launcher-Win32.ini.bak "$INSTALLER_DIR"/launcher-Win32.ini;


mv "$JARNAME" "$INSTALLER_DIR"
cd "$INSTALLER_DIR"

if [ -f  "$DIR_BASE"/"$WINDOWSZIP" ] ; then
	rm -f "$DIR_BASE"/"$WINDOWSZIP"
fi
chmod u+x "$GVSIG_INSTALLDIR"/launcher/7z/7za
"$GVSIG_INSTALLDIR"/launcher/7z/7za a -r "$DIR_BASE"/"$WINDOWSZIP" *
cd -
sed "s/%TITLE%/$APPNAME-$BRIEFVERSION\_$BUILDNUMBER/" "$GVSIG_INSTALLDIR"/launcher/7z/dist_config.txt > dist_config.txt
cat "$GVSIG_INSTALLDIR"/launcher/7z/7zS.sfx  dist_config.txt "$WINDOWSZIP" > "$WINDOWSEXE"

###


rm dist_config.txt
rm "$WINDOWSZIP"

rm -Rf "$INSTALLER_DIR"

mv "$WINDOWSEXE" "$OUTPUT_DIR"

# Limpiamos tmpResources
cd "$DIR_BASE"/
rm -r ./tmpResources
cd -

## Limpiamos

rm -rf extensiones
rm -rf bin

echo "******************"
[ $LOC = "es" ] && echo " GENERAR FUENTES "
[ $LOC != "es" ] && echo " GENERATE SOURCES "
echo "******************"

cd ..
ant generate-source-package
mv dist-src/gv*-src.zip "$OUTPUT_DIR"
cd -


echo "*****************"
echo "    FIN     "
echo "*****************"

echo ""
echo "Generados los ficheros:"
echo "$OUTPUT_DIR"
ls -lh "$OUTPUT_DIR"
cd "$DIR_BASE"
