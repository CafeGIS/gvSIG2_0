package org.gvsig.remoteClient.wfs.request;

import org.gvsig.remoteClient.utils.CapabilitiesTags;
import org.gvsig.remoteClient.wfs.WFSOperation;
import org.gvsig.remoteClient.wfs.WFSProtocolHandler;
import org.gvsig.remoteClient.wfs.WFSStatus;
import org.gvsig.remoteClient.wfs.edition.WFSTTags;

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
 * $Id$
 * $Log$
 *
 */
/**
 * Web connections are inherently stateless. As a consequence 
 * of this, the semantics of serializable transactions are not 
 * preserved. To understand the issue, consider an update operation.
 * The client fetches a feature instance. The feature is then 
 * modified on the client side, and submitted back to the database 
 * via a Transaction request for update. Serializability is lost 
 * since there is nothing to guarantee that while the feature was 
 * being modified on the client side, another client did not come 
 * along and update that same feature in the database.
 * One way to ensure serializability is to require that access to
 * data be done in a mutually exclusive manner; that is while one 
 * transaction accesses a data item, no other transaction can modify 
 * the same data item. This can be accomplished by using locks that 
 * control access to the data.
 * The purpose of the LockFeature operation is to expose a long 
 * term feature locking mechanism to ensure consistency. The lock
 * is considered long term because network latency would make 
 * feature locks last relatively longer than native commercial 
 * database locks.
 * The LockFeature operation is optional and does not need to be 
 * implemented for a WFS implementation to conform to this 
 * specification. If a WFS implements the LockFeature operation, 
 * this fact must be advertised in the capabilities document
 * @see http://www.opengeospatial.org/standards/wfs
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public abstract class WFSTLockFeatureRequest extends WFSRequest{

	public WFSTLockFeatureRequest(WFSStatus status,
			WFSProtocolHandler protocolHandler) {
		super(status, protocolHandler);	
		setDeleted(true);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.remoteClient.wfs.requests.WFSRequest#getTempFilePrefix()
	 */
	protected String getTempFilePrefix() {
		return "wfs_lockFeature.xml";
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.remoteClient.wfs.requests.WFSRequest#getOperationCode()
	 */
	protected String getOperationName() {
		return CapabilitiesTags.WFS_LOCKFEATURE;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.remoteClient.wfs.requests.WFSRequest#getSchemaLocation()
	 */
	protected String getSchemaLocation() {
		return "../wfs/1.0.0/WFS-transaction.xsd";
	}
}
