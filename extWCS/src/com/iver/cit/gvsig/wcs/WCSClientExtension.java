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
package com.iver.cit.gvsig.wcs;

import org.gvsig.fmap.mapcontrol.MapControl;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.extensionpoint.ExtensionPoint;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.AddLayer;
import com.iver.cit.gvsig.fmap.layers.FLyrWCS;
import com.iver.cit.gvsig.gui.toc.WCSPropsTocMenuEntry;
import com.iver.cit.gvsig.gui.toc.WCSZoomPixelCursorTocMenuEntry;
import com.iver.cit.gvsig.gui.wcs.WCSWizard;
import com.iver.cit.gvsig.project.documents.view.toc.gui.FPopupMenu;
import com.iver.utiles.extensionPointsOld.ExtensionPoints;
import com.iver.utiles.extensionPointsOld.ExtensionPointsSingleton;

/**
 * @author jaume - jaume.dominguez@iver.es
 *
 */
public class WCSClientExtension extends Extension {
	/**
	 * Initializes the toc menu
	 *
	 */
	public void initialize() {
		// Adds a new tab to the "add layer" wizard for WMS layer creation

		ExtensionPoint exPoint = ToolsLocator.getExtensionPointManager().add(
				"View_TocActions");



		// Adds a new tab to the "add layer" wizard for WMS layer creation
		AddLayer.addWizard(WCSWizard.class);



    	// Adds an entry to the TOC's floating menu to those layers defined in this extensionFPopupMenu.addEntry(new WCSPropsTocMenuEntry());
		exPoint.append("FSymbolChangeColor", "", new WCSPropsTocMenuEntry());

    	// Adds an entry to the TOC's floating menu for the "zoom to pixel" tool
    	exPoint.append("WCSZoomPixel", "",
						new WCSZoomPixelCursorTocMenuEntry());

		ToolsLocator.getExtensionPointManager().add("CatalogLayers").append(
				"OGC:WMS", "", FLyrWCS.class);

    	initializeIcons();


	}

	public void execute(String actionCommand) {
		// no commands, no code.
	}

	public boolean isEnabled() {
		// may return whatever
		return true;
	}

	public boolean isVisible() {
		// may return whatever
		return false;
	}

	void initializeIcons(){
		PluginServices.getIconTheme().registerDefault(
	    		"view-previsualize-area",
	    		MapControl.class.getResource("images/ZoomPixelCursor.gif")
	    	);
		PluginServices.getIconTheme().registerDefault(
	    		"ico-WCS-Layer",
	    		MapControl.class.getResource("images/icoLayer.png")
	    	);
	}
}
