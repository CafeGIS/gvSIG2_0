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

package org.gvsig.remotesensing.profiles.gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.rendering.legend.FGraphic;
import org.gvsig.fmap.mapcontrol.MapControl;
import org.gvsig.fmap.mapcontrol.tools.Behavior.Behavior;
import org.gvsig.fmap.mapcontrol.tools.Behavior.MouseMovementBehavior;
import org.gvsig.fmap.mapcontrol.tools.Behavior.PointBehavior;
import org.gvsig.fmap.raster.grid.roi.VectorialROI;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.gui.beans.graphic.GraphicChartPanel;
import org.gvsig.gui.beans.table.TableContainer;
import org.gvsig.raster.buffer.BufferFactory;
import org.gvsig.raster.buffer.RasterBuffer;
import org.gvsig.raster.buffer.RasterBufferInvalidException;
import org.gvsig.raster.dataset.IRasterDataSource;
import org.gvsig.raster.grid.Grid;
import org.gvsig.raster.grid.roi.ROI;
import org.gvsig.remotesensing.profiles.listener.DrawMouseProfileListener;
import org.gvsig.remotesensing.profiles.listener.ZProfileOptionsListener;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.XYPlot;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.project.documents.view.toolListeners.StatusBarListener;

/**
* Panel de opciones para  z-Profiles.
*
* @author Alejandro Muñoz Sanchez (alejandro.munoz@uclm.es)
* @version 11/12/2007
*
**/

