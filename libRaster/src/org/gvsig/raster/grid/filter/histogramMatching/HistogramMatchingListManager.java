/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2006 Instituto de Desarrollo Regional and Generalitat Valenciana.
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
 *
 * For more information, contact:
 *
 *  Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ibañez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   Instituto de Desarrollo Regional (Universidad de Castilla La-Mancha)
 *   Campus Universitario s/n
 *   02071 Alabacete
 *   Spain
 *
 *   +34 967 599 200
 */

package org.gvsig.raster.grid.filter.histogramMatching;

import java.util.ArrayList;

import org.gvsig.raster.dataset.IRasterDataSource;
import org.gvsig.raster.dataset.Params;
import org.gvsig.raster.datastruct.Histogram;
import org.gvsig.raster.grid.filter.FilterTypeException;
import org.gvsig.raster.grid.filter.IRasterFilterListManager;
import org.gvsig.raster.grid.filter.RasterFilter;
import org.gvsig.raster.grid.filter.RasterFilterList;
import org.gvsig.raster.grid.filter.RasterFilterListManager;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.extensionpoint.ExtensionPoint;
import org.gvsig.tools.extensionpoint.ExtensionPointManager;

/**
 * Gestion  de la lista de filtros para el filtro de tipo HistogramMatchig
 *
 * @author aMuÑoz (alejandro.munoz@uclm.es)
 * @version 27-05-2008
 *
 * */
public class HistogramMatchingListManager implements IRasterFilterListManager{

	protected RasterFilterList			filterList				= null;

	// Constructor
	public HistogramMatchingListManager(RasterFilterListManager filterListManager){
		this.filterList = filterListManager.getFilterList();
	}

	/** Metodo de registro del filtro */
	public static void register() {
		ExtensionPointManager extensionPoints =ToolsLocator.getExtensionPointManager();
		ExtensionPoint point=extensionPoints.get("RasterFilter");
		point.append("Matching", "", HistogramMatchingListManager.class);
	}


	public void addFilter(Class classFilter, Params params) throws FilterTypeException {
		if (classFilter.equals(HistogramMatchingFilter.class)) {
			String fileNameOutput=null;
			IRasterDataSource raster=null;
			int numbands=0;
			Histogram histogramReference		= null;
			for (int i = 0; i < params.getNumParams(); i++) {
				if (params.getParam(i).id.equals("raster"))
					raster = (IRasterDataSource)(params.getParam(i).defaultValue);
				if (params.getParam(i).id.equals("numbands"))
					numbands = ((Integer)(params.getParam(i).defaultValue)).intValue();
				if (params.getParam(i).id.equals("histogramReference"))
					histogramReference = (Histogram)(params.getParam(i).defaultValue);
				if (params.getParam(i).id.equals("fileNameOutput"))
					fileNameOutput= new String((String) params.getParam(i).defaultValue);
			}
			addHistogramMatchingFilter(raster,numbands,histogramReference,fileNameOutput);
		}
	}

	/**
	 * Añade un filtro HistogramMatching a la pila de filtros.
	 * @param histogramReference Histograma de referencia al que ajustar el raster
	 * @param fileNameOutput fichero de salida
	 * @throws FilterTypeException
	 */
	public void addHistogramMatchingFilter(IRasterDataSource raster,int numbands,Histogram histogramReference,String fileNameOutput) throws FilterTypeException {
		RasterFilter filter = new HistogramMatchingByteFilter();
		if (filter != null){
			filter.addParam("raster", raster);
			filter.addParam("histogramReference", histogramReference);
			filter.addParam("numbands",new Integer(numbands));
			filter.addParam("fileNameOutput",fileNameOutput);
			filterList.add(filter);
		}
	}


	public int createFilterListFromStrings(ArrayList filters, String fil, int filteri) {
		return filteri;
	}


	/**
	 * Obtiene un Array de Strings a partir de una pila de filtros. Cada elemento
	 * del array tendrá la forma de elemento=valor.
	 */
	public ArrayList getStringsFromFilterList(ArrayList filterList, RasterFilter rf) {

		if (rf instanceof HistogramMatchingFilter) {
			HistogramMatchingFilter histogramMatchingFilter = (HistogramMatchingFilter) rf;
			filterList.add("filter.histogramMatch.active=true");
			filterList.add("filter.histogramMatch.histogramReference="+histogramMatchingFilter.histogramReference);
			filterList.add("filter.histogramMatch.numbands="+histogramMatchingFilter.numbands);
			filterList.add("filter.histogramMatch.filenameOutput="+histogramMatchingFilter.fileNameOutput);
			filterList.add("filter.histogramMatch.filterName=" + histogramMatchingFilter.getName());
		}

		return filterList;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.IRasterFilterListManager#getRasterFilterList()
	 */
	public ArrayList getRasterFilterList() {
		ArrayList filters = new ArrayList();
		filters.add(HistogramMatchingFilter.class);
		return filters;
	}

	public static RasterFilter createHistogramMatchFilter() {
		RasterFilter filter = new HistogramMatchingFilter();
		return filter;
	}
}
