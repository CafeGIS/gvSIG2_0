package org.gvsig.xmlschema.utils;

import org.gvsig.xmlschema.som.IXSElementDeclaration;

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
 * $Id: TypeUtils.java 151 2007-06-14 16:15:05Z jorpiell $
 * $Log$
 * Revision 1.1  2007/06/14 16:15:03  jorpiell
 * builds to create the jars generated and add the schema code to the libGPEProject
 *
 * Revision 1.1  2007/06/14 13:50:07  jorpiell
 * The schema jar name has been changed
 *
 * Revision 1.1  2007/06/07 14:54:13  jorpiell
 * Add the schema support
 *
 *
 */
/**
 * A class with some utils to manage the types
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class TypeUtils {
	public static final String STRING = "string";
	public static final String INTEGER = "integer";
	public static final String DOUBLE = "double";
	public static final String LONG = "long";
	public static final String BOOLEAN = "boolean";
	public static final String DURATION = "duration";
	public static final String DATETIME = "dateTime";
	public static final String TIME = "time";
	public static final String DATE = "date";
	public static final String GYEARMONTH = "gYearMonth";
	public static final String GYEAR = "gYear";
	public static final String GMONTHDAY = "gMonthDay";
	public static final String GDAY = "gDay";
	public static final String GMONTH = "gMonth";
	public static final String BASE64BINARY = "base64Binary";
	public static final String HEXBINARY = "hexBinary";
	public static final String ANYURI = "anyUri";
	public static final String QNAME = "QName";
	public static final String NOTATION = "NOTATION";
	public static final String DECIMAL = "decimal";
	public static final String NONPOSITIVEINTEGER = "nonPositiveInteger";
	public static final String NONNEGATIVEINTEGER = "nonNegativeInteger";
	public static final String NEGATIVEINTEGER = "negativeInteger";
	public static final String POSITIVEINTEGER = "positiveInteger";
	public static final String INT = "int";
	public static final String INSIGNEDSHORT = "unsignedLong";
	public static final String SHORT = "short";
	public static final String UNSIGNEDINT = "unsignedInt";
	public static final String BYTE = "byte";
	public static final String UNSIGNEDSHORT = "unsignedShort";
	public static final String UNSIGNEDBYTE = "unsignedByte";
	public static final String NORMALIZEDSTRING = "normalizedString";
	public static final String TOKEN = "token";
	public static final String LANGUAGE = "language";
	public static final String NAME = "Name";
	public static final String NMTOKEN = "NMTOKEN";
	public static final String NCNAME = "NCName";
	public static final String ID = "ID";
	public static final String IDREF = "IDREF";
	public static final String ENTITY = "ENTITY";

	/**
	 * Return the XML schema simple type from a class
	 * @param clazz
	 * Clazz to compare the name
	 * @return
	 * Xml schema simple type
	 */
	public static String getType(Class clazz){
		if (clazz.getName().compareTo(String.class.getName()) == 0){
			return STRING;
		}else if (clazz.getName().compareTo(Integer.class.getName()) == 0){
			return INTEGER;
		}else if (clazz.getName().compareTo(Double.class.getName()) == 0){
			return DOUBLE;
		}else if (clazz.getName().compareTo(Long.class.getName()) == 0){
			return LONG;
		}else if (clazz.getName().compareTo(Boolean.class.getName()) == 0){
			return BOOLEAN;
		}
		return STRING;
	}
	
	/**
	 * Return the XML schema simple type from a class. The 
	 * name contains the Xlink namespace
	 * @param clazz
	 * Clazz to compare the name
	 * @return
	 * Xml schema simple type with the xlink prefix
	 */
	public static String getXSType(Class clazz){
		return SchemaTags.XS_NS + ":" + getType(clazz);
	}

	public static Object getValue(IXSElementDeclaration element, String value) {
		if ((element == null) || (element.getTypeName() == null)){
			return value;
		}
		String typeName = element.getTypeName();
		typeName = typeName.substring(typeName.indexOf(":") + 1,typeName.length());
		if (typeName.compareTo(INTEGER) == 0){
			return new Integer(Integer.parseInt(value));
		}else if (typeName.compareTo(BOOLEAN) == 0){
			return new Boolean(Boolean.getBoolean(value));
		}
		return value;
	}
	
	
}
