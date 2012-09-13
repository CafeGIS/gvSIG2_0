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

package org.gvsig.rastertools.roi.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.GraphicLayer;
import org.gvsig.fmap.mapcontext.rendering.legend.FGraphic;
import org.gvsig.fmap.mapcontext.rendering.symbols.IFillSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.ILineSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.IMarkerSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.ISymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.SymbologyFactory;
import org.gvsig.fmap.mapcontrol.MapControl;
import org.gvsig.fmap.mapcontrol.tools.Behavior.Behavior;
import org.gvsig.fmap.mapcontrol.tools.Behavior.MouseMovementBehavior;
import org.gvsig.fmap.mapcontrol.tools.Behavior.PointBehavior;
import org.gvsig.fmap.mapcontrol.tools.Behavior.PolygonBehavior;
import org.gvsig.fmap.mapcontrol.tools.Behavior.PolylineBehavior;
import org.gvsig.fmap.raster.grid.roi.VectorialROI;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.gui.beans.table.TableContainer;
import org.gvsig.gui.beans.table.exceptions.NotInitializeException;
import org.gvsig.gui.beans.table.models.IModel;
import org.gvsig.raster.buffer.BufferFactory;
import org.gvsig.raster.buffer.RasterBuffer;
import org.gvsig.raster.buffer.RasterBufferInvalidException;
import org.gvsig.raster.dataset.IRasterDataSource;
import org.gvsig.raster.grid.Grid;
import org.gvsig.raster.grid.GridException;
import org.gvsig.raster.grid.roi.ROI;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.rastertools.roi.ui.listener.DrawMouseViewListener;
import org.gvsig.rastertools.roi.ui.listener.ROIsTablePanelListener;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;
import com.iver.cit.gvsig.project.documents.view.toolListeners.StatusBarListener;

/**
 * Panel con la tabla de ROIs.
 *
 * @author Diego Guerrero Sevilla (diego.guerrero@uclm.es)
 *
 */
public class ROIsTablePanel extends JPanel {
	private static final long serialVersionUID = 2138437448356198224L;

	private ROIsManagerPanel managerPanel;

	private ROIsTablePanelListener listener = null;

	private JPanel controlPanel = null;

	private JPanel tableButtonsPanel = null;

	private JPanel toolsPanel = null;

	private TableContainer tableContainer = null;

	private JButton newButton = null;

	private JButton deleteButton = null;

	private JButton exportButton = null;

	private JButton importButton = null;

	private JToggleButton pointToolButton = null;

	private JToggleButton lineToolButton = null;

	private JToggleButton polygonToolButton = null;

	private FLayer fLayer = null;

	private MapControl mapControl = null;

	private BaseView view = null;

	private GraphicLayer graphicLayer = null;

	private LinkedHashMap rois = null;

	private HashMap roiGraphics = null;

	private Grid grid = null;

	private String pathToImages = "images/";

	private Image curImage = null;

	private String previousTool = null;

	public ROIsTablePanel(ROIsManagerPanel managerPanel) {
		super();

		this.managerPanel = managerPanel;
		setBorder(new LineBorder(Color.GRAY));
		setLayout(new BorderLayout());
		add(getTable(), BorderLayout.CENTER);
		add(getControlPanel(), BorderLayout.SOUTH);

		initialize();
	}

	private void initialize() {
		listener = new ROIsTablePanelListener(this);
		getNewButton().addActionListener(listener);
		getDeleteButton().addActionListener(listener);
		getImportButton().addActionListener(listener);
		getExportButton().addActionListener(listener);
		getPointToolButton().addActionListener(listener);
		getLineToolButton().addActionListener(listener);
		getPolygonToolButton().addActionListener(listener);

		getTable().getTable().getJTable().getSelectionModel()
				.addListSelectionListener(listener);
		getTable().getTable().getJTable().getModel().addTableModelListener(
				listener);

		getPolygonToolButton().setSelected(true);
		setToolsEnabled(false);
	}

	public ROIsManagerPanel getManagerPanel() {
		return managerPanel;
	}

