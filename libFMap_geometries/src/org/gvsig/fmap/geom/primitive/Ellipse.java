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
 * An ellipse is the finite or bounded case of a conic section, 
 * the geometric shape that results from cutting a circular conical 
 * or cylindrical surface with an oblique plane (the other two cases being 
 * the parabola and the hyperbola). It is also the locus of all points of 
 * the plane whose distances to two fixed points add to the same constant.
 * </p>
 * @see <a href="http://en.wikipedia.org/wiki/Ellipse">Full definition from Wikipedia</a>
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public interface Ellipse extends Surface {
		
	/**
	 * Sets the values to define a ellipse.
	 * @param axis1Start
	 * The point where the first axis starts.
	 * @param axis1End
	 * The point where the first axis ends.
	 * @param axis2Length
	 * The length of the second axis.
	 */
	void setPoints(Point axis1Start, Point axis1End, double axis2Length);	
	
	/**
	 * Returns the point where the first axis starts.
	 * @return
	 * The point where the first axis starts.
	 */
	Point getAxis1Start();
	
	/**
	 * Returns the point where the first axis ends.
	 * @return
	 * The point where the first axis ends.
	 */
	Point getAxis1End();
	
	/**
	 * Returns the length of the second axis.
	 * @return
	 * The length of the second axis.
	 */
	double getAxis2Dist();
}

