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
package com.iver.cit.gvsig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;


/**
 * Extensión para editar los vértices de las geometrías añadidas en un FFrameGraphics.
 *
 * @author Vicente Caballero Navarro
 */
public class LayoutInsertToolsExtension extends Extension {
    private static final Logger logger = LoggerFactory
            .getLogger(LayoutInsertToolsExtension.class);
    
    private Layout layout = null;


    public void initialize() {
		// TODO Auto-generated method stub
    	registerIcons();
	}

    private void registerIcons(){

    	PluginServices.getIconTheme().registerDefault(
    			"view-select-geometry",
				this.getClass().getClassLoader().getResource("images/Select.png")
			);

    	PluginServices.getIconTheme().registerDefault(
    			"layout-insert-text",
				this.getClass().getClassLoader().getResource("images/MapaTexto.png")
			);

    	PluginServices.getIconTheme().registerDefault(
    			"layout-insert-point",
				this.getClass().getClassLoader().getResource("images/Point.png")
			);

    	PluginServices.getIconTheme().registerDefault(
    			"layout-insert-polygon",
				this.getClass().getClassLoader().getResource("images/Rectangle.png")
			);

    	PluginServices.getIconTheme().registerDefault(
    			"layout-insert-circle",
				this.getClass().getClassLoader().getResource("images/Circle.png")
			);


    	PluginServices.getIconTheme().registerDefault(
    			"layout-insert-line",
				this.getClass().getClassLoader().getResource("images/Rect.png")
			);

    	PluginServices.getIconTheme().registerDefault(
    			"layout-insert-polyline",
				this.getClass().getClassLoader().getResource("images/Line.png")
			);

    	PluginServices.getIconTheme().registerDefault(
    			"layout-insert-poligon",
				this.getClass().getClassLoader().getResource("images/Polygon.png")
			);

    	PluginServices.getIconTheme().registerDefault(
    			"layout-insert-image",
				this.getClass().getClassLoader().getResource("images/MapaImagen.png")
			);

    	PluginServices.getIconTheme().registerDefault(
    			"layout-insert-view",
				this.getClass().getClassLoader().getResource("images/MapaVista.png")
			);

    	PluginServices.getIconTheme().registerDefault(
    			"layout-insert-locator",
				this.getClass().getClassLoader().getResource("images/MapaLocalizador.png")
			);

    	PluginServices.getIconTheme().registerDefault(
    			"layout-insert-legend",
				this.getClass().getClassLoader().getResource("images/MapaLeyenda.png")
			);

    	PluginServices.getIconTheme().registerDefault(
    			"layout-insert-scalebar",
				this.getClass().getClassLoader().getResource("images/MapaEscala.png")
			);

    	PluginServices.getIconTheme().registerDefault(
    			"layout-insert-north",
				this.getClass().getClassLoader().getResource("images/MapaNorth.png")
			);

    	PluginServices.getIconTheme().registerDefault(
    			"layout-insert-box",
				this.getClass().getClassLoader().getResource("images/box.png")
			);
    	
    	PluginServices.getIconTheme().registerDefault(
    			"remove-selection",
				this.getClass().getClassLoader().getResource("images/remove.png")
			);
    }

    public void execute(String s) {
    	 layout = (Layout) PluginServices.getMDIManager().getActiveWindow();

         logger.debug("Comand : " + s);
         boolean insertGroupPosibility=false;
        if (s.equals("SELECT")) {
             layout.getLayoutControl().setTool("layoutselect");
        } else if (s.equals("RECTANGLEVIEW")) {
     		layout.getLayoutControl().setTool("layoutaddview");
     		insertGroupPosibility=true;
     	} else if (s.equals("RECTANGLEOVERVIEW")) {
     		layout.getLayoutControl().setTool("layoutaddoverview");
     		insertGroupPosibility=true;
     	} else if (s.equals("RECTANGLEPICTURE")) {
     		layout.getLayoutControl().setTool("layoutaddpicture");
     		insertGroupPosibility=true;
     	} else if (s.equals("RECTANGLESCALEBAR")) {
     		layout.getLayoutControl().setTool("layoutaddscale");
     		insertGroupPosibility=true;
     	} else if (s.equals("RECTANGLELEGEND")) {
     		layout.getLayoutControl().setTool("layoutaddlegend");
     		insertGroupPosibility=true;
     	} else if (s.equals("RECTANGLETEXT")) {
     		layout.getLayoutControl().setTool("layoutaddtext");
     		insertGroupPosibility=true;
     	} else if (s.equals("RECTANGLENORTH")) {
     		layout.getLayoutControl().setTool("layoutaddnorth");
     		insertGroupPosibility=true;
     	} else if (s.equals("RECTANGLEBOX")) {
     		layout.getLayoutControl().setTool("layoutaddbox");
     		insertGroupPosibility=true;
     	} else if (s.equals("POINT")) {
     		layout.getLayoutControl().setTool("layoutaddpoint");
     	} else if (s.equals("LINE")) {
     		layout.getLayoutControl().setTool("layoutaddline");
     	} else if (s.equals("POLYLINE")) {
     		layout.getLayoutControl().setTool("layoutaddpolyline");
     	} else if (s.equals("CIRCLE")) {
     		layout.getLayoutControl().setTool("layoutaddcircle");
     	} else if (s.equals("RECTANGLESIMPLE")) {
     		layout.getLayoutControl().setTool("layoutaddrectangle");
     	} else if (s.equals("POLYGON")) {
     		layout.getLayoutControl().setTool("layoutaddpolygon");
     	} else if (s.equals("REMOVE")){
     		layout.getLayoutContext().delFFrameSelected();
     		layout.getLayoutControl().refresh();
     	}
        layout.getModel().setModified(true);
   }


    public boolean isEnabled() {
    	IWindow f = PluginServices.getMDIManager().getActiveWindow();

        if (f == null) {
            return false;
        }

        if (f instanceof Layout) {
            return ((Layout) f).getLayoutContext().isEditable();
        }

        return false;
	}


    public boolean isVisible() {
		IWindow f = PluginServices.getMDIManager().getActiveWindow();

		if (f == null) {
			return false;
		}

		if (f instanceof Layout) {
			return true;
		} else {
			return false;
		}
	}
}
