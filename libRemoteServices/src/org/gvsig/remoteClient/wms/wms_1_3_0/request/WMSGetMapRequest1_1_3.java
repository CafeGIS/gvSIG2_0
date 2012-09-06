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
 
package org.gvsig.remoteClient.wms.wms_1_3_0.request;

import java.util.Vector;

import org.gvsig.remoteClient.utils.Utilities;
import org.gvsig.remoteClient.wms.WMSProtocolHandler;
import org.gvsig.remoteClient.wms.WMSStatus;
import org.gvsig.remoteClient.wms.wms_1_1_0.request.WMSGetMapRequest1_1_0;

/**
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public class WMSGetMapRequest1_1_3 extends WMSGetMapRequest1_1_0{

	public WMSGetMapRequest1_1_3(WMSStatus status,
			WMSProtocolHandler protocolHandler) {
		super(status, protocolHandler);		
	}
	
	 protected String getPartialQuery(WMSStatus status)
	    {
	        StringBuffer req = new StringBuffer();
	        req.append("LAYERS=" + Utilities.Vector2CS(status.getLayerNames()))
	           .append("&CRS=" + status.getSrs())
	           .append("&BBOX=" + status.getExtent().getMinX()+ "," )
	           .append(status.getExtent().getMinY()+ ",")
	           .append(status.getExtent().getMaxX()+ ",")
	           .append(status.getExtent().getMaxY())
	           .append("&WIDTH=" + status.getWidth())
	           .append("&HEIGHT=" + status.getHeight())
	           .append("&FORMAT=" + status.getFormat())
	           .append("&STYLES=");
	        Vector v = status.getStyles();
	        if (v!=null && v.size()>0)
	        	req.append(Utilities.Vector2CS(v));
	        v = status.getDimensions();
	        if (v!=null && v.size()>0)
	            req.append("&" + Utilities.Vector2URLParamString(v));
	        if (status.getTransparency()) {
	            req.append("&TRANSPARENT=TRUE");
	        }
	        return req.toString();
	    }

}

