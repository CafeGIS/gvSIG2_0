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

package org.gvsig.rastertools.roi.ui.listener;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.mapcontext.exceptions.LoadLayerException;
import org.gvsig.fmap.mapcontext.layers.GraphicLayer;
import org.gvsig.fmap.mapcontext.rendering.legend.FGraphic;
import org.gvsig.fmap.mapcontext.rendering.symbols.ISymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.SymbologyFactory;
import org.gvsig.fmap.raster.grid.roi.InvalidROIsShpException;
import org.gvsig.fmap.raster.grid.roi.VectorialROI;
import org.gvsig.fmap.raster.grid.roi.VectorialROIsReader;
import org.gvsig.fmap.raster.grid.roi.VectorialROIsWriter;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelEvent;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelListener;
import org.gvsig.gui.beans.table.exceptions.NotInitializeException;
import org.gvsig.gui.beans.table.models.IModel;
import org.gvsig.raster.dataset.FileNotExistsException;
import org.gvsig.raster.grid.GridException;
import org.gvsig.raster.grid.roi.ROI;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.rastertools.roi.ui.ROIsTablePanel;

/**
 * Listener para el panel ROIsTablePanel
 *
 * @author Diego Guerrero Sevilla (diego.guerrero@uclm.es)
 *
 */
public class ROIsTablePanelListener implements ButtonsPanelListener, ActionListener, ListSelectionListener, TableModelListener{

	private ROIsTablePanel tablePanel = null;
	private String 		   roiSelectedName = "";

	public ROIsTablePanelListener(ROIsTablePanel tablePanel) {
		this.tablePanel = tablePanel;
	}

