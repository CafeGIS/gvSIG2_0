/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
 *
 * For more information, contact:
 *
 *  Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ibáñez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   IVER T.I. S.A
 *   Salamanca 50
 *   46005 Valencia
 *   Spain
 *
 *   +34 963163400
 *   dac@iver.es
 */

/* CVS MESSAGES:
*
* $Id: IQueue.java 5230 2006-05-16 17:10:27Z jaume $
* $Log$
* Revision 1.2  2006-05-16 17:10:27  jaume
* *** empty log message ***
*
* Revision 1.1  2006/05/12 07:15:45  jaume
* *** empty log message ***
*
* Revision 1.1  2006/05/11 17:18:06  jaume
* Un planificador, monitorizador, de descargas que trabaja en segundo plano
*
*
*/
package org.gvsig.remoteClient.taskplanning;

import java.util.Vector;

/**
 *  <p>
 *  You should write your own concrete Queue implementation to hold the tasks in
 *  a specific kind of queue.<br>
 *  </p>
 *  <p>
 *  Following the contract, you should also write a task planner that does what 
 *  you desire. Task planners are a concrete class of the ITaskPlanner that would 
 *  plannify which is the next, the previous,.. task to be done. However, if
 *  you don't want to write anything special, just guess a simple FIFO queue<br>
 *  </p>
 *  
 *  @author jaume dominguez faus - jaume.dominguez@iver.es
 *  @see ITaskPlanner
 */
public interface IQueue {
	
	/**
	 * Adds a new task to the queue. The place where the new task will be put its
	 * left to the concrete implementation of this interface.
	 * @param IRunnableTask task
	 */
	IRunnableTask put(IRunnableTask task);
	
	/**
	 * Returns the next task by calling the task planner's nextTask() method.
	 * @return IRunnableTask with the next task to be executed.
	 */
	IRunnableTask take();
	
	/**
	 * Returns true if the Queue has no (more) jobs to do.
	 * @return
	 */
	boolean isEmpty();
	
	/**
	 * Returns the task planner currently defined by this queue.
	 * @return ITaskPlanner
	 */
	ITaskPlanner getTaskPlanner();
	
	/**
	 * Sets the TaskPlanner that will decide which of the tasks in the queue will
	 * be executed next. A null value should represent a FIFO planner. 
	 * @param planner
	 */
	void setTaskPlanner(ITaskPlanner planner);
	
	/**
	 * Causes the execution of this queue to be paused. The task currently in execution
	 * finishes and after it the planner will not issue more tasks until resume() is
	 * invoked.
	 */
	void pause();
	
	/**
	 * Causes the execution of this queue to be resumed. The execution will continue
	 * with the next task issued by the planner. It has no effect if the queue was not
	 * paused yet. 
	 */
	void resume();

	/**
	 * Returns the set of tasks in a Vector (thread-safe).
	 * @return Vector containing the tasks in this queue.
	 */
	Vector getTasks();
}
