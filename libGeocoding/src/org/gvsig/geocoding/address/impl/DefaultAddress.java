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

import org.gvsig.geocoding.address.Address;
import org.gvsig.geocoding.address.AddressComponent;
import org.gvsig.geocoding.address.Literal;
import org.gvsig.tools.persistence.PersistenceException;
import org.gvsig.tools.persistence.PersistentState;

/**
 * Address class implementation, this class has the elements to locate one place
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 */
public class DefaultAddress implements Address {

	private static final String MAINLITERAL = "mainliteral";
	private Literal mainLiteral = new DefaultLiteral();

	/**
	 * Default address constructor
	 * 
	 */
	public DefaultAddress() {

	}

	/**
	 * Constructor with literal parameter
	 * 
	 * @param main
	 */
	public DefaultAddress(Literal main) {
		this();
		this.mainLiteral = main;
	}

	/**
	 * Get the address main literal
	 * 
	 * @return
	 */
	public Literal getMainLiteral() {
		return mainLiteral;
	}

	/**
	 * set the address main literal
	 * 
	 * @param literal
	 */
	public void setMainLiteral(Literal literal) {
		this.mainLiteral = literal;
	}

	/**
	 * Add new component to address main literal
	 * 
	 * @param component
	 */
	public void addAddressComponent(AddressComponent component) {
		mainLiteral.add(component);
	}

	/**
	 * Clear all address main literal elements
	 */
	public void clearAddress() {
		mainLiteral.clear();
	}

	/**
	 * Compare this address with other address
	 */
	public int compareTo(Address address) {
		boolean equal = true;
		Literal literal = this.getMainLiteral();
		Literal otherLiteral = address.getMainLiteral();
		for (int i = 0; i < literal.size(); i++) {
			DefaultAddressComponent comp = (DefaultAddressComponent) literal
					.get(i);
			DefaultAddressComponent othercomp = (DefaultAddressComponent) otherLiteral
					.get(i);
			String value = comp.getValue();
			String othervalue = othercomp.getValue();
			if (value.compareTo(othervalue) != 0) {
				equal = false;
			}
		}
		if (equal) {
			return 1;
		} else
			return -1;
	}

	/**
	 * Saves the internal state of the object on the provided PersistentState
	 * object.
	 * 
	 * @param state
	 */
	public void saveToState(PersistentState state) throws PersistenceException {
		state.set(MAINLITERAL, this.mainLiteral);

	}

	/**
	 * Set the state of the object from the state passed as parameter.
	 * 
	 * @param state
	 */
	public void loadFromState(PersistentState state)
			throws PersistenceException {
		this.mainLiteral = (Literal) state.get(MAINLITERAL);
		
	}
}
