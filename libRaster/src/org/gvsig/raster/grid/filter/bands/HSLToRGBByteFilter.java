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
package org.gvsig.raster.grid.filter.bands;


/**
 * Filtro de conversión de datos RGB a HSL para tipo de datos byte.
 *
 * @version 06/06/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class HSLToRGBByteFilter extends HSLToRGBFilter {
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.bands.ColorTableFilter#process(int, int)
	 */
	public void process(int col, int line) {
		byte[] value = new byte[3];
		for (int i = 0; i < renderBands.length; i++) 
			value[i] = raster.getElemByte(line, col, renderBands[i]);
		int[] rgb = colorConversion.HSLtoRGB((value[0] & 0xff), (value[1] & 0xff), (value[2] & 0xff));
		for (int band = 0; band < 3; band++)
			value[band] = (byte)(((byte)rgb[band]) & 0xff);
		rasterResult.setElemByte(line, col, value);
	}

}