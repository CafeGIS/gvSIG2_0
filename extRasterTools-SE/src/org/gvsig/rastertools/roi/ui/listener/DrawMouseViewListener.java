package org.gvsig.rastertools.roi.ui.listener;

import java.awt.Color;
import java.awt.Image;
import java.awt.geom.Point2D;

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.exception.CreateGeometryException;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.geom.util.UtilFunctions;
import org.gvsig.fmap.mapcontext.layers.GraphicLayer;
import org.gvsig.fmap.mapcontext.rendering.legend.FGraphic;
import org.gvsig.fmap.mapcontext.rendering.symbols.IFillSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.ILineSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.IMarkerSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.ISymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.SimpleMarkerSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.SymbologyFactory;
import org.gvsig.fmap.mapcontrol.tools.BehaviorException;
import org.gvsig.fmap.mapcontrol.tools.Events.MeasureEvent;
import org.gvsig.fmap.mapcontrol.tools.Events.PointEvent;
import org.gvsig.fmap.mapcontrol.tools.Listeners.PointListener;
import org.gvsig.fmap.mapcontrol.tools.Listeners.PolylineListener;
import org.gvsig.fmap.raster.grid.roi.VectorialROI;
import org.gvsig.gui.beans.table.exceptions.NotInitializeException;
import org.gvsig.raster.grid.GridException;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.rastertools.rasterresolution.ZoomPixelCursorListener;
import org.gvsig.rastertools.roi.ui.ROIsTablePanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DrawMouseViewListener implements PolylineListener,PointListener {
	private static final GeometryManager	geomManager = GeometryLocator.getGeometryManager();
	private static final Logger 			logger 		= LoggerFactory.getLogger(DrawMouseViewListener.class);
	private ROIsTablePanel 					tablePanel 	= null;
	private VectorialROI 					roi 		= null;

	public DrawMouseViewListener(ROIsTablePanel roiManagerPanel) {
		this.tablePanel = roiManagerPanel;
	}

	public void pointFixed(MeasureEvent event) throws BehaviorException {

	}

	public void points(MeasureEvent event) throws BehaviorException {

	}

	public void polylineFinished(MeasureEvent event) throws BehaviorException {
		GeneralPathX gp = event.getGP();
		Geometry geometry = null;
		try{
			if (tablePanel.getMapControl().getCurrentTool().equals("drawPolygonROI"))
				geometry = geomManager.createSurface(gp, SUBTYPES.GEOM2D);
			else
				geometry = geomManager.createSurface(gp, SUBTYPES.GEOM2D);
		}catch(CreateGeometryException e){
			logger.error("Error creating the envelope", e);
		}
		String roiName = "";

		int selectedRow;
		try {
			selectedRow = tablePanel.getTable().getSelectedRow();
			roiName = (String)tablePanel.getTable().getModel().getValueAt(selectedRow,0);
			roi = (VectorialROI)tablePanel.getROI(roiName);
			roi.addGeometry(geometry);
			int numGeometries;

			ISymbol sym = null;
			Color geometryColor = (Color)tablePanel.getTable().getModel().getValueAt(selectedRow, 4);

			if(tablePanel.getPolygonToolButton().isSelected()){
				numGeometries = ((Integer)tablePanel.getTable().getModel().getValueAt(selectedRow,1)).intValue();
				tablePanel.getTable().getModel().setValueAt(new Integer(numGeometries+1), selectedRow, 1);
				sym =SymbologyFactory.createDefaultFillSymbol();
				((IFillSymbol)sym).setFillColor(geometryColor);
			}
			else{
				numGeometries = ((Integer)tablePanel.getTable().getModel().getValueAt(selectedRow,2)).intValue();
				tablePanel.getTable().getModel().setValueAt(new Integer(numGeometries+1), selectedRow, 2);
				sym =SymbologyFactory.createDefaultLineSymbol();
				((ILineSymbol)sym).setLineColor(geometryColor);
			}

			tablePanel.getTable().getModel().setValueAt(new Integer(roi.getValues()), selectedRow, 5);

			GraphicLayer graphicLayer = tablePanel.getMapControl().getMapContext().getGraphicsLayer();

			FGraphic fGraphic = new FGraphic(geometry,graphicLayer.addSymbol(sym));
			tablePanel.getMapControl().getMapContext().getGraphicsLayer().addGraphic(fGraphic);
			tablePanel.getRoiGraphics(roiName).add(fGraphic);
			tablePanel.getMapControl().drawGraphics();
		} catch (NotInitializeException e) {
			RasterToolsUtil.messageBoxError("error_tabla_rois", tablePanel, e);
		} catch (GridException e) {
			e.printStackTrace();
		}

	}

	public boolean cancelDrawing() {
		return true;
	}

	public Image getImageCursor() {
		return tablePanel.getToolImage();
	}

	public void point(PointEvent event) throws BehaviorException {
		Point2D point = event.getPoint();
		Point2D p= tablePanel.getMapControl().getViewPort().toMapPoint(point);
		Geometry geometry = null;
		try {
			geometry = geomManager.createPoint(p.getX(), p.getY(), SUBTYPES.GEOM2D);
		} catch (CreateGeometryException e1) {
			logger.error("Error creating a point", e1);
		}
		String roiName = "";
		int selectedRow;
		try {
			selectedRow = tablePanel.getTable().getSelectedRow();
			roiName = (String)tablePanel.getTable().getModel().getValueAt(selectedRow,0);
			roi = (VectorialROI)tablePanel.getROI(roiName);
			roi.addGeometry(geometry);
			int numPoints = ((Integer)tablePanel.getTable().getModel().getValueAt(selectedRow,3)).intValue();
			tablePanel.getTable().getModel().setValueAt(new Integer(numPoints+1), selectedRow, 3);
			tablePanel.getTable().getModel().setValueAt(new Integer(roi.getValues()), selectedRow, 5);

			GraphicLayer graphicLayer = tablePanel.getMapControl().getMapContext().getGraphicsLayer();
			Color geometryColor = (Color)tablePanel.getTable().getModel().getValueAt(selectedRow, 4);
			ISymbol sym = SymbologyFactory.createDefaultMarkerSymbol();
			((IMarkerSymbol)sym).setColor(geometryColor);
			((SimpleMarkerSymbol)sym).setStyle(SimpleMarkerSymbol.CIRCLE_STYLE);

			FGraphic fGraphic = new FGraphic(geometry,graphicLayer.addSymbol(sym));
			tablePanel.getMapControl().getMapContext().getGraphicsLayer().addGraphic(fGraphic);
			tablePanel.getRoiGraphics(roiName).add(fGraphic);
			tablePanel.getMapControl().drawGraphics();
		} catch (NotInitializeException e) {
			RasterToolsUtil.messageBoxError("error_tabla_rois", tablePanel, e);
		} catch (GridException e) {
			e.printStackTrace();
		}
	}

	public void pointDoubleClick(PointEvent event) throws BehaviorException {
	}

}
