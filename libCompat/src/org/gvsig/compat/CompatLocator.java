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
 * 2008 DiSiD Technologies   Create initial base implementation
 */
package org.gvsig.compat;

import org.gvsig.compat.lang.MathUtils;
import org.gvsig.compat.lang.GraphicsUtils;
import org.gvsig.compat.lang.StringUtils;
import org.gvsig.tools.locator.BaseLocator;
import org.gvsig.tools.locator.Locator;
import org.gvsig.tools.locator.LocatorException;

/**
 * Locator for the libCompat Library. Returns references to the library's main
 * utilities.
 * 
 * @author <a href="mailto:cordin@disid.com">Cèsar Ordiñana</a>
 */
public class CompatLocator extends BaseLocator {
    
    /**
     * The name of the StringUtils reference.
     */
    public static final String STRINGUTILS_NAME = "StringUtils";
    
    /**
     * The name of the MathUtils reference.
     */
    public static final String MATHUTILS_NAME = "MathUtils";
    public static final String GRAPHICSUTILS_NAME = "GraphicsUtils";

    /**
     * The description of the StringUtils reference.
     */
    private static final String STRINGUTILS_DESCRIPTION = "Compatible implementation for String Utilities";
    
    /**
     * The description of the MathUtils reference.
     */
    private static final String MATHUTILS_DESCRIPTION = "Compatible implementation for Math Utilities";
    private static final String GRAPHICSUTILS_DESCRIPTION = "Compatible implementation for Graphics Utilities";

    /**
     * Unique instance.
     */
    private static final CompatLocator instance = new CompatLocator();

    /**
     * Return the singleton instance.
     * 
     * @return the singleton instance
     */
    public static CompatLocator getInstance() {
        return instance;
    }

    /**
     * Return a reference to StringUtils.
     * 
     * @return a reference to StringUtils
     * @throws LocatorException
     *             if there is no access to the class or the class cannot be
     *             instantiated
     * @see Locator#get(String)
     */
    public static StringUtils getStringUtils() throws LocatorException {
        return (StringUtils) getInstance().get(STRINGUTILS_NAME);
    }

    /**
     * Return a reference to MathUtils.
     * 
     * @return a reference to MathUtils
     * @throws LocatorException
     *             if there is no access to the class or the class cannot be
     *             instantiated
     * @see Locator#get(String)
     */
    public static MathUtils getMathUtils() throws LocatorException {
        return (MathUtils) getInstance().get(MATHUTILS_NAME);
    }

    
    /**
     * Registers the Class implementing the MathUtils interface.
     * 
     * @param clazz
     *            implementing the MathUtils interface
     */
    public static void registerMathUtils(Class clazz) {
        getInstance()
                .register(MATHUTILS_NAME, MATHUTILS_DESCRIPTION, clazz);
    }
    
    /**
     * Registers the Class implementing the StringUtils interface.
     * 
     * @param clazz
     *            implementing the StringUtils interface
     */
    public static void registerStringUtils(Class clazz) {
        getInstance()
                .register(STRINGUTILS_NAME, STRINGUTILS_DESCRIPTION, clazz);
    }
    
    // ============================================================
    // ============================================================
    // ============================================================
    
    /**
     * Return a reference to GraphicsUtils.
     * 
     * @return a reference to GraphicsUtils
     * @throws LocatorException
     *             if there is no access to the class or the class cannot be
     *             instantiated
     * @see Locator#get(String)
     */
    public static GraphicsUtils getGraphicsUtils() throws LocatorException {
        return (GraphicsUtils) getInstance().get(GRAPHICSUTILS_NAME);
    }

    
    /**
     * Registers the Class implementing the GraphicsUtils interface.
     * 
     * @param clazz
     *            implementing the GraphicsUtils interface
     */
    public static void registerGraphicsUtils(Class clazz) {
        getInstance()
                .register(GRAPHICSUTILS_NAME, GRAPHICSUTILS_DESCRIPTION, clazz);
    }
}