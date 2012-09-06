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
package org.gvsig.raster.buffer;

import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.process.RasterTask;
import org.gvsig.raster.process.RasterTaskQueue;

/**
 * Clase que contiene la funcionalidad para poder interpolar un buffer de datos
 * por distintos métodos.
 * 
 * @version 07/05/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class BufferInterpolation {
	public final static int INTERPOLATION_Undefined        = 0;
	public final static int INTERPOLATION_NearestNeighbour = 1;
	public final static int INTERPOLATION_Bilinear         = 2;
	public final static int INTERPOLATION_InverseDistance  = 3;
	public final static int INTERPOLATION_BicubicSpline    = 4;
	public final static int INTERPOLATION_BSpline          = 5;

	private RasterBuffer buffer  = null;
	private double       percent = 0;
	
	/**
	 * Constructor. Asigna un RasterBuffer.
	 * @param buf
	 */
	public BufferInterpolation(RasterBuffer buf) {
		this.buffer = buf;
	}
	
	/**
	 * Ajusta el raster al ancho y alto solicitado por el vecino más cercano. Promedia el valor de dos
	 * pixeles contiguos.
	 * @param w Nuevo ancho
	 * @param h Nuevo alto
	 */
	public RasterBuffer adjustRasterNearestNeighbourInterpolation(int w, int h) throws InterruptedException {
		RasterTask task = RasterTaskQueue.get(Thread.currentThread().toString());
		
		double stepX = (double) w / (double) buffer.width;
		double stepY = (double) h / (double) buffer.height;
		RasterBuffer rasterBuf = RasterBuffer.getBuffer(buffer.getDataType(), w, h, buffer.getBandCount(), true);
		

		int[] bands = new int[rasterBuf.getBandCount()];
		for (int iBand = 0; iBand < rasterBuf.getBandCount(); iBand++)
			bands[iBand] = iBand;

		percent = 0;
		double multip = 100.0D / (buffer.height * bands.length);
		switch (buffer.dataType) {
			case RasterBuffer.TYPE_BYTE:
				for (int iBand = 0; iBand < bands.length; iBand++) {
					if (w <= buffer.width) { // submuestreo
						for (int iRow = 0; iRow < buffer.height; iRow++) {
							for (int iCol = 0; iCol < buffer.width; iCol++)
								rasterBuf.setElem((int) (iRow * stepY), (int) (iCol * stepX), bands[iBand], buffer.getElemByte(iRow, iCol, iBand));
							percent = (iBand * buffer.height + iRow) * multip;
							if (task.getEvent() != null)
								task.manageEvent(task.getEvent());
						}
					} else { // supermuestreo
						for (int iRow = 0; iRow < h; iRow++) {
							for (int iCol = 0; iCol < w; iCol++)
								rasterBuf.setElem(iRow, iCol, bands[iBand], buffer.getElemByte((int) (iRow / stepY), (int) (iCol / stepX), iBand));
							percent = (iBand * buffer.height + iRow) * multip;
							if (task.getEvent() != null)
								task.manageEvent(task.getEvent());
						}
					}
				}
				break;
			case RasterBuffer.TYPE_DOUBLE:
				for (int iBand = 0; iBand < bands.length; iBand++) {
					if (w <= buffer.width) { // submuestreo
						for (int iRow = 0; iRow < buffer.height; iRow++) {
							for (int iCol = 0; iCol < buffer.width; iCol++)
								rasterBuf.setElem((int) (iRow * stepY), (int) (iCol * stepX), bands[iBand], buffer.getElemDouble(iRow, iCol, iBand));
							percent = (iBand * buffer.height + iRow) * multip;
							if (task.getEvent() != null)
								task.manageEvent(task.getEvent());
						}
					} else { // supermuestreo
						for (int iRow = 0; iRow < h; iRow++) {
							for (int iCol = 0; iCol < w; iCol++)
								rasterBuf.setElem(iRow, iCol, bands[iBand], buffer.getElemDouble((int) (iRow / stepY), (int) (iCol / stepX), iBand));
							percent = (iBand * buffer.height + iRow) * multip;
							if (task.getEvent() != null)
								task.manageEvent(task.getEvent());
						}
					}
				}
				break;
			case RasterBuffer.TYPE_FLOAT:
				for (int iBand = 0; iBand < bands.length; iBand++) {
					if (w <= buffer.width) { // submuestreo
						for (int iRow = 0; iRow < buffer.height; iRow++) {
							for (int iCol = 0; iCol < buffer.width; iCol++)
								rasterBuf.setElem((int) (iRow * stepY), (int) (iCol * stepX), bands[iBand], buffer.getElemFloat(iRow, iCol, iBand));
							percent = (iBand * buffer.height + iRow) * multip;
							if (task.getEvent() != null)
								task.manageEvent(task.getEvent());
						}
					} else { // supermuestreo
						for (int iRow = 0; iRow < h; iRow++) {
							for (int iCol = 0; iCol < w; iCol++)
								rasterBuf.setElem(iRow, iCol, bands[iBand], buffer.getElemFloat((int) (iRow / stepY), (int) (iCol / stepX), iBand));
							percent = (iBand * buffer.height + iRow) * multip;
							if (task.getEvent() != null)
								task.manageEvent(task.getEvent());
						}
					}
				}
				break;
			case RasterBuffer.TYPE_INT:
				for (int iBand = 0; iBand < bands.length; iBand++) {
					if (w <= buffer.width) { // submuestreo
						for (int iRow = 0; iRow < buffer.height; iRow++) {
							for (int iCol = 0; iCol < buffer.width; iCol++)
								rasterBuf.setElem((int) (iRow * stepY), (int) (iCol * stepX), bands[iBand], buffer.getElemInt(iRow, iCol, iBand));
							percent = (iBand * buffer.height + iRow) * multip;
							if (task.getEvent() != null)
								task.manageEvent(task.getEvent());
						}
					} else { // supermuestreo
						for (int iRow = 0; iRow < h; iRow++) {
							for (int iCol = 0; iCol < w; iCol++)
								rasterBuf.setElem(iRow, iCol, bands[iBand], buffer.getElemInt((int) (iRow / stepY), (int) (iCol / stepX), iBand));
							percent = (iBand * buffer.height + iRow) * multip;
							if (task.getEvent() != null)
								task.manageEvent(task.getEvent());
						}
					}
				}
				break;
			case RasterBuffer.TYPE_USHORT:
			case RasterBuffer.TYPE_SHORT:
				for (int iBand = 0; iBand < bands.length; iBand++) {
					if (w <= buffer.width) { // submuestreo
						for (int iRow = 0; iRow < buffer.height; iRow++) {
							for (int iCol = 0; iCol < buffer.width; iCol++)
								rasterBuf.setElem((int) (iRow * stepY), (int) (iCol * stepX), bands[iBand], buffer.getElemShort(iRow, iCol, iBand));
							percent = (iBand * buffer.height + iRow) * multip;
							if (task.getEvent() != null)
								task.manageEvent(task.getEvent());
						}
					} else { // supermuestreo
						for (int iRow = 0; iRow < h; iRow++) {
							for (int iCol = 0; iCol < w; iCol++)
								rasterBuf.setElem(iRow, iCol, bands[iBand], buffer.getElemShort((int) (iRow / stepY), (int) (iCol / stepX), iBand));
							percent = (iBand * buffer.height + iRow) * multip;
							if (task.getEvent() != null)
								task.manageEvent(task.getEvent());
						}
					}
				}
				break;
		}
		return rasterBuf; 
	}
	
	/**
	 * Ajusta el raster al ancho y alto solicitado ajustando con una interpolación bilineal. Promedia
	 * el valor de cuatro pixeles adyacentes.
	 * <P>
	 * Para cada pixel del raster A:(x, y) obtiene el B:(x + 1, y), C:(x, y + 1), D:(x + 1, y + 1)
	 * Para cada valor del kernel se calcula un valor 'd' que es un peso dependiendo de su posición.
	 * Este peso depende de la posición del pixel destino dentro del origen. La posición del pixel destino
	 * en el origen es un valor decimal que puede ir de 0 a 1. Si está muy pegado a la esquina superior
	 * izquierda estará cercano a 0 y si está muy pegado a la esquina inferior derecha estará cercano a 1.
	 * Este valor está representado por 'dx' y 'dy'. 
	 * </P>
	 * <P>
	 * Los pesos aplicados son a
	 * <UL> 
	 * <LI>A (1-dx) * (1-dy)</LI>
	 * <LI>B dx * (1-dy)</LI>
	 * <LI>C (1-dx) * dy</LI>
	 * <LI>D dx * dy</LI>
	 * </UL>
	 * La variable 'z' contiene el valor acumulado de cada peso por el valor del pixel.
	 * La variable 'n' contiene el valor acumulado de los pesos de los cuatro pixeles.
	 * El valor final del pixel será 'z/n', es decir un promedio del valor de los cuatro teniendo
	 * en cuenta el peso de cada uno.
	 * </P>
	 * @param w Nuevo ancho del buffer de salida
	 * @param h Nuevo alto del buffer de salida
	 */
	public RasterBuffer adjustRasterBilinearInterpolation(int w, int h) throws InterruptedException {
		RasterTask task = RasterTaskQueue.get(Thread.currentThread().toString());
		
		double pxSize = (double) buffer.width / (double) w;
		RasterBuffer rasterBuf = RasterBuffer.getBuffer(buffer.getDataType(), w, h, buffer.getBandCount(), true);
		
		double posX = pxSize / 2D; // Empieza en el centro del primer pixel
		double posY = posX;
		double dx = 0D, dy = 0D;

		percent = 0;
		double multip = 100.0D / (h * buffer.getBandCount());

		for (int iBand = 0; iBand < buffer.getBandCount(); iBand++) {
			posY = pxSize / 2D;
			switch (buffer.dataType) {
				case RasterBuffer.TYPE_BYTE:
					for (int iRow = 0; iRow < h; iRow++) {
						dy = posY - ((int) posY);
						posX = pxSize / 2D;
						for (int iCol = 0; iCol < w; iCol++) {
							dx = posX - ((int) posX);
							try {
								double[] kernel = getKernelByte(((int) posX), ((int) posY), iBand);
								rasterBuf.setElem(iRow, iCol, iBand, (byte) ((byte) getBilinearValue(dx, dy, kernel) & 0xff));
							} catch (ArrayIndexOutOfBoundsException e) {
								// System.out.println(posX + " " + posY);
							}
							posX += pxSize;
						}
						posY += pxSize;
						percent = (iBand * h + iRow) * multip;
						if (task.getEvent() != null)
							task.manageEvent(task.getEvent());
					}
					break;
				case RasterBuffer.TYPE_SHORT:
					for (int iRow = 0; iRow < h; iRow++) {
						dy = posY - ((int) posY);
						posX = pxSize / 2D;
						for (int iCol = 0; iCol < w; iCol++) {
							dx = posX - ((int) posX);
							try {
								double[] kernel = getKernelShort(((int) posX), ((int) posY), iBand);
								rasterBuf.setElem(iRow, iCol, iBand, (short) ((short) getBilinearValue(dx, dy, kernel) & 0xffff));
							} catch (ArrayIndexOutOfBoundsException e) {
								// System.out.println(posX + " " + posY);
							}
							posX += pxSize;
						}
						posY += pxSize;
						percent = (iBand * h + iRow) * multip;
						if (task.getEvent() != null)
							task.manageEvent(task.getEvent());
					}
					break;
				case RasterBuffer.TYPE_INT:
					for (int iRow = 0; iRow < h; iRow++) {
						dy = posY - ((int) posY);
						posX = pxSize / 2D;
						for (int iCol = 0; iCol < w; iCol++) {
							dx = posX - ((int) posX);
							try {
								double[] kernel = getKernelInt(((int) posX), ((int) posY), iBand);
								rasterBuf.setElem(iRow, iCol, iBand, (int) ((int) getBilinearValue(dx, dy, kernel) & 0xff));
							} catch (ArrayIndexOutOfBoundsException e) {
								// System.out.println(posX + " " + posY);
							}
							posX += pxSize;
						}
						posY += pxSize;
						percent = (iBand * h + iRow) * multip;
						if (task.getEvent() != null)
							task.manageEvent(task.getEvent());
					}
					break;
				case RasterBuffer.TYPE_FLOAT:
					for (int iRow = 0; iRow < h; iRow++) {
						dy = posY - ((int) posY);
						posX = pxSize / 2D;
						for (int iCol = 0; iCol < w; iCol++) {
							dx = posX - ((int) posX);
							try {
								double[] kernel = getKernelFloat(((int) posX), ((int) posY), iBand);
								rasterBuf.setElem(iRow, iCol, iBand, (float) getBilinearValue(dx, dy, kernel));
							} catch (ArrayIndexOutOfBoundsException e) {
								// System.out.println(posX + " " + posY);
							}
							posX += pxSize;
						}
						posY += pxSize;
						percent = (iBand * h + iRow) * multip;
						if (task.getEvent() != null)
							task.manageEvent(task.getEvent());
					}
					break;
				case RasterBuffer.TYPE_DOUBLE:
					for (int iRow = 0; iRow < h; iRow++) {
						dy = posY - ((int) posY);
						posX = pxSize / 2D;
						for (int iCol = 0; iCol < w; iCol++) {
							dx = posX - ((int) posX);
							try {
								double[] kernel = getKernelDouble(((int) posX), ((int) posY), iBand);
								rasterBuf.setElem(iRow, iCol, iBand, (double) getBilinearValue(dx, dy, kernel));
							} catch (ArrayIndexOutOfBoundsException e) {
								// System.out.println(posX + " " + posY);
							}
							posX += pxSize;
						}
						posY += pxSize;
						percent = (iBand * h + iRow) * multip;
						if (task.getEvent() != null)
							task.manageEvent(task.getEvent());
					}
					break;
			}
		}

		return rasterBuf;
	}

	/**
	 * Ajusta el raster al ancho y alto solicitado ajustando con una interpolación de distancia inversa.
	 * Asigna el valor de un pixel en función inversa de la distancia.
	 * <P>
	 * Para cada pixel del raster A:(x, y) obtiene el B:(x + 1, y), C:(x, y + 1), D:(x + 1, y + 1)
	 * Para cada valor del kernel se calcula un valor 'd' que es un peso dependiendo de su posición.
	 * Este peso será dependiente de la posición del pixel destino dentro del origen. La posición del pixel destino
	 * en el origen es un valor decimal que puede ir de 0 a 1. Si está muy pegado a la esquina superior
	 * izquierda estará cercano a 0 y si está muy pegado a la esquina inferior derecha estará cercano a 1.
	 * Este valor está representado por 'dx' y 'dy'. En este caso, y a diferencia del método
	 * bilinear el peso vendrá representado por la inversa de la distancia entre la posición 
	 * dentro del pixel y el origen del mismo.
	 * </P>
	 * <P>
	 * Los pesos aplicados son a
	 * <UL> 
	 * <LI>A  1 / sqrt((1-dx) * (1-dy))</LI>
	 * <LI>B  1 / sqrt(dx * (1-dy))</LI>
	 * <LI>C  1 / sqrt((1-dx) * dy)</LI>
	 * <LI>D  1 / sqrt(dx * dy)</LI>
	 * </UL>
	 * La variable 'z' contiene el valor acumulado de cada peso por el valor del pixel.
	 * La variable 'n' contiene el valor acumulado de los pesos de los cuatro pixeles.
	 * El valor final del pixel será 'z/n', es decir un promedio del valor de los cuatro teniendo
	 * en cuenta el peso de cada uno.
	 * </P>
	 * @param w Nuevo ancho del buffer de salida
	 * @param h Nuevo alto del buffer de salida
	 */
	public RasterBuffer adjustRasterInverseDistanceInterpolation(int w, int h) throws InterruptedException {
		RasterTask task = RasterTaskQueue.get(Thread.currentThread().toString());
		
		double pxSize = (double) buffer.width / (double) w;
		RasterBuffer rasterBuf = RasterMemoryBuffer.getBuffer(buffer.getDataType(), w, h, buffer.getBandCount(), true);

		double posX = pxSize / 2D;
		double posY = posX;
		double dx = 0D, dy = 0D;

		percent = 0;
		double multip = 100.0D / (h * buffer.getBandCount());

		for (int iBand = 0; iBand < buffer.getBandCount(); iBand++) {
			posY = pxSize / 2D;
			switch (buffer.dataType) {
				case RasterBuffer.TYPE_BYTE:
					for (int iRow = 0; iRow < h; iRow++) {
						dy = posY - ((int) posY);
						posX = pxSize / 2D;
						for (int iCol = 0; iCol < w; iCol++) {
							dx = posX - ((int) posX);
							try {
								double[] kernel = getKernelByte(((int) posX), ((int) posY), iBand);
								rasterBuf.setElem(iRow, iCol, iBand, (byte) ((byte) getInverseDistanceValue(dx, dy, kernel) & 0xff));
							} catch (ArrayIndexOutOfBoundsException e) {
								// System.out.println(posX + " " + posY);
							}
							posX += pxSize;
						}
						posY += pxSize;
						percent = (iBand * h + iRow) * multip;
						if (task.getEvent() != null)
							task.manageEvent(task.getEvent());
					}
					break;
				case RasterBuffer.TYPE_SHORT:
					for (int iRow = 0; iRow < h; iRow++) {
						dy = posY - ((int) posY);
						posX = pxSize / 2D;
						for (int iCol = 0; iCol < w; iCol++) {
							dx = posX - ((int) posX);
							try {
								double[] kernel = getKernelShort(((int) posX), ((int) posY), iBand);
								rasterBuf.setElem(iRow, iCol, iBand, (short) ((short) getInverseDistanceValue(dx, dy, kernel) & 0xffff));
							} catch (ArrayIndexOutOfBoundsException e) {
								// System.out.println(posX + " " + posY);
							}
							posX += pxSize;
						}
						posY += pxSize;
						percent = (iBand * h + iRow) * multip;
						if (task.getEvent() != null)
							task.manageEvent(task.getEvent());
					}
					break;
				case RasterBuffer.TYPE_INT:
					for (int iRow = 0; iRow < h; iRow++) {
						dy = posY - ((int) posY);
						posX = pxSize / 2D;
						for (int iCol = 0; iCol < w; iCol++) {
							dx = posX - ((int) posX);
							try {
								double[] kernel = getKernelInt(((int) posX), ((int) posY), iBand);
								rasterBuf.setElem(iRow, iCol, iBand, (int) ((int) getInverseDistanceValue(dx, dy, kernel) & 0xff));
							} catch (ArrayIndexOutOfBoundsException e) {
								// System.out.println(posX + " " + posY);
							}
							posX += pxSize;
						}
						posY += pxSize;
						percent = (iBand * h + iRow) * multip;
						if (task.getEvent() != null)
							task.manageEvent(task.getEvent());
					}
					break;
				case RasterBuffer.TYPE_FLOAT:
					for (int iRow = 0; iRow < h; iRow++) {
						dy = posY - ((int) posY);
						posX = pxSize / 2D;
						for (int iCol = 0; iCol < w; iCol++) {
							dx = posX - ((int) posX);
							try {
								double[] kernel = getKernelFloat(((int) posX), ((int) posY), iBand);
								rasterBuf.setElem(iRow, iCol, iBand, (float) getInverseDistanceValue(dx, dy, kernel));
							} catch (ArrayIndexOutOfBoundsException e) {
								// System.out.println(posX + " " + posY);
							}
							posX += pxSize;
						}
						posY += pxSize;
						percent = (iBand * h + iRow) * multip;
						if (task.getEvent() != null)
							task.manageEvent(task.getEvent());
					}
					break;
				case RasterBuffer.TYPE_DOUBLE:
					for (int iRow = 0; iRow < h; iRow++) {
						dy = posY - ((int) posY);
						posX = pxSize / 2D;
						for (int iCol = 0; iCol < w; iCol++) {
							dx = posX - ((int) posX);
							try {
								double[] kernel = getKernelDouble(((int) posX), ((int) posY), iBand);
								rasterBuf.setElem(iRow, iCol, iBand, (double) getInverseDistanceValue(dx, dy, kernel));
							} catch (ArrayIndexOutOfBoundsException e) {
								// System.out.println(posX + " " + posY);
							}
							posX += pxSize;
						}
						posY += pxSize;
						percent = (iBand * h + iRow) * multip;
						if (task.getEvent() != null)
							task.manageEvent(task.getEvent());
					}
					break;
			}
		}
		return rasterBuf;
	}

	/**
	 * Ajusta el raster al ancho y alto solicitado ajustando con una interpolación BSpline. Promedia
	 * el valor de cuatro pixeles adyacentes.
	 * @param w Nuevo ancho
	 * @param h Nuevo alto
	 */
	public RasterBuffer adjustRasterBSplineInterpolation(int w, int h) throws InterruptedException {
		RasterTask task = RasterTaskQueue.get(Thread.currentThread().toString());
		
		double pxSize = (double) buffer.width / (double) w;
		RasterBuffer rasterBuf = RasterMemoryBuffer.getBuffer(buffer.getDataType(), w, h, buffer.getBandCount(), true);

		double posX = pxSize / 2D;
		double posY = posX;
		double dx = 0D, dy = 0D;

		percent = 0;
		double multip = 100.0D / (h * buffer.getBandCount());

		for (int iBand = 0; iBand < buffer.getBandCount(); iBand++) {
			posY = pxSize / 2D;
			switch (buffer.dataType) {
				case RasterBuffer.TYPE_BYTE:
					for (int iRow = 0; iRow < h; iRow++) {
						dy = posY - ((int) posY);
						posX = pxSize / 2D;
						for (int iCol = 0; iCol < w; iCol++) {
							dx = posX - ((int) posX);
							try {
								double[][] kernel = get4x4Submatrix(((int) posX), ((int) posY), iBand);
								if (kernel == null) {
									double[] k = getKernelByte(((int) posX), ((int) posY), iBand);
									rasterBuf.setElem(iRow, iCol, iBand, (byte) ((byte) getBilinearValue(dx, dy, k) & 0xff));
								} else
									rasterBuf.setElem(iRow, iCol, iBand, (byte) ((byte) getBSplineValue(dx, dy, kernel) & 0xff));
							} catch (ArrayIndexOutOfBoundsException e) {
								// System.out.println(posX + " " + posY);
							}
							posX += pxSize;
						}
						posY += pxSize;
						percent = (iBand * h + iRow) * multip;
						if (task.getEvent() != null)
							task.manageEvent(task.getEvent());
					}
					break;
				case RasterBuffer.TYPE_SHORT:
					for (int iRow = 0; iRow < h; iRow++) {
						dy = posY - ((int) posY);
						posX = pxSize / 2D;
						for (int iCol = 0; iCol < w; iCol++) {
							dx = posX - ((int) posX);
							try {
								double[][] kernel = get4x4Submatrix(((int) posX), ((int) posY), iBand);
								if (kernel == null) {
									double[] k = getKernelShort(((int) posX), ((int) posY), iBand);
									rasterBuf.setElem(iRow, iCol, iBand, (short) ((short) getBilinearValue(dx, dy, k) & 0xffff));
								} else
									rasterBuf.setElem(iRow, iCol, iBand, (short) ((short) getBSplineValue(dx, dy, kernel) & 0xffff));
							} catch (ArrayIndexOutOfBoundsException e) {
								// System.out.println(posX + " " + posY);
							}
							posX += pxSize;
						}
						posY += pxSize;
						percent = (iBand * h + iRow) * multip;
						if (task.getEvent() != null)
							task.manageEvent(task.getEvent());
					}
					break;
				case RasterBuffer.TYPE_INT:
					for (int iRow = 0; iRow < h; iRow++) {
						dy = posY - ((int) posY);
						posX = pxSize / 2D;
						for (int iCol = 0; iCol < w; iCol++) {
							dx = posX - ((int) posX);
							try {
								double[][] kernel = get4x4Submatrix(((int) posX), ((int) posY), iBand);
								if (kernel == null) {
									double[] k = getKernelInt(((int) posX), ((int) posY), iBand);
									rasterBuf.setElem(iRow, iCol, iBand, (int) ((int) getBilinearValue(dx, dy, k) & 0xffffffff));
								} else
									rasterBuf.setElem(iRow, iCol, iBand, (int) ((int) getBSplineValue(dx, dy, kernel) & 0xffffffff));
							} catch (ArrayIndexOutOfBoundsException e) {
								// System.out.println(posX + " " + posY);
							}
							posX += pxSize;
						}
						posY += pxSize;
						percent = (iBand * h + iRow) * multip;
						if (task.getEvent() != null)
							task.manageEvent(task.getEvent());
					}
					break;
				case RasterBuffer.TYPE_FLOAT:
					for (int iRow = 0; iRow < h; iRow++) {
						dy = posY - ((int) posY);
						posX = pxSize / 2D;
						for (int iCol = 0; iCol < w; iCol++) {
							dx = posX - ((int) posX);
							try {
								double[][] kernel = get4x4Submatrix(((int) posX), ((int) posY), iBand);
								if (kernel == null) {
									double[] k = getKernelFloat(((int) posX), ((int) posY), iBand);
									rasterBuf.setElem(iRow, iCol, iBand, (float) getBilinearValue(dx, dy, k));
								} else
									rasterBuf.setElem(iRow, iCol, iBand, (float) getBSplineValue(dx, dy, kernel));
							} catch (ArrayIndexOutOfBoundsException e) {
								// System.out.println(posX + " " + posY);
							}
							posX += pxSize;
						}
						posY += pxSize;
						percent = (iBand * h + iRow) * multip;
						if (task.getEvent() != null)
							task.manageEvent(task.getEvent());
					}
					break;
				case RasterBuffer.TYPE_DOUBLE:
					for (int iRow = 0; iRow < h; iRow++) {
						dy = posY - ((int) posY);
						posX = pxSize / 2D;
						for (int iCol = 0; iCol < w; iCol++) {
							dx = posX - ((int) posX);
							try {
								double[][] kernel = get4x4Submatrix(((int) posX), ((int) posY), iBand);
								if (kernel == null) {
									double[] k = getKernelDouble(((int) posX), ((int) posY), iBand);
									rasterBuf.setElem(iRow, iCol, iBand, (double) getBilinearValue(dx, dy, k));
								} else
									rasterBuf.setElem(iRow, iCol, iBand, (double) getBSplineValue(dx, dy, kernel));
							} catch (ArrayIndexOutOfBoundsException e) {
								// System.out.println(posX + " " + posY);
							}
							posX += pxSize;
						}
						posY += pxSize;
						percent = (iBand * h + iRow) * multip;
						if (task.getEvent() != null)
							task.manageEvent(task.getEvent());
					}
					break;
			}
		}
		return rasterBuf;
	}

	/**
	 * Ajusta el raster al ancho y alto solicitado ajustando con una interpolación de spline bicubica.
	 * @param w Nuevo ancho
	 * @param h Nuevo alto
	 */
	public RasterBuffer adjustRasterBicubicSplineInterpolation(int w, int h) throws InterruptedException {
		RasterTask task = RasterTaskQueue.get(Thread.currentThread().toString());
		
		double pxSize = (double) buffer.width / (double) w;
		RasterBuffer rasterBuf = RasterMemoryBuffer.getBuffer(buffer.getDataType(), w, h, buffer.getBandCount(), true);

		double posX = pxSize / 2D;
		double posY = posX;
		double dx = 0D, dy = 0D;

		percent = 0;
		double multip = 100.0D / (h * buffer.getBandCount());

		for (int iBand = 0; iBand < buffer.getBandCount(); iBand++) {
			posY = pxSize / 2D;
			switch (buffer.dataType) {
				case RasterBuffer.TYPE_BYTE:
					for (int iRow = 0; iRow < h; iRow++) {
						dy = posY - ((int) posY);
						posX = pxSize / 2D;
						for (int iCol = 0; iCol < w; iCol++) {
							dx = posX - ((int) posX);
							try {
								double[][] kernel = get4x4Submatrix(((int) posX), ((int) posY), iBand);
								if (kernel == null) {
									double[] k = getKernelByte(((int) posX), ((int) posY), iBand);
									rasterBuf.setElem(iRow, iCol, iBand, (byte) ((byte) getBilinearValue(dx, dy, k) & 0xff));
								} else
									rasterBuf.setElem(iRow, iCol, iBand, (byte) ((byte) getBicubicSplineValue(dx, dy, kernel) & 0xff));
							} catch (ArrayIndexOutOfBoundsException e) {
								// System.out.println(posX + " " + posY);
							}
							posX += pxSize;
						}
						posY += pxSize;
						percent = (iBand * h + iRow) * multip;
						if (task.getEvent() != null)
							task.manageEvent(task.getEvent());
					}
					break;
				case RasterBuffer.TYPE_SHORT:
					for (int iRow = 0; iRow < h; iRow++) {
						dy = posY - ((int) posY);
						posX = pxSize / 2D;
						for (int iCol = 0; iCol < w; iCol++) {
							dx = posX - ((int) posX);
							try {
								double[][] kernel = get4x4Submatrix(((int) posX), ((int) posY), iBand);
								if (kernel == null) {
									double[] k = getKernelShort(((int) posX), ((int) posY), iBand);
									rasterBuf.setElem(iRow, iCol, iBand, (short) ((short) getBilinearValue(dx, dy, k) & 0xffff));
								} else
									rasterBuf.setElem(iRow, iCol, iBand, (short) ((short) getBicubicSplineValue(dx, dy, kernel) & 0xffff));
							} catch (ArrayIndexOutOfBoundsException e) {
								// System.out.println(posX + " " + posY);
							}
							posX += pxSize;
						}
						posY += pxSize;
						percent = (iBand * h + iRow) * multip;
						if (task.getEvent() != null)
							task.manageEvent(task.getEvent());
					}
					break;
				case RasterBuffer.TYPE_INT:
					for (int iRow = 0; iRow < h; iRow++) {
						dy = posY - ((int) posY);
						posX = pxSize / 2D;
						for (int iCol = 0; iCol < w; iCol++) {
							dx = posX - ((int) posX);
							try {
								double[][] kernel = get4x4Submatrix(((int) posX), ((int) posY), iBand);
								if (kernel == null) {
									double[] k = getKernelInt(((int) posX), ((int) posY), iBand);
									rasterBuf.setElem(iRow, iCol, iBand, (int) ((int) getBilinearValue(dx, dy, k) & 0xffffffff));
								} else
									rasterBuf.setElem(iRow, iCol, iBand, (int) ((int) getBicubicSplineValue(dx, dy, kernel) & 0xffffffff));
							} catch (ArrayIndexOutOfBoundsException e) {
								// System.out.println(posX + " " + posY);
							}
							posX += pxSize;
						}
						posY += pxSize;
						percent = (iBand * h + iRow) * multip;
						if (task.getEvent() != null)
							task.manageEvent(task.getEvent());
					}
					break;
				case RasterBuffer.TYPE_FLOAT:
					for (int iRow = 0; iRow < h; iRow++) {
						dy = posY - ((int) posY);
						posX = pxSize / 2D;
						for (int iCol = 0; iCol < w; iCol++) {
							dx = posX - ((int) posX);
							try {
								double[][] kernel = get4x4Submatrix(((int) posX), ((int) posY), iBand);
								if (kernel == null) {
									double[] k = getKernelFloat(((int) posX), ((int) posY), iBand);
									rasterBuf.setElem(iRow, iCol, iBand, (float) getBilinearValue(dx, dy, k));
								} else
									rasterBuf.setElem(iRow, iCol, iBand, (float) getBicubicSplineValue(dx, dy, kernel));
							} catch (ArrayIndexOutOfBoundsException e) {
								// System.out.println(posX + " " + posY);
							}
							posX += pxSize;
						}
						posY += pxSize;
						percent = (iBand * h + iRow) * multip;
						if (task.getEvent() != null)
							task.manageEvent(task.getEvent());
					}
					break;
				case RasterBuffer.TYPE_DOUBLE:
					for (int iRow = 0; iRow < h; iRow++) {
						dy = posY - ((int) posY);
						posX = pxSize / 2D;
						for (int iCol = 0; iCol < w; iCol++) {
							dx = posX - ((int) posX);
							try {
								double[][] kernel = get4x4Submatrix(((int) posX), ((int) posY), iBand);
								if (kernel == null) {
									double[] k = getKernelDouble(((int) posX), ((int) posY), iBand);
									rasterBuf.setElem(iRow, iCol, iBand, (double) getBilinearValue(dx, dy, k));
								} else
									rasterBuf.setElem(iRow, iCol, iBand, (double) getBicubicSplineValue(dx, dy, kernel));
							} catch (ArrayIndexOutOfBoundsException e) {
								// System.out.println(posX + " " + posY);
							}
							posX += pxSize;
						}
						posY += pxSize;
						percent = (iBand * h + iRow) * multip;
						if (task.getEvent() != null)
							task.manageEvent(task.getEvent());
					}
					break;
			}
		}
		return rasterBuf;
	}
	
	/**
	 * 
	 * @param dx
	 * @param dy
	 * @param kernel
	 * @return
	 */
	private double getBicubicSplineValue(double dx, double dy, double[][] kernel) {
		int	i;
		double a0, a2, a3, b1, b2, b3;
		double[] c = new double[4];

		for(i = 0; i < 4; i++) {
			a0 = kernel[0][i] - kernel[1][i];
			a2 = kernel[2][i] - kernel[1][i];
			a3 = kernel[3][i] - kernel[1][i];

			b1 = -a0 / 3.0 + a2 - a3 / 6.0;
			b2 = a0 / 2.0 + a2 / 2.0;
			b3 = -a0 / 6.0 - a2 / 2.0 + a3 / 6.0;

			c[i] = kernel[1][i] + b1 * dx + b2 * (dx * dx) + b3 * (dx * dx * dx);
		}

		a0 = c[0] - c[1];
		a2 = c[2] - c[1];
		a3 = c[3] - c[1];

		b1 = -a0 / 3.0 + a2 - a3 / 6.0;
		b2 =  a0 / 2.0 + a2 / 2.0;
		b3 = -a0 / 6.0 - a2 / 2.0 + a3 / 6.0;

		return( c[1] + b1 * dy + b2 * (dy * dy) + b3 * (dy * dy * dy) );
	}

	/**
	 * 
	 * @param dx
	 * @param dy
	 * @param kernel
	 * @return
	 */
	private double getBSplineValue(double dx, double dy, double[][] kernel) {
		int i = 0, ix = 0, iy = 0;
		double px = 0, py = 0, z = 0;
		double[] Rx = new double[4];
		double[] Ry = new double[4];

		for(i = 0, px = -1.0 - dx, py = -1.0 - dy; i < 4; i++, px++, py++){
			Rx[i]	= 0.0;
			Ry[i]	= 0.0;

			if( (z = px + 2.0) > 0.0 )
				Rx[i] += z * z * z;
			if( (z = px + 1.0) > 0.0 )
				Rx[i] += -4.0 * z * z * z;
			if( (z = px + 0.0) > 0.0 )
				Rx[i] += 6.0 * z * z * z;
			if( (z = px - 1.0) > 0.0 )
				Rx[i] += -4.0 * z * z * z;
			if( (z = py + 2.0) > 0.0 )
				Ry[i] += z * z * z;
			if( (z = py + 1.0) > 0.0 )
				Ry[i] += -4.0 * z * z * z;
			if( (z = py + 0.0) > 0.0 )
				Ry[i] += 6.0 * z * z * z;
			if( (z = py - 1.0) > 0.0 )
				Ry[i] += -4.0 * z * z * z;

			Rx[i] /= 6.0;
			Ry[i] /= 6.0;
		}

		for(iy = 0, z = 0.0; iy < 4; iy++) {
			for(ix = 0; ix < 4; ix++) {
				z	+= kernel[ix][iy] * Rx[ix] * Ry[iy];
			}
		}
		return z;
	}

	/**
	 * Calcula los valores N y Z para el método bilinear y obtiene el valor del pixel como
	 * Z / N
	 * @param dx distancia en X desde el centro del pixel hasta el punto. Es un valor entre 0 y 1
	 * @param dy distancia en Y desde el centro del pixel hasta el punto. Es un valor entre 0 y 1
	 * @param kernel valor del pixel y alrededor 
	 * @return valor del pixel
	 */
	private double getBilinearValue(double dx, double dy, double[] kernel) {
		double z = 0.0, n = 0.0, d;
		d = (1.0 - dx) * (1.0 - dy);
		z += d * kernel[0];
		n += d;

		d = dx * (1.0 - dy);
		z += d * kernel[1]; 
		n += d;

		d = (1.0 - dx) * dy;
		z += d * kernel[2]; 
		n += d;

		d = dx * dy;
		z += d * kernel[3]; 
		n += d;

		double b = 0;
		if(n > 0.0)
			b = (z / n);
		return b;
	}

	/**
	 * Calcula los valores N y Z para el método de distancia inversa y calcula el valor del
	 * pixel como Z / N.
	 * @param dx distancia en X desde el centro del pixel hasta el punto. Es un valor entre 0 y 1
	 * @param dy distancia en Y desde el centro del pixel hasta el punto. Es un valor entre 0 y 1
	 * @param kernel valor del pixel y alrededor 
	 * @return valor del pixel
	 */
	private double getInverseDistanceValue(double dx, double dy, double[] kernel) {
		double z = 0.0, n = 0.0, d;
		d = 1.0 / Math.sqrt(dx * dx + dy * dy);
		z += d * kernel[0];
		n += d;

		d = 1.0 / Math.sqrt((1.0 - dx) * ( 1.0 - dx) + dy * dy);
		z += d * kernel[1]; 
		n += d;

		d = 1.0 / Math.sqrt(dx*dx + (1.0-dy)*(1.0-dy));
		z += d * kernel[2]; 
		n += d;

		d = 1.0 / Math.sqrt((1.0 - dx) *( 1.0 - dx) + (1.0 - dy) * (1.0 - dy));
		z += d * kernel[3]; 
		n += d;

		double b = 0;
		if(n > 0.0)
			b = (z / n);
		return b;
	}

	/**
	 * Obtiene un kernel de 4x4 elementos. Si alguno de los elementos se sale de la imagen
	 * , por ejemplo en los bordes devuelve null.  
	 * @param x
	 * @param y
	 * @param band
	 * @return
	 */
	private double[][] get4x4Submatrix(int x, int y, int band) {
		int	ix, iy, px, py;
		double[][] z_xy = new double[4][4];

		for(iy = 0, py = y - 1; iy < 4; iy++, py++) {
			for(ix = 0, px = x - 1; ix < 4; ix++, px++) {
				if (!buffer.isInside(px, py))
					return null;
				else {
					switch(buffer.getDataType()) {
					case IBuffer.TYPE_BYTE: z_xy[ix][iy] = (buffer.getElemByte(py, px, band) & 0xff); break;
					case IBuffer.TYPE_SHORT: z_xy[ix][iy] = (buffer.getElemShort(py, px, band) & 0xffff); break;
					case IBuffer.TYPE_INT: z_xy[ix][iy] = (buffer.getElemInt(py, px, band) & 0xffffffff); break;
					case IBuffer.TYPE_FLOAT: z_xy[ix][iy] = buffer.getElemFloat(py, px, band); break;
					case IBuffer.TYPE_DOUBLE: z_xy[ix][iy] = buffer.getElemDouble(py, px, band); break;
					}
				}
			}
		}
		return z_xy;
	}
	  
	/**
	 * Obtiene un kernel de cuatro elemento que corresponden a los pixeles (x, y), (x + 1, y),
	 * (x, y + 1), (x + 1, y + 1). Si los pixeles x + 1 o y + 1 se salen del raster de origen
	 * se tomará x e y. 
	 * @param x Coordenada X del pixel inicial
	 * @param y Coordenada Y del pixel inicial
	 * @param band Número de banda.
	 * @return Kernel solicitado en forma de array.
	 */
	private double[] getKernelByte(int x, int y, int band) {
		double[] d = new double[4];
		d[0] = (buffer.getElemByte(y, x, band) & 0xff);
		int nextX = ((x + 1) >= buffer.getWidth()) ? x : (x + 1);
		int nextY = ((y + 1) >= buffer.getHeight()) ? y : (y + 1);
		d[1] = (buffer.getElemByte(y, nextX, band) & 0xff);
		d[2] = (buffer.getElemByte(nextY, x, band) & 0xff);
		d[3] = (buffer.getElemByte(nextY, nextX, band) & 0xff);
		return d;
	}

	/**
	 * Obtiene un kernel de cuatro elemento que corresponden a los pixeles (x, y), (x + 1, y),
	 * (x, y + 1), (x + 1, y + 1). Si los pixeles x + 1 o y + 1 se salen del raster de origen
	 * se tomará x e y. 
	 * @param x Coordenada X del pixel inicial
	 * @param y Coordenada Y del pixel inicial
	 * @param band Número de banda.
	 * @return Kernel solicitado en forma de array.
	 */
	private double[] getKernelShort(int x, int y, int band) {
		double[] d = new double[4];
		d[0] = (buffer.getElemShort(y, x, band) & 0xffff);
		int nextX = ((x + 1) >= buffer.getWidth()) ? x : (x + 1);
		int nextY = ((y + 1) >= buffer.getHeight()) ? y : (y + 1);
		d[1] = (buffer.getElemShort(y, nextX, band) & 0xffff);
		d[2] = (buffer.getElemShort(nextY, x, band) & 0xffff);
		d[3] = (buffer.getElemShort(nextY, nextX, band) & 0xffff);
		return d;
	}

	/**
	 * Obtiene un kernel de cuatro elemento que corresponden a los pixeles (x, y), (x + 1, y),
	 * (x, y + 1), (x + 1, y + 1). Si los pixeles x + 1 o y + 1 se salen del raster de origen
	 * se tomará x e y. 
	 * @param x Coordenada X del pixel inicial
	 * @param y Coordenada Y del pixel inicial
	 * @param band Número de banda.
	 * @return Kernel solicitado en forma de array.
	 */
	private double[] getKernelInt(int x, int y, int band) {
		double[] d = new double[4];
		d[0] = (buffer.getElemInt(y, x, band) & 0xffffffff);
		int nextX = ((x + 1) >= buffer.getWidth()) ? x : (x + 1);
		int nextY = ((y + 1) >= buffer.getHeight()) ? y : (y + 1);
		d[1] = (buffer.getElemInt(y, nextX, band) & 0xffffffff);
		d[2] = (buffer.getElemInt(nextY, x, band) & 0xffffffff);
		d[3] = (buffer.getElemInt(nextY, nextX, band) & 0xffffffff);
		return d;
	}

	/**
	 * Obtiene un kernel de cuatro elemento que corresponden a los pixeles (x, y), (x + 1, y),
	 * (x, y + 1), (x + 1, y + 1). Si los pixeles x + 1 o y + 1 se salen del raster de origen
	 * se tomará x e y. 
	 * @param x Coordenada X del pixel inicial
	 * @param y Coordenada Y del pixel inicial
	 * @param band Número de banda.
	 * @return Kernel solicitado en forma de array.
	 */
	private double[] getKernelFloat(int x, int y, int band) {
		double[] d = new double[4];
		d[0] = buffer.getElemFloat(y, x, band);
		int nextX = ((x + 1) >= buffer.getWidth()) ? x : (x + 1);
		int nextY = ((y + 1) >= buffer.getHeight()) ? y : (y + 1);
		d[1] = buffer.getElemFloat(y, nextX, band);
		d[2] = buffer.getElemFloat(nextY, x, band);
		d[3] = buffer.getElemFloat(nextY, nextX, band);
		return d;
	}

	/**
	 * Obtiene un kernel de cuatro elemento que corresponden a los pixeles (x, y), (x + 1, y),
	 * (x, y + 1), (x + 1, y + 1). Si los pixeles x + 1 o y + 1 se salen del raster de origen
	 * se tomará x e y. 
	 * @param x Coordenada X del pixel inicial
	 * @param y Coordenada Y del pixel inicial
	 * @param band Número de banda.
	 * @return Kernel solicitado en forma de array.
	 */
	private double[] getKernelDouble(int x, int y, int band) {
		double[] d = new double[4];
		d[0] = buffer.getElemDouble(y, x, band);
		int nextX = ((x + 1) >= buffer.getWidth()) ? x : (x + 1);
		int nextY = ((y + 1) >= buffer.getHeight()) ? y : (y + 1);
		d[1] = buffer.getElemDouble(y, nextX, band);
		d[2] = buffer.getElemDouble(nextY, x, band);
		d[3] = buffer.getElemDouble(nextY, nextX, band);
		return d;
	}
	
	/**
	 * Obtiene el porcentaje del proceso de interpolacion
	 * @return
	 */
	public int getPercent() {
		return Math.min((int) percent, 100);
	}
}