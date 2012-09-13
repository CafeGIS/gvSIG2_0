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
package org.gvsig.rastertools.filter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.Hashtable;

import javax.swing.ListModel;

import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.gui.beans.propertiespanel.PropertiesComponent;
import org.gvsig.gui.beans.propertiespanel.PropertiesComponentListener;
import org.gvsig.gui.beans.propertiespanel.PropertyStruct;
import org.gvsig.gui.beans.treelist.event.TreeListChangeEvent;
import org.gvsig.gui.beans.treelist.event.TreeListEvent;
import org.gvsig.gui.beans.treelist.listeners.TreeListChangeListener;
import org.gvsig.gui.beans.treelist.listeners.TreeListComponentListener;
import org.gvsig.raster.IProcessActions;
import org.gvsig.raster.beans.previewbase.ParamStruct;
import org.gvsig.raster.dataset.IRasterDataSource;
import org.gvsig.raster.dataset.Params;
import org.gvsig.raster.dataset.Params.Param;
import org.gvsig.raster.grid.filter.FilterTypeException;
import org.gvsig.raster.grid.filter.FilterUIListener;
import org.gvsig.raster.grid.filter.IRasterFilterListManager;
import org.gvsig.raster.grid.filter.RasterFilter;
import org.gvsig.raster.grid.filter.RasterFilterList;
import org.gvsig.raster.grid.filter.RasterFilterListManager;
import org.gvsig.raster.grid.filter.RegistrableFilterListener;
import org.gvsig.raster.hierarchy.IRasterRendering;
import org.gvsig.raster.util.RasterNotLoadException;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.raster.util.process.FilterProcess;
import org.gvsig.rastertools.filter.ui.FilterPanel;

