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
 
package org.gvsig.fmap.geom.operation.println;

import junit.framework.TestCase;

import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.Geometry.TYPES;
import org.gvsig.fmap.geom.aggregate.MultiPoint;
import org.gvsig.fmap.geom.exception.CreateGeometryException;
import org.gvsig.fmap.geom.impl.DefaultGeometryLibrary;
import org.gvsig.fmap.geom.operation.GeometryOperationException;
import org.gvsig.fmap.geom.operation.GeometryOperationNotSupportedException;
import org.gvsig.fmap.geom.operation.impl.DefaultGeometryOperationLibrary;
import org.gvsig.fmap.geom.primitive.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public class PrintlnOperationTest extends TestCase {
	final static private Logger logger = LoggerFactory.getLogger("org.gvsig");
	private GeometryManager manager;
	
	protected void setUp() throws Exception {
		super.setUp();

		DefaultGeometryLibrary lib = new DefaultGeometryLibrary();		
		lib.initialize();
		lib.postInitialize();
		
		DefaultGeometryOperationLibrary opLib = new DefaultGeometryOperationLibrary();
		opLib.initialize();
		opLib.postInitialize();			
		
		manager = GeometryLocator.getGeometryManager();
		manager.registerGeometryOperation("println", new PrintPoint(), TYPES.POINT, SUBTYPES.GEOM2D);
		manager.registerGeometryOperation("println", new PrintMultiPoint(), TYPES.MULTIPOINT, SUBTYPES.GEOM2D);
	}
	
	public void testPoint() throws InstantiationException, IllegalAccessException, GeometryOperationNotSupportedException, GeometryOperationException, CreateGeometryException{
		Point point = (Point) manager.create(TYPES.POINT, SUBTYPES.GEOM2D);		
		assertEquals((String)manager.invokeOperation("println", point, null), "println-point");		
	}
	
	public void testMultiPoint() throws InstantiationException, IllegalAccessException, GeometryOperationNotSupportedException, GeometryOperationException, CreateGeometryException{
		Point point1 = (Point)manager.create(TYPES.POINT, SUBTYPES.GEOM2D);
		point1.setCoordinateAt(0, 1.0);
		point1.setCoordinateAt(1, 2.0);
		
		Point point2 = (Point)manager.create(TYPES.POINT, SUBTYPES.GEOM2D);
		point2.setCoordinateAt(0, 4.0);
		point2.setCoordinateAt(1, 5.0);
			
		MultiPoint multiPoint = (MultiPoint)manager.create(TYPES.MULTIPOINT, SUBTYPES.GEOM2D);
		multiPoint.addPoint(point1);
		multiPoint.addPoint(point2);
		
		assertEquals((String)manager.invokeOperation("println", multiPoint, null), "println-multipoint");	
		assertEquals((String)manager.invokeOperation("println", point1, null), "println-point");	
		assertEquals((String)manager.invokeOperation("println", point2, null), "println-point");			
						
		assertEquals((String)point1.invokeOperation("println", null), "println-point");	
		
	}
}



