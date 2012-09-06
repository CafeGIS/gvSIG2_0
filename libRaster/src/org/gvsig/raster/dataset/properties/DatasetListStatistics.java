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

import java.util.ArrayList;

import org.gvsig.raster.dataset.FileNotOpenException;
import org.gvsig.raster.dataset.IRasterDataSource;
import org.gvsig.raster.dataset.MultiRasterDataset;
import org.gvsig.raster.dataset.RasterDataset;
import org.gvsig.raster.dataset.RasterDriverException;
import org.gvsig.raster.dataset.serializer.RmfSerializerException;


/**
 * Estadisticas asociadas a un fichero raster formado por multiples ficheros.
 *  
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class DatasetListStatistics extends DatasetStatistics {
	 
	private DatasetStatistics[]     statList = null;
	private RasterDataset[]         datasetList = null;
	private int                     nDataset = 0;
	
	/**
	 * Carga el objeto DatasetListStatistics desde los datos del RMF. 
	 * @param datasource
	 * @return
	 * @throws RmfSerializerException
	 */
	public static DatasetListStatistics loadDatasetListStatistics(IRasterDataSource datasource) throws RmfSerializerException {
		DatasetStatistics[] stats = new DatasetStatistics[datasource.getDatasetCount()];
		boolean isCalc = true;
		for (int i = 0; i < datasource.getDatasetCount(); i++) {
			DatasetStatistics statFile = ((RasterDataset) datasource.getDataset(i)[0]).getStatistics();
			stats[i] = (DatasetStatistics)((RasterDataset) datasource.getDataset(i)[0]).loadObjectFromRmf(DatasetStatistics.class, statFile);
			
			//Con que un dataset no tenga la estadistica calculada se pone a no calculado el DatasetListStatistics
			if(!stats[i].isCalculated()) 
				isCalc = false;
		}
		DatasetListStatistics result = new DatasetListStatistics(stats);
		result.setCalculated(isCalc);
		return result;
	}
	
	/**
	 * Constructor
	 */
	public DatasetListStatistics(RasterDataset[] datasetList) {
		super(null);
		this.datasetList = datasetList;
		statList = new DatasetStatistics[datasetList.length];
		for(int i = 0; i < datasetList.length; i ++)
			statList[i] = this.datasetList[i].getStatistics();
	}
	
	/**
	 * Constructor
	 */
	public DatasetListStatistics(ArrayList datasetList) {
		super(null);
		statList = new DatasetStatistics[datasetList.size()];
		this.datasetList = new RasterDataset[datasetList.size()];
		for(int i = 0; i < datasetList.size(); i ++){
			this.datasetList[i] = (RasterDataset)datasetList.get(i);
			if(this.datasetList[i] != null)
				statList[i] = this.datasetList[i].getStatistics();
		}
	}
	
	/**
	 * Constructor
	 */
	public DatasetListStatistics(DatasetStatistics[] statList) {
		super(null);
		this.statList = statList;
		datasetList = new RasterDataset[statList.length];
		for(int i = 0; i < statList.length; i ++)
			datasetList[i] = this.statList[i].getDataset();
		int len = 0;
		for(nDataset = 0; nDataset < statList.length; nDataset ++) {
			try {
				statList[nDataset].calcFullStatistics();
			} catch (FileNotOpenException e) {
			} catch (RasterDriverException e) {
			} catch (InterruptedException e) {
			}
			len += datasetList[nDataset].getBandCount();
		}
		constructStats(len);
	}
	
	/**
	 * Constructor
	 */
	public DatasetListStatistics(MultiRasterDataset[][] mosaic) {
		super(null);
		int n = mosaic.length;
		int m = mosaic[0].length;
		if(mosaic == null || mosaic[0][0] == null)
			return;
		int nDat = mosaic[0][0].getDatasetCount();
		this.datasetList = new RasterDataset[n * m * nDat];
		int count = 0;
		for (int row = 0; row < n; row++) {
			for (int col = 0; col < m; col++) {
				for (int i = 0; i < mosaic[row][col].getDatasetCount(); i++) {
					datasetList[count] = mosaic[row][col].getDataset(i)[0];
					count ++;
				}
			}
		}
	
		statList = new DatasetStatistics[datasetList.length];
		for(int i = 0; i < datasetList.length; i ++)
			statList[i] = this.datasetList[i].getStatistics();
	}
	
	/**
	 * Añade un dataset a la lista de estadisticas
	 * @param dataset
	 */
	public void addDataset(RasterDataset dataset) {
		DatasetStatistics[] statListAux = new DatasetStatistics[datasetList.length + 1];
		RasterDataset[] datasetListAux = new RasterDataset[datasetList.length + 1];
		for(int i = 0; i < datasetList.length; i ++) {
			datasetListAux[i] = datasetList[i];
			statListAux[i] = statList[i];
		}
		datasetListAux[datasetList.length] = dataset;
		statListAux[datasetList.length] = dataset.getStatistics();
		datasetList = datasetListAux;
		statList = statListAux;
	}
	
	/**
	 * Calcula las estadisticas recorriendo todo el fichero.
	 */
	public void calcFullStatistics()throws FileNotOpenException, RasterDriverException, InterruptedException {
//		long t2;
//		long t1 = new Date().getTime();
		int len = 0;
		for(nDataset = 0; nDataset < statList.length; nDataset ++) {
			statList[nDataset].calcFullStatistics();
			len += datasetList[nDataset].getBandCount();
		}
				
		try {
			constructStats(len);
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new RasterDriverException("Error en el acceso al array de máximos y mínimos");
		}
		
		calculated = true;
//		t2 = new Date().getTime();
//		System.out.println("Estadisticas MultiFile: " + ((t2 - t1) / 1000D) + ", secs.");
	}
	
	/**
	 * Obtiene el porcentaje de progreso del proceso de calculo de histograma
	 * @return porcentaje de progreso
	 */
	public int getPercent() {
		try {
			return (int)(statList[nDataset].getPercent() / statList.length);
		} catch (ArrayIndexOutOfBoundsException e) {
			return 0;
		}
	}
	
	/**
	 * Cuando se llama a este método fuerza que la siguiente petición de estadísticas 
	 * no sea leída de RMF y sean recalculadas por completo.
	 * @param forceToRecalc
	 */
	public void forceToRecalc() {
		for(int i = 0; i < statList.length; i ++) 
			statList[i].forceToRecalc();
	}
	
	/**
	 * Construye todos los datos del DatasetStadistics actual a partir de toda la
	 * lista de DatasetStatistics que tiene este objeto.
	 * 
	 * @param len
	 * @throws ArrayIndexOutOfBoundsException
	 */
	private void constructStats(int len) throws ArrayIndexOutOfBoundsException {
		max = new double[len];
		min = new double[len];
		secondMax = new double[len];
		secondMin = new double[len];
		maxRGB = new double[len];
		minRGB = new double[len];
		secondMaxRGB = new double[len];
		secondMinRGB = new double[len];
		mean = new double[len];
		variance = new double[len];
			
		int count = 0;
		for(int i = 0; i < statList.length; i ++) {
			if(statList[i].getMax() == null)
				return;
			for(int j = 0; j < statList[i].getMax().length; j ++) {
				max[count] = statList[i].getMax()[j];
				min[count] = statList[i].getMin()[j];
				secondMax[count] = statList[i].getSecondMax()[j];
				secondMin[count] = statList[i].getSecondMin()[j];
				maxRGB[count] = statList[i].getMaxRGB()[j];
				minRGB[count] = statList[i].getMinRGB()[j];
				secondMaxRGB[count] = statList[i].getSecondMaxRGB()[j];
				secondMinRGB[count] = statList[i].getSecondMinRGB()[j];
				mean[count] = statList[i].getMean()[j];
				variance[count] = statList[i].getVariance()[j];
				count ++;
			}
		}
	}

	/**
	 * Obtiene el flag que informa de si las estadísticas están calculadas o no.
	 * @return true indica que están calculadas y false que no lo están
	 */
	public boolean isCalculated() {
		return calculated;
	}
	
	/**
	 * Número de bandas
	 * @return
	 */
	public int getBandCount() {
		int bandCount = 0;
		for(int i = 0; i < statList.length; i ++)
			bandCount += statList[i].getBandCount();
		return bandCount;
	}
}