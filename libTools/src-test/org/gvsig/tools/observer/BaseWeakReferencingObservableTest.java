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
package org.gvsig.tools.observer;

import java.lang.ref.WeakReference;

import junit.framework.TestCase;

import org.easymock.MockControl;
import org.gvsig.tools.observer.impl.BaseWeakReferencingObservable;

/**
 * Unit tests for the {@link BaseWeakReferencingObservable} class.
 *
 * @author <a href="mailto:cordin@disid.com">Cèsar Ordiñana</a>
 */
public class BaseWeakReferencingObservableTest extends TestCase {

    private BaseWeakReferencingObservable observable;
    private MockControl obsControl;
    private Observer observer;

    protected void setUp() throws Exception {
        super.setUp();
        observable = new BaseWeakReferencingObservable();
        obsControl = MockControl.createStrictControl(Observer.class);
        observer = (Observer) obsControl.getMock();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test method for
     * {@link org.gvsig.tools.observer.impl.BaseWeakReferencingObservable#enableNotifications()}
     * .
     */
    public void testEnableNotifications() {
        observer.update(observable, null);
        obsControl.replay();

        observable.addObserver(observer);
        observable.enableNotifications();
        observable.notifyObservers();

        obsControl.verify();
    }

    /**
     * Test method for {@link org.gvsig.tools.observer.impl.BaseWeakReferencingObservable#dis}.
     */
    public void testDisableNotifications() {
        // The observer must not be notified
        obsControl.replay();

        observable.addObserver(observer);
        observable.disableNotifications();
        observable.notifyObservers();

        obsControl.verify();
    }

    /**
     * Test method for
     * {@link org.gvsig.tools.observer.impl.BaseWeakReferencingObservable#isEnabledNotifications()}
     * .
     */
    public void testIsEnabledNotifications() {
        assertTrue(observable.isEnabledNotifications());

        observable.disableNotifications();
        assertFalse(observable.isEnabledNotifications());

        observable.enableNotifications();
        assertTrue(observable.isEnabledNotifications());
    }

    /**
     * Test method for
     * {@link org.gvsig.tools.observer.impl.BaseWeakReferencingObservable#addObserver(org.gvsig.tools.observer.Observer)}
     * .
     */
    public void testAddObserverObserver() {
        assertEquals(0, observable.countObservers());
        observable.addObserver(observer);
        assertEquals(1, observable.countObservers());
        observable.addObserver(observer);
        assertEquals(1, observable.countObservers());
    }

    /**
     * Test method for
     * {@link org.gvsig.tools.observer.impl.BaseWeakReferencingObservable#addObserver(java.lang.ref.Reference)}
     * .
     */
    public void testAddObserverReference() {
        assertEquals(0, observable.countObservers());
        observable.addObserver(new WeakReference(observer));
        assertEquals(1, observable.countObservers());
        observable.addObserver(new WeakReference(observer));
        assertEquals(1, observable.countObservers());
    }

    /**
     * Test method for
     * {@link org.gvsig.tools.observer.impl.BaseObservable#addObservers(org.gvsig.tools.observer.impl.BaseObservable)}
     * .
     */
    // public void testAddObservers() {
    // fail("Not yet implemented");
    // }
    /**
     * Test method for
     * {@link org.gvsig.tools.observer.impl.BaseWeakReferencingObservable#deleteObserver(org.gvsig.tools.observer.Observer)}
     * .
     */
    public void testDeleteObserverObserver() {
        observable.addObserver(observer);
        assertEquals(1, observable.countObservers());
        observable.deleteObserver(observer);
        assertEquals(0, observable.countObservers());
    }

    /**
     * Test method for
     * {@link org.gvsig.tools.observer.impl.BaseWeakReferencingObservable#deleteObserver(java.lang.ref.Reference)}
     * .
     */
    public void testDeleteObserverReference() {
        observable.addObserver(new WeakReference(observer));
        assertEquals(1, observable.countObservers());
        observable.deleteObserver(new WeakReference(observer));
        assertEquals(0, observable.countObservers());
    }

    /**
     * Test method for
     * {@link org.gvsig.tools.observer.impl.BaseObservable#notifyObservers()}.
     */
    // public void testNotifyObservers() {
    // fail("Not yet implemented");
    // }
    /**
     * Test method for
     * {@link org.gvsig.tools.observer.impl.BaseObservable#notifyObservers(org.gvsig.tools.observer.Observable, java.lang.Object)}
     * .
     */
    // public void testNotifyObserversObservableObject() {
    // fail("Not yet implemented");
    // }
    /**
     * Test method for
     * {@link org.gvsig.tools.observer.impl.BaseObservable#deleteObservers()}.
     */
    // public void testDeleteObservers() {
    // fail("Not yet implemented");
    // }
    /**
     * Test method for
     * {@link org.gvsig.tools.observer.impl.BaseObservable#hasChanged()}.
     */
    // public void testHasChanged() {
    // fail("Not yet implemented");
    // }
    /**
     * Test method for
     * {@link org.gvsig.tools.observer.impl.BaseObservable#countObservers()}.
     */
    // public void testCountObservers() {
    // fail("Not yet implemented");
    // }
    /**
     * Test method for
     * {@link org.gvsig.tools.observer.impl.BaseObservable#inComplex()}.
     */
    // public void testInComplex() {
    // fail("Not yet implemented");
    // }
    /**
     * Test method for
     * {@link org.gvsig.tools.observer.impl.BaseObservable#beginComplexNotification(org.gvsig.tools.observer.ComplexNotification)}
     * .
     */
    // public void testBeginComplexNotification() {
    // fail("Not yet implemented");
    // }
    /**
     * Test method for
     * {@link org.gvsig.tools.observer.impl.BaseObservable#endComplexNotification(org.gvsig.tools.observer.Observable)}
     * .
     */
    // public void testEndComplexNotification() {
    // fail("Not yet implemented");
    // }
}
