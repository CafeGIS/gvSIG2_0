package org.gvsig.gpe.kml.writer;

import java.io.IOException;

import org.gvsig.gpe.kml.utils.Kml2_1_Tags;
import org.gvsig.gpe.kml.writer.profiles.IWriterProfile;
import org.gvsig.gpe.warnings.FeatureNotSupportedWarning;
import org.gvsig.gpe.writer.ICoordinateSequence;
import org.gvsig.gpe.xml.stream.IXmlStreamWriter;
import org.gvsig.gpe.xml.stream.stax.StaxXmlStreamWriter;
import org.gvsig.gpe.xml.utils.XMLTags;
import org.gvsig.gpe.xml.writer.GPEXmlWriterHandlerImplementor;


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
 * $Id: GPEKmlWriterHandlerImplementor.java 362 2008-01-10 08:41:41Z jpiera $
 * $Log$
 * Revision 1.1  2007/06/29 12:19:48  jorpiell
 * The schema validation is made independently of the concrete writer
 *
 * Revision 1.17  2007/06/11 06:41:41  jorpiell
 * Add a new Exception
 *
 * Revision 1.16  2007/06/07 14:53:59  jorpiell
 * Add the schema support
 *
 * Revision 1.15  2007/05/16 15:54:20  jorpiell
 * Add elements support
 *
 * Revision 1.14  2007/05/16 12:08:14  jorpiell
 * A multipoint layer is not supported
 *
 * Revision 1.13  2007/05/16 09:30:09  jorpiell
 * the writting methods has to have the errorHandler argument
 *
 * Revision 1.12  2007/05/15 12:37:45  jorpiell
 * Add multyGeometries
 *
 * Revision 1.11  2007/05/09 08:36:24  jorpiell
 * Add the bbox to the layer
 *
 * Revision 1.10  2007/05/08 09:28:17  jorpiell
 * Add comments to create javadocs
 *
 * Revision 1.9  2007/05/08 08:22:37  jorpiell
 * Add comments to create javadocs
 *
 * Revision 1.8  2007/05/07 07:07:18  jorpiell
 * Add a constructor with the name and the description fields
 *
 * Revision 1.7  2007/05/02 11:46:50  jorpiell
 * Writing tests updated
 *
 * Revision 1.6  2007/04/20 08:38:59  jorpiell
 * Tests updating
 *
 * Revision 1.5  2007/04/17 10:30:41  jorpiell
 * Add a method to compress a file
 *
 * Revision 1.4  2007/04/14 16:08:07  jorpiell
 * Kml writing support added
 *
 * Revision 1.3  2007/04/13 13:16:21  jorpiell
 * Add KML reading support
 *
 * Revision 1.2  2007/04/12 17:06:43  jorpiell
 * First GML writing tests
 *
 * Revision 1.1  2007/04/12 10:21:52  jorpiell
 * Add the writers
 *
 *
 */
