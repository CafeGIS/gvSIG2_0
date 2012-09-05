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

package com.iver.cit.gvsig.animation.traks;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.iver.cit.gvsig.animation.IAnimationType;
import com.iver.cit.gvsig.animation.interval.AnimationFunctionInterval;
import com.iver.cit.gvsig.animation.interval.AnimationKeyFrameInterval;
import com.iver.cit.gvsig.animation.interval.IAnimationInterval;
import com.iver.cit.gvsig.animation.interval.IAnimationTimeInterval;
import com.iver.utiles.IPersistence;
import com.iver.utiles.XMLEntity;
import com.iver.utiles.extensionPoints.ExtensionPoint;
import com.iver.utiles.extensionPoints.ExtensionPoints;
import com.iver.utiles.extensionPoints.ExtensionPointsSingleton;

/**
 * @author Ángel Fraile Griñán  e-mail: angel.fraile@iver.es
 * @since 1.1
 * 
 */

public class AnimationTimeTrack implements IAnimationTrack {

	private IAnimationType animationType;

	private String name;

	private String description;

	private double beginTime;

	private double endTime;

	private List<IAnimationInterval> animationTimeIntervalList;

	private boolean enable;

	private Object animatedObject;

	private String className;

	private int intervalListSize;

	private IAnimationTimeInterval keyframeTrack;

	public AnimationTimeTrack() {

	}

	public AnimationTimeTrack(IAnimationType animationType) {
		this.animationTimeIntervalList = new ArrayList();
		this.animationType = animationType;
	}

	public Object getAnimatedObject() {
		if (this.animatedObject == null)
			return this.getAnimationType().getAnimatedObject();
		else
			return this.animatedObject;
	}

	public String getDescription() {
		return this.description;
	}

	public List getIntervalList() {
		return animationTimeIntervalList;
	}

	public String getName() {
		return this.name;
	}

	public void setAnimatedObject(Object animatedObject) {
		this.animatedObject = animatedObject;
		if (this.getAnimationType() != null) {
			this.getAnimationType().setAnimatedObject(animatedObject);
		}
	}

	public void setIntervalList(List intervalList) {
		animationTimeIntervalList = intervalList;
	}

	public double getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(double beginTime) {
		this.beginTime = beginTime;
	}

	public double getendTime() {
		return endTime;
	}

	public void setendTime(double endTime) {
		this.endTime = endTime;
	}

	public IAnimationType getAnimationType() {
		return this.animationType;
	}

