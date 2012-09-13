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
package com.iver.gvsig.centerviewpoint;

import java.awt.Color;

import org.gvsig.fmap.mapcontext.MapContext;
import org.gvsig.fmap.mapcontext.layers.FLayers;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.project.documents.view.IProjectView;
import com.iver.cit.gvsig.project.documents.view.gui.View;
import com.iver.gvsig.centerviewtopoint.gui.InputCoordinatesPanel;

/**
 * The CenterViewToPointExtension class allows to center the View over a
 * concrete point given by its coordinates.
 *
 * @author jmorell
 */
public class CenterViewToPointExtension extends Extension {
	private View vista;
	public static Color COLOR=Color.red;
    /* (non-Javadoc)
     * @see com.iver.andami.plugins.Extension#inicializar()
     */
    public void initialize() {
        // TODO Auto-generated method stub
    	PluginServices.getIconTheme().registerDefault(
				"view-center-to-point",
				this.getClass().getClassLoader().getResource("images/centerviewtopoint.png")
			);
    }

    /* (non-Javadoc)
     * @see com.iver.andami.plugins.Extension#execute(java.lang.String)
     */
    public void execute(String actionCommand) {
		vista = (View)PluginServices.getMDIManager().getActiveWindow();
        MapContext mapContext = vista.getModel().getMapContext();
        InputCoordinatesPanel dataSelectionPanel = new InputCoordinatesPanel(mapContext);
        //dataSelectionPanel.setColor(color);
		PluginServices.getMDIManager().addWindow(dataSelectionPanel);
    }

    public View getView(){
    	return vista;
    }
    /* (non-Javadoc)
     * @see com.iver.andami.plugins.Extension#isEnabled()
     */
    public boolean isEnabled() {
		com.iver.andami.ui.mdiManager.IWindow f = PluginServices.getMDIManager()
		 .getActiveWindow();
		if (f == null) {
		    return false;
		}
		if (f.getClass() == View.class) {
		    View vista = (View) f;
		    IProjectView model = vista.getModel();
		    MapContext mapa = model.getMapContext();
		    FLayers layers = mapa.getLayers();
		    for (int i=0;i < layers.getLayersCount();i++) {
               if (layers.getLayer(i).isAvailable()) return true;
		    }
		}
		return false;

    }

    /* (non-Javadoc)
     * @see com.iver.andami.plugins.Extension#isVisible()
     */
    public boolean isVisible() {
		com.iver.andami.ui.mdiManager.IWindow f = PluginServices.getMDIManager()
		 .getActiveWindow();
		if (f == null) {
		    return false;
		}
		if (f.getClass() == View.class) {
		    View vista = (View) f;
		    IProjectView model = vista.getModel();
		    MapContext mapa = model.getMapContext();
            if (mapa.getLayers().getLayersCount() > 0) {
                return true;
            }
            return false;
        }
		return false;
	}

}
