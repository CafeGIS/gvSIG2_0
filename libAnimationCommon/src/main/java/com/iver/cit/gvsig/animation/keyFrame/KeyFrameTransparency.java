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

import com.iver.cit.gvsig.animation.keyframe.IKeyFrame;
import com.iver.cit.gvsig.fmap.layers.FLyrDefault;
import com.iver.utiles.XMLEntity;


/**
 * @author Ángel Fraile Griñán  e-mail: angel.fraile@iver.es
 * @since 1.1
 * * Class to create a keyframe used in animation with transparency.
 */

public class KeyFrameTransparency implements IKeyFrame {

	private String name;
	private double time;
	private int levelTransparency;
	private boolean visibility;
	private FLyrDefault layerDefault;
	private String layerName;
	


	/**
	 * 
	 * @see com.iver.cit.gvsig.animation.keyframe.IKeyFrame#CapturesProperties()
	 * Capturing the property transparency of the layer and the property is selected?. 
	 */
	
	public void CapturesProperties() {
		if (layerDefault != null){
			levelTransparency = this.layerDefault.getTransparency();
			visibility = this.layerDefault.isVisible();//Check box is selected?.
		}
	}

	/**
	 * Getting the keyframe name.
	 */
	
	public String getName() {
		return this.name;
	}

	public List<IKeyFrame> getPropertiesList() {
		return null;
	}

	/**
	 * @param name: name of the keyframe.
	 * Setting the keyframe name.
	 */
	
	public void setName(String name) {
		this.name = name;

	}

	public void setPropertiesList(List<IKeyFrame> list) {
		// TODO Auto-generated method stub
	}

	/**
	 * Information of the keyframe in a String.
	 */
	
	public String toString() {
		String result;
		result = " keyframeTransparency:\n";
		result += " tiempo: " + this.time;
		// result= "Name: " + this.getName();
		return result;
	}

	/**
	 * 
	 * @return the Object to animate.(FLyrDefault)
	 */
	
	public Object getAnimatedObject() {
		return layerDefault;
	}

	/**
	 * 
	 * @param object : Object to animate.
	 * Setting the object to animate.(FLyrDefault).
	 * 
	 */
	public void setAnimatedObject(Object object) {
		this.layerDefault = (FLyrDefault) object;
	}

	/**
	 * Getting the time of the keyframe.
	 */
	
	public double getTime() {
		return time;
	}
	
	public double getLevelTransparency() {
		return levelTransparency;
	}

	public void setLevelTransparency(int levelTransparency) {
		this.levelTransparency = levelTransparency;
	}

	/**
	 * Setting the time of the keyframe.
	 */
	
	public void setTime(double time) {
		this.time = time;
	}

	/**
	 * @return the name of the class.
	 */
	public String getClassName() {
		// TODO Auto-generated method stub
		return this.getClass().getName();
	}

	/*	 
	 * IPersistence methods.
	 */

	public XMLEntity getXMLEntity() {
		
		XMLEntity xml = new XMLEntity();
		
		xml.putProperty("className", this.getClassName());
		xml.putProperty("keyFrameName", name);
		xml.putProperty("levelTransparency", levelTransparency);
		xml.putProperty("visibility", visibility);
		xml.putProperty("time", time);
		xml.putProperty("layerName", layerDefault.getClass().getName());
		
		return xml;
	}

	public void setXMLEntity(XMLEntity xml) {
		
	//	if (xml.contains("className"))
	//		this.className = xml.getStringProperty("className");
		if (xml.contains("keyFrameName"))
			this.name = xml.getStringProperty("keyFrameName");
		if (xml.contains("levelTransparency"))
			this.levelTransparency = xml.getIntProperty("levelTransparency");
		if (xml.contains("visibility"))
			this.visibility = xml.getBooleanProperty("visibility");
		if (xml.contains("time"))
			this.time = xml.getDoubleProperty("time");
		

		try {
			this.layerName = xml.getStringProperty("layerName");
			Class<?> classProp = Class.forName(this.layerName);
			Object obj = classProp.newInstance();
			this.layerDefault = (FLyrDefault) obj;
		} catch (Exception e) {
			
		}
	}
}
