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
package org.gvsig.fmap.geom.aggregate.impl;

import java.awt.geom.Point2D;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.aggregate.MultiPoint;
import org.gvsig.fmap.geom.type.GeometryType;

/**
 * Multipunto 3D.
 * 
 * @author Vicente Caballero Navarro
 */
public class MultiPoint2DZ extends MultiPoint2D implements MultiPoint {
	private static final long serialVersionUID = -2905029760276555041L;
	double[] z = null;

	/**
	 * The constructor with the GeometryType like and argument 
	 * is used by the {@link GeometryType}{@link #create()}
	 * to create the geometry
	 * @param type
	 * The geometry type
	 */
	public MultiPoint2DZ(GeometryType geometryType) {
		super(geometryType);		
	}
	
	/**
	 * 
	 * @param id
	 * @param projection
	 * @param x
	 * @param y
	 * @param z
	 */
	MultiPoint2DZ(GeometryType geometryType, String id, IProjection projection, double[] x,
			double[] y, double[] z) {
		super(geometryType, id, projection, x, y);
		this.z = z;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.iver.cit.gvsig.fmap.core.IGeometry#cloneGeometry()
	 */
	public Geometry cloneGeometry() {
		double[] x = new double[getPrimitivesNumber()];
		double[] y = new double[getPrimitivesNumber()];
		for (int i = 0; i < getPrimitivesNumber(); i++) {
			Point2D p = ((Geometry)geometries.get(i)).getHandlers(Geometry.SELECTHANDLER)[0]
					.getPoint();

			x[i] = p.getX();
			y[i] = p.getY();
		}
		return new MultiPoint2DZ(geometryType, id, projection, x, y, (double[]) z.clone());
	}

	/**
	 * Devuelve un array con todos los valores de Z.
	 * 
	 * @return Array de Zs.
	 */
	public double[] getZs() {
		return z;
	}
}
