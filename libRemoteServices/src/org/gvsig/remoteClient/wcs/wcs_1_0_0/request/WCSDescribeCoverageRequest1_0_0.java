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
 
package org.gvsig.remoteClient.wcs.wcs_1_0_0.request;

import org.gvsig.remoteClient.wcs.WCSProtocolHandler;
import org.gvsig.remoteClient.wcs.WCSStatus;
import org.gvsig.remoteClient.wcs.request.WCSDescribeCoverageRequest;

/**
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public class WCSDescribeCoverageRequest1_0_0 extends WCSDescribeCoverageRequest{

	public WCSDescribeCoverageRequest1_0_0(WCSStatus status,
			WCSProtocolHandler protocolHandler) {
		super(status, protocolHandler);	
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.remoteClient.ogc.request.OGCRequest#getHttpGetRequest(java.lang.String)
	 */
	protected String getHttpGetRequest(String onlineResource) {
		StringBuffer req = new StringBuffer();
		req.append(onlineResource).append("REQUEST=DescribeCoverage&SERVICE=WCS&");
		if (status != null && status.getCoverageName()!= null)
			req.append("COVERAGE="+status.getCoverageName()+"&");
		req.append("VERSION=").append(protocolHandler.getVersion()).append("&EXCEPTIONS=XML");
		return req.toString();
	}

}

