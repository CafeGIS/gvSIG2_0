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

import org.gvsig.compat.lang.GraphicsUtils;
import org.gvsig.compat.lang.MathUtils;
import org.gvsig.compat.lang.StringUtils;
import org.gvsig.tools.locator.BaseLibrary;
import org.gvsig.tools.locator.ReferenceNotRegisteredException;

/**
 * Initialization of the libCompat library.
 * 
 * @author <a href="mailto:cordin@disid.com">Cèsar Ordiñana</a>
 */
public class CompatLibrary extends BaseLibrary {

    public void postInitialize() {
        super.postInitialize();

        // Validate there is any implementation registered.
        StringUtils stringUtils = CompatLocator.getStringUtils();

        if (stringUtils == null) {
            throw new ReferenceNotRegisteredException(
                    CompatLocator.STRINGUTILS_NAME, CompatLocator.getInstance());
        }
        
     // Validate there is any implementation registered.
        MathUtils mathUtils = CompatLocator.getMathUtils();

        if (mathUtils == null) {
            throw new ReferenceNotRegisteredException(
                    CompatLocator.MATHUTILS_NAME, CompatLocator.getInstance());
        }
        
        // Validate there is any implementation registered.
        GraphicsUtils gUtils = CompatLocator.getGraphicsUtils();

        if (gUtils == null) {
            throw new ReferenceNotRegisteredException(
                    CompatLocator.GRAPHICSUTILS_NAME, CompatLocator.getInstance());
        }
        
    }

}