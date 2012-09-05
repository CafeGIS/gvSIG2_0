package org.gvsig.gpe.writer;

import java.io.OutputStream;

import org.gvsig.gpe.GPELocator;
import org.gvsig.gpe.GPEManager;
import org.gvsig.gpe.parser.GPEErrorHandler;
import org.gvsig.gpe.parser.ICoordinateIterator;
import org.gvsig.gpe.warnings.FeatureNotSupportedWarning;

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
 * $Id: GPEWriterHandlerImplementor.java 203 2007-12-03 09:36:17Z jpiera $
 * $Log$
 * Revision 1.1  2007/06/28 13:04:33  jorpiell
 * The Qname has been updated to the 1.5 JVM machine. The schema validation is made in the GPEWriterHandlerImplementor class
 *
 *
 */
/**
 * This class is a common implementation for all the
 * IGPEWriterHandlerImplementor. It creates a warning 
 * message in all its methods to report to the application
 * that the writer handler doesn't support one operation.
 * If the concrete writer supports it, it has to rewrite
 * the method overriding the parent implementation.
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 * @author Carlos Sánchez Periñán (sanchez_carper@gva.es)
 */
public abstract class GPEWriterHandlerImplementor implements IGPEWriterHandlerImplementor {
	private OutputStream os = null;
	private GPEErrorHandler errorHandler = null;
	private GPEManager gpeManager = null;
	//private IXSSchemaDocument schemaDocument = null;

