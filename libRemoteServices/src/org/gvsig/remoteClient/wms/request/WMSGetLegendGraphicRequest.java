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
 
package org.gvsig.remoteClient.wms.request;

import org.gvsig.remoteClient.utils.CapabilitiesTags;
import org.gvsig.remoteClient.wms.WMSProtocolHandler;
import org.gvsig.remoteClient.wms.WMSStatus;

/**
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public abstract class WMSGetLegendGraphicRequest extends WMSRequest {
	protected String layerName = null;	
	
	public WMSGetLegendGraphicRequest(WMSStatus status, WMSProtocolHandler protocolHandler, String layerName) {
		super(status, protocolHandler);	
		this.layerName = layerName;
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
		return CapabilitiesTags.GETLEGENDGRAPHIC;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.remoteClient.wfs.requests.WFSRequest#getTempFilePrefix()
	 */
	protected String getTempFilePrefix() {
		return "wms_getLegendGraphic.xml";
	}
}
