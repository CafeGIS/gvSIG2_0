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
* $Id: Monitorable.java 5317 2006-05-22 10:36:54Z fjp $
* $Log$
* Revision 1.1  2006-05-22 10:31:55  fjp
* Monitorable tasks easy
*
* Revision 1.3  2006/05/15 14:56:23  azabala
* Añadida la posibilidad de modificar el paso actual (para que en tareas que constan de varios pasos, para cada paso se pueda mostrar una barra llenandose)
*
* Revision 1.2  2006/03/09 18:43:29  azabala
* *** empty log message ***
*
* Revision 1.1  2006/03/09 18:26:19  azabala
* *** empty log message ***
*
*
*/
package com.iver.utiles.swing.threads;
/**
 * Interface to monitorize a long process, which operates
 * in many individual steps
 * @author azabala
 *
 */
public interface Monitorable {
	/**
	 * Report to monitor that an individual step
	 * was processed.
	 *
	 */
	public void reportStep();
	/**
	 * Return the number of steps processed
	 * @return
	 */
	public int getCurrentStep();
	/**
	 * Allows to modify current step.
	 *
	 */
	public void setCurrentStep(int currentStep);
	
	/**
	 * report monitor what number has the initial
	 * step of the process is monitoring
	 * @param step
	 */
	public void setInitialStep(int step);
	
	public int getInitialStep();
	
	public int getFinalStep();
	/**
	 * report monitor what number has the final
	 * step of the process is monitoring
	 * (no apply to undefined process)
	 * @param step
	 */
	public void setFinalStep(int step);
	/**
	 * Reports to the monitor that the process  is
	 * monitoring is (or not) a defined process. A defined
	 * process is a process whose number of step is predefined
	 * (in front of not defined process, which may run in
	 * an unknown number of steps)
	 * @param defined
	 */
	public void setDeterminatedProcess(boolean defined);
	/**
	 * Tells if the process is monitoring is defined
	 * @return
	 */
	public boolean isDeterminatedProcess();
	/**
	 * Sets initial default monitorization values
	 */
	public void reset();
	
}

