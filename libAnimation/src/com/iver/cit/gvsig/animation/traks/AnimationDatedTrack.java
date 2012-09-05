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

package com.iver.cit.gvsig.animation.traks;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.iver.cit.gvsig.animation.IAnimationType;
import com.iver.cit.gvsig.animation.interval.AnimationDatedInterval;
import com.iver.cit.gvsig.animation.interval.IAnimationInterval;
import com.iver.utiles.DateTime;
import com.iver.utiles.XMLEntity;

public class AnimationDatedTrack implements IAnimationTrack {

	private String Name;

	private String Description;

	// List of AnimationDateInterval
	private List animationDateIntervalList;

	private IAnimationType animationType;

	private Object animatedObject;

	private boolean enable;

	private String beginDateString;

	private String endDateString;

	private Date beginDate;

	private Date endDate;
	
	private String sFormat = new String( "Y-m-d" );

	//private AnimationDatedInterval datedInterval;

	public AnimationDatedTrack(IAnimationType animationType) {
		this.animationDateIntervalList = new ArrayList<AnimationDatedInterval>();
		this.animationType = animationType;
	}

	public Date getAnimationIntervalDate() {
		return null;
	}

	public Object getAnimatedObject() {
		return this.animatedObject;
	}

	public String getDescription() {
		return this.Description;
	}

	public List getIntervalList() {
		return animationDateIntervalList;
	}

	public String getName() {
		return this.Name;
	}

	public void setIntervalList(List intervalList) {
		this.animationDateIntervalList = intervalList;

	}

	public IAnimationType getAnimationType() {
		return this.animationType;
	}

	public boolean isEnable() {
		return this.enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public IAnimationInterval createInterval() {

		List<AnimationDatedInterval> ADIList = this.getIntervalList();
		if (ADIList == null) {
			ADIList = new ArrayList<AnimationDatedInterval>();
		}
		AnimationDatedInterval ADInterval = new AnimationDatedInterval();
		ADIList.add(ADInterval);
		return ADInterval;
	}

	public String toString() {

		String result;
		List ADIL = this.animationDateIntervalList;
		result = "Mostrando lista de Intervalos de la track " + this.getName()
				+ ":" + this.getDescription();
		result += "\n**********************************************";
		result += "\nTipo de objetos que contiene "
				+ this.getAnimationType().getClassName();
		result += "\nNombre: " + this.getAnimationType().getName();
		result += "\nDescripcion: " + this.getAnimationType().getName();
		result += "\n**********************************************";
		for (Iterator iter = ADIL.iterator(); iter.hasNext();) {
			Object element = (Object) iter.next();
			result += "\n" + element;
		}
		return result;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public void setName(String name) {
		Name = name;
	}

	public void removeAllIntervals() {
		this.animationDateIntervalList.clear();
	}

	public void removeInterval(IAnimationInterval animationInterval) {
		this.animationDateIntervalList.remove(animationInterval);
	}

	public void setAnimatedObject(Object animatedObject) {
		this.animatedObject = animatedObject;
	}
	
	public void apply(double Tini, double Tend) {
		List ADTL = this.animationDateIntervalList;
		if ((ADTL != null) && (!ADTL.isEmpty())) {
			for (int i = 0; i < ADTL.size(); i++) {
				IAnimationInterval element = (IAnimationInterval) ADTL.get(i);
				element.apply(Tini, Tend, this.getAnimationType(), this
						.getAnimatedObject());
			}
		}

	}
	
	public String getClassName() {
		// TODO Auto-generated method stub
		return this.getClass().getName();
	}

	/*
	 * IPersistance methods 
	 * 
	 */
	
	public void setXMLEntity(XMLEntity xml) {
		if (xml.contains("name"))
			Name =	xml.getStringProperty("name");
		if (xml.contains("descripcion"))
			Description = xml.getStringProperty("description");
		if (xml.contains("begin_date"))
			beginDateString = xml.getStringProperty("begin_date");
		if (xml.contains("end_date"))
			endDateString = xml.getStringProperty("end_date");
		
		
//		for (int i = 0; i < this.animationDateIntervalList.size(); i++) {
//			AnimationDatedInterval datedInterval = (AnimationDatedInterval) this.animationDateIntervalList.get(i);			
//			datedInterval.setXMLEntity(xml);			
//		}					

	}

	public XMLEntity getXMLEntity() {
		// TODO method to get persisence to this class
		
		XMLEntity xml = new XMLEntity();
		xml.putProperty("className", this.getClassName());
		xml.putProperty("name", Name);
		xml.putProperty("description",Description );
		
		beginDateString = DateTime.dateToString(beginDate, sFormat);
		endDateString = DateTime.dateToString(endDate, sFormat);
		
		xml.putProperty("begin_date", beginDateString);
		xml.putProperty("end_date", endDateString);
		
//		for (int i = 0; i < this.animationDateIntervalList.size(); i++) {			
//		AnimationDatedInterval datedInterval = (AnimationDatedInterval) this.animationDateIntervalList.get(i);			
////		datedInterval.getXMLEntity();
//		xml.addChild(((IPersistence)datedInterval).getXMLEntity());
//	}			
		return xml;
		
	}
	
}