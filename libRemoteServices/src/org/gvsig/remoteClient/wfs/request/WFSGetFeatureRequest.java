package org.gvsig.remoteClient.wfs.request;

import org.gvsig.remoteClient.utils.CapabilitiesTags;
import org.gvsig.remoteClient.wfs.WFSProtocolHandler;
import org.gvsig.remoteClient.wfs.WFSStatus;
import org.gvsig.remoteClient.wfs.edition.WFSTTags;
import org.gvsig.remoteClient.wfs.filters.FilterEncoding;

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
 * The GetFeature operation allows retrieval of features from a 
 * web feature service. A GetFeature request is processed by
 * a WFS and when the value of the outputFormat attribute is 
 * set to text/gml a GML instance document, containing the 
 * result set, is returned to the client.
 * @see http://www.opengeospatial.org/standards/wfs
 * @author Jorge Piera Llodr? (jorge.piera@iver.es)
 */
public abstract class WFSGetFeatureRequest extends WFSRequest{

	public WFSGetFeatureRequest(WFSStatus status,
			WFSProtocolHandler protocolHandler) {
		super(status, protocolHandler);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.remoteClient.wfs.requests.WFSRequest#getHttpPostRequest(java.lang.String)
	 */
	protected String getHttpPostRequest(String onlineResource) {
		StringBuffer request = new StringBuffer();
		request.append(WFSTTags.XML_ROOT);
		request.append("<" + WFSTTags.WFS_NAMESPACE_PREFIX + ":");
		request.append(CapabilitiesTags.WFS_GETFEATURE + " ");
		request.append(CapabilitiesTags.VERSION + "=\"" + protocolHandler.getVersion() + "\" ");
		request.append(WFSTTags.WFST_SERVICE + "=\"WFS\" ");
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
		request.append("<" + WFSTTags.WFS_NAMESPACE_PREFIX + ":");
		request.append(WFSTTags.WFS_QUERY);
		request.append(" typeName=\"" + status.getFeatureName() + "\"");
		request.append(">");
		if ((status.getFilterByAttribute() != null) || (status.getFilterByArea() != null)){
			request.append("<" + WFSTTags.OGC_NAMESPACE_PREFIX + ":" + WFSTTags.WFS_FILTER + " ");
			request.append(WFSTTags.XMLNS + ":" + WFSTTags.OGC_NAMESPACE_PREFIX);
			request.append("=\"" + WFSTTags.OGC_NAMESPACE + "\" ");
			request.append(WFSTTags.XMLNS + ":" + WFSTTags.GML_NAMESPACE_PREFIX);
			request.append("=\"" + WFSTTags.GML_NAMESPACE + "\" ");
			request.append(">");	
			FilterEncoding filterEncoding = new FilterEncoding(status);
			request.append(filterEncoding.toString());
			request.append("</" + WFSTTags.OGC_NAMESPACE_PREFIX + ":" + WFSTTags.WFS_FILTER + ">");
		}
		request.append("</" + WFSTTags.WFS_NAMESPACE_PREFIX + ":");
		request.append(WFSTTags.WFS_QUERY);
		request.append(">");
		request.append("</" + WFSTTags.WFS_NAMESPACE_PREFIX + ":");
		request.append(CapabilitiesTags.WFS_GETFEATURE + ">");
		return request.toString();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.remoteClient.wfs.requests.WFSRequest#getOperationCode()
	 */
	protected String getOperationName() {
		return CapabilitiesTags.WFS_GETFEATURE;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.remoteClient.wfs.requests.WFSRequest#getSchemaLocation()
	 */
	protected String getSchemaLocation() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.remoteClient.wfs.requests.WFSRequest#getTempFilePrefix()
	 */
	protected String getTempFilePrefix() {
		return "wfs_getFeature.xml";
	}
}
