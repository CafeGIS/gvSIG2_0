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
 * 2009 Iver T.I.  {{Task}}
 */

package org.gvsig.remoteClient.wcs.request;

import org.gvsig.remoteClient.ogc.request.OGCRequest;
import org.gvsig.remoteClient.wcs.WCSProtocolHandler;
import org.gvsig.remoteClient.wcs.WCSStatus;

/**
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public abstract class WCSRequest extends OGCRequest{
	protected WCSStatus status = null;

	public WCSRequest(WCSStatus status, WCSProtocolHandler protocolHandler) {
		super(status, protocolHandler);	
		this.status = status;
	}

	protected String getPartialQuery(WCSStatus status)
	{
		StringBuffer req = new StringBuffer();
		req.append( (status.getTime() != null) ? "TIME="+status.getTime() : "" )
		.append( "&COVERAGE=" + status.getCoverageName())
		.append( "&CRS=" + status.getSrs())
		.append( "&FORMAT=" + status.getFormat() )
		.append( "&HEIGHT=" + status.getHeight())
		.append( "&WIDTH=" + status.getWidth())
		.append( (status.getDepth() != null) ? "&DEPTH="+status.getDepth() : "" )
		.append( "&BBOX=" + status.getExtent().getMinX()+ "," )
		.append( status.getExtent().getMinY()+ ",")
		.append( status.getExtent().getMaxX()+ ",")
		.append( status.getExtent().getMaxY())
		.append( (status.getParameters() != null) ? "&"+status.getParameters() : "");

		return req.toString();
	}
}



