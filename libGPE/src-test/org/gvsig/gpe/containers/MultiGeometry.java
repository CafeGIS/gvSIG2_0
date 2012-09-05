package org.gvsig.gpe.containers;

import java.util.ArrayList;

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
 * $Id: MultiGeometry.java 180 2007-11-21 11:19:46Z csanchez $
 * $Log$
 * Revision 1.1  2007/05/09 10:03:19  jorpiell
 * Add the multigeometry tests
 *
 *
 */
/**
 * This class represetnts a Multigeometry
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class MultiGeometry extends Geometry {
	


	private ArrayList geometries = null;
	
	public MultiGeometry(){
		geometries = new ArrayList();
	}

	/**
	 * @return the geometries
	 */
	public ArrayList getGeometries() {
		return geometries;
	}
	
	/**
	 * Adds a new Geometry
	 * @param geometry
	 * Geometry to add
	 */
	public void addGeometry(Geometry geometry) {
		geometries.add(geometry);
	}
	
	/**
	 * Gets a Geometry at position i
	 * @param i
	 * position
	 */
	public Object getGeometryAt(int i) {
		return geometries.get(i);
	}
}