public class ZProfileOptionsPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private FLayer fLayer = null;
	private Grid grid= null;
	TableContainer tableContainer = null;
	private JToggleButton newButton = null;
	private JToggleButton deleteButton = null;
	private JPanel controlPanel = null;
	private ZProfileOptionsListener listener= null;
	private Image curImage = null;
	private MapControl mapControl= null;
	private LinkedHashMap rois = null;
	private HashMap roiGraphics = null;
	private GraphicChartPanel		jPanelChart = null;
	private ProfilePanel mainPanel= null;
	private String previousTool = null;

	public  ZProfileOptionsPanel(ProfilePanel mainPanel) {
		super();
		this.mapControl = mainPanel.getMapControl();
		this.mainPanel= mainPanel;
		this.fLayer = mainPanel.getFlayer();
		BorderLayout bd= new BorderLayout();
		listener= new ZProfileOptionsListener(this);
		bd.setHgap(1);
		setLayout(bd);
		add(getTable(), BorderLayout.CENTER);
		add(getControlPanel(), BorderLayout.WEST);
		previousTool = mapControl.getCurrentTool();
		initialize();
	}


	private void initialize() {

		getNewButton().addActionListener(listener);
		getDeleteButton().addActionListener(listener);
		getTable().getTable().getJTable().getSelectionModel()
				.addListSelectionListener(listener);
		getTable().getTable().getJTable().getModel().addTableModelListener(
				listener);

		// Grid fuente de datos
		FLyrRasterSE rasterLayer = (FLyrRasterSE) fLayer;
		IRasterDataSource dsetCopy = null;
		dsetCopy = rasterLayer.getDataSource().newDataset();
		BufferFactory bufferFactory = new BufferFactory(dsetCopy);
		if (!RasterBuffer.loadInMemory(dsetCopy))
			bufferFactory.setReadOnly(true);
		try {
			bufferFactory.setAllDrawableBands();
			grid= new Grid(bufferFactory);
		} catch (RasterBufferInvalidException e) {
			e.printStackTrace();
		}
		StatusBarListener sbl = new StatusBarListener(mapControl);
		DrawMouseProfileListener drawMouseViewListener = new DrawMouseProfileListener(
				mainPanel);
		mapControl.addMapTool("drawPointROI", new Behavior[] {
				new PointBehavior(drawMouseViewListener),
				new MouseMovementBehavior(sbl) });

	}


	public TableContainer getTable() {
		if (tableContainer == null) {
			String[] columnNames = { PluginServices.getText(this, "punto"),
					PluginServices.getText(this, "color"),
					PluginServices.getText(this, "coordx"),
					PluginServices.getText(this, "coordy"),
			};
			int[] columnWidths = { 20, 25, 25, 25, 0 };
			tableContainer = new TableContainer(columnNames, columnWidths);
			tableContainer.setModel("ProfilesTableModel");
			tableContainer.initialize();
			tableContainer.setControlVisible(false);
		}
		return tableContainer;
	}


	public JPanel getControlPanel() {
		if (controlPanel == null) {
			controlPanel = new JPanel();
			//controlPanel.setPreferredSize(new Dimension(80,20));
			GridBagLayout gb = new GridBagLayout();
			controlPanel.setLayout(gb);
			GridBagConstraints constrains = new GridBagConstraints();
			constrains.insets = new java.awt.Insets(1,5 , 1, 1);
			constrains.gridx= 0;
			constrains.gridy= 0;
			controlPanel.add(getNewButton(),constrains);
			constrains.insets = new java.awt.Insets(1,5 , 1, 1);
			constrains.gridx= 0;
			constrains.gridy= 1;
			controlPanel.add(getDeleteButton(),constrains);

		}
		return controlPanel;
	}


	public JToggleButton getDeleteButton() {
		if(deleteButton== null){
			deleteButton = new JToggleButton();
			ImageIcon icono = new ImageIcon(ZProfileOptionsPanel.class.getClassLoader().getResource("images/delete.png"));
			deleteButton.setSize(30,30);
			deleteButton.setIcon(icono);
		}
		return deleteButton;
	}


	public JToggleButton getNewButton() {
		if(newButton== null){
			newButton = new JToggleButton();
			ImageIcon icono = new ImageIcon(ZProfileOptionsPanel.class.getClassLoader().getResource("images/Point.png"));
			newButton.setIcon(icono);
			newButton.setSize(30,30);
		}
		return newButton;
	}



	public void selectDrawRoiTool() {
		if (mapControl != null){
			if (getNewButton().isSelected()) {
				curImage = new ImageIcon(ZProfileOptionsPanel.class.getClassLoader().getResource(
						"images/PointCursor.png")).getImage();
				mapControl.setTool("drawPointROI");
			}
		}
	}

	public Grid getGrid(){
		 return grid;
	}


	public MapControl getMapControl() {
		return mapControl;
	}

	public Image getToolImage() {
		return curImage;
	}

	public LinkedHashMap getRois() {
		if (rois == null)
			rois = new LinkedHashMap();
		return rois;
	}

	private HashMap getRoiGraphics() {
		if (roiGraphics == null)
			roiGraphics = new HashMap();
		return roiGraphics;
	}

	public ArrayList getROIs() {
		return new ArrayList(getRois().values());
	}

	public ArrayList getRoiGraphics(String roiName) {
		return (ArrayList) getRoiGraphics().get(roiName);
	}

	public void addROI(ROI roi) {
		getRois().put(roi.getName(), roi);
		getRoiGraphics().put(roi.getName(), new ArrayList());
	}


	public void setJPanelChart(GraphicChartPanel panelChart) {


		jPanelChart = panelChart;
	}


	public GraphicChartPanel getJPanelChart() {
		jPanelChart.cleanChart();
		XYPlot plot = jPanelChart.getChart().getChart().getXYPlot();
		NumberAxis domainAxis = new NumberAxis(PluginServices.getText(this,"bandas"));
		domainAxis.setRange(1,getGrid().getBandCount() );
		domainAxis.setTickUnit(new NumberTickUnit(1.0));
		plot.setDomainAxis(domainAxis);
		return jPanelChart;
	}


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



	public void UpdateChart(){
		XYPlot plot = jPanelChart.getChart().getChart().getXYPlot();
		int index=-1;
		for (Iterator iter = getROIs().iterator(); iter.hasNext();){
			VectorialROI roi = (VectorialROI)iter.next();
			index=index+1;
			plot.getRenderer().setSeriesPaint(index, roi.getColor());
		}
	}

	public ROI getROI(String roiName) {
		return (ROI) getRois().get(roiName);
	}

	public int getNextActive(){
		return mainPanel.getActivePanel();
	}


	public String getPreviousTool() {
		return previousTool;
	}


	public ZProfileOptionsListener getListener() {
		return listener;
	}
}
