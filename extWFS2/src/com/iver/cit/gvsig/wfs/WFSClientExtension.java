package com.iver.cit.gvsig.wfs;

import org.gvsig.fmap.dal.store.wfs.WFSLibrary;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.extensionpoint.ExtensionPoint;
import org.gvsig.tools.extensionpoint.ExtensionPointManager;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.AddLayer;
import com.iver.cit.gvsig.gui.panels.WFSAreaPanel;
import com.iver.cit.gvsig.gui.panels.WFSFilterPanel;
import com.iver.cit.gvsig.gui.panels.WFSInfoPanel;
import com.iver.cit.gvsig.gui.panels.WFSOptionsPanel;
import com.iver.cit.gvsig.gui.panels.WFSSelectFeaturePanel;
import com.iver.cit.gvsig.gui.panels.WFSSelectFieldsPanel;
import com.iver.cit.gvsig.gui.toc.WFSProperties;
import com.iver.cit.gvsig.gui.wizards.WFSWizard;
import com.iver.cit.gvsig.project.documents.view.legend.gui.ThemeManagerWindow;

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
 * $Id: WFSClientExtension.java 18381 2008-01-30 12:00:25Z jpiera $
 * $Log$
 * Revision 1.6.2.2  2006-11-17 11:28:45  ppiqueras
 * Corregidos bugs y aÃ±adida nueva funcionalidad.
 *
 * Revision 1.8  2006/10/02 09:09:45  jorpiell
 * Cambios del 10 copiados al head
 *
 * Revision 1.6  2006/09/05 15:41:52  jorpiell
 * Añadida la funcionalidad de cargar WFS desde el catálogo
 *
 * Revision 1.5  2006/07/05 12:04:50  jorpiell
 * Añadida la opción de propiedades vectoriales
 *
 * Revision 1.4  2006/06/21 12:35:45  jorpiell
 * Se ha añadido la ventana de propiedades. Esto implica añadir listeners por todos los paneles. Además no se muestra la geomatría en la lista de atributos y se muestran únicamnete los que se van a descargar
 *
 * Revision 1.3  2006/05/19 12:57:34  jorpiell
 * Ahora hereda de Extension
 *
 * Revision 1.1  2006/04/19 12:50:16  jorpiell
 * Primer commit de la aplicación. Se puede hacer un getCapabilities y ver el mensaje de vienvenida del servidor
 *
 *
 */
/**
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 */
public class WFSClientExtension extends Extension{

	public void initialize() {
		addProvider();		
		
		// Adds a new tab to the "add layer" wizard for WFS layer creation
		AddLayer.addWizard(WFSWizard.class);
		
		ExtensionPointManager extensionPointManager = ToolsLocator
		.getExtensionPointManager();
    
		//WFS properties panel
		ExtensionPoint viewExtensionPoint = extensionPointManager.add("View_TocActions");
       	//viewExtensionPoint.append("WFSProperties", "", new WFSPropertiesTocMenuEntry());
		//viewExtensionPoint.append("VectorialProperties", "", new WFSVectorialPropsTocMenuEntry());
    	
		//WFS properties tabs:
		ExtensionPoint wfsTabExtensionPoint = extensionPointManager.add("WFSPropertiesDialog");
		wfsTabExtensionPoint.append("info", "", WFSInfoPanel.class);
		wfsTabExtensionPoint.append("features", "", WFSSelectFeaturePanel.class);
		wfsTabExtensionPoint.append("fields", "", WFSSelectFieldsPanel.class);
		wfsTabExtensionPoint.append("options", "", WFSOptionsPanel.class);
		wfsTabExtensionPoint.append("filter", "", WFSFilterPanel.class);
		wfsTabExtensionPoint.append("area", "", WFSAreaPanel.class);
		
		//Adding forms to the generic vectorial properties
		ThemeManagerWindow.addPage(WFSProperties.class);
		ThemeManagerWindow.setTabEnabledForLayer(WFSProperties.class, FLyrVect.class, true);
		
		//Transaction
		//ExtensionPoint wfsExtensionPoint = extensionPointManager.add("WFSExtension");
		//wfsExtensionPoint.append("WFSLayerListener", "", WFSTEditionListener.class);
		
    	initilizeIcons();		
	}

