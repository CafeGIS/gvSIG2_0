package org.gvsig.gpe.gml.writer.sfp0.geometries;

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
 * $Id$
 * $Log$
 *
 */
/**
 * It writes a gml:innerBoundaryType object. Example:
* <p>
* <pre>
* <code>
* &lt;interior&gt;
* &lt;LinearRing&gt;
* &lt;coordinates&gt;10.0,10.0 10.0,40.0 40.0,40.0 40.0,10.0 10.0,10.0&lt;/coordinates&gt;
* &lt;/LinearRing&gt;
* &lt;/interior&gt;
* </code>
* </pre>
* </p> 
* @author Jorge Piera LLodrá (jorge.piera@iver.es)
*/
public class InnerBoundaryIsWriter extends org.gvsig.gpe.gml.writer.v2.geometries.InnerBoundaryIsWriter {
	/**
	 * It writes a gml:interior init tag
	 * @param writer
	 * Writer to write the labels
	 * @param handler
	 * The writer handler implementor
	 * @param coords
	 * A coordinates sequence
	 * @throws IOException
	 */
	public void start(IXmlStreamWriter writer, GPEGmlWriterHandlerImplementor handler, 
			ICoordinateSequence coords) throws IOException{
		writer.writeStartElement(GMLTags.GML_INTERIOR);	
		handler.getProfile().getLinearRingWriter().start(writer, handler, null, coords, null);	
	}
	
	/**
	 * It writes a gml:interior end tag
	 * @param writer
	 * Writer to write the labels
	 * @param handler
	 * The writer handler implementor
	 * @throws IOException
	 */
	public void end(IXmlStreamWriter writer, GPEGmlWriterHandlerImplementor handler) throws IOException{
		handler.getProfile().getLinearRingWriter().end(writer, handler);
		writer.writeEndElement();	
	}
}
