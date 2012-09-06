/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
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
package org.gvsig.raster.dataset;

import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.cresques.cts.IProjection;
import org.gvsig.raster.dataset.properties.DatasetColorInterpretation;
import org.gvsig.raster.dataset.serializer.RmfSerializerException;
import org.gvsig.raster.util.RasterUtilities;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.extensionpoint.ExtensionPoint;
import org.gvsig.tools.extensionpoint.ExtensionPointManager;

import es.gva.cit.jgdal.GdalException;
import es.gva.cit.jgdal.GdalWarp;

/**
 * Clase abstracta de la que heredan los drivers de escritura. Tiene los métodos
 * abstractos que debe implementar cualquier driver de escritura y las
 * funcionalidades y opciones soportadas comunes a todos ellos.
 *
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public abstract class GeoRasterWriter {
	public static final int   MODE_FILEWRITE = 1;
	public static final int   MODE_DATAWRITE = 2;

	public static TreeMap     fileFeature    = new TreeMap();
	protected String          outFileName    = null;
	protected String          inFileName     = null;
	protected int             sizeWindowX    = 0;
	protected int             sizeWindowY    = 0;
	protected int             ulX            = 0;
	protected int             ulY            = 0;
	protected IDataWriter     dataWriter     = null;
	protected int             nBands         = 0;
	protected String          ident          = null;
	protected String          driver         = null;
	protected Params          driverParams   = null;
	protected AffineTransform at             = null;
	protected int             percent        = 0;
	protected int             dataType       = IBuffer.TYPE_BYTE;
	protected IProjection     proj           = null;
	protected DatasetColorInterpretation colorInterp = null;

	/**
	 * Obtiene la lista de extensiones registradas
	 * @return Lista de extensiones registradas o null si no hay ninguna
	 */
	public static String[] getDriversExtensions() {
		ExtensionPointManager extensionPoints = ToolsLocator.getExtensionPointManager();
		ExtensionPoint point = extensionPoints.get("RasterWriter");
		return (String[])point.getNames().toArray(new String[0]);
	}

	/**
	 * Obtiene la lista de extensiones de ficheros sobre los que se puede salvar
	 * en un determinado tipo de datos. Como parámetro de la función se especifica
	 * el tipo de datos sobre el que se desea consultar. Este método consulta para
	 * cada driver registrado que extensiones soportan un tipo.
	 *
	 * @param dataType Tipo de datos
	 * @param bands Numero de bandas
	 * @param reprojectable Especifica si devuelve solo los formatos reproyectables
	 * @return Lista de extensiones registradas que soportan el tipo de datos
	 *         pasado por parámetro.
	 * @throws RasterDriverException
	 */
	public static ArrayList getExtensionsSupported(int dataType, int bands, boolean reprojectable) throws RasterDriverException {
		ExtensionPointManager extensionPoints = ToolsLocator.getExtensionPointManager();
		ExtensionPoint point = extensionPoints.get("RasterWriter");
		Iterator iterator = point.iterator();
		ArrayList result = new ArrayList();
		while (iterator.hasNext()) {
			ExtensionPoint.Extension extension = (ExtensionPoint.Extension) iterator
					.next();
			String ext = extension.getName();
			Class writerClass = extension.getExtension();
			Class[] args = { String.class };
			try {
				Constructor hazNuevo = writerClass.getConstructor(args);
				Object[] args2 = { ext };
				GeoRasterWriter grw = (GeoRasterWriter) hazNuevo.newInstance(args2);
				if (grw.isSupportedThisExtension(ext, dataType, bands))
					if (reprojectable) {
						if (GdalWarp.getDrivers().contains(grw.getDriverName()))
							result.add(ext);
					} else
						result.add(ext);
			} catch (SecurityException e) {
				throw new RasterDriverException("Error SecurityException in open");
			} catch (NoSuchMethodException e) {
				throw new RasterDriverException("Error NoSuchMethodException in open");
			} catch (IllegalArgumentException e) {
				throw new RasterDriverException("Error IllegalArgumentException in open");
			} catch (InstantiationException e) {
				throw new RasterDriverException("Error InstantiationException in open");
			} catch (IllegalAccessException e) {
				throw new RasterDriverException("Error IllegalAccessException in open");
			} catch (InvocationTargetException e) {
				throw new RasterDriverException("Error in open");
			}
		}
		return result;
	}

	/**
	 * Obtiene la lista de tipos de driver
	 * @return Lista de tipos de driver registradas o null si no hay ninguno
	 */
	public static String[] getDriversType() {
		if (fileFeature.size() == 0)
			return null;
		String[] list = new String[fileFeature.size()];
		Set values = fileFeature.entrySet();
		int i = 0;
		for (Iterator it = values.iterator(); it.hasNext();) {
			list[i] = ((WriteFileFormatFeatures) ((Map.Entry) it.next()).getValue()).getDriverName();
			i++;
		}

		return list;
	}

	/**
	 * Obtiene el tipo de driver a partir de la extensión
	 * @param ext Extensión
	 * @return Tipo
	 */
	public static String getDriverType(String ext) {
		return ((WriteFileFormatFeatures) fileFeature.get(ext)).getDriverName();
	}

	/**
	 * Devuelve el número de drivers soportados
	 * @return Número de drivers soportados
	 */
	public static int getNDrivers() {
		ExtensionPointManager extensionPoints = ToolsLocator.getExtensionPointManager();
		ExtensionPoint point = extensionPoints.get("RasterWriter");
		return point.getCount();
	}

	/**
	 * Devuelve el número de tipos de driver registrados
	 * @return Número de tipos de driver soportados
	 */
	public static int getNTypes() {
		return fileFeature.size();
	}

	/**
	 * Devuelve el identificador del driver
	 * @return Identificador del driver
	 */
	public String getIdent() {
		return ident;
	}

	/**
	 * Obtiene el nombre del driver.
	 * @return Nombre del driver
	 */
	public String getDriverName() {
		return driver;
	}

	/**
	 * @return
	 */
	public String getDriverType() {
		return driver;
	}

	/**
	 * Asigna el porcentaje de incremento. Esto es usado por el driver para
	 * actualizar la variable percent
	 * @param percent
	 */
	public void setPercent(int percent) {
		this.percent = percent;
	}

	/**
	 * Porcentaje de escritura completado.
	 * @return Entero con el porcentaje de escritura.
	 */
	public int getPercent() {
		return percent;
	}

	/**
	 * Factoria para obtener escritores de los distintos tipos de raster.
	 * @param fName Nombre del fichero.
	 * @return GeoRasterWriter, o null si hay problemas.
	 */
	public static GeoRasterWriter getWriter(String fName) throws NotSupportedExtensionException, RasterDriverException {
		String ext = RasterUtilities.getExtensionFromFileName(fName);
		GeoRasterWriter grw = null;
		ExtensionPointManager extensionPoints = ToolsLocator.getExtensionPointManager();
		ExtensionPoint point = extensionPoints.get("RasterWriter");
//		ExtensionPoint extensionPoint = ExtensionPoint.getExtensionPoint("RasterWriter");

		if (!point.has(ext))
			return grw;

		Class clase = point.get(ext).getExtension();
		Class[] args = { String.class };
		try {
			Constructor hazNuevo = clase.getConstructor(args);
			Object[] args2 = { fName };
			grw = (GeoRasterWriter) hazNuevo.newInstance(args2);
		} catch (SecurityException e) {
			throw new RasterDriverException("Error SecurityException in open");
		} catch (NoSuchMethodException e) {
			throw new RasterDriverException("Error NoSuchMethodException in open");
		} catch (IllegalArgumentException e) {
			throw new RasterDriverException("Error IllegalArgumentException in open");
		} catch (InstantiationException e) {
			throw new RasterDriverException("Error InstantiationException in open");
		} catch (IllegalAccessException e) {
			throw new RasterDriverException("Error IllegalAccessException in open");
		} catch (InvocationTargetException e) {
			throw new NotSupportedExtensionException("Error in open");
		}
		return grw;
	}

	/**
	 * Factoria para obtener escritores de los distintos tipos de raster.
	 *
	 * @param fName Nombre del fichero.
	 * @return GeoRasterWriter, o null si hay problemas.
	 */
	public static GeoRasterWriter getWriter(IDataWriter dataWriter,
												 String outFileName,
												 int nBands,
												 AffineTransform at,
												 int outSizeX,
												 int outSizeY,
												 int dataType,
												 Params params,
												 IProjection proj) throws NotSupportedExtensionException, RasterDriverException {
		return GeoRasterWriter.getWriter(dataWriter, outFileName, nBands, at, outSizeX, outSizeY, dataType, params, proj, true);
	}

	/**
	 * Factoria para obtener escritores de los distintos tipos de raster.
	 *
	 * @param fName Nombre del fichero.
	 * @return GeoRasterWriter, o null si hay problemas.
	 */
	public static GeoRasterWriter getWriter(IDataWriter dataWriter,
												 String outFileName,
												 int nBands,
												 AffineTransform at,
												 int outSizeX,
												 int outSizeY,
												 int dataType,
												 Params params,
												 IProjection proj,
												 boolean geo) throws NotSupportedExtensionException, RasterDriverException {
		String ext = outFileName.toLowerCase().substring(outFileName.lastIndexOf('.') + 1);
		GeoRasterWriter grw = null;
		ExtensionPointManager extensionPoints =ToolsLocator.getExtensionPointManager();
		ExtensionPoint point=extensionPoints.get("RasterWriter");
//		ExtensionPoint extensionPoint = ExtensionPoint.getExtensionPoint("RasterWriter");

		if (!point.has(ext))
			return grw;

		Class clase = point.get(ext).getExtension();
		Class[] args = { IDataWriter.class, String.class, Integer.class, AffineTransform.class, Integer.class, Integer.class, Integer.class, Params.class, IProjection.class, Boolean.class };
		try {
			Constructor hazNuevo = clase.getConstructor(args);
			Object [] args2 = {dataWriter, outFileName, new Integer(nBands), at,
								new Integer(outSizeX), new Integer(outSizeY), new Integer(dataType),
								params, proj, new Boolean(geo)};
			grw = (GeoRasterWriter) hazNuevo.newInstance(args2);
		} catch (SecurityException e) {
			throw new RasterDriverException("Error SecurityException in open");
		} catch (NoSuchMethodException e) {
			throw new RasterDriverException("Error NoSuchMethodException in open");
		} catch (IllegalArgumentException e) {
			throw new RasterDriverException("Error IllegalArgumentException in open");
		} catch (InstantiationException e) {
			throw new RasterDriverException("Error InstantiationException in open");
		} catch (IllegalAccessException e) {
			throw new RasterDriverException("Error IllegalAccessException in open");
		} catch (InvocationTargetException e) {
			throw new NotSupportedExtensionException("Error in open. Problemas con las librerías nativas.");
		}
		return grw;
	}

	/**
	 * Obtiene los parámetros del driver.
	 * @return WriterParams
	 */
	public Params getParams() {
		return driverParams;
	}

	/**
	 * Asigna los parámetros del driver modificados por el cliente.
	 * @param Params
	 */
	public void setParams(Params params) {
		this.driverParams = params;
	}

	/**
	 * Realiza la función de compresión a partir de un GeoRasterFile.
	 * @throws IOException
	 */
	public abstract void fileWrite() throws IOException, InterruptedException;

	/**
	 * Realiza la función de compresión a partir de los datos pasados por el
	 * cliente.
	 * @throws IOException
	 * @throws RmfSerializerException
	 */
	public abstract void dataWrite() throws IOException, InterruptedException;

	/**
	 * Cierra el driver
	 */
	public abstract void writeClose();

	/**
	 * Cancela el grabado de datos
	 */
	public abstract void writeCancel();

	/**
	 * Añade la proyección Wkt con la que salvar.
	 * @param wkt
	 * @throws GdalException
	 */
	public abstract void setWkt(String wkt);

	/**
	 * Asigna la interpretación de color para el fichero de salida.
	 * @param colorInterp Interpretación de color
	 */
	public void setColorBandsInterpretation(String[] colorInterp) {
		if (colorInterp != null) {
			this.colorInterp = new DatasetColorInterpretation();
			this.colorInterp.initColorInterpretation(colorInterp.length);
			for (int i = 0; i < colorInterp.length; i++)
				this.colorInterp.setColorInterpValue(i, colorInterp[i]);
		}
	}

	/**
	 * Método que pregunta si la extensión pasada por parámetro está soportada con
	 * el tipo y número de bandas indicadas.
	 *
	 * @param dataType Tipo de dato
	 * @param bands Número de bandas
	 * @param extensión
	 * @return true si está soportada y false si no lo está
	 */
	public boolean isSupportedThisExtension(String ext, int dataType, int bands) {
		WriteFileFormatFeatures features = (WriteFileFormatFeatures) fileFeature.get(ext);
		if (features == null)
			return false;
		int[] bandsSupported = features.getNBandsSupported();
		for (int i = 0; i < bandsSupported.length; i++) {
			if (bandsSupported[i] == -1)
				break;
			if (bandsSupported[i] >= bands)
				break;
			return false;
		}
		int[] dt = features.getDataTypesSupported();
		for (int i = 0; i < dt.length; i++)
			if (dataType == dt[i])
				return true;
		return false;
	}
}