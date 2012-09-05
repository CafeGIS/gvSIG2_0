package org.gvsig.fmap.geom.operation;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is a default parameter container for geometry operation.<br>
 * 
 * Normally every GeometryOperation will extend this class and identify
 * its parameters publicly with getters/setters
 *
 * For those operations that need high performance, parameters should be declared as class 
 * members instead of using the setAttribute/getAttribute mechanism. This way you avoid a hash
 * and a cast operation.
 * 
 * @author jyarza
 *
 */
public class GeometryOperationContext {
	
	private Map ctx = new HashMap();
	
	/**
	 * Returns an attribute given its name.
	 * If it does not exist returns <code>null</code>
	 * @param name
	 * @return attribute
	 */
	public Object getAttribute(String name) {
		return ctx.get(name);
	}
	
	/**
	 * Sets an attribute
	 * @param name
	 * @param value
	 */
	public void setAttribute(String name, Object value) {
		ctx.put(name, value);
	}

}
