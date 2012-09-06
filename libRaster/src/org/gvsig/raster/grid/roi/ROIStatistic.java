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

package org.gvsig.raster.grid.roi;

import org.gvsig.raster.buffer.RasterBufferInvalidAccessException;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.grid.GridException;

/**
 * Estadísticas asociadas a una ROI.
 * 
 * Diego Guerrero Sevilla (diego.guerrero@uclm.es)
 *
 */
public class ROIStatistic {
	private ROI roi = null;
	private int[] 				values = null;
	private double[] 			max = null;
	private double[] 			min = null;
	private double[] 			mean = null;
	private double[] 			variance = null;
	private boolean 			statisticsCalculated = false;
	private boolean 			advancedStatisticsCalculated=false;
	protected int				bandToOperate = 0;
	/**
	 *  Matriz de varianza covarianza
	 */
	double [][]					varCov=null;
	
	public ROIStatistic(ROI roi) {
		this.roi = roi;
	}
		
	/**
	 * Calcula las estadísticas básicas (Max, Min, Media, Varianza).
	 * 
	 * @throws RasterBufferInvalidAccessException
	 */
	public void calculateStatistics() throws GridException {
		int x, y;
		double z;
		int bandCount = roi.getGrid().getBandCount();
		values = new int[bandCount];
		mean = new double[bandCount];
		min = new double[bandCount];
		max = new double[bandCount];
		variance = new double[bandCount];
		
		
		
		for (int iBand = 0; iBand < bandCount; iBand++) {
			mean[iBand]	= 0.0;
			variance[iBand]	= 0.0;
			values[iBand]	= 0;
		}
		int oldBandToOperate = bandToOperate;
		statisticsCalculated = true;
		
		for (int iBand = 0; iBand < bandCount; iBand++) {
			roi.setBandToOperate(iBand);
			for (y = 0; y < roi.getNY(); y++){
				for (x = 0; x < roi.getNX(); x++){
					z = getValue(x, y);
					if(!roi.isNoDataValue(z))	{
						if( values[iBand] == 0 )
							min[iBand] = max[iBand] = z;
						else if( min[iBand] > z )
							min[iBand]	= z;
						else if( max[iBand] < z )
							max[iBand]	= z;

						mean[iBand]	+= z;
						variance[iBand] += z * z;
						values[iBand]++;
					}
				}
			}
		}
		
		for (int iBand = 0; iBand < bandCount; iBand++) {
			if( values[iBand] > 0 ){
				mean[iBand]	/= (double) values[iBand];
				variance[iBand] = variance[iBand] / (double) values[iBand] - mean[iBand] * mean[iBand];
			}
		}
		bandToOperate = oldBandToOperate;
		statisticsCalculated = true;
	}
	
	/**
	 * Calculo de estadisticas avanzadas (Matriz varianza- covarianza).
	 */
	 public void calculateAdvancedStatistic() throws GridException {
		 
		 	if (!isStatisticsCalculated())
				calculateStatistics();
				
			
			int bandCount = roi.getBandCount();
			double dSum[][] = new double[bandCount][bandCount];
			double iValues[][] = new double[bandCount][bandCount];
			varCov=new double[bandCount][bandCount];
			double valorBandai=0, valorBandaj=0;
			
			for (int iBand = 0; iBand < bandCount; iBand++)
				for (int jBand = 0; jBand < bandCount; jBand++){
					dSum[iBand][jBand]=0;
					iValues[iBand][jBand]=0;
			}
		
			
			for (int k=0; k<roi.getNY(); k++){
				for (int l=0;l<roi.getNX();l++){
					for (int i = 0; i < bandCount; i++) {
						for (int j = i; j < bandCount; j++) {
							roi.setBandToOperate(i);
							valorBandai=getValue(l, k);
							roi.setBandToOperate(j);
							valorBandaj=getValue(l,k);
							if (!roi.isNoDataValue(valorBandai)&&!roi.isNoDataValue(valorBandaj)){
								valorBandai=valorBandai-mean[i];
								valorBandaj=valorBandaj- mean[j];
								dSum[i][j] += valorBandai*valorBandaj;
								iValues[i][j]++;
							}
						}
					}
				}
			}
			
			// Asigno el valor a la matriz
			for (int iBand = 0; iBand < bandCount; iBand++) 
				for (int jBand = 0; jBand < bandCount; jBand++)
					if (iValues[iBand][jBand]>1)
						varCov[iBand][jBand]=dSum[iBand][jBand]/(double)(iValues[iBand][jBand]);	
					else
						varCov[iBand][jBand]= roi.getGrid().getNoDataValue();
			
			// Completar parte simetrica de la matriz
			for (int i = 0; i < bandCount; i++){
				for (int j = 0; j < bandCount; j++) {
					if(j<i)
						varCov[i][j]=varCov[j][i];
				}
			}
			advancedStatisticsCalculated=true;
	}

