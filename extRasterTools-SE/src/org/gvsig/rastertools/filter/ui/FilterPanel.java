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
package org.gvsig.rastertools.filter.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.gui.beans.imagenavigator.ImageUnavailableException;
import org.gvsig.gui.beans.propertiespanel.PropertiesComponent;
import org.gvsig.gui.beans.treelist.TreeListContainer;
import org.gvsig.raster.beans.createlayer.CreateLayerPanel;
import org.gvsig.raster.beans.previewbase.IPreviewRenderProcess;
import org.gvsig.raster.beans.previewbase.ParamStruct;
import org.gvsig.raster.beans.previewbase.PreviewBasePanel;
import org.gvsig.raster.dataset.Params;
import org.gvsig.raster.dataset.Params.Param;
import org.gvsig.raster.grid.filter.FilterTypeException;
import org.gvsig.raster.grid.filter.RasterFilter;
import org.gvsig.raster.grid.filter.RasterFilterList;
import org.gvsig.raster.grid.filter.RasterFilterListManager;
import org.gvsig.raster.hierarchy.IRasterRendering;
import org.gvsig.raster.util.LayerVisualStatusList;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.rastertools.filter.FilterListener;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;
/**
 * Dialogo para los filtros de raster.
 *
 * @version 30/05/2007
 * @author Nacho Brodin <brodin_ign@gva.es>
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class FilterPanel extends JPanel implements IPreviewRenderProcess {
	private static final long serialVersionUID = 7152780112689637266L;

	private PreviewBasePanel       previewBasePanel    = null;
	private FLyrRasterSE           layer               = null;
	private FilterListener         filterListener      = null;
	private PropertiesComponent    propertiesComponent = null;

	private LayerVisualStatusList  status              = new LayerVisualStatusList();
	private JCheckBox              jCBShowFilters      = null;
	private String                 viewName            = null;
	private JPanel                 jPanelOptions       = null;
	private FilterMainPanel        filterMain          = null;
	private FilterDialog           filterDialog        = null;
	private CreateLayerPanel       newLayerPanel       = null;
	private boolean                showPreview         = true;	

	/**
	 * Constructor
	 * @param width Ancho del panel
	 * @param height Alto del panel
	 */
	public FilterPanel(FLyrRasterSE layer, FilterDialog filterDialog) {
		this.filterDialog = filterDialog;
		setLayer(layer);
		initialize();
	}
	
	private void initialize() {
		translate();
		setLayout(new BorderLayout());
		add(getPreviewBasePanel(), BorderLayout.CENTER);
	}
	
	/**
	 * Seccion donde irán todas las traducciones invariables del componente
	 */
	private void translate() {
		getCBShowFilters().setText(PluginServices.getText(this, "aplicar_vista_previa"));
	}
	
	/**
	 * Obtiene el panel con el histograma
	 * @return HistogramPanel
	 */
	private PreviewBasePanel getPreviewBasePanel() {
		if (previewBasePanel == null) {
			ArrayList list = new ArrayList();
			list.add(getMainPanel());
			previewBasePanel = new PreviewBasePanel(list, null, panelOptions(), this, (FLyrRasterSE) layer);
			previewBasePanel.setPreviewSize(new Dimension(230, 215));
			previewBasePanel.addButtonPressedListener(filterDialog);
		}
		return previewBasePanel;
	}

	/**
	 * Devuelve el componente <code>FilterListener</code>, que contendrá el
	 * proceso en si del panel
	 */
	private FilterListener getFilterListener() {
		if (filterListener == null) {
			filterListener = new FilterListener(this);
		}

		return filterListener;
	}

	/**
	 * Devuelve el panel de Opciones, en caso de no existir, lo crea.
	 * @return
	 */
	private JPanel panelOptions() {
		if (jPanelOptions == null) {
			jPanelOptions = new JPanel();

			GridBagConstraints gridBagConstraints;

			jPanelOptions.setLayout(new GridBagLayout());

			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 1;
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.insets = new Insets(2, 2, 2, 2);
			jPanelOptions.add(getCBShowFilters(), gridBagConstraints);

			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 4;
			gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints.insets = new Insets(3, 3, 3, 3);
			jPanelOptions.add(getNewLayerPanel().getJPanel(), gridBagConstraints);

			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 5;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.weighty = 1.0;
			jPanelOptions.add(new JPanel(), gridBagConstraints);
		}

		return jPanelOptions;
	}
	
	public CreateLayerPanel getNewLayerPanel() {
		if (newLayerPanel == null) {
			newLayerPanel = new CreateLayerPanel(layer);
		}
		return newLayerPanel;
	}

	/**
	 * Construye el arbol para el menú de filtros disponibles
	 */
	private void menuBuild() {
		Hashtable keys = new Hashtable();
		RasterFilterList filterList = ((FLyrRasterSE) layer).getRenderFilterList();
		// Creamos un stackManager para recorrer los filtros registrados en gvSIG
		RasterFilterListManager stackManager = new RasterFilterListManager(filterList);

		int count = 0;
		RasterFilter obj = null;

		for (int i = 0; i < stackManager.getRasterFilterList().size(); i++) {
			Class classFilter = (Class) stackManager.getRasterFilterList().get(i);

			try {
				obj = (RasterFilter) classFilter.newInstance();

				initFilter(obj);
				
				if (!obj.isVisible())
					continue;
				
				// Crear grupos de los filtros
				if (!keys.containsKey(PluginServices.getText(this, new String(obj.getGroup())))) {
					keys.put(PluginServices.getText(this, new String(obj.getGroup())), obj);
					getMainPanel().getTreeListContainer().addClass(PluginServices.getText(this, new String(obj.getGroup())), count);
					count++;
						
				}
				// Crear cada entrada de los filtro
				String[] names = obj.getNames();
				for (int j = 0; j < names.length; j++) {
					obj.setName(names[j]);
					getFilterListener().addNewParam(names[j], initFilterParam(obj.getUIParams(names[j])), classFilter);
					getMainPanel().getTreeListContainer().addEntry(PluginServices.getText(this, names[j]), PluginServices.getText(this, obj.getGroup()), names[j]);
				}
			} catch (InstantiationException e) {
				RasterToolsUtil.debug("No se ha podido meter el filtro en la lista", this, e);
			} catch (IllegalAccessException e) {
				RasterToolsUtil.debug("No se ha podido meter el filtro en la lista", this, e);
			}
		}

		getMainPanel().getTreeListContainer().getTree().expandRow(0);
	}
	
	/**
	 * Pasamos los parametros basicos a un filtro
	 * @param filter
	 */
	private void initFilter(RasterFilter filter) {
		if (layer != null) {
			filter.getEnv().put("MultiRasterDataset", ((FLyrRasterSE) layer).getDataSource());
			filter.getEnv().put("initRaster", layer);
		}
	}
	
	/**
	 * Sirve para añadir mas parametros genericos por defecto al panel, de momento
	 * solo he puesto para activar o desactivar un filtro.
	 * @param params
	 * @return
	 */
	private Params initFilterParam(Params params) {
		params.setParam("enabled", new Boolean(true), Params.CHECK, null);
		Param param = params.getParamById("enabled");
		params.getParams().remove(params.getParams().size() - 1);
		params.getParams().add(0, param);
		return params;
	}
	
	/**
	 * Comprueba que el nombre del filtro esta cargado en las extensiones
	 * @param filter
	 * @return
	 */
	private boolean hasThisFilter(String filter) {
		RasterFilterList filterList = new RasterFilterList();
		RasterFilterListManager stackManager = new RasterFilterListManager(filterList);
	
		boolean error;
		RasterFilter obj = null;
	
		for (int i = 0; i < stackManager.getRasterFilterList().size(); i++) {
			Class classFilter = (Class) stackManager.getRasterFilterList().get(i);
			error = true;
	
			try {
				obj = (RasterFilter) classFilter.newInstance();
				error = false;
			} catch (InstantiationException e) {
			} catch (IllegalAccessException e) {
			}
	
			if (!error) {
				String[] names = obj.getNames();
				for (int j = 0; j < names.length; j++)
					if (filter.equals(names[j]))
						return true;
			}
		}
		return false;
	}

	/**
	 * Definir el FLayer del panel, haciendo todas las cargas necesarias despues
	 * de especificarlo.
	 * @param layer
	 */
	private void setLayer(FLyrRasterSE layer) {
		if (layer == null)
			return;

		this.layer = layer;
		getPreviewBasePanel().setLayer(layer);
	
		// Construimos el arbol de filtros
		menuBuild();
	
		BaseView view = (BaseView) PluginServices.getMDIManager().getActiveWindow();
		viewName = PluginServices.getMDIManager().getWindowInfo(view).getTitle();
	
		//Comprobamos que el raster de salida no exceda los 4GB que es el máximo 
		//admisible por un TIFF. Si es así se hace un setCompress para salvar en JPEG2000
		long sizeB = (long)((((FLyrRasterSE) layer).getPxWidth() * ((FLyrRasterSE) layer).getPxWidth() * ((FLyrRasterSE) layer).getBandCount())); 
		int sizeMB = (int)(sizeB / 1048576);
		if(sizeMB >= 4096) {
			RasterToolsUtil.messageBoxInfo("file_too_big", this);
			getNewLayerPanel().setCompress(true);
		}
	
		RasterFilterList rfl = ((FLyrRasterSE) layer).getRenderFilterList();
		status.getVisualStatus(((FLyrRasterSE) layer));
	
		//Carga el listado de filtros que ya estaban aplicados en la capa al abrir
		 
		int i = 0;
		boolean filters = false;
		while (rfl.get(i) != null) {
			DefaultListModel list = (DefaultListModel) getMainPanel().getTreeListContainer().getListModel();
			if (rfl.get(i).isVisible() && hasThisFilter(rfl.get(i).getName())) {
				list.addElement(PluginServices.getText(this, rfl.get(i).getName()));
				for (int j = 0; j < getFilterListener().getParamsList().size(); j++) {
					if (((ParamStruct) getFilterListener().getParamsList().get(j)).getFilterName().equals(rfl.get(i).getName())) {
						filters = true;
						initFilter(rfl.get(i));
						((ParamStruct) getFilterListener().getParamsList().get(j)).setFilterParam(initFilterParam(rfl.get(i).getUIParams(rfl.get(i).getName())));
					}
				}
			}
			i++;
		}
	
		// Si existen filtros ya en la capa, pondremos los RadioButton para que
		// los cambios se hagan sobre la visualización
		if (filters)
			getNewLayerPanel().setOnlyView(true);
	
		// Selecciona el primer item del arbol
		TreeListContainer lc = getMainPanel().getTreeListContainer();
		lc.getTree().expandRow(1);
		lc.getTree().setSelectionInterval(2, 2);
	}

	public FilterMainPanel getMainPanel() {
		if (filterMain == null) {
			filterMain = new FilterMainPanel(getFilterListener());
			JPanel panel = getNewLayerPanel().getFileNamePanel();
			panel.setBorder(BorderFactory.createTitledBorder(null, PluginServices.getText(this, "nombre_capa"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
			filterMain.getCentralPanel().add(panel, BorderLayout.SOUTH);
		}
		return filterMain;
	}

	/**
	 * Volvemos todo a la normalidad cuando se cancela
	 */
	public void cancel() {
		if (layer != null)
			layer.getMapContext().invalidate();
	}

	/**
	 * Cuando se aceptan los cambios en el panel ejecutaremos el aceptar del
	 * listener
	 */
	public void accept() {
		filterListener.accept();
	}
	
	/**
	 * Obtener el PropertiesComponent del panel de filtros, en caso de no existir,
	 * lo crea.
	 * @return
	 */
	public PropertiesComponent getPropertiesComponent() {
		if (propertiesComponent == null) {
			propertiesComponent = new PropertiesComponent();
			getMainPanel().getCentralPanel().add(propertiesComponent, BorderLayout.CENTER);

			propertiesComponent.addStateChangedListener(getFilterListener());
		}
		return propertiesComponent;
	}

	/**
	 * Asignar un nuevo componente de PropertiesComponent, eliminando el anterior
	 * que hubiera.
	 * @param component
	 * @param name
	 */
	public void setNewPropertiesComponent(PropertiesComponent component, String name) {
		getPropertiesComponent().setVisible(false);
		this.remove(propertiesComponent);
		propertiesComponent = null;
		propertiesComponent = component;
		getMainPanel().getCentralPanel().add(propertiesComponent, BorderLayout.CENTER);
		propertiesComponent.addStateChangedListener(getFilterListener());
		setTitle(name);
		this.validate();
	}
	
	/**
	 * Asigna el título al panel
	 * @param title Título del panel
	 */
	public void setTitle(String title) {
		getPropertiesComponent().setBorder(BorderFactory.createTitledBorder(null, PluginServices.getText(this, "filtro_de") + " " + PluginServices.getText(this, title), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
	}
	
	/**
	 * Devuelve el estado de los filtros inicial
	 * @return VisualStatusLayerStack
	 */
	public LayerVisualStatusList getLayerVisualStatus() {
		return status;
	}
	

	/**
	 * Actualizamos la vista previa
	 */
	public void refreshPreview() {
		getPreviewBasePanel().refreshPreview();
	}
	
	/**
	 * Devuelve el FLayer asignado al panel
	 * @return
	 */
	public FLayer getLayer() {
		return layer;
	}
	

	/**
	 * @return the jLabelShowFilters
	 */
	public JCheckBox getCBShowFilters() {
		if (jCBShowFilters == null) {
			jCBShowFilters = new JCheckBox();
			jCBShowFilters.addActionListener(getFilterListener());
			jCBShowFilters.setSelected(true);
		}
		return jCBShowFilters;
	}

	/**
	 * Obtiene el nombre de la vista
	 * @return
	 */
	public String getViewName() {
		return viewName;
	}

	/**
	 * Especificar el nombre de la nueva capa para el recuadro de texto asignándo
	 * en cada llamada un nombre consecutivo.
	 */
	public void updateNewLayerText() {
		getNewLayerPanel().updateNewLayerText();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.beans.previewbase.IPreviewRenderProcess#process(org.gvsig.raster.hierarchy.IRasterRendering)
	 */
	public void process(IRasterRendering rendering) throws FilterTypeException, ImageUnavailableException {
		if(!showPreview)
			throw new ImageUnavailableException(RasterToolsUtil.getText(this, "panel_preview_not_available"));
		
		getFilterListener().drawImage(rendering);
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