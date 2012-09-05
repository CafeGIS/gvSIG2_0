package org.gvsig.catalog.ui.serverproperties;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import org.gvsig.catalog.CatalogClient;
import org.gvsig.catalog.drivers.profiles.IProfile;
import org.gvsig.catalog.utils.CatalogConstants;

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
public class ServerPropertiesDialogPanel extends ServerPropertiesPanel implements ActionListener{
	protected Object parentFrame;
	protected CatalogClient client = null;
	
	public ServerPropertiesDialogPanel(ServerData serverData, CatalogClient client, IProfile profile, Object parentFrame) {
		super(serverData, profile);
		this.parentFrame = parentFrame;
		this.client = client;
		addActionListener(this);		
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().compareTo(CatalogConstants.CLOSE_BUTTON_ACTION_COMMAND)==0) {
			closeButtonActionPerformed();
		}else if (e.getActionCommand().compareTo(CatalogConstants.CANCEL_BUTTON_ACTION_COMMAND)==0) {
			cancelButtonActionPerformed();
		}
	}
	
	/**
	 *  * It is thrown the the close button is clicked
	 */
	protected void closeButtonActionPerformed() {        
		client.setServerData(updateServerData());
		((JFrame)parentFrame).setVisible(false);
	} 
	
	/**
	 *  * It is thrown the the close button is clicked
	 */
	protected void cancelButtonActionPerformed() {        
		((JFrame)parentFrame).setVisible(false);
	} 

}
