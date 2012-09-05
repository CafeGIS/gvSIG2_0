package org.gvsig.catalog.drivers;

import java.net.URI;

import org.gvsig.catalog.ui.search.SearchAditionalPropertiesPanel;

import com.iver.utiles.swing.jcomboServer.ServerData;


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
public abstract class AbstractDiscoveryServiceDriver implements IDiscoveryServiceDriver {
	private String serverAnswerReady = null;

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalogClient.drivers.IDiscoveryServiceDriver#getServerAnswerReady()
	 */
	public String getServerAnswerReady() {        
		return serverAnswerReady;
	} 

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalogClient.drivers.IDiscoveryServiceDriver#setServerAnswerReady(java.lang.String)
	 */
	public void setServerAnswerReady(String serverAnswerReady) {        
		this.serverAnswerReady = serverAnswerReady;
	} 
	
	
	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalogClient.drivers.ICatalogServiceDriver#getDefaultPort()
	 */
	public int getDefaultPort() {
		return 80;
	}
	
	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalogClient.drivers.ICatalogServiceDriver#getDefaultSchema()
	 */
	public String getDefaultSchema() {
		return "http";
	}
	
	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalogClient.drivers.ICatalogServiceDriver#isProtocolSupported(java.net.URI)
	 */
	public boolean isProtocolSupported(URI uri) {
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalogClient.drivers.ICatalogServiceDriver#getAditionalSearchPanel()
	 */
	public SearchAditionalPropertiesPanel getAditionalSearchPanel(){
		return null;
	}	

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return getServiceName();
	}
	
	/* (non-Javadoc)
	 * @see es.gva.cit.catalog.drivers.IDiscoveryServiceDriver#isOneServer()
	 */
	public ServerData getOneServer() {
		return null;
	}
}
