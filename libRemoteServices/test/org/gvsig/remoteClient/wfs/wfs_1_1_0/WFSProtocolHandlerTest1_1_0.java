package org.gvsig.remoteClient.wfs.wfs_1_1_0;

import java.io.File;

import org.gvsig.remoteClient.wfs.wfs_1_0_0.WFSProtocolHandler1_0_0;

import junit.framework.TestCase;
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
 * $Id: WFSProtocolHandlerTest1_1_0.java 15554 2007-10-29 09:22:10Z jpiera $
 * $Log$
 * Revision 1.1  2007-02-09 14:11:01  jorpiell
 * Primer piloto del soporte para WFS 1.1 y para WFS-T
 *
 *
 */
/**
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 */
public class WFSProtocolHandlerTest1_1_0 extends TestCase {
	WFSProtocolHandler1_1_0 handler;
	
	public void setUp() {
		System.out.println("Setting up test..");
		handler = new WFSProtocolHandler1_1_0();		
	}
	
	public void testParsingDescribeFeatureType1() {
//		long t1 = System.currentTimeMillis();
//		assertTrue(handler.parseCapabilities(new File("test/WFS-1_1_0SercartlinGetCapabilities.xml")));
//		handler.setCurrentFeature("popplace");
//		
//		assertTrue(handler.parseDescribeFeatureType(new File("test/WFS-dmsolutionsDescribeFeatureType.xml"),null));
//		long t2 = System.currentTimeMillis();
//		System.out.println("Test parsing vivid done with apparently no errors in "+ (t2-(float)t1)/1000+" seconds");
	}
	
	public void testParsingDescribeFeatureType2() {
		long t1 = System.currentTimeMillis();
		assertTrue(handler.parseCapabilities(new File("testdata/wfs/WFS-1_1_0IDEEGetCapabilities.xml")));
		handler.setCurrentFeature("ideewfs:VerticeRedOrdenInferior");
		assertTrue(handler.parseDescribeFeatureType(new File("testdata/wfs/WFS-1_1_0IDEEDescribeFeatureType.xml"),null));
		long t2 = System.currentTimeMillis();
		Object obj = handler.getFeatures().get("ideewfs:VerticeRedOrdenInferior");
		System.out.println("Test parsing vivid done with apparently no errors in "+ (t2-(float)t1)/1000+" seconds");
	}
	
	public void testParsingDescribeFeatureTypeTNT() {
		long t1 = System.currentTimeMillis();
		assertTrue(handler.parseCapabilities(new File("testdata/wfs/WFS-1_1_0TNTGetCapabilities.xml")));
		handler.setCurrentFeature("BDLL200_Provincia");
		assertTrue(handler.parseDescribeFeatureType(new File("testdata/wfs/WFS-1_1_0TNTDescribeFeatureType1.xml"),null));
		assertTrue(handler.parseDescribeFeatureType(new File("testdata/wfs/WFS-1_1_0TNTDescribeFeatureType2.xml"),null));
		long t2 = System.currentTimeMillis();
		System.out.println("Test parsing IDEE done with apparently no errors in "+ (t2-(float)t1)/1000+" seconds");
	}
}
