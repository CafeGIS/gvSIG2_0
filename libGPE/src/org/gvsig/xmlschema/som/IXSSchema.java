package org.gvsig.xmlschema.som;

import java.io.OutputStream;
import java.util.Collection;

import org.gvsig.xmlschema.exceptions.SchemaWrittingException;
import org.gvsig.xmlschema.utils.SchemaObjectsMapping;
import org.w3c.dom.Document;

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
 * $Id: IXSSchema.java 164 2007-07-02 10:00:46Z jorpiell $
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
 * Revision 1.6  2007/06/08 11:35:16  jorpiell
 * IXSSchema interface updated
 *
 * Revision 1.5  2007/06/07 14:54:13  jorpiell
 * Add the schema support
 *
 * Revision 1.4  2007/05/30 12:53:33  jorpiell
 * Not used libraries deleted
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
 * This interface represents a XML schema. XML Schemas 
 * express shared vocabularies and allow machines to 
 * carry out rules made by people. They provide a means 
 * for defining the structure, content and semantics of 
 * XML documents. 
 * @see http://www.w3.org/XML/Schema
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public interface IXSSchema {
	
	/**
	 * @return The schema target namespace.
	 */
	public String getTargetNamespace();
	
	/**
	 * @return The prefix for the target namespace.
	 */
	public String getTargetNamespacePrefix();
	
	/**
	 * It retusn a namespace prefix
	 * @param namespaceURI
	 * Namespace URI
	 * @return
	 * The namespace prefix
	 */
	public String getNamespacePrefix(String namespaceURI);
	
	/**
	 * @return the DOM document that contains the
	 * schema information. It can be used by others 
	 * applications to modify the schema
	 */
	public Document getDocument();
	
	/**
	 * @return the XML schema elements
	 * @throws TypeNotFoundException
	 */
	public Collection getElementDeclarations();
	
	/**
	 * Search a XML schema element by name
	 * @param targetNamespace
	 * Namespace to seach the element
	 * @param elementName
	 * Element name
	 * @return
	 * A XML schema element
	 * @throws TypeNotFoundException
	 */
	public IXSElementDeclaration getElementDeclarationByName(String targetNamespace, String elementName);
	
	/**
	 * @return the XML schema type definitions
	 * @throws TypeNotFoundException
	 */
	public Collection getTypeDefinitions();
	
	/**
	 * Search a XML schema type definition by name
	 * @param targetNamespace
	 * Namespace to seach the element
	 * @param typeName
	 * XML schema type name
	 * @return
	 * A XML schema type definition
	 * @throws TypeNotFoundException
	 */
	public IXSTypeDefinition getTypeByName(String targetNamespace, String typeName);
	
	/**
	 * It writes the schema to one OutputStream
	 * @param os
	 * OutputStream to write the file
	 * @throws SchemaWrittingException
	 */
	public void write(OutputStream os) throws SchemaWrittingException ;
			
	/**
	 * Add a new XML schema element
	 * @param name
	 * Element name
	 * @param type
	 * Element type
	 * @param substitutionGroup
	 * A top-level element definition.
	 * @return
	 * A XML schema element
	 */
	public IXSElementDeclaration addElement(String name, String type, String substitutionGroup);
	
	/**
	 * Add a new XML schema element
	 * @param name
	 * Element name
	 * @param type
	 * Element type
	 * @return
	 * A XML schema element
	 */
	public IXSElementDeclaration addElement(String name, String type);
	
	/**
	 * Add a new XML schema Complex type
	 * @param name
	 * Type name
	 * @param type
	 * See the IXSComplexType interface for possible values
	 * @param contentType
	 * A complex content or a simple content
	 * See the IXSContentType for possible values
	 * @param contentTypeRestriction
	 * A extension or a restriction
	 * @return
	 * A xML schema complex type
	 */
	public IXSComplexTypeDefinition addComplexType(String name, String type,
			String contentType, String conteTypeRestriction);
	
	/**
	 * @return
	 * Return the mappings for all the schema objects that have
	 * a class that represents them.
	 */	
	public SchemaObjectsMapping getObjectsMapping();

	
}
