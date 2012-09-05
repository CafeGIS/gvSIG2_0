package org.gvsig.gpe.writer.warnings;

import org.gvsig.gpe.containers.CoordinatesSequence;
import org.gvsig.gpe.containers.Feature;
import org.gvsig.gpe.containers.GeometryAsserts;
import org.gvsig.gpe.containers.Layer;
import org.gvsig.gpe.containers.Polygon;
import org.gvsig.gpe.warnings.PolygonAutomaticallyClosedWarning;
import org.gvsig.gpe.writer.GPEWriterBaseTest;

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
 * $Id: GPEPolygonAutomaticallyClosedTest.java 144 2007-06-07 14:53:59Z jorpiell $
 * $Log$
 * Revision 1.2  2007/06/07 14:52:28  jorpiell
 * Add the schema support
 *
 * Revision 1.1  2007/05/16 09:28:34  jorpiell
 * The polygons has to be closed
 *
 *
 */
/**
 * This class creates a polygon without close it. When
 * the polygon is readed, it has had to be closed and
 * a warning has had to be throwed
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public abstract class GPEPolygonAutomaticallyClosedTest extends GPEWriterBaseTest{
	private String layerId = "l1";
	private String layerName = "Municipallity";
	private String layerDescription = "Polygons test layer";
	private String srs = "EPSG:23030";
	private String feature1Name = "Madrid";
	private String feature1Id = "f1";
	private String polygon1Id = "p1";
	private double[] polygon1X = generateRandomCoordinates();
	private double[] polygon1Y = generateRandomCoordinates();
	private double[] polygon1Z = generateRandomCoordinates();
	
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
		polygon1X = closePolygon(polygon1X);
		polygon1Y = closePolygon(polygon1Y);
		polygon1Z = closePolygon(polygon1Z);
		GeometryAsserts.polygon((Polygon)feature1.getGeometry(), polygon1X, polygon1Y, polygon1Z);
	
		//Two warinings
		boolean isClosed = false;
		for (int i=0 ; i<getErrorHandler().getWarningsSize() ; i++){
			Object obj = getErrorHandler().getWarningAt(i);
			if (obj instanceof PolygonAutomaticallyClosedWarning){
				isClosed = true;
			}
			System.out.println(getErrorHandler().getWarningAt(i));
		}
		assertTrue(isClosed);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.GPEWriterBaseTest#writeObjects()
	 */
	public void writeObjects() {
		polygon1X[polygon1X.length - 1] = 0.0;
		polygon1Y[polygon1Y.length - 1] = 0.0;
		polygon1Z[polygon1Z.length - 1]= 0.0;
		
		getWriterHandler().initialize();
		getWriterHandler().startLayer(layerId, null, layerName, layerDescription, srs);
		getWriterHandler().startFeature(feature1Id, null, feature1Name);
		getWriterHandler().startPolygon(polygon1Id, new CoordinatesSequence(
				polygon1X,
				polygon1Y,
				polygon1Z),
				srs);		
		getWriterHandler().endPolygon();		
		getWriterHandler().endFeature();
		getWriterHandler().endLayer();
		getWriterHandler().close();		
	}

}

