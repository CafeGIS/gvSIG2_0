/*
 * Created on 22-jun-2005
 *
 * gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
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
package com.iver.cit.gvsig.wms;

import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.extensionpoint.ExtensionPoint;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.AddLayer;
import com.iver.cit.gvsig.fmap.layers.FLyrWMS;
import com.iver.cit.gvsig.gui.toc.WMSPropsTocMenuEntry;
import com.iver.cit.gvsig.gui.wizards.WMSWizard;

/**
 * Extension for adding WMS support to gvSIG.
 *
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 */
public class WMSClientExtension extends Extension {

    public void initialize() {
    	// Adds an entry to the TOC's floating menu to those layers defined in this extension
		ExtensionPoint exPoint = ToolsLocator.getExtensionPointManager().add(
				"View_TocActions");

		exPoint.append("FSymbolChangeColor", "", new WMSPropsTocMenuEntry());

        // Adds a new tab to the "add layer" wizard for WMS layer creation
    	AddLayer.addWizard(WMSWizard.class);

    	ToolsLocator.getExtensionPointManager().add("CatalogLayers").append(
				"OGC:WMS", "", FLyrWMS.class);
    	initilizeIcons();
    }

    public void execute(String actionCommand) {
    	// no commands, no code.
    }

    public boolean isEnabled() {
    	// may return whatever
        return false;
    }

    public boolean isVisible() {
    	// may return whatever
        return false;
    }


    void initilizeIcons(){
    	// WMSParamsPanel.java
		PluginServices.getIconTheme().registerDefault(
				"aplication-preferences-uparrow",
				this.getClass().getClassLoader().getResource("images/up-arrow.png")
			);

		// WMSParamsPanel.java
		PluginServices.getIconTheme().registerDefault(
				"aplication-preferences-downarrow",
				this.getClass().getClassLoader().getResource("images/down-arrow.png")
			);
    }
}
