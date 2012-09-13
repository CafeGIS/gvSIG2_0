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
package org.gvsig.rastertools.overviews;

import org.gvsig.addo.BuildingOverviewsException;
import org.gvsig.addo.IOverviewIncrement;
import org.gvsig.addo.Jaddo;
import org.gvsig.addo.WritingException;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.raster.Configuration;
import org.gvsig.raster.RasterProcess;
import org.gvsig.raster.util.RasterToolsUtil;

import com.iver.andami.PluginServices;

/**
 * Proceso para la generación de overviews.
 *
 * 10/12/2007
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class OverviewsProcess extends RasterProcess implements IOverviewIncrement {
	private FLyrRasterSE rasterSE      = null;
	private int          value         = 0;
	private int          resamplingAlg = Jaddo.AVERAGE;
	private int[]        overviews     = new int[] { 2, 4, 8, 16 };

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.rastertools.RasterProcess#init()
	 */
	public void init() {
		rasterSE = getLayerParam("layer");

		int overviewsRate = 2;
		int nOverviews = 4;
		overviewsRate = Configuration.getValue("overviews_rate", new Integer(overviewsRate)).intValue();
		nOverviews = Configuration.getValue("overviews_number", new Integer(nOverviews)).intValue();
		resamplingAlg = Configuration.getValue("overviews_resampling_algorithm", new Integer(resamplingAlg)).intValue();

		// Leemos de la configuración los valores de algoritmo a usar,
		// número de overviews a generar y proporción de la primera overview

		overviews = new int[nOverviews];
		overviews[0] = overviewsRate;
		for (int i = 1; i < nOverviews; i++)
			overviews[i] = overviewsRate * overviews[i - 1];
	}

	/**
	 * Método donde se ejecutará el Thread, aquí se generaran las
	 * overviews
	 */
	public void process() {
		insertLineLog(PluginServices.getText(this, "overviews_generating"));

		Jaddo build = new Jaddo();
		build.setIncrementListener(this);
		try {
			for (int i = 0; i < rasterSE.getFileCount(); i++) {
				insertLineLog(" Dataset: " + i);
				build.buildOverviews(resamplingAlg, rasterSE.getFileName()[i], overviews);
			}
			if (externalActions != null)
				externalActions.end(rasterSE);
		} catch (BuildingOverviewsException e) {
			if (incrementableTask != null)
				incrementableTask.hideWindow();
			RasterToolsUtil.messageBoxError("error_create_overviews", this, e);
		} catch (WritingException e) {
			if (incrementableTask != null)
				incrementableTask.hideWindow();
			RasterToolsUtil.messageBoxError("error_write_overviews", this, e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.incrementabletask.IIncrementable#getPercent()
	 */
	public int getPercent() {
		return value;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.addo.IOverviewIncrement#setPercent(int)
	 */
	public void setPercent(int value) {
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.incrementabletask.IIncrementable#getTitle()
	 */
	public String getTitle() {
		return PluginServices.getText(this, "incremento_overview");
	}
}