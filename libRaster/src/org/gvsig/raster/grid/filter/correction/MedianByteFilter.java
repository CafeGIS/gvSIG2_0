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

import java.util.Arrays;

import org.gvsig.raster.buffer.RasterBuffer;
import org.gvsig.raster.dataset.IBuffer;
/**
 * Filtro de Mediana que se aplica en la imagen. Toma como entrada la imagen y
 * el lado de la ventana de filtrado.
 *
 * @author Diego Guerrero Sevilla <diego.guerrero@uclm.es>
 */
public class MedianByteFilter extends MedianFilter {
	private int[] window = null;

	public MedianByteFilter() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.correction.MedianFilter#pre()
	 */
	public void pre() {
		super.pre();
		window = new int[sizeWindow];
		rasterResult = RasterBuffer.getBuffer(IBuffer.TYPE_BYTE, raster.getWidth(), raster.getHeight(), raster.getBandCount(), true);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.correction.MedianFilter#process(int, int)
	 */
	public void process(int col, int line) {
		// Obtener el vector con la ventanas de muestras
		for (int band = 0; band < raster.getBandCount(); band++) {
			int k = 0;
			for (int i = -halfSide; i <= halfSide; i++)
				for (int j = -halfSide; j <= halfSide; j++) {
					if ((col + i >= 0) && (line + j >= 0) && (col + i < width) && (line + j < height)) {
						window[k] = raster.getElemByte(line + j, col + i, band) & 0xff;
						k++;
					}
				}
			// Ordenar los valores de las ventanas, se supone que usa quickSort.
			Arrays.sort(window, 0, k);

			// Extraer los elementos centrales y asignarselos al pixel (x,y)
			rasterResult.setElem(line, col, band, (byte) window[k >> 1]);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.correction.MedianFilter#getInRasterDataType()
	 */
	public int getInRasterDataType() {
		return RasterBuffer.TYPE_BYTE;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#getOutRasterDataType()
	 */
	public int getOutRasterDataType() {
		return RasterBuffer.TYPE_BYTE;
	}
}