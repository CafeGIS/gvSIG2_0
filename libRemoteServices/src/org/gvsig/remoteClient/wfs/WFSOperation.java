package org.gvsig.remoteClient.wfs;

import java.util.Hashtable;

import org.gvsig.remoteClient.ogc.OGCClientOperation;
import org.gvsig.remoteClient.utils.CapabilitiesTags;

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
 * $Id: WFSOperation.java 27881 2009-04-07 14:47:27Z jpiera $
 * $Log$
 * Revision 1.1  2007-02-09 14:11:01  jorpiell
 * Primer piloto del soporte para WFS 1.1 y para WFS-T
 *
 *
 */
/**
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 */
public class WFSOperation extends OGCClientOperation{
	protected static Hashtable operations = new Hashtable();
	
	static{
		operations.put(CapabilitiesTags.GETCAPABILITIES, new WFSOperation(CapabilitiesTags.GETCAPABILITIES));
		operations.put(CapabilitiesTags.WFS_DESCRIBEFEATURETYPE, new WFSOperation(CapabilitiesTags.WFS_DESCRIBEFEATURETYPE));
		operations.put(CapabilitiesTags.WFS_GETFEATURE, new WFSOperation(CapabilitiesTags.WFS_GETFEATURE));
		operations.put(CapabilitiesTags.WFS_TRANSACTION, new WFSOperation(CapabilitiesTags.WFS_TRANSACTION));
		operations.put(CapabilitiesTags.WFS_LOCKFEATURE, new WFSOperation(CapabilitiesTags.WFS_LOCKFEATURE));
	}

	public WFSOperation(String operationName) {
		super(operationName);		
	}

	public WFSOperation(String operationName, String onlineResource) {
		super(operationName, onlineResource);		
	}

	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.remoteClient.OGCClientOperation#getOperations()
	 */
	public Hashtable getOperations() {
		return operations;
	}
	
	
}

