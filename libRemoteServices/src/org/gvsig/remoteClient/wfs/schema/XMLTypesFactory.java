package org.gvsig.remoteClient.wfs.schema;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import org.gvsig.remoteClient.wfs.schema.type.GMLGeometryType;
import org.gvsig.remoteClient.wfs.schema.type.IXMLType;
import org.gvsig.remoteClient.wfs.schema.type.XMLComplexType;
import org.gvsig.remoteClient.wfs.schema.type.XMLSimpleType;

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
 * $Id: XMLTypesFactory.java 18290 2008-01-24 17:11:37Z jpiera $
 * $Log$
 * Revision 1.9  2007-02-05 13:20:13  jorpiell
 * Añadidos nuevos métodos en la factoria.
 *
 * Revision 1.8  2006/12/29 17:15:48  jorpiell
 * Se tienen en cuenta los simpleTypes y los choices, además de los atributos multiples
 *
 * Revision 1.7  2006/12/22 11:25:44  csanchez
 * Nuevo parser GML 2.x para gml's sin esquema
 *
 * Revision 1.5  2006/10/11 11:21:00  jorpiell
 * Se escriben los tipos correctamente (no en mayusculas) para que las traducciones funcionen
 *
 * Revision 1.4  2006/10/10 12:52:28  jorpiell
 * Soporte para features complejas.
 *
 * Revision 1.3  2006/10/02 08:33:49  jorpiell
 * Cambios del 10 copiados al head
 *
 * Revision 1.1.2.1  2006/09/19 12:23:15  jorpiell
 * Ya no se depende de geotools
 *
 * Revision 1.2  2006/09/18 12:08:55  jorpiell
 * Se han hecho algunas modificaciones que necesitaba el WFS
 *
 * Revision 1.1  2006/08/10 12:00:49  jorpiell
 * Primer commit del driver de Gml
 *
 *
 */
/**
 * GML and XML types factory. All the types that are
 * located in the schemas must be registered here
 * 
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 * @author Carlos Sánchez Periñán (sanchez_carper@gva.es)
 * 
 */
public class XMLTypesFactory {
	private static Hashtable types = new Hashtable();
		
	static{
		types.put(XMLSimpleType.STRING.toUpperCase(),new XMLSimpleType(XMLSimpleType.STRING));
		types.put(XMLSimpleType.INTEGER.toUpperCase(),new XMLSimpleType(XMLSimpleType.INTEGER));
		types.put(XMLSimpleType.INT.toUpperCase(),new XMLSimpleType(XMLSimpleType.INT));
		types.put(XMLSimpleType.DOUBLE.toUpperCase(),new XMLSimpleType(XMLSimpleType.DOUBLE));
		types.put(XMLSimpleType.FLOAT.toUpperCase(),new XMLSimpleType(XMLSimpleType.FLOAT));
		types.put(XMLSimpleType.BOOLEAN.toUpperCase(),new XMLSimpleType(XMLSimpleType.BOOLEAN));
		types.put(XMLSimpleType.LONG.toUpperCase(),new XMLSimpleType(XMLSimpleType.LONG));
		types.put(XMLSimpleType.DECIMAL.toUpperCase(),new XMLSimpleType(XMLSimpleType.DECIMAL));
		types.put(GMLGeometryType.POINT.toUpperCase(),new GMLGeometryType(GMLGeometryType.POINT));
		types.put(GMLGeometryType.MULTIPOINT.toUpperCase(),new GMLGeometryType(GMLGeometryType.MULTIPOINT));
		types.put(GMLGeometryType.LINE.toUpperCase(),new GMLGeometryType(GMLGeometryType.LINE));
		types.put(GMLGeometryType.MULTILINE.toUpperCase(),new GMLGeometryType(GMLGeometryType.MULTILINE));
		types.put(GMLGeometryType.POLYGON.toUpperCase(),new GMLGeometryType(GMLGeometryType.POLYGON));
		types.put(GMLGeometryType.MULTIPOLYGON.toUpperCase(),new GMLGeometryType(GMLGeometryType.MULTIPOLYGON));
		types.put(GMLGeometryType.GEOMETRY.toUpperCase(),new GMLGeometryType(GMLGeometryType.GEOMETRY));
		types.put(GMLGeometryType.MULTIGEOMETRY.toUpperCase(),new GMLGeometryType(GMLGeometryType.MULTIGEOMETRY));
		types.put(GMLGeometryType.SURFACE.toUpperCase(),new GMLGeometryType(GMLGeometryType.SURFACE));
		types.put(GMLGeometryType.MULTISURFACE.toUpperCase(),new GMLGeometryType(GMLGeometryType.MULTISURFACE));
	}
	
	/**
	 * Gets a type by name
	 * @param type
	 * Type name
	 * @return
	 */
	public static IXMLType getType(String type){
		IXMLType xmlType = (IXMLType)types.get(type.toUpperCase());
		if (xmlType == null){
			xmlType = getTypeWithOutNameSpace(type);			
		}
		return xmlType;		
	}
	
	/**
	 * This method is used to solve some mistakes. It doesn't
	 * consider the namespace
	 * @param type
	 * @return
	 */
	public static IXMLType getTypeWithOutNameSpace(String type){
		Set keys = types.keySet();
		Iterator it = keys.iterator();
		String[] typeParts = type.split(":");
		String typeAux = type;
		if (typeParts.length > 1){
			typeAux = typeParts[1];
		}
		while(it.hasNext()){
			String key = (String)it.next();
			String[] parts = key.split(":");
			if (parts.length == 1){
				if (parts[0].compareTo(typeAux.toUpperCase())==0){
					return (IXMLType)types.get(key);
				}
			}else if (parts.length > 1){
				if (parts[parts.length-1].compareTo(typeAux.toUpperCase())==0){
					return (IXMLType)types.get(key);
				}
			}
		}
		return null;
	}
	
	/**
	 * Adds a new type
	 * @param type
	 * type to add 
	 */
	private static void addType(IXMLType type){
		types.put(type.getName().toUpperCase(),type);
	}	
	
	/**
	 * Adds a complex type
	 * @param nameSpace
	 * NameSpace where is located
	 * @param name
	 * Complex type name
	 * @return
	 */
	public static XMLComplexType addComplexType(String nameSpace,String name){
		XMLComplexType complexType = new XMLComplexType(name);
		addType(complexType);
		return complexType;
	}
	
	public static XMLSimpleType addSimpleType(String name,String type){
		XMLSimpleType simpleType = new XMLSimpleType(name,type);
		types.put(name.toUpperCase(),simpleType);
		return simpleType;
	}

	/**
	 * Just for degug. It prints all the registred components.
	 */
	public static void printTypes(){
		System.out.println("*** TIPOS ***");
		Object[] keys = types.keySet().toArray();
		for (int i=0 ; i<types.size() ; i++){
			IXMLType type = (IXMLType)types.get(keys[i]);
			System.out.print("NAME: " + type.getName());
			System.out.print(" TYPE: " + type.getType() + "\n");
		}
	}
	
}
