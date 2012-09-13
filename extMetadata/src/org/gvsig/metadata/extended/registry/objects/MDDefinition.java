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


package org.gvsig.metadata.extended.registry.objects;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;



public class MDDefinition {
	private int id;
	private String name;
	private String description;
	private MDDefinition father;
	private Map<String, MDElementDefinition> elements;
	private boolean changed, persisted;
	
	//eliminar este construtor
	public MDDefinition() {
		this.id = 0;
		this.name = "";
		this.description = "";
		this.father = null;
		this.elements = new LinkedHashMap<String, MDElementDefinition>();
	}
	
	public MDDefinition(String name, String description) {
		this.id = 0;
		this.name = name;
		this.description = description;
		this.father = null;
		this.elements = new LinkedHashMap<String, MDElementDefinition>();
		this.changed = true;
		this.persisted = false;
	}
	
	public MDDefinition(int id, String name, String description) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.father = null;
		this.elements = new LinkedHashMap<String, MDElementDefinition>();
		this.changed = false;
		this.persisted = true;
	}

	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	
	public String getName() {
		return this.name;
	}

	public String getDescription() {
		return this.description;
	}
	
	/**
	 * @return the father
	 */
	public MDDefinition getFather() {
		return father;
	}
	
	public void setName(String name) {
		this.name = name;
		this.changed = true;
	}
	
	public void setDescription(String description) {
		this.description = description;
		this.changed = true;
	}

	/**
	 * @param father the father to set
	 */
	public void setFather(MDDefinition father) {
		this.father = father;
		this.changed = true;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the changed
	 */
	public boolean isChanged() {
		return changed;
	}

	/**
	 * @param changed the changed to set
	 */
	public void setChanged(boolean changed) {
		this.changed = changed;
	}
	
	public Iterator iterator() {
		return elements.entrySet().iterator();
	}

	/**
	 * @return the persisted
	 */
	public boolean isPersisted() {
		return persisted;
	}

	/**
	 * @param persisted the persisted to set
	 */
	public void setPersisted(boolean persisted) {
		this.persisted = persisted;
	}
	
	public void addMDElementDefinition(MDElementDefinition mded) {
		elements.put(mded.getName(), mded);
		this.changed = true;
	}
	
	public MDElementDefinition getMDElementDefinition(String name) {
		return elements.get(name);
	}

}