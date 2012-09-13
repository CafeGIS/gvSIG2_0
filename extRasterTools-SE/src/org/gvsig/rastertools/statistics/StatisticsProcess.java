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
package org.gvsig.rastertools.statistics;

import javax.swing.SwingUtilities;

import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.raster.IProcessActions;
import org.gvsig.raster.RasterProcess;
import org.gvsig.raster.dataset.FileNotOpenException;
import org.gvsig.raster.dataset.RasterDriverException;
import org.gvsig.raster.dataset.properties.DatasetListStatistics;
import org.gvsig.raster.util.RasterToolsUtil;

import com.iver.andami.PluginServices;

/**
 * Proceso para la generación de estadísticas.
 *
 * 10/12/2007
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class StatisticsProcess extends RasterProcess {
	private FLyrRasterSE          lyr   = null;
	private DatasetListStatistics stats = null;
	private boolean               force = false;

	/**
	 * Lanzador del procesos de estadísticas
	 * @param lyr Capa a calcular las estadísticas
	 * @param actions Clase que recoge eventos del proceso
	 */
	public static void launcher(FLyrRasterSE lyr, IProcessActions actions) {
		RasterProcess process = new StatisticsProcess();
		process.addParam("layer", lyr);
		process.addParam("force", new Boolean(false));
		process.setActions(actions);
		process.start();	
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.rastertools.RasterProcess#init()
	 */
	public void init() {
		lyr = getLayerParam("layer");
		force = getBooleanParam("force");
		stats = lyr.getDataSource().getStatistics();
	}

	/**
	 * Método donde se ejecutará el Thread, aquí se calcularán las 
	 * estadísticas.
	 */
	public void process() throws InterruptedException {
		insertLineLog(PluginServices.getText(this, "statistics_generation"));
		if (force)
			stats.forceToRecalc();
		try {
			stats.calcFullStatistics();
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					if (externalActions != null)
						externalActions.end(lyr);
				}
			});
		} catch (FileNotOpenException e) {
			RasterToolsUtil.debug("No se ha podido escribir en el fichero rmf", this, e);
		} catch (RasterDriverException e) {
			RasterToolsUtil.debug("Error leyendo bloques de datos para calcular estadísticas", this, e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.incrementabletask.IIncrementable#getPercent()
	 */
	public int getPercent() {
		return (stats != null) ? stats.getPercent() : 0;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.incrementabletask.IIncrementable#getTitle()
	 */
	public String getTitle() {
		return PluginServices.getText(this, "increase_statistics");
	}
}