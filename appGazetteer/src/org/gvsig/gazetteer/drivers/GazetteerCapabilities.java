package org.gvsig.gazetteer.drivers;

import org.gvsig.catalog.drivers.DiscoveryServiceCapabilities;
import org.gvsig.gazetteer.querys.FeatureType;


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
 * $Id: GazetteerCapabilities.java 545 2007-07-26 11:23:59 +0000 (Thu, 26 Jul 2007) jpiera $
 * $Log$
 * Revision 1.1.2.2  2007/07/23 12:52:47  jorpiell
 * Refactoring of the capabilities message
 *
 * Revision 1.1.2.1  2007/07/10 11:18:04  jorpiell
 * Added the registers
 *
 *
 */
/**
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class GazetteerCapabilities extends DiscoveryServiceCapabilities{
	private FeatureType[] featureTypes = null;
	

	/**
	 * @return the featureTypes
	 */
	public FeatureType[] getFeatureTypes() {
		return featureTypes;
	}

	/**
	 * @param featureTypes the featureTypes to set
	 */
	public void setFeatureTypes(FeatureType[] featureTypes) {
		this.featureTypes = featureTypes;
	}

	
	
}
