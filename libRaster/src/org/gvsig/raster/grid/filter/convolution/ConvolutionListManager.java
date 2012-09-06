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
package org.gvsig.raster.grid.filter.convolution;

import java.util.ArrayList;

import org.gvsig.raster.dataset.Params;
import org.gvsig.raster.grid.filter.FilterTypeException;
import org.gvsig.raster.grid.filter.IRasterFilterListManager;
import org.gvsig.raster.grid.filter.RasterFilter;
import org.gvsig.raster.grid.filter.RasterFilterList;
import org.gvsig.raster.grid.filter.RasterFilterListManager;
import org.gvsig.raster.grid.filter.RegistrableFilterListener;
import org.gvsig.raster.grid.filter.RasterFilter.Kernel;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.extensionpoint.ExtensionPoint;
import org.gvsig.tools.extensionpoint.ExtensionPointManager;
/**
 * Gestion  de la lista de filtros
 */
public class ConvolutionListManager implements IRasterFilterListManager {
	protected RasterFilterList filterList = null;

	public ConvolutionListManager(RasterFilterListManager filterListManager) {
		this.filterList = filterListManager.getFilterList();
	}

	/**
	 * Registra ConvolutionListManager en los puntos de extension de RasterFilter
	 */
	public static void register() {
		ExtensionPointManager extensionPoints =ToolsLocator.getExtensionPointManager();
		ExtensionPoint point=extensionPoints.get("RasterFilter");
		point.append("Convolucion", "", ConvolutionListManager.class);
	}

	/**
	 * Añade un filtro de convolucion  a la pila de filtros.
	 * @param ladoVentana
	 * @throws FilterTypeException
	 */
	public void addConvolutionFilter(String Name,int ladoVentana, double agudeza, Kernel kernel) throws FilterTypeException {
		RasterFilter filter = new ConvolutionByteFilter();

		// Cuando el filtro esta creado, tomamos los valores y lo añadimos a la pila
		if (filter != null) {
			filter.addParam("ladoVentana", Integer.valueOf(ladoVentana));
			filter.addParam("Agudeza", Double.valueOf(agudeza));
			filter.addParam("filterName", String.valueOf(Name));
			filter.addParam("kernel", kernel);
			filterList.add(filter);
		}
	}

	public ArrayList getStringsFromFilterList(ArrayList filterList, RasterFilter rf) {
		if (rf instanceof ConvolutionFilter) {
			filterList.add("filter.convolution.active=true");
			ConvolutionFilter convolutionFilter = (ConvolutionFilter) rf;
			switch (convolutionFilter.ladoVentana) {
				case 0:
					filterList.add("filter.convolution.ladoVentana=3");
					break;
				case 1:
					filterList.add("filter.convolution.ladoVentana=5");
					break;
				case 2:
					filterList.add("filter.convolution.ladoVentana=7");
					break;
			}

			filterList.add("filter.convolution.filterName=" + convolutionFilter.getName());
			if (convolutionFilter.getName().equals("personalizado")) {
				double[][] listDouble = convolutionFilter.kernel.kernel;
				String listString = "";
				for (int i = 0; i < listDouble.length; i++)
					for (int j = 0; j < listDouble[0].length; j++)
						listString = listString + listDouble[i][j] + " ";

				listString = listString.trim();
				filterList.add("filter.convolution.kernel=" + listString);
			}
		}

		return filterList;
	}

	public void addFilter(Class classFilter, Params params) throws FilterTypeException {
		if (classFilter.equals(ConvolutionFilter.class)) {
			int ladoVentana = 0;
			double agudeza = 0;
			Kernel kernel = null;
			String name = "";
			for (int i = 0; i < params.getNumParams(); i++) {
				if (params.getParam(i).id.equals("Panel") &&
					params.getParam(i).defaultValue instanceof RegistrableFilterListener) {
					params = ((RegistrableFilterListener) params.getParam(i).defaultValue).getParams();
					break;
				}
			}

			for (int i = 0; i < params.getNumParams(); i++) {
				if (params.getParam(i).id.equals("LadoVentana") &&
					params.getParam(i).defaultValue instanceof Integer)
					ladoVentana = ((Integer) params.getParam(i).defaultValue).intValue();

				if (params.getParam(i).id.equals("FilterName"))
					name = new String((String) params.getParam(i).defaultValue);

				if (params.getParam(i).id.equals("Kernel") &&
						params.getParam(i).defaultValue instanceof Kernel)
					kernel = (Kernel) params.getParam(i).defaultValue;

				if (params.getParam(i).id.equals("Agudeza") &&
						params.getParam(i).defaultValue instanceof Double)
					agudeza = ((Double) params.getParam(i).defaultValue).doubleValue();
			}
			addConvolutionFilter(name, ladoVentana, agudeza, kernel);
		}
	}

	public int createFilterListFromStrings(ArrayList filters, String fil, int filteri) throws FilterTypeException {
		if ((fil.startsWith("filter.convolution.active")) && (RasterFilterListManager.getValue(fil).equals("true"))) {
			int ladoVentana = 0;
			double agudeza = 0;
			String name = "";
			Kernel kernel = null;
			filters.remove(0);
			for (int prop = 0; prop < filters.size(); prop++) {
				String elem = (String) filters.get(prop);
				if (elem.startsWith("filter.convolution.ladoVentana")) {
					ladoVentana = Integer.parseInt(RasterFilterListManager.getValue(elem));
					ladoVentana = (ladoVentana == 7) ? 2 : (ladoVentana == 5) ? 1 : 0;
					filters.remove(prop);
					prop--;
				}
				if (elem.startsWith("filter.convolution.filterName")) {
					name = RasterFilterListManager.getValue(elem);
					filters.remove(prop);
					prop--;
				}
				if (elem.startsWith("filter.convolution.kernel")) {
					String k = RasterFilterListManager.getValue(elem);
					String[] listString = k.split(" ");
					if (listString != null) {
						int lado = (ladoVentana == 0) ? 3 : (ladoVentana == 1) ? 5 : 7;
						double[][] listDouble = new double[lado][lado];
						int cont = 0;
						for (int i = 0; i < lado; i++) {
							for (int j = 0; j < lado; j++) {
								try {
									listDouble[i][j] = Double.parseDouble(listString[cont]);
									cont++;
								} catch (NumberFormatException e) {
								}
							}
						}
						kernel = new Kernel(listDouble);
					}
					filters.remove(prop);
					prop--;
				}
				if (elem.startsWith("filter.convolution.agudeza")) {
					agudeza = Double.parseDouble(RasterFilterListManager.getValue(elem));
					filters.remove(prop);
					prop--;
				}
			}
			addConvolutionFilter(name, ladoVentana, agudeza, kernel);
		}
		return filteri;
	}

	public ArrayList getRasterFilterList() {
		ArrayList filters = new ArrayList();
		filters.add(ConvolutionFilter.class);
		return filters;
	}
}