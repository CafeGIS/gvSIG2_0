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

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.gvsig.metadata.exceptions.NamedMetadataNotFoundException;
import org.gvsig.metadata.extended.ExtendedMetadata;
import org.gvsig.metadata.extended.MDElement;
import org.gvsig.metadata.extended.registry.objects.MDDefinition;


public class ExtendedMetadataImpl implements ExtendedMetadata {
	
	private String id;
	private Date createDate;
	private Date changeDate;
	private MDDefinition type;
	private String author;
	private Map<String, MDElement> elements = new LinkedHashMap<String, MDElement>();	
	private ExtendedMetadata father;
	
	
	
	
	public ExtendedMetadataImpl(String name) {
		this.type = new MDDefinition();
		this.type.setName(name);
		this.createDate = new Date();
		this.changeDate = null;
		this.father = null;
	}
	
	public ExtendedMetadataImpl(MDDefinition type, String author) {
		this.createDate = new Date();
		this.changeDate = null;
		this.type = type;
		this.author = author;
		this.father = null;
	}
	
	public Object get(String name) throws NamedMetadataNotFoundException {
		if(!hasValue(name))
			throw new NamedMetadataNotFoundException(this.getClass());
		return ((MDElement) elements.get(name)).getValue();
	}

	public boolean getBoolean(String name) throws ClassCastException, NamedMetadataNotFoundException {
		if(!hasValue(name))
			throw new NamedMetadataNotFoundException(this.getClass());
		return ((Boolean) ((MDElement) elements.get(name)).getValue()).booleanValue();
	}

	public double getDouble(String name) throws ClassCastException, NamedMetadataNotFoundException {
		if(!hasValue(name))
			throw new NamedMetadataNotFoundException(this.getClass());
		return ((Double)((MDElement) elements.get(name)).getValue()).doubleValue();
	}

	public int getInt(String name) throws ClassCastException, NamedMetadataNotFoundException {
		if(!hasValue(name))
			throw new NamedMetadataNotFoundException(this.getClass());
		return ((Integer)((MDElement) elements.get(name)).getValue()).intValue();
	}
	
	public String getString(String name) throws ClassCastException, NamedMetadataNotFoundException {
		if(!hasValue(name))
			throw new NamedMetadataNotFoundException(this.getClass());
		return (String)((MDElement) elements.get(name)).getValue().toString();
	}

	public Date getDate(String name) throws ClassCastException, NamedMetadataNotFoundException {
		if(!hasValue(name))
			throw new NamedMetadataNotFoundException(this.getClass());
		return (Date)((MDElement) elements.get(name)).getValue();
	}
	
	public MDElement getElement(String idElem) throws ClassCastException, NamedMetadataNotFoundException {
		if(!hasValue(idElem))
			throw new NamedMetadataNotFoundException(this.getClass());
		return (MDElement) elements.get(idElem);
	}
	
	public String getId() {
		return this.id;
	}
	
	public String getName() {
		return this.type.getName();
	}
	
	public String getDescription() {
		return this.type.getDescription();
	}
	
	public Date getCreateDate() {
		return this.createDate;
	}
	
	public Date getChangeDate() {
		return this.changeDate;
	}
	
	public MDDefinition getType() {
		return this.type;
	}
	
	public String getAuthor() {
		return this.author;
	}
	
	public void set(String name, Object value) {
		if(value instanceof Boolean)
			setBoolean(name, (Boolean)value);
		else if(value instanceof Date)
			setDate(name, (Date)value);
		else if(value instanceof Double)
			setDouble(name, (Double)value);
		else if(value instanceof Integer)
			setInt(name, (Integer)value);
		else if(value instanceof String)
			setString(name, (String)value);
		else {
			MDElement mde = new MDElementImpl(value, name, null);
			elements.put(name, mde);
		}
	}
	
	public void setBoolean(String name, boolean value) {
		MDElement mde = new MDElementImpl(Boolean.valueOf(value), name, null);
		elements.put(name, mde);
		mde.setFather(this);
	}

	public void setDate(String name, Date value) {
		MDElement mde = new MDElementImpl(value, name, null);
		elements.put(name, mde);
		mde.setFather(this);
	}

	public void setDouble(String name, double value) {
		MDElement mde = new MDElementImpl(Double.valueOf(value), name, null);
		elements.put(name, mde);
		mde.setFather(this);
	}

	public void setInt(String name, int value) {
		MDElement mde = new MDElementImpl(Integer.valueOf(value), name, null);
		elements.put(name, mde);
		mde.setFather(this);
	}

	public void setString(String name, String value) {
		MDElement mde = new MDElementImpl(value, name, null);
		elements.put(name, mde);
		mde.setFather(this);
	}
	
	public void setElement(MDElement elem) {
		String idElem = elem.getName();
		elements.put(idElem, elem);
		elem.setFather(this);
	}
	
	public void setId(String id) {
		this.id = id;
		setChangeDate();
	}
	
	public void setName(String name) {
		this.type.setName(name);
	}
	
	public void setDescription(String desc) {
		this.type.setDescription(desc);
	}
	
	public void setChangeDate() {
		this.changeDate = new Date();
	}
	
	public void setType(MDDefinition type) {
		this.type = type;
		setChangeDate();
	}

	public boolean hasValue(String id) {
		return elements.containsKey(id);
	}
	
	public boolean isEmpty() {
		return elements.isEmpty();
	}

	/**
	 * @return the father
	 */
	public ExtendedMetadata getFather() {
		return father;
	}

	/**
	 * @param father the father to set
	 */
	public void setFather(ExtendedMetadata father) {
		this.father = father;
	}
	
	public boolean hasFather() {
		return (father != null);
	}
	
	public int getLevel() {
		if(!this.hasFather())
			return 0;
		else
			return 1 + this.getFather().getLevel();
	}
	
	public Iterator iterator() {
		return elements.entrySet().iterator();
	}
	
	public String toString() {
		String aux = "<MD_Metadata>";
		Iterator it = iterator();
		while (it.hasNext()) {
			Map.Entry e = (Map.Entry)it.next();
			aux += e.getValue().toString();
		}
		aux += "</MD_Metadata>";
		return aux;
	}
	
}