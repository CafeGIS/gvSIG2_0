package org.gvsig.gpe.writer;

import org.gvsig.gpe.containers.CoordinatesSequence;
import org.gvsig.gpe.containers.Feature;
import org.gvsig.gpe.containers.GeometryAsserts;
import org.gvsig.gpe.containers.Layer;
import org.gvsig.gpe.containers.LineString;
import org.gvsig.gpe.containers.MultiGeometry;
import org.gvsig.gpe.containers.Point;
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
 * $Id: GPEMultiGeometryLayerTest.java 144 2007-06-07 14:53:59Z jorpiell $
 * $Log$
 * Revision 1.4  2007/06/07 14:52:28  jorpiell
 * Add the schema support
 *
 * Revision 1.3  2007/05/16 09:28:19  jorpiell
 * The polygons has to be closed
 *
 * Revision 1.2  2007/05/09 10:03:19  jorpiell
 * Add the multigeometry tests
 *
 * Revision 1.1  2007/05/02 11:46:07  jorpiell
 * Writing tests updated
 *
 *
 */
/**
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public abstract class GPEMultiGeometryLayerTest extends GPEWriterBaseTest{
	private String layerId = "l1";
	private String srs = "EPSG:23030";
	private String feature1Id = "f1";
	private String multiGeometryId = "mg1";
	private String point1Id = "p1";
	private double point1X = generateRandomPoint();
	private double point1Y = generateRandomPoint();
	private double point1Z = generateRandomPoint();
	private String lineString1Id = "ls1";
	private double[] lineString1X = generateRandomCoordinates();
	private double[] lineString1Y = generateRandomCoordinates();
	private double[] lineString1Z = generateRandomCoordinates();	
	private String polygon1Id = "pol1";
	private double[] polygon1X = generateRandomLinearRing();
	private double[] polygon1Y = generateRandomLinearRing();
	private double[] polygon1Z = generateRandomLinearRing();	
	
	
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
		MultiGeometry multiGeometry = (MultiGeometry)feature1.getGeometry();
		assertEquals(multiGeometry.getGeometries().size(), 3);
		GeometryAsserts.point((Point)multiGeometry.getGeometryAt(0), point1X, point1Y, point1Z);
		GeometryAsserts.lineString((LineString)multiGeometry.getGeometryAt(1), lineString1X, lineString1Y, lineString1Z);
		GeometryAsserts.polygon((Polygon)multiGeometry.getGeometryAt(2), polygon1X, polygon1Y, polygon1Z);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.GPEWriterBaseTest#writeObjects()
	 */
	public void writeObjects() {
		getWriterHandler().initialize();
		getWriterHandler().startLayer(layerId, null , null , srs, null);
		getWriterHandler().startFeature(feature1Id, null, null);
		getWriterHandler().startMultiGeometry(multiGeometryId, srs);
		getWriterHandler().startPoint(point1Id, new CoordinatesSequence(point1X, point1Y, point1Z), srs);
		getWriterHandler().endPoint();	
		getWriterHandler().startLineString(lineString1Id, new CoordinatesSequence(
				lineString1X,
				lineString1Y,
				lineString1Z), 
				srs);
		getWriterHandler().endLineString();		
		getWriterHandler().startPolygon(polygon1Id, new CoordinatesSequence(
				polygon1X,
				polygon1Y,
				polygon1Z),
				srs);		
		getWriterHandler().endPolygon();		
		getWriterHandler().endMultiGeometry();		
		getWriterHandler().endFeature();
		getWriterHandler().endLayer();
		getWriterHandler().close();	
	}
}