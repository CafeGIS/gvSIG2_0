
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
package org.gvsig.catalog;
import java.io.File;
import java.net.MalformedURLException;
import java.util.Locale;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.gvsig.catalog.impl.DefaultCatalogLibrary;
import org.gvsig.catalog.ui.serverconnect.ServerConnectDialog;
import org.gvsig.i18n.Messages;


/**
 * This class is the launcher application
 * 
 * 
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class CatalogAP {

	/**
	 * @param args 
	 */
	public static void main(String[] args) {
		DefaultCatalogLibrary library = new DefaultCatalogLibrary();
		library.initialize();
		library.postInitialize();
		
		Messages.addLocale(Locale.getDefault());
		try {
			Messages.addResourceFamily("text", new File("."));
		} catch (MalformedURLException e1) {
			System.err.println("Error obteniendo recurso de traduccion");

		}

		//Get the currently installed look and feel
		UIManager.getLookAndFeel();
		// Install a different look and feel; specifically, the Windows look and feel
		try {
			UIManager.setLookAndFeel(
					"com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (InstantiationException e) {
		} catch (ClassNotFoundException e) {
		} catch (UnsupportedLookAndFeelException e) {
		} catch (IllegalAccessException e) {
		}



		//ServerConnectDialogPanel frame = new ServerConnectDialogPanel();
		new ServerConnectDialog();
	} 
}
