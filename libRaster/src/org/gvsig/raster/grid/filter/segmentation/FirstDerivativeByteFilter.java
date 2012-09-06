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
package org.gvsig.raster.grid.filter.segmentation;

import org.gvsig.raster.buffer.RasterBuffer;
/**
 * Filtro de primera derivada que se aplica en la imagen. Toma como entrada la imagen y el
 * umbral (cero para no umbralizar).
 * 
 * @author Diego Guerrero Sevilla <diego.guerrero@uclm.es>
 */
public class FirstDerivativeByteFilter extends FirstDerivativeFilter {
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#process(int, int)
	 */
	public void process(int col, int line) {
		int out;
		int convoResult[] = new int[4];
		int ladoVentana = 3;
		int semiLado = (ladoVentana - 1) >> 1;

		double ventana[][] = new double[ladoVentana][ladoVentana];

		Kernel kernel = null;

		for (int band = 0; band < raster.getBandCount(); band++) {
			if ((col - semiLado >= 0) && (line - semiLado >= 0) && (col + semiLado < width) && (line + semiLado < height)) {
				// Obtener el vector con la ventanas de muestras
				for (int j = -semiLado; j <= semiLado; j++)
					for (int i = -semiLado; i <= semiLado; i++)
						ventana[j + semiLado][i + semiLado] = raster.getElemByte(line + j, col + i, band) & 0xff;

				kernel = new Kernel(ventana);

				convoResult[0] = (int) operatorH.convolution(kernel);
				convoResult[1] = (int) operatorV.convolution(kernel);

				if (compare) {
					if (convoResult[0] > convoResult[1])
						out = convoResult[0];
					else
						out = convoResult[1];
				} else {
					out = (int) Math.sqrt(Math.pow(convoResult[0], 2) + Math.pow(convoResult[1], 2));
				}

				if (umbral > 0) {
					if (out >= (255 - umbral))
						out = 255;
					else
						out = 0;
				} else {
					if (out < 0)
						out = 0;
					else if (out > 255)
						out = 255;
				}

				rasterResult.setElem(line, col, band, (byte) out);
			} else {
				rasterResult.setElem(line, col, band, (byte) raster.getElemByte(line, col, band));
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.segmentation.FirstDerivativeFilter#getInRasterDataType()
	 */
	public int getInRasterDataType() {
		return RasterBuffer.TYPE_BYTE;
	}
}