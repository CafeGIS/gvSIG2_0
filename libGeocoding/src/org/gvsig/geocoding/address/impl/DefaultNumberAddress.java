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

import org.gvsig.geocoding.address.Literal;
import org.gvsig.geocoding.address.NumberAddress;
import org.gvsig.tools.persistence.PersistenceException;
import org.gvsig.tools.persistence.PersistentState;

/**
 * NumberAddress class implementation, this class has the literal of address, plus the address
 * number
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 */

public class DefaultNumberAddress extends DefaultAddress implements NumberAddress {

	private static final String NUMBER = "number";
	private int number;

	/**
	 * default Constructor
	 * 
	 */
	public DefaultNumberAddress() {
		super();
	}

	/**
	 * Constructor with main literal
	 * 
	 * @param main
	 */
	public DefaultNumberAddress(Literal main) {
		super(main);

	}

	/**
	 * Constructor with main literal
	 * 
	 * @param main
	 */
	public DefaultNumberAddress(Literal main, int number) {
		super(main);
		this.number = number;

	}

	/**
	 * Get number
	 * 
	 * @return
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * set number
	 * 
	 * @param number
	 */
	public void setNumber(int number) {
		this.number = number;
	}
	
	/**
	 * Saves the internal state of the object on the provided
	 * PersistentState object.
	 * 
	 * @param state
	 */
	public void saveToState(PersistentState state) throws PersistenceException {
		state.set(NUMBER, this.number);
		
	}

	/**
	 * Set the state of the object from the state passed as parameter.
	 *
	 * @param state
	 */
	public void setState(PersistentState state) throws PersistenceException {
		this.number = state.getInt(NUMBER);
		
	}
}
