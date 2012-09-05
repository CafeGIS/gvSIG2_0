package org.gvsig.fmap.geom.operation;

import org.gvsig.fmap.geom.Geometry;

/**
 * Every geometry operation that is registered dynamically must extend this class.<br>
 *
 * The following example shows how to implement and register a custom operation:
 *  
 * <pre>
 * public class MyOperation extends GeometryOperation {
 * 
 *   // Check GeometryManager for alternative methods to register an operation  
 *   public static final int CODE = 
 *     GeometryManager.getInstance()
 *        .registerGeometryOperation("MyOperation", new MyOperation(), geomType);
 *   
 *   public Object invoke(Geometry geom, GeometryOperationContext ctx) throws GeometryOperationException {
 *        // Operation logic goes here
 *   }     
 *   
 *   public int getOperationIndex() {
 *      return CODE;
 *   }
 *   
 * }
 * </pre>
 *
 * @author jiyarza
 *
 */
public abstract class GeometryOperation {
	
	/**
	 * Invokes this operation given the geometry and context 
	 * @param geom Geometry to which apply this operation
	 * @param ctx Parameter container
	 * @return Place-holder object that may contain any specific return value. 
	 * @throws GeometryOperationException The implementation is responsible to throw this exception when needed.
	 */
	public abstract Object invoke(Geometry geom, GeometryOperationContext ctx) throws GeometryOperationException;

	/**
	 * Returns the constant value that identifies this operation and that was obtained upon registering it. 
	 * @return operation unique index 
	 */
	public abstract int getOperationIndex();
}
