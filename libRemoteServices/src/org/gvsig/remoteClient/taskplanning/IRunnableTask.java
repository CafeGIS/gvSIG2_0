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
* $Id: IRunnableTask.java 20715 2008-05-14 15:10:56Z vcaballero $
* $Log$
* Revision 1.1  2006-05-12 07:15:45  jaume
* *** empty log message ***
*
* Revision 1.1  2006/05/11 17:18:06  jaume
* Un planificador, monitorizador, de descargas que trabaja en segundo plano
*
*
*/
package org.gvsig.remoteClient.taskplanning;

/**
 * Interface implemented by those tasks that can be background-executed,
 * cancelled or any other thing.
 * 
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 */
public interface IRunnableTask {
	/**
	 * Executes this task's operations.
	 */
	public void execute();
	
	/**
	 * Cancels the current execution, if any, of this task. Should have no
	 * effect if the task is not executing anything.
	 */
	public void cancel();
	
	/**
	 * Tells if the task is on execution.
	 * @return true if the task is busy, false otherwise.
	 */
	public boolean isRunning();
	
	/**
	 * Returns the timeout set to this task in milliseconds
	 * @return the amount of milliseconds to wait until the task
	 * 		   will be considered as unsuccessful, or 0 or less to
	 * 		   say that task can wait forever.
	 */
	public long getTaskTimeout();
}
