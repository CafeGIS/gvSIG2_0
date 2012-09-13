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
 * 2008 Prodevelop S.L. main developer
 */

package org.gvsig.normalization.preferences;

import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.iver.andami.PluginServices;
import com.iver.andami.preferences.AbstractPreferencePage;
import com.iver.andami.preferences.StoreException;
import com.iver.utiles.XMLEntity;

/**
 * Preferences of the normalization extension
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 */

public class NormPreferences extends AbstractPreferencePage {

	private static final long serialVersionUID = 1L;
	private String normFolder = System.getProperty("user.home")
			+ File.separator + "gvSIG" + File.separator + "normalization"
			+ File.separator;

	private ImageIcon icon;

	private String tag = "Normalization_pattern_folder";

	private static final Logger log = LoggerFactory
			.getLogger(NormPreferences.class);

	static String id = NormPreferences.class.getName();

	private PreferencesNormPanel panel;

	private String pathFolder;

	/**
	 * Builder
	 */
	public NormPreferences() {
		super();
		PluginServices ps = PluginServices.getPluginServices(this);

		String bDir = ps.getClassLoader().getBaseDir();

		String path = bDir + File.separator + "images" + File.separator
				+ "preferences.png";

		icon = new ImageIcon(path);

		/* PERSISTENCE */
		XMLEntity xml = ps.getPersistentXML();
		// TAG exists in the persistence
		if (xml.contains(tag)) {
			pathFolder = String.valueOf(xml.getStringProperty(tag));
			log.debug("Getting the patterns folder from the persistence");
		}
		// TAG don't exit in the persistence
		else {
			log.debug("There isn't a folder. Doing one new");
			createNormFolder();
			xml.putProperty(tag, normFolder);
			ps.setPersistentXML(xml);
			pathFolder = normFolder;
			log.debug("Putting the default folder path");
		}

		/* Create Panel and add to gvSIG */
		panel = new PreferencesNormPanel(pathFolder);
		this.addComponent(panel);

	}

	public void storeValues() throws StoreException {

		String txt = panel.getFolderPattern();

		/* Overwrite the text in the persistence file */
		if (txt.compareToIgnoreCase("") != 0) {

			PluginServices ps = PluginServices.getPluginServices(this);
			XMLEntity xml = ps.getPersistentXML();

			xml.putProperty(tag, txt);
			ps.setPersistentXML(xml);
		}

	}

	/**
	 * This method creates a folder where you put the normalization pattern
	 */
	private void createNormFolder() {
		File normFol = new File(normFolder);
		if (!normFol.exists()) {
			normFol.mkdir();
		}
	}

	public void setChangesApplied() {
		setChanged(false);

	}

	public String getID() {
		return id;
	}

	public ImageIcon getIcon() {
		return icon;
	}

	public JPanel getPanel() {
		return this;
	}

	public String getTitle() {
		String title = PluginServices.getText(this, "pref_normalization");
		return title;
	}

	public void initializeDefaults() {

	}

	public void initializeValues() {

	}

	public boolean isValueChanged() {
		return super.hasChanged();
	}

}
