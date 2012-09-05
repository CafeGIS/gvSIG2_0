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
* 2008 Iver T.I.  {{Task}}
*/
 
package org.gvsig.gpe.xml;


/**
 * This class contains the properties for the XML
 * parsers and writers. 
 */
public class XmlProperties{
	/**
	 * XML Schema prefix that have to be used to generate the file for the
	 * formats based on XML.
	 * @see 
	 * <a href="http://www.w3.org/XML/Schema">XML Schema</a> 
	 */
	public static final String DEFAULT_NAMESPACE_PREFIX = "namespacePrefix";
		
	/**
	 * Default namespace of the files based on XML.
	 * @see 
	 * <a href="http://www.w3.org/XML/Schema">XML Schema</a> 
	 */
	public static final String DEFAULT_NAMESPACE_URI= "namespaceURI";
		
	/**
	 * Place where the XML Schema is located.
	 * @see 
	 * <a href="http://www.w3.org/XML/Schema">XML Schema</a> 
	 */
	public static final String XSD_SCHEMA_FILE = "schemaName";
		
	/**
	 * XML number of version.
	 * @see 
	 * <a href=" http://www.w3.org/XML/">XML</a> 
	 */
	public static final String XML_VERSION = "xmlVersion";
	
	/**
	 * Encoding of the generated XML files.
	 * @see 
	 * <a href=" http://www.w3.org/XML/">XML</a> 
	 */
	public static final String XML_ENCODING = "xmlEncoding";
	
	/**
	 * Character to replace the blank spaces in the names that
	 * has to be added like a XML node. On the writing process
	 * all the blank spaces are replaced by this character and on
	 * the reading process this character is replaced by a blank
	 * space.
	 */
	public static final String DEFAULT_BLANC_SPACE = "defaultBlancSpace";
	
	/**
	 * If the parser can download XML Schemas. 
	 */
	public static final String XML_SCHEMA_VALIDATED  = "xmlSchemaValidated";
}

