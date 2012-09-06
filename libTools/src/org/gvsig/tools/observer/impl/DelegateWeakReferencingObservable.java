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
package org.gvsig.tools.observer.impl;

import org.gvsig.tools.observer.WeakReferencingObservable;

/**
 * Observer implementation which uses another Observable as the parameter for
 * the notification to the registered Observers.
 * 
 * Most useful to be used as a delegate by Observable clases that can't extend
 * the BaseObservable.
 * 
 * @author <a href="mailto:cordin@disid.com">C�sar Ordi�ana</a>
 */
public class DelegateWeakReferencingObservable extends BaseWeakReferencingObservable {

    private WeakReferencingObservable delegated;

    /**
     * Creates a new DelegateObservable with the Observable to be used to notify
     * the Observers.
     */
    public DelegateWeakReferencingObservable(WeakReferencingObservable observable) {
        delegated = observable;
    }

    protected void notify(WeakReferencingObservable observable, Object object) {
        super.notify(delegated, object);
    }
}
