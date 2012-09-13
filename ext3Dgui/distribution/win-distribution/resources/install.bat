REM This is used at installation time, in order to perform some extra actions
REM that the installer is not able to do

REM different syntax in win98 and (nt, xp)

SET > salida.txt
SET
IF OS_%os% == OS_Windows_NT GOTO wnt
GOTO win

:wnt
IF NOT EXIST $USER_HOME\gvSIG MKDIR $USER_HOME\gvSIG
IF NOT EXIST $USER_HOME\gvSIG\andami-config.xml COPY $INSTALL_PATH\bin\andami-config.xml $USER_HOME\gvSIG\andami-config.xml

:win
IF NOT EXIST "$USER_HOME"\gvSIG MKDIR "$USER_HOME"\gvSIG
IF NOT EXIST "$USER_HOME"\gvSIG\andami-config.xml COPY "$INSTALL_PATH"\bin\andami-config.xml "$USER_HOME"\gvSIG\andami-config.xml


