/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
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

import org.gvsig.raster.buffer.RasterBuffer;
/**
 * Filtro de brillo para buffer de datos tipo byte. En el m�todo de proceso
 * procesa un solo pixel byte e incrementa su brillo en la cantidad indicada en
 * la variable incrBrillo.
 * 
 * @author Miguel �ngel Querol Carratal� (miguelangel.querol@iver.es)
 */
public class BrightnessByteFilter extends BrightnessFilter {
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.enhancement.BrightnessFilter#process(int, int)
	 */
	public void process(int col, int line) {
		for (int i = 0; i < raster.getBandCount(); i++)
			rasterResult.setElem(line, col, i, (byte) calcBrightness(raster.getElemByte(line, col, i) & 0xff));
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.enhancement.BrightnessFilter#getInRasterDataType()
	 */
	public int getInRasterDataType() {
		return RasterBuffer.TYPE_BYTE;
	}
}