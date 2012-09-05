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

import java.awt.Frame;
import java.util.Collection;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.gvsig.catalog.CatalogClient;
import org.gvsig.catalog.drivers.GetRecordsReply;
import org.gvsig.catalog.metadataxml.XMLNode;
import org.gvsig.catalog.schemas.Record;
import org.gvsig.catalog.schemas.Resource;
import org.gvsig.catalog.ui.showresults.ShowResultsDialogPanel;
import org.gvsig.catalog.utils.Frames;
import org.gvsig.i18n.Messages;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;


/**
 * DOCUMENT ME!
 *
 * @author luisw
 */
public class ShowResultsDialog extends ShowResultsDialogPanel implements IWindow {
    private JDialog frame = null;
    /**
     * DOCUMENT ME!
     *
     * @param client
     * @param nodes
     * @param currentRecord
     */
    public ShowResultsDialog(JDialog frame,CatalogClient client, GetRecordsReply recordsReply,
        int currentRecord) {
        super(client, recordsReply, currentRecord);
        this.frame = frame;
    }

    /**
     * DOCUMENT ME!
     */
    public void descriptionButtonActionPerformed() {
    	Record record = recordsReply.getRecordAt(getCurrentNode());
    	if (record == null){
    		return;
    	}
    	XMLNode node = record.getNode();
    	if (node == null){
    		JOptionPane.showMessageDialog(this,Messages.getText("Error_accediendo_a_los_datos"));
    	}else{
    		
    		JDialog panel = new JDialog((Frame) PluginServices.getMainFrame(), false);
    		Frames.centerFrame(panel, 796, 675);
    		//panel.setBounds(0, 0, 796, 675);
    		panel.setTitle(Messages.getText("metadata_tree"));
    		panel.setResizable(false);
    		
    		ShowTreeDialog dialog = new ShowTreeDialog(panel,node);
    		
    		panel.getContentPane().add(dialog);
    		panel.show();
    	}
        
//        PluginServices.getMDIManager().addView(dialog);
    }

    /**
     * DOCUMENT ME!
     */
    public void mapButtonActionPerformed() {
        Resource[] resources = controlsPanel.getRecord().getResources();
        Collection col = new java.util.ArrayList();
        for (int i=0 ; i<resources.length ;i++){
            col.add(resources[i]);
        }
        ChooseResourceDialog dialog = new ChooseResourceDialog(col);
        dialog.setBounds(0, 0, 586, 145);
        dialog.setName(Messages.getText("Recursos Disponibles"));
        dialog.setVisible(true);
        PluginServices.getMDIManager().addWindow(dialog);
    }

    /**
     * DOCUMENT ME!
     */
    public void closeButtonActionPerformed() {
        closeJDialog();
    }

    /**
     * Cierra el Dialog
     */
    public void closeJDialog() {
        frame.setVisible(false);
    }

    /* (non-Javadoc)
     * @see com.iver.andami.ui.mdiManager.View#getViewInfo()
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
