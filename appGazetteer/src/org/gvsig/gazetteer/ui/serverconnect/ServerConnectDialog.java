
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
package org.gvsig.gazetteer.ui.serverconnect;
import org.gvsig.catalog.utils.Frames;

/**
 * This class implements the JFrame for the server
 * connect panel that will be used when the gazetteer
 * client is runned without gvSIG. It also implements
 * the window listener that closes the application when
 * the window is closed.
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class ServerConnectDialog extends org.gvsig.catalog.ui.serverconnect.ServerConnectDialog{
	private static final long serialVersionUID = -6457336150268592182L;

	public  ServerConnectDialog() {        
		super();       
		initialize();
	} 

	/**
	 * This method initializes jDialog
	 */
	protected void initialize() {        
		Frames.centerFrame(this, 625, 290);
		this.setTitle("Metadata catalog");
		setResizable(false);
		setName("serverConnect");
		getContentPane().add(new ServerConnectDialogPanel(
				this));
		addWindowListener(this);
		setVisible(true);
	} 	
}
