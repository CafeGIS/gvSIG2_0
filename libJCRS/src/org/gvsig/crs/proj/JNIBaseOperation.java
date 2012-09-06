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

package org.gvsig.crs.proj;

/**
 * 
 * @author Miguel García Jiménez (garciajimenez.miguel@gmail.com)
 *
 */
public class JNIBaseOperation
{
	protected static native int operation(double[] firstCoord,
								    double[] secondCoord,
								    double[] values,
								    long srcCodeString,
								    long destCodeString);
	
	protected static native int operationSimple(double firstcoord,
												double secondcoord,
												double values,
												long srcCodeString,
												long destCodeString);
	
	protected static native int operationArraySimple(double[] Coord,
													 long srcCodeString,
													 long destCodeString);
	protected static native int compareDatums(long datum1, long datum2);
	static
	{
		System.loadLibrary("crsjniproj2.0.0");
	}
}
