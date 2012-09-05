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

package org.gvsig.geocoding.address.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.gvsig.geocoding.address.ComposedAddress;
import org.gvsig.geocoding.address.Literal;
import org.gvsig.tools.persistence.PersistenceException;
import org.gvsig.tools.persistence.PersistentState;

/**
 * ComposeAddress class implementation, this class has the main literal and one list of
 * intersection literals to complete the address
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 */

public class DefaultComposedAddress extends DefaultAddress implements ComposedAddress {

	private static final String INTERSECTIONLITERAL = "intersectionLiteral"; 
	private List<Literal> intersectionLiteral = new ArrayList<Literal>();

	/**
	 * default Constructor
	 */
	public DefaultComposedAddress() {
		super();
	}

	/**
	 * Constructor with main literal
	 * 
	 * @param main
	 */
	public DefaultComposedAddress(Literal main) {
		super(main);
	}

	/**
	 * Constructor with main literal and intersect literals
	 * 
	 * @param main
	 * @param intersectionListLiterals
	 */
	public DefaultComposedAddress(Literal main, List<Literal> intersectionListLiterals) {
		super(main);
		this.intersectionLiteral = intersectionListLiterals;
	}

	/**
	 * get list of intersection literals
	 * 
	 * @return
	 */
	public List<Literal> getIntersectionLiterals() {
		return intersectionLiteral;
	}

	/**
	 * Set list of the intersection literals
	 * 
	 * @param intersectionLiteral
	 */
	public void setIntersectionLiterals(List<Literal> intersectionLiteral) {
		this.intersectionLiteral = intersectionLiteral;
	}
	
	/**
	 * Saves the internal state of the object on the provided
	 * PersistentState object.
	 * 
	 * @param state
	 */
	public void saveToState(PersistentState state) throws PersistenceException {
		state.set(INTERSECTIONLITERAL, this.intersectionLiteral.iterator());
		
	}

	/**
	 * Set the state of the object from the state passed as parameter.
	 *
	 * @param state
	 */
	public void setState(PersistentState state) throws PersistenceException {		
		this.intersectionLiteral.clear();
		Iterator<Literal> it = state.getIterator(INTERSECTIONLITERAL);		
		while (it.hasNext()) {
			Literal lit = it.next();
			this.intersectionLiteral.add(lit);
		}	
		
		
	}

}
