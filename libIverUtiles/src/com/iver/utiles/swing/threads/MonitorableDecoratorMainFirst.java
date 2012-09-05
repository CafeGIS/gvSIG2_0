/*
 * Created on 13-mar-2006
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
* $Id: MonitorableDecoratorMainFirst.java 11634 2007-05-15 07:24:19Z cesar $
* $Log$
* Revision 1.2  2007-05-15 07:21:20  cesar
* Add the finished method for execution from Event Dispatch Thread
*
* Revision 1.1  2006/05/08 15:52:30  azabala
* *** empty log message ***
*
* Revision 1.2  2006/04/18 15:17:20  azabala
* añadidos comentarios a metodos
*
* Revision 1.1  2006/03/14 19:23:42  azabala
* *** empty log message ***
*
*
*/
package com.iver.utiles.swing.threads;
/**
 * Task that wraps a main task, executing it and doing some preprocess
 * stuff after main task, by executing a secondary task
 * @author azabala
 *
 */
public class MonitorableDecoratorMainFirst implements IMonitorableTask {

	/**
	 * main task
	 */
	private IMonitorableTask mainTask;
	/**
	 * preprocess task 
	 */
	private IMonitorableTask secondaryTask;
	/**
	 * task that is currently in execution
	 */
	private IMonitorableTask currentTask;
	/**
	 * flag for cancelations
	 */
	private boolean canceled = false;
	/**
	 * flag for finalization
	 */
	private boolean finished = false;
	
	/**
	 * Constructor
	 * @param mainTask
	 * @param secondaryTask
	 */
	public MonitorableDecoratorMainFirst(IMonitorableTask mainTask, IMonitorableTask secondaryTask){
		this.mainTask = mainTask;
		this.secondaryTask = secondaryTask;
		this.currentTask = mainTask;
	}
	
	/**
	 * Makes some preprocess steps, and return a boolean flag that
	 * indicates if this task can be launched.
	 * @return
	 */
	public boolean preprocess(){
		if(mainTask != null  && secondaryTask != null)
			return true;
		else 
			return false;
	}
	
	
	public int getInitialStep() {
		return mainTask.getInitialStep();
	}

	public int getFinishStep() {
		//we add 1 because secondaryTask is consideered as
		//a step
		return mainTask.getFinishStep() + 1;
	}

	public int getCurrentStep() {
		if(currentTask == mainTask){
			return mainTask.getCurrentStep();
		}else{
			return getFinishStep();
		}
	}

	public String getStatusMessage() {
		return mainTask.getStatusMessage();
	}

	public String getNote() {
		return mainTask.getNote();
	}

	public boolean isDefined() {
		return mainTask.isDefined();
	}

	public void cancel() {
		canceled = true;
		currentTask.cancel();

	}

	public void run() throws Exception {
		currentTask = mainTask;
		if(! canceled)
			mainTask.run();
		if(! canceled){
			currentTask = secondaryTask;
			secondaryTask.run();
		}
		finished  = true;
		

	}

	public boolean isCanceled() {
		return canceled == true;
	}

	public boolean isFinished() {
		if(currentTask == mainTask){
			return currentTask.isFinished();
		}else
			return finished == true;
	}
	
	public void finished() {
		
	}

}

