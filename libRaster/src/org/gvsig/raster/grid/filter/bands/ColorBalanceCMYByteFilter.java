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
 * Filtro de balance de colores para tipo de datos byte.
 *
 * @version 30/11/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class ColorBalanceCMYByteFilter extends ColorBalanceCMYFilter {
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.bands.ColorTableFilter#process(int, int)
	 */
	public void process(int col, int line) {
		byte[] value = new byte[3];
		for (int i = 0; i < renderBands.length; i++) 
			value[i] = raster.getElemByte(line, col, renderBands[i]);
		
		double[] cmyk = colorConversion.RGBtoCMYK(value[0] & 0xff, value[1] & 0xff, value[2] & 0xff, 1);
		double lum = colorConversion.getLuminosity(value[0] & 0xff, value[1] & 0xff, value[2] & 0xff);
		cmyk[0] = Math.min(cmyk[0] + (cyan / 300.0), 1D);
		cmyk[1] = Math.min(cmyk[1] + (magenta / 300.0), 1D);
		cmyk[2] = Math.min(cmyk[2] + (yellow / 300.0), 1D);
		double[] rgb = colorConversion.CMYKtoRGB(cmyk[0], cmyk[1], cmyk[2], cmyk[3]);
		for (int band = 0; band < 3; band++)
			value[band] = (byte)(rgb[band] * 255);
		
		if(luminosity) {
			double[] hsl = colorConversion.RGBtoHSL(value[0] & 0xff, 
													value[1] & 0xff, 
													value[2] & 0xff);
			hsl[0] = (int)(255.0 * hsl[0] / 360.0 + 0.5);
			hsl[1] = (int) (hsl[1] * 255. + 0.5);
			hsl[2] = (int) (lum * 255. + 0.5);
			int[] v1 = colorConversion.HSLtoRGB((int)(((int)hsl[0]) & 0xffffffff), 
												(int)(((int)hsl[1]) & 0xffffffff), 
												(int)(((int)hsl[2]) & 0xffffffff));
			byte[] v = new byte[3];
			for (int band = 0; band < 3; band++)
				v[band] = (byte)v1[band];
			rasterResult.setElemByte(line, col, v);
		} else
			rasterResult.setElemByte(line, col, value);
	}
}