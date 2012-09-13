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
package org.gvsig.rastertools.enhanced.ui;

import java.util.ArrayList;

import org.gvsig.gui.beans.imagenavigator.ImageUnavailableException;
import org.gvsig.raster.beans.previewbase.IPreviewRenderProcess;
import org.gvsig.raster.beans.previewbase.ParamStruct;
import org.gvsig.raster.dataset.Params;
import org.gvsig.raster.grid.filter.FilterTypeException;
import org.gvsig.raster.grid.filter.IRasterFilterListManager;
import org.gvsig.raster.grid.filter.RasterFilter;
import org.gvsig.raster.grid.filter.RasterFilterList;
import org.gvsig.raster.grid.filter.RasterFilterListManager;
import org.gvsig.raster.hierarchy.IRasterRendering;
import org.gvsig.raster.util.RasterToolsUtil;

import com.iver.andami.PluginServices;
/**
 * Procesado de la imagen para la previsualización. Para poder usar esta clase después de instanciarla
 * habrá que asignarle un valor a showFiltersSelected aunque por defecto está a true por lo que se
 * visualizaran los cambios sin problemas, asigna una lista de filtros inicial (que ya tenga el raster
 * aplicado) con setFilterStatus y asignar los filtros que queramos aplicar con addNewParam (una llamada
 * por cada filtro).
 * 
 * 19/02/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class PreviewFiltering implements IPreviewRenderProcess {

	private boolean          showFiltersSelected     = true;
	private ArrayList        paramsList              = new ArrayList();
	private ArrayList        filtersInit             = new ArrayList();
	private boolean          showPreview             = true;	
	
	/**
	 * Flag de selección de activación y desactivación
	 * @param show
	 */
	public void showFiltersSelected(boolean show) {
		this.showFiltersSelected = show;
	}
	
	/**
	 * Obtiene la lista de parámetros
	 * @return the paramsList
	 */
	public ArrayList getParamsList() {
		return paramsList;
	}
	
	/**
	 * Asigna la lista de parámetros
	 * @param params
	 */
	public void setParamsList(ArrayList params) {
		this.paramsList = params;
	}
	
	/**
	 * Asigna el arrayList de filtros inicial. Esto hace que aplique los filtros que ya
	 * existen en el raster. Estos pueden ser obtenidos del render de la capa de la forma
	 * lyr.getRender().getFilterList().getStatusCloned().  
	 * @param params Lista de filtros aplicados.
	 */
	public void setFilterStatus(ArrayList filtersInit) {
		this.filtersInit = filtersInit;
	}
	
	/**
	 * Devuelve el arrayList de filtros inicial
	 * @return
	 */
	public ArrayList getFilterStatus() {
		return filtersInit;
	}
	
	/**
	 * Añadir un nuevo Params a la lista de Params que podemos manejar. Un Params
	 * equivale a un filtro cargado. El hecho de trabajar con Params y no con
	 * filtros, simplifica totalmente el panel. Sin tener que depender de los
	 * filtros nada más que para el momento de dibujado o guardado.
	 * @param name
	 * @param params
	 * @param classFilter
	 */
	public void addNewParam(String name, Params params, Class classFilter) {
		ParamStruct param = new ParamStruct();
		param.setFilterName(name);
		param.setFilterParam(params);
		param.setFilterClass(classFilter);
		paramsList.add(param);
	}

	/**
	 * Procesa la imagen con la lista de filtros si el flag showFilterSelected está a true.
	 * Esta función llama a addFilter por cada filtro añadido pero es applyFilters la encargada
	 * de construir la lista. 
	 */
	public void process(IRasterRendering rendering) throws FilterTypeException, ImageUnavailableException {
		if(!showPreview)
			throw new ImageUnavailableException(RasterToolsUtil.getText(this, "panel_preview_not_available"));
		
		rendering.getRenderFilterList().clear();

		if (showFiltersSelected) {
			RasterFilterList filterList = rendering.getRenderFilterList();
			RasterFilterListManager stackManager = new RasterFilterListManager(filterList);

			ArrayList listFilterUsed = applyFilters(rendering);
			ArrayList exc = new ArrayList();
			for (int i = 0; i < listFilterUsed.size(); i++) {
				IRasterFilterListManager filterManager = stackManager.getManagerByFilterClass(((ParamStruct) listFilterUsed.get(i)).getFilterClass());
				try {
					filterManager.addFilter(((ParamStruct) listFilterUsed.get(i)).getFilterClass(), ((ParamStruct) listFilterUsed.get(i)).getFilterParam());
				} catch (FilterTypeException e) {
					exc.add(e);
				}
			}
			if(exc.size() != 0) {
				RasterToolsUtil.messageBoxError(PluginServices.getText(this, "error_adding_filters"), this, exc);
				exc.clear();
			}
		}
	}
	
	/**
	 * Aqui se seleccionan que filtros se van a aplicar y se devuelven en forma
	 * de ArrayList tanto para el dibujado como cuando aceptan o aplican el panel.
	 * @param rendering
	 * @return
	 */
	public ArrayList applyFilters(IRasterRendering rendering) {
		ArrayList listFilterUsed = new ArrayList();

		RasterFilterList filterList = new RasterFilterList();
		filterList.setEnv(rendering.getRenderFilterList().getEnv());
		RasterFilterListManager stackManager = new RasterFilterListManager(filterList);

		if(filtersInit == null)
			return listFilterUsed;
		
		// Conservamos filtros ya existentes
		for (int i = 0; i < filtersInit.size(); i++) {
			
			RasterFilter obj = null;
			for (int j = 0; j < stackManager.getRasterFilterList().size(); j++) {
				Class classFilter = (Class) stackManager.getRasterFilterList().get(j);
				try {
					obj = (RasterFilter) classFilter.newInstance();
					if (obj.getName().equals(((RasterFilter) filtersInit.get(i)).getName()))
						break;
				} catch (InstantiationException e) {
					RasterToolsUtil.messageBoxError("error_creando_filtro", this, e);
				} catch (IllegalAccessException e) {
					RasterToolsUtil.messageBoxError("error_creando_filtro", this, e);
				}
			}

			// Si no encontramos el filtro apropiado, nos olvidamos de el
			if (obj == null)
				continue;

			// Si no es visible tenemos que conservar el filtro
			try {
				Params params = (Params) ((RasterFilter) filtersInit.get(i)).getUIParams(((RasterFilter) filtersInit.get(i)).getName()).clone();
				// Añado el parametro RenderBands a los parametros del filtro
				String rgb = rendering.getRenderBands()[0] + " " + rendering.getRenderBands()[1] + " " + rendering.getRenderBands()[2];
				params.setParam("RenderBands", rgb, 0, null);
			
				ParamStruct newParam = new ParamStruct();
				newParam.setFilterClass(obj.getClass());
				newParam.setFilterName(((RasterFilter) filtersInit.get(i)).getName());
				newParam.setFilterParam(params);
				listFilterUsed.add(newParam);
			} catch (CloneNotSupportedException e) {
			}
		}
		
		// Metemos los filtros seleccionados en listFilterUsed
		for (int i = 0; i < paramsList.size(); i++) {
			// En caso de existir el filtro, lo reemplazamos
			boolean finded = false;
			for (int j = 0; j < listFilterUsed.size(); j++) {
				if (((ParamStruct) listFilterUsed.get(j)).getFilterName().equals(((ParamStruct) paramsList.get(i)).getFilterName())) {
					listFilterUsed.set(j, paramsList.get(i));
					finded = true;
					break;
				}
			}
			if (!finded)
				listFilterUsed.add(paramsList.get(i));
		}
		
		return listFilterUsed;
	}
	
	/**
	 * Obtiene el flag que informa de si se está mostrando la previsualización o no.
	 * En caso de no mostrarse se lanza una excepción ImageUnavailableExcepcion con el 
	 * mensaje "La previsualización no está disponible para este panel"
	 * @return
	 */
	public boolean isShowPreview() {
		return showPreview;
	}
	
	/**
	 * Asigna el flag para mostrar u ocultar la preview. En caso de no mostrarse se lanza una 
	 * excepción ImageUnavailableExcepcion con el mensaje "La previsualización no está disponible para
	 * este panel"
	 * @param showPreview
	 */
	public void setShowPreview(boolean showPreview) {
		this.showPreview = showPreview;
	}
}