	public void setToolsEnabled(boolean b) {
		getPointToolButton().setEnabled(b);
		getLineToolButton().setEnabled(b);
		getPolygonToolButton().setEnabled(b);
	}

	public TableContainer getTable() {
		if (tableContainer == null) {
			String[] columnNames = { PluginServices.getText(this, "nombre"),
					PluginServices.getText(this, "poligonos"),
					PluginServices.getText(this, "lineas"),
					PluginServices.getText(this, "puntos"),
					PluginServices.getText(this, "color"),
					PluginServices.getText(this, "pixeles")};
			int[] columnWidths = { 50, 25, 25, 25, 45, 50 };
			tableContainer = new TableContainer(columnNames, columnWidths);
			tableContainer.setModel("ROIsTableModel");
			tableContainer.initialize();
			tableContainer.setControlVisible(false);
			tableContainer.setBorder(new EmptyBorder(5, 5, 0, 5));
		}
		return tableContainer;
	}

	public JPanel getControlPanel() {
		if (controlPanel == null) {
			controlPanel = new JPanel();
			controlPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

			controlPanel.add(getTableButtonsPanel());

			controlPanel.add(getToolsPanel());
		}
		return controlPanel;
	}

	public JButton getDeleteButton() {
		if (deleteButton == null) {
			deleteButton = new JButton(PluginServices.getText(this, "borrar"));
		}
		return deleteButton;
	}

	public JButton getNewButton() {
		if (newButton == null) {
			newButton = new JButton(PluginServices.getText(this, "nuevo"));
		}
		return newButton;
	}

	public FLayer getFLayer() {
		return fLayer;
	}

	public void setFLayer(FLayer layer) throws GridException {
		fLayer = layer;
		FLyrRasterSE rasterLayer = (FLyrRasterSE) layer;
		IRasterDataSource dsetCopy = null;
		dsetCopy = rasterLayer.getDataSource().newDataset();
		BufferFactory bufferFactory = new BufferFactory(dsetCopy);
		if (!RasterBuffer.loadInMemory(dsetCopy))
			bufferFactory.setReadOnly(true);
		bufferFactory.setAllDrawableBands();

		int bands[] = null;
		bands = new int[rasterLayer.getBandCount()];
		for (int i = 0; i < rasterLayer.getBandCount(); i++)
			bands[i] = i;
		try {
			grid = new Grid(bufferFactory, bands);
		} catch (RasterBufferInvalidException e) {
			e.printStackTrace();
		}

		if (view==null){
			IWindow[] list = PluginServices.getMDIManager().getAllWindows();
			for (int i = 0; i < list.length; i++) {
				if(list[i] instanceof BaseView)
					view = (BaseView)list[i];
			}
			if (view == null)
				return;
			mapControl = view.getMapControl();
			graphicLayer = view.getMapControl().getMapContext().getGraphicsLayer();

			/*
			 * Guardar la herramienta actual para recuperarla más tarde, por ejemplo
			 * al cerrar el contenedor del este panel.
			 */
			previousTool = mapControl.getCurrentTool();

			// Listener de eventos de movimiento que pone las coordenadas del ratón
			// en
			// la barra de estado
			StatusBarListener sbl = new StatusBarListener(mapControl);

			DrawMouseViewListener drawMouseViewListener = new DrawMouseViewListener(
					this);
			mapControl.addMapTool("drawPolygonROI", new Behavior[] {
					new PolygonBehavior(drawMouseViewListener),
					new MouseMovementBehavior(sbl) });
			mapControl.addMapTool("drawLineROI", new Behavior[] {
					new PolylineBehavior(drawMouseViewListener),
					new MouseMovementBehavior(sbl) });
			mapControl.addMapTool("drawPointROI", new Behavior[] {
					new PointBehavior(drawMouseViewListener),
					new MouseMovementBehavior(sbl) });
		}

		/*
		 * Caragar las ROIs asociadas a la capa, si las hay:
		 */
		clearROIs();
		loadROIs();
	}

