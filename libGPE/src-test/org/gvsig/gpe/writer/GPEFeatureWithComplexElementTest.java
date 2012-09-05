package org.gvsig.gpe.writer;

import java.io.File;

import org.gvsig.gpe.containers.CoordinatesSequence;
import org.gvsig.gpe.containers.Element;
import org.gvsig.gpe.containers.Feature;
import org.gvsig.gpe.containers.Layer;
import org.gvsig.xmlschema.utils.TypeUtils;

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
 * $Id: GPEFeatureWithComplexElementTest.java 150 2007-06-14 13:50:06Z jorpiell $
 * $Log$
 * Revision 1.4  2007/06/14 13:50:06  jorpiell
 * The schema jar name has been changed
 *
 * Revision 1.3  2007/06/07 14:52:28  jorpiell
 * Add the schema support
 *
 * Revision 1.2  2007/05/15 10:14:13  jorpiell
 * The complexType has not type
 *
 * Revision 1.1  2007/05/02 11:46:07  jorpiell
 * Writing tests updated
 *
 *
 */
/**
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public abstract class GPEFeatureWithComplexElementTest extends GPEWriterBaseTest {
	private String namespace = "http://www,gvsig.org/cit";
	private String layerId = "l1";
	private String layerName = "Points Layer";
	private String layerDescription = "This is a test of people";
	private String srs = "EPSG:23030";
	private String feature1Name = "New York";
	private String feature1Id = "f1";
	private String point1Id = "p1";
	private double point1X = generateRandomPoint();
	private double point1Y = generateRandomPoint();
	private double point1Z = generateRandomPoint();
	private String element1Name = "Name";
	private String element1Value = null;
	//private String element1Type = null;
	private String element11Name = "First Name";
	private String element11Value = "Jorge";
	private String element11Type = TypeUtils.getXSType(element11Value.getClass());
	private String element12Name = "Surname";
	private String element12Value = "Piera";
	private String element12Type = TypeUtils.getXSType(element12Value.getClass());
	
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
		//assertEquals(element1.getValue(), element1Value);
		//assertEquals(element1.getType(), element1Type);
		
		assertEquals(element1.getElements().size(),2);
		Element element11 = element1.getElementAt(0);
		assertEquals(element11.getName(), element11Name);
		assertEquals(element11.getValue(), element11Value);
		assertEquals(element11.getType(), element11Type);
		Element element12 = element1.getElementAt(1);
		assertEquals(element12.getName(), element12Name);
		assertEquals(element12.getValue(), element12Value);
		assertEquals(element12.getType(), element12Type);
		
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
		getWriterHandler().startElement(namespace,
				element11Name,
				element11Value);
		getWriterHandler().endElement();
		getWriterHandler().startElement(namespace,
				element12Name,
				element12Value);
		getWriterHandler().endElement();
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