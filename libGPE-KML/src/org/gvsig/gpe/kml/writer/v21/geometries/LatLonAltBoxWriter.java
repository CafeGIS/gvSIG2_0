package org.gvsig.gpe.kml.writer.v21.geometries;

import java.io.IOException;

import org.gvsig.gpe.kml.utils.Kml2_1_Tags;
import org.gvsig.gpe.kml.writer.GPEKmlWriterHandlerImplementor;
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
 * $Id: LatLonAltBoxWriter.java 361 2008-01-10 08:41:21Z jpiera $
 * $Log$
 * Revision 1.3  2007/05/16 09:30:09  jorpiell
 * the writting methods has to have the errorHandler argument
 *
 * Revision 1.2  2007/05/09 08:36:23  jorpiell
 * Add the bbox to the layer
 *
 * Revision 1.1  2007/05/08 07:53:08  jorpiell
 * Add comments to the writers
 *
 *
 */
/**
 * This class writes a LatLonAltBox Kml tag. Example:
 * <p>
 * <pre>
 * <code> 
 * &lt;LatLonAltBox&gt;
 * &lt;north&gt;43.374&lt;/north&gt;
 * &lt;south&gt;42.983&lt;/south&gt;
 * &lt;east&gt;-0.335&lt;/east&gt;
 * &lt;west&gt;-1.423&lt;/west&gt;
 * &lt;minAltitude&gt;0&lt;/minAltitude&gt;
 * &lt;maxAltitude&gt;0&lt;/maxAltitude&gt;
 * &lt;/LatLonAltBox&gt;
 * </code>
 * </pre>
 * </p> 
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 * @see http://code.google.com/apis/kml/documentation/kml_tags_21.html#latlonaltbox
 */
public class LatLonAltBoxWriter {
	
	/**
	 * It writes the LatLonAltBox init tag and its fields
	 * @param writer
	 * Writer to write the labels
	 * @param handler
	 * The writer handler implementor
	 * @param coords
	 * A coordinates iterator. 
	 * @throws IOException
	 */
	public void start(IXmlStreamWriter writer, GPEKmlWriterHandlerImplementor handler, 
			ICoordinateSequence coords) throws IOException{
		double[] minBuffer = new double[coords.iterator().getDimension()];
		double[] maxBuffer = new double[coords.iterator().getDimension()];
		//TODO what happends if the length is < 3!!! If the iterator only has 1 element
		if (coords.iterator().hasNext()){
			coords.iterator().next(minBuffer);
		}
		if (coords.iterator().hasNext()){
			coords.iterator().next(maxBuffer);
		}
		writer.writeStartElement(Kml2_1_Tags.LATLONALTBOX);
		writer.writeStartElement(Kml2_1_Tags.NORTH);
		writer.writeValue(maxBuffer[1]);
		writer.writeEndElement();
		writer.writeStartElement(Kml2_1_Tags.SOUTH);
		writer.writeValue(minBuffer[1]);
		writer.writeEndElement();
		writer.writeStartElement(Kml2_1_Tags.EAST);
		writer.writeValue(maxBuffer[0]);
		writer.writeEndElement();
		writer.writeStartElement(Kml2_1_Tags.WEST);
		writer.writeValue(minBuffer[0]);
		writer.writeEndElement();
		writer.writeStartElement(Kml2_1_Tags.MINALTITUDE);
		if (minBuffer.length > 2){
			writer.writeValue(minBuffer[2]);
		}else{
			writer.writeValue(0.0);
		}	
		writer.writeEndElement();
		writer.writeStartElement(Kml2_1_Tags.MAXALTITUDE);
		if (maxBuffer.length > 2){
			writer.writeValue(maxBuffer[2]);
		}else{
			writer.writeValue(0.0);
		}
		writer.writeEndElement();
	}
	
	/**
	/**
	 * It writes the LatLonAltBox end tag and its fields
	 * @param writer
	 * Writer to write the labels
	 * @param handler
	 * The writer handler implementor
	 * @throws IOException
	 */
	public void end(IXmlStreamWriter writer, GPEKmlWriterHandlerImplementor hanlder) throws IOException{
		writer.writeEndElement();
	}
}
