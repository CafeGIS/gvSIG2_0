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
package org.gvsig.raster.grid.filter.correction;

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
 * Gestor de la pila de filtros para el filtro de mediana.
 *
 * @author Diego Guerrero Sevilla  <diego.guerrero@uclm.es>
 */
public class MedianListManager implements IRasterFilterListManager {
	protected RasterFilterList			filterList = null;

	/**
	 * Constructor. Asigna la lista de filtros y el manager.
	 * @param filterListManager
	 */
	public MedianListManager(RasterFilterListManager filterListManager) {
		this.filterList = filterListManager.getFilterList();
	}

	/**
	 * Registra MedianListManager en los puntos de extension de RasterFilter
	 */
	public static void register() {
		ExtensionPointManager extensionPoints =ToolsLocator.getExtensionPointManager();
		ExtensionPoint point=extensionPoints.get("RasterFilter");
		point.append("Median", "", MedianListManager.class);
	}

	/**
	 * Añade un filtro de mediana a la pila de filtros.
	 * @param ladoVentana
	 * @throws FilterTypeException
	 */
	public void addMedianFilter(int ladoVentana) throws FilterTypeException {
		RasterFilter filter = new MedianByteFilter();

		//Cuando el filtro esta creado, tomamos los valores y lo aï¿½adimos a la pila

		if (filter != null) {
			filter.addParam("ladoVentana", new Integer(ladoVentana));
			filterList.add(filter);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.IRasterFilterListManager#getStringsFromFilterList(java.util.ArrayList, org.gvsig.raster.grid.filter.RasterFilter)
	 */
	public ArrayList getStringsFromFilterList(ArrayList filterList, RasterFilter rf) {
		if (rf instanceof MedianFilter) {
			filterList.add("filter.median.active=true");
			MedianFilter medianFilter = (MedianFilter) rf;
			filterList.add("filter.median.ladoVentana=" + medianFilter.getSideWindow());
		}

		return filterList;
	}


	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.IRasterFilterListManager#createFilterListFromStrings(java.util.ArrayList, java.lang.String, int)
	 */
	public int createFilterListFromStrings(ArrayList filters, String fil, int filteri) throws FilterTypeException {
		if ((fil.startsWith("filter.median.active")) && (RasterFilterListManager.getValue(fil).equals("true"))) {

			int ladoVentana = 0;
			filters.remove(0);

			for (int prop = 0; prop < filters.size(); prop++) {
				String elem = (String) filters.get(prop);
				if (elem.startsWith("filter.median.ladoVentana")) {
					ladoVentana = Integer.parseInt(RasterFilterListManager.getValue(elem));
					filters.remove(prop);
					prop--;
				}
			}
			addMedianFilter(ladoVentana);
		}
		return filteri;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.IRasterFilterListManager#getRasterFilterList()
	 */
	public ArrayList getRasterFilterList() {
		ArrayList filters = new ArrayList();
		filters.add(MedianFilter.class);
		return filters;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.IRasterFilterListManager#addFilter(java.lang.Class, org.gvsig.raster.dataset.Params)
	 */
	public void addFilter(Class classFilter, Params params) throws FilterTypeException {
		if (classFilter.equals(MedianFilter.class)) {
			int ladoVentana = 0;
			for (int i = 0; i < params.getNumParams(); i++) {
				if (params.getParam(i).id.equals("ladoVentana") &&
					params.getParam(i).defaultValue instanceof Integer)
					ladoVentana = ((Integer) params.getParam(i).defaultValue).intValue();
			}
			addMedianFilter(ladoVentana);
		}
	}
}