package org.gvsig.remoteClient.wfs.schema.type;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.gvsig.remoteClient.wfs.schema.XMLElement;
import org.gvsig.remoteClient.wfs.schema.XMLTypesFactory;

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
 * $Id: XMLComplexType.java 9510 2006-12-29 18:05:20Z jorpiell $
 * $Log$
 * Revision 1.3  2006-12-29 18:05:20  jorpiell
 * Se tienen en cuenta los simpleTypes y los choices, además de los atributos multiples
 *
 * Revision 1.1  2006/12/22 11:25:04  csanchez
 * Nuevo parser GML 2.x para gml's sin esquema
 *
 * Revision 1.7  2006/10/31 13:52:37  ppiqueras
 * Mejoras para uso de features complejas
 *
 * Revision 1.6  2006/10/31 12:24:33  jorpiell
 * En caso de que el elemento forme parte de un tipo complejo tiene que tener un enlace al tipo del objeto padre
 *
 * Revision 1.5  2006/10/10 12:52:28  jorpiell
 * Soporte para features complejas.
 *
 * Revision 1.4  2006/10/02 08:33:49  jorpiell
 * Cambios del 10 copiados al head
 *
 * Revision 1.2.2.1  2006/09/19 12:23:15  jorpiell
 * Ya no se depende de geotools
 *
 * Revision 1.3  2006/09/18 12:08:55  jorpiell
 * Se han hecho algunas modificaciones que necesitaba el WFS
 *
 * Revision 1.2  2006/08/29 08:43:13  jorpiell
 * Dos pequeños cambios para tener en cuenta las referencias
 *
 * Revision 1.1  2006/08/10 12:00:49  jorpiell
 * Primer commit del driver de Gml
 *
 *
 */
/**
 * This class implements an XSD complex type
 * 
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 * @author Carlos Sánchez Periñán (sanchez_carper@gva.es)
 * 
 */
public class XMLComplexType implements IXMLType {
	public static int SEQUENCE_TYPE = 0;
	public static int CHOICE_TYPE = 1;
	
	private String type = null;
	private LinkedHashMap baseElements = null;
	private String baseType = null;

	private int attributesType = 0; 
			
	public XMLComplexType(String type) {
		super();
		this.type = type;
		baseElements = new LinkedHashMap();
	}
	
	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.remoteClient.gml.schemas.IXMLType#getType()
	 */
	public int getType() {
		return IXMLType.COMPLEX;
	}
	
	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.remoteClient.gml.schemas.IXMLType#getName()
	 */
	public String getName() {
		return type;
	}
	
	/**
	 * @return Returns the subtypes.
	 */
	public Map getSubtypes() {
		return baseElements;
	}
	
	/**
	 * @param subtypes The subtypes to set.
	 */
	public void addElements(XMLElement element) {
		if (element.getName() != null){
			this.baseElements.put(element.getName(),element);
		}
	}
	
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public XMLElement getAttribute(String name){
		return (XMLElement)baseElements.get(name);
	}

	/**
	 * @return Returns the vElements.
	 */
	public Vector getAttributes() {
		Set keys = baseElements.keySet();
		Iterator it = keys.iterator();
		Vector vector = new Vector();
		while(it.hasNext()){
			vector.add(baseElements.get((String)it.next()));
		}
		if (baseType != null){
			IXMLType type = XMLTypesFactory.getType(baseType);
			if (type != null){
				if (type instanceof XMLComplexType){
					Vector vector2 = ((XMLComplexType)type).getAttributes();
					for (int i=0 ; i<vector2.size() ; i++){
						vector.add(vector2.get(i));
					}
				}
			}
		}
		return vector;
	}

	public int getAttributesType() {
		return attributesType;
	}

	public void setAttributesType(int attributesType) {
		this.attributesType = attributesType;
	}	
	
	/**
	 * @param baseType the baseType to set
	 */
	public void setBaseType(String baseType) {
		this.baseType = baseType;
	}
	
}
