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

import java.awt.geom.Rectangle2D;
import java.util.List;

import com.iver.cit.gvsig.animation.keyFrame.KeyFrame2D;
import com.iver.cit.gvsig.animation.keyframe.IKeyFrame;
import com.iver.cit.gvsig.animation.keyframe.interpolator.IInterpolator;
import com.iver.cit.gvsig.animation.keyframe.interpolator.IInterpolatorTimeFuntion;
import com.iver.cit.gvsig.project.ProjectExtent;
import com.iver.utiles.XMLEntity;

/**
 * @author Ángel Fraile Griñán  e-mail: angel.fraile@iver.es
 * @since 1.1
 * 
 */

public class Interpolator2D implements IInterpolator {

	private String description = "Interpolación basada en encuadres";
	private String name = "Interpolator2D";
	
	private IInterpolatorTimeFuntion funtion; 

	/**
	 * 	Calculating the new positions in the movement animation
	 */
	public IKeyFrame interpolate(List<IKeyFrame> kfList, int index, double time) {
		KeyFrame2D KF = new KeyFrame2D();

		if (kfList == null)
			return null;


		switch (kfList.size()) {
		// this case when there are only has 2 keyframes
		case 2:
			// getting the keyframes
			KeyFrame2D kf1 = (KeyFrame2D) kfList.get(0);
			KeyFrame2D kf2 = (KeyFrame2D) kfList.get(1);

			if (index == 1) {
				KeyFrame2D kaux = kf1;
				kf1 = kf2;
				kf2 = kaux;
			}

			ProjectExtent vp1 = (ProjectExtent) kf1.getAnimatedObject();
			ProjectExtent vp2 = (ProjectExtent) kf2.getAnimatedObject();


			double min1 = vp1.getExtent().getMinX();
			double time1 = kf1.getTime();
			double min2 = vp2.getExtent().getMinX();
			double time2 = kf2.getTime();
			double left = linearInterpolate(min1, min2, time1, time2, time);
			min1 = vp1.getExtent().getMinY();
			min2 = vp2.getExtent().getMinY();
			double top = linearInterpolate(min1, min2, time1, time2, time);
			min1 = vp1.getExtent().getWidth();
			min2 = vp2.getExtent().getWidth();
			double right = linearInterpolate(min1, min2, time1, time2, time);
			min1 = vp1.getExtent().getHeight();
			min2 = vp2.getExtent().getHeight();
			double bottom = linearInterpolate(min1, min2, time1, time2, time);

			Rectangle2D newExtent = new Rectangle2D.Double(left, top, right,
					bottom);
			ProjectExtent pe = new ProjectExtent();
			pe.setExtent(newExtent);
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
