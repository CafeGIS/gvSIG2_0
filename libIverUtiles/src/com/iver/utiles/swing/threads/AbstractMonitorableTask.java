/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
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
package com.iver.utiles.swing.threads;


/**
 * @author fjp
 * 
 * Basada en el trabajo de A. Zabala.
 * Para usarla, extender de AbstractMonitorableTask e implementar el método run().
 * Ejemplo:
 * 
La tarea se lanza con:

PluginServices.cancelableBackgroundExecution(new WriterTask(layer, writer)); <br>

private class WriterTask extends AbstractMonitorableTask <br>
	{ <br>
		FLyrVect lyrVect; <br>
		IWriter writer; <br>
		int rowCount; <br>
		public WriterTask(FLyrVect lyr, IWriter writer) throws DriverException, DriverIOException <br>
		{ <br>
			this.lyrVect = lyr; <br>
			this.writer = writer; <br>
			
			setInitialStep(0); <br>
			setDeterminatedProcess(true); <br>
			setStatusMessage(PluginServices.getText(this, "exportando_features")); <br>
 
			setFinalStep(rowCount); // Importante: Fijarlo en el constructor, para que esté establecido antes del run() <br>
			
		} <br>
		public void run() throws Exception { <br>
 
			// Creamos la tabla. <br>
			writer.preProcess(); <br>
 
			rowCount = va.getShapeCount(); <br>
			for (int i = 0; i < rowCount; i++) { <br>
				IGeometry geom = va.getShape(i); <br>
 
				// DO PROGRESS STUFF <br>
				reportStep(); <br>
				setNote(PluginServices.getText(this, "exporting_") + i); <br>
				if (isCanceled()) <br>
					break; <br>
					
				// END DO PROGRESS STUFF <br>
 
				if (geom != null) { <br>
					Value[] values = sds.getRow(i); <br>
					IFeature feat = new DefaultFeature(geom, values, "" + i); <br>
					DefaultRowEdited edRow = new DefaultRowEdited(feat, <br>
							DefaultRowEdited.STATUS_ADDED, i); <br>
					writer.process(edRow); <br>
				} <br>
			} <br>
			writer.postProcess(); <br>
						
			JOptionPane.showMessageDialog( <br>
					(JComponent) PluginServices.getMDIManager().getActiveView() <br>
					, PluginServices.getText(this, "capa_exportada"), "Export", <br>
					JOptionPane.INFORMATION_MESSAGE); <br>
 
		} <br>
		
	}  <br>
 */
public abstract class AbstractMonitorableTask extends DefaultCancellableMonitorable implements IMonitorableTask {

	String statusMessage;

	String note;

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public int getFinishStep() {
		return getFinalStep();
	}

	public boolean isDefined() {
		return isDeterminatedProcess();
	}


	public boolean isFinished() {
		return (getCurrentStep() >= getFinalStep());
	}

	public void cancel() {
		setCanceled(true);
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see com.iver.utiles.swing.threads.IMonitorableTask#finished()
	 */
	public void finished() {
	
		
	}


}
