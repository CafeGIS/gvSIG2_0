
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
package org.gvsig.catalog.ui.search;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import org.gvsig.catalog.CatalogClient;


/**
 * DOCUMENT ME!
 * 
 * 
 * @author luisw
 */
public class SearchDialog extends JFrame implements WindowListener {

	/**
	 * Crea un nuevo SearchDialog.
	 * 
	 * @param cliente DOCUMENT ME!
	 * 
	 * @param client 
	 * @param isMinimized DOCUMENT ME!
	 * @param title DOCUMENT ME!
	 */
	public  SearchDialog(CatalogClient client, Object serverConnectFrame) {        
		super();
		initialize(client, serverConnectFrame);
	} 

	/**
	 * This method initializes jDialog
	 * @param client 
	 * @param isMinimized DOCUMENT ME!
	 * @param title DOCUMENT ME!
	 */
	private void initialize(CatalogClient client, Object serverConnectFrame) {        
		setBounds(0, 0, 525, 165);
		this.setTitle("Búsqueda de Catálogo");
		setName("search");
		SearchDialogPanel panel = new SearchDialogPanel(client,serverConnectFrame);
		panel.setParent(this);
		getContentPane().add(panel);
		setVisible(true);
	} 
	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
	 */

	/**
	 * 
	 * 
	 * 
	 * @param e 
	 */
	public void windowActivated(WindowEvent e) {        
		// your code here
	} 
	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowClosed(java.awt.event.WindowEvent)
	 */

	/**
	 * 
	 * 
	 * 
	 * @param e 
	 */
	public void windowClosed(WindowEvent e) {        
		System.exit(0);
	} 
	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowClosed(java.awt.event.WindowEvent)
	 */

	/**
	 * 
	 * 
	 * 
	 * @param arg0 
	 */
	public void windowClosing(WindowEvent arg0) {        
		System.exit(0);
	} 
	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowDeactivated(java.awt.event.WindowEvent)
	 */

	/**
	 * 
	 * 
	 * 
	 * @param e 
	 */
	public void windowDeactivated(WindowEvent e) {        
		// your code here
	} 
	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowDeiconified(java.awt.event.WindowEvent)
	 */

	/**
	 * 
	 * 
	 * 
	 * @param e 
	 */
	public void windowDeiconified(WindowEvent e) {        
		// your code here
	} 
	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowIconified(java.awt.event.WindowEvent)
	 */

	/**
	 * 
	 * 
	 * 
	 * @param e 
	 */
	public void windowIconified(WindowEvent e) {        
		// your code here
	} 
	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowOpened(java.awt.event.WindowEvent)
	 */

	/**
	 * 
	 * 
	 * 
	 * @param e 
	 */
	public void windowOpened(WindowEvent e) {        
		// your code here
	} 
}
