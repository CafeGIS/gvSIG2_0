package org.gvsig.gpe.containers;

import junit.framework.TestCase;

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
 * $Id: GeometryAsserts.java 122 2007-05-15 12:15:50Z jorpiell $
 * $Log$
 * Revision 1.2  2007/05/15 12:15:50  jorpiell
 * Some nullPointerexceptions throwed
 *
 * Revision 1.1  2007/04/26 14:39:12  jorpiell
 * Add some tests
 *
 *
 */
/**
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class GeometryAsserts {

	public static void bbox(Bbox bbox, double[] x,double[] y, double[] z){
		TestCase.assertNotNull(bbox);
		if (bbox != null){
			TestCase.assertEquals(new Double(bbox.getMinX()), new Double(x[0]));
			TestCase.assertEquals(new Double(bbox.getMaxX()), new Double(x[1]));
			TestCase.assertEquals(new Double(bbox.getMinY()), new Double(y[0]));
			TestCase.assertEquals(new Double(bbox.getMaxY()), new Double(y[1]));
			TestCase.assertEquals(new Double(bbox.getMinZ()), new Double(z[0]));
			TestCase.assertEquals(new Double(bbox.getMaxZ()), new Double(z[1]));
		}
	}

	public static void point(Point point, double x,double y, double z){
		TestCase.assertNotNull(point);
		if (point != null){
			TestCase.assertEquals(new Double(point.getX()), new Double(x));
			TestCase.assertEquals(new Double(point.getY()), new Double(y));
			TestCase.assertEquals(new Double(point.getZ()), new Double(z));
		}
	}

	public static void lineString(LineString lineString, double[] x,double[] y, double[] z){
		TestCase.assertNotNull(lineString);
		if (lineString != null){
			for (int i=0 ; i<lineString.getCoordinatesNumber() ; i++){
				//for (int j=0 ; j<lineString.getDimension() ; j++){
				TestCase.assertEquals(new Double(lineString.geCoordinateAt(i,0)), new Double(x[i]));
				TestCase.assertEquals(new Double(lineString.geCoordinateAt(i,1)), new Double(y[i]));
				TestCase.assertEquals(new Double(lineString.geCoordinateAt(i,2)), new Double(z[i]));
				//}
			}
		}
	}

	public static void linearRing(LinearRing linearRing, double[] x,double[] y, double[] z){
		TestCase.assertNotNull(linearRing);
		if (linearRing != null){
			for (int i=0 ; i<linearRing.getCoordinatesNumber() ; i++){
				TestCase.assertEquals(new Double(linearRing.geCoordinateAt(i,0)), new Double(x[i]));
				TestCase.assertEquals(new Double(linearRing.geCoordinateAt(i,1)), new Double(y[i]));
				TestCase.assertEquals(new Double(linearRing.geCoordinateAt(i,2)), new Double(z[i]));
			}
		}
	}

	public static void polygon(Polygon polygon, double[] x,double[] y, double[] z){
		TestCase.assertNotNull(polygon);
		if (polygon != null){
			for (int i=0 ; i<polygon.getCoordinatesNumber() ; i++){
				TestCase.assertEquals(new Double(polygon.geCoordinateAt(i,0)), new Double(x[i]));
				TestCase.assertEquals(new Double(polygon.geCoordinateAt(i,1)), new Double(y[i]));
				TestCase.assertEquals(new Double(polygon.geCoordinateAt(i,2)), new Double(z[i]));
			}
		}
	}
}
