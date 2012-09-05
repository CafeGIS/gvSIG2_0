package org.gvsig.xmlschema.utils;



import javax.xml.namespace.QName;

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
 * $Id: SchemaUtils.java 192 2007-11-26 08:49:01Z jpiera $
 * $Log$
 * Revision 1.2  2007/06/28 13:04:33  jorpiell
 * The Qname has been updated to the 1.5 JVM machine. The schema validation is made in the GPEWriterHandlerImplementor class
 *
 * Revision 1.1  2007/06/14 16:15:03  jorpiell
 * builds to create the jars generated and add the schema code to the libGPEProject
 *
 * Revision 1.1  2007/06/14 13:50:07  jorpiell
 * The schema jar name has been changed
 *
 * Revision 1.6  2007/06/08 13:00:40  jorpiell
 * Add the targetNamespace to the file
 *
 * Revision 1.5  2007/06/08 07:31:20  jorpiell
 * Add the euroRoadS test
 *
 * Revision 1.4  2007/06/08 06:55:05  jorpiell
 * Fixed some bugs
 *
 * Revision 1.3  2007/05/30 12:50:53  jorpiell
 * Refactoring of some duplicated methods
 *
 * Revision 1.2  2007/05/30 12:25:48  jorpiell
 * Add the element collection
 *
 * Revision 1.1  2007/05/25 11:55:00  jorpiell
 * First update
 *
 *
 */
/**
 * Some utils to manage xml schemas
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class SchemaUtils {
	
	/**
	 * Compare a DOM nod3 by name
	 * @param n
	 * DOM node
	 * @param namespace
	 * Node namespace
	 * @param tagName
	 * Node name
	 * @return
	 * <code>true</code> if the node has this name
	 */
	public static boolean matches(Node n, String namespace, String tagName){
		String nodeName = n.getNodeName().substring(n.getNodeName().indexOf(":")+1, n.getNodeName().length());
		int index = n.getNodeName().indexOf(":");
		String nodeNSURI = null;
		if (index > 0){
			nodeNSURI = n.getNodeName().substring(0,n.getNodeName().indexOf(":"));
		}
		if (nodeNSURI == null){
			return objNullEq(tagName.toUpperCase(), nodeName.toUpperCase());
		}
		return objNullEq(nodeNSURI.toUpperCase(), namespace.toUpperCase()) &&
			objNullEq(tagName.toUpperCase(), nodeName.toUpperCase());
	}
	
	/**
	 * Determines if two QNames are equal
	 * @param qname1
	 * First qname to compare
	 * @param qname2
	 * Second qname to compare
	 * @return
	 * <code>true</code> or <code>false</code> according
	 * to the above description.
	 */
	public static boolean matches(QName qname1, QName qname2){
		return objNullEq(qname1.getNamespaceURI().toUpperCase(), qname2.getNamespaceURI().toUpperCase()) &&
			objNullEq(qname1.getLocalPart().toUpperCase(), qname2.getLocalPart().toUpperCase());
	}
	
	/**
	 * Search a Element by name
	 * @param element
	 * Parent element
	 * @param childName
	 * Element name to find
	 * @return
	 */
	public static Element searchChildByTagName(Element element, String childName){
		NodeList nodeList = element.getChildNodes();
		for(int i = 0; i < nodeList.getLength(); i++){
			Node node = nodeList.item(i);
			if(node.getNodeType() == Node.ELEMENT_NODE){
				if (SchemaUtils.matches(node, SchemaTags.XS_NS, childName)){
					return (Element)node;
				}
			}
		}
		return null;
	}	
	
	/**
	 * Search a Element with a specified type and 
	 * a specified attribute name
	 * @param element
	 * Parent element
	 * @param tagName
	 * Element tag name to find
	 * @param targetNamespace
	 * Namespace for the name attribute
	 * @param attributeName
	 * Value for the attribute name 
	 * @return
	 */
	public static Element searchChildByAttributeName(Element element, String tagName, String targetNamespace, String attributeName){
		NodeList nodeList = element.getChildNodes();
		for(int i = 0; i < nodeList.getLength(); i++){
			Node node = nodeList.item(i);
			if(node.getNodeType() == Node.ELEMENT_NODE){
				if (SchemaUtils.matches(node, SchemaTags.XS_NS, tagName)){
					String name = ((Element)node).getAttribute(SchemaTags.NAME);
					if (objNullEq(name, attributeName)){
						return (Element)node;
					}
				}
			}
		}
		return null;
	}	
	
	/**
	 * Determines if two objects are equal.  
	 * @param object1 
	 * First object to compare.
	 * @param object2 
	 * Second object to compare.
	 * @return 
	 * <code>true</code> or <code>false</code> according
	 * to the above description.
	 */
	private static boolean objNullEq(Object object1, Object object2){
		if((object1 == null) && (object2 == null)){
			return true;
		}
		if((object1 == null) && (object2 != null)){
			return false;
		}
		if((object1 != null) && (object2 == null)){
			return false;
		}
		return object1.equals(object2);
	}

	/**
	 * Gets the local name for a qualified name
	 * @param qualifiedName
	 * @return
	 */
	public static String getLocalName(String qualifiedName) {
		int index = qualifiedName.indexOf(":");
		if (index < 0){
			return qualifiedName;
		}
		return qualifiedName.substring(index + 1, qualifiedName.length());
	}
}
