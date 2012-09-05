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
package org.gvsig.fmap.geom.handler;

import java.awt.geom.Point2D;



/**
 * This class expands the <code>Handler</code> interface adding support for selection, and
 *  identifying a handler of a geometry.
 * 
 * @see Handler
 *
 * @author Vicente Caballero Navarro
 */
public abstract class AbstractHandler implements Handler {
	/**
	 * Identifies each handler of a geometry.
	 */
	protected int index;
	/**
	 * Position of this handler.
	 * 
	 * @see #getPoint()
	 * @see Handler#set(double, double)
	 * @see Handler#move(double, double)
	 */
	protected Point2D point;
	/**
	 * True when the handler is selected.
	 * 
	 * @see #select
	 * @see #isSelected()
	 */
	private boolean select=false;
	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.core.Handler#getPoint()
	 */
	public Point2D getPoint() {
		return point;
	}
	/**
	 * Sets the state of the handler. <code>True</code> if is selected, <code>false</code> if it's not.
	 * 
	 * @param b <code>true</code> if the handler is selected, <code>false</code>  otherwise
	 * 
	 * @see #isSelected()
	 */
	public void select(boolean b) {
		select=b;
	}
	/**
	 * Returns the state of the handler. <code>True</code> if is selected, <code>false</code> if it's not.
	 * 
	 * @return <code>true</code> if the handler is selected,<code>false</code> otherwise 
	 * 
	 * @see #select(boolean)
	 */
	public boolean isSelected(){
		return select;
	}
	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.core.Handler#equalsPoint(java.lang.Object)
	 */
	public boolean equalsPoint(Object obj) {
		Point2D p1=this.getPoint();
		Point2D p2=((Handler)obj).getPoint();
		if (p1.getX()==p2.getX() && p1.getY()==p2.getY()) {
			return true;
		}
		return false;
	}
}