/**
 * KML writer handler used to write KML/KMZ files
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public abstract class GPEKmlWriterHandlerImplementor extends GPEXmlWriterHandlerImplementor{
	private int layerLevel = 0;
//	private Stack currentElementStream = null;	
//	private Stack currentElementName = null;	
	private IWriterProfile profile = null;
	
	public GPEKmlWriterHandlerImplementor() {
		super();
//		currentElementStream = new Stack();
//		currentElementName = new Stack();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.xml.writer.GPEXmlWriterHandler#createOutputStream()
	 */
	protected IXmlStreamWriter createWriter() throws IOException {
		return new StaxXmlStreamWriter(getOutputStream());
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.GPEWriterHandler#getDefaultFormat()
	 */
	public String getDefaultFormat() {
		return "KML";
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writer.IGPEWriterHandlerImplementor#getFormat()
	 */
	public String getFormat() {
		return "text/xml; subtype=kml/2.1";		
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writer.GPEWriterHandler#initialize()
	 */
	public void initialize(){
		super.initialize();
		try {
			writer.setDefaultNamespace(Kml2_1_Tags.NAMESPACE_21);
			writer.writeStartElement(Kml2_1_Tags.ROOT);	
			writer.writeStartAttribute(XMLTags.XML_NAMESPACE_URI, XMLTags.XML_NAMESPACE_PREFIX);
			writer.writeValue(Kml2_1_Tags.NAMESPACE_21);
			writer.writeEndAttributes();
		} catch (IOException e) {
			getErrorHandler().addError(e);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writer.GPEWriterHandler#close()
	 */
	public void close(){
		try {
			writer.writeEndElement();
			writer.flush();
			writer.close();
		} catch (IOException e) {
			getErrorHandler().addError(e);
		}		
	}	
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writer.GPEWriterHandlerImplementor#startLayer(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void startLayer(String id, String namespace, String name, String description, String srs) {
		try{
			if (layerLevel == 0){
				getProfile().getDocumentWriter().start(writer, this, id, name, description);
			}else{
				getProfile().getFolderWriter().start(writer, this, id, name, description);
			}			
		} catch (IOException e) {
			getErrorHandler().addError(e);
		}
		layerLevel++;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.GPEWriterHandler#endLayer()
	 */
	public void endLayer() {
		layerLevel--;
		try{
			if (layerLevel == 0){
				getProfile().getDocumentWriter().end(writer, this);
			}else{
				getProfile().getFolderWriter().end(writer, this);
			}
		} catch (IOException e) {
			getErrorHandler().addError(e);
		}		
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.GPEWriterHandler#startFeature(java.lang.String, java.lang.String)
	 */
	public void startFeature(String id, String namespace, String name) {
//		currentElementStream.push(new IXMLStreamWriter(new StringWriter()));
//		currentElementName.push(new Stack());
		try{
			getProfile().getPlaceMarkWriter().start(writer, this, id, name);
		} catch (IOException e) {
			getErrorHandler().addError(e);
		}	
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.GPEWriterHandler#endFeature()
	 */
	public void endFeature() {
//		IXmlStreamWriter elementWriter = (IXmlStreamWriter)currentElementStream.pop();
//		currentElementName.pop();
		try{
			getProfile().getMetadataWriter().start(writer, this);
//			writer.write(elementWriter.toString());
			getProfile().getMetadataWriter().end(writer, this);
			getProfile().getPlaceMarkWriter().end(writer, this);
		} catch (IOException e) {
			getErrorHandler().addError(e);
		}			
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writer.GPEWriterHandler#startElement(java.lang.String, java.lang.Object, java.lang.Object)
	 */
	public void startElement(String namespace, String name, Object value) {
//		IXmlStreamWriter buffer = ((IXmlStreamWriter)currentElementStream.lastElement());	
//		QName qName = new QName(name);
//		((Stack)currentElementName.lastElement()).push(qName);
//		try {
//			getProfile().getElementWriter().start(buffer, this, qName, value);
//		} catch (IOException e) {
//			getErrorHandler().addError(e);
//		}
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writer.GPEWriterHandler#endElement()
	 */
	public void endElement() {
//		IXmlStreamWriter buffer = ((IXmlStreamWriter)currentElementStream.lastElement());	
//		QName elementName = (QName)((Stack)currentElementName.lastElement()).pop();
//		try {
//			getProfile().getElementWriter().end(buffer, this, elementName);		   
//		} catch (IOException e) {
//			getErrorHandler().addError(e);
//		}		
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writer.IGPEWriterHandlerImplementor#startPoint(java.lang.String, org.gvsig.gpe.writer.ICoordinateSequence, java.lang.String)
	 */
	public void startPoint(String id, ICoordinateSequence coords, String srs) {
		try {
			getProfile().getPointTypeWriter().start(writer, this, id, coords);
		} catch (IOException e) {
			getErrorHandler().addError(e);
		}		
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writer.GPEWriterHandler#endPoint()
	 */
	public void endPoint() {
		try {
			getProfile().getPointTypeWriter().end(writer, this);			
		} catch (IOException e) {
			getErrorHandler().addError(e);
		}		
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writer.IGPEWriterHandlerImplementor#startLineString(java.lang.String, org.gvsig.gpe.writer.ICoordinateSequence, java.lang.String)
	 */
	public void startLineString(String id, ICoordinateSequence coords, String srs) {
		try {
			getProfile().getLineStringTypeWriter().start(writer, this, id, coords);			
		} catch (IOException e) {
			getErrorHandler().addError(e);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.GPEWriterHandler#endLineString()
	 */
	public void endLineString() {
		try {
			getProfile().getLineStringTypeWriter().end(writer, this);			
		} catch (IOException e) {
			getErrorHandler().addError(e);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writer.IGPEWriterHandlerImplementor#startLinearRing(java.lang.String, org.gvsig.gpe.writer.ICoordinateSequence, java.lang.String)
	 */
	public void startLinearRing(String id, ICoordinateSequence coords, String srs) {
		try {
			getProfile().getLinearRingWriter().start(writer, this, coords);			
		} catch (IOException e) {
			getErrorHandler().addError(e);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.GPEWriterHandler#endLinearRing()
	 */
	public void endLinearRing() {
		try {
			getProfile().getLinearRingWriter().end(writer, this);			
		} catch (IOException e) {
			getErrorHandler().addError(e);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writer.IGPEWriterHandlerImplementor#startPolygon(java.lang.String, org.gvsig.gpe.writer.ICoordinateSequence, java.lang.String)
	 */
	public void startPolygon(String id, ICoordinateSequence coords, String srs) {
		try {
			getProfile().getPolygonTypeWriter().start(writer, this, id, coords);			
		} catch (IOException e) {
			getErrorHandler().addError(e);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.GPEWriterHandler#endPolygon()
	 */
	public void endPolygon() {
		try {
			getProfile().getPolygonTypeWriter().end(writer, this);			
		} catch (IOException e) {
			getErrorHandler().addError(e);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writer.IGPEWriterHandlerImplementor#startInnerBoundary(java.lang.String, org.gvsig.gpe.writer.ICoordinateSequence, java.lang.String)
	 */
	public void startInnerBoundary(String id, ICoordinateSequence coords, String srs) {
		try {
			getProfile().getInnerBoundaryIsWriter().start(writer, this, coords);			
		} catch (IOException e) {
			getErrorHandler().addError(e);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.GPEWriterHandler#endInnerBoundary()
	 */
	public void endInnerBoundary() {
		try {
			getProfile().getInnerBoundaryIsWriter().end(writer, this);			
		} catch (IOException e) {
			getErrorHandler().addError(e);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writer.IGPEWriterHandlerImplementor#startBbox(java.lang.String, org.gvsig.gpe.writer.ICoordinateSequence, java.lang.String)
	 */
	public void startBbox(String id, ICoordinateSequence coords, String srs) {
		try {
			getProfile().getRegionWriter().start(writer, this, coords);			
		} catch (IOException e) {
			getErrorHandler().addError(e);
		}	
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.GPEWriterHandler#endBbox()
	 */
	public void endBbox() {
		try {
			getProfile().getRegionWriter().end(writer, this);			
		} catch (IOException e) {
			getErrorHandler().addError(e);
		}		
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.GPEWriterHandler#startMultiPoint(java.lang.String, java.lang.String)
	 */
	public void startMultiGeometry(String id, String srs) {
		try {
			getProfile().getMultiGeometryWriter().start(writer, this, id);
		} catch (IOException e) {
			getErrorHandler().addError(e);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.GPEWriterHandler#endMuliPoint()
	 */
	public void endMultiGeometry() {
		try {
			getProfile().getMultiGeometryWriter().end(writer, this);
		} catch (IOException e) {
			getErrorHandler().addError(e);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.GPEWriterHandler#startMultiPoint(java.lang.String, java.lang.String)
	 */
	public void startMultiPoint(String id, String srs) {
		getErrorHandler().addWarning(
				new FeatureNotSupportedWarning(FeatureNotSupportedWarning.MULTIPOINTCREATION,getName()));
		startMultiGeometry(id, srs);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.GPEWriterHandler#endMuliPoint()
	 */
	public void endMultiPoint() {
		super.endMultiPoint();
		endMultiGeometry();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.GPEWriterHandler#startMultiPoint(java.lang.String, java.lang.String)
	 */
	public void startMultiLineString(String id, String srs) {
		getErrorHandler().addWarning(
				new FeatureNotSupportedWarning(FeatureNotSupportedWarning.MULTILINESTRINGCREATION,getName()));
		startMultiGeometry(id, srs);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.GPEWriterHandler#endMuliPoint()
	 */
	public void endMultiLineString() {
		endMultiGeometry();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.GPEWriterHandler#startMultiPoint(java.lang.String, java.lang.String)
	 */
	public void startMultiPolygon(String id, String srs) {
		getErrorHandler().addWarning(
				new FeatureNotSupportedWarning(FeatureNotSupportedWarning.MULTIPOLYGONCREATION,getName()));
		startMultiGeometry(id, srs);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.GPEWriterHandler#endMuliPoint()
	 */
	public void endMultiPolygon() {
		endMultiGeometry();
	}

	/**
	 * @return the profile
	 */
	public IWriterProfile getProfile() {
		return profile;
	}

	/**
	 * @param profile the profile to set
	 */
	public void setProfile(IWriterProfile profile) {
		this.profile = profile;
	}
	
	/* (non-Javadoc)
	 * @see org.gvsig.gpe.writer.IGPEWriterHandlerImplementor#getFileExtension()
	 */
	public String getFileExtension() {
		return "kml";
	}
}
