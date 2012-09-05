#!/bin/sh
# gvSIG.sh

GVSIG_SH_FILE=`readlink -f $0`
GVSIG_BIN=`dirname $GVSIG_SH_FILE`
#GVSIG_JRE_HOME=$HOME/gvSIG/jre/1.5.0_12
GVSIG_JRE_HOME=$HOME/gvSIG/jre/1.5.0_12
# Comporbar la JRE
if [ ! -f "$GVSIG_JRE_HOME/bin/java" ] 
then 
{
	echo No se ha encontrado el ejecutalble de la JRE en $GVSIG_JRE_HOME/bin/java
	echo 
	echo "Edite este fichero ($0) y modifique la variable GVSIG_JRE_HOME"
	echo para que tenga una ruta al JAVA_HOME preparado para gvSIG
	exit 1
}
fi

export JAVA_HOME="$GVSIG_JRE_HOME"
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:"$GVSIG_LIBS"
cd "$GVSIG_BIN"


for i in $GVSIG_BIN/lib/*.jar ; do
  LIBRARIES=$LIBRARIES:"$i"
done
for i in $GVSIG_BIN/lib/*.zip ; do
  LIBRARIES=$LIBRARIES:"$i"
done

"$JAVA_HOME"/bin/java -cp $GVSIG_BIN/bin:$LIBRARIES  -Xmx500M com.iver.andami.Launcher gvSIG gvSIG/extensiones "$@"