package org.gvsig.tools.dynobject;

import org.gvsig.tools.dynobject.exception.DynMethodException;
import org.gvsig.tools.dynobject.exception.DynMethodNotSupportedException;

public interface DynMethod {
	public Object invoke(Object self, DynObject context)
	throws DynMethodException;

	public String getName();

	public String getDescription();

	public int getCode() throws DynMethodNotSupportedException;
	
}
