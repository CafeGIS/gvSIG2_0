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
package org.gvsig.raster.grid.filter.pansharp;

import java.util.ArrayList;

import org.gvsig.raster.dataset.Params;
import org.gvsig.raster.grid.filter.FilterTypeException;
import org.gvsig.raster.grid.filter.IRasterFilterListManager;
import org.gvsig.raster.grid.filter.RasterFilter;
import org.gvsig.raster.grid.filter.RasterFilterList;
import org.gvsig.raster.grid.filter.RasterFilterListManager;
import org.gvsig.raster.hierarchy.IRasterDataset;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.extensionpoint.ExtensionPoint;
import org.gvsig.tools.extensionpoint.ExtensionPointManager;
/**
 * Gestion  de la lista de filtros
 */
public class PanSharpeningListManager implements IRasterFilterListManager {
	protected RasterFilterList filterList = null;

	// Constructor
	public PanSharpeningListManager(RasterFilterListManager filterListManager){
		this.filterList = filterListManager.getFilterList();
	}

	/**
	 * Registra PanSharpeningListManager en los puntos de extension de RasterFilter
	 */
	public static void register() {
		ExtensionPointManager extensionPoints =ToolsLocator.getExtensionPointManager();
		ExtensionPoint point=extensionPoints.get("RasterFilter");
		point.append("PanSharpening", "", PanSharpeningListManager.class);
	}

	public void addFilter(Class classFilter, Params params) throws FilterTypeException {
		if (classFilter.equals(PanSharpeningFilter.class)) {
			String method = "";
			String fileNameOutput = null;
			IRasterDataset dataset = null;
			ArrayList order = null;
			int alpha = 0, coefBrovey = 0, posPancromatica = -1;
			double coef = 0;

			for (int i = 0; i < params.getNumParams(); i++) {
				if (params.getParam(i).id.equals("dataset"))
					dataset = (IRasterDataset) params.getParam(i);
				if (params.getParam(i).id.equals("posPancromatica"))
					posPancromatica = new Integer((String) params.getParam(i).defaultValue).intValue();
				if (params.getParam(i).id.equals("order"))
					order = (ArrayList) params.getParam(i).defaultValue;
				if (params.getParam(i).id.equals("alpha"))
					alpha = new Integer((String) params.getParam(i).defaultValue).intValue();
				if (params.getParam(i).id.equals("method"))
					method = new String((String) params.getParam(i).defaultValue);
				if (params.getParam(i).id.equals("coef"))
					coef = new Double((String) params.getParam(i).defaultValue).doubleValue();
				if (params.getParam(i).id.equals("coefBrovey"))
					coefBrovey = new Integer((String) params.getParam(i).defaultValue).intValue();
				if (params.getParam(i).id.equals("fileNameOutput"))
					fileNameOutput = new String((String) params.getParam(i).defaultValue);
			}
			addPanSharpeningFilter(dataset, posPancromatica, order, alpha, method, coef, coefBrovey, fileNameOutput);
		}
	}

	/**
	 * Añade un filtro pansharp a la pila de filtros.
	 * @param dataset: dataset con los ficheros
	 * @param posPancromatica  posicion de la banda pancromatica
	 * @param orden: orden de asignacion de las bandas a RGB. El valor contenido en cada posicion del array
	 * 		  se corresponde al número de banda de fichero que se visualiza en R, G y B.
	 * @param alpha
	 * @param method  metodo(HSL o Brovey)
	 * @param coef
	 * @param coefBrovey
	 * @throws FilterTypeException
	 */
	public void addPanSharpeningFilter(IRasterDataset dataset,int posPancromatica, ArrayList order, int alpha, String method, double coef, int coefBrovey,String fileNameOutput) throws FilterTypeException {
		RasterFilter filter = new PanSharpeningByteFilter();

		if (filter != null) {
			filter.addParam("dataset", dataset);
			filter.addParam("posPancromatica", new Integer(posPancromatica));
			filter.addParam("order", order);
			filter.addParam("alpha", new Integer(alpha));
			filter.addParam("method", method);
			filter.addParam("coef", new Double(coef));
			filter.addParam("coefBrovey", new Integer(coefBrovey));
			filter.addParam("fileNameOutput", fileNameOutput);
			filterList.add(filter);
		}
	}

