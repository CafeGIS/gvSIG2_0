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

package com.iver.cit.gvsig.animation.interpolator;

import java.util.List;

import com.iver.cit.gvsig.animation.animatedObject.AnimationObjectTransparency;
import com.iver.cit.gvsig.animation.keyFrame.KeyFrameTransparency;
import com.iver.cit.gvsig.animation.keyframe.IKeyFrame;
import com.iver.cit.gvsig.animation.keyframe.interpolator.IInterpolator;
import com.iver.cit.gvsig.animation.keyframe.interpolator.IInterpolatorTimeFuntion;
import com.iver.cit.gvsig.fmap.layers.FLyrDefault;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;
import com.iver.utiles.XMLEntity;


/**
 * @author Ángel Fraile Griñán  e-mail: angel.fraile@iver.es
 * @since 1.1
 * 
 */
public class InterpolatorTransparency implements IInterpolator {

	public BaseView view;
	private String description = "Interpolación basada en Transparencias";
	private String name = "InterpolatorTransparency";
	
	private AnimationObjectTransparency animationObjectTransparency = new AnimationObjectTransparency() ;
	
	/**
	 *  Class to interpolate the transparency of a activated layer.
	 */
	public IKeyFrame interpolate(List<IKeyFrame> kfList, int index, double time) {
		KeyFrameTransparency KF = new KeyFrameTransparency();

		// if the list of keyframes is empty.
		if (kfList == null)
			return null;
		
 
		switch (kfList.size()) {
		// this case when there are only has 2 keyframes
		case 2:
		
			KeyFrameTransparency kf1 = (KeyFrameTransparency) kfList.get(0);
			KeyFrameTransparency kf2 = (KeyFrameTransparency) kfList.get(1);
			
			if (index == 1) {
				KeyFrameTransparency kaux = kf1;
				kf1 = kf2;
				kf2 = kaux;
			}

			if ((kf1 == null) ||(kf2 == null))
				return null;
			//initial transparency level.
			double transparencia1 = kf1.getLevelTransparency();
			//final transparency level.
			double transparencia2 = kf2.getLevelTransparency();
			
			// Object to animate getting the keyframe.
			FLyrDefault fd1 = (FLyrDefault) kf1.getAnimatedObject();
			//FLyrDefault fd2 = (FLyrDefault) kf2.getAnimatedObject();
			
			double time1 = kf1.getTime();//initial time.
			double time2 = kf2.getTime();//final time.
			double valorTrans = linearInterpolate(transparencia1, transparencia2, time1, time2, time);
			
			//Creating the keyframe KF to return.
				try {
					KF.setLevelTransparency((int)valorTrans);
					KF.setAnimatedObject(fd1);// keyframe with the layer and the new transparecy to apply.
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return KF;
	}

	/**
	 * Return a value interpolate 
	 * 
	 * @param minX: initial x value in the 2D extent
	 * @param minX2: final x value in the 2D extent
	 * @param timePos: initial time value
	 * @param timePos2: final time value
	 * @param time: total time
	 * @return
	 */
	private double linearInterpolate(double minX, double minX2, double timePos,
			double timePos2, double time) {
		// P1 + (P2-P1)*((t-t1)/(t2-t1))
		return (minX + (minX2 - minX)
				* ((time - timePos) / (timePos2 - timePos)));
	}

	
	public Object getAnimatedObject() {
		return this.animationObjectTransparency;
	}


	public void setAnimatedObject(Object object) {
		this.animationObjectTransparency = (AnimationObjectTransparency) object;
		this.view = (BaseView) animationObjectTransparency.getAnimatedView();
	}

	public IInterpolatorTimeFuntion getFuntion() {
		return null;
	}


	public void setFuntion(IInterpolatorTimeFuntion arg0) {
		// TODO Auto-generated method stub
	}
	
	public String getClassName() {
		return this.getClass().getName();
	}

	public String getDescription() {
		return this.description;
	}

	public String getName() {
		return this.name;
	}

	/*
	 * IPersistence methods.
	 */
	
	public XMLEntity getXMLEntity() {
		XMLEntity xml = new XMLEntity();	
		xml.putProperty("className", this.getClassName());
		xml.putProperty("description", this.description);
		return xml;
	}
	
	public void setXMLEntity(XMLEntity xml) {
		if (xml.contains("description"))
			this.description = xml.getStringProperty("description");
	}

}
