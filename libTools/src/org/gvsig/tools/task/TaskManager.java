package org.gvsig.tools.task;


public interface TaskManager {

	public Queue createQueue();
	
	/**
	 * Register the executor for the current thread.
	 * 
	 * @param executor
	 */
	public void registerExecutor(Executor executor) ;
	
	/**
	 * Get the associated executor to the current thread.
	 * 
	 * If not exist an executor for the current thread return a dumb
	 * executor. 
	 *  
	 * @return the executor for the current thread.
	 */
	public Executor getExecutor(); 	
}
