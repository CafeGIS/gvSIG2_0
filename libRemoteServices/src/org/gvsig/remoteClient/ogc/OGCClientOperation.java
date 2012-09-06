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
 
package org.gvsig.remoteClient.ogc;

import java.util.Hashtable;

public abstract class OGCClientOperation {
	public static final int PROTOCOL_UNDEFINED = -1;
	public static final int PROTOCOL_GET = 0;
	public static final int PROTOCOL_POST = 1;
	protected String operationName;
	protected String onlineResource;
		
	public OGCClientOperation(String operationName) {
		super();
		this.operationName = operationName;		
	}		

	public OGCClientOperation(String operationName, String onlineResource) {
		this.onlineResource = onlineResource;
		this.operationName = operationName;
	}	
	
	public abstract Hashtable getOperations(); 
	
	/**
	 * @return Returns the onlineResource.
	 */
	public String getOnlineResource() {
		return onlineResource;
	}
	/**
	 * @param onlineResource The onlineResource to set.
	 */
	public void setOnlineResource(String onlineResource) {
		this.onlineResource = onlineResource;
	}	

	/**
	 * @return Returns the operationName.
	 */
	public String getOperationName() {
		return operationName;
	}
	
	/**
	 * @param operationName The operationName to set.
	 */
	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}		
}



