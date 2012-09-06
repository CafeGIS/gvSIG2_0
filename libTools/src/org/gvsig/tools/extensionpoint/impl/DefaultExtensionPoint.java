package org.gvsig.tools.extensionpoint.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import org.gvsig.tools.extensionpoint.ExtensionBuilder;
import org.gvsig.tools.extensionpoint.ExtensionPoint;
import org.gvsig.tools.extensionpoint.ExtensionPointManager;

public class DefaultExtensionPoint implements ExtensionPoint {

	class Extension implements ExtensionPoint.Extension {
		private Class extension;
		private ExtensionBuilder builder;
		private String name;
		private String description;
		List alias;

		Extension(String name, String description, Class extension) {
			alias = new ArrayList();
			this.name = name;
			this.description = description;
			this.builder = null;
			this.extension = extension;
		}

		Extension(String name, String description, ExtensionBuilder builder) {
			alias = new ArrayList();
			this.name = name;
			this.description = description;
			this.builder = builder;
			this.extension = null;
		}

		public List getAlias() {
			return Collections.unmodifiableList(this.alias);
		}

		public ExtensionBuilder getBuilder() {
			return this.builder;
		}

		public String getDescription() {
			return this.description;
		}

		public Class getExtension() {
			return this.extension;
		}

		public String getName() {
			return this.name;
		}

		public boolean isBuilder() {
			return this.builder != null;
		}

		public Object create() throws InstantiationException,
			IllegalAccessException {
			if (isBuilder()) {
				return getBuilder().create();
			}
			return manager.create(getExtension());
		}


		public Object create(Object[] args) throws SecurityException,
			IllegalArgumentException, NoSuchMethodException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {
			if (isBuilder()) {
				return getBuilder().create(args);
			}
			return manager.create(getExtension(), args);
		}

		public Object create(Map args) throws SecurityException,
				IllegalArgumentException, NoSuchMethodException,
				InstantiationException, IllegalAccessException,
				InvocationTargetException {
			if (isBuilder()) {
				return getBuilder().create(args);
			}
			return manager.create(getExtension(), args);
		}


	}

	private String name;
	private String description;

	private Map alias; // Map of Extension
	private LinkedHashMap extensions; // Map of Extension

	private DefaultExtensionPointManager manager;

	DefaultExtensionPoint(ExtensionPointManager manager, String name) {
		this.initialize(manager, name, "");
	}

	DefaultExtensionPoint(ExtensionPointManager manager, String name,
			String description) {
		this.initialize(manager, name, description);
	}

	private void initialize(ExtensionPointManager manager, String name,
			String description) {
		this.manager = (DefaultExtensionPointManager) manager;
		this.name = name;
		this.description = description;
		this.alias = new HashMap();
		this.extensions = new LinkedHashMap();
	}

	private ExtensionPoint.Extension append(Extension extension) {
		this.extensions.put(extension.getName(), extension);
		return extension;
	}

	public ExtensionPoint.Extension append(String name, String description,
			ExtensionBuilder builder) {
		return append(new Extension(name, description, builder));
	}

	public ExtensionPoint.Extension append(String name, String description,
			Class extension) {
		return append(new Extension(name, description, extension));
	}

	public String getDescription() {
		return this.description;
	}

	public String getName() {
		return this.name;
	}

	private Extension insert(Extension extension) {
		LinkedHashMap x = new LinkedHashMap();
		x.put(extension.getName(), extension);
		x.putAll(this.extensions);
		this.extensions = x;
		return extension;
	}

	public ExtensionPoint.Extension insert(String name, String description,
			Class extension) {
		return insert(new Extension(name, description, extension));
	}

	public ExtensionPoint.Extension insert(String name, String description,
			ExtensionBuilder builder) {
		return insert(new Extension(name, description, builder));
	}

	private ExtensionPoint.Extension insert(String beforeName,
			Extension extension) {
		if (extensions.containsKey(beforeName)) {
            LinkedHashMap x = new LinkedHashMap();

            Iterator it = this.extensions.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry e = (Map.Entry) it.next();
                if (e.getKey().equals(beforeName)) {
                    x.put(extension.getName(), extension);
                }
                x.put(e.getKey(), e.getValue());
            }
            this.extensions = x;
        } else {
            // If the beforName extension is not already registered, add
            // the new extension as the last one
            extensions.put(extension.getName(), extension);
        }
		
		return extension;
	}

	synchronized public ExtensionPoint.Extension insert(String beforeName,
			String name,
			String description, Class extension) {
		return insert(beforeName, new Extension(name, description, extension));
	}

	synchronized public ExtensionPoint.Extension insert(String beforeName,
			String name,
			String description, ExtensionBuilder builder) {
		return insert(beforeName, new Extension(name, description, builder));
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean addAlias(String name, String alias) {
		Extension extension = (Extension) this.extensions.get(name);
		if (extension == null) {
			return false;
		}
		this.alias.put(alias, extension);
		extension.alias.add(alias);
		return true;
	}

	public Object create(String name) throws InstantiationException,
			IllegalAccessException {
		Extension extension = (Extension) this.alias.get(name);
		if (extension == null) {
			extension = (Extension) this.extensions.get(name);
			if (extension == null) {
				return null; // FIXME: throw exception
			}
		}
		return extension.create();
	}

	public Object create(String name, Object[] args) throws SecurityException,
			IllegalArgumentException, NoSuchMethodException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {
		Extension extension = (Extension) this.alias.get(name);
		if (extension == null) {
			extension = (Extension) this.extensions.get(name);
			if (extension == null) {
				return null; // FIXME: throw exception
			}
		}
		return extension.create(args);
	}

	public Object create(String name, Map args) throws SecurityException,
			IllegalArgumentException, NoSuchMethodException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {
		Extension extension = (Extension) this.alias.get(name);
		if (extension == null) {
			extension = (Extension) this.extensions.get(name);
			if (extension == null) {
				return null; // FIXME: throw exception
			}
		}
		return extension.create(args);
	}

	public ExtensionPoint.Extension get(String name) {
		Extension extension = (Extension) this.alias.get(name);
		if (extension == null) {
			extension = (Extension) this.extensions.get(name);
			if (extension == null) {
				return null; // FIXME: throw exception
			}
		}
		return extension;
	}

	public int getCount() {
		return this.extensions.size();
	}

	public Iterator iterator() {
		return this.extensions.values().iterator();
	}

	public List getNames() {
		String[] names = (String[]) this.extensions.keySet().toArray(
				new String[] {});
		Arrays.sort(names);
		return Collections.unmodifiableList(Arrays.asList(names));
	}

	public boolean has(String name) {
		return this.extensions.containsKey(name);
	}

}
