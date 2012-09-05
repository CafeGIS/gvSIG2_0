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
 * 2008 PRODEVELOP S.L. Main Development
 */
package org.gvsig.compat.lang;

import junit.framework.TestCase;

/**
 * Parent class for Unit tests for {@link org.gvsig.compat.lang.MathUtils}
 * implementations.
 * 
 * @author <a href="mailto:jcarrasco@prodevelop.es">Javier Carrasco</a>
 */
public abstract class MathUtilsTestParent extends TestCase{
	
	private MathUtils utils;

    protected void setUp() throws Exception {
        super.setUp();
        utils = createUtils();
    }

    /**
     * Return a new instance of an implementation of the MathUtils interface.
     * 
     * @return a new MathUtils instance
     */
    protected abstract MathUtils createUtils();
    
    /**
     * Test method for
     * {@link org.gvsig.compat.se.lang.MathUtils#log10(double)}
     * .
     */
    public void testLog10() {
    	assertEquals(utils.log10(1000),3.0,0.00000000001);
    }
}
