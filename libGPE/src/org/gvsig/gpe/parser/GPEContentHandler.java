package org.gvsig.gpe.parser;

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
 * $Id: GPEContentHandler.java 202 2007-11-27 12:00:11Z jpiera $
 * $Log$
 * Revision 1.17  2007/06/20 09:35:37  jorpiell
 * Add the javadoc comments
 *
 * Revision 1.16  2007/06/19 10:34:51  jorpiell
 * Add some comments and creates a warning when an operation is not supported
 *
 * Revision 1.15  2007/06/14 13:50:06  jorpiell
 * The schema jar name has been changed
 *
 * Revision 1.14  2007/06/07 14:52:28  jorpiell
 * Add the schema support
 *
 * Revision 1.13  2007/05/15 12:09:18  jorpiell
 * The bbox is linked to the feature
 *
 * Revision 1.12  2007/05/09 10:25:45  jorpiell
 * Add the multiGeometries
 *
 * Revision 1.11  2007/05/07 12:57:55  jorpiell
 * Add some methods to manage the multigeometries
 *
 * Revision 1.10  2007/04/19 07:23:20  jorpiell
 * Add the add methods to teh contenhandler and change the register mode
 *
 * Revision 1.9  2007/04/18 12:48:16  jorpiell
 * The ID attribute is always on the first position
 *
 * Revision 1.8  2007/04/18 10:46:23  jorpiell
 * bbox para from tyhe starLayer method deleted
 *
 * Revision 1.7  2007/04/18 10:43:24  jorpiell
 * Eliminados los Object de la interfaz
 *
 * Revision 1.6  2007/04/17 07:53:55  jorpiell
 * Before to start a new parsing process, the initialize method of the content handlers is throwed
 *
 * Revision 1.5  2007/04/14 16:06:13  jorpiell
 * The writer handler has been updated
 *
 * Revision 1.4  2007/04/13 13:14:55  jorpiell
 * Created the base tests and add some methods to the content handler
 *
 * Revision 1.3  2007/04/12 17:06:42  jorpiell
 * First GML writing tests
 *
 * Revision 1.2  2007/04/11 13:04:51  jorpiell
 * Add the srs param to the addLayer method
 *
 * Revision 1.1  2007/04/11 08:46:21  csanchez
 * Actualizacion protoripo libGPE
 *
 *
 */
/**
 * This class is a common implementation for all
 * the application content handlers. It has an attribute
 * with the geographical file schema. It has a default
 * implementation for all the methods defined by the 
 * IGPEContentHandler.
 * @author Jorge Piera Llodrá (jorge.piera@iver.es)
 * @author Carlos Sánchez Periñán (sanchez_carper@gva.es)
 */
public abstract class GPEContentHandler implements IGPEContentHandler {
	private IGPEErrorHandler errorHandler = null;
	
	/**
	 * @return the errorHandler
	 */
	public IGPEErrorHandler getErrorHandler() {
		return errorHandler;
	}

