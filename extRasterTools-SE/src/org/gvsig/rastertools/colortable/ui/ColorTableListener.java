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
package org.gvsig.rastertools.colortable.ui;

import java.util.ArrayList;

import javax.swing.JComponent;

import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.gui.beans.imagenavigator.ImageUnavailableException;
import org.gvsig.raster.beans.previewbase.IPreviewRenderProcess;
import org.gvsig.raster.datastruct.ColorItem;
import org.gvsig.raster.datastruct.ColorTable;
import org.gvsig.raster.grid.GridPalette;
import org.gvsig.raster.grid.filter.FilterTypeException;
import org.gvsig.raster.grid.filter.RasterFilter;
import org.gvsig.raster.grid.filter.RasterFilterList;
import org.gvsig.raster.grid.filter.RasterFilterListManager;
import org.gvsig.raster.grid.filter.bands.ColorTableFilter;
import org.gvsig.raster.grid.filter.bands.ColorTableListManager;
import org.gvsig.raster.grid.filter.enhancement.LinearStretchEnhancementFilter;
import org.gvsig.raster.grid.filter.statistics.TailTrimFilter;
import org.gvsig.raster.hierarchy.IRasterRendering;
import org.gvsig.raster.util.PropertyEvent;
import org.gvsig.raster.util.PropertyListener;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.rastertools.colortable.data.ColorTableData;
import org.gvsig.rastertools.colortable.ui.tabs.ColorTableUIListener;
import org.gvsig.rastertools.colortable.ui.tabs.IColorTableUI;

