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

import org.gvsig.normalization.pattern.Stringvalue;
import org.gvsig.tools.persistence.PersistenceException;
import org.gvsig.tools.persistence.PersistentState;

/**
 * This class defines the new field type like String
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 */
public class DefaultStringvalue implements Stringvalue {

	/**
	 * Width of the new String field
	 */
	private int _stringvaluewidth;

	/**
	 * Constructor
	 */
	public DefaultStringvalue() {
		super();
	}

	/**
	 * Returns the value of field 'stringvaluewidth'.
	 * 
	 * @return the value of field 'stringvaluewidth'.
	 */
	public int getStringvaluewidth() {
		return this._stringvaluewidth;
	}

	/**
	 * Sets the value of field 'stringvaluewidth'.
	 * 
	 * @param stringvaluewidth
	 *            the value of field 'stringvaluewidth'
	 */
	public void setStringvaluewidth(int stringvaluewidth) {
		this._stringvaluewidth = stringvaluewidth;
	}

	/**
	 * Saves the internal state of the object on the provided PersistentState
	 * object.
	 * 
	 * @param state
	 */
	public void saveToState(PersistentState state) throws PersistenceException {
		state.set("stringvaluewidth", this._stringvaluewidth);
	}

	/**
	 * Set the state of the object from the state passed as parameter.
	 * 
	 * @param state
	 */
	public void loadFromState(PersistentState state)
			throws PersistenceException {
		this._stringvaluewidth = state.getInt("stringvaluewidth");
	}

}
