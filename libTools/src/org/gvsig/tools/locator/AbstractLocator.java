/* gvSIG. Geographic Information System of the Valencian Government
 *
 * Copyright (C) 2007-2008 Infrastructures and Transports Department
 * of the Valencian Gobernment (CIT)
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 *
 */

/*
 * AUTHORS (In addition to CIT):
 * 2008 {DiSiD Technologies}   {Create a base Locator implementation}
 */
package org.gvsig.tools.locator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gvsig.tools.extensionpoint.ExtensionPoint;
import org.gvsig.tools.extensionpoint.ExtensionPointManager;
import org.gvsig.tools.extensionpoint.impl.DefaultExtensionPointManager;

/**
 * Locator implementation based on the use of the ExtensionPoints.
 *
 * @author <a href="mailto:cordin@disid.com">Cèsar Ordiñana</a>
 */
public abstract class AbstractLocator implements Locator {

    private Map instances = new HashMap();

    private Object lock = new Object();

    public Object get(String name) throws LocatorException {
        Object instance = null;

        // Synchronize the creation and storage of instances
        synchronized (lock) {
            instance = instances.get(name);
            if (instance == null) {
                try {
                    instance = getExtensionPoint().create(name);
                } catch (Exception ex) {
                    throw new LocatorReferenceException(ex, name, this);
                }
                instances.put(name, instance);
            }
        }

        return instance;
    }

    public String[] getNames() {
        ExtensionPoint extensionPoint = getExtensionPoint();
        List names = extensionPoint.getNames();
        return names == null || names.size() == 0 ? null
                : (String[]) names
                .toArray(new String[names.size()]);
    }

    public void register(String name, Class clazz) {
    	DefaultExtensionPointManager.getManager().add(getLocatorName())
				.append(name, null, clazz);
    }

    public void registerDefault(String name, Class clazz) {
		ExtensionPoint ep = getExtensionPoint();
		if (ep.get(name) == null) {
			register(name, clazz);
		}
	}

    public void register(String name, String description, Class clazz) {
    	DefaultExtensionPointManager.getManager().add(getLocatorName())
				.append(name,
                description, clazz);
    }

    public void registerDefault(String name, String description, Class clazz) {
    	ExtensionPoint ep = getExtensionPoint();
    	if( ep.get(name) == null ) {
    		register(name,description, clazz);
    	}
    }

    public void register(String name, LocatorObjectFactory factory) {
    	DefaultExtensionPointManager.getManager().add(getLocatorName()).append(
				name, null,
                factory);
    }

    public void register(String name, String description,
            LocatorObjectFactory factory) {
    	DefaultExtensionPointManager.getManager().add(getLocatorName()).append(
				name,
                description, factory);
    }

    public String toString() {
        return getLocatorName();
    }

    /**
     * Returns the ExtensionPoint to use for the Locator values.
     */
    private ExtensionPoint getExtensionPoint() {
        ExtensionPointManager manager = DefaultExtensionPointManager
				.getManager();
        String moduleName = getLocatorName();
        // synchronize the retrieval of the ExtensionPoint
        synchronized (lock) {
            ExtensionPoint extensionPoint = manager.get(moduleName);
            if (extensionPoint == null) {
                extensionPoint = manager.add(moduleName);
            }
            return extensionPoint;
        }
    }
}