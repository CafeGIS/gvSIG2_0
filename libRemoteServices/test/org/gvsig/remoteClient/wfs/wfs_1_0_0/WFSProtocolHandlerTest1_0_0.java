package org.gvsig.remoteClient.wfs.wfs_1_0_0;

import java.io.File;

import org.gvsig.remoteClient.wcs.wcs_1_0_0.WCSProtocolHandler1_0_0;
import org.gvsig.remoteClient.wfs.WFSStatus;
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
 * $Id: WFSProtocolHandlerTest1_0_0.java 15554 2007-10-29 09:22:10Z jpiera $
 * $Log$
 * Revision 1.2  2006-10-10 12:52:28  jorpiell
 * Soporte para features complejas.
 *
 * Revision 1.1  2006/08/10 12:00:49  jorpiell
 * Primer commit del driver de Gml
 *
 * Revision 1.3  2006/05/23 13:23:13  jorpiell
 * Se ha cambiado el final del bucle de parseado y se tiene en cuenta el online resource
 *
 * Revision 1.2  2006/04/20 16:39:16  jorpiell
 * Añadida la operacion de describeFeatureType y el parser correspondiente.
 *
 * Revision 1.1  2006/04/19 12:51:35  jorpiell
 * Añadidas algunas de las clases del servicio WFS
 *
 *
 */
/**
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 */
public class WFSProtocolHandlerTest1_0_0 extends TestCase {
	WFSProtocolHandler1_0_0 handler;
	
	public void setUp() {
		System.out.println("Setting up test..");
		handler = new WFSProtocolHandler1_0_0();		
	}
	
	
	public void testParsingDescribeFeatureType() {
		long t1 = System.currentTimeMillis();
		assertTrue(handler.parseCapabilities(new File("testdata/wfs/WFS-dmsolutionsGetCapabilities.xml")));
		handler.setCurrentFeature("popplace");
		assertTrue(handler.parseDescribeFeatureType(new File("testdata/wfs/WFS-dmsolutionsDescribeFeatureType.xml"),null));
		long t2 = System.currentTimeMillis();
		System.out.println("Test parsing vivid done with apparently no errors in "+ (t2-(float)t1)/1000+" seconds");
	}
	
	public void testParsingDescribeFeatureType2() {
		long t1 = System.currentTimeMillis();
		assertTrue(handler.parseCapabilities(new File("testdata/wfs/WFS-sercartlinGetCapabilities.xml")));
		handler.setCurrentFeature("topp:tasmania_roads");
		assertTrue(handler.parseDescribeFeatureType(new File("testdata/wfs/WFS-sercartlinDescribeFeatureType.xml"),"cit"));
		long t2 = System.currentTimeMillis();
		System.out.println("Test parsing sercartlin done with apparently no errors in "+ (t2-(float)t1)/1000+" seconds");
	}
	
	public void testParsingDescribeFeatureType3() {
		long t1 = System.currentTimeMillis();
		assertTrue(handler.parseCapabilities(new File("testdata/wfs/WFS-IDEEGetCapabilities.xml")));
		handler.setCurrentFeature("BDLL200_Provincia");
		assertTrue(handler.parseDescribeFeatureType(new File("testdata/wfs/WFS-IDEEDescribeFeatureType.xml"),null));
		long t2 = System.currentTimeMillis();
		System.out.println("Test parsing TNT done with apparently no errors in "+ (t2-(float)t1)/1000+" seconds");
	}
	

	
}
