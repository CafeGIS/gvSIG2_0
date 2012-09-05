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
 
package org.gvsig.fmap.geom.primitive;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.Geometry.TYPES;
import org.gvsig.fmap.geom.exception.CreateGeometryException;
import org.gvsig.fmap.geom.impl.DefaultGeometryLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public class CircleTest extends TestCase {
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
	
	public void testCreateCircle2D() throws InstantiationException, IllegalAccessException, CreateGeometryException{
		Circle circle = (Circle)manager.create(TYPES.CIRCLE, SUBTYPES.GEOM2D);
		GeneralPathX generalPathX = new GeneralPathX();
		Exception e = null;
		try{
			circle.setGeneralPath(generalPathX);
		}catch (UnsupportedOperationException e1){
			e = e1;
		}
		Assert.assertTrue(e instanceof UnsupportedOperationException);
		
		Point center = (Point)manager.create(TYPES.POINT, SUBTYPES.GEOM2D);
		center.setCoordinateAt(0, 0);
		center.setCoordinateAt(1, 0);
		
		Point radious = (Point)manager.create(TYPES.POINT, SUBTYPES.GEOM2D);
		radious.setCoordinateAt(0, 1);
		radious.setCoordinateAt(1, 1);
				
		circle.setPoints(center, radious);
		
		assertEquals(0.0, circle.getCenter().getX(), 0);
		assertEquals(0.0, circle.getCenter().getY(), 0);
		assertEquals(Math.sqrt(2), circle.getRadious(), 0);
		
	}	
}



