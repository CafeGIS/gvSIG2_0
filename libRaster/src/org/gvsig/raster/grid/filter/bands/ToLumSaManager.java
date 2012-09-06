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
 * Gestor del filtro de Tono, Saturación y Brillo
 *
 * @version 04/12/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class ToLumSaManager  implements IRasterFilterListManager {

	protected RasterFilterList	filterList = null;

	/**
	 * Registra ToLumSaManager en los puntos de extension de RasterFilter
	 */
	public static void register() {
		ExtensionPointManager extensionPoints =ToolsLocator.getExtensionPointManager();
		ExtensionPoint point=extensionPoints.get("RasterFilter");
		point.append("ToLumSa", "", ToLumSaManager.class);
	}

	/**
	 * Constructor.
	 * Asigna la lista de filtros y el managener global.
	 *
	 * @param filterListManager
	 */
	public ToLumSaManager(RasterFilterListManager filterListManager) {
		this.filterList = filterListManager.getFilterList();
	}

	/**
	 * Añade un filtro de control de Tono, Saturación y Brillo a la lista de filtros.
 * @throws FilterTypeException
	 */
	public void addToLumSaFilter(double hue, double luminosity, double saturation, int[] renderBands) throws FilterTypeException {
		RasterFilter filter = new ToLumSaByteFilter();

		// Cuando el filtro esta creado, tomamos los valores y lo añadimos a la pila
		if (filter != null) {
			filter.addParam("hue", new Double(hue));
			filter.addParam("luminosity", new Double(luminosity));
			filter.addParam("saturation", new Double(saturation));
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
		filters.add(ToLumSaFilter.class);
		return filters;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.IRasterFilterListManager#addFilter(java.lang.Class, org.gvsig.raster.dataset.Params)
	 */
	public void addFilter(Class classFilter, Params params) throws FilterTypeException {
		if (classFilter.equals(ToLumSaFilter.class)) {
			double hue = 0, saturation = 0, luminosity = 0;
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
				if (params.getParam(i).id.equals("hue"))
					hue = ((Double) params.getParam(i).defaultValue).doubleValue();
				if (params.getParam(i).id.equals("saturation"))
					saturation = ((Double) params.getParam(i).defaultValue).doubleValue();
				if (params.getParam(i).id.equals("luminosity"))
					luminosity = ((Double) params.getParam(i).defaultValue).doubleValue();

			}
			addToLumSaFilter(hue, luminosity, saturation, renderBands);
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