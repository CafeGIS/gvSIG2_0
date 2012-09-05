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
 
package org.gvsig.fmap.geom.operation.distance;

import java.awt.geom.Point2D;

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.Geometry.TYPES;
import org.gvsig.fmap.geom.operation.GeometryOperation;
import org.gvsig.fmap.geom.operation.GeometryOperationContext;
import org.gvsig.fmap.geom.operation.GeometryOperationException;
import org.gvsig.fmap.geom.primitive.Point;
import org.gvsig.fmap.geom.type.GeometryType;

/**
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public class PointDistance extends GeometryOperation{
	public static final int CODE = GeometryLocator.getGeometryManager().registerGeometryOperation("distance", new PointDistance(), TYPES.POINT);
	
	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.operation.GeometryOperation#getOperationIndex()
	 */
	public int getOperationIndex() {
		return CODE;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.operation.GeometryOperation#invoke(org.gvsig.fmap.geom.Geometry, org.gvsig.fmap.geom.operation.GeometryOperationContext)
	 */
	public Object invoke(Geometry geom, GeometryOperationContext ctx)
			throws GeometryOperationException {
		Geometry geom2 = (Geometry)ctx.getAttribute("geom");
		GeometryType geomType2 = geom2.getGeometryType();
		
		if ((TYPES.POINT != geom.getType()) && TYPES.POINT != geomType2.getType()){
			throw new UnsupportedOperationException("The distance only can be execudet between two points");
		}
		
		return new Double(Point2D.distance(((Point)geom).getX(),
				((Point)geom).getY(),
				((Point)geom2).getX(),
				((Point)geom2).getX()));
	}
}

