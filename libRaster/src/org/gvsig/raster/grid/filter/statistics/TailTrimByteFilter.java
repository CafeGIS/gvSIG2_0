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
package org.gvsig.raster.grid.filter.statistics;

import org.gvsig.raster.dataset.IBuffer;
/**
 * Proceso del filtro de recorte de colas aplicado al tipo de datos byte
 * @version 31/05/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class TailTrimByteFilter extends TailTrimFilter {

	/**
	 * Array con el resultado. La primera dimensión es el número de bandas y la segunda son dos elementos 
	 * el máximo y el mínimo para esa banda.
	 */
	private double[][]	result	= null;

	public TailTrimByteFilter() {}

	/**
	 * Obtiene parámetros para el filtro y obtiene el ancho y alto de
	 * la imagen sobre la que se aplica el filtro
	 */
	public void pre() {
		super.pre();
		sample = new int[raster.getBandCount()][nSamples];
		result = new double[raster.getBandCount()][2];
	}

	/**
	 * Carga sobre el vector de muestras los valores de estas cogidos
	 * de la imagen
	 */
	public void process(int col, int line) {
		for (int iBand = 0; iBand < raster.getBandCount(); iBand++)
			sample[iBand][count] = (raster.getElemByte(line, col, iBand) & 0xff);
		count++;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.statistics.TailTrimFilter#getInRasterDataType()
	 */
	public int getInRasterDataType() {
		return IBuffer.TYPE_BYTE;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.statistics.TailTrimFilter#getOutRasterDataType()
	 */
	public int getOutRasterDataType() {
		return IBuffer.TYPE_BYTE;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.statistics.TailTrimFilter#post()
	 */
	public void post() {
		super.post();

		//Cogemos el minimo y máximo para cada banda
		for (int i = 0; i < raster.getBandCount(); i++) {
			result[i][0] = sample[i][posInit + tailSize];
			result[i][1] = sample[i][(posInit + nSamples) - tailSize];
		}
		stats.setTailTrimValue(tailPercent, result);
		
		//Cogemos el minimo y máximo para cada banda
		for (int iValue = 0; iValue < tailSizeList.length; iValue++) {
			double [][] res = new double[raster.getBandCount()][2];
			for (int i = 0; i < raster.getBandCount(); i++) {
				res[i][0] = sample[i][posInit + tailSizeList[iValue]];
				res[i][1] = sample[i][(posInit + nSamples) - tailSizeList[iValue]];
			}
			stats.setTailTrimValue(tailPercentList[iValue], res);
		}
	}

	/**
	 * Obtiene el objeto con el máximo y mínimo calculado
	 */
	public Object getResult(String name) {
		if (name.equals("raster"))
			return this.raster;
		return null;
	}
}
