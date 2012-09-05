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

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.animation.IAnimationType;
import com.iver.cit.gvsig.animation.animatedObject.AnimationObject2D;
import com.iver.cit.gvsig.animation.interpolator.Interpolator2D;
import com.iver.cit.gvsig.animation.keyFrame.KeyFrame2D;
import com.iver.cit.gvsig.animation.keyframe.IAnimationTypeKeyFrame;
import com.iver.cit.gvsig.animation.keyframe.interpolator.FuntionFactory;
import com.iver.cit.gvsig.animation.keyframe.interpolator.IInterpolator;
import com.iver.cit.gvsig.animation.keyframe.interpolator.IInterpolatorTimeFuntion;
import com.iver.cit.gvsig.project.ProjectExtent;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;
import com.iver.cit.gvsig.project.documents.view.gui.View;
import com.iver.utiles.IPersistence;
import com.iver.utiles.XMLEntity;


/**
 * @author Ángel Fraile Griñán  e-mail: angel.fraile@iver.es
 * @since 1.1
 * 
 */
public class AnimationLayer2D implements IAnimationTypeKeyFrame {

	private String className = "AnimationLayer2D";
	private String description = "Animacion para zoom de capas";
	private String name = "Animacion de zoom capas";
	private int typeTrack = IAnimationType.TIME_TYPE_TRACK;
	private IInterpolator interpolator;
	private View view;
	private AnimationObject2D animationObject2D = new AnimationObject2D() ;
	private String titleWindow;

	public AnimationLayer2D() {
		this.interpolator = new Interpolator2D();
	}

	public String getClassName() {
		return className;
	}

	public String getDescription() {
		return description;
	}

	public String getName() {
		return name;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	/**
	 * Repainting the view
	 */
	
	public void AppliesToObject(Object animated) {

		// Casting the animated object
		KeyFrame2D keyFrame2D = (KeyFrame2D) animated;
		this.view = (View) this.animationObject2D.getAnimatedView();
		this.view.getMapControl().getViewPort().setExtent(
				((ProjectExtent) keyFrame2D.getAnimatedObject()).getExtent());
		// Repainting the object
		this.view.getMapControl().drawMap(true);
	}

	public int getTypeTrack() {
		return typeTrack;
	}

	public void setTypeTrack(int typeTrack) {
		this.typeTrack = typeTrack;
	}

	public IInterpolator getInterpolator() {
		return this.interpolator;
	}

	public void setInterpolator(IInterpolator interpolator) {
		this.interpolator = interpolator;
	}

	/**
	 * Getting the animated object, in movement track animation; the view
	 */
	public Object getAnimatedObject() {
		if(this.animationObject2D != null) {
			// getting all views.
			try{
				IWindow[] viewArray = PluginServices.getMDIManager().getAllWindows();
				for (int i = 0; i < viewArray.length; i++) {
					IWindow window = viewArray[i];
						if(window.getWindowInfo().getTitle().equals(this.titleWindow)){
							BaseView newView = (BaseView)window;
							this.animationObject2D.setAnimatedView(newView);
							this.setAnimatedObject(this.animationObject2D);
							IInterpolatorTimeFuntion funtion = FuntionFactory.createObject("com.iver.cit.gvsig.animation.keyframe.interpolator.LinearFuntion");
							this.interpolator.setFuntion(funtion);
						}
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return this.animationObject2D;
	}
	
	
	public void setAnimatedObject(Object object) {
		if (object instanceof AnimationObject2D)
			this.animationObject2D = (AnimationObject2D) object;

	}
public XMLEntity getXMLEntity() {
		
		View myActualView = null;
		
		XMLEntity xml = new XMLEntity();
		xml.putProperty("className", this.getClassName());
		xml.putProperty("description", description);
		xml.putProperty("animationTrackTipe", typeTrack);
		myActualView = (View) animationObject2D.getAnimatedView();
		titleWindow = myActualView.getWindowInfo().getTitle();
		xml.putProperty("titleWindow", titleWindow);
		xml.addChild(((IPersistence)this.interpolator).getXMLEntity());
		return xml;
	}

	public void setXMLEntity(XMLEntity xml) {
		if (xml.contains("className"))
			this.className=	xml.getStringProperty("className");
		if (xml.contains("animationTrackTipe"))
			this.typeTrack = xml.getIntProperty("animationTrackTipe");
		if (xml.contains("titleWindow"))
			this.titleWindow = xml.getStringProperty("titleWindow");
		
		XMLEntity xmlInterpolator = xml.getChild(0);
		
		try {
			String class_name = xmlInterpolator.getStringProperty("className");
			Class<?> classInterpolator = Class.forName(class_name);
			Object obj = classInterpolator .newInstance();
			IPersistence objPersist = (IPersistence) obj;
			objPersist.setXMLEntity(xmlInterpolator);
			this.interpolator = (IInterpolator) obj;
			this.setInterpolator(interpolator);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
