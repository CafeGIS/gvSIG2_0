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
 * This interface is equivalent to the GM_Circle specified in 
 * <a href="http://www.iso.org/iso/iso_catalogue/catalogue_tc/catalogue_detail.htm?csnumber=26012">ISO 19107</a>.
 * Same as Arc, but closed to form a full circle. The "start" and "end" points are equal.
 * @see <a href="http://www.iso.org/iso/iso_catalogue/catalogue_tc/catalogue_detail.htm?csnumber=26012">ISO 19107</a>
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public interface Circle extends Surface {
	
	/**
	 * Sets the two values to define a circle.
	 * @param center
	 * The center point of the circle.
	 * @param radious
	 * A point that is used to calculate the radius.
	 */	
	void setPoints(Point center, Point radious);		
	
	/**
	 * Sets the two values to define a circle.
	 * @param center
	 * The center point of the circle.
	 * @param radious
	 * The radius of the circle.
	 */	
	void setPoints(Point center, double radious);	
	
	/**
	 * Sets the values to define a circle from three points. The circle
	 * will be inside or the area defined by the three points.
	 * @param p1
	 * First point
	 * @param p2
	 * Second point 
	 * @param p3
	 * Third point
	 */
	void setPoints(Point p1, Point p2, Point p3);
	
	/**
	 * Returns the center of the circle.
	 * @return
	 * The center of the circle.
	 */
	Point getCenter();
	
	/**
	 * Returns the radius of the circle
	 * @return
	 * The radius of the circle
	 */
	double getRadious();
}
