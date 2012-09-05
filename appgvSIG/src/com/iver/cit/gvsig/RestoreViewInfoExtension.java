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
import com.iver.andami.ui.mdiManager.IWindowTransform;


public class RestoreViewInfoExtension extends Extension{
    private static final Logger logger = LoggerFactory
            .getLogger(RestoreViewInfoExtension.class);
    
	/**
	 * @see com.iver.andami.plugins.IExtension#initialize()
	 */
	public void initialize() {
	}

	/**
	 * @see com.iver.andami.plugins.IExtension#execute(java.lang.String)
	 */
	public void execute(String s) {
		IWindow view = PluginServices.getMDIManager().getFocusWindow();
        if (s.equals("RESTORE")){
        	IWindowTransform vt=(IWindowTransform)view;
        	vt.restore();
        }
        PluginServices.getMainFrame().enableControls();
	}

	/**
	 * @see com.iver.andami.plugins.IExtension#isEnabled()
	 */
	public boolean isEnabled() {
       return true;
	}

	/**
	 * @see com.iver.andami.plugins.IExtension#isVisible()
	 */
	public boolean isVisible() {
		IWindow f = PluginServices.getMDIManager().getFocusWindow();
		if (f == null) {
			return false;
		}
		if (f instanceof IWindowTransform) {
			IWindowTransform vt=(IWindowTransform)f;
			if (vt.isPalette())
				return true;
		}
		return false;
	}

}
