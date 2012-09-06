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
* 2008 Geographic Information research group: http://www.geoinfo.uji.es
* Departamento de Lenguajes y Sistemas Informáticos (LSI)
* Universitat Jaume I   
* {{Task}}
*/

package org.gvsig.metadata.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.gvsig.tools.exception.BaseException;

public class NamedMetadataNotFoundException extends BaseException {

	private static final long serialVersionUID = 1L;
	private String className = null;
	
	public NamedMetadataNotFoundException(Class md){
		if (md != null) {
			this.className = md.getName();
		}
	}
	
	/**
	 * Initializes some values
	 */
	public void init() {
		messageKey = "named_metadata_not_found_exception";
		formatString = "Named Metadata can't be found.";
		code = serialVersionUID;
	}
	
	
	protected Map values() {
		HashMap map = new HashMap();
		map.put("className", className);		
		return map;
	}

}
