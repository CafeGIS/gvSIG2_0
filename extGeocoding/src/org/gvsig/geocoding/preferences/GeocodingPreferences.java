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
 * 2008 Prodevelop S.L. vsanjaime Programador
 */

package org.gvsig.geocoding.preferences;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iver.andami.PluginServices;
import com.iver.andami.preferences.AbstractPreferencePage;
import com.iver.andami.preferences.StoreException;
import com.iver.utiles.XMLEntity;

/**
 * Geocoding preferences
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 */

public class GeocodingPreferences extends AbstractPreferencePage {

	private static final long serialVersionUID = 1L;

	private ImageIcon icon;

	private String lang = "";

	private String tag = "";

	private static final Logger log = LoggerFactory
			.getLogger(GeocodingPreferences.class);

	static String id = GeocodingPreferences.class.getName();

	public static final String GEOCODINGELEMENTS = "GeocodingElements";

	public static final String ES = "es";
	public static final String CA = "ca";
	public static final String EN = "en";

	private static final String TEMPLATES_FOLDER = "templates";

	private PreferencesGeocoPanel panel;

	private String andamiConfigPath = System.getProperty("user.home")
			+ File.separator + "gvSIG" + File.separator;

	private String geocoPath = System.getProperty("user.home") + File.separator
			+ "gvSIG" + File.separator + "geocoding";

	private String geocoFolder = System.getProperty("user.home")
			+ File.separator + "gvSIG" + File.separator + "geocoding"
			+ File.separator;

	private File persistenceFile;

	private FileReader reader;

	private FileWriter writer;

	/**
	 * Constructor
	 */
	public GeocodingPreferences() {
		super();

		icon = new ImageIcon(this.getClass().getClassLoader().getResource(
				"images/preferences.png"));

		/* Create Panel and add to gvSIG */
		panel = new PreferencesGeocoPanel();
		addComponent(panel);

		/* Get the gvSIG Language */
		lang = this.getLanguage();
		tag = GEOCODINGELEMENTS + "_" + lang.trim();

		/* Create the geocoding folder in the persistence */
		this.createGeocoFolder();

		/* PERSISTENCE */
		PluginServices ps = PluginServices.getPluginServices(this);
		XMLEntity xml = ps.getPersistentXML();

		// TAG exists in the persistence
		if (xml.contains(tag)) {

			String nam = String.valueOf(xml.getStringProperty(tag));
			persistenceFile = new File(nam);
			BufferedReader br = null;
			String str = "";
			StringBuffer arr = new StringBuffer();

			try {
				reader = new FileReader(persistenceFile);
				br = new BufferedReader(reader);
				while ((str = br.readLine()) != null) {
					arr.append(str + "\n");
				}
			} catch (IOException e) {
				log.error("Reading the geocoding elements file");
				e.printStackTrace();
			}

			/* Set the name file and its contents */
			panel.setFileName(nam);
			panel.setFileText(arr.toString());
		}
		// TAG doesn´t exist in the persistence
		else {

			URL extensionURL = this.getClass().getClassLoader().getResource(
					TEMPLATES_FOLDER + "/geocoding_" + lang + ".txt");
			String path = extensionURL.getPath();

			String topath = geocoFolder + "geocoding_" + lang + ".txt";

			/* save in the persistence */
			xml.putProperty(tag, topath);
			ps.setPersistentXML(xml);

			/* Copy the address elements file in the persistence */
			FileChannel ic;
			try {
				ic = new FileInputStream(path).getChannel();

				FileChannel oc = new FileOutputStream(topath).getChannel();
				ic.transferTo(0, ic.size(), oc);
				ic.close();
				oc.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}

			BufferedReader br = null;
			persistenceFile = new File(topath);
			String str = "";
			StringBuffer arr = new StringBuffer();
			try {
				reader = new FileReader(persistenceFile);
				br = new BufferedReader(reader);
				while ((str = br.readLine()) != null) {
					arr.append(str + "\n");
				}
			} catch (IOException e) {
				log.error("Reading the geocoding elements file");
				e.printStackTrace();
			}
			/* Set the name file and its contents */
			panel.setFileName(topath);
			panel.setFileText(arr.toString());
		}

	}

	/**
	 * Store values in the persistence
	 */
	@Override
	public void storeValues() throws StoreException {

		String txt = panel.getFileText();
		/* Overwrite the text in the persistence file */
		if (txt.compareToIgnoreCase("") != 0) {

			try {
				writer = new FileWriter(persistenceFile);
				writer.write(txt);
				writer.close();

			} catch (IOException e) {
				log.error("Reading the geocoding elements file");
				e.printStackTrace();
			}
		}
	}

	/**
	 * Get Id
	 * 
	 * @return id
	 */
	public String getID() {
		return id;
	}

	/**
	 * Get panel
	 */
	public JPanel getPanel() {
		return this;
	}

	/**
	 * Get name panel
	 */
	public String getTitle() {
		return PluginServices.getText(this, "geocoding");

	}

	/**
	 * Initialize default values
	 */
	public void initializeDefaults() {

	}

	/**
	 * initialize values
	 */
	public void initializeValues() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.iver.andami.preferences.IPreference#getIcon()
	 */
	public ImageIcon getIcon() {
		return icon;
	}

	/**
	 * 
	 */
	public boolean isValueChanged() {
		return super.hasChanged();
	}

	/**
	 * 
	 */
	public void setChangesApplied() {
		setChanged(false);
	}

	/**
	 * Get the language of gvSIG
	 * 
	 * @return
	 */
	private String getLanguage() {

		ArrayList<Locale> myLocs = org.gvsig.i18n.Messages
				.getPreferredLocales();
		Locale myLoc = myLocs.size() == 0 ? Locale.ENGLISH : myLocs.get(0);
		String lang = myLoc.getLanguage().toLowerCase();
		return lang;
	}

	/**
	 * Create the geocoding persistence folder
	 */
	private void createGeocoFolder() {
		File geocoFile = new File(geocoPath);
		if (!geocoFile.exists()) {
			geocoFile.mkdir();
		}
	}

}
