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
* 2009 IVER T.I   {{Task}}
*/

package org.gvsig.tools.observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import junit.framework.TestCase;

import org.gvsig.tools.observer.impl.BaseWeakReferencingObservable;

public class myTest extends TestCase {

    private BaseWeakReferencingObservable observable;

    protected void setUp() throws Exception {
		super.setUp();
		observable = new BaseWeakReferencingObservable();
	}

    protected void tearDown() throws Exception {
		super.tearDown();
	}


	public void testComplex() {

		Random rnd = new Random();
		rnd.setSeed(System.currentTimeMillis());
		int i;
		int notifications = rnd.nextInt(10) + 2;
		int observers = rnd.nextInt(10) + 3;
		ArrayList observerList = new ArrayList(observers);
		MyObserver obs;
		for (i = 0; i < observers; i++) {
			obs = new MyObserver();
			obs.addObserverInFirstNotification = rnd.nextBoolean();
			observerList.add(obs);
			observable.addObserver(obs);
		}
		observable.beginComplexNotification();
		for (i = 0; i < notifications; i++) {
			Object obj = new Integer(i);
			observable.notifyObservers(obj);
		}

		for (i=0; i < observers;i++){
			obs = (MyObserver) observerList.get(i);
			assertEquals(0, obs.notifications.size());
			assertNull(obs.observerAdded);
		}

		observable.endComplexNotification();

		for (i = 0; i < observers; i++) {
			obs = (MyObserver) observerList.get(i);
			assertEquals(notifications, obs.notifications.size());
			if (obs.addObserverInFirstNotification) {
				assertNotNull(obs.observerAdded);
				assertEquals(notifications - 1, obs.observerAdded.notifications
						.size());
			}
		}







	}
    private class MyObserver implements Observer {
		public List notifications = new ArrayList();
		public boolean addObserverInFirstNotification = false;
		public MyObserver observerAdded = null;


		public void update(Observable observable, Object notification) {
			notifications.add(notification);
			if (addObserverInFirstNotification && notifications.size() == 1) {
				observerAdded = new MyObserver();
				observable.addObserver(observerAdded);
			}
		}

	}

}
