package org.gvsig.remoteClient.wfs.edition;

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
 * $Id$
 * $Log$
 *
 */
/**
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public abstract class WFSTOperation implements IWFSTOperation{
	//The table to apply the operation
	private String namespace = null;
	private String typename = null;
	//The filter to apply the operation
	private boolean isfiltered = true;
	private String filterEncoding = null;	

	WFSTOperation(String typename) {
		super();
		this.typename = typename;		
		isfiltered = false;
	}

	WFSTOperation(String typename, String filterEncoding) {
		super();
		this.typename = typename;
		this.filterEncoding = filterEncoding;
		isfiltered = true;
	}	

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.remoteClient.wfs.edition.IWFSTOperation#getRequest()
	 */
	public String getRequest() {
		StringBuffer request = new StringBuffer();
		request.append("<" + CapabilitiesTags.WFS_NAMESPACE_PREFIX + ":");
		request.append(getOperationName());
		if (hasTypeName()){
			request.append(" typeName=\"" + typename + "\"");
		}
		request.append(">");
		request.append(getGml());
		request.append(getFilter());
		request.append("</" + CapabilitiesTags.WFS_NAMESPACE_PREFIX + ":");
		request.append(getOperationName() + ">");
		return request.toString();
	}
	
	/**
	 * @return if the opartion has a typename
	 */
	protected boolean hasTypeName(){
		return false;
	}

	/**
	 * @return the GML
	 */
	protected String getGml(){
		return "";
	}

	/**
	 * @return the filter where the operation is applied
	 */
	private String getFilter(){
		StringBuffer filter = new StringBuffer();
		if (isfiltered){
			filter.append(filterEncoding);			
		}
		return filter.toString();
	}
}
