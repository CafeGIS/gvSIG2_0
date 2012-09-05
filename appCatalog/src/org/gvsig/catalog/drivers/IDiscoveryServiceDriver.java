package org.gvsig.catalog.drivers;

import java.net.URI;

import org.gvsig.catalog.exceptions.NotSupportedProtocolException;
import org.gvsig.catalog.exceptions.NotSupportedVersionException;
import org.gvsig.catalog.querys.DiscoveryServiceQuery;
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
public interface IDiscoveryServiceDriver {
	/**
	 * It try to discover the server capabilities.
	 * @return Node with the server answer.
	 * @param uri Server URI
	 */
	public DiscoveryServiceCapabilities getCapabilities(URI uri) throws NotSupportedVersionException;
	
	/**
	 * It have to prove if the specified protocol is supported.
	 * @return true if is supported, false if it isn't supported
	 * @param uri Server URI
	 */
	public boolean isProtocolSupported(URI uri) throws NotSupportedProtocolException;
	
	/**
	 * It return a message to write in the server connection frame
	 * @return It is/isn't the supported protocol
	 */
	public String getServerAnswerReady();
	
	/**
	 * It sets an error message 
	 */
	public void setServerAnswerReady(String message);

	/**
	 * @return the dafault port for an specific
	 * service
	 */
	public int getDefaultPort();
	
	/**
	 * @return the dafault schema for an specific
	 * service
	 */
	public String getDefaultSchema();
	
	/**
	 * @return the service name that will be showed
	 * in the user interface
	 */
	public String getServiceName();
	
	/**
	 * This message will be showed in the protocols combo
	 * @return The message to show
	 */
	public String toString();
	
	/**
	 * There are protocols that can support other attributes
	 * to do the search. They have to provide its own
	 * panel to do the search
	 * @return
	 * JPanel with a list of properties.
	 */
	public SearchAditionalPropertiesPanel getAditionalSearchPanel();

	/**
	 * Create and return an empty query
	 * @return
	 */
	public DiscoveryServiceQuery createQuery();
	
	/**
	 * @return if the protocol always works with the 
	 * same server
	 */
	public ServerData getOneServer();		
}

