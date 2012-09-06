package org.gvsig.tools.operations.impl;

import java.util.HashMap;
import java.util.Map;

import org.gvsig.tools.operations.Operation;
import org.gvsig.tools.operations.OperationContext;
import org.gvsig.tools.operations.OperationException;
import org.gvsig.tools.operations.OperationManager;
import org.gvsig.tools.operations.OperationNotSupportedException;

public class DefaultOperationManager implements OperationManager {

	private class OperationInfo {
		Class theClass;
		int code;
		Operation operation;

		OperationInfo(Class theClass, int code, Operation operation) {
			this.theClass = theClass;
			this.code = code;
			this.operation = operation;
		}

		String getKey() {
			return theClass.getName() + ":" + operation.getName();
		}
	}

	private Operation[] operations;
	private Map operationsInfoMap;
	private OperationInfo[] operationsInfo;

	public DefaultOperationManager() {
		operationsInfoMap = new HashMap();
		operations = new Operation[1];
	}

	public int registerOperation(Operation operation, Class theClass) {
		OperationInfo info = new OperationInfo(theClass, 0, operation);
		OperationInfo oldInfo = (OperationInfo) operationsInfoMap.get(info
				.getKey());
		if (oldInfo != null) {
			// Update the operation
			oldInfo.theClass = theClass;
			oldInfo.operation = operation;
			return oldInfo.code;
		}
		if (operations == null) {
			operations = new Operation[1];
			operationsInfo = new OperationInfo[1];
			info.code = 0;
		} else {
			Operation[] temp1 = new Operation[operations.length + 1];
			System.arraycopy(operations, 0, temp1, 0, operations.length);
			OperationInfo[] temp2 = new OperationInfo[operationsInfo.length + 1];
			System
					.arraycopy(operationsInfo, 0, temp2, 0,
							operationsInfo.length);
			info.code = operations.length - 1;
		}
		operations[info.code] = info.operation;
		operationsInfo[info.code] = info;
		operationsInfoMap.put(info.getKey(), info);
		return info.code;
	}

	private String getKey(Object obj, String name) {
		return obj.getClass().getName() + ":" + name;
	}

	private String getKey(Class theClass, String name) {
		return theClass.getName() + ":" + name;
	}

	public Operation getOperation(Object obj, String name)
			throws OperationNotSupportedException {
		OperationInfo info = (OperationInfo) operationsInfoMap.get(getKey(obj,
				name));
		if (info == null) {
			throw new OperationNotSupportedException(obj.getClass(), name);
		}
		return info.operation;
	}

	public Operation getOperation(Class theClass, String name)
			throws OperationNotSupportedException {
		OperationInfo info = (OperationInfo) operationsInfoMap.get(getKey(
				theClass, name));
		if (info == null) {
			throw new OperationNotSupportedException(theClass, name);
		}
		return info.operation;
	}

	public Operation getOperation(Object obj, int code)
			throws OperationNotSupportedException {
		if (code >= operationsInfo.length) {
			throw new OperationNotSupportedException(obj.getClass(), code);
		}
		OperationInfo info = operationsInfo[code];
		if (!info.theClass.isAssignableFrom(obj.getClass())) {
			throw new OperationNotSupportedException(obj.getClass(), code);
		}
		return info.operation;
	}

	public Operation getOperation(Class theClass, int code)
			throws OperationNotSupportedException {
		if (code >= operationsInfo.length) {
			throw new OperationNotSupportedException(theClass, code);
		}
		OperationInfo info = operationsInfo[code];
		if (!info.theClass.isAssignableFrom(theClass)) {
			throw new OperationNotSupportedException(theClass, code);
		}
		return info.operation;
	}

	public boolean hasOperation(Object obj, String name) {
		try {
			Operation op = getOperation(obj, name);
			return op != null;
		} catch (OperationNotSupportedException e) {
			return false;
		}
	}

	public boolean hasOperation(Object obj, int code) {
		try {
			Operation op = getOperation(obj, code);
			return op != null;
		} catch (OperationNotSupportedException e) {
			return false;
		}
	}

	public boolean hasOperation(Class theClass, String name) {
		try {
			Operation op = getOperation(theClass, name);
			return op != null;
		} catch (OperationNotSupportedException e) {
			return false;
		}
	}

	public boolean hasOperation(Class theClass, int code) {
		try {
			Operation op = getOperation(theClass, code);
			return op != null;
		} catch (OperationNotSupportedException e) {
			return false;
		}
	}

	public Object invokeOperation(Object obj, String name,
			OperationContext context) throws OperationException {
		Operation op = getOperation(obj, name);
		return op.invoke(obj, context);
	}

	public Object invokeOperation(Object obj, int code, OperationContext context)
			throws OperationException {
		try {
			/*
			 * Intentamos ejecutar la operacion, y si peta ya aremos las
			 * comprobaciones oportunas para lanzar la excepcion que toque.
			 *
			 * Asi evitamos codigo de comprobacion para los casos que valla bien
			 * que deberian ser la mayoria.
			 */
			return operations[code].invoke(obj, context);
		} catch (RuntimeException e) {
			getOperation(obj, code);
			throw e;
		} catch (OperationException e) {
			getOperation(obj, code);
			throw e;
		}
	}

	public int getOperationCode(Class theClass, String name)
			throws OperationNotSupportedException {
		OperationInfo info = (OperationInfo) operationsInfoMap.get(getKey(
				theClass, name));
		if (info == null) {
			throw new OperationNotSupportedException(theClass, name);
		}
		return info.code;
	}

	public int registerOperation(Operation operation, Object obj) {
		return registerOperation(operation, obj.getClass());
	}


}
