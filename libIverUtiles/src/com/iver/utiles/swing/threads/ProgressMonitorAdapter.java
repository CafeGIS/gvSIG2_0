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
* $Id: ProgressMonitorAdapter.java 4856 2006-04-18 15:16:44Z azabala $
* $Log$
* Revision 1.2  2006-04-18 15:16:44  azabala
* añadido comentario de cabecera de la clase
*
* Revision 1.1  2006/03/14 19:23:42  azabala
* *** empty log message ***
*
*
*/
package com.iver.utiles.swing.threads;

import java.awt.Component;

import javax.swing.ProgressMonitor;

/**
 * It is a try to launch tasks in background and to report its evolution
 * with javax.swing.ProgressMonitor.
 * It isnt satisfactory at all (because we cant control
 * when dialog is showed, etc)
 * @author azabala
 *
 */
public class ProgressMonitorAdapter implements IProgressMonitorIF {
	
	private ProgressMonitor progressMonitor;
	
	public ProgressMonitorAdapter(Component parent,
			String statusMessage,
			String note,
			int minimum,
			int maximum,
			int currentValue){
		progressMonitor = new ProgressMonitor(parent,statusMessage,
				note, minimum, maximum);
		progressMonitor.setProgress(currentValue);
		progressMonitor.setMillisToDecideToPopup(0);
		progressMonitor.setMillisToPopup(0);
		
	}
	
	
	public void setInitialStep(int step) {
		progressMonitor.setMinimum(step);

	}

	public void setLastStep(int step) {
		progressMonitor.setMaximum(step);
	}

	public void setCurrentStep(int step) {
		progressMonitor.setProgress(step);
	}

	public int getInitialStep() {
		return progressMonitor.getMinimum();
	}

	public int getLastStep() {
		return progressMonitor.getMaximum();
	}

	//ProgressMonitor recibe el step, no lo devuelve
	public int getCurrentStep() {
		return -1;
	}

	public void setIndeterminated(boolean indeterminated) {
	}

	public boolean isIndeterminated() {
		return false;
	}

	public void setBarStringDrawed(boolean stringDrawed) {
	}

	public void setBarString(String barString) {
	}

	public void setMainTitleLabel(String text) {
	}

	public void setNote(String note) {
		progressMonitor.setNote(note);
	}

	public void cancel() {
	}

	public boolean isCanceled() {
		return progressMonitor.isCanceled();
	}

	public void taskInBackground() {
		// TODO Auto-generated method stub

	}



	public void close() {
		progressMonitor.close();
		
	}


	public void open() {
		progressMonitor.setMillisToPopup(0);
	}

}

