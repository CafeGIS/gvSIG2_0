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
* 2009 {Iver T.I.}   {Task}
*/
 
package org.gvsig.remoteClient.wfs;

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.remoteClient.wfs.filters.AFilter;

/**
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public class WFSGeometryOperation {
	private Geometry area = null;
	private int operation = AFilter.GEOMETRIC_OPERATOR_INTERSECT;
	private String attributeName = null;
	private String srs = null;
	
	/**
	 * @param area
	 * @param operation
	 * @param attributeName
	 * @param srs
	 */
	public WFSGeometryOperation(Geometry area, int operation, String attributeName,
			String srs) {
		super();
		this.area = area;
		this.operation = operation;
		this.attributeName = attributeName;
		this.srs = srs;
	}

	/**
	 * @return the area
	 */
	public Geometry getArea() {
		return area;
	}

	/**
	 * @return the operation
	 */
	public int getOperation() {
		return operation;
	}

	/**
	 * @return the attributeName
	 */
	public String getAttributeName() {
		return attributeName;
	}

	/**
	 * @return the srs
	 */
	public String getSrs() {
		return srs;
	}
}

