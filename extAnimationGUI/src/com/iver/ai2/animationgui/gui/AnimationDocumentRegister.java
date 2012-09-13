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

package com.iver.ai2.animationgui.gui;

import com.iver.ai2.animationgui.gui.document.ProjectAnimationDocumentFactory;
import com.iver.andami.plugins.Extension;

public class AnimationDocumentRegister extends Extension{

	public void execute(String actionCommand) {
		// TODO Auto-generated method stub
	}

	public void initialize() {
		ProjectAnimationDocumentFactory.register();
	}

	public boolean isEnabled() {
		return false;
	}

	public boolean isVisible() {
		return false;
	}

}
