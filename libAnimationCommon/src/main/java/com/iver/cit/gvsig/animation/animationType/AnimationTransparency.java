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
import com.iver.cit.gvsig.animation.animatedObject.AnimationObjectTransparency;
import com.iver.cit.gvsig.animation.animatedObject.IAnimatedObject;
import com.iver.cit.gvsig.animation.keyFrame.KeyFrameTransparency;
import com.iver.cit.gvsig.animation.keyframe.IAnimationTypeKeyFrame;
import com.iver.cit.gvsig.animation.keyframe.interpolator.FuntionFactory;
import com.iver.cit.gvsig.animation.keyframe.interpolator.IInterpolator;
import com.iver.cit.gvsig.animation.keyframe.interpolator.IInterpolatorTimeFuntion;
import com.iver.cit.gvsig.fmap.layers.FLyrDefault;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;
import com.iver.utiles.IPersistence;
import com.iver.utiles.XMLEntity;


/**
 * @author Ángel Fraile Griñán  e-mail: angel.fraile@iver.es
 * @since 1.1
 * 
 */
public class AnimationTransparency implements IAnimationTypeKeyFrame {

	private String description = "Animacion basada en Transparencias";
	private String name = "AnimationTransparency";
	private int typeTrack = IAnimationType.TIME_TYPE_TRACK;
	private IInterpolator interpolator;
	private String nameView;
	private IAnimatedObject animationObjectTransparency = new AnimationObjectTransparency() ;
	private String titleWindow;

	public AnimationTransparency() {
	}

	public String getClassName() {
		return this.getClass().getName();
	}

	public String getDescription() {
		return description;
	}

	public String getName() {
		return name;
	}

	public void setClassName(String className) {
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @param animate : Object to animate.
	 *  Application of attributes to our object animated.
	 *  In this animation the object is a layer(FLyerDefault).
	 *  Attributes: layer transparency.
	 *  invalidate(): repaint the view. 
	 */
	
	public void AppliesToObject(Object animated) {

		KeyFrameTransparency kf = (KeyFrameTransparency) animated;
		FLyrDefault layer = (FLyrDefault)kf.getAnimatedObject();
		int trans = (int)kf.getLevelTransparency();
		
	//	System.err.println("antes: " + System.currentTimeMillis());
	//	long antes = System.currentTimeMillis();
		
		layer.setTransparency(trans);	
		layer.getMapContext().invalidate();
		
	//	System.err.println("despues: " + System.currentTimeMillis());
	//	long despues = System.currentTimeMillis();
	//	System.err.println("intervalo de tiempo: " + (despues-antes));
		
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
		if(this.animationObjectTransparency != null){
			// getting all views.
			try{
				IWindow[] viewArray = PluginServices.getMDIManager().getAllWindows();
				for (int i = 0; i < viewArray.length; i++) {
					IWindow window = viewArray[i];
					if (window.getClass().getName().equals(this.nameView)){
						// get the actual name of the view, and compare it with the view saved in xml.
						if(window.getWindowInfo().getTitle().equals(this.titleWindow)){
							BaseView newView = (BaseView)window;
							this.animationObjectTransparency.addAnimatedObject("view", newView);
							this.setAnimatedObject(this.animationObjectTransparency);
							IInterpolatorTimeFuntion funtion = FuntionFactory.createObject("com.iver.cit.gvsig.animation.keyframe.interpolator.LinearFuntion");
							this.interpolator.setFuntion(funtion);
						}
					}
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return this.animationObjectTransparency;
	}

	public void setAnimatedObject(Object object) {
		this.animationObjectTransparency = (IAnimatedObject) object;
	}

	/*
	 * IPersistence methods.
	 */
	
	public XMLEntity getXMLEntity() {
		
		
		XMLEntity xml = new XMLEntity();
		xml.putProperty("className", this.getClassName());
		xml.putProperty("description", description);
		xml.putProperty("animationTrackTipe", typeTrack);
		
		BaseView myActualView =  (BaseView) animationObjectTransparency.getAnimatedObject("view");
		xml.putProperty("nameClassView", myActualView.getClass().getName());
		
		titleWindow = myActualView.getWindowInfo().getTitle();
		xml.putProperty("titleWindow", titleWindow);
		xml.addChild(((IPersistence)this.interpolator).getXMLEntity());
		return xml;
	}

	public void setXMLEntity(XMLEntity xml) {
		
		
		if (xml.contains("animationTrackTipe"))
			this.typeTrack = xml.getIntProperty("animationTrackTipe");
		if (xml.contains("nameView"))
			this.nameView =	xml.getStringProperty("nameView");
		
		//Acceding to the InterpolatorX of the AnimationTypeX
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
