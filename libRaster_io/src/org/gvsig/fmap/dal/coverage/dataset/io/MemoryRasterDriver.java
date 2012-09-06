/*
 * Cresques Mapping Suite. Graphic Library for constructing mapping applications.
 *
 * Copyright (C) 2004-5.
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
 *
 * For more information, contact:
 *
 * cresques@gmail.com
 */
package org.gvsig.fmap.dal.coverage.dataset.io;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

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
import org.gvsig.raster.datastruct.Extent;
import org.gvsig.raster.datastruct.Transparency;
import org.gvsig.raster.util.RasterUtilities;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.extensionpoint.ExtensionPoint;
import org.gvsig.tools.extensionpoint.ExtensionPointManager;

/**
 * Driver para datos cargados en un objeto IBuffer
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class MemoryRasterDriver extends RasterDataset {

	private Extent           v = null;
	protected IBuffer        buffer = null;
	private Extent 		 extent = null;

	/**
	 * Constructor
	 */
	public MemoryRasterDriver() {}

	/**
	 * Estado de transparencia del raster.
	 */
	protected Transparency   fileTransparency = null;

	public static void register() {
		ExtensionPointManager extensionPoints =ToolsLocator.getExtensionPointManager();
		ExtensionPoint point=extensionPoints.get("RasterReader");
		point.append(new MemoryRasterDriverParam().getFormatID(), "", MemoryRasterDriver.class);
	}

	/**
	 * Constructor. Asigna el buffer de datos y la extensión
	 * @param proj Proyección
	 * @param buf Buffer
	 * @throws NotSupportedExtensionException
	 */
	public MemoryRasterDriver(IProjection proj, Object buf)throws NotSupportedExtensionException {
		super(null, null);
		setParam(buf);
		if(!(buf instanceof MemoryRasterDriverParam))
			throw new NotSupportedExtensionException("Buffer not supported");

		extent = ((MemoryRasterDriverParam)buf).getExtent();
		this.buffer = ((MemoryRasterDriverParam)buf).getBuffer();

		if(extent != null) {
			double psX = (extent.maxX() - extent.minX()) / buffer.getWidth();
			double psY = (extent.minY() - extent.maxY()) / buffer.getHeight();
			ownTransformation = new AffineTransform(psX, 0, 0, psY, extent.minX(), extent.maxY());
		} else
			ownTransformation = new AffineTransform(1, 0, 0, -1, 0, buffer.getHeight());

		if(buffer == null)
			throw new NotSupportedExtensionException("Buffer invalid");

		load();
		bandCount = buffer.getBandCount();

		//Obtenemos el tipo de dato de gdal y lo convertimos el de RasterBuf
		int[] dt = new int[buffer.getBandCount()];
		for (int i = 0; i < dt.length; i++)
			dt[i] = buffer.getDataType();
		setDataType(dt);
	}

	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.raster.dataset.GeoInfo#load()
	 */
	public GeoInfo load() {
		return this;
	}

	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.raster.dataset.GeoInfo#close()
	 */
	public void close() {
		buffer = null;
	}

	/**
	 * Asigna el extent de la vista actual.
	 */
	public void setView(Extent e) {
		v = e;
	}

	/**
	 * Obtiene extent de la vista actual
	 */
	public Extent getView() {
		return v;
	}

	/**
	 * Obtiene la anchura del fichero
	 */
	public int getWidth() {
		return buffer.getWidth();
	}

	/**
	 * Obtiene la altura del fichero
	 */
	public int getHeight() {
		return buffer.getHeight();
	}

	/*
	 *  (non-Javadoc)
	 * @see org.cresques.geo.Projected#reProject(org.cresques.cts.ICoordTrans)
	 */
	public void reProject(ICoordTrans rp) {
	}

	/**
	 * Obtiene la orientación de la imagen a partir del signo del tamaño de pixel para poder
	 * asignarlo en el setView. Esto es util para poder conocer como debe leerse la image,
	 * de abajo a arriba, de arriba a abajo, de izquierda a derecha o de derecha a izquierda.
	 * La posición habitual es la que el pixel size en X es positivo y en Y negativo leyendose
	 * en este caso las X de menor a mayor y las Y de mayor a menor. Los casos posibles son:
	 * <UL>
	 * <LI><B>X > 0; Y < 0;</B> {true, false}</LI>
	 * <LI><B>X > 0; Y > 0;</B> {true, true}</LI>
	 * <LI><B>X < 0; Y > 0;</B> {false, true}</LI>
	 * <LI><B>X < 0; Y < 0;</B> {false, false}</LI>
	 * </UL>
	 *
	 * @return
	 */
	/*private boolean[] getOrientation(){
		boolean[] orientation = {true, false};
		return orientation;
	}*/

	/* (non-Javadoc)
	 * @see org.cresques.io.GeoRasterFile#getData(int, int, int)
	 */
	public Object getData(int x, int y, int band) {
		if(buffer.getDataType() == IBuffer.TYPE_BYTE){
			return new Integer(buffer.getElemByte(y, x, band));
		}else if(buffer.getDataType() == IBuffer.TYPE_SHORT){
			return new Integer(buffer.getElemShort(y, x, band));
		}else if(buffer.getDataType() == IBuffer.TYPE_INT){
			return new Integer(buffer.getElemInt(y, x, band));
		}else if(buffer.getDataType() == IBuffer.TYPE_FLOAT){
			return new Float(buffer.getElemFloat(y, x, band));
		}else if(buffer.getDataType() == IBuffer.TYPE_DOUBLE){
			return new Double(buffer.getElemDouble(y, x, band));
		}
		return null;
	}


	/**
	 * Devuelve el tamaño de bloque
	 * @return Tamaño de bloque
	 */
	public int getBlockSize(){
		return 0;
	}

	/**
	 * Obtiene el flag que dice si la imagen está o no georreferenciada
	 * @return true si está georreferenciada y false si no lo está.
	 */
	public boolean isGeoreferenced() {
		return (this.extent != null);
	}

	/**
	 * Informa de si el driver ha supersampleado en el último dibujado. Es el driver el que colocará
	 * el valor de esta variable cada vez que dibuja.
	 * @return true si se ha supersampleado y false si no se ha hecho.
	 */
	public boolean isSupersampling() {
		return false;
	}

	/**
	 * @return Returns the dataType.
	 */
	public int[] getDataType() {
		int[] dt = new int[buffer.getBandCount()];
		for (int i = 0; i < dt.length; i++)
			dt[i] = buffer.getDataType();
		return dt;
	}

	/**
	 * Ajusta los puntos pasados por parámetro a los límites del buffer. Es decir si alguno excede
	 * los límites por arriba o por abajo los ajusta.
	 * @param begin Punto inicial
	 * @param end Punto final
	 */
	private void adjustPointsToBufferLimits(Point2D begin, Point2D end) {
		if(begin.getX() < 0)
			begin.setLocation(0, begin.getY());
		if(begin.getY() > buffer.getHeight())
			begin.setLocation(begin.getX(), buffer.getHeight());
		if(end.getY() < 0)
			end.setLocation(begin.getX(), 0);
		if(end.getX() > buffer.getWidth())
			begin.setLocation(buffer.getWidth(), begin.getY());
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.RasterDataset#getWindowRaster(double, double, double, double, org.gvsig.raster.dataset.BandList, org.gvsig.raster.dataset.IBuffer)
	 */
	public IBuffer getWindowRaster(double ulx, double uly, double lrx, double lry, BandList bandList, IBuffer rasterBuf) {
		Point2D begin = worldToRaster(new Point2D.Double(ulx, uly));
		Point2D end = worldToRaster(new Point2D.Double(lrx, lry));
		setView(new Extent(ulx, uly, lrx, lry));

		adjustPointsToBufferLimits(begin, end);

		switch(buffer.getDataType()){
		case IBuffer.TYPE_BYTE: writeByteBuffer(rasterBuf, 1, 1, begin, bandList); break;
		case IBuffer.TYPE_SHORT: writeShortBuffer(rasterBuf, 1, 1, begin, bandList); break;
		case IBuffer.TYPE_INT: writeIntBuffer(rasterBuf, 1, 1, begin, bandList); break;
		case IBuffer.TYPE_FLOAT: writeFloatBuffer(rasterBuf, 1, 1, begin, bandList); break;
		case IBuffer.TYPE_DOUBLE: writeDoubleBuffer(rasterBuf, 1, 1, begin, bandList); break;
		}
		return rasterBuf;
	}

	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.raster.dataset.RasterDataset#getWindowRaster(double, double, double, double, org.gvsig.raster.dataset.BandList, org.gvsig.raster.dataset.IBuffer, boolean)
	 */
	public IBuffer getWindowRaster(double x, double y, double w, double h, BandList bandList, IBuffer rasterBuf, boolean adjustToExtent) {
		Point2D begin = worldToRaster(new Point2D.Double(x, y));
		Point2D end = worldToRaster(new Point2D.Double(x + w, y - h));
		setView(new Extent(x, y, x + w, y - h));

		adjustPointsToBufferLimits(begin, end);

		switch(buffer.getDataType()){
		case IBuffer.TYPE_BYTE: writeByteBuffer(rasterBuf, 1, 1, begin, bandList); break;
		case IBuffer.TYPE_SHORT: writeShortBuffer(rasterBuf, 1, 1, begin, bandList); break;
		case IBuffer.TYPE_INT: writeIntBuffer(rasterBuf, 1, 1, begin, bandList); break;
		case IBuffer.TYPE_FLOAT: writeFloatBuffer(rasterBuf, 1, 1, begin, bandList); break;
		case IBuffer.TYPE_DOUBLE: writeDoubleBuffer(rasterBuf, 1, 1, begin, bandList); break;
		}
		return rasterBuf;
	}

	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.raster.dataset.RasterDataset#getWindowRaster(double, double, double, double, int, int, org.gvsig.raster.dataset.BandList, org.gvsig.raster.dataset.IBuffer, boolean)
	 */
	public IBuffer getWindowRaster(double minX, double minY, double maxX, double maxY, int bufWidth, int bufHeight, BandList bandList, IBuffer rasterBuf, boolean adjustToExtent) {
		Point2D begin = worldToRaster(new Point2D.Double(minX, maxY));
		Point2D end = worldToRaster(new Point2D.Double(maxX, minY));
		setView(new Extent(minX, minY, maxX, maxY));

		adjustPointsToBufferLimits(begin, end);

		//Ancho y alto en pixels (double) del area seleccionada.
		double w = Math.abs(end.getX() - begin.getX());
		double h = Math.abs(end.getY() - begin.getY());

		//Relación entre el número de pixels del buffer origen (area seleccionada) y el destino
		double stepX = w / ((double)bufWidth);
		double stepY = h / ((double)bufHeight);

		//Escritura separada en 5 llamadas para mejorar el rendimiento
		switch(buffer.getDataType()){
		case IBuffer.TYPE_BYTE: writeByteBuffer(rasterBuf, stepX, stepY, begin, bandList); break;
		case IBuffer.TYPE_SHORT: writeShortBuffer(rasterBuf, stepX, stepY, begin, bandList); break;
		case IBuffer.TYPE_INT: writeIntBuffer(rasterBuf, stepX, stepY, begin, bandList); break;
		case IBuffer.TYPE_FLOAT: writeFloatBuffer(rasterBuf, stepX, stepY, begin, bandList); break;
		case IBuffer.TYPE_DOUBLE: writeDoubleBuffer(rasterBuf, stepX, stepY, begin, bandList); break;
		}

		/*int xPx = 0, yPx = 0;
		for (int iBand = 0; iBand < rasterBuf.getBandCount(); iBand++) {
			yPx = 0;
			for(double row = begin.getY(); yPx < bufHeight; row += stepY) {
				xPx = 0;
				for(double col = begin.getX(); xPx < bufWidth; col += stepX) {
					switch(buffer.getDataType()){
					case IBuffer.TYPE_BYTE: rasterBuf.setElem(yPx, xPx, iBand, buffer.getElemByte((int)row, (int)col, iBand)); break;
					case IBuffer.TYPE_SHORT: rasterBuf.setElem(yPx, xPx, iBand, buffer.getElemShort((int)row, (int)col, iBand)); break;
					case IBuffer.TYPE_INT: rasterBuf.setElem(yPx, xPx, iBand, buffer.getElemInt((int)row, (int)col, iBand)); break;
					case IBuffer.TYPE_FLOAT: rasterBuf.setElem(yPx, xPx, iBand, buffer.getElemFloat((int)row, (int)col, iBand)); break;
					case IBuffer.TYPE_DOUBLE: rasterBuf.setElem(yPx, xPx, iBand, buffer.getElemDouble((int)row, (int)col, iBand)); break;
					}
					xPx ++;
				}
				yPx ++;
			}
		}*/
		return rasterBuf;
	}

	/**
	 * Escribe sobre el buffer pasado por parámetro los valores solicitados, desde el buffer de la clase. Los valores
	 * se solicitan a través de los parámetros. En ellos se especifica el tamaño del buffer de destino, las bandas a
	 * escribir, el punto inicial en coordenadas pixel (double) y el incremento.
	 * @param rasterBuf Buffer donde se escriben los datos
	 * @param stepX Incremento en X. Cada vez que se escribe un pixel en X se incrementa el contador en stepX pixels. Esto es necesario
	 * ya que el buffer destino no tiene porque tener el mismo ancho que el de origen. Este valor suele ser ancho_buffer_origen / ancho_buffer_destino.
	 * @param stepY Incremento en Y. Cada vez que se escribe un pixel en Y se incrementa el contador en stepY pixels. Esto es necesario
	 * ya que el buffer destino no tiene porque tener el mismo alto que el de origen. Este valor suele ser alto_buffer_origen / alto_buffer_destino.
	 * @param begin pixel donde se comienza a leer en el buffer de origen. Este valor es decimal ya que no tiene porque empezar a leerse al principio
	 * del pixel. Esto es util cuando se supersamplea.
	 */
	private void writeByteBuffer(IBuffer rasterBuf, double stepX, double stepY, Point2D begin, BandList bandList) {
		int xPx = 0, yPx = 0;
		for (int iBand = 0; iBand < buffer.getBandCount(); iBand++) {
			int[] drawableBands = bandList.getBufferBandToDraw(this.getFName(), iBand);
			if(drawableBands == null || (drawableBands.length == 1 && drawableBands[0] == -1))
				continue;
			for(int drawBands = 0; drawBands < drawableBands.length; drawBands++) {
				yPx = 0;
				for(double row = begin.getY(); (yPx < rasterBuf.getHeight() && row < buffer.getHeight()); row += stepY) {
					xPx = 0;
					for(double col = begin.getX(); (xPx < rasterBuf.getWidth() && col < buffer.getWidth()); col += stepX) {
						rasterBuf.setElem(yPx, xPx, iBand, buffer.getElemByte((int)row, (int)col, iBand));
						xPx ++;
					}
					yPx ++;
				}
			}
		}
	}

	/**
	 * Escribe sobre el buffer pasado por parámetro los valores solicitados, desde el buffer de la clase. Los valores
	 * se solicitan a través de los parámetros. En ellos se especifica el tamaño del buffer de destino, las bandas a
	 * escribir, el punto inicial en coordenadas pixel (double) y el incremento.
	 * @param rasterBuf Buffer donde se escriben los datos
	 * @param stepX Incremento en X. Cada vez que se escribe un pixel en X se incrementa el contador en stepX pixels. Esto es necesario
	 * ya que el buffer destino no tiene porque tener el mismo ancho que el de origen. Este valor suele ser ancho_buffer_origen / ancho_buffer_destino.
	 * @param stepY Incremento en Y. Cada vez que se escribe un pixel en Y se incrementa el contador en stepY pixels. Esto es necesario
	 * ya que el buffer destino no tiene porque tener el mismo alto que el de origen. Este valor suele ser alto_buffer_origen / alto_buffer_destino.
	 * @param begin pixel donde se comienza a leer en el buffer de origen. Este valor es decimal ya que no tiene porque empezar a leerse al principio
	 * del pixel. Esto es util cuando se supersamplea.
	 */
	private void writeShortBuffer(IBuffer rasterBuf, double stepX, double stepY, Point2D begin, BandList bandList) {
		int xPx = 0, yPx = 0;
		for (int iBand = 0; iBand < buffer.getBandCount(); iBand++) {
			int[] drawableBands = bandList.getBufferBandToDraw(this.getFName(), iBand);
			if(drawableBands == null || (drawableBands.length == 1 && drawableBands[0] == -1))
				continue;
			for(int drawBands = 0; drawBands < drawableBands.length; drawBands++) {
				yPx = 0;
				for(double row = begin.getY(); yPx < rasterBuf.getHeight(); row += stepY) {
					xPx = 0;
					for(double col = begin.getX(); xPx < rasterBuf.getWidth(); col += stepX) {
						rasterBuf.setElem(yPx, xPx, iBand, buffer.getElemShort((int)row, (int)col, iBand));
						xPx ++;
					}
					yPx ++;
				}
			}
		}
	}

	/**
	 * Escribe sobre el buffer pasado por parámetro los valores solicitados, desde el buffer de la clase. Los valores
	 * se solicitan a través de los parámetros. En ellos se especifica el tamaño del buffer de destino, las bandas a
	 * escribir, el punto inicial en coordenadas pixel (double) y el incremento.
	 * @param rasterBuf Buffer donde se escriben los datos
	 * @param stepX Incremento en X. Cada vez que se escribe un pixel en X se incrementa el contador en stepX pixels. Esto es necesario
	 * ya que el buffer destino no tiene porque tener el mismo ancho que el de origen. Este valor suele ser ancho_buffer_origen / ancho_buffer_destino.
	 * @param stepY Incremento en Y. Cada vez que se escribe un pixel en Y se incrementa el contador en stepY pixels. Esto es necesario
	 * ya que el buffer destino no tiene porque tener el mismo alto que el de origen. Este valor suele ser alto_buffer_origen / alto_buffer_destino.
	 * @param begin pixel donde se comienza a leer en el buffer de origen. Este valor es decimal ya que no tiene porque empezar a leerse al principio
	 * del pixel. Esto es util cuando se supersamplea.
	 */
	private void writeIntBuffer(IBuffer rasterBuf, double stepX, double stepY, Point2D begin, BandList bandList) {
		int xPx = 0, yPx = 0;
		for (int iBand = 0; iBand < buffer.getBandCount(); iBand++) {
			int[] drawableBands = bandList.getBufferBandToDraw(this.getFName(), iBand);
			if(drawableBands == null || (drawableBands.length == 1 && drawableBands[0] == -1))
				continue;
			for(int drawBands = 0; drawBands < drawableBands.length; drawBands++) {
				yPx = 0;
				for(double row = begin.getY(); yPx < rasterBuf.getHeight(); row += stepY) {
					xPx = 0;
					for(double col = begin.getX(); xPx < rasterBuf.getWidth(); col += stepX) {
						rasterBuf.setElem(yPx, xPx, iBand, buffer.getElemInt((int)row, (int)col, iBand));
						xPx ++;
					}
					yPx ++;
				}
			}
		}
	}

	/**
	 * Escribe sobre el buffer pasado por parámetro los valores solicitados, desde el buffer de la clase. Los valores
	 * se solicitan a través de los parámetros. En ellos se especifica el tamaño del buffer de destino, las bandas a
	 * escribir, el punto inicial en coordenadas pixel (double) y el incremento.
	 * @param rasterBuf Buffer donde se escriben los datos
	 * @param stepX Incremento en X. Cada vez que se escribe un pixel en X se incrementa el contador en stepX pixels. Esto es necesario
	 * ya que el buffer destino no tiene porque tener el mismo ancho que el de origen. Este valor suele ser ancho_buffer_origen / ancho_buffer_destino.
	 * @param stepY Incremento en Y. Cada vez que se escribe un pixel en Y se incrementa el contador en stepY pixels. Esto es necesario
	 * ya que el buffer destino no tiene porque tener el mismo alto que el de origen. Este valor suele ser alto_buffer_origen / alto_buffer_destino.
	 * @param begin pixel donde se comienza a leer en el buffer de origen. Este valor es decimal ya que no tiene porque empezar a leerse al principio
	 * del pixel. Esto es util cuando se supersamplea.
	 */
	private void writeFloatBuffer(IBuffer rasterBuf, double stepX, double stepY, Point2D begin, BandList bandList) {
		int xPx = 0, yPx = 0;
		for (int iBand = 0; iBand < buffer.getBandCount(); iBand++) {
			int[] drawableBands = bandList.getBufferBandToDraw(this.getFName(), iBand);
			if(drawableBands == null || (drawableBands.length == 1 && drawableBands[0] == -1))
				continue;
			for(int drawBands = 0; drawBands < drawableBands.length; drawBands++) {
				yPx = 0;
				for(double row = begin.getY(); yPx < rasterBuf.getHeight(); row += stepY) {
					xPx = 0;
					for(double col = begin.getX(); xPx < rasterBuf.getWidth(); col += stepX) {
						rasterBuf.setElem(yPx, xPx, iBand, buffer.getElemFloat((int)row, (int)col, iBand));
						xPx ++;
					}
					yPx ++;
				}
			}
		}
	}

	/**
	 * Escribe sobre el buffer pasado por parámetro los valores solicitados, desde el buffer de la clase. Los valores
	 * se solicitan a través de los parámetros. En ellos se especifica el tamaño del buffer de destino, las bandas a
	 * escribir, el punto inicial en coordenadas pixel (double) y el incremento.
	 * @param rasterBuf Buffer donde se escriben los datos
	 * @param stepX Incremento en X. Cada vez que se escribe un pixel en X se incrementa el contador en stepX pixels. Esto es necesario
	 * ya que el buffer destino no tiene porque tener el mismo ancho que el de origen. Este valor suele ser ancho_buffer_origen / ancho_buffer_destino.
	 * @param stepY Incremento en Y. Cada vez que se escribe un pixel en Y se incrementa el contador en stepY pixels. Esto es necesario
	 * ya que el buffer destino no tiene porque tener el mismo alto que el de origen. Este valor suele ser alto_buffer_origen / alto_buffer_destino.
	 * @param begin pixel donde se comienza a leer en el buffer de origen. Este valor es decimal ya que no tiene porque empezar a leerse al principio
	 * del pixel. Esto es util cuando se supersamplea.
	 */
	private void writeDoubleBuffer(IBuffer rasterBuf, double stepX, double stepY, Point2D begin, BandList bandList) {
		int xPx = 0, yPx = 0;
		for (int iBand = 0; iBand < buffer.getBandCount(); iBand++) {
			int[] drawableBands = bandList.getBufferBandToDraw(this.getFName(), iBand);
			if(drawableBands == null || (drawableBands.length == 1 && drawableBands[0] == -1))
				continue;
			for(int drawBands = 0; drawBands < drawableBands.length; drawBands++) {
				yPx = 0;
				for(double row = begin.getY(); yPx < rasterBuf.getHeight(); row += stepY) {
					xPx = 0;
					for(double col = begin.getX(); xPx < rasterBuf.getWidth(); col += stepX) {
						rasterBuf.setElem(yPx, xPx, iBand, buffer.getElemDouble((int)row, (int)col, iBand));
						xPx ++;
					}
					yPx ++;
				}
			}
		}
	}

	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.raster.dataset.RasterDataset#getWindowRaster(int, int, int, int, org.gvsig.raster.dataset.BandList, org.gvsig.raster.dataset.IBuffer)
	 */
	public IBuffer getWindowRaster(int x, int y, int w, int h, BandList bandList, IBuffer rasterBuf) {
		setView(
				new Extent( RasterUtilities.getMapRectFromPxRect(getExtent().toRectangle2D(),
							getWidth(),
							getHeight(),
							new Rectangle2D.Double(x, y, w, h)))
				);

		for(int iBand = 0; iBand < buffer.getBandCount(); iBand ++){
			int[] drawableBands = bandList.getBufferBandToDraw(this.getFName(), iBand);
			if(drawableBands == null || (drawableBands.length == 1 && drawableBands[0] == -1))
				continue;
			if(buffer.getDataType() == IBuffer.TYPE_BYTE) {
				for(int drawBands = 0; drawBands < drawableBands.length; drawBands++) {
					for(int line = y; line < (y + h); line ++)
						for(int col = x; col < (x + w); col ++)
							rasterBuf.setElem((line - y), (col - x), drawableBands[drawBands], buffer.getElemByte(line, col, drawableBands[drawBands]));
				}
			}else if(buffer.getDataType() == IBuffer.TYPE_SHORT){
				for(int drawBands = 0; drawBands < drawableBands.length; drawBands++){
					for(int line = y; line < (y + h); line ++)
						for(int col = x; col < (x + w); col ++)
							rasterBuf.setElem((line - y), (col - x), drawableBands[drawBands], buffer.getElemShort(line, col, drawableBands[drawBands]));
				}
			}else if(buffer.getDataType() == IBuffer.TYPE_INT){
				for(int drawBands = 0; drawBands < drawableBands.length; drawBands++){
					for(int line = y; line < (y + h); line ++)
						for(int col = x; col < (x + w); col ++)
							rasterBuf.setElem((line - y), (col - x), drawableBands[drawBands], buffer.getElemInt(line, col, drawableBands[drawBands]));
				}
			}else if(buffer.getDataType() == IBuffer.TYPE_FLOAT){
				for(int drawBands = 0; drawBands < drawableBands.length; drawBands++){
					for(int line = y; line < (y + h); line ++)
						for(int col = x; col < (x + w); col ++)
							rasterBuf.setElem((line - y), (col - x), drawableBands[drawBands], buffer.getElemFloat(line, col, drawableBands[drawBands]));
				}
			}else if(buffer.getDataType() == IBuffer.TYPE_DOUBLE){
				for(int drawBands = 0; drawBands < drawableBands.length; drawBands++){
					for(int line = y; line < (y + h); line ++)
						for(int col = x; col < (x + w); col ++)
							rasterBuf.setElem((line - y), (col - x), drawableBands[drawBands], buffer.getElemDouble(line, col, drawableBands[drawBands]));
				}
			}
		}
		return rasterBuf;
	}

	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.raster.dataset.RasterDataset#getWindowRaster(int, int, int, int, int, int, org.gvsig.raster.dataset.BandList, org.gvsig.raster.dataset.IBuffer)
	 */
	public IBuffer getWindowRaster(int x, int y, int w, int h, int bufWidth, int bufHeight, BandList bandList, IBuffer rasterBuf) {
		setView(
				new Extent( RasterUtilities.getMapRectFromPxRect(getExtent().toRectangle2D(),
							getWidth(),
							getHeight(),
							new Rectangle2D.Double(x, y, w, h)))
				);

		//Relación entre el número de pixels del buffer origen (area seleccionada) y el destino
		double stepX = w / ((double)bufWidth);
		double stepY = h / ((double)bufHeight);
		switch(buffer.getDataType()){
		case IBuffer.TYPE_BYTE: writeByteBuffer(rasterBuf, stepX, stepY, new Point2D.Double(x, y), bandList); break;
		case IBuffer.TYPE_SHORT: writeShortBuffer(rasterBuf, stepX, stepY, new Point2D.Double(x, y), bandList); break;
		case IBuffer.TYPE_INT: writeIntBuffer(rasterBuf, stepX, stepY, new Point2D.Double(x, y), bandList); break;
		case IBuffer.TYPE_FLOAT: writeFloatBuffer(rasterBuf, stepX, stepY, new Point2D.Double(x, y), bandList); break;
		case IBuffer.TYPE_DOUBLE: writeDoubleBuffer(rasterBuf, stepX, stepY, new Point2D.Double(x, y), bandList); break;
		}
		return rasterBuf;
	}

	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.raster.dataset.RasterDataset#readCompleteLine(int, int)
	 */
	public Object readCompleteLine(int line, int band) throws InvalidSetViewException, FileNotOpenException, RasterDriverException {
		switch(buffer.getDataType()){
		case IBuffer.TYPE_BYTE: return buffer.getLineFromBandByte(line, band);
		case IBuffer.TYPE_SHORT: return buffer.getLineFromBandShort(line, band);
		case IBuffer.TYPE_INT: return buffer.getLineFromBandInt(line, band);
		case IBuffer.TYPE_FLOAT: return buffer.getLineFromBandFloat(line, band);
		case IBuffer.TYPE_DOUBLE: return buffer.getLineFromBandDouble(line, band);
		}
		return null;
	}

	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.raster.dataset.RasterDataset#readBlock(int, int)
	 */
	public Object readBlock(int pos, int blockHeight) throws InvalidSetViewException, FileNotOpenException, RasterDriverException {
		if(pos < 0)
			throw new InvalidSetViewException("Request out of grid");

		if((pos + blockHeight) > buffer.getHeight())
			blockHeight = Math.abs(buffer.getHeight() - pos);

		switch(buffer.getDataType()){
		case IBuffer.TYPE_BYTE:
			byte[][][] bufb = new byte[getBandCount()][][];
			for (int iBand = 0; iBand < buffer.getBandCount(); iBand++) {
				for (int row = 0; row < blockHeight; row++) {
					bufb[iBand][row] = buffer.getLineFromBandByte(row, iBand);
				}
			}
			return bufb;
		case IBuffer.TYPE_SHORT:
			short[][][] bufs = new short[getBandCount()][][];
			for (int iBand = 0; iBand < buffer.getBandCount(); iBand++) {
				for (int row = 0; row < blockHeight; row++) {
					bufs[iBand][row] = buffer.getLineFromBandShort(row, iBand);
				}
			}
			return bufs;
		case IBuffer.TYPE_INT:
			int[][][] bufi = new int[getBandCount()][][];
			for (int iBand = 0; iBand < buffer.getBandCount(); iBand++) {
				for (int row = 0; row < blockHeight; row++) {
					bufi[iBand][row] = buffer.getLineFromBandInt(row, iBand);
				}
			}
			return bufi;
		case IBuffer.TYPE_FLOAT:
			float[][][] buff = new float[getBandCount()][][];
			for (int iBand = 0; iBand < buffer.getBandCount(); iBand++) {
				for (int row = 0; row < blockHeight; row++) {
					buff[iBand][row] = buffer.getLineFromBandFloat(row, iBand);
				}
			}
			return buff;
		case IBuffer.TYPE_DOUBLE:
			double[][][] bufd = new double[getBandCount()][][];
			for (int iBand = 0; iBand < buffer.getBandCount(); iBand++) {
				for (int row = 0; row < blockHeight; row++) {
					bufd[iBand][row] = buffer.getLineFromBandDouble(row, iBand);
				}
			}
			return bufd;
		}
		return null;
	}

		/**
	 * Obtiene el objeto que contiene el estado de la transparencia
	 */
	public Transparency getTransparencyDatasetStatus() {
		if(fileTransparency == null)
			fileTransparency = new Transparency();
		return fileTransparency;
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
}