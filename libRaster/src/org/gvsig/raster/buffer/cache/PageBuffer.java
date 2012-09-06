
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
package org.gvsig.raster.buffer.cache;

import java.io.IOException;

import org.gvsig.raster.buffer.IBand;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.datastruct.Histogram;
import org.gvsig.raster.process.RasterTask;
import org.gvsig.raster.process.RasterTaskQueue;

/**
 * Esta clase representa una página de cache.Cada página de caché está compuesta por
 * varios objetos PageBandBuffer donde cada uno es una página que corresponde a una 
 * banda de raster cacheada. 
 * 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class PageBuffer implements IBuffer {
	private PageBandBuffer[]    pageBandBuffer = null;

	private int                 percent = 0;
	
	/**
	 * Constructor
	 * @param dataType
	 * @param width
	 * @param height
	 * @param bandNr
	 * @param malloc
	 */
	public PageBuffer(int dataType, int width, int height, int bandNr, boolean malloc, int nHddPags) {
		pageBandBuffer = new PageBandBuffer[bandNr];
		for (int i = 0; i < pageBandBuffer.length; i++)
			pageBandBuffer[i] = new PageBandBuffer(dataType, width, height, 1, malloc, i);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.driver.IBuffer#isBandSwitchable()
	 */
	public boolean isBandSwitchable() {
		return false;
	}
	
	/**
	 * Asigna la lista de paginas de disco
	 * @param hddList Lista de páginas de disco
	 */
	public void setHddPages(HddPage[] hddList) {
		for (int i = 0; i < pageBandBuffer.length; i++)
			pageBandBuffer[i].setHddPages(hddList);
	}
	
	/**
	 * Carga una página especificada en el parámetro nPag con los datos necesarios.
	 * Para esto recorre todas las bandas de la página llamando al load de cada una.
	 *   
	 * @param nPag Número de página a cargar
	 */
	public void loadPage(int nPag) {
		for (int i = 0; i < pageBandBuffer.length; i++) {
			try {
				pageBandBuffer[i].loadPage(nPag);
			} catch (InterruptedException e) {
				//Cuando se cancela la carga de una página salimos al acabar la página anterior
				break; 
			}
		}
	}
	
	/**
	 * Salva una página especificada en el parámetro nPag a disco. 
	 * Para esto recorre todas las bandas de la página llamando al save de cada una.
	 *   
	 * @param nPag Número de página a salvar
	 * @throws IOException
	 */
	public void savePage(int nPag) throws IOException {
		for (int i = 0; i < pageBandBuffer.length; i++)
			pageBandBuffer[i].savePage(nPag);
	}
	
	public void assign(int band, byte value) {
		pageBandBuffer[band].assign(0, value);
	}

	public void assign(int band, short value) {
		pageBandBuffer[band].assign(0, value);
	}

	public void assign(int band, int value) {
		pageBandBuffer[band].assign(0, value);
	}

	public void assign(int band, float value) {
		pageBandBuffer[band].assign(0, value);
	}

	public void assign(int band, double value) {
		pageBandBuffer[band].assign(0, value);
	}

	public void assignBand(int nBand, IBand band) {
	}

	public void assignBandToNotValid(int iBand) {
	}

	public IBuffer cloneBuffer() {
		return null;
	}

	public void copyBand(int nBand, IBand band) {
	}

	public IBand createBand(byte defaultValue) {
		return null;
	}

	public IBand getBand(int nBand) {
		return null;
	}

	public IBuffer getBandBuffer(int nBand) {
		return null;
	}

	public int getBandCount() {
		return pageBandBuffer[0].getBandCount();
	}
	
	public int getDataType() {
		return pageBandBuffer[0].getDataType();
	}

	public void setDataType(int dataType) {
	
	}
	
	public int getHeight() {
		return pageBandBuffer[0].getHeight();
	}
	
	public int getWidth() {
		return pageBandBuffer[0].getWidth();
	}

	
	
	public byte getByteNoDataValue() {
		return pageBandBuffer[0].getByteNoDataValue();
	}
	
	public float getFloatNoDataValue() {
		return pageBandBuffer[0].getFloatNoDataValue();
	}

	public int getIntNoDataValue() {
		return pageBandBuffer[0].getIntNoDataValue();
	}

	public double getNoDataValue() {
		return pageBandBuffer[0].getNoDataValue();
	}

	public double getNotValidValue() {
		return pageBandBuffer[0].getNotValidValue();
	}

	public short getShortNoDataValue() {
		return pageBandBuffer[0].getShortNoDataValue();
	}


	
	public byte getElemByte(int line, int col, int band) {
		return pageBandBuffer[band].getElemByte(line, col, 0);
	}

	public short getElemShort(int line, int col, int band) {
		return pageBandBuffer[band].getElemShort(line, col, 0);
	}
	
	public int getElemInt(int line, int col, int band) {
		return pageBandBuffer[band].getElemInt(line, col, 0);
	}

	public float getElemFloat(int line, int col, int band) {
		return pageBandBuffer[band].getElemFloat(line, col, 0);
	}
	
	public double getElemDouble(int line, int col, int band) {
		return pageBandBuffer[band].getElemDouble(line, col, 0);
	}

	


	public void getElemByte(int line, int col, byte[] data) {
		for (int i = 0; i < pageBandBuffer.length; i++) 
			data[i] = pageBandBuffer[i].getElemByte(line, col, 0);
	}
	
	public void getElemShort(int line, int col, short[] data) {
		for (int i = 0; i < pageBandBuffer.length; i++) 
			data[i] = pageBandBuffer[i].getElemShort(line, col, 0);
	}

	public void getElemInt(int line, int col, int[] data) {
		for (int i = 0; i < pageBandBuffer.length; i++) 
			data[i] = pageBandBuffer[i].getElemInt(line, col, 0);
	}

	public void getElemFloat(int line, int col, float[] data) {
		for (int i = 0; i < pageBandBuffer.length; i++) 
			data[i] = pageBandBuffer[i].getElemFloat(line, col, 0);
	}

	public void getElemDouble(int line, int col, double[] data) {
		for (int i = 0; i < pageBandBuffer.length; i++) 
			data[i] = pageBandBuffer[i].getElemDouble(line, col, 0);
	}
	
	
	public byte[][] getLineByte(int line) {
		byte[][] b = new byte[pageBandBuffer.length][];
		for (int i = 0; i < pageBandBuffer.length; i++) 
			b[i] = pageBandBuffer[i].getLineByte(line)[0];
		return b;
	}

	public double[][] getLineDouble(int line) {
		double[][] d = new double[pageBandBuffer.length][];
		for (int i = 0; i < pageBandBuffer.length; i++) 
			d[i] = pageBandBuffer[i].getLineDouble(line)[0];
		return d;
	}

	public float[][] getLineFloat(int line) {
		float[][] f = new float[pageBandBuffer.length][];
		for (int i = 0; i < pageBandBuffer.length; i++) 
			f[i] = pageBandBuffer[i].getLineFloat(line)[0];
		return f;
	}

	public int[][] getLineInt(int line) {
		int[][] in = new int[pageBandBuffer.length][];
		for (int i = 0; i < pageBandBuffer.length; i++) 
			in[i] = pageBandBuffer[i].getLineInt(line)[0];
		return in;
	}

	public short[][] getLineShort(int line) {
		short[][] s = new short[pageBandBuffer.length][];
		for (int i = 0; i < pageBandBuffer.length; i++) 
			s[i] = pageBandBuffer[i].getLineShort(line)[0];
		return s;
	}
	
	
	
	public byte[] getLineFromBandByte(int line, int band) {
		return pageBandBuffer[band].getLineFromBandByte(line, 0);
	}

	public double[] getLineFromBandDouble(int line, int band) {
		return pageBandBuffer[band].getLineFromBandDouble(line, 0);
	}

	public float[] getLineFromBandFloat(int line, int band) {
		return pageBandBuffer[band].getLineFromBandFloat(line, 0);
	}

	public int[] getLineFromBandInt(int line, int band) {
		return pageBandBuffer[band].getLineFromBandInt(line, 0);
	}

	public short[] getLineFromBandShort(int line, int band) {
		return pageBandBuffer[band].getLineFromBandShort(line, 0);
	}

	
	
	
	public void interchangeBands(int band1, int band2) {
	}

	public void mallocOneBand(int dataType, int width, int height, int band) {
	}

	public void replicateBand(int orig, int dest) {
	}



	public void setElem(int line, int col, int band, byte data) {
		pageBandBuffer[band].setElem(line, col, 0, data);
	}

	public void setElem(int line, int col, int band, short data) {
		pageBandBuffer[band].setElem(line, col, 0, data);
	}

	public void setElem(int line, int col, int band, int data) {
		pageBandBuffer[band].setElem(line, col, 0, data);
	}

	public void setElem(int line, int col, int band, float data) {
		pageBandBuffer[band].setElem(line, col, 0, data);
	}

	public void setElem(int line, int col, int band, double data) {
		pageBandBuffer[band].setElem(line, col, 0, data);
	}


	
	public void setElemByte(int line, int col, byte[] data) {
		byte[] b = new byte[1];
		for (int i = 0; i < pageBandBuffer.length; i++) {
			b[0] = data[i];
			pageBandBuffer[i].setElemByte(line, col, b);
		}
	}

	public void setElemDouble(int line, int col, double[] data) {
		double[] b = new double[1];
		for (int i = 0; i < pageBandBuffer.length; i++) {
			b[0] = data[i];
			pageBandBuffer[i].setElemDouble(line, col, b);
		}
	}

	public void setElemFloat(int line, int col, float[] data) {
		float[] b = new float[1];
		for (int i = 0; i < pageBandBuffer.length; i++) {
			b[0] = data[i];
			pageBandBuffer[i].setElemFloat(line, col, b);
		}
	}

	public void setElemInt(int line, int col, int[] data) {
		int[] b = new int[1];
		for (int i = 0; i < pageBandBuffer.length; i++) {
			b[0] = data[i];
			pageBandBuffer[i].setElemInt(line, col, b);
		}
	}

	public void setElemShort(int line, int col, short[] data) {
		short[] b = new short[1];
		for (int i = 0; i < pageBandBuffer.length; i++) {
			b[0] = data[i];
			pageBandBuffer[i].setElemShort(line, col, b);
		}
	}
	
	public void setLineByte(byte[][] data, int line) {
		byte[][] bAux = new byte[1][];
		for (int i = 0; i < pageBandBuffer.length; i++) {
			bAux[0] = data[i];
			pageBandBuffer[i].setLineByte(bAux, line);
		}
	}

	public void setLineDouble(double[][] data, int line) {
		double[][] bAux = new double[1][];
		for (int i = 0; i < pageBandBuffer.length; i++) {
			bAux[0] = data[i];
			pageBandBuffer[i].setLineDouble(bAux, line);
		}
	}

	public void setLineFloat(float[][] data, int line) {
		float[][] bAux = new float[1][];
		for (int i = 0; i < pageBandBuffer.length; i++) {
			bAux[0] = data[i];
			pageBandBuffer[i].setLineFloat(bAux, line);
		}
	}
	
	public void setLineInt(int[][] data, int line) {
		int[][] bAux = new int[1][];
		for (int i = 0; i < pageBandBuffer.length; i++) {
			bAux[0] = data[i];
			pageBandBuffer[i].setLineInt(bAux, line);
		}
	}

	public void setLineShort(short[][] data, int line) {
		short[][] bAux = new short[1][];
		for (int i = 0; i < pageBandBuffer.length; i++) {
			bAux[0] = data[i];
			pageBandBuffer[i].setLineShort(bAux, line);
		}
	}

	public void setLineInBandByte(byte[] data, int line, int band) {
		pageBandBuffer[band].setLineInBandByte(data, line, 0);
	}

	public void setLineInBandDouble(double[] data, int line, int band) {
		pageBandBuffer[band].setLineInBandDouble(data, line, 0);
	}

	public void setLineInBandFloat(float[] data, int line, int band) {
		pageBandBuffer[band].setLineInBandFloat(data, line, 0);
	}

	public void setLineInBandInt(int[] data, int line, int band) {
		pageBandBuffer[band].setLineInBandInt(data, line, 0);
	}

	public void setLineInBandShort(short[] data, int line, int band) {
		pageBandBuffer[band].setLineInBandShort(data, line, 0);
	}

	public void setNoDataValue(double nd) {
	}

	public void setNotValidValue(double value) {
	}

	public void switchBands(int[] bands) {
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IBuffer#getLimits()
	 */
	public double[] getLimits() throws InterruptedException {
		RasterTask task = RasterTaskQueue.get(Thread.currentThread().toString());
		double max = Double.NEGATIVE_INFINITY;
		double secondMax = max;
		double min = Double.MAX_VALUE;
		double secondMin = min;
		double value = 0;

		switch (getDataType()) {
			case IBuffer.TYPE_BYTE:
				for (int i = 0; i < getBandCount(); i++) {
					for (int r = 0; r < getHeight(); r++) {
						for (int c = 0; c < getWidth(); c++) {
							value = (double) (getElemByte(r, c, i) & 0xff);
							if (value > max) {
								if (max != value)
									secondMax = max;
								max = value;
							}
							if (value < min) {
								if (min != value)
									secondMin = min;
								min = value;
							}
						}
						if (task.getEvent() != null)
							task.manageEvent(task.getEvent());
					}
				}
				break;
			case IBuffer.TYPE_SHORT:
				for (int i = 0; i < getBandCount(); i++) {
					for (int r = 0; r < getHeight(); r++) {
						for (int c = 0; c < getWidth(); c++) {
							value = (double) getElemShort(r, c, i);
							if (value > max) {
								if (max != value)
									secondMax = max;
								max = value;
							}
							if (value < min) {
								if (min != value)
									secondMin = min;
								min = value;
							}
						}
						if (task.getEvent() != null)
							task.manageEvent(task.getEvent());
					}
				}
				break;
			case IBuffer.TYPE_INT:
				for (int i = 0; i < getBandCount(); i++) {
					for (int r = 0; r < getHeight(); r++) {
						for (int c = 0; c < getWidth(); c++) {
							value = (double) getElemInt(r, c, i);
							if (value > max) {
								if (max != value)
									secondMax = max;
								max = value;
							}
							if (value < min) {
								if (min != value)
									secondMin = min;
								min = value;
							}
						}
						if (task.getEvent() != null)
							task.manageEvent(task.getEvent());
					}
				}
				break;
			case IBuffer.TYPE_FLOAT:
				for (int i = 0; i < getBandCount(); i++) {
					for (int r = 0; r < getHeight(); r++) {
						for (int c = 0; c < getWidth(); c++) {
							value = (double) getElemFloat(r, c, i);
							if (value > max) {
								if (max != value)
									secondMax = max;
								max = value;
							}
							if (value < min) {
								if (min != value)
									secondMin = min;
								min = value;
							}
						}
						if (task.getEvent() != null)
							task.manageEvent(task.getEvent());
					}
				}
				break;
			case IBuffer.TYPE_DOUBLE:
				for (int i = 0; i < getBandCount(); i++) {
					for (int r = 0; r < getHeight(); r++) {
						for (int c = 0; c < getWidth(); c++) {
							value = getElemDouble(r, c, i);
							if (value > max) {
								if (max != value)
									secondMax = max;
								max = value;
							}
							if (value < min) {
								if (min != value)
									secondMin = min;
								min = value;
							}
						}
						if (task.getEvent() != null)
							task.manageEvent(task.getEvent());
					}
				}
				break;
		}
		// Si no existe un secondMax lo igualo al maximo existente
		if (secondMax == Double.NEGATIVE_INFINITY)
			secondMax = max;
		// Si no existe un secondMin lo igualo al minimo existente
		if (secondMin == Double.MAX_VALUE)
			secondMin = min;

		double[] values = new double[2];
		values[0] = min;
		values[1] = max;
		values[2] = secondMin;
		values[3] = secondMax;
		return values;
	}
	
	
	private double[][] getAllBandsLimits() throws InterruptedException {
		RasterTask task = RasterTaskQueue.get(Thread.currentThread().toString());
		double max[] = new double[getBandCount()];
		double min[] = new double[getBandCount()];
		double value = 0;

		for (int i = 0; i < getBandCount(); i++) {
			max[i] = Double.NEGATIVE_INFINITY;
			min[i] = Double.MAX_VALUE;
		}

		switch (getDataType()) {
			case IBuffer.TYPE_BYTE:
				for (int i = 0; i < getBandCount(); i++) {
					for (int r = 0; r < getHeight(); r++) {
						for (int c = 0; c < getWidth(); c++) {
							value = (double) (getElemByte(r, c, i) & 0xff);
							if (value > max[i])
								max[i] = value;
							if (value < min[i])
								min[i] = value;
						}
						if (task.getEvent() != null)
							task.manageEvent(task.getEvent());
					}
				}
				break;
			case IBuffer.TYPE_SHORT:
				for (int i = 0; i < getBandCount(); i++) {
					for (int r = 0; r < getHeight(); r++) {
						for (int c = 0; c < getWidth(); c++) {
							value = (double) getElemShort(r, c, i);
							if (value > max[i])
								max[i] = value;
							if (value < min[i])
								min[i] = value;
						}
						if (task.getEvent() != null)
							task.manageEvent(task.getEvent());
					}
				}
				break;
			case IBuffer.TYPE_INT:
				for (int i = 0; i < getBandCount(); i++) {
					for (int r = 0; r < getHeight(); r++) {
						for (int c = 0; c < getWidth(); c++) {
							value = (double) getElemInt(r, c, i);
							if (value > max[i])
								max[i] = value;
							if (value < min[i])
								min[i] = value;
						}
						if (task.getEvent() != null)
							task.manageEvent(task.getEvent());
					}
				}
				break;
			case IBuffer.TYPE_FLOAT:
				for (int i = 0; i < getBandCount(); i++) {
					for (int r = 0; r < getHeight(); r++) {
						for (int c = 0; c < getWidth(); c++) {
							value = (double) getElemFloat(r, c, i);
							if (value > max[i])
								max[i] = value;
							if (value < min[i])
								min[i] = value;
						}
						if (task.getEvent() != null)
							task.manageEvent(task.getEvent());
					}
				}
				break;
			case IBuffer.TYPE_DOUBLE:
				for (int i = 0; i < getBandCount(); i++) {
					for (int r = 0; r < getHeight(); r++) {
						for (int c = 0; c < getWidth(); c++) {
							value = getElemDouble(r, c, i);
							if (value > max[i])
								max[i] = value;
							if (value < min[i])
								min[i] = value;
						}
						if (task.getEvent() != null)
							task.manageEvent(task.getEvent());
					}
				}
				break;
		}
		double[][] values = new double[2][getBandCount()];

		for (int i = 0; i < getBandCount(); i++) {
			values[0][i] = min[i];
			values[1][i] = max[i];
		}
		return values;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.driver.datasetproperties.IHistogramable#getHistogram()
	 */
	public Histogram getHistogram() throws InterruptedException {
		RasterTask task = RasterTaskQueue.get(Thread.currentThread().toString());
		percent = 0;
		Histogram hist = null;
		double[][] limits = getAllBandsLimits();

		hist = new Histogram(getBandCount(), limits[0], limits[1], getDataType());
		
		hist.setNoDataValue(pageBandBuffer[0].getNoDataValue());

		for (int iBand = 0; iBand < getBandCount(); iBand++) {
			for (int row = 0; row < getHeight(); row++) {
				switch(getDataType()) {
				case IBuffer.TYPE_BYTE:
					for (int col = 0; col < getWidth(); col++) 
						hist.incrementPxValue(iBand, (double)(getElemByte(row, col, iBand) & 0xff));
					break;
				case IBuffer.TYPE_SHORT:
					for (int col = 0; col < getWidth(); col++) 
						hist.incrementPxValue(iBand, (double)(getElemShort(row, col, iBand) & 0xffff));
					break;
				case IBuffer.TYPE_INT:
					for (int col = 0; col < getWidth(); col++) 
						hist.incrementPxValue(iBand, (double)(getElemInt(row, col, iBand) & 0xffffffff));
					break;
				case IBuffer.TYPE_FLOAT:
					for (int col = 0; col < getWidth(); col++) 
						hist.incrementPxValue(iBand, (double)getElemFloat(row, col, iBand));
					break;
				case IBuffer.TYPE_DOUBLE:
					for (int col = 0; col < getWidth(); col++) 
						hist.incrementPxValue(iBand, getElemDouble(row, col, iBand));
					break;
				}
				
				if (task.getEvent() != null)
					task.manageEvent(task.getEvent());
				percent = ((iBand*getHeight() + row) * 100) /(getHeight() * getBandCount());
			}
		}
		percent = 100;
		return hist;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.util.IHistogramable#resetPercent()
	 */
	public void resetPercent() {
		percent = 0;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.util.IHistogramable#getPercent()
	 */
	public int getPercent() {
		return percent;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IBuffer#isInside(int, int)
	 */
	public boolean isInside(int x, int y) {
		 if (x < 0 || y < 0 || x >= getWidth() || y >= getHeight())
				return false;
			return true;
	}
	
	/**
	 * Libera el buffer de memoria
	 */
	public void free(){
		if(pageBandBuffer != null)
			for (int i = 0; i < pageBandBuffer.length; i++) 
				pageBandBuffer[i].free();
	}
}
