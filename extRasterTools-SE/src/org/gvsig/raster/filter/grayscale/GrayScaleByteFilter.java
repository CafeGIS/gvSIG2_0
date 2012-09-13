/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2007 IVER T.I. and Generalitat Valenciana.
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
package org.gvsig.raster.filter.grayscale;

/**
 * Filtro para la conversión a escala de grises de tipo byte
 * 26/06/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class GrayScaleByteFilter extends GrayScaleFilter {
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.bands.ColorTableFilter#process(int, int)
	 */
	public void process(int col, int line) {
		byte value = 0;
		switch (type) {
		case 0:
		case 4:	value = raster.getElemByte(line, col, 0);
				break;
		case 1:	value = raster.getElemByte(line, col, 1);
				break;
		case 2:	value = raster.getElemByte(line, col, 2);
				break;
		case 3:	int r = (raster.getElemByte(line, col, 0) & 0xff);
				int g = (raster.getElemByte(line, col, 1) & 0xff);
				int b = (raster.getElemByte(line, col, 2) & 0xff);
				value = (byte)((r + g + b) / 3);
				break;
		}
		
		for (int i = 0; i < rasterResult.getBandCount(); i++) 
			rasterResult.setElem(line, col, i, value);
	}
}