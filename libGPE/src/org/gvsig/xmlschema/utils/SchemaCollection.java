package org.gvsig.xmlschema.utils;

import java.util.Collection;
import java.util.Iterator;

import org.gvsig.xmlschema.som.IXSNode;
import org.gvsig.xmlschema.som.IXSSchema;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

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
 * $Id: SchemaCollection.java 157 2007-06-22 12:22:53Z jorpiell $
 * $Log$
 * Revision 1.2  2007/06/22 12:20:48  jorpiell
 * The typeNotFoundException has been deleted. It never was thrown
 *
 * Revision 1.1  2007/06/14 16:15:03  jorpiell
 * builds to create the jars generated and add the schema code to the libGPEProject
 *
 * Revision 1.1  2007/06/14 13:50:07  jorpiell
 * The schema jar name has been changed
 *
 * Revision 1.5  2007/06/08 06:55:05  jorpiell
 * Fixed some bugs
 *
 * Revision 1.4  2007/06/07 14:54:13  jorpiell
 * Add the schema support
 *
 * Revision 1.3  2007/05/30 12:53:33  jorpiell
 * Not used libraries deleted
 *
 * Revision 1.2  2007/05/30 12:50:53  jorpiell
 * Refactoring of some duplicated methods
 *
 * Revision 1.1  2007/05/30 12:25:48  jorpiell
 * Add the element collection
 *
 *
 */
/**
 * This class represents the children from a 
 * DOM node
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class SchemaCollection implements Collection{
	private Element element = null;	
	private SchemaObjectsMapping collectionTypes = null;
	private IXSSchema schema = null;
	
	public SchemaCollection(IXSSchema schema, Element element) {
		this.schema = schema;
		this.element = element;		
		this.collectionTypes = schema.getObjectsMapping();		
	}	
	
	public SchemaCollection(IXSSchema schema, Element element, SchemaObjectsMapping collectionTypes) {
		this.schema = schema;
		this.element = element;		
		this.collectionTypes = collectionTypes;		
	}	
	
	/**
	 * Return the first element of the collection
	 * @return
	 */
	public IXSNode getFirstNode(){
		Iterator it = iterator();
		it.hasNext();
		Object obj = it.next();
		if (obj != null){
			return (IXSNode)obj;
		}
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.util.Collection#add(java.lang.Object)
	 */
	public boolean add(Object o) {
		if (o instanceof IXSNode){
			element.appendChild(((IXSNode)o).getElement());
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Collection#addAll(java.util.Collection)
	 */
	public boolean addAll(Collection c) {
		Iterator it = c.iterator();
		while (it.hasNext()){
			Object obj = it.next();
			if (!add(obj)){
				return false;
			}
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Collection#clear()
	 */
	public void clear() {
		NodeList nl = element.getChildNodes();
		for(int i = 0; i < nl.getLength(); i++){
			element.removeChild(nl.item(i));
		}		
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Collection#contains(java.lang.Object)
	 */
	public boolean contains(Object o) {
		if (o instanceof IXSNode){
			return false;
		}
		Element element = ((IXSNode)o).getElement();
		NodeList nl = element.getChildNodes();
		for(int i = 0; i < nl.getLength(); i++){
			if (nl.item(i).equals(element)){
				return true;
			}
		}
		return false;
	}

	
	public boolean containsAll(Collection c) {
		throw new UnsupportedOperationException();	
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Collection#isEmpty()
	 */
	public boolean isEmpty() {
		return element.getChildNodes().getLength() == 0;
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Collection#iterator()
	 */
	public Iterator iterator() {
		return new SchemaIterator(element.getChildNodes(), collectionTypes);
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Collection#remove(java.lang.Object)
	 */
	public boolean remove(Object o) {
		NodeList nl = element.getChildNodes();
		for(int i = 0; i < nl.getLength(); i++){
			if (o.equals(nl.item(i))){
				element.removeChild(nl.item(i));
				return true;
			}
		}	
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Collection#removeAll(java.util.Collection)
	 */
	public boolean removeAll(Collection c) {
		NodeList nl = element.getChildNodes();
		for(int i = 0; i < nl.getLength(); i++){
			element.removeChild(nl.item(i));
		}	
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Collection#retainAll(java.util.Collection)
	 */
	public boolean retainAll(Collection c) {
		throw new UnsupportedOperationException();	
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Collection#size()
	 */
	public int size() {
		int size = 0;
		Iterator it = collectionTypes.getTypes().iterator();
		while (it.hasNext()){
			String type = (String)it.next();
			Element childElement = SchemaUtils.searchChildByTagName(element, type);
			if (childElement != null){
				size++;
			}
		}
		return size;
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Collection#toArray()
	 */
	public Object[] toArray() {
		throw new UnsupportedOperationException();	
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Collection#toArray(java.lang.Object[])
	 */
	public Object[] toArray(Object[] a) {
		throw new UnsupportedOperationException();	
	}
	
	/**
	 * Add a new type to the types collection
	 * @param type
	 * Type name that will be found in the XML file
	 * @param clazz
	 * Class used to create the object that envolves the element
	 * @throws TypeNotFoundException
	 */
	public void addType(String type, Class clazz){
		if (collectionTypes == null){
			collectionTypes = new SchemaObjectsMapping(schema);
		}
		collectionTypes.addType(type, clazz);		
	}	
}
