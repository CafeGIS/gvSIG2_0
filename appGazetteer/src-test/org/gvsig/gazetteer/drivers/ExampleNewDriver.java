package org.gvsig.gazetteer.drivers;

import java.awt.geom.Point2D;
import java.net.URI;

import org.gvsig.catalog.drivers.DiscoveryServiceCapabilities;
import org.gvsig.catalog.ui.search.SearchAditionalPropertiesPanel;
import org.gvsig.gazetteer.querys.Feature;
import org.gvsig.gazetteer.querys.GazetteerQuery;


/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
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
 *   Av. Blasco Ib��ez, 50
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
 * $Id: ExampleNewDriver.java 537 2007-07-26 11:21:10 +0000 (Thu, 26 Jul 2007) jpiera $
 * $Log$
 * Revision 1.1.2.1  2007/07/13 12:00:35  jorpiell
 * Add the posibility to add a new panel
 *
 *
 */
/**
 * @author Jorge Piera LLodr� (jorge.piera@iver.es)
 */
public class ExampleNewDriver extends AbstractGazetteerServiceDriver {

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalogClient.drivers.IDiscoveryServiceDriver#getCapabilities(java.net.URI)
	 */
	public DiscoveryServiceCapabilities getCapabilities(URI uri) {
		return new GazetteerCapabilities();
	}

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.gazetteer.drivers.IGazetteerServiceDriver#getFeature(java.net.URI, es.gva.cit.gazetteer.querys.Query)
	 */
	public Feature[] getFeature(URI uri, GazetteerQuery query) throws Exception {
		String prop = (String)query.getProperty("PROP1");
		Feature[] features = new Feature[1];
		features[0] = new Feature("1","Result 1","description 1",new Point2D.Double(0,0));
		return features;
	}

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.gazetteer.drivers.IGazetteerServiceDriver#getServiceName()
	 */
	public String getServiceName() {
		return "My service";
	}
	
	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.gazetteer.drivers.IGazetteerServiceDriver#getAditionalSearchPanel()
	 */
	public SearchAditionalPropertiesPanel getAditionalSearchPanel(){
		return new ExampleNewPanel();
	}

}
