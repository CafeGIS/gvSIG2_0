package org.gvsig.gpe.gml.writer.v2.geometries;

import java.io.IOException;

import org.gvsig.gpe.gml.utils.GMLTags;
import org.gvsig.gpe.gml.writer.GPEGmlWriterHandlerImplementor;
import org.gvsig.gpe.writer.ICoordinateSequence;
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
 * $Id: PolygonMemberWriter.java 352 2008-01-09 13:51:51Z jpiera $
 * $Log$
 * Revision 1.6  2007/06/07 14:53:30  jorpiell
 * Add the schema support
 *
 * Revision 1.5  2007/05/16 13:00:48  csanchez
 * ActualizaciÃ³n de libGPE-GML
 *
 * Revision 1.4  2007/05/14 11:18:12  jorpiell
 * Add the ErrorHandler to all the methods
 *
 * Revision 1.3  2007/05/08 10:24:16  jorpiell
 * Add comments to create javadocs
 *
 * Revision 1.2  2007/04/14 16:07:30  jorpiell
 * The writer has been created
 *
 * Revision 1.1  2007/04/13 13:16:00  jorpiell
 * Add the multiple geometries
 *
 *
 */
/**
 * It writes a gml:polygonMemberType object. Example:
 * <p>
 * <pre>
 * <code>
 * &lt;polygonMember&gt;
 * &lt;Polygon gid="_877789"&gt;
 * &lt;outerBoundaryIs&gt;
 * &lt;LinearRing&gt;
 * &lt;coordinates&gt;0.0,0.0 100.0,0.0 50.0,100.0 0.0,0.0&lt;/coordinates&gt;
 * &lt;/LinearRing&gt;
 * &lt;/outerBoundaryIs&gt;
 * &lt;/Polygon&gt;
 * &lt;/polygonMember&gt;
 * </code>
 * </pre>
 * </p> 
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class PolygonMemberWriter extends GeometryWriter{
	
	/**
	 * It writes a gml:PolygonMember init tag
	 * @param writer
	 * Writer to write the labels
	 * @param handler
	 * The writer handler implementor
	 * @param id
	 * Geometry ID
	 * @param coords
	 * A coordinates sequence
	 * @param srs
	 * Spatial reference system
	 * @throws IOException
	 */
	public void start(IXmlStreamWriter writer, GPEGmlWriterHandlerImplementor handler, String id, 
			ICoordinateSequence coords, String srs) throws IOException{
		writer.writeStartElement(GMLTags.GML_NAMESPACE_URI, getGeometryName());
		handler.getProfile().getPolygonWriter().start(writer, handler, id, coords, srs);	
	}
	
	/**
	 * It writes a gml:PolygonMember end tag
	 * @param writer
	 * Writer to write the labels
	 * @param handler
	 * The writer handler implementor
	 * @throws IOException
	 */
	public void end(IXmlStreamWriter writer, GPEGmlWriterHandlerImplementor handler) throws IOException{
		handler.getProfile().getPolygonWriter().end(writer, handler);
		writer.writeEndElement();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.writer.v2.geometries.GeometryWriter#getGeometryName()
	 */
	public String getGeometryName() {
		return GMLTags.GML_POLYGONMEMBER.getLocalPart();
	}	
}
