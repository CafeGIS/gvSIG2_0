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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public class MultiCurveTest extends TestCase {
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
	
	public void testCreateMultiCurve2D() throws InstantiationException, IllegalAccessException, CreateGeometryException{
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
		
		MultiCurve multiCurve = (MultiCurve)manager.create(TYPES.MULTICURVE, SUBTYPES.GEOM2D);
		multiCurve.addCurve(curve1);
		multiCurve.addCurve(curve2);
				
		Assert.assertEquals(2, multiCurve.getPrimitivesNumber());
		Assert.assertEquals(0.0, ((Curve)multiCurve.getPrimitiveAt(0)).getCoordinateAt(0, 0), 0);
		Assert.assertEquals(0.0, ((Curve)multiCurve.getPrimitiveAt(0)).getCoordinateAt(0, 1), 0);
		Assert.assertEquals(1.0, ((Curve)multiCurve.getPrimitiveAt(0)).getCoordinateAt(1, 0), 1);
		Assert.assertEquals(1.0, ((Curve)multiCurve.getPrimitiveAt(0)).getCoordinateAt(1, 1), 1);
		Assert.assertEquals(2.0, ((Curve)multiCurve.getPrimitiveAt(0)).getCoordinateAt(2, 0), 2);
		Assert.assertEquals(1.0, ((Curve)multiCurve.getPrimitiveAt(0)).getCoordinateAt(2, 1), 1);
		
		Assert.assertEquals(2, multiCurve.getPrimitivesNumber());
		Assert.assertEquals(0.0, ((Curve)multiCurve.getPrimitiveAt(1)).getCoordinateAt(0, 0), 0);
		Assert.assertEquals(0.0, ((Curve)multiCurve.getPrimitiveAt(1)).getCoordinateAt(0, 1), 0);
		Assert.assertEquals(1.0, ((Curve)multiCurve.getPrimitiveAt(1)).getCoordinateAt(1, 0), 1);
		Assert.assertEquals(1.0, ((Curve)multiCurve.getPrimitiveAt(1)).getCoordinateAt(1, 1), 1);
		Assert.assertEquals(2.0, ((Curve)multiCurve.getPrimitiveAt(1)).getCoordinateAt(2, 0), 2);
		Assert.assertEquals(1.0, ((Curve)multiCurve.getPrimitiveAt(1)).getCoordinateAt(2, 1), 1);
	}	
}

