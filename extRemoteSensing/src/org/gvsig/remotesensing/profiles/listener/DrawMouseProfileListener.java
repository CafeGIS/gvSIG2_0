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
import java.awt.Image;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.exception.CreateGeometryException;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.geom.util.UtilFunctions;
import org.gvsig.fmap.mapcontext.layers.GraphicLayer;
import org.gvsig.fmap.mapcontext.rendering.legend.FGraphic;
import org.gvsig.fmap.mapcontext.rendering.symbols.ILineSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.IMarkerSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.ISymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.SymbologyFactory;
import org.gvsig.fmap.mapcontrol.tools.BehaviorException;
import org.gvsig.fmap.mapcontrol.tools.Events.MeasureEvent;
import org.gvsig.fmap.mapcontrol.tools.Events.PointEvent;
import org.gvsig.fmap.mapcontrol.tools.Listeners.PointListener;
import org.gvsig.fmap.mapcontrol.tools.Listeners.PolylineListener;
import org.gvsig.fmap.raster.grid.roi.VectorialROI;
import org.gvsig.gui.beans.table.exceptions.NotInitializeException;
import org.gvsig.raster.buffer.RasterBuffer;
import org.gvsig.raster.grid.GridException;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.remotesensing.profiles.gui.ProfilePanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iver.andami.PluginServices;

/**
 * Clase que define el comportamiento tras los eventos de dibujado sobre la imagen.
 * Se encarga del pintado de la grafica asociada a la geometria (punto o linea) dibujada.
 *
 * @author aMuÑoz (alejandro.munoz@uclm.es)
 * @version 11/3/2008
 */

public class DrawMouseProfileListener implements PolylineListener,PointListener {
	private static final GeometryManager geomManager = GeometryLocator.getGeometryManager();
	private static final Logger logger = LoggerFactory.getLogger(DrawMouseProfileListener.class);
	private ProfilePanel	panel 		= null;

	/**
	 * Constructor
	 * @param panel principal de perfiles
	 * */
	public DrawMouseProfileListener(ProfilePanel panel) {
		this.panel= panel;
	}

	/**
	 *  Eventos tras el dibujado de una linea. Se pinta la geometria dibujada en la vista y se genera el
	 *  gráfico asociado.
	 * */
	public void polylineFinished(MeasureEvent event) throws BehaviorException {

		GeneralPathX gp = event.getGP();
		Geometry geometry = null;

		try {
			geometry = geomManager.createCurve(gp, SUBTYPES.GEOM2D);
			String roiName = "";
			VectorialROI lineRoi= null;
			int selectedRow;
			selectedRow = panel.getLineOptionsPanel().getTable().getSelectedRow();
			roiName = (String)panel.getLineOptionsPanel().getTable().getModel().getValueAt(selectedRow,0);
			lineRoi = (VectorialROI)panel.getLineOptionsPanel().getROI(roiName);

			// Si hay una roi con mas de una geometria no se hace nada.
			if(lineRoi.getGeometries().size()>0) {
				return;
			} else {
				lineRoi.addGeometry(geometry);
				panel.getLineOptionsPanel().addROI(lineRoi);
				ISymbol sym = null;
				Color geometryColor = (Color)panel.getLineOptionsPanel().getTable().getModel().getValueAt(selectedRow, 1);
				sym =SymbologyFactory.createDefaultLineSymbol();
				((ILineSymbol)sym).setLineColor(geometryColor);

				GraphicLayer graphicLayer = panel.getLineOptionsPanel().getMapControl().getMapContext().getGraphicsLayer();
				FGraphic fGraphic = new FGraphic(geometry,graphicLayer.addSymbol(sym));
				panel.getLineOptionsPanel().getMapControl().getMapContext().getGraphicsLayer().addGraphic(fGraphic);
				panel.getLineOptionsPanel().getRoiGraphics(roiName).add(fGraphic);
				panel.getLineOptionsPanel().getMapControl().drawGraphics();
				panel.getLineOptionsPanel().getNewButton().setSelected(false);
				panel.getMapControl().setTool(panel.getLineOptionsPanel().getPreviousTool());
				// Se pinta el grafico asociado
				drawChartRoi(lineRoi,panel.getLineOptionsPanel().getComboBands().getSelectedIndex());
			}
		} catch (NotInitializeException e) {
			RasterToolsUtil.messageBoxError("tabla_no_inicializada", this, e);
		} catch (CreateGeometryException e) {
			RasterToolsUtil.messageBoxError("Error creating the Envelope", this, e);
		}
	}

	/**
	 * @return  cursor asociado al panel activo
	 * */
	public Image getImageCursor() {
		if(panel.getPointOptionsPanel().getNextActive()==ProfilePanel.PANELZPROFILE) {
			return panel.getLineOptionsPanel().getToolImage();
		} else {
			return panel.getPointOptionsPanel().getToolImage();
		}
	}


