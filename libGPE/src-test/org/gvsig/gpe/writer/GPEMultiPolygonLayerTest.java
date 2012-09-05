package org.gvsig.gpe.writer;

import org.gvsig.gpe.containers.CoordinatesSequence;
import org.gvsig.gpe.containers.Feature;
import org.gvsig.gpe.containers.GeometryAsserts;
import org.gvsig.gpe.containers.Layer;
import org.gvsig.gpe.containers.MultiPolygon;


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
 * $Id: GPEMultiPolygonLayerTest.java 144 2007-06-07 14:53:59Z jorpiell $
 * $Log$
 * Revision 1.7  2007/06/07 14:52:28  jorpiell
 * Add the schema support
 *
 * Revision 1.6  2007/05/16 09:28:19  jorpiell
 * The polygons has to be closed
 *
 * Revision 1.5  2007/05/09 10:03:19  jorpiell
 * Add the multigeometry tests
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
 * Revision 1.1  2007/04/13 13:14:55  jorpiell
 * Created the base tests and add some methods to the content handler
 *
 *
 */
/**
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public abstract class GPEMultiPolygonLayerTest extends GPEWriterBaseTest{
	private String layerId = "l1";
	private String srs = "EPSG:23030";
	private String feature1Id = "f1";
	private String multiPolygon1Id = "mp1";
	private String polygon1Id = "p1";
	private double[] polygon1X = generateRandomLinearRing();
	private double[] polygon1Y = generateRandomLinearRing();
	private double[] polygon1Z = generateRandomLinearRing();	
	private String polygon2Id = "p2";
	private double[] polygon2X = generateRandomLinearRing();
	private double[] polygon2Y = generateRandomLinearRing();
	private double[] polygon2Z = generateRandomLinearRing();	
	private String polygon3Id = "p3";
	private double[] polygon3X = generateRandomLinearRing();
	private double[] polygon3Y = generateRandomLinearRing();
	private double[] polygon3Z = generateRandomLinearRing();		
	
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
		MultiPolygon multiPolygon = (MultiPolygon)feature1.getGeometry();
		assertEquals(multiPolygon.getGeometries().size(), 3);
		GeometryAsserts.polygon(multiPolygon.getMultiPolygonAt(0), polygon1X, polygon1Y, polygon1Z);
		GeometryAsserts.polygon(multiPolygon.getMultiPolygonAt(1), polygon2X, polygon2Y, polygon2Z);
		GeometryAsserts.polygon(multiPolygon.getMultiPolygonAt(2), polygon3X, polygon3Y, polygon3Z);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.GPEWriterBaseTest#writeObjects()
	 */
	public void writeObjects() {
		getWriterHandler().initialize();
		getWriterHandler().startLayer(layerId, null , null , srs, null);
		getWriterHandler().startFeature(feature1Id, null, null);
		getWriterHandler().startMultiPolygon(multiPolygon1Id, srs);
		getWriterHandler().startPolygon(polygon1Id, new CoordinatesSequence(
				polygon1X,
				polygon1Y,
				polygon1Z),
				srs);		
		getWriterHandler().endPolygon();		
		getWriterHandler().startPolygon(polygon2Id, new CoordinatesSequence(
				polygon2X,
				polygon2Y,
				polygon2Z),
				srs);
		getWriterHandler().endPolygon();	
		getWriterHandler().startPolygon(polygon3Id, new CoordinatesSequence(
				polygon3X,
				polygon3Y,
				polygon3Z),
				srs);
		getWriterHandler().endPolygon();	
		getWriterHandler().endMultiPolygon();
		getWriterHandler().endFeature();
		getWriterHandler().endLayer();
		getWriterHandler().close();	
	}

}
