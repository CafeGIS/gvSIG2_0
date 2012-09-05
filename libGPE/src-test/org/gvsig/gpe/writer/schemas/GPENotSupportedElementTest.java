package org.gvsig.gpe.writer.schemas;

import org.gvsig.gpe.containers.CoordinatesSequence;
//import org.gvsig.gpe.containers.Feature;
import org.gvsig.gpe.containers.Layer;
import org.gvsig.gpe.warnings.NotSupportedElementWarning;

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
 * $Id: GPENotSupportedElementTest.java 162 2007-06-29 12:19:48Z jorpiell $
 * $Log$
 * Revision 1.1  2007/06/29 12:19:14  jorpiell
 * The schema validation is made independently of the concrete writer
 *
 * Revision 1.1  2007/06/28 13:04:33  jorpiell
 * The Qname has been updated to the 1.5 JVM machine. The schema validation is made in the GPEWriterHandlerImplementor class
 *
 * Revision 1.1  2007/06/22 12:21:18  jorpiell
 * The typeNotFoundException has been deleted. It never was thrown
 *
 *
 */
/**
 * This class is used to test the warning that is
 * thrown when the consumer application tries to write
 * an element that is not in the XML schema 
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public abstract class GPENotSupportedElementTest extends GPENotSupportedSchema {
	private String namespace = "http://www,gvsig.org/cit";
	private String layer1Id = "l1";
	private String layer1Name = "Parent layer";
	private String layer1Description = "This is a test of a wrong eleemnt";
	private String layer1Srs = "EPSG:23030";
	//private String layer1XsElementName = "cities";
	private String feature1Name = "city";
	private String feature1Id = "f1";
	private String point1Id = "p1";
	private double point1X = generateRandomPoint();
	private double point1Y = generateRandomPoint();
	private double point1Z = generateRandomPoint();
	private String element1Name = "Population";
	private Integer element1Value = new Integer(30000);
	private String element2Name = "Country";
	private String element2Value = "USA";
	private String element3Name = "Capital";
	private Boolean element3Value = new Boolean(false);
	private String element4Name = "Size";
	private Integer element4Value = new Integer(100000);
	//Schema 
	//private String xsFeature1Name = "city";
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.GPEWriterBaseTest#readObjects()
	 */
	public void readObjects() {
		Layer[] layers = getLayers();
		assertEquals(layers.length, 1);		
		Layer layer = layers[0];
	
		assertEquals(layer.getFeatures().size(), 1);
		//FEATURE 1
		//Feature feature1 = (Feature)layer.getFeatures().get(0);
		
		boolean elementNotSupported = false;
		for (int i=0 ; i<getErrorHandler().getWarningsSize() ; i++){
			if (getErrorHandler().getWarningAt(i) instanceof NotSupportedElementWarning){
				elementNotSupported = true;
			}			
		}
		assertTrue(elementNotSupported);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.GPEWriterBaseTest#writeObjects()
	 */
	public void writeObjects() {
		getWriterHandler().initialize();
		getWriterHandler().startLayer(layer1Id, null, layer1Name, layer1Description, layer1Srs);
		getWriterHandler().startFeature(feature1Id, null, feature1Name);
		getWriterHandler().startPoint(point1Id, new CoordinatesSequence(point1X, point1Y, point1Z), layer1Srs);
		getWriterHandler().endPoint();	
		getWriterHandler().startElement(namespace,
				element1Name,
				element1Value);
		getWriterHandler().endElement();
		getWriterHandler().startElement(namespace,
				element2Name,
				element2Value);
		getWriterHandler().endElement();
		getWriterHandler().startElement(namespace,
				element3Name,
				element3Value);
		getWriterHandler().endElement();
		getWriterHandler().startElement(namespace,
				element4Name,
				element4Value);
		getWriterHandler().endElement();
		getWriterHandler().endFeature();		
		getWriterHandler().endLayer();
		getWriterHandler().close();		
	}
}
