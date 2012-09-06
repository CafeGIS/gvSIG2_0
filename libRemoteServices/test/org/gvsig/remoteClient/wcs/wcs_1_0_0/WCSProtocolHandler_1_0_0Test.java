/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
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
/*
 * $Id: WCSProtocolHandler_1_0_0Test.java 14880 2007-10-25 08:32:38Z jpiera $ 
 * $Log$
 * Revision 1.8  2006-05-12 07:45:49  jaume
 * some warnings removed
 *
 * Revision 1.7  2006/03/15 08:54:31  jaume
 * *** empty log message ***
 *
 * Revision 1.6  2006/03/06 07:16:08  jaume
 * *** empty log message ***
 *
 * Revision 1.5  2006/03/02 07:17:08  jaume
 * *** empty log message ***
 *
 * Revision 1.3  2006/03/01 18:07:11  jaume
 * *** empty log message ***
 *
 * Revision 1.2  2006/03/01 17:56:28  jaume
 * *** empty log message ***
 *
 * Revision 1.1  2006/02/28 15:26:59  jaume
 * *** empty log message ***
 * 
 */
package org.gvsig.remoteClient.wcs.wcs_1_0_0;

import java.io.File;

import junit.framework.TestCase;
/**
 * 
 * @author jaume
 *
 */
public class WCSProtocolHandler_1_0_0Test extends TestCase {
	WCSProtocolHandler1_0_0 handler1, handler2, handler3, handler4;
	
	public void setUp() {
		System.out.println("Setting up test..");
		handler1 = new WCSProtocolHandler1_0_0();
		handler2 = new WCSProtocolHandler1_0_0();
		handler3 = new WCSProtocolHandler1_0_0();
		handler4 = new WCSProtocolHandler1_0_0();
	}
	
	
	
	public void testParsing() {
		long t1 = System.currentTimeMillis();
		assertTrue(handler1.parseCapabilities(new File("testdata/wcs/WCS-hypnosGetCapabilities.xml")));
		assertTrue(handler1.parseDescribeCoverage(new File("testdata/wcs/WCS-hypnosDescribeCoverage.xml")));
		assertTrue(handler2.parseCapabilities(new File("testdata/wcs/WCS-simonCITCostasGetCapabilities.xml")));
		assertTrue(handler2.parseDescribeCoverage(new File("testdata/wcs/WCS-simonCITCostasDescribeCoverage.xml")));
		assertTrue(handler3.parseCapabilities(new File("testdata/wcs/WCS-simonCITSpotGetCapabilities.xml")));
		assertTrue(handler3.parseDescribeCoverage(new File("testdata/wcs/WCS-simonCITSpotDescribeCoverage.xml")));
		assertTrue(handler4.parseCapabilities(new File("testdata/wcs/WCS-ionicGetCapabilities.xml")));
		assertTrue(handler4.parseDescribeCoverage(new File("testdata/wcs/WCS-ionicDescribeCoverage.xml")));
		long t2 = System.currentTimeMillis();
		System.out.println("Test parsing done with apparently no errors in "+ (t2-(float)t1)/1000+" seconds");
	}
}
