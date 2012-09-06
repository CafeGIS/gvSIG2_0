package org.gvsig.tools.operations;

import org.gvsig.tools.ToolsLocator;

public abstract class AbstractOperation {

	protected String name;
	protected String description;

	public AbstractOperation(String name, String descrption) {
		this.description = descrption;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	/**
	 * Get the operation code associated to this operation.
	 * 
	 * Overwrite this method for optimize.
	 * 
	 * @return the operation code
	 * @throws OperationNotSupportedException
	 */
	public int getCode() throws OperationNotSupportedException {
		OperationManager manager = ToolsLocator.getOperationManager();
		return manager.getOperationCode(this.getClass(), name);
	}

	/**
	 * Invokes this operation in the context.
	 *
	 * @param self
	 *            the object to which apply this operation
	 * @param ctx
	 *            Parameter container
	 * @return Place-holder object that may contain any specific return value.
	 * @throws OperationException
	 *             The implementation is responsible to throw this exception
	 *             when needed.
	 */
	public abstract Object invoke(Object self, OperationContext context)
			throws OperationException;

}
