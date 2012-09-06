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
package org.gvsig.raster.grid.filter.segmentation;

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
 * Gestor de la pila de filtros para el filtro de primera derivada.
 *
 * @version 04/06/2007
 * @author Diego Guerrero Sevilla  <diego.guerrero@uclm.es>
 */
public class FirstDerivativeListManager implements IRasterFilterListManager {
	protected RasterFilterList			filterList				= null;
	//private RasterFilterListManager	filterListManager	= null;

	/**
	 * Constructor. Asigna la lista de filtros y el manager.
	 * @param filterListManager
	 */
	public FirstDerivativeListManager(RasterFilterListManager filterListManager) {
		//this.filterListManager = filterListManager;
		this.filterList = filterListManager.getFilterList();
	}

	/**
	 * Registra FirstDerivativeListManager en los puntos de extension de RasterFilter
	 */
	public static void register() {
		ExtensionPointManager extensionPoints =ToolsLocator.getExtensionPointManager();
		ExtensionPoint point=extensionPoints.get("RasterFilter");
		point.append("FirstDerivative", "", FirstDerivativeListManager.class);
	}

	/**
	 * Añade un filtro de convolucón a la pila de filtros.
	 * @param Name Nombre del filtro
	 * @param umbral
	 * @param operator
	 * @param compare
	 * @param Name
	 * @throws FilterTypeException
	 */
	public void addFirstDerivativeFilter(int umbral, boolean compare, String Name) throws FilterTypeException {
		RasterFilter filter = new FirstDerivativeByteFilter();

		// Cuando el filtro esta creado, tomamos los valores y lo añadimos a la pila

		if (filter != null) {
			filter.addParam("umbral", new Integer(umbral));
			filter.addParam("compare", new Boolean(compare));
			filter.addParam("filterName", new String(Name));
			filter.setName(Name);
			filterList.add(filter);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.IRasterFilterListManager#getStringsFromFilterList(java.util.ArrayList, org.gvsig.raster.grid.filter.RasterFilter)
	 */
	public ArrayList getStringsFromFilterList(ArrayList filterList, RasterFilter rf) {
		if (rf instanceof FirstDerivativeFilter) {
			filterList.add("filter.firstDerivative.active=true");
			FirstDerivativeFilter firstDerivative = (FirstDerivativeFilter) rf;
			filterList.add("filter.firstDerivative.umbral=" + firstDerivative.umbral);
			filterList.add("filter.firstDerivative.compare=" + firstDerivative.compare);
			filterList.add("filter.firstDerivative.filterName=" + firstDerivative.getName());
		}

		return filterList;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.IRasterFilterListManager#createFilterListFromStrings(java.util.ArrayList, java.lang.String, int)
	 */
	public int createFilterListFromStrings(ArrayList filters, String fil, int filteri) throws FilterTypeException {
		if ((fil.startsWith("filter.firstDerivative.active")) && (RasterFilterListManager.getValue(fil).equals("true"))) {

			int umbral = 0;
			boolean compare = false;
			String name = "";
			filters.remove(0);

			for (int prop = 0; prop < filters.size(); prop++) {
				String elem = (String) filters.get(prop);
				if (elem.startsWith("filter.firstDerivative.umbral")) {
					umbral = Integer.parseInt(RasterFilterListManager.getValue(elem));
					filters.remove(prop);
					prop--;
				}
				if (elem.startsWith("filter.firstDerivative.compare")) {
					compare = Boolean.getBoolean(RasterFilterListManager.getValue(elem));
					filters.remove(prop);
					prop--;
				}
				if (elem.startsWith("filter.firstDerivative.filterName")) {
					name = RasterFilterListManager.getValue(elem);
					filters.remove(prop);
					prop--;
				}
			}
			addFirstDerivativeFilter(umbral, compare, name);
		}
		return filteri;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.IRasterFilterListManager#getRasterFilterList()
	 */
	public ArrayList getRasterFilterList() {
		ArrayList filters = new ArrayList();
		filters.add(FirstDerivativeFilter.class);
		return filters;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.IRasterFilterListManager#addFilter(java.lang.Class, org.gvsig.raster.dataset.Params)
	 */
	public void addFilter(Class classFilter, Params params) throws FilterTypeException {
		if (classFilter.equals(FirstDerivativeFilter.class)) {
			int umbral = 0;
			boolean compare = false;
			String name = "";
			for (int i = 0; i < params.getNumParams(); i++) {
				if (params.getParam(i).id.equals("Umbral") &&
					params.getParam(i).defaultValue instanceof Integer)
					umbral = ((Integer)params.getParam(i).defaultValue).intValue();
				if (params.getParam(i).id.equals("Compare") &&
					params.getParam(i).defaultValue instanceof Boolean)
					compare = ((Boolean)params.getParam(i).defaultValue).booleanValue();
				if (params.getParam(i).id.equals("FilterName") &&
					params.getParam(i).defaultValue instanceof String)
					name = (String)params.getParam(i).defaultValue;
			}
			addFirstDerivativeFilter(umbral, compare, name);
		}
	}
}