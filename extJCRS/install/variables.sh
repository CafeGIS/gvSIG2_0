#!/bin/sh

export APPNAME=gvSIG
export FULLVERSION='1.1.2'
export TARGET_DIR=/tmp/versiones/
#export TARGET_DIR=/mnt/sercartlin/grupo-sig-ca/Testing/Versiones/


## estas extensiones se muestran en los packs, y se puede elegir instalarlos o no
## (el fichero install.xml también debe estar actualizado para reflejar esto)
EXTENSIONS=(
com.iver.cit.gvsig.cad
com.iver.cit.gvsig.jdbc_spatial
com.iver.cit.gvsig.wcs
com.iver.cit.gvsig.wfs2
com.iver.cit.gvsig.wms
com.iver.gvsig.datalocator
com.iver.cit.gvsig.geoprocess
com.iver.cit.gvsig.geoprocessextensions
es.gva.cit.gvsig.catalogClient
org.gvsig.georeferencing
org.gvsig.rasterTools
org.gvsig.scripting
com.iver.gvsig.centerviewpoint
es.prodevelop.cit.gvsig.arcims
com.iver.gvsig.expresionfield
com.iver.cit.gvsig.annotation
org.gvsig.crs
com.iver.cit.gvsig.oracle_spatial
)

## estas extensiones se instalan pero no se muestran en los packs
MANDATORY_EXTENSIONS=(
com.iver.cit.gvsig
com.iver.core
com.iver.gvsig.addeventtheme
)

