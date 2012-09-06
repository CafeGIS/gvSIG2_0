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

import org.gvsig.raster.dataset.IBuffer;
/**
 * Filtro de ecualización de histograma para tipo de datos short.
 *
 * @version 11/05/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class EqualizationShortFilter extends EqualizationFilter {

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.enhancement.LinearEnhancementFilter#process(int, int)
	 */
	public void process(int col, int line) {
		for (int iBand = 0; iBand < raster.getBandCount(); iBand++) {
			short p = raster.getElemShort(line, col, iBand);
			if(!equalizationActive(iBand)) {
				rasterResult.setElem(line, col, iBand, p);
				continue;
			}
			
			if (p > maxBandValue[renderBands[iBand]])
				p = (short) maxBandValue[renderBands[iBand]];
			else if (p < minBandValue[renderBands[iBand]])
				p = (short) minBandValue[renderBands[iBand]];
		
			int pos = (int)(((p + dto[iBand]) * nClasses) / distance[iBand]);
			int ecualizationPositive = (int)(lahe[iBand][pos]);
			int ecualizationNegative = (int)(lahe[iBand][nElements - pos]);
			double value = ((nElements - ecualizationNegative) + ecualizationPositive) / 2;
			
			rasterResult.setElem(line, col, iBand, (short)value);
		}
	}
	
	double[] distance = null;
	double[] dto = null;
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#pre()
	 */
	public void pre() {
		super.pre();
		distance = new double[raster.getBandCount()];
		dto = new double[raster.getBandCount()];
		for (int i = 0; i < raster.getBandCount(); i++) {
			distance[i] = maxBandValue[renderBands[i]] - minBandValue[renderBands[i]];
			dto[i] = -minBandValue[renderBands[i]];
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.enhancement.LinearEnhancementFilter#getInRasterDataType()
	 */
	public int getInRasterDataType() {
		return IBuffer.TYPE_SHORT;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#getOutRasterDataType()
	 */
	public int getOutRasterDataType() {
		return IBuffer.TYPE_SHORT;
	}
}