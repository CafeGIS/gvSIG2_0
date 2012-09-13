/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2007 IVER T.I. and Generalitat Valenciana.
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
 */
package org.gvsig.raster.util;

/*
 * Created on 10-mar-2006
 *
 * gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
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
 */

import java.util.Vector;

/**
 * A simple FIFO queue class which causes the calling thread to wait if the
 * queue is empty and notifies threads that are waiting when it is not
 * empty.
 * 
 * @author Anil V (akv@eng.sun.com)
 */
public class Queue {
	private Vector vector = new Vector();

	/**
	 * Put the object into the queue.
	 * 
	 * @param object
	 *            the object to be appended to the queue.
	 */
	public synchronized void put(Object object) {
		vector.addElement(object);
		notify();
	}

	/**
	 * Pull the first object out of the queue. Wait if the queue is empty.
	 */
	public synchronized Object pull() {
		while (isEmpty())
			try {
				wait();
			} catch (InterruptedException ex) {
			}
			return get();
	}

	/**
	 * Get the first object out of the queue. Return null if the queue is
	 * empty.
	 */
	public synchronized Object get() {
		Object object = peek();
		if (object != null)
			vector.removeElementAt(0);
		return object;
	}

	/**
	 * Peek to see if something is available.
	 */
	public Object peek() {
		if (isEmpty())
			return null;
		return vector.elementAt(0);
	}

	/**
	 * Is the queue empty?
	 */
	public boolean isEmpty() {
		return vector.isEmpty();
	}

	/**
	 * How many elements are there in this queue?
	 */
	public int size() {
		return vector.size();
	}
}
