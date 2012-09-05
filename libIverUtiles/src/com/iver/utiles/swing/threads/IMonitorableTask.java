/*
 * Created on 06-mar-2006
 *
 * gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
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
* $Id: IMonitorableTask.java 12024 2007-06-06 14:02:39Z cesar $
* $Log$
* Revision 1.3  2007-06-06 14:02:39  cesar
* Include the finished method in the abstract class. Add comments in the interface.
*
* Revision 1.2  2007/05/15 07:21:20  cesar
* Add the finished method for execution from Event Dispatch Thread
*
* Revision 1.1  2006/03/14 19:23:42  azabala
* *** empty log message ***
*
* Revision 1.2  2006/03/09 18:57:39  azabala
* *** empty log message ***
*
* Revision 1.1  2006/03/07 21:00:29  azabala
* *** empty log message ***
*
*
*/
package com.iver.utiles.swing.threads;

/**
 * Long time task that could be monitored. The monitorization
 * information that this task could offer is:<br>
 * <ul>
 * <li>Defined (number of steps known) or Undefined task</li>
 * <li>Number of steps (for defined tasks)</li>
 * <li>Initial and final step</li>
 * <li>Description of the task (status)</li>
 * <li>Description of subtasks (note)</li>
 * </ul>
 * @author azabala
 *
 */
public interface IMonitorableTask extends ICancelableTask, 
									ICancelMonitor {
	public int getInitialStep();
	public int getFinishStep();
	public int getCurrentStep();
	public String getStatusMessage();
	public String getNote();
	public boolean isDefined();
	/**
	 * This method is executed <b>from the Event Dispatch Thread</b>
	 * when the background task finishes. Any interaction with SWing
	 * objects should be placed here.
	 */
	public void finished();
	
}

