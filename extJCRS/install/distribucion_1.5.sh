#!/bin/bash

# Si se produce un error, salimos inmediatamente
set -e

# Obtenemos el locale
LOC=`echo $LANG | sed 's/_.*//'`

# Comprobar que estamos en el directorio adecuado
if [ "$LOC" = "es" ] ; then
  ERROR_BASEDIR="Este script debe invocarse desde el directorio raiz del workspace o desde el directorio \'install\'"
else
  ERROR_BASEDIR="This script must be run from the workspace\'s root directory or from the \'install\' directory"
fi
[ -d _fwAndami ] && [ -d install ] && cd install
if [ ! -d instalador-gvSIG-lin ] || [ ! -d instalador-gvSIG-win ] || [ ! -d ../_fwAndami ] ; then
        echo $ERROR_BASEDIR
        exit 1 ;
fi

DIR_BASE=`pwd`

source variables.sh
# Get a version with format 1_x_x
UNDERSCORE_VERSION=`echo $FULLVERSION | sed 's/\./_/g'`
# The name of the dir which will be ZIPed, containing the resulting installer
INSTALLER_DIR="$APPNAME"-$FULLVERSION\_installer
JARNAME="$APPNAME"-"$FULLVERSION".jar
# The extension targets on the this version of gvSIG:
GVSIG_VERSION=1.0

BUILDNUMBER=`sed -n 's/build.number=//p' ../_fwAndami/gvSIG/extensiones/com.iver.cit.gvsig/build.number`

#ZIPNAME="$APPNAME"-"$UNDERSCORE_VERSION"\_"$BUILDNUMBER".zip
DIR_LIN=instalador-gvSIG-lin
DIR_WIN=instalador-gvSIG-win
DIR_MAC=instalador-gvSIG-mac
BASE_FILE_NAME="$APPNAME"-"$UNDERSCORE_VERSION"-"$BUILDNUMBER"
WINDOWSZIP="$BASE_FILE_NAME"-windows-i586_j1_5.7z
LINUXZIP="$BASE_FILE_NAME"-linux-i586_J1_5.tgz

LINUXBIN="$BASE_FILE_NAME"-linux-i586_J1_5.bin
LINUXBINWITHJRE="$BASE_FILE_NAME"-linux-i586-withjre_J1_5.bin

WINDOWSEXE="$BASE_FILE_NAME"-windows-i586_J1_5.exe
WINDOWSEXEWITHJRE="$BASE_FILE_NAME"-windows-i586-withjre_J1_5.exe

MAC_10_ZIP="$BASE_FILE_NAME"-mac-10_4.zip

#Directorios
#OUTPUT_DIR=/mnt/sercartlin/grupo-sig-ca/Testing/Versiones/v"$FULLVERSION"\_"$BUILDNUMBER"
OUTPUT_DIR="$TARGET_DIR"/v"$FULLVERSION"\_"$BUILDNUMBER"
PATH_SOURCE_EXT=../_fwAndami/gvSIG/extensiones
PATH_SOURCE_MANDAT_EXT=../../../../../_fwAndami/gvSIG/extensiones


