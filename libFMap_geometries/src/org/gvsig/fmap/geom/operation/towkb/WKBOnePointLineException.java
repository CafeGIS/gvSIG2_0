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


package org.gvsig.fmap.geom.operation.towkb;

import org.gvsig.fmap.geom.Geometry;

public class WKBOnePointLineException extends WKBEncodingException {

	/**
	 * Generated serial version UID
	 */
	private static final long serialVersionUID = 1810408131264048570L;

	/**
	 * Key to retrieve this exception's message
	 */
	private static final String MESSAGE_KEY = "wkb_one_point_line_exception";
	private static final String FORMAT_STRING =
		"Single point line definition";

	/**
	 * Class name of the geometry type. Should never be null in this exception
	 */
	public Geometry geometry = null;


	public WKBOnePointLineException(Geometry geom) {
		super(FORMAT_STRING, MESSAGE_KEY, serialVersionUID);
		this.geometry = geom;
	}

}
