package org.gvsig.catalog.utils;

import java.net.URI;
import java.net.URISyntaxException;

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
/* CVS MESSAGES:
 *
 * $Id: URIUtils.java 561 2007-07-27 06:49:30 +0000 (Fri, 27 Jul 2007) jpiera $
 * $Log$
 * Revision 1.1.2.1  2007/07/10 11:18:04  jorpiell
 * Added the registers
 *
 *
 */
/**
 * Some utils to manage URI's
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class URIUtils {
	
	/**
	 * It creates an URI from a String. If the port and the schema
	 * have not been especified on the string this method
	 * adds them
	 * @param sUri
	 * The uri like a string
	 * @param defaultSchema
	 * The deafulet schema
	 * @param deafultPort
	 * The default port
	 * @return
	 * An uri
	 * @throws URISyntaxException 
	 */
	public static URI createUri(String sUri, String defaultSchema, int deafultPort) throws URISyntaxException{
		String schema = defaultSchema;
		String host = null;
		int port = deafultPort;
		String path = "";
		//Cut the schema
		int index = sUri.indexOf("://");
		if (index > -1){
			schema = sUri.substring(0,index);
			sUri = sUri.substring(index + 3, sUri.length());
		}
		//Cut the path
		index = sUri.indexOf("/");
		if (index > -1){
			path = sUri.substring(index, sUri.length());
			sUri = sUri.substring(0,index);			
		}
		//Cut the host:port
		index = sUri.indexOf(":");
		if (index > -1){
			host = sUri.substring(0,index);
			port = Integer.valueOf(sUri.substring(index + 1, sUri.length())).intValue();
		}else{
			host = sUri;			
		}
		return new URI(schema + "://" + host + ":" + port + path);
	}

}
