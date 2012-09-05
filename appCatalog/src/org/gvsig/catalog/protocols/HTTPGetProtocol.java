
/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
 *
 * For more information, contact:
 *
 *  Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ibáñez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   IVER T.I. S.A
 *   Salamanca 50
 *   46005 Valencia
 *   Spain
 *
 *   +34 963163400
 *   dac@iver.es
 */
package org.gvsig.catalog.protocols;
import java.io.File;
import java.net.URL;
import java.util.Collection;

import org.apache.commons.httpclient.NameValuePair;
import org.gvsig.catalog.metadataxml.XMLTree;
import org.gvsig.remoteClient.utils.Utilities;


/**
 * 
 * 
 * 
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class HTTPGetProtocol implements IProtocols {

	/**
	 * 
	 * 
	 * 
	 * @return 
	 * @param url 
	 * @param object 
	 * @param firstRecord 
	 */
	public Collection doQuery(URL url, Object object, int firstRecord) {        
		NameValuePair[] parameters = (NameValuePair[]) object;
		File file = null;

		String sUrl = "http://" + url.getHost() + ":" +
		url.getPort() + url.getPath();
		sUrl = sUrl + createURLParams(parameters);
		
		try {	
			file = Utilities.downloadFile(new URL(sUrl),
					"catalog-", null);

		} catch (Exception e) {
			return null;
		}

		Collection col = new java.util.ArrayList();
		col.add(XMLTree.xmlToTree(file));
		return col;
	} 

	/**
	 * Create and string for a list of params
	 * @param params
	 * @return
	 */
	private String createURLParams(NameValuePair[] params){
		StringBuffer buffer = new StringBuffer();
		for (int i=0 ; i<params.length ; i++){
			if (i==0){
				buffer.append("?");
			}else{
				buffer.append("&");
			}
			buffer.append(params[i].getName());
			buffer.append("=");
			buffer.append(params[i].getValue());
		}
		return buffer.toString();
	}

}
