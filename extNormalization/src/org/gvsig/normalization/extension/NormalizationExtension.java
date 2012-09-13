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

package org.gvsig.normalization.extension;


import org.gvsig.app.daltransform.DataTransformLocator;
import org.gvsig.app.daltransform.DataTransformManager;
import org.gvsig.geocoding.GeocodingLibrary;
import org.gvsig.normalization.gui.NormalizationTransformGui;
import org.gvsig.normalization.preferences.NormPreferences;
import org.gvsig.tools.locator.Library;

import com.iver.andami.plugins.Extension;
import com.iver.andami.preferences.IPreference;
import com.iver.andami.preferences.IPreferenceExtension;

/**
 * Normalization Extension. This extension puts the normalization preferences.
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 */

public class NormalizationExtension extends Extension implements
		IPreferenceExtension {

	private static final IPreference preference = new NormPreferences();

	/**
	 * This method executes the normalization file extension
	 * 
	 * @param actionCommand
	 */
	public void execute(String actionCommand) {

	}

	/**
	 * This method initializes some parameters of the extension
	 */

	public void initialize() {
		DataTransformManager dataTransformManager = DataTransformLocator.getDataTransformManager();
		dataTransformManager.registerDataTransform("NormalizationTransform", NormalizationTransformGui.class);
	}

	/**
	 * This method puts available the extension
	 * 
	 * @return enable
	 */
	public boolean isEnabled() {
		return false;
	}

	/**
	 * This method puts visible the extension
	 * 
	 * @return visible
	 */
	public boolean isVisible() {
		return false;
	}

	/**
 * 
 */
	public IPreference[] getPreferencesPages() {
		IPreference[] preferences = new IPreference[1];
		preferences[0] = preference;
		return preferences;
	}

	/**
	 * 
	 */
	public void postInitialize() {
		Library api = new GeocodingLibrary();
		api.postInitialize();
	}

}
