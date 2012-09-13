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

import java.io.File;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.About;

/**
 * About Extension of ExtNormalization
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 */
public class AboutExtension extends Extension {

	/**
	 * @see com.iver.andami.plugins.Extension#inicializar()
	 */
	public void initialize() {
		About classAbout = (About) PluginServices
				.getExtension(com.iver.cit.gvsig.About.class);
		ClassLoader classLoader = getClass().getClassLoader();
		java.net.URL aboutURL2 = classLoader.getResource("about"
				+ File.separator + "normabout.htm");
		String title = PluginServices.getText(null, "pref_normalization");
		classAbout.getAboutPanel().addAboutUrl(title, aboutURL2);
	}

	/**
	 * @see com.iver.andami.plugins.Extension#execute(java.lang.String)
	 */
	public void execute(String actionCommand) {
	}

	/**
	 * @see com.iver.andami.plugins.Extension#isEnabled()
	 */
	public boolean isEnabled() {
		return true;
	}

	/**
	 * @see com.iver.andami.plugins.Extension#isVisible()
	 */
	public boolean isVisible() {
		return true;
	}

}
