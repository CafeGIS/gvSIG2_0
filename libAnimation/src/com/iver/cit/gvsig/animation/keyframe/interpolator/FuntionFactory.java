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

package com.iver.cit.gvsig.animation.keyframe.interpolator;

import java.util.HashMap;
import java.util.Map;


/*
 * factoria para el registro y la creacion de funciones de animacion de tiempo
 * 
 * 
 */
public class FuntionFactory {

//	private static Map<String, IInterpolatorFuntion> objectsList;
	private static Map objectsList;

	static {
//		objectsList = new HashMap<String, IInterpolatorFuntion>();
		objectsList = new HashMap();
		LinearFuntion lf = new LinearFuntion();
		lf.setName("LinearFuntion");
		lf.setDescription("Funcion para el tiempo lineal");
		FuntionFactory.register(lf);
	}

	public static void register(IInterpolatorTimeFuntion funtion) {
		objectsList.put(funtion.getClassName(), funtion);
	}

	public static IInterpolatorTimeFuntion createObject(String type) {
		IInterpolatorTimeFuntion funtion = null;
		try {
			if ((objectsList.containsKey(type)) == false)
				return null;
			funtion = (IInterpolatorTimeFuntion) objectsList.get(type).getClass().newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return funtion;
	}

}
