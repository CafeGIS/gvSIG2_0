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
 * $Id: GMLGeometryType.java 18290 2008-01-24 17:11:37Z jpiera $
 * $Log$
 * Revision 1.2  2007-02-05 13:20:13  jorpiell
 * Añadidos nuevos métodos en la factoria.
 *
 * Revision 1.1  2006/12/22 11:25:04  csanchez
 * Nuevo parser GML 2.x para gml's sin esquema
 *
 * Revision 1.2  2006/10/11 11:21:00  jorpiell
 * Se escriben los tipos correctamente (no en mayusculas) para que las traducciones funcionen
 *
 * Revision 1.1  2006/08/10 12:00:49  jorpiell
 * Primer commit del driver de Gml
 *
 *
 */
/**
 * All the GML geometries must be here
 * 
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 * @author Carlos Sánchez Periñán (sanchez_carper@gva.es)
 * 
 */
public class GMLGeometryType implements IXMLType {
	private String type = null;
	
	public static final String POINT = "gml:PointPropertyType";
	public static final String MULTIPOINT = "gml:MultiPointPropertyType";
	public static final String LINE = "gml:LineStringPropertyType";
	public static final String MULTILINE = "gml:MultiLineStringPropertyType";	
	public static final String POLYGON = "gml:PolygonPropertyType";
	public static final String MULTIPOLYGON = "gml:MultiPolygonPropertyType";
	public static final String GEOMETRY = "gml:GeometryPropertyType";
	public static final String MULTIGEOMETRY = "gml:MultiGeometryPropertyType";
	public static final String SURFACE = "gml:SurfacePropertyType";
	public static final String MULTISURFACE = "gml:MultiSurfacePropertyType";
  
	public GMLGeometryType(String type) {
		super();		
		this.type = type;
	}
	
	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.remoteClient.gml.model.IXMLType#getType()
	 */
	public int getType() {
		return IXMLType.GML_GEOMETRY;
	}
	
	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.remoteClient.gml.model.IXMLType#getName()
	 */
	public String getName() {
		return type;
	}


}
