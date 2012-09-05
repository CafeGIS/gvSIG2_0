package org.gvsig.gpe.gml.parser;

import junit.framework.Test;
import junit.framework.TestSuite;

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
 * $Id: GMLReadersTestSuite.java 346 2008-01-08 15:35:58Z jpiera $
 * $Log$
 * Revision 1.6  2007/06/11 06:17:36  jorpiell
 * Add a new GML test
 *
 * Revision 1.5  2007/06/08 13:01:12  jorpiell
 * Add the targetNamespace to the file
 *
 * Revision 1.4  2007/05/16 12:34:55  csanchez
 * GPEParser Prototipo final de lectura
 *
 * Revision 1.3  2007/04/25 11:08:38  csanchez
 * Parseo correcto con XSOM de esquemas, EntityResolver para los imports
 *
 * Revision 1.2  2007/04/20 08:33:37  jorpiell
 * Test updated
 *
 * Revision 1.1  2007/04/20 08:13:43  csanchez
 * Actualizacion protoripo libGPE
 *
 *
 */
/**
 * @author Carlos Sánchez Periñán (sanchez_carper@gva.es)
 */
public class GMLReadersTestSuite {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.gvsig.gpe.gml.readers");
		//$JUnit-BEGIN$
		suite.addTestSuite(GMLLinesReader.class);
		suite.addTestSuite(GMLPointsReader.class);
		suite.addTestSuite(GMLEuroroadsReader.class);
		suite.addTestSuite(GMLNomenclatorIdeeReader.class);
		suite.addTestSuite(GMLAforosReader.class);
		suite.addTestSuite(GMLCurvesReader.class);
		suite.addTestSuite(GMLIDEECosntruccionesReader.class);
		suite.addTestSuite(GMLfromSHPReader.class);
		suite.addTestSuite(GMLCitiesReader.class);
		suite.addTestSuite(GMLMultiSurfaceReader.class);
		suite.addTestSuite(GMLProvinciasIDEEReader.class);
		suite.addTestSuite(GMLMultiPolygonTest.class);
		suite.addTestSuite(GMLMultiLineStringTest.class);
		//$JUnit-END$
		return suite;
	}

}
