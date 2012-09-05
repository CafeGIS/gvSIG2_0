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
 
/**
 * 
 */
package org.gvsig.fmap.geom.operation.fromwkt;

import org.gvsig.fmap.geom.operation.GeometryOperationContext;

/**
 * @author jmvivo
 * 
 */
public class FromWKTGeometryOperationContext extends GeometryOperationContext {

	private String text;
	private String srs;

	public FromWKTGeometryOperationContext(String text,String srs) {
		this.text = text;
		this.srs = srs;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}

	protected String getSrs() {
		return srs;
	}

	protected void setSrs(String srs) {
		this.srs = srs;
	}
}
