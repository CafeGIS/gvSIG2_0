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
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.Params;
import org.gvsig.raster.grid.filter.RasterFilter;
/**
 * Clase base para todos los filtros de contraste
 *
 * @version 31/05/2007
 * @author Miguel Ángel Querol Carratalá  (miguelangel.querol@iver.es)
 */
public class ContrastFilter extends RasterFilter {
	public static String[]	names = new String[] {"contrast"};

	/**
	 * Variable que contendra el incremento del contraste.
	 */
	int incrContraste = 0;

	/**
	 * Constructor. Llama al constructor de la clase base y asigna el
	 * nombre del filtro.
	 */
	public ContrastFilter(){
		setName(names[0]);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#pre()
	 */
	public void pre() {
		exec = true;
		raster = rasterResult;
		raster = (RasterBuffer) params.get("raster");
		height = raster.getHeight();
		width = raster.getWidth();
		incrContraste = ((Integer) params.get("incrContraste")).intValue();
		rasterResult = RasterBuffer.getBuffer(IBuffer.TYPE_BYTE, raster.getWidth(), raster.getHeight(), raster.getBandCount(), true);
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
	 * Obtiene el incremento de contraster que se está aplicando
	 * @return entero que representa el incremento de contraste aplicado.
	 */
	public int getContrastIncrease(){
		return this.incrContraste;
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
		params.setParam("Contrast",
				new Integer(incrContraste),
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
	 * @see org.gvsig.raster.grid.filter.enhancement.ContrastFilter#getResult(java.lang.String)
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
	 * Algoritmo de contraste
	 * @param px valor de la banda
	 * @return valor de la banda con el algoritmo aplicado
	 */
	protected int calcContrast(int px) {
		int result;
		int diferencia = 127 - px;
		if (incrContraste >= 0) {
			result = (int) (px - (0.02 * diferencia * incrContraste)); // ((5.0 * 0.1) / 25) = 0.02
		} else {
			result = (int) (px - (0.004 * diferencia * incrContraste)); // (0.1 / 25) = 0.004;
			if (px < 127) {
				if (result > 127) result = 127;
			} else {
				if (result < 127) result = 127;
			}
		}
		if (result < 0)
			result = 0;
		else {
			if (result > 255)
				result = 255;
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#getNames()
	 */
	public String[] getNames() {
		return names;
	}
}