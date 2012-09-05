package org.gvsig.catalog.catalog.drivers;

import java.net.URI;

import org.gvsig.catalog.CatalogLocator;
import org.gvsig.catalog.CatalogManager;
import org.gvsig.catalog.drivers.AbstractCatalogServiceDriver;
import org.gvsig.catalog.drivers.CatalogCapabilities;
import org.gvsig.catalog.drivers.DiscoveryServiceCapabilities;
import org.gvsig.catalog.drivers.GetRecordsReply;
import org.gvsig.catalog.querys.CatalogQuery;
import org.gvsig.catalog.schemas.Record;
import org.gvsig.catalog.ui.search.SearchAditionalPropertiesPanel;


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
 * $Id: ExampleNewDriver.java 537 2007-07-26 11:21:10Z jpiera $
 * $Log$
 * Revision 1.1.2.1  2007/07/13 12:00:35  jorpiell
 * Add the posibility to add a new panel
 *
 *
 */
/**
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class ExampleNewDriver extends AbstractCatalogServiceDriver {
	private static final CatalogManager catalogManager = CatalogLocator.getCatalogManager();
	
	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalogClient.drivers.IDiscoveryServiceDriver#getCapabilities(java.net.URI)
	 */
	public DiscoveryServiceCapabilities getCapabilities(URI uri) {
		return new CatalogCapabilities();
	}

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalog.drivers.ICatalogServiceDriver#getRecords(java.net.URI, es.gva.cit.catalog.querys.CatalogQuery, int)
	 */
	public GetRecordsReply getRecords(URI uri, CatalogQuery query,
			int firstRecord) {
		GetRecordsReply reply = new GetRecordsReply(1);
		Record record = catalogManager.createRecord(uri, null);
		record.setTitle("Record example");
		record.setAbstract_("Just for testing");
		reply.addRecord(record);
		return reply;
	}

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalog.drivers.IDiscoveryServiceDriver#getServiceName()
	 */
	public String getServiceName() {
		return "My catalog service";
	}

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.gazetteer.drivers.IGazetteerServiceDriver#getAditionalSearchPanel()
	 */
	public SearchAditionalPropertiesPanel getAditionalSearchPanel(){
		return new ExampleNewPanel();
	}




}
