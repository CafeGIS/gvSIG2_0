package org.gvsig.gpe.writer.schemas;

import org.gvsig.gpe.containers.Layer;
import org.gvsig.gpe.warnings.NotSupportedFeatureWarning;
//import org.gvsig.xmlschema.utils.TypeUtils;

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
 * $Id: GPENotSupportedFeatureTest.java 162 2007-06-29 12:19:48Z jorpiell $
 * $Log$
 * Revision 1.1  2007/06/29 12:19:14  jorpiell
 * The schema validation is made independently of the concrete writer
 *
 * Revision 1.1  2007/06/28 13:04:33  jorpiell
 * The Qname has been updated to the 1.5 JVM machine. The schema validation is made in the GPEWriterHandlerImplementor class
 *
 *
 */
/**
 * This test try to add a Feature with a type that doesn't exist 
 * in its layer schema
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public abstract class GPENotSupportedFeatureTest extends GPENotSupportedSchema {
	private String namespace = "http://www,gvsig.org/cit";
	private String layerId = "l1";
	private String layerName = "Parent layer";
	private String layerDescription = "This is a test of a wrong layer feature";
	private String layerSrs = "EPSG:23030";
	//private String layerXsElementName = "cities";
	private String feature1Name = "city";
	private String feature1Id = "f1";
	private String element1Name = "Population";
	private Integer element1Value = new Integer(30000);
	//private String element1Type = TypeUtils.getXSType(element1Value.getClass());
	//Schema 
	//private String xsFeature1Name = "river";

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.GPEWriterBaseTest#readObjects()
	 */
	public void readObjects() {
		Layer[] layers = getLayers();
		assertEquals(layers.length, 1);		
		Layer layer = layers[0];

		assertEquals(layer.getFeatures().size(),0);

		boolean featuretNotSupported = false;
		for (int i=0 ; i<getErrorHandler().getWarningsSize() ; i++){
			if (getErrorHandler().getWarningAt(i) instanceof NotSupportedFeatureWarning){
				featuretNotSupported = true;
			}			
		}
		assertTrue(featuretNotSupported);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.GPEWriterBaseTest#writeObjects()
	 */
	public void writeObjects() {			
		getWriterHandler().initialize();
		getWriterHandler().startLayer(layerId, null, layerName, layerDescription, layerSrs);
		getWriterHandler().startFeature(feature1Id, null, feature1Name);
		getWriterHandler().startElement(namespace,
				element1Name,
				element1Value);
		getWriterHandler().endElement();
		getWriterHandler().endFeature();		
		getWriterHandler().endLayer();
		getWriterHandler().close();	
	}

}
