/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2006 Instituto de Desarrollo Regional and Generalitat Valenciana.
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
 *   Instituto de Desarrollo Regional (Universidad de Castilla La-Mancha)
 *   Campus Universitario s/n
 *   02071 Alabacete
 *   Spain
 *
 *   +34 967 599 200
 */

package org.gvsig.crs;

import org.cresques.cts.IDatum;

/**
 * Clase que establece el datum del CRS seleccionado
 * 
 * @author Diego Guerrero Sevilla (diego.guerrero@uclm.es)
 *
 */

public class CRSDatum implements IDatum {
	private double eSemiMajorAxis = 0;
	private double eIFlattening = 0;

	public CRSDatum(double semiMajorAxis, double flattening) {
		super();
		eSemiMajorAxis = semiMajorAxis;
		eIFlattening = flattening;
	}

	public double getESemiMajorAxis() {
		return eSemiMajorAxis;
	}

	public double getEIFlattening() {
		return eIFlattening;
	}
}
