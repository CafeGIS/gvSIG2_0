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

package com.iver.cit.gvsig.animation.test;

import com.iver.cit.gvsig.animation.IAnimationType;
import com.iver.cit.gvsig.animation.keyframe.interpolator.IInterpolator;
import com.iver.utiles.XMLEntity;

public class Animation3D implements IAnimationType {

	private String className;
	private String Description;
	private String Name;
	private int typeTrack = IAnimationType.TIME_TYPE_TRACK;

	public String getClassName() {
		return className;
	}

	public String getDescription() {
		return Description;
	}

	public String getName() {
		return Name;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public void setName(String name) {
		Name = name;
	}

	public void AppliesToObject(Object animated) {
		// TODO Auto-generated method stub
		
	}

	public int getTypeTrack() {
		return typeTrack;
	}

	public void setTypeTrack(int typeTrack) {
		this.typeTrack = typeTrack;
		
	}

	public Object getAnimatedObject() {
		return null;
	}

	public void setAnimatedObject(Object object) {
		// TODO Auto-generated method stub
		
	}

	public XMLEntity getXMLEntity() {
		return null;
	}

	public void setXMLEntity(XMLEntity arg0) {
		// TODO Auto-generated method stub
		
	}

	public void setInterpolator(IInterpolator interpolator) {
		// TODO Auto-generated method stub
	}
}
