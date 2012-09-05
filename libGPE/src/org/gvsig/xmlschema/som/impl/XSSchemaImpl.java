package org.gvsig.xmlschema.som.impl;

import java.io.OutputStream;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.gvsig.compat.CompatLocator;
import org.gvsig.xmlschema.exceptions.SchemaWrittingException;
import org.gvsig.xmlschema.som.IXSComplexTypeDefinition;
import org.gvsig.xmlschema.som.IXSElementDeclaration;
import org.gvsig.xmlschema.som.IXSNode;
import org.gvsig.xmlschema.som.IXSSchema;
import org.gvsig.xmlschema.som.IXSTypeDefinition;
import org.gvsig.xmlschema.utils.DOMObjectsFactory;
import org.gvsig.xmlschema.utils.SchemaCollection;
import org.gvsig.xmlschema.utils.SchemaObjectsMapping;
import org.gvsig.xmlschema.utils.SchemaTags;
import org.gvsig.xmlschema.utils.SchemaUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2 IVER T.I. and Generalitat Valenciana.
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
 * Foundation, Inc., 9 Temple Place - Suite , Boston, MA  2111-1,USA.
 *
 * For more information, contact:
 *
 *  Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ibáñez, 
 *   1 VALENCIA
 *   SPAIN
 *
 *      + 
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   IVER T.I. S.A
 *   Salamanca 
 *    Valencia
 *   Spain
 *
 *   + 1
 *   dac@iver.es
 */
/* CVS MESSAGES:
 *
 * $Id: XSSchemaImpl.java 164 2007-07-02 10:00:46Z jorpiell $
 * $Log$
 * Revision 1.4  2007/07/02 09:57:35  jorpiell
 * The generated xsd schemas have to be valid
 *
 * Revision 1.3  2007/06/28 13:04:33  jorpiell
 * The Qname has been updated to the 1.5 JVM machine. The schema validation is made in the GPEWriterHandlerImplementor class
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
 * Revision 1.9  2007/06/08 11:35:16  jorpiell
 * IXSSchema interface updated
 *
 * Revision 1.8  2007/06/08 07:31:20  jorpiell
 * Add the euroRoadS test
 *
 * Revision 1.7  2007/06/08 06:55:05  jorpiell
 * Fixed some bugs
 *
 * Revision 1.6  2007/06/07 14:54:13  jorpiell
 * Add the schema support
 *
 * Revision 1.5  2007/05/30 12:53:33  jorpiell
 * Not used libraries deleted
 *
 * Revision 1.4  2007/05/30 12:50:53  jorpiell
 * Refactoring of some duplicated methods
 *
 * Revision 1.3  2007/05/30 12:25:48  jorpiell
 * Add the element collection
 *
 * Revision 1.2  2007/05/28 12:38:03  jorpiell
 * Some bugs fixed
 *
 * Revision 1.1  2007/05/25 11:55:00  jorpiell
 * First update
 *
 *
 */
