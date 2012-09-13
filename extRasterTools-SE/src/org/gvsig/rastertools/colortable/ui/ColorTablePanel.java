/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
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

import java.awt.BorderLayout;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanel;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelEvent;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelListener;
import org.gvsig.raster.beans.previewbase.PreviewBasePanel;
import org.gvsig.raster.dataset.RasterDataset;
import org.gvsig.raster.dataset.serializer.RmfSerializerException;
import org.gvsig.raster.datastruct.ColorTable;
import org.gvsig.raster.grid.GridPalette;
import org.gvsig.raster.grid.GridTransparency;
import org.gvsig.raster.grid.filter.RasterFilterList;
import org.gvsig.raster.grid.filter.bands.ColorTableFilter;
import org.gvsig.raster.grid.render.Rendering;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.rastertools.colortable.data.ColorTableData;
import org.gvsig.rastertools.colortable.ui.library.ColorTableLibraryEvent;
import org.gvsig.rastertools.colortable.ui.library.ColorTableLibraryListener;
import org.gvsig.rastertools.colortable.ui.library.ColorTableLibraryPanel;
import org.gvsig.rastertools.colortable.ui.tabs.IColorTableUI;
import org.gvsig.rastertools.colortable.ui.tabs.TabInterpolated;
import org.gvsig.rastertools.colortable.ui.tabs.TabTable;

