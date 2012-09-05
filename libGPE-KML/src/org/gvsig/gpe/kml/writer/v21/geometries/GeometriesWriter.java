package org.gvsig.gpe.kml.writer.v21.geometries;

import java.io.IOException;

import javax.xml.namespace.QName;

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
 * $Id: GeometriesWriter.java 361 2008-01-10 08:41:21Z jpiera $
 * $Log$
 * Revision 1.4  2007/06/07 14:53:59  jorpiell
 * Add the schema support
 *
 * Revision 1.3  2007/05/16 09:30:09  jorpiell
 * the writting methods has to have the errorHandler argument
 *
 * Revision 1.2  2007/05/08 07:53:08  jorpiell
 * Add comments to the writers
 *
 * Revision 1.1  2007/05/02 11:46:50  jorpiell
 * Writing tests updated
 *
 *
 */
/**
 * A Geometry element is an abstract element and cannot be used
 * directly in a KML file. It provides a placeholder object for
 * all derived Geometry objects.
 * <br>
 * This class has methods to write the common parts. 
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 * @see http://code.google.com/apis/kml/documentation/kml_tags_21.html#geometry
 */
public class GeometriesWriter {

	/**
	 * It writes the geometry init tag and its attributes
	 * @param writer
	 * Writer to write the labels
	 * @param handler
	 * The writer handler implementor
	 * Geometry tag name
	 * @param id
	 * Geometry id
	 * @throws IOException
	 */
	public void startGeometry(IXmlStreamWriter writer, GPEKmlWriterHandlerImplementor handler, QName tagName,String id) throws IOException{
		writer.writeStartElement(tagName);
		if (id != null){
			writer.writeStartAttribute(Kml2_1_Tags.GEOMETRY_ID);
			writer.writeValue(id);			
		}
		writer.writeEndAttributes();	
	}
	
	/**
	 * It writes the geometry end tag
	 * @param writer
	 * Writer to write the labels
	 * @param handler
	 * The writer handler implementor
	 * @param tagName
	 * Geometry tag name
	 * @throws IOException
	 */
	public void endGeometry(IXmlStreamWriter writer, GPEKmlWriterHandlerImplementor hanlder,QName tagName) throws IOException{
		writer.writeEndElement();		
	}
}
