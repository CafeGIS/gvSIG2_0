package org.gvsig.xmlschema.som.impl;

import java.io.FileInputStream;
import java.net.URI;
import java.util.Enumeration;
import java.util.Hashtable;

import org.gvsig.xmlschema.exceptions.NotDownloadFileException;
import org.gvsig.xmlschema.exceptions.SchemaCreationException;
import org.gvsig.xmlschema.som.IXSElementDeclaration;
import org.gvsig.xmlschema.som.IXSSchema;
import org.gvsig.xmlschema.som.IXSSchemaDocument;
import org.gvsig.xmlschema.som.IXSTypeDefinition;
import org.gvsig.xmlschema.utils.SchemaDocumentBuilder;

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
 * $Id: XSSchemaDocumentImpl.java 175 2007-11-20 16:14:37Z jpiera $
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
 * Revision 1.3  2007/06/08 13:00:40  jorpiell
 * Add the targetNamespace to the file
 *
 * Revision 1.2  2007/06/08 11:59:18  jorpiell
 * Add the default schema location
 *
 * Revision 1.1  2007/06/07 14:54:13  jorpiell
 * Add the schema support
 *
 *
 */
/**
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class XSSchemaDocumentImpl implements IXSSchemaDocument{
	private Hashtable schemas = null;
	private Hashtable prefixes = null;
	private Hashtable schemaLocations = null;
	private boolean isElementQualified = false;
	private String targetNamespace = null;
	
	public XSSchemaDocumentImpl(){
		schemas = new Hashtable();
		prefixes = new Hashtable();
		schemaLocations = new Hashtable();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.schema.som.IXSSchemaMap#addSchema(java.lang.String, org.gvsig.gpe.schema.som.IXSSchema)
	 */
	public void addSchema(URI uri, IXSSchema schema){
		schemas.put(uri, schema);
		schemaLocations.put(uri, uri.toString());
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.schema.som.IXSSchemaMap#hasSchema(java.lang.String)
	 */
	public boolean hasSchema(URI uri){
		return schemas.get(uri) != null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.schema.som.IXSSchemaMap#getURIs()
	 */
	public Enumeration getURIs(){
		return schemas.keys();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.schema.som.IXSSchemaMap#getSchemas()
	 */
	public Enumeration getSchemas(){
		return schemas.elements();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.schema.som.IXSSchemaMap#getSchema(java.lang.String)
	 */
	public IXSSchema getSchema(URI uri){
		return (IXSSchema)schemas.get(uri);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.schema.som.IXSSchemaMap#remove(java.lang.String)
	 */
	public void remove(URI uri){
		schemas.remove(uri);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.schema.som.IXSSchemaMap#addNamespacePrefix(java.lang.String, java.lang.String)
	 */
	public void addNamespacePrefix(String prefix, String namespaceURI) {
		prefixes.put(prefix, namespaceURI);	
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.schema.som.IXSSchemaMap#getNamespaceURI(java.lang.String)
	 */
	public String getNamespaceURI(String prefix) {
		if (prefixes.get(prefix) != null){
			return (String)prefixes.get(prefix);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.schema.som.IXSSchemaMap#addSchema(java.lang.String)
	 */
	public void addSchema(URI uri) throws SchemaCreationException{
		try {		
			IXSSchema schema = SchemaDocumentBuilder.getInstance().parse(new FileInputStream(uri.getPath()));
			addSchema(uri, schema);
		} catch (Exception e){
			throw new SchemaCreationException(new NotDownloadFileException(e));
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.schema.som.IXSSchemaMap#getElementDeclarationByName(java.lang.String, java.lang.String)
	 */
	public IXSElementDeclaration getElementDeclarationByName(String namespacePrefix, String elementName){
		Enumeration it = getSchemas();
		String namespaceURI = null;
		if (namespacePrefix != null){
			namespaceURI = getNamespaceURI(namespacePrefix);
		}		
		if (namespaceURI == null){
			namespaceURI = getTargetNamespace();
		}
		while (it.hasMoreElements()){
			IXSSchema schema = (IXSSchema)it.nextElement();
			if (schema.getTargetNamespace().compareTo(namespaceURI) == 0){
				return schema.getElementDeclarationByName(namespaceURI, elementName);
			}
		}
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.schema.som.IXSSchemaMap#getElementDeclarationByName(java.lang.String)
	 */
	public IXSElementDeclaration getElementDeclarationByName(String elementName){
		int index = elementName.indexOf(":");
		//If is a qualified name
		if (index == -1){
			return getElementDeclarationByName(
					null,
					elementName);
		}else{
			String prefix = elementName.substring(0, index);
			return getElementDeclarationByName(
					prefix,
					elementName.substring(index + 1,elementName.length()));
		}		
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.schema.som.IXSSchemaMap#isElementQualified()
	 */
	public boolean isElementFormDefault() {
		return isElementQualified;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.schema.som.IXSSchemaMap#setElementQualified(boolean)
	 */
	public void setElementFormDefault(boolean isElementQualified) {
		this.isElementQualified = isElementQualified;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.schema.som.IXSSchemaDocument#addSchemaLocation(java.net.URI, java.lang.String)
	 */
	public void addSchemaLocation(URI uri, String schemaLocation) {
		schemaLocations.put(uri, schemaLocation);		
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.schema.som.IXSSchemaDocument#getSchemaLocation(java.net.URI)
	 */
	public String getSchemaLocation(URI uri) {
		Object obj = schemaLocations.get(uri);
		if (obj != null){
			return (String)obj;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.schema.som.IXSSchemaDocument#hasSchemaLocation(java.net.URI)
	 */
	public boolean hasSchemaLocation(URI uri) {
		return (schemaLocations.get(uri) != null);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.schema.som.IXSSchemaDocument#getTargetNamespace()
	 */
	public String getTargetNamespace() {
		return targetNamespace;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.schema.som.IXSSchemaDocument#setTargetNamespace(java.lang.String)
	 */
	public void setTargetNamespace(String targetNamespace) {
		this.targetNamespace = targetNamespace;		
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.schema.som.IXSSchemaDocument#hasSchemas()
	 */
	public boolean hasSchemas() {
		return (schemas.size() > 0);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.schema.som.IXSSchemaDocument#getPrefixes()
	 */
	public Enumeration getPrefixes() {
		return prefixes.elements();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.xmlschema.som.IXSSchemaDocument#getNamespacePrefix(java.lang.String)
	 */
	public String getNamespacePrefix(String namespaceURI) {
		if (namespaceURI == null){
			return null;
		}
		Enumeration enume = prefixes.elements();
		while (enume.hasMoreElements()){
			Object obj = enume.nextElement();
			if (namespaceURI.equals(obj)){
				return (String)obj;
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.schema.som.IXSSchemaDocument#hasPrefixes()
	 */
	public boolean hasPrefixes() {
		return (prefixes.size() > 0);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.xmlschema.som.IXSSchemaDocument#getTypeByName(java.lang.String, java.lang.String)
	 */
	public IXSTypeDefinition getTypeByName(String targetNamespace,
			String typeName) {
		Enumeration it = getSchemas();
		String namespaceURI = null;
		if (targetNamespace != null){
			namespaceURI = getNamespaceURI(targetNamespace);
		}		
		if (namespaceURI == null){
			namespaceURI = getTargetNamespace();
		}
		while (it.hasMoreElements()){
			IXSSchema schema = (IXSSchema)it.nextElement();
			if (schema.getTargetNamespace().compareTo(namespaceURI) == 0){
				return schema.getTypeByName(namespaceURI, typeName);
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.xmlschema.som.IXSSchemaDocument#getTypeByName(java.lang.String)
	 */
	public IXSTypeDefinition getTypeByName(String typeName) {
		int index = typeName.indexOf(":");
		//If is a qualified name
		if (index == -1){
			return getTypeByName(
					null,
					typeName);
		}else{
			String prefix = typeName.substring(0, index);
			return getTypeByName(
					prefix,
					typeName.substring(index + 1,typeName.length()));
		}		
	}
}

