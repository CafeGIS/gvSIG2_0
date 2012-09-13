/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
	 *
	 * Copyright (C) 2006 Instituto de Desarrollo Regional and Generalitat Valenciana.
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
	 *   Av. Blasco Ibañez, 50
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

package org.gvsig.remotesensing.mosaic.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.mapcontext.layers.FLayers;
import org.gvsig.fmap.mapcontext.layers.GraphicLayer;
import org.gvsig.fmap.mapcontext.rendering.legend.FGraphic;
import org.gvsig.fmap.mapcontext.rendering.symbols.ILineSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.ISymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.SymbologyFactory;
import org.gvsig.fmap.raster.grid.roi.VectorialROI;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.gui.beans.table.TableContainer;
import org.gvsig.gui.beans.table.exceptions.NotInitializeException;
import org.gvsig.gui.beans.table.models.IModel;
import org.gvsig.raster.util.RasterToolsUtil;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.project.documents.view.gui.View;
import com.iver.utiles.swing.JComboBox;

/**
 * Panel que gestiona la selección de metodos a aplicar en el proceso de
 * generación del Mosaico.
 *
 * @author aMuÑoz (alejandro.munoz@uclm.es)
 * @version 24/4/2008
 *
 * */
public class MethodMosaicPanel extends JPanel  implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JPanel northPanel = null;
	private JComboBox masterImageCombo= null;
	private JCheckBox checkHistogramMatching= null;
	private DefaultComboBoxModel jComboBoxLayersModel=null;
	private JPanel southPanel = null;
	private String nameLayers[] = null;
	private JPanel edgeFeatheringPanel = null;
	private JPanel cutLineFeatheringPanel = null;
	private JCheckBox checkFeathering= null;
	private TableContainer      tableContainer    = null;
	private JRadioButton rButtomEdge = null;
	private JRadioButton rButtomCutLine = null;
	private JPanel panelCombo= null;
	private View view= null;
	private JPanel panelOverlapFunction=null;
	private ArrayList cutLineRois = null;
	private JComboBox comboOverlapFunction= null;
	private JTextField jTextFeathering=null;
	/**
	 * Constructor
	 * param nameLayersAviable array con los nombres de las capas que intervendran
	 * en el proceso de mosaico//setBorder(topBorder);
	 *
	 * */
	public MethodMosaicPanel(String[] nameLayersAviable, View view){
		nameLayers=nameLayersAviable;
		this.view =  view;
		initUI();
		updateCombo();

	}

	/**
	 * Inicialización de la interfaz
	 * */
	private void initUI(){
		BorderLayout bd= new BorderLayout();
		bd.setVgap(2);
		this.setLayout(bd);
		TitledBorder topBorder = BorderFactory.createTitledBorder(PluginServices.getText(this,"methods"));
		this.setBorder(new CompoundBorder(topBorder,new EmptyBorder(3,5,5,5)));

		this.setSize(300,550);
		this.add(getNorthPanel(),BorderLayout.NORTH);
		this.add(getSouthPanel(),BorderLayout.SOUTH);
		this.add(getPanelOverlapFunction(),BorderLayout.CENTER);
	}




	/**
	 * @return panel de histogram_matchig
	 * */
	public JPanel getNorthPanel() {
		if(northPanel == null){
			northPanel= new JPanel();
			northPanel.setSize(new Dimension(200,300));
			TitledBorder topBorder = BorderFactory.createTitledBorder("");//setBorder(topBorder);
			northPanel.setBorder(new CompoundBorder(topBorder,new EmptyBorder(2,2,2,2)));
			northPanel.setLayout(new BorderLayout());
			northPanel.add(getCheckHistogramMatching(),BorderLayout.NORTH);
			//panelCombo.add(getMasterImageCombo(), BorderLayout.EAST);
			JPanel panel = new JPanel ();
			panel.setBorder(new EmptyBorder(10,5,5,5));
			GridBagLayout bd= new GridBagLayout();
			GridBagConstraints constrains= new GridBagConstraints();
			panel.setLayout(bd);
			constrains.gridx = 0;
			constrains.gridy = 0;
			panel.add(new JLabel(PluginServices.getText(this,"image_master")),constrains);
			constrains.insets = new java.awt.Insets(5, 5, 5, 5);
			constrains.gridx = 2;
			constrains.gridy = 0;
			getPanelCombo().setPreferredSize(new Dimension(200,25));
			panel.add(getPanelCombo(),constrains);

			northPanel.add(panel,BorderLayout.CENTER);
		}
		return northPanel;
	}


	/**
	 * @return panel de Feathering
	 * */
	public JPanel getSouthPanel() {
		if(southPanel == null){
			southPanel= new JPanel();
			TitledBorder topBorder = BorderFactory.createTitledBorder("");
			southPanel.setBorder(new CompoundBorder(topBorder,new EmptyBorder(2,2,2,2)));
			southPanel.setLayout(new BorderLayout());
			JPanel panel = new JPanel();
			BorderLayout bd= new BorderLayout();
			bd.setHgap(50);
			panel.setLayout(bd);
			JPanel panel2= new JPanel();
			panel2.setLayout(new BorderLayout());
			panel.add(getCheckFeathering(),BorderLayout.CENTER);
			//panel2.add(new JLabel("distance:"),BorderLayout.CENTER);
			//panel2.add(getJTextFeathering(),BorderLayout.EAST);
			panel.add(panel2,BorderLayout.EAST);
			southPanel.add(panel,BorderLayout.NORTH);
			//southPanel.add(getJTextFeathering(),BorderLayout.EAST);
			JPanel bottomPanel=new JPanel();
			bottomPanel.setBorder(new EmptyBorder(5,5,5,5));
			bottomPanel.setLayout(new BorderLayout());
			ButtonGroup buttonGroup = new ButtonGroup();
			buttonGroup.add(getRButtomCutLine());
			buttonGroup.add(getRButtomEdge());
			bottomPanel.add(getEdgeFeatheringPanel(),BorderLayout.NORTH);
		//	bottomPanel.add(getCutLineFeatheringPanel(),BorderLayout.CENTER);
			southPanel.add(bottomPanel,BorderLayout.CENTER);
		}
		return southPanel;
	}



	 /**
	  * @return modelo de Combo
	  */
	 public DefaultComboBoxModel getComboModel(){
		 if(jComboBoxLayersModel==null){
			 jComboBoxLayersModel=new DefaultComboBoxModel(nameLayers);
		 }
		 return jComboBoxLayersModel;
	 }



	/**
	 * @return JCombo con imagen maestra
	 * */
	public JComboBox getMasterImageCombo() {
		if(masterImageCombo ==null){
			masterImageCombo= new JComboBox();
			masterImageCombo.setEnabled(false);
			masterImageCombo.setModel(getComboModel());
			masterImageCombo.setSelectedIndex(0);
			masterImageCombo.setMaximumSize(new Dimension(50,50));
		}
		return masterImageCombo;
	}


	/**
	 * @return JCheckBox HistogramMatching
	 * */
	public JCheckBox getCheckHistogramMatching() {
		if(checkHistogramMatching==null){
			checkHistogramMatching=new JCheckBox(PluginServices.getText(this,"histogram_matching"),false);
			checkHistogramMatching.addActionListener(this);
		}
		return checkHistogramMatching;
	}

	/**
	 * @return JCheckBox Feathering
	 * */
	public JCheckBox getCheckFeathering() {
		if(checkFeathering==null){
			checkFeathering=new JCheckBox(PluginServices.getText(this,"feathering"),false);
			checkFeathering.addActionListener(this);
		}
		return checkFeathering;
	}

	/**
	 *  Método que actualiza el combo con la imagen maestra de histogram matching
	 * */
	public void updateCombo(){
		 getComboModel().removeAllElements();
		 String []a = nameLayers;
		 for (int i=0;i<a.length;i++)
			 getComboModel().addElement(new String(a[i]));
		 updateUI();
	}


	/**
	 * Acciones al habilitar o  desabilitar los checkBox de métodos
	 * */
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(getCheckHistogramMatching())){
			getMasterImageCombo().setEnabled(getCheckHistogramMatching().isSelected());
		}

		if(e.getSource().equals(getCheckFeathering())){
			//getRButtomCutLine().setEnabled(getCheckFeathering().isSelected());
			getRButtomEdge().setEnabled(getCheckFeathering().isSelected());
			getRButtomEdge().setSelected(true);
			getTableContainer().setEnabled(getCheckFeathering().isSelected());
			getTableContainer().getTable().setEnabled(getCheckFeathering().isSelected() || getRButtomCutLine().isSelected());
			getComboOverlapFunction().setEnabled(!getCheckFeathering().isSelected());
			//getJTextFeathering().setEnabled(getCheckFeathering().isSelected());
			updateUI();
		}

		if(e.getSource().equals(getRButtomCutLine())){
			if(getRButtomCutLine().isSelected()){
				drawRoi();
				updateUI();
			}
		}
		if(e.getSource().equals(getRButtomEdge())){
				view.getMapControl().getMapContext().getGraphicsLayer().clearAllGraphics();
				view.getMapControl().rePaintDirtyLayers();
		}
	}


	/**
	 * @return panel EdgeFeatheringPanel
	 * */
	public JPanel getEdgeFeatheringPanel() {
		if(edgeFeatheringPanel ==null){
			edgeFeatheringPanel= new JPanel();
			edgeFeatheringPanel.setLayout(new BorderLayout());
			edgeFeatheringPanel.add(getRButtomEdge(),BorderLayout.NORTH);
			edgeFeatheringPanel.setEnabled(false);

			// Añadir aqui si el panel tiene que contener algún parámetro
			JPanel panel = new JPanel();
			panel.setPreferredSize(new Dimension(200,10));
			panel.setBorder(new EmptyBorder(5,10,10,10));
			edgeFeatheringPanel.add(panel,BorderLayout.CENTER);
		}
		return edgeFeatheringPanel;
	}


	/**
	 * @return panel CutLineFeatheringPanel
	 * */
	public JPanel getCutLineFeatheringPanel() {
		if(cutLineFeatheringPanel ==null){
			cutLineFeatheringPanel= new JPanel();
			cutLineFeatheringPanel.setLayout(new BorderLayout());
			cutLineFeatheringPanel.add(getRButtomCutLine(),BorderLayout.NORTH);
			JPanel panel = new JPanel();
			panel.setLayout(new BorderLayout());
			panel.setPreferredSize(new Dimension(200,80));
			panel.setBorder(new EmptyBorder(5,2,2,2));
			panel.add(getTableContainer(),BorderLayout.CENTER);
			cutLineFeatheringPanel.setSize(200,80);
			cutLineFeatheringPanel.add(panel, BorderLayout.CENTER);
			cutLineFeatheringPanel.setEnabled(false);
		}
		return cutLineFeatheringPanel;
	}


	/**
	 * Obtiene el contenedor con la tabla para visualizacion de lineas de sutura
	 * @return
	 */
	private TableContainer getTableContainer() {
		if (tableContainer == null) {
			String[] columnNames = {" ", PluginServices.getText(this,"nombre"), ""};
			int[] columnWidths = {22, 334, 0};
			tableContainer = new TableContainer(columnNames, columnWidths);
			tableContainer.setModel("CheckBoxModel");
			tableContainer.initialize();
			tableContainer.setControlVisible(false);
			tableContainer.setMoveRowsButtonsVisible(false);
			tableContainer.getTable().getJTable().getColumnModel().getColumn(0).setMinWidth(22);
			tableContainer.getTable().getJTable().getColumnModel().getColumn(0).setMaxWidth(22);
			tableContainer.getTable().getJTable().getColumnModel().getColumn(2).setMinWidth(0);
			tableContainer.getTable().getJTable().getColumnModel().getColumn(2).setMaxWidth(0);
			tableContainer.setEnabled(false);

			updateRoisTable();
		}
		return tableContainer;
	}



	/**
	 * @return JRadioButton cutLineFeathering
	 * */
	public JRadioButton getRButtomCutLine() {
		if(rButtomCutLine==null){
			rButtomCutLine= new JRadioButton(PluginServices.getText(this,"cut_line_Feathering"));
			rButtomCutLine.setEnabled(false);
			rButtomCutLine.addActionListener(this);
		}
		return rButtomCutLine;
	}



	/**
	 * @return JRadioButton edgeFeathering
	 * */
	public JRadioButton getRButtomEdge() {
		if(rButtomEdge==null){
			rButtomEdge= new JRadioButton(PluginServices.getText(this,"edge_Feathering"));
			rButtomEdge.setEnabled(false);
			rButtomEdge.addActionListener(this);
		}
		return rButtomEdge;
	}


	private JPanel getPanelOverlapFunction(){
		if(panelOverlapFunction==null){
			panelOverlapFunction=new JPanel();
			TitledBorder topBorder = BorderFactory.createTitledBorder(PluginServices.getText(this,"overlap"));
			panelOverlapFunction.setBorder(new CompoundBorder(topBorder,new EmptyBorder(5,2,2,2)));
			panelOverlapFunction.setSize(300,80);
			BorderLayout bd= new BorderLayout();
			bd.setHgap(10);
			panelOverlapFunction.setLayout(bd);
			panelOverlapFunction.add(new JLabel(PluginServices.getText(this,"function")),BorderLayout.WEST);
			panelOverlapFunction.add(getComboOverlapFunction(),BorderLayout.CENTER);

		}
		return panelOverlapFunction;
	}


	public void setNameLayers(String[] newLayersNames){
		nameLayers = newLayersNames;
	}


	public JPanel getPanelCombo() {
		if(panelCombo==null){
			panelCombo= new JPanel();
			panelCombo.setSize(100,100);
			panelCombo.setLayout(new BorderLayout());
			panelCombo.add(getMasterImageCombo(),BorderLayout.CENTER);
		}
		return panelCombo;
	}



	/**
	 * Actualizacion de las rois que pueden ser tomadas como líneas de sutura
	 * */
	public void updateRoisTable(){

//		 Borrado de todas las rois cargadas en la tabla
		try {
			if(getTableContainer().getRowCount()>0)
				getTableContainer().getTable().removeAllRows();
		} catch (NotInitializeException e) {
			RasterToolsUtil.messageBoxError("tabla_no_inicializada", this, e);
		}

		FLyrRasterSE raster=null;
		VectorialROI roi= null;
		Geometry geometry=null;
		boolean load= true;
		int countGeometries= 0;
		cutLineRois = new ArrayList();
		for(int i=0; i<nameLayers.length; i++){
			raster= (FLyrRasterSE)view.getMapControl().getMapContext().getLayers().getLayer(nameLayers[i]);
			ArrayList roisArray = raster.getRois();
			if(roisArray!=null){
				for (Iterator iter = roisArray.iterator(); iter.hasNext();) {
					roi= (VectorialROI)iter.next();;
					for (Iterator iterator = roi.getGeometries()
							.iterator(); iterator.hasNext();) {
						countGeometries=roi.getGeometries().size();
						geometry = (Geometry) iterator.next();
						switch(geometry.getType()){
							case Geometry.TYPES.POINT:
								load= false;
								break;
							case Geometry.TYPES.SURFACE:
								load=false;
								break;
						}
						if(load && countGeometries==1){
							Object row [] = ((IModel)tableContainer.getTable().getTableModel()).getNewLine();
							row[1]= roi.getName()+"-"+raster.getName(); row[0]= new Boolean(true);
							tableContainer.getTable().addRow(row);
							cutLineRois.add(roi);
						}
						load=true;

					}

				}
			}
		}
	//	drawRoi();
		updateUI();
	}


	public void drawRoi(){

		VectorialROI roi = null;
		ISymbol symbol = null;
		FGraphic fGraphic = null;
		Geometry geometry=null;
		view.getMapControl().rePaintDirtyLayers();
		GraphicLayer graphicLayer = view.getMapControl().getMapContext().getGraphicsLayer();

		for (Iterator iter = cutLineRois.iterator(); iter.hasNext();) {
			roi= (VectorialROI)iter.next();;
			for (Iterator iterator = roi.getGeometries()
					.iterator(); iterator.hasNext();) {
				geometry = (Geometry) iterator.next();
				switch (geometry.getType()) {
				case Geometry.TYPES.CURVE:
					symbol = SymbologyFactory.createDefaultLineSymbol();
					((ILineSymbol) symbol).setLineColor(roi.getColor());
					break;
				}
				fGraphic = new FGraphic(geometry, graphicLayer.addSymbol(symbol));
				//graphicLayer.addGraphic(fGraphic);

				view.getMapControl().getMapContext().getGraphicsLayer().addGraphic(fGraphic);
			}
		}
		view.getMapControl().drawGraphics();
	}


	public FLayers getLayers(){
		FLayers fLayers= new FLayers();
		for(int i=0; i<nameLayers.length;i++)
			try {
				fLayers.addLayer(view.getMapControl().getMapContext().getLayers().getLayer(nameLayers[i]).cloneLayer());
			} catch (Exception e) {
				e.printStackTrace();
			}

		return fLayers;

	}

	public JComboBox getComboOverlapFunction() {
		if(comboOverlapFunction==null){
			comboOverlapFunction= new JComboBox();
			comboOverlapFunction.addItem(new String(PluginServices.getText(this,"maximo")));
			comboOverlapFunction.addItem(new String(PluginServices.getText(this,"minimo")));
			comboOverlapFunction.addItem(new String(PluginServices.getText(this,"average")));
			comboOverlapFunction.addItem(new String(PluginServices.getText(this,"overlay")));
			comboOverlapFunction.addItem(new String(PluginServices.getText(this,"back")));
		}
		return comboOverlapFunction;
	}

	public JTextField getJTextFeathering() {
		if(jTextFeathering==null){
			jTextFeathering= new JTextField(10);
		}

		return jTextFeathering;
	}
}
