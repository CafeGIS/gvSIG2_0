package org.gvsig.fmap.geom.aggregate.impl;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.aggregate.MultiCurve;
import org.gvsig.fmap.geom.primitive.Curve;
import org.gvsig.fmap.geom.primitive.impl.Curve2DZ;
import org.gvsig.fmap.geom.type.GeometryType;

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
/* CVS MESSAGES:
 *
 * $Id: FMultiPolyline2D.java,v 1.2 2008/03/25 08:47:41 cvs Exp $
 * $Log: FMultiPolyline2D.java,v $
 * Revision 1.2  2008/03/25 08:47:41  cvs
 * Visitors removed
 *
 * Revision 1.1  2008/03/12 08:46:20  cvs
 * *** empty log message ***
 *
 *
 */
/**
 * @author Jorge Piera Llodrá (jorge.piera@iver.es)
 */
public class MultiCurve2DZ extends BaseMultiPrimitive implements MultiCurve {
	private static final long serialVersionUID = 3079910108112255174L;

	/**
	 * The constructor with the GeometryType like and argument 
	 * is used by the {@link GeometryType}{@link #create()}
	 * to create the geometry
	 * @param type
	 * The geometry type
	 */
	public MultiCurve2DZ(GeometryType geomType) {
		super(geomType);
	}

	MultiCurve2DZ(GeometryType geomType, String id, IProjection projection) {
		super(geomType, id, projection);
	}

	MultiCurve2DZ(GeometryType geomType, String id, IProjection projection, Curve2DZ[] lines) {
		super(geomType, id, projection, lines);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.iver.cit.gvsig.fmap.core.FGeometryCollection#cloneGeometry()
	 */
	public Geometry cloneGeometry() {
		MultiCurve2DZ auxCurve = new MultiCurve2DZ(geometryType, id, projection);
		for (int i = 0; i < getPrimitivesNumber(); i++) {
			auxCurve.addCurve((Curve)((Curve) geometries.get(i)).cloneGeometry());
		}
		return auxCurve;
	}
	
	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.aggregate.MultiCurve#addCurve(org.gvsig.fmap.geom.primitive.Curve)
	 */
	public void addCurve(Curve curve) {
		addPrimitive(curve);
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.aggregate.MultiCurve#getCurveAt(int)
	 */
	public Curve getCurveAt(int index) {
		return (Curve)getPrimitiveAt(index);
	}
}
