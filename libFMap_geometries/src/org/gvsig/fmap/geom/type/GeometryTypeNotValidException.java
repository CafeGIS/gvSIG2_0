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
 
package org.gvsig.fmap.geom.type;

import java.util.HashMap;
import java.util.Map;

import org.gvsig.tools.exception.BaseException;

/**
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public class GeometryTypeNotValidException extends BaseException {
	private static final long serialVersionUID = 2588983103873322295L;

	/**
	 * Key to retrieve this exception's message
	 */
	private static final String MESSAGE_KEY = "geometry_type_not_valid_exception";
	/**
	 * This exception's message. Substitution fields follow the format %(fieldName) and 
	 * must be stored in the Map returned by the values() method. 
	 */
	private static final String FORMAT_STRING = 
		"The geometry with type %(type) and subType %(subType) is not supported.";
	
	/**
	 * Class name of the geometry type. Should never be null in this exception
	 */
	private int type;
	private int subType;
		
	/**
	 * Main constructor that provides both context data and a cause Exception
	 * @param geomClass geometry class 
	 * @param e cause exception
	 */
	public GeometryTypeNotValidException(int type, int subType, Exception e) {
		messageKey = MESSAGE_KEY;
		formatString = FORMAT_STRING;
		code = serialVersionUID;
		
		if (e != null) {
			initCause(e);
		}
		
		this.type = type;
		this.subType = subType;
	}
	
	/**
	 * Main constructor that provides both context data and a cause Exception
	 * @param geomClass geometry class 
	 * @param e cause exception
	 */
	public GeometryTypeNotValidException(int type, int subType) {
		this(type, subType, null);
	}	
	
	protected Map values() {
		HashMap map = new HashMap();
		map.put("type", new Integer(type));
		map.put("subType", new Integer(subType));
		return map;
	}
}