	public boolean isEnable() {
		return this.enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public IAnimationInterval createKeyFrameInterval() {
		List ATIList = this.getIntervalList();
		if (ATIList == null) {
			ATIList = new ArrayList();
		}
		AnimationKeyFrameInterval ATInterval = new AnimationKeyFrameInterval();
		ATIList.add(ATInterval);
		return ATInterval;
	}

	public IAnimationInterval createFunctionInterval() {
		List ATIList = this.getIntervalList();
		if (ATIList == null) {
			ATIList = new ArrayList();
		}
		AnimationFunctionInterval AFInterval = new AnimationFunctionInterval();
		ATIList.add(AFInterval);
		return AFInterval;
	}

	
	public void removeInterval(IAnimationInterval animationInterval) {
		this.animationTimeIntervalList.remove(animationInterval);
	}
	public void removeAllIntervals() {
		//this.keyframeTrack.removeAllIntervals();
		for (int i=animationTimeIntervalList.size() - 1; i>=0; i--) {
			removeInterval(animationTimeIntervalList.get(i));
		}
	}
	
	public void setAnimationType(IAnimationType animationType) {
		this.animationType = animationType;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {

		String result;
		List ATIL = this.animationTimeIntervalList;
		result = "Mostrando lista de Intervalos de la track " + this.getName()
				+ ":" + this.getDescription();
		result += "\n**********************************************";
		result += "\nTipo de objetos que contiene "
				+ this.getAnimationType().getClassName();
		result += "\nNombre: " + this.getAnimationType().getName();
		result += "\nDescripcion: " + this.getAnimationType().getName();
		result += "\n**********************************************";
		for (Iterator iter = ATIL.iterator(); iter.hasNext();) {
			Object element = (Object) iter.next();
			result += "\n" + element;
		}
		return result;
	}

	public void apply(double Tini, double Tend) {
		List ATTL = this.animationTimeIntervalList;
		if ((ATTL != null) && (!ATTL.isEmpty())) {
			for (int i = 0; i < ATTL.size(); i++) {
				IAnimationInterval element = (IAnimationInterval) ATTL.get(i);
				element.apply(Tini, Tend, this.getAnimationType(), this.getAnimatedObject());
			}
		}

	}

	/**
	 * Return the name of this class.
	 * 
	 * @return name.
	 */

	public String getClassName() {
		return this.getClass().getName();
	}

	public List getAnimationTimeIntervalList() {
		return animationTimeIntervalList;
	}

	public void setAnimationTimeIntervalList(List animationTimeIntervalList) {
		this.animationTimeIntervalList = animationTimeIntervalList;
	}

	/*
	 * IPersistence methods
	 * 
	 */

	public XMLEntity getXMLEntity() {
		// TODO Auto-generated method stub

		XMLEntity xml = new XMLEntity();
		xml.putProperty("className", this.getClassName());
		xml.putProperty("name", name);
		xml.putProperty("intervalListSize", this.animationTimeIntervalList
				.size());
		xml.putProperty("beginTime", beginTime);
		xml.putProperty("endTime", endTime);

		xml.addChild(((IPersistence) animationType).getXMLEntity());

		for (int i = 0; i < this.animationTimeIntervalList.size(); i++) {
			IAnimationTimeInterval timeInterval = (IAnimationTimeInterval) this.animationTimeIntervalList
					.get(i);
			xml.addChild(((IPersistence) timeInterval).getXMLEntity());
		}
		return xml;
	}

	public void setXMLEntity(XMLEntity xml) {
		// TODO Auto-generated method stub
		String class_name;

		// if (xml.contains("className"))
		// this.className = xml.getStringProperty("className");
		if (xml.contains("name"))
			// this.name = xml.getStringProperty("name");
			this.setName(xml.getStringProperty("name"));
		if (xml.contains("intervalListSize"))
			this.intervalListSize = xml.getIntProperty("intervalListSize");
		if (xml.contains("beginTime"))
			this.beginTime = xml.getDoubleProperty("beginTime");
		if (xml.contains("endTime"))
			this.endTime = xml.getDoubleProperty("endTime");

		// falta setAnimatedObject(view);

		XMLEntity xmlType = xml.getChild(0);

		ExtensionPoints extensionPoints = ExtensionPointsSingleton
				.getInstance();
		ExtensionPoint extPoint = ((ExtensionPoint) extensionPoints
				.get("Animation"));

		class_name = xmlType.getStringProperty("className");

		// Node IAnimationType in the animation tree. Calling setXmlEntity()
		// methods.
		// Reconstruyendo el framework. Paso: accediendo al tipo de animación
		// IAnimationType.
		try {
			this.animationType = (IAnimationType) extPoint.create(class_name);
			this.animationType.setXMLEntity(xmlType);
			this.setAnimationType(animationType);

		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		// Node IAnimationTimeInterval in the animation tree. Calling
		// setXmlEntity() methods.
		// Reconstruyendo el framework. Paso: nuevo IAnimationTimeInterva;
		// AnimationKeyFrameInterval o AnimationFunctionInterval.
		List timeIntervalList = new ArrayList<IAnimationTimeInterval>();
		for (int i = 1; i <= this.intervalListSize; i++) {// desde el hijo 1

			XMLEntity xmlInterval = xml.getChild(i);
			try {
				class_name = xmlInterval.getStringProperty("className");
				Class classTrack = Class.forName(class_name);
				Object obj = classTrack.newInstance();
				IPersistence objPersist = (IPersistence) obj;
				objPersist.setXMLEntity(xmlInterval);
				this.keyframeTrack = (IAnimationTimeInterval) obj;
				timeIntervalList.add(keyframeTrack);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		this.setAnimationTimeIntervalList(timeIntervalList);

	}
	
}