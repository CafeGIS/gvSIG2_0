package org.gvsig.remoteClient.wfs.request;

import org.gvsig.remoteClient.utils.CapabilitiesTags;
import org.gvsig.remoteClient.wfs.WFSOperation;
import org.gvsig.remoteClient.wfs.WFSProtocolHandler;
import org.gvsig.remoteClient.wfs.WFSStatus;

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
 * The function of the DescribeFeatureType operation is to 
 * generate a schema description of feature types serviced 
 * by a WFS implementation. The schema descriptions define 
 * how a WFS implementation expects feature instances to 
 * be encoded on input (via Insert and Update requests) 
 * and how feature instances will be generated on output 
 * (in response to GetFeature and GetGmlObject requests). 
 * @see http://www.opengeospatial.org/standards/wfs
 * @author Jorge Piera Llodr? (jorge.piera@iver.es)
 */
public abstract class WFSDescribeFeatureTypeRequest extends WFSRequest{

	public WFSDescribeFeatureTypeRequest(WFSStatus status,
			WFSProtocolHandler protocolHandler) {
		super(status, protocolHandler);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.remoteClient.wfs.requests.WFSRequest#getHttpPostRequest(java.lang.String)
	 */
	protected String getHttpPostRequest(String onlineResource) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.remoteClient.wfs.requests.WFSRequest#getOperationCode()
	 */
	protected String getOperationName() {
		return CapabilitiesTags.WFS_DESCRIBEFEATURETYPE;
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
		return "wfs_describeFeatureType.xml";
	}
}
