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
 * 2008 PRODEVELOP		Main development
 */

/**
 * 
 */
package org.gvsig.geocoding.styles;

import java.util.List;
import java.util.Set;

import org.gvsig.geocoding.address.Address;
import org.gvsig.geocoding.address.Literal;
import org.gvsig.geocoding.result.GeocodingResult;
import org.gvsig.geocoding.result.ScoredFeature;
import org.gvsig.tools.persistence.PersistenceException;
import org.gvsig.tools.persistence.Persistent;
import org.gvsig.tools.persistence.PersistentState;

/**
 * Style of geocoding
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 */

public abstract class AbstractGeocodingStyle implements Persistent {

	private static final String RELATIONSLITERAL = "relationsliteral";

	private Literal relationsLiteral = null;

	/**
	 * Get style relation literal
	 * 
	 * @return
	 */
	public Literal getRelationsLiteral() {
		return relationsLiteral;
	}

	/**
	 * set style relation literal
	 * 
	 * @param name
	 */
	public void setRelationsLiteral(Literal name) {
		this.relationsLiteral = name;
	}

	/**
	 * Spatial search over the geometries of the selected features
	 * 
	 * @param lists
	 *            list of lists with ScoredFeatures
	 * @param address
	 * @return
	 */
	public abstract Set<GeocodingResult> match(List<List<ScoredFeature>> lists,
			Address address);

	/**
	 * Saves the internal state of the object on the provided PersistentState
	 * object.
	 * 
	 * @param state
	 */
	public void saveToState(PersistentState state) throws PersistenceException {
		state.set(RELATIONSLITERAL, this.relationsLiteral.iterator());
	}

	/**
	 * Set the state of the object from the state passed as parameter.
	 * 
	 * @param state
	 */
	public void setState(PersistentState state) throws PersistenceException {
		this.relationsLiteral = (Literal) state.get(RELATIONSLITERAL);
	}

}
