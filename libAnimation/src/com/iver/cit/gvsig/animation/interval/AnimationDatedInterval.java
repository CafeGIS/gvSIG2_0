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

package com.iver.cit.gvsig.animation.interval;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import com.iver.cit.gvsig.animation.IAnimationType;
import com.iver.utiles.DateTime;
import com.iver.utiles.IPersistence;
import com.iver.utiles.XMLEntity;

public class AnimationDatedInterval implements IAnimationInterval, IPersistence {

	private Date beginDateInterval;

	private Date endDateInterval;

	private String beginDateString = new String();

	private String endDateString = new String();

	private String sFormat = new String("Y-m-d");

	static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	public Date getBeginDateInterval() {
		return beginDateInterval;
	}

	public void setBeginDateInterval(Date beginDateInterval) {
		this.beginDateInterval = beginDateInterval;
	}

	public Date getEndDateInterval() {
		return endDateInterval;
	}

	public void setEndDateInterval(Date endDateInterval) {
		this.endDateInterval = endDateInterval;
	}

	public String toString() {
		String result = "Intervalo de " + this.beginDateInterval + " a "
				+ this.endDateInterval;
		return result;
	}

	public double getEndTime() {
		return this.endDateInterval.getTime();
	}

	public double getInitialTime() {
		return this.beginDateInterval.getTime();
	}

	public double getIntervalTime() {
		return (this.endDateInterval.getTime() - this.beginDateInterval
				.getTime());
	}

	public void setEndTime(double time) {
		this.endDateInterval.setTime((long) time);

	}

	public void setInitialTime(double time) {
		this.beginDateInterval.setTime((long) time);

	}

	public void setIntervalTime(double time) {
		// TODO generate method to calculate interval time
	}

	public void apply(double tini, double tend, IAnimationType animationType,
			Object animated) {
		// falta completar la funcion
		// System.out.println("PINTANDO OBJETO");
		// System.out.println("milisegundos del inicio del intervalo "
		// + this.dateToSeconds(this.getBeginDateInterval()));

//		int day1 = this.getBeginDateInterval().getDate();
//		int day2 = this.getEndDateInterval().getDate();
//		int result = linearInterpolate(day1, day2, 0.0, 1.0, tini);
//		// System.out.println("fecha resultante " + result);
//		int min = (int) (result - 1);
//		// System.out.println("fehca minima " + "2000-01-" + min );
//		int max = (int) (result + 1);
//		// System.out.println("fehca minima " + "2000-01-" + max );
//
//		DateFilter filter = new DateFilter();
//		filter.setFieldIndex(0); // Date field in com_aut data sample
//		filter.setFieldType(DateFilter.FIELDTYPE_DATESTRING);
////		filter.setMinDate(Date.valueOf("2000-01-1"));
//		filter.setMinDate(Date.valueOf("2000-01-" + min));
//		filter.setMaxDate(Date.valueOf("2000-01-" + max));

		animationType.AppliesToObject(animated);

	}

	private int linearInterpolate(int minX, int minX2, double timePos,
			double timePos2, double time) {
		// P1 + (P2-P1)*((t-t1)/(t2-t1))

		double result = ((minX + (minX2 - minX)) * time);

		return (int) Math.ceil(result);
	}

	private double dateToSeconds(Date date) {
		Calendar ca = Calendar.getInstance(Locale.US);
		ca.setTime(date);
		return ca.getTimeInMillis();
	}

	/*
	 * IPersistance methods.
	 */

	public String getClassName() {
		// TODO Auto-generated method stub
		return null;
	}

	public XMLEntity getXMLEntity() {

		XMLEntity xml = new XMLEntity();
		xml.putProperty("className", this.getClassName());
		// xml.putProperty("beginDateInterval", beginDateString);
		// xml.putProperty("endDateInterval", endDateString);

		beginDateString = DateTime.dateToString(beginDateInterval, sFormat);
		endDateString = DateTime.dateToString(endDateInterval, sFormat);

		xml.putProperty("begin_date", beginDateString);
		xml.putProperty("end_date", endDateString);

		return xml;
	}

	public void setXMLEntity(XMLEntity xml) {
		if (xml.contains("begin_date"))
			beginDateString = xml.getStringProperty("begin_date");
		if (xml.contains("end_date"))
			endDateString = xml.getStringProperty("end_date");

	}
}
