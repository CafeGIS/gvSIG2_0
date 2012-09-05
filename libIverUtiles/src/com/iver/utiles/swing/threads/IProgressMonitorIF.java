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
* $Id: IProgressMonitorIF.java 4483 2006-03-20 16:04:21Z azabala $
* $Log$
* Revision 1.2  2006-03-20 16:04:11  azabala
* *** empty log message ***
*
* Revision 1.1  2006/03/14 19:23:42  azabala
* *** empty log message ***
*
*
*/
package com.iver.utiles.swing.threads;
/**
 * IProgressMonitorIF must be all classes which
 * monitors the process of a long costly task.
 * <br>
 * It is designed to monitor defined tasks (those
 * which we know what number of steps have) and 
 * undefined tasks.
 * <br>
 * It must has associated a GUI component to show
 * progress of the task (usually a Progress Bar and
 * a cancel button).
 * 
 * @author azabala
 *
 */
public interface IProgressMonitorIF {
	/**
	 * sets initial step number of the task
	 * monitored
	 * @param step
	 */
	public void setInitialStep(int step);
	/**
	 * sets final step number of the task
	 * monitored
	 * @param step
	 */
	public void setLastStep(int step);
	/**
	 * sets current step number of the task
	 * monitored
	 * @param step
	 */
	public void setCurrentStep(int step);
	public int getInitialStep();
	public int getLastStep();
	public int getCurrentStep();
	/**
	 * Sets if the monitored task is defined (known
	 * number of steps) or undefined (unknown)
	 * @param indeterminated
	 */
	public void setIndeterminated(boolean indeterminated);
	public boolean isIndeterminated();
	/**
	 * Sets if the progress bar associated to monitor
	 * must draw a complementary text to progress bar
	 * @param stringDrawed
	 */
	public void setBarStringDrawed(boolean stringDrawed);
	/**
	 * Sets complementary text to progress bar.
	 * @param barString
	 */
	public void setBarString(String barString);
	/**
	 * Set main text of GUI component
	 * @param text
	 */
	public void setMainTitleLabel(String text);
	public void setNote(String note);
	/**
	 * Sends cancel message to monitored task.
	 *
	 */
	public void cancel();
	/**
	 * Returns if has received cancel message
	 * @return
	 */
	public boolean isCanceled();
	/**
	 * Says if associated task is running in background
	 *
	 */
//	public void taskInBackground();
	/**
	 * Closes associated GUI component
	 *
	 */
	public void close();
	/**
	 * Sets associated gui component visible
	 *
	 */
	public void open();
	
}

