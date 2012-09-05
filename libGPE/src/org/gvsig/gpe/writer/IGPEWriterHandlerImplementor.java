package org.gvsig.gpe.writer;

import java.io.OutputStream;

import org.gvsig.gpe.parser.GPEErrorHandler;

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
 * $Id: IGPEWriterHandlerImplementor.java 203 2007-12-03 09:36:17Z jpiera $
 * $Log$
 * Revision 1.1  2007/06/28 13:04:33  jorpiell
 * The Qname has been updated to the 1.5 JVM machine. The schema validation is made in the GPEWriterHandlerImplementor class
 *
 * Revision 1.7  2007/06/20 09:35:37  jorpiell
 * Add the javadoc comments
 *
 * Revision 1.6  2007/06/07 14:52:28  jorpiell
 * Add the schema support
 *
 * Revision 1.5  2007/05/16 12:06:22  jorpiell
 * Add Deafult methods
 *
 * Revision 1.4  2007/05/07 12:57:55  jorpiell
 * Add some methods to manage the multigeometries
 *
 * Revision 1.3  2007/04/26 14:29:15  jorpiell
 * Add a getStringProperty method to the GEPDeafults
 *
 * Revision 1.2  2007/04/13 13:14:55  jorpiell
 * Created the base tests and add some methods to the content handler
 *
 * Revision 1.1  2007/04/13 07:17:54  jorpiell
 * Add the writting tests for the simple geometries
 *
 * Revision 1.2  2007/04/12 17:06:42  jorpiell
 * First GML writing tests
 *
 * Revision 1.1  2007/04/12 10:20:40  jorpiell
 * Add the writer
 *
 *
 */
/**
 * This interface defines the writing process methods. To write
 * a new concrete format it is necessary to create a class that
 * implements this methods and creates the output file whereas
 * the consumer application is invoking these methods.
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 * @author Carlos Sánchez Periñán (sanchez_carper@gva.es)
 */
public interface IGPEWriterHandlerImplementor {
	
	/**
	 * @return the writer name
	 */
	public String getName();
	
	/**
	 * @return the writer description
	 */
	public String getDescription();
	
	/**
	 * Sets the outputstream
	 */
	public void setOutputStream(OutputStream os);
			
	/**
	 * @return the supported format
	 */
	public String getFormat();
	
	/**
	 * @return the default file extension
	 */
	public String getFileExtension();
	
//	/**
//	 * @return the schema document
//	 */
//	public IXSSchemaDocument getSchemaDocument();

//	/**
//	 * @param schemaDocument the schemaDocument to set
//	 */
//	public void setSchemaDocument(IXSSchemaDocument schemaDocument);
	
	/**
	 * @return the error handler
	 */
	public GPEErrorHandler getErrorHandler();
		
	/**
	 * @param errorHandler the errorHandler to set
	 */
	public void setErrorHandler(GPEErrorHandler errorHandler);
	
	/**
	 * It must be invoked before to start the reading process.
	 * It is used just to indicate to the writer that the
	 * writing process is going to start.
	 */
	public void initialize();
	
	/**
	 * It indicates that the writing process has finished. 
	 * The writer can close the file.
	 */
	public void close();
	
	/**
	 * It is thrown by the consumer application to indicate 
	 * that it has to write a layer.
	 * @param id
	 * Layer identifier
	 * @param namespace
	 * Layer namespace
	 * @param name
	 * Layer name
	 * @param description
	 * Layer description
	 * @param srs
	 * Layer spatial reference system
	 */
	public void startLayer(String id, String namespace, String name, String description, String srs);
	
	/**
	 * It means that the current layer has finished 
	 * to be written.
	 */
	public void endLayer();
	
	/**
	 * It is thrown by the consumer application to indicate
	 * that it has to write a bounding box.
	 * @param id
	 * Bounding box identifier
	 * @param coords
	 * A coordinates iterator
	 * @param srs
	 * Spatial reference system
	 */
	public void startBbox(String id, ICoordinateSequence coords, String srs);
	
	/**
	 * It means that the current bounding box has finished to 
	 * be written.
	 */
	public void endBbox();
	
	/**
	 * It is thrown by the consumer application to indicate 
	 * that it has to write a feature.
	 * @param id
	 * Feature identifier
	 * @param namespace
	 * Feature namespace
	 * @param name
	 * Feature name
	 */
	public void startFeature(String id, String namespace, String name);
	
