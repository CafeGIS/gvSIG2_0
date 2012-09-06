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
package org.gvsig.raster.dataset.properties;

import org.gvsig.raster.dataset.FileNotOpenException;
import org.gvsig.raster.dataset.IRasterDataSource;
import org.gvsig.raster.dataset.RasterDriverException;
import org.gvsig.raster.datastruct.Histogram;

/**
 * Clase para la gestión de histogramas de un raster formado por una lista de ficheros.
 * Para devolver un histograma pedirá el histograma a todos los ficheros que
 * componen el multifichero.
 * 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class DatasetListHistogram {
	/**
	 * Histograma de la imagen completa
	 */
	private Histogram						histogram = null;
	/**
	 * Dataset del cual se calcula el histograma
	 */
	private IRasterDataSource               raster = null;
	
	/**
	 * Constructor
	 * @param dataset
	 */
	public DatasetListHistogram(IRasterDataSource raster){
		this.raster = raster;
	}
	
	/**
	 * Obtiene el histograma. Pregunta a todos los datasets que forman el multidataset
	 * por su histograma y luego los fusiona.
	 * @return histograma 
	 */
	public Histogram getHistogram()
		throws FileNotOpenException, RasterDriverException, InterruptedException {
		if (raster != null) {
			//Obtenemos los histogramas de cada dataset
			Histogram[] hList = new Histogram[raster.getDatasetCount()];
			
			for (int i = 0; i < hList.length; i++) {
				hList[i] = raster.getDataset(i)[0].getHistogram().getHistogram();
				if (hList[i] == null) 
					return null;
			}
			
			if (hList[0].getNumBands() == 0)
				return null;
			
			raster.getStatistics().calcFullStatistics();

			histogram = new Histogram(raster.getBandCount(), raster.getStatistics().getMin(), raster.getStatistics().getMax(), raster.getDataType()[0]);
			
			int band = 0;
			for (int iDataset = 0; iDataset < hList.length; iDataset++) {
				for (int iBand = 0; iBand < hList[iDataset].getNumBands(); iBand++) {
					for (int iPxValue = 0; iPxValue < hList[iDataset].getBandLenght(iBand); iPxValue ++) {
						histogram.setHistogramValueByPos(band, iPxValue, (long) hList[iDataset].getHistogramValueByPos(iBand, iPxValue));
					}
					band ++;
				}	
			}
			
			return histogram;
		}
		return null;
	}

	/**
	 * Pone a cero el porcentaje de progreso del proceso de calculo de histograma
	 */
	public void resetPercent() {
		for (int i = 0; i < raster.getDatasetCount(); i++) 
			raster.getDataset(i)[0].resetPercent();
	}

	/**
	 * Obtiene el porcentaje de proceso en la construcción del histograma,
	 * @return porcentaje de progreso
	 */
	public int getPercent() {
		int percent = 0;
		for (int i = 0; i < raster.getDatasetCount(); i++) 
			percent += raster.getDataset(i)[0].getPercent();
		percent = percent / raster.getDatasetCount();
		return percent;
	}
}
