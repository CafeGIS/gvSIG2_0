#!/bin/sh

export APPNAME=gvsig_expressionfield
export FULLVERSION='0.1'
#export FULLVERSION='1.0'
export TARGET_DIR=/tmp/"$APPNAME"/versiones
#export TARGET_DIR=/mnt/sercartlin/grupo-sig-ca/Testing/Versiones/
export MAIN_INSTALL_PLUGIN=com.iver.gvsig.expressionfield #Nombre del plugin que proporciona el 'build.nuber' para la distribucion

## estas extensiones se muestran en los packs, y se puede elegir instalarlos o no
## (el fichero install.xml tambien debe estar actualizado para reflejar esto)
EXTENSIONS=(
"$MAIN_INSTALL_PLUGIN"
)
