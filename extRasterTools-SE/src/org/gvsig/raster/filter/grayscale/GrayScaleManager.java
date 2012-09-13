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
package org.gvsig.raster.filter.grayscale;

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
 * Gestor del filtro de conversión a escala de grises
 *
 * 26/06/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class GrayScaleManager  implements IRasterFilterListManager {

	protected RasterFilterList	filterList = null;

	public static void register() {
		ExtensionPointManager extensionPoints =ToolsLocator.getExtensionPointManager();
		ExtensionPoint point=extensionPoints.get("RasterFilter");
		point.append("GrayScale", "", GrayScaleManager.class);
	}

	/**
	 * Constructor.
	 * Asigna la lista de filtros y el managener global.
	 *
	 * @param filterListManager
	 */
	public GrayScaleManager(RasterFilterListManager filterListManager) {
		this.filterList = filterListManager.getFilterList();
	}

	/**
	 * Añade un filtro de conversión de balance de color RGB.
	 * @param type. parámetro para el filtro que indica que banda o combinación de
	 * estas es usada para la conversión a escala de gris. El valor de este parámtro
	 * está definido en las constantes de la clase GrayScaleFilter
	 * @throws FilterTypeException
	 */
	public void addGrayScaleFilter(int type) throws FilterTypeException {
		RasterFilter filter = new GrayScaleByteFilter();

		if (filter != null) {
			filter.addParam("typeBand", new Integer(type));
			filterList.add(filter);
		}

	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.IRasterFilterListManager#getRasterFilterList()
	 */
	public ArrayList getRasterFilterList() {
		ArrayList filters = new ArrayList();
		filters.add(GrayScaleFilter.class);
		return filters;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.IRasterFilterListManager#addFilter(java.lang.Class, org.gvsig.raster.dataset.Params)
	 */
	public void addFilter(Class classFilter, Params params) throws FilterTypeException {
		if (classFilter.equals(GrayScaleFilter.class)) {
			int type = 0;

			for (int i = 0; i < params.getNumParams(); i++) {
				if (params.getParam(i).id.equals("typeBand"))
					type = ((Integer) params.getParam(i).defaultValue).intValue();
			}
			addGrayScaleFilter(type);
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