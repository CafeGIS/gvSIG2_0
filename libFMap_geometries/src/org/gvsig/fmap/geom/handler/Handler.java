/*
 * Created on 10-feb-2005
 *
 * gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
     USA.
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
package org.gvsig.fmap.geom.handler;

import java.awt.geom.Point2D;


/**
* <p>In a <i>FMap</i> graphic layer, each geometry drawn has control points named <i>handlers</i>
* that allow user to move, modify, set, ... that geometry.</p>
*  
* <p>Each geometry will have its own handlers, and each one can have different behavior 
* according its nature.</p>
* 
* <p>The <code>Handler</code> interface defines the least set of common methods for all
* geometry handlers.</p>
*/
public interface Handler {
	/**
	 * <p>Generic method of moving in 2D a handler of a geometry using two numbers that
	 * represent the 2D coordinates.</p>
	 * 
	 * <p>Each handler of each geometry adapts this method to its own behavior in that 
	 * geometry, that implies that could not be implemented.</p>
	 *
	 * @param x first dimension coordinate
	 * @param y second dimension coordinate
	 * 
	 * @see #set(double, double)
	 */
	public void move(double x, double y);
	/**
 	 * <p>Generic method of situating in 2D a handler of a geometry using two numbers
 	 * that represent the 2D coordinates.</p>
	 * 
	 * <p>Each handler of each geometry adapts this method to its own behavior in that 
	 * geometry, that implies that could not be implemented.</p>
	 *
	 * @param x first dimension coordinate
	 * @param y second dimension coordinate
	 * 
	 * @see #move(double, double)
	 * @see #getPoint()
	 */
	public void set(double x, double y);

	/**
 	 * <p>Generic method of getting the 2D point that represents a handler of a geometry.</p>
	 * 
	 * <p>Each handler of each geometry adapts this method to its own behavior in that
	 * geometry, that implies that could not be implemented.</p>
	 *
	 * @return point 2D that represents this handler of geometry
	 * 
	 * @see #set(double, double)
	 * @see #move(double, double)
	 */
	public Point2D getPoint();
	/**
	 * <p>Returns <code>true</code> if the object is a <code>Handler</code> and has the
	 *  same coordinates as this one.</p>
	 * 
	 * @param obj the reference object with which to compare 
	 * @return <code>true</code> if this object is the same as the <code>obj</code> 
	 * argument; <code>false</code> otherwise
	 * 
	 * @see #getPoint()
	 */
	public boolean equalsPoint(Object obj);
}