	public void actionButtonPressed(ButtonsPanelEvent e) {
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == tablePanel.getNewButton()){
			String roiName;
			try {
				roiName = "ROI" + String.valueOf(tablePanel.getTable().getRowCount());
				Object row [] = ((IModel)tablePanel.getTable().getModel()).getNewLine();
				row[0]= roiName;
				((DefaultTableModel)tablePanel.getTable().getModel()).addRow(row);
				if (tablePanel.getGrid()!=null){
					ROI roi = new VectorialROI(tablePanel.getGrid());
					roi.setName(roiName);
					roi.setColor((Color)row[4]);
					tablePanel.addROI(roi);
				}
				tablePanel.getTable().setSelectedIndex(tablePanel.getTable().getRowCount()-1);
				tablePanel.selectDrawRoiTool();
				tablePanel.setToolsEnabled(true);
			} catch (NotInitializeException e1) {
				RasterToolsUtil.messageBoxError("error_tabla_rois", tablePanel, e1);
				if (tablePanel.getManagerPanel() != null)
					tablePanel.getManagerPanel().getRoiManagerDialog().close();
			}
		}else if (e.getSource() == tablePanel.getDeleteButton()){
			try {
				if (tablePanel.getTable().getSelectedRows().length>0){
					String className = (String)tablePanel.getTable().getModel().getValueAt(tablePanel.getTable().getSelectedRow(),0);
					tablePanel.removeROI(className);
					((DefaultTableModel)tablePanel.getTable().getModel()).removeRow(tablePanel.getTable().getSelectedRow());
				}
			} catch (NotInitializeException e1) {
				RasterToolsUtil.messageBoxError("error_tabla_rois", tablePanel, e1);
				if (tablePanel.getManagerPanel() != null)
					tablePanel.getManagerPanel().getRoiManagerDialog().close();
			}
		}else if (e.getSource() == tablePanel.getPointToolButton()){
			tablePanel.getPointToolButton().setSelected(true);
			tablePanel.getLineToolButton().setSelected(false);
			tablePanel.getPolygonToolButton().setSelected(false);
			tablePanel.selectDrawRoiTool();
		}else if (e.getSource() == tablePanel.getLineToolButton()){
			tablePanel.getLineToolButton().setSelected(true);
			tablePanel.getPointToolButton().setSelected(false);
			tablePanel.getPolygonToolButton().setSelected(false);
			tablePanel.selectDrawRoiTool();
		}else if (e.getSource() == tablePanel.getPolygonToolButton()){
			tablePanel.getPolygonToolButton().setSelected(true);
			tablePanel.getLineToolButton().setSelected(false);
			tablePanel.getPointToolButton().setSelected(false);
			tablePanel.selectDrawRoiTool();
		}
		else if (e.getSource() == tablePanel.getExportButton()){
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.removeChoosableFileFilter(fileChooser.getAcceptAllFileFilter());
			fileChooser.addChoosableFileFilter(new ShpFileFilter());
			if (fileChooser.showSaveDialog(tablePanel) == JFileChooser.APPROVE_OPTION){
				File file = fileChooser.getSelectedFile();
				VectorialROIsWriter writer = new VectorialROIsWriter(file.getPath(),tablePanel.getFLayer().getProjection());
				writer.write((VectorialROI[])tablePanel.getROIs().toArray(new VectorialROI[0]));
			}
		}else if (e.getSource() == tablePanel.getImportButton()){
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.removeChoosableFileFilter(fileChooser.getAcceptAllFileFilter());
			fileChooser.addChoosableFileFilter(new ShpFileFilter());
			if (fileChooser.showOpenDialog(tablePanel) == JFileChooser.APPROVE_OPTION){
				File file = fileChooser.getSelectedFile();
				try {
					VectorialROIsReader reader = new VectorialROIsReader(file.getPath(),tablePanel.getGrid(),tablePanel.getFLayer().getProjection());
					ArrayList rois = reader.read(tablePanel.getROIs());
					tablePanel.clearROIs();
					tablePanel.setROIs(rois);
				} catch (LoadLayerException e1) {
					RasterToolsUtil.messageBoxError("error_file_not_valid", tablePanel, e1);
				} catch (FileNotExistsException e1) {
					RasterToolsUtil.messageBoxError("error_file_not_found", tablePanel, e1);
				} catch (ReadException e1) {
					RasterToolsUtil.messageBoxError("error_file_not_valid", tablePanel, e1);
				} catch (GridException e1) {
					RasterToolsUtil.messageBoxError("error_creating_rois", tablePanel, e1);
				} catch (InvalidROIsShpException e1) {
					RasterToolsUtil.messageBoxError("error_file_not_valid", tablePanel, e1);
				} catch (DataException e1) {
					RasterToolsUtil.messageBoxError("error_file_not_valid", tablePanel, e1);
				}
			}
		}
	}

	public void valueChanged(ListSelectionEvent e) {
		try {
			if (tablePanel.getTable().getSelectedRows().length<=0){
				tablePanel.setToolsEnabled(false);
				tablePanel.setPreviousTool();
			}
			else{
				tablePanel.setToolsEnabled(true);
				tablePanel.selectDrawRoiTool();

				int selectedRow = tablePanel.getTable().getSelectedRow();
				roiSelectedName = (String)tablePanel.getTable().getTable().getJTable().getValueAt(selectedRow,0);
				}
		} catch (NotInitializeException e1) {
			RasterToolsUtil.messageBoxError("error_rois_table", tablePanel, e1);
		}
	}

	public void tableChanged(TableModelEvent e) {
		/*
		 * Cambio de color en una ROI:
		 */
		if(e.getColumn()==4){
			String roiName = (String)tablePanel.getTable().getTable().getJTable().getValueAt(e.getFirstRow(),0);
			ArrayList graphics = tablePanel.getRoiGraphics(roiName);
			if(tablePanel.getMapControl()!=null){
				GraphicLayer graphicLayer = tablePanel.getMapControl().getMapContext().getGraphicsLayer();
				ISymbol symbol = null;
				Color color = (Color)tablePanel.getTable().getTable().getJTable().getValueAt(e.getFirstRow(),4);
				for (int i = 0; i< graphics.size(); i++){
					symbol = SymbologyFactory.createDefaultSymbolByShapeType(((FGraphic)graphics.get(i)).getGeom().getType(), color);
					((FGraphic)graphics.get(i)).setIdSymbol(graphicLayer.addSymbol(symbol));
				}
				tablePanel.getROI(roiName).setColor(color);
				tablePanel.getMapControl().drawGraphics();
			}
		}else if(e.getColumn()==0){
			String newName = (String)tablePanel.getTable().getTable().getJTable().getValueAt(e.getFirstRow(),0);
			tablePanel.changeRoiName(roiSelectedName,newName);
			roiSelectedName = newName;
		}
	}
}

/**
 * Filtro para el selector de expresiones matemáticas.
 * @author Alejandro Muñoz (alejandro.munoz@uclm.es)
 *
 */
class ShpFileFilter extends FileFilter {

	final static String exp = "shp";
	public boolean accept(File f) {
		if (f.isDirectory()) {
           return true;
       }
       String s = f.getName();
       int i = s.lastIndexOf('.');

       if (i > 0 &&  i < s.length() - 1) {
           String extension = s.substring(i+1).toLowerCase();
           if (exp.equals(extension)){
                   return true;
           } else {
               return false;
           }
       }
       return false;
	}

	public String getDescription() {
		 return "Shape file .shp";
	}

}
