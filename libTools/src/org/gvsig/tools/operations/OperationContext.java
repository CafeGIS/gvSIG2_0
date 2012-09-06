package org.gvsig.tools.operations;

public interface OperationContext {

	/**
	 * Returns an attribute given its name.
	 * If it does not exist returns <code>null</code>
	 * @param name
	 * @return attribute
	 */
	public abstract Object getAttribute(String name);

	/**
	 * Sets an attribute
	 * @param name
	 * @param value
	 */
	public abstract void setAttribute(String name, Object value);

}