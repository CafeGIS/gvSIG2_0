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

import org.gvsig.osgvp.viewer.IViewerContainer;

import com.iver.cit.gvsig.project.documents.view.gui.BaseView;


/**
 * @author Ángel Fraile Griñán  e-mail: angel.fraile@iver.es
 * @since 1.1
 * 
 */
public class AnimationObject3DFlat extends AnimatedObjectBase {
	
	private Object view;
	
	public String getClassName() {
		return this.getClass().getName();
	}
	
	public void setClassName(String className) {
	}
	
	public IViewerContainer getAnimatedCanvas3D() {
		return (IViewerContainer) this.getAnimatedObject("canvas");
	}
	
	public void setAnimatedCanvas3D(IViewerContainer canvas3d) {
		this.addAnimatedObject("canvas", canvas3d);
	}
	
	public Object getAnimatedView() {
		return this.getAnimatedObject("view");
	}
	
	public void setAnimatedView(Object object) {
		this.addAnimatedObject("view", object);
	}
	
	public String getNameView() {
		return ((BaseView)view).getName();
		
	}
	
}
