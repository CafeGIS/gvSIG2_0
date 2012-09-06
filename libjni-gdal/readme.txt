Notas de compilación de la librería jgdal.

La compilación de la librería jgdal se realiza automáticamente ejecutando el build.xml
del proyecto. Para ello el sistema tiene que tener instalado y accesible:

- CMake - Probado con la versión 2.4
- Compilador gcc - g++ en el caso de Linux - Compilado con gcc 4.1. Con la versión 3.3
	de gcc - g++ no compila correctamente.
- Entorno de desarrollo Visual Studio en caso de Windows con las variables de
	entorno cargadas. - Compilado con Visual Studio 2005
- Librería gdal instalada. Hasta el momento (0.6.3) se está compilando con gdal 1.5.0 con 
	soporte para Grass y Modis (Hdf 4 y 5). Si el SDK está en alguna ruta distinta a las que
	se incluyen en el archivo FindGDAL, habrá que añadirla a dicho archivo o instalar el SDK
	en alguna de las indicadas.
- JDK de Java.

Con ejecutar el build.xml es suficiente para generar la librería y el jar.

*********************************************************
Cambios según versiones a partir de la 0.6.1

0.6.1: Eliminación de la comprobación de direcciones de memoria negaticas ya que
		éstas se utilizan.

0.6.2: Versión en desarrollo. Migración a CMake.

0.6.3: No se utiliza la captura de señales de las librerías nativas ya que produce 
		inestabilidades en la librería. Soporte para grass y HDF 4 y 5. Grass solo en
		Linux.
			
0.6.4: Se incluyen comprobaciones de los parámetros que se pasan a JNI 
		y lanzamiento de excepciones desde la parte de java. 
		
0.7.0: Recubrimiento de la utilidad para reproyeccion gdalwarp.
		Incluido el recubrimiento del generador de overwiews gdaladdo.
		Método getMetadata con parámetro para poder seleccionar dominios de metadatos.
		Recubrimiento de la funcion GDALSetRaserColorInterpretation.
		
0.7.1: Resolución de un bug en el método createCopy de la clase GdalDriver.

0.7.2: Resolución de bug en GdalDriver. El params de create debe admitir el valor de null

0.8.0: Llamada existsNoDataValue de GdalRasterBand
		
0.9.0: Quitadas las constantes de GdalWarp
		Quitado metodo setFormat de GdalWarp
		Añadido nuevo parametro al constructor de GdalWarp
		Añadido nuevo metodo a GdalWarp que devuelve los drivers reproyectables.

0.9.1: Agregado el control de excepciones a GdalWarp
		Corregido bug cuando params vale null en GdalDriver.java

*********************************************************

NOTAS DE COMPILACIÓN:

Linux:
* Es necesario que haya instalado un gdal. Según la versión de la librería, puede requerir
que gdal esté compilado con diferentes soportes.



Windows:
* Es necesario que haya un Visual Studio instalado con sus variables de entorno cargadas por
defecto en el sistema para poder acceder al copilador por linea de comandos.
* Es necesario que haya un CMake en el sistema con sus variable de entorno cargadas en el
sistema para generar los Makefiles.
* Es necesario que la variable JAVA_HOME exista y apunte a un Jdk.
* Es necesario indicar la variable de entorno JAVA_HOME al ejecutar el ant desde eclipse 
para que se puedan encontrar los includes JNI. La dirección de estos includes se guardará
en las variables JAVA_INCLUDE_PATH y JAVA_INCLUDE_PATH2.
* En el archivo de configuración para la compilación de la librería gdal, es necesario
que se utilice la opción STDCALL. También se tiene que deshabilitar el soporte para ODBC.
Comentar la linea ODBC_SUPPORTED=YES. Importante, comentar la linea, no poner valor =NO.
* En el caso de que se haya compilado la librería con soporte externo (tal como HDF4, HDF5...)
es necesario que las librerías de las que depende gdal estén accesibles.

* SOPORTE PARA HDF4:
	- La librería 1.5.0 de gdal se tiene que compilar con el sdk 4.2r2 de HDF4. La versión
	4.2r1 está compilada con Visual Studio 6.0 y no soporta VS 2005. La versión 4.2r3 da un
	error en la compilación de gdal posiblemente porque este último no esté actualizado.

* SOPORTE PARA HDF5:
	- Al igual que ocurre con HDF4, gdal 1.5.0 no soporta la última versión hasta la fecha
	de HDF5 (1.8.0) por lo que hay que utilizar la (1.6.7) que si que soporta Visual Studio
	2005. También es conveniente utilizar una version de szip compatible con Visual Studio
	2005.
 
		