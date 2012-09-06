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

import org.gvsig.raster.dataset.IBuffer;

/**
 * Filtro de conversión de datos RGB a CMYK para tipo de datos byte.
 *
 * @version 30/11/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class RGBToCMYKByteFilter extends RGBToCMYKFilter {
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.bands.ColorTableFilter#process(int, int)
	 */
	public void process(int col, int line) {
		byte[] value = new byte[4];
		raster.getElemByte(line, col, value);
		if(value[0] == 0 && value[1] == 0 && value[2] == 2)
			System.out.println();
		double[] cmyk = colorConversion.RGBtoCMYK(value[0] & 0xff, value[1] & 0xff, value[2] & 0xff, 1);
		if(out == IBuffer.TYPE_BYTE) {
			double[] rgb = colorConversion.CMYKtoRGB(cmyk[0], cmyk[1], cmyk[2], cmyk[3]);
			for (int band = 0; band < 3; band++)
				value[band] = (byte)(rgb[band] * 255);
				
			//System.out.println();
			//System.out.println("+" + cmyk[0] + " " + cmyk[1] + " " + cmyk[2] + " " + cmyk[3]);
			/*value[0] = (byte)(((byte) (cmyk[0] * 100. )) & 0xff);
			value[1] = (byte)(((byte) (cmyk[1] * 100. )) & 0xff);
			value[2] = (byte)(((byte) (cmyk[2] * 100. )) & 0xff);
			value[3] = (byte)(((byte) (cmyk[3] * 100. )) & 0xff);*/
			/*value[0] = ((byte) Math.round(cmyk[0] * 100));
			value[1] = ((byte) Math.round(cmyk[1] * 100));
			value[2] = ((byte) Math.round(cmyk[2] * 100));
			value[3] = ((byte) Math.round(cmyk[3] * 100));*/
			//cmyk[3] = (int) (cmyk[3] * 255. + 0.5);
			//System.out.println("-" + value[0] + " " + value[1] + " " + value[2] + " " + value[3]);
			//for (int band = 0; band < 4; band++)
				//value[band] = (byte)(((byte)cmyk[band]) & 0xff);
			rasterResult.setElemByte(line, col, value);
		} else if(out == IBuffer.TYPE_DOUBLE) 
			rasterResult.setElemDouble(line, col, cmyk);
	}
}