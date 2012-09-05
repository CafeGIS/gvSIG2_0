package org.gvsig.gpe.kml.writer.v21.kml;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.gvsig.gpe.kml.writer.v21.kml.warnings.KMLNotMultiPointSupported;
import org.gvsig.gpe.kml.writer.v21.kml.warnings.KMLPolygonAutomaticallyClosedTest;

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
 * $Id: KMLWriterTestSuite.java 358 2008-01-09 17:50:58Z jpiera $
 * $Log$
 * Revision 1.5  2007/05/16 15:54:36  jorpiell
 * Add elements support
 *
 * Revision 1.4  2007/05/16 12:08:14  jorpiell
 * A multipoint layer is not supported
 *
 * Revision 1.3  2007/05/16 09:30:09  jorpiell
 * the writting methods has to have the errorHandler argument
 *
 * Revision 1.2  2007/05/09 08:36:24  jorpiell
 * Add the bbox to the layer
 *
 * Revision 1.1  2007/05/02 11:46:50  jorpiell
 * Writing tests updated
 *
 *
 */
/**
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class KMLWriterv21TestSuite {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.gvsig.gpe.kml.writers.kml");
		//$JUnit-BEGIN$
		suite.addTestSuite(KMLPolygonWithInnerTest.class);
		suite.addTestSuite(KMLLayerWithChildrenTest.class);
		suite.addTestSuite(KMLFeatureWithNameTest.class);
		suite.addTestSuite(KMLGeometryWithIdTest.class);
		suite.addTestSuite(KMLEmptyLayerTest.class);
		suite.addTestSuite(KMLLayerWithIdTest.class);
		suite.addTestSuite(KMLPointsLayerTest.class);
		suite.addTestSuite(KMLFeatureWithIdTest.class);
		suite.addTestSuite(KMLLayerWithDescriptionTest.class);
		suite.addTestSuite(KMLMultiGeometryLayerTest.class);
		suite.addTestSuite(KMLLayerWithNameTest.class);
		suite.addTestSuite(KMLLineStringLayerTest.class);
		suite.addTestSuite(KMLPolygonLayerTest.class);
		suite.addTestSuite(KMLLayerWithBboxTest.class);
		suite.addTestSuite(KMLPolygonAutomaticallyClosedTest.class);
		suite.addTestSuite(KMLNotMultiPointSupported.class);
		suite.addTestSuite(KMLFeatureWithElementsTest.class);
		suite.addTestSuite(KMLFeatureWithComplexElementTest.class);
		//$JUnit-END$
		return suite;
	}

}
