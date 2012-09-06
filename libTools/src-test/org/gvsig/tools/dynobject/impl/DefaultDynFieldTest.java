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
* 2008 {DiSiD Technologies}  {{Task}}
*/
package org.gvsig.tools.dynobject.impl;

import junit.framework.TestCase;

/**
 * Unit tests for the class {@link DefaultDynField}.
 * 
 * @author <a href="mailto:cordin@disid.com">Cèsar Ordiñana</a>
 */
public class DefaultDynFieldTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }
    
    public void testEquals() {
        DefaultDynField field1 = new DefaultDynField("field1");
        DefaultDynField field2 = new DefaultDynField("field2");
        DefaultDynField field1b = new DefaultDynField("field1");

        assertTrue(field1.equals(field1b));
        assertFalse(field1.equals(field2));
        assertFalse(field1.equals(null));
    }

}
