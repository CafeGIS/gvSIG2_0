package org.gvsig.fmap.dal.store.gpe.handlers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.gvsig.fmap.crs.CRSFactory;
import org.gvsig.fmap.dal.DataTypes;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.EditableFeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.EditableFeatureType;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.feature.spi.FeatureProvider;
import org.gvsig.fmap.dal.feature.spi.FeatureStoreProviderServices;
import org.gvsig.fmap.dal.store.gpe.GPEStoreProvider;
import org.gvsig.fmap.dal.store.gpe.model.GPEElement;
import org.gvsig.fmap.dal.store.gpe.model.GPEFeature;
import org.gvsig.fmap.dal.store.gpe.xml.XMLSchemaManager;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.Geometry.TYPES;
import org.gvsig.fmap.geom.aggregate.MultiCurve;
import org.gvsig.fmap.geom.aggregate.MultiPoint;
import org.gvsig.fmap.geom.aggregate.MultiSurface;
import org.gvsig.fmap.geom.exception.CreateEnvelopeException;
import org.gvsig.fmap.geom.exception.CreateGeometryException;
import org.gvsig.fmap.geom.primitive.Curve;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.geom.primitive.Point;
import org.gvsig.fmap.geom.primitive.Surface;
import org.gvsig.gpe.parser.GPEContentHandler;
import org.gvsig.gpe.parser.GPEErrorHandler;
import org.gvsig.gpe.parser.IAttributesIterator;
import org.gvsig.gpe.parser.ICoordinateIterator;
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
 * $Id$
 * $Log$
 *
 */