	public int createFilterListFromStrings(ArrayList filters, String fil, int filteri) {

		/*		if ((fil.startsWith("filter.pansharpening.active")) && (RasterFilterListManager.getValue(fil).equals("true"))) {

			int posPancromatica = 0;
						int[] bandOrder = {0, 1, 2};
						ArrayList files = new ArrayList();
						IRasterDataset dataset=null;
						int alpha = 0;
						String method = "";
						double coef = 0D;
						int coefBrovey = 0;

						for (int propFilter = 0;propFilter < filters.size();propFilter++) {
								String elem = (String) filters.get(propFilter);

								if (elem.startsWith("filter.pansharpening.dataset")) {
										dataset = RasterFilterListManager.getValue(elem);
										filters.remove(propFilter);
										propFilter--;
								}
								if (elem.startsWith("filter.pansharpening.posPancromatica")) {
										posPancromatica = Integer.parseInt(RasterFilterListManager.getValue(elem));
										filters.remove(propFilter);
										propFilter--;
								}
								if (elem.startsWith("filter.pansharpening.bandOrder")) {
									String rango = RasterFilterListManager.getValue(elem);
										bandOrder[0] = Integer.parseInt(rango.substring(0, rango.indexOf(",")));
										bandOrder[1] = Integer.parseInt(rango.substring(rango.indexOf(",") + 1, rango.lastIndexOf(",")));
										bandOrder[2] = Integer.parseInt(rango.substring(rango.lastIndexOf(",") + 1, rango.length()));
										filters.remove(propFilter);
										propFilter--;
								}
								if (elem.startsWith("filter.pansharpening.file")) {
										files.add(RasterFilterListManager.getValue(elem));
										filters.remove(propFilter);
										propFilter--;
								}
								if (elem.startsWith("filter.pansharpening.alpha")) {
										alpha = Integer.parseInt(RasterFilterListManager.getValue(elem));
										filters.remove(propFilter);
										propFilter--;
								}
								if (elem.startsWith("filter.pansharpening.method")) {
										method = RasterFilterListManager.getValue(elem);
										filters.remove(propFilter);
										propFilter--;
								}
								if (elem.startsWith("filter.pansharpening.coefHSL")) {
										coef = Double.parseDouble(RasterFilterListManager.getValue(elem));
										filters.remove(propFilter);
										propFilter--;
								}
					if (elem.startsWith("filter.pansharpening.coefBrovey")) {
						coefBrovey = Integer.parseInt(RasterFilterListManager.getValue(elem));
						//filters.remove(propFilter);
						//propFilter--;
					}
						}

						addPanSharpeningFilter(dataset,posPancromatica, bandOrder, alpha, method, coef, coefBrovey);
		}*/
		return filteri;
	}

	public ArrayList getStringsFromFilterList(ArrayList filterList, RasterFilter rf) {
		if (rf instanceof PanSharpeningFilter) {
			PanSharpeningFilter pansharpFilter = (PanSharpeningFilter) rf;
			filterList.add("filter.pansharpening.active=true");
			filterList.add("filter.pansharpening.dataset=" + pansharpFilter.dataset);
			filterList.add("filter.pansharpening.posPancromatica=" + pansharpFilter.posPancromatica);
			filterList.add("filter.pansharpening.bandOrder=" + pansharpFilter.bandOrder.get(0) + "," + pansharpFilter.bandOrder.get(1) + "," + pansharpFilter.bandOrder.get(2));
			filterList.add("filter.pansharpening.method=" + pansharpFilter.method);
			filterList.add("filter.pansharpening.coefHSL=" + pansharpFilter.coef);
			filterList.add("filter.pansharpening.coefBrovey=" + pansharpFilter.coefBrovey);
			filterList.add("filter.pansharpening.alpha=" + pansharpFilter.alpha);
			filterList.add("filter.pansharp.filterName=" + pansharpFilter.getName());
		}
		return filterList;
	}

	public ArrayList getRasterFilterList() {
		ArrayList filters = new ArrayList();
		filters.add(PanSharpeningFilter.class);
		return filters;
	}
}
