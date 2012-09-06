package org.gvsig.tools.dynobject.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.gvsig.tools.dynobject.DynClass;
import org.gvsig.tools.dynobject.DynMethod;
import org.gvsig.tools.dynobject.DynObject;
import org.gvsig.tools.dynobject.DynObjectManager;
import org.gvsig.tools.dynobject.exception.DynMethodException;
import org.gvsig.tools.dynobject.exception.DynMethodIllegalCodeException;
import org.gvsig.tools.dynobject.exception.DynMethodNotSupportedException;
import org.gvsig.tools.dynobject.exception.IllegalDynMethodException;
import org.gvsig.tools.dynobject.exception.IllegalDynMethodInvocationException;

public class DefaultDynObjectManager implements DynObjectManager {

	private static DefaultDynObjectManager manager = null;

	private class MethodInfo {
		int code;
		DynClass dynClass;
		DynMethod dynMethod;
		Class theClass;

		MethodInfo(Class theClass, DynClass dynClass, DynMethod dynMethod, int code) {
			this.code = code;
			this.dynClass = dynClass;
			this.dynMethod = dynMethod;
			this.theClass = theClass;
		}

		String getKey() {
			return DefaultDynObjectManager.getKey(theClass, dynClass, dynMethod);
		}

		void check(Class theClass, int code) throws DynMethodException {
			if( code != this.code) {
				throw new DynMethodIllegalCodeException(dynMethod.getName(),this.code, code);
			}
			if( theClass != null ) {
				if( this.theClass == null ) {
					throw new IllegalDynMethodInvocationException(dynMethod.getName(), theClass);
				}
				if (!this.theClass.isAssignableFrom(theClass)) {
					throw new IllegalDynMethodInvocationException(dynMethod.getName(),theClass);
				}
			}
		}

		void check(DynClass dynClass, int code) throws DynMethodException  {
			if( code != this.code) {
				throw new DynMethodIllegalCodeException(dynMethod.getName(),this.code, code);
			}
			if( dynClass != null ) {
				if( this.dynClass == null ) {
					throw new IllegalDynMethodInvocationException(dynMethod.getName(), dynClass);
				}
				if( dynClass != this.dynClass || !dynClass.getName().equalsIgnoreCase(this.dynClass.getName()) ) {
					throw new IllegalDynMethodInvocationException(dynMethod.getName(), dynClass);
				}
			}
		}
	}

	private Map anonymousClasses;
	private Map classes;
	private Map methodsMap;
	private MethodInfo[] methods;

	public static DefaultDynObjectManager getManager() {
		if (manager == null) {
			manager = new DefaultDynObjectManager();
		}
		return manager;
	}

    static String getKey(Class theClass, DynClass dynClass, DynMethod dynMethod) {
    	return DefaultDynObjectManager.getKey(theClass, dynClass, dynMethod.getName());
    }

    static String getKey(Class theClass, DynClass dynClass, String methodName) {
		if( dynClass == null ) {
			return theClass.getName() + ":" + methodName;
		} else {
			return dynClass.getName() + ":" + methodName;
		}
    }


	public DefaultDynObjectManager() {
		this.classes = new HashMap();
		this.anonymousClasses = new HashMap();
		this.methodsMap = new HashMap();
		this.methods = null;
	}

	public DynClass createDynClass(String name, String description) {
		return new DefaultDynClass(this, name, description);
	}

	public boolean add(DynClass dynClass) {
		this.classes.put(dynClass.getName().toLowerCase(), dynClass);
		return true;
	}

	public DynClass add(String name, String description) {
		DynClass dynClass = (DynClass) this.classes.get(name.toLowerCase());
		if (dynClass == null) {
			dynClass = this.createDynClass(name, description);
			this.add(dynClass);
		}
		return dynClass;
	}

	public DynClass add(String name) {
		return this.add(name, null);
	}

	public DynClass get(String name) {
		return (DynClass) this.classes.get(name.toLowerCase());
	}

	public DynClass get(DynClass[] superClasses) {
		StringBuffer name = new StringBuffer();
		for( int i=0; i<superClasses.length; i++) {
			name.append(superClasses[i].getName()).append("+");
		}
		DefaultDynClass dynClass = (DefaultDynClass) this.anonymousClasses.get(name.toString());
		if( dynClass == null ) {
			dynClass = new DefaultDynClass(this, name.toString(), null, superClasses);
			dynClass.setAnonymous(true);
		}
		return dynClass;
	}

	public int getCount() {
		return this.classes.size();
	}

	public List getNames() {
		String[] names = (String[]) this.classes.keySet().toArray();
		Arrays.sort(names);
		return Collections.unmodifiableList(Arrays.asList(names));
	}

	public boolean has(String name) {
		return this.classes.containsKey(name.toLowerCase());
	}

