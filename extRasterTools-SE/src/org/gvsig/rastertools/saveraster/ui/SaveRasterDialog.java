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
package org.gvsig.rastertools.saveraster.ui;

import java.awt.BorderLayout;
import java.awt.Container;

import org.gvsig.fmap.mapcontext.layers.FLayers;
import org.gvsig.fmap.mapcontrol.MapControl;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanel;
import org.gvsig.gui.beans.defaultbuttonspanel.DefaultButtonsPanel;
import org.gvsig.rastertools.saveraster.ui.listener.SaveRasterDialogListener;
import org.gvsig.rastertools.saveraster.ui.panels.listener.DataInputListener;
import org.gvsig.rastertools.saveraster.util.DriverProperties;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
/**
 * Panel que contiene los botones de Aceptar, Cancelar y Aplicar heredando de
 * DefaultDialogPanel. Dentro de este estará el panel con los controles de
 * salvar a raster.
 * 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class SaveRasterDialog extends DefaultButtonsPanel implements IWindow {
	final private static long 			serialVersionUID = -3370601314380922368L;
	private DataInputListener			listener =  null;
	public DriverProperties				driverProps = null;
	private SaveRasterPanel				controlsPanel = null;
	private FLayers				 		layers = null;

	/**
	 * Constructor de la ventana de dialogo para gvSIG.
	 */
	public SaveRasterDialog(FLayers layers, MapControl mapCtrl) {
		super(ButtonsPanel.BUTTONS_APPLYCLOSE);
		driverProps = new DriverProperties();
		this.layers = layers;
		init(mapCtrl);
	}

	/**
	 * Constructor generico para poder visualizar la ventana desde
	 * una función main.
	 */
	public SaveRasterDialog() {
		super(ButtonsPanel.BUTTONS_NONE);
			init(null);
	}


	/**
	 * Inicialización del panel.
	 * @return void
	 */
	public void init(MapControl mapCtrl) {
		setLayout(new BorderLayout());
		add(getControlsPanel(), BorderLayout.CENTER);

		if (layers != null && mapCtrl != null)
			new SaveRasterDialogListener(this, layers, mapCtrl);

		setSize(new java.awt.Dimension(405, 410));
		getDataInputListener();

		// Ocultamos el botón de aplicar
		getButtonsPanel().getButton(ButtonsPanel.BUTTON_APPLY).setEnabled(false);

		setName("saveRaster");
	}

	/**
	 * Cierra esta ventana a través de andami
	 */
	public void closeJDialog() {
		this.setVisible(false);
		for(int i = 0; i < layers.getLayersCount(); i++){
			layers.getLayer(i).getMapContext().invalidate();
		}
		PluginServices.getMDIManager().closeWindow(SaveRasterDialog.this);
	}

	/**
	 * Obtiene el panel con los controles de Salvar a Raster.
	 * @return
	 */
	public SaveRasterPanel getControlsPanel() {
		if (controlsPanel == null) {
			controlsPanel = new SaveRasterPanel();
			controlsPanel.setPreferredSize(new java.awt.Dimension(379, 317));
		}
		return controlsPanel;
	}

	public DataInputListener getDataInputListener() {
		if (listener == null) {
			listener = new DataInputListener(getControlsPanel());
			listener.setDialogPanel(this);
		}
		return listener;
	}

	/**
	 * This method initializes jContentPane	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	public Container getContentPane() {
		return this;
	}

	/**
	 * Asigna la lista de capas
	 * @param layers
	 */
	public void setLayerList(FLayers layers){
		this.layers = layers;
	}

	/**
	 * @see com.iver.mdiApp.ui.MDIManager.IWindow#getWindowInfo()
	 */
	public WindowInfo getWindowInfo() {
		WindowInfo m_viewinfo = new WindowInfo(WindowInfo.MODELESSDIALOG | WindowInfo.RESIZABLE);
		m_viewinfo.setTitle(PluginServices.getText(this, "salvar_raster_geo"));
		return m_viewinfo;
	}

	public Object getWindowProfile() {
		return WindowInfo.PROPERTIES_PROFILE;
	}
}