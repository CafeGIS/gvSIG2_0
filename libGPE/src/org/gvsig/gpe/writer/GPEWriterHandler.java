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
 * $Id: GPEWriterHandler.java 203 2007-12-03 09:36:17Z jpiera $
 * $Log$
 * Revision 1.15  2007/06/29 12:19:14  jorpiell
 * The schema validation is made independently of the concrete writer
 *
 * Revision 1.14  2007/06/28 13:04:33  jorpiell
 * The Qname has been updated to the 1.5 JVM machine. The schema validation is made in the GPEWriterHandlerImplementor class
 *
 * Revision 1.13  2007/06/22 12:19:52  jorpiell
 * The targetNamespace is now writen fine
 *
 * Revision 1.12  2007/06/20 09:35:37  jorpiell
 * Add the javadoc comments
 *
 * Revision 1.11  2007/06/14 13:50:06  jorpiell
 * The schema jar name has been changed
 *
 * Revision 1.10  2007/06/07 14:52:28  jorpiell
 * Add the schema support
 *
 * Revision 1.9  2007/05/16 12:06:22  jorpiell
 * Add Deafult methods
 *
 * Revision 1.8  2007/05/07 12:57:55  jorpiell
 * Add some methods to manage the multigeometries
 *
 * Revision 1.7  2007/05/07 07:06:26  jorpiell
 * Add a constructor with the name and the description fields
 *
 * Revision 1.6  2007/04/26 14:29:15  jorpiell
 * Add a getStringProperty method to the GEPDeafults
 *
 * Revision 1.5  2007/04/19 11:50:20  csanchez
 * Actualizacion protoripo libGPE
 *
 * Revision 1.4  2007/04/19 07:23:20  jorpiell
 * Add the add methods to teh contenhandler and change the register mode
 *
 * Revision 1.3  2007/04/14 16:06:13  jorpiell
 * The writer handler has been updated
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
 * This class make the XML schema validation and call
 * to the concrete parser to write the output file.
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 * @author Carlos Sánchez Periñán (sanchez_carper@gva.es)
 */
public class GPEWriterHandler {
	private IGPEWriterHandlerImplementor writerImplementor = null;	
	
	/**
	 * The constructor
	 * @param writerImplementor
	 * The concrete writer handler implementation
	 */
	public GPEWriterHandler(IGPEWriterHandlerImplementor writerImplementor){
		this.writerImplementor = writerImplementor;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return writerImplementor.toString();
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return writerImplementor.getDescription();
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return writerImplementor.getName();
	}
	
	/**
	 * @return the errorHandler
	 */
	public GPEErrorHandler getErrorHandler() {
		return writerImplementor.getErrorHandler();
	}

