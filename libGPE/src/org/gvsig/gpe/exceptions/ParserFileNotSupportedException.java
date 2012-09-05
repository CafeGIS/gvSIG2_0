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
* 2008 Iver T.I.  {{Task}}
*/
 
package org.gvsig.gpe.exceptions;

import java.net.URI;
import java.util.Hashtable;
import java.util.Map;

public class ParserFileNotSupportedException extends ParserCreationException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 576971727796678053L;
	private URI uri = null;
	
	public ParserFileNotSupportedException(URI uri) {
		this.uri = uri;
		initialize();		
	}
	
	/**
	 * Initialize the properties
	 */
	private void initialize() {
		messageKey = "gpe_parser_creation_uri_not_found";
		formatString = "There is not a parser to parse the uri %(uri)";
	}
	
	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.tools.exception.BaseException#values()
	 */
	protected Map values() {
		Hashtable params = new Hashtable();
		params.put("uri",uri);			
		return params;	
	}

}



