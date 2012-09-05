package org.gvsig.gpe.kml.writer.v21.features;

import java.io.IOException;

import javax.xml.namespace.QName;

import org.gvsig.gpe.kml.writer.GPEKmlWriterHandlerImplementor;
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
 * $Id: ElementWriter.java 361 2008-01-10 08:41:21Z jpiera $
 * $Log$
 * Revision 1.1  2007/05/16 15:54:20  jorpiell
 * Add elements support
 *
 *
 */
/**
 * It parses an element. An element is a tag inside of a 
 * metadata tag
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class ElementWriter {
	
	/**
	 * It writes an Element tag
	 * @param writer
	 * Writer to write the labels
	 * @param handler
	 * The writer handler implementor
	 * @param name
	 * Element name
	 * @param value
	 * Element value
	 * @param type
	 * Element type
	 * @throws IOException
	 */
	public void start(IXmlStreamWriter writer, GPEKmlWriterHandlerImplementor handler, QName name, Object value) throws IOException{
		writer.writeStartElement(name);
		if (value != null){
			writer.writeValue(value.toString());
		}
	}
	
	/**
	 * It writes an end Element tag
	 * @param writer
	 * Writer to write the labels
	 * @param handler
	 * The writer handler implementor
	 * @param name
	 * Element name
	 * @throws IOException
	 */
	public void end(IXmlStreamWriter writer, GPEKmlWriterHandlerImplementor handler) throws IOException{
		writer.writeEndElement();		
	}
	
}
