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



public class MDElementDefinition {
	
	private int id;
	private String name;
	private String type;
	private Boolean required;
	private Object defaultValue;
	private String description;
	private MDDefinition father;
	private boolean changed, persisted;

	//Eliminar este constructor
	public MDElementDefinition(String name, String type, Boolean required, Object defaultValue, String description) {
		this.id = 0;
		this.name = name;
		this.type = type;
		this.required = required;
		this.defaultValue = defaultValue;
		this.description = description;
	}
	
	public MDElementDefinition(String name, String description, Boolean required, MDDefinition father, String type, Object defaultValue) {
		this.id = 0;
		this.name = name;
		this.description = description;
		this.required = required;
		this.father = father;
		this.type = type;
		this.defaultValue = defaultValue;
		this.changed = true;
		this.persisted = false;
	}
	
	public MDElementDefinition(int id, String name, String description, Boolean required, MDDefinition father, String type, Object defaultValue) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.required = required;
		this.father = father;
		this.type = type;
		this.defaultValue = defaultValue;
		this.changed = false;
		this.persisted = true;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getType() {
		return this.type;
	}
	
	public Boolean isRequired() {
		return this.required;
	}
	
	public Object getDefaultValue() {
		return this.defaultValue;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public void setName(String name) {
		this.name = name;
		this.changed = true;
	}
	
	public void setType(String type) {
		this.type = type;
		this.changed = true;
	}
	
	public void setRequired(Boolean required) {
		this.required = required;
		this.changed = true;
	}
	
	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
		this.changed = true;
	}
	
	public void setDescription(String description) {
		this.description = description;
		this.changed = true;
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

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the required
	 */
	public Boolean getRequired() {
		return required;
	}

	/**
	 * @return the father
	 */
	public MDDefinition getFather() {
		return father;
	}

	/**
	 * @param father the father to set
	 */
	public void setFather(MDDefinition father) {
		this.father = father;
		this.changed = true;
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
	
}