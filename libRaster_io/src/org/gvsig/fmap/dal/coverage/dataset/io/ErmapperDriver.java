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
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.Vector;

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
import org.gvsig.raster.dataset.rmf.ParsingException;
import org.gvsig.raster.datastruct.Extent;
import org.gvsig.raster.datastruct.Transparency;
import org.gvsig.raster.process.RasterTask;
import org.gvsig.raster.process.RasterTaskQueue;
import org.gvsig.raster.util.RasterUtilities;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.extensionpoint.ExtensionPoint;
import org.gvsig.tools.extensionpoint.ExtensionPointManager;

import com.ermapper.ecw.JNCSException;
import com.ermapper.ecw.JNCSFile;
import com.ermapper.ecw.JNCSFileNotOpenException;
import com.ermapper.ecw.JNCSInvalidSetViewException;
import com.ermapper.ecw.JNCSProgressiveUpdate;
/**
 * Driver de Ecw
 *
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class ErmapperDriver extends RasterDataset implements JNCSProgressiveUpdate {

	private JNCSFile 				      file = null;

	/**
	 * Estado de transparencia del raster.
	 */
	protected Transparency  		      fileTransparency = null;

	/**
	 * Extent de la ventana seleccionada
	 */
	private Extent                        viewRequest = null;

	private DatasetColorInterpretation    colorInterpr = null;

	public static void register() {
		ExtensionPointManager extensionPoints =ToolsLocator.getExtensionPointManager();
		ExtensionPoint point=extensionPoints.get("RasterReader");
//		ExtensionPoint point = ExtensionPoint.getExtensionPoint("RasterReader");
		point.append("ecw", "", ErmapperDriver.class);
		point.append("jp2", "", ErmapperDriver.class);
	}

	class Contour extends Vector {
		final private static long serialVersionUID = 0;
		public double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE;
		public double maxX = -Double.MAX_VALUE, maxY = -Double.MAX_VALUE;
		public Contour() {
			super();
		}
		public void add(Point2D pt) {
			super.add(pt);
			if (pt.getX() > maxX) maxX = pt.getX();
			if (pt.getX() < minX) minX = pt.getX();
			if (pt.getY() > maxY) maxY = pt.getY();
			if (pt.getY() < minY) minY = pt.getY();
		}
	}

	/**
	 * Constructor. Abre el dataset.
	 * @param proj Proyección
	 * @param fName Nombre del fichero ecw
	 * @throws NotSupportedExtensionException
	 */
	public ErmapperDriver(IProjection proj, Object param)throws NotSupportedExtensionException {
		super(proj, ((String)param));
		setParam(param);
		try {

			if (!new File(((String)param)).exists() && !((String)param).startsWith("ecwp:"))
				throw new NotSupportedExtensionException("Extension not supported");

			file = new JNCSFile(((String)param), false);
			load();
			bandCount = file.numBands;
			getTransparencyDatasetStatus();

			int[] dt = new int[bandCount];
			for (int i = 0; i < bandCount; i++)
				dt[i] = IBuffer.TYPE_BYTE;
			setDataType(dt);

			super.init();

			try {
				loadFromRmf(getRmfBlocksManager());
			} catch (ParsingException e) {
				//No lee desde rmf
			}

		} catch (Exception e) {
			throw new NotSupportedExtensionException("Extension not supported");
		}
	}

	/**
	 * Carga un ECW.
	 * @param fname
	 */
	public GeoInfo load() {
		ownTransformation = new AffineTransform(file.cellIncrementX, 0, 0, file.cellIncrementY, file.originX, file.originY);
		externalTransformation = (AffineTransform) ownTransformation.clone();
		return this;
	}

	/**
	 * Cierra el fichero ECW
	 */
	public void close() {
		if(file != null) {
			file.close(false);
			file = null;
		}
	}

	/**
	 * Obtiene el objeto que contiene el estado de la transparencia
	 */
	public Transparency getTransparencyDatasetStatus() {
		if(fileTransparency == null)
			fileTransparency = new Transparency();
		return fileTransparency;
	}

	/**
	 * Devuelve el ancho de la imagen
	 */
	public int getWidth() {
		return file.width;
	}

	/**
	 * Devuelve el alto de la imagen
	 */
	public int getHeight() {
		return file.height;
	}

	/**
	 * Obtiene el extent de la última ventana seleccionada.
	 * @return Extent
	 */
	public Extent getView() {
		return viewRequest;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.driver.RasterDataset#setView(org.gvsig.fmap.raster.Extent)
	 */
	public void setView(Extent e) {
		viewRequest = new Extent(e);
	}

	/**
	 * Cuando se hace una petición de carga de buffer la extensión pedida puede estar ajustada a la extensión del raster
	 * o no estarlo. En caso de no estarlo los pixeles del buffer que caen fuera de la extensión del raster tendrán valor
	 * de NoData. Esta función calcula en que pixel del buffer hay que empezar a escribir en caso de que este sea mayor
	 * que los datos a leer.
	 * @param dWorldTLX Posición X superior izquierda en coord reales
	 * @param dWorldTLY Posición Y superior izquierda en coord reales
	 * @param dWorldBRX Posición X inferior derecha en coord reales
	 * @param dWorldBRY Posición Y inferior derecha en coord reales
	 * @param nWidth Ancho en pixeles del buffer
	 * @param nHeight Alto en pixeles del buffer
	 * @return desplazamiento dentro del buffer en X e Y
	 */
	/*private int[] calcStepBuffer(Extent dataExtent, int nWidth, int nHeight, int[] stpBuffer) {
		Extent imageExtent = getExtentWithoutRot();
		Extent ajustDataExtent = RasterUtilities.calculateAdjustedView(dataExtent, imageExtent);
		if(!RasterUtilities.compareExtents(dataExtent, ajustDataExtent)) {
			Point2D p1 = worldToRaster(new Point2D.Double(ajustDataExtent.minX(), ajustDataExtent.maxY()));
			Point2D p2 = worldToRaster(new Point2D.Double(ajustDataExtent.maxX(), ajustDataExtent.minY()));
			Point2D p3 = worldToRaster(new Point2D.Double(dataExtent.minX(), dataExtent.maxY()));
			Point2D p4 = worldToRaster(new Point2D.Double(dataExtent.maxX(), dataExtent.minY()));
			//Ese es el ancho y alto q tendría el buffer en caso de haberse ajustado
			int w = (int)Math.abs(Math.ceil(p2.getX()) - Math.floor(p1.getX()));
			int h = (int)Math.abs(Math.floor(p1.getY()) - Math.ceil(p2.getY()));

			stpBuffer[0] = (int)(p1.getX() + (-p3.getX()));
			stpBuffer[1] = (int)(p1.getY() + (-p3.getY()));
			stpBuffer[2] = stpBuffer[0] + w;
			stpBuffer[3] = stpBuffer[1] + h;
			return new int[]{w, h};
		}
		return new int[]{nWidth, nHeight};
	}*/

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.RasterDataset#getWindowRaster(double, double, double, double, org.gvsig.raster.dataset.BandList, org.gvsig.raster.dataset.IBuffer)
	 */
	public IBuffer getWindowRaster(double ulx, double uly, double lrx, double lry, BandList bandList, IBuffer rasterBuf) throws InterruptedException, RasterDriverException {
		Point2D p1 = new Point2D.Double(ulx, uly);
		Point2D p2 = new Point2D.Double(lrx, lry);
		try {
			externalTransformation.inverseTransform(p1, p1);
			externalTransformation.inverseTransform(p2, p2);
			ownTransformation.transform(p1, p1);
			ownTransformation.transform(p2, p2);
		} catch (NoninvertibleTransformException e) {
			throw new RasterDriverException("Noninvertible transform");
		}

		Extent selectedExtent = new Extent(p1.getX(), p1.getY(), p2.getX(), p2.getY());

		setView(selectedExtent);
		int wPx = rasterBuf.getWidth();
		int hPx = rasterBuf.getHeight();
		int[] stpBuffer = new int[]{0, 0 , wPx, hPx};

		loadBuffer(viewRequest, wPx, hPx, rasterBuf, bandList, stpBuffer);

		return rasterBuf;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.driver.RasterDataset#getWindowRaster(double, double, double, double, org.gvsig.fmap.driver.BandList, org.gvsig.fmap.driver.IBuffer, boolean)
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

		Point2D p1 = new Point2D.Double(ulx, uly);
		Point2D p2 = new Point2D.Double(lrx, lry);
		try {
			externalTransformation.inverseTransform(p1, p1);
			externalTransformation.inverseTransform(p2, p2);
			ownTransformation.transform(p1, p1);
			ownTransformation.transform(p2, p2);
		} catch (NoninvertibleTransformException e) {
			throw new RasterDriverException("Noninvertible transform");
		}

		Extent selectedExtent = new Extent(p1.getX(), p1.getY(), p2.getX(), p2.getY());

		setView(selectedExtent);
		int wPx = rasterBuf.getWidth();
		int hPx = rasterBuf.getHeight();
		int[] stpBuffer = new int[]{0, 0 , wPx, hPx};

		//TODO: FUNCIONALIDAD: Implementar adjustToExtent = false
		/*if(!adjustToExtent){
					 int[] wh = calcStepBuffer(selectedExtent, wPx, hPx, stpBuffer);
					 if(x < 0)
						 x  = 0;
					 if(y < 0)
						 y  = 0;
					 readData(buf, bandList, x, y, wh[0], wh[1], wh[0], wh[1], 0, 0, stpBuffer);
					 return;
			}*/

		loadBuffer(viewRequest, wPx, hPx, rasterBuf, bandList, stpBuffer);

		return rasterBuf;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.driver.RasterDataset#getWindowRaster(int, int, int, int, org.gvsig.fmap.driver.BandList, org.gvsig.fmap.driver.IBuffer)
	 */
	public IBuffer getWindowRaster(int x, int y, int w, int h, BandList bandList, IBuffer rasterBuf) throws InterruptedException, RasterDriverException {
		Point2D init = this.rasterToWorld(new Point2D.Double(x, y));
		Point2D end = this.rasterToWorld(new Point2D.Double(x + w, y + h));
		Extent selectedExtent = new Extent(init.getX(), init.getY(), end.getX(), end.getY());
		setView(selectedExtent);
		int[] stpBuffer = new int[]{0, 0 , w, h};

		loadBuffer(viewRequest, w, h, rasterBuf, bandList, stpBuffer);
		return rasterBuf;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.driver.RasterDataset#getWindowRaster(double, double, double, double, int, int, org.gvsig.fmap.driver.BandList, org.gvsig.fmap.driver.IBuffer, boolean)
	 */
	public IBuffer getWindowRaster(double ulx, double uly, double lrx, double lry, int bufWidth, int bufHeight, BandList bandList, IBuffer rasterBuf, boolean adjustToExtent) throws InterruptedException, RasterDriverException {
		Point2D p1 = new Point2D.Double(ulx, uly);
		Point2D p2 = new Point2D.Double(lrx, lry);
		try {
			externalTransformation.inverseTransform(p1, p1);
			externalTransformation.inverseTransform(p2, p2);
			ownTransformation.transform(p1, p1);
			ownTransformation.transform(p2, p2);
		} catch (NoninvertibleTransformException e) {
			throw new RasterDriverException("Noninvertible transform");
		}
		Extent selectedExtent = new Extent(p1, p2);
		setView(selectedExtent);
		int[] stpBuffer = new int[]{0, 0 , bufWidth, bufHeight};

		//TODO: FUNCIONALIDAD: Implementar adjustToExtent = false

		loadBuffer(viewRequest, bufWidth, bufHeight, rasterBuf, bandList, stpBuffer);
		return rasterBuf;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.driver.RasterDataset#getWindowRaster(int, int, int, int, int, int, org.gvsig.fmap.driver.BandList, org.gvsig.fmap.driver.IBuffer)
	 */
	public IBuffer getWindowRaster(int x, int y, int w, int h, int bufWidth, int bufHeight, BandList bandList, IBuffer rasterBuf) throws InterruptedException, RasterDriverException {
		Point2D init = this.rasterToWorld(new Point2D.Double(x, y));
		Point2D end = this.rasterToWorld(new Point2D.Double(x + w, y + h));
		Extent selectedExtent = new Extent(init.getX(), init.getY(), end.getX(), end.getY());
		setView(selectedExtent);
		int[] stpBuffer = new int[]{0, 0 , bufWidth, bufHeight};

		loadBuffer(viewRequest, bufWidth, bufHeight, rasterBuf, bandList, stpBuffer);
		return rasterBuf;
	}

	/**
	 * Carga el buffer con las bandas RGB del raster con los parámetros especificados de extensión
	 * y tamaño de buffer. El problema de ecw es que solo podemos leer 3 bandas de una vez ya que solo disponemos
	 * de una llamada readLineRGBA. Para leer más bandas tendremos que hacer multiples llamadas a setView para leer
	 * 3 cada vez.
	 *
	 * Si por ejemplo tenemos un ecw de 6 bandas [0, 1, 2, 3, 4, 5] y queremos cargar un buffer con el siguiente orden
	 * [0, -, 2, -, 4, -] La variable readBandsFromECW hará la llamada a setView con los valores [0, 2, 4, 0, 0, 0]. La
	 * función drawRGB será la encargada de hacer el switch para obtener [0, -, 2, -, 4, -].
	 *
	 * Bug#1: Si se dispone de un ecw de más de tres bandas podemos llamar a setView con readBandsFromECW con el orden
	 * que queramos, por ejemplo [3, 2, 5, 1, 0] pero para ecw de 3 bandas la llamada con las bandas cambiadas no
	 * hace caso. El caso de readBandsFromECW = [2, 0, 1] será tomado siempre como [0, 1, 2].
	 *
	 * @param selectedExtent Extensión seleccionada
	 * @param bufWidth Ancho de buffer
	 * @param bufHeight Alto de buffer
	 * @param rasterBuf Buffer de datos
	 */
	private void loadBuffer(Extent selectedExtent, int bufWidth, int bufHeight, IBuffer rasterBuf, BandList bandList, int[] stpBuffer) throws InterruptedException, RasterDriverException {
		try{
			RasterTask task = RasterTaskQueue.get(Thread.currentThread().toString());

			//Leemos el raster desde la librería

			//Extent ext = getExtent();//new Extent(file.originX, file.originY, file.originX + (file.width * file.cellIncrementX), file.originY + (file.height * file.cellIncrementY));
			//selectedExtent = RasterUtilities.calculateAdjustedView(selectedExtent, ext);
			//selectedExtent = RasterUtilities.calculateAdjustedView(selectedExtent, this.getAffineTransform(), new Dimension(file.width, file.height));
			int[] readBandsFromECW = new int[Math.max(file.numBands, 3)];
			int[] readBands = new int[Math.max(file.numBands, 3)];


			for(int i = 0; i < readBandsFromECW.length; i ++)
				readBands[i] = -1;
			int cont = 0;
			for(int i = 0; i < file.numBands; i++) {
				int[] bandsToDraw = bandList.getBand(i).getBufferBandListToDraw();
				if(bandsToDraw != null) {
					for(int j = 0; j < bandsToDraw.length; j++){
						readBandsFromECW[cont] = i;
						readBands[cont] = i;
						cont ++;
					}
				}

			}

			if(task.getEvent() != null)
				task.manageEvent(task.getEvent());

			//Si el ancho y alto pedido no coincide con el estimado es que se intenta resamplear.
			//Otros drivers soportan el resampleo pero no es el caso. Hay que hacer la petición y luego remuestrear.

			/*if((selectedExtent.width()/file.cellIncrementX) || (selectedExtent.height()/file.cellIncrementY)) {

			}*/

			if(bufWidth > Math.round(Math.abs(selectedExtent.width()/file.cellIncrementX)))
				bufWidth = (int)Math.round(Math.abs(selectedExtent.width()/file.cellIncrementX));
			if(bufHeight > Math.round(Math.abs(selectedExtent.height()/file.cellIncrementY)))
				bufHeight = (int)Math.round(Math.abs(selectedExtent.height()/file.cellIncrementY));
			file.setView(file.numBands, readBandsFromECW, selectedExtent.minX(), selectedExtent.maxY(), selectedExtent.maxX(), selectedExtent.minY(), bufWidth, bufHeight);
			
			//Escribimos el raster sobre un IBuffer
			int[] pRGBArray = new int[bufWidth];
			drawRGB(rasterBuf, pRGBArray, readBandsFromECW, bandList, task);

		}catch(JNCSInvalidSetViewException exc){
			throw new RasterDriverException("Error setting coords");
		}catch (JNCSFileNotOpenException e) {
			throw new RasterDriverException("Error opening file");
		}catch (JNCSException ex) {
			throw new RasterDriverException("Error reading data");
		}

	}


	private void drawRGB(IBuffer rasterBuf, int[] pRGBArray, int[] readBands, BandList bandList, RasterTask task) throws JNCSException, InterruptedException {
		int bandR = readBands[0];
		int bandG = (readBands.length > 1) ? readBands[1] : -1;
		int bandB = (readBands.length > 2) ? readBands[2] : -1;

		//********* caso especial que resuelve Bug#1 **********************
		if(file.numBands == 3 && bandList.getDrawableBandsCount() < 3) {
			for(int i = 0; i < 3; i ++) {
				int[] b = bandList.getBand(i).getBufferBandListToDraw();
				if(b != null){
					bandG = 1; bandR = 0; bandB = 2;
				}
			}
		}
		if(file.numBands == 3 && bandR == bandG && bandG == bandB) { //caso especial que resuelve Bug#1
			for(int i = 0; i < 3; i ++) {
				int[] b = bandList.getBand(i).getBufferBandListToDraw();
				if(b != null) {
					if(i == 0) {
						for (int line = 0; line < rasterBuf.getHeight(); line++) {
							file.readLineRGBA(pRGBArray);
							for(int col = 0; col < pRGBArray.length; col ++){
								rasterBuf.setElem(line, col, bandR, (byte)((pRGBArray[col] & 0x00ff0000) >> 16));
								rasterBuf.setElem(line, col, bandG, (byte)((pRGBArray[col] & 0x00ff0000) >> 16));
								rasterBuf.setElem(line, col, bandB, (byte)((pRGBArray[col] & 0x00ff0000) >> 16));
							}
						}
						return;
					}
					if(i == 1) {
						for (int line = 0; line < rasterBuf.getHeight(); line++) {
							file.readLineRGBA(pRGBArray);
							for(int col = 0; col < pRGBArray.length; col ++){
								rasterBuf.setElem(line, col, bandR, (byte)((pRGBArray[col] & 0x0000ff00) >> 8));
								rasterBuf.setElem(line, col, bandG, (byte)((pRGBArray[col] & 0x0000ff00) >> 8));
								rasterBuf.setElem(line, col, bandB, (byte)((pRGBArray[col] & 0x0000ff00) >> 8));
							}
						}
						return;
					}
					if(i == 2) {
						for (int line = 0; line < rasterBuf.getHeight(); line++) {
							file.readLineRGBA(pRGBArray);
							for(int col = 0; col < pRGBArray.length; col ++){
								rasterBuf.setElem(line, col, bandR, (byte)(pRGBArray[col] & 0x000000ff));
								rasterBuf.setElem(line, col, bandG, (byte)(pRGBArray[col] & 0x000000ff));
								rasterBuf.setElem(line, col, bandB, (byte)(pRGBArray[col] & 0x000000ff));
							}
						}
						return;
					}
				}
				if(task.getEvent() != null)
					task.manageEvent(task.getEvent());
			}

		}
		//********* END caso especial que resuelve Bug#1 **********************

		if(bandR >= 0 && bandG >= 0 && bandB >= 0) {
			for (int line = 0; line < rasterBuf.getHeight(); line++) {
				file.readLineRGBA(pRGBArray);
				for(int col = 0; col < pRGBArray.length; col ++){
					rasterBuf.setElem(line, col, bandR, (byte)((pRGBArray[col] & 0x00ff0000) >> 16));
					rasterBuf.setElem(line, col, bandG, (byte)((pRGBArray[col] & 0x0000ff00) >> 8));
					rasterBuf.setElem(line, col, bandB, (byte)(pRGBArray[col] & 0x000000ff));
				}
			}
			return;
		}

		if(task.getEvent() != null)
			task.manageEvent(task.getEvent());

		if(bandR >= 0 && bandG >= 0) {
			for (int line = 0; line < rasterBuf.getHeight(); line++) {
				file.readLineRGBA(pRGBArray);
				for(int col = 0; col < pRGBArray.length; col ++){
					rasterBuf.setElem(line, col, bandR, (byte)((pRGBArray[col] & 0x00ff0000) >> 16));
					rasterBuf.setElem(line, col, bandG, (byte)((pRGBArray[col] & 0x0000ff00) >> 8));
				}
			}
			return;
		}

		if(task.getEvent() != null)
			task.manageEvent(task.getEvent());

		if(bandR >= 0){
			for (int line = 0; line < rasterBuf.getHeight(); line++) {
				file.readLineRGBA(pRGBArray);
				for(int col = 0; col < pRGBArray.length; col ++)
					rasterBuf.setElem(line, col, bandR, (byte)((pRGBArray[col] & 0x00ff0000) >> 16));
			}
			return;
		}

		if(task.getEvent() != null)
			task.manageEvent(task.getEvent());

	}

	public void reProject(ICoordTrans rp) {
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.RasterDataset#getBlockSize()
	 */
	public int getBlockSize() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.driver.RasterDataset#readCompletetLine(int, int)
	 */
	public Object readCompleteLine(int line, int band) throws InvalidSetViewException, FileNotOpenException, RasterDriverException {
		if(line < 0 || line >= file.height || band < 0 || band >= getBandCount())
			throw new InvalidSetViewException("Request out of grid");

		Point2D begin = rasterToWorld(new Point2D.Double(0, line));
		Point2D end = rasterToWorld(new Point2D.Double(file.width, line + 1));
		int[] readBandsFromECW = new int[file.numBands];
		if(file.numBands <= 3) {
			for(int i = 0; i < file.numBands; i++)
				readBandsFromECW[i] = i;
		}else {
			readBandsFromECW[0] = band;
		}

		Extent e = new Extent(begin.getX(), begin.getY(), end.getX(), end.getY());

		try {
			int[] value = new int[file.width];
			file.setView(file.numBands, readBandsFromECW, e.minX(), e.maxY(), e.maxX(), e.minY(), file.width, 1);
			file.readLineRGBA(value);

			if(file.numBands <= 3) {
				switch(getDataType()[0]) {
				case IBuffer.TYPE_BYTE: byte[] b = new byte[file.width];
										switch(band) {
										case 0: for(int i = 0; i < file.width; i ++)
													b[i] = (byte)(((value[i] & 0x00ff0000) >> 16) & 0xff);
												break;
										case 1: for(int i = 0; i < file.width; i ++)
													b[i] = (byte)(((value[i] & 0x0000ff00) >> 8) & 0xff);
												break;
										case 2: for(int i = 0; i < file.width; i ++)
													b[i] = (byte)((value[i] & 0x000000ff) & 0xff);
												break;
										}
										return b;
				}
			}else {
				switch(getDataType()[0]) {
				case IBuffer.TYPE_BYTE: byte[] b = new byte[file.width];
										for(int i = 0; i < file.width; i ++)
											b[i] = (byte)(((value[i] & 0x00ff0000) >> 16) & 0xff);
										break;
				}
			}
			//TODO: FUNCIONALIDAD: Ecw con otro tipo de dato != Byte
		} catch (JNCSFileNotOpenException e1) {
			throw new FileNotOpenException("Error en jecw: JNCSFileNotOpenException");
		} catch (JNCSInvalidSetViewException e1) {
			throw new FileNotOpenException("Error en jecw: JNCSInvalidSetViewException");
		} catch (JNCSException e1) {
			throw new RasterDriverException("Error la lectura de datos ecw");
		}

		return null;
	}

	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.raster.dataset.RasterDataset#readBlock(int, int, int)
	 */
	public Object readBlock(int pos, int blockHeight) throws InvalidSetViewException, FileNotOpenException, RasterDriverException, InterruptedException {
		RasterTask task = RasterTaskQueue.get(Thread.currentThread().toString());
		if(pos < 0)
			throw new InvalidSetViewException("Request out of grid");

		if((pos + blockHeight) > file.height)
			blockHeight = Math.abs(file.height - pos);

		Point2D begin = rasterToWorld(new Point2D.Double(0, pos));
		Point2D end = rasterToWorld(new Point2D.Double(file.width, pos + blockHeight));
		int[] readBandsFromECW = new int[file.numBands];

		for(int i = 0; i < file.numBands; i++)
			readBandsFromECW[i] = i;

		byte[][][] buf = new byte[file.numBands][blockHeight][file.width];
		Extent e = new Extent(begin.getX(), begin.getY(), end.getX(), end.getY());
		e = RasterUtilities.calculateAdjustedView(getExtent(), e);

		try {
			int[] value = new int[file.width];
			file.setView(file.numBands, readBandsFromECW, e.minX(), e.maxY(), e.maxX(), e.minY(), file.width, blockHeight);

			if(file.numBands <= 3) {
				for (int row = 0; row < blockHeight; row++) {
					file.readLineRGBA(value);
					switch(getDataType()[0]) {
					case IBuffer.TYPE_BYTE:
						for(int col = 0; col < file.width; col ++) {
							buf[0][row][col] = (byte)(((value[col] & 0x00ff0000) >> 16) & 0xff);
							buf[1][row][col] = (byte)(((value[col] & 0x0000ff00) >> 8) & 0xff);
							buf[2][row][col] = (byte)((value[col] & 0x000000ff) & 0xff);
						}
						break;
					}
				}

				if(task.getEvent() != null)
					task.manageEvent(task.getEvent());

			} else {
				//TODO: FUNCIONALIDAD: file.numBands > 3
			}

			//TODO: FUNCIONALIDAD: Ecw con otro tipo de dato != Byte
		} catch (JNCSFileNotOpenException e1) {
			throw new FileNotOpenException("Error en jecw: JNCSFileNotOpenException");
		} catch (JNCSInvalidSetViewException e1) {
			throw new FileNotOpenException("Error en jecw: JNCSInvalidSetViewException");
		} catch (JNCSException e1) {
			throw new RasterDriverException("Error la lectura de datos ecw");
		}

		return buf;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.driver.RasterDataset#getData(int, int, int)
	 */
	public Object getData(int x, int y, int band)throws InvalidSetViewException, FileNotOpenException, RasterDriverException {
		if(x < 0 || y < 0 || x >= file.width || y >= file.height)
			throw new InvalidSetViewException("Request out of grid");

		Point2D begin = new Point2D.Double(x, y);
		Point2D end = new Point2D.Double(x + 1, y + 1);

		ownTransformation.transform(begin, begin);
		ownTransformation.transform(end, end);

		int[] readBandsFromECW = new int[file.numBands];
		if(file.numBands <= 3){
			for(int i = 0; i < file.numBands; i++)
				readBandsFromECW[i] = i;
		}else{
			readBandsFromECW[0] = band;
		}

		Extent e = new Extent(begin.getX(), begin.getY(), end.getX(), end.getY());
		try {
			int[] value = new int[1];
			file.setView(file.numBands, readBandsFromECW, e.minX(), e.maxY(), e.maxX(), e.minY(), 1, 1);
			file.readLineRGBA(value);
			if(file.numBands <= 3){
				switch(band){
				case 0: return new Integer((((value[0] & 0x00ff0000) >> 16) & 0xffffffff));
				case 1: return new Integer((((value[0] & 0x0000ff00) >> 8) & 0xffffffff));
				case 2: return new Integer((((value[0] & 0x000000ff)) & 0xffffffff));
				}
			}
			return new Integer((((value[0] & 0x00ff0000) >> 16) & 0xffffffff));
		} catch (JNCSFileNotOpenException e1) {
			throw new FileNotOpenException("Error en jecw: JNCSFileNotOpenException");
		} catch (JNCSInvalidSetViewException e1) {
			throw new FileNotOpenException("Error en jecw: JNCSInvalidSetViewException");
		} catch (JNCSException e1) {
			throw new RasterDriverException("Error reading ecw data");
		}
	}

	public void refreshUpdate(int arg0, int arg1, double arg2, double arg3, double arg4, double arg5) {
	}

	public void refreshUpdate(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5) {
	}

	/**
	 * Obtiene el objeto que contiene que contiene la interpretación de
	 * color por banda
	 * @return
	 */
	public DatasetColorInterpretation getColorInterpretation(){
		if(colorInterpr == null) {
			colorInterpr = new DatasetColorInterpretation();
			colorInterpr.initColorInterpretation(getBandCount());
			if(getBandCount() == 1)
				colorInterpr.setColorInterpValue(0, DatasetColorInterpretation.GRAY_BAND);
			if(getBandCount() >= 3) {
				colorInterpr.setColorInterpValue(0, DatasetColorInterpretation.RED_BAND);
				colorInterpr.setColorInterpValue(1, DatasetColorInterpretation.GREEN_BAND);
				colorInterpr.setColorInterpValue(2, DatasetColorInterpretation.BLUE_BAND);
			}
		}
		return colorInterpr;
	}

	/**
	 * Asigna el objeto que contiene que contiene la interpretación de
	 * color por banda
	 * @param DatasetColorInterpretation
	 */
	public void setColorInterpretation(DatasetColorInterpretation colorInterpretation){
		this.colorInterpretation = colorInterpretation;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.RasterDataset#getOverviewCount(int)
	 */
	public int getOverviewCount(int band) throws BandAccessException, RasterDriverException {
		if(band >= getBandCount())
			throw new BandAccessException("Wrong band");
		return 0;
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
	 * @see org.gvsig.raster.dataset.RasterDataset#getOverviewWidth(int, int)
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
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.driver.GeoData#getStringProjection()
	 */
	public String getStringProjection() throws RasterDriverException{
		return file.projection;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.driver.GeoData#getStringProjection()
	 */
	public String getWktProjection() {
		//System.err.println("======>" + file.projection);
		return null;
	}
}