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
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.gvsig.crs.gui.CRSMainTrPanel;
import org.gvsig.crs.gui.panels.InfoCRSPanel;
import org.gvsig.crs.gui.panels.InfoTransformationsRecentsPanel;
import org.gvsig.crs.gui.panels.TransformationCapaPanel;
import org.gvsig.crs.gui.panels.TransformationVistaPanel;
import org.gvsig.crs.ogr.TransEPSG;

import com.iver.andami.PluginServices;

import es.idr.teledeteccion.connection.Query;

/**
 * Esta clase contiene todos los listeners necesarios para el manejo 
 * de los eventos del panel de la definicion de una transformacion
 * compuesta entre los crs de la vista y de la capa 
 *  
 * @author Jos Luis Gmez Martnez (jolugomar@gmail.com)
 *
 */

public class TransformationCompuestaPanelListener implements ActionListener, 
ListSelectionListener, ItemListener, KeyListener {

	TransformationCapaPanel capa = null;
	TransformationVistaPanel vista = null;
	private CRSMainTrPanel panel = null;	
	boolean condition = false;
	
	
	public TransformationCompuestaPanelListener(TransformationCapaPanel p, CRSMainTrPanel pa) {
		capa = p;
		panel = pa;
	}
	
	public TransformationCompuestaPanelListener(TransformationVistaPanel p, CRSMainTrPanel pa) {
		vista = p;
		panel = pa;
	}

	public void actionPerformed(ActionEvent e) {
		if (capa != null) {			
			if(	e.getSource() == capa.getRecentsTrPanel().getJButtonInfo()){
				String[] data = new String[5];
				data[0] = (String)capa.getRecentsTrPanel().sorter.getValueAt(capa.getRecentsTrPanel().selectedRowTable,0);
				data[1] = (String)capa.getRecentsTrPanel().sorter.getValueAt(capa.getRecentsTrPanel().selectedRowTable,1);
				data[2] = (String)capa.getRecentsTrPanel().sorter.getValueAt(capa.getRecentsTrPanel().selectedRowTable,2);
				data[3] = (String)capa.getRecentsTrPanel().sorter.getValueAt(capa.getRecentsTrPanel().selectedRowTable,3);
				data[4] = (String)capa.getRecentsTrPanel().sorter.getValueAt(capa.getRecentsTrPanel().selectedRowTable,4);
				
				//Mostrar el panel de informacin de las transformaciones recientes
				InfoCRSPanel info = new InfoCRSPanel(data[2].split(":")[0], Integer.parseInt(data[2].split(":")[1]) , data[0]+" <--> "+ data[4]);
				PluginServices.getMDIManager().addWindow(info);
			}
			//Al cambiar el item seleccionado del combo
			if(e.getSource().equals(capa.getJComboOptions())){
				CardLayout cl = (CardLayout)(capa.getPCenter().getLayout());
				if(capa.getJComboOptions().getSelectedItem().equals(PluginServices.getText(this,"recents_transformation"))){
					//Mostrar el panel de transformacin reciente
					 cl.show(capa.getPCenter(), "recents");
					 if (capa.getRecentsTrPanel().selectedRowTable == -1) {
						 capa.setSourceTransformation(null);
						 panel.getJButtonNext().setEnabled(false);
					 } else {
						String[] transform = ((String)capa.getRecentsTrPanel().sorter.getValueAt(capa.getRecentsTrPanel().selectedRowTable,0)).split(":");
						if (transform[0].equals("USR")){
							capa.getRecentsTrPanel().setParamsManual((String)capa.getRecentsTrPanel().sorter.getValueAt(capa.getRecentsTrPanel().selectedRowTable,4));
			        		capa.setSourceTransformation(capa.getRecentsTrPanel().getParams());
			        	}
						if (transform[0].equals("EPSG")){
							TransEPSG epsgParams = new TransEPSG(capa.getRecentsTrPanel().getTrCode(),capa.getEpsgTrPanel().connect, capa.getRecentsTrPanel().getInverseTransformation());
							capa.getRecentsTrPanel().setParamsEPGS(epsgParams.getParamValue());
							capa.setSourceTransformation(capa.getRecentsTrPanel().getParams());						
			        	}
						if (transform[0].equals("NADGR")){
							String p = (String)capa.getRecentsTrPanel().sorter.getValueAt(capa.getRecentsTrPanel().selectedRowTable,4);
							capa.setSourceTransformation(capa.getNadsParams(p));
						}
						panel.getJButtonNext().setEnabled(true);
					 }
				}
				else if(capa.getJComboOptions().getSelectedItem().equals(PluginServices.getText(this,"transformacion_epsg"))){
					//mostrar el panel de transformacin epsg
					 cl.show(capa.getPCenter(), "epsg");
					 if (capa.getEpsgTrPanel().selectedRowTable == -1) {
						 capa.setSourceTransformation(null);
						 panel.getJButtonNext().setEnabled(false);
					 } else {
						TransEPSG epsgParams = new TransEPSG(capa.getEpsgTrPanel().getTransformation_code(),capa.getEpsgTrPanel().connect, capa.getEpsgTrPanel().inverseTranformation);
						capa.getEpsgTrPanel().setValues(epsgParams.getParamValue());	
						capa.setSourceTransformation(capa.getParamsEpsg(capa.getEpsgTrPanel().getValues()));
						panel.getJButtonNext().setEnabled(true);
					 }
				}
				else if(capa.getJComboOptions().getSelectedItem().equals(PluginServices.getText(this,"transformacion_manual"))){
					//mostrar el panel de transformacin manual
					cl.show(capa.getPCenter(), "manual");
					if(!capa.getManualTrPanel().getStatus()) 
						panel.getJButtonNext().setEnabled(false);		
					else {
						panel.getJButtonNext().setEnabled(true);
						capa.setSourceTransformation(capa.getManualParams());
					}					
		        	
				}
				else if(capa.getJComboOptions().getSelectedItem().equals(PluginServices.getText(this,"nadgrids"))){
					//mostrar el panel de nadgrids
					 cl.show(capa.getPCenter(), "nad");
					 if (capa.getNadsTrPanel().getJComboNadFile().getSelectedIndex()!=0) {
						 capa.setSourceTransformation("+nadgrids="+capa.getNadsTrPanel().getNadFile());
						 panel.getJButtonNext().setEnabled(true);
					 }					 
					 else {
						 capa.setSourceTransformation(null);	
						 panel.getJButtonNext().setEnabled(false);
					 }
				}
			}
		}
		
		if (vista != null) {			
			if(	e.getSource() == vista.getRecentsTrPanel().getJButtonInfo()){
				String[] data = new String[5];
				data[0] = (String)vista.getRecentsTrPanel().sorter.getValueAt(vista.getRecentsTrPanel().selectedRowTable,0);
				data[1] = (String)vista.getRecentsTrPanel().sorter.getValueAt(vista.getRecentsTrPanel().selectedRowTable,1);
				data[2] = (String)vista.getRecentsTrPanel().sorter.getValueAt(vista.getRecentsTrPanel().selectedRowTable,2);
				data[3] = (String)vista.getRecentsTrPanel().sorter.getValueAt(vista.getRecentsTrPanel().selectedRowTable,3);
				data[4] = (String)vista.getRecentsTrPanel().sorter.getValueAt(vista.getRecentsTrPanel().selectedRowTable,4);
				
				//Mostrar el panel de informacin de las transformaciones recientes
				InfoCRSPanel info = new InfoCRSPanel(data[2].split(":")[0], Integer.parseInt(data[2].split(":")[1]) , data[0]+" <--> "+ data[4]);
				PluginServices.getMDIManager().addWindow(info);
			}
			//Al cambiar el item seleccionado del combo
			if(e.getSource().equals(vista.getJComboOptions())){
				CardLayout cl = (CardLayout)(vista.getPCenter().getLayout());
				if(vista.getJComboOptions().getSelectedItem().equals(PluginServices.getText(this,"recents_transformation"))){
					//Mostrar el panel de transformacin reciente
					 cl.show(vista.getPCenter(), "recents");
					 if (vista.getRecentsTrPanel().selectedRowTable == -1) {
						 vista.setTargetTransformation(null);
						 panel.getJButtonAccept().setEnabled(false);
					 } else {
						String[] transform = ((String)vista.getRecentsTrPanel().sorter.getValueAt(vista.getRecentsTrPanel().selectedRowTable,0)).split(":");
						if (transform[0].equals("USR")){
							vista.getRecentsTrPanel().setParamsManual((String)vista.getRecentsTrPanel().sorter.getValueAt(vista.getRecentsTrPanel().selectedRowTable,4));
							vista.setTargetTransformation(vista.getRecentsTrPanel().getParams());
			        	}
						if (transform[0].equals("EPSG")){
							TransEPSG epsgParams = new TransEPSG(vista.getRecentsTrPanel().getTrCode(),vista.getEpsgTrPanel().connect, vista.getRecentsTrPanel().getInverseTransformation());
							vista.getRecentsTrPanel().setParamsEPGS(epsgParams.getParamValue());
							vista.setTargetTransformation(vista.getRecentsTrPanel().getParams());						
			        	}
						if (transform[0].equals("NADGR")){
							String p = (String)vista.getRecentsTrPanel().sorter.getValueAt(vista.getRecentsTrPanel().selectedRowTable,4);
							vista.setTargetTransformation(vista.getNadsParams(p));
						}
						 panel.getJButtonAccept().setEnabled(true);
					 }
				}
				else if(vista.getJComboOptions().getSelectedItem().equals(PluginServices.getText(this,"transformacion_epsg"))){
					//mostrar el panel de transformacin epsg
					 cl.show(vista.getPCenter(), "epsg");
					 if (vista.getEpsgTrPanel().selectedRowTable == -1) {
						 vista.setTargetTransformation(null);
						 panel.getJButtonAccept().setEnabled(false);
					 } else {
						TransEPSG epsgParams = new TransEPSG(vista.getEpsgTrPanel().getTransformation_code(),vista.getEpsgTrPanel().connect, vista.getEpsgTrPanel().inverseTranformation);
						vista.getEpsgTrPanel().setValues(epsgParams.getParamValue());	
						vista.setTargetTransformation(vista.getParamsEpsg(vista.getEpsgTrPanel().getValues()));
						panel.getJButtonAccept().setEnabled(true);
					 }
					 
		        }
				else if(vista.getJComboOptions().getSelectedItem().equals(PluginServices.getText(this,"transformacion_manual"))){
					//mostrar el panel de transformacin manual
					 cl.show(vista.getPCenter(), "manual");
					 if(!vista.getManualTrPanel().getStatus()) 
							panel.getJButtonAccept().setEnabled(false);		
						else {
							panel.getJButtonAccept().setEnabled(true);
							vista.setTargetTransformation(vista.getManualParams());
						}
				}
				else if(vista.getJComboOptions().getSelectedItem().equals(PluginServices.getText(this,"nadgrids"))){
					//mostrar el panel de nadgrids
					 cl.show(vista.getPCenter(), "nad");
					 if (vista.getNadsTrPanel().getJComboNadFile().getSelectedIndex()!=0) {
						 vista.setTargetTransformation("+nadgrids="+vista.getNadsTrPanel().getNadFile());
						 panel.getJButtonAccept().setEnabled(true);
					 }					 
					 else {
						 vista.setTargetTransformation(null);	
						 panel.getJButtonAccept().setEnabled(false);
					 }
				}
			}
		}
	}

	public void valueChanged(ListSelectionEvent e) {
		if (capa != null) {
			if (e.getSource() == capa.getEpsgTrPanel().getJTable().getSelectionModel()){
				ListSelectionModel lsm = (ListSelectionModel)e.getSource();
		        if (lsm.isSelectionEmpty()) {
		        	capa.getEpsgTrPanel().selectedRowTable = -1;	        	
		        	panel.getJButtonNext().setEnabled(false);
		        	if (panel.getJButtonAccept().isEnabled()) {
		        		condition = true;
		        	}
		        	panel.getJButtonAccept().setEnabled(false);
		        } 
		        else {
		        	capa.getEpsgTrPanel().selectedRowTable = lsm.getMinSelectionIndex();
		        	capa.getEpsgTrPanel().setTrasformation_code(Integer.parseInt((String)capa.getEpsgTrPanel().sorter.getValueAt(capa.getEpsgTrPanel().selectedRowTable,0)));
		            String sentence = "SELECT target_crs_code " +
									"FROM epsg_coordoperation " +                        
									"WHERE coord_op_code = " + capa.getEpsgTrPanel().getTransformation_code() ;
		            ResultSet result = Query.select(sentence,capa.getEpsgTrPanel().connect.getConnection());
		            try {
						result.next();
						int tar = result.getInt("target_crs_code");
						if (tar == capa.getEpsgTrPanel().crs_target) 
							capa.getEpsgTrPanel().inverseTranformation = false;					
						else capa.getEpsgTrPanel().inverseTranformation = true;
					} catch (SQLException e1) {							
						e1.printStackTrace();
					}
										
					TransEPSG epsgParams = new TransEPSG(capa.getEpsgTrPanel().getTransformation_code(),capa.getEpsgTrPanel().connect, capa.getEpsgTrPanel().inverseTranformation);
					capa.getEpsgTrPanel().setValues(epsgParams.getParamValue());	
					capa.setSourceTransformation(capa.getParamsEpsg(capa.getEpsgTrPanel().getValues()));
					if (condition) {
						panel.getJButtonAccept().setEnabled(true);
						condition = false;
					}
					panel.getJButtonNext().setEnabled(true);					
		        }
			}
			
			/*
			 * una vez cambiamos la transformacion a elegir, habra que ver que parametros
			 * tenemos que definir para que cuando aceptemos hayan sido definidos
			 * anteriormente
			 */
			if (e.getSource() == capa.getRecentsTrPanel().getJTable().getSelectionModel()){
				ListSelectionModel lsm = (ListSelectionModel)e.getSource();
		        if (lsm.isSelectionEmpty()) {
		        	capa.getRecentsTrPanel().selectedRowTable = -1;
		        	capa.getRecentsTrPanel().setCode(0);
		        	panel.getJButtonNext().setEnabled(false);
		        	capa.getRecentsTrPanel().getJButtonInfo().setEnabled(false);
		        	if (panel.getJButtonAccept().isEnabled()) {
		        		condition = true;
		        	}
		        	panel.getJButtonAccept().setEnabled(false);
		        } 
		        else {
		        	capa.getRecentsTrPanel().selectedRowTable = lsm.getMinSelectionIndex();
		        	String[] cad = ((String)capa.getRecentsTrPanel().sorter.getValueAt(capa.getRecentsTrPanel().selectedRowTable,2)).split(":");	        	
		        	capa.getRecentsTrPanel().setCode(Integer.parseInt(cad[1]));
		        	String[] transformation = ((String)capa.getRecentsTrPanel().sorter.getValueAt(capa.getRecentsTrPanel().selectedRowTable,0)).split(":");
		        	if (transformation[0].equals("EPSG")){	        		
		        		capa.getRecentsTrPanel().setTrCode(Integer.parseInt(transformation[1]));	
		        	}
		        	capa.getRecentsTrPanel().getJButtonInfo().setEnabled(true);	        	
		        			        	
		        	String[] transform = ((String)capa.getRecentsTrPanel().sorter.getValueAt(capa.getRecentsTrPanel().selectedRowTable,0)).split(":");
					if (transform[0].equals("USR")){
						capa.getRecentsTrPanel().setParamsManual((String)capa.getRecentsTrPanel().sorter.getValueAt(capa.getRecentsTrPanel().selectedRowTable,4));
		        		capa.setSourceTransformation(capa.getRecentsTrPanel().getParams());
		        	}
					if (transform[0].equals("EPSG")){
						TransEPSG epsgParams = new TransEPSG(capa.getRecentsTrPanel().getTrCode(),capa.getEpsgTrPanel().connect, capa.getRecentsTrPanel().getInverseTransformation());
						capa.getRecentsTrPanel().setParamsEPGS(epsgParams.getParamValue());
						capa.setSourceTransformation(capa.getRecentsTrPanel().getParams());						
		        	}
					if (transform[0].equals("NADGR")){
						String p = (String)capa.getRecentsTrPanel().sorter.getValueAt(capa.getRecentsTrPanel().selectedRowTable,4);
						capa.setSourceTransformation(capa.getNadsParams(p));
					}					
		        	panel.getJButtonNext().setEnabled(true);
		        	if (condition = true) {
						panel.getJButtonAccept().setEnabled(true);
						condition = false;
					}
		        }
			}
		}
		
		if (vista != null) {
			if (e.getSource() == vista.getEpsgTrPanel().getJTable().getSelectionModel()){
				ListSelectionModel lsm = (ListSelectionModel)e.getSource();
		        if (lsm.isSelectionEmpty()) {
		        	vista.getEpsgTrPanel().selectedRowTable = -1;	        	
		        	panel.getJButtonAccept().setEnabled(false);
		        } 
		        else {
		        	vista.getEpsgTrPanel().selectedRowTable = lsm.getMinSelectionIndex();
		        	vista.getEpsgTrPanel().setTrasformation_code(Integer.parseInt((String)vista.getEpsgTrPanel().sorter.getValueAt(vista.getEpsgTrPanel().selectedRowTable,0)));
		            String sentence = "SELECT target_crs_code " +
									"FROM epsg_coordoperation " +                        
									"WHERE coord_op_code = " + vista.getEpsgTrPanel().getTransformation_code() ;
		            ResultSet result = Query.select(sentence,vista.getEpsgTrPanel().connect.getConnection());
		            try {
						result.next();
						int tar = result.getInt("target_crs_code");
						if (tar == vista.getEpsgTrPanel().crs_target) 
							vista.getEpsgTrPanel().inverseTranformation = false;					
						else vista.getEpsgTrPanel().inverseTranformation = true;
					} catch (SQLException e1) {							
						e1.printStackTrace();
					}
					
					/**
					 * revisar esto
					 */
					TransEPSG epsgParams = new TransEPSG(vista.getEpsgTrPanel().getTransformation_code(),vista.getEpsgTrPanel().connect, vista.getEpsgTrPanel().inverseTranformation);
					vista.getEpsgTrPanel().setValues(epsgParams.getParamValue());	
					vista.setTargetTransformation(vista.getParamsEpsg(vista.getEpsgTrPanel().getValues()));
					panel.getJButtonAccept().setEnabled(true);
		        }
			}
			
			/*
			 * una vez cambiamos la transformacion a elegir, habra que ver que parametros
			 * tenemos que definir para que cuando aceptemos hayan sido definidos
			 * anteriormente
			 */
			if (e.getSource() == vista.getRecentsTrPanel().getJTable().getSelectionModel()){
				ListSelectionModel lsm = (ListSelectionModel)e.getSource();
		        if (lsm.isSelectionEmpty()) {
		        	vista.getRecentsTrPanel().selectedRowTable = -1;
		        	vista.getRecentsTrPanel().setCode(0);
		        	panel.getJButtonAccept().setEnabled(false);
		        	vista.getRecentsTrPanel().getJButtonInfo().setEnabled(false);
		        } 
		        else {
		        	vista.getRecentsTrPanel().selectedRowTable = lsm.getMinSelectionIndex();
		        	String[] cad = ((String)vista.getRecentsTrPanel().sorter.getValueAt(vista.getRecentsTrPanel().selectedRowTable,2)).split(":");	        	
		        	vista.getRecentsTrPanel().setCode(Integer.parseInt(cad[1]));
		        	String[] transformation = ((String)vista.getRecentsTrPanel().sorter.getValueAt(vista.getRecentsTrPanel().selectedRowTable,0)).split(":");
		        	if (transformation[0].equals("EPSG")){	        		
		        		vista.getRecentsTrPanel().setTrCode(Integer.parseInt(transformation[1]));	
		        	}
		        	vista.getRecentsTrPanel().getJButtonInfo().setEnabled(true);	        	
		        	
		        	/**
		        	 * revisar esto
		        	 */
		        	String[] transform = ((String)vista.getRecentsTrPanel().sorter.getValueAt(vista.getRecentsTrPanel().selectedRowTable,0)).split(":");
					if (transform[0].equals("USR")){
						vista.getRecentsTrPanel().setParamsManual((String)vista.getRecentsTrPanel().sorter.getValueAt(vista.getRecentsTrPanel().selectedRowTable,4));
						vista.setTargetTransformation(vista.getRecentsTrPanel().getParams());
		        	}
					if (transform[0].equals("EPSG")){
						TransEPSG epsgParams = new TransEPSG(vista.getRecentsTrPanel().getTrCode(),vista.getEpsgTrPanel().connect, vista.getRecentsTrPanel().getInverseTransformation());
						vista.getRecentsTrPanel().setParamsEPGS(epsgParams.getParamValue());
						vista.setTargetTransformation(vista.getRecentsTrPanel().getParams());						
		        	}
					if (transform[0].equals("NADGR")){
						String p = (String)vista.getRecentsTrPanel().sorter.getValueAt(vista.getRecentsTrPanel().selectedRowTable,4);
						vista.setTargetTransformation(vista.getNadsParams(p));
					}
		        	
		        	panel.getJButtonAccept().setEnabled(true);
		        }
			}
		}
	}

	public void itemStateChanged(ItemEvent e) {
		if (capa != null) {
			if (e.getSource() == capa.getNadsTrPanel().getJComboNadFile()){
				if (capa.getNadsTrPanel().getJComboNadFile().getSelectedIndex()!=0){
					capa.getNadsTrPanel().setNadFile(capa.getNadsTrPanel().getJComboNadFile().getSelectedItem().toString());//nadFile = getJComboNadFile().getSelectedItem().toString();
					capa.getNadsTrPanel().getTreePanel().setRoot(PluginServices.getText(this,"grids_en")+": "+capa.getNadsTrPanel().getNadFile());//nadFile);
					capa.getNadsTrPanel().initializeTree();
					// Guardar en persistencia nadFile:
					capa.getNadsTrPanel().saveNadFileName(capa.getNadsTrPanel().getNadFile());//nadFile);
					capa.setSourceTransformation("+nadgrids="+capa.getNadsTrPanel().getNadFile());
					panel.getJButtonNext().setEnabled(true);
					if(condition) {
						panel.getJButtonAccept().setEnabled(true);
						condition = false;
					}
				}
				else{
					capa.getNadsTrPanel().setNadFile(null);//nadFile = null;
					capa.getNadsTrPanel().getTreePanel().setRoot("");
					capa.setSourceTransformation(null);					
					panel.getJButtonNext().setEnabled(false);
					if (panel.getJButtonAccept().isEnabled())
						condition = true;
					panel.getJButtonAccept().setEnabled(false);
				}
				
			}
		}
		
		if (vista != null) {
			if (e.getSource() == vista.getNadsTrPanel().getJComboNadFile()){
				if (vista.getNadsTrPanel().getJComboNadFile().getSelectedIndex()!=0){
					vista.getNadsTrPanel().setNadFile(vista.getNadsTrPanel().getJComboNadFile().getSelectedItem().toString());//nadFile = getJComboNadFile().getSelectedItem().toString();
					vista.getNadsTrPanel().getTreePanel().setRoot(PluginServices.getText(this,"grids_en")+": "+vista.getNadsTrPanel().getNadFile());//nadFile);
					vista.getNadsTrPanel().initializeTree();
					// Guardar en persistencia nadFile:
					vista.getNadsTrPanel().saveNadFileName(vista.getNadsTrPanel().getNadFile());//nadFile);
					vista.setTargetTransformation("+nadgrids="+vista.getNadsTrPanel().getNadFile());
					panel.getJButtonAccept().setEnabled(true);
				}
				else{
					vista.getNadsTrPanel().setNadFile(null);//nadFile = null;
					vista.getNadsTrPanel().getTreePanel().setRoot("");
					vista.setTargetTransformation(null);
					panel.getJButtonAccept().setEnabled(false);
				}
				
			}
		}
	}

	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void keyReleased(KeyEvent e) {
		if (capa != null) {
			if(!capa.getManualTrPanel().getStatus()) 
				panel.getJButtonNext().setEnabled(false);		
			else {
				panel.getJButtonNext().setEnabled(true);
				capa.setSourceTransformation(capa.getManualParams());
			}
		}
		
		if (vista != null) {
			if(!vista.getManualTrPanel().getStatus()) 
				panel.getJButtonAccept().setEnabled(false);		
			else {
				panel.getJButtonAccept().setEnabled(true);
				vista.setTargetTransformation(vista.getManualParams());
			}
		}		
	}

	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
