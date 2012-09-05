package org.gvsig.xmlschema.utils;

import java.util.Iterator;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
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
 * $Id: SchemaIterator.java 151 2007-06-14 16:15:05Z jorpiell $
 * $Log$
 * Revision 1.1  2007/06/14 16:15:03  jorpiell
 * builds to create the jars generated and add the schema code to the libGPEProject
 *
 * Revision 1.1  2007/06/14 13:50:07  jorpiell
 * The schema jar name has been changed
 *
 * Revision 1.2  2007/06/07 14:54:13  jorpiell
 * Add the schema support
 *
 * Revision 1.1  2007/05/30 12:25:48  jorpiell
 * Add the element collection
 *
 *
 */
/**
 * To retrieve the DOM element children
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class SchemaIterator implements Iterator {
	private NodeList nodeList = null;
	private SchemaObjectsMapping collectionTypes = null;
	private String currentNode = null;
	int index = -1;
	
	public SchemaIterator(NodeList nodeList, SchemaObjectsMapping collectionTypes){
		this.nodeList = nodeList;
		this.collectionTypes = collectionTypes;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
		boolean nextFound = false;
		index ++;
		while ((!nextFound) && (index < nodeList.getLength())){
			Node node = nodeList.item(index);
			if(node.getNodeType() == Node.ELEMENT_NODE){
				Iterator it = collectionTypes.getTypes().iterator();
				while (it.hasNext()){
					String type = (String)it.next();
					if (SchemaUtils.matches(node, SchemaTags.XS_NS, type)){
						currentNode = type;
						return true;
					}
				}
			}
			index++;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Iterator#next()
	 */
	public Object next() {
		if ((index > -1) && (index < nodeList.getLength())){
			return collectionTypes.getNode(currentNode, (Element)nodeList.item(index));
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Iterator#remove()
	 */
	public void remove() {
		throw new UnsupportedOperationException();		
	}

}
