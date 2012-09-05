package org.gvsig.xmlschema.som.impl;

import java.util.Iterator;

import org.gvsig.xmlschema.som.IXSComplexTypeDefinition;
import org.gvsig.xmlschema.som.IXSElementDeclaration;
import org.gvsig.xmlschema.som.IXSSchema;
import org.gvsig.xmlschema.som.IXSTypeDefinition;
import org.gvsig.xmlschema.utils.SchemaCollection;
import org.gvsig.xmlschema.utils.SchemaTags;
import org.gvsig.xmlschema.utils.SchemaUtils;
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
 * $Id: XSElementDeclarationImpl.java 164 2007-07-02 10:00:46Z jorpiell $
 * $Log$
 * Revision 1.3  2007/07/02 09:57:35  jorpiell
 * The generated xsd schemas have to be valid
 *
 * Revision 1.2  2007/06/22 12:20:48  jorpiell
 * The typeNotFoundException has been deleted. It never was thrown
 *
 * Revision 1.1  2007/06/14 16:15:03  jorpiell
 * builds to create the jars generated and add the schema code to the libGPEProject
 *
 * Revision 1.1  2007/06/14 13:50:07  jorpiell
 * The schema jar name has been changed
 *
 * Revision 1.4  2007/06/08 11:35:16  jorpiell
 * IXSSchema interface updated
 *
 * Revision 1.3  2007/06/07 14:54:13  jorpiell
 * Add the schema support
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
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class XSElementDeclarationImpl extends XSComponentImpl implements IXSElementDeclaration{
		
	public XSElementDeclarationImpl(IXSSchema schema) {
		super(schema);		
	}

	/**
	 * @return The element type
	 * @throws TypeNotFoundException 
	 */
	public IXSTypeDefinition getTypeDefinition(){
		//If is defined in the same node
		Iterator it = new SchemaCollection(getSchema(),getElement()).iterator();
		it.hasNext();
		Object type = it.next();
		//If is defined in the same schema
		if (type == null){
			type = getSchema().getTypeByName(getQName().getNamespaceURI(), 
					getElement().getAttribute(SchemaTags.TYPE));
		}
		if (type != null){
			return (IXSTypeDefinition)type;
		}
		return null;
	}


	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.schema.som.IXSElementDeclaration#getTypeName()
	 */
	public String getTypeName() {
		return getElement().getAttribute(SchemaTags.TYPE);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.schema.som.IXSElementDeclaration#isNillable()
	 */
	public boolean isNillable() {
		String nillable = getElement().getAttribute(SchemaTags.NILLABLE);
		if (nillable != null){
			if (nillable.compareTo(SchemaTags.FALSE) == 0){
				return false;
			}
		}
		return true;
	}	
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.schema.som.IXSElementDeclaration#getMaxOccurs()
	 */
	public int getMaxOccurs() {
		String maxOccurs = getElement().getAttribute(SchemaTags.MAX_OCCURS);
		if (maxOccurs != null){
			return Integer.parseInt(maxOccurs);
		}
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.schema.som.IXSElementDeclaration#getMinOccurs()
	 */
	public int getMinOccurs() {
		String minOccurs = getElement().getAttribute(SchemaTags.MIN_OCCURS);
		if (minOccurs != null){
			return Integer.parseInt(minOccurs);
		}
		return 0;
	}

	public IXSElementDeclaration getSubElementByName(String nodeName) {
		if (getTypeDefinition() == null){
			return null;
		}
		Element element = getTypeDefinition().getElement();
		nodeName = nodeName.substring(nodeName.indexOf(":")+1,nodeName.length());
		Element newElement = searchNode(element, nodeName);
		if (newElement != null){
			XSElementDeclarationImpl childElement =  new XSElementDeclarationImpl(getSchema());
			childElement.setElement(newElement);
			return childElement;
		}
		return null;	
	}
	
	private Element searchNode(Element element, String nodeName){
		NodeList nodeList = element.getChildNodes();
		for (int i=0 ; i<nodeList.getLength() ; i++){
			Node node = nodeList.item(i);
				if(node.getNodeType() == Node.ELEMENT_NODE){
				Element childElement = (Element)node;				
				if (SchemaUtils.matches(node, SchemaTags.XS_NS, SchemaTags.COMPLEX_CONTENT)){
					return searchNode(childElement, nodeName);
				}else if (SchemaUtils.matches(node, SchemaTags.XS_NS, SchemaTags.EXTENSION)){
					return searchNode(childElement, nodeName);
				}else if (SchemaUtils.matches(node, SchemaTags.XS_NS, SchemaTags.RESTRICTION)){
					return searchNode(childElement, nodeName);
				}else if (SchemaUtils.matches(node, SchemaTags.XS_NS, SchemaTags.SEQUENCE)){
					return searchNode(childElement, nodeName);
				}else if (SchemaUtils.matches(node, SchemaTags.XS_NS, SchemaTags.ALL)){
					return searchNode(childElement, nodeName);
				}else if (SchemaUtils.matches(node, SchemaTags.XS_NS, SchemaTags.CHOICE)){
					return searchNode(childElement, nodeName);
				}else if (SchemaUtils.matches(node, SchemaTags.XS_NS, SchemaTags.GROUP)){
					return searchNode(childElement, nodeName);
				}			
				if (childElement.getAttribute(SchemaTags.NAME).compareTo(nodeName) == 0){
					return childElement;
				}	
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.schema.som.IXSElementDeclaration#addComplexType(java.lang.String, java.lang.String, java.lang.String)
	 */
	public IXSComplexTypeDefinition addComplexType(String type,
			String contentType, String contentTypeRestriction) {
		getElement().removeAttribute(SchemaTags.TYPE);
		Element eComplexType = getElementsFactory().createComplexType(
				getSchema(), 
				null,
				type,
				contentType,
				contentTypeRestriction);
		addChildElement(eComplexType);
		XSComplexTypeDefinitionImpl complexType = new XSComplexTypeDefinitionImpl(getSchema());
		complexType.setElement(eComplexType);
		return complexType;
	}
	
}
