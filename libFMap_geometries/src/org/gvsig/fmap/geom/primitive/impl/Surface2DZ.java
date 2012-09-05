/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
 *
 * For more information, contact:
 *
 *  Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ibáñez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   IVER T.I. S.A
 *   Salamanca 50
 *   46005 Valencia
 *   Spain
 *
 *   +34 963163400
 *   dac@iver.es
 */
package org.gvsig.fmap.geom.primitive.impl;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.primitive.FShape;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.geom.primitive.Surface;
import org.gvsig.fmap.geom.type.GeometryType;

/**
 * Polilinea 3D.
 *
 * @author Vicente Caballero Navarro
 */
public class Surface2DZ extends Surface2D implements Surface {
	private static final long serialVersionUID = 1L;
	double[] pZ = null;

	/**
	 * The constructor with the GeometryType like and argument 
	 * is used by the {@link GeometryType}{@link #create()}
	 * to create the geometry
	 * @param type
	 * The geometry type
	 */
	public Surface2DZ(GeometryType geometryType) {
		super(geometryType);
	}

	/**
	 * Constructor used in the {@link Geometry#cloneGeometry()} method
	 * @param id
	 * @param projection
	 * @param gpx
	 * @param pZ
	 */
	Surface2DZ(GeometryType geometryType, String id, IProjection projection, GeneralPathX gpx,
			double[] pZ) {
		super(geometryType, id, projection, gpx);
		this.pZ = pZ;
	}

	/**
	 * Clona FPolygon3D.
	 *
	 * @return FShape clonado.
	 */
	public FShape cloneFShape() {
		return new Surface2DZ(getGeometryType(), id, projection, (GeneralPathX) gp.clone(),
				(double[]) pZ.clone());
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.impl.Surface2D#getShapeType()
	 */
	public int getShapeType() {
		return TYPES.SURFACE;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.Surface#setCoordinateAt(int, int, double)
	 */
	public void setCoordinateAt(int index, int dimension, double value) {
		if (dimension == 2){
			pZ[index] = value;
		}else{
			super.setCoordinateAt(index, dimension, value);
		}
	}
}
