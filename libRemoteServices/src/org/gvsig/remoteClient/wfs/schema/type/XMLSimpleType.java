package org.gvsig.remoteClient.wfs.schema.type;


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
 * $Id: XMLSimpleType.java 18271 2008-01-24 09:06:43Z jpiera $
 * $Log$
 * Revision 1.4  2007-01-15 13:11:00  csanchez
 * Sistema de Warnings y Excepciones adaptado a BasicException
 *
 * Revision 1.3  2006/12/29 18:05:20  jorpiell
 * Se tienen en cuenta los simpleTypes y los choices, además de los atributos multiples
 *
 * Revision 1.1  2006/12/22 11:25:04  csanchez
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
 * A XS simple data type.
 * 
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 * @author Carlos Sánchez Periñán (sanchez_carper@gva.es)
 * 
 */
public class XMLSimpleType implements IXMLType{
	public static final String STRING = "xs:string";
	public static final String INTEGER = "xs:integer";
	public static final String DOUBLE = "xs:double";
	public static final String FLOAT = "xs:float";
	public static final String BOOLEAN = "xs:boolean";
	public static final String LONG = "xs:long";
	public static final String INT = "xs:int";
	public static final String DECIMAL = "xs:decimal";
	
	private String type = null;
	private String name = null;
	
	public XMLSimpleType(String type) {
		super();
		this.name = type;
		this.type = type;
	}
	
	public XMLSimpleType(String name, String type) {
		super();
		this.name = name;
		this.type = type;
	}

	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.remoteClient.gml.IXMLType#getName()
	 */
	public String getName() {
		return name;
	}
	
	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.remoteClient.gml.IXMLType#getType()
	 */
	public int getType() {
		return IXMLType.SIMPLE;
	}	
	
	/**
	 * @param type The type to set.
	 */
	public void setType(String type) {
		this.type = type;	
	}	

	public Object getJavaType(String value)throws GMLException{
		if (value == null){
			return null;			
		}
		try{
			if (type.equals(STRING)){
				return value;
			}else if (type.equals(INTEGER)){
				return new Integer(value);
			}else if (type.equals(DOUBLE)){
				return new Double(value);
			}else if (type.equals(FLOAT)){
				return new Float(value);
			}else if (type.equals(BOOLEAN)){
				return new Boolean(value);
			}else if (type.equals(DECIMAL)){
				return new Double(value);
			}
		}catch (Exception e){
			/*********************************************************
			 * TIO CAPTURA AQUI "e" que te dirá que typo ha fallado
			 * y metelo en el constructor de Invalid Fromat exception
			 * 
			 *********************************************************/
			throw new GMLInvalidFormatException();
		}
		return value;
	}
	
	
}
