package org.gvsig.gpe.gml.writer.v2.features;

import java.io.IOException;

import org.gvsig.gpe.GPELocator;
import org.gvsig.gpe.GPEManager;
import org.gvsig.gpe.gml.writer.GPEGmlWriterHandlerImplementor;
import org.gvsig.gpe.xml.XmlProperties;
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
 * $Id: ElementWriter.java 350 2008-01-09 12:53:07Z jpiera $
 * $Log$
 * Revision 1.5  2007/06/07 14:53:30  jorpiell
 * Add the schema support
 *
 * Revision 1.4  2007/05/15 09:35:09  jorpiell
 * the tag names cant have blanc spaces
 *
 * Revision 1.3  2007/05/08 10:24:16  jorpiell
 * Add comments to create javadocs
 *
 * Revision 1.2  2007/04/14 16:07:30  jorpiell
 * The writer has been created
 *
 * Revision 1.1  2007/04/12 17:06:44  jorpiell
 * First GML writing tests
 *
 *
 */
/**
 * This class writes the Elemtent tag. One Element
 * is a feature attribute.
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class ElementWriter {
	private static GPEManager gpeManager = GPELocator.getGPEManager();
	
	/**
	 * It writes an Element tag
	 * @param writer
	 * Writer to write the labels
	 * @param handler
	 * The writer handler implementor
	 * @param namespace
	 * Element namespcae
	 * @param name
	 * Element name
	 * @param value
	 * Element value
	 * @throws IOException
	 */
	public void start(IXmlStreamWriter writer, GPEGmlWriterHandlerImplementor handler, String namespace, String name, Object value) throws IOException{
		if (namespace != null){
			writer.writeStartElement(namespace, name);
		}else{
			writer.writeStartElement(gpeManager.getStringProperty(XmlProperties.DEFAULT_NAMESPACE_URI),
					name);
		}
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
	 * @param namespace
	 * Element namespace
	 * @param name
	 * Element name
	 * @throws IOException
	 */
	public void end(IXmlStreamWriter writer, GPEGmlWriterHandlerImplementor hanlder, String namespace, String name) throws IOException{
		writer.writeEndElement();	
	}
}

