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
public class EllipseTest extends TestCase {
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
	
	public void testEllipse2D() throws InstantiationException, IllegalAccessException, CreateGeometryException{
		Ellipse ellipse = (Ellipse)manager.create(TYPES.ELLIPSE, SUBTYPES.GEOM2D);
		GeneralPathX generalPathX = new GeneralPathX();
		Exception e = null;
		try{
			ellipse.setGeneralPath(generalPathX);
		}catch (UnsupportedOperationException e1){
			e = e1;
		}
		Assert.assertTrue(e instanceof UnsupportedOperationException);
		
		Point initPoint = (Point)manager.create(TYPES.POINT, SUBTYPES.GEOM2D);
		initPoint.setCoordinateAt(0, 0);
		initPoint.setCoordinateAt(1, 0);
		
		Point endPoint = (Point)manager.create(TYPES.POINT, SUBTYPES.GEOM2D);
		endPoint.setCoordinateAt(0, 1);
		endPoint.setCoordinateAt(1, 1);
		
		double axisLength = 1;
		
		ellipse.setPoints(initPoint, endPoint, axisLength);
		
		assertEquals(0, ellipse.getAxis1Start().getX(), 0);
		assertEquals(0, ellipse.getAxis1Start().getY(), 0);
		assertEquals(1, ellipse.getAxis1End().getX(), 0);
		assertEquals(1, ellipse.getAxis1End().getY(), 0);
		assertEquals(1, ellipse.getAxis2Dist(), axisLength);
	;
	}	
}



