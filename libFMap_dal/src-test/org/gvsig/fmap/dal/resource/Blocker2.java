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
* 2009 IVER T.I. S.A.   {{Task}}
*/

/**
 *
 */
package org.gvsig.fmap.dal.resource;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * @author jmvivo
 *
 */
public class Blocker2 extends AbstractBlocker {
	private Object semaforo = new Object();
	private Reference threadRef = null;
	private int beginCount = 0;

	private boolean allreadyBeginThisConsumer() {
		if (threadRef == null) {
			return false;
		}
		if (threadRef.isEnqueued()) {
			threadRef = null;
			return false;
		}
		Thread thread = (Thread) threadRef.get();
		if (thread == null) {
			threadRef = null;
			return false;
		}
		return Thread.currentThread().equals(thread);
	}

	public void begin(int idConsumer, int consumerData) {
		if (inUse && allreadyBeginThisConsumer()) {
			beginCount++;
			return;
		}

		synchronized (semaforo) {
			while (inUse) {
				try {
					semaforo.wait();
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
			beginCount = 1;
			this.setUsed(idConsumer, consumerData);
		}
	}



	public final void end() {
		if (!inUse) {
			// XXX or throw an excetion???
			return;
		}
		if (allreadyBeginThisConsumer()) {
			if (beginCount > 1) {
				beginCount--;
				return;
			}
		} else if (beginCount > 0) {
			// XXX Some one try to do a end from a diferent thread
			throw new RuntimeException();
		}
		synchronized (semaforo) {
			this.endUsed();
			semaforo.notifyAll();
		}
	}

	protected void setUsed(int idConsumer, int consumerData) {
		super.setUsed(idConsumer, consumerData);
		this.threadRef = new WeakReference(Thread.currentThread());
	}

	protected void endUsed() {
		super.endUsed();
		this.threadRef = null;
	}

}
