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
package org.gvsig.raster.grid;

import org.gvsig.raster.datastruct.ColorTable;
/**
 * Tabla de color asociada a un grid
 *
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class GridPalette extends ColorTable {
	/**
	 * Flag de activación de la paleta cuando se visualiza o exporta.
	 */
	private boolean 		paletteActive = true;

	/**
	 * Constructor
	 * @param fp
	 */
	public GridPalette() {
		paletteActive = true;
	}

	/**
	 * Constructor
	 * @param fp
	 */
	public GridPalette(ColorTable fp){
		if (fp == null)
			return;
		this.range = fp.getRange();
		this.paletteActive = true;
		this.paletteByBand = fp.getColorTableByBand();
		this.nameClass = fp.getNameClass();
		this.name = fp.getName();
		this.filePath = fp.getFilePath();
		this.colorItems = fp.getColorItems();
		this.interpolated = fp.isInterpolated();
	}

	/**
	 * Obtiene el flag de paleta activa o desactivada.
	 * @return true la paleta está activa y false desactiva
	 */
	public boolean isPaletteActive() {
		return paletteActive;
	}

	/**
	 * Asigna el flag de paleta activa o desactivada.
	 * @param paletteActive true activa la paleta false la desactiva
	 */
	public void setPaletteActive(boolean paletteActive) {
		this.paletteActive = paletteActive;
	}
}