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

package org.gvsig.fmap.dal.feature.testmulithread;

import java.util.Random;

import org.gvsig.fmap.dal.feature.FeatureStore;

public abstract class StoreTask extends Thread {

	public static final int STATUS_NONE = -1;
	public static final int STATUS_WAITING = 0;
	public static final int STATUS_RUNING = 1;

	public static final int STATUS_FINISHED_OK = 20;
	public static final int STATUS_FINISHED_NO_OK = 30;
	public static final int STATUS_FINISHED_ERROR = 40;
	public static final int STATUS_FINISHED_CONCURRENT_ERROR = 50;
	public static final int STATUS_ERROR = 60;

	public static final int TIME_TO_WAIT_RANDOM = -1;
	public static final int TIME_TO_WAIT_NO_WAIT = 0;

	private long startTimer = 0;
	private long afterWaitTimer = 0;
	private long finishTimer = 0;

	private int timeToWait = 0;
	private int finalTimeToWait = 0;

	private int timeToOutOfDate = 5000;

	private int curStatus = STATUS_NONE;

	protected FeatureStore store;
	protected Throwable exception;

	public StoreTask(String name, FeatureStore store) {
		super(name);
		this.store = store;
		this.timeToWait = TIME_TO_WAIT_NO_WAIT;
	}

	public StoreTask(String name, FeatureStore store, int timeToWait) {
		super(name);
		this.store = store;
		this.timeToWait = timeToWait;
	}

	public int getTimeToOutOfDate() {
		return this.timeToOutOfDate;
	}

	public void setTimeToOutOfDate(int mlSeconds) {
		this.timeToOutOfDate = mlSeconds;
	}

	public long getStartTimer() {
		return this.startTimer;
	}

	public long getAfterWaitTimer() {
		return this.afterWaitTimer;
	}

	public long getFinishTimer() {
		return this.finishTimer;
	}

	public int getCurrentStatus() {
		return this.curStatus;
	}

	public boolean isFinishedOk() {
		return this.curStatus == STATUS_FINISHED_OK;
	}

	public Throwable getException() {
		return exception;
	}

	public boolean isOutOfDate() {
		if (curStatus < STATUS_RUNING) {
			return false;
		}
		long curTimer = System.currentTimeMillis();
		if ((curTimer - afterWaitTimer) > (finalTimeToWait + 5000)) {
			return true;
		}
		return false;
	}

	protected boolean startProcess() {
		if (this.curStatus != STATUS_NONE) {
			throw new IllegalStateException();
		}
		this.startTimer = System.currentTimeMillis();
		this.curStatus = STATUS_WAITING;
		if (timeToWait != TIME_TO_WAIT_NO_WAIT) {
			if (timeToWait == TIME_TO_WAIT_RANDOM) {
				Random rnd = new Random();
				rnd.setSeed(System.currentTimeMillis());
				finalTimeToWait = rnd.nextInt(200) + 4;

			} else {
				finalTimeToWait = timeToWait;
			}
			try {
				for (int x = 0; x < 4; x++) {
					yield();
					sleep(finalTimeToWait / 4);
				}
			} catch (InterruptedException e) {
				this.curStatus = STATUS_FINISHED_ERROR;
				this.exception = e;
				return false;
			}
		}
		this.afterWaitTimer = System.currentTimeMillis();
		this.curStatus = STATUS_RUNING;

		return true;
	}

	protected void finishedOk() {
		if (this.curStatus != STATUS_RUNING) {
			throw new IllegalStateException();
		}
		this.finishTimer = System.currentTimeMillis();
		this.curStatus = STATUS_FINISHED_OK;

	}

	protected void finishedNoOk() {
		if (this.curStatus != STATUS_RUNING) {
			throw new IllegalStateException();
		}
		this.finishTimer = System.currentTimeMillis();
		this.curStatus = STATUS_FINISHED_NO_OK;

	}

	protected void finishedError(Throwable ex) {
		if (this.curStatus != STATUS_RUNING) {
			throw new IllegalStateException();
		}
		this.finishTimer = System.currentTimeMillis();
		this.curStatus = STATUS_FINISHED_ERROR;
		this.exception = ex;
	}

	protected void finishedConcurrentError(Exception ex) {
		if (this.curStatus != STATUS_RUNING) {
			throw new IllegalStateException();
		}
		this.finishTimer = System.currentTimeMillis();
		this.curStatus = STATUS_FINISHED_CONCURRENT_ERROR;
		this.exception = ex;
	}


	public boolean isFinished() {
		return curStatus >= STATUS_FINISHED_OK;

	}


}