	/**
	 * It means that the current feature has finished 
	 * to be written.
	 */
	public void endFeature();
	
	/**
	 * It is thrown by the consumer application to indicate that
	 * it has to write a element.
	 * @param namespace
	 * Element namespace
	 * @param name
	 * Element name
	 * @param value
	 * Element value
	  */
	public void startElement(String namespace, String name, Object value);
	
	/**
	 * It means that the current element has finished 
	 * to be written.
	 */
	public void endElement();
	
	/**
	 * It is thrown by the consumer application to 
	 * indicate that it has to write a point.
	 * @param id
	 * Point identifier
	 * @param coords
	 * A coordinates iterator
	 * @param srs
	 * Spatial reference system
	 */
	public void startPoint(String id, ICoordinateSequence coords, String srs);
	
	/**
	 * It means that the current point has finished 
	 * to be written.
	 */
	public void endPoint();
	
	/**
	 * It is thrown by the consumer application to
	 * indicate that it has to write a multiPoint. 
	 * @param id
	 * Geometry identifier
	 * @param srs
	 * Spatial reference system
	 */
	public void startMultiPoint(String id, String srs);
	
	/**
	 * It means that the current multiPoint has finished 
	 * to be written.
	 */
	public void endMultiPoint();
	
	/**
	 * It is thrown by the consumer application to 
	 * indicate that it has to write a lineString.
	 * @param id
	 * LineString identifier
	 * @param coords
	 * A coordinates iterator
	 * @param srs
	 * Spatial reference system
	 */
	public void startLineString(String id, ICoordinateSequence coords, String srs);
	
	/**
	 * It means that the current lineString has finished 
	 * to be written.
	 */
	public void endLineString();
	
	/**
	 * It is thrown by the consumer application to
	 * indicate that it has to write a multiLineString. 
	 * @param id
	 * Geometry identifier
	 * @param srs
	 * Spatial reference system
	 */
	public void startMultiLineString(String id, String srs);
	
	/**
	 * It means that the current multiLineString has finished 
	 * to be written.
	 */
	public void endMultiLineString();
	
	/**
	 * It is thrown by the consumer application to 
	 * indicate that it has to write a linearRing.
	 * @param id
	 * LineString identifier
	 * @param coords
	 * A coordinates iterator
	 * @param srs
	 * Spatial reference system
	 */
	public void startLinearRing(String id, ICoordinateSequence coords, String srs);
	
	/**
	 * It means that the current linearRing has finished
	 *  to be written.
	 */
	public void endLinearRing();
	
	/**
	 * It is thrown by the consumer application to 
	 * indicate that it has to write a polygon.
	 * @param id
	 * LineString identifier
	 * @param coords
	 * A coordinates iterator
	 * @param srs
	 * Spatial reference system
	 */
	public void startPolygon(String id, ICoordinateSequence coords, String srs);
	
	/**
	 * It means that the current polygon has finished 
	 * to be written.
	 */
	public void endPolygon();
	
	/**
	 * It is thrown by the consumer application to
	 * indicate that it has to write a multiPolygon. 
	 * @param id
	 * Geometry identifier
	 * @param srs
	 * Spatial reference system
	 */
	public void startMultiPolygon(String id, String srs);
	
	/**
	 * It means that the current multiPolygon has finished
	 *  to be written.
	 */
	public void endMultiPolygon();
	
	/**
	 * It is thrown by the consumer application to
	 * indicate that it has to write a multiGeometry. 
	 * @param id
	 * Geometry identifier
	 * @param srs
	 * Spatial reference system
	 */
	public void startMultiGeometry(String id, String srs);
	
	/**
	 * It means that the current multiGeometry has finished 
	 * to be written.
	 */
	public void endMultiGeometry();
	
	/**
	 * It is thrown by the consumer application to 
	 * indicate that it has to write a innerPolygon.
	 * @param id
	 * LineString identifier
	 * @param coords
	 * A coordinates iterator
	 * @param srs
	 * Spatial reference system
	 */
	public void startInnerBoundary(String id, ICoordinateSequence coords, String srs);
	
	/**
	 * It means that the current innerPolygon has finished to 
	 * be written.
	 */
	public void endInnerBoundary();
}