	/**
	 * Obtiene un valor de una celda del grid independientemente del tipo de dato.
	 * @param x Posición en X
	 * @param y Posición en Y
	 * @return Valor de la celda en formato double
	 * @throws RasterBufferInvalidAccessException 
	 * @throws RasterBufferInvalidAccessException
	 */
	private double getValue(int x, int y) throws GridException {
		switch(roi.getGrid().getDataType()) {
		case IBuffer.TYPE_BYTE:return (double)(roi.getCellValueAsByte(x, y));
		case IBuffer.TYPE_SHORT:return (double)(roi.getCellValueAsShort(x, y));
		case IBuffer.TYPE_INT:return (double)(roi.getCellValueAsInt(x, y));
		case IBuffer.TYPE_FLOAT:return (double)roi.getCellValueAsFloat(x, y);
		case IBuffer.TYPE_DOUBLE:return (double)roi.getCellValueAsDouble(x, y);
		}
		return 0;
	}
	
	/**
	 * Obtiene el valor máximo del grid
	 * @return Valor máximo
	 */
	public double getMax() {
		return max[bandToOperate];
	}

	/**
	 * Obtiene el valor médio del grid
	 * @return Valor medio
	 */
	public double getMean() {
		return mean[bandToOperate];
	}

	/**
	 * Obtiene el valor míximo del grid
	 * @return Valor mínimo
	 */
	public double getMin() {
		return min[bandToOperate];
	}

	/**
	 * Consulta si las estadisticas han sido calculadas o no.
	 * @return true si las estadisticas del grid están calculadas y false si no lo están 
	 */
	public boolean isStatisticsCalculated() {
		return statisticsCalculated;
	}
	
	/**
	 * Asigna el flag que informa si las estadisticas están calculadas para un grid concreto
	 * @param calc true si están calculadas y false si no lo están
	 */
	public void setStatisticsCalculated(boolean calc){
		this.statisticsCalculated = calc;
	}

	/**
	 * Obtiene la varianza
	 * @return Varianza
	 */
	public double getVariance() {
		return variance[bandToOperate];
	}
	
	/**
	 * Asigna la banda sobre la que se realizan las operaciones. Por defecto es la banda 0
	 * con lo que para el uso de MDTs no habrá que modificar este valor.
	 * @param band Banda sobre la que se realizan las operaciones.
	 */
	public void setBandToOperate(int band){
		this.bandToOperate = band;
	}
	
	
	/**
	* Devuelve la matriz de varianza-covarianza, si no se encuentra calculada se calcula
	* @retrun Matriz de varianza-covarianza
	*/
	public double[][] getVarianceCovarianceMatrix(){
		return varCov;
	}
	 
	public boolean isAdvancedStatisticsCalculated(){
		return advancedStatisticsCalculated;
		
	}

	public void setAdvancedStatisticCalculated(
			boolean advancedStatisticCalculated) {
		this.advancedStatisticsCalculated = advancedStatisticCalculated;
	}

	/**
	 * 
	 * @return número de celdas del ROI.
	 */
	public int getValues() {
		return values[bandToOperate];
	}
 
}
