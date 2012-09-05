package org.gvsig.gpe.writer;

import org.gvsig.gpe.containers.CoordinatesSequence;
import org.gvsig.gpe.containers.Feature;
import org.gvsig.gpe.containers.GeometryAsserts;
import org.gvsig.gpe.containers.Layer;
import org.gvsig.gpe.containers.Polygon;

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
 * $Id: GPEPolygonWithInnerTest.java 144 2007-06-07 14:53:59Z jorpiell $
 * $Log$
 * Revision 1.3  2007/06/07 14:52:28  jorpiell
 * Add the schema support
 *
 * Revision 1.2  2007/05/16 09:28:19  jorpiell
 * The polygons has to be closed
 *
 * Revision 1.1  2007/05/02 11:46:07  jorpiell
 * Writing tests updated
 *
 *
 */
/**
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public abstract class GPEPolygonWithInnerTest extends GPEWriterBaseTest{
	private String layerId = "l1";
	private String layerName = "Municipallity";
	private String layerDescription = "Polygons test layer";
	private String srs = "EPSG:23030";
	private String bboxId = "bboxID";
	private double[] bboxX = generateRandomBBox();
	private double[] bboxY = generateRandomBBox();
	private double[] bboxZ = generateRandomBBox();
	private String feature1Name = "Madrid";
	private String feature1Id = "f1";
	private String polygon1Id = "p1";
	private double[] polygon1X = generateRandomLinearRing();
	private double[] polygon1Y = generateRandomLinearRing();
	private double[] polygon1Z = generateRandomLinearRing();
	private String inner11Id = "inner11";
	private double[] inner11X = generateRandomLinearRing();
	private double[] inner11Y = generateRandomLinearRing();
	private double[] inner11Z = generateRandomLinearRing();
	private String inner12Id = "inner12";
	private double[] inner12X = generateRandomLinearRing();
	private double[] inner12Y = generateRandomLinearRing();
	private double[] inner12Z = generateRandomLinearRing();
	private String feature2Name = "Valencia";
	private String feature2Id = "f2";
	private String polygon2Id = "p2";
	private double[] polygon2X = generateRandomLinearRing();
	private double[] polygon2Y = generateRandomLinearRing();
	private double[] polygon2Z = generateRandomLinearRing();
	
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
		GeometryAsserts.polygon((Polygon)feature1.getGeometry(), polygon1X, polygon1Y, polygon1Z);
		Polygon inner1 = (Polygon)((Polygon)feature1.getGeometry()).getInnerBoundary().get(0);
		GeometryAsserts.polygon(inner1, inner11X, inner11Y, inner11Z);
		Polygon inner2 = (Polygon)((Polygon)feature1.getGeometry()).getInnerBoundary().get(1);
		GeometryAsserts.polygon(inner2, inner12X, inner12Y, inner12Z);
	
		//FEATURE 2
		Feature feature2 = (Feature)layer.getFeatures().get(1);
		GeometryAsserts.polygon((Polygon)feature2.getGeometry(), polygon2X, polygon2Y, polygon2Z);
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
		getWriterHandler().startPolygon(polygon1Id, new CoordinatesSequence(
				polygon1X,
				polygon1Y,
				polygon1Z),
				srs);
		getWriterHandler().startInnerBoundary(inner11Id, new CoordinatesSequence(
				inner11X,
				inner11Y,
				inner11Z),
				srs);
		getWriterHandler().endInnerBoundary();
		getWriterHandler().startInnerBoundary(inner12Id, new CoordinatesSequence(
				inner12X,
				inner12Y,
				inner12Z),
				srs);
		getWriterHandler().endInnerBoundary();
		getWriterHandler().endPolygon();		
		getWriterHandler().endFeature();
		getWriterHandler().startFeature(feature2Id, null, feature2Name);
		getWriterHandler().startPolygon(polygon2Id, new CoordinatesSequence(
				polygon2X,
				polygon2Y,
				polygon2Z),
				srs);
		getWriterHandler().endPolygon();		
		getWriterHandler().endFeature();
		getWriterHandler().endLayer();
		getWriterHandler().close();		
	}

}
