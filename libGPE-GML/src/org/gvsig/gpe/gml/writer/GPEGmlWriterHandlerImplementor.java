package org.gvsig.gpe.gml.writer;

import java.io.IOException;
import java.util.Stack;

import org.gvsig.gpe.gml.utils.GMLTags;
import org.gvsig.gpe.gml.writer.profiles.IWriterProfile;
import org.gvsig.gpe.writer.ICoordinateSequence;
import org.gvsig.gpe.xml.XmlProperties;
import org.gvsig.gpe.xml.stream.XmlStreamException;
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
 * $Id: GPEGmlWriterHandlerImplementor.java 350 2008-01-09 12:53:07Z jpiera $
 * $Log$
 * Revision 1.1  2007/06/29 12:19:34  jorpiell
 * The schema validation is made independently of the concrete writer
 *
 * Revision 1.21  2007/06/28 13:05:09  jorpiell
 * The Qname has been updated to the 1.5 JVM machine. The schema validation is made in the GPEWriterHandlerImplementor class
 *
 * Revision 1.20  2007/06/22 12:39:28  jorpiell
 * Bug adding with a element without feature name fixed
 *
 * Revision 1.19  2007/06/22 12:22:40  jorpiell
 * The typeNotFoundException has been deleted. It never was thrown
 *
 * Revision 1.18  2007/06/14 13:50:05  jorpiell
 * The schema jar name has been changed
 *
 * Revision 1.17  2007/06/07 14:53:30  jorpiell
 * Add the schema support
 *
 * Revision 1.16  2007/05/17 14:33:51  jorpiell
 * The fid attribute is written
 *
 * Revision 1.15  2007/05/16 12:07:47  jorpiell
 * Some warnings throwed
 *
 * Revision 1.14  2007/05/15 11:55:11  jorpiell
 * MultiGeometry is now supported
 *
 * Revision 1.13  2007/05/15 10:14:45  jorpiell
 * The element and the feature is managed like a Stack
 *
 * Revision 1.12  2007/05/14 11:18:12  jorpiell
 * Add the ErrorHandler to all the methods
 *
 * Revision 1.11  2007/05/08 10:24:16  jorpiell
 * Add comments to create javadocs
 *
 * Revision 1.10  2007/05/07 07:08:02  jorpiell
 * Add a constructor with the name and the description fields
 *
 * Revision 1.9  2007/04/26 14:40:03  jorpiell
 * Some writer handler methods updated
 *
 * Revision 1.8  2007/04/19 07:25:49  jorpiell
 * Add the add methods to teh contenhandler and change the register mode
 *
 * Revision 1.7  2007/04/18 11:03:55  jorpiell
 * Add the default schema property
 *
 * Revision 1.6  2007/04/17 07:00:35  jorpiell
 * GML name, descripction and Id tags separated
 *
 * Revision 1.5  2007/04/14 16:07:30  jorpiell
 * The writer has been created
 *
 * Revision 1.4  2007/04/13 13:16:00  jorpiell
 * Add the multiple geometries
 *
 * Revision 1.3  2007/04/13 07:17:57  jorpiell
 * Add the writting tests for the simple geometries
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
 * GPE writer handler for the GML format. It writes a 
 * GML file.
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public abstract class GPEGmlWriterHandlerImplementor extends GPEXmlWriterHandlerImplementor{
	//In GML there is only one label. It the consumer
	//try to add more labels the driver will be report
	//one exception
	private boolean oneLayer = false;

	//Used for the multi-geometries
	private boolean isMultiple = false;

	//Used to know if the multiple geometries has type
	private boolean hasMultipleType = false;

	//Used to close the labels
	private Stack currentFeature = null;
	private Stack currentElement = null;	

	//To manage the end of file
	private boolean isInitialized = false;

	//Profile to write a concrete version
	private IWriterProfile profile = null;

	public GPEGmlWriterHandlerImplementor() {
		super();	
		currentFeature = new Stack();
		currentElement = new Stack();
	}
	
	/* (non-Javadoc)
	 * @see org.gvsig.gpe.gml.writer.GPEGmlWriterHandlerImplementor#initialize()
	 */
	public void initialize() {
		super.initialize();
		try {
			//Initializes the namespaces
			initPrefixes();
		} catch (XmlStreamException e) {
			getErrorHandler().addError(e);
		}	
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writer.GPEWriterHandler#close()
	 */
	public void close(){
		try {
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
		try {
			getProfile().getFeatureCollectionWriter().start(writer,
					this, id, namespace, name);		
			writer.writeEndAttributes();
			getProfile().getNameWriter().write(writer, this, name);
			getProfile().getDescriptionWriter().write(writer, this, description);
		} catch (IOException e) {
			getErrorHandler().addError(e);
		}	
	}	

	/**
	 * Initializes the prefixes that will be used
	 * @throws XmlStreamException
	 */
	protected void initPrefixes() throws XmlStreamException{
		writer.setPrefix(XMLTags.XML_NAMESPACE_URI, XMLTags.XML_NAMESPACE_PREFIX);
		writer.setPrefix(XMLTags.XML_SCHEMA_INSTANCE_NAMESPACE_URI, XMLTags.XML_SCHEMA_INSTANCE_NAMESPACE_PREFIX);
		writer.setPrefix(GMLTags.GML_NAMESPACE_PREFIX, GMLTags.GML_NAMESPACE_URI);		
		writer.setPrefix(getGpeManager().getStringProperty(XmlProperties.DEFAULT_NAMESPACE_PREFIX),
				getGpeManager().getStringProperty(XmlProperties.DEFAULT_NAMESPACE_URI));
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.GPEWriterHandler#endLayer()
	 */
	public void endLayer() {
		try {
			writer.writeEndElement();
		} catch (IOException e) {
			getErrorHandler().addError(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writer.GPEWriterHandlerImplementor#startFeature(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void startFeature(String id, String namespace, String name) {
		currentFeature.push(name);
		try {
			profile.getFeatureMemberWriter().start(writer, this, id, namespace, name);
		} catch (IOException e) {
			getErrorHandler().addError(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writer.GPEWriterHandler#endFeature()
	 */
	public void endFeature() {
		try {
			profile.getFeatureMemberWriter().end(writer, this, (String)currentFeature.pop(), getTargetNamespacePrefix());
		} catch (IOException e) {
			getErrorHandler().addError(e);
		}		
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writer.IGPEWriterHandlerImplementor#startElement(java.lang.String, java.lang.String, java.lang.Object)
	 */
	public void startElement(String namespace, String name, Object value) {
		currentElement.push(name);
		try {
			profile.getElementWriter().start(writer, this, namespace, name, value);
		} catch (IOException e) {
			getErrorHandler().addError(e);
		}		
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writer.GPEWriterHandler#endElement()
	 */
	public void endElement() {
		try {
			profile.getElementWriter().end(writer, this, (String)currentElement.pop(), getTargetNamespacePrefix());		   
		} catch (IOException e) {
			getErrorHandler().addError(e);
		}	
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writer.IGPEWriterHandlerImplementor#startPoint(java.lang.String, org.gvsig.gpe.writer.ICoordinateSequence, java.lang.String)
	 */
	public void startPoint(String id, ICoordinateSequence coords, String srs) {
		try {
			if (isMultiple){
				if (hasMultipleType){
					profile.getGeometryMemberWriter().startPoint(writer, this, id, srs);
					profile.getPointWriter().start(writer, this, id, coords, srs);
				}else{
					profile.getPointMemberWriter().start(writer, this, id, coords, srs);
				}
			}else{
				profile.getPointWriter().start(writer, this, id, coords, srs);
			}
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
			if (isMultiple){
				if (hasMultipleType){
					profile.getPointWriter().end(writer, this);
					profile.getGeometryMemberWriter().end(writer, this);
				}else{
					profile.getPointMemberWriter().end(writer, this);
				}
			}else{
				profile.getPointWriter().end(writer, this);
			}
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
			if (isMultiple){
				if (hasMultipleType){
					profile.getGeometryMemberWriter().startPoint(writer, this, id, srs);
					profile.getLineStringWriter().start(writer, this, id, coords, srs);
				}else{
					profile.getLineStringMemeberWriter().start(writer, this, id, coords, srs);
				}
			}else{
				profile.getLineStringWriter().start(writer, this, id, coords, srs);
			}
		} catch (IOException e) {
			getErrorHandler().addError(e);
		}		
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writer.GPEWriterHandler#endLineString()
	 */
	public void endLineString() {
		try {
			if (isMultiple){
				if (hasMultipleType){
					profile.getLineStringWriter().end(writer, this);
					profile.getGeometryMemberWriter().end(writer, this);
				}else{
					profile.getLineStringMemeberWriter().end(writer, this);
				}
			}else{
				profile.getLineStringWriter().end(writer, this);
			}
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
			profile.getBoundedByWriter().start(writer, this, id, coords, srs);
		} catch (IOException e) {
			getErrorHandler().addError(e);
		}		
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writer.GPEWriterHandler#endBbox()
	 */
	public void endBbox() {
		try {
			profile.getBoundedByWriter().end(writer, this);
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
			profile.getLinearRingWriter().start(writer, this, id, coords, srs);
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
			profile.getLinearRingWriter().end(writer, this);
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
			if (isMultiple){
				if (hasMultipleType){
					profile.getGeometryMemberWriter().startPoint(writer, this, id, srs);
					profile.getPolygonWriter().start(writer, this, id, coords, srs);
				}else{
					profile.getPolygonMemberWriter().start(writer, this, id, coords, srs);
				}
			}else{
				profile.getPolygonWriter().start(writer, this, id, coords, srs);
			}
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
			if (isMultiple){
				if (hasMultipleType){
					profile.getPolygonWriter().end(writer, this);
					profile.getGeometryMemberWriter().end(writer, this);
				}else{
					profile.getPolygonMemberWriter().end(writer, this);
				}
			}else{
				profile.getPolygonWriter().end(writer, this);
			}
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
			profile.getInnerBoundaryIsWriter().start(writer, this, coords);
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
			profile.getInnerBoundaryIsWriter().end(writer, this);
		} catch (IOException e) {
			getErrorHandler().addError(e);
		}	
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.GPEWriterHandler#startMultiPoint(java.lang.String, java.lang.String)
	 */
	public void startMultiPoint(String id, String srs) {
		isMultiple = true;
		try {
			profile.getMultiPointWriter().start(writer, this, id, srs);
		} catch (IOException e) {
			getErrorHandler().addError(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.GPEWriterHandler#endMuliPoint()
	 */
	public void endMultiPoint() {
		isMultiple = false;
		try {
			profile.getMultiPointWriter().end(writer, this);
		} catch (IOException e) {
			getErrorHandler().addError(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.GPEWriterHandler#startMultiPoint(java.lang.String, java.lang.String)
	 */
	public void startMultiGeometry(String id, String srs) {
		isMultiple = true;
		hasMultipleType = true;
		try {
			profile.getMultiGeometryWriter().start(writer, this, id, srs);
		} catch (IOException e) {
			getErrorHandler().addError(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.GPEWriterHandler#endMuliPoint()
	 */
	public void endMultiGeometry() {
		isMultiple = false;
		hasMultipleType = false;
		try {
			profile.getMultiGeometryWriter().end(writer, this);
		} catch (IOException e) {
			getErrorHandler().addError(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.GPEWriterHandler#startMultiLineString(java.lang.String, java.lang.String)
	 */
	public void startMultiLineString(String id, String srs) {
		isMultiple = true;
		try {
			profile.getMultiLineStringWriter().start(writer, this, id, srs);
		} catch (IOException e) {
			getErrorHandler().addError(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.GPEWriterHandler#endMultiLineString()
	 */
	public void endMultiLineString() {
		isMultiple = false;
		try {
			profile.getMultiLineStringWriter().end(writer, this);
		} catch (IOException e) {
			getErrorHandler().addError(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.GPEWriterHandler#startMultiPolygon(java.lang.String, java.lang.String)
	 */
	public void startMultiPolygon(String id, String srs) {
		isMultiple = true;
		try {
			profile.getMultiPolygonWriter().start(writer, this, id, srs);
		} catch (IOException e) {
			getErrorHandler().addError(e);
		}		
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.GPEWriterHandler#endMultiPolygon()
	 */
	public void endMultiPolygon() {
		isMultiple = false;
		try {
			profile.getMultiPolygonWriter().end(writer, this);
		} catch (IOException e) {
			getErrorHandler().addError(e);
		}		
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
		return "gml";
	}
	
}