/**
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class XSSchemaImpl implements IXSSchema{
	//XML DOM objects
	private Document document = null;
	private Element element = null;
	//The target namespace
	private String targetNamespace = null;
	//Mappings to save objects
	private Map elements = null;
	private Map types = null;
	//XML DOM objects factory
	private DOMObjectsFactory objectsFactory = null;
	//All the XML schema object mapping
	private SchemaObjectsMapping objectsMapping = null;	

	public XSSchemaImpl(Document document){
		super();
		this.document = document;
		this.element = document.getDocumentElement();
		targetNamespace = element.getAttribute(SchemaTags.TARGET_NAMESPACE).trim();
		elements = new Hashtable();
		types = new Hashtable();
		initializeMapping();
	}

	/**
	 * It initializes the mapping with the supported mappings
	 * @throws TypeNotFoundException 
	 */
	private void initializeMapping(){
		objectsMapping = new SchemaObjectsMapping(this);
		objectsMapping.addType(SchemaTags.SIMPLE_TYPE, XSSimpleTypeDefinitionImpl.class);
		objectsMapping.addType(SchemaTags.COMPLEX_TYPE, XSComplexTypeDefinitionImpl.class);
		objectsMapping.addType(SchemaTags.ELEMENT, XSElementDeclarationImpl.class);
		objectsMapping.addType(SchemaTags.CHOICE, XSChoiceImpl.class);
		objectsMapping.addType(SchemaTags.SEQUENCE, XSSequenceImpl.class);
		objectsMapping.addType(SchemaTags.GROUP, XSGroupImpl.class);
		objectsMapping.addType(SchemaTags.ALL, XSAllImpl.class);
		objectsMapping.addType(SchemaTags.EXTENSION, XSExtensionImpl.class);
		objectsMapping.addType(SchemaTags.RESTRICTION, XSRestrictionImpl.class);		
		objectsMapping.addType(SchemaTags.SIMPLE_CONTENT, XSRestrictionImpl.class);		
		objectsMapping.addType(SchemaTags.COMPLEX_CONTENT, XSComplexContentImpl.class);		
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.schema.som.IXSSchema#getDocument()
	 */
	public Document getDocument() {
		return document;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.schema.som.IXSSchema#getElementDeclarations()
	 */
	public Collection getElementDeclarations(){
		return new SchemaCollection(this,element,getElementMapping());
	}		

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.schema.som.IXSSchema#getElementDeclarationByName(java.lang.String, java.lang.String)
	 */
	public IXSElementDeclaration getElementDeclarationByName(String elementURI, String elementName){
		elementName = SchemaUtils.getLocalName(elementName);
		Object obj = elements.get(elementName);
		if (obj != null){
			return (IXSElementDeclaration)obj;
		}
		IXSNode element = getObjectByName(elementURI, elementName,getElementMapping());
		if (element != null){
			elements.put(elementName, element);
			return (IXSElementDeclaration)element;
		}		
		return null;
	}


	/**
	 * Search a Element by name in the Dom tree and returns the
	 * IXSNode that contains it
	 * @param targetNamespace
	 * Element targetNamespace
	 * @param elementName
	 * Element local name
	 * @param typesMapping
	 * Mapping to create the IXSNode
	 * @return
	 * the IXSNode
	 */
	private IXSNode getObjectByName(String targetNamespace, String elementName, SchemaObjectsMapping typesMapping){
		QName qname = new QName(targetNamespace,elementName);
		Iterator it = typesMapping.getTypes().iterator();
		while (it.hasNext()){
			String type = (String)it.next();
			Element childElement = SchemaUtils.searchChildByAttributeName(element, type, targetNamespace, elementName);
			if (childElement != null){
				QName newQname = new QName(getTargetNamespace(),childElement.getAttribute(SchemaTags.NAME));
				if (SchemaUtils.matches(qname,newQname)){
					return typesMapping.getNode(type,childElement);
				}
			}				

		}	
		return null;
	}

	/**
	 * @return The elements mapping
	 * @throws TypeNotFoundException
	 */
	private SchemaObjectsMapping getElementMapping(){
		SchemaObjectsMapping objectsTypeMapping = new SchemaObjectsMapping(this);
		objectsTypeMapping.addType(SchemaTags.ELEMENT, XSElementDeclarationImpl.class);
		return objectsTypeMapping;
	}	

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.schema.som.IXSSchema#getTypeDefinitions()
	 */
	public Collection getTypeDefinitions(){
		return new SchemaCollection(this,element,getTypeMapping());
	}	

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.schema.som.IXSSchema#getTypeByName(java.lang.String, java.lang.String)
	 */
	public IXSTypeDefinition getTypeByName(String typeURI, String typeName){
		typeName = SchemaUtils.getLocalName(typeName);
		Object obj = types.get(typeName);
		if (obj != null){
			return (IXSTypeDefinition)obj;
		}
		IXSNode type = getObjectByName(typeURI, typeName, getTypeMapping());
		if (type != null){
			types.put(typeName, type);
			return (IXSTypeDefinition)type;
		}		
		return null;
	}

	/**
	 * @return The complex type mapping
	 * @throws TypeNotFoundException
	 */
	private SchemaObjectsMapping getTypeMapping(){
		SchemaObjectsMapping objectsTypeMapping = new SchemaObjectsMapping(this);
		objectsTypeMapping.addType(SchemaTags.COMPLEX_TYPE, XSComplexTypeDefinitionImpl.class);
		objectsTypeMapping.addType(SchemaTags.SIMPLE_TYPE, XSSimpleTypeDefinitionImpl.class);
		return objectsTypeMapping;
	}


	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.schema.som.IXSSchema#write(java.io.OutputStream)
	 */
	public void write(OutputStream os) throws SchemaWrittingException {
		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer;
		try {
			transformer = tFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(os);
			transformer.transform(source, result);
		} catch (TransformerConfigurationException e) {
			throw new SchemaWrittingException(e);
		} catch (TransformerException e) {
			throw new SchemaWrittingException(e);
		}
	}	

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.schema.som.IXSSchema#getNamespaceURI()
	 */
	public String getTargetNamespace() {
		return targetNamespace;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.xmlschema.som.IXSSchema#getTargetNamespacePrefix()
	 */
	public String getTargetNamespacePrefix() {
		return getNamespacePrefix(targetNamespace);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.xmlschema.som.IXSSchema#getNamespacePrefix(java.lang.String)
	 */
	public String getNamespacePrefix(String namespaceURI) {
		if (namespaceURI != null){
			NamedNodeMap attributes = element.getAttributes();
			for (int i=0 ; i<attributes.getLength() ; i++){
				Node node = attributes.item(i);
				//String[] name = node.getNodeName().split(":");
				//String[] name = org.gvsig.gpe.utils.StringUtils.splitString(node.getNodeName(),":");		
				String[] name = CompatLocator.getStringUtils().split(node.getNodeName(), ":");
				if ((name.length == 2) && (name[0].equals(SchemaTags.XMLNS_NS))){
					if (node.getNodeValue().equals(namespaceURI)){
						return name[1];
					}
				}
			}			
		}
		return null;
	}

	/**
	 * Add a new child element
	 * @param childElement
	 */
	private void addChildElement(Element childElement) {
		element.appendChild(childElement);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.schema.som.IXSSchema#addElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public IXSElementDeclaration addElement(String name, String type, String substitutionGroup) {
		String typeName = getTargetNamespacePrefix();
		if (typeName != null){
			type = typeName + ":" + type;
		}
		Element element = getObjectsFactory().createElement(
				this, 
				name,
				type,
				substitutionGroup);
		addChildElement(element);
		XSElementDeclarationImpl xsElement = new XSElementDeclarationImpl(this);
		xsElement.setElement(element);
		return xsElement;
	}	

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.schema.som.IXSSchema#addElement(java.lang.String, java.lang.String)
	 */
	public IXSElementDeclaration addElement(String name, String type) {
		return addElement(name, type, null);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.schema.som.IXSSchema#addComplexType(java.lang.String)
	 */
	public IXSComplexTypeDefinition addComplexType(String name, String type, 
			String contentType, String conteTypeRestriction) {
		Element eComplexType = getObjectsFactory().createComplexType(
				this, 
				name,
				type,
				contentType,
				conteTypeRestriction);
		addChildElement(eComplexType);
		XSComplexTypeDefinitionImpl complexType = new XSComplexTypeDefinitionImpl(this);
		complexType.setElement(eComplexType);
		return complexType;
	}

	/**
	 * @return the DOM objects factory
	 */
	private DOMObjectsFactory getObjectsFactory() {
		if (objectsFactory == null){
			objectsFactory = DOMObjectsFactory.getInstance();
		}
		return objectsFactory;
	}	

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.schema.som.IXSSchema#getTypesMapping()
	 */
	public SchemaObjectsMapping getObjectsMapping() {
		return objectsMapping;
	}
}


