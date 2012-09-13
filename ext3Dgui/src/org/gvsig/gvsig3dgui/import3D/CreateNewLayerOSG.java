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

package org.gvsig.gvsig3dgui.import3D;

import org.gvsig.gvsig3d.cacheservices.OSGCacheService;
import org.gvsig.gvsig3d.drivers.GvsigDriverOSG;
import org.gvsig.gvsig3d.map3d.MapContext3D;
import org.gvsig.gvsig3dgui.view.View3D;

import com.iver.ai2.gvsig3d.map3d.layers.Layer3DProps;
import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.VectorialAdapter;
import com.iver.cit.gvsig.fmap.layers.VectorialDefaultAdapter;
import com.iver.cit.gvsig.project.documents.view.IProjectView;

/**
 * @author Ángel Fraile Griñán  e-mail: angel.fraile@iver.es
 * @since 1.1
 * 
 */
public class CreateNewLayerOSG extends Extension {
	private FLyrVect layerOSG;

	
	/**
	 * Creating a new layer OSG editable, ready to insert OSG objects
	 */
	public void execute(String actionCommand) {

		if (actionCommand.equals("CREARCAPA")) {
			com.iver.andami.ui.mdiManager.IWindow activeWindow = PluginServices
					.getMDIManager().getActiveWindow();

			if (activeWindow instanceof View3D) {
				GvsigDriverOSG d = new GvsigDriverOSG();
				VectorialAdapter adapter = new VectorialDefaultAdapter();
				adapter.setDriver(d);

				layerOSG = new FLyrVect();
				View3D vista = (View3D) PluginServices.getMDIManager()
						.getActiveWindow();
				IProjectView model = vista.getModel();

				MapContext3D mapa = (MapContext3D) model.getMapContext();
				layerOSG.setName("defaultLayerOSG");
				layerOSG.setSource(adapter);

				// add 3D properties, mainly to keep track of hooked state
				Layer3DProps props3D = new Layer3DProps();
				props3D.setType(Layer3DProps.layer3DOSG);
				props3D.setChooseType(false);
				props3D.setLayer(layerOSG);
				props3D.setNewLayerOSG(true);
				

				layerOSG.setProperty("3DLayerExtension", props3D);
				layerOSG.setProjection(mapa.getProjection());
				mapa.getLayers().addLayer(layerOSG);

				OSGCacheService osgCacheService = (OSGCacheService) props3D
						.getCacheService();
				osgCacheService.startEditing();
			}
		}
	}

	public void initialize() {
		PluginServices.getIconTheme().registerDefault(
				"create_new_layer_OSG",
				this.getClass().getClassLoader().getResource(
						"images/plugin_link.png"));
	}

	public boolean isEnabled() {
		com.iver.andami.ui.mdiManager.IWindow f = PluginServices
				.getMDIManager().getActiveWindow();

		if (f == null) {
			return false;
		}

		// Only isEnable = true, where the view has a view3D.
		if (f instanceof View3D) {
			return true;
		}
		return false;

	}

	public boolean isVisible() {
		com.iver.andami.ui.mdiManager.IWindow f = PluginServices
				.getMDIManager().getActiveWindow();

		if (f == null) {
			return false;
		}

		// Only isVisible = true, where the view has a view3D.
		if (f instanceof View3D) {
			return true;
		}
		return false;

	}

}
