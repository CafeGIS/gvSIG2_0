package org.gvsig.gpe.gml.writer.v2;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.gvsig.gpe.gml.writer.v2.schemas.GMLFeatureWithSequenceElementTest;
import org.gvsig.gpe.gml.writer.v2.schemas.GMLNotSupportedChildLayerTest;
import org.gvsig.gpe.gml.writer.v2.schemas.GMLNotSupportedElementTest;
import org.gvsig.gpe.gml.writer.v2.schemas.GMLNotSupportedFeatureTest;
import org.gvsig.gpe.gml.writer.v2.schemas.GMLNotSupportedLayerTest;
import org.gvsig.gpe.gml.writer.v2.warnings.GMLPolygonAutomaticallyClosedTest;

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
 * $Id: GMLWritersTestSuite.java 162 2007-06-29 12:19:48Z jorpiell $
 * $Log$
 * Revision 1.8  2007/06/29 12:19:34  jorpiell
 * The schema validation is made independently of the concrete writer
 *
 * Revision 1.7  2007/06/28 13:05:09  jorpiell
 * The Qname has been updated to the 1.5 JVM machine. The schema validation is made in the GPEWriterHandlerImplementor class
 *
 * Revision 1.6  2007/06/22 12:22:53  jorpiell
 * The typeNotFoundException has been deleted. It never was thrown
 *
 * Revision 1.5  2007/06/07 14:53:30  jorpiell
 * Add the schema support
 *
 * Revision 1.4  2007/05/16 09:29:31  jorpiell
 * The polygons has to be closed
 *
 * Revision 1.3  2007/05/15 12:10:01  jorpiell
 * The bbox is linked to the feature
 *
 * Revision 1.2  2007/05/14 11:40:27  jorpiell
 * Add the writting tests
 *
 *
 */
/**
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class GMLv2WritersTestSuite {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.gvsig.gpe.gml.writers");
		//$JUnit-BEGIN$
		suite.addTestSuite(GMLLayerWithNameTest.class);
		suite.addTestSuite(GMLMultiPointLayerTest.class);
		suite.addTestSuite(GMLFeatureWithBboxTest.class);
		suite.addTestSuite(GMLLineStringLayerTest.class);
		suite.addTestSuite(GMLFeatureWithElementsTest.class);
		suite.addTestSuite(GMLFeatureWithIdTest.class);
		suite.addTestSuite(GMLFeatureWithNameTest.class);
		suite.addTestSuite(GMLPolygonLayerTest.class);
		suite.addTestSuite(GMLEmptyLayerTest.class);
		suite.addTestSuite(GMLFeatureWithComplexElementTest.class);
		suite.addTestSuite(GMLLayerWithDescriptionTest.class);
		suite.addTestSuite(GMLLayerWithIdTest.class);
		suite.addTestSuite(GMLLayerWithBboxTest.class);
		suite.addTestSuite(GMLLayerHeaderTest.class);
		suite.addTestSuite(GMLMultiPolygonLayerTest.class);
		suite.addTestSuite(GMLMultiLineStringLayerTest.class);
		suite.addTestSuite(GMLMultiGeometryLayerTest.class);
		suite.addTestSuite(GMLGeometryWithIdTest.class);
		suite.addTestSuite(GMLPointsLayerTest.class);
		suite.addTestSuite(GMLLinearRingLayerTest.class);
		suite.addTestSuite(GMLPolygonAutomaticallyClosedTest.class);
		suite.addTestSuite(GMLFeatureWithSequenceElementTest.class);
		suite.addTestSuite(GMLNotSupportedLayerTest.class);
		suite.addTestSuite(GMLNotSupportedChildLayerTest.class);
		suite.addTestSuite(GMLNotSupportedFeatureTest.class);
		suite.addTestSuite(GMLNotSupportedElementTest.class);
		//$JUnit-END$
		return suite;
	}

}
