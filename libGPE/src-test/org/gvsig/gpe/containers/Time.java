/* gvSIG. Geographic Information System of the Valencian Government
*
* Copyright (C) 2007-2008 Infrastructures and Transports Department
* of the Valencian Government (CIT)
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
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
* MA  02110-1301, USA.
* 
*/

/*
* AUTHORS (In addition to CIT):
* 2008 PRODEVELOP S.L.
*/
 
package org.gvsig.gpe.containers;

import java.util.Date;

/**
 * @author Carlos Sánchez Periñán
 * 		   PRODEVELOP S.L.
 * 08/01/2009
 */
public class Time {
	Date date=null;
	Time hour=null;
	String UTCoffset=null;
	Double duration=null;
	String unit=null;
	Time next=null, previous=null;
	String type=null,description=null,name=null,value=null;
	
	public Time getNext() {
		return next;
	}
	public void setNext(Time next) {
		this.next = next;
	}
	public Time getPrevious() {
		return previous;
	}
	public void setPrevious(Time previous) {
		this.previous = previous;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Time getHour() {
		return hour;
	}
	public void setHour(Time hour) {
		this.hour = hour;
	}
	public String getUTCoffset() {
		return UTCoffset;
	}
	public void setUTCoffset(String coffset) {
		UTCoffset = coffset;
	}
	public Double getDuration() {
		return duration;
	}
	public void setDuration(Double duration) {
		this.duration = duration;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
