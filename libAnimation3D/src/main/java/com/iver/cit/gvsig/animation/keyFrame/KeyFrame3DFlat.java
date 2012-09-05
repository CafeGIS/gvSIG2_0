/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
*
* Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
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
*/

package com.iver.cit.gvsig.animation.keyFrame;

import java.util.List;

import com.iver.ai2.gvsig3d.camera.ProjectCamera;
import com.iver.cit.gvsig.animation.keyframe.IKeyFrame;
import com.iver.utiles.IPersistence;
import com.iver.utiles.XMLEntity;

/**
 * @author Ángel Fraile Griñán  e-mail: angel.fraile@iver.es
 * @since 1.1
 * 
 */
public class KeyFrame3DFlat implements IKeyFrame {

	private String name;
	private double time;
	
	private  String aliasKeyFrame = "KeyFrame3DFlat";

	ProjectCamera projectCamera = null;

	

	public void CapturesProperties() {
		// TODO Auto-generated method stub

	}

	public String getName() {
		return this.name;
	}

	public List<IKeyFrame> getPropertiesList() {
		return null;
	}

	public void setName(String name) {
		this.name = name;

	}

	public void setPropertiesList(List<IKeyFrame> list) {
		// TODO Auto-generated method stub
	}

	public String toString() {
		String result;
		result = " keyframe3DFlats:\n";
		result += " tiempo: " + this.time;
		result += " nombre del extent: " + this.projectCamera.getDescription();
		return result;
	}

	

	public Object getAnimatedObject() {
		return projectCamera;
	}
	public void setAnimatedObject(Object object) {
		this.projectCamera = (ProjectCamera) object;
	}

	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}

	public String getClassName() {
		return this.getClass().getName();
	}

	public XMLEntity getXMLEntity() {
		XMLEntity xml = new XMLEntity();
		
		xml.putProperty("className", this.getClassName());
		xml.putProperty("keyFrameAlias", aliasKeyFrame);
//		xml.putProperty("keyFrameName", name);
		xml.putProperty("time", time);
		
		xml.addChild(((IPersistence)projectCamera).getXMLEntity());
		
		return xml;
	}

	public void setXMLEntity(XMLEntity xml) {
		
		if (xml.contains("time"))
			this.time = xml.getDoubleProperty("time");
		if (xml.contains("keyFrameAlias"))
			this.aliasKeyFrame = xml.getStringProperty("keyFrameAlias");
		
		// Get camera
		XMLEntity xmlProp = xml.getChild(0);
		if (xmlProp.contains("eyeX")) {
			try {
				String className = xmlProp.getStringProperty("className");
				Class<?> classProp = Class.forName(className);
				Object obj = classProp.newInstance();
				IPersistence objPersist = (IPersistence) obj;
				objPersist.setXMLEntity(xmlProp);
				projectCamera = (ProjectCamera) obj;
			} catch (Exception e) {

			}
		}
	}
}
