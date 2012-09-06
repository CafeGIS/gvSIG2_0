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
 
package org.gvsig.remoteClient.wms.wms_1_1_0.request;

import org.gvsig.remoteClient.wms.WMSProtocolHandler;
import org.gvsig.remoteClient.wms.WMSStatus;
import org.gvsig.remoteClient.wms.request.WMSGetMapRequest;

/**
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public class WMSGetMapRequest1_1_0 extends WMSGetMapRequest{

	public WMSGetMapRequest1_1_0(WMSStatus status,
			WMSProtocolHandler protocolHandler) {
		super(status, protocolHandler);		
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.remoteClient.ogc.request.OGCRequest#getHttpGetRequest(java.lang.String)
	 */
	protected String getHttpGetRequest(String onlineResource) {
		StringBuffer req = new StringBuffer();
		req.append(onlineResource);
		req.append("REQUEST=GetMap&SERVICE=WMS&VERSION=").append(protocolHandler.getVersion()).append("&");
		req.append(getPartialQuery(status));
//        if (status.getExceptionFormat() != null) {
//            req.append("&EXCEPTIONS=" + status.getExceptionFormat());
//        } else {
//            req.append("&EXCEPTIONS=XML");
//        }
		return req.toString().replaceAll(" ", "%20");
	}	

}

