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
* $Id: ITaskPlanner.java 5157 2006-05-12 07:45:49Z jaume $
* $Log$
* Revision 1.2  2006-05-12 07:45:49  jaume
* some warnings removed
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

/**
 * <p>
 * ITaskPlanner provides an interface to program your own task planning. It gives
 * you operations for pick a task from the queue according on the criteria that
 * you designed.<br>
 * </p>
 * <p>
 * The simplest implementation of ITaskPlanner would be a FIFO task planner (see
 * FIFOTaskPlanner.java) which takes jobs from a task queue in the same order
 * they were put. But any kind of planner is possible (SJF, LIFO, RoundRobin, etc.). 
 * </p>
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 *
 */
public interface ITaskPlanner {
	/**
	 * Takes the next task to be executed.
	 * @return IRunnableTask representing the next task to be executed
	 */
	public IRunnableTask nextTask();
	
	/**
	 * Takes the previous executed task. Notice that it may or may not have
	 * sense for specific implementations. For example, in a FIFO-like planner,
	 * the task is taken from the queue, executed until it is finished and 
	 * removed from the queue. So, there is no previous task.
	 * 
	 * @return IRunnableTask representing the previous executed task, or null
	 * if none.
	 * @deprecated (probably this is unuseful and i'll remove it)
	 */
	public IRunnableTask previousTask();
}
