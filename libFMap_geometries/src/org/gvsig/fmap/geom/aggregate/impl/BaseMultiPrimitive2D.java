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

package org.gvsig.fmap.geom.aggregate.impl;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.aggregate.MultiPrimitive;
import org.gvsig.fmap.geom.primitive.Primitive;
import org.gvsig.fmap.geom.type.GeometryType;

/**
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public class BaseMultiPrimitive2D extends BaseMultiPrimitive implements
MultiPrimitive {

	/**
	 * The constructor with the GeometryType like and argument 
	 * is used by the {@link GeometryType}{@link #create()}
	 * to create the geometry
	 * @param type
	 * The geometry type
	 */
	public BaseMultiPrimitive2D(GeometryType geomType) {
		super(geomType);
	}

	/**
	 * @param id
	 * @param projection
	 * @param geometries
	 */
	BaseMultiPrimitive2D(GeometryType geomType, String id, IProjection projection,
			Geometry[] geometries) {
		super(geomType, id, projection, geometries);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.iver.cit.gvsig.fmap.core.IGeometry#cloneGeometry()
	 */
	public Geometry cloneGeometry() {
		Primitive[] aux = new Primitive[getPrimitivesNumber()];
		for (int i = 0; i < getPrimitivesNumber(); i++) {
			aux[i] = (Primitive)((Geometry)geometries.get(i)).cloneGeometry()
			.getInternalShape();
		}
		return new BaseMultiPrimitive2D(getGeometryType(), id, projection, aux);
	}	
}

