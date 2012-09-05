/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
 *
 * For more information, contact:
 *
 *  Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ibáñez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   IVER T.I. S.A
 *   Salamanca 50
 *   46005 Valencia
 *   Spain
 *
 *   +34 963163400
 *   dac@iver.es
 */
package com.iver.ai2.gvsig3d.camera;


import org.gvsig.osgvp.core.osg.Vec3;
import org.gvsig.osgvp.viewer.Camera;

import com.iver.utiles.IPersistence;
import com.iver.utiles.XMLEntity;

/**
 * This class is used to manage the cameras that the "herramienta de encuades"
 * needs like a data model
 * 
 * @author
 */
public class ProjectCamera implements IPersistence {
	// private Object camera;

	private Camera camera;

	private String description;

	/**
	 * Return the description
	 * 
	 * @return description of the camera
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Set the description
	 * 
	 * @param the
	 *            description of the camera
	 */
	public void setDescription(String string) {
		description = string;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return description;
	}

	/**
	 * Return the camera object
	 * 
	 * @return camera object
	 */
	public Camera getCamera() {
		return camera;
	}

	/**
	 * Set the camera object
	 * 
	 * @param the camera object
	 */
	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	/* (non-Javadoc)
	 * @see com.iver.utiles.IPersistence#getClassName()
	 */
	public String getClassName() {
		return this.getClass().getName();
	}

	/* (non-Javadoc)
	 * @see com.iver.utiles.IPersistence#getXMLEntity()
	 */
	public XMLEntity getXMLEntity() {
		XMLEntity xml = new XMLEntity();
		xml.putProperty("className", this.getClassName());

		xml.putProperty("description", this.getDescription());
		// Saving camera propeties

		// Eye
		xml.putProperty("eyeX", this.camera.getEye().x());
		xml.putProperty("eyeY", this.camera.getEye().y());
		xml.putProperty("eyeZ", this.camera.getEye().z());
		// Center
		xml.putProperty("centerX", this.camera.getCenter().x());
		xml.putProperty("centerY", this.camera.getCenter().y());
		xml.putProperty("centerZ", this.camera.getCenter().z());
		// Up
		xml.putProperty("upX", this.camera.getUp().x());
		xml.putProperty("upY", this.camera.getUp().y());
		xml.putProperty("upZ", this.camera.getUp().z());

		return xml;
	}

	/* (non-Javadoc)
	 * @see com.iver.utiles.IPersistence#setXMLEntity(com.iver.utiles.XMLEntity)
	 */
	public void setXMLEntity(XMLEntity xml) {
		if (xml.contains("description"))
			this.setDescription(xml.getStringProperty("description"));

		// Getting camera properties
		camera = new Camera();

		// Variables auxiliares
		Vec3 center = new Vec3(), eye = new Vec3(), up = new Vec3();

		// Eye
		if (xml.contains("eyeX"))
			eye.setX(xml.getDoubleProperty("eyeX"));
		if (xml.contains("eyeY"))
			eye.setY(xml.getDoubleProperty("eyeY"));
		if (xml.contains("eyeZ"))
			eye.setZ(xml.getDoubleProperty("eyeZ"));

		// Center
		if (xml.contains("centerX"))
			center.setX(xml.getDoubleProperty("centerX"));
		if (xml.contains("centerY"))
			center.setY(xml.getDoubleProperty("centerY"));
		if (xml.contains("centerZ"))
			center.setZ(xml.getDoubleProperty("centerZ"));

		// Up
		if (xml.contains("upX"))
			up.setX(xml.getDoubleProperty("upX"));
		if (xml.contains("upY"))
			up.setY(xml.getDoubleProperty("upY"));
		if (xml.contains("upZ"))
			up.setZ(xml.getDoubleProperty("upZ"));

		this.camera.setViewByLookAt(eye, center, up);

	}
}
