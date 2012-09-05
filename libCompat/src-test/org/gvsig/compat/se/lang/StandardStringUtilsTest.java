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
 * 2008 {{Company}}   {{Task}}
 */
package org.gvsig.compat.se.lang;

import org.gvsig.compat.lang.StringUtils;
import org.gvsig.compat.lang.StringUtilsTestParent;

/**
 * Unit tests for the {@link org.gvsig.compat.se.lang.StandardStringUtils}
 * class.
 * 
 * @author <a href="mailto:cordin@disid.com">C�sar Ordi�ana</a>
 */
public class StandardStringUtilsTest extends StringUtilsTestParent {

    protected StringUtils createUtils() {
        return new StandardStringUtils();
    }

}