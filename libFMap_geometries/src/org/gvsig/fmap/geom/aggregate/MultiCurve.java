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

import org.gvsig.fmap.geom.primitive.Curve;
import org.gvsig.fmap.geom.primitive.OrientableCurve;

/**
 * <p>
 * This interface is equivalent to the GM_Curve specified in 
 * <a href="http://www.iso.org/iso/iso_catalogue/catalogue_tc/catalogue_detail.htm?csnumber=26012">ISO 19107</a>.
 * MultiCurve is an aggregate class containing only instances of {@link OrientableCurve}.  
 * The association role "element" shall be the set of {@link OrientableCurve}'s contained in 
 * this MultiCurve.
 * </p>
 * @see <a href="http://www.iso.org/iso/iso_catalogue/catalogue_tc/catalogue_detail.htm?csnumber=26012">ISO 19107</a>
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public interface MultiCurve extends Aggregate{
	
	/**
	 * Adds a new curve to the  multi curve
	 * @param curve
	 * The curve to add
	 */
	void addCurve(Curve curve);
	
	/**
	 * Gets the curve that is on a concrete position
	 * inside the multi curve 
	 * @param index
	 * The position
	 * @return
	 * The selected curve
	 */
	Curve getCurveAt(int index);
}
