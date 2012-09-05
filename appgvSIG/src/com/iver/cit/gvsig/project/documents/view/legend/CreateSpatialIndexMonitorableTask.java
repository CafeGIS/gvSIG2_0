/*
 * Created on 03-may-2006
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
* $Id: CreateSpatialIndexMonitorableTask.java 25089 2008-11-13 17:33:16Z jmvivo $
* $Log$
* Revision 1.4  2007-07-30 12:56:04  jaume
* organize imports, java 5 code downgraded to 1.4 and added PictureFillSymbol
*
* Revision 1.3  2007/05/15 07:22:56  cesar
* Add the finished method for execution from Event Dispatch Thread
*
* Revision 1.2  2007/03/06 16:37:08  caballero
* Exceptions
*
* Revision 1.1  2006/09/15 10:41:30  caballero
* extensibilidad de documentos
*
* Revision 1.2  2006/05/22 10:35:41  fjp
* Monitorable tasks easy
*
* Revision 1.1  2006/05/08 15:41:06  azabala
* added rtree spatial indexing capabilities
*
*
*/
package com.iver.cit.gvsig.project.documents.view.legend;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;

import com.iver.andami.PluginServices;
import com.iver.utiles.swing.threads.CancellableMonitorable;
import com.iver.utiles.swing.threads.DefaultCancellableMonitorable;
import com.iver.utiles.swing.threads.IMonitorableTask;

public class CreateSpatialIndexMonitorableTask implements IMonitorableTask{

	String MAIN_MESSAGE;
	String HULL_MESSAGE = PluginServices.getText(this, "Indexando");
	String OF = "de";

	FLyrVect layer;
	/**
	 * It monitors advance of indexing process
	 */
	private CancellableMonitorable cancelMonitor = null;
	/**
	 * flag to tell if the process is finished
	 */
	private boolean finished = false;

	/**
	 * Default constructor
	 * @throws DriverIOException
	 * @throws DriverException
	 */
	public CreateSpatialIndexMonitorableTask(FLyrVect layer)
			throws DataException {
		this.layer = layer;
		MAIN_MESSAGE = PluginServices.getText(this, "Indexando_espacialmente") +
								layer.getName();
//		cancelMonitor = createCancelMonitor();
	}

	/**
	 * Creates a CancellableMonitorable instance to monitor
	 * progress and to cancel the process.
	 * @return
	 * @throws DriverIOException
	 * @throws DriverException
	 */
//	private CancellableMonitorable createCancelMonitor()
//				throws ReadException {
//		DefaultCancellableMonitorable monitor =
//			new DefaultCancellableMonitorable();
//		monitor.setInitialStep(0);
//		monitor.setDeterminatedProcess(true);
//		int numSteps = ((FLyrVect)layer).getFeatureStore().getDataCollection().size();
//		monitor.setFinalStep(numSteps);
//		return monitor;
//	}

	public void run() throws Exception {
		FeatureStore fs=(layer).getFeatureStore();
		//TODO comentado para que compile
//		fs.createIndex(fs.getDefaultFeatureType());
		finished = true;
	}

	public int getInitialStep() {
		return cancelMonitor.getInitialStep();
	}

	public int getFinishStep() {
		return cancelMonitor.getFinalStep();
	}

	public int getCurrentStep() {
		return cancelMonitor.getCurrentStep();
	}

	public String getStatusMessage() {
		return MAIN_MESSAGE;
	}

	public String getNote() {
		return HULL_MESSAGE + " " +
		getCurrentStep()+ " " +
		OF  + " "+
		getFinishStep();
	}
	//FIXME Borrar los ficheros de indice en caso de cancelacion
	public void cancel() {
		((DefaultCancellableMonitorable) cancelMonitor).setCanceled(true);
	}
	/**
	 * Tells if it is a defined process (we know its number of steps)
	 */
	public boolean isDefined() {
		return true;
	}
	public boolean isCanceled() {
		return cancelMonitor.isCanceled();
	}

	public boolean isFinished() {
		return finished;
	}

	/* (non-Javadoc)
	 * @see com.iver.utiles.swing.threads.IMonitorableTask#finished()
	 */
	public void finished() {
		// TODO Auto-generated method stub

	}
}//CreateSpatialIndexMonitorableTask


