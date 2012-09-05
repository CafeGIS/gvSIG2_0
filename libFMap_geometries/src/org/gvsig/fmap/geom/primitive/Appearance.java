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
 * 2009 Instituto de Automática e Informática Industrial, UPV.
 */

package org.gvsig.fmap.geom.primitive;

import java.awt.Color;
import java.awt.Image;

/**
 * This interface serves to set the appearance of a Solid in a CityGML model.
 * 
 * @author <a href="mailto:jtorres@ai2.upv.es">Jordi Torres Fabra</a>
 * 
 */

public interface Appearance {

	public interface Material {

	

		public static final class Face {
			public static final int FRONT = 0;
			public static final int BACK = 1;
			public static final int FRONT_AND_BACK = 2;
		}
		/**
		 * Gets the face to apply the material
		 * @return the face
		 */
		public Face getFace();
		
		/**
		 * Sets the face where the material will be applied
		 * @param face the face
		 */
		public void setFace(Face face);
		/**
		 * Get the ambient color of the material
		 * @return the ambien color
		 */
		public Color getAmbient();
		/**
		 * Set the ambient color of the material
		 * @param ambient the ambien color
		 */
		public void setAmbient(Color ambient);
		/**
		 * Get the diffuse color of the material
		 * @return the diffuse color
		 */
		public Color getDiffuse();
		/**
		 * Set the diffuse color of the material
		 * @param diffuse the diffuse color
		 */
		public void setDiffuse(Color diffuse);
		/**
		 * Get the specular color of the material 
		 * @return the specular color
		 */
		public Color getSpecular();
		/**
		 * Set the specular color of the material
		 * @param specular the specular color
		 */
		public void setSpecular(Color specular);
		/**
		 * Get the emissive color of the material
		 * @return	the emissive color
		 */
		public Color getEmission();
		/**
		 * Set the emissive color of the material
		 * @param emission the emissive color 
		 */
		public void setEmission(Color emission);
		/**
		 * Get the shininess of the material
		 * @return a float representing the shininess
		 */
		public float getShininess();
		/**
		 * Set the shininess of the material
		 * @param shininess a float representing the shininess
		 */
		public void setShininess(float shininess);

	}

	/**
	 * Enables/disables the blending in a solid
	 * 
	 * @param value
	 *            the value to set
	 */
	public void setEnabledBlending(boolean value);

	/**
	 * Gets if blending is enabled/disabled
	 * 
	 * @return a boolean representing enabled(true)/disabled(false)
	 */
	public boolean getEnabledBlending();

	/**
	 * Enables disables lighting in a solid
	 * 
	 * @param value
	 *            the value to set
	 */
	public void setEnabledLighting(boolean value);

	/**
	 * Gets if lighting is enabled/disabled
	 * 
	 * @return a boolean representing enabled(true)/disabled(false)
	 */
	public boolean getEnabledLighting();

	/**
	 * Sets the material of a solid
	 * 
	 * @param mat
	 *            the material to set
	 */
	public void setMaterial(Material mat);

	/**
	 * Gets the material of a Solid
	 * 
	 * @return the material
	 */
	public Material getMaterial();

	/**
	 * Add a texture to the whole Solid
	 * 
	 * @param img
	 *            the texture
	 * @param stage
	 *            the texture stage
	 */
	public void addTexture(Image img, int stage);

	/**
	 * Get the texure of a solid in a concrete stage
	 * 
	 * @param stage
	 *            the concrete stage
	 * @return the texture
	 */
	public Image getTexture(int stage);

}