	/**
	 * Elimina todas las filas de la tabla de rois así como la ROI y objetos
	 * FGraphic asociados (borrándolos también de la vista) a cada una.
	 *
	 */
	public void clearROIs() {
		boolean repaint = false;
		try {
			getTable().removeAllRows();
		} catch (NotInitializeException e) {
			RasterToolsUtil.messageBoxError("error_tabla_rois", this, e);
		}
		for (Iterator iter = getRoiGraphics().values().iterator(); iter.hasNext();) {
			ArrayList roiGraphics = (ArrayList) iter.next();
			for (int i = 0; i < roiGraphics.size(); i++){
				getMapControl().getMapContext().getGraphicsLayer().removeGraphic((FGraphic)roiGraphics.get(i));
				repaint = true;
			}
		}
		if (repaint)
			getMapControl().rePaintDirtyLayers();
		getRoiGraphics().clear();
		getRois().clear();
	}

	/**
	 * Cargar las ROIs asociadas a la capa, si las hay.
	 * @throws GridException
	 *
	 */
	private void loadROIs() throws GridException {
		ArrayList roisArray = ((FLyrRasterSE) fLayer).getRois();
		if (roisArray != null) {
			GraphicLayer graphicLayer = getMapControl().getMapContext()
					.getGraphicsLayer();
			ISymbol symbol = null;
			FGraphic fGraphic = null;
			int nPoints, nPolygons, nLines;

			for (Iterator iter = roisArray.iterator(); iter.hasNext();) {
				ROI roi = (ROI) iter.next();

				nPoints = 0;
				nPolygons = 0;
				nLines = 0;

				Object row[] = ((IModel) getTable().getModel()).getNewLine();
				row[0] = roi.getName();
				row[4] = roi.getColor();

				addROI(roi);
				VectorialROI vectorialROI = (VectorialROI) roi;
				for (Iterator iterator = vectorialROI.getGeometries()
						.iterator(); iterator.hasNext();) {
					Geometry geometry = (Geometry) iterator.next();
					switch (geometry.getType()) {
					case Geometry.TYPES.POINT:
						symbol = SymbologyFactory.createDefaultMarkerSymbol();
						((IMarkerSymbol) symbol).setColor(roi.getColor());
						nPoints++;
						break;
					case Geometry.TYPES.SURFACE:
						symbol = SymbologyFactory.createDefaultFillSymbol();
						((IFillSymbol) symbol).setFillColor(roi.getColor());
						nPolygons++;
						break;
					case Geometry.TYPES.CURVE:
						symbol = SymbologyFactory.createDefaultLineSymbol();
						((ILineSymbol) symbol).setLineColor(roi.getColor());
						nLines++;
						break;
					}
					fGraphic = new FGraphic(geometry, graphicLayer
							.addSymbol(symbol));
					graphicLayer.addGraphic(fGraphic);
					getRoiGraphics(roi.getName()).add(fGraphic);
				}
				row[1] = new Integer(nPolygons);
				row[2] = new Integer(nLines);
				row[3] = new Integer(nPoints);
				row[5] = new Integer(roi.getValues());
				((DefaultTableModel) getTable().getModel()).addRow(row);
			}
			selectDrawRoiTool();
			getMapControl().drawGraphics();
		}
	}

