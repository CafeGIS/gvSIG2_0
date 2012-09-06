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
 * Filtro de realce para tipos de datos float. En el método de proceso procesa
 * un solo pixel float. Asigna su valor en relación a los datos calculados en el
 * método pre() del padre.
 * 
 * @version 11/05/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class LinearEnhancementFloatFilter extends LinearEnhancementFilter {

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.enhancement.LinearEnhancementFilter#process(int, int)
	 */
	public void process(int col, int line) {
		for (int iBand = 0; iBand < raster.getBandCount(); iBand++) {
			float p = raster.getElemFloat(line, col, iBand);
			if(renderBands[iBand] < 0) {
				rasterResult.setElem(line, col, iBand, (byte)p);
				continue;
			}
			if (p > maxBandValue[renderBands[iBand]])
				p = (float) maxBandValue[renderBands[iBand]];
			else if (p < minBandValue[renderBands[iBand]])
				p = (float) minBandValue[renderBands[iBand]];

			int value = (int) (((int) ((p * scale[renderBands[iBand]]) + offset[renderBands[iBand]])) & 0xff);
			rasterResult.setElem(line, col, iBand, (byte) value);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.enhancement.LinearEnhancementFilter#getInRasterDataType()
	 */
	public int getInRasterDataType() {
		return IBuffer.TYPE_FLOAT;
	}
}