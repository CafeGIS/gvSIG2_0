package org.gvsig.gazetteer;

import java.util.Hashtable;

import org.gvsig.fmap.mapcontext.layers.GraphicLayer;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;


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
/* CVS MESSAGES:
 *
 * $Id: DeleteSearchesExtension.java 499 2007-07-10 11:18:10 +0000 (Tue, 10 Jul 2007) jorpiell $
 * $Log$
 * Revision 1.1.2.1  2007/07/10 11:18:10  jorpiell
 * Added the registers
 *
 * Revision 1.7.2.4  2007/01/08 12:19:13  jcampos
 * Revert changes
 *
 * Revision 1.7.2.2  2006/11/15 00:08:16  jjdelcerro
 * *** empty log message ***
 *
 * Revision 1.9  2006/10/02 08:29:06  jorpiell
 * Modificados los cambios del Branch 10 al head
 *
 * Revision 1.7.2.1  2006/09/20 12:01:47  jorpiell
 * Se ha cambiado la versión de la jzkit, se ha incorporado la funcionalidad de cargar arcims
 *
 * Revision 1.8  2006/09/20 11:23:50  jorpiell
 * Se ha cambiado la versión de la librería jzkit de la 1 a la 2.0. Además se ha modificado lo del document
 *
 * Revision 1.7  2006/08/29 07:56:34  cesar
 * Rename the *View* family of classes to *Window* (ie: SingletonView to SingletonWindow, ViewInfo to WindowInfo, etc)
 *
 * Revision 1.6  2006/08/29 07:13:58  cesar
 * Rename class com.iver.andami.ui.mdiManager.View to com.iver.andami.ui.mdiManager.IWindow
 *
 * Revision 1.5  2006/07/24 10:10:06  jorpiell
 * Se ha añadido una hashtable de vistas activas para asociar la goma de borrar con la vista activa
 *
 * Revision 1.4  2006/06/19 11:19:35  jorpiell
 * Cambiadas las comprobaciones de una clase con == por instanceof.
 *
 * Revision 1.3  2006/05/02 15:53:59  jorpiell
 * Se ha cambiado la interfaz Extension por dos clases: una interfaz (IExtension) y una clase abstract(Extension). A partir de ahora todas las extensiones deben heredar de Extension
 *
 * Revision 1.2  2006/03/10 12:40:20  jorpiell
 * Ahora la goma de borrar los resultados del gazetteer ya aparece y desaparece en lugar de habilitarse/deshabilitarse
 *
 * Revision 1.1  2006/03/10 11:59:31  jorpiell
 * Se habilita la gomita para borrar los resultados de la busqueda del gazetteer en funcion de si hay o no resultados para borrar.
 *
 *
 */
/**
 * @autor Jorge Piera Llodrá (piera_jor@gva.es)
 */
public class DeleteSearchesExtension extends Extension{
	private static Hashtable views = new Hashtable();
	
	/*
	 * (non-Javadoc)
	 * @see com.iver.andami.plugins.Extension#inicializar()
	 */
	public void initialize() {
		// TODO Auto-generated method stub
		registerIcons();
	}
	
	private void registerIcons(){
		PluginServices.getIconTheme().registerDefault(
				"catalog-clear-point",
				this.getClass().getClassLoader().getResource("images/delone.png")
			);
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.andami.plugins.Extension#execute(java.lang.String)
	 */
	public void execute(String actionCommand) {
		deteleAllFeatures();		
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.andami.plugins.Extension#isEnabled()
	 */
	public boolean isEnabled() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.andami.plugins.Extension#isVisible()
	 */
	public boolean isVisible() {
		com.iver.andami.ui.mdiManager.IWindow activeView = PluginServices.getMDIManager()
		.getActiveWindow();
		
		if (activeView == null) {
			return false;
		}
		
		if (activeView instanceof BaseView){
			Boolean bool = (Boolean)views.get(activeView);
			if (bool == null){
				return false;
			}else{
				return bool.booleanValue();
			}
		}else{
			return false;
		}
	
	
	}

	/**
	 * Delete all the current view searches
	 *
	 */
	public static void deteleAllFeatures(){
		BaseView activeView = 
			(BaseView) PluginServices.getMDIManager().getActiveWindow();
		GraphicLayer lyr = activeView.getMapControl().getMapContext().getGraphicsLayer();
		lyr.clearAllGraphics();
		activeView.getMapControl().getViewPort().setEnvelope(activeView.getMapControl().getViewPort().getAdjustedExtent());
		views.put(activeView,new Boolean(false));
	}
	
	/**
	 * Sets the extension enabled
	 *
	 */
	public static void setVisible(){
		BaseView activeView = 
			(BaseView) PluginServices.getMDIManager().getActiveWindow();
		views.put(activeView,new Boolean(true));
	}

}
