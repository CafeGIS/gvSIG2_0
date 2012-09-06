package org.gvsig.tools.operations;


public interface Operations {

	public Object invokeOperation(int code, OperationContext context)
			throws OperationException, OperationNotSupportedException;

	public Object invokeOperation(String name, OperationContext context)
			throws OperationException, OperationNotSupportedException;


	public Object getOperation(int code)
			throws OperationException,
			OperationNotSupportedException;

	public Object getOperation(String name)
			throws OperationException,
			OperationNotSupportedException;


	public boolean hasOperation(int code);

	public boolean hasOperation(String name);

}