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
 * Proceso del filtro de recorte de colas aplicado a imágenes 16 bits
 *
 * @version 31/05/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class TailTrimFloatFilter extends TailTrimFilter {

	public TailTrimFloatFilter() {}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.statistics.TailTrimFilter#pre()
	 */
	public void pre() {
		super.pre();
		sampleDec = new double[raster.getBandCount()][nSamples];
		result = new double[raster.getBandCount()][2];
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.statistics.TailTrimFilter#process(int, int)
	 */
	public void process(int col, int line) {
		for (int iBand = 0; iBand < raster.getBandCount(); iBand++)
			sampleDec[iBand][count] = raster.getElemFloat(line, col, iBand);
		count++;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.statistics.TailTrimFilter#getInRasterDataType()
	 */
	public int getInRasterDataType() {
		return IBuffer.TYPE_FLOAT;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.statistics.TailTrimFilter#getOutRasterDataType()
	 */
	public int getOutRasterDataType() {
		return IBuffer.TYPE_FLOAT;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.statistics.TailTrimFilter#getResult(java.lang.String)
	 */
	public Object getResult(String name) {
		if (name.equals("raster"))
			return this.raster;
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.statistics.TailTrimFilter#post()
	 */
	public void post() {
		super.post();

		//Cogemos el minimo y máximo para cada banda
		for (int i = 0; i < raster.getBandCount(); i++) {
			result[i][0] = sampleDec[i][posInit + tailSize];
			result[i][1] = sampleDec[i][(posInit + nSamples) - tailSize];
		}
		stats.setTailTrimValue(tailPercent, result);
		
		//Cogemos el minimo y máximo para cada banda
		for (int iValue = 0; iValue < tailSizeList.length; iValue++) {
			double [][] res = new double[raster.getBandCount()][2];
			for (int i = 0; i < raster.getBandCount(); i++) {
				res[i][0] = sampleDec[i][posInit + tailSizeList[iValue]];
				res[i][1] = sampleDec[i][(posInit + nSamples) - tailSizeList[iValue]];
			}
			stats.setTailTrimValue(tailPercentList[iValue], res);
		}
	}
}