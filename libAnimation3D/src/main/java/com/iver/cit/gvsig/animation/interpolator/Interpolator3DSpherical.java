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

import org.gvsig.osgvp.core.osg.Matrix;
import org.gvsig.osgvp.core.osg.Quat;
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
public class Interpolator3DSpherical implements IInterpolator {

	private IInterpolatorTimeFuntion function;
	private String description = "Interpolación basada en encuadres";
	private String name = "Interpolator3DSpherical";
	
	
	/**
	 * 	Calculating the new positions in the movement animation
	 */
	
	public IKeyFrame interpolate(List<IKeyFrame> kfList, int index, double time) {
		KeyFrame3DFlat KF = new KeyFrame3DFlat();

		if (kfList == null)
			return null;

				
		/*Interpolación por quaterniones.*/
		
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
			
			newTime = (newTime-time1)/(time2-time1);//normalize time 0..1
			
			Quat from = cam1.getViewMatrix().getRotate();
			Quat to = cam2.getViewMatrix().getRotate();

			Vec3 trans1 = cam1.getViewMatrix().getTrans();
			Vec3 trans2 = cam2.getViewMatrix().getTrans();

			Vec3 new_trans = vec3LinearInterpolate(trans1, trans2, newTime);
			Matrix m1 = Matrix.translate(new_trans);
			Quat q = sphericalInterpolate(from, to, newTime);
			Matrix m = Matrix.rotate(q);
			Matrix m2 = m.prod(m1);
			
			Camera c = new Camera();
			c.setViewMatrix(m2);
		
			ProjectCamera pe = new ProjectCamera();
			pe.setCamera(c);
			KF.setName("temporal_keyframe");
			KF.setAnimatedObject(pe);
			break;

		}
		
		
		return KF;
	}
	
	/**
	 * Spherical interpolation based in quaternions
	 * @param from: initial position 
	 * @param to: final position
	 * @param time: total time
	 * @return the actual position of the earth movement 
	 */
	private Quat sphericalInterpolate(Quat from, Quat to, double time) {

		Quat fromQuat = new Quat();

		return fromQuat.slerp(time, from, to);

	}

//	private double doubleInterpolate(double from, double to, double time) {
//		if (from == to)
//			return from;
//		return from + (to - from) * time;
//	}

	private Vec3 vec3LinearInterpolate(Vec3 from, Vec3 to, double time) {
		if (from.equals(to))
			return new Vec3(from);
		return from.sum(to.sub(from).escalarProduct(time));
	}
	
	public IInterpolatorTimeFuntion getFuntion() {
		return this.function;
	}

	public void setFuntion(IInterpolatorTimeFuntion function) {
		this.function = function;
		
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

	/**
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
