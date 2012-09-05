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
 * This interface is equivalent to the GM_Arc specified in 
 * <a href="http://www.iso.org/iso/iso_catalogue/catalogue_tc/catalogue_detail.htm?csnumber=26012">ISO 19107</a>.
 * A Arc is defined by 3 points, and consists of the arc of the circle determined by the 3 points, 
 * starting at the first, passing through the second and terminating at the third. 
 * If the 3 points are co-linear, then the arc shall be a 3-point line string, and will 
 * not be able to return values for center, radius, start angle and end angle. 
 * </p> 
 * @see <a href="http://www.iso.org/iso/iso_catalogue/catalogue_tc/catalogue_detail.htm?csnumber=26012">ISO 19107</a>
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public interface Arc extends Curve {
		
	/**
	 * Sets the three points to define an arc.
	 * @param midPoint
	 * The middle point of an arc.
	 * @param startPoint
	 * The start point of an arc.
	 * @param endPoint
	 * The end point of an arc.
	 */
	void setPoints(Point midPoint, Point startPoint, Point endPoint);
	
	/**
	 * Sets the values to define an arc.
	 * @param center
	 * The center of the arc.
	 * @param radius
	 * The radius.
	 * @param initAngle
	 * The start angle of the arc.
	 * @param angleExt
	 * The end angle of the arc.
	 */
	void setPoints (Point center, double radius, double startAngle, double endAngle );
	
	/**
	 * Return the first point that has been used to create the arc.
	 * @return
	 * The first point of the arc.
	 */
	Point getInitPoint();
		
	/**
	 * Return the end point that has been used to create the arc.
	 * @return
	 * The end point of the arc.
	 */
	Point getEndPoint();
	
	/**
	 * Return the center point that has been used to create the arc.
	 * @return
	 * The center point of the arc.
	 */
	Point getCenterPoint();
		
		
}
