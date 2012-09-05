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

package org.gvsig.geocoding.pattern;

import java.io.File;
import java.io.IOException;

import org.gvsig.tools.persistence.PersistenceException;
import org.gvsig.tools.persistence.Persistent;

/**
 * Class Patterngeocoding.
 * 
 * This class is the geocoding pattern. This pattern has your name (name), the
 * settings (settings) and the data source (source)
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 */
public interface Patterngeocoding extends Persistent {

	/**
	 * Returns the value of field 'patternname'.
	 * 
	 * @return name
	 */
	public String getPatternname();

	/**
	 * Returns the value of field 'Settings'.
	 * 
	 * @return settings
	 */
	public GeocodingSettings getSettings();

	/**
	 * Returns the value of field 'source'.
	 * 
	 * @return source.
	 */
	public GeocodingSource getSource();

	/**
	 * Sets the value of field 'settings'.
	 * 
	 * @param settings
	 */
	public void setSettings(GeocodingSettings _settings);

	/**
	 * Sets the value of field 'patternname'.
	 * 
	 * @param name
	 * 
	 */
	public void setPatternName(String _name);

	/**
	 * Sets the value of field 'source'.
	 * 
	 * @param source
	 *            the value of field 'source'.
	 */
	public void setSource(GeocodingSource _source);

	/**
	 * Save the pattern to XML file
	 * 
	 * @param file
	 * @throws PersistenceException
	 * @throws IOException
	 */
	public void saveToXML(File file) throws PersistenceException, IOException;

	/**
	 * Load the pattern from XML file
	 * 
	 * @param reader
	 * @throws PersistenceException
	 * @throws IOException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void loadFromXML(File file) throws PersistenceException, IOException;

}
