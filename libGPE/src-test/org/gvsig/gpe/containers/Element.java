package org.gvsig.gpe.containers;

import java.util.ArrayList;


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
 * $Id: Element.java 108 2007-05-15 07:28:34Z jorpiell $
 * $Log$
 * Revision 1.4  2007/05/15 07:28:34  jorpiell
 * Children Element printed
 *
 * Revision 1.3  2007/05/02 11:46:07  jorpiell
 * Writing tests updated
 *
 * Revision 1.2  2007/04/19 11:50:20  csanchez
 * Actualizacion protoripo libGPE
 *
 * Revision 1.1  2007/04/14 16:06:35  jorpiell
 * Add the container classes
 *
 *
 */
/**
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class Element {
	private String namespace = null;
	private String name = null;
	private String id = null;
	private Object value = null;
	private Object type = null;
	private Element parentElement = null;
	private ArrayList elements = new ArrayList();
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the type
	 */
	public Object getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(Object type) {
		this.type = type;
	}
	/**
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * @return the parentElement
	 */
	public Element getParentElement() {
		return parentElement;
	}
	/**
	 * @param parentElement the parentElement to set
	 */
	public void setParentElement(Object parentElement) {
		if (parentElement != null){
			this.parentElement = (Element)parentElement;
			((Element)parentElement).addChildEElement(this);
		}
	}
	/**
	 * @return the elements
	 */
	public ArrayList getElements() {
		return elements;
	}
	
	/**
	 * @return the element at position i
	 * @param i
	 * Element position
	 */
	public Element getElementAt(int i) {
		return (Element)elements.get(i);
	}
		
	/**
	 * @param parentElement the parentElement to set
	 */
	public void setParentElement(Element parentElement) {
		this.parentElement = parentElement;
	}
	
	/**
	 * @param adds a child element
	 */
	public void addChildEElement(Element element) {
		getElements().add(element);
	}
	/**
	 * @return the namespace
	 */
	public String getNamespace() {
		return namespace;
	}
	/**
	 * @param namespace the namespace to set
	 */
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
}
