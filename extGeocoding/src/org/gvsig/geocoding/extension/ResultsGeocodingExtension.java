/* gvSIG. Geographic Information System of the Valencian Government
 *
 * Copyright (C) 2007-2008 Infrastructures and Transports Department
 * of the Valencian Government (CIT)
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 * 
 */

/*
 * AUTHORS (In addition to CIT):
 * 2008 Prodevelop S.L. main development
 */

package org.gvsig.geocoding.extension;

import java.awt.Component;

import javax.swing.JOptionPane;

import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.impl.DefaultFeatureStore;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.FLayers;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;
import org.gvsig.geocoding.gui.results.ResultsPanel;
import org.gvsig.geocoding.utils.GeocodingTags;
import org.gvsig.project.document.table.FeatureTableDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.andami.preferences.IPreference;
import com.iver.andami.preferences.IPreferenceExtension;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.ProjectExtension;
import com.iver.cit.gvsig.project.Project;
import com.iver.cit.gvsig.project.ProjectFactory;
import com.iver.cit.gvsig.project.documents.view.gui.View;

/**
 * Geocoding Extension. This extension geoposition in the map one postal
 * address.
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 */

public class ResultsGeocodingExtension extends Extension implements
		IPreferenceExtension {


	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory
			.getLogger(ResultsGeocodingExtension.class);

	private GeocodingController control = null;

	/**
	 * constructor
	 */
	public ResultsGeocodingExtension() {
		control = GeocodingController.getInstance();
	}

	/**
	 * execute
	 */
	public void execute(String actionCommand) {

		if (actionCommand.equalsIgnoreCase(GeocodingTags.RESULTSGEOCODING)) {
			//get control
			control = GeocodingController.getInstance();
			//show attributes selected geocoding layer
			DefaultFeatureStore shpstore = (DefaultFeatureStore)showTableAttributes();
			//registry panel like store observer
			shpstore.addObserver(control.getGResPanel());
			control.getGResPanel().setSelectedStore(shpstore);
			//Get table all results relate with the layer
			FeatureTableDocument table = loadTableAllResultsGeocodingLayer();
			FeatureStore allstore = table.getStore();
			control.getGResPanel().setAllStore(allstore);
			//show panel
			IWindow[] wins = PluginServices.getMDIManager().getAllWindows();
			boolean exist = false;
			for (int i = 0; i < wins.length; i++) {
				if (wins[i] instanceof ResultsPanel) {
					((ResultsPanel) wins[i]).setVisible(true);
					exist = true;
					break;
				}
			}
			if (!exist) {
				PluginServices.getMDIManager()
						.addWindow(control.getGResPanel());
				control.getGResPanel().setVisible(true);
			}
			
			//registry panel like store observer
			shpstore.addObserver(control.getGResPanel());
		}
	}

	/**
	 * This method initializes some parameters of the extension
	 */
	public void initialize() {

	}

	/**
	 * This method puts available the extension
	 * 
	 * @return true or false
	 */
	public boolean isEnabled() {

		IWindow window = PluginServices.getMDIManager().getActiveWindow();
		// Visible when there are views in the window
		if (window instanceof View) {
			return true;
		}
		return false;
	}

	/**
	 * This method puts visible the extension
	 * 
	 * @return true or false
	 */
	public boolean isVisible() {
		FLyrVect lyr = getSelectedVectLayer();
		if (lyr != null) {
			Object obj = lyr.getProperty(GeocodingTags.GEOCODINGPROPERTY);
			if (obj instanceof FeatureTableDocument) {
				return true;
			}
			return false;
		}
		return false;
	}

	/**
	 * Get array of extension preferences pages
	 * 
	 * @return
	 */
	public IPreference[] getPreferencesPages() {
		return null;
	}

	/**
	 * 
	 */
	private FeatureStore showTableAttributes() {

		FLyrVect lyr = getSelectedVectLayer();
		FeatureStore store = null;
		if (lyr != null) {
			try {
				store = lyr.getFeatureStore();
				if (store != null) {
					Project project = ((ProjectExtension) PluginServices
							.getExtension(ProjectExtension.class)).getProject();

					FeatureTableDocument table = ProjectFactory.createTable(lyr
							.getName(), store);					

					project.addDocument(table);

					IWindow window = table.createWindow();
					if (window == null) {
						JOptionPane.showMessageDialog(
								(Component) PluginServices.getMainFrame(),
								PluginServices.getText(this,
										"error_opening_the_document"));
					}
					PluginServices.getMDIManager().addWindow(window);
					return store;
				}
			} catch (ReadException e) {
				log.error("", e);
			}
		}
		return null;

	}

	private FLyrVect getSelectedVectLayer() {
		IWindow window = PluginServices.getMDIManager().getActiveWindow();
		if (window instanceof View) {
			View vi = (View) window;
			FLayers lyrs = vi.getMapControl().getMapContext().getLayers();
			if (lyrs != null && lyrs.getLayersCount() > 0) {
				if (lyrs.getActives().length > 0) {
					FLayer lyr = lyrs.getActives()[0];
					if (lyr instanceof FLyrVect) {
						return (FLyrVect) lyr;
					}
				}
			}
		}
		return null;
	}
	
	private FLyrVect getSelectedGeocodingVectLayer() {
		IWindow[] window = PluginServices.getMDIManager().getOrderedWindows();
		for (int i = 0; i < window.length; i++) {
			if (window[i] instanceof View) {
				View vi = (View) window[i];
				FLayers lyrs = vi.getMapControl().getMapContext().getLayers();
				if (lyrs != null && lyrs.getLayersCount() > 0) {
					if (lyrs.getActives().length > 0) {
						FLayer[] lyr = lyrs.getActives();
						for (int k = 0; k < lyr.length; k++) {
							if(lyr[k] instanceof FLyrVect){
								FLyrVect vlyr = (FLyrVect)lyr[k];
								Object obj = vlyr.getProperty(GeocodingTags.GEOCODINGPROPERTY);
								if(obj != null){
									return vlyr;									
								}
							}
						}
					}
				}
			}
		}
		
		return null;
	}
	
	
	private FeatureTableDocument loadTableAllResultsGeocodingLayer(){
		FLyrVect lyr = getSelectedGeocodingVectLayer();
		FeatureTableDocument table = (FeatureTableDocument)lyr.getProperty(GeocodingTags.GEOCODINGPROPERTY);
		return table;
	}

}
