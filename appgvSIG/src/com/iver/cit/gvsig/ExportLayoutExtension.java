/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
 *
 * Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
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
 *   Av. Blasco Ib��ez, 50
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
* $Id: ExportLayoutExtension.java 15647 2007-10-30 12:03:52Z jmvivo $
* $Log$
* Revision 1.2  2006-12-20 14:40:37  caballero
* Remodelado Layout
*
* Revision 1.1  2006/11/06 15:14:52  jaume
* new extension for pdf and ps
*
*
*/
package com.iver.cit.gvsig;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;
/**
 * Extension for exporting the layout to PDF or PostScript.
 *
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 */
public class ExportLayoutExtension extends Extension {

	private Layout layout;

	public void initialize() {
		// TODO Auto-generated method stub
		registerIcons();
	}

	private void registerIcons(){
		PluginServices.getIconTheme().registerDefault(
				"layout-export-pdf",
				this.getClass().getClassLoader().getResource("images/pdf.png")
			);

		PluginServices.getIconTheme().registerDefault(
				"layout-export-ps",
				this.getClass().getClassLoader().getResource("images/ps.png")
			);
	}

	/**
	 * @see com.iver.andami.plugins.IExtension#execute(java.lang.String)
	 */
	public void execute(String s) {
		layout = (Layout) PluginServices.getMDIManager().getActiveWindow();
		if (s.equals("PDF")){
			layout.layoutToPDF();
		} else if (s.equals("PS")){
			layout.layoutToPS();
		}
	}

	public boolean isEnabled() {
		return true;
	}

	public boolean isVisible() {
		IWindow f = PluginServices.getMDIManager().getActiveWindow();

		if (f == null) {
			return false;
		}

		if (f instanceof Layout) {
		//	Layout layout = (Layout) f;

			return true; //layout.m_Display.getMapControl().getMapContext().getLayers().layerCount() > 0;
		} else {
			return false;
		}
	}

}
