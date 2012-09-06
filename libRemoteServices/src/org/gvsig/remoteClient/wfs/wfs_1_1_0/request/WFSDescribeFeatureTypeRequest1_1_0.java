package org.gvsig.remoteClient.wfs.wfs_1_1_0.request;

import org.gvsig.remoteClient.wfs.WFSProtocolHandler;
import org.gvsig.remoteClient.wfs.WFSStatus;
import org.gvsig.remoteClient.wfs.wfs_1_0_0.request.WFSDescribeFeatureTypeRequest1_0_0;

/* gvSIG. Sistema de Informaci?n Geogr?fica de la Generalitat Valenciana
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
 *   Av. Blasco Ib??ez, 50
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
 * $Id$
 * $Log$
 *
 */
/**
 * @author Jorge Piera Llodr? (jorge.piera@iver.es)
 */
public class WFSDescribeFeatureTypeRequest1_1_0 extends WFSDescribeFeatureTypeRequest1_0_0{

	public WFSDescribeFeatureTypeRequest1_1_0(WFSStatus status,
			WFSProtocolHandler protocolHandler) {
		super(status, protocolHandler);		
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.remoteClient.wfs.requests.WFSRequest#getHttpGetRequest(java.lang.String)
	 */
	protected String getHttpGetRequest(String onlineResource) {
		StringBuffer req = new StringBuffer();
		req.append(super.getHttpGetRequest(onlineResource));
		//Typename
		String typeName = status.getFeatureName();
		if ((status.getNamespace() != null) && 
				(status.getNamespacePrefix() != null)){
			req.append("&");	
			req.append("NAMESPACE=");
			req.append("xmlns(" + status.getNamespacePrefix() + "=");
			req.append(status.getNamespace() + ")");						
		}		
		return req.toString();
	}

}
