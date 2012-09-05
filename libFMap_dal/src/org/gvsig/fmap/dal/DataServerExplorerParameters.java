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

/**
 * DataServerExplorer parameter container. Provides a way to obtain the name 
 * of the specific data explorer type described by this parameters.
 * 
 */
public interface DataServerExplorerParameters extends DataParameters {

	/**
	 * Returns the name of the data explorer type of the DataServerExplorer
	 * represented by this DataServerExplorerParameters
	 * 
	 * @return a String containing the name of the data explorer type
	 */
	public String getExplorerName();

}
