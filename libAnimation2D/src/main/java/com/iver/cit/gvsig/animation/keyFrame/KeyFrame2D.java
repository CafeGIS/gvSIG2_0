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

import java.awt.geom.Rectangle2D;
import java.util.List;

import com.iver.cit.gvsig.animation.keyframe.IKeyFrame;
import com.iver.cit.gvsig.project.ProjectExtent;
import com.iver.utiles.XMLEntity;

/**
 * @author Ángel Fraile Griñán  e-mail: angel.fraile@iver.es
 * @since 1.1
 * 
 */
public class KeyFrame2D implements IKeyFrame {

	private String name;
	private double time;
	private  String aliasKeyFrame = "KeyFrame2D";

	ProjectExtent projectExtent = null;
	private double valueX;
	private double valueY;
	private double valueW;
	private double valueH;


	public void CapturesProperties() {
		// TODO Auto-generated method stub
	}

	public void CapturesProperties(ProjectExtent projectExtent) {
		this.projectExtent = projectExtent;
	}

	public String getName() {
		return this.name;
	}

	public List<IKeyFrame> getPropertiesList() {
		// TODO Auto-generated method stub
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
		result = " keyframe2D:\n";
		result += " tiempo: " + this.time;
		result += " nombre del extent: " + this.projectExtent.getDescription();

		return result;
	}


	public Object getAnimatedObject() {
		return projectExtent;
	}
	public void setAnimatedObject(Object object) {
		this.projectExtent = (ProjectExtent) object;
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
		xml.putProperty("time", time);
		
		xml.addChild((this.projectExtent).getXMLEntity());
		
		return xml;
	}

	public void setXMLEntity(XMLEntity xml) {
		
		if (xml.contains("time"))
			this.time = xml.getDoubleProperty("time");
		if (xml.contains("keyFrameAlias"))
			this.aliasKeyFrame = xml.getStringProperty("keyFrameAlias");
		
		XMLEntity xmlProp = xml.getChild(0);
		if (xmlProp.contains("extentX")) {
			valueX = xmlProp.getDoubleProperty("extentX");
		}
		if (xmlProp.contains("extentY")) {
			valueY = xmlProp.getDoubleProperty("extentY");
		}
		if (xmlProp.contains("extentW")) {
			valueW = xmlProp.getDoubleProperty("extentW");
		}
		if (xmlProp.contains("extentH")) {
			valueH = xmlProp.getDoubleProperty("extentH");
		}
		
		Rectangle2D rectangle2D = new Rectangle2D.Double(valueX, valueY, valueW, valueH);
		this.projectExtent = new ProjectExtent();
		this.projectExtent.setExtent(rectangle2D);
		
	}
}
