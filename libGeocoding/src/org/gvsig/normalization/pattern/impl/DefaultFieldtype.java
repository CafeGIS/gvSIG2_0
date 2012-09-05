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




import org.gvsig.normalization.pattern.Datevalue;
import org.gvsig.normalization.pattern.Decimalvalue;
import org.gvsig.normalization.pattern.Fieldtype;
import org.gvsig.normalization.pattern.Integervalue;
import org.gvsig.normalization.pattern.Stringvalue;
import org.gvsig.tools.persistence.PersistenceException;
import org.gvsig.tools.persistence.PersistentState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class defines the type of the one new field
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 */
public class DefaultFieldtype implements Fieldtype {
	
	private static final Logger log = LoggerFactory
	.getLogger(DefaultFieldtype.class);

	/**
	 * new field of type String
	 */
	private Stringvalue _stringvalue;

	/**
	 * new field of type Date
	 */
	private Datevalue _datevalue;

	/**
	 * new field of type Decimal
	 */
	private Decimalvalue _decimalvalue;

	/**
	 * new field of type Integer
	 */
	private Integervalue _integervalue;

	/**
	 * Constructor
	 */
	public DefaultFieldtype() {
		super();
	}

	/**
	 * @return the value of field 'datevalue'.
	 */
	public Datevalue getDatevalue() {
		return this._datevalue;
	}

	/**
	 * @return the value of field 'decimalvalue'.
	 */
	public Decimalvalue getDecimalvalue() {
		return this._decimalvalue;
	}

	/**
	 * @return the value of field 'integervalue'.
	 */
	public Integervalue getIntegervalue() {
		return this._integervalue;
	}

	/**
	 * @return the value of field 'stringvalue'.
	 */
	public Stringvalue getStringvalue() {
		return this._stringvalue;
	}

	/**
	 * Sets the value of field 'datevalue'.
	 * 
	 * @param datevalue
	 *            the value of field 'datevalue'.
	 */
	public void setDatevalue(Datevalue datevalue) {
		this._datevalue = datevalue;
	}

	/**
	 * Sets the value of field 'decimalvalue'.
	 * 
	 * @param decimalvalue
	 *            the value of field 'decimalvalue'.
	 */
	public void setDecimalvalue(Decimalvalue decimalvalue) {
		this._decimalvalue = decimalvalue;
	}

	/**
	 * Sets the value of field 'integervalue'.
	 * 
	 * @param integervalue
	 *            the value of field 'integervalue'.
	 */
	public void setIntegervalue(Integervalue integervalue) {
		this._integervalue = integervalue;
	}

	/**
	 * Sets the value of field 'stringvalue'.
	 * 
	 * @param stringvalue
	 *            the value of field 'stringvalue'.
	 */
	public void setStringvalue(Stringvalue stringvalue) {
		this._stringvalue = stringvalue;
	}

	
	
	/**
	 * Saves the internal state of the object on the provided
	 * PersistentState object.
	 * 
	 * @param state
	 */
	public void saveToState(PersistentState state) throws PersistenceException{		
		if (this._stringvalue != null) {
			state.set("stringvalue", this._stringvalue);
		} else if (this._datevalue != null) {
			state.set("datevalue", this._datevalue);
		} else if (this._decimalvalue != null) {
			state.set("decimalvalue", this._decimalvalue);
		} else {
			state.set("integervalue", this._integervalue);
		}
	}

	/**
	 * Set the state of the object from the state passed as parameter.
	 *
	 * @param state
	 */
	public void loadFromState(PersistentState state) throws PersistenceException{
		try {
			if (state.get("stringvalue") != null) {
				this._stringvalue = (Stringvalue) state
						.get("stringvalue");
			} else if (state.get("datevalue") != null) {
				this._datevalue = (Datevalue) state.get("datevalue");
			} else if (state.get("decimalvalue") != null) {
				this._decimalvalue = (Decimalvalue) state
						.get("decimalvalue");
			} else {
				this._integervalue = (Integervalue) state
						.get("integervalue");
			}
		} catch (Exception e) {
			log.error("Error parsing the object",e);
		} 
	}

}
