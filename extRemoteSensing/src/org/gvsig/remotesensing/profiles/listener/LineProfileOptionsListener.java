/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
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
*   Av. Blasco Ib��ez, 50
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

package org.gvsig.remotesensing.profiles.listener;

/**
 * Clase que define el comportamiento para los eventos de un LineProfileOptionsPanel.
 * 
 * @author aMu�oz (alejandro.munoz@uclm.es) 
 * @see LineProfileOptionsPanel 
 * @version 11/3/2008
 */

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import org.gvsig.fmap.mapcontext.layers.GraphicLayer;
import org.gvsig.fmap.mapcontext.rendering.legend.FGraphic;
import org.gvsig.fmap.mapcontext.rendering.symbols.ISymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.SymbologyFactory;
import org.gvsig.fmap.raster.grid.roi.VectorialROI;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelEvent;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelListener;
import org.gvsig.gui.beans.table.exceptions.NotInitializeException;
import org.gvsig.gui.beans.table.models.IModel;
import org.gvsig.raster.buffer.RasterBuffer;
import org.gvsig.raster.grid.GridException;
import org.gvsig.raster.grid.roi.ROI;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.remotesensing.profiles.gui.LineProfileOptionsPanel;

import com.iver.andami.PluginServices;

public class LineProfileOptionsListener implements ButtonsPanelListener, ActionListener, ListSelectionListener, TableModelListener  {

	LineProfileOptionsPanel optionsPanel = null;
	int createRois =0;
	private boolean delete= false;
	
	/**
	 * Constructor 
	 * */
	public LineProfileOptionsListener( LineProfileOptionsPanel optionsPanel){
		this.optionsPanel= optionsPanel;
    
	}
	
	
	public void actionPerformed(ActionEvent e) {
		
		// Acciones a ejecutar cuando se crea una nueva entrada en la tabla
		// Construccion de la roi (vacia) que espera el dibujado de la geometria de tipo punto 
		// selecci�n de la herramienta de dibujado de point
		
		if (e.getSource() == optionsPanel.getNewButton()){
			try {
				String roiName = PluginServices.getText(this,"line") + String.valueOf(createRois);
				createRois++;
				Object row [] = ((IModel)optionsPanel.getTable().getModel()).getNewLine();
				row[0]= roiName;
				((DefaultTableModel)optionsPanel.getTable().getModel()).addRow(row);
				
				if (optionsPanel.getGrid()!=null){
					ROI roi = new VectorialROI(optionsPanel.getGrid());
					roi.setName(roiName);
					roi.setColor((Color)row[1]);
					optionsPanel.addROI(roi);
				}
				optionsPanel.getTable().setSelectedIndex(optionsPanel.getTable().getRowCount()-1);
			
			} catch (NotInitializeException e1) {
				RasterToolsUtil.messageBoxError("tabla_no_inicializada", this, e1);	
			}
			optionsPanel.selectDrawRoiTool();
			optionsPanel.getNewButton().setSelected(true);
			optionsPanel.getDeleteButton().setSelected(false);
		}
		
		// Seleccion de la banda cuyo perfil de linea se pretende representar.
		// Seleccionada la banda se actualiza la grafica y los datos en la tabla de la roi seleccionada
		if (e.getSource() == optionsPanel.getComboBands()){
			
			try {
				if(optionsPanel.getTable().getRowCount()>0){
					ROI lineRoi =(VectorialROI)optionsPanel.getROIs().get(optionsPanel.getTable().getSelectedRow());
					int band = optionsPanel.getComboBands().getSelectedIndex();
					drawChartRoi((VectorialROI)lineRoi,band);
				}
				
			} catch (NotInitializeException e1) {
				RasterToolsUtil.messageBoxError("tabla_no_inicializada", this, e1);	
			}
			optionsPanel.selectDrawRoiTool();
		}
			
		// Borrado de una roi
		if (e.getSource() == optionsPanel.getDeleteButton()){
			optionsPanel.getDeleteButton().setSelected(false);
			delete=true;
			try {
				if (optionsPanel.getTable().getRowCount()>0){
					optionsPanel.getJPanelChart().cleanChart();
					String className = (String)optionsPanel.getTable().getModel().getValueAt(optionsPanel.getTable().getSelectedRow(),0);
					optionsPanel.removeROI(className);
					((DefaultTableModel)optionsPanel.getTable().getModel()).removeRow(optionsPanel.getTable().getSelectedRow());
					
					if(optionsPanel.getTable().getRowCount()>0){
						optionsPanel.getTable().setSelectedIndex(0);
						ROI lineRoi =(VectorialROI)optionsPanel.getROIs().get(optionsPanel.getTable().getSelectedRow());
						int band = optionsPanel.getComboBands().getSelectedIndex();
						drawChartRoi((VectorialROI)lineRoi,band);
					}
					delete=false;
				}
				
			} catch (NotInitializeException e2) {
				RasterToolsUtil.messageBoxError("tabla_no_inicializada", this, e2);	
			} 
		}			
	}

	
	/**
	 * Acciones cuando se produce una modificaci�n en la tabla
	 * */
	public void tableChanged(TableModelEvent e) {
		// Corregir segun cual sea definitivamente  la columna asociada al color
		if(e.getColumn()==1){
			String roiName = (String)optionsPanel.getTable().getTable().getJTable().getValueAt(e.getFirstRow(),0);
			ArrayList graphics = optionsPanel.getRoiGraphics(roiName);
			if(optionsPanel.getMapControl()!=null){
				GraphicLayer graphicLayer = optionsPanel.getMapControl().getMapContext().getGraphicsLayer();
				ISymbol symbol = null;
				Color color = (Color)optionsPanel.getTable().getTable().getJTable().getValueAt(e.getFirstRow(),1);
				for (int i = 0; i< graphics.size(); i++){
					symbol = SymbologyFactory.createDefaultSymbolByShapeType(((FGraphic)graphics.get(i)).getGeom().getGeometryType().getType(), color);
					((FGraphic)graphics.get(i)).setIdSymbol(graphicLayer.addSymbol(symbol));
				}
				
				optionsPanel.getROI(roiName).setColor(color);
				optionsPanel.getMapControl().drawGraphics();
			}
		optionsPanel.SetColorSeriesChart();
		}
	}
	
	
	public void valueChanged(ListSelectionEvent e) {	
		try {
			if(optionsPanel.getTable().getRowCount()>0 && !delete){			
				VectorialROI lineRoi =(VectorialROI)optionsPanel.getROIs().get(optionsPanel.getTable().getSelectedRow());
				int band = optionsPanel.getComboBands().getSelectedIndex();
				
				if(lineRoi.getGeometries().size()>0)
					drawChartRoi((VectorialROI)lineRoi,band);
			}
		} catch (NotInitializeException e3) {
			RasterToolsUtil.messageBoxError("tabla_no_inicializada", this, e3);	
		}
	}