	public void setROIs(ArrayList roisArray) throws GridException{
		if (roisArray != null) {
			rois = new LinkedHashMap();
			GraphicLayer graphicLayer = getMapControl().getMapContext()
					.getGraphicsLayer();
			ISymbol symbol = null;
			FGraphic fGraphic = null;
			int nPoints, nPolygons, nLines;

			for (Iterator iter = roisArray.iterator(); iter.hasNext();) {
				ROI roi = (ROI) iter.next();

				nPoints = 0;
				nPolygons = 0;
				nLines = 0;

				Object row[] = ((IModel) getTable().getModel()).getNewLine();
				row[0] = roi.getName();
				row[4] = roi.getColor();

				addROI(roi);
				VectorialROI vectorialROI = (VectorialROI) roi;
				for (Iterator iterator = vectorialROI.getGeometries()
						.iterator(); iterator.hasNext();) {
					Geometry geometry = (Geometry) iterator.next();
					switch (geometry.getType()) {
					case Geometry.TYPES.POINT:
						symbol = SymbologyFactory.createDefaultMarkerSymbol();
						((IMarkerSymbol) symbol).setColor(roi.getColor());
						nPoints++;
						break;
					case Geometry.TYPES.SURFACE:
						symbol = SymbologyFactory.createDefaultFillSymbol();
						((IFillSymbol) symbol).setFillColor(roi.getColor());
						nPolygons++;
						break;
					case Geometry.TYPES.CURVE:
						symbol = SymbologyFactory.createDefaultLineSymbol();
						((ILineSymbol) symbol).setLineColor(roi.getColor());
						nLines++;
						break;
					}
					fGraphic = new FGraphic(geometry, graphicLayer
							.addSymbol(symbol));
					graphicLayer.addGraphic(fGraphic);
					getRoiGraphics(roi.getName()).add(fGraphic);
				}
				row[1] = new Integer(nPolygons);
				row[2] = new Integer(nLines);
				row[3] = new Integer(nPoints);
				row[5] = new Integer(roi.getValues());
				((DefaultTableModel) getTable().getModel()).addRow(row);
			}
			selectDrawRoiTool();
			getMapControl().drawGraphics();
		}
	}

	public MapControl getMapControl() {
		return mapControl;
	}

	public void selectDrawRoiTool() {
		if (mapControl != null)
			if (getPolygonToolButton().isSelected()) {
				Image img = new ImageIcon(getClass().getResource(
						"images/PoligonCursor.png")).getImage();
				curImage = img;
				mapControl.setTool("drawPolygonROI");
			} else if (getLineToolButton().isSelected()) {
				Image img = new ImageIcon(getClass().getResource(
						"images/LineCursor.png")).getImage();
				curImage = img;
				mapControl.setTool("drawLineROI");
			} else if (getPointToolButton().isSelected()) {
				Image img = new ImageIcon(getClass().getResource(
						"images/PointCursor.png")).getImage();
				curImage = img;
				mapControl.setTool("drawPointROI");
			}
	}

	public Grid getGrid() {
		return grid;
	}

	private LinkedHashMap getRois() {
		if (rois == null)
			rois = new LinkedHashMap();
		return rois;
	}

	public JPanel getToolsPanel() {
		if (toolsPanel == null) {
			toolsPanel = new JPanel();
			toolsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
			toolsPanel.add(getPointToolButton());
			toolsPanel.add(getLineToolButton());
			toolsPanel.add(getPolygonToolButton());
			toolsPanel.add(getExportButton());
			toolsPanel.add(getImportButton());
		}
		return toolsPanel;
	}

	public JPanel getTableButtonsPanel() {
		if (tableButtonsPanel == null) {
			tableButtonsPanel = new JPanel();
			tableButtonsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

			tableButtonsPanel.add(getNewButton());

			tableButtonsPanel.add(getDeleteButton());
		}
		return tableButtonsPanel;
	}

	public JToggleButton getPointToolButton() {
		if (pointToolButton == null) {
			pointToolButton = new JToggleButton();
			pointToolButton.setIcon(new ImageIcon(getClass().getResource(
					pathToImages + "Point.png")));
		}
		return pointToolButton;
	}

	public JToggleButton getLineToolButton() {
		if (lineToolButton == null) {
			lineToolButton = new JToggleButton();
			lineToolButton.setIcon(new ImageIcon(getClass().getResource(
					pathToImages + "Line.png")));
		}
		return lineToolButton;
	}

	public JToggleButton getPolygonToolButton() {
		if (polygonToolButton == null) {
			polygonToolButton = new JToggleButton();
			polygonToolButton.setIcon(new ImageIcon(getClass().getResource(
					pathToImages + "Poligon.png")));
		}
		return polygonToolButton;
	}

	public JButton getImportButton() {
		if (importButton == null) {
			importButton = new JButton();
			importButton.setIcon(PluginServices.getIconTheme().get("tfwload-icon"));
			importButton.setToolTipText(PluginServices.getText(this,"cargar_rois"));
		}
		return importButton;
	}

