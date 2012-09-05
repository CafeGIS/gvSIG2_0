package org.gvsig.xmlschema.utils;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import org.gvsig.xmlschema.som.IXSNode;
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
 * $Id: SchemaObjectsMapping.java 157 2007-06-22 12:22:53Z jorpiell $
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
 * Revision 1.1  2007/06/07 14:54:13  jorpiell
 * Add the schema support
 *
 * Revision 1.1  2007/05/30 12:25:48  jorpiell
 * Add the element collection
 *
 *
 */
/**
 * A mapping between a xml node name and a class that
 * implements it.
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class SchemaObjectsMapping {
	private Map types = null;
	private IXSSchema schema = null;
	
	public SchemaObjectsMapping(IXSSchema schema) {
		super();
		this.schema = schema;
		types = new Hashtable();		
	}	
	
	/**
	 * @return the supported types
	 */
	public Set getTypes(){
		return types.keySet();
	}
	
	/**
	 * Add a new type and creates the object that will be used to 
	 * envolve the element
	 * @param type
	 * Type name that will be found in the XML file
	 * @param clazz
	 * Class used to create the object that envolves the element
	 * @throws TypeNotFoundException
	 */
	public void addType(String type, Class clazz){
		Class[] parameterTypes = {IXSSchema.class};
		Object[] initargs = {schema};
		IXSNode node = null;
		try {
			 node = (IXSNode)clazz.getConstructor(parameterTypes).newInstance(initargs);
		} catch (Exception e){
			e.printStackTrace();			
		}
		types.put(type, node);
	}
	
	/**
	 * Returns the node
	 * @param type
	 * Type name
	 * @return
	 * A node with a element inside 
	 */
	public IXSNode getNode(String type, Element element){
		Object obj = types.get(type);
		if (obj != null){
			IXSNode node = (IXSNode)obj;
			node.setElement(element);
			return node;
		}
		return null;
	}
}
