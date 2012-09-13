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
package org.gvsig.remotesensing.profiles.listener;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.mapcontext.layers.GraphicLayer;
import org.gvsig.fmap.mapcontext.rendering.legend.FGraphic;
import org.gvsig.fmap.mapcontext.rendering.symbols.IMarkerSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.ISymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.SymbologyFactory;
import org.gvsig.fmap.raster.grid.roi.VectorialROI;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelEvent;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelListener;
import org.gvsig.gui.beans.table.exceptions.NotInitializeException;
import org.gvsig.gui.beans.table.models.IModel;
import org.gvsig.raster.grid.GridException;
import org.gvsig.raster.grid.roi.ROI;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.remotesensing.profiles.gui.ZProfileOptionsPanel;

import com.iver.andami.PluginServices;
/**
 * Clase que define el comportamiento para los eventos de un ZProfileOptionsPanel.
 *
 * @author aMuÑoz (alejandro.munoz@uclm.es)
 * @version 11/3/2008
 */

public class ZProfileOptionsListener implements ButtonsPanelListener, ActionListener, ListSelectionListener, TableModelListener {

	ZProfileOptionsPanel optionsPanel = null;
	int createRois =0;
	/**
	 * Constructor
	 * */
	public ZProfileOptionsListener( ZProfileOptionsPanel optionsPanel){
		this.optionsPanel= optionsPanel;
	}


	public void actionPerformed(ActionEvent e) {

		// Acciones a ejecutar cuando se crea una nueva entrada en la tabla
		// Construccion de la roi (vacia) que espera el dibujado de la geometria y
		// selección de la herramienta de dibujado de polyline

		if (e.getSource() == optionsPanel.getNewButton()){
			try {
				String roiName=PluginServices.getText(this,"point") + String.valueOf(createRois);
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
				optionsPanel.selectDrawRoiTool();
				optionsPanel.getNewButton().setSelected(true);
				optionsPanel.getDeleteButton().setSelected(false);

			} catch (NotInitializeException e1) {
				RasterToolsUtil.messageBoxError("tabla_no_inicializada", this, e1);
			}
		}

		// Acciones a ejecutar cuando se borra una entrada de la tabla
		// Borrado de la roi correspondiente. Repintado de todas las rois

		if (e.getSource() == optionsPanel.getDeleteButton()){
			optionsPanel.getDeleteButton().setSelected(false);
			try {
				if (optionsPanel.getTable().getRowCount()>0){
					optionsPanel.getJPanelChart().cleanChart();
					String className = (String)optionsPanel.getTable().getModel().getValueAt(optionsPanel.getTable().getSelectedRow(),0);
					optionsPanel.removeROI(className);
					((DefaultTableModel)optionsPanel.getTable().getModel()).removeRow(optionsPanel.getTable().getSelectedRow());
					if(optionsPanel.getTable().getRowCount()>0){
						optionsPanel.getTable().setSelectedIndex(0);
					}
					drawChartAllPointsRois();
					optionsPanel.getMapControl().rePaintDirtyLayers();
				}
			} catch (NotInitializeException e2) {
				RasterToolsUtil.messageBoxError("tabla_no_inicializada", this, e2);
			}
		}
	}



	public void valueChanged(ListSelectionEvent e) {

	}


	/**
	 * Acciones cuando se produce una modificación en la tabla
	 * */
	public void tableChanged(TableModelEvent e) {

		// Cambios en la columna que visualiza el color
		if(e.getColumn()==1){

			String roiName = (String)optionsPanel.getTable().getTable().getJTable().getValueAt(e.getFirstRow(),0);
			ArrayList graphics = optionsPanel.getRoiGraphics(roiName);

			if(optionsPanel.getMapControl()!=null){
				GraphicLayer graphicLayer = optionsPanel.getMapControl().getMapContext().getGraphicsLayer();
				ISymbol symbol = null;
				Color color = (Color)optionsPanel.getTable().getTable().getJTable().getValueAt(e.getFirstRow(),1);
				for (int i = 0; i< graphics.size(); i++){
					symbol = SymbologyFactory.createDefaultSymbolByShapeType(((FGraphic)graphics.get(i)).getGeom().getType(), color);
					((FGraphic)graphics.get(i)).setIdSymbol(graphicLayer.addSymbol(symbol));
				}
				optionsPanel.getROI(roiName).setColor(color);
				optionsPanel.getMapControl().drawGraphics();
			}

			optionsPanel.UpdateChart();
		}
	}


	/**
	 * Método que dibuja  sobre la vista las rois del panel.
	 * y construye el gráfico correspondiente a todas las rois definidas en el panel.
	 * */
	public void drawChartAllPointsRois(){

		ISymbol symbol = null;
		FGraphic fGraphic = null;
		Geometry geometry=null;
		ArrayList roisArray =optionsPanel.getROIs();
		GraphicLayer graphicLayer = optionsPanel.getMapControl().getMapContext().getGraphicsLayer();
		VectorialROI pointRoi=null;
		if(roisArray.isEmpty())
			return;
		if (((VectorialROI)roisArray.get(0)).getGeometries().size() ==0)
			return;
		for (Iterator iter = roisArray.iterator(); iter.hasNext();) {
			 pointRoi= (VectorialROI)iter.next();;
			for (Iterator iterator = pointRoi.getGeometries()
					.iterator(); iterator.hasNext();) {
				geometry = (Geometry) iterator.next();
				switch (geometry.getType()) {
				case Geometry.TYPES.POINT:
					symbol = SymbologyFactory.createDefaultMarkerSymbol();
					((IMarkerSymbol) symbol).setColor(pointRoi.getColor());
					break;
				}
				fGraphic = new FGraphic(geometry, graphicLayer
						.addSymbol(symbol));
				graphicLayer.addGraphic(fGraphic);
				optionsPanel.getRoiGraphics(pointRoi.getName()).add(fGraphic);
			}

		}
		optionsPanel.getMapControl().drawGraphics();

		int nSeries = optionsPanel.getROIs().size();
		int[][] series = new int[nSeries][pointRoi.getBandCount()+1];
		String[] names = new String[nSeries];

		// Se establecen los colores de las graficas segun color de las rois
		for(int iSerie = 0; iSerie < nSeries; iSerie++){
			series[iSerie][0] = 0;
			names[iSerie] = "Band " + (iSerie+1);
			pointRoi=(VectorialROI) optionsPanel.getROIs().get(iSerie);
			try {
				for (int i = 1; i <= pointRoi.getBandCount(); i++){
					pointRoi.setBandToOperate(i-1);
					series[iSerie][i] = (int) pointRoi.getMeanValue();
				}
			} catch (GridException e) {
				RasterToolsUtil.messageBoxError(PluginServices.getText(this, "grid_error"), this, e);
			}
		}
		optionsPanel.UpdateChart();
		optionsPanel.getJPanelChart().setNewChart(series, names);
	}



	public void actionButtonPressed(ButtonsPanelEvent e) {
	}
}

