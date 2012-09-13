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
import java.util.LinkedHashMap;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.rendering.legend.FGraphic;
import org.gvsig.fmap.mapcontrol.MapControl;
import org.gvsig.fmap.mapcontrol.tools.Behavior.Behavior;
import org.gvsig.fmap.mapcontrol.tools.Behavior.MouseMovementBehavior;
import org.gvsig.fmap.mapcontrol.tools.Behavior.PolylineBehavior;
import org.gvsig.fmap.raster.grid.roi.VectorialROI;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.gui.beans.graphic.GraphicChartPanel;
import org.gvsig.gui.beans.table.TableContainer;
import org.gvsig.gui.beans.table.exceptions.NotInitializeException;
import org.gvsig.raster.buffer.BufferFactory;
import org.gvsig.raster.buffer.RasterBuffer;
import org.gvsig.raster.buffer.RasterBufferInvalidException;
import org.gvsig.raster.dataset.IRasterDataSource;
import org.gvsig.raster.grid.Grid;
import org.gvsig.raster.grid.roi.ROI;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.remotesensing.profiles.listener.DrawMouseProfileListener;
import org.gvsig.remotesensing.profiles.listener.LineProfileOptionsListener;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.project.documents.view.toolListeners.StatusBarListener;
import com.iver.utiles.swing.JComboBox;

