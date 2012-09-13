/* gvSIG. Geographic Information System of the Valencian Government
*
* Copyright (C) 2007-2008 Infrastructures and Transports Department
* of the Valencian Government (CIT)
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
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
* MA  02110-1301, USA.
*
*/

/*
* AUTHORS (In addition to CIT):
* 2009 IVER T.I   {{Task}}
*/

package org.gvsig.raster.gui.wizards;

import java.util.Map;

import org.cresques.cts.IProjection;
import org.gvsig.PrepareContextView;
import org.gvsig.PrepareLayer;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontrol.MapControl;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.raster.Configuration;
import org.gvsig.raster.gui.wizards.projection.RasterProjectionActionsDialog;
import org.gvsig.raster.gui.wizards.projection.RasterProjectionActionsPanel;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.rastertools.reproject.ui.LayerReprojectPanel;

public class PrepareLayerAskProjection implements PrepareLayer {
	public FLayer prepare(FLayer layer, PrepareContextView context) {
		if (!(layer instanceof FLyrRasterSE))
			return layer;
		FLyrRasterSE lyrRaster = (FLyrRasterSE) layer;
		if (Configuration.getValue("general_ask_projection",
				Boolean.valueOf(false)).booleanValue())
			lyrRaster = compareProjections(lyrRaster, context.getMapControl());

		return lyrRaster;
	}

	/**
	 * Compara las proyecciones de la vista y la capa. En caso de ser diferentes
	 * pregunta por las opciones a realizar.
	 *
	 * @param lyr
	 * @param context
	 * @param mapControl
	 */
	private FLyrRasterSE compareProjections(FLyrRasterSE lyr,
			MapControl mapControl) {
		IProjection viewProj = mapControl.getProjection();
		IProjection lyrProj = lyr.readProjection();
		if (lyrProj == null) {
			lyr.setProjection(viewProj);
			return lyr;
		}
		if (viewProj == null) {
			mapControl.setProjection(lyrProj);
			return lyr;
		}

		/*
		 * Si las proyecciones de vista y raster son distintas se lanza el
		 * dialogo de selección de opciones. Este dialogo solo se lanza en caso
		 * de que el checkbox de aplicar a todos los ficheros no haya sido
		 * marcado. En este caso para el resto de ficheros de esa selección se
		 * hará la misma acción que se hizo para el primero.
		 */
		if (!viewProj.getAbrev().endsWith(lyrProj.getAbrev())) {
			boolean showDialog = false;
			if (!RasterProjectionActionsPanel.selectAllFiles)
				showDialog = true;

			RasterProjectionActionsDialog dialog = null;
			if (showDialog)
				dialog = new RasterProjectionActionsDialog(lyr);
			else if (FileOpenRaster.defaultActionLayer == FileOpenRaster.REPROJECT // FIXME
					&& !lyr.isReproyectable())
				dialog = new RasterProjectionActionsDialog(lyr);
			int select = FileOpenRaster.defaultActionLayer;
			if (dialog != null)
				select = dialog.getRasterProjectionActionsPanel()
						.getSelection();

			return layerActions(select, lyr, mapControl);
		}
		return lyr;

	}

	/**
	 * Acciones posibles cuando la proyección de la capa es distinta de la de la
	 * vista.
	 *
	 * @param action
	 *            Valor de la acción. Está representado en
	 *            RasterProjectionActionsPanel
	 * @param mapControl
	 */
	private FLyrRasterSE layerActions(int action, FLyrRasterSE lyr,
			MapControl mapControl) {
		// Cambia la proyección de la vista y carga la capa
		if (action == FileOpenRaster.CHANGE_VIEW_PROJECTION)
			if (lyr != null) {
				mapControl.setProjection(lyr.readProjection());
				lyr.setVisible(true);
			}

		// Ignora la proyección de la capa y la carga
		if (action == FileOpenRaster.IGNORE)
			if (lyr != null)
				lyr.setVisible(true);

		// Reproyectando
		if (action == FileOpenRaster.REPROJECT) {
			LayerReprojectPanel reprojectPanel = new LayerReprojectPanel(lyr,
					Boolean.FALSE);
			RasterToolsUtil.addWindow(reprojectPanel);
		}

		// No carga
		if (action == FileOpenRaster.NOTLOAD)
			return null;
		return lyr;
	}

	public String getDescription() {
		return "Prepare projection for Raster Layer";
	}

	public String getName() {
		return "PrepareRasterLayerProjection";
	}

	public Object create() {
		return this;
	}

	public Object create(Object[] args) {
		return this;
	}

	public Object create(Map args) {
		return this;
	}
}
