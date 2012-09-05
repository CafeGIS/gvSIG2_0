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

import java.util.Random;

import junit.framework.TestCase;

/**
 * @author jmvivo
 *
 */
public class BlockTest extends TestCase {

	static int MAX_TIME_OPEN = 200;

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testBlocker1() {
		AbstractBlocker blocker = new Blocker1();

		runTesters(blocker, 5, 6);
		runMultyBeginTesters(blocker, 5, 6, 3);
	}

	public void testBlocker2() {
		AbstractBlocker blocker = new Blocker2();

		runTesters(blocker, 5, 6);
		runMultyBeginTesters(blocker, 5, 6, 6);

	}

	private void runTesters(AbstractBlocker blocker, int numTesters,
			int maxBegins) {
		BlockerTester[] testers = new BlockerTester[numTesters];
		Random rnd = new Random();
		rnd.setSeed(System.currentTimeMillis());
		for (int i = 0; i < testers.length; i++) {
			testers[i] = new BlockerTester(i, blocker,
					rnd.nextInt(maxBegins) + 1);
		}

		for (int i = 0; i < testers.length; i++) {
			testers[i].start();
		}

		try {
			Thread.sleep(
					(numTesters * maxBegins * MAX_TIME_OPEN) + (1000));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (int i = 0; i < testers.length; i++) {
			assertTrue("Tester " + i, testers[i].isOk());
		}
	}

	private void runMultyBeginTesters(AbstractBlocker blocker, int numTesters,
			int maxBegins, int maxInternalBegins) {
		BlockerMultyTester[] testers = new BlockerMultyTester[numTesters];
		Random rnd = new Random();
		rnd.setSeed(System.currentTimeMillis());
		for (int i = 0; i < testers.length; i++) {
			testers[i] = new BlockerMultyTester(i, blocker,
					rnd
					.nextInt(maxBegins) + 1, rnd.nextInt(maxInternalBegins));
		}

		for (int i = 0; i < testers.length; i++) {
			testers[i].start();
		}

		try {
			Thread
					.sleep((numTesters * maxBegins * maxInternalBegins * MAX_TIME_OPEN) + (1000));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (int i = 0; i < testers.length; i++) {
			assertTrue("Tester " + i, testers[i].isOk());
		}
	}


	class BlockerTester extends Thread {
		AbstractBlocker blocker;

		private boolean isOk;

		private int timesToDo;

		private int id;

		/**
		 *
		 */
		public BlockerTester(int id, AbstractBlocker blocker, int timesToDo) {
			super();
			this.id = id;
			this.blocker = blocker;
			this.timesToDo = timesToDo;
			this.isOk = false;
		}

		private boolean checkBlocker(int data) {
			if (!blocker.inUse()) {
				System.out.println("a");
				return false;
			}
			if (blocker.getConsumerId() != this.id) {
				System.out.println("b");
				return false;
			}
			if (blocker.getConsumerData() != data) {
				System.out.println("c");
				return false;
			}

			return true;
		}

		public void run() {
			Random rnd = new Random();
			rnd.setSeed(System.currentTimeMillis());

			for (int i = 1; i <= this.timesToDo; i++) {
				System.out.println("begin: " + this.id + " (" + i + "/"
						+ this.timesToDo + ")");
				this.blocker.begin(this.id, i);


				System.out.println("runing: " + this.id + " (" + i + "/"
						+ this.timesToDo + ")");

				if (!this.checkBlocker(i)) {
					this.isOk = false;
					return;
				}
				try {
					sleep(rnd.nextInt(MAX_TIME_OPEN) + 1);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				} finally {

					if (!this.checkBlocker(i)) {
						this.isOk = false;
						return;
					}

					System.out.println("end: " + this.id + " (" + i + "/"
							+ this.timesToDo + ")");

					this.blocker.end();
				}
			}
			System.out.println("ok: " + this.id + " - " + this.timesToDo
					+ " times");

			this.isOk = true;
		}

		public boolean isOk() {
			return this.isOk;
		}
	}

	/**
	 * 
	 * 
	 * @author jmvivo
	 * 
	 */
	class BlockerMultyTester extends Thread {
		AbstractBlocker blocker;

		private boolean isOk;

		private int timesToDo;

		private int timesToDoInternal;

		private int id;

		private Random rnd = new Random();

		/**
		 *
		 */
		public BlockerMultyTester(int id, AbstractBlocker blocker,
				int timesToDo, int timesToDoInternal) {
			super();
			this.id = id;
			this.blocker = blocker;
			this.timesToDo = timesToDo;
			this.timesToDoInternal = timesToDoInternal;
			this.isOk = false;
		}

		private boolean checkBlocker(int data) {
			if (!blocker.inUse()) {
				return false;
			}
			if (blocker.getConsumerId() != this.id) {
				return false;
			}
			if (blocker.getConsumerData() != data) {
				return false;
			}

			return true;
		}

		private void doEnd(int data, int internal) {
			String idStr;
			if (internal > 0) {
				idStr = "(" + data + "/" + this.timesToDo + "." + internal
						+ ")";
			} else {
				idStr = "(" + data + "/" + this.timesToDo + ")";
			}

			if (!this.checkBlocker(data)) {
				this.isOk = false;
				return;
			}

			System.out.println("end: " + this.id + " " + idStr);

			this.blocker.end();

		}

		private void doBegin(int data, int internal) {
			String idStr;
			if (internal > 0) {
				idStr = "(" + data + "/" + this.timesToDo + "." + internal
						+ ")";
			} else {
				idStr = "(" + data + "/" + this.timesToDo + ")";
			}
			System.out.println("begin: " + this.id + " " + idStr);
			this.blocker.begin(this.id, data);

			System.out.println("runing: " + this.id + " " + idStr);

			if (!this.checkBlocker(data)) {
				this.isOk = false;
				return;
			}

		}

		public void run() {
			rnd.setSeed(System.currentTimeMillis());

			int count = 0;

			for (int i = 1; i <= this.timesToDo; i++) {

				for (int j = 0; j <= this.timesToDoInternal; j++) {

					this.doBegin(i, j);
					try {
						sleep(rnd.nextInt(MAX_TIME_OPEN) + 1);
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
					count ++;

				}

				for (int j = 0; j <= this.timesToDoInternal; j++) {

					this.doEnd(i, this.timesToDoInternal - j);
					try {
						sleep(rnd.nextInt(MAX_TIME_OPEN) + 1);
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}

				}
			}
			System.out.println("ok: " + this.id + " - " + count
					+ " times");
			this.isOk = true;
		}

		public boolean isOk() {
			return this.isOk;
		}
	}

}

