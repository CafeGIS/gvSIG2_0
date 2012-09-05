package org.gvsig.gpe.utils;
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
 * $Id: GeographicalUtils.java 161 2007-06-28 13:05:27Z jorpiell $
 * $Log$
 * Revision 1.1  2007/06/28 13:04:33  jorpiell
 * The Qname has been updated to the 1.5 JVM machine. The schema validation is made in the GPEWriterHandlerImplementor class
 *
 *
 */
/**
 * This class contains some utils to manage
 * geographical properties
 * @author Jorge Piera Llodrá (jorge.piera@iver.es)
 * @author Carlos Sánchez Periñán (sanchez_carper@gva.es)
 */
public class GeographicalUtils {

	/**
	 * It tries if a polygon is closed
	 * @param x
	 * X coordinates 
	 * @param y
	 * Y coordinates 
	 * @param z
	 * Z coordinates 
	 * @return
	 * <true> if the polygon is close.
	 */
	public static boolean isClosed(double[] x, double[] y,double[] z){
		if ((x[0] != x[x.length - 1]) ||
				(y[0] != y[y.length - 1]) ||
				(z[0] != z[z.length - 1])){
			return false;
		}
		return true;
	}
	
	/**
	 * Closes a polygon
	 * @param x
	 * Polygon coordinates
	 * @return
	 * A closed polygon
	 */
	public static double[] closePolygon(double[] x){
		double[] xClosed = new double[x.length + 1];
		System.arraycopy(x, 0, xClosed, 0, x.length);
		xClosed[xClosed.length -1] = x[0];
		return xClosed;
	}
}
