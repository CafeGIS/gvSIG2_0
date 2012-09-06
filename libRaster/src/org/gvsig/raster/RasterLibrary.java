/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
 *
 * Copyright (C) 2006 IVER T.I. and Generalitat Valenciana.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
 */
package org.gvsig.raster;

import java.io.File;
import java.util.ArrayList;

import org.gvsig.raster.dataset.serializer.ColorInterpretationRmfSerializer;
import org.gvsig.raster.dataset.serializer.GeoInfoRmfSerializer;
import org.gvsig.raster.dataset.serializer.GeoPointListRmfSerializer;
import org.gvsig.raster.dataset.serializer.ProjectionRmfSerializer;
import org.gvsig.raster.dataset.serializer.StatisticsRmfSerializer;
import org.gvsig.raster.datastruct.serializer.ColorTableRmfSerializer;
import org.gvsig.raster.datastruct.serializer.HistogramRmfSerializer;
import org.gvsig.raster.datastruct.serializer.NoDataRmfSerializer;
import org.gvsig.raster.grid.filter.bands.ColorBalanceCMYManager;
import org.gvsig.raster.grid.filter.bands.ColorBalanceRGBManager;
import org.gvsig.raster.grid.filter.bands.ColorTableListManager;
import org.gvsig.raster.grid.filter.bands.HSLToRGBManager;
import org.gvsig.raster.grid.filter.bands.RGBToHSLManager;
import org.gvsig.raster.grid.filter.bands.ToLumSaManager;
import org.gvsig.raster.grid.filter.convolution.ConvolutionListManager;
import org.gvsig.raster.grid.filter.correction.MedianListManager;
import org.gvsig.raster.grid.filter.correction.ModeManager;
import org.gvsig.raster.grid.filter.enhancement.BrightnessContrastListManager;
import org.gvsig.raster.grid.filter.enhancement.EnhancementStretchListManager;
import org.gvsig.raster.grid.filter.enhancement.EqualizationManager;
import org.gvsig.raster.grid.filter.pansharp.PanSharpeningListManager;
import org.gvsig.raster.grid.filter.segmentation.FirstDerivativeListManager;
import org.gvsig.raster.grid.filter.statistics.StatisticsListManager;
import org.gvsig.raster.util.PropertyEvent;
import org.gvsig.raster.util.PropertyListener;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.extensionpoint.ExtensionPointManager;
/**
 * Clase principal de la libreria. En ella se definen variables globales con informaci�n
 * de uso general, as� como acciones a realizar al arracar la librer�a. El m�todo que
 * contiene las acciones de arranque es wakeUp. Las tareas principales de este m�todo
 * son de registro de drivers de lectura y escritura y eliminaci�n del directorio de
 * temporales.
 *
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class RasterLibrary {
	/**
	 * Control de librer�a ya inicializada.
	 */
	public static boolean       wakeup = false;
	public static ArrayList     writersClassList = new ArrayList();

	/**
	 * En la generaci�n autom�tica de clases esta variable representa el n�mero de
	 * clases en las que se hace la divisi�n.
	 */
	public static int          defaultNumberOfClasses = 64;

	/**
	 * En la genraci�n de las paletas de color, esta variable representa el n�mero
	 * de colores en las que se hace la divisi�n para generar la paleta nueva.
	 * Con esto conseguimos velocidad de procesamiento. Cuanto menor sea, peor
	 * ser� la calidad representada de la imagen.
	 */
	public static int          defaultNumberOfColors = 256;
	/**
	 * Tama�o de bloque en los procesos que recorren un raster completo a base de ventanas con recorrido
	 * descendente. Esta variable indica la altura de dicho bloque. Por lo tanto cada bloque ser� de
	 * raster.width X blockHeight. Tipicamente recorridos de este tipo se usan para el calculo de estad�sticas,
	 * histogramas, salvado a raster, etc... Es importante para el buen funcionamiento que este bloque sea
	 * potencia de dos.
	 */
	public static int          blockHeight = 512;

	//*************CACHE*******************
	/**
	 * Tama�o aproximado de cach� en Megas. Si este valor es alto cabr�n muchas p�ginas en memoria
	 * a la vez y si es bajo cabr�n pocas. Hay que tener en cuenta que al instanciar se convertira en bytes
	 * para su mejor tratamiento. Al llamar al constructor esta variable contendr� el tama�o exacto
	 * de la cache en bytes. El tama�o aqu� especificado es aproximado. Este variar� dependiendo de los
	 * par�metros del raster a cachear ya que las p�ginas deben tener una altura potencia de 2.
	 */
	public static long          cacheSize = 25;
	/**
	 * Tama�o m�ximo de la p�gina en Megas. Hay que tener en cuenta que al instanciar se convertira en bytes
	 * para su mejor tratamiento. Al llamar al constructor esta variable contendr� el tama�o exacto
	 * de la p�gina en bytes
	 */
	public static double        pageSize = 4;
	/**
	 * N�mero de p�ginas que tiene cada conjunto de cach�
	 */
	public static int           pagsPerGroup = 5;

	//*************PATHS*******************

	/**
	 * Directorio temporal para la cach�. Si gastamos el mismo que andami este se ocupar� de gestionar su
	 * destrucci�n al cerrar gvSIG.
	 */
	private static String tempCacheDirectoryPath = System
			.getProperty("java.io.tmpdir")
			+ File.separator + "tmp-andami";
	/**
	 * Ruta o rutas donde busca jars con clases que incorporen elementos nuevos que extiendan
	 * otros ya existentes. Estos pueden ser drivers o filtros.
	 */
	public static String[]       pathExtensions = {"." + File.separator};

	/**
	 * Valor noData por defecto para la librer�a. En caso de no tener un valor asociado
	 * al raster se usar� este.
	 */
	public static double         defaultNoDataValue = -99999;
	/**
	 * Contador global de las capas generadas para raster
	 */
	private static int           layerCount = 1;
	private static ArrayList     propetiesListeners = new ArrayList();

	public static final int NODATATYPE_DISABLED = 0;
	public static final int NODATATYPE_LAYER    = 1;
	public static final int NODATATYPE_USER     = 2;
	/**
	 * Ejecuta las acciones necesarias para arrancar la librer�a.
	 */
	public static void wakeUp() {
		if (wakeup)
			return;

		ExtensionPointManager extensionPoints = ToolsLocator.getExtensionPointManager();
		// Punto de extensi�n para registro de drivers de lectura
		extensionPoints.add("RasterReader", "Raster Reader Classes");
		// Punto de extensi�n para registro de drivers de escritura
		extensionPoints.add("RasterWriter", "Raster Writer Classes");
		// Punto de extensi�n de los filtros
		extensionPoints.add("RasterFilter", "Raster Filter Classes");
		// Punto de extensi�n de los serializadores
		extensionPoints.add("Serializer", "Raster Serializer Classes");
		// Registro de clases por defecto
		extensionPoints.add("DefaultDriver", "Default Raster Drivers");

		// Invoca las llamadas est�ticas de cada clase para registrarlas en los puntos de extensi�n
		BrightnessContrastListManager.register();
		FirstDerivativeListManager.register();
		MedianListManager.register();
		ConvolutionListManager.register();
		ColorTableListManager.register();
		StatisticsListManager.register();
		PanSharpeningListManager.register();
		RGBToHSLManager.register();
		HSLToRGBManager.register();
		ColorBalanceCMYManager.register();
		ColorBalanceRGBManager.register();
		ToLumSaManager.register();
		EnhancementStretchListManager.register();
		EqualizationManager.register();
		ModeManager.register();
		// EnhancementListManager.register();
		// RGBToCMYKManager.register();
		// Registrar los nuevos filtros del directorio
		// registerClasses();

		// Registro de serializadores
		ColorInterpretationRmfSerializer.register();
		GeoInfoRmfSerializer.register();
		GeoPointListRmfSerializer.register();
		ProjectionRmfSerializer.register();
		StatisticsRmfSerializer.register();
		ColorTableRmfSerializer.register();
		HistogramRmfSerializer.register();
		NoDataRmfSerializer.register();

		// Limpiamos el directorio temporal
		RasterLibrary.cleanUpTempFiles();
		// Crea el directorio de temporales
		getTemporalPath();
		wakeup = true;
	}

	/**
	 * Elimina los ficheros del directorio temporal. Realizamos esta acci�n al
	 * levantar la librer�a.
	 */
	public static void cleanUpTempFiles() {
		try {
			File tempDirectory = new File(tempCacheDirectoryPath);

			File[] files = tempDirectory.listFiles();
			if (files != null)
				for (int i = 0; i < files.length; i++) {
					// s�lo por si en un futuro se necesitan crear directorios temporales
					if (files[i].isDirectory())
						deleteDirectory(files[i]);
					files[i].delete();
				}
			tempDirectory.delete();
		} catch (Exception e) {
		}
	}

	/**
	 * Recursive directory delete.
	 * @param f
	 */
	private static void deleteDirectory(File f) {
		File[] files = f.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory())
				deleteDirectory(files[i]);
			files[i].delete();
		}
	}

	//******* Servicio de nombres de capas �nicos **************

	/**
	 * La gesti�n de nombres �nicos en la generaci�n de capas se lleva de forma
	 * autom�tica. Cuando alguien crea una capa nueva, si esta no tiene nombre especifico,
	 * obtiene su nombre mediante este m�todo. La siguiente vez que se llame dar� un nombre
	 * distinto. El nombre de la capa ser� NewLayer_ seguido de un contador de actualizaci�n
	 * autom�tica cada vez que se usa.
	 * @return Nombre �nico para la capa.
	 */
	public static String usesOnlyLayerName() {
		String oldValue = getOnlyLayerName();
		String newValue = "NewLayer_" + (++RasterLibrary.layerCount);
		for (int i = 0; i < propetiesListeners.size(); i++)
			if(propetiesListeners.get(i) instanceof PropertyListener)
				((PropertyListener)propetiesListeners.get(i)).actionValueChanged(new PropertyEvent(oldValue, "NewLayer", newValue, oldValue));
		return newValue;
	}

	/**
	 * Obtiene el nombre �nico de la siguiente capa sin actualizar el contador. Es
	 * solo para consulta. La siguiente vez que se llama a getOnlyLayerName o usesOnlyLayerName
	 * devolver� el mismo nomnbre.
	 * @return Nombre �nico para la capa.
	 */
	public static String getOnlyLayerName() {
		return "NewLayer_" + RasterLibrary.layerCount;
	}

	/**
	 * A�adir un listener a la lista de eventos
	 * @param listener
	 */
	public static void addOnlyLayerNameListener(PropertyListener listener) {
		if (!propetiesListeners.contains(listener))
			propetiesListeners.add(listener);
	}

	/**
	 * Elimina un listener de la lista de eventos
	 * @param listener
	 */
	public static void removeOnlyLayerNameListener(PropertyListener listener) {
		for (int i = 0; i < propetiesListeners.size(); i++)
			if(propetiesListeners.get(i) == listener)
				propetiesListeners.remove(i);
	}

	//******* End: Servicio de nombres de capas �nicos **************

	/**
	 * Esta funci�n crea el directorio para temporales y devuelve el manejador
	 * del directorio
	 * @return
	 */
	static public File getTemporalFile() {
		File tempDirectory = new File(tempCacheDirectoryPath);
		if (!tempDirectory.exists())
			tempDirectory.mkdir();
		return tempDirectory;
	}

	/**
	 * Esta funci�n crea el directorio para temporales y devuelve la ruta de este
	 * @return
	 */
	static public String getTemporalPath() {
		return getTemporalFile().getAbsolutePath();
	}

