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

import org.gvsig.fmap.geom.primitive.Solid;

/**
 * <p>
 * This interface is equivalent to the GM_MultiSolid specified in 
 * <a href="http://www.iso.org/iso/iso_catalogue/catalogue_tc/catalogue_detail.htm?csnumber=26012">ISO 19107</a>.
 * MultiSolid is an aggregate class containing only solids. The association role "element" 
 * shall be the set of {@link Solid}'s contained in this MultiSolid.
 * </p>
 * @see <a href="http://www.iso.org/iso/iso_catalogue/catalogue_tc/catalogue_detail.htm?csnumber=26012">ISO 19107</a>
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public interface MultiSolid extends Aggregate{
	
	/**
	 * Adds a new solid to the  multi solid
	 * @param solid
	 * The solid to add
	 */
	void addSolid(Solid solid);
	
	/**
	 * Gets the solid that is on a concrete position
	 * inside the multi solid 
	 * @param index
	 * The position
	 * @return
	 * The selected solid
	 */
	Solid getSolidAt(int index);
}
