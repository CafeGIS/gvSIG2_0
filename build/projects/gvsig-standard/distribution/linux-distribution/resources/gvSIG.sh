#!/bin/sh
# gvSIG.sh
export GVSIG_LIBS="$INSTALL_PATH/libs/"
export LD_LIBRARY_PATH="$LD_LIBRARY_PATH:$GVSIG_LIBS"
export PROJ_LIB="$INSTALL_PATH/bin/gvSIG/extensiones/org.gvsig.crs/data"
export GDAL_DATA="$GVSIG_LIBS/gdal_data"
cd "$INSTALL_PATH/bin"
#java -Djava.library.path=/usr/lib:"$INSTALL_PATH/libs" -cp andami.jar:./lib/gvsig-i18n.jar:./lib/beans.jar:./lib/log4j-1.2.8.jar:./lib/iver-utiles.jar:./lib/castor-0.9.5.3-xml.jar:./lib/xerces_2_5_0.jar:./lib/javaws.jar:./lib/xml-apis.jar:./lib/looks-2.0.2.jar:./lib/JWizardComponent.jar -Xmx500M com.iver.andami.Launcher gvSIG gvSIG/extensiones $1

for i in ./lib/*.jar ; do
  LIBRARIES=$LIBRARIES:"$i"
done
for i in ./lib/*.zip ; do
  LIBRARIES=$LIBRARIES:"$i"
done

$JAVA_HOME/bin/java -Djava.library.path=/usr/lib:"$INSTALL_PATH/libs" -cp $LIBRARIES -Xmx500M com.iver.andami.Launcher gvSIG gvSIG/extensiones "$@"

