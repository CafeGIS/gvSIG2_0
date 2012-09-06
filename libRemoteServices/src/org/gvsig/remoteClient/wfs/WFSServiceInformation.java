package org.gvsig.remoteClient.wfs;

import java.util.HashMap;
import java.util.Vector;

import org.gvsig.remoteClient.ogc.OGCClientOperation;
import org.gvsig.remoteClient.ogc.OGCServiceInformation;

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
public class WFSServiceInformation extends OGCServiceInformation{
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
	private HashMap operationsGet; 
	private HashMap operationsPost; 
	private HashMap namespaces;
	
	public WFSServiceInformation() {  	
		clear();     
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
		operationsGet = new HashMap();  
		operationsPost = new HashMap();   
		namespaces = new HashMap();
	}
	
	/**
	 * Adds a new namespace
	 * @param namespacePrefix
	 * Namespace prefix
	 * @param namespaceURI
	 * Namespace URI
	 */
	public void addNamespace(String namespacePrefix, String namespaceURI){
		namespaces.put(namespacePrefix, namespaceURI);
	}
	
	/**
	 * Gest a namespace URI
	 * @param namespaceprefix
	 * Namespace prefix
	 * @return
	 * The namespace URI
	 */
	public String getNamespace(String namespaceprefix){
		if (namespaces.containsKey(namespaceprefix)){
			return (String)namespaces.get(namespaceprefix);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.remoteClient.ogc.OGCServiceInformation#createOperation(java.lang.String)
	 */	
	public OGCClientOperation createOperation(String name) {
		return new WFSOperation(name); 
	}

	/* (non-Javadoc)
	 * @see org.gvsig.remoteClient.ogc.OGCServiceInformation#createOperation(java.lang.String, java.lang.String)
	 */	
	public OGCClientOperation createOperation(String name, String onlineResource) {
		return new WFSOperation(name, onlineResource);
	}	
}

