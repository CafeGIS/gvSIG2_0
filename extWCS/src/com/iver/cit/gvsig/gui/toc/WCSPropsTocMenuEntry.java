/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
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
package com.iver.cit.gvsig.gui.toc;

import org.gvsig.fmap.mapcontext.layers.FLayer;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.layers.FLyrWCS;
import com.iver.cit.gvsig.gui.dialog.WCSPropsDialog;
import com.iver.cit.gvsig.project.documents.view.toc.AbstractTocContextMenuAction;
import com.iver.cit.gvsig.project.documents.view.toc.ITocItem;

/**
 * Changes the connection properties.
 *
 * Cambia las propiedades de la conexión al servidor WCS.
 *
 * @author jaume - jaume.dominguez@iver.es
 */
public class WCSPropsTocMenuEntry extends AbstractTocContextMenuAction {

	public void execute(ITocItem item, FLayer[] selectedItems) {
       	WCSPropsDialog dialog = new WCSPropsDialog((FLyrWCS) selectedItems[0]);
       	PluginServices.getMDIManager().addWindow(dialog);
	}

	public String getText() {
		return PluginServices.getText(this, "wcs_properties");
	}

	public boolean isEnabled(ITocItem item, FLayer[] selectedItems) {
		return selectedItems.length == 1 && selectedItems[0].isAvailable();
	}

	public boolean isVisible(ITocItem item, FLayer[] selectedItems) {
		if (selectedItems.length != 1) {
			return false;
		}
		return selectedItems[0] instanceof FLyrWCS;

	}

}
