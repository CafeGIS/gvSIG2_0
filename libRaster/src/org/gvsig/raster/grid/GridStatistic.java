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
package org.gvsig.raster.grid;

import org.gvsig.raster.buffer.RasterBufferInvalidAccessException;
import org.gvsig.raster.dataset.IBuffer;


/**
 * Estadisticas de un grid
 * @author Victor Olaya (volaya@ya.com)
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class GridStatistic {
	private double[] 			max = null;
	private double[] 			min = null;
	private double[] 			mean = null;
	private double[] 			variance = null;
	private boolean 			statisticsCalculated = false;
	private Grid				grid = null;
	protected int				bandToOperate = 0;
	
	public GridStatistic(Grid grid){
		this.grid = grid;
	}
	
	public void calculateStatistics() throws GridException {
		int x, y;
		double z;
		int[] iValues = new int[grid.getBandCount()];
		mean = new double[grid.getBandCount()];
		min = new double[grid.getBandCount()];
		max = new double[grid.getBandCount()];
		variance = new double[grid.getBandCount()];
		
		for (int iBand = 0; iBand < grid.getBandCount(); iBand++) {
			mean[iBand]	= 0.0;
			variance[iBand]	= 0.0;
			iValues[iBand]	= 0;
		}
		int oldBandToOperate = bandToOperate;
		statisticsCalculated = true;
		
		for (int iBand = 0; iBand < grid.getBandCount(); iBand++) {
			grid.setBandToOperate(iBand);
			for (y = 0; y < grid.getNY(); y++){
				for (x = 0; x < grid.getNX(); x++){
					z = getValue(x, y);
					if( !grid.isNoDataValue(z))	{
						if( iValues[iBand] == 0 )
							min[iBand] = max[iBand] = z;
						else if( min[iBand] > z )
							min[iBand]	= z;
						else if( max[iBand] < z )
							max[iBand]	= z;

						mean[iBand]	+= z;
						variance[iBand] += z * z;
						iValues[iBand]++;
					}
				}
			}
		
			if( iValues[iBand] > 0 ){
				mean[iBand]	/= (double) iValues[iBand];
				variance[iBand] = variance[iBand] / (double) iValues[iBand] - mean[iBand] * mean[iBand];
			}
		}
		bandToOperate = oldBandToOperate;
		statisticsCalculated = true;
	}

	/**
	 * Obtiene un valor de una celda del grid independientemente del tipo de dato.
	 * @param x Posición en X
	 * @param y Posición en Y
	 * @return Valor de la celda en formato double
	 * @throws RasterBufferInvalidAccessException
	 */
	private double getValue(int x, int y) throws GridException {
		switch(grid.getDataType()) {
		case IBuffer.TYPE_BYTE:return (double)(grid.getCellValueAsByte(x, y)  & 0xff);
		case IBuffer.TYPE_SHORT:return (double)(grid.getCellValueAsShort(x, y)  & 0xffff);
		case IBuffer.TYPE_INT:return (double)(grid.getCellValueAsInt(x, y)  & 0xffffffff);
		case IBuffer.TYPE_FLOAT:return (double)grid.getCellValueAsFloat(x, y);
		case IBuffer.TYPE_DOUBLE:return (double)grid.getCellValueAsDouble(x, y);
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
	
}
