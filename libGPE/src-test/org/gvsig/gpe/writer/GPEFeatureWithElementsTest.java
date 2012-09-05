package org.gvsig.gpe.writer;

import java.io.File;

import org.gvsig.gpe.containers.CoordinatesSequence;
import org.gvsig.gpe.containers.Element;
import org.gvsig.gpe.containers.Feature;
import org.gvsig.gpe.containers.Layer;
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
 * $Id: GPEFeatureWithElementsTest.java 150 2007-06-14 13:50:06Z jorpiell $
 * $Log$
 * Revision 1.4  2007/06/14 13:50:06  jorpiell
 * The schema jar name has been changed
 *
 * Revision 1.3  2007/06/07 14:52:28  jorpiell
 * Add the schema support
 *
 * Revision 1.2  2007/05/15 09:52:00  jorpiell
 * The namespace is deleted from the element name
 *
 * Revision 1.1  2007/05/02 11:46:07  jorpiell
 * Writing tests updated
 *
 *
 */
/**
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public abstract class GPEFeatureWithElementsTest extends GPEWriterBaseTest {
	private String namespace = "http://www,gvsig.org/cit";
	private String layerId = "l1";
	private String layerName = "Points Layer";
	private String layerDescription = "This is a test of a points layer";
	private String srs = "EPSG:23030";
	private String feature1Name = "New York";
	private String feature1Id = "f1";
	private String point1Id = "p1";
	private double point1X = generateRandomPoint();
	private double point1Y = generateRandomPoint();
	private double point1Z = generateRandomPoint();
	private String element1Name = "Population";
	private String element1Value = "30000";
	//private String element1Type = TypeUtils.getXSType(element1Value.getClass());
	private String element2Name = "Country";
	private String element2Value = "USA";
	//private String element2Type = TypeUtils.getXSType(element2Value.getClass());
	private String element3Name = "Capital";
	private String element3Value = "false";
	//private String element3Type = TypeUtils.getXSType(element3Value.getClass());
	
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
		Feature feature1 = (Feature)layer.getFeatures().get(0);
		Element element1 = feature1.getElementAt(0);
		assertEquals(element1.getName(), element1Name);
		assertEquals(element1.getValue(), element1Value.toString());
		//assertEquals(element1.getType(), element1Type);
		Element element2 = feature1.getElementAt(1);
		assertEquals(element2.getName(), element2Name);
		assertEquals(element2.getValue(), element2Value.toString());
		//assertEquals(element2.getType(), element2Type);
		Element element3 = feature1.getElementAt(2);
		assertEquals(element3.getName(), element3Name);
		assertEquals(element3.getValue(), element3Value.toString());
		//assertEquals(element3.getType(), element3Type);
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.GPEWriterBaseTest#writeObjects()
	 */
	public void writeObjects() {
		getWriterHandler().initialize();
		getWriterHandler().startLayer(layerId, null, layerName, layerDescription, srs);
		getWriterHandler().startFeature(feature1Id, null, feature1Name);
		getWriterHandler().startPoint(point1Id, new CoordinatesSequence(point1X, point1Y, point1Z), srs);
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
		getWriterHandler().endFeature();		
		getWriterHandler().endLayer();
		getWriterHandler().close();		
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.GPEWriterBaseTest#getSchemaPath()
	 */
	protected String getSchemaPath(){
		return new File("testdata/cities.xsd").getAbsolutePath();
	}
}