	/** 
	 * 	Metodo que se encarga de pintar la roi que se pasa como parametro sobre el grafico.
	 * 	La banda de la que se tomar�n los valores es la que se pasa como par�metro.
	 * */
	private  void drawChartRoi(VectorialROI roi, int band){
		try {
			int[][] series;
			series = new int[1][roi.getValues()];
			String[] names = new String[1];
			names[0] = "Grafico";
			roi.setBandToOperate(band);
			series = new int[1][roi.getValues()];
			int k=0;
			int z=0;
			if(roi.getGrid().getDataType() == RasterBuffer.TYPE_BYTE){
				for (int i = 0; i < roi.getNX(); i++){
					for (int j = 0; j < roi.getNY(); j++){	
						z= (int) roi.getCellValueAsByte(i,j);
						if(!roi.isNoDataValue(z)){
							series[0][k] =z;
							k++;
						}
					}
				}
			}
			else if(roi.getGrid().getDataType() == RasterBuffer.TYPE_SHORT){
				for (int i = 0; i < roi.getNX(); i++){
					for (int j = 0; j < roi.getNY(); j++){	
						z= (int) roi.getCellValueAsShort(i,j);
						if(!roi.isNoDataValue(z)){
							series[0][k] =z;
							k++;
						}
					}
				}
			}
			else if(roi.getGrid().getDataType() == RasterBuffer.TYPE_INT){
				for (int i = 0; i < roi.getNX(); i++){
					for (int j = 0; j < roi.getNY(); j++){	
						z= (int) roi.getCellValueAsInt(i,j);
						if(!roi.isNoDataValue(z)){
							series[0][k] =z;
							k++;
						}
					}
				}
			}
			else if(roi.getGrid().getDataType() == RasterBuffer.TYPE_FLOAT){
				for (int i = 0; i < roi.getNX(); i++){
					for (int j = 0; j < roi.getNY(); j++){	
						z= (int) roi.getCellValueAsFloat(i,j);
						if(!roi.isNoDataValue(z)){
							series[0][k] =z;
							k++;
						}
					}
				}
			}
			else if(roi.getGrid().getDataType() == RasterBuffer.TYPE_DOUBLE){
				for (int i = 0; i < roi.getNX(); i++){
					for (int j = 0; j < roi.getNY(); j++){	
						z= (int) roi.getCellValueAsDouble(i,j);
						if(!roi.isNoDataValue(z)){
							series[0][k] =z;
							k++;
						}
					}
				}
			}
			
			// Actualizacion del grafico
			optionsPanel.SetColorSeriesChart();
			optionsPanel.getJPanelChart().setNewChart(series, names);
		
			// Actualizacion de la tabla
			int selectedRow = optionsPanel.getTable().getSelectedRow();
			roi.setBandToOperate(band);
			double max=roi.getMaxValue();
			double min= roi.getMinValue();
			double mean = roi.getMeanValue();
			optionsPanel.getTable().getModel().setValueAt(new Double(max), selectedRow, 2);
			optionsPanel.getTable().getModel().setValueAt(new Double(min), selectedRow, 3);
			optionsPanel.getTable().getModel().setValueAt(new Double(mean), selectedRow, 4);
			optionsPanel.updateUI();
		} catch (GridException e) {
			RasterToolsUtil.messageBoxError(PluginServices.getText(this, "grid_error"), this, e);
		} catch (NotInitializeException e) {
			RasterToolsUtil.messageBoxError("tabla_no_inicializada", this, e);	
		}
	}
			
	
	public void actionButtonPressed(ButtonsPanelEvent e) {
	}

}
