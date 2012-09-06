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
package org.gvsig.raster.grid.filter.enhancement;

import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.buffer.RasterBuffer;
import org.gvsig.raster.dataset.FileNotOpenException;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.Params;
import org.gvsig.raster.dataset.RasterDriverException;
import org.gvsig.raster.dataset.properties.DatasetListStatistics;
import org.gvsig.raster.datastruct.Histogram;
import org.gvsig.raster.datastruct.HistogramException;
import org.gvsig.raster.grid.filter.RasterFilter;
/**
 * Clase base para los filtros de ecualización de histograma.
 *
 * @version 31/05/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class EqualizationFilter extends RasterFilter {
	protected DatasetListStatistics	   stats              = null;
	protected double[]                 minBandValue	      = null;
	protected double[]                 maxBandValue	      = null;
	protected int                      nbands             = 3;
	protected int[]                    renderBands        = null;
	protected int[]                    k                  = null;                   
	public static String[]             names              = new String[] {"equalization"};
	protected Histogram                histogram          = null;
	protected long                     nPixels            = 0;
	protected int[][]                  resValues          = null;
	protected int                      nElements          = 0;
	protected int[]                    ecualizedBands     = null;

	protected double[][]               aproximationNeg    = null;
	
	protected int                      nClasses           = RasterLibrary.defaultNumberOfClasses;
	
	protected long[][]                 lahe               = null;
	protected long[][]                 laheNegative       = null;
	
	/**
	 * Construye un LinearEnhancementFilter
	 */
	public EqualizationFilter() {
		setName(names[0]);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#pre()
	 */
	public void pre() {
		raster = (IBuffer) params.get("raster");
		stats = (DatasetListStatistics) params.get("stats");
		histogram = (Histogram) params.get("histogram");
		
		if(histogram == null) {
			try {
				histogram = raster.getHistogram();
			} catch (HistogramException e) {
				exec = false;
			} catch (InterruptedException e) {
				exec = false;
			}
			//return;
		}
		
		renderBands = (int[]) params.get("renderBands");
		if(renderBands != null && renderBands.length < raster.getBandCount()) {
			int[] newRenderBands = new int[raster.getBandCount()];
			for (int i = 0; i < renderBands.length; i++)
				newRenderBands[i] = renderBands[i];
			for (int i = renderBands.length; i < newRenderBands.length; i++)
				newRenderBands[i] = i;
			renderBands = newRenderBands;
		}
		if(renderBands == null)
			renderBands = new int[]{0, 1, 2};
		
		ecualizedBands = (int[]) params.get("ecualizedBands");
		if(ecualizedBands == null)
			ecualizedBands = renderBands;
		
		height = raster.getHeight();
		width = raster.getWidth();
		nPixels = height * width;
		
		try {
			stats.calcFullStatistics();
		} catch (FileNotOpenException e) {
			exec = false;
		} catch (RasterDriverException e) {
			exec = false;
		} catch (InterruptedException e) {
			exec = false;
		}

		if(raster.getDataType() == IBuffer.TYPE_BYTE) {
			minBandValue = stats.getMinRGB();
			maxBandValue = stats.getMaxRGB();
		} 
		if((minBandValue == null || maxBandValue == null) || (minBandValue[0] == 0 && maxBandValue[0] == 0)) {
			minBandValue = new double[raster.getBandCount()];
			maxBandValue = new double[raster.getBandCount()];
			if(raster.getDataType() == IBuffer.TYPE_BYTE) {
				for (int i = 0; i < minBandValue.length; i++) {
					minBandValue[i] = 0;
					maxBandValue[i] = 255;
				}
			} else {
				minBandValue = stats.getMin();
				maxBandValue = stats.getMax();
			}
		}
				
		Histogram rgbHistogram = null;
		try {
			if(raster.getDataType() == IBuffer.TYPE_BYTE)
				rgbHistogram = Histogram.convertHistogramToRGB(raster.getHistogram());
			else 
				rgbHistogram = raster.getHistogram();
		} catch (HistogramException e) {
			return;
		} catch (InterruptedException e) {
			return;
		}
		double[][] accumNormalize = Histogram.convertTableToNormalizeAccumulate(rgbHistogram.getTable());
		double[][] accumNormalizeNeg = Histogram.convertTableToNormalizeAccumulate(rgbHistogram.getNegativeTable());
		
		int value = 255;
		if(raster.getDataType() != RasterBuffer.TYPE_BYTE)
			value = nClasses;
		
		lahe = lahe(accumNormalize, value);
		laheNegative = lahe(accumNormalizeNeg, value);
		nElements = (laheNegative[0].length - 1);
		
		nbands = stats.getBandCount();
		rasterResult = RasterBuffer.getBuffer(raster.getDataType(), raster.getWidth(), raster.getHeight(), raster.getBandCount(), true);
	}

	/**
	 * Método lahe para la ecualización. Cada posición del array resultante tendrá el valor de salida para
	 * un valor de entrada dado.
	 * @param accumNorm Histograma acumulado
	 * @param value Valor máximo
	 * @return
	 */
	private long[][] lahe(double[][] accumNorm, int value) {
		long[][] res = new long[accumNorm.length][accumNorm[0].length];
		for (int i = 0; i < res.length; i++) 
			  for (int j = 0; j < res[i].length; j++) 
				  res[i][j] = Math.round(accumNorm[i][j] * value);
		return res;
	}
	
	/**
	 * Consulta si la ecualización está activa para una banda o no
	 * @param band Número de banda a consultar si se ha activado la ecualización
	 * @return true si está activa para esa banda y false si no lo está.
	 */
	protected boolean equalizationActive(int band) {
		for (int i = 0; i < ecualizedBands.length; i++) {
			if(band == ecualizedBands[i])
				return true;
		}
		return false;
	}
		
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#getOutRasterDataType()
	 */
	public int getOutRasterDataType() {
		return IBuffer.TYPE_BYTE;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#getResult(java.lang.String)
	 */
	public Object getResult(String name) {
		if (name.equals("raster")) {
			if (!exec)
				return this.raster;
			return this.rasterResult;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#getGroup()
	 */
	public String getGroup() {
		return "realces";
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#getUIParams()
	 */
	public Params getUIParams(String nameFilter) {
		Params params = new Params();
		return params;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#post()
	 */
	public void post() {
		// En caso de que nadie apunte a raster, se liberará su memoria.
		raster = null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#getInRasterDataType()
	 */
	public int getInRasterDataType() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#process(int, int)
	 */
	public void process(int x, int y) {
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#getNames()
	 */
	public String[] getNames() {
		return names;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#isVisible()
	 */
	public boolean isVisible() {
		return true;
	}
}