package org.gvsig.fmap.geom.primitive.impl;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.geom.primitive.OrientableSurface;
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
 * $Id: FOrientablePolygon2D.java,v 1.1 2008/03/12 08:46:20 cvs Exp $
 * $Log: FOrientablePolygon2D.java,v $
 * Revision 1.1  2008/03/12 08:46:20  cvs
 * *** empty log message ***
 *
 *
 */
/**
 * @author Jorge Piera Llodrá (jorge.piera@iver.es)
 */
public abstract class OrientableSurface2D extends OrientablePrimitive2D implements OrientableSurface {
	private static final long serialVersionUID = 9212660743520612859L;

	/**
	 * The constructor with the GeometryType like and argument 
	 * is used by the {@link GeometryType}{@link #create()}
	 * to create the geometry
	 * @param type
	 * The geometry type
	 */
	public OrientableSurface2D(GeometryType geometryType) {
		super(geometryType);		
	}
	
	public OrientableSurface2D(GeometryType geometryType, String id, IProjection projection, GeneralPathX gpx) {
		super(geometryType, id, projection, gpx);		
	}

}
