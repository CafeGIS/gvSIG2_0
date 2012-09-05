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
* 2008 IVER T.I. S.A.   {{Task}}
*/

package org.gvsig.fmap.dal;

import org.gvsig.fmap.dal.exception.ValidateDataParametersException;
import org.gvsig.tools.dynobject.DynObject;
import org.gvsig.tools.persistence.Persistent;

/**
 * This interface is a generic persistent parameter container. It is used in a
 * variety of contexts in which a set of arbitrary parameters is needed.
 */
public interface DataParameters extends Persistent, DynObject {

	/**
	 * clears the parameter container.
	 */
	public void clear();

	/**
	 * Creates and returns a new copy of this DataParameters.
	 *
	 * @return a new copy of this
	 */
	public DataParameters getCopy();

	/**
	 * Checks its valid
	 *
	 * @throws ValidateDataParametersException
	 *             if any problem was detected
	 */
	public void validate() throws ValidateDataParametersException;
}
