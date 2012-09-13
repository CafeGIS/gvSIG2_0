/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
*
* Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
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
*/
package org.gvsig.rastertools.histogram;

import org.gvsig.raster.RasterProcess;
import org.gvsig.raster.datastruct.Histogram;
import org.gvsig.raster.datastruct.HistogramException;
import org.gvsig.raster.hierarchy.IHistogramable;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
/**
 * Clase para calcular histogramas. Esta clase implementa IIncrementable para
 * poder ser usado con una ventana de incremento <code>IncrementableTask</code>
 *
 * @version 23/04/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class HistogramProcess extends RasterProcess {
	private IHistogramable iHistogramable = null;
	private Histogram      lastHistogram  = null;

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.rastertools.RasterProcess#init()
	 */
	public void init() {
		iHistogramable = (IHistogramable) getParam("histogramable");
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.rastertools.RasterProcess#process()
	 */
	public void process() throws InterruptedException {
		try {
			// Proceso duro de obtener un histograma. Puede durar bastante tiempo.
			lastHistogram = iHistogramable.getHistogram();
			// Ya tenemos el histograma y lo representamos en la ventana
			if ((externalActions != null) && (lastHistogram != null))
				externalActions.end(lastHistogram);
		} catch (HistogramException e) {
			NotificationManager.addError("Error calculando el histograma.", e);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.RasterProcess#getResult()
	 */
	public Object getResult() {
		return lastHistogram;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.rastertools.RasterProcess#getLog()
	 */
	public String getLog() {
		return PluginServices.getText(this, "calculando_histograma") + "...\n";
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.incrementabletask.IIncrementable#getPercent()
	 */
	public int getPercent() {
		if (iHistogramable != null)
			return iHistogramable.getPercent();
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.incrementabletask.IIncrementable#getTitle()
	 */
	public String getTitle() {
		return PluginServices.getText(this, "calculando_histograma");
	}
}