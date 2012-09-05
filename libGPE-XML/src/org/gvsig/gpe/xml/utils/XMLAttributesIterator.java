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
 * 2009 {Iver T.I.}   {Task}
 */

package org.gvsig.gpe.xml.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.namespace.QName;

import org.gvsig.gpe.parser.IAttributesIterator;
import org.gvsig.gpe.xml.stream.IXmlStreamReader;
import org.gvsig.gpe.xml.stream.XmlStreamException;

/**
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public class XMLAttributesIterator implements IAttributesIterator {
	private Map attributes = new HashMap(); 
	private Iterator keys = null;
	private QName currentAttibuteName = null;

	public XMLAttributesIterator(IXmlStreamReader parser) throws XmlStreamException {
		super();
		while(parser.getEventType() != IXmlStreamReader.START_ELEMENT){
			parser.next();
		}
		int num_atributos = parser.getAttributeCount();
		for (int i=0 ; i<parser.getAttributeCount() ; i++){
			QName atributo = parser.getAttributeName(i);
			String valor=parser.getAttributeValue(i);
			if (valor!=null)
				attributes.put(atributo,valor);
		}
		initialize();
	}
	
	public Map getAttributes(){
		return attributes;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.gpe.parser.IAttributesIterator#getNumAttributes()
	 */
	public int getNumAttributes() {
		return attributes.size();
	}

	/* (non-Javadoc)
	 * @see org.gvsig.gpe.parser.IAttributesIterator#hasNext()
	 */
	public boolean hasNext() throws IOException {
		currentAttibuteName = null;
		return (keys.hasNext());
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

