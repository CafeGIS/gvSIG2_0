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


import org.gvsig.normalization.pattern.Fieldseparator;
import org.gvsig.tools.persistence.PersistenceException;
import org.gvsig.tools.persistence.PersistentState;
import org.htmlparser.util.Translate;

/**
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 * 
 */

/**
 * Class Fieldseparator.
 * 
 * This class has the separators between two fields and they are necessary for
 * the split string process
 */
public class DefaultFieldseparator implements Fieldseparator {

	/**
	 * Split string by tab
	 */
	private boolean _tabsep;

	/**
	 * Split string by one space
	 */
	private boolean _spacesep;

	/**
	 * Split string by colon
	 */
	private boolean _colonsep;

	/**
	 * Split string by semicolon
	 */
	private boolean _semicolonsep;

	/**
	 * Other characters to split strings
	 */
	private String _othersep;

	/**
	 * Take consecutive separators like one
	 */
	private boolean _joinsep;

	/**
	 * Constructor
	 */
	public DefaultFieldseparator() {
		super();
	}

	/**
	 * Returns the value of field 'colonsep'.
	 * 
	 * @return the value of field 'colonsep'.
	 */
	public boolean getColonsep() {
		return this._colonsep;
	}

	/**
	 * Returns the value of field 'joinsep'.
	 * 
	 * @return the value of field 'joinsep'.
	 */
	public boolean getJoinsep() {
		return this._joinsep;
	}

	/**
	 * Returns the value of field 'othersep'.
	 * 
	 * @return the value of field 'othersep'.
	 */
	public java.lang.String getOthersep() {
		return this._othersep;
	}

	/**
	 * Returns the value of field 'semicolonsep'.
	 * 
	 * @return the value of field 'semicolonsep'.
	 */
	public boolean getSemicolonsep() {
		return this._semicolonsep;
	}

	/**
	 * Returns the value of field 'spacesep'.
	 * 
	 * @return the value of field 'spacesep'.
	 */
	public boolean getSpacesep() {
		return this._spacesep;
	}

	/**
	 * Returns the value of field 'tabsep'.
	 * 
	 * @return the value of field 'tabsep'.
	 */
	public boolean getTabsep() {
		return this._tabsep;
	}

	/**
	 * Sets the value of field 'colonsep'.
	 * 
	 * @param colonsep
	 *            the value of field 'colonsep'.
	 */
	public void setColonsep(boolean colonsep) {
		this._colonsep = colonsep;
	}

	/**
	 * Sets the value of field 'joinsep'.
	 * 
	 * @param joinsep
	 *            the value of field 'joinsep'.
	 */
	public void setJoinsep(boolean joinsep) {
		this._joinsep = joinsep;
	}

	/**
	 * Sets the value of field 'othersep'.
	 * 
	 * @param othersep
	 *            the value of field 'othersep'.
	 */
	public void setOthersep(java.lang.String othersep) {
		this._othersep = othersep;
	}

	/**
	 * Sets the value of field 'semicolonsep'.
	 * 
	 * @param semicolonsep
	 *            the value of field 'semicolonsep'.
	 */
	public void setSemicolonsep(boolean semicolonsep) {
		this._semicolonsep = semicolonsep;
	}

	/**
	 * Sets the value of field 'spacesep'.
	 * 
	 * @param spacesep
	 *            the value of field 'spacesep'.
	 */
	public void setSpacesep(boolean spacesep) {
		this._spacesep = spacesep;
	}

	/**
	 * Sets the value of field 'tabsep'.
	 * 
	 * @param tabsep
	 *            the value of field 'tabsep'.
	 */
	public void setTabsep(boolean tabsep) {
		this._tabsep = tabsep;
	}	
		
	/**
	 * Saves the internal state of the object on the provided
	 * PersistentState object.
	 * 
	 * @param state
	 */
	public void saveToState(PersistentState state) throws PersistenceException{
		
		state.set("tabsep", this._tabsep);
		state.set("spacesep", this._spacesep);
		state.set("colonsep", this._colonsep);
		state.set("semicolonsep", this._semicolonsep);
		String ot = (String) this._othersep;
		if(ot != null){
			String oth = Translate.encode(ot);
			state.set("othersep", oth);
		}else{
			state.set("othersep", "");
		}
		
		state.set("joinsep", this._joinsep);
	}

	/**
	 * Set the state of the object from the state passed as parameter.
	 *
	 * @param state
	 */
	public void loadFromState(PersistentState state) throws PersistenceException{
		this._tabsep = state.getBoolean("tabsep");
		this._spacesep = state.getBoolean("spacesep");
		this._colonsep = state.getBoolean("colonsep");
		this._semicolonsep = state.getBoolean("semicolonsep");
		this._othersep = null;
		String ot = state.getString("othersep");
		if (ot != null) {
			String oth = Translate.decode(ot);
			this._othersep = oth;
		}
		this._joinsep = state.getBoolean("joinsep");
	}
}
