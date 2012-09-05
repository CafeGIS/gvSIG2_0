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
import org.gvsig.fmap.geom.primitive.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public class MultiPointTest extends TestCase {
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
	
	public void testCreateMultiPoint2D() throws InstantiationException, IllegalAccessException, CreateGeometryException{
		Point point1 = (Point)manager.create(TYPES.POINT, SUBTYPES.GEOM2DZ);
		point1.setCoordinateAt(0, 1.0);
		point1.setCoordinateAt(1, 2.0);
		point1.setCoordinateAt(2, 3.0);
		
		Point point2 = (Point)manager.create(TYPES.POINT, SUBTYPES.GEOM2DZ);
		point2.setCoordinateAt(0, 4.0);
		point2.setCoordinateAt(1, 5.0);
		point2.setCoordinateAt(2, 6.0);
		
		MultiPoint multiPoint = (MultiPoint)manager.create(TYPES.MULTIPOINT, SUBTYPES.GEOM2D);
		multiPoint.addPoint(point1);
		multiPoint.addPoint(point2);
		
		Assert.assertEquals(2, multiPoint.getPrimitivesNumber());
		Assert.assertEquals(1.0, ((Point)multiPoint.getPrimitiveAt(0)).getX(), 0);
		Assert.assertEquals(2.0, ((Point)multiPoint.getPrimitiveAt(0)).getY(), 0);
		Assert.assertEquals(3.0, ((Point)multiPoint.getPrimitiveAt(0)).getCoordinateAt(2), 0);
		Assert.assertEquals(4.0, ((Point)multiPoint.getPrimitiveAt(1)).getX(), 0);
		Assert.assertEquals(5.0, ((Point)multiPoint.getPrimitiveAt(1)).getY(), 0);
		Assert.assertEquals(6.0, ((Point)multiPoint.getPrimitiveAt(1)).getCoordinateAt(2), 0);
		
	}
	
}

