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

package org.gvsig.geocoding.pattern.impl;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import org.gvsig.geocoding.pattern.GeocodingSettings;
import org.gvsig.geocoding.pattern.GeocodingSource;
import org.gvsig.geocoding.pattern.Patterngeocoding;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.persistence.PersistenceException;
import org.gvsig.tools.persistence.PersistenceManager;
import org.gvsig.tools.persistence.PersistentState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  Patterngeocoding class implementation.
 * 
 * This class is the geocoding pattern. This pattern has your name (name), the
 * settings (settings) and the data source (source)
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 */
public class DefaultPatterngeocoding implements Patterngeocoding {

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory
			.getLogger(DefaultPatterngeocoding.class);

	private static final String PATTERNNAME = "gpatternname";
	private static final String SOURCE = "gsource";
	private static final String SETTINGS = "gsettings";

	private String name;
	private GeocodingSettings settings;
	private GeocodingSource source;

	/**
	 * Default constructor
	 */
	public DefaultPatterngeocoding() {
		this.setSource(new DefaultGeocodingSource());
		this.setSettings(new DefaultGeocodingSettings());
	}

	/**
	 * Constructor with parameters
	 */
	public DefaultPatterngeocoding(GeocodingSource source,
			GeocodingSettings settings) {
		this.setSource(source);
		this.setSettings(settings);
	}

	/**
	 * Constructor with pattern name
	 * 
	 * @param name
	 */
	public DefaultPatterngeocoding(String name) {
		this();
		this.setPatternName(name);
	}

	/**
	 * Returns the value of field 'patternname'.
	 * 
	 * @return name
	 */
	public String getPatternname() {
		return this.name;
	}

	/**
	 * Returns the value of field 'Settings'.
	 * 
	 * @return settings
	 */
	public GeocodingSettings getSettings() {
		return this.settings;
	}

	/**
	 * Returns the value of field 'source'.
	 * 
	 * @return source.
	 */
	public GeocodingSource getSource() {
		return this.source;
	}

	/**
	 * Sets the value of field 'settings'.
	 * 
	 * @param settings
	 */
	public void setSettings(GeocodingSettings _settings) {
		this.settings = _settings;
	}

	/**
	 * Sets the value of field 'patternname'.
	 * 
	 * @param name
	 * 
	 */
	public void setPatternName(String _name) {
		this.name = _name;
	}

	/**
	 * Sets the value of field 'source'.
	 * 
	 * @param source
	 *            the value of field 'source'.
	 */
	public void setSource(GeocodingSource _source) {
		this.source = _source;
	}

	/**
	 * Save the pattern to XML file
	 * 
	 * @param file
	 * @throws PersistenceException
	 * @throws IOException
	 */
	public void saveToXML(File file) throws PersistenceException, IOException {

		PersistenceManager manager = ToolsLocator.getPersistenceManager();
		
		Writer writer = new FileWriter(file);
		PersistentState state = manager.getState(this);
		state.save(writer);
		writer.close();
	}

	/**
	 * Load the pattern from XML file
	 * 
	 * @param reader
	 * @throws PersistenceException
	 * @throws IOException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void loadFromXML(File file) throws PersistenceException, IOException {
		PersistenceManager manager = ToolsLocator.getPersistenceManager();
		Reader reader = new FileReader(file);
		PersistentState state = manager.loadState(reader);
		this.loadFromState(state);
		reader.close();
	}

	/**
	 * Set the state of the pattern from the state passed as parameter.
	 * 
	 * @param state
	 */
	public void loadFromState(PersistentState state) throws PersistenceException {
		this.name = ((String) state.get(PATTERNNAME)).trim();
		this.source = (GeocodingSource) state.get(SOURCE);
		this.settings = (GeocodingSettings) state.get(SETTINGS);
	}

	/**
	 * Saves the internal state of the object on the provided PersistentState
	 * object.
	 * 
	 * @param state
	 */
	public void saveToState(PersistentState state) throws PersistenceException {
		state.set(PATTERNNAME, this.name);
		state.set(SOURCE, this.source);
		state.set(SETTINGS, this.settings);
	}

}
