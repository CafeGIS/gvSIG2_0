/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
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
package org.gvsig.raster.filter.regionalpha;

import java.util.ArrayList;

import org.gvsig.raster.dataset.Params;
import org.gvsig.raster.grid.filter.FilterTypeException;
import org.gvsig.raster.grid.filter.IRasterFilterListManager;
import org.gvsig.raster.grid.filter.RasterFilter;
import org.gvsig.raster.grid.filter.RasterFilterList;
import org.gvsig.raster.grid.filter.RasterFilterListManager;
import org.gvsig.raster.grid.filter.RegistrableFilterListener;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.extensionpoint.ExtensionPoint;
import org.gvsig.tools.extensionpoint.ExtensionPointManager;

public class RegionAlphaListManager implements IRasterFilterListManager {
	protected RasterFilterList	filterList = null;

	/**
	 * Constructor. Asigna la lista de filtros y el manager.
	 * @param filterListManager
	 */
	public RegionAlphaListManager(RasterFilterListManager filterListManager) {
		this.filterList = filterListManager.getFilterList();
	}

	/**
	 * Registrar los manager en los puntos de extension
	 */
	public static void register() {
		ExtensionPointManager extensionPoints =ToolsLocator.getExtensionPointManager();
		ExtensionPoint point=extensionPoints.add("RasterFilter");
		point.append("RegionAlpha", "", RegionAlphaListManager.class);
	}


	public void addRegionAlphaFilter(ArrayList rois, int alpha, Boolean inverse) throws FilterTypeException {
		RasterFilter filter = new RegionAlphaByteFilter();

		//Cuando el filtro esta creado, tomamos los valores y lo añadimos a la pila
		if (filter != null) {
			filter.addParam("rois", rois);
			filter.addParam("inverse", inverse);
			filter.addParam("alpha", Integer.valueOf(alpha));
			filterList.add(filter);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.IRasterFilterListManager#getRasterFilterList()
	 */
	public ArrayList getRasterFilterList() {
		ArrayList filters = new ArrayList();
		filters.add(RegionAlphaFilter.class);
		return filters;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.IRasterFilterListManager#addFilter(java.lang.Class, org.gvsig.raster.dataset.Params)
	 */
	public void addFilter(Class classFilter, Params params) throws FilterTypeException {
		if (classFilter.equals(RegionAlphaFilter.class)) {
			ArrayList rois = new ArrayList();
			Boolean inverse = new Boolean(false);
			int alpha = 255;

			Params paramsUI = null;
			for (int i = 0; i < params.getNumParams(); i++) {
				if (params.getParam(i).id.equals("Panel") &&
					params.getParam(i).defaultValue instanceof RegistrableFilterListener) {
					paramsUI = ((RegistrableFilterListener) params.getParam(i).defaultValue).getParams();
				}
				if (params.getParam(i).id.equals("Alpha") && params.getParam(i).defaultValue instanceof Integer) {
					alpha = ((Integer) params.getParam(i).defaultValue).intValue();
				}
			}

			if (paramsUI != null) {
				for (int i = 0; i < paramsUI.getNumParams(); i++) {
					if (paramsUI.getParam(i).id.equals("rois"))
						rois = (ArrayList) paramsUI.getParam(i).defaultValue;
					if (paramsUI.getParam(i).id.equals("inverse"))
						inverse = (Boolean) paramsUI.getParam(i).defaultValue;
				}
			}
			addRegionAlphaFilter(rois, alpha, inverse);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.IRasterFilterListManager#createFilterListFromStrings(java.util.ArrayList, java.lang.String, int)
	 */
	public int createFilterListFromStrings(ArrayList filters, String fil, int filteri) throws FilterTypeException {
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