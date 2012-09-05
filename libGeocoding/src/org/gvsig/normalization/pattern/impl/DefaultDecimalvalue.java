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

import org.gvsig.normalization.pattern.Decimalvalue;
import org.gvsig.tools.persistence.PersistenceException;
import org.gvsig.tools.persistence.PersistentState;

/**
 * Class Decimalvalue.
 * 
 * This class defines the new field type like Decimal
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 */
public class DefaultDecimalvalue implements Decimalvalue {


	/**
	 * width of the integer part
	 */
	private int _decimalvalueint;

	/**
	 * width of the fractional part, precision
	 */
	private int _decimalvaluedec;

	/**
	 * Constructor
	 */
	public DefaultDecimalvalue() {
		super();
	}

	/**
	 * Returns the value of field 'decimalvaluedec'.
	 * 
	 * @return the value of field 'decimalvaluedec'.
	 */
	public int getDecimalvaluedec() {
		return this._decimalvaluedec;
	}

	/**
	 * Returns the value of field 'decimalvalueint'.
	 * 
	 * @return the value of field 'decimalvalueint'.
	 */
	public int getDecimalvalueint() {
		return this._decimalvalueint;
	}

	/**
	 * Sets the value of field 'decimalvaluedec'.
	 * 
	 * @param decimalvaluedec
	 *            the value of field 'decimalvaluedec'.
	 */
	public void setDecimalvaluedec(int decimalvaluedec) {
		this._decimalvaluedec = decimalvaluedec;
	}

	/**
	 * Sets the value of field 'decimalvalueint'.
	 * 
	 * @param decimalvalueint
	 *            the value of field 'decimalvalueint'.
	 */
	public void setDecimalvalueint(int decimalvalueint) {
		this._decimalvalueint = decimalvalueint;
	}

	/**
	 * Saves the internal state of the object on the provided
	 * PersistentState object.
	 * 
	 * @param state
	 */
	public void saveToState(PersistentState state) throws PersistenceException{
		state.set("decimalvalueint", this._decimalvalueint);
		state.set("decimalvaluedec", this._decimalvaluedec);
	}

	/**
	 * Set the state of the object from the state passed as parameter.
	 *
	 * @param state
	 */
	public void loadFromState(PersistentState state) throws PersistenceException{
		this._decimalvalueint = state.getInt("decimalvalueint");
		this._decimalvaluedec = state.getInt("decimalvaluedec");
	}

}
