package org.gvsig.fmap.geom.type;

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.exception.CreateGeometryException;
import org.gvsig.fmap.geom.operation.GeometryOperation;

/**
 * This class represents the type of a geometry. All the geometries
 * has to have a type that can be retrieved using the 
 * {@link Geometry}{@link #getGeometryType()} method.
 * 
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public interface GeometryType {
		
	/**
	 * @return the identifier of the geometry type. This identifier
	 * is assigned by the {@link GeometryManager} in run time.
	 */
	public int getId();
	
	/**
	 * @return the name of the geometry type.
	 */
	public String getName();
	
	/**
	 * @return the type of the geometry. It is a constant value
	 * that has to be one of the values in {@link Geometry.TYPES}
	 * The type is an abstract representation of the object (Point, Curve...) 
	 * but it is not a concrete representation (Point2D, Point3D...). 
	 */
	public int getType();	
	
	/**
	 * @return the subtype of the geometry. It is a constant value
	 * that has to be one of the values in {@link Geometry.SUBTYPES}.
	 * The subtype represents a set of geometries with a 
	 * dimensional relationship (2D, 3D, 2DM...)
	 */
	public int getSubType();
	
	/**
	 * This method creates a {@link Geometry} with the type specified 
	 * by this class. The geometry is empty, and all the internal 
	 * attributes must be assigned to a value when the geometry has  
	 * been created.
	 * 
	 * @return
	 * A empty geometry 
	 * @throws InstantiationException
	 * This exception is maybe thrown when  the application is  trying 
	 * to instantiate the geometry
	 * @throws IllegalAccessException
	 * This exception is maybe thrown when  the application is  trying 
	 * to instantiate the geometry
	 */
	public Geometry create() throws CreateGeometryException;
		
	/**
	 * Registers an operation for this geometry type. 
	 * @param index
	 * @param geomOp
	 */
	public void setGeometryOperation(int index, GeometryOperation geomOp);
	
	/**
	 * Get the operation for this geometry at a concrete position
	 * @param index
	 * The position of the operation
	 * @return
	 * A geometry operation
	 */
	public GeometryOperation getGeometryOperation(int index);
		
	/**
	 * @return the geometry as a String
	 */
	public String toString();
	
}
