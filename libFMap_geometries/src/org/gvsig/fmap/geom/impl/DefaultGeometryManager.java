/* gvSIG. Geographic Information System of the Valencian Government
 *
 * Copyright (C) 2007-2008 Infrastructures and Transports Department
 * of the Valencian Government (CIT)
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 *
 */

/*
 * AUTHORS (In addition to CIT):
 * 2009 {Iver T.I.}   {Task}
 */

package org.gvsig.fmap.geom.impl;

import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.Geometry.TYPES;
import org.gvsig.fmap.geom.aggregate.MultiCurve;
import org.gvsig.fmap.geom.aggregate.MultiSurface;
import org.gvsig.fmap.geom.exception.CreateEnvelopeException;
import org.gvsig.fmap.geom.exception.CreateGeometryException;
import org.gvsig.fmap.geom.operation.GeometryOperation;
import org.gvsig.fmap.geom.operation.GeometryOperationContext;
import org.gvsig.fmap.geom.operation.GeometryOperationException;
import org.gvsig.fmap.geom.operation.GeometryOperationNotSupportedException;
import org.gvsig.fmap.geom.primitive.Curve;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.geom.primitive.NullGeometry;
import org.gvsig.fmap.geom.primitive.Point;
import org.gvsig.fmap.geom.primitive.Surface;
import org.gvsig.fmap.geom.primitive.impl.Envelope2D;
import org.gvsig.fmap.geom.primitive.impl.Envelope3D;
import org.gvsig.fmap.geom.type.GeometryType;
import org.gvsig.fmap.geom.type.GeometryTypeNotSupportedException;
import org.gvsig.fmap.geom.type.GeometryTypeNotValidException;
import org.gvsig.fmap.geom.type.impl.DefaultGeometryType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * Default implementation for the {@link GeometryManager}. When the
 * application starts, this class is registered in the
 * {@link GeometryLocator} using the {@link DefaultGeometryLibrary}.
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */

public class DefaultGeometryManager implements GeometryManager{
	private static final Logger logger = LoggerFactory.getLogger(GeometryManager.class);

	private static GeometryManager instance;

	/** This list holds the unique name of all registered geometry operations.
	 * The index in which they are stored is also the operation code used to invoke each one of them */
	private List geometryOperations = new ArrayList();

	/** Common operations are registered here. Type specific operations are registered in the corresponding GeometryType instance */
	//private List commonOperations = new ArrayList();

	/** This map holds the instances of all registered GeometryType. The key is the name of the specific Geometry subclass.
	 * In other words, the string "org.gvsig.fmap.geom.primitive.Point2D" is the hash key to obtain an instance of GeometryType holding the
	 * operations associated to the class org.gvsig.fmap.geom.primitive.Point2D.
	 */
	private Map geometryTypeName = new HashMap();

	/**
	 * This attribute is used to keep the relationship between type,
	 * subtype and GeometryType. The key is the type and the
	 * value is other Map with the GeometryTypes
	 */
	private Map geometrySubtypes = new HashMap();

	/**
	 * This array contains all the geometry types.
	 */
	private ArrayList geometryTypeList = new ArrayList();

	/** GeometryType index counter. Each time a new geometry type (not predefined) is registered it is assigned this counter's value as index and after that
	 * it is incremented by 1 */
	private int geometryTypeIndex = 0;//Geometry.EXTENDED_GEOMTYPE_OFFSET; //65536;//2^16

	/**
	 * Geometry Factory
	 */
	private GeometryFactory factory = new GeometryFactory();

	/**
	 * The GeometryManager has to have a public constructor
	 * that will be invoked by a extension point.
	 */
	public DefaultGeometryManager() {
		this.registerGeometryFactory(new GeometryFactory());
	}

	/**
	 * Registers a GeometryFactory that will be used instead of the default one.
	 * @param factory
	 */
	private void registerGeometryFactory(GeometryFactory factory) {
		this.factory = factory;
	}

