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

package org.gvsig.normalization.pattern.impl;

import org.gvsig.normalization.pattern.Integervalue;
import org.gvsig.tools.persistence.PersistenceException;
import org.gvsig.tools.persistence.PersistentState;

/**
 * This class defines the new field type like Integer
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 */
public class DefaultIntegervalue implements Integervalue {

	/**
	 * width of the new Integer field
	 */
	private int _integervaluewidth;

	/**
	 * Constructor
	 */
	public DefaultIntegervalue() {
		super();
	}

	/**
	 * Returns the value of field 'integervaluewidth'.
	 * 
	 * @return the value of field 'integervaluewidth'.
	 */
	public int getIntegervaluewidth() {
		return this._integervaluewidth;
	}

	/**
	 * Sets the value of field 'integervaluewidth'.
	 * 
	 * @param integervaluewidth
	 *            the value of field 'integervaluewidth'.
	 */
	public void setIntegervaluewidth(int integervaluewidth) {
		this._integervaluewidth = integervaluewidth;
	}

	/**
	 * Set the state of the object from the state passed as parameter.
	 * 
	 * @param state
	 */
	public void loadFromState(PersistentState state)
			throws PersistenceException {
		this._integervaluewidth = state.getInt("integervaluewidth");

	}

	/**
	 * Saves the internal state of the object on the provided PersistentState
	 * object.
	 * 
	 * @param state
	 */
	public void saveToState(PersistentState state) throws PersistenceException {
		state.set("integervaluewidth", this._integervaluewidth);
	}

}
