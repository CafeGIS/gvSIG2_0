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

import javax.swing.JDialog;

import org.gvsig.catalog.metadataxml.XMLNode;
import org.gvsig.catalog.ui.showtree.ShowTreeDialogPanel;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;


/**
 * DOCUMENT ME!
 *
 * @author jorpiell TODO To change the template for this generated type comment
 *         go to Window - Preferences - Java - Code Style - Code Templates
 */
public class ShowTreeDialog extends ShowTreeDialogPanel implements IWindow {
	 private JDialog frame = null;
	/**
     * DOCUMENT ME!
     *
     * @param node
     */
    public ShowTreeDialog(JDialog frame,XMLNode node) {
        super(node);
        this.frame = frame;
        //8 --> Modal window
        PluginServices.getMDIManager().getWindowInfo(this).setWindowInfo(new WindowInfo(
                8));
    }

    /**
     * DOCUMENT ME!
     */
    public void closeButtonActionPerformed() {
        closeJDialog();
    }

    /**
     * DOCUMENT ME!
     */
    public void closeJDialog() {
    	frame.setVisible(false);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public WindowInfo getWindowInfo() {
        WindowInfo m_viewinfo = new WindowInfo(WindowInfo.MODALDIALOG);
        m_viewinfo.setTitle(getName());

        return m_viewinfo;
    }
    public Object getWindowProfile(){
		return WindowInfo.DIALOG_PROFILE;
	}
}
