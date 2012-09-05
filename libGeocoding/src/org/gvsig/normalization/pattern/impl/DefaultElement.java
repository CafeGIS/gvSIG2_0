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

import org.gvsig.normalization.pattern.Element;
import org.gvsig.normalization.pattern.Fieldseparator;
import org.gvsig.normalization.pattern.Fieldtype;
import org.gvsig.normalization.pattern.Infieldseparators;
import org.gvsig.tools.persistence.PersistenceException;
import org.gvsig.tools.persistence.PersistentState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class Element.
 * 
 * This class are the all elements that they make the pattern. Each element
 * defines one new field in final table. The attributes of each element are: -
 * (_fieldname) Name of the new field - (_fieldtype) Type of the new field
 * (String, Integer, Decimal or Date) - (_fieldwith) Number of position to split
 * the main string. If this value is zero the split process will be via
 * separators - (_fieldseparator) separators between fields -
 * (infieldseparators) special characters within one substring (thousand
 * character, decimal character, text characters) - (_importfield) this boolean
 * defines if this new field will be normalized
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 */
public class DefaultElement implements Element {

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory
			.getLogger(DefaultElement.class);

	/**
	 * Name of the new field
	 */
	private String _fieldname;

	/**
	 * Type of the field in the new table
	 */
	private Fieldtype _fieldtype;

	/**
	 * number of positions to split the main string
	 */
	private int _fieldwidth;

	/**
	 * separators (characters) to split the main string
	 */
	private Fieldseparator _fieldseparator;

	/**
	 * characters of thousands, decimals and text
	 */
	private Infieldseparators _infieldseparators;

	/**
	 * this value says if the new field will be normalized and it will insert in
	 * the final table
	 */
	private boolean _importfield;

	/**
	 * Constructor
	 */
	public DefaultElement() {
		super();
	}

	/**
	 * Returns the value of field 'fieldname'.
	 * 
	 * @return the value of field 'fieldname'.
	 */
	public String getFieldname() {
		return this._fieldname;
	}

	/**
	 * Returns the value of field 'fieldseparator'.
	 * 
	 * @return the value of field 'fieldseparator'.
	 */
	public Fieldseparator getFieldseparator() {
		return this._fieldseparator;
	}

	/**
	 * Returns the value of field 'fieldtype'.
	 * 
	 * @return the value of field 'fieldtype'.
	 */
	public Fieldtype getFieldtype() {
		return this._fieldtype;
	}

	/**
	 * Returns the value of field 'fieldwidth'.
	 * 
	 * @return the value of field 'fieldwidth'.
	 */
	public int getFieldwidth() {
		return this._fieldwidth;
	}

	/**
	 * Returns the value of field 'importfield'.
	 * 
	 * @return the value of field 'importfield'.
	 */
	public boolean getImportfield() {
		return this._importfield;
	}

	/**
	 * Returns the value of field 'infieldseparators'.
	 * 
	 * @return the value of field 'infieldseparators'.
	 */
	public Infieldseparators getInfieldseparators() {
		return this._infieldseparators;
	}

	/**
	 * Sets the value of field 'fieldname'.
	 * 
	 * @param fieldname
	 *            the value of field 'fieldname'.
	 */
	public void setFieldname(String fieldname) {
		this._fieldname = fieldname;
	}

	/**
	 * Sets the value of field 'fieldseparator'.
	 * 
	 * @param fieldseparator
	 *            the value of field 'fieldseparator'.
	 */
	public void setFieldseparator(Fieldseparator fieldseparator) {
		this._fieldseparator = fieldseparator;
	}

	/**
	 * Sets the value of field 'fieldtype'.
	 * 
	 * @param fieldtype
	 *            the value of field 'fieldtype'.
	 */
	public void setFieldtype(Fieldtype fieldtype) {
		this._fieldtype = fieldtype;
	}

	/**
	 * Sets the value of field 'fieldwidth'.
	 * 
	 * @param fieldwidth
	 *            the value of field 'fieldwidth'.
	 */
	public void setFieldwidth(int fieldwidth) {
		this._fieldwidth = fieldwidth;
	}

	/**
	 * Sets the value of field 'importfield'.
	 * 
	 * @param importfield
	 *            the value of field 'importfield'.
	 */
	public void setImportfield(boolean importfield) {
		this._importfield = importfield;
	}

	/**
	 * Sets the value of field 'infieldseparators'.
	 * 
	 * @param infieldseparators
	 *            the value of field 'infieldseparators'.
	 */
	public void setInfieldseparators(Infieldseparators infieldseparators) {
		this._infieldseparators = infieldseparators;
	}

	/**
	 * toString
	 */
	public String toString() {
		return this.getFieldname();

	}

	/**
	 * Saves the internal state of the object on the provided PersistentState
	 * object.
	 * 
	 * @param state
	 */
	public void saveToState(PersistentState state) throws PersistenceException {
		state.set("fieldname", this._fieldname);
		state.set("fieldtype", this._fieldtype);
		state.set("fieldwidth", this._fieldwidth);
		state.set("fieldseparator", this._fieldseparator);
		state.set("infieldseparators", this._infieldseparators);
		state.set("importfield", this._importfield);
	}

	/**
	 * Set the state of the object from the state passed as parameter.
	 * 
	 * @param state
	 */
	public void loadFromState(PersistentState state)
			throws PersistenceException {
		this._fieldname = state.getString("fieldname");
		this._fieldwidth = state.getInt("fieldwidth");
		this._importfield = state.getBoolean("importfield");
		this._fieldtype = (Fieldtype) state.get("fieldtype");
		this._fieldseparator = (Fieldseparator) state.get("fieldseparator");
		this._infieldseparators = (Infieldseparators) state
				.get("infieldseparators");
	}

}
