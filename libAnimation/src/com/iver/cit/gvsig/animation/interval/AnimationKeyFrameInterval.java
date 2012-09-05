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

package com.iver.cit.gvsig.animation.interval;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.iver.cit.gvsig.animation.IAnimationType;
import com.iver.cit.gvsig.animation.keyframe.IAnimationTypeKeyFrame;
import com.iver.cit.gvsig.animation.keyframe.IKeyFrame;
import com.iver.cit.gvsig.animation.keyframe.interpolator.IInterpolator;
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

public class AnimationKeyFrameInterval implements IAnimationTimeInterval {

	private List<IKeyFrame> KeyFrameList;

	private double initialTime = 0;

	private double endTime = 1;

	private double intervalTime = endTime - initialTime;

	private int keyFrameListSize;

	private IKeyFrame keyframe;
	

	public List<IKeyFrame> getKeyFrameList() {
		return KeyFrameList;
	}

	public void setKeyFrameList(List<IKeyFrame> keyFrameList) {
		KeyFrameList = keyFrameList;
	}

	public double getEndTime() {
		return endTime;
	}

	public double getInitialTime() {
		return initialTime;
	}

	public double getIntervalTime() {
		return (endTime - initialTime);
	}

	public void setEndTime(double time) {
		this.endTime = time;

	}

	public void setInitialTime(double time) {
		this.initialTime = time;
	}

	public void setIntervalTime(double time) {
		this.intervalTime = time;
	}

	public String toString() {

		String result;
		List<IKeyFrame> KFL = this.KeyFrameList;
		if (KFL != null) {
			result = "Mostrando lista de KeyFrames del intervalo Tini:"
					+ this.getInitialTime() + " Tend" + this.getEndTime();
			for (Iterator<IKeyFrame> iter = KFL.iterator(); iter.hasNext();) {
				Object element = (Object) iter.next();
				result += "\n" + element;
			}
		} else {
			result = "Lista de key frame vacia";
		}
		return result;
	}

	
	public void apply(double tini, double tend, IAnimationType animationType,
			Object animated) {

		IAnimationTypeKeyFrame animationTypeFrame = (IAnimationTypeKeyFrame) animationType;
		IInterpolator interpolator = animationTypeFrame.getInterpolator();

		IKeyFrame previus = null;
		IKeyFrame next = null;
		List<IKeyFrame> KFLInterpolate = new ArrayList<IKeyFrame>();

		List<IKeyFrame> KFL = this.KeyFrameList;
		if (KFL != null) {
			for (Iterator<IKeyFrame> iter = KFL.iterator(); iter.hasNext();) {
				IKeyFrame element = (IKeyFrame) iter.next();
				double KFtime = element.getTime();
				boolean asigned = false;
				if (KFtime <= tini) {
					if (previus != null) {
						if (KFtime > previus.getTime())
							previus = element;
					} else {
						previus = element;
					}
					asigned = true;
				}
				if ((KFtime > tini) && (!asigned)) {
					if (next != null) {
						if (KFtime < previus.getTime())
							next = element;
					} else {
						next = element;
					}

				}

			}
			KFLInterpolate.add(previus);
			KFLInterpolate.add(next);
		}

		IKeyFrame frame = null;
		if (KFLInterpolate != null) {
			frame = interpolator.interpolate(KFLInterpolate, 0, tini);
		}
		if (frame != null)
			animationType.AppliesToObject(frame);

	}
	
	public String getClassName() {
		return this.getClass().getName();
	}
	
	public int getKeyFrameListSize() {
		return keyFrameListSize;
	}

	public void setKeyFrameListSize(int keyFrameListSize) {
		this.keyFrameListSize = keyFrameListSize;
	}

	
	/*
	 * IPersistence methods.
	 */

	public XMLEntity getXMLEntity() {
		XMLEntity xml = new XMLEntity();

		xml.putProperty("className", this.getClassName());
		xml.putProperty("keyFrameListSize", this.KeyFrameList.size());
		xml.putProperty("beginTime", initialTime);
		xml.putProperty("endTime", endTime);
		xml.putProperty("intervalTime", intervalTime);

		//all the keyFrames in the interval.
		for (int i = 0; i < this.KeyFrameList.size(); i++) {
			IKeyFrame keyFrameItem = (IKeyFrame) this.KeyFrameList.get(i);	
			xml.addChild(((IPersistence)keyFrameItem).getXMLEntity());
		}
		return xml;		

	}

	public void setXMLEntity(XMLEntity xml) {
		
		if (xml.contains("keyFrameListSize"))
			this.keyFrameListSize=	xml.getIntProperty("keyFrameListSize");
		if (xml.contains("beginTime"))
			this.initialTime = xml.getDoubleProperty("beginTime");
		if (xml.contains("endTime"))
			this.endTime = xml.getDoubleProperty("endTime");
		if (xml.contains("intervalTime"))
			this.intervalTime = xml.getDoubleProperty("intervalTime");
		
		
		// Node IKeyFrame in the animation tree. Calling setXmlEntity() methods.
		// Reconstruyendo el framework. Paso: nuevos(según el número de keyframes del intervalo) IKeyFrame, del  
		// 									  tipo KeyFrameX, según la animación.
		List<IKeyFrame> timeKeyFrameList = new ArrayList<IKeyFrame>();
		for (int i = 0; i < this.keyFrameListSize; i++) {// Desde el hijo 0.
			
			XMLEntity xmlKeyframe = xml.getChild(i);
			
			ExtensionPoints extensionPoints = ExtensionPointsSingleton.getInstance();
			ExtensionPoint extPoint = ((ExtensionPoint) extensionPoints.get("Animation"));
			
			String aliasKeyFrameName = xmlKeyframe.getStringProperty("keyFrameAlias");
			
			try {
				keyframe = (IKeyFrame) extPoint.create(aliasKeyFrameName);
				keyframe.setXMLEntity(xmlKeyframe);
				timeKeyFrameList.add(keyframe);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		
		}
		this.setKeyFrameList(timeKeyFrameList);
	}

}