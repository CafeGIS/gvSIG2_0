/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
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
package org.gvsig.raster.grid.filter.correction;

import org.gvsig.raster.buffer.RasterBuffer;

/**
 * Proceso que aplica el filtro de Moda a un raster de tipo double
 * 
 * 23/07/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class ModeDoubleFilter extends ModeFilter {
	private double[]                   window = null;
	private double                     tempValue       = 0;

	public ModeDoubleFilter() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.correction.MedianFilter#pre()
	 */
	public void pre() {
		super.pre();
		window = new double[sizeWindow];
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.correction.MedianFilter#process(int, int)
	 */
	public void process(int col, int line) {
		for (int band = 0; band < raster.getBandCount(); band++) {
			int k = 0;
			count = 0;
			for (int i = -halfSide; i <= halfSide; i++) {
				for (int j = -halfSide; j <= halfSide; j++) {
					if ((col + i >= 0) && (line + j >= 0) && (col + i < width) && (line + j < height)) {
						window[k] = raster.getElemDouble(line + j, col + i, band);
						if(i == -halfSide && j == -halfSide) 
							tempValue = window[k];
						if(tempValue == window[k])
							count ++;
						k++;
					}
				}
			}

			if(count > sizeWindow)
				return;
			
			k = 1;
			while(k < window.length) {
				int auxCount = 0;
				for (int i = k; i < window.length; i++) {
					if(window[i] == window[k])
						auxCount ++;
				}
				if(auxCount > count) {
					count = auxCount;
					tempValue = window[k];
					if(count > sizeWindow)
						return;
				}
				k++;
			}
			rasterResult.setElem(line, col, band, (double) tempValue);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.correction.MedianFilter#getInRasterDataType()
	 */
	public int getInRasterDataType() {
		return RasterBuffer.TYPE_DOUBLE;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#getOutRasterDataType()
	 */
	public int getOutRasterDataType() {
		return RasterBuffer.TYPE_DOUBLE;
	}
}