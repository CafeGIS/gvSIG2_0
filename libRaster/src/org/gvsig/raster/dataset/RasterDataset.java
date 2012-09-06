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
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

import org.cresques.cts.ICoordTrans;
import org.cresques.cts.IProjection;
import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.dataset.properties.DatasetColorInterpretation;
import org.gvsig.raster.dataset.properties.DatasetHistogram;
import org.gvsig.raster.dataset.properties.DatasetMetadata;
import org.gvsig.raster.dataset.properties.DatasetStatistics;
import org.gvsig.raster.dataset.rmf.ClassSerializer;
import org.gvsig.raster.dataset.rmf.ParsingException;
import org.gvsig.raster.dataset.rmf.RmfBlocksManager;
import org.gvsig.raster.dataset.serializer.ColorInterpretationRmfSerializer;
import org.gvsig.raster.dataset.serializer.GeoInfoRmfSerializer;
import org.gvsig.raster.dataset.serializer.ProjectionRmfSerializer;
import org.gvsig.raster.dataset.serializer.RmfSerializerException;
import org.gvsig.raster.datastruct.ColorTable;
import org.gvsig.raster.datastruct.Extent;
import org.gvsig.raster.datastruct.NoData;
import org.gvsig.raster.datastruct.Transparency;
import org.gvsig.raster.datastruct.serializer.ColorTableRmfSerializer;
import org.gvsig.raster.datastruct.serializer.NoDataRmfSerializer;
import org.gvsig.raster.projection.CRS;
import org.gvsig.raster.util.RasterUtilities;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.extensionpoint.ExtensionPoint;
import org.gvsig.tools.extensionpoint.ExtensionPointManager;
import org.gvsig.tools.extensionpoint.ExtensionPoint.Extension;

/**
 * Manejador de ficheros raster georeferenciados.
 *
 * Esta clase abstracta es el ancestro de todas las clases que proporcionan
 * soporte para ficheros raster georeferenciados.<br>
 * Actua tambien como una 'Fabrica', ocultando al cliente la manera en que
 * se ha implementado ese manejo. Una clase nueva que soportara un nuevo
 * tipo de raster tendría que registrar su extensión o extensiones usando
 * el método @see registerExtension.<br>
 */
public abstract class RasterDataset extends GeoInfo {
	/**
	 * Flags que representan a las bandas visualizables
	 */
	public static final int              RED_BAND            = 0x01;
	public static final int              GREEN_BAND          = 0x02;
	public static final int              BLUE_BAND           = 0x04;
	public static final int              ALPHA_BAND          = 0x08;

	/**
	 * Número de bandas de la imagen
	 */
	protected int                        bandCount           = 1;
	private int[]                        dataType            = null;

	protected DatasetStatistics          stats               = new DatasetStatistics(this);
	protected DatasetHistogram           histogram           = null;
	protected Object                     param               = null;
	protected RmfBlocksManager           rmfBlocksManager    = null;

	protected ColorTable                 colorTable          = null;
	protected DatasetColorInterpretation colorInterpretation = null;

	/**
	 * Valor del NoData
	 */
	protected double                     noData              = 0;

	protected String                     wktProjection       = "";