	public JButton getExportButton() {
		if (exportButton == null) {
			exportButton = new JButton();
			exportButton.setIcon(PluginServices.getIconTheme().get("save-icon"));
			exportButton.setToolTipText(PluginServices.getText(this,"salvar_rois"));
		}
		return exportButton;
	}

	public Image getToolImage() {
		return curImage;
	}

	public BaseView getView() {
		return view;
	}


/*	public void drawROIs() {
		Color geometryColor;

		BufferedImage img = getMapControl().getImage();
		Graphics2D gImag = (Graphics2D) img.getGraphics();
		ViewPort vp = getMapControl().getViewPort();
		try {
			for (int i = 0; i < getTable().getRowCount(); i++) {
				geometryColor = (Color) getTable().getModel().getValueAt(i, 4);
				VectorialROI vROI = (VectorialROI) getRois().get(
						getTable().getTable().getJTable().getValueAt(i, 0));

				for (Iterator iter = vROI.getGeometries().iterator(); iter
						.hasNext();) {
					IGeometry geometry = (IGeometry) iter.next();

					ISymbol sym = SymbologyFactory
							.createDefaultSymbolByShapeType(geometry
									.getGeometryType(), geometryColor);
					geometry.draw(gImag, vp, sym, null);
				}
			}
			getView().repaint();
		} catch (NotInitializeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
*/

	private HashMap getRoiGraphics() {
		if (roiGraphics == null)
			roiGraphics = new HashMap();
		return roiGraphics;
	}

	public void addROI(ROI roi) {
		getRois().put(roi.getName(), roi);
		getRoiGraphics().put(roi.getName(), new ArrayList());
	}

	public ROI getROI(String roiName) {

		return (ROI) getRois().get(roiName);
	}

	/**
	 * Elimina la ROI correspondiente al nombre <code>roiName</code> así
	 * como sus objetos FGraphic asociados (borrándolos también de la vista).
	 * (NO elimina la fila correspondiente en la tabla del panel).
	 *
	 * @param roiName Nombre de la ROI a eliminar.
	 */
	public void removeROI(String roiName) {
		boolean repaint = false;
		getRois().remove(roiName);
		ArrayList roiGraphics = getRoiGraphics(roiName);
		if (roiGraphics != null) {
			for (int i = 0; i < roiGraphics.size(); i++){
				getMapControl().getMapContext().getGraphicsLayer().removeGraphic((FGraphic) getRoiGraphics(roiName).get(i));
				repaint = true;
			}
			if (repaint)
				getMapControl().rePaintDirtyLayers();
			getRoiGraphics().remove(roiName);
		}
	}

	public ArrayList getRoiGraphics(String roiName) {
		return (ArrayList) getRoiGraphics().get(roiName);
	}

	/**
	 * Borra de capa GraphicsLayer de mapContext los gráficos pertenecientes a
	 * las rois del panel. Si la GraphicsLayer se queda sin gráficos se eliminan
	 * tambien los símbolos de esta.
	 */
	public void clearRoiGraphics() {
		for (Iterator iter = getRoiGraphics().values().iterator(); iter
				.hasNext();) {
			ArrayList fGraphics = (ArrayList) iter.next();

			for (Iterator iterator = fGraphics.iterator(); iterator.hasNext();) {
				FGraphic fGraphic = (FGraphic) iterator.next();
				getMapControl().getMapContext().getGraphicsLayer()
						.removeGraphic(fGraphic);
			}
		}
		if (getMapControl().getMapContext().getGraphicsLayer().getNumGraphics() == 0)
			getMapControl().getMapContext().getGraphicsLayer()
					.clearSymbolsGraphics();
	}

	public ArrayList getROIs() {
		return new ArrayList(getRois().values());
	}

	public void setPreviousTool() {
		if (previousTool != null)
			getMapControl().setTool(previousTool);
	}

	public void changeRoiName(String currentName, String newName) {
		getROI(currentName).setName(newName);
		getRois().put(newName, getRois().remove(currentName));
		getRoiGraphics().put(newName, getRoiGraphics().remove(currentName));
	}

	public void sortTable(){

	}

	public GraphicLayer getGraphicLayer() {
		return graphicLayer;
	}
}
