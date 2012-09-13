#!/bin/sh
# gvSIG.sh
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:"$INSTALL_PATH/libs/"
export PROJ_LIB="$INSTALL_PATH/bin/gvSIG/extensiones/org.gvsig.crs/data"
cd "$INSTALL_PATH/bin"
#java -Djava.library.path=/usr/lib:"$INSTALL_PATH/libs" -cp andami.jar:./lib/gvsig-i18n.jar:./lib/beans.jar:./lib/log4j-1.2.8.jar:./lib/iver-utiles.jar:./lib/castor-0.9.5.3-xml.jar:./lib/crimson.jar:./lib/xerces_2_5_0.jar:./lib/javaws.jar:./lib/xml-apis.jar:./lib/looks-2.0.2.jar:./lib/JWizardComponent.jar:./gvSIG/extensiones/com.iver.ai2.gvsig3d/lib/jogl.jar:./gvSIG/extensiones/com.iver.ai2.gvsig3d/lib/gluegen-rt.jar:./gvSIG/extensiones/com.iver.ai2.gvsig3d/lib/libjosg-core-1.0-SNAPSHOT.jar:./gvSIG/extensiones/com.iver.ai2.gvsig3d/lib/libjosg-features-1.0-SNAPSHOT.jar:./gvSIG/extensiones/com.iver.ai2.gvsig3d/lib/libjosg-planets-1.0-SNAPSHOT.jar:./gvSIG/extensiones/com.iver.ai2.gvsig3d/lib/libjosg-viewer-1.0-SNAPSHOT.jar -Xmx500M com.iver.andami.Launcher gvSIG gvSIG/extensiones $1

for i in ./lib/*.jar ; do
  LIBRARIES=$LIBRARIES:"$i"
done
for i in ./lib/*.zip ; do
  LIBRARIES=$LIBRARIES:"$i"
done

$JAVA_HOME/bin/java -Djava.library.path=/usr/lib:"$INSTALL_PATH/libs" -cp andami.jar$LIBRARIES -Xmx500M com.iver.andami.Launcher gvSIG gvSIG/extensiones "$@"

