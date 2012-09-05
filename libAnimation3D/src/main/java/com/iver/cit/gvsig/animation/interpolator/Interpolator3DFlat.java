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

import org.gvsig.osgvp.core.osg.Vec3;
import org.gvsig.osgvp.viewer.Camera;

import com.iver.ai2.gvsig3d.camera.ProjectCamera;
import com.iver.cit.gvsig.animation.keyFrame.KeyFrame3DFlat;
import com.iver.cit.gvsig.animation.keyframe.IKeyFrame;
import com.iver.cit.gvsig.animation.keyframe.interpolator.IInterpolator;
import com.iver.cit.gvsig.animation.keyframe.interpolator.IInterpolatorTimeFuntion;
import com.iver.utiles.XMLEntity;

/**
 * @author Ángel Fraile Griñán  e-mail: angel.fraile@iver.es
 * @since 1.1
 * 
 */
public class Interpolator3DFlat implements IInterpolator {

	private IInterpolatorTimeFuntion funtion;
	private String description = "Interpolación basada en encuadres";
	private String name = "Interpolator3DFlat";
	
	
	/**
	 * 	Calculating the new positions in the movement animation
	 */
	public IKeyFrame interpolate(List<IKeyFrame> kfList, int index, double time) {
		KeyFrame3DFlat KF = new KeyFrame3DFlat();

		if (kfList == null)
			return null;

		// convert the time in the new time using funtion
		double newTime = this.getFuntion().interpolate(time);
		
		switch (kfList.size()) {
		// this case when there are only has 2 keyframes
		case 2:
			// getting the keyframes
			KeyFrame3DFlat kf1 = (KeyFrame3DFlat) kfList.get(0);
			KeyFrame3DFlat kf2 = (KeyFrame3DFlat) kfList.get(1);

			if (index == 1) {
				KeyFrame3DFlat kaux = kf1;
				kf1 = kf2;
				kf2 = kaux;
			}

			if ((kf1 == null) ||(kf2 == null))
				return null;
			ProjectCamera vp1 = (ProjectCamera) kf1.getAnimatedObject();
			ProjectCamera vp2 = (ProjectCamera) kf2.getAnimatedObject();

			Camera cam1 = vp1.getCamera();
			Camera cam2 = vp2.getCamera();
			double time1 = kf1.getTime();
			double time2 = kf2.getTime();
			
			Vec3 eye = linearInterpolate(cam1.getEye(), cam2.getEye(), time1, time2, newTime);
			Vec3 center = linearInterpolate(cam1.getCenter(), cam2.getCenter(), time1, time2, newTime);
			Vec3 up = linearInterpolate(cam1.getUp(), cam2.getUp(), time1, time2, newTime);
		
			Camera newCamera = new Camera();
//			newCamera.setViewByLookAt(eye, center, cam1.getUp());
			newCamera.setViewByLookAt(eye, center, up);

			ProjectCamera pe = new ProjectCamera();
			pe.setCamera(newCamera);
			KF.setName("temporal_keyframe");
			KF.setAnimatedObject(pe);
			break;
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

	private Vec3 linearInterpolate(Vec3 minX, Vec3 minX2, double timePos,
			double timePos2, double time) {

		Vec3 result = new Vec3();

		double ele1 = linearInterpolate(minX.x(), minX2.x(), timePos, timePos2,
				time);
		double ele2 = linearInterpolate(minX.y(), minX2.y(), timePos, timePos2,
				time);
		double ele3 = linearInterpolate(minX.z(), minX2.z(), timePos, timePos2,
				time);

		result.setX(ele1);
		result.setY(ele2);
		result.setZ(ele3);
		return result;
	}
	
	public IInterpolatorTimeFuntion getFuntion() {
		return this.funtion;
	}

	public void setFuntion(IInterpolatorTimeFuntion funtion) {
		this.funtion = funtion;
		
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