/*
	private static Hashtable     clasesJar = new Hashtable();

	 *
	 * Esta funci�n buscar� todos los jars en las rutas de pathExtensions y
	 * registrar� todos las clases registrables. En este momento hay posibilidad
	 * de registro de drivers y filtros.
	 *
	private static void registerClasses() throws Exception {
		RasterClassLoader loader = new RasterClassLoader();

		//Cargamos sobre jarList todos los File correspondientes a los jar contenidos en pathExtensions
		File[] jarList = null;
		for (int iPath = 0; iPath < pathExtensions.length; iPath++) {
			File directory = new File(pathExtensions[iPath]);
			if (directory.isDirectory() && directory.canRead()) {
				jarList = directory.listFiles(new FileFilter() {
												public boolean accept(File pathname) {
														return (pathname.getName().toUpperCase().endsWith(".JAR"));
												}
												});
			}
		}

		//Creamos las URL
		URL[] urls = new URL[jarList.length];

		for (int j = 0; j < jarList.length; j++) {
			try {
				urls[j] = new URL("file:" + jarList[j]);
			} catch (MalformedURLException e) {
				Logger.getLogger(RasterLibrary.class.getName()).debug("Error formando la URL, jar incorrecto", e);
			}
		}

		//Comprobamos que no haya clases repetidas
		ZipFile[] jarFiles = new ZipFile[jarList.length];
		for (int i = 0; i < jarList.length; i++) {
			try {
				jarFiles[i] = new ZipFile(jarList[i].getPath());

				Enumeration entradas = jarFiles[i].entries();

				while (entradas.hasMoreElements()) {
					ZipEntry file = (ZipEntry) entradas.nextElement();
					String fileName = file.getName();

					if (!fileName.toLowerCase().endsWith(".class"))
						continue;

					fileName = fileName.substring(0, fileName.length() - 6).replace('/', '.');

					if (clasesJar.get(fileName) != null) {
						throw new JarException("CLASES REPETIDAS: " + fileName + " " + " en " +
								jarFiles[i].getName() + " y en " + ((ZipFile) clasesJar.get(fileName)).getName());
					}

					clasesJar.put(fileName, jarFiles[i]);
				}
			} catch (ZipException e) {
				throw new IOException(" Jar: " + jarList[i].getPath() + ": " + jarFiles[i]);
			} catch (IOException e) {
				throw e;
			}
		}
	}
*/
}
