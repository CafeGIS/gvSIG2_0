package org.gvsig.catalog.gui;

import org.gvsig.catalog.CatalogClient;
import org.gvsig.catalog.drivers.profiles.IProfile;
import org.gvsig.catalog.ui.serverproperties.ServerPropertiesDialogPanel;
import org.gvsig.i18n.Messages;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.utiles.swing.jcomboServer.ServerData;

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
/* CVS MESSAGES:
 *
 * $Id$
 * $Log$
 *
 */
/**
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class ServerPropertiesDialog extends ServerPropertiesDialogPanel implements IWindow {
	private WindowInfo m_windowinfo = null;
	
	public ServerPropertiesDialog(ServerData serverData, CatalogClient client, 
			IProfile profile, Object parentFrame) {
		super(serverData, client, profile, parentFrame);		
	}

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalog.ui.serverproperties.ServerPropertiesDialogPanel#closeButtonActionPerformed()
	 */
	protected void closeButtonActionPerformed() {        
		client.setServerData(updateServerData());
		PluginServices.getMDIManager().closeWindow(this);
	} 
	
	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalog.ui.serverproperties.ServerPropertiesDialogPanel#cancelButtonActionPerformed()
	 */
	protected void cancelButtonActionPerformed() {        
		PluginServices.getMDIManager().closeWindow(this);
	} 

	
	/*
	 * (non-Javadoc)
	 * @see com.iver.andami.ui.mdiManager.IWindow#getWindowInfo()
	 */
	public WindowInfo getWindowInfo() {
		if (m_windowinfo == null){
			m_windowinfo = new WindowInfo(WindowInfo.MODALDIALOG);
			m_windowinfo.setTitle(Messages.getText("propertiesNameWindow"));
			m_windowinfo.setWidth(500);
			m_windowinfo.setHeight(280);
		}
		return m_windowinfo;
	}
	public Object getWindowProfile(){
		return WindowInfo.DIALOG_PROFILE;
	}

}
