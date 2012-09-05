package org.gvsig.gpe.kml.writer.v21.features;

import java.io.IOException;

import org.gvsig.gpe.kml.utils.Kml2_1_Tags;
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
 * $Id: DocumentWriter.java 361 2008-01-10 08:41:21Z jpiera $
 * $Log$
 * Revision 1.5  2007/05/16 09:30:09  jorpiell
 * the writting methods has to have the errorHandler argument
 *
 * Revision 1.4  2007/05/08 08:22:37  jorpiell
 * Add comments to create javadocs
 *
 * Revision 1.3  2007/05/08 07:53:08  jorpiell
 * Add comments to the writers
 *
 * Revision 1.2  2007/04/20 08:38:59  jorpiell
 * Tests updating
 *
 * Revision 1.1  2007/04/14 16:08:07  jorpiell
 * Kml writing support added
 *
 *
 */
/**
 * This class writes a Document kml tag. Example:
 * <p>
 * <pre>
 * <code>
 * &lt;Document&gt;
 * &lt;Placemark&gt;
 * &lt;name&gt;CDATA example&lt;/name&gt;
 * &lt;description&gt;Description example&lt;/description&gt;
 * &lt;Point&gt;
 * &lt;oordinates&gt;102.595626,14.996729&lt;/coordinates&gt;
 * &lt;/Point&gt;
 * &lt;/Placemark&gt;
 * &lt;/Document&gt;
 * </code>
 * </pre>
 * </p>
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 * @see http://code.google.com/apis/kml/documentation/kml_tags_21.html#document
 */
public class DocumentWriter {

	/**
	 * Writes a Document init tag and its attributes
	 * @param writer
	 * Writer to write the labels
	 * @param handler
	 * The writer handler implementor
	 * @param id
	 * Document id
	 * @param name
	 * Document name
	 * @param description
	 * Document description
	 * @throws IOException
	 */
	public void start(IXmlStreamWriter writer, GPEKmlWriterHandlerImplementor handler, String id, String name, String description) throws IOException{
		handler.getProfile().getFeatureWriter().start(writer, handler, id, Kml2_1_Tags.DOCUMENT);
		if (name != null){
			handler.getProfile().getNameWriter().write(writer, handler, name);
		}
		if (description != null){
			handler.getProfile().getDescriptionWriter().write(writer, handler, description);
		}
	}

	/**
	 * It writes the end Docuemnt tag
	 * @param writer
	 * Writer to write the labels
	 * @param handler
	 * The writer handler implementor
	 * @throws IOException
	 */
	public void end(IXmlStreamWriter writer, GPEKmlWriterHandlerImplementor handler) throws IOException{
		handler.getProfile().getFeatureWriter().end(writer, handler, Kml2_1_Tags.DOCUMENT);
	}
}
