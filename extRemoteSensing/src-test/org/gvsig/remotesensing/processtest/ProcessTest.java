package org.gvsig.remotesensing.processtest;

import org.gvsig.gui.beans.incrementabletask.IncrementableTask;

public class ProcessTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Process process = new Process();
		IncrementableTask incrementableTask = new IncrementableTask(process);
		process.setIncrementableTask(incrementableTask);
		incrementableTask.showWindow();
	    process.start();
		incrementableTask.start();	

	}

}
