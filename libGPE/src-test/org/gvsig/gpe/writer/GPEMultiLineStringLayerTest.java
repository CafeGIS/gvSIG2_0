package org.gvsig.gpe.writer;

import org.gvsig.gpe.containers.CoordinatesSequence;
import org.gvsig.gpe.containers.Feature;
import org.gvsig.gpe.containers.GeometryAsserts;
import org.gvsig.gpe.containers.Layer;
import org.gvsig.gpe.containers.MultiLineString;

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
 * $Id: GPEMultiLineStringLayerTest.java 144 2007-06-07 14:53:59Z jorpiell $
 * $Log$
 * Revision 1.5  2007/06/07 14:52:28  jorpiell
 * Add the schema support
 *
 * Revision 1.4  2007/05/09 10:03:19  jorpiell
 * Add the multigeometry tests
 *
 * Revision 1.3  2007/04/26 14:39:12  jorpiell
 * Add some tests
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
public abstract class GPEMultiLineStringLayerTest extends GPEWriterBaseTest{
	private String layerId = "l1";
	private String srs = "EPSG:23030";
	private String feature1Id = "f1";
	private String multiLineString1Id = "mp1";
	private String lineString1Id = "ls1";
	private double[] lineString1X = generateRandomCoordinates();
	private double[] lineString1Y = generateRandomCoordinates();
	private double[] lineString1Z = generateRandomCoordinates();	
	private String lineString2Id = "ls2";
	private double[] lineString2X = generateRandomCoordinates();
	private double[] lineString2Y = generateRandomCoordinates();
	private double[] lineString2Z = generateRandomCoordinates();	
	private String lineString3Id = "ls3";
	private double[] lineString3X = generateRandomCoordinates();
	private double[] lineString3Y = generateRandomCoordinates();
	private double[] lineString3Z = generateRandomCoordinates();	
	
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
		MultiLineString multiLinestring = (MultiLineString)feature1.getGeometry();
		assertEquals(multiLinestring.getGeometries().size(), 3);
		GeometryAsserts.lineString(multiLinestring.getMultiLineStringAt(0), lineString1X, lineString1Y, lineString1Z);
		GeometryAsserts.lineString(multiLinestring.getMultiLineStringAt(1), lineString2X, lineString2Y, lineString2Z);
		GeometryAsserts.lineString(multiLinestring.getMultiLineStringAt(2), lineString3X, lineString3Y, lineString3Z);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.GPEWriterBaseTest#writeObjects()
	 */
	public void writeObjects() {
		getWriterHandler().initialize();
		getWriterHandler().startLayer(layerId, null , null , srs, null);
		getWriterHandler().startFeature(feature1Id, null, null);
		getWriterHandler().startMultiLineString(multiLineString1Id, srs);
		getWriterHandler().startLineString(lineString1Id, new CoordinatesSequence(
				lineString1X,
				lineString1Y,
				lineString1Z), 
				srs);
		getWriterHandler().endLineString();		
		getWriterHandler().startLineString(lineString2Id, new CoordinatesSequence(
				lineString2X,
				lineString2Y,
				lineString2Z), 
				srs);
		getWriterHandler().endLineString();		
		getWriterHandler().startLineString(lineString3Id, new CoordinatesSequence(
				lineString3X,
				lineString3Y,
				lineString3Z), 
				srs);
		getWriterHandler().endLineString();		
		getWriterHandler().endMultiLineString();		
		getWriterHandler().endFeature();
		getWriterHandler().endLayer();
		getWriterHandler().close();	
	}

}
