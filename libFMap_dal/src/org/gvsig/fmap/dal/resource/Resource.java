package org.gvsig.fmap.dal.resource;

import java.util.Date;

import org.gvsig.fmap.dal.resource.exception.AccessResourceException;
import org.gvsig.fmap.dal.resource.exception.ResourceBeginException;
import org.gvsig.fmap.dal.resource.exception.ResourceException;
import org.gvsig.fmap.dal.resource.spi.ResourceConsumer;

/**
 * Encapsulates a system resource (file, database connection, etc). 
 * It is used to manage usage and availability of shared system 
 * resources. This interface allows monitoring a resource and helps 
 * preventing dead locks on it as well as being freed as soon as
 * it is not being used.
 * 
 * Data providers can provide implementations for their own resources. This
 * is specially interesting when resources require a specific treatment
 * beyond the standard shared file or connection, for instance to manage 
 * connections to a server through its own connection pool.
 */
public interface Resource {

	/**
	 * Returns the resource's name.
	 * 
	 * @return resource's name
	 * 
	 * @throws AccessResourceException
	 */
	public String getName() throws AccessResourceException;

	/**
	 * Returns the resource parameters. These parameters contain
	 * all the necessary information to access the resource.
	 * 
	 * @return resource parameters.
	 */
	public ResourceParameters getParameters();

	/**
	 * Returns the date and time in which this resource was 
	 * opened for the last time.
	 * 
	 * @return 
	 *      date and time in which this resource was opened for the last time.
	 */
	public Date getLastTimeOpen();

	/**
	 * Returns the date and time in which this resource was 
	 * accessed for the last time.
	 * 
	 * @return 
	 *      date and time in which this resource was accessed for the last time.
	 */
	public Date getLastTimeUsed();

	/**
	 * Returns whether this resource is already in use by someone.
	 * 
	 * @return
	 * 		true if this resource is in use, false if not.
	 */
	public boolean inUse();

	/**
	 * Returns whether this resource is opened.
	 * 
	 * @return
	 * 		true if this resource is opened, false if not.
	 */
	public boolean isOpen();

	/**
	 * Returns the number of times this resource has been opened 
	 * since it was created.
	 * 
	 * @return 
	 * 		number of times this resource has been opened 
	 * since it was created.
	 */
	public int openCount();

	/**
	 * Initiates a mutual exclusion block over this resource. It should be used 
	 * whenever a resource is going to be changed in any way, to avoid concurrent 
	 * changes and unexpected behavior.
	 * 
	 * @throws ResourceBeginException
	 */
	public void begin() throws ResourceBeginException;

	/**
	 * Ends a mutual exclusion block. It <b>must</b> be used always after a begin()
	 * call to prevent a dead lock.
	 */
	public void end();

	/**
	 * If the resource is not in use, calling this method will send a close request 
	 * to all consumers referencing this resource. If the resource is in use, 
	 * calling this method will do nothing.
	 * 
	 * @throws ResourceException
	 */
	public void closeRequest() throws ResourceException;

	/**
	 * Adds a consumer to this resource. This will create a weak reference 
	 * to the consumer in this resource's consumer list.
	 * 
	 * @param consumer
	 * 				the consumer that will be added to this resource's consumer list.
	 */
	public void addConsumer(ResourceConsumer consumer);

	/**
	 * Removes a consumer from this resource's consumer list.
	 * 
	 * @param consumer
	 * 				the consumer that will be removed.
	 */
	public void removeConsumer(ResourceConsumer consumer);

	/**
	 * Returns this resource's current number of consumers.
	 * 
	 * @return
	 * 		current number of consumers of this resource.
	 */
	public int getConsumersCount();
	
	/**
	 * Returns an object that represents the resource. The actual type
	 * depends on the resource provider. It could be a string with a 
	 * descriptive name or something more elaborated.
	 * 
	 * @return
	 * 		an object that represents the resource.
	 * 
	 * @throws AccessResourceException
	 */
	public Object get() throws AccessResourceException;

	/**
	 * Returns a custom object containing extended data relative to 
	 * this resource.
	 * 
	 * This is part of a simple mechanism to allow passing further data to 
	 * the resource that may be necessary for optimal treatment.
	 * 
	 * @return
	 * 		data related to this resource
	 */
	public Object getData();

	/**
	 * Sets a custom object as this resource's extended data.
     *
	 * This is part of a simple mechanism to allow passing further data to 
	 * the resource that may be necessary for optimal treatment.
	 *
	 * @param data
	 * 			a custom object containing data related to this resource.
	 */
	public void setData(Object data);

}