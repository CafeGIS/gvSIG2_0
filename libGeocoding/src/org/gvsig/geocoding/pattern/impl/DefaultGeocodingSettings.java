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

import org.gvsig.geocoding.pattern.GeocodingSettings;
import org.gvsig.tools.persistence.PersistenceException;
import org.gvsig.tools.persistence.PersistentState;

/**
 * Settings class implementation. This class has the parameters that define the
 * quality of search and the maximun number of results showed in the panel.
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 */
public class DefaultGeocodingSettings implements GeocodingSettings {

	private static final String SCORE = "score";
	private static final String RESULTSNUMBER = "resultsnumber";

	private int resultsNumber;
	private double score;

	/**
	 * Default Constructor with parameters initializated.
	 */
	public DefaultGeocodingSettings() {

		this.resultsNumber = 10;
		this.score = 60.0;
	}

	/**
	 * Constructor with parameters
	 */
	public DefaultGeocodingSettings(int results, double score) {
		this.resultsNumber = results;
		this.score = score;
	}

	/**
	 * @return the results
	 */
	public int getResultsNumber() {
		return resultsNumber;
	}

	/**
	 * @param results
	 *            the results to set
	 */
	public void setResultsNumber(int results) {
		this.resultsNumber = results;
	}

	/**
	 * @return the quality
	 */
	public double getScore() {
		return this.score;
	}

	/**
	 * @param quality
	 *            the quality to set
	 */
	public void setScore(double score) {
		this.score = score;
	}	

	/**
	 * Saves the internal state of the object on the provided PersistentState
	 * object.
	 * 
	 * @param state
	 */
	public void saveToState(PersistentState state) throws PersistenceException {
		state.set(RESULTSNUMBER, this.resultsNumber);
		state.set(SCORE, this.score);

	}

	/**
	 * Set the state of the settings from the state passed as parameter.
	 * 
	 * @param state
	 */
	public void loadFromState(PersistentState state)
			throws PersistenceException {
		this.resultsNumber = state.getInt(RESULTSNUMBER);
		this.score = state.getInt(SCORE);
		
	}

}
