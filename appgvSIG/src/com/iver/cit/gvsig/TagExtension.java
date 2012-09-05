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

import java.awt.Component;

import javax.swing.JOptionPane;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;


public class TagExtension extends Extension{
	private Layout layout = null;
	/**
	 * @see com.iver.andami.plugins.IExtension#initialize()
	 */
	public void initialize() {
		registerIcons();
	}

	private void registerIcons(){

		PluginServices.getIconTheme().registerDefault(
				"layout-tag",
				this.getClass().getClassLoader().getResource("images/tag.png")
			);

		PluginServices.getIconTheme().registerDefault(
				"layout-insert-tag",
				this.getClass().getClassLoader().getResource("images/gtk-add.png")
			);

		PluginServices.getIconTheme().registerDefault(
				"layout-show-tag",
				this.getClass().getClassLoader().getResource("images/gtk-apply.png")
			);
	}

	/**
	 * @see com.iver.andami.plugins.IExtension#execute(java.lang.String)
	 */
	public void execute(String s) {

		/* FLayoutZooms zooms = new FLayoutZooms(layout);
		logger.debug("Comand : " + s);
		if (s.compareTo("SET_TAG") == 0) {
			layout.setTool(Layout.SET_TAG);
		} */

        // FJP: Cambio: abrimos la ventana de tag para
        // asignar el tag a los elementos seleccionados
        if (s.equals("SET_TAG"))
        {
            IFFrame[] selectedFrames = layout.getLayoutContext().getFFrameSelected();
            if (selectedFrames.length > 0)
            {
                String defaultStr = "";
                if (selectedFrames.length == 1)
                    defaultStr = selectedFrames[0].getTag();
                String theTag = JOptionPane.showInputDialog((Component)PluginServices.getMainFrame(), "Introduzca el tag:", defaultStr);
                if (theTag!=null) {
                	for (int i=0; i< selectedFrames.length; i++)
                		selectedFrames[i].setTag(theTag);
            		}
                	layout.getModel().setModified(true);
            	}
        } else if (s.equals("VIEW_TAGS"))
        {
//            IFFrame[] frames = layout.getFFrames();
//            for (int i=0; i< frames.length; i++)
//            {
//                IFFrame f = frames[i];
//                if (f instanceof FFrameText)
//                {
//                    FFrameText txt = (FFrameText) f;
//                    if (f.getTag() != null)
//                    {
//                        txt.clearText();
//                        txt.addText(f.getTag());
//                    }
//                }
//            }
        	layout.setShowIconTag(true);
            layout.getLayoutControl().refresh();
        }
	}

	/**
	 * @see com.iver.andami.plugins.IExtension#isEnabled()
	 */
	public boolean isEnabled() {
		IFFrame[] selectedFrames = layout.getLayoutContext().getFFrameSelected();
		if (selectedFrames == null) return false;
		return (selectedFrames.length > 0);
	}

	/**
	 * @see com.iver.andami.plugins.IExtension#isVisible()
	 */
	public boolean isVisible() {
		IWindow f = PluginServices.getMDIManager().getActiveWindow();

		if (f!=null && f instanceof Layout) {
			layout=(Layout)f;
			return true;
		}
		return false;
	}

}