/**
* Panel para los lineProfiles. Contiene la tabla y los controles asociados para añadir y eliminar
* nuevos profiles.
*
* @author Alejandro Muñoz Sanchez (alejandro.munoz@uclm.es)
* @version 11/12/2007
*
**/
public class LineProfileOptionsPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private FLayer fLayer = null;
	private Grid grid= null;
	TableContainer tableContainer = null;
	private JToggleButton newButton = null;
	private JToggleButton deleteButton = null;
	private JPanel controlPanel = null;
	private LineProfileOptionsListener listener= null;
	private Image curImage = null;
	private MapControl mapControl= null;
	private LinkedHashMap rois = null;
	private HashMap roiGraphics = null;
	private GraphicChartPanel		jPanelChart = null;
	private ProfilePanel mainPanel= null;
	private JComboBox comboBands = null;
	private String previousTool = null;

	/**
	 * Constructor
	 * */
	public LineProfileOptionsPanel(ProfilePanel mainPanel) {
		super();
		this.mapControl = mainPanel.getMapControl();
		this.mainPanel = mainPanel;
		try {
			this.fLayer = mainPanel.getFlayer().cloneLayer();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BorderLayout bd= new BorderLayout();
		listener= new LineProfileOptionsListener(this);
		bd.setHgap(1);
		setLayout(bd);
		initialize();
		add(getTable(), BorderLayout.CENTER);
		add(getControlPanel(), BorderLayout.WEST);
		previousTool = mapControl.getCurrentTool();
	}


	/** Inicialización del panel*/

	private void initialize() {

		getNewButton().addActionListener(listener);
		getDeleteButton().addActionListener(listener);
		getTable().getTable().getJTable().getSelectionModel()
				.addListSelectionListener(listener);
		getTable().getTable().getJTable().getModel().addTableModelListener(
				listener);

		// Asignación de la capa
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
			RasterToolsUtil.messageBoxError("buffer_incorrecto", this, e);
		}
		StatusBarListener sbl = new StatusBarListener(mapControl);
		DrawMouseProfileListener drawMouseViewListener = new DrawMouseProfileListener(
				mainPanel);
		mapControl.addMapTool("drawLineROI", new Behavior[] {
				new PolylineBehavior(drawMouseViewListener),
				new MouseMovementBehavior(sbl) });

	}


	/**
	 * Tabla que muestra la información asociada a cada una de las lineas cuyo perfil se quiere extraer
	 * La información para cada linea es: identificador, color asociado, valor maximo, valor mínimo
	 * y valor medio
	 * */
	public TableContainer getTable() {
		if (tableContainer == null) {
			String[] columnNames = { PluginServices.getText(this, "Linea"),
					PluginServices.getText(this, "color"),
					PluginServices.getText(this, "valor_max"),
					PluginServices.getText(this, "valor_min"),
					PluginServices.getText(this, "valor_medio")
			};
			int[] columnWidths = {30, 30, 25, 25, 25};
			tableContainer = new TableContainer(columnNames, columnWidths);
			tableContainer.setModel("ProfilesTableModel");
			tableContainer.initialize();
			tableContainer.setControlVisible(false);
		}
		return tableContainer;
	}


	/**
	 * @return panel con los controles para agregar o eliminar perfiles y combo
	 * para selección de banda
	 * */
	public JPanel getControlPanel() {
		if (controlPanel == null) {
			controlPanel = new JPanel();
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
			constrains.insets = new java.awt.Insets(1,5 , 1, 1);
			constrains.gridx= 0;
			constrains.gridy= 2;
			controlPanel.add(getComboBands(),constrains);
		}
		return controlPanel;
	}

	/**
	 * @return JToggleButton para el borrado de perfiles
	 * */
	public JToggleButton getDeleteButton() {
		if(deleteButton== null){
			deleteButton = new JToggleButton();
			ImageIcon icono = new ImageIcon(LineProfileOptionsPanel.class.getClassLoader().getResource("images/delete.png"));
			deleteButton.setSize(30,30);
			deleteButton.setIcon(icono);
		}
		return deleteButton;
	}

	/**
	 * @return JToggleButton para añadir nuevo perfil
	 * */
	public JToggleButton getNewButton() {
		if(newButton== null){
			newButton = new JToggleButton();
			ImageIcon icono = new ImageIcon(LineProfileOptionsPanel.class.getClassLoader().getResource("images/Line.png"));
			newButton.setIcon(icono);
			newButton.setSize(30,30);
		}
		return newButton;
	}


	/** Metodo que asigna el cursor asociado al dibujado de la linea y asigna la herramienta
	 * drawLineROI */

	public void selectDrawRoiTool() {
		if (mapControl != null){
			if (getNewButton().isSelected()) {
				curImage = new ImageIcon(LineProfileOptionsPanel.class.getClassLoader().getResource(
						"images/LineCursor.png")).getImage();
				mapControl.setTool("drawLineROI");
			}
		}
	}


	/**
	 * @return combo para la selección de banda
	 * */
	public JComboBox getComboBands() {
		if (comboBands==null){
			comboBands= new JComboBox();
			String[] sNames = new String[getGrid().getBandCount()];
			for(int i=0; i<getGrid().getBandCount();i++)
				sNames[i]= PluginServices.getText(this,"banda")+(i+1);
			ComboBoxModel jComboBoxLayersModel = new  DefaultComboBoxModel(sNames);
			comboBands.setModel(jComboBoxLayersModel);
			comboBands.setEnabled(true);
			comboBands.addActionListener(listener);
		}
		return comboBands;
	}

	/**
	 * @return grid fuente de datos
	 * */
	public Grid getGrid(){
		 return grid;
	}

	/**
	 * @return mapControl
	 * */
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

	/**
	 * @return tabla Hash con las rois actuales del panel
	 * */
	private HashMap getRoiGraphics() {
		if (roiGraphics == null)
			roiGraphics = new HashMap();
		return roiGraphics;
	}


	/**
	 * @return arraylist con las rois que contiene el panel
	 * */
	public ArrayList getROIs() {
		return new ArrayList(getRois().values());
	}

	/**
	 * @param roiName identificador de la roi
	 * @return arraylist con los graphics de la roi cuyo nombre se pasa como parámetro
	 * */
	public ArrayList getRoiGraphics(String roiName) {
		return (ArrayList) getRoiGraphics().get(roiName);
	}

	/**
	 * Método que añade una nueva roi al panel
	 * @param roi Roi a añadir
	 * */
	public void addROI(ROI roi) {
		getRois().put(roi.getName(), roi);
		getRoiGraphics().put(roi.getName(), new ArrayList());
	}


	public void setJPanelChart(GraphicChartPanel panelChart) {
		jPanelChart = panelChart;
	}


	/**
	 * @return panel sobre el que se dibuja la grafica.
	 * Cada vez que se llama este método se limpia el gráfico.
	 * */
	public GraphicChartPanel getJPanelChart() {
		 jPanelChart.cleanChart();
		 XYPlot plot = jPanelChart.getChart().getChart().getXYPlot();
		 NumberAxis domainAxis = new NumberAxis(PluginServices.getText(this,"pixeles"));
	     plot.setDomainAxis(domainAxis);
	     return jPanelChart;
	}


	/**
	 * Método que elimina de la lista de rois del panel la roi cuyo nombre coincide con el que se pasa como parámetro
	 * @param roiName nombre de la ROI a eliminar
	 * */
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


	/**
	 *  Método que asigna el color a cada una de las series de la gráfica
	 *  */
	public void SetColorSeriesChart(){
		XYPlot plot = jPanelChart.getChart().getChart().getXYPlot();
		int index=0;
		try {
			index = getTable().getSelectedRow();
			VectorialROI roi = (VectorialROI)getROIs().get(index);
			plot.getRenderer().setSeriesPaint(0, roi.getColor());
		} catch (NotInitializeException e) {
			RasterToolsUtil.messageBoxError("tabla_no_inicializada", this, e);
		}
	}

	/**
	 * @param roiName nombre de la roi
	 * @return ROI cuyo nombre coincide con el pasado como argumento
	 * */

	public ROI getROI(String roiName) {
		return (ROI) getRois().get(roiName);
	}


	/**
	 * @retun identificador de panel activo  PANELZPROFILE o PANELLINEPROFILE
	 * */
	public int getActive(){
		return mainPanel.getActivePanel();
	}


	/**
	 * @return string con el identificador de la herramienta previa
	 * */
	public String getPreviousTool() {
		return previousTool;
	}

}