/**
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class FmapContentHandler extends GPEContentHandler {
	private HashMap featureSet = null;
	private EditableFeatureType featureType = null;
	private FeatureStoreProviderServices store = null;
	private GPEStoreProvider storeProvider = null;
	private GeometryManager geomManager = GeometryLocator.getGeometryManager();
	private int featureId = 0;
	private Envelope envelope = null;
	private File file = null;
	private XMLSchemaManager schemaManager = null;

	public FmapContentHandler(GPEErrorHandler errorHandler, FeatureStoreProviderServices store,
			GPEStoreProvider storeProvider, File file) {
		super();
		setErrorHandler(errorHandler);
		this.store = store;
		this.storeProvider = storeProvider;
		this.file = file;
		schemaManager = new XMLSchemaManager();
	}

	public int getFeaturesCount(){
		return featureSet.size();
	}

	public Feature getFeatureAt(int index){
		return (Feature)featureSet.get(new Integer(index));
	}

	public EditableFeatureType getFeatureType(){
		return featureType;
	}


	/**
	 * @return the featureSet
	 */
	public HashMap getFeatureSet() {
		return featureSet;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.parser.GPEContentHandler#startLayer(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, org.gvsig.gpe.parser.IAttributesIterator, java.lang.Object, java.lang.Object)
	 */
	public Object startLayer(String id, String namespace, String name,
			String description, String srs, IAttributesIterator iterator, Object parentLayer, Object box) {
		if (featureSet == null){
			featureSet = new HashMap();
		}
		try {
			schemaManager = new XMLSchemaManager();
			schemaManager.addSchemas(iterator, file);
		} catch (Exception e) {
			getErrorHandler().addError(e);
		}
		return featureSet;
	}



	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.IGPEContentHandler#endLayer(java.lang.Object)
	 */
	public void endLayer(Object layer) {
		storeProvider.setEnvelope(envelope);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.parser.GPEContentHandler#startPoint(java.lang.String, org.gvsig.gpe.parser.ICoordinateIterator, java.lang.String)
	 */
	public Object startPoint(String id, ICoordinateIterator coords, String srs) {
		return createPoint(coords);
	}

	private Point createPoint(ICoordinateIterator coords){
		double[] buffer = new double[coords.getDimension()];
		try {
			coords.hasNext();
			coords.next(buffer);
			Point point = null;
			if (coords.getDimension() == 2){
				point = (Point) geomManager.create(TYPES.POINT, SUBTYPES.GEOM2D);
			}else if (coords.getDimension() == 3){
				point = (Point) geomManager.create(TYPES.POINT, SUBTYPES.GEOM2DZ);
			}
			for (int i=0 ; i<buffer.length ; i++){
				point.setCoordinateAt(i, buffer[i]);
			}
			return point;
		} catch (IOException e) {
			getErrorHandler().addError(e);
		} catch (CreateGeometryException e) {
			getErrorHandler().addError(e);
		}
		return null;
	}

	private Point createPoint(double[] buffer){
		try {
			Point point = null;
			if (buffer.length == 2){
				point = (Point) geomManager.create(TYPES.POINT, SUBTYPES.GEOM2D);
			}else if (buffer.length == 3){
				point = (Point) geomManager.create(TYPES.POINT, SUBTYPES.GEOM2DZ);
			}
			for (int i=0 ; i<buffer.length ; i++){
				point.setCoordinateAt(i, buffer[i]);
			}
			return point;
		}catch (CreateGeometryException e) {
			getErrorHandler().addError(e);
		}
		return null;
	}


	/* (non-Javadoc)
	 * @see org.gvsig.gpe.parser.GPEContentHandler#startLineString(java.lang.String, org.gvsig.gpe.parser.ICoordinateIterator, java.lang.String)
	 */
	public Object startLineString(String id, ICoordinateIterator coords,
			String srs) {
		return createCurve(coords);
	}

	private Curve createCurve(ICoordinateIterator coords){
		GeneralPathX gp = createGeneralPathX(coords);
		try {
			Curve curve = null;
			if (coords.getDimension() == 2){
				curve = (Curve) geomManager.create(TYPES.CURVE, SUBTYPES.GEOM2D);
			}else if (coords.getDimension() == 3){
				curve = (Curve) geomManager.create(TYPES.CURVE, SUBTYPES.GEOM2DZ);
			}
			curve.setGeneralPath(gp);
			return curve;
		} catch (CreateGeometryException e) {
			getErrorHandler().addError(e);
		}
		return null;
	}

	private GeneralPathX createGeneralPathX(ICoordinateIterator coords){
		GeneralPathX gp = new GeneralPathX();
		if (coords == null){
			return gp;
		}
		double[] buffer = new double[coords.getDimension()];
		try {
			if (coords.hasNext()){
				coords.next(buffer);
				gp.moveTo(buffer[0], buffer[1]);
			}
			while(coords.hasNext()){
				coords.next(buffer);
				gp.lineTo(buffer[0], buffer[1]);
			}
		} catch (IOException e) {
			getErrorHandler().addError(e);
		}
		return gp;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.gpe.parser.GPEContentHandler#startPolygon(java.lang.String, org.gvsig.gpe.parser.ICoordinateIterator, java.lang.String)
	 */

	public Object startPolygon(String id, ICoordinateIterator coords, String srs) {
		return createSurface(coords);
	}

	private Surface createSurface(ICoordinateIterator coords){
		GeneralPathX gp = createGeneralPathX(coords);
		try {
			Surface surface = null;
			if (coords.getDimension() == 2){
				surface = (Surface) geomManager.create(TYPES.SURFACE, SUBTYPES.GEOM2D);
			}else if (coords.getDimension() == 3){
				surface = (Surface) geomManager.create(TYPES.SURFACE, SUBTYPES.GEOM2DZ);
			}
			surface.setGeneralPath(gp);
			return surface;
		} catch (CreateGeometryException e) {
			getErrorHandler().addError(e);
		}
		return null;
	}


	/* (non-Javadoc)
	 * @see org.gvsig.gpe.parser.GPEContentHandler#startInnerPolygon(java.lang.String, org.gvsig.gpe.parser.ICoordinateIterator, java.lang.String)
	 */
	public Object startInnerPolygon(String id, ICoordinateIterator coords,
			String srs) {
		return createSurface(coords);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.IGPEContentHandler#addGeometryToFeature(java.lang.Object, java.lang.Object)
	 */
	public void addGeometryToFeature(Object geometry, Object feature) {
		((GPEFeature)feature).setGeometry((Geometry)geometry);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.IGPEContentHandler#addBboxToLayer(java.lang.Object, java.lang.Object)
	 */
	public void addBboxToLayer(Object bbox, Object layer) {
		//		if (layer != null){
		//		GPEBBox gpeBBox = (GPEBBox)bbox;
		//		if (gpeBBox.getSrs() != null){
		//		IProjection projection = null;
		//		try{
		//		CRSFactory.getCRS(gpeBBox.getSrs());
		//		}catch(Exception e){
		//		//If the CRS factory has an error.
		//		}
		//		if ((projection != null) && (!(projection.equals(((FLayer)layer).getProjection())))){
		//		//TODO reproyectar la bbox y asignarsela a la capa
		//		}
		//		}
		//		((IGPEDriver)layer).setExtent(gpeBBox.getBbox2D());
		//		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.IGPEContentHandler#addElementToFeature(java.lang.Object, java.lang.Object)
	 */
	public void addElementToFeature(Object element, Object feature) {
		GPEElement gpeElement = (GPEElement)element;
		((GPEFeature)feature).addElement(gpeElement);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.IGPEContentHandler#addFeatureToLayer(java.lang.Object, java.lang.Object)
	 */
	public void addFeatureToLayer(Object feature, Object layer) {
		GPEFeature gpefeature = (GPEFeature)feature;
		Envelope currentEnvelope = gpefeature.getGeometry().getEnvelope();
		if (featureType == null){
			Map elements = gpefeature.getelements();
			Iterator keys = elements.keySet().iterator();
			featureType = store.createFeatureType();
			EditableFeatureAttributeDescriptor attributeDescriptor = featureType.add("the_geom", DataTypes.GEOMETRY).setGeometryType(TYPES.GEOMETRY);
			attributeDescriptor.setGeometrySubType(SUBTYPES.GEOM2D);
			attributeDescriptor.setSRS(CRSFactory.getCRS("EPSG:23030"));
			featureType.setDefaultGeometryAttributeName("the_geom");
			featureType.setHasOID(true);
			while (keys.hasNext()){
				String elementName = (String)keys.next();
				featureType.add(elementName, DataTypes.STRING);
			}
			FeatureType defaultType = featureType.getNotEditableCopy();
			List types = new ArrayList(1);
			types.add(defaultType);
			this.store.setFeatureTypes(types, defaultType);

			//Initialize the envelope
			try {
				envelope = geomManager.createEnvelope(SUBTYPES.GEOM2D);
			} catch (CreateEnvelopeException e) {
				getErrorHandler().addError(e);
			}
			//Calculates the envelope
			envelope = currentEnvelope;
		}else{
			//Calculates the envelope
			envelope.add(currentEnvelope);
		}
		//If it is null is a multilayer: not supported yet
		if (layer != null){
			FeatureProvider data;
			try {
				data = store.createDefaultFeatureProvider(featureType
						.getNotEditableCopy());
				Map elements = gpefeature.getelements();
				Iterator keys = elements.keySet().iterator();
				data.setDefaultGeometry(gpefeature.getGeometry());
				data.set("the_geom", gpefeature.getGeometry());
				data.setOID(gpefeature.getId());
				while (keys.hasNext()){
					String elementName = (String)keys.next();
					data.set(elementName, ((GPEElement)elements.get(elementName)).getValue());
				}
				Feature dalFeature = store.createFeature(data);
				((HashMap)layer).put(gpefeature.getId(),dalFeature);

			} catch (DataException e) {
				getErrorHandler().addError(e);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.IGPEContentHandler#addInnerPolygonToPolygon(java.lang.Object, java.lang.Object)
	 */
	public void addInnerPolygonToPolygon(Object innerPolygon, Object Polygon) {
		//((Geometry)Polygon).addGeometry((GPEGeometry)innerPolygon);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.IGPEContentHandler#addNameToFeature(java.lang.String, java.lang.Object)
	 */
	public void addNameToFeature(String name, Object feature) {

	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.IGPEContentHandler#addParentElementToElement(java.lang.Object, java.lang.Object)
	 */
	public void addParentElementToElement(Object parent, Object element) {

	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.IGPEContentHandler#addSrsToLayer(java.lang.String, java.lang.Object)
	 */
	public void addSrsToLayer(String srs, Object Layer) {
		//		this.srs = srs;
	}


	/* (non-Javadoc)
	 * @see org.gvsig.gpe.parser.GPEContentHandler#startBbox(java.lang.String, org.gvsig.gpe.parser.ICoordinateIterator, java.lang.String)
	 */
	public Object startBbox(String id, ICoordinateIterator coords, String srs) {
		Envelope envelope = null;
		try {
			if (coords.getDimension() == 2){
				envelope = geomManager.createEnvelope(SUBTYPES.GEOM2D);
			}else if (coords.getDimension() == 3){
				envelope = geomManager.createEnvelope(SUBTYPES.GEOM2DZ);
			}
			double[] buffer = new double[coords.getDimension()];

			if (coords.hasNext()){
				coords.next(buffer);
				envelope.setLowerCorner(createPoint(buffer));
			}
			if (coords.hasNext()){
				coords.next(buffer);
				envelope.setUpperCorner(createPoint(buffer));
			}
		} catch (IOException e) {
			getErrorHandler().addError(e);
		} catch (CreateEnvelopeException e) {
			getErrorHandler().addError(e);
		}
		return envelope;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.parser.GPEContentHandler#startElement(java.lang.String, java.lang.String, java.lang.Object, org.gvsig.gpe.parser.IAttributesIterator, java.lang.Object)
	 */
	public Object startElement(String namespace, String name, Object value,
			IAttributesIterator attributesIterator,	Object parentElement) {
		return new GPEElement(name, (String)value, (GPEElement)parentElement);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.parser.GPEContentHandler#startFeature(java.lang.String, java.lang.String, java.lang.String, org.gvsig.gpe.parser.IAttributesIterator, java.lang.Object)
	 */
	public Object startFeature(String id, String namespace, String name,
			IAttributesIterator attributes, Object layer){
		Long lId;
		if (id == null){
			lId = new Long(featureId);
			featureId++;
		}else{
			try{
				lId = new Long(Long.parseLong(id));
			}catch(Exception e){
				lId = new Long(featureId);
				featureId++;
			}
		}
		//Check the xml schema...
//		for (int i=0 ; i<schemas.size() ; i++){
//			IXSSchema schema = schemas.get(i);
//			IXSElementDeclaration element = schema.getElementDeclarationByName(namespace, name);
//			if (element != null){
//				featureType = store.createFeatureType();
//				IXSTypeDefinition type = element.getTypeDefinition();
//				if (type instanceof IXSComplexTypeDefinition){
//					IXSComplexTypeDefinition complexType = (IXSComplexTypeDefinition)type;
//					complexType.getContentType().
//				}
//
//				featureType.add("the_geom", DataTypes.GEOMETRY).setGeometryType(TYPES.GEOMETRY);
//				featureType.setDefaultGeometryAttributeName("the_geom");
//				featureType.setHasOID(true);
//				while (keys.hasNext()){
//					String elementName = (String)keys.next();
//					featureType.add(elementName, DataTypes.STRING);
//				}
//				FeatureType defaultType = featureType.getNotEditableCopy();
//				List types = new ArrayList(1);
//				types.add(defaultType);
//				this.store.setFeatureTypes(types, defaultType);
//
//				//Initialize the envelope
//				try {
//					envelope = geomManager.createEnvelope(SUBTYPES.GEOM2D);
//				} catch (CreateEnvelopeException e) {
//					getErrorHandler().addError(e);
//				}
//			}
//		}
		return new GPEFeature(lId,
				name, name);
	}

	/* (non-Javadoc)
	 * @see org.gvsig.gpe.parser.GPEContentHandler#startLinearRing(java.lang.String, org.gvsig.gpe.parser.ICoordinateIterator, java.lang.String)
	 */
	public Object startLinearRing(String id, ICoordinateIterator coords,
			String srs) {
		return createSurface(coords);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.IGPEContentHandler#startMultiPoint(java.lang.String, java.lang.String)
	 */
	public Object startMultiPoint(String id, String srs) {
		try {
			return geomManager.create(TYPES.MULTIPOINT, SUBTYPES.GEOM2D);
		} catch (CreateGeometryException e) {
			getErrorHandler().addError(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.IGPEContentHandler#addPointToMultiPoint(java.lang.Object, java.lang.Object)
	 */
	public void addPointToMultiPoint(Object point, Object multiPoint) {
		((MultiPoint)multiPoint).addPoint((Point)point);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.IGPEContentHandler#startMultiLineString(java.lang.String, java.lang.String)
	 */
	public Object startMultiLineString(String id, String srs) {
		super.startMultiLineString(id, srs);
		try {
			return geomManager.create(TYPES.MULTICURVE, SUBTYPES.GEOM2D);
		} catch (CreateGeometryException e) {
			getErrorHandler().addError(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.IGPEContentHandler#addLineStringToMultiLineString(java.lang.Object, java.lang.Object)
	 */
	public void addLineStringToMultiLineString(Object lineString, Object multiLineString) {
		((MultiCurve)multiLineString).addCurve((Curve)lineString);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.IGPEContentHandler#startMultiPolygon(java.lang.String, java.lang.String)
	 */
	public Object startMultiPolygon(String id, String srs) {
		super.startMultiPolygon(id, srs);
		try {
			return geomManager.create(TYPES.MULTISURFACE, SUBTYPES.GEOM2D);
		} catch (CreateGeometryException e) {
			getErrorHandler().addError(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.IGPEContentHandler#addPolygonToMultiPolygon(java.lang.Object, java.lang.Object)
	 */
	public void addPolygonToMultiPolygon(Object polygon, Object multiPolygon) {
		((MultiSurface)multiPolygon).addSurface((Surface)polygon);
	}

	/* (non-Javadoc)
	 * @see org.gvsig.gpe.GPEContentHandler#addCurveToMultiCurve(java.lang.Object, java.lang.Object)
	 */
	public void addCurveToMultiCurve(Object curve, Object multiCurve) {
		((MultiCurve)multiCurve).addCurve((Curve)curve);
	}

	/* (non-Javadoc)
	 * @see org.gvsig.gpe.GPEContentHandler#addSegmentToCurve(java.lang.Object, java.lang.Object)
	 */
	public void addSegmentToCurve(Object segment, Object curve) {
		((Curve)curve).addVertex((Point)segment);
	}

	/* (non-Javadoc)
	 * @see org.gvsig.gpe.parser.GPEContentHandler#startCurve(java.lang.String, org.gvsig.gpe.parser.ICoordinateIterator, java.lang.String)
	 */
	public Object startCurve(String id, ICoordinateIterator coords, String srs) {
		return createCurve(coords);
	}

	/* (non-Javadoc)
	 * @see org.gvsig.gpe.GPEContentHandler#startCurve(java.lang.String, java.lang.String)
	 */
	public Object startCurve(String id, String srs) {
		return createCurve(null);
	}

	/* (non-Javadoc)
	 * @see org.gvsig.gpe.GPEContentHandler#startMultiCurve(java.lang.String, java.lang.String)
	 */
	public Object startMultiCurve(String id, String srs) {
		try {
			return geomManager.create(TYPES.MULTICURVE, SUBTYPES.GEOM2D);
		} catch (CreateGeometryException e) {
			getErrorHandler().addError(e);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.gpe.GPEContentHandler#addGeometryToMultiGeometry(java.lang.Object, java.lang.Object)
	 */
	public void addGeometryToMultiGeometry(Object geometry, Object multiGeometry) {
		((MultiCurve)multiGeometry).addCurve((Curve)geometry);
	}

	/* (non-Javadoc)
	 * @see org.gvsig.gpe.GPEContentHandler#startMultiGeometry(java.lang.String, java.lang.String)
	 */
	public Object startMultiGeometry(String id, String srs) {
		try {
			return geomManager.create(TYPES.AGGREGATE, SUBTYPES.GEOM2D);
		} catch (CreateGeometryException e) {
			getErrorHandler().addError(e);
		}
		return null;
	}

	/**
	 * @return a new OID
	 */
	public Object createNewOID() {
		featureId++;
		return String.valueOf(featureId);
	}

}
