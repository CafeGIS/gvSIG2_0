/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
 *
 * For more information, contact:
 *
 *  Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ibáñez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   IVER T.I. S.A
 *   Salamanca 50
 *   46005 Valencia
 *   Spain
 *
 *   +34 963163400
 *   dac@iver.es
 */
package com.iver.utiles.exceptionHandling;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * <p>Stores a group of <code>ExceptionListener</code> that, in whatever moment
 * could be notified a <code>Throwable</code> Java error or exception.</p>
 * 
 * <p><code>ExceptionHandlingSupport</code> is useful to manage a set of listeners
 * that need to be executed when a particular error or exception in a object is produced. For
 * instance, when user drags the mouse over a geometrical object in a view, it's possible that
 * some <code>ExceptionLister</code> of that object and other nearby, must be notified. Using
 * <code>ExceptionHandlingSupport</code> the developer can manage easily a set of that kind of
 * listeners, that can change according to an external factor. (In the previous sample, for intance,
 * can change according the closed objects to the one which this <code>ExceptionHandlingSupport</code>
 * refers).</p>
 */
public class ExceptionHandlingSupport {
	/**
	 * <p>List with a group of <code>ExceptionListener</code>.</p>
	 */
	private ArrayList exceptionListeners = new ArrayList();

    /**
     * <p>Adds a new <code>ExceptionListener</code> for adding support to it.</p>
     *
     * @param o listener adapted to be notified by the <code>ExceptionHandlingSupport</code>
     */
    public void addExceptionListener(ExceptionListener o) {
        exceptionListeners.add(o);
    }

    /**
     * <p>Removes an <code>ExceptionListener</code> for finishing the support to it.</p>
     *
     * @param o listener adapted to be notified by the <code>ExceptionHandlingSupport</code>
     *
     * @return <code>true</code> if the list contained the specified element, <code>false</code> otherwise
     */
    public boolean removeExceptionListener(ExceptionListener o) {
        return exceptionListeners.remove(o);
    }

    /**
     * <p>Notifies all registered listeners that an error or exception
     *  throwable by the Java Virtual Machine has been produced.</p>
     *
     * @param t an error or exception in the Java language
     */
    public void throwException(Throwable t) {
        for (Iterator iter = exceptionListeners.iterator(); iter.hasNext();) {
            ExceptionListener listener = (ExceptionListener) iter.next();
            listener.exceptionThrown(t);
        }
    }
}
