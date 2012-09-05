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

package com.iver.cit.gvsig.animation.animatedObject;

import java.util.HashMap;
import java.util.Map;

public class AnimatedObjectBase implements IAnimatedObject {

	private Map<String, Object> objectsList;

	
	public AnimatedObjectBase() {
		super();
	}

	public void addAnimatedObject(String name, Object object) {
		if (objectsList == null)
			objectsList = new HashMap<String, Object>();			
		objectsList.put(name, object);
		
	}

	public Object getAnimatedObject(String name) {
		Object object = null;
		
		if ((objectsList.containsKey(name)) == false) {
			return null;
		} else {
			object = (Object) objectsList.get(name);
		}
		return object;

	}

}
