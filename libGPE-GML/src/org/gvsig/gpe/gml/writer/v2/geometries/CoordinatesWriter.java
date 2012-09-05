package org.gvsig.gpe.gml.writer.v2.geometries;

import java.io.IOException;

import org.gvsig.gpe.gml.utils.GMLTags;
import org.gvsig.gpe.gml.writer.GPEGmlWriterHandlerImplementor;
import org.gvsig.gpe.writer.ICoordinateSequence;
import org.gvsig.gpe.xml.stream.EventType;
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
 * $Id: CoordinatesWriter.java 350 2008-01-09 12:53:07Z jpiera $
 * $Log$
 * Revision 1.8  2007/05/16 13:00:48  csanchez
 * ActualizaciÃ³n de libGPE-GML
 *
 * Revision 1.7  2007/05/14 11:18:12  jorpiell
 * Add the ErrorHandler to all the methods
 *
 * Revision 1.6  2007/05/08 10:24:16  jorpiell
 * Add comments to create javadocs
 *
 * Revision 1.5  2007/04/26 14:40:03  jorpiell
 * Some writer handler methods updated
 *
 * Revision 1.4  2007/04/14 16:07:30  jorpiell
 * The writer has been created
 *
 * Revision 1.3  2007/04/12 17:06:44  jorpiell
 * First GML writing tests
 *
 * Revision 1.2  2007/04/12 11:36:15  jorpiell
 * Added new geometry writers
 *
 * Revision 1.1  2007/04/12 10:23:41  jorpiell
 * Add some writers and the GPEXml parser
 *
 *
 */
/**
 * It writes a gml:CoordinatesType object. Example:
 * <p>
 * <pre>
 * <code>
 * &lt;coordinates&gt;60.0,60.0 60.0,90.0 90.0,90.0&lt;/coordinates&gt;
 * </code>
 * </pre>
 * </p> 
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class CoordinatesWriter {
			
	/**
	 * It writes a gml:coordinates tag list
	 * @param writer
	 * Writer to write the labels
	 * @param handler
	 * The writer handler implementor
	 * @param coords
	 * A coordinates sequence
	 * @throws IOException
	 */
	public void write(IXmlStreamWriter writer, GPEGmlWriterHandlerImplementor handler, 
			ICoordinateSequence coords) throws IOException{
		writer.writeStartElement(GMLTags.GML_COORDINATES);
		writer.startArray(EventType.VALUE_DOUBLE, coords.getSize()*coords.iterator().getDimension());
		double[] buffer = new double[coords.iterator().getDimension()];
	
		while (coords.iterator().hasNext()){
			coords.iterator().next(buffer);		
			writer.writeValue(buffer, 0, buffer.length);
		}	
		writer.endArray();	
		writer.writeEndElement();
	}
}
