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
 * Gestor de los filtros de brillo y contraste. Contiene métodos para añadir
 * ambos tipos de filtro en la pila.
 *
 * @author Miguel Ángel Querol Carratalá (miguelangel.querol@iver.es)
 */
public class BrightnessContrastListManager implements IRasterFilterListManager {
	protected RasterFilterList			filterList				= null;

	/**
	 * Constructor. Asigna la lista de filtros y el manager.
	 * @param filterListManager
	 */
	public BrightnessContrastListManager(RasterFilterListManager filterListManager) {
		this.filterList = filterListManager.getFilterList();
	}

	/**
	 * Registra BrightnessContrastListManager en los puntos de extension de RasterFilter
	 */
	public static void register() {
		ExtensionPointManager extensionPoints =ToolsLocator.getExtensionPointManager();
		ExtensionPoint point=extensionPoints.get("RasterFilter");
		point.append("BrightnessContrast", "", BrightnessContrastListManager.class);
	}

	/**
	 * Añade un filtro de brillo a la pila de filtros.
	 * @param incrBrillo valor de brillo a aplicar
	 * @param removeAll true si se desea eliminar cualquier filtro de brillo que
	 * hubiera en la pila antes y false si se acumula sobre lo que haya
	 * @throws FilterTypeException
	 */
	public void addBrightnessFilter(int incrBrillo) throws FilterTypeException {
		RasterFilter filter = createBrightnessFilter(incrBrillo);

		if (filter != null)
			filterList.add(filter);
	}

	public static RasterFilter createBrightnessFilter(int incrBrillo) {
		RasterFilter filter = new BrightnessByteFilter();

		// Cuando el filtro esta creado, tomamos los valores y lo añadimos a la pila
		if (filter != null) {
			filter.addParam("incrBrillo", new Integer(incrBrillo));
		}
		return filter;
	}

	/**
	 * Añade un filtro de contraste a la pila de filtros.
	 * @param incrBrillo
	 * @throws FilterTypeException
	 */
	public void addContrastFilter(int incrContraste) throws FilterTypeException {
		RasterFilter filter = createContrastFilter(incrContraste);

		// Cuando el filtro esta creado, tomamos los valores y lo aï¿½adimos a la pila
		if (filter != null)
			filterList.add(filter);
	}

	public static RasterFilter createContrastFilter(int incrContraste) {
		RasterFilter filter = new ContrastByteFilter();

		// Cuando el filtro esta creado, tomamos los valores y lo aï¿½adimos a la pila
		if (filter != null)
			filter.addParam("incrContraste", new Integer(incrContraste));

		return filter;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.IRasterFilterListManager#getStringsFromFilterList(java.util.ArrayList, org.gvsig.raster.grid.filter.RasterFilter)
	 */
	public ArrayList getStringsFromFilterList(ArrayList filterList, RasterFilter rf) {

		if ((rf instanceof BrightnessFilter) || (rf instanceof ContrastFilter)) {
			filterList.add("filter.brightCont.active=true");
		}

		if (rf instanceof BrightnessFilter) {
			BrightnessFilter bright = (BrightnessFilter) rf;
			filterList.add("filter.brightness.incrBrillo=" + bright.incrBrillo);
		} else if (rf instanceof ContrastFilter) {
			ContrastFilter contrast = (ContrastFilter) rf;
			filterList.add("filter.contrast.incrContraste=" + contrast.incrContraste);
		}

		return filterList;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.IRasterFilterListManager#createFilterListFromStrings(java.util.ArrayList, java.lang.String, int)
	 */
	public int createFilterListFromStrings(ArrayList filters, String fil, int filteri) throws FilterTypeException {
		if ((fil.startsWith("filter.brightCont.active")) && (RasterFilterListManager.getValue(fil).equals("true"))) {

			int incrBrillo = 0;
			int incrContraste = 0;
			filters.remove(0);

			for (int prop = 0; prop < filters.size(); prop++) {
				String elem = (String) filters.get(prop);
				if (elem.startsWith("filter.brightness.incrBrillo")) {
					incrBrillo = Integer.parseInt(RasterFilterListManager.getValue(elem));
					addBrightnessFilter(incrBrillo);
					filters.remove(prop);
					prop--;
				}

				if (elem.startsWith("filter.contrast.incrContraste")) {
					incrContraste = Integer.parseInt(RasterFilterListManager.getValue(elem));
					addContrastFilter(incrContraste);
					filters.remove(prop);
					prop--;
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
		filters.add(BrightnessFilter.class);
		filters.add(ContrastFilter.class);
		return filters;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.IRasterFilterListManager#addFilter(java.lang.Class, org.gvsig.raster.dataset.Params)
	 */
	public void addFilter(Class classFilter, Params params) throws FilterTypeException {
		if (classFilter.equals(ContrastFilter.class)) {
			int contrast = 0;
			for (int i = 0; i < params.getNumParams(); i++) {
				if (params.getParam(i).id.equals("Contrast"))
					contrast = ((Integer) params.getParam(i).defaultValue).intValue();
			}
			addContrastFilter(contrast);
		}
		if (classFilter.equals(BrightnessFilter.class)) {
			int brightness = 0;
			for (int i = 0; i < params.getNumParams(); i++) {
				if (params.getParam(i).id.equals("Brightness") && params.getParam(i).defaultValue instanceof Integer)
					brightness = ((Integer) params.getParam(i).defaultValue).intValue();
			}
			addBrightnessFilter(brightness);
		}
	}
}