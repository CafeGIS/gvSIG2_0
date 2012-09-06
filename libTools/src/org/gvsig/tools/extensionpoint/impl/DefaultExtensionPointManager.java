package org.gvsig.tools.extensionpoint.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.gvsig.tools.extensionpoint.ExtensionBuilder;
import org.gvsig.tools.extensionpoint.ExtensionPoint;
import org.gvsig.tools.extensionpoint.ExtensionPointManager;
import org.gvsig.tools.extensionpoint.ExtensionPoint.Extension;

public class DefaultExtensionPointManager implements ExtensionPointManager {

	private static DefaultExtensionPointManager manager = null;

	private Map extensionsPoints;

	public DefaultExtensionPointManager() {
		this.extensionsPoints = new HashMap();
	}

	public static DefaultExtensionPointManager getManager() {
		if (manager == null) {
			manager = new DefaultExtensionPointManager();
		}
		return manager;
	}

	public Object create(Class cls) throws InstantiationException,
			IllegalAccessException {
		Object obj = null;

		if (cls == null) {
			return null;
		}
		obj = cls.newInstance();
		return obj;
	}

	public Object create(Class cls, Map args) throws SecurityException,
			NoSuchMethodException, IllegalArgumentException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {
		Object obj = null;
		Constructor create = null;
		Class[] types = new Class[1];
		Object[] argsx = new Object[1];

		if (cls == null) {
			return null;
		}
		types[0] = Map.class;
		argsx[0] = args;
		create = this.findConstructor(cls, types);
		obj = create.newInstance(argsx);
		return this.createObject(cls, argsx);
	}

	public Object create(Class cls, Object[] args) throws SecurityException,
			NoSuchMethodException, IllegalArgumentException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {

		return this.createObject(cls, args);

	}

	/**
	 * @param cls
	 * @param types
	 * @return
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	private Constructor findConstructor(Class clazz, Class[] types)
			throws SecurityException, NoSuchMethodException {
		try {
			return clazz.getConstructor(types);
		} catch (NoSuchMethodException e) {
			//Nothing to do
		}

		// search for the required constructor
		Constructor[] constrs = clazz.getConstructors();
		boolean allMatch;
		for (int i = 0; i < constrs.length; i++) {
			Class[] paramTypes = constrs[i].getParameterTypes();
			if (paramTypes.length == types.length) { // a general candidate
				allMatch = true;
				for (int j = 0; j < paramTypes.length; j++) {
					if (!isParameterCompatible(types[j], paramTypes[j])) {
						allMatch = false;
						break;
					}
				}
				if (allMatch) {
					return constrs[i];
				}

			}
		}
		StringBuffer strb = new StringBuffer();
		strb.append(clazz.getName());
		strb.append('(');
		if (types.length > 0 ){
			for (int i = 0; i < types.length - 1; i++) {
				strb.append(types[i].getName());
				strb.append(',');
			}
			strb.append(types[types.length - 1].getName());
		}
		strb.append(')');
		throw new NoSuchMethodException(strb.toString());
	}

	private boolean isParameterCompatible(Class current, Class defined) {
		if (current == null) {
			return !defined.isPrimitive();
		} else {
			return defined.isAssignableFrom(current);
		}
	}

	public ExtensionPoint create(String name, String description) {
		return new DefaultExtensionPoint(this, name, description);
	}

	public boolean add(ExtensionPoint extensionPoint) {
	    ExtensionPoint original = (ExtensionPoint) extensionsPoints
                .get(extensionPoint.getName());
	    if (original == null) {
	        extensionsPoints.put(extensionPoint.getName(), extensionPoint);
	    } else {
	        for (Iterator iterator = extensionPoint.iterator(); iterator
                    .hasNext();) {
                Extension extension = (Extension) iterator.next();
                if (extension.getBuilder() != null) {
                    original.append(extension.getName(), extension
                            .getDescription(), extension.getBuilder());
                } else {
                    original.append(extension.getName(), extension
                            .getDescription(), extension.getExtension());
                }
            }
        }
		return true;
	}

	public synchronized ExtensionPoint add(String name, String description) {
		ExtensionPoint ep = (ExtensionPoint) this.extensionsPoints.get(name);
		if (ep == null) {
			ep = new DefaultExtensionPoint(this, name, description);
			this.extensionsPoints.put(ep.getName(), ep);
		}
		return ep;
	}

	public ExtensionPoint add(String name) {
		return add(name, null);
	}

	public ExtensionPoint.Extension add(String name, String description,
			String extName,
			String extDescription, Class extension) {
		ExtensionPoint ep = add(name, description);
		return ep.append(extName, extDescription, extension);
	}

	public ExtensionPoint.Extension add(String name, String description,
			String extName,
			String extDescription, ExtensionBuilder builder) {
		ExtensionPoint ep = add(name, description);
		return ep.append(extName, extDescription, builder);
	}

	public ExtensionPoint get(String name) {
		return (ExtensionPoint) this.extensionsPoints.get(name);
	}

	public int getCount() {
		return this.extensionsPoints.size();
	}

	public boolean has(String name) {
		return this.extensionsPoints.get(name) != null;
	}

	public Iterator interator() {
		return this.extensionsPoints.values().iterator();
	}

	public List getNames() {
		String[] names = (String[]) this.extensionsPoints.keySet().toArray();
		Arrays.sort(names);
		return Collections.unmodifiableList(Arrays.asList(names));
	}

	public Object createObject(Class klass, Object[] args)
			throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, InstantiationException,
			IllegalAccessException, InvocationTargetException {
		Object obj = null;
		Constructor create = null;
		Class[] types = new Class[args.length];

		if (klass == null) {
			return null;
		}
		for (int n = 0; n < args.length; n++) {
			Object arg = args[n];
			if (arg != null) {
				types[n] = arg.getClass();
			} else {
				types[n] = null;
			}
		}
		create = this.findConstructor(klass, types);

		obj = create.newInstance(args);
		return obj;

	}


}
