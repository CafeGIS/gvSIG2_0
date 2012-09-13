/*
 * Created on 1-dic-2007
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
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
package org.gvsig.gvsig3dgui.view;

import java.awt.Component;
import java.io.File;
import java.util.Hashtable;
import java.util.Iterator;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.gvsig.osgvp.viewer.IViewerContainer;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.fmap.layers.FLayers;



/**
 * Extensión para exportar en algunos formatos raster la vista3D actual.
 *
 * @author Ángel Fraile.
 */
public class Export extends Extension {
	private String lastPath = null;
	private String path = null;
	private Hashtable cmsExtensionsSupported = null;
	private Hashtable jimiExtensionsSupported = null;
	private IViewerContainer m_canvas3d = null;
	private String arg0;

	/**
	 * @see com.iver.andami.plugins.IExtension#isEnabled()
	 */
	public boolean isEnabled() {
		
		View3D f = (View3D) PluginServices.getMDIManager().getActiveWindow();

		if (f == null) {//Si no es una vista3D.
			return false;
		}
		//es una vista 3D, miro sus capas.
		FLayers layers = f.getModel().getMapContext().getLayers();
		for (int i=0;i< layers.getLayersCount();i++) {
			return layers.getLayer(i).isAvailable();
		}
		return false;

	}

	/**
	 * @see com.iver.mdiApp.plugins.IExtension#isVisible()
	 */
	public boolean isVisible() {
		com.iver.andami.ui.mdiManager.IWindow f = (com.iver.andami.ui.mdiManager.IWindow) 
		PluginServices.getMDIManager().getActiveWindow();

		if (f == null) {
			return false;
		}

		return (f instanceof View3D);
	}

	/**
	 * @see com.iver.andami.plugins.IExtension#initialize()
	 */
	public void initialize() {
	}

	/* (non-Javadoc)
	 * @see com.iver.andami.plugins.Extension#postInitialize()
	 */
	public void postInitialize() {
		cmsExtensionsSupported = new Hashtable();
		jimiExtensionsSupported = new Hashtable();
		jimiExtensionsSupported.put("png",new MyFileFilter("png",
				PluginServices.getText(this, "png"), "jimi"));
		cmsExtensionsSupported.put("jpg", new MyFileFilter("jpg",
				PluginServices.getText(this, "jpg"), "cms"));		
//		jimiExtensionsSupported.put("bmp",new MyFileFilter("bmp",
//				PluginServices.getText(this, "bmp"), "jimi"));


	}

	/**
	 * @see com.iver.andami.plugins.IExtension#execute(java.lang.String)
	 */
	public void execute(String actionCommand) {
		JFileChooser jfc = new JFileChooser(lastPath);
		jfc.removeChoosableFileFilter(jfc.getAcceptAllFileFilter());
		Iterator iter;
		
		iter = cmsExtensionsSupported.values().iterator();
		while (iter.hasNext()){
			jfc.addChoosableFileFilter((FileFilter)iter.next());
		}

		iter = jimiExtensionsSupported.values().iterator();
		while (iter.hasNext()){
			jfc.addChoosableFileFilter((FileFilter)iter.next());
		}

		jfc.setFileFilter((FileFilter)jimiExtensionsSupported.get("png"));
		if (jfc.showSaveDialog((Component) PluginServices.getMainFrame())==JFileChooser.APPROVE_OPTION){
			
			com.iver.andami.ui.mdiManager.IWindow activeWindow = PluginServices
			.getMDIManager().getActiveWindow();
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
					View3D view3D;
					view3D = (View3D) activeWindow;
					MyFileFilter filter = (MyFileFilter)jfc.getFileFilter();// png, jpg
					f = filter.normalizeExtension(f);//"name" + "." + "png", "name" + "." + "jpg".
					view3D.getCanvas3d().getOSGViewer().takeScreenshot(f.getAbsolutePath());//screen snapshop.
			}//if activeWindow.
		}//If aprove option.
	}//void execute.
}//end class.


/**
*
* @version 14/08/2007
* @author Borja Sánchez Zamorano (borja.sanchez@iver.es)
*
*/
class MyFileFilter extends FileFilter{

	private String[] extensiones=new String[1];
	private String description;
	private boolean dirs = true;
	private String info= null;

	public MyFileFilter(String[] ext, String desc) {
		extensiones = ext;
		description = desc;
	}

	public MyFileFilter(String[] ext, String desc,String info) {
		extensiones = ext;
		description = desc;
		this.info = info;
	}

	public MyFileFilter(String ext, String desc) {
		extensiones[0] = ext;
		description = desc;
	}

	public MyFileFilter(String ext, String desc,String info) {
		extensiones[0] = ext;
		description = desc;
		this.info = info;
	}

	public MyFileFilter(String ext, String desc, boolean dirs) {
		extensiones[0] = ext;
		description = desc;
		this.dirs = dirs;
	}

	public MyFileFilter(String ext, String desc, boolean dirs,String info) {
		extensiones[0] = ext;
		description = desc;
		this.dirs = dirs;
		this.info = info;
	}

	public boolean accept(File f) {
		if (f.isDirectory()) {
			if (dirs) {
				return true;
			} else {
				return false;
			}
		}
		for (int i=0;i<extensiones.length;i++){
			if (extensiones[i].equals("")){
				continue;
			}
			if (getExtensionOfAFile(f).equalsIgnoreCase(extensiones[i])){
				return true;
			}
		}

		return false;
	}

	/**
	 * @see javax.swing.filechooser.FileFilter#getDescription()
	 */
	public String getDescription() {
		return description;
	}

	public String[] getExtensions() {
		return extensiones;
	}

	public boolean isDirectory(){
		return dirs;
	}

	private String getExtensionOfAFile(File file){
		String name;
		int dotPos;
		name = file.getName();
		dotPos = name.lastIndexOf(".");
		if (dotPos < 1){
			return "";
		}
		return name.substring(dotPos+1);
	}

	public File normalizeExtension(File file){
		String ext = getExtensionOfAFile(file);
		if (ext.equals("") || !(this.accept(file))){
			return new File(file.getAbsolutePath() + "." + extensiones[0]);
		}
		return file;
	}

	public String getInfo(){
		return this.info;
	}

	public void setInfo(String info){
		this.info = info;
	}

}