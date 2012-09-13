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

/* CVS MESSAGES:
*
* $Id: WebMapContextTest.java 6500 2006-07-21 11:51:13Z jaume $
* $Log$
* Revision 1.3  2006-07-21 11:51:13  jaume
* improved appearance in wms panel and a wmc bug fixed
*
* Revision 1.2  2006/05/12 07:47:39  jaume
* removed unnecessary imports
*
* Revision 1.1  2006/05/03 07:51:21  jaume
* *** empty log message ***
*
* Revision 1.2  2006/04/19 16:34:29  jaume
* *** empty log message ***
*
* Revision 1.1  2006/04/19 07:57:29  jaume
* *** empty log message ***
*
*
*/
package com.iver.cit.gvsig.wmc;

import java.io.File;

import junit.framework.TestCase;

public class WebMapContextTest extends TestCase {
	WebMapContext wmc1, wmc2;
	File fWmc1, fWmc2;
	public void setUp() {
		System.out.println("Setting up test..");
		fWmc1 = new File("test/WebMapContext-sample.cml");
		fWmc2 = new File("test/MyContext.cml");
	}

	public void testParsing() {
		String message = null;
		try {
			message = fWmc1.getAbsolutePath();
			wmc1 = new WebMapContext();
			wmc1.readFile(fWmc1);
			assertTrue(wmc1.layerList.size()==4); // this map context contains four layers

			wmc2 = new WebMapContext();
			wmc2.readFile(fWmc2);
			System.out.println("Parsing done with apparently no errors...");
		} catch (Exception e) {
			fail(message+" parse failed");
		}
	}
}