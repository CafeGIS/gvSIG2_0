/* gvSIG. Geographic Information System of the Valencian Government
 *  osgVP. OSG Virtual Planets.
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
 * 2008 Instituto de Automática e Informática Industrial, UPV.
 */
package org.gvsig.fmap.geom.primitive;

/**
 * This interface serves to set the surface appearance characteristics like textures 
 * materials or colors.
 *  
 */

/**
 * 
 * @author Jordi Torres Fabra <jtorres@ai2.upv.es>
 * 
 */

public interface SurfaceAppearance extends Appearance {

	public static final class AttributeBinding {
		public static final int BIND_OFF = 0;
		public static final int BIND_OVERALL = 1;
		public static final int BIND_PER_PRIMITIVE_SET = 2;
		public static final int BIND_PER_PRIMITIVE = 3;
		public static final int BIND_PER_VERTEX = 4;
	}

	public static class PrimitiveMode {
		public static final int POINTS = 0;
		public static final int LINES = 1;
		public static final int LINE_STRIP = 3;
		public static final int LINE_LOOP = 2;
		public static final int TRIANGLES = 4;
		public static final int TRIANGLE_STRIP = 5;
		public static final int TRIANGLE_FAN = 6;
		public static final int QUADS = 7;
		public static final int QUAD_STRIP = 8;
		public static final int POLYGON = 9;
	}

	public static class PrimitiveType {
		public static final int PrimitiveType = 0;
		public static final int DrawArrays = 1;
		public static final int DrawArrayLengths = 2;
		public static final int DrawElementsUInt = 5;
	}

	/**
	 * Add a normal vector to the normal array of the surface
	 * 
	 * @param p
	 */
	public void addNormal(Point p);

	/**
	 * Sets normal vector in a concrete position.
	 * 
	 * @param position
	 *            the array position to set the normal vector
	 * @param p
	 *            the normal vector to set (dimension 3)
	 * 
	 */
	public void setNormalAt(int position, Point p);

	/**
	 * Gets the normal vector in a concrete position
	 * 
	 * @param position
	 *            the direct position
	 * @return the normal vector
	 * 
	 */
	public Point getNormalAt(int position);

	/**
	 * Get the number of normals in the surface
	 * 
	 * @return an integer representing the number of normals
	 */
	public int getNumNormals();

	/**
	 * Remove normal vector in a concrete position
	 * 
	 * @param position
	 *            the direct position
	 */
	public void removeNormal(int position);

	/**
	 * Sets the binding of normal array to Surface Geometry.
	 * 
	 * @param binding
	 *            Value of the binding
	 * 
	 */
	public void setNormalBinding(AttributeBinding binding);

	/**
	 * Gets the binding of normal array
	 * 
	 * @return Value representing attributeBinding
	 * 
	 */
	public AttributeBinding getNormalBinding();

	/**
	 * Adds a texture coordinate to TexCoord array in Surface
	 * 
	 * @param p
	 *            the texture coordinate (dimension 2)
	 */

	public void addTextureCoord(Point p);

	/**
	 * Set texture coordinate at concrete position
	 * 
	 * @param position
	 *            the concrete position
	 * @param p
	 *            the texture coordinate value
	 */
	public void setTextureCoordAt(int position, Point p);

	/**
	 * Get texture coordinate at concrete position
	 * 
	 * @param position
	 *            the concrete position
	 * @return the texure coordinate (dimension 2)
	 */
	public Point getTextureCoordAt(int position);

	/**
	 * Gets the number of texture coordinates in the texcoord array of the
	 * surface
	 * 
	 * @return the number of texture coordinates
	 */
	public int getNumTextureCoords();

	/**
	 * Remove texture coordinate at concrete position
	 * 
	 * @param position
	 */
	public void removeTextureCoord(int position);

	/**
	 * Add a index in the index array of the surface
	 * 
	 * @param element
	 *            the index to add
	 */
	public void addIndex(int element);

	/**
	 * Get the index element at concrete position
	 * 
	 * @param position
	 *            the concrete position
	 * @return teh index element
	 */
	public int getIndexAt(int position);

	/**
	 * Set an index element in a concrete position
	 * 
	 * @param position
	 *            the concrete position
	 * @param element
	 *            the index element
	 */
	public void setIndexAt(int position, int element);

	/**
	 * Remove index element at concrete position
	 * 
	 * @param position
	 *            the concrete position
	 */
	public void removeIndex(int position);

	/**
	 * Get number of indices in the index array of the surface
	 * 
	 * @return the number of indices
	 */
	public int getNumIndices();

	/**
	 * Get the primitive mode of the surface
	 * 
	 * @return the primitive mode
	 */
	public PrimitiveMode getPrimitiveMode();

	/**
	 * Sets the primitive mode of the surface
	 * 
	 * @param mode
	 *            the primitive mode
	 */
	public void setPrimitiveMode(PrimitiveMode mode);

	/**
	 * Get the primitive type of the surface
	 * 
	 * @return the primitive type
	 */
	public PrimitiveType getPrimitiveType();

	/**
	 * Set the primitive type of the surface
	 * 
	 * @param type
	 *            the primitive type
	 */
	public void setPrimitiveType(PrimitiveType type);

	/**
	 * Add a color to teh color array of the surface
	 * 
	 * @param p
	 *            the color(dimension 4 rgba)
	 */
	public void addColor(Point p);

	/**
	 * Set a color in a concrete position of the color array
	 * 
	 * @param position
	 *            the concrete position
	 * @param p
	 *            the color(dimension 4 rgba)
	 */
	public void setColorAt(int position, Point p);

	/**
	 * Get the color at concrete position
	 * 
	 * @param position
	 *            the concrete position
	 * @return the color
	 */
	public Point getColorAt(int position);

	/**
	 * Remove color in a concrete position
	 * 
	 * @param position
	 *            the concrete position
	 */
	public void removeColor(int position);

	/**
	 * Get the number of colors in the color array of the surface
	 * 
	 * @return
	 */
	public int getNumColors();

	/**
	 * Sets the colorbinding of the color array of the surface
	 * 
	 * @param binding
	 *            attribute binding representing color binding
	 */
	public void setColorBinding(AttributeBinding binding);

	/**
	 * Get the color binding of the color array of the surface
	 * 
	 * @return Attribute binding representing color binding
	 */
	public AttributeBinding getColorBinding();

}
