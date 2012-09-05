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
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public interface EllipticArc extends Surface{

	/**
	 * Sets the values to define a ellipticArc
	 * @param axis1Start
	 * The point where the first axis starts.
	 * @param axis1End
	 * The point where the first axis ends.
	 * @param semiAxis2Dist
	 * @param angSt
	 * @param angExt
	 */
	void setPoints(Point axis1Start, Point axis1End, double semiAxis2Dist, double angSt, double angExt);
	
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
		
	double getAxis2Dist();
		
	double getAngSt();
		
	double getAngExt();
		
}

