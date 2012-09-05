package org.gvsig.gpe.exceptions;

import java.util.Hashtable;
import java.util.Map;

import org.gvsig.tools.exception.BaseException;

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
 * $Id: PolygonWithoutThreeCoordinatesError.java 130 2007-05-16 12:07:07Z jorpiell $
 * $Log$
 * Revision 1.1  2007/05/16 12:07:07  jorpiell
 * New exceptions added
 *
 *
 */
/**
 * This error is throwed when the application try
 * to add a polygon with less than tree points (to create
 * a polygon is necessary three points at least)
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class PolygonWithoutThreeCoordinatesError extends BaseException {
	private static final long serialVersionUID = -2769635981750046340L;
	private double[] x  = null;
	private double[] y  = null;
	private double[] z  = null;
	
	/**
	 * Costructor
	 * @param x
	 * Coordinate X
	 * @param y
	 * Coordinate Y
	 * @param z
	 * Coordinate Z
	 */
	public PolygonWithoutThreeCoordinatesError(double[] x, double[] y, double[] z){
		this.x = x;
		this.y = y ;
		this.z = z;
		initialize();
	}

	/**
	 * Initialize the properties
	 */
	private void initialize() {
		messageKey = "gpe_polygon_without_three_coordinates_error";
		formatString = "The polygon has not three coordinates at least (it has" +
				" %(size)). Its coordinates are %(coordinates)";				
		code = serialVersionUID;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.tools.exception.BaseException#values()
	 */
	protected Map values() {
		Hashtable hash = new Hashtable();
		hash.put("coordinates", getCoordinates());
		hash.put("size", String.valueOf(x.length));
		return hash;
	}
	
	private String getCoordinates(){
		String coordinates = "";
		for (int i=0 ; i<x.length ; i++){
			coordinates = String.valueOf(x[i]);
			coordinates = String.valueOf(",");
			coordinates = String.valueOf(y[i]);
			coordinates = String.valueOf(",");
			coordinates = String.valueOf(z[i]);
			coordinates = String.valueOf(" ");
		}
		return coordinates;
	}
}
