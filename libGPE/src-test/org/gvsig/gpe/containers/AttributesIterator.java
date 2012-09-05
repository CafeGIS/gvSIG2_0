package org.gvsig.gpe.containers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.namespace.QName;

import org.gvsig.gpe.parser.IAttributesIterator;
/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
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
*   Av. Blasco Ib��ez, 50
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
* @author Carlos S�nchez Peri��n
*/
public class AttributesIterator implements IAttributesIterator{
	private HashMap attributes;
	private Iterator keys = null;
	private QName currentAttibuteName = null;
	
	public AttributesIterator(QName name, Object value){
		attributes = new HashMap();
		attributes.put(name, value);
		initialize();
	}
	
	public AttributesIterator(QName[] names, String[] values){
		attributes = new HashMap();
		for (int i=0 ; i<names.length ; i++){
			attributes.put(names[i], values[i]);
		}
		initialize();
	}	
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.parser.IAttributesIterator#hasNext()
	 */
	public boolean hasNext() throws IOException {
		currentAttibuteName = null;
		return (keys.hasNext());
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.parser.IAttributesIterator#getNumAttributes()
	 */
	public int getNumAttributes() {
		return attributes.size();
	}

	/* (non-Javadoc)
	 * @see org.gvsig.gpe.parser.IAttributesIterator#nextAttribute()
	 */
	public Object nextAttribute() throws IOException {
		setAttributeName();
		return attributes.get(currentAttibuteName);	
	}

	/* (non-Javadoc)
	 * @see org.gvsig.gpe.parser.IAttributesIterator#nextAttributeName()
	 */
	public QName nextAttributeName() {
		setAttributeName();
		return currentAttibuteName;
	}
	
	private void setAttributeName(){
		if (currentAttibuteName == null){
			currentAttibuteName = (QName)keys.next();
		}
	}

	/* (non-Javadoc)
	 * @see org.gvsig.gpe.parser.IAttributesIterator#initialize()
	 */
	public void initialize() {
		keys = attributes.keySet().iterator();		
	}

}
