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



/**
 * @author �ngel Fraile Gri��n  e-mail: angel.fraile@iver.es
 * @since 1.1
 * 
 */
package com.iver.cit.gvsig.animation.animationType;

import com.iver.cit.gvsig.animation.AnimationFactory;

public class AnimationTypeFactoryLayer2D extends AnimationFactory {

	public static String registerName = "AnimationLayer2D";
	
	
	public static AnimationLayer2D animationLayer2D;
	
	static {
		if (animationLayer2D == null) {
			animationLayer2D = new AnimationLayer2D();
			animationLayer2D.setClassName("AnimationLayer2D");
			animationLayer2D.setName("AnimationLayer2D");
			animationLayer2D.setDescription("Animacion basada en encuadres para vista 2D.");
		}
	}
	

	
	public Object create() {
		
       return animationLayer2D;
	}
	
	/**
	 * Registers in the points of extension the Factory with alias.
	 * 
	 */
	public static void register() {
		register(registerName, new AnimationTypeFactoryLayer2D(), "com.iver.cit.gvsig.animation.test.AnimationLayer2D");
	}

	public String getRegisterName() {
		return registerName;
	}

}
