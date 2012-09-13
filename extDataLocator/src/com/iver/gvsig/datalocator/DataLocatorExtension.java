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
package com.iver.gvsig.datalocator;

import java.util.prefs.Preferences;

import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.mapcontext.MapContext;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.LayersIterator;
import org.gvsig.fmap.mapcontext.layers.operations.LayerCollection;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.project.documents.view.IProjectView;
import com.iver.cit.gvsig.project.documents.view.gui.View;
import com.iver.gvsig.datalocator.gui.DataSelectionPanel;

/**
 * The DataLocatorExtension class allows to make a quick zoom based on an
 * alphanumeric attribute.
 *
 * @author jmorell
 */
public class DataLocatorExtension extends Extension {

	IWindow iWDataSelection = null;
	IWindow previousView = null;

    /* (non-Javadoc)
     * @see com.iver.andami.plugins.Extension#inicializar()
     */
    public void initialize() {
    	registerIcons();

    }

    private void registerIcons(){
    	//view-locator
    	PluginServices.getIconTheme().registerDefault(
				"view-locator",
				this.getClass().getClassLoader().getResource("images/locator.png")
			);
    }

    /* (non-Javadoc)
     * @see com.iver.andami.plugins.Extension#execute(java.lang.String)
     */
    public void execute(String actionCommand) {
		View vista = (View)PluginServices.getMDIManager().getActiveWindow();
		MapContext mapContext = vista.getModel().getMapContext();
		DataSelectionPanel dataSelectionPanel = new DataSelectionPanel(mapContext);
		WindowInfo vi = dataSelectionPanel.getWindowInfo();
		vi.setX(Preferences.userRoot().getInt("gvSIG.DataLocator.x", vi.getX()));
		vi.setY(Preferences.userRoot().getInt("gvSIG.DataLocator.y", vi.getY()));
//		vi.setWidth(Preferences.userRoot().getInt("gvSIG.DataLocator.w", vi.getWidth()));
//		vi.setHeight(Preferences.userRoot().getInt("gvSIG.DataLocator.h", vi.getHeight()));

		PluginServices.getMDIManager().addWindow(dataSelectionPanel);
		iWDataSelection = dataSelectionPanel;
		iWDataSelection.getWindowInfo();
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
		    MapContext mapContext = model.getMapContext();
            if (mapContext.getLayers().getLayersCount() > 0) {
                LayersIterator iter = newValidLayersIterator(mapContext.getLayers());
                if (!iter.hasNext()) {
                	return false;
                }
            } else {
                return false;
            }

			// Si la vista tiene el tema con el que está configurado
			// el DataLocator y el usuario ha decidido que se abra
			// automáticamente, la abrimos inmediatamente.
            if (iWDataSelection == null || (PluginServices.getMDIManager()
            		.getWindowInfo(iWDataSelection).isClosed() && f!=previousView)) {
			    int userOpen = Preferences.userRoot().getInt("gvSIG.DataLocator.open_first_time", -1);
			    if (userOpen == 1)
			    {
					String layerName = Preferences.userRoot().get("LAYERNAME_FOR_DATA_LOCATION", "");
					FLayer lyr = mapContext.getLayers().getLayer(layerName);
					if (lyr != null)
					{
						DataSelectionPanel dataSelectionPanel = new DataSelectionPanel(mapContext);
						WindowInfo vi = dataSelectionPanel.getWindowInfo();
						vi.setX(Preferences.userRoot().getInt("gvSIG.DataLocator.x", vi.getX()));
						vi.setY(Preferences.userRoot().getInt("gvSIG.DataLocator.y", vi.getY()));
//						vi.setWidth(Preferences.userRoot().getInt("gvSIG.DataLocator.w", vi.getWidth()));
//						vi.setHeight(Preferences.userRoot().getInt("gvSIG.DataLocator.h", vi.getHeight()));

						PluginServices.getMDIManager().addWindow(dataSelectionPanel);
        				iWDataSelection = dataSelectionPanel;
        				iWDataSelection.getWindowInfo();
					}
			    }
			}
            previousView = f;
		}
        return true;
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
		    MapContext mapContext = model.getMapContext();
            return mapContext.getLayers().getLayersCount() > 0;
		} else {
		    return false;
		}
    }

    public static LayersIterator newValidLayersIterator(LayerCollection layer) {
    	return new LayersIterator((FLayer)layer){
    		public boolean evaluate(FLayer layer) {
    			if (!(layer instanceof FLyrVect))
    				return false;

    			FeatureStore featureStore;
    			try {
    				featureStore = ((FLyrVect)layer).getFeatureStore();
    				if (featureStore.getDefaultFeatureType().size() < 1)
    					return false;

    			} catch (Exception e) {
    				return false;
    			}
    			return true;
    		}
    	};
    }
}
