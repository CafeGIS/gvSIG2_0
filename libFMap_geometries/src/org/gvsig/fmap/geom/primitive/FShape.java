/* gvSIG. Geographic Information System of the Valencian Government
 *
 * Copyright (C) 2007-2008 Infrastructures and Transports Department
 * of the Valencian Government (CIT)
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 * 
 */

/*
 * AUTHORS (In addition to CIT):
 * 2009 {Iver T.I.}   {Task}
 */

package org.gvsig.fmap.geom.primitive;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.io.Serializable;

import org.cresques.cts.ICoordTrans;
import org.gvsig.fmap.geom.handler.Handler;

/**
 * <p>The interface <code>FShape</code> extends <code>Shape</code> adding shape types, and allowing
 *  to work with it as a geometry.</p>
 *  
 *  @deprecated
 */
public interface FShape extends Shape, Serializable {
	/**
	 * Gets the geometry type of this shape.
	 *
	 * @return int the geometry type of this shape.
	 */
	public int getShapeType();
	
	/**
	 * Creates and returns a shape equal and independent of this one.
	 *
	 * @return the new shape.
	 */
	public FShape cloneFShape();
	/**
	 * Re-projects this shape using transformation coordinates. 
	 *
	 * @param ct the transformation coordinates
	 */
	public void reProject(ICoordTrans ct);

	/**
	 * Returns the handlers they utilized to stretch the geometries.
	 *
	 * @return Handlers the handlers used to stretch the geometries
	 */
	public Handler[] getStretchingHandlers();

	/**
	 * Returns the handlers used to select the geometries.
	 *
	 * @return Handlers the handlers used to select the geometries
	 */
	public Handler[] getSelectHandlers();
	/**
	 * Executes a 2D transformation on this shape, using six parameters.
	 * 
	 * @param at object that allows execute the affine transformation
	 * 
	 * @see AffineTransform
	 */
	public void transform(AffineTransform at);
}
