package org.gvsig.fmap.dal.resource;

import java.util.Iterator;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.resource.exception.DisposeResorceManagerException;
import org.gvsig.tools.observer.Observer;
import org.gvsig.tools.observer.WeakReferencingObservable;

/**
 * This interface is the responsible of shared resources management.
 *
 * Allows getting a resource, iterating over the available resources, and
 * collecting resources to free them as they become unused
 *
 */
public interface ResourceManager extends WeakReferencingObservable {
	public Resource getResource(String name);

	/**
	 * Returns an iterator over the available resources.
	 *
	 * @return
	 * 		iterator over the resources.
	 */
	public Iterator iterator();

	/**
	 * Iterates over the resources and frees them if they are ready to be freed
	 * or try to close them if they are idle.
	 * 
	 * @throws DataException
	 * @see {@link ResourceManager#getTimeToBeIdle()}
	 *      {@link ResourceManager#setTimeToBeIdle(int)}
	 */
	public void collectResources() throws DataException;

	/**
	 * Returns the wait time to consider that a resource is idle in seconds.
	 * Used in collect resouces action. <br>
	 * if is lower than 1 never is idle.
	 *
	 * @return seconds
	 *
	 * @see {@link ResourceManager#collectResources()}
	 * @see {@link ResourceManager#startResourceCollector(long, Observer)}
	 */
	public int getTimeToBeIdle();

	/**
	 * Sets the wait time to consider that a resource is idle. Used in collect
	 * resouces action. <br>
	 * if is lower than 1 never is idle.
	 *
	 * @see {@link ResourceManager#collectResources()}
	 * @see {@link ResourceManager#startResourceCollector(long, Observer)}
	 */

	public void setTimeToBeIdle(int seconds);

	/**
	 * Initializes the resource collection background process. Allows setting
	 * of the delay between each execution of the collector and also an
	 * observer to be notified on each execution.
	 *
	 * @param milis
	 * 			delay between each execution of the resource collection process, in milliseconds.
	 *
	 * @param observer
	 * 			an observer that will be notified on each execution of the resource collection process.
	 */
	public void startResourceCollector(long milis, Observer observer);

	/**
	 * Stops successive executions of the resource collector process. It does not interrupt
	 * the process if it is currently running, but it will not be executed anymore times.
	 */
	public void stopResourceCollector();

	/**
	 * Close all register resources.
	 *
	 * @throws DataException
	 */
	public void closeResources() throws DataException;

	public void dispose() throws DisposeResorceManagerException;

}