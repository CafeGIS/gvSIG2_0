package org.gvsig.xmlschema.som;

import java.net.URI;
import java.util.Enumeration;

import org.gvsig.xmlschema.exceptions.SchemaCreationException;

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
 * $Id: IXSSchemaDocument.java 175 2007-11-20 16:14:37Z jpiera $
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
 * Revision 1.2  2007/06/08 13:00:40  jorpiell
 * Add the targetNamespace to the file
 *
 * Revision 1.1  2007/06/07 14:54:13  jorpiell
 * Add the schema support
 *
 *
 */
/**
 * This interface represents a XML file. It could has some
 * associated schemas.
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public interface IXSSchemaDocument {
	
	/**
	 * @return <true> if the document has a schema
	 */
	public boolean hasSchemas();
	
	/**
	 * @return <true> if the document has namespace prefixes
	 */
	public boolean hasPrefixes();
	
	/**
	 * Adds a new Schema from a URI. It downloads the file
	 * and creates the schema
	 * @param uri
	 * URI tha contains the schema
	 * @param schema
	 * Schema to add
	 */
	public void addSchema(URI uri) throws SchemaCreationException;	
	
	/**
	 * Adds a new Schema from a URI
	 * @param uri
	 * URI tha contains the schema
	 * @param schema
	 * Schema to add
	 */
	public void addSchema(URI uri, IXSSchema schema);
	
	/**
	 * Add a schema location.
	 * @param uri
	 * @param schemaLocation
	 */
	public void addSchemaLocation(URI uri, String schemaLocation);
	
	/**
	 * Gets the schema location
	 * @param uri
	 * Schema URI
	 * @return
	 * The schema location
	 */
	public String getSchemaLocation(URI uri);
	
	/**
	 * Return <true> if the schema URI has a schema location
	 * @param uri
	 * @return
	 */
	public boolean hasSchemaLocation(URI uri);

	/**
	 * Return if the map has a schema
	 * @param uri
	 * URI tha contains the schema
	 * @return
	 * <true> if exists or <false>
	 */
	public boolean hasSchema(URI uri);

	/**
	 * @return
	 * A lsit of the schemas URI's
	 */
	public Enumeration getURIs();

	/**
	 * @return
	 * A list of schemas
	 */
	public Enumeration getSchemas();

	/**
	 * @return
	 * A list of namespace prefixes
	 */
	public Enumeration getPrefixes();
	
	/**
	 * Get a schema from a URI
	 * @param uri
	 * URI where the schema is
	 * @return
	 * A Schema
	 */
	public IXSSchema getSchema(URI uri);

	/**
	 * Remove a schema from a URI
	 * @param uri
	 * Schema URI
	 */
	public void remove(URI uri);

	/**
	 * Adds a prefix from a namespace that is used into the
	 * XML file
	 * @param prefix
	 * Napescape prefix
	 * @param namespaceURI
	 * Napespace URI
	 */
	public void addNamespacePrefix(String prefix, String namespaceURI);

	/**
	 * Return the namespace URI from a prefix
	 * @param prefix
	 * Namespace prefix
	 * @return
	 * A namespace URI
	 */
	public String getNamespaceURI(String prefix);
	
	/**
	 * It retusn the namespace prefix
	 * @param namespaceURI
	 * Namespace URI
	 * @return
	 * The namespace prefix
	 */
	public String getNamespacePrefix(String namespaceURI);
	
	/**
	 * Get a element from a qualified name
	 * @param namespacePrefix
	 * Namespace prefix
	 * @param elementName
	 * Local name
	 * @return
	 * A SXD element
	 * @throws TypeNotFoundException 
	 */
	public IXSElementDeclaration getElementDeclarationByName(String namespacePrefix, String elementName) ;
	
	/**
	 * Get a element from a qualified name
	 * @param elementName
	 * Namespace prefix and local name
	 * @return
	 * A SXD element
	 * @throws TypeNotFoundException 
	 */
	public IXSElementDeclaration getElementDeclarationByName(String elementName);
		
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
	 * Search a XML schema type definition by name
	 * @param typeName
	 * XML schema type name
	 * @return
	 * A XML schema type definition
	 * @throws TypeNotFoundException
	 */
	public IXSTypeDefinition getTypeByName(String typeName);
	
	/**
	 * @return <true> if the element names are qualified
	 */
	public boolean isElementFormDefault();
	
	/**
	 * Set if the element names are qualified
	 * @param isElementQualified
	 * <true> if the element names are qualified
	 */
	public void setElementFormDefault(boolean isQualified);
	
	/**
	 * @return the targetNamespace for the file. It will be used
	 * for the unqualified names
	 */
	public String getTargetNamespace();
	
	/**
	 * Set the targetNamespace
	 * @param targetNamespace
	 * TargetNamespace to set
	 */
	public void setTargetNamespace(String targetNamespace);
}
