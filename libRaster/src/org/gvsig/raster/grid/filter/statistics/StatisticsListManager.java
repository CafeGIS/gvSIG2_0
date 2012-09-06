/* gvSIG. Sistema de Informaciï¿½n Geogrï¿½fica de la Generalitat Valenciana
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
package org.gvsig.raster.grid.filter.statistics;

import java.util.ArrayList;

import org.gvsig.i18n.Messages;
import org.gvsig.raster.dataset.Params;
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
 * Gestor de la pila de filtros para filtros estadísticos.
 *
 * @version 31/05/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class StatisticsListManager implements IRasterFilterListManager {
	protected RasterFilterList			filterList				= null;
	private RasterFilterListManager	filterListManager	= null;
	private IStatistics							stats							= null;

	/**
	 * Constructor. Asigna el gestor de todos los filtros y el objeto que
	 * contiene las estadísticas.
	 * @param filterListManager Gestor general de filtros donde se registran los gestores de filtros
	 * de las extensiones
	 * @param stats Estadisticas
	 */
	public StatisticsListManager(RasterFilterListManager filterListManager) {
		this.filterListManager = filterListManager;
		this.filterList = filterListManager.getFilterList();
		stats = (IStatistics)filterListManager.getFilterList().getEnvParam("IStatistics");
	}

	/**
	 * Registra StatisticsListManager en los puntos de extension de RasterFilter
	 */
	public static void register() {
		ExtensionPointManager extensionPoints =ToolsLocator.getExtensionPointManager();
		ExtensionPoint point=extensionPoints.get("RasterFilter");
		point.append("Statistics", "", StatisticsListManager.class);
	}

	/**
	 * Constructor. Asigna el gestor de todos los filtros y el objeto que
	 * contiene las estadísticas.
	 * @param filterListManager Gestor general de filtros donde se registran los gestores de filtros
	 * de las extensiones
	 * @param stats Estadisticas
	 */
	public StatisticsListManager(RasterFilterListManager filterListManager, IStatistics stats) {
		this(filterListManager);
		this.stats = stats;
	}

	/**
	 * Añade un filtro de recorte de colas.
	 * @param tail porcentaje de recorte
	 * @param samples porcentaje de muestras tomadas del total de la imagen
	 * @param removeMaxValue
	 * @param stats
	 * @throws FilterTypeException
	 */
	public void addTailFilter(double tail, double samples, boolean removeMaxValue, IStatistics stats) throws FilterTypeException {
		RasterFilter filter = createTailFilter(tail, samples, removeMaxValue, stats);

		if (filter != null)
			filterList.add(filter);
	}

	/**
	 * Añade un filtro de recorte de colas con una lista de valores de recorte a obtener
	 * @param tail porcentaje de recorte
	 * @param samples porcentaje de muestras tomadas del total de la imagen
	 * @param removeMaxValue
	 * @param stats
	 * @throws FilterTypeException
	 */
	public void addTailFilter(double[] tailList, double samples, boolean removeMaxValue, IStatistics stats) throws FilterTypeException {
		RasterFilter filter = createTailFilter(tailList, samples, removeMaxValue, stats);

		if (filter != null)
			filterList.add(filter);
	}

	/**
	 * Crea un filtro de recorte de forma estática
	 * @param tail Porcentaje de recorte
	 * @param samples
	 * @param removeMaxValue
	 * @param stats
	 * @return
	 */
	public static RasterFilter createTailFilter(double tail, double samples, boolean removeMaxValue, IStatistics stats) {
		RasterFilter filter = new TailTrimFloatFilter();

		if (filter != null) {
			filter.addParam("stats", stats);
			filter.addParam("tail", new Double(tail));
			filter.addParam("samples", new Double(samples));
			filter.addParam("remove", new Boolean(removeMaxValue));
		}

		return filter;
	}

	/**
	 * Crea un filtro de recorte de forma estática
	 * @param tailList Lista de porcentajes de recorte
	 * @param samples
	 * @param removeMaxValue
	 * @param stats
	 * @return
	 */
	public static RasterFilter createTailFilter(double[] tailList, double samples, boolean removeMaxValue, IStatistics stats) {
		RasterFilter filter = new TailTrimFloatFilter();

		if (filter != null) {
			filter.addParam("stats", stats);
			filter.addParam("tailList", tailList);
			filter.addParam("samples", new Double(samples));
			filter.addParam("remove", new Boolean(removeMaxValue));
		}

		return filter;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.gvsig.raster.grid.filter.IRasterFilterListManager#getStringsFromFilterList(java.util.ArrayList,
	 *      org.gvsig.raster.grid.filter.RasterFilter)
	 */
	public ArrayList getStringsFromFilterList(ArrayList filterList, RasterFilter rf) {
		if (rf instanceof TailTrimFilter) {
			filterList.add("filter.tail.active=true");
			filterList.add("filter.tail.value=" + ((TailTrimFilter) rf).tailPercent);
			filterList.add("filter.tail.remove=" + ((TailTrimFilter) rf).removeMaxValue());
		}

		return filterList;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.gvsig.raster.grid.filter.IRasterFilterListManager#createFilterListFromStrings(java.util.ArrayList,
	 *      java.lang.String, int)
	 */
	public int createFilterListFromStrings(ArrayList filters, String fil, int filteri) throws FilterTypeException {
		if (fil.startsWith("filter.tail.active") && RasterFilterListManager.getValue(fil).equals("true")) {
			filters.remove(filteri);
			filterListManager.removeFilter(Messages.getText(TailTrimFilter.names[0]));

			double recorte = 0D;
			boolean remove = false;

			for (int propFilter = 0; propFilter < filters.size(); propFilter++) {
				String elem = (String) filters.get(propFilter);

				if (elem.startsWith("filter.tail.value")) {
					recorte = Double.parseDouble(RasterFilterListManager.getValue(elem));
					filters.remove(propFilter);
					propFilter--;
				}

				if (elem.startsWith("filter.tail.remove")) {
					remove = Boolean.valueOf(RasterFilterListManager.getValue(elem)).booleanValue();
					filters.remove(propFilter);
					propFilter--;
				}
			}

			this.addTailFilter(recorte, 0D, remove, stats);
			filteri = -1;
		}
		return filteri;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.gvsig.raster.grid.filter.IRasterFilterListManager#getRasterFilterList()
	 */
	public ArrayList getRasterFilterList() {
		ArrayList filters = new ArrayList();
		filters.add(TailTrimFilter.class);
		return filters;
	}

	public void addFilter(Class classFilter, Params params) {
		// TODO: FUNCIONALIDAD: Implementacion de añadir un filtro
	}
}