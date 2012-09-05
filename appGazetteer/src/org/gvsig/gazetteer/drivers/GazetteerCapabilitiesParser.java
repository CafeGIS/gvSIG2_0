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
 
package org.gvsig.gazetteer.drivers;

import java.util.Collection;

import org.gvsig.catalog.metadataxml.XMLNode;
import org.gvsig.i18n.Messages;

public abstract class GazetteerCapabilitiesParser {
	protected IGazetteerServiceDriver driver = null;
	protected GazetteerCapabilities capabilities = null;
	
	public GazetteerCapabilitiesParser(IGazetteerServiceDriver driver,
			GazetteerCapabilities capabilities) {
		super();
		this.driver = driver;
		this.capabilities = capabilities;
	}
	
	public GazetteerCapabilitiesParser(IGazetteerServiceDriver driver) {
		super();
		this.driver = driver;
		this.capabilities = new GazetteerCapabilities();
	}
	
	/**
	 * It parses the server request
	 * @param node
	 */
	public void parse(Collection nodes){
		if ((nodes == null) || (nodes.size() == 0)){
			capabilities.setAvailable(false);
			capabilities.setServerMessage(Messages.getText("errorNotParsedReply"));
			return;
		}else{
			XMLNode node = (XMLNode)nodes.toArray()[0];
			parseCapabilities(node);
		}		
	}
	
	/**
	 * It parses the server capabilities 
	 */
	protected abstract void parseCapabilities(XMLNode node);

	/**
	 * @return the capabilities
	 */
	public GazetteerCapabilities getCapabilities() {
		return capabilities;
	}	
	
}
