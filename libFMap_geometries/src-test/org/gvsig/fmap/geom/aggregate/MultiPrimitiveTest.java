/* gvSIG. Geographic Information System of the Valencian Government
*
* Copyright (C) 2007-2008 Infrastructures and Transports Department
* of the Valencian Government (CIT)
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
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
* MA  02110-1301, USA.
* 
*/

/*
* AUTHORS (In addition to CIT):
* 2009 {Iver T.I.}   {Task}
*/
 
package org.gvsig.fmap.geom.aggregate;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.Geometry.TYPES;
import org.gvsig.fmap.geom.exception.CreateGeometryException;
import org.gvsig.fmap.geom.impl.DefaultGeometryLibrary;
import org.gvsig.fmap.geom.primitive.Curve;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.geom.primitive.Point;
import org.gvsig.fmap.geom.primitive.Surface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public class MultiPrimitiveTest extends TestCase {
	private GeometryManager manager;
	
	final static private Logger logger = LoggerFactory.getLogger("org.gvsig");

	static {
		//logger.addAppender(new ConsoleAppender(new SimpleLayout()));
	}

	public void setUp() throws Exception {
		super.setUp();

		DefaultGeometryLibrary lib = new DefaultGeometryLibrary();		
		lib.initialize();
		lib.postInitialize();
		
		manager = GeometryLocator.getGeometryManager();
	}
	
	public void testCreateMultiGeometry() throws InstantiationException, IllegalAccessException, CreateGeometryException{
		Curve curve1 = (Curve)manager.create(TYPES.CURVE, SUBTYPES.GEOM2D);
		GeneralPathX generalPathX1 = new GeneralPathX();
		generalPathX1.moveTo(0, 0);
		generalPathX1.lineTo(1, 1);
		generalPathX1.lineTo(2, 1);		
		curve1.setGeneralPath(generalPathX1);	
		
		Curve curve2 = (Curve)manager.create(TYPES.CURVE, SUBTYPES.GEOM2D);
		GeneralPathX generalPathX2 = new GeneralPathX();
		generalPathX2.moveTo(0, 0);
		generalPathX2.lineTo(1, 1);
		generalPathX2.lineTo(2, 1);		
		curve2.setGeneralPath(generalPathX2);	
		
		Point point = (Point)manager.create(TYPES.POINT, SUBTYPES.GEOM2D);
		point.setCoordinateAt(0, 1.0);
		point.setCoordinateAt(1, 2.0);
		
		Surface surface = (Surface)manager.create(TYPES.SURFACE, SUBTYPES.GEOM2D);
		GeneralPathX generalPathX = new GeneralPathX();
		generalPathX.moveTo(0, 0);
		generalPathX.lineTo(1, 1);
		generalPathX.lineTo(2, 1);
		surface.setGeneralPath(generalPathX);	
		
		MultiPrimitive multiPrimitive = (MultiPrimitive)manager.create(TYPES.AGGREGATE, SUBTYPES.GEOM2D);
		multiPrimitive.addPrimitive(curve1);
		multiPrimitive.addPrimitive(point);
		multiPrimitive.addPrimitive(surface);
		multiPrimitive.addPrimitive(curve2);
						
		Assert.assertEquals(4, multiPrimitive.getPrimitivesNumber());
		Assert.assertEquals(0.0, ((Curve)multiPrimitive.getPrimitiveAt(0)).getCoordinateAt(0, 0), 0);
		Assert.assertEquals(0.0, ((Curve)multiPrimitive.getPrimitiveAt(0)).getCoordinateAt(0, 1), 0);
		Assert.assertEquals(1.0, ((Curve)multiPrimitive.getPrimitiveAt(0)).getCoordinateAt(1, 0), 1);
		Assert.assertEquals(1.0, ((Curve)multiPrimitive.getPrimitiveAt(0)).getCoordinateAt(1, 1), 1);
		Assert.assertEquals(2.0, ((Curve)multiPrimitive.getPrimitiveAt(0)).getCoordinateAt(2, 0), 2);
		Assert.assertEquals(1.0, ((Curve)multiPrimitive.getPrimitiveAt(0)).getCoordinateAt(2, 1), 1);
		
		Assert.assertEquals(1.0, ((Point)multiPrimitive.getPrimitiveAt(1)).getX(), 0);
		Assert.assertEquals(2.0, ((Point)multiPrimitive.getPrimitiveAt(1)).getY(), 0);
		
		Assert.assertEquals(0.0, ((Surface)multiPrimitive.getPrimitiveAt(2)).getCoordinateAt(0, 0), 0);
		Assert.assertEquals(0.0, ((Surface)multiPrimitive.getPrimitiveAt(2)).getCoordinateAt(0, 1), 0);
		Assert.assertEquals(1.0, ((Surface)multiPrimitive.getPrimitiveAt(2)).getCoordinateAt(1, 0), 1);
		Assert.assertEquals(1.0, ((Surface)multiPrimitive.getPrimitiveAt(2)).getCoordinateAt(1, 1), 1);
		Assert.assertEquals(2.0, ((Surface)multiPrimitive.getPrimitiveAt(2)).getCoordinateAt(2, 0), 2);
		Assert.assertEquals(1.0, ((Surface)multiPrimitive.getPrimitiveAt(2)).getCoordinateAt(2, 1), 1);

		Assert.assertEquals(0.0, ((Curve)multiPrimitive.getPrimitiveAt(3)).getCoordinateAt(0, 0), 0);
		Assert.assertEquals(0.0, ((Curve)multiPrimitive.getPrimitiveAt(3)).getCoordinateAt(0, 1), 0);
		Assert.assertEquals(1.0, ((Curve)multiPrimitive.getPrimitiveAt(3)).getCoordinateAt(1, 0), 1);
		Assert.assertEquals(1.0, ((Curve)multiPrimitive.getPrimitiveAt(3)).getCoordinateAt(1, 1), 1);
		Assert.assertEquals(2.0, ((Curve)multiPrimitive.getPrimitiveAt(3)).getCoordinateAt(2, 0), 2);
		Assert.assertEquals(1.0, ((Curve)multiPrimitive.getPrimitiveAt(3)).getCoordinateAt(2, 1), 1);
	}	
}

