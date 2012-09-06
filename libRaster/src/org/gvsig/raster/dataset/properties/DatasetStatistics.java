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
package org.gvsig.raster.dataset.properties;

import java.util.ArrayList;
import java.util.Hashtable;

import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.dataset.FileNotOpenException;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.InvalidSetViewException;
import org.gvsig.raster.dataset.RasterDataset;
import org.gvsig.raster.dataset.RasterDriverException;
import org.gvsig.raster.dataset.serializer.RmfSerializerException;
import org.gvsig.raster.hierarchy.IStatistics;
import org.gvsig.raster.process.RasterTask;
import org.gvsig.raster.process.RasterTaskQueue;
/**
 * Estadisticas asociadas a un fichero raster.
 *  
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class DatasetStatistics implements IStatistics {
	
	/*
	 * Esta a false si las estadisticas no son del fichero completo. Esto es posible porque podemos
	 * tener unas estadísticas calculadas a partir de una petición con subsampleo. Hay que tener en
	 * cuenta que el raster puede ser muy grande y este calculo muy costoso.
	 */
	protected boolean       complete       = false;
	protected double[]      max            = null;
	protected double[]      min            = null;
	protected double[]      secondMax      = null;
	protected double[]      secondMin      = null;

	protected double[]      maxRGB         = null;
	protected double[]      minRGB         = null;
	protected double[]      secondMaxRGB   = null;
	protected double[]      secondMinRGB   = null;

	protected double[]      mean           = null;
	protected double[]      variance       = null;

	protected String        fName          = null;
	protected RasterDataset dataset        = null;
	protected boolean       calculated     = false;
	protected Hashtable     tailTrim       = new Hashtable();
	protected ArrayList     tailTrimValues = new ArrayList();
	private int             bandCount      = 0;
	private int             percent        = 0;
	private boolean         forceToRecalc  = false;
	
	/**
	 * Constructor. Asigna el fichero asociado.
	 */
	public DatasetStatistics(RasterDataset grf){
		this.dataset = grf;
		if(dataset != null)
			bandCount = dataset.getBandCount();
	}
	
	/**
	 * Obtiene el dataset asociado
	 * @return
	 */
	public RasterDataset getDataset() {
		return dataset;
	}
	
	/**
	 * Asigna el valor máximo del grid
	 * @return Valor máximo
	 */
	public void setMax(double[] max) {
		this.max = max;
	}

	/**
	 * Asigna el valor del segundo máximo
	 * @return Valor del segundo máximo
	 */
	public void setSecondMax(double[] smax) {
		this.secondMax = smax;
	}
	
	/**
	 * Asigna el valor máximo del grid
	 * @return Valor máximo
	 */
	public void setMaxRGB(double[] max) {
		this.maxRGB = max;
	}

	/**
	 * Asigna el valor del segundo máximo
	 * @return Valor del segundo máximo
	 */
	public void setSecondMaxRGB(double[] smax) {
		this.secondMaxRGB = smax;
	}
	
	/**
	 * Asigna el valor médio del grid
	 * @return Valor medio
	 */
	public void setMean(double[] mean) {
		this.mean = mean;
	}

	/**
	 * Asigna el valor míximo del grid
	 * @return Valor mínimo
	 */
	public void setMin(double[] min) {
		this.min = min;
	}

	/**
	 * Asigna el valor del segundo mínimo
	 * @return Valor del segundo mínimo
	 */
	public void setSecondMin(double[] smin) {
		this.secondMin = smin;
	}
	
	/**
	 * Asigna el valor míximo del grid
	 * @return Valor mínimo
	 */
	public void setMinRGB(double[] min) {
		this.minRGB = min;
	}

	/**
	 * Asigna el valor del segundo mínimo
	 * @return Valor del segundo mínimo
	 */
	public void setSecondMinRGB(double[] smin) {
		this.secondMinRGB = smin;
	}
	
	/**
	 * Asigna la varianza
	 * @return Varianza
	 */
	public void setVariance(double[] variance) {
		this.variance = variance;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.hierarchy.IStatistics#getMin()
	 */
	public double[] getMin() {
		return min;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.hierarchy.IStatistics#getMax()
	 */
	public double[] getMax() {
		return max;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.hierarchy.IStatistics#getSecondMax()
	 */
	public double[] getSecondMax() {
		return secondMax;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.hierarchy.IStatistics#getSecondMin()
	 */
	public double[] getSecondMin() {
		return secondMin;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.hierarchy.IStatistics#getMinRGB()
	 */
	public double[] getMinRGB() {
		return minRGB;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.hierarchy.IStatistics#getMaxRGB()
	 */
	public double[] getMaxRGB() {
		return maxRGB;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.hierarchy.IStatistics#getSecondMaxRGB()
	 */
	public double[] getSecondMaxRGB() {
		return secondMaxRGB;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.hierarchy.IStatistics#getSecondMinRGB()
	 */
	public double[] getSecondMinRGB() {
		return secondMinRGB;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.hierarchy.IStatistics#getMaximun()
	 */
	public double getMaximun() {
		double m = Double.NEGATIVE_INFINITY;
		for (int i = 0; i < max.length; i++)
			m = Math.max(m, max[i]);
		return m;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.hierarchy.IStatistics#getMinimun()
	 */
	public double getMinimun() {
		double m = Double.MAX_VALUE;
		for (int i = 0; i < min.length; i++)
			m = Math.min(m, min[i]);
		return m;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.hierarchy.IStatistics#getMaximunRGB()
	 */
	public double getMaximunRGB() {
		double m = Double.NEGATIVE_INFINITY;
		for (int i = 0; i < maxRGB.length; i++)
			m = Math.max(m, maxRGB[i]);
		return m;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.hierarchy.IStatistics#getMinimunRGB()
	 */
	public double getMinimunRGB() {
		double m = Double.MAX_VALUE;
		for (int i = 0; i < minRGB.length; i++)
			m = Math.min(m, minRGB[i]);
		return m;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.hierarchy.IStatistics#getMean()
	 */
	public double[] getMean() {
		return mean;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.hierarchy.IStatistics#getVariance()
	 */
	public double[] getVariance() {
		return variance;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.hierarchy.IStatistics#getBandCount()
	 */
	public int getBandCount(){
		return this.bandCount;
	}
	
	/**
	 * Asigna el número de bandas
	 * @param bandCount
	 */
	public void setBandCount(int bandCount) {
		this.bandCount = bandCount;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.hierarchy.IStatistics#calcFullStatistics()
	 */
	public void calcFullStatistics() throws FileNotOpenException, RasterDriverException, InterruptedException {
		if (dataset == null)
			return;
		
		RasterTask task = RasterTaskQueue.get(Thread.currentThread().toString());
		percent = 0;
		
		// Si no se fuerza su calculo, intentamos ver si estan calculadas y sino
		// las intentamos cargar del RMF
		if (!forceToRecalc) {
			if (!isCalculated()) {
				try {
					dataset.loadObjectFromRmf(DatasetStatistics.class, this);
				} catch (RmfSerializerException e) {
					// Si no se puede cargar del RMF, recalcularemos las estadisticas.
				}
			}
			if (isCalculated())
				return;
		}

		bandCount = dataset.getBandCount();
		max = new double[bandCount];
		min = new double[bandCount];
		secondMax = new double[bandCount];
		secondMin = new double[bandCount];
		maxRGB = new double[bandCount];
		minRGB = new double[bandCount];
		secondMaxRGB = new double[bandCount];
		secondMinRGB = new double[bandCount];
		mean = new double[bandCount];
		variance = new double[bandCount];

		int blockHeight = RasterLibrary.blockHeight;
		long[] iValues = new long[bandCount];
		boolean[] initializedBand = new boolean[bandCount];
		int[] type = new int[bandCount];
		byte[][][] b = null;
		short[][][] s = null;
		int[][][] i = null;
		float[][][] f = null;
		double[][][] d = null;
		double z = 0;
		double rgb = 0;

		for (int iBand = 0; iBand < bandCount; iBand++) {
			max[iBand] = Double.NEGATIVE_INFINITY;
			min[iBand] = Double.POSITIVE_INFINITY;
			secondMax[iBand] = Double.NEGATIVE_INFINITY;
			secondMin[iBand] = Double.POSITIVE_INFINITY;
			maxRGB[iBand] = 0;
			minRGB[iBand] = 255;
			secondMaxRGB[iBand] = 0;
			secondMinRGB[iBand] = 255;
			initializedBand[iBand] = false;
			type[iBand] = dataset.getDataType()[iBand];
		}

		for (int height = 0; height < dataset.getHeight(); height += blockHeight) {
			try {
				Object buf = dataset.readBlock(height, blockHeight);
				switch (type[0]) {
					case IBuffer.TYPE_BYTE:	  b = (byte[][][]) buf; break;
					case IBuffer.TYPE_SHORT:  s = (short[][][]) buf; break;
					case IBuffer.TYPE_FLOAT:  f = (float[][][]) buf; break;
					case IBuffer.TYPE_DOUBLE: d = (double[][][]) buf; break;
					case IBuffer.TYPE_INT:    i = (int[][][]) buf; break;
				}
			} catch (InvalidSetViewException e) {
				// La vista se asigna automáticamente
				return;
			}
			
			int hB = blockHeight;
			if ((height + hB) > dataset.getHeight())
				hB = dataset.getHeight() - height;
			for (int iBand = 0; iBand < bandCount; iBand++) {
				for (int col = 0; col < dataset.getWidth(); col++) {
					for (int row = 0; row < hB; row++) {
						z = (b != null) ? b[iBand][row][col] :
								(s != null) ? s[iBand][row][col] :
								(d != null) ? d[iBand][row][col] :
								(f != null) ? f[iBand][row][col] :
								(i != null) ? i[iBand][row][col] :
								0;

						if (dataset.isNoDataEnabled() && (z == dataset.getNoDataValue()))
							continue;

						if (Double.isNaN(z))
							continue;

						rgb = (b != null) ? ((byte) z) & 0xff : 0;

						mean[iBand] += z;
						variance[iBand] += z * z;
						iValues[iBand]++;

						if (!initializedBand[iBand]) {
							secondMin[iBand] = min[iBand];
							secondMax[iBand] = max[iBand];
							min[iBand] = z;
							max[iBand] = z;
							secondMinRGB[iBand] = minRGB[iBand];
							secondMaxRGB[iBand] = maxRGB[iBand];
							minRGB[iBand] = rgb;
							maxRGB[iBand] = rgb;
							initializedBand[iBand] = true;
							continue;
						}

						if (z < secondMin[iBand]) {
							if (z < min[iBand]) {
								secondMin[iBand] = min[iBand];
								min[iBand] = z;
							} else {
								if (z > min[iBand])
									secondMin[iBand] = z;
							}
						}

						if (z > secondMax[iBand]) {
							if (z > max[iBand]) {
								secondMax[iBand] = max[iBand];
								max[iBand] = z;
							} else {
								if (z < max[iBand])
									secondMax[iBand] = z;
							}
						}

						if (rgb < secondMinRGB[iBand]) {
							if (rgb < minRGB[iBand]) {
								secondMinRGB[iBand] = minRGB[iBand];
								minRGB[iBand] = rgb;
							} else {
								if (rgb > minRGB[iBand])
									secondMinRGB[iBand] = rgb;
							}
						}

						if (rgb > secondMaxRGB[iBand]) {
							if (rgb > maxRGB[iBand]) {
								secondMaxRGB[iBand] = maxRGB[iBand];
								maxRGB[iBand] = rgb;
							} else {
								if (rgb < maxRGB[iBand])
									secondMaxRGB[iBand] = rgb;
							}
						}
					}
				}
				if (task.getEvent() != null)
					task.manageEvent(task.getEvent());
			}
			percent = ((height * 100) / dataset.getHeight());
		}
		percent = 100;

		for (int iBand = 0; iBand < bandCount; iBand++) {
			if (iValues[iBand] > 0) {
				mean[iBand] = mean[iBand] / (double) iValues[iBand];
				variance[iBand] = variance[iBand] / (double) iValues[iBand] - mean[iBand] * mean[iBand];
			}
		}

		calculated = true;
		forceToRecalc = false;
		try {
			dataset.saveObjectToRmf(DatasetStatistics.class, this);
		} catch (RmfSerializerException e) {
			// No salva a rmf
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.hierarchy.IStatistics#isCalculated()
	 */
	public boolean isCalculated() {
		return calculated;
	}
	
	/**
	 * Asigna el flag de estadísticas calculadas.
	 * @param calc
	 */
	public void setCalculated(boolean calc) {
		calculated = calc;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.hierarchy.IStatistics#setTailTrimValue(double, java.lang.Object)
	 */
	public void setTailTrimValue(double percent, Object valueByBand){
		tailTrim.put(percent + "", valueByBand);
		for (int i = 0; i < tailTrimValues.size(); i++) {
			if(tailTrimValues.get(i).equals(percent + "")) {
				tailTrimValues.set(i, percent + "");
				return;
			}
		}
		tailTrimValues.add(percent + "");
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.hierarchy.IStatistics#getTailTrimValue(double)
	 */
	public Object getTailTrimValue(double percent){
		return tailTrim.get(percent + "");
	}
	
	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.raster.shared.IStatistics#getTailTrimValue(int)
	 */
	public Object[] getTailTrimValue(int pos) {
		return new Object[] { Double.valueOf(Double.parseDouble(tailTrimValues.get(pos) + "")), tailTrim.get(tailTrimValues.get(pos)) };
	}
	
	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.raster.shared.IStatistics#getTailTrimCount()
	 */
	public int getTailTrimCount() {
		return tailTrimValues.size();
	}

	/**
	 * Pone a cero el porcentaje de progreso del proceso de calculo de histograma
	 */
	public void resetPercent() {
		percent = 0;
	}

	/**
	 * Obtiene el porcentaje de progreso del proceso de calculo de histograma
	 * @return porcentaje de progreso
	 */
	public int getPercent() {
		return percent;
	}

	/**
	 * Cuando se llama a este método fuerza que la siguiente petición de estadísticas 
	 * no sea leída de RMF y sean recalculadas por completo.
	 * @param forceToRecalc
	 */
	public void forceToRecalc() {
		this.forceToRecalc = true;
	}
}