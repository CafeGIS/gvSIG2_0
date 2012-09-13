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
* 2008 Geographic Information research group: http://www.geoinfo.uji.es
* Departamento de Lenguajes y Sistemas Informáticos (LSI)
* Universitat Jaume I   
* {{Task}}
*/



package org.gvsig.metadata.extended.manager;

import java.util.Locale;

import org.gvsig.metadata.extended.ExtendedMetadata;
import org.gvsig.metadata.extended.MDElement;
import org.gvsig.metadata.extended.registry.objects.MDElementDefinition;


public class MDElementImpl implements MDElement{
	
	private String id;
	private String name = "";
	private Object value;
	private MDElementDefinition type = null;
	private Locale locale;
	
	private Object father;


	public MDElementImpl() {}
	
	public MDElementImpl(Object value, MDElementDefinition type, Locale locale) {
		//this.id = id; Que poner como ID???
		this.value = value;
		this.type = type;
		this.locale = locale;
	}
	
	public MDElementImpl(Object value, String name, Locale locale) {
		//this.id = id; Que poner como ID???
		this.value = value;
		this.name = name;
		this.locale = locale;
	}
	
	public String getId() {
		return this.id;
	}
	
	public Object getValue() {
		return this.value;
	}
	
	public MDElementDefinition getType() {
		return this.type;
	}
	
	public Locale getLocale() {
		return this.locale;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setValue(Object value) {
		this.value = value;
	}
	
	public void setType(MDElementDefinition type) {
		this.type = type;
	}
	
	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	
	public String getName() {
		if(this.type != null)
			return this.type.getName();
		return this.name;
	}
	
	public void setName(String name) {
		this.type.setName(name);
	}
	
	/**
	 * @return the father
	 */
	public Object getFather() {
		return father;
	}

	/**
	 * @param father the father to set
	 */
	public void setFather(Object father) {
		this.father = father;
	}
	
	public boolean hasFather() {
		return (father != null);
	}
	
	public int getLevel() {
		if(!this.hasFather())
			return 0;
		else {
			if(getFather() instanceof MDElement)
				return 1 + ((MDElement)this.getFather()).getLevel();
			else if(getFather() instanceof ExtendedMetadata)
				return 1 + ((ExtendedMetadata)this.getFather()).getLevel();
			else
				return 0;
		}
	}
	
	public String toString() {
		String aux = "<" + getName() + ">";
		aux += value.toString();
		aux += "</" + getName() + ">";
		return aux;
	}
	
}