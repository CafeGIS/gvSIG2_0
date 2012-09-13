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
package org.gvsig.gazetteer.gui;


import java.awt.Frame;

import javax.swing.JDialog;

import org.gvsig.catalog.utils.Frames;
import org.gvsig.gazetteer.ui.serverconnect.ServerConnectDialogPanel;
import org.gvsig.i18n.Messages;

import com.iver.andami.PluginServices;
import com.iver.andami.persistence.serverData.ServerDataPersistence;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.utiles.swing.jcomboServer.ServerData;

/**
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class ConnectDialog extends ServerConnectDialogPanel
implements IWindow {
	public WindowInfo m_windowInfo = null;

	/**
	 * Building the dialog Window
	 */
	public ConnectDialog(/*FLayers layers, MapControl mapCtrl*/) {
		super(null);
		init();           
	}

	/**
	 * Establishing window properties
	 *
	 */
	private void init() {
		this.setBounds(0, 0, 610, 263);
		setName("connectDialog");
	}

	public static void addServer(ServerData server) {
		ServerConnectDialogPanel.addTreeMapServer(server);
	}

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.gazetteer.ui.serverconnect.ServerConnectDialogPanel#searchButtonActionPerformed()
	 */
	protected void searchButtonActionPerformed() {
		addCurrentHost();
		closeJDialog();	       

		JDialog panel = new JDialog((Frame) PluginServices.getMainFrame(), false);
		Frames.centerFrame(panel,525,125);
		panel.setTitle(Messages.getText( "gazetteer_search")); 
		SearchDialog dialog = new SearchDialog(client,this);
		dialog.setFrame(panel);
		dialog.setCurrentServer(getCurrentServer());
		dialog.setFrame(panel);
		panel.getContentPane().add(dialog);
		panel.setVisible(true);   
	}

	/**
	 * Save the current host (if is neccessary) in a file using the andami
	 * persistence
	 * @param host
	 * Host to save
	 */
	private void addCurrentHost(){
		String host = client.getSUri();
		ServerDataPersistence persistence = new ServerDataPersistence(this,ServerData.SERVER_TYPE_GAZETTEER);
		persistence.addServerData(new ServerData(host, ServerData.SERVER_TYPE_GAZETTEER, client.getProtocol()));
		persistence.setPersistent();
	}

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.gazetteer.ui.serverconnect.ServerConnectDialogPanel#closeButtonActionPerformed()
	 */
	protected void closeButtonActionPerformed() {   
		closeJDialog();
	}

	/**
	 * Cierra el Dialog
	 */
	public void closeJDialog() {
		PluginServices.getMDIManager().closeWindow(ConnectDialog.this);
	}

	/* (non-Javadoc)
	 * @see com.iver.andami.ui.mdiManager.View#getViewInfo()
	 */
	public WindowInfo getWindowInfo() {
		if (m_windowInfo == null){
			m_windowInfo=new WindowInfo(WindowInfo.RESIZABLE | WindowInfo.MODALDIALOG);		
			m_windowInfo.setTitle(Messages.getText( "gazetteer_connect"));
			m_windowInfo.setHeight(225);
			m_windowInfo.setWidth(610);
		}
		return m_windowInfo;
	}
	public Object getWindowProfile(){
		return WindowInfo.DIALOG_PROFILE;
	}

}
