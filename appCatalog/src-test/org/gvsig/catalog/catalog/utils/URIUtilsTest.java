package org.gvsig.catalog.catalog.utils;

import java.net.URI;
import java.net.URISyntaxException;

import junit.framework.TestCase;

import org.gvsig.catalog.utils.URIUtils;

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
 * $Id: URIUtilsTest.java,v 1.1.2.1 2007/07/10 11:18:04 jorpiell Exp $
 * $Log: URIUtilsTest.java,v $
 * Revision 1.1.2.1  2007/07/10 11:18:04  jorpiell
 * Added the registers
 *
 *
 */
/**
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class URIUtilsTest extends TestCase {
	
	public void test1() throws URISyntaxException{
		URI uri = URIUtils.createUri("http://www.upv.es", "http", 80);	
		assertEquals(uri.getHost(), "www.upv.es");
		assertEquals(uri.getScheme(), "http");
		assertEquals(uri.getPort(), 80);
		assertEquals(uri.getPath(), "");
	}
	
	public void test2() throws URISyntaxException{
		URI uri = URIUtils.createUri("www.upv.es", "http", 80);	
		assertEquals(uri.getHost(), "www.upv.es");
		assertEquals(uri.getScheme(), "http");
		assertEquals(uri.getPort(), 80);
		assertEquals(uri.getPath(), "");
	}
	
	public void test3() throws URISyntaxException{
		URI uri = URIUtils.createUri("www.upv.es", "z3950", 2100);	
		assertEquals(uri.getHost(), "www.upv.es");
		assertEquals(uri.getScheme(), "z3950");
		assertEquals(uri.getPort(), 2100);
		assertEquals(uri.getPath(), "");
	}
	
	public void test4() throws URISyntaxException{
		URI uri = URIUtils.createUri("http://193.144.250.29/webservices/services/IDEC_GeoServeisPort", "http", 80);	
		assertEquals(uri.getHost(), "193.144.250.29");
		assertEquals(uri.getScheme(), "http");
		assertEquals(uri.getPort(), 80);
		assertEquals(uri.getPath(), "/webservices/services/IDEC_GeoServeisPort");
	}
}
