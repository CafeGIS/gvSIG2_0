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
package org.gvsig.compat.se;

import org.gvsig.compat.CompatLocator;
import org.gvsig.compat.se.lang.SEGraphUtils;
import org.gvsig.compat.se.lang.SEMathUtils;
import org.gvsig.compat.se.lang.StandardStringUtils;
import org.gvsig.tools.locator.BaseLibrary;

/**
 * Initialization of the libCompat library, Java Standard Edition
 * implementation.
 * 
 * @author <a href="mailto:cordin@disid.com">C�sar Ordi�ana</a>
 */
public class SECompatLibrary extends BaseLibrary {

    public void initialize() {
        super.initialize();
        CompatLocator.registerStringUtils(StandardStringUtils.class);
        CompatLocator.registerMathUtils(SEMathUtils.class);
        CompatLocator.registerGraphicsUtils(SEGraphUtils.class);
    }

}