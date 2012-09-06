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
import org.gvsig.raster.grid.filter.FilterTypeException;
import org.gvsig.raster.grid.filter.IRasterFilterListManager;
import org.gvsig.raster.grid.filter.RasterFilter;
import org.gvsig.raster.grid.filter.RasterFilterList;
import org.gvsig.raster.grid.filter.RasterFilterListManager;
import org.gvsig.raster.grid.filter.statistics.StatisticsListManager;
import org.gvsig.raster.hierarchy.IStatistics;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.extensionpoint.ExtensionPoint;
import org.gvsig.tools.extensionpoint.ExtensionPointManager;
/**
 * Gestor de la pila de filtros para el filtro de realce.
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class EnhancementListManager implements IRasterFilterListManager {
	protected RasterFilterList      filterList        = null;
	private RasterFilterListManager filterListManager = null;
	private IStatistics             stats             = null;

	/**
	 * Constructor
	 * @param filterListManager
	 */
	public EnhancementListManager(RasterFilterListManager filterListManager) {
		this.filterListManager = filterListManager;
		this.filterList = filterListManager.getFilterList();
		stats = (IStatistics)filterList.getEnvParam("IStatistics");
	}

	/**
	 * Asigna el objeto con las estadisticas.
	 * @param stats
	 */
	public void setStatistics(IStatistics stats) {
		this.stats = stats;
	}

	/**
	 * Registra EnhancementListManager en los puntos de extension de RasterFilter
	 */
	public static void register() {
		ExtensionPointManager extensionPoints =ToolsLocator.getExtensionPointManager();
		ExtensionPoint point=extensionPoints.get("RasterFilter");
		point.append("Enhancement", "", EnhancementListManager.class);
	}

	/**
	 * Añade un filtro de realce.
	 * La forma de inserción del filtro es fija ya que la inserción de un realce lleva implicita
	 * la inserción de un filtro de recorte de colas (tailtrim), aunque no en todos los casos.
	 * Si ya existe un filtro de realce en la lista se obtiene la posición de este.
	 * Si es necesario un recorte de colas entonces se comprueba si existe un uno reemplazandose
	 * por el nuevo y sino se insertará uno. Al final reemplazamos el realce que existia.
	 *
	 * Si por el contrario no existen realce ni trim se añaden ambos al final de la lista.
	 * @param removeEnds eliminar extremos en los máximos y mínimos
	 * @param stats Objeto de estadisticas asociado
	 * @param tailTrim porcentaje de recorte de colas. Será un valor entre 0 y 1.
	 * @param insertionMode Modo de inserción
	 * @param renderBands bandas RGB mostradas en la visualización.
	 * @throws FilterTypeException
	 */
	public void addEnhancedFilter(boolean removeEnds, IStatistics stats, double tailTrim, int[] renderBands) throws FilterTypeException {
		try {
			if (tailTrim == 0) { // En este caso siempre es necesario el máximo y
				// mínimo
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
			} else {
				StatisticsListManager slm = new StatisticsListManager(filterListManager, stats);
				slm.addTailFilter(tailTrim, 0D, removeEnds, stats);
			}

			RasterFilter filter = createEnhancedFilter(removeEnds, stats, tailTrim, renderBands);
			if (filter != null)
				filterList.add(filter);
		} catch (InterruptedException e) {
			//Si se ha interrumpido no añadimos el filtro
		}
	}

	public static RasterFilter createEnhancedFilter(boolean removeEnds, IStatistics stats, double tailTrim, int[] renderBands) {
		RasterFilter filter = new LinearEnhancementFloatFilter();
		if (filter != null) {
			filter.addParam("stats", stats);
			if (removeEnds) {
				filter.addParam("remove", new Boolean(true));
			} else {
				filter.addParam("remove", new Boolean(false));
			}
			filter.addParam("tailTrim", new Double(tailTrim));
			filter.addParam("renderBands", renderBands);
		}

		return filter;
	}

	/**
	 * Obtiene un Array de Strings a partir de una pila de filtros. Cada elemento
	 * del array tendrá la forma de elemento=valor.
	 */
	public ArrayList getStringsFromFilterList(ArrayList filterList, RasterFilter rf) {
		if (rf instanceof LinearEnhancementFilter) {
			filterList.add("filter.enhanced.active=true");
			filterList.add("filter.enhanced.tailTrim=" + ((LinearEnhancementFilter) rf).getTailTrim().toString());
			StringBuffer values = new StringBuffer();
			for (int i = 0; i < ((LinearEnhancementFilter) rf).renderBands.length; i++) {
				values.append(((LinearEnhancementFilter) rf).renderBands[i]);
				if (i < ((LinearEnhancementFilter) rf).renderBands.length - 1)
					values.append(",");
			}
			filterList.add("filter.enhanced.renderbands=" + values.toString());
			filterList.add("filter.enhanced.remove=" + ((LinearEnhancementFilter) rf).getRemoveEnds().toString());
		}

		return filterList;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.IRasterFilterListManager#createFilterListFromStrings(java.util.ArrayList, java.lang.String, int)
	 */
	public int createFilterListFromStrings(ArrayList filters, String fil, int filteri) throws FilterTypeException {
		if (fil.startsWith("filter.enhanced.active") && RasterFilterListManager.getValue(fil).equals("true") && stats != null) {
			filters.remove(filteri);
			double tailTrim = 0D;
			int[] renderBands = new int[] { 0, 0, 0 };

			for (int propFilter = 0; propFilter < filters.size(); propFilter++) {
				String elem = (String) filters.get(propFilter);

				if (elem.startsWith("filter.enhanced.tailTrim")) {
					try {
						tailTrim = new Double(RasterFilterListManager.getValue(elem)).doubleValue();
					} catch (NumberFormatException ex) {
						// tailTrim sigue valiendo 0
					}
				}

				if (elem.startsWith("filter.enhanced.renderbands")) {
					String value = RasterFilterListManager.getValue(elem);
					String[] valueList = value.split(",");
					renderBands = new int[valueList.length];
					for (int i = 0; i < renderBands.length; i++) {
						try {
							renderBands[i] = Integer.parseInt(valueList[i]);
						} catch (NumberFormatException e) {
							// No añade el valor
						}
					}
					// filters.remove(propFilter);
					// propFilter--;
				}

				if (elem.startsWith("filter.enhanced.remove")) {
					addEnhancedFilter(Boolean.valueOf(RasterFilterListManager.getValue(elem)).booleanValue(), stats, tailTrim, renderBands);
					filters.remove(propFilter);
					propFilter--;
				}
			}

			filteri = -1;
		}
		return filteri;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.IRasterFilterListManager#getRasterFilterList()
	 */
	public ArrayList getRasterFilterList() {
		ArrayList filters = new ArrayList();
		filters.add(LinearEnhancementFilter.class);
		return filters;
	}

	public void addFilter(Class classFilter, Params params) throws FilterTypeException {
		if (classFilter.equals(LinearEnhancementFilter.class)) {
			boolean removeEnds = false;
			double tailTrim = 0.0;
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
				if (params.getParam(i).id.equals("RemoveEnds") &&
					params.getParam(i).defaultValue instanceof Boolean) {
					removeEnds = ((Boolean) params.getParam(i).defaultValue).booleanValue();
					continue;
				}
				if (params.getParam(i).id.equals("TailTrim") &&
					params.getParam(i).defaultValue instanceof Double)
					tailTrim = ((Double) params.getParam(i).defaultValue).doubleValue() / 100.0;
			}

			addEnhancedFilter(removeEnds, (IStatistics) filterList.getEnvParam("IStatistics"), tailTrim, renderBands);
		}
	}
}