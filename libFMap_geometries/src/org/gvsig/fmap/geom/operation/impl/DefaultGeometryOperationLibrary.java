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
 
package org.gvsig.fmap.geom.operation.impl;

import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.Geometry.TYPES;
import org.gvsig.fmap.geom.aggregate.impl.BaseMultiPrimitive2D;
import org.gvsig.fmap.geom.aggregate.impl.MultiCurve2D;
import org.gvsig.fmap.geom.aggregate.impl.MultiPoint2D;
import org.gvsig.fmap.geom.aggregate.impl.MultiPoint2DZ;
import org.gvsig.fmap.geom.aggregate.impl.MultiSurface2D;
import org.gvsig.fmap.geom.impl.DefaultGeometryManager;
import org.gvsig.fmap.geom.operation.distance.PointDistance;
import org.gvsig.fmap.geom.operation.ensureOrientation.EnsureOrientation;
import org.gvsig.fmap.geom.operation.flip.Flip;
import org.gvsig.fmap.geom.operation.fromwkb.FromWKB;
import org.gvsig.fmap.geom.operation.fromwkt.FromWKT;
import org.gvsig.fmap.geom.operation.isCCW.IsCCW;
import org.gvsig.fmap.geom.operation.relationship.Contains;
import org.gvsig.fmap.geom.operation.relationship.Crosses;
import org.gvsig.fmap.geom.operation.relationship.Disjoint;
import org.gvsig.fmap.geom.operation.relationship.Equals;
import org.gvsig.fmap.geom.operation.relationship.Intersects;
import org.gvsig.fmap.geom.operation.relationship.Overlaps;
import org.gvsig.fmap.geom.operation.relationship.Touches;
import org.gvsig.fmap.geom.operation.relationship.Within;
import org.gvsig.fmap.geom.operation.tojts.ToJTS;
import org.gvsig.fmap.geom.operation.towkb.ToWKB;
import org.gvsig.fmap.geom.operation.towkb.ToWKBNative;
import org.gvsig.fmap.geom.operation.towkt.ToWKT;
import org.gvsig.fmap.geom.operation.utils.PointGetAngle;
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
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public class DefaultGeometryOperationLibrary extends BaseLibrary  {

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.tools.locator.BaseLibrary#initialize()
	 */
	public void initialize() throws ReferenceNotRegisteredException {
		super.initialize();

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
		
		geometryManager.registerGeometryOperation("contains", new Contains());
		geometryManager.registerGeometryOperation("crosses", new Crosses());
		geometryManager.registerGeometryOperation("disjoint", new Disjoint());
		geometryManager.registerGeometryOperation("equals", new Equals());
		geometryManager.registerGeometryOperation("intersects", new Intersects());
		geometryManager.registerGeometryOperation("overlaps", new Overlaps());
		geometryManager.registerGeometryOperation("touches", new Touches());
		geometryManager.registerGeometryOperation("within", new Within());
		geometryManager.registerGeometryOperation("isCCW", new IsCCW());
		geometryManager.registerGeometryOperation("fromWKT", new FromWKT());
		geometryManager.registerGeometryOperation("fromWKB", new FromWKB());
		geometryManager.registerGeometryOperation("flip", new Flip());	
		geometryManager.registerGeometryOperation("ensureOrientation",  new EnsureOrientation());		
		geometryManager.registerGeometryOperation("toWKB",  new ToWKB());
		geometryManager.registerGeometryOperation("toWKBNative",  new ToWKBNative());
		geometryManager.registerGeometryOperation("toWKT", new ToWKT());
		geometryManager.registerGeometryOperation("distance",  new PointDistance(), TYPES.POINT);
		geometryManager.registerGeometryOperation("toJTS", new ToJTS());
		geometryManager.registerGeometryOperation("getAngle", new PointGetAngle());
	}	
}