	/**
	 * Returns the current geometry factory
	 * @return
	 */
	public GeometryFactory getGeometryFactory() {
		return factory;
	}

	/**
	 * Registers the unique name of one operation. If it already exists then this method does nothing but returning
	 * the name's corresponding index.
	 * @param geomOpName Name used to register the geometry operation
	 * @return index assigned to the operation name passed as parameter
	 */
	private int registerGeometryOperationName(String geomOpName) {
		if (geomOpName == null) {
			throw new IllegalArgumentException("geomOpName cannot be null.");
		}

		int index = geometryOperations.indexOf(geomOpName);
		if (index == -1) {
			geometryOperations.add(geomOpName);
			index = geometryOperations.indexOf(geomOpName);
		}
		return index;
	}


	//	/**
	//	 * Sets a common operation into the common operations map.
	//	 * @param index index in which to set the operation
	//	 * @param geomOp operation to be set
	//	 */
	//	private void setCommonOperation(int index, GeometryOperation geomOp) {
	//
	//		while (index > commonOperations.size()) {
	//			commonOperations.add(null);
	//		}
	//
	//		if (index == commonOperations.size()) {
	//			commonOperations.add(geomOp);
	//		} else {
	//			commonOperations.set(index, geomOp);
	//		}
	//	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.geom.GeometryManager#registerGeometryOperation(java.lang.String, org.gvsig.fmap.geom.operation.GeometryOperation, org.gvsig.fmap.geom.type.GeometryType)
	 */
	public int registerGeometryOperation(String geomOpName,
			GeometryOperation geomOp, GeometryType geomType) {
		if (geomOp == null) {
			throw new IllegalArgumentException("geomOp cannot be null.");
		}
		if (geomType == null) {
			throw new IllegalArgumentException("geomType cannot be null.");
		}

		int index = registerGeometryOperationName(geomOpName);

		geomType.setGeometryOperation(index, geomOp);

		return index;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.geom.GeometryManager#registerGeometryOperation(java.lang.String, org.gvsig.fmap.geom.operation.GeometryOperation)
	 */
	public int registerGeometryOperation(String geomOpName,
			GeometryOperation geomOp) {
		if (geomOpName == null) {
			throw new IllegalArgumentException("geomOpName cannot be null.");
		}
		if (geomOp == null) {
			throw new IllegalArgumentException("geomOp cannot be null.");
		}

		int index = registerGeometryOperationName(geomOpName);

		Iterator it = geometryTypeName.keySet().iterator();
		while (it.hasNext()){
			String className = (String)it.next();
			GeometryType geometryType = (GeometryType) geometryTypeName.get(className);
			registerGeometryOperation(geomOpName, geomOp, geometryType);
		}

		return index;

	}

	/**
	 * Registers a GeometryOperation associated to a GeometryType.
	 * Returns an unique index that is used later to identify and invoke the operation.<br>
	 *
	 * By convention, the return value should be stored in a public constant within the class implementing
	 * the operation:<BR>
	 * <pre>
	 * public class MyOperation extends GeometryOperation {
	 *   public static final int CODE =
	 *     GeometryManager.getInstance()
	 *        .registerGeometryOperation("MyOperation", new MyOperation(), MyGeometry.class);
	 * }
	 * </pre>
	 *
	 * This method is only used if you have not a reference to the GeometryType associated to the
	 * geometry class. If you have such reference then it is slightly faster to use the method that receives
	 * the GeometryType.<br>
	 *
	 * @param geomOpName Operation's unique name
	 * @param geomOp Specific GeometryOperation's instance implementing this operation
	 * @param geomClass Geometry implementation class
	 * @return Index assigned to this operation. This index is used later to access the operation.
	 */
	private int registerGeometryOperation(String geomOpName,
			GeometryOperation geomOp, Class geomClass) {

		GeometryType geomType = getGeometryType(geomClass);
		return registerGeometryOperation(geomOpName, geomOp, geomType);
	}

	/**
	 * Registers a GeometryOperation associated to a GeometryType.
	 * Returns an unique index that is used later to identify and invoke the operation.<br>
	 *
	 * By convention, the return value should be stored in a public constant within the class implementing
	 * the operation:<BR>
	 * <pre>
	 * public class MyOperation extends GeometryOperation {
	 *   public static final int CODE =
	 *     GeometryManager.getInstance()
	 *        .registerGeometryOperation("MyOperation", MyOperation.class, myGeomType);
	 * }
	 * </pre>
	 *
	 * @param geomOpName Operation's unique name
	 * @param geomOpClass GeometryOperation class
	 * @param geomType GeometryType instance to which this operation should be associated
	 * @return Index assigned to this operation. This index is used later to access the operation.
	 * @throws IllegalAccessException, {@link InstantiationException} Either exception maybe thrown when
	 * trying to instance the geometry operation class.
	 */
	private int registerGeometryOperation(String geomOpName, Class geomOpClass,
			GeometryType geomType)
	throws IllegalAccessException, InstantiationException {

		GeometryOperation geomOp = (GeometryOperation) geomOpClass.newInstance();
		return registerGeometryOperation(geomOpName, geomOp, geomType);
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.GeometryManager#registerGeometryOperation(java.lang.String, org.gvsig.fmap.geom.operation.GeometryOperation, int, int)
	 */
	public int registerGeometryOperation(String geomOpName,
			GeometryOperation geomOp, int type, int subType) throws GeometryTypeNotSupportedException, GeometryTypeNotValidException {
		GeometryType geometryType = getGeometryType(type, subType);
		return registerGeometryOperation(geomOpName, geomOp, geometryType);
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.GeometryManager#registerGeometryOperation(java.lang.String, org.gvsig.fmap.geom.operation.GeometryOperation, int)
	 */
	public int registerGeometryOperation(String geomOpName,
			GeometryOperation geomOp, int type) {
		Iterator it = geometryTypeName.keySet().iterator();
		int code = -1;
		while (it.hasNext()){
			String className = (String)it.next();
			GeometryType geometryType = (GeometryType) geometryTypeName.get(className);
			if ((type == geometryType.getType())) {
				code = registerGeometryOperation(geomOpName, geomOp, geometryType);
			}
			{
			}
		}
		return code;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.GeometryManager#registerGeometryOperationBySubtype(java.lang.String, org.gvsig.fmap.geom.operation.GeometryOperation, int)
	 */
	public int registerGeometryOperationBySubtype(String geomOpName,
			GeometryOperation geomOp, int subType) {
		Iterator it = geometryTypeName.keySet().iterator();
		int code = -1;
		while (it.hasNext()){
			String className = (String)it.next();
			GeometryType geometryType = (GeometryType) geometryTypeName.get(className);
			if ((subType == geometryType.getSubType())) {
				code = registerGeometryOperation(geomOpName, geomOp, geometryType);
			}
			{
			}
		}
		return code;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.geom.GeometryManager#registerGeometryType(java.lang.Class, java.lang.String, int)
	 */
	public GeometryType registerGeometryType(Class geomClass, String name, int type, int subType) {
		return registerGeometryType(geomClass, name, geometryTypeIndex++, type, subType);
	}

	/**
	 * Registers a Geometry implementation class with a predefined geometry type and returns the
	 * associated GeometryType instance. Available predefined types are defined in {@link Geometry.TYPES}
	 * If the class is already registered then this method throws an IllegalArgumentException.<br>
	 *
	 * How to register a geometry class with a predefined type:
	 * <pre>
	 *
	 * public class Point2D implements Point {
	 *   private static final GeometryType geomType = GeometryManager.getInstance()
	 *	   .registerBasicGeometryType(Point2D.class, "Point2D", Geometry.TYPES.POINT);
	 *
	 *   public static final int CODE = geomType.getId();
	 * ...
	 *   public int getType() {
	 *      return geomType.getType();
	 *   }
	 * }
	 * </pre>
	 *
	 * @param geomClass
	 *            Geometry subclass. It must not be null and must implement Geometry, otherwise an exception
	 *            is raised.
	 * @param name
	 * 			  Symbolic name for the geometry type, it can be null. If it is null then the symbolic name
	 * 		      will be the simple class name.
	 * @param id
	 * 			  Geometry identifier.
	 * @param type
	 * 			  Type of geometry. Must be a value defined in {@link Geometry.TYPES}
	 * @param subType
	 * SubType of geometry. Must be a value defined in {@link Geometry.SUBTYPES}
	 * @return Instance of GeometryType associated to the Geometry implementation class
	 *         geomClass
	 * @throws IllegalArgumentException
	 *             If geomClass is null or does not implement Geometry
	 */
	private GeometryType registerGeometryType(Class geomClass, String name, int id,
			int type, int subType){
		if (geomClass == null) {
			throw new IllegalArgumentException("geomClass cannot be null.");
		}

		if (!Geometry.class.isAssignableFrom(geomClass)) {
			throw new IllegalArgumentException(geomClass.getName()
					+ " must implement the Geometry interface");
		}

		// Check if it is registered
		GeometryType geomType = (GeometryType) geometryTypeName.get(geomClass
				.getName());

		// If it is not, register it
		if (geomType == null) {
			geomType = new DefaultGeometryType(geomClass, name, id, type, subType);
			geometryTypeName.put(geomClass.getName(), geomType);
			geometryTypeList.add(id, geomType);
			Map subTypes = null;
			Integer key = new Integer(type);
			if (!geometrySubtypes.containsKey(key)){
				subTypes = new HashMap();
				geometrySubtypes.put(key, subTypes);
			}else{
				subTypes = (HashMap)geometrySubtypes.get(key);
			}
			subTypes.put(new Integer(subType), geomType);
		} else {
			// If it is already registered, throw exception
			geometryTypeIndex--;
			//throw new IllegalArgumentException("Attempt to register a geometry type that is already registered: " + geomClass.getName());
		}
		logger.debug("Class " + geomClass + " registered with name " + geomType.getName());

		return geomType;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.geom.GeometryManager#registerGeometryType(java.lang.Class, int)
	 */
	public GeometryType registerGeometryType(Class geomClass, int type, int subType) {
		return registerGeometryType(geomClass, null, type, subType);
	}

	/**
	 * Returns an instance of GeometryType given the associated Geometry implementation
	 * class.
	 *
	 * @param geomClass
	 * @return Instance of GeometryType associated to the Geometry implementation class
	 */
	private GeometryType getGeometryType(Class geomClass) {
		logger.debug("getting " + geomClass.getName());
		return (GeometryType) geometryTypeName.get(geomClass.getName());
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.geom.GeometryManager#getGeometryType(java.lang.String)
	 */
	public GeometryType getGeometryType(String className) throws GeometryTypeNotSupportedException {
		if (!geometryTypeName.containsKey(className)){
			throw new GeometryTypeNotSupportedException(className);
		}
		return (GeometryType) geometryTypeName.get(className);
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.GeometryManager#getGeometryType(int, int)
	 */
	public GeometryType getGeometryType(int type, int subType) throws GeometryTypeNotSupportedException, GeometryTypeNotValidException {
		Iterator it = geometryTypeName.keySet().iterator();
		while (it.hasNext()){
			String className = (String)it.next();
			GeometryType geometryType = getGeometryType(className);
			if ((type == geometryType.getType()) &&
					(subType == geometryType.getSubType())){
				return geometryType;
			}
			if (subType == SUBTYPES.UNKNOWN){
				throw new GeometryTypeNotValidException(type, subType);
			}
		}
		throw new GeometryTypeNotSupportedException(type, subType);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.geom.GeometryManager#create(org.gvsig.fmap.geom.type.GeometryType)
	 */
	public Geometry create(GeometryType geomType) throws CreateGeometryException{
		return geomType.create();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.geom.GeometryManager#create(java.lang.String)
	 */
	public Geometry create(String name) throws CreateGeometryException{
		if (!geometryTypeName.containsKey(name)){
			throw new IllegalArgumentException(name + " has not been registered yet.");
		}
		return ((GeometryType)geometryTypeName.get(name)).create();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.geom.GeometryManager#create(int)
	 */
	public Geometry create(int type, int subType) throws CreateGeometryException{
		if (!geometrySubtypes.containsKey(new Integer(type))){
			throw new IllegalArgumentException("geometry type" + type + " has not been registered yet.");
		}
		Map subtypes = (Map)geometrySubtypes.get(new Integer(type));
		if (!subtypes.containsKey(new Integer(subType))){
			throw new IllegalArgumentException("geometry subtype" + subType + " has not been registered yet.");
		}
		return ((GeometryType)subtypes.get(new Integer(subType))).create();
	}


	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.GeometryManager#createCurve(org.gvsig.fmap.geom.primitive.GeneralPathX, int)
	 */
	public Curve createCurve(GeneralPathX generalPathX, int subType)
	throws CreateGeometryException {
		Curve curve = (Curve)create(TYPES.CURVE, subType);
		curve.setGeneralPath(generalPathX);
		return curve;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.GeometryManager#createNullGeometry(int)
	 */
	public NullGeometry createNullGeometry(int subType)
	throws CreateGeometryException {
		NullGeometry nullGeom = (NullGeometry)create(TYPES.NULL, subType);
		return nullGeom;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.GeometryManager#createPoint(double, double, int)
	 */
	public Point createPoint(double x, double y, int subType)
	throws CreateGeometryException {
		Point point = (Point)create(TYPES.POINT, subType);
		point.setX(x);
		point.setY(y);
		return point;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.GeometryManager#createSurface(org.gvsig.fmap.geom.primitive.GeneralPathX, int)
	 */
	public Surface createSurface(GeneralPathX generalPathX, int subType)
	throws CreateGeometryException {
		Surface surface = (Surface)create(TYPES.SURFACE, subType);
		surface.setGeneralPath(generalPathX);
		return surface;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.GeometryManager#getGeometryOperation(int, int, int)
	 */
	public GeometryOperation getGeometryOperation(int opCode, int type,
			int subType) throws GeometryTypeNotSupportedException,
			GeometryOperationNotSupportedException, GeometryTypeNotValidException {
		GeometryType geometryType = getGeometryType(type, subType);
		return geometryType.getGeometryOperation(opCode);
	}

	//	/*
	//	 * (non-Javadoc)
	//	 * @see org.gvsig.fmap.geom.GeometryManager#getGeometryOperation(java.lang.Class, int)
	//	 */
	//	private GeometryOperation getGeometryOperation(Class geomClass, int opCode) throws GeometryTypeNotSupportedException, GeometryOperationNotSupportedException {
	//
	//		GeometryOperation geomOp = null;
	//
	//		// Check if it is a common operation, and if so, get it from the common registry
	//		if (opCode >= COMMON_OPS_OFFSET) {
	//			geomOp = ((GeometryOperation)commonOperations.get(opCode - COMMON_OPS_OFFSET));
	//
	//			if (geomOp == null) {
	//				throw new GeometryOperationNotSupportedException(opCode);
	//			}
	//		} else {
	//			// If it is type specific, get it from its type	registry
	//			if (geomClass != null) {
	//				GeometryType geomType = getGeometryType(geomClass);
	//
	//				// If the geometry type is not registered, throw an exception
	//				if (geomType == null) {
	//					throw new GeometryTypeNotSupportedException(geomClass);
	//				}
	//
	//				// Get the operation
	//				geomOp = geomType.getGeometryOperation(opCode);
	//
	//				// If the operation is not registered throw an exception
	//				if (geomOp == null) {
	//					throw new GeometryOperationNotSupportedException(opCode, geomType);
	//				}
	//			}
	//		}
	//		return geomOp;
	//	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.geom.GeometryManager#invokeOperation(int, org.gvsig.fmap.geom.Geometry, org.gvsig.fmap.geom.operation.GeometryOperationContext)
	 */
	public Object invokeOperation(int opCode, Geometry geom, GeometryOperationContext ctx) throws GeometryOperationNotSupportedException, GeometryOperationException {
		GeometryOperation geomOp = geom.getGeometryType().getGeometryOperation(opCode);;

		if (geomOp != null) {
			return geomOp.invoke(geom, ctx);
		}

		throw new GeometryOperationNotSupportedException(opCode, geom.getGeometryType());
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.GeometryManager#invokeOperation(java.lang.String, org.gvsig.fmap.geom.Geometry, org.gvsig.fmap.geom.operation.GeometryOperationContext)
	 */
	public Object invokeOperation(String geomOpName, Geometry geom,
			GeometryOperationContext ctx)
	throws GeometryOperationNotSupportedException,
	GeometryOperationException {
		int index = geometryOperations.indexOf(geomOpName);
		if (index == -1){
			throw new GeometryOperationNotSupportedException(-1);
		}
		return invokeOperation(index, geom, ctx);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.geom.GeometryManager#unregisterGeometryType(java.lang.Class)
	 */
	public GeometryType unregisterGeometryType(Class geomClass) {
		return (GeometryType) geometryTypeName.remove(geomClass.getName());
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.GeometryManager#createEnvelope(int)
	 */
	public Envelope createEnvelope(int subType) {
		//TODO: register the envelopes!!!
		switch (subType){
		case SUBTYPES.GEOM2DZ:
		case SUBTYPES.GEOM3D:
			return new Envelope3D();
		default:
			return new Envelope2D();
		}
	}


	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.GeometryManager#createEnvelope(double, double, double, double)
	 */
	public Envelope createEnvelope(double minX, double minY, double maxX,
			double maxY, int subType) throws CreateEnvelopeException {
		Envelope envelope = null;
		org.gvsig.fmap.geom.primitive.Point min = null;
		org.gvsig.fmap.geom.primitive.Point max = null;
		try {
			min = (org.gvsig.fmap.geom.primitive.Point)create(TYPES.POINT, subType);
			min.setX(minX);
			min.setY(minY);
			max = (org.gvsig.fmap.geom.primitive.Point)create(TYPES.POINT, subType);
			max.setX(maxX);
			max.setY(maxY);
		}catch (CreateGeometryException e){
			throw new CreateEnvelopeException(subType, e);
		}
		envelope = createEnvelope(subType);
		envelope.setLowerCorner(min);
		envelope.setUpperCorner(max);
		return envelope;
	}

	public MultiCurve createMultiCurve(GeneralPathX generalPathX, int subType)
			throws CreateGeometryException {
		if (subType != SUBTYPES.GEOM2D){
			// FIXME Exception
			throw new UnsupportedOperationException();
		}
		MultiCurve multiCurve = (MultiCurve) create(TYPES.MULTICURVE, subType);
		PathIterator piter = generalPathX.getPathIterator(null);
		GeneralPathX tmpPath = null;
		Curve tmpCurve = null;
		double[] coords = new double[6];
		double[] first = new double[6];
		int type;
		while (!piter.isDone()) {
			type = piter.currentSegment(coords);
			switch (type) {
			case PathIterator.SEG_MOVETO:
				if (tmpPath != null){
					tmpCurve = createCurve(tmpPath, subType);
					multiCurve.addCurve(tmpCurve);
				}
				System.arraycopy(coords, 0, first, 0, 2);
				tmpPath =  new GeneralPathX(piter.getWindingRule());
				tmpPath.moveTo(coords[0], coords[1]);
				break;


			case PathIterator.SEG_LINETO:
				if (tmpPath == null) {
					System.arraycopy(coords, 0, first, 0, 2);
					tmpPath = new GeneralPathX(piter.getWindingRule());
				}
				tmpPath.lineTo(coords[0], coords[1]);
				break;


			case PathIterator.SEG_QUADTO:
				if (tmpPath == null) {
					System.arraycopy(coords, 0, first, 0, 2);
					tmpPath = new GeneralPathX(piter.getWindingRule());
				}
				tmpPath.quadTo(coords[0], coords[1], coords[2], coords[3]);
				break;

			case PathIterator.SEG_CUBICTO:
				if (tmpPath == null) {
					System.arraycopy(coords, 0, first, 0, 2);
					tmpPath = new GeneralPathX(piter.getWindingRule());
				}
				tmpPath.curveTo(coords[0], coords[1], coords[2], coords[3],
						coords[4], coords[5]);
				 break;

			case PathIterator.SEG_CLOSE:
				tmpPath.lineTo(first[0], first[1]);
				 break;

			} // end switch

			piter.next();

		}
		if (tmpPath != null) {
			tmpCurve = createCurve(tmpPath, subType);
			multiCurve.addCurve(tmpCurve);
		}

		return multiCurve;

	}

	public MultiSurface createMultiSurface(GeneralPathX generalPathX,
			int subType)
			throws CreateGeometryException {
		if (subType != SUBTYPES.GEOM2D) {
			// FIXME Exception
			throw new UnsupportedOperationException();
		}
		MultiSurface multiSurface = (MultiSurface) create(TYPES.MULTISURFACE,
				subType);
		PathIterator piter = generalPathX.getPathIterator(null);
		GeneralPathX tmpPath = null;
		Surface tmpSurface = null;
		double[] coords = new double[6];
		double[] first = new double[6];
		int type;
		while (!piter.isDone()) {
			type = piter.currentSegment(coords);
			switch (type) {
			case PathIterator.SEG_MOVETO:
				if (tmpPath != null) {
					tmpSurface = createSurface(tmpPath, subType);
					multiSurface.addSurface(tmpSurface);
				}
				System.arraycopy(coords, 0, first, 0, 2);
				tmpPath = new GeneralPathX(piter.getWindingRule());
				tmpPath.moveTo(coords[0], coords[1]);

			case PathIterator.SEG_LINETO:
				if (tmpPath == null) {
					System.arraycopy(coords, 0, first, 0, 2);
					tmpPath = new GeneralPathX(piter.getWindingRule());
				}
				tmpPath.lineTo(coords[0], coords[1]);
				break;


			case PathIterator.SEG_QUADTO:
				if (tmpPath == null) {
					System.arraycopy(coords, 0, first, 0, 2);
					tmpPath = new GeneralPathX(piter.getWindingRule());
				}
				tmpPath.quadTo(coords[0], coords[1], coords[2], coords[3]);
				break;

			case PathIterator.SEG_CUBICTO:
				if (tmpPath == null) {
					System.arraycopy(coords, 0, first, 0, 2);
					tmpPath = new GeneralPathX(piter.getWindingRule());
				}
				tmpPath.curveTo(coords[0], coords[1], coords[2], coords[3],
						coords[4], coords[5]);
				break;

			case PathIterator.SEG_CLOSE:
				tmpPath.lineTo(first[0], first[1]);
				break;
			} // end switch

			piter.next();

		}
		if (tmpPath != null) {
			tmpSurface = createSurface(tmpPath, subType);
			multiSurface.addSurface(tmpSurface);
		}

		return multiSurface;

	}
}

