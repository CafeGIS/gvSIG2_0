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
 
package org.gvsig.fmap.dal.feature.exception;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.Geometry.TYPES;

/**
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public class CreateGeometryException extends DataException {
	private static final long serialVersionUID = -3963756418194447731L;
	private final static String MESSAGE_FORMAT = "Exception creating a geometry with type: '%(type)' and subtype:'%(subtype)'.";
	private final static String MESSAGE_KEY = "_CreateGeometryException";

	public CreateGeometryException(Throwable cause) {
		this(TYPES.NULL, SUBTYPES.UNKNOWN, cause);
	}
	
	public CreateGeometryException(int type, int subtype, Throwable cause) {
		super(MESSAGE_FORMAT, cause, MESSAGE_KEY, serialVersionUID);
		setValue("type", new Integer(type));
		setValue("subtype", new Integer(subtype));
	}

}


