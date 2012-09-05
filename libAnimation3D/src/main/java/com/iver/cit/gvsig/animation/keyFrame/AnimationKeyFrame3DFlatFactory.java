/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
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

import com.iver.cit.gvsig.animation.AnimationFactory;

/**
 * @author �ngel Fraile Gri��n  e-mail: angel.fraile@iver.es
 * @since 1.1
 * 
 */
public class AnimationKeyFrame3DFlatFactory extends AnimationFactory {

	public static String registerName = "KeyFrame3DFlat";
	
	
	/**
	 * Creating a new keyFrmame 3DFlat in the factory.
	 */
	public Object create() {
		KeyFrame3DFlat keyFrameFlat = new KeyFrame3DFlat();
        return keyFrameFlat;
	}
	
	/**
	 * Registers in the points of extension the Factory with alias.
	 */
	public static void register() {
		register(registerName, new AnimationKeyFrame3DFlatFactory(), "com.iver.cit.gvsig.animation.KeyFrame3DFlat");
	}

	public String getRegisterName() {
		return registerName;
	}

}