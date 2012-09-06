package org.gvsig.tools.operations;

public interface Operation {
	public Object invoke(Object self, OperationContext context)
			throws OperationException;

	public String getName();

	public String getDescription();

	public int getCode() throws OperationNotSupportedException;
}
