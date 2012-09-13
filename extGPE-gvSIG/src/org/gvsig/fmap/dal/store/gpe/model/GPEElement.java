package org.gvsig.fmap.dal.store.gpe.model;

import java.util.HashMap;

/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
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
 *
 * For more information, contact:
 *
 *  Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ibáñez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   IVER T.I. S.A
 *   Salamanca 50
 *   46005 Valencia
 *   Spain
 *
 *   +34 963163400
 *   dac@iver.es
 */
/* CVS MESSAGES:
 *
 * $Id$
 * $Log$
 *
 */
/**
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class GPEElement {
	private String name = null;
	private String value = null;
	private HashMap subElements = null;
	
	public GPEElement(String name, String value) {
		super();
		this.name = name;
		this.value = value;
		subElements = new HashMap();
	}
	
	public GPEElement(String name, String value, GPEElement parentElement) {
		this(name,value);
		//If it is a complex value
		if (parentElement != null){
			parentElement.addChildElement(this);			
		}
	}	
	
	/**
	 * It adds a new child element
	 * @param children
	 * The element to add
	 */
	public void addChildElement(GPEElement children){
		subElements.put(children.getName(), children);
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return the value
	 */
	public String getValue() {
		if (subElements.size() > 0){
//			ComplexValue cValue = ValueFactory.createComplexValue(name);
//			Iterator it = subElements.keySet().iterator();
//			while(it.hasNext()){
//				GPEElement element = (GPEElement)subElements.get(it.next());
//				cValue.put(element.getName(), element.getValue());
//			}
//			return cValue;
		}
		return value;		
	}
}
