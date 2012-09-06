/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2007 IVER T.I. and Generalitat Valenciana.
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
import java.awt.image.BufferedImage;

import org.cresques.cts.ICoordTrans;
import org.cresques.cts.IProjection;
import org.gvsig.raster.dataset.BandAccessException;
import org.gvsig.raster.dataset.BandList;
import org.gvsig.raster.dataset.FileNotOpenException;
import org.gvsig.raster.dataset.GeoInfo;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.InvalidSetViewException;
import org.gvsig.raster.dataset.RasterDataset;
import org.gvsig.raster.dataset.RasterDriverException;
import org.gvsig.raster.dataset.properties.DatasetColorInterpretation;
import org.gvsig.raster.dataset.rmf.ParsingException;
import org.gvsig.raster.datastruct.Extent;
import org.gvsig.raster.datastruct.Transparency;
import org.gvsig.raster.process.RasterTask;
import org.gvsig.raster.process.RasterTaskQueue;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.extensionpoint.ExtensionPoint;
import org.gvsig.tools.extensionpoint.ExtensionPointManager;

import es.gva.cit.jmrsid.MrSIDException;
/**
 * Clase encargada del acceso a los datos y repintado de imagenes MrSID. Estos
 * son registrados con la extensión sid
 *
 * @version 15/05/2008
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class MrSidDriver extends RasterDataset {
	protected MrSidNative              file             = null;
	private Extent                     viewRequest      = null;
	private DatasetColorInterpretation colorInterpr     = null;

	/**
	 * Estado de transparencia del raster.
	 */
	protected Transparency             fileTransparency = null;

	public static void register() {
		ExtensionPointManager extensionPoints = ToolsLocator.getExtensionPointManager();
		ExtensionPoint point = extensionPoints.get("RasterReader");
		point.append("sid", "", MrSidDriver.class);
	}

	/**
	 * Contructor. Abre el fichero mrsid
	 * @param proj Proyección
	 * @param fName Nombre del fichero mrsid
	 */
	public MrSidDriver(IProjection proj, Object param) {
		super(proj, ((String) param));
		setParam(param);
		try {
			file = new MrSidNative(((String) param));
			load();
			bandCount = file.nbands;
			int[] dt = new int[bandCount];
			for (int i = 0; i < dt.length; i++)
				dt[i] = IBuffer.TYPE_BYTE;
			setDataType(dt);
			super.init();

			try {
				loadFromRmf(getRmfBlocksManager());
			} catch (ParsingException e) {
				// No lee desde rmf
			}
		} catch (Exception e) {
			System.out.println("Error en constructor de MrSID");
			e.printStackTrace();
			file = null;
		}
	}

	/**
	 * Obtenemos o calculamos el extent de la imagen.
	 */
	public GeoInfo load() {
		ownTransformation = file.getOwnTransformation();
		externalTransformation = (AffineTransform) ownTransformation.clone();
		return this;
	}

	/**
	 * Libera el objeto que ha abierto el fichero
	 */
	public void close() {
		if (file != null) {
			file.close();
			file = null;
		}
	}

	/**
	 * Asigna el extent de la vista
	 */
	public void setView(Extent e) {
		viewRequest = new Extent(e);
	}

	/**
	 * Obtiene el Extent de la vista
	 */
	public Extent getView() {
		return viewRequest;
	}

	/**
	 * Obtiene el ancho de la imagen
	 */
	public int getWidth() {
		return file.width;
	}

	/**
	 * Obtiene el alto de la imagen
	 */
	public int getHeight() {
		return file.height;
	}

	public void reProject(ICoordTrans rp) {}

	/**
	 * Asigna al objeto Image los valores con los dato de la imagen contenidos en
	 * el vector de enteros.
	 * @param image imagen con los datos actuales
	 * @param startX inicio de la posición en X dentro de la imagen
	 * @param startY inicio de la posición en X dentro de la imagen
	 * @param w Ancho de la imagen
	 * @param h Alto de la imagen
	 * @param rgbArray vector que contiene la banda que se va a sustituir
	 * @param offset desplazamiento
	 * @param scansize tamaño de imagen recorrida por cada p
	 */
	protected void setRGBLine(BufferedImage image, int startX, int startY, int w, int h,
														int[] rgbArray, int offset, int scansize) {
		image.setRGB(startX, startY, w, h, rgbArray, offset, scansize);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.RasterDataset#getData(int, int, int)
	 */
	public Object getData(int x, int y, int band) throws InvalidSetViewException, FileNotOpenException, RasterDriverException {
		if (file != null) {
			if (x < 0 || y < 0 || x >= file.width || y >= file.height)
				throw new InvalidSetViewException("Request out of grid");
			Object[] data = file.getData(x, y);
			return data[band];
		}
		throw new FileNotOpenException("MrSIDNative not exist");
	}

	/**
	 * Devuelve el tamaño de bloque
	 * @return Tamaño de bloque
	 */
	public int getBlockSize() {
		return file.blocksize;
	}

	/**
	 * Informa de si el driver ha supersampleado en el último dibujado. Es el
	 * driver el que colocará el valor de esta variable cada vez que dibuja.
	 * @return true si se ha supersampleado y false si no se ha hecho.
	 */
	public boolean isSupersampling() {
		return file.isSupersampling;
	}

	public IBuffer getWindowRaster(double ulx, double uly, double lrx, double lry,
																	BandList bandList, IBuffer rasterBuf)
																	throws InterruptedException, RasterDriverException {
		RasterTask task = RasterTaskQueue.get(Thread.currentThread().toString());

		// TODO: FUNCIONALIDAD: Hacer caso del bandList
		int width = rasterBuf.getWidth();
		int height = rasterBuf.getHeight();

		// Impedimos que los valores de ancho y alto de la imágen sean menores que 1
		if (width <= 0)
			width = 1;

		if (height <= 0)
			height = 1;

		setView(new Extent(ulx, uly, lrx, lry));
		file.setView(viewRequest.getULX(), viewRequest.getULY(), viewRequest.getLRX(), viewRequest.getLRY(), width, height);

		int[] pRGBArray = new int[width * height];

		try {
			file.readScene(pRGBArray, task);
			int wBuf = rasterBuf.getWidth();
			for (int row = 0; row < rasterBuf.getHeight(); row++) {
				for (int col = 0; col < wBuf; col++) {
					rasterBuf.setElem(row, col, 0, (byte) ((pRGBArray[(row * wBuf) + col] & 0x00ff0000) >> 16));
					rasterBuf.setElem(row, col, 1, (byte) ((pRGBArray[(row * wBuf) + col] & 0x0000ff00) >> 8));
					rasterBuf.setElem(row, col, 2, (byte) (pRGBArray[(row * wBuf) + col] & 0x000000ff));
				}
				if (task.getEvent() != null)
					task.manageEvent(task.getEvent());
			}
		} catch (MrSIDException e) {
			throw new RasterDriverException("Error reading data");
		}
		return rasterBuf;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.RasterDataset#getWindowRaster(double, double, double, double, org.gvsig.raster.dataset.BandList, org.gvsig.raster.dataset.IBuffer, boolean)
	 */
	public IBuffer getWindowRaster(double ulx, double uly, double w, double h, BandList bandList, IBuffer rasterBuf, boolean adjustToExtent) throws InterruptedException, RasterDriverException {
		RasterTask task = RasterTaskQueue.get(Thread.currentThread().toString());

		// El incremento o decremento de las X e Y depende de los signos de rotación
		// y escala en la matriz de transformación. Por esto
		// tenemos que averiguar si lrx es x + w o x -w, asi como si lry es y + h o
		// y - h
		Extent ext = getExtent();
		Point2D pInit = rasterToWorld(new Point2D.Double(0, 0));
		Point2D pEnd = rasterToWorld(new Point2D.Double(getWidth(), getHeight()));
		double wRaster = Math.abs(pEnd.getX() - pInit.getX());
		double hRaster = Math.abs(pEnd.getY() - pInit.getY());
		double lrx = (((ext.getULX() - wRaster) > ext.maxX()) || ((ext.getULX() - wRaster) < ext.minX())) ? (ulx + w) : (ulx - w);
		double lry = (((ext.getULY() - hRaster) > ext.maxY()) || ((ext.getULY() - hRaster) < ext.minY())) ? (uly + h) : (uly - h);

		// TODO: FUNCIONALIDAD: Hacer caso del bandList
		int width = rasterBuf.getWidth();
		int height = rasterBuf.getHeight();

		// Impedimos que los valores de ancho y alto de la imágen sean menores que 1
		if (width <= 0)
			width = 1;

		if (height <= 0)
			height = 1;

		setView(new Extent(ulx, uly, lrx, lry));
		file.setView(viewRequest.minX(), viewRequest.maxY(), viewRequest.maxX(), viewRequest.minY(), width, height);

		int[] pRGBArray = new int[width * height];

		try {
			file.readScene(pRGBArray, task);
			int wBuf = rasterBuf.getWidth();
			for (int row = 0; row < rasterBuf.getHeight(); row++) {
				for (int col = 0; col < wBuf; col++) {
					rasterBuf.setElem(row, col, 0, (byte) ((pRGBArray[(row * wBuf) + col] & 0x00ff0000) >> 16));
					rasterBuf.setElem(row, col, 1, (byte) ((pRGBArray[(row * wBuf) + col] & 0x0000ff00) >> 8));
					rasterBuf.setElem(row, col, 2, (byte) (pRGBArray[(row * wBuf) + col] & 0x000000ff));
				}
				if (task.getEvent() != null)
					task.manageEvent(task.getEvent());
			}
		} catch (MrSIDException e) {
			throw new RasterDriverException("Error reading data");
		}
		return rasterBuf;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.RasterDataset#getWindowRaster(double, double, double, double, int, int, org.gvsig.raster.dataset.BandList, org.gvsig.raster.dataset.IBuffer, boolean)
	 */
	public IBuffer getWindowRaster(double ulx, double uly, double lrx, double lry, int bufWidth, int bufHeight, BandList bandList, IBuffer rasterBuf, boolean adjustToExtent) throws InterruptedException, RasterDriverException {
		RasterTask task = RasterTaskQueue.get(Thread.currentThread().toString());

		// Impedimos que los valores de ancho y alto de la imágen sean menores que 1
		if (bufWidth <= 0)
			bufWidth = 1;

		if (bufHeight <= 0)
			bufHeight = 1;

		setView(new Extent(ulx, uly, lrx, lry));
		file.setView(viewRequest.getULX(), viewRequest.getULY(), viewRequest.getLRX(), viewRequest.getLRY(), bufWidth, bufHeight);

		int[] pRGBArray = new int[bufWidth * bufHeight];

		try {
			file.readScene(pRGBArray, task);
			int w = rasterBuf.getWidth();
			if(getBandCount() >= 3) {
				for (int row = 0; row < rasterBuf.getHeight(); row++) {
					for (int col = 0; col < w; col++) {
						rasterBuf.setElem(row, col, 0, (byte) ((pRGBArray[(row * w) + col] & 0x00ff0000) >> 16));
						rasterBuf.setElem(row, col, 1, (byte) ((pRGBArray[(row * w) + col] & 0x0000ff00) >> 8));
						rasterBuf.setElem(row, col, 2, (byte) (pRGBArray[(row * w) + col] & 0x000000ff));
					}
					if (task.getEvent() != null)
						task.manageEvent(task.getEvent());
				}
			}
			if(getBandCount() == 1) {
				for (int row = 0; row < rasterBuf.getHeight(); row++) {
					for (int col = 0; col < w; col++)
						rasterBuf.setElem(row, col, 0, (byte) (pRGBArray[(row * w) + col] & 0x000000ff));
					if (task.getEvent() != null)
						task.manageEvent(task.getEvent());
				}
			}
		} catch (MrSIDException e) {
			throw new RasterDriverException("Error reading data");
		}
		return rasterBuf;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.RasterDataset#getWindowRaster(int, int, int, int, int, int, org.gvsig.raster.dataset.BandList, org.gvsig.raster.dataset.IBuffer)
	 */
	public IBuffer getWindowRaster(int x, int y, int w, int h, int bufWidth, int bufHeight, BandList bandList, IBuffer rasterBuf) throws InterruptedException, RasterDriverException {
		RasterTask task = RasterTaskQueue.get(Thread.currentThread().toString());

		// TODO: FUNCIONALIDAD: Hacer caso del bandList
		// Impedimos que los valores de ancho y alto de la imágen sean menores que 1
		if (bufWidth <= 0)
			bufWidth = 1;

		if (bufHeight <= 0)
			bufHeight = 1;

		Point2D begin = rasterToWorld(new Point2D.Double(x, y));
		Point2D end = rasterToWorld(new Point2D.Double(x + w, y + h));

		file.setView(begin.getX(), begin.getY(), end.getX(), end.getY(), bufWidth, bufHeight);

		int[] pRGBArray = new int[bufWidth * bufHeight];
		try {
			file.readScene(pRGBArray, task);
			for (int row = 0; row < bufHeight; row++) {
				for (int col = 0; col < bufWidth; col++) {
					rasterBuf.setElem(row, col, 0, (byte) ((pRGBArray[(row * bufWidth) + col] & 0x00ff0000) >> 16));
					rasterBuf.setElem(row, col, 1, (byte) ((pRGBArray[(row * bufWidth) + col] & 0x0000ff00) >> 8));
					rasterBuf.setElem(row, col, 2, (byte) (pRGBArray[(row * bufWidth) + col] & 0x000000ff));
				}
				if (task.getEvent() != null)
					task.manageEvent(task.getEvent());
			}
		} catch (MrSIDException e) {
			throw new RasterDriverException("Error reading data");
		}
		return rasterBuf;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.RasterDataset#readBlock(int, int)
	 */
	public Object readBlock(int pos, int blockHeight) throws InvalidSetViewException, FileNotOpenException, RasterDriverException, InterruptedException {
		RasterTask task = RasterTaskQueue.get(Thread.currentThread().toString());

		if (pos < 0)
			throw new InvalidSetViewException("Request out of grid");

		if ((pos + blockHeight) > file.height)
			blockHeight = Math.abs(file.height - pos);

		Point2D begin = rasterToWorld(new Point2D.Double(0, pos));
		Point2D end = rasterToWorld(new Point2D.Double(file.width, pos + blockHeight));

		int w = file.width;

		file.setView(begin.getX(), begin.getY(), end.getX(), end.getY(), w, blockHeight);

		int[] pRGBArray = new int[file.width * blockHeight];
		try {
			file.readScene(pRGBArray, task);
			byte[][][] buf = new byte[3][blockHeight][w];
			for (int row = 0; row < blockHeight; row++) {
				for (int col = 0; col < w; col++) {
					buf[0][row][col] = (byte) ((pRGBArray[(row * w) + col] & 0x00ff0000) >> 16);
					buf[1][row][col] = (byte) ((pRGBArray[(row * w) + col] & 0x0000ff00) >> 8);
					buf[2][row][col] = (byte) (pRGBArray[(row * w) + col] & 0x000000ff);
				}
				if (task.getEvent() != null)
					task.manageEvent(task.getEvent());
			}
			return buf;
		} catch (MrSIDException e) {
			throw new RasterDriverException("Error reading data");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.RasterDataset#readCompleteLine(int, int)
	 */
	public Object readCompleteLine(int line, int band)
					throws InvalidSetViewException, FileNotOpenException, RasterDriverException {
		RasterTask task = RasterTaskQueue.get(Thread.currentThread().toString());

		if (line > this.getHeight() || band > this.getBandCount())
			throw new InvalidSetViewException("Request out of grid");

		try {
			Extent extent = getExtent();
			Point2D pt = rasterToWorld(new Point2D.Double(extent.minX(), line));
			file.setView(extent.minX(), pt.getY(), extent.maxX(), pt.getY(), getWidth(), 1);
			int[] pRGBArray = new int[getWidth()];
			file.readScene(pRGBArray, task);
			return pRGBArray;
		} catch (MrSIDException e) {
			throw new RasterDriverException("Error reading data from MrSID library");
		} catch (InterruptedException e) {
			// El proceso que debe ser interrumpido es el que llama a readLine.
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.RasterDataset#getWindowRaster(int, int, int, int, org.gvsig.raster.dataset.BandList, org.gvsig.raster.dataset.IBuffer)
	 */
	public IBuffer getWindowRaster(int x, int y, int w, int h, BandList bandList, IBuffer rasterBuf) throws InterruptedException, RasterDriverException {
		try {
			file.readWindow(rasterBuf, bandList, x, y, w, h);
		} catch (MrSIDException e) {
			throw new RasterDriverException("Error reading data");
		}
		return rasterBuf;
	}

	/**
	 * Obtiene el objeto que contiene el estado de la transparencia
	 */
	public Transparency getTransparencyDatasetStatus() {
		if (fileTransparency == null)
			fileTransparency = new Transparency();
		return fileTransparency;
	}

	/**
	 * Obtiene el objeto que contiene que contiene la interpretación de color por banda
	 * @return
	 */
	public DatasetColorInterpretation getColorInterpretation() {
		if (colorInterpr == null) {
			colorInterpr = new DatasetColorInterpretation();
			colorInterpr.initColorInterpretation(getBandCount());
			if (getBandCount() == 1)
				colorInterpr.setColorInterpValue(0, DatasetColorInterpretation.GRAY_BAND);
			if (getBandCount() >= 3) {
				colorInterpr.setColorInterpValue(0, DatasetColorInterpretation.RED_BAND);
				colorInterpr.setColorInterpValue(1, DatasetColorInterpretation.GREEN_BAND);
				colorInterpr.setColorInterpValue(2, DatasetColorInterpretation.BLUE_BAND);
			}
		}
		return colorInterpr;
	}

	/**
	 * Asigna el objeto que contiene que contiene la interpretación de color por banda
	 * @param DatasetColorInterpretation
	 */
	public void setColorInterpretation(DatasetColorInterpretation colorInterpretation) {
		this.colorInterpretation = colorInterpretation;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.GeoInfo#getWktProjection()
	 */
	public String getWktProjection() {
		// System.err.println("======>" + file);
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.GeoInfo#setAffineTransform(java.awt.geom.AffineTransform)
	 */
	public void setAffineTransform(AffineTransform t) {
		super.setAffineTransform(t);
		file.setExternalTransform(t);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.RasterDataset#getOverviewCount(int)
	 */
	public int getOverviewCount(int band) throws BandAccessException, RasterDriverException {
		if (band >= getBandCount())
			throw new BandAccessException("Wrong band");
		try {
			return file.getNumLevels();
		} catch (MrSIDException e) {
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
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.RasterDataset#getOverviewHeight(int, int)
	 */
	public int getOverviewHeight(int band, int overview) throws BandAccessException, RasterDriverException {
		if (band >= getBandCount())
			throw new BandAccessException("Wrong band");
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.RasterDataset#overviewsSupport()
	 */
	public boolean overviewsSupport() {
		// No podemos escribir por lo que no podemos informar de que soporta overviews aunque el formato si lo haga.
		return false;
	}
}