	/**
	 * Adds the WFS provider
	 */
	private void addProvider() {
		WFSLibrary wfsLib = new WFSLibrary();
		wfsLib.initialize();
		wfsLib.postInitialize();		
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.andami.plugins.IExtension#execute(java.lang.String)
	 */
	public void execute(String actionCommand) {
		// TODO Auto-generated method stub
		
	}	

	/*
	 * (non-Javadoc)
	 * @see com.iver.andami.plugins.IExtension#isEnabled()
	 */
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.andami.plugins.IExtension#isVisible()
	 */
	public boolean isVisible() {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
	 * Prepares support for the icons that can be used in the WFS extension.
	 */
	void initilizeIcons(){
		// FLyrWFS.java
		PluginServices.getIconTheme().registerDefault(
	    		"WFS-icolayer",
	    		this.getClass().getClassLoader().getResource("images/icoLayer.png")
	    	);

		// WFSAreaPanel.java
		PluginServices.getIconTheme().registerDefault(
	    		"edit-undo",
	    		this.getClass().getClassLoader().getResource("images/edit-undo.png")
	    	);

		// WFSAreaPanel.java
		PluginServices.getIconTheme().registerDefault(
	    		"WFS-move",
	    		this.getClass().getClassLoader().getResource("images/move.png")
	    	);

		// WFSAreaPanel.java
		PluginServices.getIconTheme().registerDefault(
	    		"WFS-scaling",
	    		this.getClass().getClassLoader().getResource("images/scaling.png")
	    	);

		// WFSAreaPanel.java
		PluginServices.getIconTheme().registerDefault(
	    		"view-pan",
	    		this.getClass().getClassLoader().getResource("images/Pan.png")
	    	);

		// WFSAreaPanel.java
		PluginServices.getIconTheme().registerDefault(
	    		"view-zoom-in",
	    		this.getClass().getClassLoader().getResource("images/ZoomIn.png")
	    	);

		// WFSAreaPanel.java
		PluginServices.getIconTheme().registerDefault(
	    		"view-zoom-out",
	    		this.getClass().getClassLoader().getResource("images/ZoomOut.png")
	    	);

		// WFSAreaPanel.java
		PluginServices.getIconTheme().registerDefault(
	    		"view-zoom-back",
	    		this.getClass().getClassLoader().getResource("images/ZoomPrevio.png")
	    	);

		// WFSAreaPanel.java
		PluginServices.getIconTheme().registerDefault(
	    		"view-zoom-map-contents",
	    		this.getClass().getClassLoader().getResource("images/MapContents.png")
	    	);

		// WFSAreaPanel.java
		PluginServices.getIconTheme().registerDefault(
	    		"view-zoom-mas",
	    		this.getClass().getClassLoader().getResource("images/zoommas.png")
	    	);

		//		 WFSAreaPanel.java
		PluginServices.getIconTheme().registerDefault(
	    		"view-zoom-menos",
	    		this.getClass().getClassLoader().getResource("images/zoommenos.png")
	    	);

		//		 WFSAreaPanel.java
		PluginServices.getIconTheme().registerDefault(
	    		"view-query-distance",
	    		this.getClass().getClassLoader().getResource("images/Distancia.png")
	    	);

		//	 	WFSAreaPanel.java
		PluginServices.getIconTheme().registerDefault(
	    		"view-query-area",
	    		this.getClass().getClassLoader().getResource("images/Poligono16.png")
	    	);

		// 		WFSAreaPanel.java
		PluginServices.getIconTheme().registerDefault(
	    		"validate-area",
	    		this.getClass().getClassLoader().getResource("images/validate-area.png")
	    	);
	}

}
