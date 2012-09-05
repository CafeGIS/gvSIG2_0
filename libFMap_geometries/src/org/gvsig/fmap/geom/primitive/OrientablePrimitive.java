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

/**
 * <p>
 * This interface is equivalent to the GM_OrientablePrimitive specified in 
 * <a href="http://www.iso.org/iso/iso_catalogue/catalogue_tc/catalogue_detail.htm?csnumber=26012">ISO 19107</a>.
 * Orientable primitives are those that can be mirrored into new 
 * geometric objects in terms of their internal local coordinate 
 * systems (manifold charts).
 * </p>
 * <p>
 * For curves, the orientation reflects the direction in which the curve is traversed, 
 * that is, the sense of its parameterization. When used as boundary curves, 
 * the surface being bounded is to the "left" of the oriented curve. 
 * </p>
 * <p>
 * For surfaces, the orientation reflects from which direction the local coordinate 
 * system can be viewed as right handed, the "top" or the surface being the direction 
 * of a completing z-axis that would form a right-handed system.
 * </p>
 * <p>
 * When used as a boundary surface, the bounded solid is "below" the surface. 
 * The orientation of points and solids has no immediate geometric interpretation 
 * in 3-dimensional space.
 * </p>
 * <p> OrientablePrimitive objects are essentially references to geometric primitives 
 * that carry an "orientation" reversal flag (either "+" or "-") that determines whether 
 * this primitive agrees or disagrees with the orientation of the referenced object.
 * </p>
 * @see <a href="http://www.iso.org/iso/iso_catalogue/catalogue_tc/catalogue_detail.htm?csnumber=26012">ISO 19107</a>
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public interface OrientablePrimitive extends Primitive {
		
}
