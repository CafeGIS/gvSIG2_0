/* gvSIG. Sistema de Informacin Geogrfica de la Generalitat Valenciana
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
 *   Av. Blasco Ibez, 50
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

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.gvsig.crs.CrsException;
import org.gvsig.crs.CrsWkt;
import org.gvsig.crs.ICrs;
import org.gvsig.crs.gui.CRSSelectionDialog;
import org.gvsig.crs.persistence.CrsData;
import org.gvsig.crs.persistence.RecentCRSsPersistence;

import com.iver.andami.PluginServices;

/**
 * Esta clase contiene los listeners necesarios para el manejo 
 * de los eventos del panel de eleccin de CRS de la vista principal.
 * 
 * @author Diego Guerrero Sevilla (diego.guerrero@uclm.es)
 * @author Jos Luis Gmez Martnez (jolugomar@gmail.com)
 *
 */

public class CRSSelectionDialogListener implements ActionListener,
		 ListSelectionListener, ItemListener, MouseListener {
	
	int[] valid_method_code = {9800, 9801, 9802, 9803, 9804, 9805, 9806, 9807, 9809, 9810, 
			9811, 9812, 9813, 9814, 9815, 9602, 9659, 9818, 9819, 9820, 9821, 9822, 9823, 
			9827, 9829};
	
	CRSSelectionDialog dialog = null;

	public CRSSelectionDialogListener(CRSSelectionDialog d) {
		dialog = d;
	}
	
	/**
	 * Manejador de los eventos relacionados con los botones del panel
	 * de seleccin de CRS.
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == dialog.getCrsMainPanel().getJButtonAccept()){
			ICrs crs = (ICrs) dialog.getProjection();
			try {
				crs.getProj4String();
			} catch (CrsException e1) {
				JOptionPane.showMessageDialog(dialog, PluginServices.getText(this,e1.getMessage()), "Warning...",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			dialog.setLastProj(crs);
			dialog.setDataSource(dialog.getCrsMainPanel().getDataSource());
        	if (dialog.getLastProj() != null){
        		/*
        		 * Actualizar recientes...
        		 */
        		String authority = ((ICrs)dialog.getLastProj()).getCrsWkt().getAuthority()[0];
        		String name = ((ICrs)dialog.getLastProj()).getCrsWkt().getName();
        		int code = ((ICrs)dialog.getLastProj()).getCode();
        		CrsData crsData = new CrsData(authority,code,name);
        		RecentCRSsPersistence persistence = new RecentCRSsPersistence();
        		persistence.addCrsData(crsData);
        		
        		
        		PluginServices.getMDIManager().closeWindow(dialog);
        		dialog.setOkPressed(true);
			}
        	else {
        		dialog.setOkPressed(false);
        	}	
		}
		
		if (e.getSource() == dialog.getCrsMainPanel().getJButtonCancel()){
			dialog.setProjection(dialog.getLastProj());
            PluginServices.getMDIManager().closeWindow(dialog);
            dialog.setOkPressed(false);
		}

	}


	/**
	 * Maneja los eventos del combobox de eleccin de repositorio para
	 * la seleccin del CRS de la vista
	 */
	public void itemStateChanged(ItemEvent e) {
		CardLayout cl = (CardLayout)(dialog.getCrsMainPanel().getJPanelMain().getLayout());
		String op = (String)e.getItem();		
		if (op.equals("EPSG")){
			dialog.getCrsMainPanel().getEpsgPanel().connection();	
			if(dialog.getCrsMainPanel().getEpsgPanel().getJTable().getSelectedRowCount()>0)
				dialog.getCrsMainPanel().getJButtonAccept().setEnabled(true);
			else
				dialog.getCrsMainPanel().getJButtonAccept().setEnabled(false);
				
		} else if (op.equals("IAU2000")) {
			dialog.getCrsMainPanel().getIauPanel().connection();
			if(dialog.getCrsMainPanel().getIauPanel().getJTable().getSelectedRowCount()>0)
				dialog.getCrsMainPanel().getJButtonAccept().setEnabled(true);
			else
				dialog.getCrsMainPanel().getJButtonAccept().setEnabled(false);
		}else if (op.equals("Recientes")) {
			//Comprobar de nuevo recientes por si hemos eliminado alguno de usuario
			dialog.getCrsMainPanel().getRecentsPanel().loadRecents();
			if(dialog.getCrsMainPanel().getRecentsPanel().getJTable().getSelectedRowCount()>0)
				dialog.getCrsMainPanel().getJButtonAccept().setEnabled(true);
			else
				dialog.getCrsMainPanel().getJButtonAccept().setEnabled(false);
		}
		else if (op.equals("ESRI")) {
			dialog.getCrsMainPanel().getEsriPanel().connection();
		if(dialog.getCrsMainPanel().getEsriPanel().getJTable().getSelectedRowCount()>0)
			dialog.getCrsMainPanel().getJButtonAccept().setEnabled(true);
		else
			dialog.getCrsMainPanel().getJButtonAccept().setEnabled(false);
		}else if (op.equals(PluginServices.getText(this, "newCRS"))) {
			dialog.getCrsMainPanel().getNewCrsPanel().connection();
			if(dialog.getCrsMainPanel().getNewCrsPanel().getJTable().getSelectedRowCount()>0) {
				dialog.getCrsMainPanel().getJButtonAccept().setEnabled(true);
				if (dialog.getCrsMainPanel().getNewCrsPanel().getSearchTextField().getText().equals(""))
					dialog.getCrsMainPanel().getNewCrsPanel().initializeTable();				
			}
			else
				dialog.getCrsMainPanel().getJButtonAccept().setEnabled(false);
		}
	    cl.show(dialog.getCrsMainPanel().getJPanelMain(), (String)e.getItem());
	    dialog.getCrsMainPanel().setDataSource((String)e.getItem());
	}

	/**
	 * Maneja el evento de cambio de valor de las tablas existentes
	 * en cada uno de los distintos repositorios para la seleccin
	 * del CRS de la vista.
	 */
	public void valueChanged(ListSelectionEvent e) {
		if (e.getSource() == dialog.getCrsMainPanel().getRecentsPanel().getJTable().getSelectionModel()){
			ListSelectionModel lsm = (ListSelectionModel)e.getSource();
			
	        if (lsm.isSelectionEmpty()) {
	        	dialog.getCrsMainPanel().getRecentsPanel().selectedRowTable = -1;
	        	dialog.getCrsMainPanel().getRecentsPanel().setCodeCRS(-1);
	        	dialog.getCrsMainPanel().getJButtonAccept().setEnabled(false);
	        	dialog.getCrsMainPanel().getRecentsPanel().getInfoCrs().setEnabled(false);
	        	dialog.setCode(0);	        	
	        } 
	        else {        	
	        	dialog.getCrsMainPanel().getRecentsPanel().initCrs();	        			        	
        		dialog.setCode(dialog.getCrsMainPanel().getRecentsPanel().getCodeCRS());
        		dialog.getCrsMainPanel().getRecentsPanel().getInfoCrs().setEnabled(true);	        	
            	dialog.getCrsMainPanel().getJButtonAccept().setEnabled(true);
	        }
		}
		
		if (e.getSource() == dialog.getCrsMainPanel().getIauPanel().getJTable().getSelectionModel()){
			ListSelectionModel lsm = (ListSelectionModel)e.getSource();
			String[] not_soported = {"Oblique_Cylindrical_Equal_Area"};
			boolean soported = true;
			 if (lsm.isSelectionEmpty()) {
		        	dialog.getCrsMainPanel().getIauPanel().selectedRowTable = -1;
		        	dialog.getCrsMainPanel().getIauPanel().setCodeCRS(-1);
		        	dialog.getCrsMainPanel().getJButtonAccept().setEnabled(false);
		        	dialog.getCrsMainPanel().getIauPanel().getInfoCrs().setEnabled(false);
		        	dialog.setCode(0);        	
		        	
			 } 
			 else {  
				 
				 dialog.getCrsMainPanel().getIauPanel().selectedRowTable = lsm.getMinSelectionIndex();
				 dialog.getCrsMainPanel().getIauPanel().setCodeCRS(Integer.parseInt((String)dialog.getCrsMainPanel().getIauPanel().sorter.getValueAt(dialog.getCrsMainPanel().getIauPanel().selectedRowTable,0)));
				 dialog.getCrsMainPanel().getIauPanel().setWKT();
				 CrsWkt crs = new CrsWkt(dialog.getCrsMainPanel().getIauPanel().getWKT());
				 String crs_kind = crs.getProjection();
				 for (int i = 0; i < not_soported.length; i++) {
					 if (crs_kind.equals(not_soported[i]))
		        		soported = false;					 
		        }
		        if (soported){
		        	dialog.setCode(dialog.getCrsMainPanel().getIauPanel().getCodeCRS());
		        	dialog.getCrsMainPanel().getIauPanel().getInfoCrs().setEnabled(true);
		        	dialog.getCrsMainPanel().getJButtonAccept().setEnabled(true);	
		        }
		        else {
		        	JOptionPane.showMessageDialog(dialog, PluginServices.getText(this,"crs_not_soported"), "Warning...",
							JOptionPane.WARNING_MESSAGE);
		        	dialog.getCrsMainPanel().getIauPanel().setCodeCRS(-1);
		        	dialog.getCrsMainPanel().getIauPanel().setCodeCRS(0);
		        	dialog.getCrsMainPanel().getIauPanel().getInfoCrs().setEnabled(false);
		        	dialog.getCrsMainPanel().getJButtonAccept().setEnabled(false);	        		
		        }	        		            	
		     }
		}
		
		if (e.getSource() == dialog.getCrsMainPanel().getEpsgPanel().getJTable().getSelectionModel()){
			String[] not_soported = {"engineering", "vertical", "compound", "geocentric", "geographic 3D"};
			
			String problem = "";
			boolean soported = true;
			boolean soported2 = false;
			ListSelectionModel lsm = (ListSelectionModel)e.getSource();
								
	        if (lsm.isSelectionEmpty()) {
	        	dialog.getCrsMainPanel().getEpsgPanel().selectedRowTable = -1;
	        	dialog.getCrsMainPanel().getEpsgPanel().setCodeCRS(-1);
	        	dialog.getCrsMainPanel().getJButtonAccept().setEnabled(false);        	
	        	dialog.getCrsMainPanel().getEpsgPanel().getInfoCrs().setEnabled(false);
	        	dialog.setCode(0);     	        	
	        } 
	        else {	        	
	        	dialog.getCrsMainPanel().getEpsgPanel().selectedRowTable = lsm.getMinSelectionIndex();
	        	String crs_kind = (String)dialog.getCrsMainPanel().getEpsgPanel().sorter.getValueAt(dialog.getCrsMainPanel().getEpsgPanel().selectedRowTable,2);	        	
	        	for (int i = 0; i < not_soported.length; i++) {
	        		if (crs_kind.equals(not_soported[i])){
	        			soported = false;
	        			problem = not_soported[i];
	        		}
	        	}
	        	for (int i=0; i< valid_method_code.length;i++) {
	        		if (dialog.getCrsMainPanel().getEpsgPanel().getProjectionCode((String)dialog.getCrsMainPanel().getEpsgPanel().sorter.getValueAt(dialog.getCrsMainPanel().getEpsgPanel().selectedRowTable,0)) == valid_method_code[i]) {
	        			soported2 = true;
	        		}
	        	}
	        	if (crs_kind.equals("geographic 2D")) {
	        		soported2 = true;
	        	}
	        	if (soported && soported2){
	        		dialog.getCrsMainPanel().getEpsgPanel().setCodeCRS(Integer.parseInt((String)dialog.getCrsMainPanel().getEpsgPanel().sorter.getValueAt(dialog.getCrsMainPanel().getEpsgPanel().selectedRowTable,0)));
	            	dialog.getCrsMainPanel().getEpsgPanel().setWKT();
	            	dialog.setCode(dialog.getCrsMainPanel().getEpsgPanel().epsg_code);
	            	dialog.getCrsMainPanel().getJButtonAccept().setEnabled(true);
	            	dialog.getCrsMainPanel().getEpsgPanel().getInfoCrs().setEnabled(true);
	            	
	        	}
	        	else {
	        		JOptionPane.showMessageDialog(dialog, PluginServices.getText(this,"crs_not_soported")+" "+PluginServices.getText(this,problem), "Warning...",
							JOptionPane.WARNING_MESSAGE);
	        		dialog.getCrsMainPanel().getEpsgPanel().setCodeCRS(-1);
	        		dialog.setCode(0);
	        		dialog.getCrsMainPanel().getJButtonAccept().setEnabled(false);
	        		dialog.getCrsMainPanel().getEpsgPanel().getInfoCrs().setEnabled(false);
	        	}	        	
	        }
		}
		
		if (e.getSource() == dialog.getCrsMainPanel().getEsriPanel().getJTable().getSelectionModel()){
			ListSelectionModel lsm = (ListSelectionModel)e.getSource();
			String[] not_soported = {"Oblique_Cylindrical_Equal_Area"};
			boolean soported = true;
			 if (lsm.isSelectionEmpty()) {
		        	dialog.getCrsMainPanel().getEsriPanel().selectedRowTable = -1;
		        	dialog.getCrsMainPanel().getEsriPanel().setCodeCRS(-1);
		        	dialog.getCrsMainPanel().getEsriPanel().getInfoCrs().setEnabled(false);		        	
		        	dialog.getCrsMainPanel().getJButtonAccept().setEnabled(false);		        	
		        	dialog.setCode(0);        	
		        	
			 } 
			 else {  
				 
				 dialog.getCrsMainPanel().getEsriPanel().selectedRowTable = lsm.getMinSelectionIndex();
				 dialog.getCrsMainPanel().getEsriPanel().setCodeCRS(Integer.parseInt((String)dialog.getCrsMainPanel().getEsriPanel().sorter.getValueAt(dialog.getCrsMainPanel().getEsriPanel().selectedRowTable,0)));
				 dialog.getCrsMainPanel().getEsriPanel().setWKT();
				 CrsWkt crs = new CrsWkt(dialog.getCrsMainPanel().getEsriPanel().getWKT());
				 String crs_kind = crs.getProjection();
				 for (int i = 0; i < not_soported.length; i++) {
					 if (crs_kind.equals(not_soported[i]))
		        		soported = false;					 
		        }
		        if (soported){
		        	dialog.setCode(dialog.getCrsMainPanel().getEsriPanel().getCodeCRS());
		        	dialog.getCrsMainPanel().getEsriPanel().getInfoCrs().setEnabled(true);
		        	dialog.getCrsMainPanel().getJButtonAccept().setEnabled(true);	
		        }
		        else {
		        	JOptionPane.showMessageDialog(dialog, PluginServices.getText(this,"crs_not_soported"), "Warning...",
							JOptionPane.WARNING_MESSAGE);
		        	dialog.getCrsMainPanel().getEsriPanel().setCodeCRS(-1);
		        	dialog.getCrsMainPanel().getEsriPanel().setCodeCRS(0);
		        	dialog.getCrsMainPanel().getEsriPanel().getInfoCrs().setEnabled(false);
		        	dialog.getCrsMainPanel().getJButtonAccept().setEnabled(false);	        		
		        }	        		            	
		     }
		}
		
		if (e.getSource() == dialog.getCrsMainPanel().getNewCrsPanel().getJTable().getSelectionModel()){
			ListSelectionModel lsm = (ListSelectionModel)e.getSource();
			if (lsm.isSelectionEmpty()) {
		        	dialog.getCrsMainPanel().getNewCrsPanel().selectedRowTable = -1;
		        	dialog.getCrsMainPanel().getNewCrsPanel().setCodeCRS(-1);
		        	dialog.getCrsMainPanel().getNewCrsPanel().getInfoCrs().setEnabled(false);		        	
		        	dialog.getCrsMainPanel().getJButtonAccept().setEnabled(false);
		        	dialog.getCrsMainPanel().getNewCrsPanel().getBtnEliminar().setEnabled(false);
		        	dialog.getCrsMainPanel().getNewCrsPanel().getBtnEditar().setEnabled(false);
		        	dialog.setCode(0);        	
		        	
			 } 
			 else {  
				 
				 dialog.getCrsMainPanel().getNewCrsPanel().selectedRowTable = lsm.getMinSelectionIndex();
				 dialog.getCrsMainPanel().getNewCrsPanel().setCodeCRS(Integer.parseInt((String)dialog.getCrsMainPanel().getNewCrsPanel().sorter.getValueAt(dialog.getCrsMainPanel().getNewCrsPanel().selectedRowTable,0)));
				 dialog.getCrsMainPanel().getNewCrsPanel().setWKT();
				 dialog.getCrsMainPanel().getNewCrsPanel().getInfoCrs().setEnabled(true);
				 dialog.getCrsMainPanel().getJButtonAccept().setEnabled(true);	
				 dialog.getCrsMainPanel().getNewCrsPanel().getBtnEliminar().setEnabled(true);
				 dialog.getCrsMainPanel().getNewCrsPanel().getBtnEditar().setEnabled(true);
		     }
		}
	}
		
	/**
	 * Controla los eventos relacionados con el doble click para
	 * la seleccin del CRS de la vista de las tablas del panel de
	 * seleccin de CRS
	 */	
	public void mouseClicked(MouseEvent e) {

		if (e.getSource() == dialog.getCrsMainPanel().getRecentsPanel().getJTable()){
			if (e.getClickCount() == 2){
				ICrs crs = (ICrs) dialog.getProjection();
				try {
					crs.getProj4String();
				} catch (CrsException e1) {
					JOptionPane.showMessageDialog(dialog, PluginServices.getText(this,e1.getMessage()), "Warning...",
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				dialog.setLastProj(crs);
				dialog.setDataSource(dialog.getCrsMainPanel().getDataSource());
	        	if (dialog.getLastProj() != null){
	        		/*
	        		 * Actualizar recientes...
	        		 */
	        		String authority = ((ICrs)dialog.getLastProj()).getCrsWkt().getAuthority()[0];	        		
	        		String name = ((ICrs)dialog.getLastProj()).getCrsWkt().getName();
	        		int code = ((ICrs)dialog.getLastProj()).getCode();
	        		CrsData crsData = new CrsData(authority,code,name);
	        		RecentCRSsPersistence persistence = new RecentCRSsPersistence();
	        		persistence.addCrsData(crsData);
	        		
	        		PluginServices.getMDIManager().closeWindow(dialog);
	        		dialog.setOkPressed(true);
				}
	        	else dialog.setOkPressed(false);	        	
		    }
		}
		
		if (e.getSource() == dialog.getCrsMainPanel().getEpsgPanel().getJTable()){
			if (e.getClickCount() == 2){
				ICrs crs = (ICrs) dialog.getProjection();
				try {
					crs.getProj4String();
				} catch (CrsException e1) {
					JOptionPane.showMessageDialog(dialog, PluginServices.getText(this,e1.getMessage()), "Warning...",
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				dialog.setLastProj(dialog.getProjection());
				dialog.setDataSource(dialog.getCrsMainPanel().getDataSource());
	        	if (dialog.getLastProj() != null){
	        		/*
	        		 * Actualizar recientes...
	        		 */
	        		String authority = ((ICrs)dialog.getLastProj()).getCrsWkt().getAuthority()[0];	        		
	        		String name = ((ICrs)dialog.getLastProj()).getCrsWkt().getName();
	        		int code = ((ICrs)dialog.getLastProj()).getCode();
	        		CrsData crsData = new CrsData(authority,code,name);
	        		RecentCRSsPersistence persistence = new RecentCRSsPersistence();
	        		persistence.addCrsData(crsData);
	        		
	        		PluginServices.getMDIManager().closeWindow(dialog);
	        		dialog.setOkPressed(true);
				}
	        	else dialog.setOkPressed(false);	        		
		    }
		}
		
		if (e.getSource() == dialog.getCrsMainPanel().getIauPanel().getJTable()){
			if (e.getClickCount() == 2){
				ICrs crs = (ICrs) dialog.getProjection();
				try {
					crs.getProj4String();
				} catch (CrsException e1) {
					JOptionPane.showMessageDialog(dialog, PluginServices.getText(this,e1.getMessage()), "Warning...",
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				dialog.setLastProj(dialog.getProjection());
				dialog.setDataSource(dialog.getCrsMainPanel().getDataSource());
	        	if (dialog.getLastProj() != null){
	        		/*
	        		 * Actualizar recientes...
	        		 */
	        		String authority = ((ICrs)dialog.getLastProj()).getCrsWkt().getAuthority()[0];	        		
	        		String name = ((ICrs)dialog.getLastProj()).getCrsWkt().getName();
	        		int code = ((ICrs)dialog.getLastProj()).getCode();
	        		CrsData crsData = new CrsData(authority,code,name);
	        		RecentCRSsPersistence persistence = new RecentCRSsPersistence();
	        		persistence.addCrsData(crsData);	        		
	        		
	        		PluginServices.getMDIManager().closeWindow(dialog);
	        		dialog.setOkPressed(true);
				}
	        	else dialog.setOkPressed(false);	        		
		    }
		}
		
		if (e.getSource() == dialog.getCrsMainPanel().getEsriPanel().getJTable()){
			if (e.getClickCount() == 2){
				ICrs crs = (ICrs) dialog.getProjection();
				try {
					crs.getProj4String();
				} catch (CrsException e1) {
					JOptionPane.showMessageDialog(dialog, PluginServices.getText(this,e1.getMessage()), "Warning...",
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				dialog.setLastProj(dialog.getProjection());
				dialog.setDataSource(dialog.getCrsMainPanel().getDataSource());
	        	if (dialog.getLastProj() != null){
	        		/*
	        		 * Actualizar recientes...
	        		 */
	        		String authority = ((ICrs)dialog.getLastProj()).getCrsWkt().getAuthority()[0];	        		
	        		String name = ((ICrs)dialog.getLastProj()).getCrsWkt().getName();
	        		int code = ((ICrs)dialog.getLastProj()).getCode();
	        		CrsData crsData = new CrsData(authority,code,name);
	        		RecentCRSsPersistence persistence = new RecentCRSsPersistence();
	        		persistence.addCrsData(crsData);	        		
	        		
	        		PluginServices.getMDIManager().closeWindow(dialog);
	        		dialog.setOkPressed(true);
				}
	        	else dialog.setOkPressed(false);	        		
		    }
		}
		
		if (e.getSource() == dialog.getCrsMainPanel().getNewCrsPanel().getJTable()){
			if (e.getClickCount() == 2){
				ICrs crs = (ICrs) dialog.getProjection();
				try {
					crs.getProj4String();
				} catch (CrsException e1) {
					JOptionPane.showMessageDialog(dialog, PluginServices.getText(this,e1.getMessage()), "Warning...",
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				dialog.setLastProj(dialog.getProjection());
				dialog.setDataSource(dialog.getCrsMainPanel().getDataSource());
	        	if (dialog.getLastProj() != null){
	        		/*
	        		 * Actualizar recientes...
	        		 */
	        		String authority = ((ICrs)dialog.getLastProj()).getCrsWkt().getAuthority()[0];	        		
	        		String name = ((ICrs)dialog.getLastProj()).getCrsWkt().getName();
	        		int code = ((ICrs)dialog.getLastProj()).getCode();
	        		CrsData crsData = new CrsData(authority,code,name);
	        		RecentCRSsPersistence persistence = new RecentCRSsPersistence();
	        		persistence.addCrsData(crsData);	        		
	        		
	        		PluginServices.getMDIManager().closeWindow(dialog);
	        		dialog.setOkPressed(true);
				}
	        	else dialog.setOkPressed(false);	        		
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
