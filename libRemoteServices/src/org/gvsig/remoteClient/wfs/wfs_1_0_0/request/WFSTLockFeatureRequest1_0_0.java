package org.gvsig.remoteClient.wfs.wfs_1_0_0.request;

import org.gvsig.remoteClient.utils.CapabilitiesTags;
import org.gvsig.remoteClient.wfs.WFSProtocolHandler;
import org.gvsig.remoteClient.wfs.WFSStatus;
import org.gvsig.remoteClient.wfs.edition.WFSTTags;
import org.gvsig.remoteClient.wfs.request.WFSTLockFeatureRequest;

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
 * @author Jorge Piera LLodr? (jorge.piera@iver.es)
 */
public class WFSTLockFeatureRequest1_0_0 extends WFSTLockFeatureRequest{

	public WFSTLockFeatureRequest1_0_0(WFSStatus status,
			WFSProtocolHandler protocolHandler) {
		super(status, protocolHandler);		
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.remoteClient.wfs.requests.WFSRequest#getHttpGetRequest(java.lang.String)
	 */
	protected String getHttpGetRequest(String onlineResource) {
		StringBuffer req = new StringBuffer();
		req.append(onlineResource);
		req.append("REQUEST=LockFeature&SERVICE=WFS&");
		req.append("TYPENAME=").append(status.getFeatureName()).append("&");
		req.append("LOCKACTION=" + status.getLockAction() + "&");		
		if (status.getFilterQueryLocked() != null){
			req.append("FILTER=");
			req.append(status.getFilterQueryLocked() + "&");
		}		
		if (status.getExpiry() > 0){
			req.append("EXPIRY=" + status.getExpiry() + "&");
		}
		req.append("VERSION=").append(protocolHandler.getVersion()).append("&EXCEPTIONS=XML");
		req.append("&MAXFEATURES=").append(status.getBuffer());
		return req.toString();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.remoteClient.wfs.requests.WFSRequest#getHttpPostRequest(java.lang.String)
	 */
	protected String getHttpPostRequest(String onlineResource) {
		StringBuffer request = new StringBuffer();
		request.append(WFSTTags.XML_ROOT);
		request.append("<" + WFSTTags.WFS_NAMESPACE_PREFIX + ":");
		request.append(CapabilitiesTags.WFS_LOCKFEATURE + " ");
		request.append(CapabilitiesTags.VERSION + "=\"" + protocolHandler.getVersion() + "\" ");
		request.append(WFSTTags.WFST_SERVICE + "=\"WFS\" ");
		if (status.getExpiry() > 0){
			request.append(WFSTTags.WFST_EXPIRYTIME + "=\"" + status.getExpiry() + "\" ");
		}
		request.append(WFSTTags.XMLNS + ":" + WFSTTags.OGC_NAMESPACE_PREFIX);
		request.append("=\"" + WFSTTags.OGC_NAMESPACE + "\" ");
		request.append(WFSTTags.XMLNS + ":" + WFSTTags.WFS_NAMESPACE_PREFIX);
		request.append("=\"" + WFSTTags.WFS_NAMESPACE + "\" ");
		request.append(WFSTTags.XMLNS + ":" + WFSTTags.XML_NAMESPACE_PREFIX);
		request.append("=\"" + WFSTTags.XML_NAMESPACE + "\" ");
		request.append(WFSTTags.XMLNS + ":" + WFSTTags.GML_NAMESPACE_PREFIX);
		request.append("=\"" + WFSTTags.GML_NAMESPACE + "\" ");
		request.append(WFSTTags.XMLNS + ":" + status.getNamespacePrefix());
		request.append("=\"" + status.getNamespace() + "\" ");
		request.append(WFSTTags.XML_NAMESPACE_PREFIX + ":" + WFSTTags.XML_SCHEMALOCATION);
		request.append("=\"" + WFSTTags.WFS_NAMESPACE + " ");
		request.append(getSchemaLocation());
		request.append("\">");
		request.append(getLockPostRequest());
		request.append("</" + WFSTTags.WFS_NAMESPACE_PREFIX + ":");
		request.append(CapabilitiesTags.WFS_LOCKFEATURE + ">");
		return request.toString();
	}

	/**
	 * Creates a Lock query for a HTTP Post request
	 * @return
	 */
	private String getLockPostRequest(){
		StringBuffer request = new StringBuffer();
		request.append("<" + WFSTTags.WFS_NAMESPACE_PREFIX + ":");
		request.append(WFSTTags.WFST_LOCK);
		request.append(" typeName=\"" + status.getFeatureName() + "\"");
		request.append(">");
		String filter = status.getFilterQueryLocked();
		if (filter != null){
			request.append(status.getFilterQueryLockedPost());
		}
		request.append("</" + WFSTTags.WFS_NAMESPACE_PREFIX + ":");
		request.append(WFSTTags.WFST_LOCK + ">");
		return request.toString();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.remoteClient.wfs.requests.WFSRequest#getSchemaLocation()
	 */
	protected String getSchemaLocation() {
		return "../wfs/1.0.0/WFS-transaction.xsd";
	}

}
