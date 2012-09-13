/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2007 Instituto de Desarrollo Regional and Generalitat Valenciana.
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
 *   Av. Blasco Ibáñez, 50
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

package org.gvsig.rastertools.roi.ui;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JPanel;

import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontrol.MapControl;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanel;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelEvent;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelListener;
import org.gvsig.gui.beans.table.exceptions.NotInitializeException;
import org.gvsig.raster.grid.GridException;
import org.gvsig.raster.util.RasterToolsUtil;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.IWindowListener;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;

/**
 * Diálogo para el gestor de ROIs.
 * 
 * @author Diego Guerrero Sevilla (diego.guerrero@uclm.es)
 *
 */
public class ROIManagerDialog extends JPanel implements IWindow, IWindowListener, ButtonsPanelListener {
	private static final long serialVersionUID = 2847035927527203595L;
	
	
	private ROIsManagerPanel roiManagerPanel = null;
	
	private MapControl mapControl = null;
	
	private String previousTool = null;
	
	/**
	 * Constructor
	 * @param width Ancho del panel
	 * @param height Alto del panel
	 */
	public ROIManagerDialog(int width, int height) {
		this.setSize(width, height);
		this.setLayout(new BorderLayout());
		this.add(getROIsManagerPanel(), BorderLayout.CENTER);
		IWindow[] list = PluginServices.getMDIManager().getAllWindows();
		BaseView view = null;
		for (int i = 0; i < list.length; i++) {
			if(list[i] instanceof BaseView)
				view = (BaseView)list[i];
		}
		if (view == null)
			return;
		mapControl = view.getMapControl();
		previousTool = mapControl.getCurrentTool();
		
	}

	public ROIsManagerPanel getROIsManagerPanel() {
		if (roiManagerPanel == null){
			roiManagerPanel = new ROIsManagerPanel(this);
			roiManagerPanel.addButtonPressedListener(this);
		}
		return roiManagerPanel;
	}

	public WindowInfo getWindowInfo() {
		WindowInfo m_viewinfo = new WindowInfo(WindowInfo.MODELESSDIALOG | WindowInfo.RESIZABLE | WindowInfo.MAXIMIZABLE);
		if(roiManagerPanel.getTablePanel().getFLayer() != null)
			m_viewinfo.setAdditionalInfo(roiManagerPanel.getTablePanel().getFLayer().getName());
		m_viewinfo.setTitle(PluginServices.getText(this, "regiones_interes")+" - "+m_viewinfo.getAdditionalInfo());
		m_viewinfo.setHeight(this.getHeight());
		m_viewinfo.setWidth(this.getWidth());
		return m_viewinfo;
	}

	public void actionButtonPressed(ButtonsPanelEvent e) {
//		 Al pulsar Aceptar o Aplicar se ejecuta el aceptar del panel
		if (e.getButton() == ButtonsPanel.BUTTON_APPLY || e.getButton() == ButtonsPanel.BUTTON_ACCEPT) {
			ArrayList rois = roiManagerPanel.getTablePanel().getROIs();
			if (rois.size() >0){
				((FLyrRasterSE)roiManagerPanel.getTablePanel().getFLayer()).setRois(rois);
			}else
				((FLyrRasterSE)roiManagerPanel.getTablePanel().getFLayer()).setRois(null);
		}

		// Al pulsar Cancelar la ventana se cierra y se refresca la vista
		if (e.getButton() == ButtonsPanel.BUTTON_CANCEL) {
			close();
		}

		// Al pulsar Aceptar simplemente la ventana se cierra
		if (e.getButton() == ButtonsPanel.BUTTON_ACCEPT) {
			close();
		}
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
	 * Se asigna el layer sobre el que trabaja el gestor de ROIs
	 * @param layer
	 * @throws GridException 
	 */
	public void setLayer(FLayer layer) throws GridException {
		getROIsManagerPanel().getTablePanel().setFLayer(layer);
	}
	
	public void setPreviousTool(){
		if (previousTool!=null)
			roiManagerPanel.getTablePanel().getMapControl().setTool(previousTool);
	}

	public void windowActivated() {
		try {
			if (getROIsManagerPanel().getTablePanel().getTable().getSelectedRows().length==1){
				getROIsManagerPanel().getTablePanel().setToolsEnabled(true);
				getROIsManagerPanel().getTablePanel().selectDrawRoiTool();
			}
		} catch (NotInitializeException e) {
			RasterToolsUtil.messageBoxError("error_tabla_rois", this, e);
		}
	}

	public void windowClosed() {
		/*
		 * Limpiar los gráficos de la vista:
		 */
		getROIsManagerPanel().getTablePanel().clearRoiGraphics();
		getROIsManagerPanel().getTablePanel().getMapControl().rePaintDirtyLayers();
		setPreviousTool();
	}

	public Object getWindowProfile() {
		return WindowInfo.PROPERTIES_PROFILE;
	}
}