	/** 
	 * All the GPE writer handlers must implement a constructor 
	 * with this two arguments.
	 **/
	public GPEWriterHandlerImplementor(){
		super();
		gpeManager = GPELocator.getGPEManager();
	}	

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return getName();
	}
	
	/**
	 * @return the errorHandler
	 */
	public GPEErrorHandler getErrorHandler() {
		return errorHandler;
	}

	/**
	 * @param errorHandler the errorHandler to set
	 */
	public void setErrorHandler(GPEErrorHandler errorHandler) {
		this.errorHandler = errorHandler;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.IGPEWriterHandlerImplementor#setOutputStream(java.io.OutputStream)
	 */
	public void setOutputStream(OutputStream os){
		this.os = os;
	}	
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.IGPEWriterHandlerImplementor#setFormat(java.lang.String)
	 */
	public void setFormat(String format) {
	}	
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.IGPEWriterHandlerImplementor#getSchemaDocument()
	 */
//	public IXSSchemaDocument getSchemaDocument() {
//		if (schemaDocument == null){
//			schemaDocument = new XSSchemaDocumentImpl();
//		}
//		return schemaDocument;
//	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.IGPEWriterHandlerImplementor#setSchemaDocument(org.gvsig.xmlschema.som.IXSSchemaDocument)
	 */
//	public void setSchemaDocument(IXSSchemaDocument schemaDocument) {
//		this.schemaDocument = schemaDocument;
//	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writer.IGPEWriterHandlerImplementor#startPoint(java.lang.String, org.gvsig.gpe.parser.ICoordinateIterator, java.lang.String)
	 */
	public void startPoint(String id, ICoordinateIterator coords, String srs) {
		getErrorHandler().addWarning(
				new FeatureNotSupportedWarning(FeatureNotSupportedWarning.POINTCREATION,getName()));
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.IGPEWriterHandler#endPoint()
	 */
	public void endPoint() {

	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writer.IGPEWriterHandlerImplementor#startLineString(java.lang.String, org.gvsig.gpe.parser.ICoordinateIterator, java.lang.String)
	 */
	public void startLineString(String id, ICoordinateIterator coords, String srs) {
		getErrorHandler().addWarning(
				new FeatureNotSupportedWarning(FeatureNotSupportedWarning.LINESTRINGCREATION,getName()));
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.IGPEWriterHandler#endLineString()
	 */	
	public void endLineString() {

	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writer.IGPEWriterHandlerImplementor#startLinearRing(java.lang.String, org.gvsig.gpe.parser.ICoordinateIterator, java.lang.String)
	 */
	public void startLinearRing(String id, ICoordinateIterator coords, String srs) {
		getErrorHandler().addWarning(
				new FeatureNotSupportedWarning(FeatureNotSupportedWarning.LINEARRINGCREATION,getName()));
	}	

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.IGPEWriterHandler#endLinearRing()
	 */
	public void endLinearRing() {
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writer.IGPEWriterHandlerImplementor#startPolygon(java.lang.String, org.gvsig.gpe.parser.ICoordinateIterator, java.lang.String)
	 */
	public void startPolygon(String id, ICoordinateIterator coords, String srs) {
		getErrorHandler().addWarning(
				new FeatureNotSupportedWarning(FeatureNotSupportedWarning.POLYGONCREATION,getName()));
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.IGPEWriterHandler#endPolygon()
	 */
	public void endPolygon() {

	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writer.IGPEWriterHandlerImplementor#startInnerBoundary(java.lang.String, org.gvsig.gpe.parser.ICoordinateIterator, java.lang.String)
	 */
	public void startInnerBoundary(String id, ICoordinateIterator coords, String srs) {
		getErrorHandler().addWarning(
				new FeatureNotSupportedWarning(FeatureNotSupportedWarning.INNERBOUNDARYCREATION,getName()));
	}
	
/*
 * (non-Javadoc)
 * @see org.gvsig.gpe.writers.IGPEWriterHandler#endInnerBoundary()
 */
	public void endInnerBoundary() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.IGPEWriterHandler#initialize()
	 */
	public void initialize() {
		

	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.IGPEWriterHandler#close()
	 */
	public void close() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writer.IGPEWriterHandlerImplementor#startBbox(java.lang.String, org.gvsig.gpe.parser.ICoordinateIterator, java.lang.String)
	 */
	public void startBbox(String id, ICoordinateIterator  coords, String srs) {
		getErrorHandler().addWarning(
				new FeatureNotSupportedWarning(FeatureNotSupportedWarning.BBOXCREATION,getName()));
	}
	
/*
 * (non-Javadoc)
 * @see org.gvsig.gpe.writers.IGPEWriterHandler#endBbox()
 */
	public void endBbox() {
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.IGPEWriterHandler#startElement(java.lang.String, java.lang.Object, java.lang.Object)
	 */
	public void startElement(String name, Object value, String xsElementName) {
		getErrorHandler().addWarning(
				new FeatureNotSupportedWarning(FeatureNotSupportedWarning.ELEMENTCREATION,getName()));
	}

/*+
 * (non-Javadoc)
 * @see org.gvsig.gpe.writers.IGPEWriterHandler#endElement()
 */
	public void endElement() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.IGPEWriterHandler#startLayer(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void startLayer(String id, String name, String description, String srs, String xsElementName) {
		// TODO Auto-generated method stub

	}
	
/*
 * (non-Javadoc)
 * @see org.gvsig.gpe.writers.IGPEWriterHandler#endLayer()
 */
	public void endLayer() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.IGPEWriterHandler#startFeature(java.lang.String, java.lang.String)
	 */
	public void startFeature(String id, String name, String xsElementName) {
		getErrorHandler().addWarning(
				new FeatureNotSupportedWarning(FeatureNotSupportedWarning.FEATURECREATION,getName()));	
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.IGPEWriterHandler#endFeature()
	 */
	public void endFeature() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.IGPEWriterHandler#startMultiPoint(java.lang.String, java.lang.String)
	 */
	public void startMultiPoint(String id, String srs) {
		getErrorHandler().addWarning(
				new FeatureNotSupportedWarning(FeatureNotSupportedWarning.MULTIPOINTCREATION,getName()));		
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.IGPEWriterHandler#endMuliPoint()
	 */
	public void endMultiPoint() {

	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.IGPEWriterHandler#startMultiLineString(java.lang.String, java.lang.String)
	 */
	public void startMultiLineString(String id, String srs) {
		getErrorHandler().addWarning(
				new FeatureNotSupportedWarning(FeatureNotSupportedWarning.MULTILINESTRINGCREATION,getName()));
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.IGPEWriterHandler#endMultiLineString()
	 */
	public void endMultiLineString() {

	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.IGPEWriterHandler#startMultiPolygon(java.lang.String, java.lang.String)
	 */
	public void startMultiPolygon(String id, String srs) {
		getErrorHandler().addWarning(
				new FeatureNotSupportedWarning(FeatureNotSupportedWarning.MULTIPOLYGONCREATION,getName()));
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.IGPEWriterHandler#endMultiPolygon()
	 */
	public void endMultiPolygon() {

	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.IGPEWriterHandler#startMultiGeometry(java.lang.String, java.lang.String)
	 */
	public void startMultiGeometry(String id, String srs) {
		getErrorHandler().addWarning(
				new FeatureNotSupportedWarning(FeatureNotSupportedWarning.MULTIGEOMETRYCREATION,getName()));
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.IGPEWriterHandler#endMultiGeometry()
	 */
	public void endMultiGeometry() {
		// TODO Auto-generated method stub

	}

	/**
	 * @return the output stream
	 */
	protected OutputStream getOutputStream() {
		return os;
	}	

	/**
	 * @return the gpeManager
	 */
	public GPEManager getGpeManager() {
		return gpeManager;
	}

}
