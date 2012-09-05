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
* 2009 IVER T.I   {{Task}}
*/

/**
 *
 */
package org.gvsig.fmap.geom.operation;

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.TYPES;
import org.gvsig.fmap.geom.aggregate.Aggregate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jmvivo
 *
 */
public class AggregateDrawInts extends GeometryOperation {
	final static private Logger logger = LoggerFactory
			.getLogger(DrawInts.class);

	public static final int CODE = GeometryLocator.getGeometryManager()
			.registerGeometryOperation("drawInts", new AggregateDrawInts(),
					TYPES.AGGREGATE);


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

		Aggregate aggregate = (Aggregate) geom;
		for (int i=0;i<aggregate.getPrimitivesNumber();i++){
			Geometry prim = aggregate.getPrimitiveAt(i);
			try {
				prim.invokeOperation(CODE, ctx);
			} catch (GeometryOperationNotSupportedException e) {
				throw new GeometryOperationException(e);
			}
		}
		return null;
	}

	public static void register() {
		GeometryManager geoMan = GeometryLocator.getGeometryManager();

		geoMan.registerGeometryOperation("drawInts", new AggregateDrawInts(),
				TYPES.MULTIPOINT);
		geoMan.registerGeometryOperation("drawInts", new AggregateDrawInts(),
				TYPES.MULTICURVE);
		geoMan.registerGeometryOperation("drawInts", new AggregateDrawInts(),
				TYPES.MULTISURFACE);
		geoMan.registerGeometryOperation("drawInts", new AggregateDrawInts(),
				TYPES.MULTISOLID);

	}

}
