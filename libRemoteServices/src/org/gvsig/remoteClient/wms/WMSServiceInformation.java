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
 
package org.gvsig.remoteClient.wms;

import java.util.Vector;

import org.gvsig.remoteClient.ogc.OGCClientOperation;
import org.gvsig.remoteClient.ogc.OGCServiceInformation;
import org.gvsig.remoteClient.utils.CapabilitiesTags;

/**
 * Class that represents the description of the WMS metadata.
 * The first part of the capabilities will return the service information
 * from the WMS, this class will hold this information.
 * 
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public class WMSServiceInformation extends OGCServiceInformation{
	/*public String map_online_resource = null;
    public String feature_online_resource = null;*/
    public String version;
    public String name;
    public String scope;
    public String title;
    public String abstr;
    public String keywords;
    public String fees;
    public String operationsInfo;
    public String personname;
    public String organization;
    public String function;
    public String addresstype;
    public String address;
    public String place;
    public String province;
    public String postcode;
    public String country;
    public String phone;
    public String fax;
    public String email;
    public Vector formats;

    public WMSServiceInformation()
    {
        version = new String();
        name = new String();
        scope = new String();
        title = new String();
        abstr = new String();
        keywords = new String();
        fees = new String();
        operationsInfo = new String();
        personname = new String();
        organization = new String();
        function = new String();
        addresstype = new String();
        address = new String();
        place = new String();
        province = new String();
        postcode = new String();
        country = new String();
        phone = new String();
        fax = new String();
        email = new String();
        formats = new Vector();       
    }
    
    public boolean isQueryable()
    {
    	if (getOnlineResource(CapabilitiesTags.GETFEATUREINFO) != null)    	
    		return true;
    	else
    		return false;
    }
    
    public boolean hasLegendGraphic()
    {
    	if (getOnlineResource(CapabilitiesTags.GETLEGENDGRAPHIC) != null) 
    		return true;
    	else
    		return false;
    }
    
    public void clear() {
    	version = new String();
        name = new String();
        scope = new String();
        title = new String();
        abstr = new String();
        keywords = new String();
        fees = new String();
        operationsInfo = new String();
        personname = new String();
        organization = new String();
        function = new String();
        addresstype = new String();
        address = new String();
        place = new String();
        province = new String();
        postcode = new String();
        country = new String();
        phone = new String();
        fax = new String();
        email = new String();
        formats = new Vector();        
    }      
    
	/* (non-Javadoc)
	 * @see org.gvsig.remoteClient.ogc.OGCServiceInformation#createOperation(java.lang.String)
	 */	
	public OGCClientOperation createOperation(String name) {
		return new WMSOperation(name); 
	}

	/* (non-Javadoc)
	 * @see org.gvsig.remoteClient.ogc.OGCServiceInformation#createOperation(java.lang.String, java.lang.String)
	 */	
	public OGCClientOperation createOperation(String name, String onlineResource) {
		return new WMSOperation(name, onlineResource);
	}	

 }

