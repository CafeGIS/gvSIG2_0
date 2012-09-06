package org.gvsig.tools.task.impl;

import java.util.*;

import org.gvsig.tools.task.Executor;
import org.gvsig.tools.task.Queue;
import org.gvsig.tools.task.TaskManager;

public class DefaultTaskManager implements TaskManager {




	class ExecutorAndThread {
		Thread thread;
		Executor executor;

		ExecutorAndThread(Thread thread, Executor executor) {
			this.thread = thread;
			this.executor = executor;
		}
	}
	class DumbExecutor implements Executor {
		public void execute(Runnable command) {
			command.run();
		}
	}

	private Map executorsByThread;
	private Executor dumbExecutor;
	private Timer timer;

	public DefaultTaskManager() {
		this.executorsByThread = new HashMap();
		this.dumbExecutor = new DumbExecutor();
		startDeadsExecutorsCollector(60000); // 1 minuto.
	}

	/**
	 * Register the executor for the current thread.
	 *
	 * @param executor
	 */
	public void registerExecutor(Executor executor) {
		synchronized (executorsByThread) {
			ExecutorAndThread x = new ExecutorAndThread(Thread.currentThread(), executor);
            // Object key = new Long(x.thread.getId());
            // this.executorsByThread.put(key, x);
            this.executorsByThread.put(x.thread, x);
		}
	}

	/**
	 * Get the associated executor to the current thread.
	 *
	 * If not exist an executor for the current thread return a dumb
	 * executor.
	 *
	 * @return the executor for the current thread.
	 */
	public Executor getExecutor() {
		synchronized (executorsByThread) {
            // Object key = new Long(Thread.currentThread().getId());
            // ExecutorAndThread x = (ExecutorAndThread)
            // this.executorsByThread.get(key);
            ExecutorAndThread x = (ExecutorAndThread) this.executorsByThread
                    .get(Thread.currentThread());
			if( x != null ) {
				if( x.thread.isAlive() ) {
					return x.executor;
				}
				this.executorsByThread.remove(x);
			}
			return this.dumbExecutor;
		}
	}

	private void startDeadsExecutorsCollector(long milis) {
		if (this.timer == null){
			this.timer = new Timer();
		}
		this.timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				try {
					collectDeadExecutors();
				} catch (Exception e) {
//					DefaultTaskManager.logger.warn("Exception collecting dead executors.", e);
				}
			}

		}, milis, milis);
	}

	/*
	private void stopDeadsExecutorsCollector() {
		if (this.timer != null) {
			this.timer.cancel();
		}

	}
	*/

	private void collectDeadExecutors() {
		synchronized (executorsByThread) {
			Iterator it = this.executorsByThread.values().iterator();
			while( it.hasNext() ) {
				ExecutorAndThread x = (ExecutorAndThread) it.next();
				if( ! x.thread.isAlive() ) {
					it.remove();
				}
			}
		}
	}

	public Queue createQueue() {
		return new DefaultQueue();
	}


}
