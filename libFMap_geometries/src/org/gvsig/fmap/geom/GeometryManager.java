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
package org.gvsig.fmap.geom;

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
import org.gvsig.fmap.geom.type.GeometryType;
import org.gvsig.fmap.geom.type.GeometryTypeNotSupportedException;
import org.gvsig.fmap.geom.type.GeometryTypeNotValidException;

/**
 * This singleton provides a centralized access to gvSIG's Geometry Model.
 * Its responsibilities are:<br>
 *
 * <ul>
 * <li>Offering a set of convenient methods for registering and retrieving geometry types.
 * <li>Offering a set of convenient methods for registering and retrieving geometry operations associated
 * to one or more geometry types.
 * <li>Offering a set of convenient methods for registering and retrieving new geometries.
 * </ul>
 *
 * @author jiyarza
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public interface GeometryManager {
	/**
	 * <p>
	 * Registers a GeometryOperation associated to a GeometryType.
	 * Returns an unique index that is used later to identify and invoke the operation.
	 * </p>
	 * <p>
	 * By convention, the return value should be stored in a public constant within the class implementing
	 * the operation:<BR>
	 * <pre>
	 * public class MyOperation extends GeometryOperation {
	 *   public static final int CODE =
	 *     GeometryLocator.getGeometryManager()
	 *        .registerGeometryOperation("MyOperation", new MyOperation(), geomType);
	 * }
	 * </pre>
	 * </p>
	 * @param geomOpName Operation's unique name
	 * @param geomOp Specific GeometryOperation's instance implementing this operation
	 * @param geomType GeometryType instance to which this operation should be associated
	 * @return Index assigned to this operation. This index is used later to access the operation.
	 *
	 */
	public int registerGeometryOperation(String geomOpName,
			GeometryOperation geomOp, GeometryType geomType);

	/**
	 * <p>
	 * Registers a GeometryOperation that is common for all GeometryType (registered yet or not).
	 * Returns an unique index that is used later to identify and invoke the operation.
	 * </p>
	 * <p>
	 * By convention, the return value should be stored in a public constant within the class implementing
	 * the operation:<BR>
	 * <pre>
	 * public class MyOperation extends GeometryOperation {
	 *   public static final int CODE =
	 *     GeometryLocator.getGeometryManager()
	 *        .registerGeometryOperation("MyOperation", new MyOperation());
	 * }
	 * </pre>
	 * </p>
	 * @param geomOpName Operation's unique name
	 * @param geomOp Specific GeometryOperation's instance implementing this operation
	 * @return Index assigned to this operation. This index is used later to access the operation.
	 */
	public int registerGeometryOperation(String geomOpName,
			GeometryOperation geomOp);

	/**
	 * <p>
	 * Registers a GeometryOperation associated to a GeometryType, that has been specified
	 * using the type code and the subtype code.
	 * Returns an unique index that is used later to identify and invoke the operation.
	 * </p>
	 * <p>
	 * By convention, the return value should be stored in a public constant within the class implementing
	 * the operation:<BR>
	 * <pre>
	 * public class MyOperation extends GeometryOperation {
	 *   public static final int CODE =
	 *     GeometryLocator.getGeometryManager()
	 *        .registerGeometryOperation("MyOperation", new MyOperation(), TYPES.POINT, SUBTYPES.GEOM2D);
	 * }
	 * </pre>
	 * </p>
	 * <p>
	 * This method is only used if you have not a reference to the GeometryType associated to the
	 * geometry class. If you have such reference then it is slightly faster to use the method that receives
	 * the GeometryType.<br>
	 * </p>
	 * @param geomOpName Operation's unique name
	 * @param geomOp Specific GeometryOperation's instance implementing this operation
	 * @param type
	 * Type of geometry. Must be a value defined in {@link Geometry.TYPES}
	 * @param subType
	 * SubType of geometry. Must be a value defined in {@link Geometry.SUBTYPES}
	 * @return Index assigned to this operation. This index is used later to access the operation.
	 * @throws GeometryTypeNotSupportedException
	 * Returns this exception if there is not a registered geometry with
	 * these type and subtype
	 * @throws GeometryTypeNotValidException
	 * Returns if the type and subtype are not valid
	 */
	public int registerGeometryOperation(String geomOpName,
			GeometryOperation geomOp, int type, int subType) throws GeometryTypeNotSupportedException, GeometryTypeNotValidException;

	/**
	 * <p>
	 * Registers a GeometryOperation associated to all the geometries with a concrete type.
	 * Returns an unique index that is used later to identify and invoke the operation.<br>
	 * </p>
	 * <p>
	 * By convention, the return value should be stored in a public constant within the class implementing
	 * the operation:<BR>
	 * <pre>
	 * public class MyOperation extends GeometryOperation {
	 *   public static final int CODE =
	 *     GeometryLocator.getGeometryManager()
	 *        .registerGeometryOperation("MyOperation", new MyOperation(), TYPES.POINT);
	 * }
	 * </pre>
	 * </p>
	 * @param geomOpName Operation's unique name
	 * @param geomOp Specific GeometryOperation's instance implementing this operation
	 * @param type
	 * Type of geometry. Must be a value defined in {@link Geometry.TYPES}
	 * @return Index assigned to this operation. This index is used later to access the operation.
	 */
	public int registerGeometryOperation(String geomOpName,
			GeometryOperation geomOp, int type);


	/**
	 * <p>
	 * Registers a GeometryOperation associated to all the geometries with a concrete subtype.
	 * Returns an unique index that is used later to identify and invoke the operation.<br>
	 * </p>
	 * <p>
	 * By convention, the return value should be stored in a public constant within the class implementing
	 * the operation:<BR>
	 * <pre>
	 * public class MyOperation extends GeometryOperation {
	 *   public static final int CODE =
	 *     GeometryLocator.getGeometryManager()
	 *        .registerGeometryOperation("MyOperation", new MyOperation(), SUBTYPES.GEOM2D);
	 * }
	 * </pre>
	 * </p>
	 * @param geomOpName Operation's unique name
	 * @param geomOp Specific GeometryOperation's instance implementing this operation
	 * @param subType
	 * SubType of geometry. Must be a value defined in {@link Geometry.SUBTYPES}
	 * @return Index assigned to this operation. This index is used later to access the operation.
	 */
	public int registerGeometryOperationBySubtype(String geomOpName,
			GeometryOperation geomOp, int subType);

	/**
	 * <p>
	 * Registers a Geometry implementation class with a predefined geometry type and returns the
	 * associated GeometryType instance. Available predefined types are defined in {@link Geometry.TYPES}
	 * and the subtypes are defined in {@link Geometry.SUBTYPES}.
	 * </p>
	 * <p>
	 * How to register a geometry class with a predefined type:
	 * <pre>
	 *   GeometryType geomType = GeometryLocator.getGeometryManager().
	 *      registerBasicGeometryType(Point2D.class, "Point2D", Geometry.TYPES.POINT, Geometry.SYBTYPES.GEOM2D);
	 * </pre>
	 * </p>
	 * @param geomClass
	 *            Geometry subclass. It must not be null and must implement Geometry, otherwise an exception
	 *            is raised.
	 * @param name
	 * 			  Symbolic name for the geometry type, it can be null. If it is null then the symbolic name
	 * 		      will be the simple class name.
	 * @param type
	 * Type of geometry. Must be a value defined in {@link Geometry.TYPES}
	 * @param subType
	 * SubType of geometry. Must be a value defined in {@link Geometry.SUBTYPES}
	 * @return Instance of GeometryType associated to the Geometry implementation class
	 *         geomClass
	 * @throws IllegalArgumentException
	 *             If geomClass is null or does not implement Geometry
	 */
	public GeometryType registerGeometryType(Class geomClass, String name, int type, int subType);

	/**
	 * <p>
	 * Registers a Geometry implementation class with a predefined geometry type and returns the
	 * associated GeometryType instance. Available predefined types are defined in {@link Geometry.TYPES}
	 * and the subtypes are defined in {@link Geometry.SUBTYPES}.
	 * </p>
	 * <p>
	 * In this case the symbolic name will be the geometry's simple class name
	 * </p>
	 * How to register a new geometry type:
	 * <pre>
	 * GeometryType geomType = GeometryLocator.getGeometryManager().
	 *      registerBasicGeometryType(Point2D.class, Geometry.TYPES.POINT, Geometry.SYBTYPES.GEOM2D);
	 * </pre>
	 *
	 * @param geomClass Geometry implementation class. It must not be null and must implement Geometry,
	 * otherwise an exception is thrown.
	 * @param type
	 * Type of geometry. Must be a value defined in {@link Geometry.TYPES}
	 * @param subType
	 * SubType of geometry. Must be a value defined in {@link Geometry.SUBTYPES}
	 * @return Instance of GeometryType associated to the Geometry implementation class
	 * @throws IllegalArgumentException
	 *             If geomClass is null or does not implement Geometry
	 */
	public GeometryType registerGeometryType(Class geomClass, int type, int subType);

	/**
	 * <p>
	 * Returns an instance of GeometryType given the Geometry type
	 * and the subtype.
	 * </p>
	 * @param type
	 * Type of geometry. Must be a value defined in {@link Geometry.TYPES}
	 * @param subType
	 * SubType of geometry. Must be a value defined in {@link Geometry.SUBTYPES}
	 * @return Instance of GeometryType associated to the type and the subtype
	 * @throws GeometryTypeNotSupportedException
	 * Returns this exception if there is not a registered geometry with
	 * these type and subtype
	 * @throws GeometryTypeNotValidException
	 * Returns if the type and subtype are not valid
	 */
	public GeometryType getGeometryType(int type, int subType) throws GeometryTypeNotSupportedException, GeometryTypeNotValidException;

	/**
	 * <p>
	 * This method creates a {@link Geometry} with the type specified
	 * by this GeometryType. The geometry is empty, and all the internal
	 * attributes must be assigned to a value when the geometry has
	 * been created.
	 * </p>
	 * <p>
	 * This example creates a point2D and sets the coordinates to 1,1:
	 *
	 * <pre>
	 * Point point = (Point)GeometryLocator
	 * 		.getGeometryManager().create(GEOMETRY.TYPES.POINT, GEOMETRY.SUBTYPES.GEOM2D);
	 * 	point.setX(1);
	 * 	point.setY(1);
	 * </pre>
	 * </p>
	 * @param geomType
	 * The geometry type
	 * @return
	 * A instance of a geometry.
	 * @throws CreateGeometryException
	 * This exception is thrown when the manager can not create
	 * the geometry.
	 */
	public Geometry create(GeometryType geomType) throws CreateGeometryException;

	/**
	 * <p>
	 * Creates a Envelope with a concrete subtype. The envelope is empty
	 * and it have to be filled with the corners once has been created.
	 * </p>
	 * @param subType
	 * SubType of envelope. Must be a value defined in {@link Geometry.SUBTYPES}
	 * @return
	 * A Envelope
	 * @throws CreateEnvelopeException
	 * If it is not possible to create the envelope.
	 */
	public Envelope createEnvelope(int subType) throws CreateEnvelopeException;

	/**
	 * <p>
	 * Creates a Envelope with a concrete subtype. It sets the values
	 * for the lower corner and the upper corner (in 2D) using the
	 * method parameters.
	 * </p>
	 * @param minX
	 * The minimum value for the X coordinate.
	 * @param minY
	 * The minimum value for the Y coordinate.
	 * @param maxX
	 * The maximum value for the X coordinate.
	 * @param maxY
	 * The maximum value for the Y coordinate.
	 * @param subType
	 * SubType of envelope. Must be a value defined in {@link Geometry.SUBTYPES}
	 * @return
	 * @throws CreateEnvelopeException
	 */
	public Envelope createEnvelope(double minX, double minY, double maxX, double maxY, int subType) throws CreateEnvelopeException;

	/**
	 * <p>
	 * This method creates a {@link Geometry} with the type specified
	 * by this name. If a geometry with this name doesn't exist, a
	 * {@link IllegalArgumentException} is thrown. The geometry is empty,
	 * and all the internal attributes must be assigned to a value when
	 * the geometry has  been created.
	 * </p>
	 * <p>
	 * This example creates a point2D and sets the coordinates to 1,1:
	 * It supposes that there is a Point2D class with name "Point2D".
	 * </p>
	 * <pre>
	 * Point point = (Point)GeometryLocator
	 * 		.getGeometryManager().create("Point2D");
	 * 	point.setX(1);
	 * 	point.setY(1);
	 * </pre>
	 *
	 * @param name
	 * The name of the geometry type
	 * @return
	 * A instance of a geometry.
	 * @throws CreateGeometryException
	 * This exception is thrown when the manager can not create
	 * the geometry.
	 */
	public Geometry create(String name) throws CreateGeometryException;

	/**
	 * <p>
	 * This method creates a {@link Geometry} with a concrete type and subtype.
	 * The geometry is empty, and all the internal attributes must be assigned
	 * to a value when the geometry has  been created.
	 * </p>
	 * <p>
	 * This example creates a point2D and sets the coordinates to 1,1.
	 * It supposes that there is a Point2D class with the id 1.
	 * </p>
	 * <pre>
	 * Point point = (Point)GeometryLocator
	 * 		.getGeometryManager().create(Geometry.TYPES.POINT, Geometry.SYBTYPES.GEOM2D);
	 * 	point.setX(1);
	 * 	point.setY(1);
	 * </pre>
	 * @param type
	 * Type of geometry. Must be a value defined in {@link Geometry.TYPES}
	 * @param subType
	 * SubType of geometry. Must be a value defined in {@link Geometry.SUBTYPES}
	 * @return
	 * A instance of a geometry.
	 * @throws CreateGeometryException
	 * This exception is thrown when the manager can not create
	 * the geometry.
	 */
	public Geometry create(int type, int subType) throws CreateGeometryException;

	/**
	 * <p>
	 * It creates a null geometry with a concrete subtype.
	 * <p>
	 * @param subType
	 * SubType of geometry. Must be a value defined in {@link Geometry.SUBTYPES}
	 * @return
	 * A NullGeometry
	 * @throws CreateGeometryException
	 * This exception is thrown when the manager can not create
	 * the geometry.
	 */
	public NullGeometry createNullGeometry(int subType) throws CreateGeometryException;

	/**
	 * <p>
	 * Create a new point with a concrete type and sets the value
	 * for the X and the Y.
	 * </p>
	 * @param x
	 * The X coordinate
	 * @param y
	 * The y coordinate
	 * @param subType
	 * SubType of geometry. Must be a value defined in {@link Geometry.SUBTYPES}
	 * @throws CreateGeometryException
	 * This exception is thrown when the manager can not create
	 * the geometry.
	 * @return
	 * The Point
	 */
	public Point createPoint(double x, double y, int subType) throws CreateGeometryException;

	/**
	 * <p>
	 * Create a new curve with a concrete type and sets the value
	 * for the coordinates using a GeneralPathX.
	 * </p>
	 * @param generalPathX
	 * It is used to set the values for the X and Y coordinates.
	 * @param subType
	 * SubType of geometry. Must be a value defined in {@link Geometry.SUBTYPES}
	 * @return
	 * A curve
	 * @throws CreateGeometryException
	 * This exception is thrown when the manager can not create
	 * the geometry.
	 */
	public Curve createCurve(GeneralPathX generalPathX, int subType) throws CreateGeometryException;


	/**
	 * <p>
	 * Create a new multicurve with a concrete type and sets the value for the
	 * coordinates using a GeneralPathX.
	 * </p>
	 *
	 * @param generalPathX
	 *            It is used to set the values for the X and Y coordinates.
	 * @param subType
	 *            SubType of geometry. Must be a value defined in
	 *            {@link Geometry.SUBTYPES}
	 * @return A multicurve
	 * @throws CreateGeometryException
	 *             This exception is thrown when the manager can not create the
	 *             geometry.
	 */
	public MultiCurve createMultiCurve(GeneralPathX generalPathX, int subType) throws CreateGeometryException;


	/**
	 * <p>
	 * Create a new surface with a concrete type and sets the value
	 * for the coordinates using a GeneralPathX.
	 * </p>
	 * @param generalPathX
	 * It is used to set the values for the X and Y coordinates.
	 * @param subType
	 * SubType of geometry. Must be a value defined in {@link Geometry.SUBTYPES}
	 * @return
	 * A surface
	 * @throws CreateGeometryException
	 * This exception is thrown when the manager can not create
	 * the geometry.
	 */
	public Surface createSurface(GeneralPathX generalPathX, int subType) throws CreateGeometryException;

	/**
	 * <p>
	 * Create a new multisurface with a concrete type and sets the value for the
	 * coordinates using a GeneralPathX.
	 * </p>
	 * 
	 * @param generalPathX
	 *            It is used to set the values for the X and Y coordinates.
	 * @param subType
	 *            SubType of geometry. Must be a value defined in
	 *            {@link Geometry.SUBTYPES}
	 * @return A multisurface
	 * @throws CreateGeometryException
	 *             This exception is thrown when the manager can not create the
	 *             geometry.
	 */
	public MultiSurface createMultiSurface(GeneralPathX generalPathX, int subType) throws CreateGeometryException;

	/**
	 * <p>
	 * Returns an operation given the Geometry type, the Geometry subtype and and the operation
	 * code. If opCode corresponds to a common operation (a common operation is an operation
	 * registered for all geometries), then this method returns the common operation.
	 * </p>
	 * <p>
	 * For better performance, if you need to call an operation multiple times,
	 * use this method only once and keep the returned object in a local variable
	 * over which you can iterate. For instance:
	 *
	 * <pre>
	 * // Get the operation you need
	 * GeometryManager gm = GeometryLocator.getGeometryManager()
	 * GeometryOperation geomOp = null;
	 * try {
	 *    geomOp = gm.getGeometryOperation(Draw2D.CODE);
	 * } catch (GeometryTypeNotSupportedException gtnse) {
	 *    // treat exception
	 * } catch (GeometryOperationNotSupportedException gonse) {
	 *    // treat exception
	 * }
	 *
	 *  // Fill the operation context with required params
	 * GeometryOperationContext ctx = new GeometryOperationContext();
	 *
	 *  // Here is the main loop where you call the operation
	 * for (int i=0; i<MyGeometries.length; i++) {
	 *    Object result = geomOp.invoke(myGeometries[i], ctx);
	 * }
	 * </pre>
	 * </p>
	 * @param opCode
	 * The operation code
	 * @param type
	 * Type of geometry. Must be a value defined in {@link Geometry.TYPES}
	 * @param subType
	 * SubType of geometry. Must be a value defined in {@link Geometry.SUBTYPES}
	 * @return Geometry operation
	 * @throws GeometryTypeNotSupportedException
	 * Returns this exception if there is not a registered geometry with
	 * these type and subtype
	 * @throws GeometryTypeNotValidException
	 * Returns this exception if the type and subtype are not valid
	 * @throws GeometryOperationNotSupportedException
	 * Returns this exception if there is not a registered operation with
	 * this operation code
	 */
	public GeometryOperation getGeometryOperation(int opCode, int type, int subType) throws GeometryTypeNotSupportedException, GeometryOperationNotSupportedException, GeometryTypeNotValidException;

	/**
	 * <p>
	 * Invokes an operation given its code, the geometry and the operation context holding the
	 * parameters required for the operation.
	 * </p>
	 * @param opCode
	 * Operation code.
	 * @param geom
	 * Geometry to which apply the operation
	 * @param ctx
	 * Context holding the operation parameters
	 * @return
	 * The object returned by an operation, depends on each operation.
	 */
	public Object invokeOperation(int opCode, Geometry geom, GeometryOperationContext ctx) throws GeometryOperationNotSupportedException, GeometryOperationException;

	/**
	 * <p>
	 * Invokes an operation given its code, the geometry and the operation context holding the
	 * parameters required for the operation.
	 * </p>
	 * @param geomOpName
	 * Operation name.
	 * @param geom
	 * Geometry to which apply the operation
	 * @param ctx
	 * Context holding the operation parameters
	 * @return
	 * The object returned by an operation, depends on each operation.
	 */
	public Object invokeOperation(String geomOpName, Geometry geom, GeometryOperationContext ctx) throws GeometryOperationNotSupportedException, GeometryOperationException;
}
