package org.gvsig.gpe.utils;

import java.util.Stack;

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
 * $Id: TypedObjectStack.java 162 2007-06-29 12:19:48Z jorpiell $
 * $Log$
 * Revision 1.1  2007/06/29 12:19:14  jorpiell
 * The schema validation is made independently of the concrete writer
 *
 *
 */
/**
 * This class implements a object stack. Every object
 * is composed by an object name and an XML schema
 * element name. It is used on writing process to save
 * the state information
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 * @author Carlos Sánchez Periñán (sanchez_carper@gva.es)
 */
public class TypedObjectStack {
	private Stack stack = null;
	
	public TypedObjectStack(){
		this.stack = new Stack();
	}
	
	/**
	 * Retrieve the las element from the top of the 
	 * stack
	 * @return
	 * The las stack element
	 */
	public TypedObject pop(){
		if (stack.size() > 0){
			return (TypedObject)stack.pop();
		}
		return null;
	}
	
	/**
	 * Add a new element to the stack
	 * @param typedObject
	 * Object to add
	 */
	public void push(TypedObject typedObject){
		stack.push(typedObject);
	}
	
	/**
	 * Add a new element to the stack
	 * @param namespace
	 * Element namespace	
	 * @param name
	 * Element name
	 * @param isRemoved
	 * If the element has to be removed
	 */
	public void push(String namespace, String name, boolean isRemoved){
		stack.push(new TypedObject(namespace,name,isRemoved));
	}
	
	/**
	 * Add a new element to the stack
	 * @param namespace
	 * Element namespace	
	 * @param name
	 * Element name
	  */
	public void push(String namespace, String name){
		stack.push(new TypedObject(namespace,name,false));
	}
	
	/**
	 * @return the stack size
	 */
	public int size(){
		return stack.size();
	}	
	
	/**
	 * @return the last stack element without retrieve it
	 */
	public TypedObject lastElement(){
		if (stack.size() > 0){
			return (TypedObject)stack.lastElement();
		}
		return null;
	}
	
	/**
	 * This class implements a pair of element name
	 * ald XML schema element type.
	 * @author Jorge Piera Llodrá (jorge.piera@iver.es)
	 */
	public class TypedObject{
		private String name = null;
		private String namespace = null;
		private boolean isRemoved = false;
		
		TypedObject(String namespace, String name, boolean isRemoved) {
			this.name = name;
			this.namespace = namespace;
			this.isRemoved = isRemoved;
		}

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @return the namespace
		 */
		public String getNamespace() {
			return namespace;
		}

		/**
		 * @return the isRemoved
		 */
		public boolean isRemoved() {
			return isRemoved;
		}
	}

}
