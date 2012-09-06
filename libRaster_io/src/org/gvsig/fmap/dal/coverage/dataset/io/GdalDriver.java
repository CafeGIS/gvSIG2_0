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
package org.gvsig.fmap.dal.coverage.dataset.io;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.cresques.cts.ICoordTrans;
import org.cresques.cts.IProjection;
import org.gvsig.raster.dataset.BandAccessException;
import org.gvsig.raster.dataset.BandList;
import org.gvsig.raster.dataset.FileNotOpenException;
import org.gvsig.raster.dataset.GeoInfo;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.InvalidSetViewException;
import org.gvsig.raster.dataset.NotSupportedExtensionException;
import org.gvsig.raster.dataset.RasterDataset;
import org.gvsig.raster.dataset.RasterDriverException;
import org.gvsig.raster.dataset.properties.DatasetColorInterpretation;
import org.gvsig.raster.dataset.properties.DatasetMetadata;
import org.gvsig.raster.dataset.rmf.ParsingException;
import org.gvsig.raster.datastruct.Extent;
import org.gvsig.raster.datastruct.Transparency;
import org.gvsig.raster.util.RasterUtilities;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.extensionpoint.ExtensionPoint;
import org.gvsig.tools.extensionpoint.ExtensionPointManager;

import es.gva.cit.jgdal.GdalException;
/**
 * Clase que representa al driver de acceso a datos de gdal.
 *
 * @author Luis W. Sevilla
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class GdalDriver extends RasterDataset {
	public static final String FORMAT_GTiff    = "GTiff";
	public static final String FORMAT_VRT      = "VRT";
	public static final String FORMAT_NITF     = "NITF";
	public static final String FORMAT_HFA      = "HFA";
	public static final String FORMAT_ELAS     = "ELAS";
	public static final String FORMAT_MEM      = "MEM";
	public static final String FORMAT_BMP      = "BMP";
	public static final String FORMAT_PCIDSK   = "PCIDSK";
	public static final String FORMAT_ILWIS    = "ILWIS";
	public static final String FORMAT_HDF4     = "HDF4Image";
	public static final String FORMAT_PNM      = "PNM";
	public static final String FORMAT_ENVI     = "ENVI";
	public static final String FORMAT_EHDR     = "EHdr";
	public static final String FORMAT_PAUX     = "PAux";
	public static final String FORMAT_MFF      = "MFF";
	public static final String FORMAT_MFF2     = "MFF2";
	public static final String FORMAT_BT       = "BT";
	public static final String FORMAT_IDA      = "IDA";
	public static final String FORMAT_RMF      = "RMF";
	public static final String FORMAT_RST      = "RST";
	public static final String FORMAT_LEVELLER = "Leveller";
	public static final String FORMAT_TERRAGEN = "Terragen";
	public static final String FORMAT_ERS      = "ERS";
	public static final String FORMAT_INGR     = "INGR";
	public static final String FORMAT_GSAG     = "GSAG";
	public static final String FORMAT_GSBG     = "GSBG";
	public static final String FORMAT_ADRG     = "ADRG";
	public static final int    BAND_HEIGHT     = 64;
	protected GdalNative       file            = null;


	public GdalDriver(){super(null, null);}

	private Extent viewRequest = null;

	public static void register() {
		ExtensionPointManager extensionPoints = ToolsLocator.getExtensionPointManager();
		ExtensionPoint point = extensionPoints.get("RasterReader");

		point.append("bmp", "", GdalDriver.class);
		point.append("gif", "", GdalDriver.class);
		point.append("tif", "", GdalDriver.class);
		point.append("tiff", "", GdalDriver.class);
		point.append("jpg", "", GdalDriver.class);
		point.append("jpeg", "", GdalDriver.class);
		point.append("png", "", GdalDriver.class);
		point.append("vrt", "", GdalDriver.class);
		point.append("dat", "", GdalDriver.class); // Envi
		point.append("lan", "", GdalDriver.class); // Erdas
		point.append("gis", "", GdalDriver.class); // Erdas
		point.append("img", "", GdalDriver.class); // Erdas
		point.append("pix", "", GdalDriver.class); // PCI Geomatics
		point.append("aux", "", GdalDriver.class); // PCI Geomatics
		point.append("adf", "", GdalDriver.class); // ESRI Grids
		point.append("mpr", "", GdalDriver.class); // Ilwis
		point.append("mpl", "", GdalDriver.class); // Ilwis
		point.append("map", "", GdalDriver.class); // PC Raster
		point.append("asc", "", GdalDriver.class);
		point.append("pgm", "", GdalDriver.class); //Ficheros PNM en escala de grises
		point.append("ppm", "", GdalDriver.class); //Ficheros PNM en RGB
		point.append("rst", "", GdalDriver.class); //IDRISIS
		point.append("rmf", "", GdalDriver.class); //Raster Matrix Format
		point.append("nos", "", GdalDriver.class);
		point.append("kap", "", GdalDriver.class);
		point.append("hdr", "", GdalDriver.class);
		point.append("raw", "", GdalDriver.class);
		point.append("ers", "", GdalDriver.class);

		point = extensionPoints.get("DefaultDriver");
		point.append("reader", "", GdalDriver.class);
	}

	/**
	 * Constructor del driver de Gdal. Crea las referencias al fichero y carga
	 * las estructuras con la información y los metadatos.
	 * @param proj Proyección
	 * @param param Parametros de carga
	 * @throws NotSupportedExtensionException
	 */
	public GdalDriver(IProjection proj, Object param)throws NotSupportedExtensionException {
		super(proj, param);
		try {
			if(param instanceof String) {
				setParam(translateFileName((String)param));
				validRmf(((String)param));
				file = new GdalNative(translateFileName((String)param));
				setColorTable(file.palette);
				noData = file.getNoDataValue();
				wktProjection = file.getProjectionRef();
				noDataEnabled = file.existsNoDataValue();
				ownTransformation = file.getOwnTransformation();
				externalTransformation = (AffineTransform)ownTransformation.clone();
				load();
			} else
				setParam(param);
				//TODO: FUNCIONALIDAD: Formatos gestionados por gdal que no tienen extensión. Estos tendrán un objeto IRegistrableRasterFormat Por ej: Grass
			bandCount = file.getRasterCount();
		} catch (GdalException e) {
			e.printStackTrace();
			throw new NotSupportedExtensionException("Extension not supported");
		} catch(Exception e) {
				System.out.println("Error en GdalOpen");
				e.printStackTrace();
				file = null;
		}

		//Obtenemos el tipo de dato de gdal y lo convertimos el de RasterBuf
		int[] dt = new int[file.getDataType().length];
		for (int i = 0; i < dt.length; i++)
			dt[i] = RasterUtilities.getRasterBufTypeFromGdalType(file.getDataType()[i]);
		setDataType(dt);

		super.init();

		try {
			loadFromRmf(getRmfBlocksManager());
		} catch (ParsingException e) {
			//No lee desde rmf
		}
	}

	/**
	 * Comprueba si el fichero abierto es un RasterMetaFile o una imagen
	 * raster.
	 * @throws GdalException
	 */
	private void validRmf(String file) throws GdalException {
		if(file.endsWith(".rmf")) {
			File f = new File(file);
			FileReader fr;
			try {
				fr = new FileReader(f);
				BufferedReader br = new BufferedReader(fr);
				char[] buffer = new char[5];
					br.read(buffer);
					StringBuffer st = new StringBuffer(new String(buffer));
					if(st.toString().equals("<?xml"))
						throw new GdalException("RasterMetaFile");
			} catch (FileNotFoundException e) {
				throw new GdalException("File Not Found");
			} catch (IOException e) {
				throw new GdalException("");
			}
		}
	}
	/**
	 * Obtenemos o calculamos el extent de la imagen.
	 */
	public GeoInfo load() {
		return this;
	}

	/**
	 * Cierra el fichero de imagen
	 */
	public void close() {
		try {
			if(file != null){
				file.close();
				file = null;
			}
		} catch (GdalException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.RasterDataset#translateFileName(java.lang.String)
	 */
	public String translateFileName(String fileName) {
		if(fileName.endsWith("hdr"))
			return fileName.substring(0, fileName.lastIndexOf("."));
		return fileName;
	}

	/**
	 * Asigna el extent de la vista actual. existe un fichero .rmf debemos hacer una transformación
	 * de la vista asignada ya que la petición viene en coordenadas del fichero .rmf y la vista (v)
	 * ha de estar en coordenadas del fichero.
	 */
	public void setView(Extent e) {
		viewRequest = e;
	}

	/**
	 * Obtiene extent de la vista actual
	 */
	public Extent getView() {
		return viewRequest;
	}

	/**
	 * Obtiene la anchura del fichero
	 */
	public int getWidth() {
		return file.width;
	}

	/**
	 * Obtiene la altura del fichero
	 */
	public int getHeight() {
		return file.height;
	}

	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.fmap.driver.GeoRasterFile#readCompletetLine(int, int)
	 */
	public Object readCompleteLine(int line, int band)throws InvalidSetViewException, FileNotOpenException, RasterDriverException {
		if(line > this.getHeight() || band > this.getBandCount())
			throw new InvalidSetViewException("Request out of grid");

		try{
			return file.readCompleteLine(line, band);
		}catch(GdalException e){
			throw new RasterDriverException("Error reading data from Gdal library");
		}
	}

	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.raster.dataset.RasterDataset#readBlock(int, int)
	 */
	public Object readBlock(int pos, int blockHeight)
		throws InvalidSetViewException, FileNotOpenException, RasterDriverException, InterruptedException {
		if(pos < 0)
			throw new InvalidSetViewException("Request out of grid");

		if((pos + blockHeight) > getHeight())
			blockHeight = Math.abs(getHeight() - pos);
		try{
			return file.readBlock(pos, blockHeight);
		}catch(GdalException e){
			throw new RasterDriverException("Error reading data from Gdal library");
		}
	}

	/* (non-Javadoc)
	 * @see org.cresques.io.GeoRasterFile#getData(int, int, int)
	 */
	public Object getData(int x, int y, int band)throws InvalidSetViewException, FileNotOpenException, RasterDriverException {
		if(file != null){
			if(x < 0 || y < 0 || x >= file.width || y >= file.height)
				throw new InvalidSetViewException("Request out of grid");
			Object[] data = file.getData(x, y);
			return data[band];
		}
		throw new FileNotOpenException("GdalNative not exist");
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.RasterDataset#getWindowRaster(double, double, double, double, org.gvsig.raster.dataset.BandList, org.gvsig.raster.dataset.IBuffer)
	 */
	public IBuffer getWindowRaster(double ulx, double uly, double lrx, double lry, BandList bandList, IBuffer rasterBuf) throws InterruptedException, RasterDriverException {
		Extent selectedExtent = new Extent(ulx, uly, lrx, lry);
		setView(selectedExtent);

		try {
			file.readWindow(rasterBuf, bandList, viewRequest.getULX(), viewRequest.getULY(), viewRequest.getLRX(), viewRequest.getLRY(), rasterBuf.getWidth(), rasterBuf.getHeight(), true);
		} catch (GdalException e) {
			throw new RasterDriverException("Error reading data");
		}

		return rasterBuf;
	}

	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.fmap.driver.GeoRasterFile#getWindowRaster(double, double, double, double, org.gvsig.fmap.driver.BandList, org.gvsig.fmap.driver.IBuffer)
	 */
	public IBuffer getWindowRaster(double ulx, double uly, double w, double h, BandList bandList, IBuffer rasterBuf, boolean adjustToExtent) throws InterruptedException, RasterDriverException {
		//El incremento o decremento de las X e Y depende de los signos de rotación y escala en la matriz de transformación. Por esto
		//tenemos que averiguar si lrx es x + w o x -w, asi como si lry es y + h o y - h
		Extent ext = getExtent();
		Point2D pInit = rasterToWorld(new Point2D.Double(0, 0));
		Point2D pEnd = rasterToWorld(new Point2D.Double(getWidth(), getHeight()));
		double wRaster = Math.abs(pEnd.getX() - pInit.getX());
		double hRaster = Math.abs(pEnd.getY() - pInit.getY());
		double lrx = (((ext.getULX() - wRaster) > ext.maxX()) || ((ext.getULX() - wRaster) < ext.minX())) ? (ulx + w) : (ulx - w);
		double lry = (((ext.getULY() - hRaster) > ext.maxY()) || ((ext.getULY() - hRaster) < ext.minY())) ? (uly + h) : (uly - h);

		Extent selectedExtent = new Extent(ulx, uly, lrx, lry);
		setView(selectedExtent);

		try {
			file.readWindow(rasterBuf, bandList, viewRequest.getULX(), viewRequest.getULY(), viewRequest.getLRX(), viewRequest.getLRY(), rasterBuf.getWidth(), rasterBuf.getHeight(), adjustToExtent);
		} catch (GdalException e) {
			throw new RasterDriverException("Error reading data");
		}

		return rasterBuf;
	}

	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.fmap.driver.GeoRasterFile#getWindowRaster(double, double, double, double, int, int, org.gvsig.fmap.driver.BandList, org.gvsig.fmap.driver.IBuffer)
	 */
	public IBuffer getWindowRaster(double minX, double minY, double maxX, double maxY, int bufWidth, int bufHeight, BandList bandList, IBuffer rasterBuf, boolean adjustToExtent) throws InterruptedException, RasterDriverException {
		Extent selectedExtent = new Extent(minX, minY, maxX, maxY);
		setView(selectedExtent);

		double width = 0;
		double height = 0;

		Point2D ul = new Point2D.Double(viewRequest.getULX(), viewRequest.getULY());
		Point2D lr = new Point2D.Double(viewRequest.getLRX(), viewRequest.getLRY());
		ul = worldToRaster(ul);
		lr = worldToRaster(lr);
		width = Math.abs(lr.getX() - ul.getX());
		height = Math.abs(lr.getY() - ul.getY());

		try {
			file.readWindow(rasterBuf, bandList, viewRequest.getULX(), viewRequest.getULY(), viewRequest.getLRX(), viewRequest.getLRY(), width, height, bufWidth, bufHeight, adjustToExtent);
		} catch (GdalException e) {
			throw new RasterDriverException("Error reading data");
		}

		return rasterBuf;
	}

	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.fmap.driver.GeoRasterFile#getWindowRaster(int, int, int, int, org.gvsig.fmap.driver.BandList, org.gvsig.fmap.driver.IBuffer)
	 */
	public IBuffer getWindowRaster(int x, int y, int w, int h, BandList bandList, IBuffer rasterBuf) throws InterruptedException, RasterDriverException {
		try {
			setView(
			new Extent( RasterUtilities.getMapRectFromPxRect(getExtent().toRectangle2D(),
						getWidth(),
						getHeight(),
						new Rectangle2D.Double(x, y, w, h)))
			);
			file.readWindow(rasterBuf, bandList, x, y, w, h);
		} catch (GdalException e) {
			throw new RasterDriverException("Error reading data");
		}
		return rasterBuf;
	}

	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.fmap.driver.GeoRasterFile#getWindowRaster(int, int, int, int, int, int, org.gvsig.fmap.driver.BandList, org.gvsig.fmap.driver.IBuffer)
	 */
	public IBuffer getWindowRaster(int x, int y, int w, int h, int bufWidth, int bufHeight, BandList bandList, IBuffer rasterBuf) throws InterruptedException, RasterDriverException {
		try {
			setView(
			new Extent( RasterUtilities.getMapRectFromPxRect(getExtent().toRectangle2D(),
						getWidth(),
						getHeight(),
						new Rectangle2D.Double(x, y, w, h)))
			);
			file.readWindow(rasterBuf, bandList, x, y, w, h, bufWidth, bufHeight);
		} catch (GdalException e) {
			throw new RasterDriverException("Error reading data");
		}
		return rasterBuf;
	}

	/**
	 * Devuelve el tamaño de bloque
	 * @return Tamaño de bloque
	 */
	public int getBlockSize(){
		if(file != null)
			return file.getBlockSize();
		else
			return 0;
	}

	/**
	 * Obtiene el objeto que contiene los metadatos
	 */
	public DatasetMetadata getMetadata() {
		if(file != null)
			return file.metadata;
		else
			return null;
	}

	/**
	 * Obtiene el objeto que contiene que contiene la interpretación de
	 * color por banda
	 * @return
	 */
	public DatasetColorInterpretation getColorInterpretation(){
		if(file != null)
			return file.colorInterpr;
		return null;
	}

	/**
	 * Asigna el objeto que contiene que contiene la interpretación de
	 * color por banda
	 * @param DatasetColorInterpretation
	 */
	public void setColorInterpretation(DatasetColorInterpretation colorInterpretation){
		if(file != null)
			file.colorInterpr = colorInterpretation;
	}

	/**
	 * Obtiene el objeto que contiene el estado de la transparencia
	 */
	public Transparency getTransparencyDatasetStatus() {
		return file.fileTransparency;
	}

	/**
	 * Obtiene el flag que dice si la imagen está o no georreferenciada
	 * @return true si está georreferenciada y false si no lo está.
	 */
	public boolean isGeoreferenced() {
		if(file != null)
			return file.georeferenced;
		else
			return false;
	}

	/**
	 * Informa de si el driver ha supersampleado en el último dibujado. Es el driver el que colocará
	 * el valor de esta variable cada vez que dibuja.
	 * @return true si se ha supersampleado y false si no se ha hecho.
	 */
	public boolean isSupersampling() {
		if(file != null)
			return file.isSupersampling;
		else
			return false;
	}

	public GdalNative getNative(){
		return file;
	}

	/**
	 * Obtiene el nombre del driver
	 */
	public String getName() {
		return "gvSIG Gdal Raster Driver";
	}

	public void reProject(ICoordTrans rp) {
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.RasterDataset#setAffineTransform(java.awt.geom.AffineTransform)
	 */
	public void setAffineTransform(AffineTransform t){
		super.setAffineTransform(t);
		file.setExternalTransform(t);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.RasterDataset#getOverviewCount(int)
	 */
	public int getOverviewCount(int band) throws BandAccessException, RasterDriverException {
		if(band >= getBandCount())
			throw new BandAccessException("Wrong band");
		try {
			return file.getRasterBand(band + 1).getOverviewCount();
		} catch (GdalException e) {
			throw new RasterDriverException("");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.RasterDataset#getOverviewWidth(int, int)
	 */
	public int getOverviewWidth(int band, int overview) throws BandAccessException, RasterDriverException {
		if (band >= getBandCount())
			throw new BandAccessException("Wrong band");
		try {
			if (overview >= file.getRasterBand(band + 1).getOverviewCount())
				throw new BandAccessException("Wrong overview count");
			return file.getRasterBand(band + 1).getOverview(overview).getRasterBandXSize();
		} catch (GdalException e) {
			throw new RasterDriverException("");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.RasterDataset#getOverviewWidth(int, int)
	 */
	public int getOverviewHeight(int band, int overview) throws BandAccessException, RasterDriverException {
		if (band >= getBandCount())
			throw new BandAccessException("Wrong band");
		try {
			if (overview >= file.getRasterBand(band + 1).getOverviewCount())
				throw new BandAccessException("Wrong overview count");
			return file.getRasterBand(band + 1).getOverview(overview).getRasterBandYSize();
		} catch (GdalException e) {
			throw new RasterDriverException("");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.RasterDataset#overviewsSupport()
	 */
	public boolean overviewsSupport() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.RasterDataset#isReproyectable()
	 */
	public boolean isReproyectable() {
		return true;
	}
}