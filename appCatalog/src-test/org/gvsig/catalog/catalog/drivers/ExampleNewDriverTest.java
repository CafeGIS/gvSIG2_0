package org.gvsig.catalog.catalog.drivers;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.gvsig.catalog.CatalogLocator;
import org.gvsig.catalog.impl.DefaultCatalogLibrary;
import org.gvsig.catalog.ui.serverconnect.ServerConnectDialog;


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
 * $Id: ExampleNewDriverTest.java 512 2007-07-24 11:25:43Z jorpiell $
 * $Log$
 * Revision 1.1.2.2  2007/07/24 11:25:42  jorpiell
 * The registers has been refactorized
 *
 * Revision 1.1.2.1  2007/07/13 12:00:35  jorpiell
 * Add the posibility to add a new panel
 *
 *
 */
/**
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class ExampleNewDriverTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DefaultCatalogLibrary library = new DefaultCatalogLibrary();
		library.initialize();
		library.postInitialize();

		CatalogLocator.getCatalogManager().register("My catalog service", ExampleNewDriver.class);
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

		new ServerConnectDialog();
	}

}
