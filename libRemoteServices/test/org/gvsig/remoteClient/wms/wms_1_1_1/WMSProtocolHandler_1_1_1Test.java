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
package org.gvsig.remoteClient.wms.wms_1_1_1;

import java.io.File;

import junit.framework.TestCase;

/**
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 */
public class WMSProtocolHandler_1_1_1Test extends TestCase {
	WMSProtocolHandler1_1_1 handler1, handler2, handler3, handler4;

	public void setUp() {
		System.out.println("Setting up test..");
		handler1 = new WMSProtocolHandler1_1_1();
	}



	public void testParsing() {
		long t1 = System.currentTimeMillis();
		handler1.parseCapabilities(new File("testdata/wms/wms.xml"));
		long t2 = System.currentTimeMillis();
		System.out.println("Test parsing done with apparently no errors in "+ (t2-(float)t1)/1000+" seconds");
	}
}

