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

package com.iver.cit.gvsig.animation.animationType;

import com.iver.cit.gvsig.animation.AnimationFactory;


/**
 * @author Ángel Fraile Griñán  e-mail: angel.fraile@iver.es
 * @since 1.1
 * 
 */
public class AnimationTypeFactoryLayer3DFlat extends AnimationFactory {

	public static String registerName = "AnimationLayer3DFlat";
	
	
	public static AnimationLayer3DFlat animationLayer3DFlat;
	
	static {
		if (animationLayer3DFlat == null) {
			animationLayer3DFlat = new AnimationLayer3DFlat();
			animationLayer3DFlat.setClassName("AnimationLayer3DFlat");
			animationLayer3DFlat.setName("AnimationLayer3DFlat");
			animationLayer3DFlat.setDescription("Animacion basada en encuadres para vista 3D plana");
		}
	}
	
	/**
	 * Creating a new animation type.
	 */
	
	public Object create() {
		
		AnimationLayer3DFlat animationLayer3DFlatAux = new AnimationLayer3DFlat();
		animationLayer3DFlatAux.setClassName("AnimationLayer3DFlat");
		animationLayer3DFlatAux.setName("AnimationLayer3DFlat");
		animationLayer3DFlatAux.setDescription("Animacion basada en encuadres para vista 3D plana");
		
       return animationLayer3DFlatAux;
	}
	
	/**
	 * Registers in the points of extension the Factory with alias.
	 * 
	 */
	public static void register() {
		register(registerName, new AnimationTypeFactoryLayer3DFlat(), "com.iver.cit.gvsig.animation.AnimationLayer3DFlat");
	}

	/**
	 * Return the register name of this animation type.
	 */
	
	public String getRegisterName() {
		return registerName;
	}

}
