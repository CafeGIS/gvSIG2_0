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
package org.gvsig.rastertools.enhanced.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;

import javax.swing.JPanel;

import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.raster.beans.createlayer.CreateLayerPanel;
import org.gvsig.raster.beans.previewbase.PreviewBasePanel;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.IWindowListener;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;
/**
 * Dialogo para el realce por expansión del contraste
 * 
 * 19/02/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class EnhancedDialog extends JPanel implements IWindow, IWindowListener {
	private static final long serialVersionUID = -5374834293534046986L;
	
	private PreviewBasePanel    previewBasePanel = null;
	private GraphicsPanel       graphicsPanel    = null;
	private FLyrRasterSE        lyr              = null;
	private PreviewFiltering    filteredPreview  = null;
	private SelectorsPanel      controlsPanel    = null;
	private CreateLayerPanel    layerPanel       = null;
	private String              viewName         = null;
	private EnhancedListener    listener         = null;
	
	/**
	 * Constructor
	 * @param lyr Capa raster sobre la que se opera
	 * @param lyrs Lista de capas cargadas en el TOC
	 * @param width Ancho
	 * @param height Alto
	 */
	public EnhancedDialog(FLyrRasterSE lyr, int width, int height) {
		this.setPreferredSize(new Dimension(width, height));
		this.setSize(width, height);
		this.setLayout(new BorderLayout(5, 5));
		this.lyr = lyr;
		
		graphicsPanel = new GraphicsPanel(this.lyr);
		filteredPreview = new PreviewFiltering();
		filteredPreview.setFilterStatus(this.lyr.getRender().getFilterList().getStatusCloned());
		controlsPanel = new SelectorsPanel(this.lyr, graphicsPanel.getInputHistogram());
				
		this.add(getPreviewBasePanel(), BorderLayout.CENTER);
		
		listener = new EnhancedListener(controlsPanel, graphicsPanel, this, filteredPreview);
		
		listener.firstLoad();
		
		graphicsPanel.setListener(listener);
		
		graphicsPanel.updateHistogram();
		listener.updatePreview();
		
		BaseView view = (BaseView) PluginServices.getMDIManager().getActiveWindow();
		viewName = PluginServices.getMDIManager().getWindowInfo(view).getTitle();
		
		getPreviewBasePanel().getButtonsPanel().addButtonPressedListener(listener);
		
		previewBasePanel.refreshPreview();
	}

	/**
	 * Obtiene el panel con el histograma
	 * @return HistogramPanel
	 */
	public PreviewBasePanel getPreviewBasePanel() {
		if (previewBasePanel == null) {
			ArrayList list = new ArrayList();
			list.add(graphicsPanel);
			
			JPanel downPreview = new JPanel();
			getNewOrSaveLayerPanel().setLabelFilename("");
			downPreview.setLayout(new GridBagLayout());
			
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
			downPreview.add(getNewOrSaveLayerPanel().getJPanel(), gridBagConstraints);
			
			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 1;
			gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints.anchor = GridBagConstraints.NORTH;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.weighty = 1.0;
			downPreview.add(getNewOrSaveLayerPanel().getFileNamePanel(), gridBagConstraints);
			
			previewBasePanel = new PreviewBasePanel(list, controlsPanel, downPreview, filteredPreview, lyr);
		}
		return previewBasePanel;
	}
	
	/**
	 * Devuelve un Panel para el guardado de capas en disco o en memoria.
	 */
	public CreateLayerPanel getNewOrSaveLayerPanel() {
		 if (layerPanel == null) {
			 layerPanel = new CreateLayerPanel(lyr);
		 }
		 return layerPanel;
	 }
	
	/**
	 * Acciones a ejecutar cuando se cancela
	 */
	public void close() {
		try {
			PluginServices.getMDIManager().closeWindow(this);
		} catch (ArrayIndexOutOfBoundsException e) {
			//Si la ventana no se puede eliminar no hacemos nada
		}
	}
	
	/**
	 * Obtiene la capa asociada.
	 * @return FLyrRasterSE
	 */
	public FLyrRasterSE getLayer() {
		return lyr;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.andami.ui.mdiManager.IWindow#getWindowInfo()
	 */
	public WindowInfo getWindowInfo() {
		WindowInfo m_viewinfo = new WindowInfo(WindowInfo.MODELESSDIALOG | WindowInfo.RESIZABLE | WindowInfo.MAXIMIZABLE);
		if(getPreviewBasePanel().getLayer() != null)
			m_viewinfo.setAdditionalInfo(getPreviewBasePanel().getLayer().getName());
		m_viewinfo.setTitle(PluginServices.getText(this, "enhanced"));
		m_viewinfo.setHeight(this.getHeight());
		m_viewinfo.setWidth(this.getWidth());
		return m_viewinfo;
	}

	/**
	 * Evento de cerrado de la ventana desde el aspa
	 */
	public void windowClosed() {
		listener.cancel();
	}
	
	public void windowActivated() {}

	/**
	 * @return the filteredPreview
	 */
	public PreviewFiltering getFilteredPreview() {
		return filteredPreview;
	}

	/**
	 * @return the viewName
	 */
	public String getViewName() {
		return viewName;
	}

	/**
	 * @return the graphicsPanel
	 */
	public GraphicsPanel getGraphicsPanel() {
		return graphicsPanel;
	}

	public Object getWindowProfile() {
		return WindowInfo.PROPERTIES_PROFILE;
	}
}