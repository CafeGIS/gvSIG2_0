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

package org.gvsig.normalization.pattern;

/**
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 * 
 */

import java.util.List;

import org.gvsig.tools.persistence.Persistent;

/**
 * This interface is the normalization pattern.
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 */
public interface Patternnormalization extends Persistent {

	/**
	 * Returns the value of field 'elements'.
	 * 
	 * @return the value of field 'elements'.
	 */

	public List<Element> getElements();

	/**
	 * Returns the value of field 'elements'.
	 * 
	 * @return the value of field 'elements'.
	 */
	public Element[] getArrayElements();

	/**
	 * Returns the value of field 'nofirstrows'.
	 * 
	 * @return the value of field 'nofirstrows'.
	 */
	public int getNofirstrows();

	/**
	 * Returns the value of field 'patternname'.
	 * 
	 * @return the value of field 'patternname'.
	 */
	public String getPatternname();

	/**
	 * Sets the value of field 'elements'.
	 * 
	 * @param elements
	 *            the value of field 'elements'.
	 */

	public void setElements(List<Element> elements);

	/**
	 * Sets the value of field 'nofirstrows'.
	 * 
	 * @param nofirstrows
	 *            the value of field 'nofirstrows'.
	 */
	public void setNofirstrows(int nofirstrows);

	/**
	 * Sets the value of field 'patternname'.
	 * 
	 * @param patternname
	 *            the value of field 'patternname'.
	 */
	public void setPatternname(String patternname);

}
