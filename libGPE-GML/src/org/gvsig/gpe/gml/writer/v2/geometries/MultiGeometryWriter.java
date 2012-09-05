package org.gvsig.gpe.gml.writer.v2.geometries;

import org.gvsig.gpe.gml.utils.GMLTags;

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
 * $Id: MultiGeometryWriter.java 352 2008-01-09 13:51:51Z jpiera $
 * $Log$
 * Revision 1.1  2007/05/15 11:55:11  jorpiell
 * MultiGeometry is now supported
 *
 *
 */
/**
 * It writes a gml:MultiGeometry object. Example:
 * <p>
 * <pre>
 * <code> 
 * &lt;MultiGeometry gid="c731" srsName="http://www.opengis.net/gml/srs/epsg.xml#4326"&gt;
 * &lt;geometryMember&gt;
 * &lt;Point gid="P6776"&gt;
 * &lt;coord&gt;&lt;X&gt;50.0&lt;/X&gt;&lt;Y&gt;50.0&lt;/Y&gt;&lt;/coord&gt;
 * &lt;/Point&gt;
 * &lt;/geometryMember&gt;
 * &lt;geometryMember&gt;
 * &lt;LineString gid="L21216"&gt;
 * &lt;coord&gt;&lt;X&gt;0.0&lt;/X&gt;&lt;Y&gt;0.0&lt;/Y&gt;&lt;/coord&gt;
 * &lt;coord&gt;&lt;X&gt;0.0&lt;/X&gt;&lt;Y&gt;50.0&lt;/Y&gt;&lt;/coord&gt;
 * &lt;coord&gt;&lt;X&gt;100.0&lt;/X&gt;&lt;Y&gt;50.0&lt;/Y&gt;&lt;/coord&gt;
 * &lt;/LineString&gt;
 * &lt;/geometryMember&gt;
 * &lt;geometryMember&gt;
 * &lt;Polygon gid="_877789"&gt;
 * &lt;outerBoundaryIs&gt;
 * &lt;LinearRing&gt;
 * &lt;coordinates&gt;0.0,0.0 100.0,0.0 50.0,100.0 0.0,0.0&lt;/coordinates&gt;
 * &lt;/LinearRing&gt;
 * &lt;/outerBoundaryIs&gt;
 * &lt;/Polygon&gt;
 * &lt;/geometryMember&gt;
 * &lt;/MultiGeometry&gt;
 * </code>
 * </pre>
 * </p> 
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class MultiGeometryWriter extends GeometryWriter{
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.writer.v2.geometries.GeometriesWriter#getGeometryName()
	 */
	public String getGeometryName() {
		return GMLTags.GML_MULTIGEOMETRY.getLocalPart();
	}
}
