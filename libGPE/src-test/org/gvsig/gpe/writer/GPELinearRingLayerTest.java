package org.gvsig.gpe.writer;

import org.gvsig.gpe.containers.CoordinatesSequence;
import org.gvsig.gpe.containers.Feature;
import org.gvsig.gpe.containers.GeometryAsserts;
import org.gvsig.gpe.containers.Layer;
import org.gvsig.gpe.containers.LinearRing;


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
 * $Id: GPELinearRingLayerTest.java 144 2007-06-07 14:53:59Z jorpiell $
 * $Log$
 * Revision 1.6  2007/06/07 14:52:28  jorpiell
 * Add the schema support
 *
 * Revision 1.5  2007/05/02 11:46:07  jorpiell
 * Writing tests updated
 *
 * Revision 1.4  2007/04/26 14:39:12  jorpiell
 * Add some tests
 *
 * Revision 1.3  2007/04/19 11:50:20  csanchez
 * Actualizacion protoripo libGPE
 *
 * Revision 1.2  2007/04/14 16:06:35  jorpiell
 * Add the container classes
 *
 * Revision 1.1  2007/04/13 07:17:54  jorpiell
 * Add the writting tests for the simple geometries
 *
 *
 */
/**
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public abstract class GPELinearRingLayerTest extends GPEWriterBaseTest{
	private String layerId = "l1";
	private String layerName = "Line String Layer";
	private String layerDescription = "This is a linear ring test";
	private String srs = "EPSG:23030";
	private String bboxId = "bboxID";
	private double[] bboxX = generateRandomBBox();
	private double[] bboxY = generateRandomBBox();
	private double[] bboxZ = generateRandomBBox();
	private String feature1Name = "Turia";
	private String feature1Id = "f1";
	private String linearRing1Id = "p1";
	private double[] linearRing1X = generateRandomLinearRing();
	private double[] linearRing1Y = generateRandomLinearRing();
	private double[] linearRing1Z = generateRandomLinearRing();	
	private String feature2Name = "Los Angeles";
	private String feature2Id = "f2";
	private String linearRing2Id = "p2";
	private double[] linearRing2X = generateRandomLinearRing();
	private double[] linearRing2Y = generateRandomLinearRing();
	private double[] linearRing2Z = generateRandomLinearRing();

	
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
		GeometryAsserts.linearRing((LinearRing)feature1.getGeometry(), linearRing1X, linearRing1Y, linearRing1Z);

		//FEATURE 2
		Feature feature2 = (Feature)layer.getFeatures().get(1);
		GeometryAsserts.linearRing((LinearRing)feature2.getGeometry(), linearRing2X, linearRing2Y, linearRing2Z);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.GPEWriterBaseTest#writeObjects()
	 */
	public void writeObjects() {
		getWriterHandler().initialize();
		getWriterHandler().startLayer(layerId, null, layerName, layerDescription, srs);
		getWriterHandler().startBbox(bboxId, new CoordinatesSequence(bboxX,	bboxY, bboxZ), srs);
		getWriterHandler().endBbox();
		getWriterHandler().startFeature(feature1Id, null, feature1Name);
		getWriterHandler().startLinearRing(linearRing1Id, new CoordinatesSequence(
				linearRing1X,
				linearRing1Y,
				linearRing1Z), 
				srs);
		getWriterHandler().endLinearRing();		
		getWriterHandler().endFeature();
		getWriterHandler().startFeature(feature2Id, null, feature2Name);
		getWriterHandler().startLinearRing(linearRing2Id, new CoordinatesSequence(
				linearRing2X,
				linearRing2Y,
				linearRing2Z), 
				srs);
		getWriterHandler().endLinearRing();		
		getWriterHandler().endFeature();
		getWriterHandler().endLayer();
		getWriterHandler().close();		
	}
}
