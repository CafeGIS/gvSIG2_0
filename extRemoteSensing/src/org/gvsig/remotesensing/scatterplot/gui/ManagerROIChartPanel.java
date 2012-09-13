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

package org.gvsig.remotesensing.scatterplot.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.rendering.legend.FGraphic;
import org.gvsig.fmap.mapcontrol.MapControl;
import org.gvsig.gui.beans.table.TableContainer;
import org.gvsig.gui.beans.table.models.IModel;
import org.gvsig.raster.grid.Grid;
import org.gvsig.raster.grid.GridException;
import org.gvsig.raster.grid.roi.ROI;
import org.gvsig.remotesensing.scatterplot.chart.ROIChart;
import org.gvsig.remotesensing.scatterplot.chart.ScatterPlotDiagram;
import org.gvsig.remotesensing.scatterplot.listener.ManagerROIChartPanelListener;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.project.documents.view.gui.View;

/**
 * Clase que define el panel de gestión de Rois del grafico.
 *
 * @author Alejandro Muñoz Sanchez (alejandro.munoz@uclm.es)
 * @version 11/12/2007
 */
public class ManagerROIChartPanel extends JPanel  implements IWindow{

	private static final long serialVersionUID = 1L;
	private ManagerROIChartPanelListener listener = null;
	private JPanel controlPanel = null;
	private JPanel tableButtonsPanel = null;
	private JPanel toolsPanel = null;
	private TableContainer tableContainer = null;
	private JButton exportRoiButtom = null;
	private JButton newRoiButtom = null;
	private JButton deleteRoiButton = null;
	private FLayer fLayer = null;
	private MapControl mapControl = null;
	private View view = null;
	private LinkedHashMap rois = null;
	private HashMap roiGraphics = null;
	private Grid grid = null;
	private Cursor cursor = null;
	private String previousTool = null;
	private ChartScaterPlotPanel scatterPlotPanel = null;


	/**
	 *  Constructor del panel.
	 *  @param scatterPlotPanel panel con el grafico asociado
	 * */
	public ManagerROIChartPanel(ChartScaterPlotPanel scatterPlotPanel) {
		super();
		setSize(400,200);
		this.scatterPlotPanel= scatterPlotPanel;
		setBorder(new LineBorder(Color.GRAY));
		setLayout(new BorderLayout());
		add(getTable(), BorderLayout.CENTER);
		add(getControlPanel(), BorderLayout.SOUTH);
		initialize();
		fLayer= scatterPlotPanel.getFLayer();
	}

	/**
	 *  Inicialización
	 * */
	private void initialize() {
		listener = new ManagerROIChartPanelListener(this);
		//Se añaden los listeners;
		getNewRoiButtom().addActionListener(listener);
		getDeleteButton().addActionListener(listener);
		getExportButton().addActionListener(listener);
		getTable().getTable().getJTable().getSelectionModel()
				.addListSelectionListener(listener);
		getTable().getTable().getJTable().getModel().addTableModelListener(
				listener);
		loadROIsChart();
	}


	public WindowInfo getWindowInfo() {
		WindowInfo m_viewinfo = new WindowInfo(WindowInfo.PALETTE| WindowInfo.RESIZABLE );
		m_viewinfo.setTitle(PluginServices.getText(this, "regiones_interes_chart"));
		m_viewinfo.setHeight(this.getHeight());
		m_viewinfo.setWidth(this.getWidth());
		return m_viewinfo;
	}


