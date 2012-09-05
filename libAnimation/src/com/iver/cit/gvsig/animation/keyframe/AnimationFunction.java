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

package com.iver.cit.gvsig.animation.keyframe;

import java.util.List;

import com.iver.utiles.IPersistence;
import com.iver.utiles.XMLEntity;


/**
 * @author Ángel Fraile Griñán  e-mail: angel.fraile@iver.es
 * @since 1.1
 * 
 */

public class AnimationFunction implements IPersistence{

	private List AnimationFunctionList;
	//private String className;
	
	//private AnimationFunction animationFunctionItem;

	public List getAnimationFuntionList() {
		return AnimationFunctionList;
	}

	public void setAnimationFuntionList(List animationFuntionList) {
		AnimationFunctionList = animationFuntionList;
	}

	public String toString() {

		String result = "";

		return result;
	}

	
	public String getClassName() {
		return this.getClass().getName();
	}

	
	public XMLEntity getXMLEntity() {
		XMLEntity xml = new XMLEntity();
		
		xml.putProperty("class_name", this.getClassName());		
		for (int i = 0; i < this.AnimationFunctionList.size(); i++) {
			AnimationFunction animationFunctionItem = (AnimationFunction) this.AnimationFunctionList.get(i);	
			xml.addChild(((IPersistence)animationFunctionItem).getXMLEntity());
			return xml;//provisional	
		}	
		return xml;
	}

	
	public void setXMLEntity(XMLEntity xml) {
		// TODO Auto-generated method stub
//		
//		if (xml.contains("class_name"))
//			className = xml.getStringProperty("class_name");	
//		for (int i = 0; i < this.AnimationFunctionList.size(); i++) {
//			AnimationFunction animationFunctionItem = (AnimationFunction) this.AnimationFunctionList.get(i);			
//			return;	 //provisional
//		}	
//		
	}
}
