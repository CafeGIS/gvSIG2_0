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
package org.gvsig.raster.grid.filter.enhancement;

import org.gvsig.raster.buffer.RasterBuffer;
/**
 * Filtro de contraste para tipo de datos int.
 * 
 * @author Miguel Ángel Querol Carratalá  (miguelangel.querol@iver.es)
 */
public class ContrastIntegerFilter extends ContrastFilter {
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.enhancement.ContrastFilter#process(int, int)
	 */
	public void process(int col, int line) {
		for (int i = 0; i < raster.getBandCount(); i++)
			rasterResult.setElem(line, col, i, (byte) calcContrast(raster.getElemInt(line, col, i) & 0xff));
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.enhancement.ContrastFilter#getInRasterDataType()
	 */
	public int getInRasterDataType() {
		return RasterBuffer.TYPE_INT;
	}
}