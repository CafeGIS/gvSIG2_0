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
public class EllipticArcTest extends TestCase {
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
	
	public void testEllipticArc2D() throws InstantiationException, IllegalAccessException, CreateGeometryException{
		EllipticArc ellipticArc = (EllipticArc)manager.create(TYPES.ELLIPTICARC, SUBTYPES.GEOM2D);
		GeneralPathX generalPathX = new GeneralPathX();
		Exception e = null;
		try{
			ellipticArc.setGeneralPath(generalPathX);
		}catch (UnsupportedOperationException e1){
			e = e1;
		}
		Assert.assertTrue(e instanceof UnsupportedOperationException);
		
		Point axis1Start = (Point)manager.create(TYPES.POINT, SUBTYPES.GEOM2D);
		axis1Start.setCoordinateAt(0, 0);
		axis1Start.setCoordinateAt(1, 0);
		
		Point axix1End = (Point)manager.create(TYPES.POINT, SUBTYPES.GEOM2D);
		axix1End.setCoordinateAt(0, 1);
		axix1End.setCoordinateAt(1, 1);
		
		double axis2Dist = 1;
		double angSt = 1;
		double angExt = 1;
		
		ellipticArc.setPoints(axis1Start, axix1End, axis2Dist, angSt, angExt);
		
		assertEquals(0, ellipticArc.getAxis1Start().getX(), 0);
		assertEquals(0, ellipticArc.getAxis1Start().getY(), 0);
		assertEquals(1, ellipticArc.getAxis1End().getX(), 0);
		assertEquals(1, ellipticArc.getAxis1End().getY(), 0);
		assertEquals(1, ellipticArc.getAxis2Dist(), axis2Dist);
		assertEquals(1, ellipticArc.getAngSt(), angSt);
		assertEquals(1, ellipticArc.getAngExt(), angExt);
	}	
}