import com.iver.andami.PluginServices;
/**
 * ColorTablePanel es el panel padre que contendra todos los paneles del interfaz
 * de tablas de color.
 *
 * @version 26/06/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class ColorTablePanel extends JPanel implements ChangeListener, ButtonsPanelListener, ColorTableLibraryListener {
	private static final long serialVersionUID = 6028780107787443656L;

	private ColorTableListener      colorTableListener = null;
	private FLayer                  fLayer             = null;
	private ArrayList               filterStatus       = null;
	private ColorTableData          colorTableData     = null;

	private IColorTableUI           tabTable           = null;
	private IColorTableUI           tabInterpolated    = null;
	
	private ColorTableLibraryPanel  colorTableLibraryPanel = null;

	private JPanel                  jPanelListView     = null;
	private ColorTableGlobalPanel   panelGeneral       = null;
	private ColorTableDialog        colorTableDialog   = null;
	private PreviewBasePanel        previewBasePanel   = null;

	/**
	 * Controla si se ha presionado el boton aceptar para el cerrado de la ventana
	 */
	boolean accepted = false;

	/**
	 * Construir un nuevo ColorTablePanel
	 */
	public ColorTablePanel(FLayer layer, ColorTableDialog colorTableDialog) {
		this.colorTableDialog = colorTableDialog;
		fLayer = layer;
		initialize();
		setLayer(layer);
	}
	
	public PreviewBasePanel getPreviewBasePanel() {
		if (previewBasePanel == null) {
			ArrayList list = new ArrayList();
			list.add(getTabTable());
			list.add(getTabInterpolated());
			getColorTableListener().setLastColorTableUI(getTabTable());
			
			previewBasePanel = new PreviewBasePanel(ButtonsPanel.BUTTONS_NONE, list, (JPanel) getGeneralPanel(), getPanelListView(), getColorTableListener(), (FLyrRasterSE) fLayer);
			previewBasePanel.getTabbedPane().addChangeListener(this);
			previewBasePanel.getButtonsPanel().addButton(RasterToolsUtil.getText(this, "equidistar"), ButtonsPanel.BUTTON_LAST + 3);
			previewBasePanel.getButtonsPanel().addButton(RasterToolsUtil.getText(this, "guardar_predeterminado"), ButtonsPanel.BUTTON_LAST + 2);
			previewBasePanel.getButtonsPanel().addApply();
			previewBasePanel.getButtonsPanel().addAccept();
			previewBasePanel.getButtonsPanel().addCancel();
			previewBasePanel.addButtonPressedListener(this);
		}
		return previewBasePanel;
	}
	

	private void initialize() {
		setLayout(new BorderLayout());
		add(getPreviewBasePanel(), BorderLayout.CENTER);
	}

	private IColorTableUI getTabTable() {
		if (tabTable == null) {
			tabTable = new TabTable();
			tabTable.addColorTableUIChangedListener(getColorTableListener());
		}
		return tabTable;
	}


	private IColorTableUI getTabInterpolated() {
		if (tabInterpolated == null) {
			tabInterpolated = new TabInterpolated();
			tabInterpolated.addColorTableUIChangedListener(getColorTableListener());
		}
		return tabInterpolated;
	}

	public JPanel getPanelListView() {
		if (jPanelListView == null) {
			jPanelListView = new JPanel();
			jPanelListView.setBorder(javax.swing.BorderFactory.createTitledBorder(null, RasterToolsUtil.getText(this, "libreria"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
			jPanelListView.setLayout(new BorderLayout());
			
			jPanelListView.add(getColorTableLibraryPanel());
		}
		return jPanelListView;
	}
	
	public ColorTableLibraryPanel getColorTableLibraryPanel() {
		if (colorTableLibraryPanel == null) {
			colorTableLibraryPanel = new ColorTableLibraryPanel();
			colorTableLibraryPanel.addColorTableLibraryListener(this);
			colorTableLibraryPanel.selectDefault();
		}
		return colorTableLibraryPanel;
	}

	/**
	 * Obtener el componente que hara la carga pesada de los listeners
	 * @return
	 */
	private ColorTableListener getColorTableListener() {
		if (colorTableListener == null) {
			colorTableListener = new ColorTableListener(this, getColorTableData());
		}
		return colorTableListener;
	}

	public ColorTableGlobalPanel getGeneralPanel() {
		if (panelGeneral == null) {
			panelGeneral = new ColorTableGlobalPanel(getColorTableData());
		}
		return panelGeneral;
	}

	/**
	 * Volvemos todo a la normalidad cuando se cancela
	 */
	private boolean restoreFilters() {
		if (getLayer() != null) {
			Rendering rendering = ((FLyrRasterSE) getLayer()).getRender();
			if(rendering.getFilterList() == null)
				return false;
			rendering.getFilterList().setStatus(getFilterStatus());
			((FLyrRasterSE) getLayer()).setRenderFilterList(rendering.getFilterList());
		}
		return true;
	}

	/**
	 * Devuelve el arrayList de filtros inicial
	 * @return
	 */
	private ArrayList getFilterStatus() {
		return filterStatus;
	}

	/**
	 * Carga la tabla de color que hay seleccionada en el componente
	 * ListViewComponent
	 */
	public void reloadPanelsFromLibraryPanel() {
		ColorTable colorTable = getColorTableLibraryPanel().getColorTableSelected();
		
		getColorTableData().setColorTable((ColorTable) colorTable.clone());
		if (colorTableData.isLimitsEnabled()) {
			double min = getColorTableData().getMinim();
			double max = getColorTableData().getMaxim();
			getColorTableData().getColorTable().createColorTableInRange(min, max, false);
		}
		getColorTableListener().refreshItems(true);
		getColorTableData().refreshPreview();
	}

	/**
	 * Salva la tabla de color al fichero rmf.
	 * @param fName
	 * @throws IOException
	 */
	private void saveColorTable() {
		RasterDataset dataset = ((FLyrRasterSE) getLayer()).getDataSource().getDataset(0)[0];
		try {
			if (getColorTableData().isEnabled())
				dataset.saveObjectToRmf(ColorTable.class, getColorTableData().getColorTable());
			else
				dataset.saveObjectToRmf(ColorTable.class, null);
		} catch (RmfSerializerException e) {
			RasterToolsUtil.messageBoxError("error_salvando_rmf", this, e);
		}
	}

	/**
	 * Restaura el estado de los filtros como estaban al abrir la ventana y quita
	 * la leyenda del TOC
	 */
	private void restoreSettings() {
		if (getLayer() == null)
			return;

		// Restauramos la vista con los filtros originales antes de cualquier acción
		if(!restoreFilters())
			return;

		((FLyrRasterSE) getLayer()).setLastLegend(null);

		RasterFilterList rasterFilterList = ((FLyrRasterSE) getLayer()).getRenderFilterList();

		ColorTableFilter colorTableFilter = (ColorTableFilter) rasterFilterList.getByName(ColorTableFilter.names[0]);
		if (colorTableFilter != null) {
			GridPalette gridPalette = new GridPalette((ColorTable) colorTableFilter.getColorTable().clone());
			((FLyrRasterSE) getLayer()).setLastLegend(gridPalette);
		}

		// Quitamos la banda de transparencia por si el filtro de color la ha activado
		try {
			GridTransparency transparency = ((FLyrRasterSE) getLayer()).getRender().getLastTransparency();
			transparency.setTransparencyBand(-1);
			transparency.activeTransparency();
			transparency.setAlphaBand(null);
		} catch (NullPointerException ex) {
			//No quitamos la banda de transparencia
		}
	}

	/**
	 * Devuelve el componente que trata los datos
	 * @return
	 */
	private ColorTableData getColorTableData() {
		if (colorTableData == null) {
			colorTableData = new ColorTableData();
		}
		return colorTableData;
	}

	/**
	 * Especificar el layer para la tabla de color
	 * @param layer
	 */
	private void setLayer(FLayer fLayer) {
		this.fLayer = fLayer;
		
		getGeneralPanel().setLayer(fLayer);
		
		getColorTableListener().setLayer(fLayer);

		RasterFilterList rasterFilterList = ((FLyrRasterSE) getLayer()).getRenderFilterList();

		filterStatus = rasterFilterList.getStatusCloned();

		ColorTableFilter colorTableFilter = (ColorTableFilter) rasterFilterList.getByName(ColorTableFilter.names[0]);
		if (colorTableFilter != null) {
			GridPalette gridPalette = new GridPalette((ColorTable) colorTableFilter.getColorTable().clone());
			if (gridPalette.isCompressible()) {
				if (RasterToolsUtil.messageBoxYesOrNot("comprimir_paleta", this)) {
					gridPalette.compressPalette();
					gridPalette.setInterpolated(true);
				}
			}

			ColorTable colorTable = new ColorTable();
			colorTable.setName(RasterToolsUtil.getText(this, "tabla_actual"));
			colorTable.createPaletteFromColorItems(gridPalette.getColorItems(), false);
			colorTable.setInterpolated(gridPalette.isInterpolated());
			getGeneralPanel().setCheckBoxInterpolated(gridPalette.isInterpolated());

			getColorTableLibraryPanel().addColorTable(0, colorTable);

			getGeneralPanel().setLimitsEnabled(false);
			getGeneralPanel().setCheckBoxEnabled(true);
		} else {
			getColorTableLibraryPanel().selectDefault();

			getGeneralPanel().setLimitsEnabled(true);
			getGeneralPanel().setCheckBoxEnabled(false);
		}
		reloadPanelsFromLibraryPanel();
	}

	/**
	 * Especificar el layer para el recorte
	 * @param layer
	 */
	public FLayer getLayer() {
		return fLayer;
	}

	/**
	 * Cuando se aceptan los cambios en el panel ejecutaremos el aceptar del
	 * listener
	 */
	private void accept() {
		getColorTableListener().accept();
	}

	/**
	 * Volvemos todo a la normalidad cuando se cancela
	 */
	private void cancel() {
		getColorTableListener().closePreviewLayer();
		getLayer().getMapContext().invalidate();
	}

	/**
	 * Se dispara cuando se selecciona una pestaña en el panel
	 */
	public void stateChanged(ChangeEvent e) {
		switch (getPreviewBasePanel().getTabbedPane().getSelectedIndex()) {
			case 1:
				getColorTableListener().setLastColorTableUI(getTabInterpolated());
				break;
			default:
				getColorTableListener().setLastColorTableUI(getTabTable());
				break;
		}

		getColorTableListener().refreshItems(true);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.buttonspanel.ButtonsPanelListener#actionButtonPressed(org.gvsig.gui.beans.buttonspanel.ButtonsPanelEvent)
	 */
	public void actionButtonPressed(ButtonsPanelEvent e) {
		restoreSettings();

		// Al pulsar Aceptar o Aplicar se ejecuta el aceptar del panel
		if (e.getButton() == ButtonsPanel.BUTTON_APPLY || e.getButton() == ButtonsPanel.BUTTON_ACCEPT) {
			accept();
		}

		// Al pulsar Cancelar la ventana se cierra y se refresca la vista
		if (e.getButton() == ButtonsPanel.BUTTON_CANCEL) {
			cancel();
			close();
		}

		// Al pulsar Aceptar simplemente la ventana se cierra
		if (e.getButton() == ButtonsPanel.BUTTON_ACCEPT) {
			accepted = true;
			close();
		}

		if (e.getButton() == (ButtonsPanel.BUTTON_LAST + 2)) {
			if (RasterToolsUtil.messageBoxYesOrNot("guardar_como_predeterminado", this)) {
				saveColorTable();
				accept();
			}
		}

		if (e.getButton() == (ButtonsPanel.BUTTON_LAST + 3)) {
			getColorTableListener().equidistar();
		}
	}

	/**
	 * Acciones a ejecutar cuando se cancela
	 */
	private void close() {
		if (colorTableDialog == null)
			return;
		try {
			PluginServices.getMDIManager().closeWindow(colorTableDialog);
		} catch (ArrayIndexOutOfBoundsException e) {
			// Si la ventana no se puede eliminar no hacemos nada
		}
	}

	public void windowClosed() {
		if (!accepted) {
			restoreSettings();
			cancel();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.rastertools.colortable.library.ColorTableLibraryListener#actionColorTableChanged(org.gvsig.rastertools.colortable.library.ColorTableLibraryEvent)
	 */
	public void actionColorTableChanged(ColorTableLibraryEvent e) {
		reloadPanelsFromLibraryPanel();
	}
}