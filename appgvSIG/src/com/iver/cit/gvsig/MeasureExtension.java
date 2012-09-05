
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

package com.iver.cit.gvsig;

import org.gvsig.fmap.mapcontext.MapContext;
import org.gvsig.fmap.mapcontext.layers.FLayers;
import org.gvsig.fmap.mapcontrol.MapControl;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.project.documents.view.IProjectView;
import com.iver.cit.gvsig.project.documents.view.gui.View;


/**
 * Extensión que controla las operaciones de medida realizadas sobre la vista.
 *
 * @author Vicente Caballero Navarro
 */
public class MeasureExtension extends Extension {
    private void registerDistanceUnits() {
    	MapContext.addDistanceUnit("Millas_Nauticas","nmi",1852);
    	MapContext.addDistanceUnit("Decimetros","dm",0.1);
    }
private void registerAreaUnits(){
	MapContext.addAreaUnit("Areas","a",false,10);
	MapContext.addAreaUnit("Hectareas","ha",false,100);
	MapContext.addAreaUnit("HanegadasV","hgV",false,28.8287);
	MapContext.addAreaUnit("HanegadasC","hgC",false,80.2467);
	MapContext.addAreaUnit("Decimetros","dm",true,0.1);
	MapContext.addAreaUnit("Acres","acre",false,63.6149);
	MapContext.addAreaUnit("Millas_Nauticas","nmi",true,1852);
}
	/* (non-Javadoc)
     * @see com.iver.andami.plugins.IExtension#initialize()
     */
    public void initialize() {
    	registerDistanceUnits();
    	registerAreaUnits();
    	PluginServices.getIconTheme().registerDefault(
				"view-query-distance",
				this.getClass().getClassLoader().getResource("images/Distancia.png")
			);

    	PluginServices.getIconTheme().registerDefault(
				"view-query-area",
				this.getClass().getClassLoader().getResource("images/Poligono16.png")
			);
    }

    /* (non-Javadoc)
     * @see com.iver.andami.plugins.IExtension#execute(java.lang.String)
     */
    public void execute(String s) {
        com.iver.andami.ui.mdiManager.IWindow view = PluginServices.getMDIManager()
                                                                   .getActiveWindow();

        if (!(view instanceof View)) {
            return;
        }

        View vista = (View) view;
        MapControl mapCtrl = vista.getMapControl();

        if (s.equals("MEDICION")) {
            mapCtrl.setTool("medicion");
        } else if (s.equals("AREA")) {
            mapCtrl.setTool("area");
        }
    }

    /* (non-Javadoc)
     * @see com.iver.andami.plugins.IExtension#isEnabled()
     */
    public boolean isEnabled() {
        com.iver.andami.ui.mdiManager.IWindow f = PluginServices.getMDIManager()
                                                                .getActiveWindow();

        if (f == null) {
            return false;
        }

        if (f instanceof View) {
            View vista = (View) f;
            IProjectView model = vista.getModel();
            MapContext mapa = model.getMapContext();

            FLayers layers = mapa.getLayers();

            for (int i = 0; i < layers.getLayersCount(); i++) {
                if (layers.getLayer(i).isAvailable()) {
                    return true;
                }
            }
        }

        return false;
    }

    /* (non-Javadoc)
     * @see com.iver.andami.plugins.IExtension#isVisible()
     */
    public boolean isVisible() {
        com.iver.andami.ui.mdiManager.IWindow f = PluginServices.getMDIManager()
                                                                .getActiveWindow();

        if (f == null) {
            return false;
        }

        if (f instanceof View) {
            View vista = (View) f;
            IProjectView model = vista.getModel();
            MapContext mapa = model.getMapContext();

            return mapa.getLayers().getLayersCount() > 0;
        }

        return false;
    }
}
