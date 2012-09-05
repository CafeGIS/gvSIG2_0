package org.gvsig.gpe.gml.writer.v2.geometries;

import java.io.IOException;

import org.gvsig.gpe.gml.utils.GMLTags;
import org.gvsig.gpe.gml.writer.GPEGmlWriterHandlerImplementor;
import org.gvsig.gpe.xml.stream.IXmlStreamWriter;

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
 * $Id: GeometryMemberWriter.java 350 2008-01-09 12:53:07Z jpiera $
 * $Log$
 * Revision 1.1  2007/05/15 11:55:11  jorpiell
 * MultiGeometry is now supported
 *
 *
 */
/**
 * It writes a gml:geometryMember object. Example:
 * <p>
 * <pre>
 * <code>
 * &lt;geometryMember&gt;
 * &lt;Point gid="P6776"&gt;
 * &lt;coord&gt;&lt;X&gt;50.0&lt;/X&gt;&lt;Y&gt;50.0&lt;/Y&gt;&lt;/coord&gt;
 * &lt;/Point&gt;
 * &lt;/geometryMember&gt;
 * </code>
 * </pre>
 * </p> 
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class GeometryMemberWriter {
	/**
	 * It writes a gml:geometryMember init tag t
	 * @param writer
	 * Writer to write the labels
	 * @param handler
	 * The writer handler implementor
	 * @param id
	 * Geometry ID
	 * @param srs
	 * Spatial reference system
	 * @throws IOException
	 */
	public void startPoint(IXmlStreamWriter writer, GPEGmlWriterHandlerImplementor handler, String id,
			String srs) throws IOException{
		writer.writeStartElement(GMLTags.GML_GEOMETRYMEMBER);	
	}
	
	/**
	 * It writes a gml:geometryMember end tag that contains
	 * a pointMember
	 * @param writer
	 * Writer to write the labels
	 * @param handler
	 * The writer handler implementor
	 */
	public void end(IXmlStreamWriter writer, GPEGmlWriterHandlerImplementor handler) throws IOException{
		writer.writeEndElement();				
	}
	
	
}
