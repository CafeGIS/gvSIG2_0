package org.gvsig.gpe.writer;

import org.gvsig.gpe.containers.CoordinatesSequence;
import org.gvsig.gpe.containers.Feature;
import org.gvsig.gpe.containers.GeometryAsserts;
import org.gvsig.gpe.containers.Layer;
import org.gvsig.gpe.containers.MultiPoint;


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
 * $Id: GPEMultiPointLayerTest.java 144 2007-06-07 14:53:59Z jorpiell $
 * $Log$
 * Revision 1.7  2007/06/07 14:52:28  jorpiell
 * Add the schema support
 *
 * Revision 1.6  2007/05/16 12:07:25  jorpiell
 * A method name changed
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
public abstract class GPEMultiPointLayerTest extends GPEWriterBaseTest{
	private String layerId = "l1";
	private String srs = "EPSG:23030";
	private String feature1Id = "f1";
	private String multiPoint1Id = "mp1";
	private String point1Id = "p1";
	private double point1X = generateRandomPoint();
	private double point1Y = generateRandomPoint();
	private double point1Z = generateRandomPoint();
	private String point2Id = "p2";
	private double point2X = generateRandomPoint();
	private double point2Y = generateRandomPoint();
	private double point2Z = generateRandomPoint();
	private String point3Id = "p3";
	private double point3X = generateRandomPoint();
	private double point3Y = generateRandomPoint();
	private double point3Z = generateRandomPoint();
	
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
		MultiPoint multiPoint = (MultiPoint)feature1.getGeometry();
		assertEquals(multiPoint.getGeometries().size(), 3);
		GeometryAsserts.point(multiPoint.getMultiPointAt(0), point1X, point1Y, point1Z);
		GeometryAsserts.point(multiPoint.getMultiPointAt(1), point2X, point2Y, point2Z);
		GeometryAsserts.point(multiPoint.getMultiPointAt(2), point3X, point3Y, point3Z);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.GPEWriterBaseTest#writeObjects()
	 */
	public void writeObjects() {
		getWriterHandler().initialize();
		getWriterHandler().startLayer(layerId, null , null , srs, null);
		getWriterHandler().startFeature(feature1Id, null, null);
		getWriterHandler().startMultiPoint(multiPoint1Id, srs);
		getWriterHandler().startPoint(point1Id, new CoordinatesSequence(point1X, point1Y, point1Z), srs);
		getWriterHandler().endPoint();		
		getWriterHandler().startPoint(point2Id, new CoordinatesSequence(point2X, point2Y, point2Z), srs);
		getWriterHandler().endPoint();		
		getWriterHandler().startPoint(point3Id, new CoordinatesSequence(point3X, point3Y, point3Z), srs);
		getWriterHandler().endPoint();		
		getWriterHandler().endMultiPoint();		
		getWriterHandler().endFeature();
		getWriterHandler().endLayer();
		getWriterHandler().close();	
	}

}
