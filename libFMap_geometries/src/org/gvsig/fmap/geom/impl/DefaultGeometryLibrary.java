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

package org.gvsig.fmap.geom.impl;

import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.Geometry.TYPES;
import org.gvsig.fmap.geom.aggregate.impl.BaseMultiPrimitive2D;
import org.gvsig.fmap.geom.aggregate.impl.MultiCurve2D;
import org.gvsig.fmap.geom.aggregate.impl.MultiCurve2DZ;
import org.gvsig.fmap.geom.aggregate.impl.MultiPoint2D;
import org.gvsig.fmap.geom.aggregate.impl.MultiPoint2DZ;
import org.gvsig.fmap.geom.aggregate.impl.MultiSurface2D;
import org.gvsig.fmap.geom.primitive.impl.Arc2D;
import org.gvsig.fmap.geom.primitive.impl.Arc2DZ;
import org.gvsig.fmap.geom.primitive.impl.Circle2D;
import org.gvsig.fmap.geom.primitive.impl.Circle2DZ;
import org.gvsig.fmap.geom.primitive.impl.Curve2D;
import org.gvsig.fmap.geom.primitive.impl.Curve2DZ;
import org.gvsig.fmap.geom.primitive.impl.DefaultNullGeometry;
import org.gvsig.fmap.geom.primitive.impl.Ellipse2D;
import org.gvsig.fmap.geom.primitive.impl.Ellipse2DZ;
import org.gvsig.fmap.geom.primitive.impl.EllipticArc2D;
import org.gvsig.fmap.geom.primitive.impl.EllipticArc2DZ;
import org.gvsig.fmap.geom.primitive.impl.Point2D;
import org.gvsig.fmap.geom.primitive.impl.Point2DZ;
import org.gvsig.fmap.geom.primitive.impl.Solid2DZ;
import org.gvsig.fmap.geom.primitive.impl.Spline2D;
import org.gvsig.fmap.geom.primitive.impl.Spline2DZ;
import org.gvsig.fmap.geom.primitive.impl.Surface2D;
import org.gvsig.fmap.geom.primitive.impl.Surface2DZ;
import org.gvsig.tools.locator.BaseLibrary;
import org.gvsig.tools.locator.ReferenceNotRegisteredException;

/**
 * Registers the default implementation for {@link GeometryManager}
 *
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public class DefaultGeometryLibrary extends BaseLibrary  {

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.tools.locator.BaseLibrary#initialize()
	 */
	public void initialize() throws ReferenceNotRegisteredException {
        super.initialize();

        //Register the default GeometryManager
        GeometryLocator.registerGeometryManager(DefaultGeometryManager.class);
    }

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.tools.locator.BaseLibrary#postInitialize()
	 */
	public void postInitialize() {
		super.postInitialize();

		// Validate there is any implementation registered.
		GeometryManager geometryManager = GeometryLocator.getGeometryManager();
		if (geometryManager == null) {
			throw new ReferenceNotRegisteredException(
					GeometryLocator.GEOMETRY_MANAGER_NAME, GeometryLocator.getInstance());
		}
		//Register the geometries in 2D
		geometryManager.registerGeometryType(DefaultNullGeometry.class, "Null", TYPES.NULL, SUBTYPES.GEOM2D);
		geometryManager.registerGeometryType(Geometry2D.class, "Goemtry2D", TYPES.GEOMETRY, SUBTYPES.GEOM2D);
		geometryManager.registerGeometryType(Arc2D.class, "Arc2D", TYPES.ARC, SUBTYPES.GEOM2D);
        geometryManager.registerGeometryType(Circle2D.class, "Circle2D", TYPES.CIRCLE, SUBTYPES.GEOM2D);
        geometryManager.registerGeometryType(Curve2D.class, "Curve2D", TYPES.CURVE, SUBTYPES.GEOM2D);
        geometryManager.registerGeometryType(Ellipse2D.class, "Ellipse2D", TYPES.ELLIPSE, SUBTYPES.GEOM2D);
        geometryManager.registerGeometryType(EllipticArc2D.class, "EllipticArc2D", TYPES.ELLIPTICARC, SUBTYPES.GEOM2D);
        geometryManager.registerGeometryType(Point2D.class, "Point2D", TYPES.POINT, SUBTYPES.GEOM2D);
        geometryManager.registerGeometryType(Spline2D.class, "Spline2D", TYPES.SPLINE, SUBTYPES.GEOM2D);
        geometryManager.registerGeometryType(Surface2D.class, "Surface2D", TYPES.SURFACE, SUBTYPES.GEOM2D);
        geometryManager.registerGeometryType(BaseMultiPrimitive2D.class, "MultiPrimitive2D", TYPES.AGGREGATE, SUBTYPES.GEOM2D);
        geometryManager.registerGeometryType(MultiCurve2D.class, "MultiCurve2D", TYPES.MULTICURVE, SUBTYPES.GEOM2D);
        geometryManager.registerGeometryType(MultiCurve2DZ.class, "MultiCurve2DZ", TYPES.MULTICURVE, SUBTYPES.GEOM2DZ);
        geometryManager.registerGeometryType(MultiPoint2D.class, "MultiPoint2D", TYPES.MULTIPOINT, SUBTYPES.GEOM2D);
        geometryManager.registerGeometryType(MultiSurface2D.class, "MultiSurface", TYPES.MULTISURFACE, SUBTYPES.GEOM2D);

      //Register the geometries in 2DZ
		geometryManager.registerGeometryType(DefaultNullGeometry.class, TYPES.NULL, SUBTYPES.GEOM2DZ);
        geometryManager.registerGeometryType(Arc2DZ.class, TYPES.ARC, SUBTYPES.GEOM2DZ);
        geometryManager.registerGeometryType(Circle2DZ.class, TYPES.CIRCLE, SUBTYPES.GEOM2DZ);
        geometryManager.registerGeometryType(Curve2DZ.class, TYPES.CURVE, SUBTYPES.GEOM2DZ);
        geometryManager.registerGeometryType(Ellipse2DZ.class, TYPES.ELLIPSE, SUBTYPES.GEOM2DZ);
        geometryManager.registerGeometryType(EllipticArc2DZ.class, TYPES.ELLIPTICARC, SUBTYPES.GEOM2DZ);
        geometryManager.registerGeometryType(Point2DZ.class, TYPES.POINT, SUBTYPES.GEOM2DZ);
        geometryManager.registerGeometryType(Spline2DZ.class, TYPES.SPLINE, SUBTYPES.GEOM2DZ);
        geometryManager.registerGeometryType(Surface2DZ.class, TYPES.SURFACE, SUBTYPES.GEOM2DZ);
        geometryManager.registerGeometryType(MultiPoint2DZ.class, TYPES.MULTIPOINT, SUBTYPES.GEOM2DZ);
        geometryManager.registerGeometryType(Solid2DZ.class, TYPES.SOLID, SUBTYPES.GEOM2DZ);

       }
}