import com.iver.andami.PluginServices;
/**
 * <code>FilterListener</code> es la clase donde se procesará gran parte del
 * código que controla el panel para el manejo de un layer en la aplicación de
 * filtros.
 *
 * @version 24/05/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class FilterListener implements ActionListener, TreeListComponentListener, TreeListChangeListener, PropertiesComponentListener, FilterUIListener, IProcessActions {
	private FilterPanel filterPanel  = null;
	private int          actualParam  = -1;
	private ArrayList    paramsList   = new ArrayList();

	/**
	 * Construye un FilterListener especificando el FilterPanel asociado
	 * @param filterDialog
	 */
	public FilterListener(FilterPanel filterPanel) {
		this.filterPanel = filterPanel;
	}
	
	/**
	 * Asignamos los valores del PropertiesComponent al Params seleccionado
	 */
	public void RefreshDataProperties() {
		if (actualParam == -1)
			return;

		ArrayList listValues = getFilterPanel().getPropertiesComponent().getValues();

		Params params = ((ParamStruct) paramsList.get(actualParam)).getFilterParam();
		for (int j = 0; j < listValues.size(); j++) {
			PropertyStruct ps = (PropertyStruct) listValues.get(j);
			params.changeParamValue(ps.getKey(), ps.getNewValue());
		}
	}

	/**
	 * Obtener la posición del Param seleccionado en el ArrayList
	 * @param filterName
	 * @return
	 */
	private int getParamSelected(String filterName) {
		for (int i = 0; i < paramsList.size(); i++) {
			if (((ParamStruct) paramsList.get(i)).getFilterName().equals(filterName))
				return i;
		}
		return -1;
	}

	/**
	 * Cambiar el panel de propiedades central por el nuevo panel, segun el filtro
	 * seleccionado que se pasa por parámetro.
	 * @param filter
	 */
	public void changePanel(String filter) {
		int posParam = getParamSelected(filter);

		RefreshDataProperties();
		actualParam = posParam;

		PropertiesComponent propertiesComponent = new PropertiesComponent();

		if (posParam != -1) {
			Params params = ((ParamStruct) paramsList.get(actualParam)).getFilterParam();
			if (params != null) {
				Param paramPanel = params.getParamById("Panel");
				if(paramPanel != null && paramPanel.defaultValue instanceof RegistrableFilterListener)
					((RegistrableFilterListener)paramPanel.defaultValue).addFilterUIListener(this);
				RasterToolsUtil.loadPropertiesFromWriterParams(propertiesComponent, params, new String[]{"FilterName"});
			}
		}
		getFilterPanel().setNewPropertiesComponent(propertiesComponent, filter);
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

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.propertiespanel.PropertiesComponentListener#actionChangeProperties(java.util.EventObject)
	 */
	public void actionChangeProperties(EventObject e) {
		RefreshDataProperties();
		getFilterPanel().refreshPreview();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.treelist.listeners.TreeListChangeListener#actionChangeSelection(org.gvsig.gui.beans.treelist.event.TreeListChangeEvent)
	 */
	public void actionChangeSelection(TreeListChangeEvent e) {
		changePanel(e.getItem());
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		getFilterPanel().refreshPreview();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.treelist.listeners.TreeListComponentListener#elementAdded(org.gvsig.gui.beans.treelist.event.TreeListEvent)
	 */
	public void elementAdded(TreeListEvent e) {
		getFilterPanel().refreshPreview();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.treelist.listeners.TreeListComponentListener#elementMoved(org.gvsig.gui.beans.treelist.event.TreeListEvent)
	 */
	public void elementMoved(TreeListEvent e) {
		getFilterPanel().refreshPreview();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.treelist.listeners.TreeListComponentListener#elementRemoved(org.gvsig.gui.beans.treelist.event.TreeListEvent)
	 */
	public void elementRemoved(TreeListEvent e) {
		getFilterPanel().refreshPreview();
	}

	/**
	 * @return the paramsList
	 */
	public ArrayList getParamsList() {
		return paramsList;
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

		// Conservamos filtros no visibles ya existentes
		ArrayList filtersInit =  getFilterPanel().getLayerVisualStatus().getLast().getFilterStatus();
		for (int i = 0; i < filtersInit.size(); i++) {
			// Si es visible no hacemos nada
			if (((RasterFilter) filtersInit.get(i)).isVisible())
				continue;

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
				params.setParam("alphaBand", new Integer(rendering.getAlphaBandNumber()), 0, null);
				
				ParamStruct newParam = new ParamStruct();
				newParam.setFilterClass(obj.getClass());
				newParam.setFilterName(((RasterFilter) filtersInit.get(i)).getName());
				newParam.setFilterParam(params);
				listFilterUsed.add(newParam);
			} catch (CloneNotSupportedException e) {
			}
		}

		// Metemos los filtros seleccionados en el panel
		ListModel list = getFilterPanel().getMainPanel().getTreeListContainer().getListModel();
		for (int i = 0; i < list.getSize(); i++) {
			Hashtable hastTable = getFilterPanel().getMainPanel().getTreeListContainer().getMap();
			for (int j = 0; j < paramsList.size(); j++) {
				boolean active = true;
				Param param = ((ParamStruct) paramsList.get(j)).getFilterParam().getParamById("enabled");
				if ((param != null) &&
					param.defaultValue instanceof Boolean &&
					((((Boolean)param.defaultValue).booleanValue()) == false))
					active = false;
				if (active) {
					if (((ParamStruct) paramsList.get(j)).getFilterName().equals(hastTable.get(list.getElementAt(i)))) {
						try {
							Params params = (Params) ((ParamStruct) paramsList.get(j)).getFilterParam().clone();
							// Añado el parametro RenderBands a los parametros del filtro
							String rgb = rendering.getRenderBands()[0] + " " + rendering.getRenderBands()[1] + " " + rendering.getRenderBands()[2];
							params.setParam("RenderBands", rgb, 0, null);
							params.setParam("alphaBand", new Integer(rendering.getAlphaBandNumber()), 0, null);

							ParamStruct newParam = new ParamStruct();
							newParam.setFilterClass(((ParamStruct) paramsList.get(j)).getFilterClass());
							newParam.setFilterName(((ParamStruct) paramsList.get(j)).getFilterName());
							newParam.setFilterParam(params);
							listFilterUsed.add(newParam);
						} catch (CloneNotSupportedException e) {
						}
					}
				}
			}
		}
		return listFilterUsed;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.imagenavigator.IClientImageNavigator#drawImage(java.awt.Graphics2D, double, double, double, double, double, int, int)
	 */
	public void drawImage(IRasterRendering rendering) {
		rendering.getRenderFilterList().clear();

		if (getFilterPanel().getCBShowFilters().isSelected()) {
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
	 * Que acciones se ejecutaran al haber presionado el botón aceptar o aplicar
	 */
	public void accept() {
		IRasterDataSource raster = ((FLyrRasterSE) getFilterPanel().getLayer()).getDataSource();
		if (raster == null)
			return;

		String path = null;
		if (!getFilterPanel().getNewLayerPanel().isOnlyViewSelected()) {
			path = getFilterPanel().getNewLayerPanel().getFileSelected();
			if (path == null)
				return;
		}

		//Rendering rendering = ((FLyrRasterSE) getFilterPanel().getLayer()).getRender();
		IRasterRendering rendering = (IRasterRendering) getFilterPanel().getLayer();

		// Array para guardar los filtros que se van a usar en forma de ParamStruct
		ArrayList listFilterUsed = applyFilters(rendering);

		if (filterPanel.getNewLayerPanel().isOnlyViewSelected()) {
			try {
				FilterProcess.addSelectedFilters(rendering.getRenderFilterList(), listFilterUsed);
				((FLyrRasterSE) getFilterPanel().getLayer()).setRenderFilterList(rendering.getRenderFilterList());
				getFilterPanel().getLayer().getMapContext().invalidate();
			} catch (FilterTypeException e) {
				RasterToolsUtil.messageBoxError(PluginServices.getText(this, "error_adding_filters"), this, e);
			}
		} else {
			FilterProcess filterProcess = new FilterProcess();
			filterProcess.setActions(this);
			filterProcess.addParam("rendering", rendering);
			filterProcess.addParam("filename", path);
			filterProcess.addParam("rasterdatasource", raster);
			filterProcess.addParam("listfilterused", listFilterUsed);
			filterProcess.start();
		}
	}

	/**
	 * Devuelve el FilterPanel asociado al FilterListener
	 * @return
	 */
	public FilterPanel getFilterPanel() {
		return filterPanel;
	}

	/**
	 * Acciones que se realizan al finalizar de crear los recortes de imagen.
	 * Este método es llamado por el thread TailRasterProcess al finalizar.
	 */
	public void loadLayerInToc(String fileName) {
		if (!getFilterPanel().getNewLayerPanel().isNewLayerSelected())
			return;
		if(!new File(fileName).exists())
			return;
		try {
			RasterToolsUtil.loadLayer(getFilterPanel().getViewName(), fileName, null);
		} catch (RasterNotLoadException e) {
			RasterToolsUtil.messageBoxError("error_cargar_capa", this, e);
		}

		if(filterPanel != null)
			filterPanel.updateNewLayerText();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.FilterUIListener#actionValuesCompleted(java.util.EventObject)
	 */
	public void actionValuesCompleted(EventObject e) {
		actionChangeProperties(e);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.rastertools.IProcessActions#end(java.lang.Object)
	 */
	public void end(Object param) {
		loadLayerInToc((String) param);
	}

	public void interrupted() {}
}