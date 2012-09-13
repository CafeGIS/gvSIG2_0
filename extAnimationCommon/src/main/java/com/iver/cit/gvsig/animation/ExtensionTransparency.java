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

package com.iver.cit.gvsig.animation;

import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.animation.animationType.AnimationTypeFactoryTransparency;
import com.iver.cit.gvsig.animation.interpolator.InterpolatorTransparency;
import com.iver.cit.gvsig.animation.keyFrame.AnimationKeyFrameTransparencyFactory;
import com.iver.cit.gvsig.animation.keyframe.interpolator.InterpolatorFactory;

/**
 * @author Ángel Fraile Griñán  e-mail: angel.fraile@iver.es
 * @since 1.1
 * 
 */

public class ExtensionTransparency extends Extension {

	public void execute(String actionCommand) {

	}
	
	/**
	 * @see com.iver.andami.plugins.IExtension#initialize()
	 * Registering a new animation factory based in transparency and time with key frames
	 */
	public void initialize() {

		AnimationTypeFactoryTransparency.register();
		AnimationKeyFrameTransparencyFactory.register();
		
		// Registering the interpolators
		InterpolatorFactory.register("InterpolatorTransparency", new InterpolatorTransparency());
	}

	public boolean isEnabled() {
		return true;
	}

	public boolean isVisible() {

		return false;
	}

}
