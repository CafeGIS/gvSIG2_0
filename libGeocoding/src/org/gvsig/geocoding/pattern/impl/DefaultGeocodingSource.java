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
 * 2009 Prodevelop S.L. main development
 */

package org.gvsig.geocoding.pattern.impl;

import org.gvsig.fmap.dal.DataStore;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.geocoding.pattern.GeocodingSource;
import org.gvsig.geocoding.styles.AbstractGeocodingStyle;
import org.gvsig.tools.persistence.PersistenceException;
import org.gvsig.tools.persistence.PersistentState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * GeocodingSource class implementation. This class has the geocoding style and the FeatureStore of the
 * data source
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 */
public class DefaultGeocodingSource implements GeocodingSource {

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory
			.getLogger(DefaultGeocodingSource.class);

	private static final String STYLE = "style";
	private static final String LAYERSOURCE = "layersource";

	private AbstractGeocodingStyle style;
	private DataStore layerSource;

	/**
	 * Default constructor
	 */
	public DefaultGeocodingSource() {

	}

	/**
	 * Constructor with parameters
	 * 
	 * @param _style
	 * @param _layerSource
	 */
	public DefaultGeocodingSource(AbstractGeocodingStyle _style,
			FeatureStore _layerSource) {
		this.style = _style;
		this.layerSource = _layerSource;
	}

	/**
	 * Get the style
	 * 
	 * @return
	 */
	public AbstractGeocodingStyle getStyle() {
		return style;
	}

	/**
	 * Set the style
	 * 
	 * @param style
	 */
	public void setStyle(AbstractGeocodingStyle style) {
		this.style = style;
	}

	/**
	 * Get layer source
	 * 
	 * @return
	 */
	public DataStore getLayerSource() {
		return layerSource;
	}

	/**
	 * Set layer source
	 * 
	 * @param layerSource
	 */
	public void setLayerSource(DataStore layerSource) {
		this.layerSource = layerSource;
	}

	/**
	 * Set the state of the GeocodingSource from the state passed as parameter.
	 * 
	 * @param state
	 */
	public void loadFromState(PersistentState state) throws PersistenceException {
		this.layerSource = (FeatureStore) state.get(LAYERSOURCE);
		this.style = (AbstractGeocodingStyle) state.get(STYLE);
	}

	/**
	 * Saves the internal state of the object on the provided PersistentState
	 * object.
	 * 
	 * @param state
	 */
	public void saveToState(PersistentState state) throws PersistenceException {
		state.set(LAYERSOURCE, this.layerSource);
		state.set(STYLE, this.style);

	}


}
