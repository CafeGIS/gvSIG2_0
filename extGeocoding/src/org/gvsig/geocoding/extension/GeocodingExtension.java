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

import org.gvsig.geocoding.GeocodingLibrary;
import org.gvsig.geocoding.impl.DefaultGeocodingLibrary;
import org.gvsig.geocoding.preferences.GeocodingPreferences;
import org.gvsig.geocoding.utils.GeocodingTags;
import org.gvsig.tools.locator.Library;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.andami.preferences.IPreference;
import com.iver.andami.preferences.IPreferenceExtension;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.About;
import com.iver.cit.gvsig.gui.panels.FPanelAbout;
import com.iver.cit.gvsig.project.documents.view.gui.View;

/**
 * Geocoding Extension. This extension geoposition in the map a postal address.
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 */

public class GeocodingExtension extends Extension implements
		IPreferenceExtension {

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory
			.getLogger(GeocodingExtension.class);

	private static final IPreference preferencePage = new GeocodingPreferences();
	private GeocodingController control = null;

	/**
	 * Execute extension
	 */
	public void execute(String actionCommand) {

		if (actionCommand.equalsIgnoreCase(GeocodingTags.GEOCODING)) {
			control = GeocodingController.getInstance();
			PluginServices.getMDIManager().addWindow(control.getGPanel());
			control.getGPanel().setVisible(true);
			control.getGmodel().setSimple(true);
		}
	}

	/**
	 * This method initializes some parameters of the extension
	 */
	public void initialize() {

		Library lib = new DefaultGeocodingLibrary();
		lib.initialize();

		About about = (About) PluginServices.getExtension(About.class);
		FPanelAbout panelAbout = about.getAboutPanel();
		java.net.URL aboutURL = getClass().getClassLoader().getResource(
				"about/extGeocoding-about.html");
		panelAbout.addAboutUrl(PluginServices.getText(this, "geocoding"),
				aboutURL);
	}

	/**
	 * This method puts available the extension
	 * 
	 * @return true or false
	 */
	public boolean isEnabled() {

		IWindow window = PluginServices.getMDIManager().getActiveWindow();
		if (window instanceof View) {
			return true;
		}
		return true;
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

	/**
	 * Get array of extension preferences pages
	 * 
	 * @return
	 */
	public IPreference[] getPreferencesPages() {
		IPreference[] preferences = new IPreference[1];
		preferences[0] = preferencePage;
		return preferences;
	}

	/**
	 * get the control of the main extension
	 * 
	 * @return
	 */
	public GeocodingController getControl() {
		return control;
	}

	/**
	 * 
	 */
	public void postInitialize() {
		Library api = new GeocodingLibrary();
		api.postInitialize();
	}

}
