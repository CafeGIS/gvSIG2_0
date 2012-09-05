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

import org.gvsig.tools.persistence.Persistent;

/**
 * This interface has the separators between two fields and they are necessary for
 * the split string process
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 */
public interface Fieldseparator extends Persistent {

	
	/**
	 * @return the value of field 'colonsep'.
	 */
	public boolean getColonsep();

	/**
	 * @return the value of field 'joinsep'.
	 */
	public boolean getJoinsep();

	/**
	 * @return the value of field 'othersep'.
	 */
	public java.lang.String getOthersep();

	/**
	 * @return the value of field 'semicolonsep'.
	 */
	public boolean getSemicolonsep();

	/** 
	 * @return the value of field 'spacesep'.
	 */
	public boolean getSpacesep();

	/**
	 * @return the value of field 'tabsep'.
	 */
	public boolean getTabsep();

	/**
	 * Sets the value of field 'colonsep'.
	 * 
	 * @param colonsep
	 *            the value of field 'colonsep'.
	 */
	public void setColonsep(boolean colonsep);

	/**
	 * Sets the value of field 'joinsep'.
	 * 
	 * @param joinsep
	 *            the value of field 'joinsep'.
	 */
	public void setJoinsep(boolean joinsep);

	/**
	 * Sets the value of field 'othersep'.
	 * 
	 * @param othersep
	 *            the value of field 'othersep'.
	 */
	public void setOthersep(String othersep);

	/**
	 * Sets the value of field 'semicolonsep'.
	 * 
	 * @param semicolonsep
	 *            the value of field 'semicolonsep'.
	 */
	public void setSemicolonsep(boolean semicolonsep);

	/**
	 * Sets the value of field 'spacesep'.
	 * 
	 * @param spacesep
	 *            the value of field 'spacesep'.
	 */
	public void setSpacesep(boolean spacesep);

	/**
	 * Sets the value of field 'tabsep'.
	 * 
	 * @param tabsep
	 *            the value of field 'tabsep'.
	 */
	public void setTabsep(boolean tabsep);

	
}
