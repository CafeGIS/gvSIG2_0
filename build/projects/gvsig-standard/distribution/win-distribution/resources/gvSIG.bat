@echo off
cd "$INSTALL_PATH\"

IF OS_%os% == OS_Windows_NT GOTO wnt
GOTO win

:wnt
SET PATH=$INSTALL_PATH\lib;%PATH%
cd bin
"$JAVA_HOME\bin\java.exe" -Djava.library.path="$INSTALL_PATH\lib" -cp andami.jar;./lib/gvsig-i18n.jar;./lib/beans.jar;./lib/log4j-1.2.8.jar;./lib/iver-utiles.jar;./lib/castor-0.9.5.3-xml.jar;./lib/xerces_2_5_0.jar;./lib/javaws.jar;./lib/xml-apis.jar;lib/looks-2.0.2.jar;./lib/JWizardComponent.jar;./lib/kxml2.jar;./lib/jcalendar.jar -Xmx500M com.iver.andami.Launcher gvSIG gvSIG/extensiones %1
GOTO end

:win
SET PATH="$INSTALL_PATH\lib";%PATH%
cd bin
"$JAVA_HOME\bin\java.exe" -Djava.library.path="$INSTALL_PATH\lib" -cp andami.jar;./lib/gvsig-i18n.jar;./lib/beans.jar;./lib/log4j-1.2.8.jar;./lib/iver-utiles.jar;./lib/castor-0.9.5.3-xml.jar;./lib/xerces_2_5_0.jar;./lib/javaws.jar;./lib/xml-apis.jar;lib/looks-2.0.2.jar;./lib/JWizardComponent.jar;./lib/kxml2.jar;./lib/jcalendar.jar -Xmx500M com.iver.andami.Launcher gvSIG gvSIG/extensiones %1

:end
pause