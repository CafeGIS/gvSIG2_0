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
* 2009 IVER T.I   {{Task}}
*/

/**
 *
 */
package org.gvsig.fmap.dal.feature.exception;

import org.gvsig.fmap.dal.exception.DataRuntimeException;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.type.GeometryType;

/**
 * @author jmvivo
 *
 */
public class UnsupportedGeometryException extends DataRuntimeException {


	/**
	 *
	 */
	private static final long serialVersionUID = 2381749762973472962L;
	private final static String MESSAGE_FORMAT = "Unsupported geometry: '%(geomTypeName)'(%(geomType),%(geomSubtype)).";
	private final static String MESSAGE_KEY = "_UnsupportedGeometryException";


	public UnsupportedGeometryException(int geomType, int geomSubtype) {
		super(MESSAGE_FORMAT, MESSAGE_KEY, serialVersionUID);
		setValue("geomType", Integer.toString(geomType));
		setValue("geomSubtype", Integer.toString(geomSubtype));
		setValue("geomTypeName", "{unknow}");
		try {
			GeometryType type = GeometryLocator.getGeometryManager()
					.getGeometryType(geomType, geomSubtype);
			setValue("geomTypeName", type.getName());
		} catch (Exception ex) {
			// Nothing to do
		}
	}
	public UnsupportedGeometryException(GeometryType geomType) {
		super(MESSAGE_FORMAT, MESSAGE_KEY, serialVersionUID);
		setValue("geomType", Integer.toString(geomType.getType()));
		setValue("geomSubtype", Integer.toString(geomType.getSubType()));
		setValue("geomTypeName", geomType.getName());
	}


}
