package org.gvsig.gui.beans.incrementabletask;

public class testThread {
	public class miThread1 extends Thread {
		public void run() {
			System.out.println("miThread1 begin");
			for (int i=0; i<=10; i++) {
				System.out.println(i);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println("miThread1 end");
		}
	}

	public testThread() {
		System.out.println("testThread begin");
		miThread1 a = new miThread1();
		a.start();
		while (true) {
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("isAlive(): " + a.isAlive());
			if (!a.isAlive()) break;
		}
		System.out.println("testThread end");
	}

	public static void main(String[] args) {
		System.out.println("main begin");
		new testThread();
		System.out.println("main end");
	}

}