	public Iterator interator() {
		return this.classes.values().iterator();
	}

	public DynObject createDynObject(String dynClassName) {
		DynClass dynClass = (DynClass) this.classes.get(dynClassName
				.toLowerCase());
		if (dynClass == null) {
			throw new IllegalArgumentException(dynClassName);
		}
		return this.createDynObject(dynClass);
	}

	public DynObject createDynObject(DynClass dynClass) {
		return new DefaultDynObject(dynClass);
	}

	public void consolide() {
		Iterator it = this.classes.values().iterator();
		while( it.hasNext() ) {
			DefaultDynClass dc = (DefaultDynClass) it.next();
			dc.consolide();
		}
		it = this.anonymousClasses.values().iterator();
		while( it.hasNext() ) {
			DefaultDynClass dc = (DefaultDynClass) it.next();
			dc.consolide();
		}
	}


	public int registerDynMethod(DynClass dynClass, DynMethod dynMethod) {
		((DefaultDynClass)dynClass).addMethod(dynMethod);
		return registerDynMethod(null, dynClass, dynMethod);
	}

	public int registerDynMethod(Class theClass, DynMethod dynMethod) {
		return registerDynMethod(theClass, null, dynMethod);
	}

	int registerDynMethod(Class theClass, DynClass dynClass, DynMethod dynMethod) {
		MethodInfo info = new MethodInfo(theClass, dynClass, dynMethod, 0);
		MethodInfo oldInfo = (MethodInfo) methodsMap.get(info.getKey());
		if (oldInfo != null) {
			// Update the method info
			oldInfo.dynClass = dynClass;
			oldInfo.dynMethod = dynMethod;
			return oldInfo.code;
		}
		if (methods == null) {
			methods = new MethodInfo[1];
			info.code = 0;
		} else {
			MethodInfo[] temp1 = new MethodInfo[methods.length + 1];
			System.arraycopy(methods, 0, temp1, 0, methods.length);
			info.code = temp1.length - 1;
			methods = temp1;
		}
		methods[info.code] = info;
		methodsMap.put(info.getKey(), info);

		return info.code;
	}

	public Object invokeDynMethod(Object self, int code, DynObject context) throws DynMethodException{

		try {
			/*
			 * Intentamos ejecutar la operacion, y si peta ya haremos las
			 * comprobaciones oportunas para lanzar la excepcion que toque.
			 *
			 * Asi evitamos codigo de comprobacion para los casos que valla bien
			 * que deberian ser la mayoria.
			 */
			return methods[code].dynMethod.invoke(self, context);
		} catch (RuntimeException e) {
			getDynMethod(self, code);
			throw e;
		} catch (DynMethodException e) {
			getDynMethod(self, code);
			throw e;
		}

	}

	public int getDynMethodCode(DynClass dynClass, String methodName) throws DynMethodException  {
		String key = DefaultDynObjectManager.getKey(null, dynClass, methodName);
		MethodInfo info = (MethodInfo) methodsMap.get(key);
		if( info == null ) {
			throw new IllegalDynMethodException(methodName, dynClass);
		}
		info.check(dynClass, info.code);
		return info.code;
	}

	public int getDynMethodCode(Class theClass, String methodName) throws DynMethodException {
		String key = DefaultDynObjectManager.getKey(theClass, null, methodName);
		MethodInfo info = (MethodInfo) methodsMap.get(key);
		if( info == null ) {
			throw new IllegalDynMethodException(methodName, theClass);
		}
		info.check(theClass, info.code);
		return info.code;
	}

	public DynMethod getDynMethod(int code) throws DynMethodException {
		if (code >= methods.length) {
			throw new DynMethodNotSupportedException(code, "{null}");
		}
		MethodInfo info = methods[code];
		info.check((Class)null, code);
		return info.dynMethod;
	}

	public DynMethod getDynMethod(Object obj, int code)
			throws DynMethodException {
		return getDynMethod(obj.getClass(), code);
	}

	public DynMethod getDynMethod(Class theClass, int code)
			throws DynMethodException {
		if (code >= methods.length) {
			throw new DynMethodNotSupportedException(code, theClass.getName());
		}
		MethodInfo info = methods[code];
		info.check(theClass, code);
		return info.dynMethod;
	}

	public DynMethod getDynMethod(DynClass dynClass, int code)
			throws DynMethodException {
		if (code >= methods.length) {
			throw new DynMethodNotSupportedException(code, dynClass.getName());
		}
		MethodInfo info = methods[code];
		info.check(dynClass, code);
		return info.dynMethod;
	}

	public DynMethod getDynMethod(DynObject dynObject, int code)
			throws DynMethodException {
		return getDynMethod(dynObject.getDynClass(), code);
	}

}
