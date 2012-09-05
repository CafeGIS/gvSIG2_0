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
/*
 * AUTHORS (In addition to CIT):
 * 2009 Instituto de Automática e Informática Industrial, UPV.
 */
package org.gvsig.fmap.geom.primitive;

/**
 * <p>
 * This interface is equivalent to the GM_Surface specified in <a href="http://www.iso.org/iso/iso_catalogue/catalogue_tc/catalogue_detail.htm?csnumber=26012"
 * >ISO 19107</a>. Surface is a subclass of {@link Primitive} and is the basis
 * for 2-dimensional geometry. Unorientable surfaces such as the Mï¿½bius band
 * are not allowed.
 * <p/>
 * <p>
 * The orientation of a surface chooses an "up" direction through the choice of
 * the upward normal, which, if the surface is not a cycle, is the side of the
 * surface from which the exterior boundary appears counterclockwise. Reversal
 * of the surface orientation reverses the curve orientation of each boundary
 * component, and interchanges the conceptual "up" and "down" direction of the
 * surface.
 * </p>
 * <p>
 * If the surface is the boundary of a solid, the "up" direction is usually
 * outward. For closed surfaces, which have no boundary, the up direction is
 * that of the surface patches, which must be consistent with one another.
 * </p>
 * 
 * @see <a
 *      href="http://www.iso.org/iso/iso_catalogue/catalogue_tc/catalogue_detail.htm?csnumber=26012">ISO
 *      19107</a>
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 * @author <a href="mailto:jtorres@ai2.upv.es">Jordi Torres Fabra</a>
 */
public interface Surface extends OrientableSurface {

	/**
	 * Sets all the coordinates of the surface.
	 * 
	 * @param generalPathX
	 *            The generalPath that contains all the coordinates.
	 */
	public void setGeneralPath(GeneralPathX generalPathX);

	/**
	 * Gets the one of the values of a coordinate (direct position) in a
	 * concrete dimension.
	 * 
	 * @param index
	 *            The index of the direct position to set.
	 * @param dimension
	 *            The dimension of the direct position.
	 * @return The value of the coordinate
	 */
	public double getCoordinateAt(int index, int dimension);

	/**
	 * Sets the value of a coordinate (direct position) in a concrete dimension.
	 * 
	 * @param index
	 *            The index of the direct position to set.
	 * @param dimension
	 *            The dimension of the direct position.
	 * @param value
	 *            The value to set
	 */
	public void setCoordinateAt(int index, int dimension, double value);

	/**
	 * Adds a vertex (or direct position) to the curve.
	 * 
	 * @param point
	 *            The new point to add.
	 */
	public void addVertex(Point point);

	/**
	 * Remove a vertex (direct position) to the curve.
	 * 
	 * @param index
	 *            The index of the vertex to remove.
	 */
	public void removeVertex(int index);

	/**
	 * Gets a vertex (direct position).
	 * 
	 * @param index
	 *            The index of the vertex to get.
	 * @return One point.
	 */
	public Point getVertex(int index);

	/**
	 * Gets the number of vertices (direct positions) of the curve.
	 * 
	 * @return The number of vertices.
	 */
	public int getNumVertices();

	/**
	 * Inserts a vertex (direct position) to the curve.
	 * 
	 * @param index
	 *            The index of the vertex where the new point has to be added.
	 * @param p
	 *            The vertex to add.
	 */

	public void insertVertex(int index, Point p);

	/**
	 * Sets the appearance of the Surface
	 * 
	 * @param app
	 *            The appearance of the surface
	 */
	public void setSurfaceAppearance(SurfaceAppearance app);

	/**
	 * Gets surface appearance
	 * 
	 * @return the surface appearance
	 * 
	 */
	public SurfaceAppearance getSurfaceAppearance();

}