echo "*****************"
echo "      BASE       "
echo "*****************"
# Para Linux
echo -n "$DIR_BASE"/"$DIR_LIN" "-- "
cd "$DIR_BASE"/"$DIR_LIN"
rm bin -rf
mkdir -p bin/gvSIG/extensiones
cp installer_files/LEEME installer_files/LLIG-ME installer_files/README bin
cp resources/gpl.txt bin
# No enlazamos directamente el directorio lib para no incluir el directorio CVS
mkdir -p bin/lib
cd "$DIR_BASE"/"$DIR_LIN"/bin/lib
for i in ../../../../_fwAndami/lib/*.jar ; do
  ln -s "$i" .
done
for i in ../../../../_fwAndami/lib/*.zip ; do
  ln -s "$i" .
done
cd "$DIR_BASE"/"$DIR_LIN"
ln -s ../../../_fwAndami/andami.jar bin/
ln -s ../../../_fwAndami/castor.properties bin/
#cp resources/andami-config.xml bin
echo OK.


# Para Windows
echo -n "$DIR_BASE"/"$DIR_WIN" "-- "
cd "$DIR_BASE"/"$DIR_WIN"
rm bin -rf
mkdir -p bin/gvSIG/extensiones
cp installer_files/LEEME.txt installer_files/LLIG-ME.txt installer_files/README.txt bin
cp resources/gpl.txt bin
# No enlazamos directamente el directorio lib para no incluir el directorio CVS
mkdir -p bin/lib
cd "$DIR_BASE"/"$DIR_WIN"/bin/lib
for i in ../../../../_fwAndami/lib/*.jar ; do
  ln -s "$i" .
done
for i in ../../../../_fwAndami/lib/*.zip ; do
  ln -s "$i" .
done
cd "$DIR_BASE"/"$DIR_WIN"
ln -s ../../../_fwAndami/andami.jar bin
ln -s ../../../_fwAndami/castor.properties bin/
#cp resources/andami-config.xml bin

#Copiamos el lanzador y sus tracuciones al tmpResources
rm -Rf tmpResources
mkdir tmpResources
cp -R ../launcher/izpack-launcher-1.3/dist/* ./tmpResources
mv ./tmpResources/launcher-Win32.exe ./tmpResources/gvSIG.exe
cp -R ./jre_installers_1.5/jai_imageio-1_0_01-lib-windows ./tmpResources
#Quitamos el ini, manifest y los CVS
rm ./tmpResources/*.ini ./tmpResources/*.manifest
find ./tmpResources -name CVS -type d -exec rm -rf {} 2> /dev/null ';' || true
find ./tmpResources -name .svn -type d -exec rm -rf {} 2> /dev/null ';' || true
cp ./resources/gvSIG.ini.J15 ./tmpResources/gvSIG.ini
echo OK.

# Estas extensiones se copian directamente al directorio destino, ya que no vamos a dar
# opcion a no instalarlas, son obligatorias
cd "$DIR_BASE"
i=0
while [ ! -z ${MANDATORY_EXTENSIONS[$i]} ]
do
  echo ln -s "$PATH_SOURCE_MANDAT_EXT"/${MANDATORY_EXTENSIONS[$i]} "$DIR_LIN"/bin/gvSIG/extensiones
  ln -s "$PATH_SOURCE_MANDAT_EXT"/${MANDATORY_EXTENSIONS[$i]} "$DIR_LIN"/bin/gvSIG/extensiones
  echo ln -s "$PATH_SOURCE_MANDAT_EXT"/${MANDATORY_EXTENSIONS[$i]} "$DIR_WIN"/bin/gvSIG/extensiones
  ln -s "$PATH_SOURCE_MANDAT_EXT"/${MANDATORY_EXTENSIONS[$i]} "$DIR_WIN"/bin/gvSIG/extensiones
  i=`expr $i + 1`
done

echo "*****************"
[ $LOC = "es" ] && echo "   EXTENSIONES   "
[ $LOC != "es" ] && echo "   EXTENSIONS   "
echo "*****************"

rm -rf $DIR_LIN/extensiones
mkdir $DIR_LIN/extensiones
rm -rf $DIR_WIN/extensiones
mkdir $DIR_WIN/extensiones

i=0
while [ ! -z ${EXTENSIONS[$i]} ]
do
  [ $LOC = "es" ] && echo "Copiando "${EXTENSIONS[$i]}
  [ $LOC != "es" ] && echo "Copying "${EXTENSIONS[$i]}
  echo cp "$PATH_SOURCE_EXT"/${EXTENSIONS[$i]} "$DIR_LIN"/extensiones -rf
  cp "$PATH_SOURCE_EXT"/${EXTENSIONS[$i]} "$DIR_LIN"/extensiones -rf
  echo cp "$PATH_SOURCE_EXT"/${EXTENSIONS[$i]} "$DIR_WIN"/extensiones -rf
  cp "$PATH_SOURCE_EXT"/${EXTENSIONS[$i]} "$DIR_WIN"/extensiones -rf
  i=`expr $i + 1`
done

## Eliminamos el driver de oracle
find -L . -name ojdbc14.jar -type f -exec rm -rf {} 2> /dev/null ';' || true
find -L . -name gt2-oracle-spatial-2.2.1.jar -type f -exec rm -rf {} 2> /dev/null ';' || true
find -L . -name sdoapi.jar -type f -exec rm -rf {} 2> /dev/null ';' || true


echo "*****************"
echo "    INST-WIN     "
echo "*****************"
# Generar el instalador (jar) para windows
cd "$DIR_BASE"/"$DIR_WIN"
rm "$JARNAME" -f
ant -DJARNAME="$JARNAME" -DGVSIG_VERSION="$GVSIG_VERSION" -DAPPNAME="$APPNAME" -DINSTALL_FILE="install_15.xml"

echo "*****************"
echo "    INST-LIN     "
echo "*****************"
# Generar el instalador (jar) para Linux
cd "$DIR_BASE"/"$DIR_LIN"
rm "$JARNAME" -f
ant -DJARNAME="$JARNAME" -DGVSIG_VERSION="$GVSIG_VERSION" -DAPPNAME="$APPNAME"


echo "******************"
[ $LOC = "es" ] && echo " GENERAR DISTRIB "
[ $LOC != "es" ] && echo " GENERATE DISTRIB "
echo "******************"
# Generar el tar.gz para Linux y el EXE para Windows

mkdir -p "$OUTPUT_DIR"
echo "- Linux"
## Para Linux
cd "$DIR_BASE"/"$DIR_LIN"
rm -Rf "$INSTALLER_DIR"
cp -a installer_files "$INSTALLER_DIR"
rm -R "$INSTALLER_DIR"/CVS 2> /dev/null && true
# Set the correct version number in the scripts and files
if [ -f  "$INSTALLER_DIR"/install.sh ] ; then
  sed "s/JARNAME/$JARNAME/" "$INSTALLER_DIR"/install.sh > "$INSTALLER_DIR"/install.sh.bak
  mv "$INSTALLER_DIR"/install.sh.bak "$INSTALLER_DIR"/install.sh ;
  chmod +x "$INSTALLER_DIR"/install.sh
fi
#mv "$JARNAME" "$INSTALLER_DIR"
cp "$JARNAME" "$INSTALLER_DIR"
tar -cvzf "$LINUXZIP" "$INSTALLER_DIR"
rm -Rf "$INSTALLER_DIR"
mv "$LINUXZIP" "$OUTPUT_DIR"

## Nueva instalacion para linux
cd "$DIR_BASE"/"$DIR_LIN"
rm -Rf "$INSTALLER_DIR"
mkdir -p "$INSTALLER_DIR"/tmp_gvSIGInstall
cp "$JARNAME" "$INSTALLER_DIR"/tmp_gvSIGInstall
cd "$INSTALLER_DIR"
cp -R "$DIR_BASE"/launcher/izpack-launcher-1.3_linux/dist/* ./tmp_gvSIGInstall
find . -name CVS -type d -exec rm -rf {} 2> /dev/null ';' || true
find . -name .svn -type d -exec rm -rf {} 2> /dev/null ';' || true
rm ./tmp_gvSIGInstall/launcher.ini
cp "$DIR_BASE"/"$DIR_LIN"/resources/launcher.ini.j15 ./tmp_gvSIGInstall/launcher.ini
sed "s/%JARNAME%/$JARNAME/" ./tmp_gvSIGInstall/launcher.ini > ./tmp_gvSIGInstall/launcher.ini.bak
mv ./tmp_gvSIGInstall/launcher.ini.bak ./tmp_gvSIGInstall/launcher.ini;

tar -cvzf ./tmp.tgz ./tmp_gvSIGInstall
cp "$DIR_BASE"/"$DIR_LIN"/jre/*-1_5_0_*.gz ./tmp_gvSIGInstall
tar -cvzf ./tmp_wjre.tgz ./tmp_gvSIGInstall
echo '#!/bin/sh' > xx.tmp
lcount=`cat xx.tmp "$DIR_BASE"/launcher/izpack-launcher-1.3_linux/h_gvSIG-install.sh | wc -l`
lcount=$(($lcount+2)) # sumamos dos: uno por la linea siguiente y otro para el inicio
echo "lcount=$lcount" >> xx.tmp
cat xx.tmp "$DIR_BASE"/launcher/izpack-launcher-1.3_linux/h_gvSIG-install.sh ./tmp.tgz  > "$LINUXBIN"
cat xx.tmp "$DIR_BASE"/launcher/izpack-launcher-1.3_linux/h_gvSIG-install.sh ./tmp_wjre.tgz  > "$LINUXBINWITHJRE"
rm xx.tmp
chmod a+x "$LINUXBIN" "$LINUXBINWITHJRE"
mv "$LINUXBINWITHJRE" "$LINUXBIN" "$OUTPUT_DIR"
cd "$DIR_BASE"/"$DIR_LIN"
rm -Rf "$INSTALLER_DIR"
## Fin Nueva instalacion para linux
rm "$JARNAME"




## Para Windows
echo "- Windows"
#set -x
cd "$DIR_BASE"/"$DIR_WIN"
rm -Rf "$INSTALLER_DIR"
cp -a installer_files "$INSTALLER_DIR"

cp -aR ../launcher/izpack-launcher-1.3/dist/* "$INSTALLER_DIR"
rm -f "$INSTALLER_DIR"/install.bat
cp ./resources/launcher-Win32.ini.j15 "$INSTALLER_DIR"/launcher-Win32.ini

find "$INSTALLER_DIR" -name CVS -type d -exec rm -rf {} 2> /dev/null ';' || true
find "$INSTALLER_DIR" -name .svn -type d -exec rm -rf {} 2> /dev/null ';' || true

## hacemos las sustituciones de la variable
sed "s/%JARNAME%/$JARNAME/" "$INSTALLER_DIR"/launcher-Win32.ini > "$INSTALLER_DIR"/launcher-Win32.ini.bak
mv "$INSTALLER_DIR"/launcher-Win32.ini.bak "$INSTALLER_DIR"/launcher-Win32.ini;


mv "$JARNAME" "$INSTALLER_DIR"
cd "$INSTALLER_DIR"

if [ -f  "$DIR_BASE"/"$DIR_WIN"/"$WINDOWSZIP" ] ; then
	rm -f "$DIR_BASE"/"$DIR_WIN"/"$WINDOWSZIP"
fi
"$DIR_BASE"/launcher/7z/7za a -r "$DIR_BASE"/"$DIR_WIN"/"$WINDOWSZIP" *
cd -
sed "s/%TITLE%/$APPNAME-$UNDERSCORE_VERSION\_$BUILDNUMBER/" ../launcher/7z/dist_config.txt > dist_config.txt
cat ../launcher/7z/7zS.sfx  dist_config.txt "$WINDOWSZIP" > "$WINDOWSEXE"

### paquete con los instalables de JRE, Jai y Jai i/o
"$DIR_BASE"/launcher/7z/7za a "$DIR_BASE"/"$DIR_WIN"/"$WINDOWSZIP" "$DIR_BASE"/"$DIR_WIN"/jre_installers_1.5/*.exe
cat ../launcher/7z/7zS.sfx  dist_config.txt "$WINDOWSZIP" > "$WINDOWSEXEWITHJRE"

###


rm dist_config.txt
rm "$WINDOWSZIP"

rm -Rf "$INSTALLER_DIR"

mv "$WINDOWSEXE" "$OUTPUT_DIR"
mv "$WINDOWSEXEWITHJRE" "$OUTPUT_DIR"

# Limpiamos tmpResources
cd "$DIR_BASE"/"$DIR_WIN"
rm -r ./tmpResources
cd -


## Para Mac
echo "- Mac"
if type ant 2>/dev/null >/dev/null; then
	cd "$DIR_BASE"/"$DIR_MAC"
	if ant -Dbuild.number="$BUILDNUMBER" -f build.xml; then
		zip -rq "$MAC_10_ZIP" gvSIG*.app
		rm -r gvSIG*.app
		mv "$MAC_10_ZIP" "$OUTPUT_DIR"
	else
		echo "No se ha generado el paquete para Mac 10 . Compruebe que su ant tiene jarbundler instalado

			  Nota: debemos tener incluido en el ANT_HOME/lib el
	    	  jar jarbundler-1.9.jar que se puede encontrar en http://jarbundler.sourceforge.net/
	    "

	fi

else
	    echo "No se ha encontrado el ant en el path de ejecucion:
	    Es necesario para poder generar la distribucion para el Mac.

	    Nota: debemos tener tambien incluido en el ANT_HOME/lib el
	    jar jarbundler-1.9.jar que se puede encontrar en http://jarbundler.sourceforge.net/
	    "
fi


## Limpiamos

rm -rf $DIR_LIN/extensiones
rm -rf $DIR_LIN/bin
rm -rf $DIR_WIN/extensiones
rm -rf $DIR_WIN/bin

echo "*****************"
echo "    FIN     "
echo "*****************"

echo ""
echo "Generados los ficheros:"
echo "$OUTPUT_DIR"
ls -lh "$OUTPUT_DIR"
cd "$DIR_BASE"