	/**
	 *  Eventos tras el dibujado de un punto. Se añade el punto a la roi activa del panel.
	 *  Si esta roi tiene alguna geometria, se elimina.
	 * */
	public void point(PointEvent event) throws BehaviorException {

		Point2D point = event.getPoint();
		Point2D p= panel.getPointOptionsPanel().getMapControl().getViewPort().toMapPoint(point);

		try {
			Geometry geometry = geomManager.createPoint(p.getX(), p.getY(), SUBTYPES.GEOM2D);
			VectorialROI pointRoi= null;
			String roiName = "";
			int selectedRow;
			selectedRow = panel.getPointOptionsPanel().getTable().getSelectedRow();
			panel.getPointOptionsPanel().getTable().getModel().setValueAt(new Double(p.getX()), selectedRow, 2);
			panel.getPointOptionsPanel().getTable().getModel().setValueAt(new Double(p.getY()), selectedRow, 3);
			roiName = (String)panel.getPointOptionsPanel().getTable().getModel().getValueAt(selectedRow,0);
			pointRoi =(VectorialROI)panel.getPointOptionsPanel().getRois().get(roiName);
			pointRoi.clear();
			pointRoi.addGeometry(geometry);
			panel.getPointOptionsPanel().addROI(pointRoi);
			// Actualización del gráfico
			drawChartAllPointsRois();
			panel.getPointOptionsPanel().getNewButton().setSelected(false);
			panel.getMapControl().setTool(panel.getPointOptionsPanel().getPreviousTool());
		} catch (NotInitializeException e) {
			RasterToolsUtil.messageBoxError("tabla_no_inicializada", this, e);
		} catch (CreateGeometryException e) {
			RasterToolsUtil.messageBoxError("Error creating the envelope", this, e);
		}
	}


	/**
	 * 	Metodo que se encarga de pintar la roi de tipo linea que se pasa como parametro sobre el grafico.
	 * 	La vanda de la que se tomarán los valores es la que se pasa como parámetro.
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
						z= roi.getCellValueAsByte(i,j);
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
						z= roi.getCellValueAsShort(i,j);
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
						z= roi.getCellValueAsInt(i,j);
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
			// Actualizacion del grafico
			panel.getLineOptionsPanel().SetColorSeriesChart();
			panel.getLineOptionsPanel().getJPanelChart().setNewChart(series, names);
			panel.getLineOptionsPanel().updateUI();
			int selectedRow = panel.getLineOptionsPanel().getTable().getSelectedRow();
			roi.setBandToOperate(band);
			double max=roi.getMaxValue();
			double min= roi.getMinValue();
			double mean = roi.getMeanValue();
			panel.getLineOptionsPanel().getTable().getModel().setValueAt(new Double(max), selectedRow, 2);
			panel.getLineOptionsPanel().getTable().getModel().setValueAt(new Double(min), selectedRow, 3);
			panel.getLineOptionsPanel().getTable().getModel().setValueAt(new Double(mean), selectedRow, 4);

		} catch (GridException e) {
			RasterToolsUtil.messageBoxError(PluginServices.getText(this, "grid_error"), this, e);
		} catch (NotInitializeException e) {
			RasterToolsUtil.messageBoxError("tabla_no_inicializada", this, e);
		}
	}

	/**
	 * Método que realiza el repintado del grafico con todas las rois la tabla del panel zprofile.
	 * */
	private void drawChartAllPointsRois(){

		ISymbol symbol = null;
		FGraphic fGraphic = null;
		Geometry geometry=null;
		ArrayList roisArray =panel.getPointOptionsPanel().getROIs();
		GraphicLayer graphicLayer = panel.getPointOptionsPanel().getMapControl().getMapContext().getGraphicsLayer();
		VectorialROI pointRoi=null;

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
				panel.getPointOptionsPanel().getRoiGraphics(pointRoi.getName()).add(fGraphic);
			}
		}
		panel.getPointOptionsPanel().getMapControl().drawGraphics();

		int nSeries = panel.getPointOptionsPanel().getROIs().size();
		int[][] series = new int[nSeries][pointRoi.getBandCount()+1];
		String[] names = new String[nSeries];

		// Se establecen los colores de las graficas segun color de las rois
		for(int iSerie = 0; iSerie < nSeries; iSerie++){
			series[iSerie][0] = 0;
			names[iSerie] = "Band " + (iSerie+1);
			pointRoi=(VectorialROI) panel.getPointOptionsPanel().getROIs().get(iSerie);
			try {
				for (int i = 1; i <= pointRoi.getBandCount(); i++){
					pointRoi.setBandToOperate(i-1);
					series[iSerie][i] = (int) pointRoi.getMeanValue();
				}
			} catch (GridException e) {
				RasterToolsUtil.messageBoxError(PluginServices.getText(this, "grid_error"), this, e);
			}
		}
		panel.getPointOptionsPanel().UpdateChart();
		panel.getPointOptionsPanel().getJPanelChart().setNewChart(series, names);
	}


	public boolean cancelDrawing() {
		return true;
	}

	public void pointFixed(MeasureEvent event) throws BehaviorException {
	}

	public void points(MeasureEvent event) throws BehaviorException {
	}

	public void pointDoubleClick(PointEvent event) throws BehaviorException {
	}
}
