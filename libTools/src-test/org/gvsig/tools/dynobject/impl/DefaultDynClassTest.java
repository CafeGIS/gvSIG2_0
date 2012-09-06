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

import org.easymock.MockControl;
import org.gvsig.tools.dynobject.DynClass;
import org.gvsig.tools.dynobject.DynField;
import org.gvsig.tools.dynobject.DynObjectManager;

/**
 * Unit tests for the class {@link DefaultDynClass}.
 * 
 * @author <a href="mailto:cordin@disid.com">Cèsar Ordiñana</a>
 */
public class DefaultDynClassTest extends TestCase {
    
    private MockControl managerControl;
    private DynObjectManager manager;
    private DefaultDynClass dynClass;

    protected void setUp() throws Exception {
        super.setUp();
        managerControl = MockControl.createNiceControl(DynObjectManager.class);
        manager = (DynObjectManager) managerControl.getMock();
        dynClass = new DefaultDynClass(manager, "dynclass", "description");
    }

    /**
     * Test method for {@link org.gvsig.tools.dynobject.impl.DefaultDynClass#newInstance()}.
     */
    public void testNewInstance() {
        // TODO: solve
        // MockControl dynObjControl = MockControl
        // .createNiceControl(DynObject.class);
        // DynObject dynObj = (DynObject) dynObjControl.getMock();
        // dynObjControl.expectAndReturn(dynObj.getDynClass(), dynClass);
        // dynObjControl.replay();
        //
        // managerControl.expectAndReturn(manager.createDynObject(dynClass),
        // dynObj);
        // managerControl.replay();
        //
        // DynObject newDynObj = dynClass.newInstance();
        // assertTrue(dynClass.isInstance(newDynObj));
    }

    /**
     * Test method for {@link org.gvsig.tools.dynobject.impl.DefaultDynClass#extend(org.gvsig.tools.dynobject.DynClass)}.
     */
    public void testExtendDynClass() {
        final String parent = "parent";

        DefaultDynClass parentDynClass = new DefaultDynClass(manager, parent,
                "parent description");
        
        managerControl.expectAndReturn(manager.has(parent), true);
        managerControl.expectAndReturn(manager.get(parent), parentDynClass);
        managerControl.replay();
        
        dynClass.extend(parentDynClass);

        DynClass[] parents = dynClass.getSuperDynClasses();
        assertEquals(parentDynClass, parents[0]);
    }

    /**
     * Test method for {@link org.gvsig.tools.dynobject.impl.DefaultDynClass#extend(java.lang.String)}.
     */
    public void testExtendString() {
        final String parent = "parent";
        
        DefaultDynClass parentDynClass = new DefaultDynClass(manager, parent,
                "parent description");

        managerControl.expectAndReturn(manager.has(parent), true);
        managerControl.expectAndReturn(manager.get(parent), parentDynClass);
        managerControl.replay();


        dynClass.extend(parent);

        DynClass[] parents = dynClass.getSuperDynClasses();
        assertEquals(parentDynClass, parents[0]);
    }

    /**
     * Test method for
     * {@link org.gvsig.tools.dynobject.impl.DefaultDynClass#getDynField(java.lang.String)}
     * ,
     * {@link org.gvsig.tools.dynobject.impl.DefaultDynClass#addDynField(java.lang.String)}
     * ,
     * {@link org.gvsig.tools.dynobject.impl.DefaultDynClass#removeDynField(java.lang.String)}
     * , {@link org.gvsig.tools.dynobject.impl.DefaultDynClass#getDeclaredDynFields()}.
     */
    public void testDynFields() {
        final String field1 = "field1";
        final String field2 = "field2";
        
        DynField dynField1 = dynClass.addDynField(field1);
        assertEquals(dynField1, dynClass.getDynField(field1));

        DynField dynField2 = dynClass.addDynField(field2);
        assertEquals(dynField2, dynClass.getDynField(field2));

        DynField[] fields = dynClass.getDeclaredDynFields();
        assertEquals(2, fields.length);

        dynClass.removeDynField(field1);
        assertNull(dynClass.getDynField(field1));
    }
    
    
    /**
     * Test method for
     * {@link org.gvsig.tools.dynobject.impl.DefaultDynClass#equals(Object)}.
     */
    public void testEquals() {
        DefaultDynClass dynClass2 = new DefaultDynClass(manager, "dynclass2",
                "description2");
        DefaultDynClass dynClassB = new DefaultDynClass(manager, "dynclass",
                "description");

        assertTrue(dynClass.equals(dynClassB));
        assertFalse(dynClass.equals(dynClass2));
        assertFalse(dynClass.equals(null));
    }
}