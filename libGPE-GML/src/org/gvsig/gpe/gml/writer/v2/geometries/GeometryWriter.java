package org.gvsig.gpe.gml.writer.v2.geometries;

import java.io.IOException;

import org.gvsig.gpe.gml.utils.GMLProjectionFactory;
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
 * $Id: GeometryWriter.java 351 2008-01-09 13:10:48Z jpiera $
 * $Log$
 * Revision 1.7  2007/05/16 13:00:48  csanchez
 * ActualizaciÃ³n de libGPE-GML
 *
 * Revision 1.6  2007/05/14 11:18:12  jorpiell
 * Add the ErrorHandler to all the methods
 *
 * Revision 1.5  2007/05/14 09:32:12  jorpiell
 * Add the a new class to compare tags
 *
 * Revision 1.4  2007/05/08 10:24:16  jorpiell
 * Add comments to create javadocs
 *
 * Revision 1.3  2007/04/14 16:07:30  jorpiell
 * The writer has been created
 *
 * Revision 1.2  2007/04/12 17:06:44  jorpiell
 * First GML writing tests
 *
 * Revision 1.1  2007/04/12 10:23:41  jorpiell
 * Add some writers and the GPEXml parser
 *
 *
 */
/**
 * This class is used to write the attributes for
 * a GML geometry. 
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public abstract class GeometryWriter {
	
	/**
	 * Writes a Geoemtry init tag in GML
	 * @param writer
	 * Writer to write the labels
	 * @param handler
	 * The writer handler implementor
	 * @param tagName
	 * Geometry type
	 * @param id
	 * Geometry id
	 * @param srs
	 * Spatial reference system
	 * @throws IOException
	 */
	public void start(IXmlStreamWriter writer, GPEGmlWriterHandlerImplementor handler, String id,String srs) throws IOException{
		writer.writeStartElement(GMLTags.GML_NAMESPACE_URI, getGeometryName());
		if (id != null){
			writer.writeStartAttribute(GMLTags.GML_GID);
			writer.writeValue(id);
		}
		if (srs != null){
			writer.writeStartAttribute(GMLTags.GML_SRS_NAME);
			writer.writeValue(GMLProjectionFactory.fromGPEToGML(srs, handler.getErrorHandler()));
		}
		writer.writeEndAttributes();		
	}
	
	/**
	 * @return the geometry name
	 */
	public abstract String getGeometryName();
	
	/**
	 * Writes a Geoemtry end tag in GML
	 * @param writer
	 * Writer to write the labels
	 * @param handler
	 * The writer handler implementor
	 * @param tagName
	 * Geometry type
	 * @throws IOException
	 */
	public void end(IXmlStreamWriter writer, GPEGmlWriterHandlerImplementor handler) throws IOException{
		writer.writeEndElement();		
	}
	
}

