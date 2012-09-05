#!/bin/sh
export INSTALL_PATH="$PWD"

if [ -n "$1"]
then
	cd "$INSTALL_PATH/libs"
else
    cd "$1"
fi

ln -sf libosgText.so.2.2.0 libosgText.so.25
ln -sf libosgManipulator.so.2.2.0 libosgManipulator.so.25
ln -sf libOpenThreads.so.9 libOpenThreads.so
ln -sf libosgDB.so.2.2.0 libosgDB.so.25
ln -sf libosgTerrain.so.2.2.0 libosgTerrain.so.25
ln -sf libosgShadow.so.25 libosgShadow.so
ln -sf libosgGA.so.2.2.0 libosgGA.so.25
ln -sf libosgFX.so.2.2.0 libosgFX.so.25
ln -sf libosgParticle.so.2.2.0 libosgParticle.so.25
ln -sf libosg.so.25 libosg.so
ln -sf libosgUtil.so.25 libosgUtil.so
ln -sf libosgViewer.so.2.2.0 libosgViewer.so.25
ln -sf libosgDB.so.25 libosgDB.so
ln -sf libosgSim.so.25 libosgSim.so
ln -sf libosgSim.so.2.2.0 libosgSim.so.25
ln -sf libosgFX.so.25 libosgFX.so
ln -sf libOpenThreads.so.2.2.0 libOpenThreads.so.9
ln -sf libosgParticle.so.25 libosgParticle.so
ln -sf libosgTerrain.so.25 libosgTerrain.so
ln -sf libosgGA.so.25 libosgGA.so
ln -sf libosgManipulator.so.25 libosgManipulator.so
ln -sf libosgUtil.so.2.2.0 libosgUtil.so.25
ln -sf libosgShadow.so.2.2.0 libosgShadow.so.25
ln -sf libosgText.so.25 libosgText.so
ln -sf libosgViewer.so.25 libosgViewer.so
ln -sf libosg.so.2.2.0 libosg.so.25
