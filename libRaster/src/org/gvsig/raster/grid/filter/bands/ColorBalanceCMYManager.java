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

import java.util.ArrayList;

import org.gvsig.raster.dataset.Params;
import org.gvsig.raster.grid.filter.FilterTypeException;
import org.gvsig.raster.grid.filter.IRasterFilterListManager;
import org.gvsig.raster.grid.filter.RasterFilter;
import org.gvsig.raster.grid.filter.RasterFilterList;
import org.gvsig.raster.grid.filter.RasterFilterListManager;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.extensionpoint.ExtensionPoint;
import org.gvsig.tools.extensionpoint.ExtensionPointManager;
/**
 * Gestor del filtro de balance de color
 *
 * @version 30/11/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class ColorBalanceCMYManager implements IRasterFilterListManager {
	protected RasterFilterList filterList = null;

	/**
	 * Registra ColorBalanceCMYManager en los puntos de extension de RasterFilter
	 */
	public static void register() {
		ExtensionPointManager extensionPoints =ToolsLocator.getExtensionPointManager();
		ExtensionPoint point=extensionPoints.get("RasterFilter");
		point.append("ColorBalanceCMY", "", ColorBalanceCMYManager.class);
	}

	/**
	 * Constructor.
	 * Asigna la lista de filtros y el managener global.
	 *
	 * @param filterListManager
	 */
	public ColorBalanceCMYManager(RasterFilterListManager filterListManager) {
		this.filterList = filterListManager.getFilterList();
	}

	/**
	 * Añade un filtro de conversión de RGB a CMYK a la pila de filtros.
 * @throws FilterTypeException
	 */
	public void addColorBalanceFilter(double cyan, double magenta, double yellow, boolean luminosity, int[] renderBands) throws FilterTypeException {
		RasterFilter filter = new ColorBalanceCMYByteFilter();

		//Cuando el filtro esta creado, tomamos los valores y lo añadimos a la pila

		if (filter != null) {
			filter.addParam("cyan", new Double(cyan));
			filter.addParam("magenta", new Double(magenta));
			filter.addParam("yellow", new Double(yellow));
			filter.addParam("luminosity", new Boolean(luminosity));
			filter.addParam("renderBands", renderBands);
			filterList.add(filter);
		}

	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.IRasterFilterListManager#getRasterFilterList()
	 */
	public ArrayList getRasterFilterList() {
		ArrayList filters = new ArrayList();
		filters.add(ColorBalanceCMYFilter.class);
		return filters;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.IRasterFilterListManager#addFilter(java.lang.Class, org.gvsig.raster.dataset.Params)
	 */
	public void addFilter(Class classFilter, Params params) throws FilterTypeException {
		if (classFilter.equals(ColorBalanceCMYFilter.class)) {
			double cyan = 0, magenta = 0, yellow = 0;
			boolean luminosity = false;
			int[] renderBands = { 0, 1, 2 };

			for (int i = 0; i < params.getNumParams(); i++) {
				if (params.getParam(i).id.equals("RenderBands") &&
						params.getParam(i).defaultValue instanceof String) {
					String[] bands = new String((String) params.getParam(i).defaultValue).split(" ");
					renderBands[0] = new Integer(bands[0]).intValue();
					renderBands[1] = new Integer(bands[1]).intValue();
					renderBands[2] = new Integer(bands[2]).intValue();
					continue;
				}
				if (params.getParam(i).id.equals("cyan"))
					cyan = ((Double) params.getParam(i).defaultValue).doubleValue();
				if (params.getParam(i).id.equals("magenta"))
					magenta = ((Double) params.getParam(i).defaultValue).doubleValue();
				if (params.getParam(i).id.equals("yellow"))
					yellow = ((Double) params.getParam(i).defaultValue).doubleValue();
				if (params.getParam(i).id.equals("luminosity"))
					luminosity = ((Boolean) params.getParam(i).defaultValue).booleanValue();

			}
			addColorBalanceFilter(cyan, magenta, yellow, luminosity, renderBands);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.IRasterFilterListManager#createFilterListFromStrings(java.util.ArrayList, java.lang.String, int)
	 */
	public int createFilterListFromStrings(ArrayList filters, String fil, int filteri) {
		return filteri;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.IRasterFilterListManager#getStringsFromFilterList(java.util.ArrayList, org.gvsig.raster.grid.filter.RasterFilter)
	 */
	public ArrayList getStringsFromFilterList(ArrayList filterList, RasterFilter rf) {
		return filterList;
	}
}