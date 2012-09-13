/* gvSIG. Sistema de Informacin Geogrfica de la Generalitat Valenciana
 *
 * Copyright (C) 2006 Instituto de Desarrollo Regional and Generalitat Valenciana.
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
 *   Av. Blasco Ibez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   Instituto de Desarrollo Regional (Universidad de Castilla La-Mancha)
 *   Campus Universitario s/n
 *   02071 Alabacete
 *   Spain
 *
 *   +34 967 599 200
 */

package org.gvsig.remotesensing;

import javax.swing.JOptionPane;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.DisposableIterator;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureSelection;
import org.gvsig.fmap.mapcontext.MapContext;
import org.gvsig.fmap.mapcontext.layers.FLayers;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;
import org.gvsig.fmap.raster.grid.roi.VectorialROI;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.raster.buffer.BufferFactory;
import org.gvsig.raster.buffer.RasterBufferInvalidException;
import org.gvsig.raster.grid.Grid;
import org.gvsig.raster.grid.GridException;
import org.gvsig.raster.util.RasterToolsUtil;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.project.documents.view.IProjectView;
import com.iver.cit.gvsig.project.documents.view.gui.View;

/**
 * Extensión para el Cálculo de Raster (Band Math)
 *
 * @author Diego Guerrero Sevilla (diego.guerrero@uclm.es)
 *
 */
public class ROIFromFeaturesExtension extends Extension {

	public void initialize() {

	}

	public void execute(String actionCommand) {
		if (actionCommand.equals("roi_from_features")){
			com.iver.andami.ui.mdiManager.IWindow activeWindow = PluginServices.getMDIManager().getActiveWindow();

			//si la ventana activa es de tipo Vista
			if (activeWindow instanceof View) {
				View view = (View) activeWindow;
				MapContext mapContext = view.getModel().getMapContext();
				FLyrVect flyrVect = null;
				FLyrRasterSE flyrRaster = null;
				for (int i = 0; i < mapContext.getLayers().getActives().length; i++){
					if (mapContext.getLayers().getActives()[i] instanceof FLyrVect) {
						flyrVect = (FLyrVect) mapContext.getLayers().getActives()[i];
					}
					if (mapContext.getLayers().getActives()[i] instanceof FLyrRasterSE) {
						flyrRaster = (FLyrRasterSE) mapContext.getLayers().getActives()[i];
					}
				}

				if (flyrRaster != null && flyrVect != null){
					Grid grid = null;
	            	BufferFactory dataSource = flyrRaster.getBufferFactory();

					int bands[]=null;
					bands = new  int [flyrRaster.getBandCount()];
					for(int i=0; i<flyrRaster.getBandCount();i++) {
						bands[i]=i;
					}
					try {
						grid = new Grid(dataSource, bands);
					} catch (RasterBufferInvalidException e) {
							e.printStackTrace();
					}
					VectorialROI roi = new VectorialROI(grid);
					DisposableIterator iterator = null;
					try {
						FeatureSelection fs = (FeatureSelection)flyrVect.getFeatureStore().getSelection();
						iterator = fs.iterator();
						boolean hasFeatures=false;
						while (iterator.hasNext()) {
							hasFeatures=true;
							Feature feature = (Feature) iterator.next();
							roi.addGeometry(feature.getDefaultGeometry());
						}
						if (hasFeatures){
							JOptionPane.showMessageDialog(null,
								"Mean: "+roi.getMeanValue()+"Max: "+roi.getMaxValue()+"Min: "+roi.getMinValue(), "ROI Statistics",
								JOptionPane.WARNING_MESSAGE);
						}
					}catch (GridException e) {
						RasterToolsUtil.messageBoxError("error_cargar_capa", this, e);
					} catch (DataException e) {
						RasterToolsUtil.messageBoxError("error_cargar_capa", this, e);
					} finally {
						if (iterator != null) {
							iterator.dispose();
						}
					}
				}
				else{

				}
			}
		}
	}

	public boolean isEnabled() {
		com.iver.andami.ui.mdiManager.IWindow f = PluginServices.getMDIManager().getActiveWindow();
		if (f == null) {
			return false;
		}
		if (f.getClass() == View.class) {
			View vista = (View) f;
			IProjectView model = vista.getModel();
			MapContext mapa = model.getMapContext();
			FLayers layers = mapa.getLayers();
			for (int i = 0; i < layers.getLayersCount(); i++) {
				if (layers.getLayer(i) instanceof FLyrRasterSE) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean isVisible() {
		com.iver.andami.ui.mdiManager.IWindow f = PluginServices.getMDIManager().getActiveWindow();
		if (f == null) {
			return false;
		}
		if (f instanceof View) {
			View vista = (View) f;
			IProjectView model = vista.getModel();
			MapContext mapa = model.getMapContext();
			return mapa.getLayers().getLayersCount() > 0;
		} else {
			return false;
		}
	}
}
