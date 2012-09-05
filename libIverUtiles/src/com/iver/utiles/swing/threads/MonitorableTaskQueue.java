/*
 * Created on 10-mar-2006
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
* $Id: MonitorableTaskQueue.java 11634 2007-05-15 07:24:19Z cesar $
* $Log$
* Revision 1.2  2007-05-15 07:21:20  cesar
* Add the finished method for execution from Event Dispatch Thread
*
* Revision 1.1  2006/03/14 19:23:42  azabala
* *** empty log message ***
*
*
*/
package com.iver.utiles.swing.threads;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * A task that could enqueue tasks.
 * @author azabala
 *
 */
public class MonitorableTaskQueue implements IMonitorableTask {

	private ArrayList taskQueue;
	private boolean canceled = false;
	private IMonitorableTask currentTask = null;
	
	public MonitorableTaskQueue(){
		taskQueue = new ArrayList();
	}
	
	public void addTask(IMonitorableTask task){
		taskQueue.add(task);
	}
	
	
	public int getInitialStep() {
		return 0;
	}

	public int getFinishStep() {
		int lastStep = 0;
		if(isDefined()){
			for(int i = 0; i < taskQueue.size(); i++){
				IMonitorableTask task = 
					(IMonitorableTask) taskQueue.get(i);
				lastStep += task.getFinishStep();
			}
		}else{
			lastStep += taskQueue.size();
		}
		return lastStep;
	}

	public int getCurrentStep() {
		int currentStep = 0;
		
			for(int i = 0; i < taskQueue.size(); i++){
				IMonitorableTask task = 
					(IMonitorableTask) taskQueue.get(i);
				
					if(task == currentTask)
					{
						if(isDefined()){
							currentStep += currentTask.getCurrentStep();
							
						}else{
							currentStep = i;
						}	
						break;
					}else{
						if(isDefined())
							currentStep += currentTask.getFinishStep();
					}
		}
		return currentStep++;
	}

	public String getStatusMessage() {
		if(currentTask != null){
			return currentTask.getStatusMessage();
		}else{
			return "Waiting for new tasks...";
		}
	}

	public String getNote() {
		if(currentTask != null){
			return currentTask.getNote();
		}else{
			return "";
		}
	}

	public boolean isDefined() {
		for(int i = 0; i < taskQueue.size(); i++){
			IMonitorableTask task = 
				(IMonitorableTask) taskQueue.get(i);
			if(!task.isDefined())
				return false;
		}
		return true;
	}

	public void cancel() {
		if(currentTask != null){
			currentTask.cancel();
		}
		canceled = true;

	}

	public void run() throws Exception {
		System.out.println("lanzando procesos encolados...");
		Iterator taskIterator = taskQueue.iterator();
		while (taskIterator.hasNext() && (! canceled)){
			currentTask = (IMonitorableTask) taskIterator.next();
			System.out.println("proceso " + currentTask.toString());
			currentTask.run();
		}
		System.out.println("Se finalizo la cola de procesos");
		

	}

	public boolean isCanceled() {
		if(currentTask != null){
			return currentTask.isCanceled();
		}
		return false;
	}

	public boolean isFinished() {
		if(currentTask != null){
			return currentTask.isFinished();
		}
		return false;
	}

	public void finished() {
	}
}

