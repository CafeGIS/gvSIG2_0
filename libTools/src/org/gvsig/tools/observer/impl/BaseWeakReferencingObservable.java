/* gvSIG. Geographic Information System of the Valencian Government
 *
 * Copyright (C) 2007-2008 Infrastructures and Transports Department
 * of the Valencian Government (CIT)
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
 * 2008 IVER T.I. S.A.   {{Task}}
 */
package org.gvsig.tools.observer.impl;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.gvsig.tools.observer.ComplexWeakReferencingObservable;
import org.gvsig.tools.observer.Observer;
import org.gvsig.tools.observer.WeakReferencingObservable;

/**
 * Implementation for observers based on the use of Reference, instead of direct
 * Observer references.
 *
 * This way an Observer may be Garbagge collected if its unique reference is the
 * one registered into the Observable.
 *
 * @author <a href="mailto:josemanuel.vivo@iver.es">José Manuel Vivó</a>
 * @author <a href="mailto:cordin@disid.com">Cèsar Ordiñana</a>
 */
public class BaseWeakReferencingObservable implements ComplexWeakReferencingObservable {

    private boolean propageNotifications = true;
    private boolean inComplex = false;
    private ComplexNotification complexNotification = null;

    private boolean changed = false;
    private List obs = new ArrayList();

    /**
     * Adds an observer to the set of observers for this object, provided that
     * it is not the same as some observer already in the set. The order in
     * which notifications will be delivered to multiple observers is not
     * specified. See the class comment.
     *
     * @param observer
     *            an observer to be added.
     * @throws NullPointerException
     *             if the parameter o is null.
     */
    public void addObserver(Observer observer) {
        addObserver(new WeakReference(observer));
    }

    public void addObserver(Reference ref) {
        if (ref == null || ref.get() == null) {
            throw new NullPointerException();
        }
        Observer observer = (Observer) ref.get();
        synchronized (obs) {
            if (!contains(observer)) {
                obs.add(ref);
            }
        }
    }

    public void addObservers(BaseWeakReferencingObservable observable) {
        observable.clearDeadReferences();
        Iterator iter = observable.obs.iterator();
        while (iter.hasNext()) {
            addObserver((Reference) iter.next());
        }

    }

    /**
     * Deletes an observer from the set of observers of this object. Passing
     * <CODE>null</CODE> to this method will have no effect.
     *
     * @param observer
     *            the observer to be deleted.
     */
    public void deleteObserver(Observer observer) {
        deleteObserverReferenced(observer);
    }

    public void deleteObserver(Reference ref) {
        synchronized (obs) {
            obs.remove(ref);
        }
        deleteObserverReferenced((Observer) ref.get());
    }

    /**
     * If this object has changed, as indicated by the <code>hasChanged</code>
     * method, then notify all of its observers and then call the
     * <code>clearChanged</code> method to indicate that this object has no
     * longer changed.
     * <p>
     * Each observer has its <code>update</code> method called with two
     * arguments: this observable object and <code>null</code>. In other words,
     * this method is equivalent to: <blockquote><tt>
     * notifyObservers(null)</tt>
     * </blockquote>
     *
     * @see java.util.Observable#clearChanged()
     * @see java.util.Observable#hasChanged()
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    public void notifyObservers() {
        notifyObservers(null);
    }

    /**
     * If this object has changed, as indicated by the <code>hasChanged</code>
     * method, then notify all of its observers and then call the
     * <code>clearChanged</code> method to indicate that this object has no
     * longer changed.
     * <p>
     * Each observer has its <code>update</code> method called with two
     * arguments: this observable object and the <code>arg</code> argument.
     *
     * @param arg
     *            any object.
     * @see java.util.Observable#clearChanged()
     * @see java.util.Observable#hasChanged()
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    public void notifyObservers(Object arg) {
        if (!inComplex()) {
            setChanged();
            notify(this, arg);
        } else {
            complexNotification.addNotification(arg);
        }

    }

    /**
     * Clears the observer list so that this object no longer has any observers.
     */
    public void deleteObservers() {
        synchronized (obs) {
            obs.clear();
        }
    }

