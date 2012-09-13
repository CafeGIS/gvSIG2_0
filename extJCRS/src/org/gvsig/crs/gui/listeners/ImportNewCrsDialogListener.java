/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2006 Instituto de Desarrollo Regional and Generalitat Valenciana.
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
 *   Instituto de Desarrollo Regional (Universidad de Castilla La-Mancha)
 *   Campus Universitario s/n
 *   02071 Alabacete
 *   Spain
 *
 *   +34 967 599 200
 */

package org.gvsig.crs.gui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.gvsig.crs.ICrs;
import org.gvsig.crs.gui.dialog.ImportNewCrsDialog;
import org.gvsig.crs.persistence.CrsData;
import org.gvsig.crs.persistence.RecentCRSsPersistence;

import com.iver.andami.PluginServices;

/**
 * Listeners para el manejo de las importaciones de las partes correspondientes
 * a los crs definidos por el usuario
 * 
 * @author Jose Luis Gomez Martinez (joseluis.gomez@uclm.es)
 *
 */

public class ImportNewCrsDialogListener implements ActionListener,
ListSelectionListener, MouseListener {

	ImportNewCrsDialog dialog = null;	
	
	public ImportNewCrsDialogListener(ImportNewCrsDialog d) {
		dialog = d;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == dialog.getJButtonAccept()){			
			PluginServices.getMDIManager().closeWindow(dialog);
		}
		
		if (e.getSource() == dialog.getJButtonCancel()) {
			dialog.setCode(-1);
			PluginServices.getMDIManager().closeWindow(dialog);
		}		
	}

	public void valueChanged(ListSelectionEvent e) {
		if (e.getSource() == dialog.getEpsgPanel().getJTable().getSelectionModel()){
			String[] not_soported= new String[6];// = {"engineering", "vertical", "compound", "geocentric"};
			boolean soported = true;
			ListSelectionModel lsm = (ListSelectionModel)e.getSource();
			if (dialog.getOption().equals(PluginServices.getText(this, "projection"))) {
				not_soported[0] = "engineering";
				not_soported[1]= "vertical";
				not_soported[2] = "compound";
				not_soported[3] ="geocentric";
				not_soported[4]= "geographic 3D";
				not_soported[5] = "geographic 2D";
			} else {
				not_soported[0] = "engineering";
				not_soported[1]= "vertical";
				not_soported[2] = "compound";
				not_soported[3] ="geocentric";
				not_soported[4]= "geographic 3D";				
			}
			
	        if (lsm.isSelectionEmpty()) {	        	
	        	dialog.getEpsgPanel().setCodeCRS(-1);
	        	dialog.getJButtonAccept().setEnabled(false);        	
	        	dialog.getEpsgPanel().getInfoCrs().setEnabled(false);
	        	dialog.setCode(-1);     	        	
	        } 
	        else {	        	
	        	dialog.getEpsgPanel().selectedRowTable = lsm.getMinSelectionIndex();
	        	String crs_kind = (String)dialog.getEpsgPanel().sorter.getValueAt(dialog.getEpsgPanel().selectedRowTable,2);	        	
	        	for (int i = 0; i < not_soported.length; i++) {
	        		if (crs_kind.equals(not_soported[i]))
	        			soported = false;	        		
	        	}
	        	if (soported){
	        		int code = Integer.parseInt((String)dialog.getEpsgPanel().sorter.getValueAt(dialog.getEpsgPanel().selectedRowTable,0));
	        		dialog.setCode(code);
	        		dialog.getEpsgPanel().setCodeCRS(code);
	        		dialog.getJButtonAccept().setEnabled(true);
	            	dialog.getEpsgPanel().getInfoCrs().setEnabled(true);
	            	
	        	}
	        	else {
	        		if (dialog.getOption().equals(PluginServices.getText(this, "projection"))) {
	        			JOptionPane.showMessageDialog(dialog, PluginServices.getText(this,"crs_not_projected"), "Warning...",
								JOptionPane.WARNING_MESSAGE);
	        		} else {        			
	        			JOptionPane.showMessageDialog(dialog, PluginServices.getText(this,"crs_not_soported"), "Warning...",
							JOptionPane.WARNING_MESSAGE);
	        		}
	        		dialog.setCode(-1);
	        		dialog.getEpsgPanel().setCodeCRS(-1);
	        		dialog.getJButtonAccept().setEnabled(false);
	        		dialog.getEpsgPanel().getInfoCrs().setEnabled(false);
	        	}	        	
	        }
		}
		
	}

	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == dialog.getEpsgPanel().getJTable()){
			if (e.getClickCount() == 2){				
				PluginServices.getMDIManager().closeWindow(dialog);	
		    }
		}
	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