import com.iver.andami.PluginServices;
/**
 * Listener generico para el panel de tablas de color, en el controlaremos el
 * refresco de la vista previa, los cambios en las tablas de color y la
 * finalizacion del panel.
 * 
 * @version 27/06/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class ColorTableListener implements PropertyListener, ColorTableUIListener, IPreviewRenderProcess {
	private ColorTablePanel colorTablePanel  = null;
	private FLyrRasterSE    previewLayer     = null;
	private ColorTableData  colorTableData   = null;
	private ArrayList       statusList       = new ArrayList();
	private IColorTableUI   lastColorTableUI = null;
	private boolean         showPreview      = true;
	
	/**
	 * Construye un ColorTableListener
	 * @param colorTablePanel
	 */
	public ColorTableListener(ColorTablePanel colorTablePanel, ColorTableData colorTableData) {
		this.colorTablePanel = colorTablePanel;
		this.colorTableData = colorTableData;
		getColorTableData().addValueChangedListener(this);
	}

	/**
	 * Devuelve el panel de ColorTable
	 * @return
	 */
	private ColorTablePanel getColorTablePanel() {
		return colorTablePanel;
	}

	private ColorTableData getColorTableData() {
		return colorTableData;
	}

	/**
	 * Asigna la capa raster de la vista
	 * @param fLayer
	 */
	public void setLayer(FLayer fLayer) {
		if (fLayer instanceof FLyrRasterSE) {
			try {
				previewLayer = (FLyrRasterSE) fLayer.cloneLayer();
			} catch (Exception e) {
				RasterToolsUtil.messageBoxError("preview_not_available", colorTablePanel, e);
			}
		}
	}

	/**
	 * Cierra la capa abierta para previsualización
	 */
	public void closePreviewLayer() {
		if (previewLayer != null) {
			previewLayer.setRemoveRasterFlag(true);
			previewLayer.removeLayerListener(null);
		}
	}

	/**
	 * Aqui se aplica el estado de las tablas de color al rendering pasado por
	 * parametro
	 * @param rendering
	 * @return
	 * @throws FilterTypeException
	 */
	private void applyColorTable(IRasterRendering rendering, boolean isPreview) throws FilterTypeException {
		RasterFilterList filterList = rendering.getRenderFilterList();
		RasterFilterListManager manager = new RasterFilterListManager(filterList);
		ColorTableListManager cManager = (ColorTableListManager) manager.getManagerByClass(ColorTableListManager.class);

		filterList.remove(ColorTableFilter.class);

		((FLyrRasterSE) getColorTablePanel().getLayer()).setLastLegend(null);

		if (getColorTableData().isEnabled()) {
			filterList.remove(LinearStretchEnhancementFilter.class);
			filterList.remove(TailTrimFilter.class);
			GridPalette gridPalette = new GridPalette(getColorTableData().getColorTable());

			// Asignamos la transparencia del render actual al filterList
			filterList.addEnvParam("Transparency", rendering.getRender().getLastTransparency());

			cManager.addColorTableFilter(gridPalette);

			if (!isPreview) {
				gridPalette.compressPalette();
				((FLyrRasterSE) getColorTablePanel().getLayer()).setLastLegend(gridPalette);
			}
		}
		for (int i = 0; i< filterList.lenght(); i++) {
			((RasterFilter) filterList.get(i)).setEnv(filterList.getEnv());
		}

		rendering.setRenderFilterList(filterList);
	}

	/**
	 * Acciones que se ejecutaran al haber presionado el botón aceptar o aplicar
	 */
	public void accept() {
		if (getColorTablePanel().getLayer() instanceof IRasterRendering) {
			try {
				applyColorTable(((IRasterRendering) getColorTablePanel().getLayer()), false);
				getColorTablePanel().getLayer().getMapContext().invalidate();
			} catch (FilterTypeException e) {
				RasterToolsUtil.messageBoxError(PluginServices.getText(this, "error_adding_filters"), this, e);
			}

		}
	}
	
	public class StatusComponent {
		private JComponent object;
		private boolean enabled;

		public boolean isEnabled() {
			return enabled;
		}

		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}

		public JComponent getObject() {
			return object;
		}

		public void setObject(JComponent object) {
			this.object = object;
		}
	}
	
	private void saveComponentsStatus(JComponent component) {
		// Guardar estado
		StatusComponent auxStatus = new StatusComponent();
		auxStatus.setEnabled(component.isEnabled());
		auxStatus.setObject(component);
		statusList.add(auxStatus);

		for (int i = 0; i < component.getComponentCount(); i++)
			if (component.getComponent(i) instanceof JComponent)
				saveComponentsStatus((JComponent) component.getComponent(i));
	}

	private void setEnabledRecursive(JComponent component, boolean enabled, int level) {
		if (enabled == true) {
			boolean auxEnabled = false;
			boolean finded = false;
			// Buscar estado de dicho componente
			for (int i = 0; i < statusList.size(); i++) {
				StatusComponent auxStatus = (StatusComponent) statusList.get(i);
				if (auxStatus.getObject() == component) {
					auxEnabled = auxStatus.isEnabled();
					statusList.remove(i);
					i--;
					finded = true;
					break;
				}
			}

			// Asignar su estado
			if (finded)
				component.setEnabled(auxEnabled);
		} else {
			// Si es la primera llamada, guardamos el estado de todos los componentes
			if (level == 0)
				saveComponentsStatus(component);

			// Desactivar componente
			component.setEnabled(false);
		}

		for (int i = 0; i < component.getComponentCount(); i++)
			if (component.getComponent(i) instanceof JComponent)
				setEnabledRecursive((JComponent) component.getComponent(i), enabled, level + 1);
	}	
	
	/**
	 * Activa/Desactiva los componentes de las pestañas segun la pestaña selecionada
	 * @param enabled
	 */
	private void setEnabledPanel(boolean enabled) {
		colorTablePanel.getGeneralPanel().setEnabledPanel(enabled);
		colorTablePanel.getPreviewBasePanel().getTabbedPane().setEnabled(enabled);
		setEnabledRecursive(getLastColorTableUI().getPanel(), enabled, 0);
		setEnabledRecursive(colorTablePanel.getPreviewBasePanel().getImageNavigator(), enabled, 0);
		setEnabledRecursive(colorTablePanel.getPanelListView(), enabled, 0);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.rastertools.PropertyListener#actionValueChanged(org.gvsig.rastertools.PropertyEvent)
	 */
	public void actionValueChanged(PropertyEvent e) {
		if (e.getName().equals("refreshPreview")) {
			colorTablePanel.getPreviewBasePanel().refreshPreview();
			return;
		}
		if (e.getName().equals("interpolated")) {
			colorTablePanel.getColorTableLibraryPanel().setInterpolated(((Boolean) e.getValue()).booleanValue());
			getColorTableData().getColorTable().setInterpolated(((Boolean) e.getValue()).booleanValue());
			getColorTableData().refreshPreview();
			getLastColorTableUI().setColorTable(getColorTableData().getColorTable());
			return;
		}
		
		if (e.getName().equals("enabled")) {
			setEnabledPanel(((Boolean) e.getValue()).booleanValue());
			return;
		}

		if (e.getName().equals("limits") ||
				e.getName().equals("maxim") ||
				e.getName().equals("minim")) {
			if (getColorTableData().isLimitsEnabled()) {
				double min = getColorTableData().getMinim();
				double max = getColorTableData().getMaxim();
				getLastColorTableUI().getColorTable().createColorTableInRange(min, max, false);
				getLastColorTableUI().setColorTable(getLastColorTableUI().getColorTable());
				getColorTableData().refreshPreview();
			} else {
				colorTablePanel.reloadPanelsFromLibraryPanel();
			}
			return;
		}
	}
	
	/**
	 * Recarga la tabla de elementos
	 * @param isNewSelection
	 */
	public void refreshItems(boolean isNewSelection) {
		if (getColorTableData().getColorTable() == null)
			return;

		if (isNewSelection)
			getColorTableData().getColorTable().removeDuplicatedValues();

		getLastColorTableUI().setColorTable(getColorTableData().getColorTable());
	}

	/**
	 * Pone en separaciones iguales todos los valores de la tabla seleccionada
	 */
	public void equidistar() {
		ColorTable colorTable = getColorTableData().getColorTable();
		if (colorTable == null)
			return;
		colorTable.removeDuplicatedValues();
		ArrayList list = colorTable.getColorItems();
		double min2 = Double.POSITIVE_INFINITY;
		double max2 = Double.NEGATIVE_INFINITY;
		for (int i = 0; i < list.size(); i++) {
			ColorItem colorItem = (ColorItem) list.get(i);
			if (min2 > colorItem.getValue())
				min2 = colorItem.getValue();
			if (max2 < colorItem.getValue())
				max2 = colorItem.getValue();
		}
		for (int i = 0; i < list.size(); i++) {
			ColorItem colorItem = (ColorItem) list.get(i);
			colorItem.setValue(min2 + (((max2 - min2) / (list.size() - 1)) * i));
		}
		refreshItems(true);
		getColorTableData().refreshPreview();
	}
	
	/**
	 * @return the lastColorTableUI
	 */
	private IColorTableUI getLastColorTableUI() {
		return lastColorTableUI;
	}

	/**
	 * @param lastColorTableUI the lastColorTableUI to set
	 */
	public void setLastColorTableUI(IColorTableUI lastColorTableUI) {
		this.lastColorTableUI = lastColorTableUI;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.rastertools.colortable.panels.ColorTableUIListener#actionColorTableUIChanged(org.gvsig.rastertools.colortable.panels.IColorTableUI)
	 */
	public void actionColorTableUIChanged(IColorTableUI colorTableUI) {
		ColorTable colorTable = colorTablePanel.getColorTableLibraryPanel().getColorTableSelected();
		colorTable.createPaletteFromColorItems(colorTableUI.getColorTable().getColorItems(), false);
		colorTablePanel.getColorTableLibraryPanel().setColorTableSelected(colorTable);

		getColorTableData().setColorTable((ColorTable) colorTableUI.getColorTable().clone());
		getColorTableData().refreshPreview();
		//getLastColorTableUI().setColorTable(getColorTableData().getColorTable());
	}

	public void process(IRasterRendering rendering) throws FilterTypeException, ImageUnavailableException {
		if(!showPreview)
			throw new ImageUnavailableException(RasterToolsUtil.getText(this, "panel_preview_not_available"));
		
//		rendering.getRenderFilterList().pushStatus();
		try {
			applyColorTable(rendering, true);
		} catch (FilterTypeException e1) {
			RasterToolsUtil.messageBoxError(PluginServices.getText(this, "error_adding_filters"), this, e1);
		}
//		rendering.getRenderFilterList().popStatus();
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