package org.gvsig.gpe.kml.parser.v21.kml;

import org.gvsig.gpe.containers.Layer;


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
 * $Id:KMLDiscoveryNetworksTest.java 359 2008-01-09 17:51:16Z jpiera $
 * $Log$
 * Revision 1.2  2007/05/17 06:56:54  jorpiell
 * Some asserts added
 *
 * Revision 1.1  2007/05/16 09:57:10  jorpiell
 * Tests refactoring
 *
 * Revision 1.2  2007/04/20 08:38:59  jorpiell
 * Tests updating
 *
 * Revision 1.1  2007/04/14 16:08:07  jorpiell
 * Kml writing support added
 *
 *
 */
/**
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class KMLDiscoveryNetworksTest extends KMLBaseTest{
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.readers.GPEReaderBaseTest#getFile()
	 */
	public String getFile() {
		return "testdata/Discovery_Networks.kml";
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.readers.GPEReaderBaseTest#makeAsserts()
	 */
	public void makeAsserts() {
		Layer[] layers = getLayers();
		assertEquals(layers.length, 1);
		Layer layer = layers[0];
		assertEquals(layer.getName(), "Discovery Networks");
		assertEquals(layer.getDescription(), "Zoom into real life.");
		assertEquals(layer.getLayers().size(), 5);
	}	



}
