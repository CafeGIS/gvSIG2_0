package org.gvsig.gpe.writer.schemas;

import org.gvsig.gpe.containers.CoordinatesSequence;
import org.gvsig.gpe.containers.Element;
import org.gvsig.gpe.containers.Feature;
import org.gvsig.gpe.containers.Layer;
import org.gvsig.xmlschema.som.IXSComplexTypeDefinition;
import org.gvsig.xmlschema.som.IXSContentType;

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
 * $Id: GPEFeatureWithSequenceElementTest.java 162 2007-06-29 12:19:48Z jorpiell $
 * $Log$
 * Revision 1.5  2007/06/29 12:19:14  jorpiell
 * The schema validation is made independently of the concrete writer
 *
 * Revision 1.4  2007/06/22 12:21:18  jorpiell
 * The typeNotFoundException has been deleted. It never was thrown
 *
 * Revision 1.3  2007/06/14 13:50:06  jorpiell
 * The schema jar name has been changed
 *
 * Revision 1.2  2007/06/08 11:34:57  jorpiell
 * IXSSchema interface updated
 *
 * Revision 1.1  2007/06/07 14:52:28  jorpiell
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
public abstract class GPEFeatureWithSequenceElementTest extends GPEWriterWithSchemaBaseTest {
	private String namespace = "http://www,gvsig.org/cit";
	private String layerId = "l1";
	private String layerName = "Points Layer";
	private String layerDescription = "This is a test of a points layer";
	private String layerSrs = "EPSG:23030";
	//private String layerXsElementName = "cities";
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
	//Schema 
	//private String xsLayerName = "cities";
	//private String xsLayerType = "citiesType";
	//private String xsLayerType_ = IXSComplexTypeDefinition.SEQUENCE;	
	private String xsFeature1Name = "city";
	private String xsFeature1Type = "cityType";	
	private String xsFeature1Type_ = IXSComplexTypeDefinition.SEQUENCE;	
	private String xsFeature1ContentType = IXSContentType.WITOUT_CONTENT;
	private String xsFeature1ContentRestriction = IXSContentType.WITOUT_RESTRICTION;	
	private String xsFeature1Element1Name = "Population";
	private String xsFeature1Element1Type = "xs:integer";
	private String xsFeature1Element2Name = "Country";
	private String xsFeature1Element2Type = "xs:string";
	private String xsFeature1Element3Name = "Capital";
	private String xsFeature1Element3Type = "xs:boolean";

	
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
		assertEquals(element1.getValue(), element1Value);
		//assertEquals(element1.getType(), element1Type);
		Element element2 = feature1.getElementAt(1);
		assertEquals(element2.getName(), element2Name);
		assertEquals(element2.getValue(), element2Value);
		//assertEquals(element2.getType(), element2Type);
		Element element3 = feature1.getElementAt(2);
		assertEquals(element3.getName(), element3Name);
		assertEquals(element3.getValue(), element3Value);
		//assertEquals(element3.getType(), element3Type);
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.GPEWriterBaseTest#writeObjects()
	 */
	public void writeObjects() {
		getWriterHandler().initialize();
		getWriterHandler().startLayer(layerId, null, layerName, layerDescription, layerSrs);
		getWriterHandler().startFeature(feature1Id, null, feature1Name);
		getWriterHandler().startPoint(point1Id, new CoordinatesSequence(point1X, point1Y, point1Z), layerSrs);
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
	 * @see org.gvsig.gpe.writers.schemas.GPEWriterWithSchemaBaseTest#writeSchema()
	 */
	public void writeSchema(){
		getSchema().addElement(
				xsFeature1Name,
				xsFeature1Type);			
		IXSComplexTypeDefinition complexType = getSchema().addComplexType(xsFeature1Type, xsFeature1Type_, xsFeature1ContentType, xsFeature1ContentRestriction);
		complexType.addElement(
				xsFeature1Element1Name,
				xsFeature1Element1Type);		
		complexType.addElement(
				xsFeature1Element2Name,
				xsFeature1Element2Type);	
		complexType.addElement(
				xsFeature1Element3Name,
				xsFeature1Element3Type);
	}	
}