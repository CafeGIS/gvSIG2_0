#!/bin/sh
# This is used at installation time, in order to perform some extra actions
# that the installer is not able to do

chmod +x "$INSTALL_PATH"/bin/gvSIG.sh
chmod +x "$INSTALL_PATH"/Uninstaller/uninstall.sh
[ -a "$USER_HOME"/gvSIG ] || mkdir -p "$USER_HOME"/gvSIG
[ -a "$USER_HOME"/gvSIG/andami-config.xml ] || cp "$INSTALL_PATH"/bin/andami-config.xml "$USER_HOME"/gvSIG/andami-config.xml
