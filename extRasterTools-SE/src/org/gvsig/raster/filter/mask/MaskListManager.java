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
package org.gvsig.raster.filter.mask;

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

/**
 * Gestor de los filtros de mascara. Los filtros de máscara generan una
 * capa enmascarando con regiones de interés. Las zonas fuera del ROI se
 * pondrán a NoData.
 *
 * 14/03/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class MaskListManager implements IRasterFilterListManager {
	protected RasterFilterList	filterList = null;

	/**
	 * Constructor. Asigna la lista de filtros y el manager.
	 * @param filterListManager
	 */
	public MaskListManager(RasterFilterListManager filterListManager) {
		this.filterList = filterListManager.getFilterList();
	}

	/**
	 * Registrar los manager en los puntos de extension
	 */
	public static void register() {
		ExtensionPointManager extensionPoints =ToolsLocator.getExtensionPointManager();
		ExtensionPoint point=extensionPoints.add("RasterFilter");
		point.append("Mask", "", MaskListManager.class);
	}

	/**
	 * Añade un filtro de máscara
	 * @param rois Lista de ROIs
	 * @param noData Valor a asignar fuera de las ROI
	 * @param inverse Inversa
	 * @throws FilterTypeException
	 */
	public void addMaskFilter(ArrayList rois, Double noData, Boolean inverse, Boolean transp) throws FilterTypeException {
		RasterFilter filter = new MaskByteFilter();

		//Cuando el filtro esta creado, tomamos los valores y lo añadimos a la pila
		if (filter != null) {
			filter.addParam("rois", rois);
			filter.addParam("nodata", noData);
			filter.addParam("inverse", inverse);
			filter.addParam("transparency", transp);
			filterList.add(filter);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.IRasterFilterListManager#getRasterFilterList()
	 */
	public ArrayList getRasterFilterList() {
		ArrayList filters = new ArrayList();
		filters.add(MaskFilter.class);
		return filters;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.IRasterFilterListManager#addFilter(java.lang.Class, org.gvsig.raster.dataset.Params)
	 */
	public void addFilter(Class classFilter, Params params) throws FilterTypeException {
		if (classFilter.equals(MaskFilter.class)) {
			ArrayList rois = new ArrayList();
			Boolean inverse = new Boolean(false);
			Boolean transp = new Boolean(false);
			Double nodata = new Double(-99999);

			Params paramsUI = null;
			for (int i = 0; i < params.getNumParams(); i++) {
				if (params.getParam(i).id.equals("Panel") &&
					params.getParam(i).defaultValue instanceof RegistrableFilterListener) {
					paramsUI = ((RegistrableFilterListener) params.getParam(i).defaultValue).getParams();
				}
			}

			if (paramsUI != null) {
				for (int i = 0; i < paramsUI.getNumParams(); i++) {
					if (paramsUI.getParam(i).id.equals("rois"))
						rois = (ArrayList) paramsUI.getParam(i).defaultValue;
					if (paramsUI.getParam(i).id.equals("inverse"))
						inverse = (Boolean) paramsUI.getParam(i).defaultValue;
					if (paramsUI.getParam(i).id.equals("nodata"))
						nodata = (Double) paramsUI.getParam(i).defaultValue;
					if (paramsUI.getParam(i).id.equals("transparency"))
						transp = (Boolean) paramsUI.getParam(i).defaultValue;
				}
			}
			addMaskFilter(rois, nodata, inverse, transp);
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