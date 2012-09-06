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
import org.gvsig.raster.grid.filter.enhancement.LinearStretchParams.Stretch;
/**
 * Filtro de realce lineal para tipos de datos byte. El realce es aplicado por intervalos.
 * Para cada pixel se obtiene en que intervalo se encuentra y se aplica la scala y offset
 * calculados para ese intervalo.
 *
 * @version 11/05/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class LinearStretchEnhancementByteFilter extends LinearStretchEnhancementFilter {
		
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.enhancement.LinearEnhancementFilter#process(int, int)
	 */
	public void process(int col, int line) {
		for (int iBand = 0; iBand < raster.getBandCount(); iBand++) {
			if (renderBands[iBand] < 0) {
				rasterResult.setElem(line, col, iBand, (byte) 0);
				continue;
			}

			int p;
			if (stretchs.rgb)
				p = (int) (raster.getElemByte(line, col, iBand) & 0xff);
			else
				p = (int) raster.getElemByte(line, col, iBand);

			if (iBand < scaleOffsetList.length)
				processValue(p, scaleOffsetList[iBand], col, line, iBand);
			else
				rasterResult.setElem(line, col, iBand, (byte) p);
		}
	}
	
	/**
	 * Procesa un dato del raster aplicandole el factor de escala y desplazamiento que
	 * necesita.
	 * @param p Valor del punto
	 * @param data Estructura de datos con los valores de escala y desplazamiento
	 * @param col Columna del valor dentro del raster
	 * @param line Línea del valor dentro del raster
	 * @param iBand Número de banda del valor dentro del raster
	 * @return true si ha podido ser procesado y false si no lo hace
	 */
	private void processValue(int p, Stretch data, int col, int line, int iBand) {
		if(data.scale != null) {
			if (p > data.maxValue)
				p = (int) data.maxValue;
			else if (p < data.minValue)
				p = (int) data.minValue;
			
			for (int i = 0; i < data.scale.length; i++) {
				if((p > data.stretchIn[i] && p <= data.stretchIn[i + 1]) || (i == 0 && p == data.stretchIn[i])) {
					p =  (int)( (((double)p) - data.stretchIn[i]) * data.scale[i] + data.offset[i]);
					break;
				}
			}
			rasterResult.setElem(line, col, iBand, (byte)p);
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