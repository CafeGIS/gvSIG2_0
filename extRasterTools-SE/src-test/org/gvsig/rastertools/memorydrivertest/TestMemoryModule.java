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
 */
package org.gvsig.rastertools.memorydrivertest;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.AddLayer;
import com.iver.cit.gvsig.project.documents.view.gui.View;


/**
 * Extensión para el filtro de pansharpening de raster
 * @author Nacho Brodin (brodin_ign@gva.es)
 */
public class TestMemoryModule extends Extension {
	
    /* (non-Javadoc)
     * @see com.iver.andami.plugins.Extension#inicializar()
     */
    public void initialize() {
    	AddLayer.addWizard(TestMemoryDriverWizard.class);
    }
   
    /**
     * Cargamos el listener y ejecutamos la herramienta de salvar a Raster.
     */
    public void execute(String actionCommand) {
    	
    }

    /* (non-Javadoc)
     * @see com.iver.andami.plugins.Extension#isEnabled()
     */
    public boolean isEnabled() {
        return true;
    }

    /**
     * Mostramos el control si hay alguna capa cargada.
     */
    public boolean isVisible() {
        com.iver.andami.ui.mdiManager.IWindow f = PluginServices.getMDIManager()
                                                             .getActiveWindow();
        if (f == null) 
            return false;
      
        return (f  instanceof View);
    }
}
