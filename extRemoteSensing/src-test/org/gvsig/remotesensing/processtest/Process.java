package org.gvsig.remotesensing.processtest;

import org.gvsig.gui.beans.incrementabletask.IIncrementable;
import org.gvsig.gui.beans.incrementabletask.IncrementableEvent;
import org.gvsig.gui.beans.incrementabletask.IncrementableListener;
import org.gvsig.gui.beans.incrementabletask.IncrementableTask;

public class Process implements Runnable, IIncrementable, IncrementableListener {
	
	private IncrementableTask		incrementableTask = null;
	private boolean							cancel = false;
	private volatile Thread			blinker = null;
	int i = 0;

	public void run() {
		for (i = 0; i<10000000; i++){
			if(cancel)
				return;
			for (int j=0;j<100;j++);
		}
				
		if (incrementableTask != null)
			incrementableTask.processFinalize();
		System.exit(0);
	}

	public void actionCanceled(IncrementableEvent e) {
		cancel = true;
		
	}

	public void actionResumed(IncrementableEvent e) {
		
	}

	public void actionSuspended(IncrementableEvent e) {
	}

	/**
	 * Arranca el proceso r
	 */
	public void start() {
		cancel = false;
		blinker = new Thread(this);
		blinker.start();
	}

	public void setIncrementableTask(IncrementableTask incrementableTask) {
		this.incrementableTask = incrementableTask;
	}

	public String getLabel() {
		return "probando, probando";
	}

	public String getLog() {
		return null;
	}

	public int getPercent() {
		return i/100000;
	}

	public String getTitle() {
		return "Rulando";
	}

	public boolean isCancelable() {
		return true;
	}

	public boolean isPausable() {
		return false;
	}
}
