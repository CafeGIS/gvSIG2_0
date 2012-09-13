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

import org.gvsig.PrepareContextView;
import org.gvsig.PrepareLayer;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.rastertools.RasterModule;
import org.gvsig.rastertools.geolocation.ui.GeoLocationOpeningRasterDialog;

import com.iver.andami.PluginServices;

public class PrepareLayerAskCoordinates implements PrepareLayer {


	public FLayer prepare(FLayer layer, PrepareContextView context) {
		if (!(layer instanceof FLyrRasterSE))
			return layer;
		FLyrRasterSE lyrRaster = (FLyrRasterSE) layer;
		if (RasterModule.askCoordinates)
			if(	lyrRaster.getFullEnvelope().getMinimum(0) == 0 &&
					lyrRaster.getFullEnvelope().getMinimum(1) == 0 &&
					lyrRaster.getFullEnvelope().getMaximum(0) == (lyrRaster).getPxWidth() &&
					lyrRaster.getFullEnvelope().getMaximum(1) == (lyrRaster).getPxHeight())
				if(RasterToolsUtil.messageBoxYesOrNot(lyrRaster.getName() + "\n" + PluginServices.getText(this, "layer_without_georref"), null)) {
					GeoLocationOpeningRasterDialog gld = new GeoLocationOpeningRasterDialog(lyrRaster);
					PluginServices.getMDIManager().addWindow(gld);
				}


		return lyrRaster;
	}

	public String getDescription() {
		return "Prepare Coordinates for Raster Layer";
	}

	public String getName() {
		return "PrepareRasterLayerCoordinates";
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
