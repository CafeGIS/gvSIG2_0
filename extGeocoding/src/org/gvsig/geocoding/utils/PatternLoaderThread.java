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
 * 2009 Prodevelop S.L  vsanjaime   programador
 */

package org.gvsig.geocoding.utils;

import java.io.File;

import javax.swing.JOptionPane;

import org.gvsig.fmap.dal.DataStore;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.geocoding.pattern.Patterngeocoding;
import org.gvsig.geocoding.pattern.impl.DefaultPatterngeocoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iver.andami.PluginServices;

/**
 * This internal class loads the pattern
 * 
 * @author Jorge Gaspar Sanz Salinas (jsanz@prodevelop.es)
 * @author Vicente Sanjaime Calvet (vsanjaime@prodevelop.es)
 * 
 */

public class PatternLoaderThread extends Thread {

	private static final Logger log = LoggerFactory
			.getLogger(PatternLoaderThread.class);
	private File file = null;
	private boolean ok = true;
	private Patterngeocoding pattern = null;

	/**
	 * Constructor
	 */
	public PatternLoaderThread() {
		super();
	}

	/**
	 * Constructor with file
	 * 
	 * @param file
	 */
	public PatternLoaderThread(File file) {
		this();
		this.file = file;
	}

	/**
	 * run
	 */
	public void run() {
		pattern = new DefaultPatterngeocoding();
		if (this.file != null) {
			try {
				pattern.loadFromXML(this.file);
			} catch (Exception e) {
				this.ok = false;
				String mes = PluginServices.getText(this,
						"geocoerrorreadingfile");
				String tit = PluginServices.getText(this, "geocoding");
				JOptionPane.showMessageDialog(null, mes, tit,
						JOptionPane.ERROR_MESSAGE);
				log.error("Error parsing the XML Pattern", e);
				file.delete();
			}
			if (ok) {

				String message = PluginServices.getText(null, "loadlayer");
				String title = PluginServices.getText(null, "geocoding");
				int i = JOptionPane.showConfirmDialog(null, message, title,
						JOptionPane.YES_NO_OPTION);
				if (i == 0) {
					DataStore store = pattern.getSource().getLayerSource();

					// TODO CREATE THE LAYER from store

					// TODO add the layer to the view
					// if (xml != null) {
					// View view = GeocoUtils.getCurrentView();
					// if (view != null) {
					// FLayers lyrs = project.addDocument(table);
					// .getLayers();
					// try {
					// lyrs.setXMLEntity(xml);
					//												
					// } catch (XMLException e) {
					// log.error("Loading the layer");
					// }
					//
					// }
					// }

				}

			}
		}
	}

	/**
	 * @return the apto
	 */
	public boolean isOk() {
		return ok;
	}

	/**
	 * @return the file
	 */
	public File getFile() {
		return file;
	}

	/**
	 * @return the pattern
	 */
	public Patterngeocoding getPattern() {
		return pattern;
	}
}
