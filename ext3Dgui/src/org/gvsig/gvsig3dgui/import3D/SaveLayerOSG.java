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

import java.awt.Component;
import java.io.File;
import java.util.Hashtable;
import java.util.Iterator;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.gvsig.gvsig3d.cacheservices.OSGCacheService;
import org.gvsig.gvsig3dgui.view.View3D;
import org.gvsig.osgvp.core.osg.Group;
import org.gvsig.osgvp.core.osgdb.osgDB;

import org.gvsig.osgvp.exceptions.node.NodeException;
import org.gvsig.osgvp.exceptions.node.SaveNodeException;

import com.iver.ai2.gvsig3d.map3d.layers.Layer3DProps;
import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.fmap.MapContext;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.project.documents.view.IProjectView;


/**
 * @author Ángel Fraile Griñán  e-mail: angel.fraile@iver.es
 * @since 1.1
 * 
 */
public class SaveLayerOSG extends Extension{


	private Hashtable iveExtensionsSupported = null;
	private Hashtable osgExtensionsSupported = null;
	private String lastPath = null;
	
	/**
	 * Creating save panel options
	 */
	public void execute(String actionCommand) {
		if (actionCommand.equals("SALVARCAPA")) {
			com.iver.andami.ui.mdiManager.IWindow activeWindow = PluginServices
			.getMDIManager().getActiveWindow();
			
			
			JFileChooser jfc = new JFileChooser(lastPath);
			jfc.removeChoosableFileFilter(jfc.getAcceptAllFileFilter());
			iveExtensionsSupported = new Hashtable();
			osgExtensionsSupported = new Hashtable();
			iveExtensionsSupported.put("ive", new MyFileFilter("ive",
					PluginServices.getText(this, "Ficheros *.ive"), "ive"));
			osgExtensionsSupported.put("osg", new MyFileFilter("osg",
					PluginServices.getText(this, "Ficheros *.osg"), "osg"));

			Iterator iter = osgExtensionsSupported.values().iterator();
			while (iter.hasNext()) {
				jfc.addChoosableFileFilter((FileFilter) iter.next());
			}

			iter = iveExtensionsSupported.values().iterator();
			while (iter.hasNext()) {
				jfc.addChoosableFileFilter((FileFilter) iter.next());
			}

			if (jfc.showSaveDialog((Component) PluginServices.getMainFrame()) == JFileChooser.APPROVE_OPTION) {
					File f = jfc.getSelectedFile();
						if (f.exists()){//file exists in the directory.
							int resp = JOptionPane.showConfirmDialog(
									(Component) PluginServices.getMainFrame(),
									PluginServices.getText(this,
											"fichero_ya_existe_seguro_desea_guardarlo")+
											"\n"+
											f.getAbsolutePath(),
									PluginServices.getText(this,"guardar"), JOptionPane.YES_NO_OPTION);
							if (resp != JOptionPane.YES_OPTION) {//cancel pressed.
								return;
							}
						}//if exits.
						if (activeWindow instanceof View3D ) {
//							View3D view3D;
//							view3D = (View3D) activeWindow;
							MyFileFilter filter = (MyFileFilter)jfc.getFileFilter();// ive, osg
							f = filter.normalizeExtension(f);//"name" + "." + "ive", "name" + "." + "osg".
							
							View3D vista = (View3D) PluginServices.getMDIManager().getActiveWindow();
							IProjectView model = vista.getModel();
							MapContext mapContext = model.getMapContext();
							FLayers layers = mapContext.getLayers();
							FLayer[] actives = layers.getActives();
							OSGCacheService osgCache = null;
							if (actives.length == 1
									&& Layer3DProps.getLayer3DProps(actives[0]).getType() == Layer3DProps.layer3DOSG) {
								osgCache = (OSGCacheService) Layer3DProps.getLayer3DProps(actives[0]).getCacheService();
								
							}
							try {
								Group node = osgCache.getLayerNode();
//								Optimizer opt = new Optimizer();
//								opt.optimize(node, OptimizationOptions.ALL_OPTIMIZATIONS);
								osgDB.writeNodeFile(node, f.getAbsolutePath());
							} catch (SaveNodeException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (NodeException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						//	view3D.getCanvas3d().getOSGViewer().takeScreenshot(f.getAbsolutePath());//screen snapshop.
					}//if activeWindow.
				}//If aprove option.
			
			}
	}

	public void initialize() {
		PluginServices.getIconTheme().registerDefault(
				"save_layer_OSG",
				this.getClass().getClassLoader().getResource(
				"images/plugin_edit.png"));
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
		if (actives.length == 1 && Layer3DProps.getLayer3DProps(actives[0])!= null && Layer3DProps.getLayer3DProps(actives[0]).getType() == Layer3DProps.layer3DOSG) {
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
