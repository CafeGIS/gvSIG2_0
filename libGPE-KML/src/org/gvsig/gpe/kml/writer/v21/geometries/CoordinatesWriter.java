package org.gvsig.gpe.kml.writer.v21.geometries;

import java.io.IOException;

import org.gvsig.gpe.kml.utils.Kml2_1_Tags;
import org.gvsig.gpe.kml.writer.GPEKmlWriterHandlerImplementor;
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
 * $Id: CoordinatesWriter.java 361 2008-01-10 08:41:21Z jpiera $
 * $Log$
 * Revision 1.3  2007/05/16 09:30:09  jorpiell
 * the writting methods has to have the errorHandler argument
 *
 * Revision 1.2  2007/05/08 07:53:08  jorpiell
 * Add comments to the writers
 *
 * Revision 1.1  2007/04/14 16:08:07  jorpiell
 * Kml writing support added
 *
 *
 */
/**
 * This class writes a coordinates Kml tag. Example:
 * <p>
 * <pre>
 * <code>
 * &lt;coordinates&gt;60.0,60.0 60.0,90.0 90.0,90.0&lt;/coordinates&gt;
 * </code>
 * </pre>
 * </p> 
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 * @see http://code.google.com/apis/kml/documentation/kml_tags_21.html#coordinates
 */
public class CoordinatesWriter {
	
	/**
	 * It writes an array of coordinates written using
	 * the kml coordinates tag
	 * @param writer
	 * Writer to write the labels
	 * @param handler
	 * The writer handler implementor
	 * @param coords
	 * A coordinates iterator. 
	 * @throws IOException
	 */
	public void write(IXmlStreamWriter writer, GPEKmlWriterHandlerImplementor handler,
			ICoordinateSequence coords) throws IOException{
		writer.writeStartElement(Kml2_1_Tags.COORDINATES);
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
