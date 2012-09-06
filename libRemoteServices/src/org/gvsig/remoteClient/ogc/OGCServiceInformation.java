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

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;



public abstract class OGCServiceInformation {
	public String online_resource = null;
	protected HashMap operationsGet = new HashMap();
	protected HashMap operationsPost = new HashMap();
	

	/**
	 * @return Returns the online_resource.
	 */
	 public String getOnline_resource() {
		return online_resource;
	}

	 public abstract OGCClientOperation createOperation(String name);
	 public abstract OGCClientOperation createOperation(String name, String onlineResource);
	/**
	 * Add a new supported operation
	 * @param operation
	 * The operation to support
	 * @param protocol
	 * The HTTP protocol (Get or Post)
	 */
	public void addOperation(String operation, int protocol){
		if (protocol == OGCClientOperation.PROTOCOL_GET){
			operationsGet.put(operation, createOperation(operation));			
		}else if (protocol == OGCClientOperation.PROTOCOL_POST){
			operationsPost.put(operation, createOperation(operation));
		}
	}
	
	/**
	 * Add a new supported operation
	 * @param operation
	 * The operation to support
	 * @param protocol
	 * The HTTP protocol (Get or Post)
	 * @param onlineResource
	 * The online resource
	 */
	public void addOperation(String operation, int protocol, String onlineResource){
		if (protocol == OGCClientOperation.PROTOCOL_GET){
			operationsGet.put(operation, createOperation(operation, onlineResource));
		}else if (protocol == OGCClientOperation.PROTOCOL_POST){
			operationsPost.put(operation, createOperation(operation, onlineResource));
		}
	}
	
	/**
	 * Gest the online resource for a concrete operation
	 * @param operation
	 * The operation
	 * @param protocol
	 * The HTTP protocol (Get or Post)
	 * @return
	 * The online resource
	 */
	public String getOnlineResource(String operation, int protocol){
		OGCClientOperation op = null;
		if (protocol == OGCClientOperation.PROTOCOL_GET){
			op = (OGCClientOperation)operationsGet.get(operation);
		}else if (protocol == OGCClientOperation.PROTOCOL_POST){
			op = (OGCClientOperation)operationsPost.get(operation);
		}
		if ((op == null) ||
				(op.getOnlineResource() == null) || 
				(op.getOnlineResource().equals(""))){
			return null;
		}
		return op.getOnlineResource();
	}
	
	/**
	 * Gets the online resource for a concrete operation.
	 * The default protocol is GET
	 * @param operation
	 * The operation
	 * @return
	 * The online resource
	 */
	public String getOnlineResource(String operation){
		return getOnlineResource(operation, OGCClientOperation.PROTOCOL_GET);
	}
	
	/**
	 * Get a hash map with the supported operations
	 * @return
	 */
	public Hashtable getSupportedOperationsByName(){
		Hashtable operations = new Hashtable();
		Iterator getIt = operationsGet.keySet().iterator();
		while (getIt.hasNext()){
			String id = (String)getIt.next();
			OGCClientOperation operation = (OGCClientOperation)operationsGet.get(id);
			operations.put(operation.getOperationName(),
					operation.getOnlineResource());
		}
		return operations;
	}
	
}

