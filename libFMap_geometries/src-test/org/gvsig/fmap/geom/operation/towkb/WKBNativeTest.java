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

package org.gvsig.fmap.geom.operation.towkb;

import junit.framework.TestCase;

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.Geometry.TYPES;
import org.gvsig.fmap.geom.impl.DefaultGeometryLibrary;
import org.gvsig.fmap.geom.operation.GeometryOperation;
import org.gvsig.fmap.geom.operation.fromwkb.FromWKB;
import org.gvsig.fmap.geom.operation.fromwkb.FromWKBGeometryOperationContext;
import org.gvsig.fmap.geom.operation.impl.DefaultGeometryOperationLibrary;
import org.gvsig.fmap.geom.primitive.Curve;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.geom.primitive.Point;
import org.gvsig.fmap.geom.primitive.Surface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public class WKBNativeTest extends TestCase {
	final static private Logger logger = LoggerFactory.getLogger("org.gvsig");
	private GeometryManager manager;
	private GeometryOperation toWkb2d;
	private GeometryOperation fromWkb2d;
	private FromWKBGeometryOperationContext fromWkbContext;

	protected void setUp() throws Exception {
		super.setUp();

		DefaultGeometryLibrary lib = new DefaultGeometryLibrary();
		lib.initialize();
		lib.postInitialize();

		DefaultGeometryOperationLibrary opLib = new DefaultGeometryOperationLibrary();
		opLib.initialize();
		opLib.postInitialize();

		manager = GeometryLocator.getGeometryManager();
		toWkb2d = manager.getGeometryOperation(ToWKBNative.CODE,
				Geometry.TYPES.GEOMETRY, Geometry.SUBTYPES.GEOM2D);
		fromWkb2d = manager.getGeometryOperation(FromWKB.CODE,
				Geometry.TYPES.GEOMETRY, Geometry.SUBTYPES.GEOM2D);
		fromWkbContext = new FromWKBGeometryOperationContext();
	}

	public void testPoint() throws Exception{
		Point point = (Point) manager.create(TYPES.POINT, SUBTYPES.GEOM2D);
		point.setX(1);
		point.setY(2);
		byte[] wkb = (byte[]) toWkb2d.invoke(point,
				null);
		fromWkbContext.setData(wkb);
		Geometry geom = (Geometry) fromWkb2d.invoke(null,
				fromWkbContext);

		assertEquals(point, geom);
	}

	public void testCurve() throws Exception {

		GeneralPathX gp = new GeneralPathX();

		gp.moveTo(0.0, 0.0);
		gp.lineTo(1.0, 1.0);
		gp.lineTo(2.0, 2.0);
		gp.lineTo(3.0, 3.0);
		Curve curve = (Curve) manager.create(TYPES.CURVE, SUBTYPES.GEOM2D);
		curve.setGeneralPath(gp);
		byte[] wkb = (byte[]) toWkb2d.invoke(curve,
				null);
		fromWkbContext.setData(wkb);
		Geometry geom = (Geometry) fromWkb2d.invoke(null,
				fromWkbContext);
		assertTrue(curve.equals(geom));
	}

	public void testPolygon() throws Exception {
		GeneralPathX gp = new GeneralPathX();
		gp.moveTo(1.0, 1.0);
		gp.lineTo(2.0, 2.0);
		gp.lineTo(3.0, 3.0);
		gp.lineTo(4.0, 4.0);
		gp.lineTo(5.0, 5.0);
		gp.closePath();

		Surface surface = manager.createSurface(gp, SUBTYPES.GEOM2D);

		byte[] wkb = (byte[]) toWkb2d.invoke(surface, null);
		fromWkbContext.setData(wkb);
		Geometry geom = (Geometry) fromWkb2d.invoke(null, fromWkbContext);
		assertTrue(surface.equals(geom));

	}

}

