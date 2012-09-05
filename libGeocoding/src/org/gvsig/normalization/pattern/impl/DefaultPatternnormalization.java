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

import java.util.Iterator;
import java.util.List;
import org.gvsig.normalization.pattern.Element;
import org.gvsig.normalization.pattern.Patternnormalization;
import org.gvsig.tools.persistence.PersistenceException;
import org.gvsig.tools.persistence.PersistentState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class Patternnormalization.
 * 
 * This class is the normalization pattern. This pattern has your name
 * (_patternname), your xml file path (_patternurl), the attribute
 * (_nofirstrows) that says the number of the rows in the text file that will
 * not be normalized and the list of elements (_elements) that make the pattern
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 */
public class DefaultPatternnormalization implements Patternnormalization {

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory
			.getLogger(DefaultPatternnormalization.class);

	/**
	 * Pattern name
	 */
	private String _patternname;

	/**
	 * number of file rows that they will not be normalized
	 */
	private int _nofirstrows;

	/**
	 * List of elements. Each element is one new field. One element has
	 * attributes that they define the new field and split strings process.
	 */
	private List<Element> _elements;

	/**
	 * Constructor
	 */
	public DefaultPatternnormalization() {
		super();
	}

	/**
	 * Returns the value of field 'elements'.
	 * 
	 * @return the value of field 'elements'.
	 */

	public List<Element> getElements() {
		return this._elements;
	}

	/**
	 * Returns the value of field 'elements'.
	 * 
	 * @return the value of field 'elements'.
	 */
	public Element[] getArrayElements() {
		Element[] eles = new DefaultElement[this._elements.size()];
		for (int i = 0; i < this._elements.size(); i++) {
			eles[i] = (Element) this._elements.get(i);
		}
		return eles;
	}

	/**
	 * Returns the value of field 'nofirstrows'.
	 * 
	 * @return the value of field 'nofirstrows'.
	 */
	public int getNofirstrows() {
		return this._nofirstrows;
	}

	/**
	 * Returns the value of field 'patternname'.
	 * 
	 * @return the value of field 'patternname'.
	 */
	public java.lang.String getPatternname() {
		return this._patternname;
	}

	/**
	 * Sets the value of field 'elements'.
	 * 
	 * @param elements
	 *            the value of field 'elements'.
	 */

	public void setElements(List<Element> elements) {
		this._elements = elements;
	}

	/**
	 * Sets the value of field 'nofirstrows'.
	 * 
	 * @param nofirstrows
	 *            the value of field 'nofirstrows'.
	 */
	public void setNofirstrows(int nofirstrows) {
		this._nofirstrows = nofirstrows;
	}

	/**
	 * Sets the value of field 'patternname'.
	 * 
	 * @param patternname
	 *            the value of field 'patternname'.
	 */
	public void setPatternname(String patternname) {
		this._patternname = patternname;
	}

	/**
	 * Saves the internal state of the object on the provided PersistentState
	 * object.
	 * 
	 * @param state
	 */
	public void saveToState(PersistentState state) throws PersistenceException {
		state.set("patternname", this._patternname);
		state.set("nofirstrows", this._nofirstrows);
		state.set("elements", this._elements.iterator());
	}

	/**
	 * Set the state of the object from the state passed as parameter.
	 * 
	 * @param state
	 */
	public void loadFromState(PersistentState state)
			throws PersistenceException {
		this._patternname = state.getString("patternname").trim();
		this._nofirstrows = state.getInt("nofirstrows");

		this._elements.clear();
		Iterator<Element> it = state.getIterator("elements");
		while (it.hasNext()) {
			Element ele = it.next();
			this._elements.add(ele);
		}
	}

}
