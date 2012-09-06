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

import java.util.ArrayList;

import org.gvsig.raster.dataset.FileNotOpenException;
import org.gvsig.raster.dataset.Params;
import org.gvsig.raster.dataset.RasterDriverException;
import org.gvsig.raster.datastruct.Histogram;
import org.gvsig.raster.grid.filter.FilterTypeException;
import org.gvsig.raster.grid.filter.IRasterFilterListManager;
import org.gvsig.raster.grid.filter.RasterFilter;
import org.gvsig.raster.grid.filter.RasterFilterList;
import org.gvsig.raster.grid.filter.RasterFilterListManager;
import org.gvsig.raster.hierarchy.IStatistics;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.extensionpoint.ExtensionPoint;
import org.gvsig.tools.extensionpoint.ExtensionPointManager;
/**
 * Gestor de la pila de filtros para el filtro de ecualización de histograma.
 *
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class EqualizationManager implements IRasterFilterListManager {

	protected RasterFilterList 			filterList = null;

	/**
	 * Constructor
	 * @param filterListManager
	 */
	public EqualizationManager(RasterFilterListManager filterListManager) {
		this.filterList = filterListManager.getFilterList();
	}

	/**
	 * Registra EqualizationManager en los puntos de extension de RasterFilter
	 */
	public static void register() {
		ExtensionPointManager extensionPoints =ToolsLocator.getExtensionPointManager();
		ExtensionPoint point=extensionPoints.get("RasterFilter");
		point.append("Equalization", "", EqualizationManager.class);
	}

	/**
	 * Añade un filtro de ecualización de histograma.
	 * @param IStatistics
	 * @throws FilterTypeException
	 */
	public void addEqualizationFilter(IStatistics stats, int[] renderBands, Histogram hist, int[] ecualizedBands) throws FilterTypeException {
		try {
			if (!stats.isCalculated()) {
				try {
					stats.calcFullStatistics();
				} catch (FileNotOpenException e) {
					// No podemos aplicar el filtro
					return;
				} catch (RasterDriverException e) {
					// No podemos aplicar el filtro
					return;
				}
			}

			RasterFilter filter = createEnhancedFilter(stats, renderBands, hist, ecualizedBands);
			if (filter != null)
				filterList.add(filter);
		} catch (InterruptedException e) {
			//Si se ha interrumpido no añadimos el filtro
		}
	}

	/**
	 * Crea un filtro de Ecualización de histograma.
	 * @param stats
	 * @param renderBands
	 * @return
	 */
	public static RasterFilter createEnhancedFilter(IStatistics stats, int[] renderBands, Histogram hist, int[] ecualizedBands) {
		RasterFilter filter = new EqualizationByteFilter();
		if (filter != null) {
			filter.addParam("stats", stats);
			filter.addParam("renderBands", renderBands);
			filter.addParam("histogram", hist);
			filter.addParam("ecualizedBands", ecualizedBands);
		}
		return filter;
	}

	/**
	 * Obtiene un Array de Strings a partir de una pila de filtros. Cada elemento
	 * del array tendrá la forma de elemento=valor.
	 */
	public ArrayList getStringsFromFilterList(ArrayList filterList, RasterFilter rf) {
		return filterList;
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
	 * @see org.gvsig.raster.grid.filter.IRasterFilterListManager#getRasterFilterList()
	 */
	public ArrayList getRasterFilterList() {
		ArrayList filters = new ArrayList();
		filters.add(EqualizationFilter.class);
		return filters;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.IRasterFilterListManager#addFilter(java.lang.Class, org.gvsig.raster.dataset.Params)
	 */
	public void addFilter(Class classFilter, Params params) throws FilterTypeException {
		if (classFilter.equals(EqualizationFilter.class)) {
			int[] renderBands = { 0, 1, 2 };
			int[] ecualizedBands = { 0, 1, 2 };
			Histogram hist = null;
			for (int i = 0; i < params.getNumParams(); i++) {
				if (params.getParam(i).id.equals("RenderBands") &&
					params.getParam(i).defaultValue instanceof String) {
					String[] bands = new String((String) params.getParam(i).defaultValue).split(" ");
					renderBands[0] = new Integer(bands[0]).intValue();
					renderBands[1] = new Integer(bands[1]).intValue();
					renderBands[2] = new Integer(bands[2]).intValue();
					continue;
				}
				if (params.getParam(i).id.equals("Histogram") &&
					params.getParam(i).defaultValue instanceof Histogram) {
					hist = (Histogram)params.getParam(i).defaultValue;
				}
				if (params.getParam(i).id.equals("EcualizedBands") &&
					params.getParam(i).defaultValue instanceof int[]) {
					ecualizedBands = (int[])params.getParam(i).defaultValue;
				}
			}

			addEqualizationFilter((IStatistics) filterList.getEnvParam("IStatistics"), renderBands, hist, ecualizedBands);
		}
	}
}