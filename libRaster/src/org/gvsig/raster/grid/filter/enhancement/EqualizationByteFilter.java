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
 * Filtro de ecualización de histograma.
 *
 * @version 11/05/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class EqualizationByteFilter extends EqualizationFilter {

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.enhancement.LinearEnhancementFilter#process(int, int)
	 */
	public void process(int col, int line) {
		for (int iBand = 0; iBand < raster.getBandCount(); iBand++) {
			int p = (int)(raster.getElemByte(line, col, iBand) & 0xff);
			if(!equalizationActive(iBand)) {
				rasterResult.setElem(line, col, iBand, (byte)p);
				continue;
			}
			
			//Las estadísticas están calculadas para todas las bandas por lo que hay que seleccionar solo las que se renderiza
			if (p > maxBandValue[renderBands[iBand]])
				p = (int) maxBandValue[renderBands[iBand]];
			else if (p < minBandValue[renderBands[iBand]])
				p = (int) minBandValue[renderBands[iBand]];

			//Método lahe
			int ecualizationPositive = (int)lahe[iBand][p % histogram.getNumValues()];
			int ecualizationNegative = (int)laheNegative[iBand][nElements - (p % histogram.getNumValues())];
			
			int value = ((nElements - ecualizationNegative) + ecualizationPositive) / 2;
			rasterResult.setElem(line, col, iBand, (byte)(value & 0x000000ff));
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.enhancement.LinearEnhancementFilter#getInRasterDataType()
	 */
	public int getInRasterDataType() {
		return IBuffer.TYPE_BYTE;
	}
}