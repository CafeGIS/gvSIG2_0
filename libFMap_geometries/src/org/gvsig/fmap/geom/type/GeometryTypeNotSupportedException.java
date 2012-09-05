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


package org.gvsig.fmap.geom.type;

import java.util.HashMap;
import java.util.Map;

import org.gvsig.tools.exception.BaseException;

/**
 * This exception is raised when the someone tries to access a geometry 
 * type that is not supported.
 * 
 * @author jiyarza
 */
public class GeometryTypeNotSupportedException extends BaseException {

	/**
	 * Generated serial version UID 
	 */
	private static final long serialVersionUID = -196778635358286969L;

	/**
	 * Key to retrieve this exception's message
	 */
	private static final String MESSAGE_KEY = "geometry_type_not_supported_exception";
	/**
	 * This exception's message. Substitution fields follow the format %(fieldName) and 
	 * must be stored in the Map returned by the values() method. 
	 */
	private static final String FORMAT_STRING = 
		"The geometry class %(geomClassName) is not registered.";
	
	/**
	 * Class name of the geometry type. Should never be null in this exception
	 */
	private String geomClassName = null;
	
	
	/**
	 * Constructor with some context data for cases in which the root cause of 
	 * <code>this</code> is internal (usually an unsatisfied logic rule).
	 * @param geomClass geometry class
	 */
	public GeometryTypeNotSupportedException(Class geomClass){
		this(geomClass, null);
	}
	
	/**
	 * Constructor to use when <code>this</code> is caused by another Exception 
	 * but there is not further context data available.
	 * @param e cause exception
	 */
	public GeometryTypeNotSupportedException(Exception e) {
		this(null, e);
	}
	
	public GeometryTypeNotSupportedException(int type, int subType){
		this(null, null);
	}
	
	public GeometryTypeNotSupportedException(String geomClassName){
		this(null, null);
		this.geomClassName = geomClassName;
	}
	
	/**
	 * Main constructor that provides both context data and a cause Exception
	 * @param geomClass geometry class 
	 * @param e cause exception
	 */
	public GeometryTypeNotSupportedException(Class geomClass, Exception e) {
		messageKey = MESSAGE_KEY;
		formatString = FORMAT_STRING;
		code = serialVersionUID;
		
		if (e != null) {
			initCause(e);
		}
		
		if (geomClass != null) {
			this.geomClassName = geomClass.getName();
		}
	}
	
	protected Map values() {
		HashMap map = new HashMap();
		map.put("geomClassName", geomClassName);
		return map;
	}
}
