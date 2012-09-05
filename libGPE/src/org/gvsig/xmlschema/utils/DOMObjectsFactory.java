package org.gvsig.xmlschema.utils;

import org.gvsig.xmlschema.som.IXSContentType;
import org.gvsig.xmlschema.som.IXSElementDeclaration;
import org.gvsig.xmlschema.som.IXSSchema;
import org.w3c.dom.Element;

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
 * $Id: DOMObjectsFactory.java 164 2007-07-02 10:00:46Z jorpiell $
 * $Log$
 * Revision 1.3  2007/07/02 09:57:35  jorpiell
 * The generated xsd schemas have to be valid
 *
 * Revision 1.2  2007/06/29 12:19:14  jorpiell
 * The schema validation is made independently of the concrete writer
 *
 * Revision 1.1  2007/06/14 16:15:03  jorpiell
 * builds to create the jars generated and add the schema code to the libGPEProject
 *
 * Revision 1.1  2007/06/14 13:50:07  jorpiell
 * The schema jar name has been changed
 *
 * Revision 1.3  2007/06/08 11:35:16  jorpiell
 * IXSSchema interface updated
 *
 * Revision 1.2  2007/06/08 06:55:05  jorpiell
 * Fixed some bugs
 *
 * Revision 1.1  2007/06/07 14:54:13  jorpiell
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
 * This class contains methods to create DOM elements.
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class DOMObjectsFactory {
	private static DOMObjectsFactory instance = null;
	private String xsNamespacePrefix = null;
	
	/**
	 * This method cretaes the singleton instance
	 *
	 */
	private synchronized static void createInstance() {
		if (instance == null) { 
			instance = new DOMObjectsFactory();
		}
	}

	/**
	 * @return the factory instance
	 */
	public static DOMObjectsFactory getInstance() {
		if (instance == null){
			createInstance();
		}
		return instance;
	}
	
	public DOMObjectsFactory(){
		xsNamespacePrefix = SchemaTags.XS_NS;
	}
	
	/**
	 * Fill the element node attributes  
	 * @param schema
	 * Schema that will be used to create the element
	 * @param name
	 * Element name
	 * @param type
	 * Element type
	 * @param nillable
	 * If the element is nillable
	 * @param minOccurs
	 * The min occurs
	 * @param maxOccurs
	 * The max occurs
	 */
	public Element createElement(IXSSchema schema, String name, String type, boolean nillable, int minOccurs, int maxOccurs){
		Element element = schema.getDocument().createElement(addXSQname(SchemaTags.ELEMENT));
		element.setAttribute(SchemaTags.NAME, name);
		if (type != null){
			element.setAttribute(SchemaTags.TYPE, type);
		}
		element.setAttribute(SchemaTags.NILLABLE, String.valueOf(nillable));
		String sMinOccurs = null;
		if (minOccurs == IXSElementDeclaration.MIN_OCCURS_UNBOUNDED){
			sMinOccurs = SchemaTags.UNBOUNDED;
		}else{
			sMinOccurs = String.valueOf(minOccurs);
		}
		element.setAttribute(SchemaTags.MIN_OCCURS, sMinOccurs);
		String sMaxOccurs = null;
		if (maxOccurs == IXSElementDeclaration.MAX_OCCURS_UNBOUNDED){
			sMaxOccurs = SchemaTags.UNBOUNDED;
		}else{
			sMaxOccurs = String.valueOf(maxOccurs);
		}		
		element.setAttribute(SchemaTags.MAX_OCCURS, sMaxOccurs);
		return element;
	}
	
	/**
	 * Fill the element node attributes  
	 * @param schema
	 * Schema that will be used to create the element
	 * @param name
	 * Element name
	 * @param type
	 * Element type
	 * @param nillable
	 * If the element is nillable
	 * @param minOccurs
	 * The min occurs
	 * @param maxOccurs
	 * The max occurs
	 */
	public Element createElement(IXSSchema schema, String name, String type, String substitutionGroup){
		Element element = schema.getDocument().createElement(addXSQname(SchemaTags.ELEMENT));
		element.setAttribute(SchemaTags.NAME, name);
		if (type != null){
			element.setAttribute(SchemaTags.TYPE, type);
		}
		if (substitutionGroup != null){
			element.setAttribute(SchemaTags.SUBSTITUTIONGROUP, type);
		}
		return element;
	}
	
	/**
	 * Creates a new ComplxType element
	 * @param schema
	 * Schema that will be used to create the element
	 * @param name
	 * ComplexType name
	 * @param type
	 * See the IXSComplexType for possible values
	 * @param contentType
	 * A complex content or a simple content
	 * See the IXSContentType for possible values
	 * @param contentTypeRestriction
	 * A extension or a restriction
	 * @return
	 */	
	public Element createComplexType(IXSSchema schema, String name, String type, String contentType, String conteTypeRestriction){
		Element eComplexType = schema.getDocument().createElement(addXSQname(SchemaTags.COMPLEX_TYPE));
		if (name != null){
			eComplexType.setAttribute(SchemaTags.NAME, name);
		}		
		if (contentType.compareTo(IXSContentType.WITOUT_CONTENT) == 0){
			Element eSimpleContent = schema.getDocument().createElement(addXSQname(type));
			eComplexType.appendChild(eSimpleContent);
		}else{
			Element eContentType = schema.getDocument().createElement(addXSQname(contentType));
			eComplexType.appendChild(eContentType);
			Element eRestriction = schema.getDocument().createElement(addXSQname(conteTypeRestriction));
			eContentType.appendChild(eRestriction);
			Element eSimpleContent = schema.getDocument().createElement(addXSQname(type));
			eRestriction.appendChild(eSimpleContent);
		}
		return eComplexType;
	}	
	
	/**
	 * Adds an element to a complex type. It only will works
	 * if the type doesn't has a simple content or a complex
	 * content 
	 * @param schema
	 * XML schema
	 * @param eElement
	 * The XML schema element
	 * @param eComplexType
	 * The XML schema complex type
	 */
	public boolean addElementToComplexType(IXSSchema schema, Element eElement, Element eComplexType){
		//If is a ComplexContent..
		Element eComplexContent = SchemaUtils.searchChildByTagName(eComplexType, SchemaTags.COMPLEX_CONTENT);
		if (eComplexContent != null){
			Element eContentElement = findElementToWrite(schema, eComplexContent, getContentElements());
			if (eContentElement != null){
				//Restriction or extension
				Element eGroup = findElementToWrite(schema, eContentElement, getGroupElements());
				if (eGroup != null){
					eGroup.appendChild(eElement);
					return true;
				}
			}			
		}
		//If is a simpleContent..
		Element eSimpleContent = SchemaUtils.searchChildByTagName(eComplexType, SchemaTags.SIMPLE_CONTENT);
		if (eSimpleContent != null){
			Element eContentElement = findElementToWrite(schema, eSimpleContent, getContentElements());
			if (eContentElement != null){
				eContentElement.appendChild(eElement);
			}
			return true;
		}
		//Else will be a group, a sequence, a choice or a all node.
		Element eGroup = findElementToWrite(schema, eComplexType, getGroupElements());
		if (eGroup != null){
			eGroup.appendChild(eElement);
 			return true;
		}
		return false;
	}	
		
	/**
	 * Find the root element to write the sub elements. 
	 * @param schema
	 * XML Schema
	 * @param root
	 * Root element
	 * @param elements
	 * Elements to search
	 * @return
	 */
	private Element findElementToWrite(IXSSchema schema, 
			Element root,
			String[] elements){	
		for (int i=0 ; i<elements.length ; i++){
			Element element = SchemaUtils.searchChildByTagName(root, elements[i]);
			if (element != null){
				return element;
			}
		}		
		return null;
	}	

	/**
	 * @return the group elements
	 */
	private String[] getGroupElements(){
		String[] groupElements = new String[4];
		groupElements[0] = SchemaTags.SEQUENCE;
		groupElements[1] = SchemaTags.CHOICE;
		groupElements[2] = SchemaTags.ALL;
		groupElements[3] = SchemaTags.GROUP;
		return groupElements;
	}
	

	/**
	 * @return the content elements
	 */
	private String[] getContentElements(){
		String[] groupElements = new String[2];
		groupElements[0] = SchemaTags.RESTRICTION;
		groupElements[1] = SchemaTags.EXTENSION;
		return groupElements;
	}
	
	/**
	 * Uset to create the XML schema tags. It adds the
	 * namespace prefix (xs or xsd)
	 * @param tagName
	 * @return
	 */
	private String addXSQname(String tagName){
		return xsNamespacePrefix + ":" + tagName;
	}

	/**
	 * @param xsNamespacePrefix the xsNamespacePrefix to set
	 */
	public void setXsNamespacePrefix(String xsNamespacePrefix) {
		this.xsNamespacePrefix = xsNamespacePrefix;
	}
}