	/**
	 *		Tabla de rois del grafico
	 * */
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
			// controlPanel.add(getToolBar());
		}
		return controlPanel;
	}


	// Boton de borrado
	public JButton getDeleteButton() {
		if (deleteRoiButton == null) {
			deleteRoiButton = new JButton(PluginServices.getText(this, "borrar"));
		}
		return deleteRoiButton;
	}


	// Boton exportar
	public JButton getExportButton() {
		if (exportRoiButtom == null) {
			exportRoiButtom = new JButton(PluginServices.getText(this, "exportar"));

		}
		return exportRoiButtom;
	}

	public JButton getNewRoiButtom() {
		if (newRoiButtom== null) {
			newRoiButtom = new JButton(PluginServices.getText(this, "nuevo"));

		}
		return newRoiButtom;
	}


	public FLayer getFLayer() {
		return fLayer;
	}


	public void setFLayer(FLayer layer) throws GridException {

	}

	/**
	 * Cargar las entradas en la tabla correspondientes a las ROichart ya definidas en el
	 * diagrama.
	 */
	public void loadROIsChart() {

		ArrayList chartRoisArray = scatterPlotPanel.getChart().getROIChartList().getList();

		if (chartRoisArray != null) {

			for (Iterator iter = chartRoisArray.iterator(); iter.hasNext();) {
				ROIChart roi = (ROIChart) iter.next();
				Object row[] = ((IModel) getTable().getModel()).getNewLine();
				row[0] = roi.getName();
				row[4] = roi.getColor();
				((DefaultTableModel) getTable().getModel()).addRow(row);
			}
		}
	}


	/**
	 * Añade la entrada en la tabla correspondiente a la roi
	 * */
	public void addNewRoiEntry(ROIChart roi){
		Object row[] = ((IModel) getTable().getModel()).getNewLine();
		row[0] = roi.getName();
		row[4] = roi.getColor();
		((DefaultTableModel) getTable().getModel()).addRow(row);
		// Establece la nueva como activa en el grafico
		getScatterPlotPanel().getChart().setActiveRoi(roi);
		updateUI();
	}



	public MapControl getMapControl() {
		return mapControl;
	}

	public void selectDrawRoiTool() {

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
		}
		return toolsPanel;
	}

	public JPanel getTableButtonsPanel() {
		if (tableButtonsPanel == null) {
			tableButtonsPanel = new JPanel();
			tableButtonsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
			tableButtonsPanel.add(getNewRoiButtom());
			tableButtonsPanel.add(getDeleteButton());
			BorderLayout fL= new BorderLayout();
			JPanel p= new JPanel();
			p.setLayout(fL);
			p.add(getExportButton(),BorderLayout.EAST);
			tableButtonsPanel.add(p);
		}
		return tableButtonsPanel;
	}




	public Cursor getToolCursor() {
		return cursor;
	}

	public View getView() {
		return view;
	}




	private HashMap getRoiGraphics() {
		if (roiGraphics == null)
			roiGraphics = new HashMap();
		return roiGraphics;
	}

	public void addROI(ROI roi) {
		getRois().put(roi.getName(), roi);
		getRoiGraphics().put(roi.getName(), new ArrayList());
	}

	public ROIChart getROI(String roiName) {
		return (ROIChart) getScatterPlotPanel().getChart().getROIChartList().getListRois().get(roiName);
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
	/*	for (Iterator iter = getRoiGraphics().values().iterator(); iter
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
					.clearSymbolsGraphics();*/
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
		scatterPlotPanel.getChart().getROIChartList().getListRois().put(newName, scatterPlotPanel.getChart().getROIChartList().getListRois().remove(currentName));
	}



	public void changeRoiColor(String roiName, Color color) {
	//	getROI(roiName).setColor(color);
		((ROIChart)scatterPlotPanel.getChart().getROIChartList().getListRois().get(roiName)).setColor(color);
	}


	public void sortTable(){

	}


	public ChartScaterPlotPanel getScatterPlotPanel(){
		return scatterPlotPanel;

	}

	public ScatterPlotDiagram getDiagram(){
		return scatterPlotPanel.getChart();
	}


	public void updateTable(){
		ROIChart roi = getDiagram().getActiveRoiChart();
		Object row[] = ((IModel) getTable().getModel()).getNewLine();
		row[0] = roi.getName();
		row[4] = roi.getColor();
		((DefaultTableModel) getTable().getModel()).addRow(row);
	}

	public Object getWindowProfile() {
		return WindowInfo.PROPERTIES_PROFILE;
	}



}
