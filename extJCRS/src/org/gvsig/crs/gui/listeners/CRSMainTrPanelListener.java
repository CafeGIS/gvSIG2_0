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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.gvsig.crs.CrsGT;
import org.gvsig.crs.CrsWkt;
import org.gvsig.crs.ICrs;
import org.gvsig.crs.gui.CRSMainTrPanel;
import org.gvsig.crs.gui.panels.InfoCRSPanel;
import org.gvsig.crs.ogr.TransEPSG;
import org.gvsig.crs.persistence.CompTrData;
import org.gvsig.crs.persistence.CrsData;
import org.gvsig.crs.persistence.RecentCRSsPersistence;
import org.gvsig.crs.persistence.RecentTrsPersistence;
import org.gvsig.crs.persistence.TrData;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;

import es.idr.teledeteccion.connection.Query;

/**
 * Esta clase contiene todos los listeners necesarios para el manejo 
 * de los eventos del panel de eleccin de CRS de la capa y de su
 * tranformacin. 
 * 
 * @author Diego Guerrero Sevilla (diego.guerrero@uclm.es) 
 * @author Jos Luis Gmez Martnez (jolugomar@gmail.com)
 *
 */

public class CRSMainTrPanelListener implements ActionListener, 
			ListSelectionListener, ItemListener, MouseListener, KeyListener{

	int[] valid_method_code = {9800, 9801, 9802, 9803, 9804, 9805, 9806, 9807, 9809, 9810, 
			9811, 9812, 9813, 9814, 9815, 9602, 9659, 9818, 9819, 9820, 9821, 9822, 9823, 
			9827, 9829};
	
	private CRSMainTrPanel panel = null;
		
	public CRSMainTrPanelListener(CRSMainTrPanel p) {
		panel=p;	
	}
	
	/**
	 * Manejador de los eventos relacionados con los botones del panel
	 * de transformacin y al combobox de las transformaciones
	 */
	public void actionPerformed(ActionEvent e) {
		if(	e.getSource() == panel.getJButtonAccept()){
			if (panel.isEpsg_tr()) {			
				TransEPSG epsgParams = new TransEPSG(panel.getEpsgTrPanel().getTransformation_code(),panel.getEpsgTrPanel().connect, panel.getEpsgTrPanel().inverseTranformation);
				panel.getEpsgTrPanel().setValues(epsgParams.getParamValue());	
				panel.setProjection(panel.getEpsgTrPanel().getProjection());
				panel.setEpsg_tr(false);
				
				/*
				 * Actualizar Transformaciones recientes
				 */
				String authority = "EPSG";
	    		String name = (String)panel.getEpsgTrPanel().sorter.getValueAt(panel.getEpsgTrPanel().selectedRowTable,1);
	    		int code = panel.getEpsgTrPanel().getTransformation_code();
	    		String crsSource = "EPSG:"+String.valueOf(panel.getEpsgTrPanel().getSource()); 	    			
	    		String crsTarget = "EPSG:"+String.valueOf(panel.getEpsgTrPanel().getTarget()); 	    			
	    		String details = (String)panel.getEpsgTrPanel().sorter.getValueAt(panel.getEpsgTrPanel().selectedRowTable,5);
	    		TrData trData = new TrData(authority,code,name,crsSource,crsTarget,details);
	    		RecentTrsPersistence trPersistence = new RecentTrsPersistence();
	    		trPersistence.addTrData(trData);
				
	    		
			}
			else if (panel.isManual_tr()) {
				boolean domain = panel.getManualTrPanel().correctJTextField();
				if (domain && panel.getManualTrPanel().correctDomain()){
					panel.setProjection(panel.getManualTrPanel().getProjection());
					panel.setManual_tr(false);
				}
				else if (!domain) {
					JOptionPane.showMessageDialog(panel, 
							PluginServices.getText(this,"numeric_format"), 
							"Warning...", JOptionPane.WARNING_MESSAGE);					
					return;
				}				
				else {
					JOptionPane.showMessageDialog(panel, 
							PluginServices.getText(this,"incorrect_domain"), 
							"Warning...", JOptionPane.WARNING_MESSAGE);					
					return;
				}
				
				/*
				 * Actualizar Transformaciones recientes
				 */
				String authority = "USR";
	    		String name = PluginServices.getText(this,"manual");
	    		int code = 0;
	    		String crsSource = panel.getManualTrPanel().getSourceAbrev();
	    		String crsTarget = panel.getManualTrPanel().getTargetAbrev();
	    		String details = panel.getManualTrPanel().getValues();
	    		 
	    		TrData trData = new TrData(authority,code,name,crsSource,crsTarget,details);
	    		RecentTrsPersistence trPersistence = new RecentTrsPersistence();
	    		trPersistence.addTrData(trData);
				
			}
			else if (panel.isNads_tr()) {
				panel.setProjection(panel.getNadsTrPanel().getProjection());
				panel.setTargetNad(panel.getNadsTrPanel().getNad());
				panel.setNads_tr(false);
				
				/*
				 * Actualizar Transformaciones recientes
				 */
				String authority = "NADGR";
	    		String name = "----";
	    		int code = 0;
	    		String crsSource = panel.getNadsTrPanel().getSourceAbrev();
	    		String crsTarget = panel.getNadsTrPanel().getTargetAbrev();
	    		String details = "";
	    		if (panel.getNadsTrPanel().getNad())
	    			details = panel.getNadsTrPanel().getNadFile()+ " ("+panel.getNadsTrPanel().getTargetAbrev() +")";
	    		else
	    			details = panel.getNadsTrPanel().getNadFile()+ " ("+panel.getNadsTrPanel().getSourceAbrev() +")"; 
	    		TrData trData = new TrData(authority,code,name,crsSource,crsTarget,details);
	    		RecentTrsPersistence trPersistence = new RecentTrsPersistence();
	    		trPersistence.addTrData(trData);
			}
			/*
			 * Ver que es necesario cuando aceptas en el panel de transformaciones
			 * recientes.
			 */
			else if (panel.isRecents_tr()) {
				String[] transformation = ((String)panel.getRecentsTrPanel().sorter.getValueAt(panel.getRecentsTrPanel().selectedRowTable,0)).split(":");
				if (transformation[0].equals("USR")){
	        		panel.getRecentsTrPanel().setParamsManual((String)panel.getRecentsTrPanel().sorter.getValueAt(panel.getRecentsTrPanel().selectedRowTable,4));
	        		panel.setProjection(panel.getRecentsTrPanel().getProjection());
	        	}
				if (transformation[0].equals("EPSG")){
					TransEPSG epsgParams = new TransEPSG(panel.getRecentsTrPanel().getTrCode(),panel.getRecentsTrPanel().connect, panel.getRecentsTrPanel().getInverseTransformation());
					panel.getRecentsTrPanel().setParamsEPGS(epsgParams.getParamValue());
					panel.setProjection(panel.getRecentsTrPanel().getProjection());
	        	}
				if (transformation[0].equals("NADGR")){
					panel.setProjection(panel.getRecentsTrPanel().getProjectionNad((String)panel.getRecentsTrPanel().sorter.getValueAt(panel.getRecentsTrPanel().selectedRowTable,4)));
				}
				if (transformation[0].equals("COMP")) {
					panel.setProjection(panel.getRecentsTrPanel().getProjectionComplex((String)panel.getRecentsTrPanel().sorter.getValueAt(panel.getRecentsTrPanel().selectedRowTable,4)));
				}
				//panel.setProjection(panel.getRecentsTrPanel().getProjection());
				panel.setRecents_tr(false);
				
				/*
				 * Actualizar Transformaciones recientes
				 */
				String authCode = (String)panel.getRecentsTrPanel().sorter.getValueAt(panel.getRecentsTrPanel().selectedRowTable,0);
				String authority = authCode.split(":")[0];
	    		String name = (String)panel.getRecentsTrPanel().sorter.getValueAt(panel.getRecentsTrPanel().selectedRowTable,1);
	    		int code = Integer.parseInt(authCode.split(":")[1]);
	    		String crsSource = (String)panel.getRecentsTrPanel().sorter.getValueAt(panel.getRecentsTrPanel().selectedRowTable,2);
	    		String crsTarget = (String)panel.getRecentsTrPanel().sorter.getValueAt(panel.getRecentsTrPanel().selectedRowTable,3);
	    		String details = (String)panel.getRecentsTrPanel().sorter.getValueAt(panel.getRecentsTrPanel().selectedRowTable,4);	    		
	    		TrData trData = new TrData(authority,code,name,crsSource,crsTarget,details);
	    		RecentTrsPersistence trPersistence = new RecentTrsPersistence();
	    		trPersistence.addTrData(trData);
			}else if(panel.isCompuesta_tr()){
				//Selecciona transformacin compuesta
				//Mostrar el panel de transformacin de la Capa
				panel.setProjection(panel.getVistaTrPanel().getProjection());
				//panel.setTargetNad(panel.getNadsTrPanel().getNad());
				panel.setCompuesta_tr(false);			
				panel.setVista_tr(false);
				
				/**
				 * para actualizar las transformaciones recientes, tendremos
				 * que coger las dos transformaciones utilizadas...
				 */
				
				/*					
				 * Actualizar Transformaciones recientes
				 */
				String authorityLayer = null;
	    		String nameLayer = null;
	    		int codeLayer = 0;
	    		String crsSourceLayer = null;
	    		String crsTargetLayer = null;
	    		String detailsLayer = "";
	    		
				if(panel.getCapaTrPanel().getJComboOptions().getSelectedItem().equals(PluginServices.getText(this,"recents_transformation"))){
					String[] transform = ((String)panel.getCapaTrPanel().getRecentsTrPanel().sorter.getValueAt(panel.getCapaTrPanel().getRecentsTrPanel().selectedRowTable,0)).split(":");
					
					authorityLayer = PluginServices.getText(this, transform[0]);						
					nameLayer = (String)panel.getCapaTrPanel().getRecentsTrPanel().sorter.getValueAt(panel.getCapaTrPanel().getRecentsTrPanel().selectedRowTable,1);
					crsSourceLayer = (String)panel.getCapaTrPanel().getRecentsTrPanel().sorter.getValueAt(panel.getCapaTrPanel().getRecentsTrPanel().selectedRowTable,2);
					crsTargetLayer = (String)panel.getCapaTrPanel().getRecentsTrPanel().sorter.getValueAt(panel.getCapaTrPanel().getRecentsTrPanel().selectedRowTable,3);
					detailsLayer = (String)panel.getCapaTrPanel().getRecentsTrPanel().sorter.getValueAt(panel.getCapaTrPanel().getRecentsTrPanel().selectedRowTable,4);
				}
				else if(panel.getCapaTrPanel().getJComboOptions().getSelectedItem().equals(PluginServices.getText(this,"transformacion_epsg"))){
					authorityLayer = "EPSG";
		    		nameLayer = (String)panel.getCapaTrPanel().getEpsgTrPanel().sorter.getValueAt(panel.getCapaTrPanel().getEpsgTrPanel().selectedRowTable,1);
		    		codeLayer = panel.getCapaTrPanel().getEpsgTrPanel().getTransformation_code();
		    		crsSourceLayer = "EPSG:"+String.valueOf(panel.getCapaTrPanel().getEpsgTrPanel().getSource()); 	    			
		    		crsTargetLayer = "EPSG:"+String.valueOf(panel.getCapaTrPanel().getEpsgTrPanel().getTarget()); 	    			
		    		detailsLayer = (String)panel.getCapaTrPanel().getEpsgTrPanel().sorter.getValueAt(panel.getCapaTrPanel().getEpsgTrPanel().selectedRowTable,5);
		    		
				}
				else if(panel.getCapaTrPanel().getJComboOptions().getSelectedItem().equals(PluginServices.getText(this,"transformacion_manual"))){
					authorityLayer = "USR";
		    		nameLayer = PluginServices.getText(this,"manual");
		    		codeLayer = 0;
		    		crsSourceLayer = panel.getCapaTrPanel().getManualTrPanel().getSourceAbrev();
		    		crsTargetLayer = panel.getCapaTrPanel().getManualTrPanel().getTargetAbrev();
		    		detailsLayer = panel.getCapaTrPanel().getManualTrPanel().getValues();
				}
				else if(panel.getCapaTrPanel().getJComboOptions().getSelectedItem().equals(PluginServices.getText(this,"nadgrids"))){
					authorityLayer = "NADGR";
		    		nameLayer = "----";
		    		codeLayer = 0;
		    		crsSourceLayer = panel.getCapaTrPanel().getNadsTrPanel().getSourceAbrev();
		    		crsTargetLayer = panel.getCapaTrPanel().getNadsTrPanel().getTargetAbrev();
		    		detailsLayer = panel.getCapaTrPanel().getNadsTrPanel().getNadFile()+ " ("+panel.getCapaTrPanel().getNadsTrPanel().getSourceAbrev() +")";
				}		    		 
	    		TrData trDataLayer = new TrData(authorityLayer,codeLayer,nameLayer,crsSourceLayer,crsTargetLayer,detailsLayer);
	    		
	    		String authorityView = null;
	    		String nameView = null;
	    		int codeView = 0;
	    		String crsSourceView = null;
	    		String crsTargetView = null;
	    		String detailsView = "";
	    		
				if(panel.getVistaTrPanel().getJComboOptions().getSelectedItem().equals(PluginServices.getText(this,"recents_transformation"))){
					String[] transform = ((String)panel.getVistaTrPanel().getRecentsTrPanel().sorter.getValueAt(panel.getVistaTrPanel().getRecentsTrPanel().selectedRowTable,0)).split(":");
					
					authorityView = PluginServices.getText(this, transform[0]);						
					nameView = (String)panel.getVistaTrPanel().getRecentsTrPanel().sorter.getValueAt(panel.getVistaTrPanel().getRecentsTrPanel().selectedRowTable,1);
					crsSourceView = (String)panel.getVistaTrPanel().getRecentsTrPanel().sorter.getValueAt(panel.getVistaTrPanel().getRecentsTrPanel().selectedRowTable,2);
					crsTargetView = (String)panel.getVistaTrPanel().getRecentsTrPanel().sorter.getValueAt(panel.getVistaTrPanel().getRecentsTrPanel().selectedRowTable,3);
					detailsView = (String)panel.getVistaTrPanel().getRecentsTrPanel().sorter.getValueAt(panel.getVistaTrPanel().getRecentsTrPanel().selectedRowTable,4);
				}
				else if(panel.getVistaTrPanel().getJComboOptions().getSelectedItem().equals(PluginServices.getText(this,"transformacion_epsg"))){
					authorityView = "EPSG";
		    		nameView = (String)panel.getVistaTrPanel().getEpsgTrPanel().sorter.getValueAt(panel.getVistaTrPanel().getEpsgTrPanel().selectedRowTable,1);
		    		codeView = panel.getVistaTrPanel().getEpsgTrPanel().getTransformation_code();
		    		crsSourceView = "EPSG:"+String.valueOf(panel.getVistaTrPanel().getEpsgTrPanel().getSource()); 	    			
		    		crsTargetView = "EPSG:"+String.valueOf(panel.getVistaTrPanel().getEpsgTrPanel().getTarget()); 	    			
		    		detailsView = (String)panel.getVistaTrPanel().getEpsgTrPanel().sorter.getValueAt(panel.getVistaTrPanel().getEpsgTrPanel().selectedRowTable,5);
		    		
				}
				else if(panel.getVistaTrPanel().getJComboOptions().getSelectedItem().equals(PluginServices.getText(this,"transformacion_manual"))){
					authorityView = "USR";
		    		nameView = PluginServices.getText(this,"manual");
		    		codeView = 0;
		    		crsSourceView = panel.getVistaTrPanel().getManualTrPanel().getSourceAbrev();
		    		crsTargetView = panel.getVistaTrPanel().getManualTrPanel().getTargetAbrev();
		    		detailsView = panel.getVistaTrPanel().getManualTrPanel().getValues();
				}
				else if(panel.getVistaTrPanel().getJComboOptions().getSelectedItem().equals(PluginServices.getText(this,"nadgrids"))){
					authorityView = "NADGR";
		    		nameView = "----";
		    		codeView = 0;
		    		crsSourceView = panel.getVistaTrPanel().getNadsTrPanel().getSourceAbrev();
		    		crsTargetView = panel.getVistaTrPanel().getNadsTrPanel().getTargetAbrev();
		    		detailsView = panel.getVistaTrPanel().getNadsTrPanel().getNadFile()+ " ("+panel.getVistaTrPanel().getNadsTrPanel().getTargetAbrev() +")";
		    	}		    		 
	    		TrData trDataView = new TrData(authorityView,codeView,nameView,crsSourceView,crsTargetView,detailsView);
	    		CompTrData comp = new CompTrData(trDataLayer, trDataView);
	    		RecentTrsPersistence trPersistence = new RecentTrsPersistence();
	    		trPersistence.addTrData(comp);
			}
			else{
				panel.setSin_tr(true);
				panel.setProjection(panel.getProjection());	
				/*
	    		 * Actualizar recientes...
	    		 */
	    		String authority = ((ICrs)panel.getProjection()).getCrsWkt().getAuthority()[0];
	    		String name = ((ICrs)panel.getProjection()).getCrsWkt().getName();
	    		int code = ((ICrs)panel.getProjection()).getCode();
	    		CrsData crsData = new CrsData(authority,code,name);
	    		RecentCRSsPersistence persistence = new RecentCRSsPersistence();
	    		persistence.addCrsData(crsData);
			}			
			
			/*
    		 * Actualizar recientes...
    		 
    		String authority = ((ICrs)panel.getProjection()).getCrsWkt().getAuthority()[0];
    		String name = ((ICrs)panel.getProjection()).getCrsWkt().getName();
    		int code = ((ICrs)panel.getProjection()).getCode();
    		CrsData crsData = new CrsData(authority,code,name);
    		RecentCRSsPersistence persistence = new RecentCRSsPersistence(RecentCRSsPersistence.pluginClassInstance);
    		persistence.addCrsData(crsData);*/
			
			PluginServices.getMDIManager().closeWindow(panel);
		}
		
		if(	e.getSource() == panel.getJButtonBefore()){
			CardLayout cl = (CardLayout)(panel.getJPanelMain().getLayout());		    
			if(panel.getNewSelection().equals(PluginServices.getText(this, "transformacion_compuesta")) && panel.isVista_tr()) {	
				cl.show(panel.getJPanelMain(), "capa");
				panel.setVista_tr(false);	        	
				panel.getJButtonNext().setEnabled(true);
				panel.getJButtonAccept().setEnabled(false);
			    panel.getJButtonBefore().setEnabled(true);
			} else {
				cl.show(panel.getJPanelMain(), "primero");
			    if (!panel.getDataSource().equals(PluginServices.getText(this,"recientes"))){
					panel.setEpsg_tr(false);
		        	panel.setNads_tr(false);
		        	panel.setManual_tr(false);
		        	panel.setRecents_tr(false);
		        	panel.setCapa_tr(false);
		        	panel.setVista_tr(false);		        	
			    }
			    /**
			     * mirar si el panel es el de recientes, el item seleccionado
			     * y actualizar estado de los botones...
			     */
			    else {
			    	panel.setEpsg_tr(false);
		        	panel.setNads_tr(false);
		        	panel.setManual_tr(false);
		        	panel.setRecents_tr(false);
		        	panel.setCapa_tr(false);
		        	panel.setVista_tr(false);
		        	int sel = panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().selectedRowTable;
		        	panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().getJTable().setRowSelectionInterval(0, 0);
		        	panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().getJTable().setRowSelectionInterval(sel, sel);
			    }
			    
			   //panel.getJButtonAccept().setEnabled(false);
	        	//panel.getJButtonNext().setEnabled(true);			    
			    panel.getJButtonBefore().setEnabled(false);
			}
		}
		
		if(	e.getSource() == panel.getJButtonCancel()){
			panel.setCancelYes(true);
			panel.setProjection(panel.getCrsfirst());
			PluginServices.getMDIManager().closeWindow(panel);
		}

		if(	e.getSource() == panel.getJButtonNext()){
			panel.getJButtonNext().setEnabled(false);
			//panel.getJButtonAccept().setEnabled(true);
			panel.getJButtonBefore().setEnabled(true);
			if (panel.getNewSelection().equals(PluginServices.getText(this,"transformacion_manual"))) {
				CardLayout cl = (CardLayout)(panel.getJPanelMain().getLayout());
				if (panel.getDataSource().equals(PluginServices.getText(this,"EPSG"))){
					panel.getManualTrPanel().setWKT(panel.getCrsMainPanel().getEpsgPanel().getWKT());
					panel.getManualTrPanel().setCode(panel.getCrsMainPanel().getEpsgPanel().epsg_code);
					panel.getManualTrPanel().setTargetAuthority(panel.getCrsWkt_target().getAuthority());
				}
				else if (panel.getDataSource().equals(PluginServices.getText(this,"recientes"))) {
					if (panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().getCrs() instanceof CrsGT) {
						panel.getManualTrPanel().setWKT(panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().getCrs());
					}
					else {
						panel.getManualTrPanel().setWKT(panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().getCrs().getWKT());
					}
					/**
					 * En el caso de haber elegido un crs con transformacion reciente
					 * vamos a cargar los datos.
					 */
					if (!((String)panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().sorter.getValueAt(panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().selectedRowTable, 3)).equals(PluginServices.getText(this, "sin_transformacion"))) {
						if (getCorrectOption().equals(PluginServices.getText(this, "USR")))
							panel.getManualTrPanel().fillData((String)panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().sorter.getValueAt(panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().selectedRowTable, 3));
						else
							panel.getManualTrPanel().resetData();
					} else
						panel.getManualTrPanel().resetData();
					panel.getManualTrPanel().setCode(panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().getCodeCRS());
					panel.getManualTrPanel().setTargetAuthority(panel.getCrsWkt_target().getAuthority());
				}
				else if (panel.getDataSource().equals(PluginServices.getText(this,"IAU2000"))) {
					panel.getManualTrPanel().setWKT(panel.getCrsMainPanel().getIauPanel().getWKT());
					panel.getManualTrPanel().setCode(panel.getCrsMainPanel().getIauPanel().getCodeCRS());
					panel.getManualTrPanel().setTargetAuthority(panel.getCrsWkt_target().getAuthority());
				}
				else if (panel.getDataSource().equals(PluginServices.getText(this,"ESRI"))) {
					panel.getManualTrPanel().setWKT(panel.getCrsMainPanel().getEsriPanel().getWKT());
					panel.getManualTrPanel().setCode(panel.getCrsMainPanel().getEsriPanel().getCodeCRS());
					panel.getManualTrPanel().setTargetAuthority(panel.getCrsWkt_target().getAuthority());
				}
				else if (panel.getDataSource().equals(PluginServices.getText(this,"newCRS"))) {
					panel.getManualTrPanel().setWKT(panel.getCrsMainPanel().getNewCrsPanel().getWKT());
					panel.getManualTrPanel().setCode(panel.getCrsMainPanel().getNewCrsPanel().getCodeCRS());
					panel.getManualTrPanel().setTargetAuthority(panel.getCrsWkt_target().getAuthority());
				}
				if(!panel.getManualTrPanel().getStatus()) {
					panel.getJButtonAccept().setEnabled(false);
				}
				panel.setManual_tr(true);
			    cl.show(panel.getJPanelMain(), "manual");		    
			}		
			else if(panel.getNewSelection().equals(PluginServices.getText(this,"nadgrids"))){
				CardLayout cl = (CardLayout)(panel.getJPanelMain().getLayout());
				if (panel.getDataSource().equals(PluginServices.getText(this,"EPSG"))){
					panel.getNadsTrPanel().setWKT(panel.getCrsMainPanel().getEpsgPanel().getWKT());
					panel.getNadsTrPanel().setSourceAbrev(PluginServices.getText(this,"EPSG"), ""+panel.getCrsMainPanel().getEpsgPanel().epsg_code);
					panel.getNadsTrPanel().setCode(panel.getCrsMainPanel().getEpsgPanel().epsg_code);
					panel.getNadsTrPanel().setTargetAuthority(panel.getCrsWkt_target().getAuthority());
				}
				else if (panel.getDataSource().equals(PluginServices.getText(this,"recientes"))) {
					panel.getNadsTrPanel().setWKT(panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().getCrs().getWKT());
					String sour = (String)panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().sorter.getValueAt(panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().selectedRowTable,0);		        	
					panel.getNadsTrPanel().setSourceAbrev(PluginServices.getText(this,sour), ""+panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().getCodeCRS());
					panel.getNadsTrPanel().setCode(panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().getCodeCRS());
					panel.getNadsTrPanel().setTargetAuthority(panel.getCrsWkt_target().getAuthority());
					/**
					 * En el caso de haber elegido un crs con transformacion reciente
					 * vamos a cargar los datos.
					 */
					if (!((String)panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().sorter.getValueAt(panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().selectedRowTable, 3)).equals(PluginServices.getText(this, "sin_transformacion"))) {
						if (getCorrectOption().equals(PluginServices.getText(this, "NADGR")))
							panel.getNadsTrPanel().fillData((String)panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().sorter.getValueAt(panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().selectedRowTable, 3));
						else
							panel.getNadsTrPanel().resetData();
					} else
						panel.getNadsTrPanel().resetData();
				}
				else if (panel.getDataSource().equals(PluginServices.getText(this,"IAU2000"))) {
					panel.getNadsTrPanel().setWKT(panel.getCrsMainPanel().getIauPanel().getWKT());
					panel.getNadsTrPanel().setSourceAbrev(PluginServices.getText(this,"IAU2000"), ""+panel.getCrsMainPanel().getIauPanel().getCodeCRS());
					panel.getNadsTrPanel().setCode(panel.getCrsMainPanel().getIauPanel().getCodeCRS());
					panel.getNadsTrPanel().setTargetAuthority(panel.getCrsWkt_target().getAuthority());
				}
				else if (panel.getDataSource().equals(PluginServices.getText(this,"ESRI"))) {
					panel.getNadsTrPanel().setWKT(panel.getCrsMainPanel().getEsriPanel().getWKT());
					panel.getNadsTrPanel().setSourceAbrev(PluginServices.getText(this,"ESRI"), ""+panel.getCrsMainPanel().getEsriPanel().getCodeCRS());
					panel.getNadsTrPanel().setCode(panel.getCrsMainPanel().getEsriPanel().getCodeCRS());
					panel.getNadsTrPanel().setTargetAuthority(panel.getCrsWkt_target().getAuthority());
				}
				else if (panel.getDataSource().equals(PluginServices.getText(this,"newCRS"))) {
					panel.getNadsTrPanel().setWKT(panel.getCrsMainPanel().getNewCrsPanel().getWKT());
					panel.getNadsTrPanel().setSourceAbrev(PluginServices.getText(this,"USR"), ""+panel.getCrsMainPanel().getNewCrsPanel().getCodeCRS());
					panel.getNadsTrPanel().setCode(panel.getCrsMainPanel().getNewCrsPanel().getCodeCRS());
					panel.getNadsTrPanel().setTargetAuthority(panel.getCrsWkt_target().getAuthority());
				}
				if (panel.getNadsTrPanel().getJComboNadFile().getSelectedIndex() == 0)
					panel.getJButtonAccept().setEnabled(false);
				else panel.getJButtonAccept().setEnabled(true);
				panel.setNads_tr(true);
			    cl.show(panel.getJPanelMain(), "nad");		    
			}
			else if(panel.getNewSelection().equals(PluginServices.getText(this,"transformacion_epsg"))){
				CardLayout cl = (CardLayout)(panel.getJPanelMain().getLayout());
				if (panel.getDataSource().equals(PluginServices.getText(this,"EPSG"))){
					panel.getEpsgTrPanel().setWKT(panel.getCrsMainPanel().getEpsgPanel().getWKT());
					panel.getEpsgTrPanel().setSource(PluginServices.getText(this,"EPSG"),panel.getCrsMainPanel().getEpsgPanel().epsg_code);
				}
				else if (panel.getDataSource().equals(PluginServices.getText(this,"recientes"))) {
					panel.getEpsgTrPanel().setWKT(panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().getCrs().getWKT());
					String sour = (String)panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().sorter.getValueAt(panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().selectedRowTable,0);		        	
					panel.getEpsgTrPanel().setSource(PluginServices.getText(this,sour),panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().getCodeCRS());
					/**
					 * En el caso de haber elegido un crs con transformacion reciente
					 * vamos a cargar los datos.
					 */
					if (!((String)panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().sorter.getValueAt(panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().selectedRowTable, 3)).equals(PluginServices.getText(this, "sin_transformacion"))) {
						if (getCorrectOption().equals(PluginServices.getText(this, "EPSG")))
							panel.getEpsgTrPanel().fillData((String)panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().sorter.getValueAt(panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().selectedRowTable, 3));
						else
							panel.getEpsgTrPanel().resetData();
					} else
						panel.getEpsgTrPanel().resetData();
				}
				else if (panel.getDataSource().equals(PluginServices.getText(this,"IAU2000"))) {
					panel.getEpsgTrPanel().setWKT(panel.getCrsMainPanel().getIauPanel().getWKT());
					panel.getEpsgTrPanel().setSource(PluginServices.getText(this,"IAU2000"),panel.getCrsMainPanel().getIauPanel().getCodeCRS());					
				}
				else if (panel.getDataSource().equals(PluginServices.getText(this,"ESRI"))) {
					panel.getEpsgTrPanel().setWKT(panel.getCrsMainPanel().getEsriPanel().getWKT());
					panel.getEpsgTrPanel().setSource(PluginServices.getText(this,"ESRI"),panel.getCrsMainPanel().getEsriPanel().getCodeCRS());					
				}
				else if (panel.getDataSource().equals(PluginServices.getText(this,"newCRS"))) {
					panel.getEpsgTrPanel().setWKT(panel.getCrsMainPanel().getNewCrsPanel().getWKT());
					panel.getEpsgTrPanel().setSource(PluginServices.getText(this,"USR"),panel.getCrsMainPanel().getNewCrsPanel().getCodeCRS());					
				}
				
				int numr = panel.getEpsgTrPanel().dtm.getRowCount();
				if (numr == 0 )
					panel.getJButtonAccept().setEnabled(false);
				panel.setEpsg_tr(true);				
			    cl.show(panel.getJPanelMain(), "epsg");		    			
			}
			/*
			 * Parte necesaria para la transformacion reciente, cuando se vaya a cargar
			 * el panel, tendremos que ver que hay que pasarle.
			 */
			else if(panel.getNewSelection().equals(PluginServices.getText(this, "recents_transformation"))) {
				CardLayout cl = (CardLayout)(panel.getJPanelMain().getLayout());
				if (panel.getDataSource().equals(PluginServices.getText(this,"EPSG"))){
					panel.getRecentsTrPanel().setWKT(panel.getCrsMainPanel().getEpsgPanel().getWKT());
					panel.getRecentsTrPanel().loadRecents(PluginServices.getText(this,"EPSG")+":"+panel.getCrsMainPanel().getEpsgPanel().getCodeCRS(),panel.getCrsWkt_target().getAuthority()[0]+":"+panel.getCrsWkt_target().getAuthority()[1]);
					//panel.getRecentsTrPanel().setTargetAuthority(panel.getCrsWkt_target().getAuthority());
				}
				else if (panel.getDataSource().equals(PluginServices.getText(this,"recientes"))) {
					panel.getRecentsTrPanel().setWKT(panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().getCrs().getWKT());
					String sour = (String)panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().sorter.getValueAt(panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().selectedRowTable,0);		        	
					panel.getRecentsTrPanel().loadRecents(PluginServices.getText(this,sour)+":"+panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().getCodeCRS(),panel.getCrsWkt_target().getAuthority()[0]+":"+panel.getCrsWkt_target().getAuthority()[1]);
					//panel.getRecentsTrPanel().setTargetAuthority(panel.getCrsWkt_target().getAuthority());
				}
				else if (panel.getDataSource().equals(PluginServices.getText(this,"IAU2000"))) {
					panel.getRecentsTrPanel().setWKT(panel.getCrsMainPanel().getIauPanel().getWKT());
					panel.getRecentsTrPanel().loadRecents(PluginServices.getText(this,"IAU2000")+":"+panel.getCrsMainPanel().getIauPanel().getCodeCRS(),panel.getCrsWkt_target().getAuthority()[0]+":"+panel.getCrsWkt_target().getAuthority()[1]);					
				}
				else if (panel.getDataSource().equals(PluginServices.getText(this,"ESRI"))) {
					panel.getRecentsTrPanel().setWKT(panel.getCrsMainPanel().getEsriPanel().getWKT());
					panel.getRecentsTrPanel().loadRecents(PluginServices.getText(this,"ESRI")+":"+panel.getCrsMainPanel().getEsriPanel().getCodeCRS(),panel.getCrsWkt_target().getAuthority()[0]+":"+panel.getCrsWkt_target().getAuthority()[1]);					
				}
				else if (panel.getDataSource().equals(PluginServices.getText(this,"newCRS"))) {
					panel.getRecentsTrPanel().setWKT(panel.getCrsMainPanel().getNewCrsPanel().getWKT());
					panel.getRecentsTrPanel().loadRecents(PluginServices.getText(this,"USR")+":"+panel.getCrsMainPanel().getNewCrsPanel().getCodeCRS(),panel.getCrsWkt_target().getAuthority()[0]+":"+panel.getCrsWkt_target().getAuthority()[1]);					
				}
				
				int numr = panel.getRecentsTrPanel().dtm.getRowCount();
				if (numr == 0 )
					panel.getJButtonAccept().setEnabled(false);
				panel.setRecents_tr(true);				
			    cl.show(panel.getJPanelMain(), "recents");
			}
			//Si se selecciona la opcin de transformacin compuesta
			else if(panel.getNewSelection().equals(PluginServices.getText(this, "transformacion_compuesta"))) {
				//vamos a definir los crs fuentes para los dos paneles para
				//las transformaciones de la capa y la vista
				if (!panel.isCapa_tr()) {
					if (panel.getDataSource().equals(PluginServices.getText(this,"EPSG"))){
						panel.getCapaTrPanel().setCrs_source(PluginServices.getText(this,"EPSG")+":"+panel.getCrsMainPanel().getEpsgPanel().epsg_code);
						panel.getVistaTrPanel().setCrs_source(PluginServices.getText(this,"EPSG")+":"+panel.getCrsMainPanel().getEpsgPanel().epsg_code);
						panel.getCapaTrPanel().fillData();
						panel.getVistaTrPanel().fillData();
					}
					else if (panel.getDataSource().equals(PluginServices.getText(this,"recientes"))) {
						String sour = (String)panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().sorter.getValueAt(panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().selectedRowTable,0);					
						panel.getCapaTrPanel().setCrs_source(PluginServices.getText(this,sour)+":"+panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().getCodeCRS());
						panel.getVistaTrPanel().setCrs_source(PluginServices.getText(this,sour)+":"+panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().getCodeCRS());
						panel.getCapaTrPanel().fillData();
						panel.getVistaTrPanel().fillData();
						/**
						 * En el caso de haber elegido un crs con transformacion reciente
						 * vamos a cargar los datos.
						 */
						if (!((String)panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().sorter.getValueAt(panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().selectedRowTable, 3)).equals(PluginServices.getText(this, "sin_transformacion"))) {
							if (getCorrectOption().equals(PluginServices.getText(this, "COMP"))) {
								panel.getCapaTrPanel().fillData((String)panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().sorter.getValueAt(panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().selectedRowTable, 3));
								panel.getVistaTrPanel().fillData((String)panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().sorter.getValueAt(panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().selectedRowTable, 3));
								panel.getJButtonAccept().setEnabled(true);								
							} else {
								panel.getCapaTrPanel().resetData();
								panel.getVistaTrPanel().resetData();
							}							
						} else {
							panel.getCapaTrPanel().resetData();
							panel.getVistaTrPanel().resetData();
						}
					}
					else if (panel.getDataSource().equals(PluginServices.getText(this,"IAU2000"))) {
						panel.getCapaTrPanel().setCrs_source(PluginServices.getText(this,"IAU2000")+":"+panel.getCrsMainPanel().getIauPanel().getCodeCRS());
						panel.getVistaTrPanel().setCrs_source(PluginServices.getText(this,"IAU2000")+":"+panel.getCrsMainPanel().getIauPanel().getCodeCRS());
						panel.getCapaTrPanel().fillData();
						panel.getVistaTrPanel().fillData();
					}
					else if (panel.getDataSource().equals(PluginServices.getText(this,"ESRI"))) {
						panel.getCapaTrPanel().setCrs_source(PluginServices.getText(this,"ESRI")+":"+panel.getCrsMainPanel().getEsriPanel().getCodeCRS());
						panel.getVistaTrPanel().setCrs_source(PluginServices.getText(this,"ESRI")+":"+panel.getCrsMainPanel().getEsriPanel().getCodeCRS());
						panel.getCapaTrPanel().fillData();
						panel.getVistaTrPanel().fillData();
					}
					else if (panel.getDataSource().equals(PluginServices.getText(this,"newCRS"))) {
						panel.getCapaTrPanel().setCrs_source(PluginServices.getText(this,"USR")+":"+panel.getCrsMainPanel().getNewCrsPanel().getCodeCRS());
						panel.getVistaTrPanel().setCrs_source(PluginServices.getText(this,"USR")+":"+panel.getCrsMainPanel().getNewCrsPanel().getCodeCRS());
						panel.getCapaTrPanel().fillData();
						panel.getVistaTrPanel().fillData();
					}		
				}
				
				if(panel.isCapa_tr()){
					//En este caso se esta mostrando el panel de seleccion de la transformacin de la capa
					panel.setVista_tr(true);					
					panel.getVistaTrPanel().setSourceTransformation(panel.getCapaTrPanel().getSourceTransformation());
					CardLayout cl = (CardLayout)(panel.getJPanelMain().getLayout());
					cl.show(panel.getJPanelMain(), "vista");
					panel.getJButtonNext().setEnabled(false);
					if (panel.getVistaTrPanel().isSthSelected())
						panel.getJButtonAccept().setEnabled(true);
				}else{					
					if (panel.getCapaTrPanel().getRecentCompTransformation()) {
						panel.getJButtonAccept().setEnabled(true);
						panel.getJButtonNext().setEnabled(true);
					}
					else {
						panel.getJButtonAccept().setEnabled(false);
						if (panel.getCapaTrPanel().isSthSelected())
							panel.getJButtonNext().setEnabled(true);
					}
					panel.setCompuesta_tr(true);
					panel.setEpsg_tr(false);
					panel.setNads_tr(false);
					panel.setManual_tr(false);
					panel.setRecents_tr(false);
					panel.setCapa_tr(true);					
					CardLayout cl = (CardLayout)(panel.getJPanelMain().getLayout());
					cl.show(panel.getJPanelMain(), "capa");
				}
			}
			
		}	
		if(	e.getSource() == panel.getJComboOptions()){
			JComboBox cb = (JComboBox)e.getSource();
			panel.setNewSelection((String)cb.getSelectedItem());
		    if (panel.getNewSelection().equals(PluginServices.getText(this,"sin_transformacion"))){
				panel.getJButtonAccept().setEnabled(true);
				panel.getJButtonNext().setEnabled(false);
				panel.setEpsg_tr(false);
				panel.setNads_tr(false);
				panel.setManual_tr(false);
				panel.setRecents_tr(false);
				panel.setCapa_tr(false);
				panel.setCompuesta_tr(false);
				panel.setVista_tr(false);
			}
		    else {
		    	if (panel.getNewSelection().equals(PluginServices.getText(this,"recents_transformation"))) {
		    		panel.setEpsg_tr(false);
					panel.setNads_tr(false);
					panel.setManual_tr(false);
					panel.setRecents_tr(true);
					panel.setCapa_tr(false);
					panel.setCompuesta_tr(false);
					panel.setVista_tr(false);
		    	}
		    	else if (panel.getNewSelection().equals(PluginServices.getText(this,"transformacion_epsg"))) {
		    		panel.setEpsg_tr(true);
					panel.setNads_tr(false);
					panel.setManual_tr(false);
					panel.setRecents_tr(false);
					panel.setCapa_tr(false);
					panel.setCompuesta_tr(false);
					panel.setVista_tr(false);
		    	}
		    	else if (panel.getNewSelection().equals(PluginServices.getText(this,"transformacion_manual"))) {
		    		panel.setEpsg_tr(false);
					panel.setNads_tr(false);
					panel.setManual_tr(true);
					panel.setRecents_tr(false);
					panel.setCapa_tr(false);
					panel.setCompuesta_tr(false);
					panel.setVista_tr(false);
		    	}
		    	else if (panel.getNewSelection().equals(PluginServices.getText(this,"transformacion_compuesta"))) {
		    		panel.setEpsg_tr(false);
					panel.setNads_tr(false);
					panel.setManual_tr(false);
					panel.setRecents_tr(false);
					panel.setCapa_tr(false);
					panel.setCompuesta_tr(true);
					panel.setVista_tr(false);
		    	}
		    	else if (panel.getNewSelection().equals(PluginServices.getText(this,"nadgrids"))) {
		    		panel.setEpsg_tr(false);
					panel.setNads_tr(true);
					panel.setManual_tr(false);
					panel.setRecents_tr(false);
					panel.setCapa_tr(false);
					panel.setCompuesta_tr(false);
					panel.setVista_tr(false);
		    	}
		    	panel.getJButtonAccept().setEnabled(false);
		    	panel.getJButtonNext().setEnabled(true);
		    }
		}
		if(	e.getSource() == panel.getRecentsTrPanel().getJButtonInfo()){
			String[] data = new String[5];
			data[0] = (String)panel.getRecentsTrPanel().sorter.getValueAt(panel.getRecentsTrPanel().selectedRowTable,0);
			data[1] = (String)panel.getRecentsTrPanel().sorter.getValueAt(panel.getRecentsTrPanel().selectedRowTable,1);
			data[2] = (String)panel.getRecentsTrPanel().sorter.getValueAt(panel.getRecentsTrPanel().selectedRowTable,2);
			data[3] = (String)panel.getRecentsTrPanel().sorter.getValueAt(panel.getRecentsTrPanel().selectedRowTable,3);
			data[4] = (String)panel.getRecentsTrPanel().sorter.getValueAt(panel.getRecentsTrPanel().selectedRowTable,4);
			
			//Mostrar el panel de informacin de las transformaciones recientes
			//InfoTransformationsRecentsPanel info = new InfoTransformationsRecentsPanel(data);
			InfoCRSPanel info = new InfoCRSPanel(data[2].split(":")[0], Integer.parseInt(data[2].split(":")[1]) , data[0]+" <--> "+ data[4]);
			PluginServices.getMDIManager().addWindow(info);
		}
	}
	
	/**
	 * Metodo que nos servira para conocer si tras la seleccion de crs mas la
	 * transformacion, hemos decidido cambiar de transformacion
	 * @return
	 */
	private String getCorrectOption (){
		String item = ((String)panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().sorter.getValueAt(panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().selectedRowTable, 3));
		if (item.startsWith(PluginServices.getText(this, "EPSG"))) {
			return "EPSG";
		}
		else if (item.startsWith(PluginServices.getText(this, "NADGR"))) {
			return "NADGR";
		}
		else if (item.startsWith(PluginServices.getText(this, "USR"))) {
			return "USR";
		}
		else if (item.startsWith(PluginServices.getText(this, "COMP"))) {
			return "COMP";
		}
		return "";
	}

	/**
	 * Manejador de los eventos relacionados al cambio de valor
	 * dentro de las tablas del panel de tranformaciones.
	 */
	public void valueChanged(ListSelectionEvent e) {
		if (e.getSource() == panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().getJTable().getSelectionModel()){
			ListSelectionModel lsm = (ListSelectionModel)e.getSource();
	        if (lsm.isSelectionEmpty()) {
	        	panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().selectedRowTable = -1;
	        	panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().setCodeCRS(-1);
	        	panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().getInfoCrs().setEnabled(false);
	        	panel.getJButtonAccept().setEnabled(false);		        	
	        	panel.getJComboOptions().setEnabled(false); 
	        	panel.getJButtonNext().setEnabled(false);
	        	panel.getJComboOptions().setSelectedIndex(0);	        	
	        } 
	        else {
	        	panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().initCrs();		        			        	
	        	panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().getInfoCrs().setEnabled(true);
	        	if (((String)panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().sorter.getValueAt(panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().selectedRowTable, 3)).equals(PluginServices.getText(this, "sin_transformacion"))) {
	        		if (panel.getJComboOptions().getSelectedIndex() == 0) {
	        			panel.getJButtonAccept().setEnabled(true);
	        			panel.getJButtonNext().setEnabled(false);
	        		} else{
	        			panel.getJButtonAccept().setEnabled(false);
	        			panel.getJButtonNext().setEnabled(true);
	        		}
	        	} else {
	        		String option = getCorrectItem();
	        		if (option.equals("USR")) {
	        			if (panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().getCrs() instanceof CrsGT) {
							panel.getManualTrPanel().setWKT(panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().getCrs());
						}
						else {
							panel.getManualTrPanel().setWKT(panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().getCrs().getWKT());
						}						
						panel.getManualTrPanel().setCode(panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().getCodeCRS());
						panel.getManualTrPanel().setTargetAuthority(panel.getCrsWkt_target().getAuthority());
					
	        			/**
	        			 * Rellenamos los datos con anterioridad para si acepta poder
	        			 * reutilizar el mecanismo que ya tenemos.
	        			 */
						panel.getManualTrPanel().fillData((String)panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().sorter.getValueAt(panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().selectedRowTable, 3));
						panel.setManual_tr(true);
						panel.setEpsg_tr(false);
						panel.setNads_tr(false);
						panel.setRecents_tr(false);
						panel.setCompuesta_tr(false);
					} else if (option.equals("EPSG")) {
						panel.getEpsgTrPanel().setWKT(panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().getCrs().getWKT());
						String sour = (String)panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().sorter.getValueAt(panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().selectedRowTable,0);		        	
						panel.getEpsgTrPanel().setSource(PluginServices.getText(this,sour),panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().getCodeCRS());
						
						panel.getEpsgTrPanel().fillData((String)panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().sorter.getValueAt(panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().selectedRowTable, 3));
						
						panel.setManual_tr(false);
						panel.setEpsg_tr(true);
						panel.setNads_tr(false);
						panel.setRecents_tr(false);
						panel.setCompuesta_tr(false);
					} else if (option.equals("NADGR")) {
						panel.getNadsTrPanel().setWKT(panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().getCrs().getWKT());
						String sour = (String)panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().sorter.getValueAt(panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().selectedRowTable,0);		        	
						panel.getNadsTrPanel().setSourceAbrev(PluginServices.getText(this,sour), ""+panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().getCodeCRS());
						panel.getNadsTrPanel().setCode(panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().getCodeCRS());
						panel.getNadsTrPanel().setTargetAuthority(panel.getCrsWkt_target().getAuthority());
						
						panel.getNadsTrPanel().fillData((String)panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().sorter.getValueAt(panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().selectedRowTable, 3));
						
						panel.setManual_tr(false);
						panel.setEpsg_tr(false);
						panel.setNads_tr(true);
						panel.setRecents_tr(false);
						panel.setCompuesta_tr(false);
						
					} else if (option.equals("COMP")) {
						String sour = (String)panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().sorter.getValueAt(panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().selectedRowTable,0);					
						panel.getCapaTrPanel().setCrs_source(PluginServices.getText(this,sour)+":"+panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().getCodeCRS());
						panel.getVistaTrPanel().setCrs_source(PluginServices.getText(this,sour)+":"+panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().getCodeCRS());
						panel.getCapaTrPanel().fillData();
						panel.getVistaTrPanel().fillData();
						panel.getCapaTrPanel().fillData((String)panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().sorter.getValueAt(panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().selectedRowTable, 3));
						panel.getVistaTrPanel().fillData((String)panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().sorter.getValueAt(panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().selectedRowTable, 3));
						panel.setManual_tr(false);
						panel.setEpsg_tr(false);
						panel.setNads_tr(false);
						panel.setRecents_tr(false);
						panel.setCompuesta_tr(true);
						
					}
	        		panel.getJButtonNext().setEnabled(true);
	        		panel.getJButtonAccept().setEnabled(true);
	        	}
	        	panel.getJComboOptions().setEnabled(true);
        		 		
	        }
		}
		
		if (e.getSource() == panel.getCrsMainPanel().getEpsgPanel().getJTable().getSelectionModel()){
			String[] not_soported = {"engineering", "vertical", "compound", "geocentric", "geographic 3D"};
			boolean soported = true;
			boolean soported2 = false;
			ListSelectionModel lsm = (ListSelectionModel)e.getSource();
			if (lsm.isSelectionEmpty()) {
				panel.getCrsMainPanel().getEpsgPanel().selectedRowTable = -1;
				panel.getCrsMainPanel().getEpsgPanel().setCodeCRS(-1);
	        	panel.getJButtonAccept().setEnabled(false);
	        	panel.getCrsMainPanel().getEpsgPanel().getInfoCrs().setEnabled(false);
	        	panel.getJComboOptions().setEnabled(false);
	        	panel.getJButtonNext().setEnabled(false);
	        	panel.getJComboOptions().setSelectedIndex(0);	        
			}
			else {
				panel.getCrsMainPanel().getEpsgPanel().selectedRowTable = lsm.getMinSelectionIndex();	        	
	        	String crs_kind = (String)panel.getCrsMainPanel().getEpsgPanel().sorter.getValueAt(panel.getCrsMainPanel().getEpsgPanel().selectedRowTable,2);
	        	for (int i = 0; i < not_soported.length; i++) {
	        		if (crs_kind.equals(not_soported[i])) 
	        			soported = false;	        		
	        	}
	        	for (int i=0; i< valid_method_code.length;i++) {
	        		if (panel.getCrsMainPanel().getEpsgPanel().getProjectionCode((String)panel.getCrsMainPanel().getEpsgPanel().sorter.getValueAt(panel.getCrsMainPanel().getEpsgPanel().selectedRowTable,0)) == valid_method_code[i]) {
	        			soported2 = true;
	        		}
	        	}
	        	if (crs_kind.equals("geographic 2D")) {
	        		soported2 = true;
	        	}
	        	if (soported && soported2){
	        		panel.getCrsMainPanel().getEpsgPanel().setCodeCRS(Integer.parseInt((String)panel.getCrsMainPanel().getEpsgPanel().sorter.getValueAt(panel.getCrsMainPanel().getEpsgPanel().selectedRowTable,0)));
	        		panel.getCrsMainPanel().getEpsgPanel().setWKT();        		
	        		panel.getJButtonAccept().setEnabled(true);
	            	int base = panel.getCrsMainPanel().getEpsgPanel().getCodeCRS();						
					//panel.compareDatum(panel.getCrsMainPanel().getEpsgPanel().getWKT());
	            	IWindow activeWindow = PluginServices.getMDIManager().getActiveWindow();
			    	BaseView activeView = (BaseView) activeWindow;
			    	String authority = ((ICrs) activeView.getMapControl().getProjection()).getCrsWkt().getAuthority()[0];
	            	/*if (authority.equals("EPSG")){
	            		panel.getJComboOptions().setEnabled(true);
	            		panel.getJButtonAccept().setEnabled(true);
	            	}
	            	else {
		        		panel.getJComboOptions().setSelectedIndex(0);	        		
		        		panel.getJComboOptions().setEnabled(false);
		        		panel.getJButtonAccept().setEnabled(true);
		        		panel.getJButtonAccept().setEnabled(true);	        			        		
		        		panel.getJButtonNext().setEnabled(false);
		        	}	*/  
			    	panel.getJComboOptions().setEnabled(true);
            		panel.getJButtonAccept().setEnabled(true);
	        		panel.getCrsMainPanel().getEpsgPanel().getInfoCrs().setEnabled(true);
	        	}
	        	else {
	        		JOptionPane.showMessageDialog(panel, PluginServices.getText(this,"crs_not_soported"), "Warning...",
							JOptionPane.WARNING_MESSAGE);
	        		panel.getCrsMainPanel().getEpsgPanel().setCodeCRS(-1);
	        		panel.getCrsMainPanel().getEpsgPanel().setCodeCRS(0);
	        		panel.getJButtonAccept().setEnabled(false);
	        		panel.getCrsMainPanel().getEpsgPanel().getInfoCrs().setEnabled(false);
	        	}	        				        	  
		    } 
		}
		
		if (e.getSource() == panel.getCrsMainPanel().getIauPanel().getJTable().getSelectionModel()){
			ListSelectionModel lsm = (ListSelectionModel)e.getSource();				
			String[] not_soported = {"Oblique_Cylindrical_Equal_Area"};
			boolean soported = true;
	        if (lsm.isSelectionEmpty()) {
	        	panel.getCrsMainPanel().getIauPanel().selectedRowTable = -1;
	        	panel.getCrsMainPanel().getIauPanel().setCodeCRS(-1);
	        	panel.getCrsMainPanel().getIauPanel().getInfoCrs().setEnabled(false);
	        	panel.getJButtonAccept().setEnabled(false);		        	
	        	panel.getJComboOptions().setEnabled(false);     
	        	panel.getJButtonNext().setEnabled(false);
	        	panel.getJComboOptions().setSelectedIndex(0);	        	
	        } 
	        else {        	
	        	panel.getCrsMainPanel().getIauPanel().selectedRowTable = lsm.getMinSelectionIndex();
	        	panel.getCrsMainPanel().getIauPanel().setCodeCRS(Integer.parseInt((String)panel.getCrsMainPanel().getIauPanel().sorter.getValueAt(panel.getCrsMainPanel().getIauPanel().selectedRowTable,0)));
	        	panel.getCrsMainPanel().getIauPanel().setWKT();
	        	CrsWkt crs = new CrsWkt(panel.getCrsMainPanel().getIauPanel().getWKT());
	        	String crs_kind = crs.getProjection();
	        	for (int i = 0; i < not_soported.length; i++) {
	        		if (crs_kind.equals(not_soported[i]))
	        			soported = false;	        		
	        	}
	        	if (soported){
	        		panel.getCrsMainPanel().getIauPanel().getInfoCrs().setEnabled(true);
	        		panel.getJComboOptions().setEnabled(true);
	        		panel.getJButtonAccept().setEnabled(true);	
	        	}
	        	else {
	        		JOptionPane.showMessageDialog(panel, PluginServices.getText(this,"crs_not_soported"), "Warning...",
							JOptionPane.WARNING_MESSAGE);
	        		panel.getCrsMainPanel().getIauPanel().setCodeCRS(-1);
	        		panel.getCrsMainPanel().getIauPanel().setCodeCRS(0);
	        		panel.getCrsMainPanel().getIauPanel().getInfoCrs().setEnabled(false);
	        		panel.getJComboOptions().setEnabled(false);	        		
	        		panel.getJButtonAccept().setEnabled(false);	        		
	        	}	        		            	
	        }
		}
		
		if (e.getSource() == panel.getCrsMainPanel().getEsriPanel().getJTable().getSelectionModel()){
			ListSelectionModel lsm = (ListSelectionModel)e.getSource();				
			String[] not_soported = {"Oblique_Cylindrical_Equal_Area"};
			boolean soported = true;
	        if (lsm.isSelectionEmpty()) {
	        	panel.getCrsMainPanel().getEsriPanel().selectedRowTable = -1;
	        	panel.getCrsMainPanel().getEsriPanel().setCodeCRS(-1);
	        	panel.getCrsMainPanel().getEsriPanel().getInfoCrs().setEnabled(false);
	        	panel.getJButtonAccept().setEnabled(false);		        	
	        	panel.getJComboOptions().setEnabled(false);     
	        	panel.getJButtonNext().setEnabled(false);
	        	panel.getJComboOptions().setSelectedIndex(0);	        	
	        } 
	        else {        	
	        	panel.getCrsMainPanel().getEsriPanel().selectedRowTable = lsm.getMinSelectionIndex();
	        	panel.getCrsMainPanel().getEsriPanel().setCodeCRS(Integer.parseInt((String)panel.getCrsMainPanel().getEsriPanel().sorter.getValueAt(panel.getCrsMainPanel().getEsriPanel().selectedRowTable,0)));
	        	panel.getCrsMainPanel().getEsriPanel().setWKT();
	        	CrsWkt crs = new CrsWkt(panel.getCrsMainPanel().getEsriPanel().getWKT());
	        	String crs_kind = crs.getProjection();
	        	for (int i = 0; i < not_soported.length; i++) {
	        		if (crs_kind.equals(not_soported[i]))
	        			soported = false;	        		
	        	}
	        	if (soported){
	        		panel.getCrsMainPanel().getEsriPanel().getInfoCrs().setEnabled(true);
	        		panel.getJComboOptions().setEnabled(true);
	        		panel.getJButtonAccept().setEnabled(true);	
	        	}
	        	else {
	        		JOptionPane.showMessageDialog(panel, PluginServices.getText(this,"crs_not_soported"), "Warning...",
							JOptionPane.WARNING_MESSAGE);
	        		panel.getCrsMainPanel().getEsriPanel().setCodeCRS(-1);
	        		panel.getCrsMainPanel().getEsriPanel().setCodeCRS(0);
	        		panel.getCrsMainPanel().getEsriPanel().getInfoCrs().setEnabled(false);
	        		panel.getJComboOptions().setEnabled(false);	        		
	        		panel.getJButtonAccept().setEnabled(false);	        		
	        	}	        		            	
	        }
		}
		
		if (e.getSource() == panel.getCrsMainPanel().getNewCrsPanel().getJTable().getSelectionModel()){
			ListSelectionModel lsm = (ListSelectionModel)e.getSource();				
			if (lsm.isSelectionEmpty()) {
	        	panel.getCrsMainPanel().getNewCrsPanel().selectedRowTable = -1;
	        	panel.getCrsMainPanel().getNewCrsPanel().setCodeCRS(-1);
	        	panel.getCrsMainPanel().getNewCrsPanel().getInfoCrs().setEnabled(false);
	        	panel.getCrsMainPanel().getNewCrsPanel().getBtnEditar().setEnabled(false);
	        	panel.getCrsMainPanel().getNewCrsPanel().getBtnEliminar().setEnabled(false);
	        	panel.getJButtonAccept().setEnabled(false);		        	
	        	panel.getJComboOptions().setEnabled(false);     
	        	panel.getJButtonNext().setEnabled(false);
	        	panel.getJComboOptions().setSelectedIndex(0);	        	
	        } 
	        else {        	
	        	panel.getCrsMainPanel().getNewCrsPanel().selectedRowTable = lsm.getMinSelectionIndex();
	        	panel.getCrsMainPanel().getNewCrsPanel().setCodeCRS(Integer.parseInt((String)panel.getCrsMainPanel().getNewCrsPanel().sorter.getValueAt(panel.getCrsMainPanel().getNewCrsPanel().selectedRowTable,0)));
	        	panel.getCrsMainPanel().getNewCrsPanel().getInfoCrs().setEnabled(true);
	        	panel.getCrsMainPanel().getNewCrsPanel().setWKT();
	        	panel.getCrsMainPanel().getNewCrsPanel().getBtnEditar().setEnabled(true);
	        	panel.getCrsMainPanel().getNewCrsPanel().getBtnEliminar().setEnabled(true);
	        	panel.getJComboOptions().setEnabled(true);        		
	        	panel.getJButtonAccept().setEnabled(true);	        	        		            	
	        }
		}
		
		
		if (e.getSource() == panel.getEpsgTrPanel().getJTable().getSelectionModel()){
			ListSelectionModel lsm = (ListSelectionModel)e.getSource();
	        if (lsm.isSelectionEmpty()) {
	        	panel.getEpsgTrPanel().selectedRowTable = -1;	        	
	        	panel.getJButtonAccept().setEnabled(false);
	        } 
	        else {
	        	panel.getEpsgTrPanel().selectedRowTable = lsm.getMinSelectionIndex();
	        	panel.getEpsgTrPanel().setTrasformation_code(Integer.parseInt((String)panel.getEpsgTrPanel().sorter.getValueAt(panel.getEpsgTrPanel().selectedRowTable,0)));
	            String sentence = "SELECT target_crs_code " +
								"FROM epsg_coordoperation " +                        
								"WHERE coord_op_code = " + panel.getEpsgTrPanel().getTransformation_code() ;
	            ResultSet result = Query.select(sentence,panel.getEpsgTrPanel().connect.getConnection());
	            try {
					result.next();
					int tar = result.getInt("target_crs_code");
					if (tar == panel.getEpsgTrPanel().crs_target) 
						panel.getEpsgTrPanel().inverseTranformation = false;					
					else panel.getEpsgTrPanel().inverseTranformation = true;
				} catch (SQLException e1) {							
					e1.printStackTrace();
				}
				panel.getJButtonAccept().setEnabled(true);
	        }
		}
		
		/*
		 * una vez cambiamos la transformacion a elegir, habra que ver que parametros
		 * tenemos que definir para que cuando aceptemos hayan sido definidos
		 * anteriormente
		 */
		if (e.getSource() == panel.getRecentsTrPanel().getJTable().getSelectionModel()){
			ListSelectionModel lsm = (ListSelectionModel)e.getSource();
	        if (lsm.isSelectionEmpty()) {
	        	panel.getRecentsTrPanel().selectedRowTable = -1;
	        	panel.getRecentsTrPanel().setCode(0);
	        	panel.getJButtonAccept().setEnabled(false);
	        	panel.getRecentsTrPanel().getJButtonInfo().setEnabled(false);
	        } 
	        else {
	        	panel.getRecentsTrPanel().selectedRowTable = lsm.getMinSelectionIndex();
	        	String[] cad = ((String)panel.getRecentsTrPanel().sorter.getValueAt(panel.getRecentsTrPanel().selectedRowTable,2)).split(":");	        	
	        	panel.getRecentsTrPanel().setCode(Integer.parseInt(cad[1]));
	        	String[] transformation = ((String)panel.getRecentsTrPanel().sorter.getValueAt(panel.getRecentsTrPanel().selectedRowTable,0)).split(":");
	        	if (transformation[0].equals("EPSG")){	        		
	        		panel.getRecentsTrPanel().setTrCode(Integer.parseInt(transformation[1]));
					String sentence = "SELECT target_crs_code " +
									"FROM epsg_coordoperation " +                        
									"WHERE coord_op_code = " + panel.getRecentsTrPanel().getTrCode();
				    ResultSet result = Query.select(sentence,panel.getRecentsTrPanel().connect.getConnection());
				    
					try {
						result.next();						
						int tar = result.getInt("target_crs_code");
						int crs_target = Integer.parseInt(((String)panel.getRecentsTrPanel().sorter.getValueAt(panel.getRecentsTrPanel().selectedRowTable,3)).split(":")[1]);
						int crs_base = -1;
						sentence = "SELECT source_geogcrs_code " +
								  "FROM epsg_coordinatereferencesystem " +	                              
	                              "WHERE coord_ref_sys_code = " + crs_target;
						result = Query.select(sentence,panel.getRecentsTrPanel().connect.getConnection());
						try {
							result.next();
							crs_base = result.getInt("source_geogcrs_code");
						} catch (SQLException e1) {
							crs_base = -1;
						}						
						
						if (tar == crs_target || tar == crs_base) 
							panel.getRecentsTrPanel().setInverseTransformation(false);					
						else panel.getRecentsTrPanel().setInverseTransformation(true);
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}	
	        	}
	        	panel.getRecentsTrPanel().getJButtonInfo().setEnabled(true);	        	
	        	panel.getJButtonAccept().setEnabled(true);
	        }
		}
	}
	
	/**
	 * Metodo que sirve para cuando seleccionamos un crs con transformacion
	 * salga en el combobox de las transformaciones la reciente utilizada
	 * y actualizada
	 * @return
	 */
	private String getCorrectItem (){
		String item = ((String)panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().sorter.getValueAt(panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().selectedRowTable, 3));
		if (item.startsWith(PluginServices.getText(this, "EPSG"))) {
			panel.getJComboOptions().setSelectedIndex(2);
			return "EPSG";
		}
		else if (item.startsWith(PluginServices.getText(this, "NADGR"))) {
			panel.getJComboOptions().setSelectedIndex(5);
			return "NADGR";
		}
		else if (item.startsWith(PluginServices.getText(this, "USR"))) {
			panel.getJComboOptions().setSelectedIndex(3);
			return "USR";
		}
		else if (item.startsWith(PluginServices.getText(this, "COMP"))) {
			panel.getJComboOptions().setSelectedIndex(4);
			return "COMP";
		}
		return "";
	}	

	/**
	 * Manejador de los eventos relacionados con el cambio de opcin
	 * dentro del combobox de eleccin de repositorio, y del combobox
	 * de eleccin del archivo .gsb para el panel de transformacin
	 * de nadgrids.
	 */
	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == panel.getCrsMainPanel().getJComboOptions()) {
			CardLayout cl = (CardLayout)(panel.getCrsMainPanel().getJPanelMain().getLayout());
			String op = (String)e.getItem();		
			if (op.equals("EPSG")){
				String[] not_soported = {"engineering", "vertical", "compound", "geocentric", "geographic 3D"};
				
				boolean soported = true;
				boolean soported2 = false;
				panel.getCrsMainPanel().getEpsgPanel().connection();
				if(panel.getCrsMainPanel().getEpsgPanel().getJTable().getSelectedRowCount()>0) {
					panel.getCrsMainPanel().getEpsgPanel().selectedRowTable = panel.getCrsMainPanel().getEpsgPanel().getJTable().getSelectedRow();	        	
		        	String crs_kind = (String)panel.getCrsMainPanel().getEpsgPanel().sorter.getValueAt(panel.getCrsMainPanel().getEpsgPanel().selectedRowTable,2);
		        	for (int i = 0; i < not_soported.length; i++) {
		        		if (crs_kind.equals(not_soported[i]))
		        			soported = false;		        		
		        	}
		        	for (int i=0; i< valid_method_code.length;i++) {
		        		if (panel.getCrsMainPanel().getEpsgPanel().getProjectionCode((String)panel.getCrsMainPanel().getEpsgPanel().sorter.getValueAt(panel.getCrsMainPanel().getEpsgPanel().selectedRowTable,0)) == valid_method_code[i]) {
		        			soported2 = true;
		        		}
		        	}
		        	if (crs_kind.equals("geographic 2D")) {
		        		soported2 = true;
		        	}
		        	if (soported && soported2){
		        		panel.getCrsMainPanel().getEpsgPanel().setCodeCRS(Integer.parseInt((String)panel.getCrsMainPanel().getEpsgPanel().sorter.getValueAt(panel.getCrsMainPanel().getEpsgPanel().selectedRowTable,0)));
		        		panel.getCrsMainPanel().getEpsgPanel().setWKT();        		
		        		panel.getJButtonAccept().setEnabled(true);
		            	int base = panel.getCrsMainPanel().getEpsgPanel().getCodeCRS();						
						//panel.compareDatum(panel.getCrsMainPanel().getEpsgPanel().getWKT());
		            	panel.getJComboOptions().setEnabled(true);
		            	if (panel.getJComboOptions().getSelectedIndex() > 0) {
				    		panel.getJButtonAccept().setEnabled(false);			
							panel.getJButtonNext().setEnabled(true);
							panel.getJButtonNext().setEnabled(true);
			    		} else {
			    			panel.getJButtonAccept().setEnabled(true);
			    			panel.getJButtonAccept().setEnabled(true);						
							panel.getJButtonNext().setEnabled(false);
			    		}
		        		//panel.getJButtonAccept().setEnabled(true);
		        		panel.getCrsMainPanel().getEpsgPanel().getInfoCrs().setEnabled(true);
		        	}
		        	else {
		        		JOptionPane.showMessageDialog(panel, PluginServices.getText(this,"crs_not_soported"), "Warning...",
								JOptionPane.WARNING_MESSAGE);
		        		panel.getCrsMainPanel().getEpsgPanel().setCodeCRS(-1);
		        		panel.getCrsMainPanel().getEpsgPanel().setCodeCRS(0);
		        		panel.getJButtonAccept().setEnabled(false);
		        		panel.getCrsMainPanel().getEpsgPanel().getInfoCrs().setEnabled(false);
		        	}		        	
				}
				else{
					panel.getJButtonNext().setEnabled(false);
					panel.getJComboOptions().setEnabled(false);
					panel.getJComboOptions().setSelectedIndex(0);
					panel.getJButtonAccept().setEnabled(false);
				}
			} else if (op.equals("IAU2000")) {
				panel.getCrsMainPanel().getIauPanel().connection();
				if(panel.getCrsMainPanel().getIauPanel().getJTable().getSelectedRowCount()>0) {
					panel.getCrsMainPanel().getIauPanel().selectedRowTable = panel.getCrsMainPanel().getIauPanel().getJTable().getSelectedRow();;
		        	panel.getCrsMainPanel().getIauPanel().setCodeCRS(Integer.parseInt((String)panel.getCrsMainPanel().getIauPanel().sorter.getValueAt(panel.getCrsMainPanel().getIauPanel().selectedRowTable,0)));
		        	panel.getCrsMainPanel().getIauPanel().setWKT();  
		    		panel.getJComboOptions().setEnabled(true);
		    		if (panel.getJComboOptions().getSelectedIndex() > 0) {
			    		panel.getJButtonAccept().setEnabled(false);			
						panel.getJButtonNext().setEnabled(true);
						panel.getJButtonNext().setEnabled(true);
		    		} else {
		    			panel.getJButtonAccept().setEnabled(true);
		    			panel.getJButtonAccept().setEnabled(true);						
						panel.getJButtonNext().setEnabled(false);
		    		}
		    		//panel.getJButtonAccept().setEnabled(true);			
					//panel.getJButtonNext().setEnabled(false);
				}
				else {
					panel.getJButtonNext().setEnabled(false);
					panel.getJComboOptions().setEnabled(false);
					panel.getJComboOptions().setSelectedIndex(0);
					panel.getJButtonAccept().setEnabled(false);
				}
			}
			else if (op.equals("Recientes")) {
				if(panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().getJTable().getSelectedRowCount()>0) {
					panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().initCrs();		        	
		        	String sour =  panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().getCrs().getCrsWkt().getAuthority()[0]; //(String)panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().sorter.getValueAt(panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().selectedRowTable,0);
		        	IWindow activeWindow = PluginServices.getMDIManager().getActiveWindow();
			    	BaseView activeView = (BaseView) activeWindow;
			    	String authority = ((ICrs) activeView.getMapControl().getProjection()).getCrsWkt().getAuthority()[0];
		        	/*if (sour.equals("EPSG") && authority.equals("EPSG")){
		        		//panel.compareDatum(panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().getCrs().getWKT());
		        		panel.getJComboOptions().setEnabled(true);
		        		panel.getJButtonAccept().setEnabled(true);
		        	}
		        	else {
		        		panel.getJComboOptions().setSelectedIndex(0);	        		
		        		panel.getJComboOptions().setEnabled(false);
		        		panel.getJButtonAccept().setEnabled(true);
		        		panel.getJButtonAccept().setEnabled(true);	        			        		
		        		panel.getJButtonNext().setEnabled(false);
		        	}	*/	
			    	panel.getJComboOptions().setEnabled(true);
			    	if (panel.getJComboOptions().getSelectedIndex() > 0) {
			    		panel.getJButtonAccept().setEnabled(false);			
						panel.getJButtonNext().setEnabled(true);
						panel.getJButtonNext().setEnabled(true);
		    		} else {
		    			panel.getJButtonAccept().setEnabled(true);
		    			panel.getJButtonNext().setEnabled(false);
		    		}
	        		//panel.getJButtonAccept().setEnabled(true);
				}			
				else {
					panel.getJButtonNext().setEnabled(false);
					panel.getJComboOptions().setEnabled(false);
					panel.getJComboOptions().setSelectedIndex(0);
					panel.getJButtonAccept().setEnabled(false);
				}
			}
			else if (op.equals("ESRI")) {
				panel.getCrsMainPanel().getEsriPanel().connection();
				if(panel.getCrsMainPanel().getEsriPanel().getJTable().getSelectedRowCount()>0) {
					panel.getCrsMainPanel().getEsriPanel().selectedRowTable = panel.getCrsMainPanel().getEsriPanel().getJTable().getSelectedRow();;
		        	panel.getCrsMainPanel().getEsriPanel().setCodeCRS(Integer.parseInt((String)panel.getCrsMainPanel().getEsriPanel().sorter.getValueAt(panel.getCrsMainPanel().getEsriPanel().selectedRowTable,0)));
		        	panel.getCrsMainPanel().getEsriPanel().setWKT();  
		    		panel.getJComboOptions().setEnabled(true);
		    		if (panel.getJComboOptions().getSelectedIndex() > 0) {
			    		panel.getJButtonAccept().setEnabled(false);			
						panel.getJButtonNext().setEnabled(true);
		    		} else {
		    			panel.getJButtonAccept().setEnabled(true);
		    			panel.getJButtonNext().setEnabled(false);
		    		}
		    		//panel.getJButtonAccept().setEnabled(true);			
					//panel.getJButtonNext().setEnabled(false);
				}
				else {
					panel.getJButtonNext().setEnabled(false);
					panel.getJComboOptions().setEnabled(false);
					panel.getJComboOptions().setSelectedIndex(0);
					panel.getJButtonAccept().setEnabled(false);
				}
			}
			
			else if (op.equals(PluginServices.getText(this, "newCRS"))) {
				panel.getCrsMainPanel().getNewCrsPanel().connection();
				if(panel.getCrsMainPanel().getNewCrsPanel().getJTable().getSelectedRowCount()>0) {
					if (panel.getCrsMainPanel().getNewCrsPanel().getSearchTextField().getText().equals(""))
						panel.getCrsMainPanel().getNewCrsPanel().initializeTable();
					panel.getCrsMainPanel().getNewCrsPanel().selectedRowTable = panel.getCrsMainPanel().getNewCrsPanel().getJTable().getSelectedRow();;
		        	panel.getCrsMainPanel().getNewCrsPanel().setCodeCRS(Integer.parseInt((String)panel.getCrsMainPanel().getNewCrsPanel().sorter.getValueAt(panel.getCrsMainPanel().getNewCrsPanel().selectedRowTable,0)));
		        	panel.getCrsMainPanel().getNewCrsPanel().setWKT();  
		    		panel.getJComboOptions().setEnabled(true);
		    		if (panel.getJComboOptions().getSelectedIndex() > 0) {
			    		panel.getJButtonAccept().setEnabled(false);			
						panel.getJButtonNext().setEnabled(true);
		    		} else {
		    			panel.getJButtonAccept().setEnabled(true);
		    			panel.getJButtonNext().setEnabled(false);
		    		}
				}
				else {
					panel.getJButtonNext().setEnabled(false);
					panel.getJComboOptions().setEnabled(false);
					panel.getJComboOptions().setSelectedIndex(0);
					panel.getJButtonAccept().setEnabled(false);
				}
			}
		    cl.show(panel.getCrsMainPanel().getJPanelMain(), (String)e.getItem());
		    panel.getCrsMainPanel().setDataSource((String)e.getItem());
		}
	    	    
	    if (e.getSource() == panel.getNadsTrPanel().getJComboNadFile()){
			if (panel.getNadsTrPanel().getJComboNadFile().getSelectedIndex()!=0){
				panel.getNadsTrPanel().setNadFile(panel.getNadsTrPanel().getJComboNadFile().getSelectedItem().toString());//nadFile = getJComboNadFile().getSelectedItem().toString();
				panel.getNadsTrPanel().getTreePanel().setRoot(PluginServices.getText(this,"grids_en")+": "+panel.getNadsTrPanel().getNadFile());//nadFile);
				panel.getNadsTrPanel().initializeTree();
				// Guardar en persistencia nadFile:
				panel.getNadsTrPanel().saveNadFileName(panel.getNadsTrPanel().getNadFile());//nadFile);
				panel.getJButtonAccept().setEnabled(true);
			}
			else{
				panel.getNadsTrPanel().setNadFile(null);//nadFile = null;
				panel.getNadsTrPanel().getTreePanel().setRoot("");
				panel.getJButtonAccept().setEnabled(false);
			}
			
		}
	}
		
	/**
	 * Mtodo para manejar el doble click sobre las tablas existentes en
	 * el panel de transformacin. Aceptar el CRS seleccionado, as como
	 * la transformacin en el caso de que elijamos transformacin EPSG.
	 */
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if (panel.getJButtonAccept().isEnabled()) {
			if (e.getSource() == panel.getCrsMainPanel().getCrsAndTransformationRecentsPanel().getJTable()){
				if (e.getClickCount() == 2){
					if (panel.isEpsg_tr()) {			
						TransEPSG epsgParams = new TransEPSG(panel.getEpsgTrPanel().getTransformation_code(),panel.getEpsgTrPanel().connect, panel.getEpsgTrPanel().inverseTranformation);
						panel.getEpsgTrPanel().setValues(epsgParams.getParamValue());	
						panel.setProjection(panel.getEpsgTrPanel().getProjection());
						panel.setEpsg_tr(false);
						
						/*
						 * Actualizar Transformaciones recientes
						 */
						String authority = "EPSG";
			    		String name = (String)panel.getEpsgTrPanel().sorter.getValueAt(panel.getEpsgTrPanel().selectedRowTable,1);
			    		int code = panel.getEpsgTrPanel().getTransformation_code();
			    		String crsSource = "EPSG:"+String.valueOf(panel.getEpsgTrPanel().getSource()); 	    			
			    		String crsTarget = "EPSG:"+String.valueOf(panel.getEpsgTrPanel().getTarget()); 	    			
			    		String details = (String)panel.getEpsgTrPanel().sorter.getValueAt(panel.getEpsgTrPanel().selectedRowTable,5);
			    		TrData trData = new TrData(authority,code,name,crsSource,crsTarget,details);
			    		RecentTrsPersistence trPersistence = new RecentTrsPersistence();
			    		trPersistence.addTrData(trData);
			    		
					}
					else if (panel.isManual_tr()) {
						boolean domain = panel.getManualTrPanel().correctJTextField();
						if (domain && panel.getManualTrPanel().correctDomain()){
							panel.setProjection(panel.getManualTrPanel().getProjection());
							panel.setManual_tr(false);
						}
						else if (!domain) {
							JOptionPane.showMessageDialog(panel, 
									PluginServices.getText(this,"numeric_format"), 
									"Warning...", JOptionPane.WARNING_MESSAGE);					
							return;
						}				
						else {
							JOptionPane.showMessageDialog(panel, 
									PluginServices.getText(this,"incorrect_domain"), 
									"Warning...", JOptionPane.WARNING_MESSAGE);					
							return;
						}
						
						/*
						 * Actualizar Transformaciones recientes
						 */
						String authority = "USR";
			    		String name = PluginServices.getText(this,"manual");
			    		int code = 0;
			    		String crsSource = panel.getManualTrPanel().getSourceAbrev();
			    		String crsTarget = panel.getManualTrPanel().getTargetAbrev();
			    		String details = panel.getManualTrPanel().getValues();
			    		 
			    		TrData trData = new TrData(authority,code,name,crsSource,crsTarget,details);
			    		RecentTrsPersistence trPersistence = new RecentTrsPersistence();
			    		trPersistence.addTrData(trData);
					}
					else if (panel.isNads_tr()) {
						panel.setProjection(panel.getNadsTrPanel().getProjection());
						panel.setTargetNad(panel.getNadsTrPanel().getNad());
						panel.setNads_tr(false);
						
						/*
						 * Actualizar Transformaciones recientes
						 */
						String authority = "NADGR";
			    		String name = "----";
			    		int code = 0;
			    		String crsSource = panel.getNadsTrPanel().getSourceAbrev();
			    		String crsTarget = panel.getNadsTrPanel().getTargetAbrev();
			    		String details = "";
			    		if (panel.getNadsTrPanel().getNad())
			    			details = panel.getNadsTrPanel().getNadFile()+ " ("+panel.getNadsTrPanel().getTargetAbrev() +")";
			    		else
			    			details = panel.getNadsTrPanel().getNadFile()+ " ("+panel.getNadsTrPanel().getSourceAbrev() +")"; 
			    		TrData trData = new TrData(authority,code,name,crsSource,crsTarget,details);
			    		RecentTrsPersistence trPersistence = new RecentTrsPersistence();
			    		trPersistence.addTrData(trData);
					}
					/*
					 * Ver que es necesario cuando aceptas en el panel de transformaciones
					 * recientes.
					 */
					else if (panel.isRecents_tr()) {
						String[] transformation = ((String)panel.getRecentsTrPanel().sorter.getValueAt(panel.getRecentsTrPanel().selectedRowTable,0)).split(":");
						if (transformation[0].equals("USR")){
			        		panel.getRecentsTrPanel().setParamsManual((String)panel.getRecentsTrPanel().sorter.getValueAt(panel.getRecentsTrPanel().selectedRowTable,4));
			        		panel.setProjection(panel.getRecentsTrPanel().getProjection());
			        	}
						if (transformation[0].equals("EPSG")){
							TransEPSG epsgParams = new TransEPSG(panel.getRecentsTrPanel().getTrCode(),panel.getRecentsTrPanel().connect, panel.getRecentsTrPanel().getInverseTransformation());
							panel.getRecentsTrPanel().setParamsEPGS(epsgParams.getParamValue());
							panel.setProjection(panel.getRecentsTrPanel().getProjection());
			        	}
						if (transformation[0].equals("NADGR")){
							panel.setProjection(panel.getRecentsTrPanel().getProjectionNad((String)panel.getRecentsTrPanel().sorter.getValueAt(panel.getRecentsTrPanel().selectedRowTable,4)));
						}
						if (transformation[0].equals("COMP")) {
							panel.setProjection(panel.getRecentsTrPanel().getProjectionComplex((String)panel.getRecentsTrPanel().sorter.getValueAt(panel.getRecentsTrPanel().selectedRowTable,4)));
						}
						//panel.setProjection(panel.getRecentsTrPanel().getProjection());
						panel.setRecents_tr(false);
						
						/*
						 * Actualizar Transformaciones recientes
						 */
						String authCode = (String)panel.getRecentsTrPanel().sorter.getValueAt(panel.getRecentsTrPanel().selectedRowTable,0);
						String authority = authCode.split(":")[0];
			    		String name = (String)panel.getRecentsTrPanel().sorter.getValueAt(panel.getRecentsTrPanel().selectedRowTable,1);
			    		int code = Integer.parseInt(authCode.split(":")[1]);
			    		String crsSource = (String)panel.getRecentsTrPanel().sorter.getValueAt(panel.getRecentsTrPanel().selectedRowTable,2);
			    		String crsTarget = (String)panel.getRecentsTrPanel().sorter.getValueAt(panel.getRecentsTrPanel().selectedRowTable,3);
			    		String details = (String)panel.getRecentsTrPanel().sorter.getValueAt(panel.getRecentsTrPanel().selectedRowTable,4);	    		
			    		TrData trData = new TrData(authority,code,name,crsSource,crsTarget,details);
			    		RecentTrsPersistence trPersistence = new RecentTrsPersistence();
			    		trPersistence.addTrData(trData);
					}else if(panel.isCompuesta_tr()){
						//Selecciona transformacin compuesta
						//Mostrar el panel de transformacin de la Capa
						panel.setProjection(panel.getVistaTrPanel().getProjection());
						//panel.setTargetNad(panel.getNadsTrPanel().getNad());
						panel.setCompuesta_tr(false);			
						panel.setVista_tr(false);
						
						/**
						 * para actualizar las transformaciones recientes, tendremos
						 * que coger las dos transformaciones utilizadas...
						 */
						
						/*					
						 * Actualizar Transformaciones recientes
						 */
						String authorityLayer = null;
			    		String nameLayer = null;
			    		int codeLayer = 0;
			    		String crsSourceLayer = null;
			    		String crsTargetLayer = null;
			    		String detailsLayer = "";
			    		
						if(panel.getCapaTrPanel().getJComboOptions().getSelectedItem().equals(PluginServices.getText(this,"recents_transformation"))){
							String[] transform = ((String)panel.getCapaTrPanel().getRecentsTrPanel().sorter.getValueAt(panel.getCapaTrPanel().getRecentsTrPanel().selectedRowTable,0)).split(":");
							
							authorityLayer = PluginServices.getText(this, transform[0]);						
							nameLayer = (String)panel.getCapaTrPanel().getRecentsTrPanel().sorter.getValueAt(panel.getCapaTrPanel().getRecentsTrPanel().selectedRowTable,1);
							crsSourceLayer = (String)panel.getCapaTrPanel().getRecentsTrPanel().sorter.getValueAt(panel.getCapaTrPanel().getRecentsTrPanel().selectedRowTable,2);
							crsTargetLayer = (String)panel.getCapaTrPanel().getRecentsTrPanel().sorter.getValueAt(panel.getCapaTrPanel().getRecentsTrPanel().selectedRowTable,3);
							detailsLayer = (String)panel.getCapaTrPanel().getRecentsTrPanel().sorter.getValueAt(panel.getCapaTrPanel().getRecentsTrPanel().selectedRowTable,4);
						}
						else if(panel.getCapaTrPanel().getJComboOptions().getSelectedItem().equals(PluginServices.getText(this,"transformacion_epsg"))){
							authorityLayer = "EPSG";
				    		nameLayer = (String)panel.getCapaTrPanel().getEpsgTrPanel().sorter.getValueAt(panel.getCapaTrPanel().getEpsgTrPanel().selectedRowTable,1);
				    		codeLayer = panel.getCapaTrPanel().getEpsgTrPanel().getTransformation_code();
				    		crsSourceLayer = "EPSG:"+String.valueOf(panel.getCapaTrPanel().getEpsgTrPanel().getSource()); 	    			
				    		crsTargetLayer = "EPSG:"+String.valueOf(panel.getCapaTrPanel().getEpsgTrPanel().getTarget()); 	    			
				    		detailsLayer = (String)panel.getCapaTrPanel().getEpsgTrPanel().sorter.getValueAt(panel.getCapaTrPanel().getEpsgTrPanel().selectedRowTable,5);
				    		
						}
						else if(panel.getCapaTrPanel().getJComboOptions().getSelectedItem().equals(PluginServices.getText(this,"transformacion_manual"))){
							authorityLayer = "USR";
				    		nameLayer = PluginServices.getText(this,"manual");
				    		codeLayer = 0;
				    		crsSourceLayer = panel.getCapaTrPanel().getManualTrPanel().getSourceAbrev();
				    		crsTargetLayer = panel.getCapaTrPanel().getManualTrPanel().getTargetAbrev();
				    		detailsLayer = panel.getCapaTrPanel().getManualTrPanel().getValues();
						}
						else if(panel.getCapaTrPanel().getJComboOptions().getSelectedItem().equals(PluginServices.getText(this,"nadgrids"))){
							authorityLayer = "NADGR";
				    		nameLayer = "----";
				    		codeLayer = 0;
				    		crsSourceLayer = panel.getCapaTrPanel().getNadsTrPanel().getSourceAbrev();
				    		crsTargetLayer = panel.getCapaTrPanel().getNadsTrPanel().getTargetAbrev();
				    		detailsLayer = panel.getCapaTrPanel().getNadsTrPanel().getNadFile()+ " ("+panel.getCapaTrPanel().getNadsTrPanel().getSourceAbrev() +")";
						}		    		 
			    		TrData trDataLayer = new TrData(authorityLayer,codeLayer,nameLayer,crsSourceLayer,crsTargetLayer,detailsLayer);
			    		
			    		String authorityView = null;
			    		String nameView = null;
			    		int codeView = 0;
			    		String crsSourceView = null;
			    		String crsTargetView = null;
			    		String detailsView = "";
			    		
						if(panel.getVistaTrPanel().getJComboOptions().getSelectedItem().equals(PluginServices.getText(this,"recents_transformation"))){
							String[] transform = ((String)panel.getVistaTrPanel().getRecentsTrPanel().sorter.getValueAt(panel.getVistaTrPanel().getRecentsTrPanel().selectedRowTable,0)).split(":");
							
							authorityView = PluginServices.getText(this, transform[0]);						
							nameView = (String)panel.getVistaTrPanel().getRecentsTrPanel().sorter.getValueAt(panel.getVistaTrPanel().getRecentsTrPanel().selectedRowTable,1);
							crsSourceView = (String)panel.getVistaTrPanel().getRecentsTrPanel().sorter.getValueAt(panel.getVistaTrPanel().getRecentsTrPanel().selectedRowTable,2);
							crsTargetView = (String)panel.getVistaTrPanel().getRecentsTrPanel().sorter.getValueAt(panel.getVistaTrPanel().getRecentsTrPanel().selectedRowTable,3);
							detailsView = (String)panel.getVistaTrPanel().getRecentsTrPanel().sorter.getValueAt(panel.getVistaTrPanel().getRecentsTrPanel().selectedRowTable,4);
						}
						else if(panel.getVistaTrPanel().getJComboOptions().getSelectedItem().equals(PluginServices.getText(this,"transformacion_epsg"))){
							authorityView = "EPSG";
				    		nameView = (String)panel.getVistaTrPanel().getEpsgTrPanel().sorter.getValueAt(panel.getVistaTrPanel().getEpsgTrPanel().selectedRowTable,1);
				    		codeView = panel.getVistaTrPanel().getEpsgTrPanel().getTransformation_code();
				    		crsSourceView = "EPSG:"+String.valueOf(panel.getVistaTrPanel().getEpsgTrPanel().getSource()); 	    			
				    		crsTargetView = "EPSG:"+String.valueOf(panel.getVistaTrPanel().getEpsgTrPanel().getTarget()); 	    			
				    		detailsView = (String)panel.getVistaTrPanel().getEpsgTrPanel().sorter.getValueAt(panel.getVistaTrPanel().getEpsgTrPanel().selectedRowTable,5);
				    		
						}
						else if(panel.getVistaTrPanel().getJComboOptions().getSelectedItem().equals(PluginServices.getText(this,"transformacion_manual"))){
							authorityView = "USR";
				    		nameView = PluginServices.getText(this,"manual");
				    		codeView = 0;
				    		crsSourceView = panel.getVistaTrPanel().getManualTrPanel().getSourceAbrev();
				    		crsTargetView = panel.getVistaTrPanel().getManualTrPanel().getTargetAbrev();
				    		detailsView = panel.getVistaTrPanel().getManualTrPanel().getValues();
						}
						else if(panel.getVistaTrPanel().getJComboOptions().getSelectedItem().equals(PluginServices.getText(this,"nadgrids"))){
							authorityView = "NADGR";
				    		nameView = "----";
				    		codeView = 0;
				    		crsSourceView = panel.getVistaTrPanel().getNadsTrPanel().getSourceAbrev();
				    		crsTargetView = panel.getVistaTrPanel().getNadsTrPanel().getTargetAbrev();
				    		detailsView = panel.getVistaTrPanel().getNadsTrPanel().getNadFile()+ " ("+panel.getVistaTrPanel().getNadsTrPanel().getTargetAbrev() +")";
				    	}		    		 
			    		TrData trDataView = new TrData(authorityView,codeView,nameView,crsSourceView,crsTargetView,detailsView);
			    		CompTrData comp = new CompTrData(trDataLayer, trDataView);
			    		RecentTrsPersistence trPersistence = new RecentTrsPersistence();
			    		trPersistence.addTrData(comp);
					}
					else{
						panel.setSin_tr(true);
						panel.setProjection(panel.getProjection());
					}
										
		    		// * Actualizar recientes...
		    		 
		    		String authority = ((ICrs)panel.getProjection()).getCrsWkt().getAuthority()[0];
		    		String name = ((ICrs)panel.getProjection()).getCrsWkt().getName();
		    		int code = ((ICrs)panel.getProjection()).getCode();
		    		CrsData crsData = new CrsData(authority,code,name);
		    		RecentCRSsPersistence persistence = new RecentCRSsPersistence();
		    		persistence.addCrsData(crsData);
					
					PluginServices.getMDIManager().closeWindow(panel);
				}
			}
			if (e.getSource() == panel.getCrsMainPanel().getEpsgPanel().getJTable()){
				if (e.getClickCount() == 2){
					if (panel.isEpsg_tr()) {			
						TransEPSG epsgParams = new TransEPSG(panel.getEpsgTrPanel().getTransformation_code(),panel.getEpsgTrPanel().connect, panel.getEpsgTrPanel().inverseTranformation);
						panel.getEpsgTrPanel().setValues(epsgParams.getParamValue());	
						panel.setProjection(panel.getEpsgTrPanel().getProjection());
						panel.setEpsg_tr(false);
						
						/*
						 * Actualizar Transformaciones recientes
						 */
						String authority = "EPSG";
			    		String name = (String)panel.getEpsgTrPanel().sorter.getValueAt(panel.getEpsgTrPanel().selectedRowTable,1);
			    		int code = panel.getEpsgTrPanel().getTransformation_code();
			    		String crsSource = "EPSG:"+String.valueOf(panel.getEpsgTrPanel().getSource()); 	    			
			    		String crsTarget = "EPSG:"+String.valueOf(panel.getEpsgTrPanel().getTarget());
			    		String details = (String)panel.getEpsgTrPanel().sorter.getValueAt(panel.getEpsgTrPanel().selectedRowTable,5);;
			    		TrData trData = new TrData(authority,code,name,crsSource,crsTarget,details);
			    		RecentTrsPersistence trPersistence = new RecentTrsPersistence();
			    		trPersistence.addTrData(trData);
					}
					else if (panel.isManual_tr()) {
						panel.setProjection(panel.getManualTrPanel().getProjection());
						panel.setManual_tr(false);
					}
					else if (panel.isNads_tr()) {
						panel.setProjection(panel.getNadsTrPanel().getProjection());
						panel.setTargetNad(panel.getNadsTrPanel().getNad());
						panel.setNads_tr(false);
					}
					else{
						panel.setSin_tr(true);
						panel.setProjection(panel.getProjection());
					}
					
		    		 //* Actualizar recientes...
		    		 
		    		String authority = ((ICrs)panel.getProjection()).getCrsWkt().getAuthority()[0];
		    		String name = ((ICrs)panel.getProjection()).getCrsWkt().getName();
		    		int code = ((ICrs)panel.getProjection()).getCode();
		    		CrsData crsData = new CrsData(authority,code,name);
		    		RecentCRSsPersistence persistence = new RecentCRSsPersistence();
		    		persistence.addCrsData(crsData);
					
					PluginServices.getMDIManager().closeWindow(panel);
				}
			}
			if (e.getSource() == panel.getCrsMainPanel().getIauPanel().getJTable()){
				if (e.getClickCount() == 2){
					if (panel.isEpsg_tr()) {			
						TransEPSG epsgParams = new TransEPSG(panel.getEpsgTrPanel().getTransformation_code(),panel.getEpsgTrPanel().connect, panel.getEpsgTrPanel().inverseTranformation);
						panel.getEpsgTrPanel().setValues(epsgParams.getParamValue());	
						panel.setProjection(panel.getEpsgTrPanel().getProjection());
						panel.setEpsg_tr(false);
					}
					else if (panel.isManual_tr()) {
						panel.setProjection(panel.getManualTrPanel().getProjection());
						panel.setManual_tr(false);
					}
					else if (panel.isNads_tr()) {
						panel.setProjection(panel.getNadsTrPanel().getProjection());
						panel.setTargetNad(panel.getNadsTrPanel().getNad());
						panel.setNads_tr(false);
					}
					else{
						panel.setSin_tr(true);
						panel.setProjection(panel.getProjection());
					}
					
		    		 //* Actualizar recientes...
		    		 
		    		String authority = ((ICrs)panel.getProjection()).getCrsWkt().getAuthority()[0];
		    		String name = ((ICrs)panel.getProjection()).getCrsWkt().getName();
		    		int code = ((ICrs)panel.getProjection()).getCode();
		    		CrsData crsData = new CrsData(authority,code,name);
		    		RecentCRSsPersistence persistence = new RecentCRSsPersistence();
		    		persistence.addCrsData(crsData);
					
					PluginServices.getMDIManager().closeWindow(panel);
				}
			}
			if (e.getSource() == panel.getCrsMainPanel().getEsriPanel().getJTable()){
				if (e.getClickCount() == 2){
					if (panel.isEpsg_tr()) {			
						TransEPSG epsgParams = new TransEPSG(panel.getEpsgTrPanel().getTransformation_code(),panel.getEpsgTrPanel().connect, panel.getEpsgTrPanel().inverseTranformation);
						panel.getEpsgTrPanel().setValues(epsgParams.getParamValue());	
						panel.setProjection(panel.getEpsgTrPanel().getProjection());
						panel.setEpsg_tr(false);
					}
					else if (panel.isManual_tr()) {
						panel.setProjection(panel.getManualTrPanel().getProjection());
						panel.setManual_tr(false);
					}
					else if (panel.isNads_tr()) {
						panel.setProjection(panel.getNadsTrPanel().getProjection());
						panel.setTargetNad(panel.getNadsTrPanel().getNad());
						panel.setNads_tr(false);
					}
					else{
						panel.setSin_tr(true);
						panel.setProjection(panel.getProjection());
					}
					
		    		 //* Actualizar recientes...
		    		 
		    		String authority = ((ICrs)panel.getProjection()).getCrsWkt().getAuthority()[0];
		    		String name = ((ICrs)panel.getProjection()).getCrsWkt().getName();
		    		int code = ((ICrs)panel.getProjection()).getCode();
		    		CrsData crsData = new CrsData(authority,code,name);
		    		RecentCRSsPersistence persistence = new RecentCRSsPersistence();
		    		persistence.addCrsData(crsData);
					
					PluginServices.getMDIManager().closeWindow(panel);
				}
			}
			if (e.getSource() == panel.getCrsMainPanel().getNewCrsPanel().getJTable()){
				if (e.getClickCount() == 2){
					if (panel.isEpsg_tr()) {			
						TransEPSG epsgParams = new TransEPSG(panel.getEpsgTrPanel().getTransformation_code(),panel.getEpsgTrPanel().connect, panel.getEpsgTrPanel().inverseTranformation);
						panel.getEpsgTrPanel().setValues(epsgParams.getParamValue());	
						panel.setProjection(panel.getEpsgTrPanel().getProjection());
						panel.setEpsg_tr(false);
					}
					else if (panel.isManual_tr()) {
						panel.setProjection(panel.getManualTrPanel().getProjection());
						panel.setManual_tr(false);
					}
					else if (panel.isNads_tr()) {
						panel.setProjection(panel.getNadsTrPanel().getProjection());
						panel.setTargetNad(panel.getNadsTrPanel().getNad());
						panel.setNads_tr(false);
					}
					else{
						panel.setSin_tr(true);
						panel.setProjection(panel.getProjection());
					}
					
		    		 //* Actualizar recientes...
		    		 
		    		String authority = ((ICrs)panel.getProjection()).getCrsWkt().getAuthority()[0];
		    		String name = ((ICrs)panel.getProjection()).getCrsWkt().getName();
		    		int code = ((ICrs)panel.getProjection()).getCode();
		    		CrsData crsData = new CrsData(authority,code,name);
		    		RecentCRSsPersistence persistence = new RecentCRSsPersistence();
		    		persistence.addCrsData(crsData);
					
					PluginServices.getMDIManager().closeWindow(panel);
				}
			}
		}
		if (e.getSource() == panel.getEpsgTrPanel().getJTable()){
			if (e.getClickCount() == 2){
				TransEPSG epsgParams = new TransEPSG(panel.getEpsgTrPanel().getTransformation_code(),panel.getEpsgTrPanel().connect, panel.getEpsgTrPanel().inverseTranformation);
				panel.getEpsgTrPanel().setValues(epsgParams.getParamValue());	
				panel.setProjection(panel.getEpsgTrPanel().getProjection());
				panel.setEpsg_tr(false);
				
				/*
				 * Actualizar Transformaciones recientes
				 */
				String authority = "EPSG";
	    		String name = (String)panel.getEpsgTrPanel().sorter.getValueAt(panel.getEpsgTrPanel().selectedRowTable,1);
	    		int code = panel.getEpsgTrPanel().getTransformation_code();
	    		String crsSource = "EPSG:"+String.valueOf(panel.getEpsgTrPanel().getSource()); 	    			
	    		String crsTarget = "EPSG:"+String.valueOf(panel.getEpsgTrPanel().getTarget());
	    		String details = (String)panel.getEpsgTrPanel().sorter.getValueAt(panel.getEpsgTrPanel().selectedRowTable,5);;
	    		TrData trData = new TrData(authority,code,name,crsSource,crsTarget,details);
	    		RecentTrsPersistence trPersistence = new RecentTrsPersistence();
	    		trPersistence.addTrData(trData);
				
				/*
	    		 * Actualizar Crss recientes...
	    		 */
	    		authority = ((ICrs)panel.getProjection()).getCrsWkt().getAuthority()[0];
	    		name = ((ICrs)panel.getProjection()).getCrsWkt().getName();
	    		code = ((ICrs)panel.getProjection()).getCode();
	    		CrsData crsData = new CrsData(authority,code,name);
	    		RecentCRSsPersistence persistence = new RecentCRSsPersistence();
	    		persistence.addCrsData(crsData);
				
				PluginServices.getMDIManager().closeWindow(panel);
			}
		}
		if (e.getSource() == panel.getRecentsTrPanel().getJTable()){
			if (e.getClickCount() == 2){
				String[] transf = ((String)panel.getRecentsTrPanel().sorter.getValueAt(panel.getRecentsTrPanel().selectedRowTable,0)).split(":");
				if (transf[0].equals("USR")){
	        		panel.getRecentsTrPanel().setParamsManual((String)panel.getRecentsTrPanel().sorter.getValueAt(panel.getRecentsTrPanel().selectedRowTable,4));
	        		panel.setProjection(panel.getRecentsTrPanel().getProjection());
	        	}
				if (transf[0].equals("EPSG")){
					TransEPSG epsgParams = new TransEPSG(panel.getRecentsTrPanel().getTrCode(),panel.getEpsgTrPanel().connect, panel.getRecentsTrPanel().getInverseTransformation());
					panel.getRecentsTrPanel().setParamsEPGS(epsgParams.getParamValue());
					panel.setProjection(panel.getRecentsTrPanel().getProjection());
	        	}
				if (transf[0].equals("NADGR")){
					panel.setProjection(panel.getRecentsTrPanel().getProjectionNad((String)panel.getRecentsTrPanel().sorter.getValueAt(panel.getRecentsTrPanel().selectedRowTable,4)));
				}
				//panel.setProjection(panel.getRecentsTrPanel().getProjection());
				panel.setRecents_tr(false);				
				
				/*
				 * Actualizar Transformaciones recientes
				 */
				String authCode = (String)panel.getRecentsTrPanel().sorter.getValueAt(panel.getRecentsTrPanel().selectedRowTable,0);
				String authority = authCode.split(":")[0];
	    		String name = (String)panel.getRecentsTrPanel().sorter.getValueAt(panel.getRecentsTrPanel().selectedRowTable,1);
	    		int code = Integer.parseInt(authCode.split(":")[1]);
	    		String crsSource = (String)panel.getRecentsTrPanel().sorter.getValueAt(panel.getRecentsTrPanel().selectedRowTable,2);
	    		String crsTarget = (String)panel.getRecentsTrPanel().sorter.getValueAt(panel.getRecentsTrPanel().selectedRowTable,3);
	    		String details = (String)panel.getRecentsTrPanel().sorter.getValueAt(panel.getRecentsTrPanel().selectedRowTable,4);	    		
	    		TrData trData = new TrData(authority,code,name,crsSource,crsTarget,details);
	    		RecentTrsPersistence trPersistence = new RecentTrsPersistence();
	    		trPersistence.addTrData(trData);
								
				/*
	    		 * Actualizar recientes...
	    		 */
	    		authority = ((ICrs)panel.getProjection()).getCrsWkt().getAuthority()[0];
	    		name = ((ICrs)panel.getProjection()).getCrsWkt().getName();
	    		code = ((ICrs)panel.getProjection()).getCode();
	    		CrsData crsData = new CrsData(authority,code,name);
	    		RecentCRSsPersistence persistence = new RecentCRSsPersistence();
	    		persistence.addCrsData(crsData);
				
				PluginServices.getMDIManager().closeWindow(panel);
			}
		}
	}

	public void mouseEntered(MouseEvent e) {
		// 
		
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

	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Mtodo para cuando en la tranformacin manual, no se hayan insertado
	 * valores de transformacin, no se habilite el botn de aceptar.
	 */
	public void keyReleased(KeyEvent e) {
		if(!panel.getManualTrPanel().getStatus()) 
			panel.getJButtonAccept().setEnabled(false);		
		else panel.getJButtonAccept().setEnabled(true);		
	}

	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
