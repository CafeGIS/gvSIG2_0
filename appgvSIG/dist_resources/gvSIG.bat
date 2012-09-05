@echo off
rem set gvSIG_JRE_HOME=
rem set this_path=
set error=N
IF DEFINED gvSIG_JRE_HOME goto check_this_path
ECHO No se ha establecido la variable 'gvSIG_JRE_HOME'
ECHO . 
set error=S
:check_this_path
IF DEFINED this_path goto check_errors
ECHO No se ha establecido la variable 'this_path'
set error=S
:check_errors
IF "%error%" == "N" goto run
Echo . 
ECHO Para que funcione este arranque de gvSIG debe editar este fichero
ECHO y modificar las siguentes lineas:
ECHO . 
ECHO rem set gvSIG_JRE_HOME=
ECHO rem set this_path=
ECHO . 
ECHO Dejandolas asi:
ECHO . 
ECHO set gvSIG_JRE_HOME={ruta a la jre preparada para gvsig}
ECHO set this_path={ruta en la que se encuentra este archivo}
ECHO .  
ECHO Ejecucion cancelada
pause
goto end

:run
cd "%this_path%"
"%gvSIG_JRE_HOME%\bin\java.exe" -cp ##Andami_classpath## -Xmx500M com.iver.andami.Launcher gvSIG gvSIG/extensiones %1

:end