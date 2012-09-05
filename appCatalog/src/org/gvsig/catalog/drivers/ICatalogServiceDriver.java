
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
package org.gvsig.catalog.drivers;
import java.net.URI;

import org.gvsig.catalog.drivers.profiles.IProfile;
import org.gvsig.catalog.querys.CatalogQuery;

/**
 * This interface has to be implemented by all the classes that implement
 * a catalog protocol. It describes all the common operations.
 * 
 * 
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public interface ICatalogServiceDriver extends IDiscoveryServiceDriver{

	/**
	 * It send a request with a query to retrieve the server records
     * @return RecordsAnswer Contains the records and additional info
	 * @param uri
	 * Server URI
	 * @param query
	 * It contains the values to do the query (title="XXX",abstract="YYY",...)
	 * @param firstRecord
	 * Number of the first record to retrieve
	 */
	public GetRecordsReply getRecords(URI uri, CatalogQuery query, int firstRecord);

	/**
	 * @return the server profile. A profile is a list of default
	 * values for the properties used to create the queries
	 */
	public IProfile getProfile();

}


