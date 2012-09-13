/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
*
* Copyright (C) 2007 Instituto de Desarrollo Regional and Generalitat Valenciana.
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

package org.gvsig.remotesensing.scatterplot.listener;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelEvent;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelListener;
import org.gvsig.gui.beans.table.exceptions.NotInitializeException;
import org.gvsig.raster.IProcessActions;
import org.gvsig.raster.grid.roi.ROI;
import org.gvsig.remotesensing.scatterplot.chart.ROIChart;
import org.gvsig.remotesensing.scatterplot.gui.ManagerROIChartPanel;


/**
 * Listener para el gestor de rois sobre el grafico
 * 
 * @author Alejandro Muñoz Sanchez (alejandro.munoz@uclm.es)  
 * @version 11/12/2007
 */

public class ManagerROIChartPanelListener implements ButtonsPanelListener, ActionListener, ListSelectionListener, TableModelListener, IProcessActions{
		
		private ManagerROIChartPanel tablePanel = null;
		private String 		  		 roiSelectedName = "";
		private ROI 		 		 lastDefinedRoi = null;
	
		/**
		 * Constructor
		 * */
		public ManagerROIChartPanelListener(ManagerROIChartPanel tablePanel) {
			this.tablePanel = tablePanel;
		}
		

		public void actionPerformed(ActionEvent e) {
			
			if (e.getSource() == tablePanel.getExportButton()){
				ROIChart roiToExport=  tablePanel.getDiagram().getActiveRoiChart();
				RoiFromChartProcess process= new RoiFromChartProcess();
				process.addParam("roi",roiToExport);
				process.addParam("raster",(FLyrRasterSE)tablePanel.getScatterPlotPanel().getFLayer());
				process.setActions(this);
				// Mirar si es apropiado siempre mostrar la barra o solo en el caso layer grandes
				process.start();
				
				
			}else if (e.getSource() == tablePanel.getDeleteButton()){
				try {
					if (tablePanel.getTable().getSelectedRows().length>0){
						String className = (String)tablePanel.getTable().getModel().getValueAt(tablePanel.getTable().getSelectedRow(),0);
						tablePanel.removeROI(className);
						// Borrado de la roi de la tabla
						((DefaultTableModel)tablePanel.getTable().getModel()).removeRow(tablePanel.getTable().getSelectedRow());
						String roiName= tablePanel.getDiagram().getActiveRoiChart().getName();
						// Borrado de la roi de la lista de rois 
						tablePanel.getDiagram().getROIChartList().deleteROI(roiName);
						
						if(tablePanel.getTable().getRowCount()>0){
							int selectedRow = 0;
							roiSelectedName = (String)tablePanel.getTable().getTable().getJTable().getValueAt(selectedRow,0);
							// Se establece la roi activa.
							tablePanel.getDiagram().setActiveRoi((ROIChart)tablePanel.getDiagram().getROIChartList().getListRois().get(roiSelectedName));
							tablePanel.getDiagram().updateUI();
						}
						
						else{
							// Se crea una nueva ROiChar que sera la activa
							tablePanel.getDiagram().newRoiChart();	
						}
					}
				
				} catch (NotInitializeException e1) {
							e1.printStackTrace();
				}
				
			} else if (e.getSource() == tablePanel.getNewRoiButtom()){
				tablePanel.getDiagram().newRoiChart();
				tablePanel.updateTable();
				tablePanel.updateUI();
			}
		}

		public void valueChanged(ListSelectionEvent e) {
			try {
				if (tablePanel.getTable().getSelectedRows().length<=0){
					tablePanel.setPreviousTool();
				}
				else{
					// Cambio en la seleccion de la tabla
					tablePanel.selectDrawRoiTool();
					int selectedRow = tablePanel.getTable().getSelectedRow();
					roiSelectedName = (String)tablePanel.getTable().getTable().getJTable().getValueAt(selectedRow,0);
					tablePanel.getDiagram().setActiveRoi((ROIChart)tablePanel.getDiagram().getROIChartList().getListRois().get(roiSelectedName));
					tablePanel.getDiagram().updateUI();
				//	tablePanel.getDiagram().setActiveRoi()
					}
			} catch (NotInitializeException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}	
		}

		public void tableChanged(TableModelEvent e) {
			/*
			 * Cambio de color en una ROI:
			 */
			if(e.getColumn()==4){
				String roiName = (String)tablePanel.getTable().getTable().getJTable().getValueAt(e.getFirstRow(),0);
				Color color = (Color)tablePanel.getTable().getTable().getJTable().getValueAt(e.getFirstRow(),4);
				tablePanel.changeRoiColor(roiName,color);
				tablePanel.updateUI();
					
			}else if(e.getColumn()==0){
				String newName = (String)tablePanel.getTable().getTable().getJTable().getValueAt(e.getFirstRow(),0);
				tablePanel.changeRoiName(roiSelectedName,newName);
				roiSelectedName = newName;
			}
	
		}
		
	
	public void actionButtonPressed(ButtonsPanelEvent e) {
		// TODO Auto-generated method stub
		
	}


	public void interrupted() {	}


	public void end(Object param) {
		lastDefinedRoi = (ROI)param;	
		ArrayList rois= ((FLyrRasterSE)tablePanel.getFLayer()).getRois();
		if(rois!= null){
			rois.add(lastDefinedRoi);
			((FLyrRasterSE)tablePanel.getFLayer()).setRois(rois);
		}
		else{
			rois= new ArrayList();
			rois.add(lastDefinedRoi);
			((FLyrRasterSE)tablePanel.getFLayer()).setRois(rois);
		}

	}
		
				
	
}
