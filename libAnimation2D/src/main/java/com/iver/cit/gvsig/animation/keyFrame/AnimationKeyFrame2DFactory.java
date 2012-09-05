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

import com.iver.cit.gvsig.animation.AnimationFactory;


/**
 * @author Ángel Fraile Griñán  e-mail: angel.fraile@iver.es
 * @since 1.1
 * 
 */
public class AnimationKeyFrame2DFactory extends AnimationFactory {

	public static String registerName = "KeyFrame2D";
	
	
	/**
	 * Creating a new keyFrmame 2D in the factory.
	 */
	public Object create() {
		KeyFrame2D keyFrameFlat = new KeyFrame2D();
        return keyFrameFlat;
	}
	
	/**
	 * Registers in the points of extension the Factory with alias.
	 */
	public static void register() {
		register(registerName, new AnimationKeyFrame2DFactory(), "com.iver.cit.gvsig.animation.KeyFrame2D");
	}

	public String getRegisterName() {
		return registerName;
	}

}