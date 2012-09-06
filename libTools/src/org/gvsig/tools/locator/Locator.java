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

/**
 * Manages references to the objects of a Library or module.
 *
 * @author <a href="mailto:cordin@disid.com">Cèsar Ordiñana</a>
 */
public interface Locator {

    /**
     * Returns a reference to the object with the given name.
     *
     * @param name
     *            of the object to return
     * @return an instance of an object, or null if not found
     * @throws LocatorException
     *             if there is no access to the class or the class cannot be
     *             instantiated
     */
    Object get(String name) throws LocatorException;

    /**
     * Returns the list of names of references available through this Locator.
     * Must return null if there are not any registered names.
     *
     * @return the list of names of references
     */
    String[] getNames();

    /**
     * Registers a class related to a name. The class is used to create an
     * instance of the object to return in the {@link #get(String)} method.
     *
     * @param name
     *            of the object to register
     * @param clazz
     *            the Class of the object to register
     */
    void register(String name, Class clazz);

    void registerDefault(String name, Class clazz);

    /**
     * Registers a class related to a name. The class is used to create an
     * instance of the object to return in the {@link #get(String)} method.
     *
     * @param name
     *            of the object to register
     * @param description
     *            of the object to register
     * @param clazz
     *            the Class of the object to register
     */
    void register(String name, String description, Class clazz);

    void registerDefault(String name, String description, Class clazz);

    /**
     * Registers an object factory related to a name. The factory is used to
     * create an instance of the object to return in the {@link #get(String)}
     * method.
     *
     * @param name
     *            of the object to register
     * @param factory
     *            the factory of objects to register
     */
    void register(String name, LocatorObjectFactory factory);

    /**
     * Registers an object factory related to a name. The factory is used to
     * create an instance of the object to return in the {@link #get(String)}
     * method.
     *
     * @param name
     *            of the object to register
     * @param description
     *            of the object to register
     * @param factory
     *            the factory of objects to register
     */
    void register(String name, String description, LocatorObjectFactory factory);

    /**
     * Returns the name of the Locator, for registration, logging, and other
     * uses.
     *
     * @return the name of the Locator
     */
    String getLocatorName();
}