	/**
	 * @param errorHandler the errorHandler to set
	 */
	public void setErrorHandler(GPEErrorHandler errorHandler) {
		writerImplementor.setErrorHandler(errorHandler);
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setOutputStream(OutputStream os) {
		writerImplementor.setOutputStream(os);
	}

	/**
	 * @return the format
	 */
	public String getFormat() {
		return writerImplementor.getFormat();
	}
	
	/**
	 * @return the default file extension
	 */
	public String getFileExtension(){
		return writerImplementor.getFileExtension();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.IGPEWriterHandler#startPoint(java.lang.String, double, double, double, java.lang.String)
	 */
	public void startPoint(String id, ICoordinateSequence coords, String srs) {
		writerImplementor.startPoint(id, coords, srs);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.IGPEWriterHandler#endPoint()
	 */
	public void endPoint() {
		writerImplementor.endPoint();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.IGPEWriterHandler#startLineString(java.lang.String, double[], double[], double[], java.lang.String)
	 */
	public void startLineString(String id, ICoordinateSequence coords, String srs) {
		writerImplementor.startLineString(id, coords, srs);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.IGPEWriterHandler#endLineString()
	 */	
	public void endLineString() {
		writerImplementor.endLineString();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.IGPEWriterHandler#startLinearRing(java.lang.String, double[], double[], double[], java.lang.String)
	 */
	public void startLinearRing(String id, ICoordinateSequence coords, String srs) {
//		if (!GeographicalUtils.isClosed(x, y, z)){
//			x = GeographicalUtils.closePolygon(x);
//			y = GeographicalUtils.closePolygon(y);
//			z = GeographicalUtils.closePolygon(z);	
//			getErrorHandler().addWarning(new PolygonAutomaticallyClosedWarning(x,y,z));
//		}
		writerImplementor.startLinearRing(id, coords, srs);
	}	

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.IGPEWriterHandler#endLinearRing()
	 */
	public void endLinearRing() {
		writerImplementor.endLinearRing();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.IGPEWriterHandler#startPolygon(java.lang.String, double[], double[], double[], java.lang.String)
	 */
	public void startPolygon(String id, ICoordinateSequence coords, String srs) {
//  		if (!GeographicalUtils.isClosed(x, y, z)){
//			x = GeographicalUtils.closePolygon(x);
//			y = GeographicalUtils.closePolygon(y);
//			z = GeographicalUtils.closePolygon(z);	
//			getErrorHandler().addWarning(new PolygonAutomaticallyClosedWarning(x,y,z));
//		}
		writerImplementor.startPolygon(id, coords, srs);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.IGPEWriterHandler#endPolygon()
	 */
	public void endPolygon() {
		writerImplementor.endPolygon();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.IGPEWriterHandler#startInnerBoundary(java.lang.String, double[], double[], double[], java.lang.String)
	 */
	public void startInnerBoundary(String id, ICoordinateSequence coords, String srs) {
//		if (!GeographicalUtils.isClosed(x, y, z)){
//			x = GeographicalUtils.closePolygon(x);
//			y = GeographicalUtils.closePolygon(y);
//			z = GeographicalUtils.closePolygon(z);	
//			getErrorHandler().addWarning(new PolygonAutomaticallyClosedWarning(x,y,z));
//		}
		writerImplementor.startInnerBoundary(id, coords, srs);
	}
	
/*
 * (non-Javadoc)
 * @see org.gvsig.gpe.writers.IGPEWriterHandler#endInnerBoundary()
 */
	public void endInnerBoundary() {
		writerImplementor.endInnerBoundary();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.IGPEWriterHandler#initialize()
	 */
	public void initialize() {
		writerImplementor.initialize();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.IGPEWriterHandler#close()
	 */
	public void close() {
		writerImplementor.close();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.IGPEWriterHandler#startBbox(java.lang.String, double[], double[], double[], java.lang.String)
	 */
	public void startBbox(String id, ICoordinateSequence coords, String srs) {
		writerImplementor.startBbox(id, coords, srs);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.IGPEWriterHandler#endBbox()
	 */
	public void endBbox() {
		writerImplementor.endBbox();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.IGPEWriterHandler#startElement(java.lang.String, java.lang.Object, java.lang.Object)
	 */
	public void startElement(String namespace, String name, Object value) {
//		if (isRemoved){
//			elementStack.push(name,xsElementName,isRemoved);
//			return;
//		}
//		TypedObject feature = featureStack.lastElement();
//		//If is a simple element
//		if (elementStack.size() == 0){
//			//If the feature has type
//			if (feature.getType() != null){
//				IXSElementDeclaration xsFeature = getSchemaDocument().getElementDeclarationByName(feature.getType());
//				//If the feature is null has been declarated inside the layer
//				if (xsFeature == null){
//					TypedObject layer = layerStack.lastElement();
//					//The layer has to have a type
//					IXSElementDeclaration xsLayer = getSchemaDocument().getElementDeclarationByName(layer.getType());
//					xsFeature = xsLayer.getSubElementByName(feature.getType());
//				}
//				//If the feature has a type it will be always on the schema
//				IXSElementDeclaration xsElement = xsFeature.getSubElementByName(name);
//				//If the element doesn't exist on the feature
//				if (xsElement == null){
//					isRemoved = true;
//					getErrorHandler().addWarning(new NotSupportedElementWarning(name,
//							xsElementName,
//							feature.getName(),
//							feature.getType()));
//				}								
//			}
//		}else{ //Is a complex element
//			TypedObject parentElement = elementStack.lastElement();
//			//TODO complex element validation
//		}			
//		elementStack.push(namespace, name);
		writerImplementor.startElement(namespace, name, value);			
	}

/*+
 * (non-Javadoc)
 * @see org.gvsig.gpe.writers.IGPEWriterHandler#endElement()
 */
	public void endElement() {
//		TypedObject element = elementStack.pop();
//		if (!element.isRemoved()){
			writerImplementor.endElement();
//		}else{
//			if (!layerStack.lastElement().isRemoved()){
//				if (!featureStack.lastElement().isRemoved()){
//					if (elementStack.size() == 0){
//						isRemoved = false;
//					}else{
//						TypedObject parentElement = elementStack.lastElement();
//						isRemoved = parentElement.isRemoved();
//					}
//				}
//			}
//		}		
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.IGPEWriterHandler#startLayer(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void startLayer(String id, String namespace, String name, String description, String srs) {
//		if (isRemoved){
//			layerStack.push(name,xsElementName,isRemoved);
//			return;
//		}
//		//If is a root layer
//		if (layerStack.size() == 0){
//			//If it has a schema
//			if (xsElementName != null){
//				//If the schema doesn't exists the layer can't be added
//				if (getSchemaDocument().getElementDeclarationByName(xsElementName) == null){
//					isRemoved = true;
//					getErrorHandler().addWarning(new NotSupportedLayerWarning(name,	
//							xsElementName));							
//				}
//			}
//		}else{//It is a child layer
//			TypedObject parentLayer = layerStack.lastElement();
//			//If the parent layer has a XML schema type
//			if (parentLayer.getType() != null){
//				IXSElementDeclaration xsParentLayer = getSchemaDocument().getElementDeclarationByName(parentLayer.getType());
//				//If the parent layer has a schema
//				if (xsParentLayer != null){
//					IXSElementDeclaration xsLayer = xsParentLayer.getSubElementByName(xsElementName);
//					//If the layer name doesn't exist on the parent schema
//					if (xsLayer == null){
//						isRemoved = true;
//						getErrorHandler().addWarning(new NotSupportedLayerWarning(name,
//								xsElementName,
//								parentLayer.getName(),
//								parentLayer.getType()));
//					}
//				}
//			}
//		}				
//		layerStack.push(name,xsElementName,isRemoved);
		writerImplementor.startLayer(id, namespace, name, description, srs);
	}
	
/*
 * (non-Javadoc)
 * @see org.gvsig.gpe.writers.IGPEWriterHandler#endLayer()
 */
	public void endLayer() {
//		TypedObject layer = layerStack.pop();
//		if (!layer.isRemoved()){
			writerImplementor.endLayer();
//		}else{
//			if (layerStack.size() == 0){
//				isRemoved = false;
//			}else{
//				TypedObject parentLayer = layerStack.lastElement();
//				isRemoved = parentLayer.isRemoved();				
//			}
//		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.IGPEWriterHandler#startFeature(java.lang.String, java.lang.String)
	 */
	public void startFeature(String id, String namespace, String name) {
//		if (isRemoved){
//			featureStack.push(name,xsElementName,isRemoved);
//			return;
//		}
//		TypedObject layer = layerStack.lastElement();
//		//If the parent layer has type
//		if (layer.getType() != null){
//			IXSElementDeclaration xsLayer = getSchemaDocument().getElementDeclarationByName(layer.getType());
//			//If the layer schema exists
//			if (xsLayer != null){
//				IXSElementDeclaration xsFeature = xsLayer.getSubElementByName(xsElementName);
//				//If the feature type doesn't exist on the layer
//				if (xsFeature == null){
//					isRemoved = true;
//					getErrorHandler().addWarning(new NotSupportedFeatureWarning(name,	
//							xsElementName,
//							layer.getName(),
//							layer.getType()));	
//				}				
//			}
//		}else{//The layer doesn't has type
//			//If the feature has type
//			if (xsElementName != null){
//				//If the feature type is not on the schema
//				if (getSchemaDocument().getElementDeclarationByName(xsElementName) == null){
//					isRemoved = true;
//					getErrorHandler().addWarning(new NotSupportedFeatureWarning(name,	
//							xsElementName));								
//				}
//			}
//		}
		//featureStack.push(name,xsElementName,isRemoved);
		writerImplementor.startFeature(id, namespace, name);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.IGPEWriterHandler#endFeature()
	 */
	public void endFeature() {
//		TypedObject feature = featureStack.pop();
//		if (!feature.isRemoved()){
			writerImplementor.endFeature();
//		}else{
//			if (!layerStack.lastElement().isRemoved()){
//				if (featureStack.size() == 0){
//					isRemoved = false;
//				}else{
//					TypedObject parentFeature = featureStack.lastElement();
//					isRemoved = parentFeature.isRemoved();
//				}
//			}
//		}		
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.IGPEWriterHandler#startMultiPoint(java.lang.String, java.lang.String)
	 */
	public void startMultiPoint(String id, String srs) {
		writerImplementor.startMultiPoint(id, srs);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.IGPEWriterHandler#endMuliPoint()
	 */
	public void endMultiPoint() {
		writerImplementor.endMultiPoint();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.IGPEWriterHandler#startMultiLineString(java.lang.String, java.lang.String)
	 */
	public void startMultiLineString(String id, String srs) {
		writerImplementor.startMultiLineString(id, srs);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.IGPEWriterHandler#endMultiLineString()
	 */
	public void endMultiLineString() {
		writerImplementor.endMultiLineString();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.IGPEWriterHandler#startMultiPolygon(java.lang.String, java.lang.String)
	 */
	public void startMultiPolygon(String id, String srs) {
		writerImplementor.startMultiPolygon(id, srs);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.IGPEWriterHandler#endMultiPolygon()
	 */
	public void endMultiPolygon() {
		writerImplementor.endMultiPolygon();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.IGPEWriterHandler#startMultiGeometry(java.lang.String, java.lang.String)
	 */
	public void startMultiGeometry(String id, String srs) {
		writerImplementor.startMultiGeometry(id, srs);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.IGPEWriterHandler#endMultiGeometry()
	 */
	public void endMultiGeometry() {
		writerImplementor.endMultiGeometry();
	}

//	/**
//	 * @return the schemaDocument
//	 */
//	public IXSSchemaDocument getSchemaDocument() {
//		return writerImplementor.getSchemaDocument();
//	}
//
//	/**
//	 * @param schemaDocument the schemaDocument to set
//	 */
//	public void setSchemaDocument(IXSSchemaDocument schemaDocument) {
//		writerImplementor.setSchemaDocument(schemaDocument);
//	}	
}
