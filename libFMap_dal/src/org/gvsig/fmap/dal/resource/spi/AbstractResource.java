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
* 2008 IVER T.I   {{Task}}
*/

/**
 *
 */
package org.gvsig.fmap.dal.resource.spi;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.gvsig.fmap.dal.exception.CopyParametersException;
import org.gvsig.fmap.dal.exception.InitializeException;
import org.gvsig.fmap.dal.resource.ResourceNotification;
import org.gvsig.fmap.dal.resource.ResourceParameters;
import org.gvsig.fmap.dal.resource.exception.AccessResourceException;
import org.gvsig.fmap.dal.resource.exception.PrepareResourceException;
import org.gvsig.fmap.dal.resource.exception.ResourceBeginException;
import org.gvsig.fmap.dal.resource.exception.ResourceException;
import org.gvsig.fmap.dal.resource.exception.ResourceNotifyChangesException;
import org.gvsig.fmap.dal.resource.exception.ResourceNotifyCloseException;
import org.gvsig.fmap.dal.resource.exception.ResourceNotifyDisposeException;
import org.gvsig.fmap.dal.resource.exception.ResourceNotifyOpenException;
import org.gvsig.fmap.dal.resource.impl.DefaultResourceNotification;
import org.gvsig.tools.observer.Observer;
import org.gvsig.tools.observer.WeakReferencingObservable;
import org.gvsig.tools.observer.impl.BaseWeakReferencingObservable;
import org.gvsig.tools.observer.impl.DelegateWeakReferencingObservable;

public abstract class AbstractResource implements ResourceProvider, WeakReferencingObservable {

	private DelegateWeakReferencingObservable delegateObservable;

	private List consumers;

	private Date lastTimeOpen;
	private Date lastTimeUsed;

	private boolean inUse;
	private Object data;

	private int openCount;

	private Object semaforo;
	private Reference threadRef = null;
	private int beginCount = 0;


	private ResourceParameters parameters;
	private ResourceParameters preparedParameters;

	protected AbstractResource(ResourceParameters parameters)
			throws InitializeException {
		try {
			delegateObservable = new DelegateWeakReferencingObservable(this);
			consumers = new ArrayList();
			lastTimeOpen = new Date();
			lastTimeUsed = new Date();
			inUse = false;
			openCount = 0;
			semaforo = new Object();
			preparedParameters = null;
			this.parameters = (ResourceParameters) parameters.getCopy();
		} catch (CopyParametersException e) {
			throw new InitializeException(e);
		}
	}

	final private void updateLastTimeUsed() {
		this.lastTimeUsed.setTime(System.currentTimeMillis());
	}

	final private void updateLastTimeOpen() {
		this.lastTimeOpen.setTime(System.currentTimeMillis());
		this.updateLastTimeUsed();
	}

	final public Date getLastTimeOpen() {
		return new Date(this.lastTimeOpen.getTime());
	}

	final public Date getLastTimeUsed() {
		return new Date(this.lastTimeOpen.getTime());
	}

	public final ResourceParameters getParameters() {
		if (preparedParameters != null) {
			return preparedParameters;
		}
		return this.parameters;
	}

	public void prepare(ResourceParameters params)
			throws PrepareResourceException {
		ResourceNotification notification = new DefaultResourceNotification(
				this, ResourceNotification.PREPARE, params);
		this.delegateObservable.notifyObservers(notification);
	}

	public void prepare() throws PrepareResourceException {
		if (preparedParameters == null) {
			try {
				ResourceParameters params = (ResourceParameters) parameters
						.getCopy();
				prepare(params);
				preparedParameters = params;
			} catch (CopyParametersException e) {
				throw new PrepareResourceException(this, e);
			}
		}

	}


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

	public final void begin() throws ResourceBeginException {
		synchronized (semaforo) {
			if (inUse && allreadyBeginThisConsumer()) {
				beginCount++;
				return;
			}
			while (inUse) {
				try {
					semaforo.wait();
				} catch (InterruptedException e) {
					throw new ResourceBeginException(this, e);
				}
			}
			inUse = true;
			beginCount = 1;
			this.threadRef = new WeakReference(Thread.currentThread());
			updateLastTimeUsed();
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
			// XXX try to do a end from a diferent thread
			throw new RuntimeException();
		}
		synchronized (semaforo) {
			this.threadRef = null;
			beginCount = 0;
			updateLastTimeUsed();
			inUse = false;
			semaforo.notifyAll();
		}
	}

	public final boolean inUse() {
		return inUse;
	}

	public void notifyOpen() throws ResourceNotifyOpenException {
		this.notifyObserver(ResourceNotification.OPEN);
		updateLastTimeOpen();
		openCount++;
	}

	public void notifyClose() throws ResourceNotifyCloseException {
		if (openCount <= 0) {
			throw new IllegalStateException();
		}
		this.notifyObserver(ResourceNotification.CLOSE);
		openCount--;
	}

	public void notifyChanges() throws ResourceNotifyChangesException {
		this.notifyObserver(ResourceNotification.CHANGED);


		Iterator it = consumers.iterator();
		while (it.hasNext()) {
			ResourceConsumer consumer = (ResourceConsumer) ((WeakReference) it
					.next()).get();
			if (consumer != null) {
				consumer.resourceChanged(this);
			} else {
				it.remove();
			}
		}

	}

	public void notifyDispose() throws ResourceNotifyDisposeException {
		this.notifyObserver(ResourceNotification.DISPOSE);

		consumers.clear();
		consumers = null;

		lastTimeOpen = null;
		lastTimeUsed = null;

		data = null;

		semaforo = null;

		delegateObservable.deleteObservers();
		delegateObservable = null;
	}

	public boolean isOpen() {
		return this.openCount > 0;
	}

	public int openCount() {
		return this.openCount;
	}

	public void addObserver(Observer o) {
		this.delegateObservable.addObserver(o);
	}

	public void deleteObserver(Observer o) {
		this.delegateObservable.deleteObserver(o);
	}

	public void deleteObservers() {
		this.delegateObservable.deleteObservers();

	}

	public final void addObservers(BaseWeakReferencingObservable observers) {
		this.delegateObservable.addObservers(observers);
	}

	public final void addConsumer(ResourceConsumer consumer) {
		this.updateConsumersList();
		consumers.add(new WeakReference(consumer));
	}

	public final void removeConsumer(ResourceConsumer consumer) {
		ResourceConsumer cur;
		Iterator it = consumers.iterator();
		while (it.hasNext()) {
			cur = (ResourceConsumer) ((WeakReference) it
					.next()).get();
			if (cur == null || (cur == consumer)) {
				it.remove();
			}
		}
	}

	public int getConsumersCount() {
		this.updateConsumersList();
		return consumers.size();
	}

	private synchronized void updateConsumersList() {
		Iterator it = consumers.iterator();
		WeakReference ref;
		while (it.hasNext()) {
			ref = (WeakReference) it.next();
			if (ref.get() == null) {
				it.remove();
			}
		}
	}

	public void closeRequest() throws ResourceException {
		if (inUse) {
			return;
		}
		Iterator it = consumers.iterator();
		while (it.hasNext()) {
			ResourceConsumer consumer = (ResourceConsumer) ((WeakReference) it
					.next()).get();
			if (consumer != null) {
				consumer.closeResourceRequested(this);
			} else {
				it.remove();
			}
		}
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Object getData() {
		return this.data;
	}

	protected void notifyObserver(String type) {
		this.delegateObservable
                .notifyObservers(new DefaultResourceNotification(
				this, type));
	}

	public abstract String getName() throws AccessResourceException;

	public abstract Object get() throws AccessResourceException;


}

