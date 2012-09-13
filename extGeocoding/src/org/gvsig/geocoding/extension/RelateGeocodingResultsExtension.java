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

import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;

import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.FLayers;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;
import org.gvsig.geocoding.gui.relation.RelatePanel;
import org.gvsig.geocoding.utils.GeocodingTags;
import org.gvsig.project.document.table.FeatureTableDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.andami.preferences.IPreference;
import com.iver.andami.preferences.IPreferenceExtension;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.project.Project;
import com.iver.cit.gvsig.project.documents.ProjectDocument;
import com.iver.cit.gvsig.project.documents.gui.ProjectWindow;
import com.iver.cit.gvsig.project.documents.view.gui.View;

/**
 * Geocoding Extension. This extension geoposition in the map one postal
 * address.
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 */

public class RelateGeocodingResultsExtension extends Extension implements
		IPreferenceExtension {

	

	private static final Logger log = LoggerFactory
			.getLogger(RelateGeocodingResultsExtension.class);

	private GeocodingController control = null;

	/**
	 * constructor
	 */
	public RelateGeocodingResultsExtension(){
		control = GeocodingController.getInstance();	
	}
	
	/**
	 * execute
	 */
	public void execute(String actionCommand) {

		if (actionCommand.equalsIgnoreCase(GeocodingTags.RELATEGEOCODING)) {					
			
			//get layer from gvSIG view
			FLyrVect lyr = control.getSelectedFLyrVect();
			// set FLayer in panel
			RelatePanel panel = control.getGRelPanel();
			panel.setLyr(lyr);
			// fill combo with project tables
			panel.putTablesInCombo();			
			//put visible panel
			PluginServices.getMDIManager().addWindow(control.getGRelPanel());
			control.getGRelPanel().setVisible(true);
			
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
		if (window instanceof View) {
			View vi = (View) window;
			FLayers lyrs = vi.getMapControl().getMapContext().getLayers();
			if (lyrs != null && lyrs.getLayersCount() > 0) {
				if (lyrs.getActives().length > 0) {
					FLayer lyr = lyrs.getActives()[0];
					if (lyr instanceof FLyrVect) {
						FLyrVect ly = (FLyrVect) lyr;
						List<FeatureTableDocument> tables = control.getListgvSIGTables();
						Object obj = ly.getProperty(GeocodingTags.GEOCODINGPROPERTY);
						if (obj instanceof FeatureTableDocument
								&& tables.size() > 0) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * This method puts visible the extension
	 * 
	 * @return true or false
	 */
	public boolean isVisible() {
		IWindow window = PluginServices.getMDIManager().getActiveWindow();
		// Visible when there are views in the window
		if (window instanceof View) {
			return true;
		}
		return false;
	}

	public IPreference[] getPreferencesPages() {

		return null;
	}
	
	

	


}
