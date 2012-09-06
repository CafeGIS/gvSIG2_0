/* gvSIG. Sistema de Informaciï¿½n Geogrï¿½fica de la Generalitat Valenciana
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
package org.gvsig.raster.grid.filter.correction;

import org.gvsig.raster.buffer.RasterBuffer;
import org.gvsig.raster.dataset.Params;
import org.gvsig.raster.grid.filter.RasterFilter;

/**
 * Clase base para los filtros de moda.
 * 
 * 23/07/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class ModeFilter extends RasterFilter {
	public static String[]          names           = new String[] {"moda"};
	/**
	 * Variable para guardar el lado de la ventana de filtrado
	 */
	protected int                   sideWindow		= 0;
	protected int                   sizeWindow      = 0;
	protected int                   halfSide        = 0;
	protected int                   count           = 0;


	public ModeFilter() {
		super();
		setName(names[0]);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#pre()
	 */
	public void pre() {
		exec = true;
		raster = (RasterBuffer) params.get("raster");
		height = raster.getHeight();
		width = raster.getWidth();
		sideWindow = ((Integer) params.get("sideLong")).intValue();
		
		//El lado de la ventana debe ser positivo e impar.
		sideWindow = Math.abs(sideWindow);
		if (sideWindow % 2 == 0)
			sideWindow++;
		sizeWindow = sideWindow * sideWindow;
		halfSide = (sideWindow - 1) >> 1;
		
		rasterResult = RasterBuffer.getBuffer(raster.getDataType(), raster.getWidth(), raster.getHeight(), raster.getBandCount(), true);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#post()
	 */
	public void post() {
		// En caso de que nadie apunte a raster, se liberará su memoria.
		raster = null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#getGroup()
	 */
	public String getGroup() {
		return "espaciales";
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
	 * @see org.gvsig.raster.grid.filter.RasterFilter#getNames()
	 */
	public String[] getNames() {
		return names;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#getOutRasterDataType()
	 */
	public int getOutRasterDataType() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#getResult(java.lang.String)
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
	 * @see org.gvsig.raster.grid.filter.RasterFilter#getUIParams()
	 */
	public Params getUIParams(String nameFilter) {
		if(params != null) {
			Object obj = params.get("sideLong");
			if(obj != null && obj instanceof Integer) {
				sideWindow = ((Integer)obj).intValue();
				sideWindow = Math.abs(sideWindow);
			}
		}
		Params params = new Params();
		params.setParam("sideLong",
				new Integer(sideWindow),
				Params.SLIDER,
				new String[] {"1", "10", "0", "1", "5" }); //min, max, valor defecto, intervalo pequeño, intervalo grande;
		return params;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#process(int, int)
	 */
	public void process(int x, int y) {
	}
	
	/**
	 * Obtiene el tamaño del lado de la ventana
	 * @return entero que representa el tamaño del lado de la 
	 * ventana en píxeles
	 */
	public int getSideWindow() {
		return sideWindow;
	}
}