    /**
     * Returns the number of observers of this object.
     *
     * @return the number of observers of this object.
     */
    public int countObservers() {
        clearDeadReferences();
        synchronized (obs) {
            return obs.size();
        }
    }

    public void enableNotifications() {
        clearDeadReferences();
        propageNotifications = true;
    }

    public void disableNotifications() {
        propageNotifications = false;
    }

    public boolean isEnabledNotifications() {
        return propageNotifications;
    }

    public boolean inComplex() {
        return inComplex;
    }

    public void beginComplexNotification() {
        clearDeadReferences();
        clearChanged();
        inComplex = true;
        complexNotification = new ComplexNotification();
        complexNotification.clear();
    }

    public void endComplexNotification() {
        inComplex = false;


        Iterator iter = complexNotification.getIterator();
        while (iter.hasNext()) {
        	setChanged();
            notify(this, iter.next());
        }
        complexNotification = null;
    }

    protected void notify(WeakReferencingObservable observable, Object object) {
        if (!isEnabledNotifications()) {
            return;
        }

        /*
         * a temporary array buffer, used as a snapshot of the state of current
         * Observers.
         */
        Object[] arrLocal;

        synchronized (obs) {
            /*
             * We don't want the Observer doing callbacks into arbitrary code
             * while holding its own Monitor. The code where we extract each
             * DefaultObservable from the Vector and store the state of the
             * Observer needs synchronization, but notifying observers does not
             * (should not). The worst result of any potential race-condition
             * here is that: 1) a newly-added Observer will miss a notification
             * in progress 2) a recently unregistered Observer will be wrongly
             * notified when it doesn't care
             */
            if (!changed) {
                return;
            }
            arrLocal = obs.toArray();
            clearChanged();
        }

        // Go through all the registered observers and call update on them.
        // If any of the Reference is null, remove it from the list.
        for (int i = arrLocal.length - 1; i >= 0; i--) {
            Observer observer = (Observer) ((Reference) arrLocal[i]).get();
            if (observer == null) {
                obs.remove(arrLocal[i]);
            } else {
                observer.update(observable, object);
            }
        }
    }

    /**
     * Tests if this object has changed.
     *
     * @return <code>true</code> if and only if the <code>setChanged</code>
     *         method has been called more recently than the
     *         <code>clearChanged</code> method on this object;
     *         <code>false</code> otherwise.
     * @see java.util.Observable#clearChanged()
     * @see java.util.Observable#setChanged()
     */
    protected boolean hasChanged() {
        return changed;
    }

    /**
     * Marks this <tt>DefaultObservable</tt> object as having been changed; the
     * <tt>hasChanged</tt> method will now return <tt>true</tt>.
     */
    protected void setChanged() {
        changed = true;
    }

    /**
     * Indicates that this object has no longer changed, or that it has already
     * notified all of its observers of its most recent change, so that the
     * <tt>hasChanged</tt> method will now return <tt>false</tt>. This method is
     * called automatically by the <code>notifyObservers</code> methods.
     *
     * @see java.util.Observable#notifyObservers()
     * @see java.util.Observable#notifyObservers(java.lang.Object)
     */
    protected void clearChanged() {
        changed = false;
    }

    private void clearDeadReferences() {
        synchronized (obs) {
            Iterator iter = obs.iterator();
            while (iter.hasNext()) {
                Object obj = iter.next();
                if (obj instanceof Reference && ((Reference) obj).get() == null) {
                    iter.remove();
                }
            }
        }
    }

    private boolean contains(Observer observer) {
        synchronized (obs) {
            Iterator iter = obs.iterator();
            while (iter.hasNext()) {
                Object obj = iter.next();
                if (obj instanceof Reference) {
                    Object value = ((Reference) obj).get();
                    if (observer.equals(value)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void deleteObserverReferenced(Observer observer) {
        if (observer == null) {
            return;
        }
        synchronized (obs) {
            Iterator iter = obs.iterator();
            while (iter.hasNext()) {
                Object obj = iter.next();
                if (obj instanceof Reference) {
                    Object value = ((Reference) obj).get();
                    if (value == null || observer.equals(value)) {
                        iter.remove();
                    }
                }
            }
        }
    }
}