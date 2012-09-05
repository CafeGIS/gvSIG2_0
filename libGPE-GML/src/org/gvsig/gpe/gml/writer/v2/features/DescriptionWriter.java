package org.gvsig.gpe.gml.writer.v2.features;

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
 * $Id: DescriptionWriter.java 350 2008-01-09 12:53:07Z jpiera $
 * $Log$
 * Revision 1.3  2007/05/15 08:11:54  jorpiell
 * The element had to have end
 *
 * Revision 1.2  2007/05/08 10:24:16  jorpiell
 * Add comments to create javadocs
 *
 * Revision 1.1  2007/04/17 07:00:35  jorpiell
 * GML name, descripction and Id tags separated
 *
 *
 */
/**
 * This class writes the gml:description tag. Example:
 * <p>
 * <pre>
 * <code>
 * &lt;gml:description&gt;GML tag desciption&lt;/gml:description&gt;
 * </code>
 * </pre>
 * </p>
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class DescriptionWriter {
	
	/**
	 * It writes a gml:description tag
	 * @param writer
	 * Writer to write the labels
	 * @param handler
	 * The writer handler implementor
	 * @param description
	 * Description to write
	 * @throws IOException
	 */
	public void write(IXmlStreamWriter writer,GPEGmlWriterHandlerImplementor handler, String description) throws IOException{
		if (description != null){
			writer.writeStartElement(GMLTags.GML_DESCRIPTION);
			writer.writeValue(description);		
			writer.writeEndElement();			
		}
	}
}
