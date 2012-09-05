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
package org.gvsig.fmap.dal.feature.testmulithread;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.DataRuntimeException;
import org.gvsig.fmap.dal.feature.DisposableIterator;
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.exception.ConcurrentDataModificationException;

/**
 * @author jmvivo
 *
 */
public class DeleteFirstAndLastFeature extends StoreTask {

	/**
	 * @param store
	 * @param timeToWait
	 */
	public DeleteFirstAndLastFeature(String id, FeatureStore store) {
		super(id, store);
	}

	/**
	 * @param store
	 * @param timeToWait
	 */
	public DeleteFirstAndLastFeature(String id, FeatureStore store, int timeToWait) {
		super(id, store, timeToWait);
	}

	public void run() {
		if (!this.startProcess()) {
			return;
		}
		FeatureSet set;
		DisposableIterator iter = null;
		try {
			set = this.store.getFeatureSet();
		} catch (DataException e) {
			finishedError(e);
			return;
		}

		try {
			long size = set.getSize();
			try {

				iter = set.iterator();

				if (!iter.hasNext()) {
					finishedNoOk();
					return;
				}
				iter.next();

				try {
					iter.remove();
					size--;
				} catch (DataRuntimeException e) {
					if (e.getCause() instanceof ConcurrentDataModificationException) {
						finishedConcurrentError(e);
						return;
					}
					finishedError(e);
					return;

				} catch (RuntimeException e) {
					finishedError(e);
					return;
				}


				if (size != set.getSize()) {
					finishedNoOk();
					return;
				}

				while (iter.hasNext()) {
					iter.next();
				}
			} catch (ConcurrentDataModificationException e) {
				finishedConcurrentError(e);
				return;
			} catch (DataException e) {
				finishedError(e);
				return;
			}
			try {

				iter.remove();
				size--;
			} catch (DataRuntimeException e) {
				if (e.getCause() instanceof ConcurrentDataModificationException) {
					finishedConcurrentError(e);
					return;
				}
				finishedError(e);
				return;

			} catch (RuntimeException e) {
				finishedError(e);
				return;
			}
			if (size != set.getSize()) {
				finishedNoOk();
			} else {
				finishedOk();
			}
		} catch (Throwable e) {
			finishedError(e);
			return;
		} finally {
			iter.dispose();
			set.dispose();
		}
	}


}
