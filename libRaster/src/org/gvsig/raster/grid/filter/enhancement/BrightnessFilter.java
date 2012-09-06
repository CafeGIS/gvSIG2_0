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

import org.gvsig.raster.buffer.RasterBuffer;
import org.gvsig.raster.buffer.cache.RasterReadOnlyBuffer;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.Params;
import org.gvsig.raster.grid.filter.RasterFilter;
/**
 * Clase base para los filtros de brillo.
 *
 * @version 31/05/2007
 * @author Miguel Ángel Querol Carratalá  (miguelangel.querol@iver.es)
 */
public class BrightnessFilter extends RasterFilter {
	public static String[]	names = new String[] {"brightness"};

	/**
	 * Variable para guardar el incremento de brillo que se va a aplicar
	 */
	int incrBrillo = 0;

	/**
	 * Constructor. Llama al constructor de la clase base y asigna el
	 * nombre del filtro.
	 */
	public BrightnessFilter() {
		setName(names[0]);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#pre()
	 */
	public void pre() {
		exec = true;
		incrBrillo = ((Integer) params.get("incrBrillo")).intValue();
		raster = rasterResult;
		raster = (RasterBuffer) params.get("raster");
		if (raster != null) {
			height = raster.getHeight();
			width = raster.getWidth();
			rasterResult = RasterBuffer.getBuffer(IBuffer.TYPE_BYTE, raster.getWidth(), raster.getHeight(), raster.getBandCount(), true);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#post()
	 */
	public void post() {
		// En caso de que nadie apunte a raster, se liberará su memoria.
		raster = null;
	}

	/**
	 * Obtiene el incremento de brillo que se está aplicando
	 * @return entero que representa el incremento de brillo aplicado.
	 */
	public int getBrightnessIncrease() {
		return this.incrBrillo;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.IRasterFilter#getGroup()
	 */
	public String getGroup() {
		return "realces";
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.IRasterFilter#getParams()
	 */
	public Params getUIParams(String nameFilter) {
		Params params = new Params();
		params.setParam("Brightness",
				new Integer(incrBrillo),
				Params.SLIDER,
				new String[]{ "-255", "255", "50", "1", "25" }); //min, max, valor defecto, intervalo pequeño, intervalo grande;
		return params;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#getInRasterDataType()
	 */
	public int getInRasterDataType() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#getOutRasterDataType()
	 */
	public int getOutRasterDataType() {
		return RasterBuffer.TYPE_BYTE;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.enhancement.BrightnessFilter#getResult(java.lang.String)
	 */
	public Object getResult(String name) {
		if (name.equals("raster")) {
			if (!exec)
				return this.raster;
			return this.rasterResult;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#process(int, int)
	 */
	public void process(int x, int y) {
	}

	/**
	 * Calcula el brillo para un pixel
	 * @param px
	 * @return
	 */
	protected int calcBrightness(int px) {
		px += incrBrillo;
		if (px > 255)
			px = 255;
		else if (px < 0)
			px = 0;
		return px;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#getName()
	 */
	public String[] getNames() {
		return names;
	}
}