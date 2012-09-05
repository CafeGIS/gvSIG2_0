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

package org.gvsig.fmap.geom.aggregate;

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.primitive.Primitive;

/**
 * <p>
 * This interface is equivalent to the GM_Aggregate specified in 
 * <a href="http://www.iso.org/iso/iso_catalogue/catalogue_tc/catalogue_detail.htm?csnumber=26012">ISO 19107</a>.
 * The aggregates gather geometric objects. Since they will often use orientation 
 * modification, the curve reference and surface references do not go directly to 
 * the {@link Curve} and {@link Surface}, but are directed to {@link OrientableCurve} 
 * and {@link OrientableSurface}.
 * </p>
 * <p>
 * Most geometric objects are contained in features, and cannot be held in 
 * collections that are strong aggregations. For this reason, the collections 
 * described on this package are all weak aggregations, and shall use 
 * references to include geometric objects. 
 * </p> 
 * @see <a href="http://www.iso.org/iso/iso_catalogue/catalogue_tc/catalogue_detail.htm?csnumber=26012">ISO 19107</a>
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public interface Aggregate extends Geometry{	
	
	/**
	 * Returns the number of {@link Primitive}'s that composes
	 * this multi geometry.
	 * @return the number of {@link Primitive}'s that composes
	 * this multi geometry.
	 */
	public int getPrimitivesNumber();
			
	/**
	 * Returns one of the {@link Primitive}'s that is in
	 * a concrete position.
	 * @param i
	 * Geometry position.
	 * @return
	 * A simple geometry.
	 */
	public Primitive getPrimitiveAt(int i);
	
}
