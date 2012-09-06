Notas de compilaci�n de la librer�a jgdal.

La compilaci�n de la librer�a jgdal se realiza autom�ticamente ejecutando el build.xml
del proyecto. Para ello el sistema tiene que tener instalado y accesible:

- CMake - Probado con la versi�n 2.4
- Compilador gcc - g++ en el caso de Linux - Compilado con gcc 4.1. Con la versi�n 3.3
	de gcc - g++ no compila correctamente.
- Entorno de desarrollo Visual Studio en caso de Windows con las variables de
	entorno cargadas. - Compilado con Visual Studio 2005
- Librer�a gdal instalada. Hasta el momento (0.6.3) se est� compilando con gdal 1.5.0 con 
	soporte para Grass y Modis (Hdf 4 y 5). Si el SDK est� en alguna ruta distinta a las que
	se incluyen en el archivo FindGDAL, habr� que a�adirla a dicho archivo o instalar el SDK
	en alguna de las indicadas.
- JDK de Java.

Con ejecutar el build.xml es suficiente para generar la librer�a y el jar.

*********************************************************
Cambios seg�n versiones a partir de la 0.6.1

0.6.1: Eliminaci�n de la comprobaci�n de direcciones de memoria negaticas ya que
		�stas se utilizan.

0.6.2: Versi�n en desarrollo. Migraci�n a CMake.

0.6.3: No se utiliza la captura de se�ales de las librer�as nativas ya que produce 
		inestabilidades en la librer�a. Soporte para grass y HDF 4 y 5. Grass solo en
		Linux.
			
0.6.4: Se incluyen comprobaciones de los par�metros que se pasan a JNI 
		y lanzamiento de excepciones desde la parte de java. 
		
0.7.0: Recubrimiento de la utilidad para reproyeccion gdalwarp.
		Incluido el recubrimiento del generador de overwiews gdaladdo.
		M�todo getMetadata con par�metro para poder seleccionar dominios de metadatos.
		Recubrimiento de la funcion GDALSetRaserColorInterpretation.
		
0.7.1: Resoluci�n de un bug en el m�todo createCopy de la clase GdalDriver.

0.7.2: Resoluci�n de bug en GdalDriver. El params de create debe admitir el valor de null

0.8.0: Llamada existsNoDataValue de GdalRasterBand
		
0.9.0: Quitadas las constantes de GdalWarp
		Quitado metodo setFormat de GdalWarp
		A�adido nuevo parametro al constructor de GdalWarp
		A�adido nuevo metodo a GdalWarp que devuelve los drivers reproyectables.

0.9.1: Agregado el control de excepciones a GdalWarp
		Corregido bug cuando params vale null en GdalDriver.java

*********************************************************

NOTAS DE COMPILACI�N:

Linux:
* Es necesario que haya instalado un gdal. Seg�n la versi�n de la librer�a, puede requerir
que gdal est� compilado con diferentes soportes.



Windows:
* Es necesario que haya un Visual Studio instalado con sus variables de entorno cargadas por
defecto en el sistema para poder acceder al copilador por linea de comandos.
* Es necesario que haya un CMake en el sistema con sus variable de entorno cargadas en el
sistema para generar los Makefiles.
* Es necesario que la variable JAVA_HOME exista y apunte a un Jdk.
* Es necesario indicar la variable de entorno JAVA_HOME al ejecutar el ant desde eclipse 
para que se puedan encontrar los includes JNI. La direcci�n de estos includes se guardar�
en las variables JAVA_INCLUDE_PATH y JAVA_INCLUDE_PATH2.
* En el archivo de configuraci�n para la compilaci�n de la librer�a gdal, es necesario
que se utilice la opci�n STDCALL. Tambi�n se tiene que deshabilitar el soporte para ODBC.
Comentar la linea ODBC_SUPPORTED=YES. Importante, comentar la linea, no poner valor =NO.
* En el caso de que se haya compilado la librer�a con soporte externo (tal como HDF4, HDF5...)
es necesario que las librer�as de las que depende gdal est�n accesibles.

* SOPORTE PARA HDF4:
	- La librer�a 1.5.0 de gdal se tiene que compilar con el sdk 4.2r2 de HDF4. La versi�n
	4.2r1 est� compilada con Visual Studio 6.0 y no soporta VS 2005. La versi�n 4.2r3 da un
	error en la compilaci�n de gdal posiblemente porque este �ltimo no est� actualizado.

* SOPORTE PARA HDF5:
	- Al igual que ocurre con HDF4, gdal 1.5.0 no soporta la �ltima versi�n hasta la fecha
	de HDF5 (1.8.0) por lo que hay que utilizar la (1.6.7) que si que soporta Visual Studio
	2005. Tambi�n es conveniente utilizar una version de szip compatible con Visual Studio
	2005.
 
		