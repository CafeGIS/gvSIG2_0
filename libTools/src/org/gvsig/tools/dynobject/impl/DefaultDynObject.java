package org.gvsig.tools.dynobject.impl;

import org.gvsig.tools.dynobject.DelegatedDynObject;
import org.gvsig.tools.dynobject.DynClass;
import org.gvsig.tools.dynobject.DynMethod;
import org.gvsig.tools.dynobject.DynObject;
import org.gvsig.tools.dynobject.exception.DynFieldNotFoundException;
import org.gvsig.tools.dynobject.exception.DynMethodException;
import org.gvsig.tools.dynobject.exception.DynMethodNotSupportedException;
import org.gvsig.tools.dynobject.impl.DefaultDynClass.FieldAndIndex;

public class DefaultDynObject implements DelegatedDynObject {

	protected DefaultDynClass dynClass;
	protected Object[] values;
	protected DynObject[] delegateds;

	public DefaultDynObject(DynClass dynClass) {
		this.dynClass = (DefaultDynClass) dynClass;
		//		this.values = null;
		this.delegateds = null;
		this.values = this.dynClass.createValues(null);
	}

	public void implement(DynClass dynClass) {
		this.dynClass = (DefaultDynClass) ((DefaultDynObjectManager) ((DefaultDynClass) dynClass)
				.getManager()).get(new DynClass[] { this.dynClass, dynClass });
		this.values = this.dynClass.createValues(this.values);
	}

	public Object getDynValue(String name) throws DynFieldNotFoundException {
		//		if( this.values == null ) {
		//			this.values = this.dynClass.createValues(null);
		//			return null;
		//		}
		FieldAndIndex x = dynClass.getDynFieldAndIndex(name);
		if (x != null) {

			Object value = values[x.getIndex()];
			if (value != null) {
				return x.getDynField().coerce(value);
			}
		}
		if( delegateds != null ) {
			for( int i=0; i<delegateds.length; i++) {
				DynObject dynObj = delegateds[i];
				if( dynObj.hasDynValue(name) ) {
					return dynObj.getDynValue(name);
				}
			}
		}
		if (x == null) {
			throw new DynFieldNotFoundException(name, dynClass.getName());
		}
		return x.getDynField().getDefaultValue();
	}

	public void setDynValue(String name, Object value) throws DynFieldNotFoundException {
		//		if( this.values == null ) {
		//			this.values = this.dynClass.createValues(null);
		//		}
		int pos = dynClass.getFieldIndex(name);
		if (pos < 0 || pos >= values.length) {
			throw new DynFieldNotFoundException(name,this.getDynClass().getName());
		}
		values[pos] = value;
	}

	public boolean instanceOf(DynClass dynClass) {
		return dynClass.isInstance(this);
	}

	public DynClass getDynClass() {
		return this.dynClass;
	}

	public boolean hasDynValue(String name) {
		//		if( this.values == null ) {
		//			this.values = this.dynClass.createValues(null);
		//		}
		int index = dynClass.getFieldIndex(name);
		if (index < 0 || this.values[index] == null) {
			if (delegateds != null) {
				for (int i = 0; i < delegateds.length; i++) {
					DynObject dynObj = delegateds[i];
					if (dynObj.hasDynValue(name)) {
						return true;
					}
				}
			}
			return false;
		}
		return true;
	}

	public void delegate(DynObject dynObjects) {
		if( delegateds == null ) {
			this.delegateds = new DynObject[1];
			this.delegateds[0] = dynObjects;
			return ;
		}
		DynObject[] newValues = new DynObject[this.delegateds.length + 1];
		System.arraycopy(delegateds, 0, newValues, 0, delegateds.length);
		newValues[delegateds.length] = dynObjects;
		this.delegateds = newValues;
	}

	public Object invokeDynMethod(String name, DynObject context) throws DynMethodException {
		throw new IllegalArgumentException("self required");
	}

	public Object invokeDynMethod(int code, DynObject context) throws DynMethodException {
		throw new IllegalArgumentException("self required");
	}

	public Object invokeDynMethod(Object self, String methodName,
			DynObject context)throws DynMethodException {
		DynMethod method = this.dynClass.getDynMethod(methodName);
		if (method == null) {
			if (delegateds != null) {
				for (int i = 0; i < delegateds.length; i++) {
					try {
						return delegateds[i].invokeDynMethod(methodName,
								context);
					} catch (DynMethodNotSupportedException e) {
						// continue next delegated
					}
				}

			}
			throw new DynMethodNotSupportedException(methodName, self
					.getClass().getName());

		}
		return method.invoke(self, context);
	}

	public Object invokeDynMethod(Object self, int methodCode, DynObject context) throws DynMethodException {
		DynMethod method = this.dynClass.getDynMethod(methodCode);
		if (method == null) {
			if (delegateds != null) {
				for (int i = 0; i < delegateds.length; i++) {
					try {
						return delegateds[i].invokeDynMethod(methodCode,
								context);
					} catch (DynMethodNotSupportedException e) {
						// continue next delegated
					}
				}
				throw new DynMethodNotSupportedException(methodCode, self
						.getClass().getName());

			} else {
				throw new DynMethodNotSupportedException(methodCode, self
						.getClass().getName());
			}
		}
		return this.dynClass.manager.invokeDynMethod(self, methodCode, context);
	}
}
