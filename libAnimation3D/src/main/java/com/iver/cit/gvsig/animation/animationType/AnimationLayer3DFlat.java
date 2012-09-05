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

import org.gvsig.osgvp.viewer.IViewerContainer;

import com.iver.ai2.gvsig3d.camera.ProjectCamera;
import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.animation.IAnimationType;
import com.iver.cit.gvsig.animation.animatedObject.AnimationObject3DFlat;
import com.iver.cit.gvsig.animation.animatedObject.IAnimatedObject;
import com.iver.cit.gvsig.animation.interpolator.Interpolator3DFlat;
import com.iver.cit.gvsig.animation.keyFrame.KeyFrame3DFlat;
import com.iver.cit.gvsig.animation.keyframe.IAnimationTypeKeyFrame;
import com.iver.cit.gvsig.animation.keyframe.interpolator.FuntionFactory;
import com.iver.cit.gvsig.animation.keyframe.interpolator.IInterpolator;
import com.iver.cit.gvsig.animation.keyframe.interpolator.IInterpolatorTimeFuntion;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;
import com.iver.utiles.IPersistence;
import com.iver.utiles.XMLEntity;

/**
 * @author Ángel Fraile Griñán  e-mail: angel.fraile@iver.es
 * @since 1.1
 * 
 */
public class AnimationLayer3DFlat implements IAnimationTypeKeyFrame {

	private String className = this.getClass().getName();
	private String description = "Animacion basada en encuadres para vista 3D plana";
	private String name = "Animacion vista 3D plana";
	private int typeTrack = IAnimationType.TIME_TYPE_TRACK;
	private IInterpolator interpolator;
	
	private IAnimatedObject animationObject3DFlat = new AnimationObject3DFlat() ;
	private String titleWindow;
	
	private IViewerContainer m_canvas3d;

	public AnimationLayer3DFlat() {
		this.interpolator = new Interpolator3DFlat();
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
	 * Repainting the canvas
	 */
	public void AppliesToObject(Object animated) {

		KeyFrame3DFlat keyf = (KeyFrame3DFlat) animated;
		ProjectCamera projectCamera = (ProjectCamera) keyf.getAnimatedObject();
		
		m_canvas3d = (IViewerContainer) ((AnimationObject3DFlat)animationObject3DFlat).getAnimatedCanvas3D();
		m_canvas3d.getOSGViewer().setCamera(projectCamera.getCamera());
	
		m_canvas3d.repaint();
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
		// if animated object is null.
		if(this.animationObject3DFlat != null){
			// getting all objects.
			try{
				IWindow[] viewArray = PluginServices.getMDIManager().getAllWindows();
				for (int i = 0; i < viewArray.length; i++) {
					IWindow window = viewArray[i];
						if(window.getWindowInfo().getTitle().equals(this.titleWindow)){
							BaseView newView = (BaseView)window;// Working with base view
							((AnimationObject3DFlat) this.animationObject3DFlat).setAnimatedView(newView);
							this.setAnimatedObject(this.animationObject3DFlat);
							IInterpolatorTimeFuntion function = FuntionFactory.createObject("com.iver.cit.gvsig.animation.keyframe.interpolator.LinearFuntion");
							this.interpolator.setFuntion(function);
							
						}
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return this.animationObject3DFlat;
	}
	

	public void setAnimatedObject(Object object) {
		if (object instanceof AnimationObject3DFlat)
			this.animationObject3DFlat = (IAnimatedObject) object;
	}

	public XMLEntity getXMLEntity() {
		
		XMLEntity xml = new XMLEntity();
		xml.putProperty("className", this.getClassName());
		xml.putProperty("description", description);
		xml.putProperty("animationTrackTipe", typeTrack);
		
		BaseView myActualView = (BaseView) animationObject3DFlat.getAnimatedObject("view");
		
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
