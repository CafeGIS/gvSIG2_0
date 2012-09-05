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

import com.iver.cit.gvsig.animation.IAnimationType;
import com.iver.cit.gvsig.animation.keyframe.AnimationFunction;
import com.iver.utiles.IPersistence;
import com.iver.utiles.XMLEntity;

/**
 * @author Ángel Fraile Griñán  e-mail: angel.fraile@iver.es
 * @since 1.1
 * 
 */

public class AnimationFunctionInterval implements IAnimationTimeInterval {

	private AnimationFunction animationFunctionItem;

	private double initialTime = 0;

	private double endTime = 1;

	private double intervalTime = endTime - initialTime;

	public AnimationFunction getAnimationFuncionItem() {
		return animationFunctionItem;
	}

	public void setAnimationFuncionList(AnimationFunction animationFuncionItem) {
		this.animationFunctionItem = animationFuncionItem;
	}

	public double getEndTime() {
		return this.endTime;
	}

	public double getInitialTime() {
		return this.initialTime;
	}

	public double getIntervalTime() {
		return endTime - initialTime;
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
		//List AFL = this.animationFuncionList;
		result = "Mostrando lista de funciones de animacion del intervalo Tini:"
				+ this.getInitialTime() + " Tend" + this.getEndTime();
		
		result+= this.animationFunctionItem;	
		
		return result;
	}

	public void apply(double tini, double tend, IAnimationType animationType,
			Object animated) {
		// TODO Auto-generated method stub
	}

	public String getClassName() {
		// TODO Auto-generated method stub
		return this.getClass().getName();
	}
	
	/*
	 * IPersistence methods.
	 */

	public XMLEntity getXMLEntity() {
		// TODO Auto-generated method stub
		XMLEntity xml = new XMLEntity();
		xml.putProperty("class_name", this.getClassName());		
		xml.putProperty("begin_time", initialTime);
		xml.putProperty("end_time", endTime);
		xml.putProperty("interval_time", intervalTime);
		//this.animationFunctionItem.getXMLEntity();	
		xml.addChild(((IPersistence)animationFunctionItem).getXMLEntity());
		
		return xml;
	}


	public void setXMLEntity(XMLEntity xml) {
		if (xml.contains("begin_time"))
			initialTime = xml.getDoubleProperty("begin_time");
		if (xml.contains("end_time"))
			endTime = xml.getDoubleProperty("end_time");
		if (xml.contains("interval_time"))
			intervalTime = xml.getDoubleProperty("interval_time");
				
	}

	public void removeAllIntervals() {
		// TODO Auto-generated method stub
		
	}
}