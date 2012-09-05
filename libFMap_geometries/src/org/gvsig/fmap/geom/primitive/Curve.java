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
 * This interface is equivalent to the GM_Curve and the GM_CurveSegment specified in 
 * <a href="http://www.iso.org/iso/iso_catalogue/catalogue_tc/catalogue_detail.htm?csnumber=26012">ISO 19107</a>.
 * Curve  is a descendent subtype of {@link Primitive} through {@link OrientablePrimitive}. 
 * It is the basis for 1-dimensional geometry. 
 * </p>
 * <p>
 * A curve is a continuous image of an open interval 
 * and so could be written as a parameterized function such as c(t):(a, b) -> E^n where "t" is a real 
 * parameter and E^n is Euclidean space of dimension n (usually 2 or 3, as determined by 
 * the coordinate reference system). Any other parameterization that results in the same image curve, 
 * traced in the same direction, such as any linear shifts and positive scales such as 
 * e(t) = c(a + t(b-a)):(0,1) -> E^n, is an equivalent representation of the same curve.
 * </p>
 * <p>
 * Curves are continuous, connected, and have a measurable length in terms of 
 * the coordinate system. The orientation of the curve is determined by this 
 * parameterization, and is consistent with the tangent function, which 
 * approximates the derivative function of the parameterization and shall 
 * always point in the "forward" direction. The parameterization of the reversal of 
 * the curve defined by c(t):(a, b) -> E^n would be defined by a function of the 
 * form s(t) = c(a + b - t):(a, b) - >E^n.
 * </p>
 * <p>
 * In the ISO model a curve is composed of one or more curve segments. 
 * In gvSIG a curve is not composed by curve segments: a curve is 
 * one and only one curve segment.
 * </p>
 * @see <a href="http://www.iso.org/iso/iso_catalogue/catalogue_tc/catalogue_detail.htm?csnumber=26012">ISO 19107</a>
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public interface Curve extends OrientableCurve{
	
	/**
	 * Sets all the coordinates of the curve 
	 * @param generalPathX
	 * The generalPath that contains all the coordinates
	 */
	public void setGeneralPath(GeneralPathX generalPathX);
	
	/**
	 * Sets the initial point and the end point of the curve. On this case,
	 * the curve is a single line
	 * @param initialPoint
	 * The initial point
	 * @param endPoint
	 * The end point
	 */
	public void setPoints(Point initialPoint, Point endPoint);
			
	/**
	 * Gets the one of the values of a coordinate (direct position) 
	 * in a concrete dimension 
	 * @param index
	 * The index of the direct position to set
	 * @param dimension
	 * The dimension of the direct position
	 * @return
	 * The value of the coordinate
	 */
	public double getCoordinateAt(int index, int dimension);
	
	/**
	 * Sets the value of a coordinate (direct position) in a concrete dimension
	 * @param index
	 * The index of the direct position to set
	 * @param dimension
	 * The dimension of the direct position
	 * @param value
	 * The value to set
	 */
	public void setCoordinateAt(int index, int dimension, double value);
	
	/**
	 * Adds a vertex (or direct position) to the curve
	 * @param point
	 * The new point to add
	 */
	public void addVertex(Point point);
	
	/**
	 * Remove a vertex (direct position) to the curve
	 * @param index
	 * The index of the vertex to remove
	 */
	public void removeVertex(int index);
		
	/**
	 * Gets a vertex (direct position) 
	 * @param index
	 * The index of the vertex to get
	 * @return
	 * One point
	 */
	public Point getVertex(int index);
	
	/**
	 * Gets the number of vertices (direct positions) of the curve
	 * @return
	 * The number of vertices
	 */
	public int getNumVertices();
		
	/**
	 * Inserts a vertex (direct position) to the curve
	 * @param index
	 * The index of the vertex where the new point has to be added
	 * @param p
	 * The vertex to add
	 */
	public void insertVertex(int index, Point p);
}
