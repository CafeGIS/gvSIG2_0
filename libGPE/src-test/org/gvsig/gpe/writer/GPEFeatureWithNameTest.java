package org.gvsig.gpe.writer;

import org.gvsig.gpe.containers.CoordinatesSequence;
import org.gvsig.gpe.containers.Feature;
import org.gvsig.gpe.containers.Layer;

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
 * $Id: GPEFeatureWithNameTest.java 144 2007-06-07 14:53:59Z jorpiell $
 * $Log$
 * Revision 1.2  2007/06/07 14:52:28  jorpiell
 * Add the schema support
 *
 * Revision 1.1  2007/05/02 11:46:07  jorpiell
 * Writing tests updated
 *
 *
 */
/**
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public abstract class GPEFeatureWithNameTest extends GPEWriterBaseTest {
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
	private String feature2Name = "Los Angeles";
	private String feature2Id = "f2";
	private String point2Id = "p2";
	private double point2X = generateRandomPoint();
	private double point2Y = generateRandomPoint();
	private double point2Z = generateRandomPoint();
	
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.GPEWriterBaseTest#readObjects()
	 */
	public void readObjects() {
		Layer[] layers = getLayers();
		assertEquals(layers.length, 1);		
		Layer layer = layers[0];
	
		assertEquals(layer.getFeatures().size(), 2);
		//FEATURE 1
		Feature feature1 = (Feature)layer.getFeatures().get(0);
		assertEquals(feature1.getName(), feature1Name);
		
		//FEATURE 2
		Feature feature2 = (Feature)layer.getFeatures().get(1);
		assertEquals(feature2.getName(), feature2Name);		
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
		getWriterHandler().endFeature();
		getWriterHandler().startFeature(feature2Id, null, feature2Name);
		getWriterHandler().startPoint(point2Id, new CoordinatesSequence(point2X, point2Y, point2Z), srs);
		getWriterHandler().endPoint();		
		getWriterHandler().endFeature();
		getWriterHandler().endLayer();
		getWriterHandler().close();		
	}
}