	/**
	 * Indica si el valor NoData esta activo
	 */
	protected boolean                    noDataEnabled       = false;

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone() {
		try {
			RasterDataset dataset = RasterDataset.open(proj, param);
			// Estas van por referencia
			dataset.histogram = histogram;
			dataset.stats = stats;
			return dataset;
		} catch (NotSupportedExtensionException e) {
			e.printStackTrace();
		} catch (RasterDriverException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Constructor
	 */
	public RasterDataset() {
	}

	/**
	 * Factoria para abrir distintos tipos de raster.
	 *
	 * @param proj Proyección en la que está el raster.
	 * @param fName Nombre del fichero.
	 * @return GeoRasterFile, o null si hay problemas.
	 */
	public static RasterDataset open(IProjection proj, Object param) throws NotSupportedExtensionException, RasterDriverException {
		String idFormat = null;

		if (param instanceof String)
			idFormat = RasterUtilities.getExtensionFromFileName(((String) param));
		if (param instanceof IRegistrableRasterFormat)
			idFormat = ((IRegistrableRasterFormat) param).getFormatID();

		RasterDataset grf = null;

		Class clase = null;
		ExtensionPointManager extensionPoints = ToolsLocator.getExtensionPointManager();
		if (param instanceof IRegistrableRasterFormat)
			clase = ((IRegistrableRasterFormat) param).getClass();
		else if (idFormat != null) {
			ExtensionPoint point = extensionPoints.get("RasterReader");
			Extension ext = point.get(idFormat);
			if (ext != null)
				clase = ext.getExtension();
		}

		ExtensionPoint pointDefaultReader = extensionPoints.get("DefaultDriver");
		Extension extReader = pointDefaultReader.get("reader");
		if (clase == null)
			clase = extReader.getExtension();

		Class [] args = {IProjection.class, Object.class};
		try {
			Constructor hazNuevo = clase.getConstructor(args);
			Object [] args2 = {proj, param};
			grf = (RasterDataset) hazNuevo.newInstance(args2);
		} catch (SecurityException e) {
			throw new RasterDriverException("Error SecurityException in open", e);
		} catch (NoSuchMethodException e) {
			throw new RasterDriverException("Error NoSuchMethodException in open", e);
		} catch (IllegalArgumentException e) {
			throw new RasterDriverException("Error IllegalArgumentException in open", e);
		} catch (InstantiationException e) {
			throw new RasterDriverException("Error InstantiationException in open", e);
		} catch (IllegalAccessException e) {
			throw new RasterDriverException("Error IllegalAccessException in open", e);
		} catch (InvocationTargetException e) {
			throw new NotSupportedExtensionException("Error opening this image with " + clase, e);
		}
		return grf;
	}

	/**
	 * Acciones de inicilización comunes a todos los drivers.
	 * Este método debe ser llamado explicitamente por el constructor de cada driver.
	 * Estas son acciones de inicialización que se ejecutan después del constructor de cada driver.
	 * Las acciones que hayan de ser realizadas antes se definen en el constructor de RasterDataset.
	 */
	protected void init() {
	}

	/**
	 * Tipo de fichero soportado.
	 * Devuelve true si el tipo de fichero (extension) está soportado, si no
	 * devuelve false.
	 *
	 * @param fName Fichero raster
	 * @return  true si está soportado, si no false.
		*/
	public static boolean fileIsSupported(String fName) {
		ExtensionPointManager extensionPoints = ToolsLocator.getExtensionPointManager();
		ExtensionPoint point = extensionPoints.get("RasterReader");
//		ExtensionPoint extensionPoint = ExtensionPoint.getExtensionPoint("RasterReader");
		return point.has(RasterUtilities.getExtensionFromFileName(fName));
	}

	/**
	 * Obtiene la lista de extensiones de ficheros soportadas
	 * @return Lista de extensiones registradas
	 */
	public static String[] getExtensionsSupported() {
		ExtensionPointManager extensionPoints = ToolsLocator.getExtensionPointManager();
		ExtensionPoint point = extensionPoints.get("RasterReader");
		return (String[])point.getNames().toArray(new String[0]);
	}

	/**
	 * Constructor
	 * @param proj	Proyección
	 * @param name	Nombre del fichero de imagen.
	 */
	public RasterDataset(IProjection proj, Object param) {
		super(proj, param);
		if(param instanceof String)
			setFileSize(new File(getFName()).length());
	}


	/**
	 * Carga un fichero raster. Puede usarse para calcular el extent e instanciar
	 * un objeto de este tipo.
	 */
	abstract public GeoInfo load();

	/**
	 * Cierra el fichero y libera los recursos.
	 */
	abstract public void close();

	/**
	 * Carga metadatos desde el fichero Rmf si estos existen
	 * @param fName Nombre del fichero
	 * @throws ParsingException
	 */
	protected void loadFromRmf(RmfBlocksManager manager) throws ParsingException {
		if (!manager.checkRmf())
			return;

		if (!new File(manager.getPath()).exists())
			return;

		GeoInfoRmfSerializer geoInfoSerializer = new GeoInfoRmfSerializer(this);
		ColorTableRmfSerializer colorTableSerializer = new ColorTableRmfSerializer();
		NoDataRmfSerializer noDataSerializer = new NoDataRmfSerializer(new NoData(noData, RasterLibrary.NODATATYPE_LAYER));
		ColorInterpretationRmfSerializer colorInterpSerializer = new ColorInterpretationRmfSerializer();
		ProjectionRmfSerializer projectionRmfSerializer = new ProjectionRmfSerializer();

		manager.addClient(geoInfoSerializer);
		manager.addClient(colorTableSerializer);
		manager.addClient(noDataSerializer);
		manager.addClient(colorInterpSerializer);
		manager.addClient(projectionRmfSerializer);

		manager.read(null);

		manager.removeAllClients();

		if (colorTableSerializer.getResult() != null)
			setColorTable((ColorTable) colorTableSerializer.getResult());

		if (noDataSerializer.getResult() != null) {
			noData = ((org.gvsig.raster.datastruct.NoData) noDataSerializer.getResult()).getValue();
			noDataEnabled = (((NoData) noDataSerializer.getResult()).getType() != RasterLibrary.NODATATYPE_DISABLED);
		}

		if (colorInterpSerializer.getResult() != null) {
			DatasetColorInterpretation ci = (DatasetColorInterpretation) colorInterpSerializer.getResult();
			setColorInterpretation(ci);
			if (ci.getBand(DatasetColorInterpretation.ALPHA_BAND) != -1)
				getTransparencyDatasetStatus().setTransparencyBand(ci.getBand(DatasetColorInterpretation.ALPHA_BAND));
		}

		if (projectionRmfSerializer.getResult() != null)
			wktProjection = CRS.convertIProjectionToWkt((IProjection) projectionRmfSerializer.getResult());
	}

	/**
	 * Obtiene el ancho de la imagen
	 * @return Ancho de la imagen
	 */
	abstract public int getWidth();

	/**
	 * Obtiene el ancho de la imagen
	 * @return Ancho de la imagen
	 */
	abstract public int getHeight();

	/**
	 * Reproyección.
	 * @param rp	Coordenadas de la transformación
	 */
	abstract public void reProject(ICoordTrans rp);

	/**
	 * Asigna un nuevo Extent
	 * @param e	Extent
	 */
	abstract public void setView(Extent e);

	/**
	 * Obtiene el extent asignado
	 * @return	Extent
	 */
	abstract public Extent getView();

	/**
	 * Obtiene el valor del raster en la coordenada que se le pasa.
	 * El valor será Double, Int, Byte, etc. dependiendo del tipo de
	 * raster.
	 * @param x	coordenada X
	 * @param y coordenada Y
	 * @return
	 */
	abstract public Object getData(int x, int y, int band)throws InvalidSetViewException, FileNotOpenException, RasterDriverException;

	/**
	 * Obtiene el núnero de bandas del fichero
	 * @return Entero que representa el número de bandas
	 */
	public int getBandCount() {
		return bandCount;
	}

	/**
	 * @return Returns the dataType.
	 */
	public int[] getDataType() {
		return dataType;
	}

	/**
	 * @param dataType The dataType to set.
	 */
	public void setDataType(int[] dataType) {
		this.dataType = dataType;
	}

	/**
	 * Obtiene el tamaño de pixel en X
	 * @return tamaño de pixel en X
	 */
	public double getPixelSizeX() {
		return externalTransformation.getScaleX();
	}

	/**
	 * Obtiene el tamaño de pixel en Y
	 * @return tamaño de pixel en Y
	 */
	public double getPixelSizeY() {
		return externalTransformation.getScaleY();
	}

	/**
	 * Obtiene una ventana de datos de la imagen a partir de coordenadas reales.
	 * No aplica supersampleo ni subsampleo sino que devuelve una matriz de igual tamaño a los
	 * pixeles de disco.
	 * @param ulx Posición X superior izquierda
	 * @param uly Posición Y superior izquierda
	 * @param lrx Posición X inferior derecha
	 * @param lry Posición Y inferior derecha
	 * @param rasterBuf	Buffer de datos
	 * @param bandList
	 * @return Buffer de datos
	 */
	abstract public IBuffer getWindowRaster(double ulx, double uly, double lrx, double lry, BandList bandList, IBuffer rasterBuf)throws InterruptedException, RasterDriverException;

	/**
	 * Obtiene una ventana de datos de la imagen a partir de coordenadas reales.
	 * No aplica supersampleo ni subsampleo sino que devuelve una matriz de igual tamaño a los
	 * pixeles de disco.
	 * @param x Posición X superior izquierda
	 * @param y Posición Y superior izquierda
	 * @param w Ancho en coordenadas reales
	 * @param h Alto en coordenadas reales
	 * @param rasterBuf	Buffer de datos
	 * @param bandList
	 * @param adjustToExtent Flag que dice si el extent solicitado debe ajustarse al extent del raster o no.
	 * @return Buffer de datos
	 */
	abstract public IBuffer getWindowRaster(double x, double y, double w, double h, BandList bandList, IBuffer rasterBuf, boolean adjustToExtent)throws InterruptedException, RasterDriverException;

	/**
	 * Obtiene una ventana de datos de la imagen a partir de coordenadas reales.
	 * Se aplica supersampleo o subsampleo dependiendo del tamaño del buffer especificado.
	 *
	 * @param minX Posición mínima X superior izquierda
	 * @param minY Posición mínima Y superior izquierda
	 * @param maxX Posición máxima X inferior derecha
	 * @param maxY Posición máxima Y inferior derecha
	 * @param bufWidth Ancho del buffer de datos
	 * @param bufHeight Alto del buffer de datos
	 * @param rasterBuf	Buffer de datos
	 * @param adjustToExtent Flag que dice si el extent solicitado debe ajustarse al extent del raster o no.
	 * @param bandList
	 * @return Buffer de datos
	 */
	abstract public IBuffer getWindowRaster(double minX, double minY, double maxX, double maxY, int bufWidth, int bufHeight, BandList bandList, IBuffer rasterBuf, boolean adjustToExtent)throws InterruptedException, RasterDriverException;

	/**
	 * Obtiene una ventana de datos de la imagen a partir de coordenadas pixel.
	 * No aplica supersampleo ni subsampleo sino que devuelve una matriz de igual tamaño a los
	 * pixeles de disco.
	 * @param x Posición X superior izquierda
	 * @param y Posición Y superior izquierda
	 * @param w Ancho en coordenadas reales
	 * @param h Alto en coordenadas reales
	 * @param rasterBuf	Buffer de datos
	 * @param bandList
	 * @return Buffer de datos
	 */
	abstract public IBuffer getWindowRaster(int x, int y, int w, int h, BandList bandList, IBuffer rasterBuf)throws InterruptedException, RasterDriverException;

	/**
	 * Obtiene una ventana de datos de la imagen a partir de coordenadas pixel.
	 * Se aplica supersampleo o subsampleo dependiendo del tamaño del buffer especificado.
	 *
	 * @param x Posición X superior izquierda
	 * @param y Posición Y superior izquierda
	 * @param w Ancho en coordenadas reales
	 * @param h Alto en coordenadas reales
	 * @param bufWidth Ancho del buffer de datos
	 * @param bufHeight Alto del buffer de datos
	 * @param rasterBuf	Buffer de datos
	 * @param bandList
	 * @return Buffer de datos
	 */
	abstract public IBuffer getWindowRaster(int x, int y, int w, int h, int bufWidth, int bufHeight, BandList bandList, IBuffer rasterBuf)throws InterruptedException, RasterDriverException;

	abstract public int getBlockSize();

	/**
	 * Obtiene el objeto que contiene los metadatos. Este método debe ser redefinido por los
	 * drivers si necesitan devolver metadatos.
	 * @return
	 */
	public DatasetMetadata getMetadata(){
		return null;
	}

	/**
	 * Obtiene el valor NoData asociado al raster. Si hay un valor en el fichero
	 * RMF se devuelve este, sino se buscará en la cabecera del raster o metadatos de
	 * este. Si finalmente no encuentra ninguno se devuelve el valor por defecto
	 * definido en la libreria.
	 * @return
	 */
	public double getNoDataValue() {
		return noData;
	}

	/**
	 * Pone el valor original de noData. Primero lo consulta del valor del metadata
	 * y luego del RMF.
	 */
	public void resetNoDataValue() {
		/**
		 * Intentamos recuperar el valor del metadatas, en caso de no encontrarlo le
		 * asignamos el noData por defecto
		 */
		noDataEnabled = false;
		do {
			if (getMetadata() == null) {
				noData = RasterLibrary.defaultNoDataValue;
				break;
			}

			if (getMetadata().getNoDataValue().length == 0) {
				noData = RasterLibrary.defaultNoDataValue;
				break;
			}

			noData = getMetadata().getNoDataValue()[0];
			noDataEnabled = getMetadata().isNoDataEnabled();
		} while (false);

		try {
			NoData noDataObject = (NoData) loadObjectFromRmf(NoData.class, null);
			if (noDataObject != null) {
				if (noDataObject.getType() > 0)
					noData = noDataObject.getValue();
				noDataEnabled = (noDataObject.getType() > 0);
			}
		} catch (RmfSerializerException e) {
		}
	}

	/**
	 * Establece el valor del NoData
	 * @param value
	 */
	public void setNoDataValue(double value) {
		noData = value;
		noDataEnabled = true;
	}

	/**
	 * Obtiene el objeto que contiene que contiene la interpretación de
	 * color por banda
	 * @return
	 */
	public DatasetColorInterpretation getColorInterpretation(){
		return colorInterpretation;
	}

	/**
	 * Asigna el objeto que contiene que contiene la interpretación de
	 * color por banda
	 * @param DatasetColorInterpretation
	 */
	public void setColorInterpretation(DatasetColorInterpretation colorInterpretation){
		this.colorInterpretation = colorInterpretation;
	}

	/**
	 * Dice si el fichero tiene georreferenciación o no.
	 * @return true si tiene georreferenciación y false si no la tiene
	 */
	public boolean isGeoreferenced(){
		return true;
	}

	/**
	 * Obtiene el objeto paleta. Esta paleta es la que tiene adjunta el fichero de disco. Si es
	 * null este objeto quiere decir que no tiene paleta para su visualización.
	 * @return Palette
	 */
	public ColorTable getColorTable() {
		return colorTable;
	}

	/**
	 * Define el objeto paleta. Si se define null quiere decir que no tiene paleta
	 * para su visualización.
	 * @param value
	 */
	public void setColorTable(ColorTable value) {
		colorTable = value;
	}

	/**
	 * Obtiene el estado de transparencia de un GeoRasterFile.
	 * @return Objeto TransparencyFileStatus
	 */
	public Transparency getTransparencyDatasetStatus() {
		return null;
	}

	/**
	 * Dado unas coordenadas reales, un tamaño de buffer y un tamaño de raster.
	 * Si el buffer es de mayor tamaño que el raster (supersampleo) quiere decir que
	 * por cada pixel de buffer se repiten varios del raster. Esta función calcula el
	 * número de pixels de desplazamiento en X e Y que corresponden al primer pixel del
	 * buffer en la esquina superior izquierda. Esto es necesario porque la coordenada
	 * solicitada es real y puede no caer sobre un pixel completo. Este calculo es
	 * util cuando un cliente quiere supersamplear sobre un buffer y que no se lo haga
	 * el driver automáticamente.
	 * @param dWorldTLX Coordenada real X superior izquierda
	 * @param dWorldTLY Coordenada real Y superior izquierda
	 * @param dWorldBRX Coordenada real X inferior derecha
	 * @param dWorldBRY Coordenada real Y inferior derecha
	 * @param nWidth Ancho del raster
	 * @param nHeight Alto del raster
	 * @param bufWidth Ancho del buffer
	 * @param bufHeight Alto del buffer
	 * @return Array de dos elementos con el desplazamiento en X e Y.
	 */
	public double[] calcSteps(double dWorldTLX, double dWorldTLY, double dWorldBRX, double dWorldBRY,
			double nWidth, double nHeight, int bufWidth, int bufHeight) {

		Point2D p1 = new Point2D.Double(dWorldTLX, dWorldTLY);
		Point2D p2 = new Point2D.Double(dWorldBRX, dWorldBRY);

		Point2D tl = worldToRaster(new Point2D.Double(p1.getX(), p1.getY()));
		Point2D br = worldToRaster(new Point2D.Double(p2.getX(), p2.getY()));

		double wPx = (bufWidth / Math.abs(br.getX() - tl.getX()));
		double hPx = (bufHeight / Math.abs(br.getY() - tl.getY()));

		int x = (int)((tl.getX() > br.getX()) ? Math.floor(br.getX()) : Math.floor(tl.getX()));
		int y = (int)((tl.getY() > br.getY()) ? Math.floor(br.getY()) : Math.floor(tl.getY()));

		double a = (tl.getX() > br.getX()) ? (Math.abs(br.getX() - x)) : (Math.abs(tl.getX() - x));
		double b = (tl.getY() > br.getY()) ? (Math.abs(br.getY() - y)) : (Math.abs(tl.getY() - y));

				double stpX = (int)((a * bufWidth) / Math.abs(br.getX() - tl.getX()));
		double stpY = (int)((b * bufHeight) / Math.abs(br.getY() - tl.getY()));

		return new double[]{stpX, stpY, wPx, hPx};
	}

	/**
	 * Lee una línea completa del raster y devuelve un array del tipo correcto. Esta función es util
	 * para una lectura rapida de todo el fichero sin necesidad de asignar vista.
	 * @param nLine Número de línea a leer
	 * @param band Banda requerida
	 * @return Object que es un array unidimendional del tipo de datos del raster
	 * @throws InvalidSetViewException
	 * @throws FileNotOpenException
	 * @throws RasterDriverException
	 */
	abstract public Object readCompleteLine(int line, int band)throws InvalidSetViewException, FileNotOpenException, RasterDriverException;

	/**
	 * Lee un bloque completo de datos del raster y devuelve un array tridimensional del tipo correcto. Esta función es util
	 * para una lectura rapida de todo el fichero sin necesidad de asignar vista.
	 * @param pos Posición donde se empieza  a leer
	 * @param blockHeight Altura máxima del bloque leido
	 * @return Object que es un array tridimendional del tipo de datos del raster. (Bandas X Filas X Columnas)
	 * @throws InvalidSetViewException
	 * @throws FileNotOpenException
	 * @throws RasterDriverException
	 */
	abstract public Object readBlock(int pos, int blockHeight)
		throws InvalidSetViewException, FileNotOpenException, RasterDriverException, InterruptedException;

	/**
	 * Convierte un punto desde coordenadas pixel a coordenadas del mundo.
	 * @param pt Punto a transformar
	 * @return punto transformado en coordenadas del mundo
	 */
	public Point2D rasterToWorld(Point2D pt) {
		Point2D p = new Point2D.Double();
		externalTransformation.transform(pt, p);
		return p;
	}

	/**
	 * Convierte un punto desde del mundo a coordenadas pixel.
	 * @param pt Punto a transformar
	 * @return punto transformado en coordenadas pixel
	 */
	public Point2D worldToRaster(Point2D pt) {
		Point2D p = new Point2D.Double();
		try {
			externalTransformation.inverseTransform(pt, p);
		} catch (NoninvertibleTransformException e) {
			return pt;
		}
		return p;
	}

	/**
	 * Calcula el extent en coordenadas del mundo real
	 * @return Extent
	 */
	public Extent getExtent() {
		return new Extent(	rasterToWorld(new Point2D.Double(0, 0)),
							rasterToWorld(new Point2D.Double(getWidth(), getHeight())),
							rasterToWorld(new Point2D.Double(getWidth(), 0)),
							rasterToWorld(new Point2D.Double(0, getHeight())));
	}

	/**
	 * Calcula el extent en coordenadas del mundo real sin rotación. Solo coordenadas y tamaño de pixel
	 * @return Extent
	 */
	public Extent getExtentWithoutRot() {
		AffineTransform at = new AffineTransform(	externalTransformation.getScaleX(), 0,
													0, externalTransformation.getScaleY(),
													externalTransformation.getTranslateX(), externalTransformation.getTranslateY());
		Point2D p1 = new Point2D.Double(0, 0);
		Point2D p2 = new Point2D.Double(getWidth(), getHeight());
		at.transform(p1, p1);
		at.transform(p2, p2);
		return new Extent(p1, p2);
	}

	/**
	 * ASigna el parámetro de inicialización del driver.
	 */
	public void setParam(Object param) {
		this.param = param;
	}

	/**
	 * Obtiene las estadisticas asociadas al fichero
	 * @return Objeto con las estadisticas
	 */
	public DatasetStatistics getStatistics() {
		return stats;
	}

	/**
	 * Obtiene el número de overviews de una banda
	 * @return
	 */
	abstract public int getOverviewCount(int band) throws BandAccessException, RasterDriverException;

	/**
	 * Obtiene el ancho de una overview de una banda
	 * @return
	 */
	abstract public int getOverviewWidth(int band, int overview) throws BandAccessException, RasterDriverException;

	/**
	 * Obtiene el alto de una overview de una banda
	 * @return
	 */
	abstract public int getOverviewHeight(int band, int overview) throws BandAccessException, RasterDriverException;

	/**
	 * Informa de si el dataset soporta overviews o no.
	 * @return true si soporta overviews y false si no las soporta.
	 */
	abstract public boolean overviewsSupport();

	/**
	 * Obtiene el histograma asociado al dataset. Este puede ser obtenido
	 * completo o según una lista de clases pasada.
	 *
	 * @return Histograma asociado al dataset.
	 */
	public DatasetHistogram getHistogram() {
		if (histogram == null)
			histogram = new DatasetHistogram(this);
		return histogram;
	}

	/**
	 * Pone a cero el porcentaje de progreso del proceso de calculo de histograma
	 */
	public void resetPercent() {
		if (histogram != null)
			histogram.resetPercent();
	}

	/**
	 * Obtiene el porcentaje de proceso en la construcción del histograma,
	 * @return porcentaje de progreso
	 */
	public int getPercent() {
		if (histogram != null)
			return histogram.getPercent();
		return 0;
	}

	/**
	 * Obtiene el gestor de ficheros RMF
	 * @return RmfBloksManager
	 */
	public RmfBlocksManager getRmfBlocksManager() {
		if (rmfBlocksManager == null) {
			String fileRMF = RasterUtilities.getNameWithoutExtension(getFName()) + ".rmf";
			rmfBlocksManager = new RmfBlocksManager(fileRMF);
		}
		return rmfBlocksManager;
	}

	/**
	 * Informa de si el punto en coordenadas del mundo real pasado por parámetro cae dentro del
	 * raster o fuera. Para este calculo cogeremos el punto a averiguar si está dentro del raster
	 * y le aplicaremos la transformación inversa de la transformación afín aplicada. Una vez hecho
	 * esto ya se puede comprobar si está dentro de los límites del extent del raster.
	 * @param p Punto a comprobar en coordenadas reales
	 * @return true si el punto está dentro y false si está fuera.
	 */
	public boolean isInside(Point2D p){
		//Realizamos los calculos solo si el punto está dentro del extent de la imagen rotada, así nos ahorramos los calculos
		//cuando el puntero está fuera

		Point2D pt = new Point2D.Double();
		try {

			getAffineTransform().inverseTransform(p, pt);
			if(	pt.getX() >= 0 && pt.getX() < getWidth() &&
					pt.getY() >= 0 && pt.getY() < getHeight())
				return true;
		} catch (NoninvertibleTransformException e) {
			return false;
		}

		return false;
	}

	/**
	 * Consulta de si un raster tiene rotación o no.
	 * @return true si tiene rotación y false si no la tiene.
	 */
	public boolean isRotated() {
		if(externalTransformation.getShearX() != 0 || externalTransformation.getShearY() != 0)
			return true;
		return false;
	}

	/**
	 * Devuelve si el RasterDataSet tiene el valor noData activo
	 * @return the noDataEnabled
	 */
	public boolean isNoDataEnabled() {
		return noDataEnabled;
	}

	/**
	 * Define si el RasterDataSet tiene el valor noData activo
	 * @param noDataEnabled the noDataEnabled to set
	 */
	public void setNoDataEnabled(boolean noDataEnabled) {
		this.noDataEnabled = noDataEnabled;
	}

	/**
	 * Devuelve si el Dataset es reproyectable
	 * @return
	 */
	public boolean isReproyectable() {
		return false;
	}

	public String getWktProjection() {
		return wktProjection;
	}

	/**
	 * Devuelve un serializador que tenga un constructor con la clase pasada por parametro
	 * @throws RmfSerializerException
	 */
	private static Class getSerializerClass(Class class1)
			throws RmfSerializerException {
		ExtensionPointManager extensionPoints = ToolsLocator.getExtensionPointManager();
		ExtensionPoint point = extensionPoints.get("Serializer");
//		ExtensionPoint extensionPoint = ExtensionPoint.getExtensionPoint("Serializer");
		Iterator iterator = point.iterator();
		while (iterator.hasNext()) {
			ExtensionPoint.Extension extension = (ExtensionPoint.Extension) iterator
					.next();
			if (extension != null) {
				Class[] args = { class1 };
				Class clase = extension.getExtension();
				try {
					clase.getConstructor(args);
					return clase;
				} catch (Exception e) {
					// Si no encuentra un constructor para esta clase, seguirá buscando en los demás elementos
				}
			}
		}
		throw new RmfSerializerException("No se ha encontrado la clase para el serializador");
	}

	/**
	 * Construye un serializador que soporte la clase class1. El constructor se invoca
	 * con el parametro value.
	 *
	 * @param class1
	 * @param value
	 * @return
	 * @throws RmfSerializerException
	 */
	static private ClassSerializer getSerializerObject(Class class1, Object value) throws RmfSerializerException {
		try {
			Class[] args = { class1 };
			Constructor constructor = getSerializerClass(class1).getConstructor(args);
			Object[] args2 = { value };
			return (ClassSerializer) constructor.newInstance(args2);
		} catch (Exception e) {
			throw new RmfSerializerException("No se ha podido crear el serializador para el Rmf", e);
		}
	}

	/**
	 * Guarda en el RMF que trata el BlocksManager el valor value usando el serializador que soporta
	 * la clase class1.
	 *
	 * @param blocksManager
	 * @param class1
	 * @param value
	 * @throws RmfSerializerException
	 */
	static private void saveObjectToRmfFile(RmfBlocksManager blocksManager, Class class1, Object value) throws RmfSerializerException {
		ClassSerializer serializerObject = getSerializerObject(class1, value);

		if (serializerObject == null)
			throw new RmfSerializerException("No se ha podido encontrar el serializador para el Rmf");

		if (!blocksManager.checkRmf())
			throw new RmfSerializerException("Error al comprobar el fichero Rmf");

		blocksManager.addClient(serializerObject);
		try {
			blocksManager.write(true);
		} catch (IOException e) {
			throw new RmfSerializerException("Error al escribir el fichero Rmf", e);
		}
		blocksManager.removeAllClients();
	}

	/**
	 * Guarda en el fichero file (en formato RMF) el objecto value usando el serializador que trata
	 * las clases class1.
	 *
	 * Si el fichero RMF no existe se crea. Ha de ser un RMF de formato valido.
	 *
	 * @param file
	 * @param class1
	 * @param value
	 * @throws RmfSerializerException
	 */
	static public void saveObjectToRmfFile(String file, Class class1, Object value) throws RmfSerializerException {
		String fileRMF = RasterUtilities.getNameWithoutExtension(file) + ".rmf";
		RmfBlocksManager blocksManager = new RmfBlocksManager(fileRMF);
		saveObjectToRmfFile(blocksManager, class1, value);
	}

	/**
	 * Guarda en el fichero file (en formato RMF) el objecto value usando el serializador que trata
	 * la misma clase que el objeto value.
	 *
	 * Si el fichero RMF no existe se crea. Ha de ser un RMF de formato valido.
	 *
	 * @param file
	 * @param value
	 * @throws RmfSerializerException
	 */
	static public void saveObjectToRmfFile(String file, Object value) throws RmfSerializerException {
		saveObjectToRmfFile(file, value.getClass(), value);
	}

	/**
	 * Guarda en el RMF el objecto actual en caso de que exista un serializador para el.
	 * El tipo del objeto se especifica en el parametro class1.
	 * Esto nos puede permitir poder poner a null un valor y encontrar su serializador.
	 * @param value
	 * @throws RmfSerializerException
	 */
	public void saveObjectToRmf(Class class1, Object value) throws RmfSerializerException {
		saveObjectToRmfFile(getRmfBlocksManager(), class1, value);
	}

	/**
	 * Carga un objecto desde un serializador del tipo class1. Usa value para iniciar dicho
	 * serializador
	 *
	 * @param class1
	 * @param value
	 * @return
	 * @throws RmfSerializerException
	 */
	static private Object loadObjectFromRmfFile(RmfBlocksManager blocksManager, Class class1, Object value) throws RmfSerializerException {
		ClassSerializer serializerObject = getSerializerObject(class1, value);

		if (serializerObject == null)
			throw new RmfSerializerException("No se ha podido encontrar el serializador para el Rmf");

		if (!blocksManager.checkRmf())
			throw new RmfSerializerException("Error al comprobar el fichero Rmf");

		blocksManager.addClient(serializerObject);
		try {
			blocksManager.read(null);
		} catch (ParsingException e) {
			throw new RmfSerializerException("Error al leer el fichero Rmf", e);
		}
		blocksManager.removeAllClients();

		return serializerObject.getResult();
	}

	/**
	 * Carga un objecto del fichero RMF del dataset
	 * @param class1
	 * @param value
	 * @return
	 * @throws RmfSerializerException
	 */
	public Object loadObjectFromRmf(Class class1, Object value) throws RmfSerializerException {
		return loadObjectFromRmfFile(getRmfBlocksManager(), class1, value);
	}

	/**
	 * Carga un objeto del fichero RMF especificado por parametro
	 * @param file
	 * @param class1
	 * @param value
	 * @return
	 * @throws RmfSerializerException
	 */
	static public Object loadObjectFromRmfFile(String file, Class class1, Object value) throws RmfSerializerException {
		String fileRMF = RasterUtilities.getNameWithoutExtension(file) + ".rmf";
		RmfBlocksManager blocksManager = new RmfBlocksManager(fileRMF);
		return loadObjectFromRmfFile(blocksManager, class1, value);
	}

	/**
	 * Guarda en el RMF el objecto actual en caso de que exista un serializador para el
	 * @param value
	 * @throws RmfSerializerException
	 */
	public void saveObjectToRmf(Object value) throws RmfSerializerException {
		saveObjectToRmf(value.getClass(), value);
	}

	/**
	 * Carga un objecto desde un serializador usando el tipo del mismo objeto pasado por parametro.
	 * Usa value para iniciar dicho serializador
	 * @param value
	 * @return
	 * @throws RmfSerializerException
	 */
	public Object loadObjectFromRmf(Object value) throws RmfSerializerException {
		return loadObjectFromRmf(value.getClass(), value);
	}
}
