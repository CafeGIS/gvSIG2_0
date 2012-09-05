/*
 * Created on 09-mar-2006
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
* $Id: DefaultCancellableMonitorable.java 5317 2006-05-22 10:36:54Z fjp $
* $Log$
* Revision 1.1  2006-05-22 10:31:55  fjp
* Monitorable tasks easy
*
* Revision 1.4  2006/05/15 14:56:23  azabala
* Añadida la posibilidad de modificar el paso actual (para que en tareas que constan de varios pasos, para cada paso se pueda mostrar una barra llenandose)
*
* Revision 1.3  2006/03/14 19:29:15  azabala
* *** empty log message ***
*
* Revision 1.2  2006/03/09 18:43:29  azabala
* *** empty log message ***
*
* Revision 1.1  2006/03/09 18:41:32  azabala
* *** empty log message ***
*
*
*/
package com.iver.utiles.swing.threads;
/**
 * Default very easy implementation of 
 * CancellableMonitorable
 * @author azabala
 *
 */
public class DefaultCancellableMonitorable implements CancellableMonitorable {
	
	private boolean canceled = false;
	private int currentStep = 0;
	private int initialStep = 0;
	private int lastStep = 0;
	private boolean determinated = false;
	
	
	
	
	public boolean isCanceled() {
		return canceled;
	}

	public void reportStep() {
		currentStep++;
	}

	public int getCurrentStep() {
		return currentStep;
	}

	public void setInitialStep(int step) {
		initialStep = step;
	}

	public void setFinalStep(int step) {
		lastStep = step;
	}

	public void setDeterminatedProcess(boolean determinated) {
		this.determinated = determinated;
	}

	public boolean isDeterminatedProcess() {
		return determinated;
	}

	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}

	public void reset() {
		canceled = false;
		currentStep = 0;
		initialStep = 0;
		lastStep = 0;
		determinated = false;
	}

	public int getInitialStep() {
		return initialStep;
	}

	public int getFinalStep() {
		return lastStep;
	}

	public void setCurrentStep(int currentStep) {
		this.currentStep = currentStep;
	}

}

