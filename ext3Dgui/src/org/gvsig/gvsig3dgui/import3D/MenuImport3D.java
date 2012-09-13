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

import java.io.File;

import org.gvsig.GeometryFactory3D;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.gvsig3d.cacheservices.OSGCacheService;
import org.gvsig.gvsig3d.drivers.GvsigDriverOSG;
import org.gvsig.gvsig3dgui.view.View3D;

import com.iver.ai2.gvsig3d.map3d.layers.Layer3DProps;
import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.fmap.MapContext;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.fmap.layers.LayerFactory;
import com.iver.cit.gvsig.project.documents.view.IProjectView;


/**
 * @author Ángel Fraile Griñán  e-mail: angel.fraile@iver.es
 * @since 1.1
 * 
 */
public class MenuImport3D extends Extension{
	
	
	private ControlImport3D controlImport;

	
	public void execute(String actionCommand) {
		int tipoObjeto = 0;
		
		if (actionCommand.equals("OBJECT"))
			tipoObjeto = 0;
		else if( actionCommand.equals("OBJECT_VECTORIAL") )
			tipoObjeto = 1;
	
		
		
		View3D vista = (View3D) PluginServices.getMDIManager().getActiveWindow();
		
		this.controlImport = new ControlImport3D(tipoObjeto,vista);
		
        PluginServices.getMDIManager().addWindow(controlImport);
		
	}

	public void initialize() {
		// Registering icons.
		PluginServices.getIconTheme().registerDefault(
				"insert_new_geometry_OSG",
				this.getClass().getClassLoader().getResource(
				"images/plugin_add.png"));
		
	}
	
	public void postInitialize() {
		LayerFactory.getDM().addDriver(new File(this.getClass().getResource(
        "/lib").getFile()),"gvSIG OSG Driver",GvsigDriverOSG.class);
		
		// Inicializing the geometry factory 3D
		GeometryManager.getInstance().registerGeometryFactory(new GeometryFactory3D());
		
	}

	/**
	 * Enable options.
	 * Enabled when a layer is selected and this type layer is layer3DOSG 
	 */
	public boolean isEnabled() {

		View3D vista = (View3D) PluginServices.getMDIManager().getActiveWindow();
		IProjectView model = vista.getModel();
		MapContext mapContext = model.getMapContext();
		FLayers layers = mapContext.getLayers();
		FLayer[] actives = layers.getActives();
		//Layer3DProps props3D = Layer3DProps.getLayer3DProps(actives[0]);
		if (actives.length == 1
				&& Layer3DProps.getLayer3DProps(actives[0]).getType() == Layer3DProps.layer3DOSG) {
			OSGCacheService osgCache = (OSGCacheService) Layer3DProps.getLayer3DProps(actives[0]).getCacheService();
			return true;
		}else
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


