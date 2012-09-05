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
* $Id: TaskMonitorTimerListener.java 8714 2006-11-13 20:46:37Z azabala $
* $Log$
* Revision 1.4  2006-11-13 20:46:37  azabala
* *** empty log message ***
*
* Revision 1.3  2006/11/06 07:29:59  jaume
* organize imports
*
* Revision 1.2  2006/03/14 19:24:07  azabala
* *** empty log message ***
*
* Revision 1.1  2006/03/07 21:00:29  azabala
* *** empty log message ***
*
*
*/
package com.iver.utiles.swing.threads;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

/**
 * It receives timer events sended by a swing Timer and does:
 * a) Reads task advances from a IMonitorableTask.
 * b) refresh task status info of a ProgressMonitor.
 * @author azabala
 *
 */
public class TaskMonitorTimerListener implements ActionListener {
	private IProgressMonitorIF progressMonitor;
	private IMonitorableTask task;
	private Timer timer;
	
	public TaskMonitorTimerListener(IProgressMonitorIF progressMonitor,
			IMonitorableTask task){
		this.progressMonitor = progressMonitor;
		this.task = task;
	}
	
	public void setTimer(Timer timer){
		this.timer = timer;
	}
	
	public void actionPerformed(ActionEvent arg0) {
		int currentStep = task.getCurrentStep();
		String statusNote = task.getNote();
        progressMonitor.setCurrentStep(currentStep);
        progressMonitor.setNote(statusNote);
        if(progressMonitor.isCanceled()){
        	 progressMonitor.close();
             task.cancel();
             timer.stop();
        }
        if (task.isFinished()) {
           progressMonitor.close();
           timer.stop();
        }
	}

}

