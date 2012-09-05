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

package org.gvsig.geocoding.pattern;

import org.gvsig.fmap.dal.DataStore;
import org.gvsig.geocoding.styles.AbstractGeocodingStyle;
import org.gvsig.tools.persistence.Persistent;

/**
 * Interface GeocodingSource. This class has the geocoding style and the
 * FeatureStore of the data source
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 */
public interface GeocodingSource extends Persistent {

	/**
	 * Get the style
	 * 
	 * @return
	 */
	public AbstractGeocodingStyle getStyle();

	/**
	 * Set the style
	 * 
	 * @param style
	 */
	public void setStyle(AbstractGeocodingStyle style);

	/**
	 * Get layer source
	 * 
	 * @return
	 */
	public DataStore getLayerSource();

	/**
	 * Set layer source
	 * 
	 * @param layerSource
	 */
	public void setLayerSource(DataStore layerSource);
}
