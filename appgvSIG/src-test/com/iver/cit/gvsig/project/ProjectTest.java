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
* $Id: ProjectTest.java 24964 2008-11-11 17:56:07Z vcaballero $
* $Log$
* Revision 1.1  2006-11-08 10:57:55  jaume
* remove unecessary imports
*
*
*/
package com.iver.cit.gvsig.project;

import junit.framework.TestCase;

//TODO comentado para que compile
public class ProjectTest extends TestCase {
	static final String projectFile1 = "test/test.gvp";
	static final String projectFile2 = "test/test.gvp";
	static final String projectFile3 = null;
	static final String projectFile4 = null;

	static final String driversPath = "lib-test/drivers";
	Project p1, p2;

	public void setUp() {

//		LayerFactory.setDriversPath(driversPath);
//
//		Reader reader;
//
//		// TODO Install drivers support for testing
//		try {
//			reader = new FileReader(new File(projectFile1));
//
//			XmlTag tag = (XmlTag) XmlTag.unmarshal(reader);
//			XMLEntity xml=new XMLEntity(tag);
//			p1 = Project.createFromXML(xml);
//			p2 = Project.createFromXML(xml);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

	}

//	public void testSignature() {
//		try {
//			assertTrue(p1.computeSignature() == p2.computeSignature());
//		} catch (SaveException e) {}
////		assertTrue(p1.equals(p2));
//	}
}
