package org.gvsig.gpe.kml.writer.v21.kml.warnings;

import org.gvsig.gpe.containers.Feature;
import org.gvsig.gpe.containers.Layer;
import org.gvsig.gpe.containers.MultiGeometry;
import org.gvsig.gpe.warnings.FeatureNotSupportedWarning;
import org.gvsig.gpe.writer.GPEMultiPointLayerTest;

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
 * $Id:KMLNotMultiPointSupported.java 358 2008-01-09 17:50:58Z jpiera $
 * $Log$
 * Revision 1.3  2007/06/29 12:19:48  jorpiell
 * The schema validation is made independently of the concrete writer
 *
 * Revision 1.2  2007/06/07 14:53:59  jorpiell
 * Add the schema support
 *
 * Revision 1.1  2007/05/16 12:08:14  jorpiell
 * A multipoint layer is not supported
 *
 *
 */
/**
 * This test is made to try the conversion between a multipoint
 * layer ans a multigeometry layer that the KML parser make.
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class KMLNotMultiPointSupported extends GPEMultiPointLayerTest {
	
	public void readObjects() {
		Layer[] layers = getLayers();
		assertEquals(layers.length, 1);		
		Layer layer = layers[0];
	
		assertEquals(layer.getFeatures().size(), 1);
		//FEATURE 1
		Feature feature1 = (Feature)layer.getFeatures().get(0);
		MultiGeometry multiGeometry = (MultiGeometry)feature1.getGeometry();
		assertEquals(multiGeometry.getGeometries().size(), 3);

		assertEquals(getErrorHandler().getWarningsSize(),1);
		assertTrue(getErrorHandler().getWarningAt(0) instanceof FeatureNotSupportedWarning);
		for (int i=0 ; i<getErrorHandler().getWarningsSize() ; i++){
			System.out.println(getErrorHandler().getWarningAt(i));
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.GPEWriterBaseTest#getGPEParserClass()
	 */
	public Class getGPEParserClass() {
		return org.gvsig.gpe.kml.parser.GPEKml2_1_Parser.class;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.GPEWriterBaseTest#getGPEWriterHandlerClass()
	 */
	public Class getGPEWriterHandlerClass() {
		return org.gvsig.gpe.kml.writer.GPEKml21WriterHandlerImplementor.class;
	}
}
