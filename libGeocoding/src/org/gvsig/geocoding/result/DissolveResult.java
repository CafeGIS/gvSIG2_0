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

package org.gvsig.geocoding.result;

import java.util.List;

import org.gvsig.fmap.geom.Geometry;

/**
 * Class DissolveResult, This class has the new dissolved geometry and the
 * references of features dissolved
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 */

public interface DissolveResult {

	/**
	 * Get the list of the dissolved features references
	 * 
	 * @return
	 */
	public List<ScoredFeature> getScoredFeatures();

	/**
	 * Set list of dissolved features references
	 * 
	 * @param features
	 */
	public void setScoredFeatures(List<ScoredFeature> references);

	/**
	 * Get the dissolved geometry
	 * 
	 * @return
	 */
	public Geometry getGeom();

	/**
	 * Set the dissolve geometry behind the process
	 * 
	 * @param geoms
	 */
	public void setGeom(Geometry geom);

	/**
	 * Set the dissolve geometry behind the process
	 * 
	 * @param geoms
	 */
	public void setJTSGeom(com.vividsolutions.jts.geom.Geometry jtsGeom);

}
