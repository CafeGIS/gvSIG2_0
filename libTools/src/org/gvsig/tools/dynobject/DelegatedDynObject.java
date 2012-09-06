package org.gvsig.tools.dynobject;

import org.gvsig.tools.dynobject.exception.DynMethodException;

public interface DelegatedDynObject extends DynObject {

	Object invokeDynMethod(Object self, String methodName, DynObject context)throws DynMethodException;
	
	Object invokeDynMethod(Object self, int methodCode, DynObject context) throws DynMethodException;
}
