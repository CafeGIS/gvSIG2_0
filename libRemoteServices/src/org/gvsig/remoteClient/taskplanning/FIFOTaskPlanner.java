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
* $Id: FIFOTaskPlanner.java 5156 2006-05-12 07:15:45Z jaume $
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
 * A simple FIFO task planner. The tasks returned by this planner are executed
 * enterely. It does not issue another task until the current is finished. 
 * @author jaume
 *
 */
public class FIFOTaskPlanner implements ITaskPlanner {
	IQueue queue;
	
	/**
	 * Creates a new instance of FIFOTaskPlanner that will work against the
	 * queue passed as paramenter 
	 * @param queue, the IQueue to be planned
	 */
	public FIFOTaskPlanner(IQueue queue) {
		this.queue = queue;
	}

	
	public IRunnableTask nextTask() {
		synchronized (this) {
			return (IRunnableTask) queue.getTasks().remove(0);
		}
	}
	/**
	 * FIFO plans have no previous tasks so, null is always returned.
	 */
	public IRunnableTask previousTask() {
		return null;
	}

}
