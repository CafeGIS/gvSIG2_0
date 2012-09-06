/*
 * Created on 01-oct-2005
 */
package org.gvsig.remoteClient.taskplanning.retrieving;

import java.util.Date;
import java.util.Vector;

import org.gvsig.remoteClient.taskplanning.FIFOTaskPlanner;
import org.gvsig.remoteClient.taskplanning.IQueue;
import org.gvsig.remoteClient.taskplanning.IRunnableTask;
import org.gvsig.remoteClient.taskplanning.ITaskPlanner;

/**
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 * 		   Luis W. Sevilla (sevilla_lui@gva.es)
 */
public class RetrieveQueue implements IQueue {
	private String hostName;
	private Date startTime;
	private Vector tasks = new Vector();
	private boolean waiting;
	private ITaskPlanner taskPlanner;
	private Worker worker;
	
	/**
	 * 
	 */
	public RetrieveQueue(String hName) {
		hostName = hName;
		startTime = new Date();
		worker = new Worker();
		new Thread(worker).start();
	}
	
	
	public IRunnableTask put(IRunnableTask task) {
		tasks.add(task);
		if (waiting) {
			synchronized (this) {
				notifyAll();
			}
		}
		return task;
	}
	
	public IRunnableTask take() {
		if (tasks.isEmpty()) {
			synchronized (this) {
				waiting = true;
				try {
					wait();
				} catch (InterruptedException ie) {
					waiting = false;
				}
			}
		}
		return getTaskPlanner().nextTask() ;
	}
	
	
	
	public boolean isEmpty() {
		synchronized (this) {
			return tasks.isEmpty() && !worker.r.isRunning();
		}
	}
	
	

	public ITaskPlanner getTaskPlanner() {
		if (taskPlanner == null) {
			taskPlanner = new FIFOTaskPlanner(this);
		}
		return taskPlanner;
	}


	public void setTaskPlanner(ITaskPlanner planner) {
		taskPlanner = planner;
	}


	public void pause() {
		waiting = true;
	}


	public void resume() {
		waiting = false;
	}


	public Vector getTasks() {
		return tasks;
	}

	private class Worker implements Runnable {
		URLRetrieveTask r;
		int i = 0; 
		public void run() {
			while (true) {
				r = (URLRetrieveTask) take();
				r.execute();
			}
		}
	}

	protected URLRetrieveTask getURLPreviousRequest(URLRequest request) {
		// Is the one currently running?
		/*URLRetrieveTask aux = (URLRetrieveTask) worker.r;
		if (request.equals(aux.getRequest())) {
				return aux;
		}*/	
		// Is one of those in the queue?
		for (int i = 0; i < tasks.size(); i++) {
			URLRetrieveTask task = (URLRetrieveTask) tasks.get(i);
			URLRequest aWorkingRequest = task.getRequest();
			if (aWorkingRequest.equals(request)) {
				return task;
			}
		}
		return null;
	}
}