	/**
	 * @param errorHandler the errorHandler to set
	 */
	public void setErrorHandler(IGPEErrorHandler errorHandler) {
		this.errorHandler = errorHandler;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.IGPEContentHandler#addBboxToFeature(java.lang.Object, java.lang.Object)
	 */
	public void addBboxToFeature(Object bbox, Object feature) {
		addNotSupportedWarning(FeatureNotSupportedWarning.FEATUEWITHBBOX);		
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.IGPEContentHandler#addBboxToLayer(java.lang.Object, java.lang.Object)
	 */
	public void addBboxToLayer(Object bbox, Object layer) {
		addNotSupportedWarning(FeatureNotSupportedWarning.LAYERWITHBBOX);				
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.IGPEContentHandler#addDescriptionToLayer(java.lang.String, java.lang.Object)
	 */
	public void addDescriptionToLayer(String description, Object layer) {
		addNotSupportedWarning(FeatureNotSupportedWarning.LAYERWITHDESCRIPTION);				
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.IGPEContentHandler#addElementToFeature(java.lang.Object, java.lang.Object)
	 */
	public void addElementToFeature(Object element, Object feature) {
		addNotSupportedWarning(FeatureNotSupportedWarning.FEATUREWITHELEMENTS);				
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.IGPEContentHandler#addFeatureToLayer(java.lang.Object, java.lang.Object)
	 */
	public void addFeatureToLayer(Object feature, Object layer) {
		addNotSupportedWarning(FeatureNotSupportedWarning.LAYERWITHFEATURES);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.IGPEContentHandler#addGeometryToFeature(java.lang.Object, java.lang.Object)
	 */
	public void addGeometryToFeature(Object geometry, Object feature) {
		addNotSupportedWarning(FeatureNotSupportedWarning.FEATUREWITHGEOMETRY);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.IGPEContentHandler#addInnerPolygonToPolygon(java.lang.Object, java.lang.Object)
	 */
	public void addInnerPolygonToPolygon(Object innerPolygon, Object Polygon) {
		addNotSupportedWarning(FeatureNotSupportedWarning.POLYGONWITHINNERPOLYGON);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.IGPEContentHandler#addNameToFeature(java.lang.String, java.lang.Object)
	 */
	public void addNameToFeature(String name, Object feature) {
		addNotSupportedWarning(FeatureNotSupportedWarning.FEATUREWITHNAME);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.IGPEContentHandler#addNameToLayer(java.lang.String, java.lang.Object)
	 */
	public void addNameToLayer(String name, Object layer) {
		addNotSupportedWarning(FeatureNotSupportedWarning.LAYERWITHNAME);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.IGPEContentHandler#addParentElementToElement(java.lang.Object, java.lang.Object)
	 */
	public void addParentElementToElement(Object parent, Object element) {
		addNotSupportedWarning(FeatureNotSupportedWarning.ELEMENTWITHCHILDREN);
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.IGPEContentHandler#addParentLayerToLayer(java.lang.Object, java.lang.Object)
	 */
	public void addParentLayerToLayer(Object parent, Object layer) {
		addNotSupportedWarning(FeatureNotSupportedWarning.LAYERWITHCHILDREN);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.IGPEContentHandler#addSrsToLayer(java.lang.String, java.lang.Object)
	 */
	public void addSrsToLayer(String srs, Object Layer) {
		addNotSupportedWarning(FeatureNotSupportedWarning.LAYERWITHSRS);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.IGPEContentHandler#endBbox(java.lang.Object)
	 */
	public void endBbox(Object bbox) {
				
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.IGPEContentHandler#endElement(java.lang.Object)
	 */
	public void endElement(Object element) {
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.IGPEContentHandler#endFeature(java.lang.Object)
	 */
	public void endFeature(Object feature) {
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.IGPEContentHandler#endInnerPolygon(java.lang.Object)
	 */
	public void endInnerPolygon(Object innerPolygon) {
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.IGPEContentHandler#endLayer(java.lang.Object)
	 */
	public void endLayer(Object layer) {
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.IGPEContentHandler#endLineString(java.lang.Object)
	 */
	public void endLineString(Object lineString) {
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.IGPEContentHandler#endLinearRing(java.lang.Object)
	 */
	public void endLinearRing(Object linearRing) {
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.IGPEContentHandler#endPoint(java.lang.Object)
	 */
	public void endPoint(Object point) {
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.IGPEContentHandler#endPolygon(java.lang.Object)
	 */
	public void endPolygon(Object Polygon) {
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.parser.IGPEContentHandler#startBbox(java.lang.String, org.gvsig.gpe.parser.ICoordinateIterator, java.lang.String)
	 */
	public Object startBbox(String id, ICoordinateIterator coords, String srs) {
		addNotSupportedWarning(FeatureNotSupportedWarning.BBOXCREATION);
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.parser.IGPEContentHandler#startElement(java.lang.String, java.lang.String, java.lang.Object, org.gvsig.gpe.parser.IAttributesIterator, java.lang.Object)
	 */
	public Object startElement(String namespace, String name, Object value, IAttributesIterator attributes,Object parentElement) {
		addNotSupportedWarning(FeatureNotSupportedWarning.ELEMENTCREATION);
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.parser.IGPEContentHandler#startFeature(java.lang.String, java.lang.String, java.lang.String, org.gvsig.gpe.parser.IAttributesIterator, java.lang.Object)
	 */
	public Object startFeature(String id, String namespace, String name, IAttributesIterator attributes, Object layer) {
		addNotSupportedWarning(FeatureNotSupportedWarning.FEATURECREATION);
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.parser.IGPEContentHandler#startInnerPolygon(java.lang.String, org.gvsig.gpe.parser.ICoordinateIterator, java.lang.String)
	 */
	public Object startInnerPolygon(String id, ICoordinateIterator coords, String srs) {
		addNotSupportedWarning(FeatureNotSupportedWarning.INNERBOUNDARYCREATION);
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.parser.IGPEContentHandler#startLayer(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, org.gvsig.gpe.parser.IAttributesIterator, java.lang.Object, java.lang.Object)
	 */
	public Object startLayer(String id, String namespace, String name, String description, String srs, IAttributesIterator attributes, Object parentLayer, Object bBox) {
		addNotSupportedWarning(FeatureNotSupportedWarning.LAYERCREATION);
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.parser.IGPEContentHandler#startLineString(java.lang.String, org.gvsig.gpe.parser.ICoordinateIterator, java.lang.String)
	 */
	public Object startLineString(String id, ICoordinateIterator coords, String srs) {
		addNotSupportedWarning(FeatureNotSupportedWarning.LINESTRINGCREATION);
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.parser.IGPEContentHandler#startLinearRing(java.lang.String, org.gvsig.gpe.parser.ICoordinateIterator, java.lang.String)
	 */
	public Object startLinearRing(String id, ICoordinateIterator coords, String srs) {
		addNotSupportedWarning(FeatureNotSupportedWarning.LINEARRINGCREATION);
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.parser.IGPEContentHandler#startPoint(java.lang.String, org.gvsig.gpe.parser.ICoordinateIterator, java.lang.String)
	 */
	public Object startPoint(String id, ICoordinateIterator coords, String srs) {
		addNotSupportedWarning(FeatureNotSupportedWarning.POINTCREATION);
		return null;
	}


	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.parser.IGPEContentHandler#startPolygon(java.lang.String, org.gvsig.gpe.parser.ICoordinateIterator, java.lang.String)
	 */
	public Object startPolygon(String id, ICoordinateIterator coords, String srs) {
		addNotSupportedWarning(FeatureNotSupportedWarning.POLYGONCREATION);
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.IGPEContentHandler#addGeometryToMultiGeometry(java.lang.Object, java.lang.Object)
	 */
	public void addGeometryToMultiGeometry(Object geometry, Object multiGeometry) {
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.IGPEContentHandlerSFP0#addCurveToMultiCurve(java.lang.Object, java.lang.Object)
	 */
	public void addCurveToMultiCurve(Object curve, Object multiCurve) {
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.IGPEContentHandlerSFP0#startMultiCurve(java.lang.String, java.lang.String)
	 */
	public Object startMultiCurve(String id, String srs) {
		addNotSupportedWarning(FeatureNotSupportedWarning.MULTICURVECREATION);
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.IGPEContentHandlerSFP0#endMultiCurve(java.lang.Object)
	 */
	public void endMultiCurve(Object multiCurve) {
		
	}
	
	
	public Object startCurve(String id, ICoordinateIterator coords, String srs){
		addNotSupportedWarning(FeatureNotSupportedWarning.CURVECREATION);
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.IGPEContentHandlerSFP0#endCurve(java.lang.Object)
	 */
	public void endCurve(Object Curve) {
		
	}
		
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.IGPEContentHandler#addLineStringToMultiLineString(java.lang.Object, java.lang.Object)
	 */
	public void addLineStringToMultiLineString(Object lineString, Object multiLineString) {
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.IGPEContentHandler#addPointToMultiPoint(java.lang.Object, java.lang.Object)
	 */
	public void addPointToMultiPoint(Object point, Object multiPoint) {
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.IGPEContentHandler#addPolygonToMultiPolygon(java.lang.Object, java.lang.Object)
	 */
	public void addPolygonToMultiPolygon(Object polygon, Object multiPolygon) {
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.IGPEContentHandler#endMultiGeometry(java.lang.Object)
	 */
	public void endMultiGeometry(Object multiGeometry) {
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.IGPEContentHandler#endMultiLineString(java.lang.Object)
	 */
	public void endMultiLineString(Object multiLineString) {
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.IGPEContentHandler#endMultiPoint(java.lang.Object)
	 */
	public void endMultiPoint(Object multiPoint) {
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.IGPEContentHandler#endMultiPolygon(java.lang.Object)
	 */
	public void endMultiPolygon(Object multiPolygon) {
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.IGPEContentHandler#startMultiGeometry(java.lang.String, java.lang.String)
	 */
	public Object startMultiGeometry(String id, String srs) {
		addNotSupportedWarning(FeatureNotSupportedWarning.MULTIGEOMETRYCREATION);
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.IGPEContentHandler#startMultiLineString(java.lang.String, java.lang.String)
	 */
	public Object startMultiLineString(String id, String srs) {
		addNotSupportedWarning(FeatureNotSupportedWarning.MULTILINESTRINGCREATION);
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.IGPEContentHandler#startMultiPoint(java.lang.String, java.lang.String)
	 */
	public Object startMultiPoint(String id, String srs) {
		addNotSupportedWarning(FeatureNotSupportedWarning.MULTIPOINTCREATION);
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.IGPEContentHandler#startMultiPolygon(java.lang.String, java.lang.String)
	 */
	public Object startMultiPolygon(String id, String srs) {
		addNotSupportedWarning(FeatureNotSupportedWarning.MULTIPOLYGONCREATION);
		return null;
	}
	
	
	/**
	 * This method adds a warning message into the ErrorHandler
	 * to indicate that the application doesn't supports 
	 * the especified objects
	 * @param objectName
	 * Object name that is not supported
	 */
	private void addNotSupportedWarning(String objectName){
		if (errorHandler != null){
			errorHandler.addWarning(new FeatureNotSupportedWarning(objectName));
		}
	}

	/* (non-Javadoc)
	 * @see org.gvsig.gpe.IGPEContentHandlerSFP0#addSegmentToCurve(java.lang.Object, java.lang.Object)
	 */
	public void addSegmentToCurve(Object segment, Object curve) {
				
	}

	/* (non-Javadoc)
	 * @see org.gvsig.gpe.IGPEContentHandlerSFP0#startCurve(java.lang.String, java.lang.String)
	 */
	public Object startCurve(String id, String srs) {
		addNotSupportedWarning(FeatureNotSupportedWarning.CURVECREATION);
		return null;
	}
	
	
	/* (non-Javadoc)
	 * @see org.gvsig.gpe.parser.IGPEContentHandler#startMetadata(java.lang.String, java.lang.String, org.gvsig.gpe.parser.IAttributesIterator)
	 */
	public Object startMetadata(String type, String data,
			IAttributesIterator attributes) {
		addNotSupportedWarning(FeatureNotSupportedWarning.METADATACREATION);
		return null;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.gpe.parser.IGPEContentHandler#startTime(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Object, java.lang.Object)
	 */
	public Object startTime(String name, String description, String type,
			String time, Object previous, Object next) {
		addNotSupportedWarning(FeatureNotSupportedWarning.TIMECREATION);
		return null;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.gpe.parser.IGPEContentHandler#startTime(java.lang.String, java.lang.String)
	 */
	public Object startTime(String type, String time) {
		addNotSupportedWarning(FeatureNotSupportedWarning.METADATACREATION);
		return null;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.gpe.parser.IGPEContentHandler#addMetadataToFeature(java.lang.Object, java.lang.Object)
	 */
	public void addMetadataToFeature(Object metadata, Object feature) {
		addNotSupportedWarning(FeatureNotSupportedWarning.FEATUREWITHMETADATA);	
	}

	/* (non-Javadoc)
	 * @see org.gvsig.gpe.parser.IGPEContentHandler#addMetadataToMetadata(java.lang.Object, java.lang.Object)
	 */
	public void addMetadataToMetadata(Object metadata, Object parent) {
		addNotSupportedWarning(FeatureNotSupportedWarning.METADATAWITHCHILDREN);	
	}

	/* (non-Javadoc)
	 * @see org.gvsig.gpe.parser.IGPEContentHandler#addTimeToFeature(java.lang.Object, java.lang.Object)
	 */
	public void addTimeToFeature(Object time, Object feature) {
		addNotSupportedWarning(FeatureNotSupportedWarning.FEATUREWITHTIME);		
	}

	/* (non-Javadoc)
	 * @see org.gvsig.gpe.parser.IGPEContentHandler#endMetadata(java.lang.Object)
	 */
	public void endMetadata(Object metadata) {
				
	}

	/* (non-Javadoc)
	 * @see org.gvsig.gpe.parser.IGPEContentHandler#endTime(java.lang.Object)
	 */
	public void endTime(Object time) {
				
	}	
}
