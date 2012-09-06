/**
 *
 */
package org.gvsig.tools.dynobject.impl;

import junit.framework.TestCase;

import org.easymock.MockControl;
import org.gvsig.tools.dynobject.DynObjectManager;

/**
 * @author jmvivo
 *
 */
public class DefaultDynObjectTest extends TestCase {

    private MockControl managerControl;
	private DynObjectManager manager;
	private DefaultDynClass dynClass;

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
        super.setUp();
		managerControl = MockControl.createNiceControl(DynObjectManager.class);
		manager = (DynObjectManager) managerControl.getMock();
		dynClass = new DefaultDynClass(manager, "dynclass", "description");
	}


    /**
     * Test method for {@link org.gvsig.tools.dynobject.impl.DefaultDynObject#hasDynValue(String)}.
     */
    public void testHasDynValueStringTest() {
    	final String fieldName = "myField";
		dynClass.addDynField(fieldName);

		DefaultDynObject obj = new DefaultDynObject(dynClass);

		assertFalse(obj.hasDynValue(fieldName));

		obj.setDynValue(fieldName, "Value");

		assertTrue(obj.hasDynValue(fieldName));

    }

	/**
	 * Test method for
	 * {@link org.gvsig.tools.dynobject.impl.DefaultDynObject#hasDynValue(String)}
	 * from extended class.
	 */
	public void testHasDynValueStringExtendedTest() {

        final String parent = "parent";

		DefaultDynClass parentDynClass = new DefaultDynClass(manager, parent,
				"parent description");

		managerControl.expectAndReturn(manager.has(parent), true);
		managerControl.expectAndReturn(manager.get(parent), parentDynClass);
		managerControl.replay();

		final String fieldName = "myField";
		parentDynClass.addDynField(fieldName);

		dynClass.extend(parent);

		DefaultDynObject obj = new DefaultDynObject(dynClass);

		assertFalse(obj.hasDynValue(fieldName));

		obj.setDynValue(fieldName, "Value");

		assertTrue(obj.hasDynValue(fieldName));

    }



	/**
	 * Test method for
	 * {@link org.gvsig.tools.dynobject.impl.DefaultDynObject#hasDynValue(String)}
	 * from extended class.
	 */
	public void testHasDynValueStringDelegatedTest() {

		final String delegated = "delegated";

		DefaultDynClass delegatedDynClass = new DefaultDynClass(manager,
				delegated,
				"parent description");

		managerControl.expectAndReturn(manager.has(delegated), true);
		managerControl.expectAndReturn(manager.get(delegated),
				delegatedDynClass);
		managerControl.replay();

		final String fieldName = "myField";
		delegatedDynClass.addDynField(fieldName);

		dynClass.addDynField(fieldName);

		DefaultDynObject delegatedObj = new DefaultDynObject(delegatedDynClass);

		DefaultDynObject obj = new DefaultDynObject(dynClass);

		assertFalse(obj.hasDynValue(fieldName));

		delegatedObj.setDynValue(fieldName, "DelegatedValue");

		obj.delegate(delegatedObj);

		assertTrue(obj.hasDynValue(fieldName));

		assertEquals(delegatedObj.getDynValue(fieldName), obj
				.getDynValue(fieldName));

		obj.setDynValue(fieldName, "Value");

		assertTrue(obj.hasDynValue(fieldName));
		assertEquals("Value", obj.getDynValue(fieldName));


	}
}
