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
package com.iver.cit.gvsig;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;


/**
 * Extensión para insertar un localizador sobre el Layout.
 *
 * @author Vicente Caballero Navarro
 */
public class LayoutInsertOverViewExtension extends Extension {
    private Layout layout = null;


    public void initialize() {
		// TODO Auto-generated method stub
	}


    public void execute(String s) {
    	 layout = (Layout) PluginServices.getMDIManager().getActiveWindow();

        if (s.equals("RECTANGLEOVERVIEW")) {
     		layout.getLayoutControl().setTool("layoutaddoverview");
     	}
    }


    public boolean isEnabled() {
    	IWindow f = PluginServices.getMDIManager().getActiveWindow();

        if (f == null) {
            return false;
        }

        if (f instanceof Layout) {
            return ((Layout) f).getLayoutContext().isEditable();
        }

        return false;
	}


    public boolean isVisible() {
		IWindow f = PluginServices.getMDIManager().getActiveWindow();

		if (f == null) {
			return false;
		}

		if (f instanceof Layout) {
			return true;
		} else {
			return false;
		}
	}
}
