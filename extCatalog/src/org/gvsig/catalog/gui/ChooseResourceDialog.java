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
package org.gvsig.catalog.gui;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import javax.swing.JOptionPane;

import org.gvsig.catalog.CatalogLocator;
import org.gvsig.catalog.CatalogManager;
import org.gvsig.catalog.loaders.LayerLoader;
import org.gvsig.catalog.loaders.LayerLoaderException;
import org.gvsig.catalog.schemas.Resource;
import org.gvsig.catalog.ui.chooseresource.ChooseResourceDialogPanel;
import org.gvsig.i18n.Messages;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;


/**
 * This class implements the resources list view.
 * 
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class ChooseResourceDialog extends ChooseResourceDialogPanel
implements IWindow {
	private static final CatalogManager catalogManager = CatalogLocator.getCatalogManager();
	/**
	 * @param resources
	 * Found resources array
	 */
	public ChooseResourceDialog(Collection resources) {
		super(resources);
	}   
	
	public WindowInfo getWindowInfo() {
		WindowInfo m_viewinfo = new WindowInfo(WindowInfo.MODALDIALOG);
		m_viewinfo.setTitle(getName());
		return m_viewinfo;
	}
	
	
	public Object getWindowProfile(){
		return WindowInfo.DIALOG_PROFILE;
	}
	
	public void closeButtonActionPerformed() {
		closeJDialog();
	}
	
	public void closeJDialog() {
		setVisible(true);
		PluginServices.getMDIManager().closeWindow(ChooseResourceDialog.this);
	}
	
	/**
	 * This method is invocated when a resource button is clicked
	 */
	public void resourceButtonActionPerformed(Resource resource){
		LayerLoader loader = null;
		try {
			loader = catalogManager.getLayerLoader(resource);
			if (loader != null) {
				loader.loadLayer();
			} else {
				JOptionPane.showMessageDialog(this,
						Messages.getText("pluginNotFound") ,
						Messages.getText("pluginNotFoundTitle"),			
						JOptionPane.INFORMATION_MESSAGE);	
				return;
			}
			
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LayerLoaderException e) {
			JOptionPane.showMessageDialog(this,
					e.getMessage(),
					e.getWindowMessage(),			
					JOptionPane.ERROR_MESSAGE);	
		}		
		closeJDialog();
	}   
}





