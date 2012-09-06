package org.gvsig.remoteClient.wfs.schema;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.gvsig.remoteClient.wfs.schema.type.IXMLType;
import org.gvsig.remoteClient.wfs.schema.type.XMLComplexType;
import org.xmlpull.v1.XmlPullParserException;

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
 * $Id: XMLElementsFactory.java 9451 2006-12-22 11:25:44Z csanchez $
 * $Log$
 * Revision 1.5  2006-12-22 11:25:44  csanchez
 * Nuevo parser GML 2.x para gml's sin esquema
 *
 * Revision 1.2  2006/10/10 12:52:28  jorpiell
 * Soporte para features complejas.
 *
 * Revision 1.1  2006/08/10 12:00:49  jorpiell
 * Primer commit del driver de Gml
 *
 *
 */
/**
 * The XML "element" is an XML tag of a XSD schema and represent
 * a simple "element" or "object". One element has its name and 
 * the data type that contains (and the data)
 * 
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 * @author Carlos Sánchez Periñán (sanchez_carper@gva.es)
 * 
 */
public class XMLElementsFactory {
	private static Hashtable elements = new Hashtable();
	
	public static XMLElement getElement(String name){
		if (name == null){
			return null;
		}
		return (XMLElement)elements.get(name.toUpperCase());
	}
	
	/**
	 * It parses the element and register it
	 * @param schema
	 * Schema parser that contains a "element"
	 * @return
	 */
	public static XMLElement addType(XMLSchemaParser schema){
		XMLElement element;
		try {
			element = new XMLElement(schema);
			if (element.getName() != null){
				elements.put(element.getName().toUpperCase(),element);
			}
			return element;
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Just for degug. It prints all the registred components.
	 */
	public static void printEntities(){
		System.out.println("******* ELEMENTOS :");
		//metemos en un array los elementos de la tabla para mostrarlos
		Object[] keys = elements.keySet().toArray();
		for (int i=0 ; i<elements.size() ; i++){
			XMLElement entity = (XMLElement)elements.get(keys[i]);
			printEntity(entity,0);			
		}
	}
	
	public static void printEntity(XMLElement entity,int level){
		String tab = "";
		for (int i=0 ; i<level ; i++){
			tab = tab + "\t";
		}
		System.out.print(tab + "NAME: " + entity.getName());
		if (entity.getEntityType() != null){
			System.out.print(" TYPE: " + entity.getEntityType().getName() + "\n");
			if (entity.getEntityType().getType() == IXMLType.COMPLEX){
				Map children = ((XMLComplexType)entity.getEntityType()).getSubtypes();
				Set childrenKeys = children.keySet();
				Iterator it = childrenKeys.iterator();
				level++;
				while(it.hasNext()){
					String child = (String)it.next();
					XMLElement eChild = (XMLElement)children.get(child);
					printEntity(eChild,level);	
				}
			}
		}else{
			System.out.print(" TYPE: ERROR \n");
		}
		
	}
	
}
