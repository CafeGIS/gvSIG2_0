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
 *   IVER T.I. S.A
 *   Salamanca 50
 *   46005 Valencia
 *   Spain
 *
 *   +34 963163400
 *   dac@iver.es
 */
package org.gvsig.catalog.gui;

import java.awt.Frame;

import javax.swing.Icon;
import javax.swing.JDialog;

import org.gvsig.catalog.drivers.ICatalogServiceDriver;
import org.gvsig.catalog.ui.serverconnect.ServerConnectDialogPanel;
import org.gvsig.catalog.utils.Frames;
import org.gvsig.i18n.Messages;

import com.iver.andami.PluginServices;
import com.iver.andami.persistence.serverData.ServerDataPersistence;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.utiles.swing.jcomboServer.ServerData;

/**
 * Inicia la consulta de catálogo de metadatos
 *
 * @author luisw
 */
public class ConnectDialog extends ServerConnectDialogPanel implements IWindow {
	public WindowInfo m_windowInfo = null;

	/**
	 * Constructor de la ventana de dialogo.
	 */
	public ConnectDialog() {
		super(null);
		init();
	}

	/**
	 * Inicializa el Dialog
	 */
	private void init() {
		this.setBounds(0, 0, 597, 238);
		setName("connectDialog");
	}

	/**
	 * Add a server
	 *
	 * @param server
	 */
	public static void addServer(ServerData server) {
		ServerConnectDialogPanel.addTreeMapServer(server);
	}

	/**
	 * Search Button action
	 */
	protected void searchButtonActionPerformed() {
		addCurrentHost();
		closeJDialog();

		JDialog panel = new JDialog((Frame) PluginServices.getMainFrame(), false);
		Frames.centerFrame(panel,525,165);
		panel.setTitle(Messages.getText( "catalog_search")); 
		SearchDialog dialog = new SearchDialog(getClient(),this);
		dialog.setCurrentServer(getCurrentServer());
		dialog.setFrame(panel);
		panel.getContentPane().add(dialog);
		panel.setVisible(true);   
	}

	/**
	 * Save the current host (if is neccessary) in a file using the andami
	 * persistence
	 */
	private void addCurrentHost() {
		String host = getClient().getSUri();
		ServerDataPersistence persistence = new ServerDataPersistence(this,ServerData.SERVER_TYPE_CATALOG);
		ServerData newServerData = new ServerData(host, ServerData.SERVER_TYPE_CATALOG, getClient().getProtocol(),getControlsPanel().getDatabase());
		if (client.getServerData() != null){
			if ((client.getServerData() .getServerAddress().compareTo(newServerData.getServerAddress()) == 0) &&
					(client.getServerData() .getServiceSubType().compareTo(newServerData.getServiceSubType()) == 0)){
				newServerData.setProperies(client.getServerData().getProperies());
			}
		}
		persistence.addServerData(newServerData);
		persistence.setPersistent();
	}

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalog.ui.serverconnect.ServerConnectDialogPanel#closeButtonActionPerformed()
	 */
	protected void closeButtonActionPerformed() {   
		closeJDialog();
	}

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalog.ui.serverconnect.ServerConnectDialogPanel#serverPropertiesButtonActionPerformed()
	 */
	protected void serverPropertiesButtonActionPerformed(){
		createClient();
		PluginServices.getMDIManager().addWindow(new ServerPropertiesDialog(
				getControlsPanel().getServer(),
				client,
				((ICatalogServiceDriver)getControlsPanel().getDriver()).getProfile(),
				null));
	}

	/**
	 * Close the dialog
	 */
	public void closeJDialog() {
		PluginServices.getMDIManager().closeWindow(ConnectDialog.this);
	}

	/* (non-Javadoc)
	 * @see com.iver.andami.ui.mdiManager.View#getViewInfo()
	 */
	public WindowInfo getWindowInfo() {
		if (m_windowInfo == null){
			m_windowInfo = new WindowInfo(WindowInfo.MODALDIALOG);
			m_windowInfo.setTitle(Messages.getText("metadata_catalog"));
			m_windowInfo.setHeight(200);
			m_windowInfo.setWidth(600);
		}
		return m_windowInfo;
	}
	public Object getWindowProfile(){
		return WindowInfo.DIALOG_PROFILE;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.catalog.ui.serverconnect.ServerConnectDialogPanel#getPropertiesIcon()
	 */	
	protected Icon getPropertiesIcon() {
		return PluginServices.getIconTheme().get("catalog-properties");
	}	
}
