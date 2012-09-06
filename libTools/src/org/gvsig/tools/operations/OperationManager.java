package org.gvsig.tools.operations;

public interface OperationManager {

	public int getOperationCode(Class aClass, String name)
			throws OperationNotSupportedException;

	public Operation getOperation(Object obj, String name)
			throws OperationNotSupportedException;

	public Operation getOperation(Object obj, int code)
			throws OperationNotSupportedException;

	public Operation getOperation(Class aClass, String name)
			throws OperationNotSupportedException;

	public Operation getOperation(Class aClass, int code)
			throws OperationNotSupportedException;

	public Object invokeOperation(Object obj, String name,
			OperationContext context) throws OperationException;

	public Object invokeOperation(Object obj, int code, OperationContext context)
			throws OperationException;

	public boolean hasOperation(Object obj, String name);

	public boolean hasOperation(Object obj, int code);

	public boolean hasOperation(Class aClass, String name);

	public boolean hasOperation(Class aClass, int code);

	public int registerOperation(Operation operation, Object obj);

	public int registerOperation(Operation operation, Class aClass);
}
