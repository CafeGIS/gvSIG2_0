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

import org.gvsig.fmap.geom.DirectPosition;

/**
 * <p>
 * This interface is equivalent to the GM_Point specified in 
 * <a href="http://www.iso.org/iso/iso_catalogue/catalogue_tc/catalogue_detail.htm?csnumber=26012">ISO 19107</a>.
 * It is the basic data type for a geometric object consisting 
 * of one and only one point. 
 * </p>
 * @see <a href="http://www.iso.org/iso/iso_catalogue/catalogue_tc/catalogue_detail.htm?csnumber=26012">ISO 19107</a>
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public interface Point extends Primitive {
	
	/**
	 * Gets the {@link DirectPosition of a point}, that is
	 * composed by a set of ordinates
	 * @return
	 * The direct position
	 */
	public DirectPosition getDirectPosition();
	
	/**
	 * Sets a ordinate in a concrete dimension
	 * @param dimension
	 * The dimension to set
	 * @param value
	 * The value to set
	 */
	void setCoordinateAt(int dimension, double value);
	
	/**
	 * Sets the point coordinates
	 * @param values
	 * The coordinates to set
	 */
	void setCoordinates(double[] values);
	
	/**
	 * Sets the X coordinate
	 * @param x
	 * The value to set
	 */
	void setX(double x);
	
	/**
	 * Sets the Y coordinate
	 * @param y
	 * The value to set
	 */
	void setY(double y);
	
	/**
	 * Gets the coordinate in a concrete dimension
	 * @param dimension
	 * The ordinate dimension
	 * @return
	 * The value of the ordinate
	 */
	double getCoordinateAt(int dimension);
	
	/**
	 * Returns an array of coordinates
	 * @return
	 * The point coordinates
	 */
	double[] getCoordinates();
	
	/**
	 * Returns the X coordinate 
	 * @return
	 * The X coordinate
	 */
	double getX();
	
	/**
	 * Returns the Y coordinate 
	 * @return
	 * The Y coordinate
	 */
	double getY();
	
}