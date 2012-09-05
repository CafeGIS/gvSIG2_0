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

import java.util.Hashtable;
import java.util.Map;

public class WriterHandlerMimeTypeNotSupportedException extends WriterHandlerCreationException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8043491289837882507L;
	
	private String mimeType = null;
	
	public WriterHandlerMimeTypeNotSupportedException(String mimeType) {
		this.mimeType = mimeType;
		initialize();		
	}
	
	/**
	 * Initialize the properties
	 */
	private void initialize() {
		messageKey = "gpe_writer_handler_creation_mimetype_not_found";
		formatString = "There is not a writer to write the mimetype %(mimetype)";
	}
	
	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.tools.exception.BaseException#values()
	 */
	protected Map values() {
		Hashtable params = new Hashtable();
		params.put("mimetype",mimeType);			
		return params;	
	}

}



